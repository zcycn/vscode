package com.zcycn.code;

/**
 * <p>Class: com.zcycn.code.Subscrible</p>
 * <p>Description: </p>
 * <pre>
 * 观察者
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/22/11:30
 */

public abstract class Subscrible<T> {

    public abstract void onNext(T t);

}
