package com.oplus.network;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.network.OlkServiceConnector;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class OplusOlkManager {
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "OplusOlkManager";
    private static OplusOlkManager sInstance;
    public Context mContext;
    private IOlk mOlkService;
    private ConcurrentHashMap<String, AuthInfo> mPendingAuth = new ConcurrentHashMap<>();
    private OlkServiceConnector.ConnectorListener mConnectorListener = new OlkServiceConnector.ConnectorListener() { // from class: com.oplus.network.OplusOlkManager.1
        @Override // com.oplus.network.OlkServiceConnector.ConnectorListener
        public void onServiceConnected(IOlk service) {
            OplusOlkManager.this.mOlkService = service;
            synchronized (OplusOlkManager.this.mPendingAuth) {
                try {
                    for (String packageName : OplusOlkManager.this.mPendingAuth.keySet()) {
                        AuthInfo info = (AuthInfo) OplusOlkManager.this.mPendingAuth.get(packageName);
                        OplusOlkManager.this.log("addAuthResultInfo permBits = " + info.mPerm + "  packageName = " + packageName);
                        OplusOlkManager.this.mOlkService.addAuthResultInfo(info.mUid, info.mPid, info.mPerm, packageName);
                    }
                    OplusOlkManager.this.mPendingAuth.clear();
                } catch (RemoteException e) {
                    Log.e(OplusOlkManager.TAG, "onServiceConnected Oops!! register fail, this is unexpected!!");
                }
            }
        }
    };

    /* loaded from: classes.dex */
    static class AuthInfo {
        int mPerm;
        int mPid;
        int mUid;

        AuthInfo() {
        }
    }

    protected OplusOlkManager(Context context) {
        this.mContext = context;
        OlkServiceConnector.create(context).connect(this.mConnectorListener);
        Log.d(TAG, "OplusOlkManager first new!");
    }

    public static OplusOlkManager getInstance(Context c) {
        OplusOlkManager oplusOlkManager;
        synchronized (OplusOlkManager.class) {
            if (sInstance == null) {
                sInstance = new OplusOlkManager(c);
            }
            oplusOlkManager = sInstance;
        }
        return oplusOlkManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String content) {
        if (DEBUG) {
            Log.d(TAG, content);
        }
    }

    public void addAuthResultInfo(Context context, int uid, int pid, int permBits, String packageName) {
        if (uid == 0) {
            throw new IllegalArgumentException("uid was 0, which is illegal.");
        }
        if (pid == 0) {
            throw new IllegalArgumentException("pid was 0, which is illegal.");
        }
        if (packageName == null) {
            throw new IllegalArgumentException("packageName was null, which is illegal.");
        }
        log("addAuthResultInfo permBits = " + permBits + "  packageName = " + packageName);
        IOlk iOlk = this.mOlkService;
        if (iOlk == null) {
            synchronized (this.mPendingAuth) {
                Log.i(TAG, "addAuthResultInfo to Pending... ");
                AuthInfo info = new AuthInfo();
                info.mUid = uid;
                info.mPid = pid;
                info.mPerm = permBits;
                this.mPendingAuth.put(packageName, info);
            }
            return;
        }
        try {
            iOlk.addAuthResultInfo(uid, pid, permBits, packageName);
            log("addAuthResultInfo success");
        } catch (RemoteException e) {
            Log.e(TAG, "addAuthResultInfo, unexpected! mount service not found");
        }
    }
}
