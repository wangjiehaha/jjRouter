package com.cv.led.clrouter.service;

/**
 * 无参数构造
 */
public class EmptyArgsFactory implements IFactory {

    public static final EmptyArgsFactory INSTANCE = new EmptyArgsFactory();

    private EmptyArgsFactory() {

    }

    @Override
    public <T> T create(Class<T> clazz) throws Exception {
        return clazz.newInstance();
    }
}
