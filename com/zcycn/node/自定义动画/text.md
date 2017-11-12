### 知识点

- 监听ScrollView滑动  
- 系统控件添加自定义属性  

### 监听ScrollView滑动 

- 覆写onScrollChanged方法  

    @Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt);
    t即滑出去的高度，以ScrollView为参照物，上滑为正，下滑为负  
    通过获取子View距ScrollView顶部高度child.getTop()，与t即可计算出百分比  
    实现接口方法实现百分比回调  

### 系统控件添加自定义属性 

- 在系统原控件包裹FrameLayout控件，通过接口回调，实现控件效果更改百分比  

- 自定义LinearLayout方法，在addView前判断系统控件是否包含自定义属性，包含则包裹上面自定义控件，并设置自定义控件属性给FrameLayout  

