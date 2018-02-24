package com.zcycn.code;

/**
 * <p>Class: com.zcycn.code.Func1</p>
 * <p>Description: </p>
 * <pre>
 * 抽象转换
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/23/12:09
 */

public interface Func1<T, R> {

    R call(T t);

}
