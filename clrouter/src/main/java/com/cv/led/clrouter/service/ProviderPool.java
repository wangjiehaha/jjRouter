package com.cv.led.clrouter.service;

import com.cv.led.annotation.RouterStructure;
import com.cv.led.clrouter.RouterUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Provider缓存
 */
public class ProviderPool {

    private static final HashMap<Class, Method> CACHE = new HashMap<>();

    private static final Method NOT_FOUND = ProviderPool.class.getDeclaredMethods()[0];

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        Method provider = getProvider(clazz);
        if (provider == NOT_FOUND) {
            return null;
        } else {
            try {
                return (T) provider.invoke(null);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private static <T> Method getProvider(Class<T> clazz) {
        Method provider = CACHE.get(clazz);
        if (provider == null) {
            synchronized (CACHE) {
                provider = CACHE.get(clazz);
                if (provider == null) {
                    provider = findProvider(clazz);
                    CACHE.put(clazz, provider);
                }
            }
        }
        return provider;
    }

    private static Method findProvider(Class clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(RouterStructure.class) != null) {
                if (Modifier.isStatic(method.getModifiers()) &&
                        method.getReturnType() == clazz &&
                        RouterUtils.isEmpty(method.getParameterTypes())) {
                    return method;
                } else {
                     return NOT_FOUND;
                }
            }
        }
        return NOT_FOUND;
    }
}
