package androidx.core.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;

/* compiled from: AppOpsManagerCompat.java */
/* renamed from: androidx.core.app.d, reason: use source file name */
/* loaded from: classes.dex */
public final class AppOpsManagerCompat {

    /* compiled from: AppOpsManagerCompat.java */
    /* renamed from: androidx.core.app.d$a */
    /* loaded from: classes.dex */
    static class a {
        static <T> T a(Context context, Class<T> cls) {
            return (T) context.getSystemService(cls);
        }

        static int b(AppOpsManager appOpsManager, String str, String str2) {
            return appOpsManager.noteProxyOp(str, str2);
        }

        static int c(AppOpsManager appOpsManager, String str, String str2) {
            return appOpsManager.noteProxyOpNoThrow(str, str2);
        }

        static String d(String str) {
            return AppOpsManager.permissionToOp(str);
        }
    }

    /* compiled from: AppOpsManagerCompat.java */
    /* renamed from: androidx.core.app.d$b */
    /* loaded from: classes.dex */
    static class b {
        static int a(AppOpsManager appOpsManager, String str, int i10, String str2) {
            if (appOpsManager == null) {
                return 1;
            }
            return appOpsManager.checkOpNoThrow(str, i10, str2);
        }

        static String b(Context context) {
            return context.getOpPackageName();
        }

        static AppOpsManager c(Context context) {
            return (AppOpsManager) context.getSystemService(AppOpsManager.class);
        }
    }

    public static int a(Context context, int i10, String str, String str2) {
        AppOpsManager c10 = b.c(context);
        int a10 = b.a(c10, str, Binder.getCallingUid(), str2);
        return a10 != 0 ? a10 : b.a(c10, str, i10, b.b(context));
    }

    public static int b(Context context, String str, String str2) {
        return a.c((AppOpsManager) a.a(context, AppOpsManager.class), str, str2);
    }

    public static String c(String str) {
        return a.d(str);
    }
}
