### View初始化布局

1. Activity中setContentView后会通过getWindow()获取PhoneWindow  

2. PhoneWindow中会初始化DecorView  

        @Override
        public final View getDecorView() {
            if (mDecor == null || mForceDecorInstall) {
                installDecor();
            }
            return mDecor;
        }

3. installDecor()方法中会调用generateLayout()方法生成布局，方法中会调用requestFeature，所以在代码中调用该方法前需要在setContentView方法前调用  

### Snackbar添加到指定布局  

1. 查找到指定布局

        private static ViewGroup findSuitableParent(View view) {
            ViewGroup fallback = null;
            do {
                if (view instanceof CoordinatorLayout) {
                    // We've found a CoordinatorLayout, use it
                    return (ViewGroup) view;
                } else if (view instanceof FrameLayout) {
                    if (view.getId() == android.R.id.content) {
                        // If we've hit the decor content view, then we didn't find a CoL in the
                        // hierarchy, so use it.
                        return (ViewGroup) view;
                    } else {
                        // It's not the content view but we'll use it as our fallback
                        fallback = (ViewGroup) view;
                    }
                }

                if (view != null) {
                    // Else, we will loop and crawl up the view hierarchy and try to find a parent
                    final ViewParent parent = view.getParent();
                    view = parent instanceof View ? (View) parent : null;
                }
            } while (view != null);

            // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
            return fallback;
        }

### View开始绘制

1. View的测量/布局/绘制的入口  

        @CallSuper
        public void requestLayout() {
            if (mMeasureCache != null) mMeasureCache.clear();

            if (mAttachInfo != null && mAttachInfo.mViewRequestingLayout == null) {
                // Only trigger request-during-layout logic if this is the view requesting it,
                // not the views in its parent hierarchy
                ViewRootImpl viewRoot = getViewRootImpl();
                if (viewRoot != null && viewRoot.isInLayout()) {
                    if (!viewRoot.requestLayoutDuringLayout(this)) {
                        return;
                    }
                }
                mAttachInfo.mViewRequestingLayout = this;
            }

            mPrivateFlags |= PFLAG_FORCE_LAYOUT;
            mPrivateFlags |= PFLAG_INVALIDATED;

            if (mParent != null && !mParent.isLayoutRequested()) {
                mParent.requestLayout();
            }
            if (mAttachInfo != null && mAttachInfo.mViewRequestingLayout == this) {
                mAttachInfo.mViewRequestingLayout = null;
            }
        }

- 会不断的调用父View的requestLayout，直到DecorView，DecorView的的mAttachInfo不为null  

        /**
        * @param info the {@link android.view.View.AttachInfo} to associated with
        *        this view
        */
        void dispatchAttachedToWindow(AttachInfo info, int visibility) {
            mAttachInfo = info;
            if (mOverlay != null) {
                mOverlay.getOverlayView().dispatchAttachedToWindow(info, visibility);
            }
            mWindowAttachCount++;
            ...
        }      

- ViewRootImpl中调用requestLayout方法，然后进入一系列方法执行performMeasure performLayout performDraw     

### measure  

1. MeasureSpec测量规格 

    EXACTLY[ɪgˈzæktli] 精准    
    AT_MOST 最大值  
    UNSPECIFIED[ˌʌnˈspesɪfaɪd] 未指定    

2. 测量结束需要调用setMeasuredDimension()方法    

3. ViewGroup的测量会先循环测量子View的大小，即measureChildWithMargins方法，该方法中调用child.measure方法，所有子控件测量完，通过serMeasureedDimension()设置自己的大小  

4. 规格与大小分离MeasureSpec.getMode getSize 方法  

5. 规格与大小合成的方法MeasureSpec.makeMeasureSpec  

6. 测量子View的方法：measureChild 包含了padding，measureChildWithMargins 包含了padding和margin  

### 测量的套路 

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
