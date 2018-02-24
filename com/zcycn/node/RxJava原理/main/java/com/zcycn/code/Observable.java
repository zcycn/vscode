package com.zcycn.code;

import android.os.Handler;
import android.os.Looper;

/**
 * <p>Class: com.zcycn.code.Observable</p>
 * <p>Description: </p>
 * <pre>
 * 中间操作
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/22/12:16
 */

public class Observable<T> {

    private OnSubscrible<T> mTOnSubscrible;

    private Observable(OnSubscrible<T> TOnSubscrible) {
        mTOnSubscrible = TOnSubscrible;
    }

    public static <T> Observable<T> create(OnSubscrible<T> TOnSubscrible){
        return new Observable<T>(TOnSubscrible);
    }

    public void subscrible(Subscrible<? super T> subscrible){
        mTOnSubscrible.call(subscrible);
    }

    public <R> Observable<R> map(Func1<? super T, ? extends R> func1){
        return lift(new OperatorMap<>(func1));
    }

    private <R> Observable<R> lift(OperatorMap<T, R> func1) {
        return new Observable<R>(new OnSubscribleLeft<>(mTOnSubscrible, func1));
    }

    public Observable<T> subscribleOnIO(){
        return create(new OnSubscribleOnIO<T>(this));
    }

    public Observable<T> subscribleMain(){
        return create(new OnSubscribleMain<T>(new Handler(Looper.getMainLooper()), this));
    }
}
