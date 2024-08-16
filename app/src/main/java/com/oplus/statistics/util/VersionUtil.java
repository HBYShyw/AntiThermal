package com.oplus.statistics.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Base64;
import com.oplus.statistics.record.ContentProviderRecorder;
import com.oplus.statistics.util.VersionUtil;
import java.nio.charset.StandardCharsets;

/* loaded from: classes2.dex */
public class VersionUtil {
    private static final String DCS_PKG_NAME = new String(Base64.decode(Constant.DCS_PKG, 0), StandardCharsets.UTF_8);
    private static final int SUPPORT_CONTENT_PROVIDER_VERSION = 5300000;
    private static final int SUPPORT_PERIOD_DATA_VERSION = 5118000;
    private static final String TAG = "VersionUtil";
    private static final int UNINSTALL_APP_FLAG = -1;

    public static long getDataCollectionAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(DCS_PKG_NAME, 1).getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e10) {
            LogUtil.w(TAG, new Supplier() { // from class: ga.h
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getDataCollectionAppVersion$0;
                    lambda$getDataCollectionAppVersion$0 = VersionUtil.lambda$getDataCollectionAppVersion$0(e10);
                    return lambda$getDataCollectionAppVersion$0;
                }
            });
            return -1L;
        }
    }

    public static boolean isContentProviderRecorder(Context context) {
        return ContentProviderRecorder.isSupport(context);
    }

    public static boolean isSupportPeriodData(Context context) {
        long dataCollectionAppVersion = getDataCollectionAppVersion(context);
        return dataCollectionAppVersion >= 5118000 || dataCollectionAppVersion == -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getDataCollectionAppVersion$0(PackageManager.NameNotFoundException nameNotFoundException) {
        return "getDataCollectionAppVersion exception: " + nameNotFoundException.toString();
    }
}
