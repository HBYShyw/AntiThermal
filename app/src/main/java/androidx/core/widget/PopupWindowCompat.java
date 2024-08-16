package androidx.core.widget;

import android.view.View;
import android.widget.PopupWindow;

/* compiled from: PopupWindowCompat.java */
/* renamed from: androidx.core.widget.j, reason: use source file name */
/* loaded from: classes.dex */
public final class PopupWindowCompat {

    /* compiled from: PopupWindowCompat.java */
    /* renamed from: androidx.core.widget.j$a */
    /* loaded from: classes.dex */
    static class a {
        static void a(PopupWindow popupWindow, View view, int i10, int i11, int i12) {
            popupWindow.showAsDropDown(view, i10, i11, i12);
        }
    }

    /* compiled from: PopupWindowCompat.java */
    /* renamed from: androidx.core.widget.j$b */
    /* loaded from: classes.dex */
    static class b {
        static boolean a(PopupWindow popupWindow) {
            return popupWindow.getOverlapAnchor();
        }

        static int b(PopupWindow popupWindow) {
            return popupWindow.getWindowLayoutType();
        }

        static void c(PopupWindow popupWindow, boolean z10) {
            popupWindow.setOverlapAnchor(z10);
        }

        static void d(PopupWindow popupWindow, int i10) {
            popupWindow.setWindowLayoutType(i10);
        }
    }

    public static void a(PopupWindow popupWindow, boolean z10) {
        b.c(popupWindow, z10);
    }

    public static void b(PopupWindow popupWindow, int i10) {
        b.d(popupWindow, i10);
    }

    public static void c(PopupWindow popupWindow, View view, int i10, int i11, int i12) {
        a.a(popupWindow, view, i10, i11, i12);
    }
}
