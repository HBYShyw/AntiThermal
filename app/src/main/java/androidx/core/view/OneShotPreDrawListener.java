package androidx.core.view;

import android.view.View;
import android.view.ViewTreeObserver;
import java.util.Objects;

/* compiled from: OneShotPreDrawListener.java */
/* renamed from: androidx.core.view.v, reason: use source file name */
/* loaded from: classes.dex */
public final class OneShotPreDrawListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

    /* renamed from: e, reason: collision with root package name */
    private final View f2412e;

    /* renamed from: f, reason: collision with root package name */
    private ViewTreeObserver f2413f;

    /* renamed from: g, reason: collision with root package name */
    private final Runnable f2414g;

    private OneShotPreDrawListener(View view, Runnable runnable) {
        this.f2412e = view;
        this.f2413f = view.getViewTreeObserver();
        this.f2414g = runnable;
    }

    public static OneShotPreDrawListener a(View view, Runnable runnable) {
        Objects.requireNonNull(view, "view == null");
        Objects.requireNonNull(runnable, "runnable == null");
        OneShotPreDrawListener oneShotPreDrawListener = new OneShotPreDrawListener(view, runnable);
        view.getViewTreeObserver().addOnPreDrawListener(oneShotPreDrawListener);
        view.addOnAttachStateChangeListener(oneShotPreDrawListener);
        return oneShotPreDrawListener;
    }

    public void b() {
        if (this.f2413f.isAlive()) {
            this.f2413f.removeOnPreDrawListener(this);
        } else {
            this.f2412e.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        this.f2412e.removeOnAttachStateChangeListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        b();
        this.f2414g.run();
        return true;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        this.f2413f = view.getViewTreeObserver();
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        b();
    }
}
