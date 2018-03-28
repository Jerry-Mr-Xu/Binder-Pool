package com.jerry.binder_pool.aidl_impl;

import android.os.RemoteException;

import com.jerry.binder_pool.ICompute;

/**
 * AIDL-Compute 接口实现类
 * <p>
 * Created by xujierui on 2018/3/28.
 */

public class ComputeImpl extends ICompute.Stub {
    private static final String TAG = ComputeImpl.class.getSimpleName();

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
