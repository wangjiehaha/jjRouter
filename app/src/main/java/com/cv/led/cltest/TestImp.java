package com.cv.led.cltest;

import android.util.Log;

import com.cv.led.annotation.RouterService;

@RouterService(interfaces = ITest.class, key = "TestImp")
public class TestImp implements ITest {
    @Override
    public void test() {
        Log.e("TestImp", "調用了TestImp");
    }
}
