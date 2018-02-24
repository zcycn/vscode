package com.zcycn.code;

/**
 * <p>Class: com.zcycn.code.Operator</p>
 * <p>Description: </p>
 * <pre>
 * 转换的抽象
 *
 * T 传递的内容  Subscrible<? super T> 观察者
 * R 获取的内容  Subscrible<? super R> 观察者
 *
 * 为了满足 比如 限时是水果  以及装满水果的盘中, 以此引入super 和 extends
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/23/12:14
 */

public interface Operator<T, R> extends Func1<Subscrible<? super T>, Subscrible<? super R>> {
}
