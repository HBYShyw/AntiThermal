package com.oplus.verifycode;

import android.app.OplusActivityManager;

/* loaded from: classes.dex */
public class OplusVerifyCodeManager {
    public static final String TAG = "OplusVerifyCodeManager";
    private static volatile OplusVerifyCodeManager sInstance;
    private Object mLock = new Object();
    private OplusActivityManager mOAms = new OplusActivityManager();

    public static OplusVerifyCodeManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusVerifyCodeManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusVerifyCodeManager();
                }
            }
        }
        return sInstance;
    }

    public boolean addOrRemoveOplusVerifyCodeListener(boolean add, IOplusVerifyCodeListener listener) {
        try {
            this.mOAms.addOrRemoveOplusVerifyCodeListener(add, listener);
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
