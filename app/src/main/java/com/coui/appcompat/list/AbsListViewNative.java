package com.coui.appcompat.list;

import android.util.Log;
import android.widget.AbsListView;
import java.lang.reflect.Field;

/* compiled from: AbsListViewNative.java */
/* renamed from: com.coui.appcompat.list.a, reason: use source file name */
/* loaded from: classes.dex */
public class AbsListViewNative {

    /* renamed from: a, reason: collision with root package name */
    private static final boolean f6288a = true;

    /* renamed from: b, reason: collision with root package name */
    private static String f6289b;

    private static boolean a() {
        try {
            Class.forName("com.oplus.inner.widget.AbsListViewWrapper");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void b(AbsListView absListView, int i10) {
        String a10 = a() ? "com.oplus.inner.widget.AbsListViewWrapper" : j3.a.c().a();
        f6289b = a10;
        try {
            if (f6288a) {
                Class.forName(a10).getDeclaredMethod("setTouchMode", AbsListView.class, Integer.TYPE).invoke(null, absListView, Integer.valueOf(i10));
            } else {
                Field declaredField = AbsListView.class.getDeclaredField("mTouchMode");
                declaredField.setAccessible(true);
                declaredField.setInt(absListView, i10);
            }
        } catch (Exception e10) {
            Log.d("AbsListViewNative", e10.toString());
        }
    }
}
