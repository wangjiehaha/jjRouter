package com.cv.led.cltest;

import android.util.Log;

import com.cv.led.annotation.RouterService;

@RouterService(interfaces = ITest.class, key = "TestImp2")
public class TestImp2 implements ITest {
    @Override
    public void test() {
        Log.e("TestImp2", "调用了TestImp2");
    }
}
