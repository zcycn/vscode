### 知识点

- 系统是如何加载子控件，拿到子控件参数的  
- ViewPager滑动监听
- Viewpager需要设置Id，否则会资源找不到的错误 

### XML布局加载

- PhoneWindow创建时会实例化LayoutInflater，通过inflate方法加载布局  
- 通过自定义LayoutInflater加载自定义属性  

### ViewPager滑动监听  

- 滑动动画分析  
比如手势向左，屏幕右滑，那么屏幕中间右侧则为in，屏幕中间左侧为out，因此onPageScrolled(int position, float positionOffset,int positionOffsetPixels)该方法实现时   
get(position)的fragment为in  
get(position-1)的fragment为out  

- 滑动动画  
可使用滑动偏移像素值positionOffsetPixels与加速度实现视差特效  
in动画  
(屏幕宽-滑动偏移量)*加速度  
ViewHelper.setTranslationX(view, (containerWidth - positionOffsetPixels) * tag.xIn)  
out动画   
ViewHelper.setTranslationX(view, 0 - positionOffsetPixels * tag.xOut);

- 滑动状态切换  

    @Override
	public void onPageScrollStateChanged(int state) {
		AnimationDrawable animation = (AnimationDrawable) iv_man.getBackground();
		switch (state) {
		case ViewPager.SCROLL_STATE_DRAGGING:
			animation.start();
			break;
			
		case ViewPager.SCROLL_STATE_IDLE:
			animation.stop();
			break;
			
		default:
			break;
		}
	}

- 滑动后显示页面  

	@Override
	public void onPageSelected(int position) {
		if (position == adapter.getCount() - 1) {
			iv_man.setVisibility(INVISIBLE);
		}else{
			iv_man.setVisibility(VISIBLE);
		}
	}    


