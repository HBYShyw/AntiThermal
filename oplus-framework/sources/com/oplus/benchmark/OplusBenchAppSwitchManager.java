package com.oplus.benchmark;

import android.content.Context;
import android.os.Process;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.app.OplusAppEnterInfo;
import com.oplus.app.OplusAppExitInfo;
import com.oplus.app.OplusAppSwitchConfig;
import com.oplus.app.OplusAppSwitchManager;
import dalvik.system.VMRuntime;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusBenchAppSwitchManager {
    private static final String TAG = "OplusBenchAppSwitchManagerr";
    private Context mContext;
    private static final boolean DEBUG = !SystemProperties.getBoolean("ro.build.release_type", false);
    private static final Object mLock = new Object();
    private static OplusBenchAppSwitchManager sInstance = null;
    private boolean mIsRegistered = false;
    private final OplusAppSwitchManager.OnAppSwitchObserver mDynamicObserver = new OplusAppSwitchManager.OnAppSwitchObserver() { // from class: com.oplus.benchmark.OplusBenchAppSwitchManager.1
        @Override // com.oplus.app.OplusAppSwitchManager.OnAppSwitchObserver
        public void onAppEnter(OplusAppEnterInfo info) {
            if (info != null) {
                String packageName = info.targetName;
                if (OplusBenchAppSwitchManager.DEBUG) {
                    Log.d(OplusBenchAppSwitchManager.TAG, "Detect " + packageName + " switch to foreground");
                }
                if (packageName.contains("antutu")) {
                    OplusBenchAppSwitchManager.this.setIgnoreTargetFootprint(true);
                    VMRuntime.getRuntime().requestConcurrentGC();
                }
            }
        }

        @Override // com.oplus.app.OplusAppSwitchManager.OnAppSwitchObserver
        public void onAppExit(OplusAppExitInfo info) {
            if (info != null) {
                String packageName = info.targetName;
                if (OplusBenchAppSwitchManager.DEBUG) {
                    Log.d(OplusBenchAppSwitchManager.TAG, "Detect " + packageName + " switch to exit");
                }
                if (packageName.contains("antutu")) {
                    OplusBenchAppSwitchManager.this.setIgnoreTargetFootprint(false);
                    OplusBenchAppSwitchManager.this.unregisterBenchAppSwitchObserver();
                    VMRuntime.getRuntime().requestConcurrentGC();
                }
            }
        }

        @Override // com.oplus.app.OplusAppSwitchManager.OnAppSwitchObserver
        public void onActivityEnter(OplusAppEnterInfo info) {
        }

        @Override // com.oplus.app.OplusAppSwitchManager.OnAppSwitchObserver
        public void onActivityExit(OplusAppExitInfo info) {
        }
    };

    private OplusBenchAppSwitchManager(Context context) {
        this.mContext = context;
    }

    public static OplusBenchAppSwitchManager getInstance(Context context) {
        OplusBenchAppSwitchManager oplusBenchAppSwitchManager;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new OplusBenchAppSwitchManager(context);
                if (DEBUG) {
                    Log.d(TAG, "new OplusBenchAppSwitchManager in pid: " + Process.myPid());
                }
            }
            oplusBenchAppSwitchManager = sInstance;
        }
        return oplusBenchAppSwitchManager;
    }

    public synchronized void registerBenchAppSwitchObserver(String pkgName) {
        if (this.mIsRegistered) {
            return;
        }
        List<String> pkgList = new ArrayList<>();
        OplusAppSwitchConfig config = new OplusAppSwitchConfig();
        pkgList.add(pkgName);
        config.addAppConfig(2, pkgList);
        try {
            OplusAppSwitchManager.getInstance().registerAppSwitchObserver(this.mContext, this.mDynamicObserver, config);
            this.mIsRegistered = true;
            VMRuntime.getRuntime().requestConcurrentGC();
            setIgnoreTargetFootprint(true);
            if (DEBUG) {
                Log.d(TAG, "register app switch observer: " + pkgList);
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Oops! Exception on register: " + e.getMessage());
            }
        }
    }

    public synchronized void unregisterBenchAppSwitchObserver() {
        if (this.mIsRegistered) {
            try {
                OplusAppSwitchManager.getInstance().unregisterAppSwitchObserver(this.mContext, this.mDynamicObserver);
                this.mIsRegistered = false;
                if (DEBUG) {
                    Log.d(TAG, "unregister app switch observer: ");
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "Oops! Exception on unregister: " + e.getMessage());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIgnoreTargetFootprint(Boolean status) {
    }
}
