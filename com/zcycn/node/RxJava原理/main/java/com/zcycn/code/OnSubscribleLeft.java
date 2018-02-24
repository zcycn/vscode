package com.zcycn.code;

/**
 * <p>Class: com.zcycn.code.OnSubscribleLeft</p>
 * <p>Description: </p>
 * <pre>
 * 中间被观察者
 * T 传递的内容
 * R 获得的内容
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/23/12:11
 */

public class OnSubscribleLeft<T, R> implements OnSubscrible<R>{

    OnSubscrible<T> parent;// 被观察者

    Operator<? extends R, ? super T> operator;

    public OnSubscribleLeft(OnSubscrible<T> parent, Operator<? extends R, ? super T> operator) {
        this.parent = parent;
        this.operator = operator;
    }

    @Override
    public void call(Subscrible<? super R> subscrible) {
        Subscrible<? super T> s1 = operator.call(subscrible);
        parent.call(s1);
    }
}
