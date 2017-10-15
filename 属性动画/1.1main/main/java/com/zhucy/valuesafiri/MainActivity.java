package com.zhucy.valuesafiri;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static com.zhucy.valuesafiri.R.id.second;

public class MainActivity extends AppCompatActivity {

    private View first_view;
    private View second_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first_view = findViewById(R.id.first);
        second_view = findViewById(second);
    }

    public void startFirstAnimation(View v){
        // 绕x轴
        ObjectAnimator firstRotationAnim = ObjectAnimator.ofFloat(first_view, "rotationX", 0f, 25f);
        firstRotationAnim.setDuration(300);

        // 透明度
        ObjectAnimator firstAlphaAnim = ObjectAnimator.ofFloat(first_view, "alpha", 1f, 0.5f);
        firstAlphaAnim.setDuration(300);

        // 缩放
        ObjectAnimator firstScaleXAnim = ObjectAnimator.ofFloat(first_view, "scaleX", 1f, 0.8f);
        firstScaleXAnim.setDuration(300);
        ObjectAnimator firstScaleYAnim = ObjectAnimator.ofFloat(first_view, "scaleY", 1f, 0.8f);
        firstScaleYAnim.setDuration(300);

        // 翻转后反向翻转
        //firstRotationAnim.addUpdateListener();
        ObjectAnimator firstResumeRotationAnim = ObjectAnimator.ofFloat(first_view, "rotationX", 25f, 0f);
        firstResumeRotationAnim.setDuration(300);
        firstResumeRotationAnim.setStartDelay(300);// 延时执行

        // 缩放后还要平移上去, 由于缩放了0.2f那么这里就是0.1f
        ObjectAnimator firstTranslationAnim = ObjectAnimator.ofFloat(first_view, "translationY", 0f, -0.1f*first_view.getHeight());
        firstTranslationAnim.setDuration(300);

        // 第二个View执行平移动画
        ObjectAnimator secondTranslationAnim = ObjectAnimator.ofFloat(second_view, "translationY", second_view.getHeight(), 0);
        secondTranslationAnim.setDuration(300);
        secondTranslationAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                // 开始时设置可见
                second_view.setVisibility(View.VISIBLE);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(firstRotationAnim, firstAlphaAnim, firstScaleXAnim, firstScaleYAnim, firstResumeRotationAnim, firstTranslationAnim, secondTranslationAnim);
        set.start();
    }

    public void startSecondAnimation(View v){
        // 绕x轴
        ObjectAnimator firstRotationAnim = ObjectAnimator.ofFloat(first_view, "rotationX", 0f, 25f);
        firstRotationAnim.setDuration(300);

        // 透明度
        ObjectAnimator firstAlphaAnim = ObjectAnimator.ofFloat(first_view, "alpha", 0.5f, 1f);
        firstAlphaAnim.setDuration(300);

        // 缩放
        ObjectAnimator firstScaleXAnim = ObjectAnimator.ofFloat(first_view, "scaleX", 0.8f, 1f);
        firstScaleXAnim.setDuration(300);
        ObjectAnimator firstScaleYAnim = ObjectAnimator.ofFloat(first_view, "scaleY", 0.8f, 1f);
        firstScaleYAnim.setDuration(300);

        // 翻转后反向翻转
        //firstRotationAnim.addUpdateListener();
        ObjectAnimator firstResumeRotationAnim = ObjectAnimator.ofFloat(first_view, "rotationX", 25f, 0f);
        firstResumeRotationAnim.setDuration(300);
        firstResumeRotationAnim.setStartDelay(300);// 延时执行

        // 缩放后还要平移上去, 由于缩放了0.2f那么这里就是0.1f
        ObjectAnimator firstTranslationAnim = ObjectAnimator.ofFloat(first_view, "translationY", -0.1f*first_view.getHeight(), 0f);
        firstTranslationAnim.setDuration(300);

        // 第二个View执行平移动画
        ObjectAnimator secondTranslationAnim = ObjectAnimator.ofFloat(second_view, "translationY", 0, second_view.getHeight());
        secondTranslationAnim.setDuration(300);
        secondTranslationAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 开始时设置不可见
                second_view.setVisibility(View.GONE);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(firstRotationAnim, firstAlphaAnim, firstScaleXAnim, firstScaleYAnim, firstResumeRotationAnim, firstTranslationAnim, secondTranslationAnim);
        set.start();
    }

}
