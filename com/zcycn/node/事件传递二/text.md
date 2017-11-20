### 知识点

- ViewGroup的事件分发  

### ViewGroup的dispatchTouchEvent 

        // Check for interception.
        final boolean intercepted;
        if (actionMasked == MotionEvent.ACTION_DOWN
                || mFirstTouchTarget != null) {
            final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
            if (!disallowIntercept) {
                intercepted = onInterceptTouchEvent(ev);
                ev.setAction(action); // restore action in case it was changed
            } else {
                intercepted = false;
            }
        } else {
            // There are no touch targets and this action is not an initial down
            // so this view group continues to intercept touches.
            intercepted = true;
        }

1. ViewGroup重写了View的dispatchTouchEvent方法  
2. 在DOWN事件或首个触摸对象不为null时会判断是否拦截      
3. disallowIntercept当子View设置了不需要拦截为true时，ViewGroup则不会拦截  
4. onInterceptTouchEvent方法返回是否拦截自己处理事件     

        for (int i = childrenCount - 1; i >= 0; i--) {
            final int childIndex = getAndVerifyPreorderedIndex(
                    childrenCount, i, customOrder);
            final View child = getAndVerifyPreorderedView(
                    preorderedList, children, childIndex);

            // If there is a view that has accessibility focus we want it
            // to get the event first and if not handled we will perform a
            // normal dispatch. We may do a double iteration but this is
            // safer given the timeframe.
            if (childWithAccessibilityFocus != null) {
                if (childWithAccessibilityFocus != child) {
                    continue;
                }
                childWithAccessibilityFocus = null;
                i = childrenCount - 1;
            }

            if (!canViewReceivePointerEvents(child)
                    || !isTransformedTouchPointInView(x, y, child, null)) {
                ev.setTargetAccessibilityFocus(false);
                continue;
            }

            newTouchTarget = getTouchTarget(child);
            if (newTouchTarget != null) {
                // Child is already receiving touch within its bounds.
                // Give it the new pointer in addition to the ones it is handling.
                newTouchTarget.pointerIdBits |= idBitsToAssign;
                break;
            }

            resetCancelNextUpFlag(child);
            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                // Child wants to receive touch within its bounds.
                mLastTouchDownTime = ev.getDownTime();
                if (preorderedList != null) {
                    // childIndex points into presorted list, find original index
                    for (int j = 0; j < childrenCount; j++) {
                        if (children[childIndex] == mChildren[j]) {
                            mLastTouchDownIndex = j;
                            break;
                        }
                    }
                } else {
                    mLastTouchDownIndex = childIndex;
                }
                mLastTouchDownX = ev.getX();
                mLastTouchDownY = ev.getY();
                newTouchTarget = addTouchTarget(child, idBitsToAssign);
                alreadyDispatchedToNewTouchTarget = true;
                break;
            }

            // The accessibility focus didn't handle the event, so clear
            // the flag and do a normal dispatch to all children.
            ev.setTargetAccessibilityFocus(false);
        }

1. 如果不拦截，则会执行上面的for循环找到可以处理事件的子View后退出循环   
2. 通过dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)分发给子View

        private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
                View child, int desiredPointerIdBits) {
            final boolean handled;

            // Canceling motions is a special case.  We don't need to perform any transformations
            // or filtering.  The important part is the action, not the contents.
            final int oldAction = event.getAction();
            if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
                event.setAction(MotionEvent.ACTION_CANCEL);
                if (child == null) {
                    handled = super.dispatchTouchEvent(event);
                } else {
                    handled = child.dispatchTouchEvent(event);
                }
                event.setAction(oldAction);
                return handled;
            }

1. 如果子View为null，则调用自己的dispatchTouchEvent，也就是View的dispatchTouchEvent  

        ...
        // Find a child that can receive the event.
        // Scan children from front to back.
        final ArrayList<View> preorderedList = buildTouchDispatchChildList();
        final boolean customOrder = preorderedList == null
                && isChildrenDrawingOrderEnabled();
        final View[] children = mChildren;
        for (int i = childrenCount - 1; i >= 0; i--) {
            ...

1. 当手指按下时所处坐标下有多层View时，会先响应最上层View  

        newTouchTarget = addTouchTarget(child, idBitsToAssign);
        alreadyDispatchedToNewTouchTarget = true;          

1. 当找到一个触摸对象时会添加到一个单向链表中，这时候mFirstTouchTarget就不会为null  

        // Dispatch to touch targets.
        if (mFirstTouchTarget == null) {
            // No touch targets so treat this as an ordinary view.
            handled = dispatchTransformedTouchEvent(ev, canceled, null,
                    TouchTarget.ALL_POINTER_IDS);
        } else {
            // Dispatch to touch targets, excluding the new touch target if we already
            // dispatched to it.  Cancel touch targets if necessary.
            TouchTarget predecessor = null;
            TouchTarget target = mFirstTouchTarget;
            while (target != null) {
                final TouchTarget next = target.next;
                if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                    handled = true;
                } else {
                    final boolean cancelChild = resetCancelNextUpFlag(target.child)
                            || intercepted;
                    if (dispatchTransformedTouchEvent(ev, cancelChild,
                            target.child, target.pointerIdBits)) {
                        handled = true;
                    }
                    if (cancelChild) {
                        if (predecessor == null) {
                            mFirstTouchTarget = next;
                        } else {
                            predecessor.next = next;
                        }
                        target.recycle();
                        target = next;
                        continue;
                    }
                }
                predecessor = target;
                target = next;
            }
        }

1. mFirstTouchTarget这个为null时会给View赋值为null，这样事件会调用super.dispatchTouchEvent分发给自己  
2. dispatchTransformedTouchEvent(ev, cancelChild, target.child, target.pointerIdBits)分发给子View  
3. mFirstTouchTarget的意义在于一旦一个View消费了DOWN事件，那么该系列的后续事件都由该View处理
    所以在alreadyDispatchedToNewTouchTarget = true;时消耗了Down事件，因此不会执行  

        if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                            handled = true;
        } else {
            // 这个逻辑不会执行
        }

    当ACTION_MOVE事件过来时，alreadyDispatchedToNewTouchTarget 表示是否是新添加TouchTarget的，这个在上一个事件DOWN的时候是被置为true，但是在这次事件中由于没有添加新的TouchTarget，所以为false。这是就会执行else逻辑进行事件分发  

    这里的target.child就是mFirstTouchTarget中持有的View，在这里就是ViewGroup B。所以通过dispatchTransformedTouchEvent我们知道这里将当前事件ACTION_MOVE传给了B的dispatchTouchEvent方法。

    总的来说就是通过mFirstTouchTarget保存DOWN事件的消费View B，然后在后续的事件直接传给了B的dispatchTouchEvent处理。

参考：http://blog.csdn.net/sinat_23092639/article/details/74858558    