### 知识点

1. ScrollView空间Scroll使用  
2. onScrollChanged方法重写  
3. 非Scroll控件自己new Scroll的使用

### ScrollView实现侧滑菜单  

控件中可以直接使用scrollTo或smoothScrollTo方法，后者为平滑的滑动  

### onScrollChanged方法

l参数表示屏幕距控件左边的距离

### 自定义控件使用Scroll 

Scroll与computeScroll是成对出现的

        mScroller.startScroll(getScrollX(), getScrollY(), offset, 0);
        invalidate();

        //在开启滑动的情况下（mScroller.startScroll），滑动的过程当中此方法会被不断调用
        @Override
        public void computeScroll() {
            if (mScroller.computeScrollOffset()) {
                this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                postInvalidate();
            }
        }