package e6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import b6.LocalLog;
import java.util.ArrayList;

/* compiled from: SmartModeSharepref.java */
/* renamed from: e6.b, reason: use source file name */
/* loaded from: classes.dex */
public class SmartModeSharepref {

    /* renamed from: a, reason: collision with root package name */
    private static final ArrayList<String> f10970a = new a();

    /* renamed from: b, reason: collision with root package name */
    private static SharedPreferences f10971b;

    /* compiled from: SmartModeSharepref.java */
    /* renamed from: e6.b$a */
    /* loaded from: classes.dex */
    class a extends ArrayList<String> {
        a() {
            add("com.google.android.gms");
            add("com.google.android.configupdater");
            add("com.android.vending");
        }
    }

    public static boolean a(Context context, String str, boolean z10) {
        g(context);
        try {
            return f10971b.getBoolean(str, z10);
        } catch (Exception e10) {
            LocalLog.b("SmartModeSharepref", "getBooleanValue() Exception: " + e10);
            return z10;
        }
    }

    public static int b(Context context, String str, int i10) {
        g(context);
        try {
            return f10971b.getInt(str, i10);
        } catch (Exception e10) {
            LocalLog.b("SmartModeSharepref", "getIntValue() Exception: " + e10);
            return i10;
        }
    }

    public static long c(Context context, String str, long j10) {
        g(context);
        try {
            return f10971b.getLong(str, j10);
        } catch (Exception e10) {
            LocalLog.b("SmartModeSharepref", "getLongValue() Exception: " + e10);
            return j10;
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void d(Context context, String str, int i10) {
        g(context);
        SharedPreferences.Editor edit = f10971b.edit();
        edit.putInt(str, i10);
        edit.apply();
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void e(Context context, String str, long j10) {
        g(context);
        SharedPreferences.Editor edit = f10971b.edit();
        edit.putLong(str, j10);
        edit.apply();
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void f(Context context, String str, boolean z10) {
        g(context);
        SharedPreferences.Editor edit = f10971b.edit();
        edit.putBoolean(str, z10);
        edit.apply();
    }

    private static void g(Context context) {
        if (f10971b == null) {
            f10971b = context.getSharedPreferences("pmScenario", 0);
        }
    }
}
