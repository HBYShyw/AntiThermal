package androidx.core.view;

import android.view.ViewGroup;

/* compiled from: MarginLayoutParamsCompat.java */
/* renamed from: androidx.core.view.h, reason: use source file name */
/* loaded from: classes.dex */
public final class MarginLayoutParamsCompat {

    /* compiled from: MarginLayoutParamsCompat.java */
    /* renamed from: androidx.core.view.h$a */
    /* loaded from: classes.dex */
    static class a {
        static int a(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getLayoutDirection();
        }

        static int b(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getMarginEnd();
        }

        static int c(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getMarginStart();
        }

        static boolean d(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.isMarginRelative();
        }

        static void e(ViewGroup.MarginLayoutParams marginLayoutParams, int i10) {
            marginLayoutParams.resolveLayoutDirection(i10);
        }

        static void f(ViewGroup.MarginLayoutParams marginLayoutParams, int i10) {
            marginLayoutParams.setLayoutDirection(i10);
        }

        static void g(ViewGroup.MarginLayoutParams marginLayoutParams, int i10) {
            marginLayoutParams.setMarginEnd(i10);
        }

        static void h(ViewGroup.MarginLayoutParams marginLayoutParams, int i10) {
            marginLayoutParams.setMarginStart(i10);
        }
    }

    public static int a(ViewGroup.MarginLayoutParams marginLayoutParams) {
        return a.b(marginLayoutParams);
    }

    public static int b(ViewGroup.MarginLayoutParams marginLayoutParams) {
        return a.c(marginLayoutParams);
    }

    public static void c(ViewGroup.MarginLayoutParams marginLayoutParams, int i10) {
        a.g(marginLayoutParams, i10);
    }

    public static void d(ViewGroup.MarginLayoutParams marginLayoutParams, int i10) {
        a.h(marginLayoutParams, i10);
    }
}
