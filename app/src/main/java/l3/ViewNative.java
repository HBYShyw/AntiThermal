package l3;

import android.util.Log;
import android.view.View;
import java.lang.reflect.Field;

/* compiled from: ViewNative.java */
/* renamed from: l3.b, reason: use source file name */
/* loaded from: classes.dex */
public class ViewNative {

    /* renamed from: a, reason: collision with root package name */
    private static final boolean f14590a = true;

    /* renamed from: b, reason: collision with root package name */
    private static String f14591b;

    private static boolean a() {
        try {
            Class.forName("com.oplus.inner.view.ViewWrapper");
            return true;
        } catch (Exception e10) {
            Log.d("ViewNative", e10.toString());
            return false;
        }
    }

    public static void b(View view, int i10) {
        String c10 = a() ? "com.oplus.inner.view.ViewWrapper" : a.b().c();
        f14591b = c10;
        try {
            if (f14590a) {
                Class.forName(c10).getDeclaredMethod("setScrollXForColor", View.class, Integer.TYPE).invoke(null, view, Integer.valueOf(i10));
            } else {
                Field declaredField = View.class.getDeclaredField("mScrollX");
                declaredField.setAccessible(true);
                declaredField.setInt(view, i10);
            }
        } catch (Exception e10) {
            Log.d("ViewNative", e10.toString());
        }
    }

    public static void c(View view, int i10) {
        String c10 = a() ? "com.oplus.inner.view.ViewWrapper" : a.b().c();
        f14591b = c10;
        try {
            if (f14590a) {
                Class.forName(c10).getDeclaredMethod("setScrollYForColor", View.class, Integer.TYPE).invoke(null, view, Integer.valueOf(i10));
            } else {
                Field declaredField = View.class.getDeclaredField("mScrollY");
                declaredField.setAccessible(true);
                declaredField.setInt(view, i10);
            }
        } catch (Exception e10) {
            Log.d("ViewNative", e10.toString());
        }
    }
}
