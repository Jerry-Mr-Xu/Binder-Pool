package com.jerry.binder_pool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnEncrypt;
    private Button btnDecrypt;
    private Button btnAdd;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    private String encryptedData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initListener() {
        btnEncrypt.setOnClickListener(this);
        btnDecrypt.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    private void initView() {
        btnEncrypt = (Button) findViewById(R.id.btn_encrypt);
        btnDecrypt = (Button) findViewById(R.id.btn_decrypt);
        btnAdd = (Button) findViewById(R.id.btn_add);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encrypt: {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Encrypt \"Binder连接池加密测试\"");
                        IBinder binder = BinderPoolConnectUtils.getInstance(MainActivity.this).queryBinder(BinderPoolConnectUtils.BINDER_CODE_SECURITY);
                        ISecurity security = ISecurity.Stub.asInterface(binder);
                        try {
                            // 加密
                            encryptedData = security.encrypt("Binder连接池加密测试");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "After encrypt \"" + encryptedData + "\"");
                    }
                });
                break;
            }
            case R.id.btn_decrypt: {
                if (encryptedData == null || encryptedData.length() <= 0) {
                    Log.e(TAG, "Decrypt error No Data");
                    return;
                }
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Decrypt \"" + encryptedData + "\"");
                        IBinder binder = BinderPoolConnectUtils.getInstance(MainActivity.this).queryBinder(BinderPoolConnectUtils.BINDER_CODE_SECURITY);
                        ISecurity security = ISecurity.Stub.asInterface(binder);
                        String originData = null;
                        try {
                            // 解密
                            originData = security.decrypt(encryptedData);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "After decrypt \"" + originData + "\"");
                    }
                });
                break;
            }
            case R.id.btn_add: {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        IBinder binder = BinderPoolConnectUtils.getInstance(MainActivity.this).queryBinder(BinderPoolConnectUtils.BINDER_CODE_COMPUTE);
                        ICompute compute = ICompute.Stub.asInterface(binder);
                        int result = 0;
                        try {
                            // 1 + 2
                            compute.add(1, 2);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Add: 1 + 2 = " + result);
                    }
                });
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        threadPool.shutdown();
        super.onDestroy();
    }
}
