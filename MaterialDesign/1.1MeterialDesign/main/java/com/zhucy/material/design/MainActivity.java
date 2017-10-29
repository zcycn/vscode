package com.zhucy.material.design;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;

public class MainActivity extends AppCompatActivity {

    private View view;
    private View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置允许转场动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.textView);
        button = findViewById(R.id.button);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void revealAnimation(View v){
        // 圆形水波纹效果  中心点 开始半径 结束半径
        Animator animator = ViewAnimationUtils.createCircularReveal(v, v.getWidth()/2, v.getHeight()/2, 0, v.getHeight()/2);
        // 勾股定理 Math.hypot()
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this // 当前Activity
                , view // 共享元素
                , "MaterialDesign");// 共享元素名称
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent, options.toBundle());

        // 多个元素共享的情况
        ActivityOptionsCompat options2 = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this // 当前Activity
                , Pair.create(view, "MaterialDesign")
                , Pair.create(button, "button"));// 共享元素名称

    }

}
