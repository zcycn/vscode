### 知识点

- View事件分发

### View的dispatchTouchEvent

        ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnTouchListener != null
                && (mViewFlags & ENABLED_MASK) == ENABLED
                && li.mOnTouchListener.onTouch(this, event)) {
            result = true;
        }

        if (!result && onTouchEvent(event)) {
            result = true;
        }

1. 从源码中可以看出ListenerInfo保存了所有的View监听  
2. 对于几个核心方法，事件分发时首先调用onTouch，只有onTouch不需要消耗该事件时才会执行onTouchEvent  
3. 事件是否需要继续分发由result返回值决定  
4. 对于事件的一些方法有些是View自身处理的比如onTouchEvent，有些是回调方法比如onTouch  

### View的onTouchEvent 

        if ((viewFlags & ENABLED_MASK) == DISABLED) {
            if (action == MotionEvent.ACTION_UP && (mPrivateFlags & PFLAG_PRESSED) != 0) {
                setPressed(false);
            }
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return (((viewFlags & CLICKABLE) == CLICKABLE
                    || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)
                    || (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE);
        }

1. 进入onTouchEvent方法首先会判断控件如果非enabled时由点击事件决定是否消耗此事件  

        if (((viewFlags & CLICKABLE) == CLICKABLE ||
                (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE) ||
                (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE) {
            ...

1. 如果控件是enabled和clickabled状态时会执行此分支             
2. MotionEvent的各Action在此执行  

        case MotionEvent.ACTION_DOWN:
            mHasPerformedLongPress = false;

            if (performButtonActionOnTouchDown(event)) {
                break;
            }

            // Walk up the hierarchy to determine if we're inside a scrolling container.
            boolean isInScrollingContainer = isInScrollingContainer();

            // For views inside a scrolling container, delay the pressed feedback for
            // a short period in case this is a scroll.
            if (isInScrollingContainer) {
                mPrivateFlags |= PFLAG_PREPRESSED;
                if (mPendingCheckForTap == null) {
                    mPendingCheckForTap = new CheckForTap();
                }
                mPendingCheckForTap.x = event.getX();
                mPendingCheckForTap.y = event.getY();
                postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
            } else {
                // Not inside a scrolling container, so show the feedback right away
                setPressed(true, x, y);
                checkForLongClick(0, x, y);
            }
            break;            

1. MotionEvent.ACTION_DOWN事件中保存一些状态值，如果该控件是滑动控件，那么需要延时按的效果  

            case MotionEvent.ACTION_UP:
                boolean prepressed = (mPrivateFlags & PFLAG_PREPRESSED) != 0;
                if ((mPrivateFlags & PFLAG_PRESSED) != 0 || prepressed) {
                    ...
                    if (!mHasPerformedLongPress && !mIgnoreNextUpEvent) {
                        // This is a tap, so remove the longpress check
                        removeLongPressCallback();

                        // Only perform take click actions if we were in the pressed state
                        if (!focusTaken) {
                            // Use a Runnable and post this rather than calling
                            // performClick directly. This lets other visual state
                            // of the view update before click actions start.
                            if (mPerformClick == null) {
                                mPerformClick = new PerformClick();
                            }
                            if (!post(mPerformClick)) {
                                performClick();
                            }
                        }
                    }            

1. MotionEvent.ACTION_UP事件中调用onClick方法，result设为true  

### ScrollView与ListView事件冲突  

继承ScrollView重写dispatchTouchEvent方法

        requestDisallowInterceptTouchEvent(true);// 不拦截
        return super.dispatchTouchEvent(ev);           

### ListView在ScrollView展开  

               