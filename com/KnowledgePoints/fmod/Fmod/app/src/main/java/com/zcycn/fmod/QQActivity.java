package com.zcycn.fmod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.fmod.FMOD;

/**
 * <p>Class: com.zcycn.fmod.QQActivity</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/4/9/8:49
 */
public class QQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FMOD.init(this);
        setContentView(R.layout.activity_main);
    }

    public void mFix(final View btn){
        new Thread(){
            @Override
            public void run() {
                String path = "file:///android_asset/a.mp3";
                switch (btn.getId()){
                    case R.id.btn_record:
                        EffectUtils.fix(path, EffectUtils.MODE_NORMAL);
                        break;
                    case R.id.btn_luoli:
                        EffectUtils.fix(path, EffectUtils.MODE_LUOLI);
                        break;
                    case R.id.btn_dashu:
                        EffectUtils.fix(path, EffectUtils.MODE_DASHU);
                        break;

                    case R.id.btn_jingsong:
                        EffectUtils.fix(path, EffectUtils.MODE_JINGSONG);
                        break;

                    case R.id.btn_gaoguai:
                        EffectUtils.fix(path, EffectUtils.MODE_GAOGUAI);
                        break;

                    case R.id.btn_kongling:
                        EffectUtils.fix(path, EffectUtils.MODE_KONGLING);
                        break;
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        FMOD.close();
        super.onDestroy();
    }
}
