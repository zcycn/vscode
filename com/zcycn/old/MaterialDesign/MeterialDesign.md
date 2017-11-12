### MaterialDesign动画 

1. 触摸反馈 Touch Feedback  

> 5.0自带效果，Button默认就有水波纹  

  <item name="colorButtonNormal">@color/colorPrimaryDark</item> 这个属性可以设置默认Button颜色  
  
  
> ?attr/selectableItemBackground  默认没有背景，点击时有背景和水波纹  
> android:background="?attr/selectableItemBackgroundBorderless" 没有边界的水波纹  
  
  <item name="colorControlHighlight">@color/colorAccent</item> 设置水波纹颜色  

> 属性时android:开头的是系统的，否则是兼容包下的  

  
2. 揭露 Reveal Effect  

        // 设置一个控件的揭露效果
        // 圆形水波纹效果  中心点 开始半径 结束半径
        Animator animator = ViewAnimationUtils.createCircularReveal(v, v.getWidth()/2, v.getHeight()/2, 0, v.getHeight()/2);
        // 勾股定理 Math.hypot()
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();

3. Activity转场动画 Activity transition  

   ActivityOptions只支持API21以上  
   ActivityOptionsCompat兼容类，在低版本没有效果  
   共享元素转换  

   - 设置允许转场，有两种方式，代码或style  

          getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);  

          <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
          <!-- Customize your theme here. -->
          <item name="colorPrimary">@color/colorPrimary</item>
          <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
          <item name="colorAccent">@color/colorAccent</item>
          <item name="colorControlHighlight">@color/colorAccent</item>
          <item name="colorButtonNormal">@color/colorPrimaryDark</item>
          <item name="android:windowContentTransitions">true</item>
          </style>

   - 设置共享元素和共享元素名称   

      共享元素不需要相同的View类型，共享元素的名称要相同  

        例如ActivityA中  

          <TextView
            android:id="@+id/textView"
            android:layout_width="300dp"
            android:layout_height="300dp"
            android:transitionName="@string/app_name"
            android:background="@color/colorAccent"
            android:text="TextView"/>

        那么ActivityB中可以  

          <Button
            android:id="@+id/textView"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:transitionName="@string/app_name"
            android:background="@color/colorAccent"
            android:text="TextView"/>

    - ActivityOptionsCompat构建平移动画  

          // Activity A中的代码  
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

          // 按返回键的时候自动实现了返回的共享元素转场动画，源码可以看出：
          public void onBackPressed() {
            finishAfterTransition();
          }
          public void finishAfterTransition() {
            if (!mActivityTransitionState.startExitBackTransition(this)) {
                finish();
            }
          }

    - 普通转换动画  
    如果有共享元素的转换则以共享元素的动画执行，其他的View按下面设置的动画  
        
        // 滑动
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);// 出去
        getWindow().setEnterTransition(slide);// 进入

        // 展开
        Explode explode = new Explode();
        explode.setDuration(1000);

        // 渐变
        Fade fade = new Fade();
        fade.setDuration(1000);

        ActivityOptionsCompat options3 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent, options3.toBundle());

        // 第二个Activity

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();// 这个才会有动画
                //onBackPressed();// 或者这个
            }
        });

        // RevealBackgroundView效果后面补充

4. Curved motion 曲线运动  

    插值器Interpolator


5. 视图状态改变 View State change  


