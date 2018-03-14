package com.zcycn.code.present;

import com.zcycn.code.view.BaseView;

import java.lang.ref.WeakReference;

/**
 * <p>Class: com.zcycn.code.present.BasePersenter</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/3/14/9:40
 */

public abstract class BasePersenter<T extends BaseView> {
    // 持有UI接口的弱引用
    protected WeakReference<T> mViewRef;

    public abstract void fectch();

    public void attachView(T view){
        mViewRef = new WeakReference<T>(view);
    }

    public void detach(){
        if(mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
