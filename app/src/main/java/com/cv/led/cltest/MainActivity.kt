package com.cv.led.cltest

import android.app.Activity
import android.os.Bundle
import com.cv.led.clrouter.Router

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Router.lazyInit()

        val test = Router.getService(ITest::class.java, "TestImp")
        val test2 = Router.getService(ITest::class.java, "TestImp2")
        test.test()
        test2.test()
    }
}
