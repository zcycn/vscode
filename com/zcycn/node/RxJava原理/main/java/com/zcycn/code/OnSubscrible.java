package com.zcycn.code;

/**
 * <p>Class: com.zcycn.code.OnSubscribe</p>
 * <p>Description: </p>
 * <pre>
 * 被观察者 T代表不同的角色
 * 获取观察者，传递动作
 *
 * <T> 被观察者发出的内容
 * <Subscrible<? super T>> 观察者
 *
 * super 泛型用在参数中
 * extends 用在返回值中
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/22/11:25
 */
// 作用 扩充泛型
public interface OnSubscrible<T> extends Action1<Subscrible<? super T>>{
}
