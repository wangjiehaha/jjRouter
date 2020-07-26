package com.cv.led.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RouterService {

    Class[] interfaces();

    String[] key() default {};

    boolean singleton() default false;

    boolean defaultImpl() default false;
}
