package com.oplus.quickreply;

import android.app.OplusActivityManager;
import android.app.PendingIntent;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusQuickReplyManager {
    public static final int CLIENT_CLOSE_QUICKREPLY = 0;
    public static final int CLIENT_OPEN_QUICKREPLY = 1;
    public static final String TAG = "OplusQuickReplyManager";
    private static volatile OplusQuickReplyManager sInstance;
    private final Object mLock = new Object();
    private OplusActivityManager mOAms = new OplusActivityManager();

    public static OplusQuickReplyManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusQuickReplyManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusQuickReplyManager();
                }
            }
        }
        return sInstance;
    }

    public void bindQuickReplyService(IQuickReplyCallback callback) {
        synchronized (this.mLock) {
            try {
                OplusActivityManager oplusActivityManager = this.mOAms;
                if (oplusActivityManager != null) {
                    oplusActivityManager.bindQuickReplyService(callback);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "bindQuickReplyService remoteException ");
            }
        }
    }

    public void unbindQuickReplyService() {
        synchronized (this.mLock) {
            try {
                OplusActivityManager oplusActivityManager = this.mOAms;
                if (oplusActivityManager != null) {
                    oplusActivityManager.unbindQuickReplyService();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "unbindQuickReplyService remoteException ");
            }
        }
    }

    public boolean sendMessage(PendingIntent weChatIntent, String message) {
        synchronized (this.mLock) {
            try {
                try {
                    OplusActivityManager oplusActivityManager = this.mOAms;
                    if (oplusActivityManager == null) {
                        return false;
                    }
                    return oplusActivityManager.sendMessage(weChatIntent, message);
                } catch (RemoteException e) {
                    Log.e(TAG, "sendMessage remoteException ");
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
