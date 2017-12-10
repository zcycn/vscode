package com.zcycn.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

/**
 * <p>Class: com.zcycn.slidingmenu.SlidingMenu</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2017/12/10/14:05
 */
/*1.在Activity中获取View的宽高，得到的值为0
        通过上面的measure分析可以知道，View的measure过程和Activity的生命周期方法不是同步的，所以无法保证Activity的某个生命周期执行后View就一定能获取到值，当我们在View还没有完成measure过程就去获取它的宽高，当然获取不到了，解决这问题的方法有很多，这里推荐使用以下方法
        （1）在View的post方法中获取：
        这个方法简单快捷，推荐使用
        ?
        1
        2
        3
        4
        5
        6
        7
        mView.post(new Runnable() {
@Override
public void run() {
        width = mView.getMeasuredWidth();
        height = mView.getMeasuredHeight();
        }
        });
        post方法中传入的Runnable对象将会在View的measure、layout过程后触发，因为UI的事件队列是按顺序执行的，所以任何post到队列中的请求都会在Layout发生变化后执行。
        （2）使用View的观察者ViewTreeObserver
        ViewTreeObserver是视图树的观察者，其中OnGlobalLayoutListener监听的是一个视图树中布局发生改变或某个视图的可视状态发生改变时，就会触发此类监听事件，其中onGlobalLayout回调方法会在View完成layout过程后调用，此时是获取View宽高的好时机
        ?
        1
        2
        3
        4
        5
        6
        7
        8
        9
        ViewTreeObserver observer = mView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
@Override
public void onGlobalLayout() {
        mView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        width = mScanIv.getMeasuredWidth();
        height = mScanIv.getMeasuredHeight();
        }
        });
        使用这个方法需要注意，随着View树的状态改变，onGlobalLayout方法会被回调多次，所以在进入onGlobalLayout回调方法时，就移除这个观察者，保证onGlobalLayout方法只被执行一次就好了
        （3）在onWindowFocusChanged回调中获取
        此方法是在View已经初始化完成，measure和layout过程已经执行完成，UI视图已经渲染完成时被回调，此时View的宽高肯定也已经被确定了，这个时候就可以去获取View的宽高了
        ?
        1
        2
        3
        4
        5
        6
        7
        8
@Override
public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
        width = mView.getMeasuredWidth();
        height = mView.getMeasuredHeight();
        }
        }
        这个方法在Activity界面发生变化时也会被多次回调，如果只需要获取一次宽高的话，建议加上标记加以限制*/
public class SlidingMenu extends HorizontalScrollView {

    private final int mScreenWidth;
    private View mMenu;
    private View mMain;
    private boolean isFirst;
    private int mMenuWidth;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            // view 加载完成
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!isFirst) {
            ViewGroup viewGroup = (ViewGroup) getChildAt(0);
            mMenu = viewGroup.getChildAt(0);
            mMain = viewGroup.getChildAt(1);
            mMenuWidth = mScreenWidth - 300;
            mMenu.getLayoutParams().width = mMenuWidth;
            mMain.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            scrollTo(mMenuWidth, 0);
            isFirst = true;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // l 控件左边距屏幕左边的距离
        super.onScrollChanged(l, t, oldl, oldt);
        float curr = (float)l / mMenuWidth; // 1 - 0
        // 平移动画
        mMenu.setTranslationX(mMenuWidth * curr * 0.6f); // 视察效果
        // 缩放动画
        mMenu.setScaleX(1 - 0.6f * curr);
        mMenu.setScaleY(1 - 0.6f * curr);
        mMain.setScaleY(0.8f + 0.2f * curr);
        mMain.setScaleY(0.8f + 0.2f * curr);
        mMenu.setAlpha(1 - curr);
    }

    float downX;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                float dx = ev.getX() - downX;
                if(dx<mScreenWidth/3){
                    this.smoothScrollTo(mMenuWidth, 0);
                }else{
                    this.smoothScrollTo(0, 0);
                }
                return true;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }


}
