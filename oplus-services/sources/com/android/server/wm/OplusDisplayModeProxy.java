package com.android.server.wm;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OplusDisplayModeProxy {
    private static OplusDisplayModeProxy sInstance;
    private static final Object sLock = new Object();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface OnDisplayResolutionChangeListener {
        void onDisplayResolutionChange();
    }

    public int getDefaultDisplayHeight() {
        return 0;
    }

    public int getDefaultDisplayWidth() {
        return 0;
    }

    public void registerOnDisplayResolutionChangeListener(OnDisplayResolutionChangeListener onDisplayResolutionChangeListener) {
    }

    protected OplusDisplayModeProxy() {
    }

    public static OplusDisplayModeProxy getInstance(Context context) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = newInstance(context);
                }
            }
        }
        return sInstance;
    }

    public static OplusDisplayModeProxy newInstance(Context context) {
        try {
            return (OplusDisplayModeProxy) Class.forName("com.android.server.wm.OplusDisplayModeProxyImpl").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return new OplusDisplayModeProxy();
        }
    }
}
