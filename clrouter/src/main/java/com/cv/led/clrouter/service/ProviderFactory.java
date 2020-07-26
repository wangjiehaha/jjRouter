package com.cv.led.clrouter.service;

public class ProviderFactory implements IFactory {

    public static final IFactory INSTANCE = new ProviderFactory();

    private ProviderFactory() {

    }

    @Override
    public <T> T create(Class<T> clazz) throws Exception {
        return ProviderPool.create(clazz);
    }
}
