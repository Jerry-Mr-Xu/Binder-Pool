// ISecurity.aidl
package com.jerry.binder_pool;

// Declare any non-default types here with import statements

interface ISecurity {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String encrypt(in String originData);
    String decrypt(in String encryptData);
}
