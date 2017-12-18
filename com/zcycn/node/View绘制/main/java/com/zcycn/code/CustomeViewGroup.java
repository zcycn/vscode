package com.zcycn.code;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>Class: com.zcycn.code.CustomeViewGroup</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2017/12/18/20:57
 */

public class CustomeViewGroup extends ViewGroup {

    private int offset = 100;

    public CustomeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left,right,top = 0,bottom;
        int childCount = getChildCount();
        for (int i=0; i<childCount; i++) {
            View child = getChildAt(i);
            left = i*offset;
            right = left + child.getMeasuredWidth();
            bottom = top + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);
            top += child.getMeasuredHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取父View传递的测量规格
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        // 获取子View测量规格，测量每个子View
        int childCount = getChildCount();
        for (int i=0; i<childCount; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        }

        // 根据子View确定自身宽高
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                for (int i=0; i<childCount; i++) {
                    View child = getChildAt(i);
                    int widthAndOffset = i * offset + child.getMeasuredWidth();
                    width = Math.max(width, widthAndOffset);
                }
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                for (int i=0; i<childCount; i++) {
                    View child = getChildAt(i);
                    height = height + child.getMeasuredHeight();
                }
                break;
        }

        // 设置自身宽高
        setMeasuredDimension(width, height);
    }
}
