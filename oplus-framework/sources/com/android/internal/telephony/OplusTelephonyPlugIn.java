package com.android.internal.telephony;

import android.app.ActivityThread;
import android.content.Context;
import android.telephony.Rlog;
import dalvik.system.DexClassLoader;

/* loaded from: classes.dex */
public class OplusTelephonyPlugIn {
    private static final String NR_FACTORY_CLS = "com.oplus.nrMode.OplusNrModeFactory";
    private static final String NR_PLUGIN_APK = "/system_ext/priv-app/OplusNrMode/OplusNrMode.apk";
    private static final String TAG = "OplusTelephonyPlugIn";
    private static OplusTelephonyPlugIn sInstance = null;
    private static IOplusNrModePlugIn sOplusNrMode = null;
    private String mCachePath;
    private Context mContext;
    private ClassLoader mLoader;

    public OplusTelephonyPlugIn() {
        Context applicationContext = ActivityThread.currentApplication().getApplicationContext();
        this.mContext = applicationContext;
        this.mCachePath = applicationContext.getFilesDir().getAbsolutePath();
        this.mLoader = ClassLoader.getSystemClassLoader();
    }

    public static OplusTelephonyPlugIn getInstance() {
        OplusTelephonyPlugIn oplusTelephonyPlugIn;
        synchronized (OplusTelephonyPlugIn.class) {
            if (sInstance == null) {
                sInstance = new OplusTelephonyPlugIn();
            }
            oplusTelephonyPlugIn = sInstance;
        }
        return oplusTelephonyPlugIn;
    }

    public static boolean isInitialized() {
        boolean z;
        synchronized (OplusTelephonyPlugIn.class) {
            z = sInstance != null;
        }
        return z;
    }

    public IOplusNrModePlugIn getOplusNrModeFactory() {
        if (sOplusNrMode == null) {
            logd("getOplusNrFactory: NR_PLUGIN_APK = /system_ext/priv-app/OplusNrMode/OplusNrMode.apk");
            logd("getOplusNrFactory: NR_FACTORY_CLS = com.oplus.nrMode.OplusNrModeFactory");
            DexClassLoader loader = new DexClassLoader(NR_PLUGIN_APK, this.mCachePath, null, this.mLoader);
            try {
                Class<?> cls = loader.loadClass(NR_FACTORY_CLS);
                IOplusNrModePlugIn iOplusNrModePlugIn = (IOplusNrModePlugIn) cls.newInstance();
                sOplusNrMode = iOplusNrModePlugIn;
                iOplusNrModePlugIn.initialOnce(this.mContext, NR_PLUGIN_APK);
            } catch (Exception ex) {
                ex.printStackTrace();
                sOplusNrMode = new IOplusNrModePlugIn() { // from class: com.android.internal.telephony.OplusTelephonyPlugIn.1
                };
            }
        }
        return sOplusNrMode;
    }

    private void logd(String msg) {
        Rlog.d(TAG, msg);
    }
}
