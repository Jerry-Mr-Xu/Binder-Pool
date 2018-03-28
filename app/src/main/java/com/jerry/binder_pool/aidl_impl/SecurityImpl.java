package com.jerry.binder_pool.aidl_impl;

import android.os.RemoteException;

import com.jerry.binder_pool.ISecurity;

/**
 * AIDL-Security 接口的实现类
 * <p>
 * Created by xujierui on 2018/3/28.
 */

public class SecurityImpl extends ISecurity.Stub {
    private static final String TAG = SecurityImpl.class.getSimpleName();

    // 秘钥
    private static final char SECRET_KEY = '^';

    @Override
    public String encrypt(String originData) throws RemoteException {
        char[] originChars = originData.toCharArray();
        // 加密
        for (int i = 0, size = originChars.length; i < size; i++) {
            originChars[i] ^= SECRET_KEY;
        }

        return new String(originChars);
    }

    @Override
    public String decrypt(String encryptData) throws RemoteException {
        // 解密
        return encrypt(encryptData);
    }
}
