package com.cv.led.clrouter.service;

/**
 * 从Class构造实例
 */
public interface IFactory {

    <T> T create(Class<T> clazz) throws Exception;
}
