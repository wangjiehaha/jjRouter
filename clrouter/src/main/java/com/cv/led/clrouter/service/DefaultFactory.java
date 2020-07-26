package com.cv.led.clrouter.service;

/**
 * 默认的Factory，先尝试Provider，再尝试无参数构造
 */

public class DefaultFactory implements IFactory {

    public static final DefaultFactory INSTANCE = new DefaultFactory();

    private DefaultFactory() {

    }

    @Override
    public <T> T create(Class<T> clazz) throws Exception {
        T t = ProviderPool.create(clazz);
        if (t != null) {
            return t;
        } else {
            return clazz.newInstance();
        }
    }
}
