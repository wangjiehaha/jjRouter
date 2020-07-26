package com.cv.led.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RouterService {

    /**
     * 实现接口
     */
    Class[] interfaces();

    /**
     * 实现类唯一识别key，多个实现类时，通过该key寻找指定的实现
     */
    String[] key() default {};

    /**
     * 是否为单例
     */
    boolean singleton() default false;

    /**
     * 是否设置为默认实现类
     */
    boolean defaultImpl() default false;
}
