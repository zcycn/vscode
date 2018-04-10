package com.zcycn.fmod;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Button;
import android.content.pm.PackageManager;

// 头文件有实现后缀名为.hpp
// DSP 音效
/*
如何指定运行库
        这里要说一下共享库和静态库的区别。共享库直观上的讲就是多个软件用一个库文件，因此内存里只会存在一份代码。而静态库就是各用各的。他们之间各有好处。这里如果使用share结尾的运行库，那么运行库就不会打包到你的so里，程序运行的时候会装载Android系统上自带的库。当然如果这台Android设备/system/libs下没有这个库，那么程序就会崩溃。但个人认为最好还是使用共享库，因为这会减小生成so的大小。

        Android.mk中使用
        给APP_STL标签指定对应的值（上表中的Name字段）即可。如果没有指定，即使用默认的libstdc++，这里需要注意一点，无论静态还是共享都只能指定一个运行库。

        1 //use gnustl_static
        2 APP_STL := gnustl_static
        3 //use gnustl_shared
        4 APP_STL := gnustl_shared

        如果要使用特征中的C++ Exceptions则还需要显示指定CPPFLAG:

        1 APP_CPPFLAGS += -fexceptions

        如果要使用特征中的RTTI则还需要显示指定CPPFLAG:

        1 APP_CPPFLAGS += -frtti

        Gradle配置
        在Gradle中stl标签等价于APP_STL,cppFlags标签等价于APP_CPPFLAGS:

        stl "stlport_share"
        cppFlags "-frtti"
        //实验版gradle
        cppFlags.add("-frtti")
*/
public class MainActivity extends Activity implements OnTouchListener, Runnable {
    private TextView mTxtScreen;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the text area
        mTxtScreen = new TextView(this);
        mTxtScreen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f);
        mTxtScreen.setTypeface(Typeface.MONOSPACE);

        // Create the buttons
        Button[] buttons = new Button[9];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(this);
            buttons[i].setText(getButtonLabel(i));
            buttons[i].setOnTouchListener(this);
            buttons[i].setId(i);
        }

        // Create the button row layouts
        LinearLayout llTopRowButtons = new LinearLayout(this);
        llTopRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout llMiddleRowButtons = new LinearLayout(this);
        llMiddleRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout llBottomRowButtons = new LinearLayout(this);
        llBottomRowButtons.setOrientation(LinearLayout.HORIZONTAL);

        // Create the main view layout
        LinearLayout llView = new LinearLayout(this);
        llView.setOrientation(LinearLayout.VERTICAL);

        // Create layout parameters
        LayoutParams lpLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);

        // Set up the view hierarchy
        llTopRowButtons.addView(buttons[0], lpLayout);
        llTopRowButtons.addView(buttons[6], lpLayout);
        llTopRowButtons.addView(buttons[1], lpLayout);
        llMiddleRowButtons.addView(buttons[4], lpLayout);
        llMiddleRowButtons.addView(buttons[8], lpLayout);
        llMiddleRowButtons.addView(buttons[5], lpLayout);
        llBottomRowButtons.addView(buttons[2], lpLayout);
        llBottomRowButtons.addView(buttons[7], lpLayout);
        llBottomRowButtons.addView(buttons[3], lpLayout);
        llView.addView(mTxtScreen, lpLayout);
        llView.addView(llTopRowButtons);
        llView.addView(llMiddleRowButtons);
        llView.addView(llBottomRowButtons);

        setContentView(llView);

        // Request necessary permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }

        org.fmod.FMOD.init(this);

        mThread = new Thread(this, "Example Main");
        mThread.start();

        setStateCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStateStart();
    }

    @Override
    protected void onStop() {
        setStateStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        setStateDestroy();

        try {
            mThread.join();
        } catch (InterruptedException e) {
        }

        org.fmod.FMOD.close();

        super.onDestroy();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            buttonDown(view.getId());
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            buttonUp(view.getId());
        }

        return true;
    }

    @Override
    public void run() {
        main();
    }

    public void updateScreen(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtScreen.setText(text);
            }
        });
    }

    private native String getButtonLabel(int index);

    private native void buttonDown(int index);

    private native void buttonUp(int index);

    private native void setStateCreate();

    private native void setStateStart();

    private native void setStateStop();

    private native void setStateDestroy();

    private native void main();

    static {
        /*
         * To simplify our examples we try to load all possible FMOD
         * libraries, the Android.mk will copy in the correct ones
         * for each example. For real products you would just load
         * 'fmod' and if you use the FMOD Studio tool you would also
         * load 'fmodstudio'.
         */

        // Try logging libraries...
        try {
            System.loadLibrary("fmodL");
        } catch (UnsatisfiedLinkError e) {
        }
        // Try release libraries...
        try {
            System.loadLibrary("fmod");
        } catch (UnsatisfiedLinkError e) {
        }

        // System.loadLibrary("stlport_shared");
        System.loadLibrary("native-lib");
    }
}
