package com.oplus.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.oplus.resolver.OplusResolverUtils;
import java.util.List;

/* loaded from: classes.dex */
public class OplusResolverUtil {
    private static final String TEST_ACTIVITY_BOTH = ".TwoGroupsActivity";
    private static final String TEST_ACTIVITY_FIRST = ".FirstActivity";
    private static final String TEST_CTS_ACTION = "android.dynamicmime.preferred.TEST_ACTION";
    private static final String TEST_PACKAGE = "android.dynamicmime.preferred";
    private static final String TEST_TYPE = "text/plain";

    public static boolean isResolverCtsTest(Context context, Intent intent, ResolveInfo resolveInfo) {
        if (OplusResolverUtils.invokeMethod(context, "getResolverWrapper", new Object[0]) == null || resolveInfo == null || intent == null || ((!TEST_CTS_ACTION.equals(intent.getAction()) && !"android.intent.action.SEND".equals(intent.getAction())) || !TEST_TYPE.equals(intent.getType()) || resolveInfo.activityInfo == null || !TEST_PACKAGE.equals(resolveInfo.activityInfo.packageName))) {
            return false;
        }
        String label = resolveInfo.loadLabel(context.getPackageManager()).toString();
        return "TestApp.FirstActivity".equals(label) || "TestApp.TwoGroupsActivity".equals(label);
    }

    public static boolean isChooserCtsTest(Context context, Intent targetIntent) {
        if (OplusResolverUtils.invokeMethod(context, "getChooserWrapper", new Object[0]) == null) {
            return false;
        }
        return ("android.intent.action.SEND".equals(targetIntent.getAction()) || TEST_CTS_ACTION.equals(targetIntent.getAction())) && "test/cts".equals(targetIntent.getType());
    }

    public static void sortCtsTest(Context context, Intent intent, List<ResolveInfo> resolveInfos) {
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (isResolverCtsTest(context, intent, resolveInfo)) {
                resolveInfos.remove(resolveInfo);
                resolveInfos.add(0, resolveInfo);
                return;
            }
        }
    }
}
