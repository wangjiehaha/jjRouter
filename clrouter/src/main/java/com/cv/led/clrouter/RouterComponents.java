package com.cv.led.clrouter;

import android.content.Intent;

import com.cv.led.clrouter.service.DefaultFactory;
import com.cv.led.clrouter.service.IFactory;

/**
 * 用于配置组件
 *
 */
public class RouterComponents {

    private static IFactory sDefaultFactory = DefaultFactory.INSTANCE;

    public static void setDefaultFactory(IFactory factory) {
        sDefaultFactory = factory == null ? DefaultFactory.INSTANCE : factory;
    }

    public static IFactory getDefaultFactory() {
        return sDefaultFactory;
    }
}
