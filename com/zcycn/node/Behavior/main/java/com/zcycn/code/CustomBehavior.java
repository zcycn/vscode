package com.zcycn.code;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * <p>Class: com.zcycn.code.CustomBehavior</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/13/15:07
 */

public class CustomBehavior extends CoordinatorLayout.Behavior<View> {

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 监听哪些控件 depends 作用于
     * @param parent 父容器
     * @param child 子控件 观察者们
     * @param dependency 被观察的
     * @return 是否监听
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // 可以根据Tag、Id来判断
        return  dependency instanceof TextView || super.layoutDependsOn(parent, child, dependency);
    }

    /**
     * 被观察者发生改变时调用，这里可以做联动动画
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        // 垂直方向 平移
        // 偏移量
        int offset = dependency.getTop() - child.getTop();
        ViewCompat.offsetTopAndBottom(child, offset);
        // 旋转 child.animate().rotation(child.getTop()*20);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
























