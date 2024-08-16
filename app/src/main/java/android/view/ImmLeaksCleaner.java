package android.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import androidx.lifecycle.o;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
final class ImmLeaksCleaner implements LifecycleEventObserver {

    /* renamed from: f, reason: collision with root package name */
    private static int f259f;

    /* renamed from: g, reason: collision with root package name */
    private static Field f260g;

    /* renamed from: h, reason: collision with root package name */
    private static Field f261h;

    /* renamed from: i, reason: collision with root package name */
    private static Field f262i;

    /* renamed from: e, reason: collision with root package name */
    private Activity f263e;

    @SuppressLint({"SoonBlockedPrivateApi"})
    private static void b() {
        try {
            f259f = 2;
            Field declaredField = InputMethodManager.class.getDeclaredField("mServedView");
            f261h = declaredField;
            declaredField.setAccessible(true);
            Field declaredField2 = InputMethodManager.class.getDeclaredField("mNextServedView");
            f262i = declaredField2;
            declaredField2.setAccessible(true);
            Field declaredField3 = InputMethodManager.class.getDeclaredField("mH");
            f260g = declaredField3;
            declaredField3.setAccessible(true);
            f259f = 1;
        } catch (NoSuchFieldException unused) {
        }
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        if (bVar != h.b.ON_DESTROY) {
            return;
        }
        if (f259f == 0) {
            b();
        }
        if (f259f == 1) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.f263e.getSystemService("input_method");
            try {
                Object obj = f260g.get(inputMethodManager);
                if (obj == null) {
                    return;
                }
                synchronized (obj) {
                    try {
                        try {
                            View view = (View) f261h.get(inputMethodManager);
                            if (view == null) {
                                return;
                            }
                            if (view.isAttachedToWindow()) {
                                return;
                            }
                            try {
                                f262i.set(inputMethodManager, null);
                                inputMethodManager.isActive();
                            } catch (IllegalAccessException unused) {
                            }
                        } catch (ClassCastException unused2) {
                        } catch (IllegalAccessException unused3) {
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            } catch (IllegalAccessException unused4) {
            }
        }
    }
}
