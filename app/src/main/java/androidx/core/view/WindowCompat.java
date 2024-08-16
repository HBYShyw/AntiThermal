package androidx.core.view;

import android.view.View;
import android.view.Window;

/* compiled from: WindowCompat.java */
/* renamed from: androidx.core.view.j0, reason: use source file name */
/* loaded from: classes.dex */
public final class WindowCompat {

    /* compiled from: WindowCompat.java */
    /* renamed from: androidx.core.view.j0$a */
    /* loaded from: classes.dex */
    static class a {
        static void a(Window window, boolean z10) {
            window.setDecorFitsSystemWindows(z10);
        }
    }

    public static WindowInsetsControllerCompat a(Window window, View view) {
        return new WindowInsetsControllerCompat(window, view);
    }

    public static void b(Window window, boolean z10) {
        a.a(window, z10);
    }
}
