package com.jerry.binder_pool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jerry.binder_pool.aidl_impl.BinderPoolImpl;

/**
 * Binder连接池服务
 * <p>
 * Created by xujierui on 2018/3/28.
 */

public class BinderPoolService extends Service {
    private static final String TAG = BinderPoolService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return new BinderPoolImpl();
    }
}
