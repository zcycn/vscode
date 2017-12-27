package com.zcycn.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Class: com.zcycn.code.BehaviorTrace</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2017/12/26/20:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BehaviorTrace {
    String value();
    int type();
}
