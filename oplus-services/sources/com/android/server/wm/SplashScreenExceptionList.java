package com.android.server.wm;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.DeviceConfig;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SplashScreenExceptionList {
    private static final boolean DEBUG = Build.isDebuggable();
    private static final String KEY_SPLASH_SCREEN_EXCEPTION_LIST = "splash_screen_exception_list";
    private static final String LOG_TAG = "SplashScreenExceptionList";
    private static final String NAMESPACE = "window_manager";
    private static final String OPT_OUT_METADATA_FLAG = "android.splashscreen.exception_opt_out";

    @GuardedBy({"mLock"})
    private final HashSet<String> mDeviceConfigExcludedPackages = new HashSet<>();
    private final Object mLock = new Object();

    @VisibleForTesting
    final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplashScreenExceptionList(Executor executor) {
        updateDeviceConfig(DeviceConfig.getString(NAMESPACE, KEY_SPLASH_SCREEN_EXCEPTION_LIST, ""));
        DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.SplashScreenExceptionList$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                SplashScreenExceptionList.this.lambda$new$0(properties);
            }
        };
        this.mOnPropertiesChangedListener = onPropertiesChangedListener;
        DeviceConfig.addOnPropertiesChangedListener(NAMESPACE, executor, onPropertiesChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        updateDeviceConfig(properties.getString(KEY_SPLASH_SCREEN_EXCEPTION_LIST, ""));
    }

    @VisibleForTesting
    void updateDeviceConfig(String str) {
        parseDeviceConfigPackageList(str);
    }

    public boolean isException(String str, int i, Supplier<ApplicationInfo> supplier) {
        if (i > 35) {
            return false;
        }
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.v(LOG_TAG, String.format(Locale.US, "SplashScreen checking exception for package %s (target sdk:%d) -> %s", str, Integer.valueOf(i), Boolean.valueOf(this.mDeviceConfigExcludedPackages.contains(str))));
            }
            if (this.mDeviceConfigExcludedPackages.contains(str)) {
                return !isOptedOut(supplier);
            }
            return false;
        }
    }

    private static boolean isOptedOut(Supplier<ApplicationInfo> supplier) {
        ApplicationInfo applicationInfo;
        Bundle bundle;
        return (supplier == null || (applicationInfo = supplier.get()) == null || (bundle = applicationInfo.metaData) == null || !bundle.getBoolean(OPT_OUT_METADATA_FLAG, false)) ? false : true;
    }

    private void parseDeviceConfigPackageList(String str) {
        synchronized (this.mLock) {
            this.mDeviceConfigExcludedPackages.clear();
            for (String str2 : str.split(",")) {
                String trim = str2.trim();
                if (!trim.isEmpty()) {
                    this.mDeviceConfigExcludedPackages.add(trim);
                }
            }
        }
    }
}
