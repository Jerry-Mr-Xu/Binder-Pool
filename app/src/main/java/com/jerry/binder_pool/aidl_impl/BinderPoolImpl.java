package com.jerry.binder_pool.aidl_impl;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jerry.binder_pool.BinderPoolConnectUtils;
import com.jerry.binder_pool.IBinderPool;

/**
 * AIDL-BinderPool 接口实现类
 * <p>
 * Created by xujierui on 2018/3/28.
 */

public class BinderPoolImpl extends IBinderPool.Stub {
    private static final String TAG = BinderPoolImpl.class.getSimpleName();

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        Log.i(TAG, "queryBinder: BINDER_CODE = " + binderCode);
        switch (binderCode) {
            case BinderPoolConnectUtils.BINDER_CODE_SECURITY:
                return new SecurityImpl();
            case BinderPoolConnectUtils.BINDER_CODE_COMPUTE:
                return new ComputeImpl();
            default:
                return null;
        }
    }
}
