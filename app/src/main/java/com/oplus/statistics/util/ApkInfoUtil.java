package com.oplus.statistics.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.oplus.statistics.g0;
import com.oplus.statistics.util.ApkInfoUtil;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class ApkInfoUtil {
    private static final String TAG = "ApkInfoUtil";
    private static final Map<Application, String> sAppCodeCache = new HashMap();

    public static String getAppCode(Context context) {
        Application application = (Application) context.getApplicationContext();
        Map<Application, String> map = sAppCodeCache;
        String str = map.get(application);
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = null;
        try {
            str2 = String.valueOf(context.getPackageManager().getApplicationInfo(getPackageName(context), 128).metaData.get("AppCode"));
            if (TextUtils.isEmpty(str2)) {
                LogUtil.e(TAG, new Supplier() { // from class: ga.b
                    @Override // com.oplus.statistics.util.Supplier
                    public final Object get() {
                        String lambda$getAppCode$1;
                        lambda$getAppCode$1 = ApkInfoUtil.lambda$getAppCode$1();
                        return lambda$getAppCode$1;
                    }
                });
            } else {
                map.put(application, str2);
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
            e10.printStackTrace();
        }
        return str2;
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageInfo(context.getPackageName(), 0).applicationInfo.loadLabel(packageManager).toString();
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
            return "0";
        }
    }

    public static String getPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
            return "0";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        String str = "0";
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo == null) {
                return "0";
            }
            String str2 = packageInfo.versionName;
            if (str2 == null) {
                return "0";
            }
            try {
                LogUtil.i(TAG, new Supplier() { // from class: ga.a
                    @Override // com.oplus.statistics.util.Supplier
                    public final Object get() {
                        String lambda$getVersionName$0;
                        lambda$getVersionName$0 = ApkInfoUtil.lambda$getVersionName$0(packageInfo);
                        return lambda$getVersionName$0;
                    }
                });
                return str2;
            } catch (Exception e10) {
                e = e10;
                str = str2;
                LogUtil.e(TAG, new g0(e));
                return str;
            }
        } catch (Exception e11) {
            e = e11;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getAppCode$1() {
        return "AppCode not set. please read the document of OplusTrack SDK.";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getVersionName$0(PackageInfo packageInfo) {
        return "versionName=" + packageInfo.versionName;
    }

    public static void putAppCodeToCache(Context context, String str) {
        sAppCodeCache.put((Application) context.getApplicationContext(), str);
    }

    public static int getVersionCode(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
            return 0;
        }
    }
}
