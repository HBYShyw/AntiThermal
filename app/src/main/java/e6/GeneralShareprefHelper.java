package e6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArrayMap;
import b6.LocalLog;

/* compiled from: GeneralShareprefHelper.java */
/* renamed from: e6.a, reason: use source file name */
/* loaded from: classes.dex */
public class GeneralShareprefHelper {

    /* renamed from: a, reason: collision with root package name */
    private static final String f10968a = "a";

    /* renamed from: b, reason: collision with root package name */
    private static volatile ArrayMap<String, SharedPreferences> f10969b = new ArrayMap<>();

    public static boolean a(Context context, String str, String str2, boolean z10) {
        g(context, str);
        try {
            return f10969b.get(str).getBoolean(str2, z10);
        } catch (Exception e10) {
            LocalLog.b(f10968a, "getBooleanValue() Exception: " + e10);
            return z10;
        }
    }

    public static int b(Context context, String str, String str2, int i10) {
        g(context, str);
        try {
            return f10969b.get(str).getInt(str2, i10);
        } catch (Exception e10) {
            LocalLog.b(f10968a, "getIntValue() Exception: " + e10);
            return i10;
        }
    }

    public static long c(Context context, String str, String str2, long j10) {
        g(context, str);
        try {
            return f10969b.get(str).getLong(str2, j10);
        } catch (Exception e10) {
            LocalLog.b(f10968a, "getLongValue() Exception: " + e10);
            return j10;
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void d(Context context, String str, String str2, int i10) {
        g(context, str);
        SharedPreferences.Editor edit = f10969b.get(str).edit();
        edit.putInt(str2, i10);
        edit.apply();
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void e(Context context, String str, String str2, long j10) {
        g(context, str);
        SharedPreferences.Editor edit = f10969b.get(str).edit();
        edit.putLong(str2, j10);
        edit.apply();
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void f(Context context, String str, String str2, boolean z10) {
        g(context, str);
        SharedPreferences.Editor edit = f10969b.get(str).edit();
        edit.putBoolean(str2, z10);
        edit.apply();
    }

    private static void g(Context context, String str) {
        if (f10969b.get(str) == null) {
            synchronized (GeneralShareprefHelper.class) {
                if (f10969b.get(str) == null) {
                    f10969b.put(str, context.createDeviceProtectedStorageContext().getSharedPreferences(str, 0));
                }
            }
        }
    }
}
