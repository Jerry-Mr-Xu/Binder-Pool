package com.jerry.binder_pool;

import android.app.Application;

/**
 * 做一些初始化工作
 * <p>
 * Created by xujierui on 2018/3/28.
 */

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化与BinderPool的连接
        BinderPoolConnectUtils.getInstance(MyApplication.this);
    }
}
