package l9;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.util.List;

/* compiled from: AppUtils.java */
/* loaded from: classes2.dex */
public final class a {
    public static Intent a(Context context, Intent intent) {
        try {
            if (intent == null) {
                LogUtils.c("AppUtils", "getExplicitActivityIntent, implicitIntent is null, return null");
                return null;
            }
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            if (queryIntentActivities.size() == 0) {
                LogUtils.c("AppUtils", "getExplicitActivityIntent, resolveInfo is not compatibale, return null");
                return null;
            }
            String str = queryIntentActivities.get(0).activityInfo.packageName;
            Intent intent2 = new Intent(intent);
            intent2.setPackage(str);
            return intent2;
        } catch (Exception e10) {
            LogUtils.a("AppUtils", "getExplicitActivityIntent:" + e10);
            return null;
        }
    }
}
