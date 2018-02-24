package com.zcycn.code;

/**
 * <p>Class: com.zcycn.code.Action1</p>
 * <p>Description: </p>
 * <pre>
 * 抽象一个动作，T传递的内容
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/22/11:23
 */

public interface Action1<T> {

    void call(T t);

}
