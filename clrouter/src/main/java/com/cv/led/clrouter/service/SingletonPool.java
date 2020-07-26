package com.cv.led.clrouter.service;

import com.cv.led.clrouter.RouterComponents;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例缓存
 *
 * Created by jzj on 2018/3/29.
 */
public class SingletonPool {

    private static final Map<Class, Object> CACHE = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <I, T extends I> T get(Class<I> clazz, IFactory factory) throws Exception {
        if (clazz == null) {
            return null;
        }
        if (factory == null) {
            factory = RouterComponents.getDefaultFactory();
        }
        Object instance = getInstance(clazz, factory);
        return (T) instance;
    }

    private static Object getInstance(Class clazz, IFactory factory) throws Exception {
        Object t = CACHE.get(clazz);
        if (t != null) {
            return t;
        } else {
            synchronized (CACHE) {
                t = CACHE.get(clazz);
                if (t == null) {
                    t = factory.create(clazz);
                    if (t != null) {
                        CACHE.put(clazz, t);
                    }
                }
            }
            return t;
        }
    }
}
