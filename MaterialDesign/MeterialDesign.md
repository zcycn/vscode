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

4. Curved motion 曲线运动  

5. 视图状态改变 View State change  


