package com.jerry.binder_pool;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;


import java.util.concurrent.CountDownLatch;

/**
 * Binder连接池服务的连接工具类
 * <p>
 * Created by xujierui on 2018/3/28.
 */

public class BinderPoolConnectUtils {
    private static final String TAG = BinderPoolConnectUtils.class.getSimpleName();

    public static final int BINDER_CODE_NONE = 0;
    public static final int BINDER_CODE_SECURITY = 1;
    public static final int BINDER_CODE_COMPUTE = 2;

    @SuppressLint("StaticFieldLeak")
    private static BinderPoolConnectUtils _Instance = null;

    public static BinderPoolConnectUtils getInstance(@NonNull Context context) {
        // 这一块代码很有意思，仔细研究一下（和之后通过CountDownLatch异步转同步有关联）
        if (_Instance == null) {
            // 加锁
            synchronized (BinderPoolConnectUtils.class) {
                if (_Instance == null) {
                    _Instance = new BinderPoolConnectUtils(context);
                }
            }
        }
        return _Instance;
    }

    private Context applicationContext;

    private BinderPoolConnectUtils(Context context) {
        applicationContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    private CountDownLatch latch = null;

    private synchronized void connectBinderPoolService() {
        Log.i(TAG, "Start connect to BinderPoolService...");
        // 使用这个CountDownLatch是为了将bindService这个异步过程转为同步过程（为什么?为了使getInstance中的同步锁生效）
        // CountDownLatch任务数 = 1
        latch = new CountDownLatch(1);

        Intent serviceIntent = new Intent("com.jerry.binder_pool.service");
        serviceIntent.setPackage("com.jerry.binder_pool");
        applicationContext.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        try {
            // 会暂停线程，直到CountDownLatch任务数 = 0
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Connect to BinderPoolService success!");
    }

    private IBinderPool binderPool = null;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binderPool = IBinderPool.Stub.asInterface(iBinder);

            try {
                // 添加死亡代理
                binderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // 将CountDownLatch任务数 - 1
            latch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // ignored
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied: ");
            // 断开重连机制
            binderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            binderPool = null;
            connectBinderPoolService();
        }
    };

    public IBinder queryBinder(int binderCode) {
        if (binderPool == null) {
            return null;
        }

        try {
            return binderPool.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }
}
