package com.cv.led.clrouter.service;

import android.content.Context;

/**
 * 使用Context构造
 */

public class ContextFactory implements IFactory {

    private final Context mContext;

    public ContextFactory(Context context) {
        mContext = context;
    }

    @Override
    public <T> T create(Class<T> clazz) throws Exception {
        return clazz.getConstructor(Context.class).newInstance(mContext);
    }
}
