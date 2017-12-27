package com.zcycn.code;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * <p>Class: com.zcycn.code.BehaviorAspect</p>
 * <p>Description: </p>
 * <pre>
 * 切面
 * </pre>
 *
 * @author zhuchengyi
 * @date 2017/12/26/20:34
 */
@Aspect
public class BehaviorAspect {

    // 切点 * *(..)任何方法任何参数
    @Pointcut("execution(@com.zcycn.code.BehaviorTrace * *(..))")
    public void annoBehavior(){

    }

    // 切面
    @Around("annoBehavior()")
    public Object dealPoint(ProceedingJoinPoint point) throws Throwable{
        // 方法执行前
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        // 获取自定义注解
        BehaviorTrace behaviorTrace = methodSignature.getMethod().getAnnotation(BehaviorTrace.class);
        String contentType = behaviorTrace.value();
        System.out.println("================>>>1 " + contentType);
        // 方法执行时
        Object object = point.proceed();
        // 方法执行后
        System.out.println("================>>>2 " + contentType + object);
        return object;
    }

}
