### 知识点

1. 子控件测量与布局  
2. scrollTo与scrollBy  
3. 子控件的边界与参考点  

### 控件测量与布局

1. 简单的测量方法 

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int size = getChildCount();
            for (int i = 0; i < size; ++i) {
                final View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }

2. 布局 

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if(changed){
                int size = getChildCount();
                for (int i = 0; i < size; ++i) {
                    final View child = getChildAt(i);
                    child.layout(i*child.getMeasuredWidth(), 0, (i+1)*child.getMeasuredWidth(), child.getMeasuredHeight());
                    child.setClickable(true);
                }
            }
            //左边界
            leftBound = getChildAt(0).getLeft();
            //右边界
            rightBound = getChildAt(getChildCount()-1).getRight();
            // rightBound 为最后一个子View的右边距父空间最左边的距离，而scrollTo时是以子控件的左上角为基准
        }

3. ScrollTo与ScrollBy

        如何有滑动的效果？
        View.scrollTo(x,y);
            让View相对于它初始的位置滚动一段距离。
        View.scrollBy(x,y);
            让View相对于它现在的位置滚动一段距离。        