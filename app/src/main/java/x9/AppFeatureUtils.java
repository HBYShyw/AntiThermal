package x9;

import android.content.Context;
import h6.AppFeatureProviderUtils;
import java.util.List;

/* compiled from: AppFeatureUtils.java */
/* renamed from: x9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class AppFeatureUtils {
    public static boolean a(Context context) {
        return AppFeatureProviderUtils.h(context.getContentResolver(), "com.android.launcher.TASKBAR_ENABLE");
    }

    public static boolean b(Context context) {
        return AppFeatureProviderUtils.h(context.getContentResolver(), "safecenter.startup.test.skip");
    }

    public static boolean c(Context context) {
        return AppFeatureProviderUtils.h(context.getContentResolver(), "safecenter.startup.custom.associate");
    }

    public static boolean d(Context context) {
        return AppFeatureProviderUtils.h(context.getContentResolver(), "safecenter.startup.custom.autostart");
    }

    public static boolean e(Context context) {
        return AppFeatureProviderUtils.h(context.getContentResolver(), "safecenter.startup.custom.autowhite");
    }

    public static List<String> f(Context context) {
        return AppFeatureProviderUtils.f(context.getContentResolver(), "safecenter.startup.test.skip");
    }

    public static List<String> g(Context context) {
        return AppFeatureProviderUtils.f(context.getContentResolver(), "safecenter.startup.custom.associate");
    }

    public static List<String> h(Context context) {
        return AppFeatureProviderUtils.f(context.getContentResolver(), "safecenter.startup.custom.autowhite");
    }

    public static List<String> i(Context context) {
        return AppFeatureProviderUtils.f(context.getContentResolver(), "safecenter.startup.custom.autostart");
    }
}
