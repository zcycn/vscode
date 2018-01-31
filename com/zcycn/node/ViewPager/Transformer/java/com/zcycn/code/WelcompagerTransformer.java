package com.zcycn.code;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.ricky.android.percent.support.MyScrollView;

/**
 * <p>Class: com.zcycn.code.WelcompagerTransformer</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/1/28/16:04
 */

public class WelcompagerTransformer implements ViewPager.PageTransformer, ViewPager.OnPageChangeListener {

    private int mPageIndex;
    private boolean mPageChanged = true;
    private static final float ROT_MOD = -15f;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPageIndex = position;
        mPageChanged = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void transformPage(View page, float position) {
        ViewGroup v = page.findViewById(R.id.rl);
        final MyScrollView mscv = v.findViewById(R.id.mscv);
        View bg1 = v.findViewById(R.id.imageView0);
        View bg2 = v.findViewById(R.id.imageView0_2);
        View bg_container = v.findViewById(R.id.bg_container);

        int bg1_green = page.getContext().getResources().getColor(R.color.bg1_green);
        int bg2_blue = page.getContext().getResources().getColor(R.color.bg2_blue);

        Integer tag = (Integer) page.getTag();
        View parent = (View) page.getParent();
        //颜色估值器
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int color = bg1_green;
        if(tag == mPageIndex){
            switch (mPageIndex){
                case 0:
                    color = (int) evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
                case 1:
                    color = (int) evaluator.evaluate(Math.abs(position), bg2_blue, bg1_green);
                    break;
                case 2:
                    color = (int) evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
            }
        }
        //设置整个viewpager的背景颜色
        parent.setBackgroundColor(color);

        if(position == 0){
            if(mPageChanged){
                bg1.setVisibility(View.VISIBLE);
                bg2.setVisibility(View.VISIBLE);

                ObjectAnimator animator_bg1 = ObjectAnimator.ofFloat(bg1, "translationX", 0, -bg1.getWidth());
                animator_bg1.setDuration(400);
                animator_bg1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mscv.smoothScrollTo((int) (mscv.getWidth()*animation.getAnimatedFraction()), 0);
                    }
                });
                animator_bg1.start();

                ObjectAnimator animator_bg2 = ObjectAnimator.ofFloat(bg2, "translationX", bg2.getWidth(), 0);
                animator_bg2.setDuration(400);
                animator_bg2.start();
                mPageChanged = false;
            }
        }else if(position == -1 || position == 1){
            // 已隐藏的做复原操作
            bg2.setTranslationX(0);
            bg1.setTranslationX(0);
            mscv.smoothScrollTo(0, 0);
        }else if(position < 1 && position > -1){
            float width = bg1.getWidth();
            float height = bg1.getHeight();
            float rotation = ROT_MOD * position * -1.25f;

            bg_container.setPivotX(width * 0.5f);
            bg_container.setPivotY(height);
            bg_container.setRotation(rotation);
        }
    }
}
