package androidx.core.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

/* compiled from: ViewPropertyAnimatorCompat.java */
/* renamed from: androidx.core.view.f0, reason: use source file name */
/* loaded from: classes.dex */
public final class ViewPropertyAnimatorCompat {

    /* renamed from: a, reason: collision with root package name */
    private final WeakReference<View> f2361a;

    /* renamed from: b, reason: collision with root package name */
    Runnable f2362b = null;

    /* renamed from: c, reason: collision with root package name */
    Runnable f2363c = null;

    /* renamed from: d, reason: collision with root package name */
    int f2364d = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewPropertyAnimatorCompat.java */
    /* renamed from: androidx.core.view.f0$a */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimatorListener f2365a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f2366b;

        a(ViewPropertyAnimatorListener viewPropertyAnimatorListener, View view) {
            this.f2365a = viewPropertyAnimatorListener;
            this.f2366b = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f2365a.a(this.f2366b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f2365a.b(this.f2366b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f2365a.c(this.f2366b);
        }
    }

    /* compiled from: ViewPropertyAnimatorCompat.java */
    /* renamed from: androidx.core.view.f0$b */
    /* loaded from: classes.dex */
    static class b {
        static ViewPropertyAnimator a(ViewPropertyAnimator viewPropertyAnimator, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
            return viewPropertyAnimator.setUpdateListener(animatorUpdateListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewPropertyAnimatorCompat(View view) {
        this.f2361a = new WeakReference<>(view);
    }

    private void j(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (viewPropertyAnimatorListener != null) {
            view.animate().setListener(new a(viewPropertyAnimatorListener, view));
        } else {
            view.animate().setListener(null);
        }
    }

    public ViewPropertyAnimatorCompat b(float f10) {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().alpha(f10);
        }
        return this;
    }

    public void c() {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().cancel();
        }
    }

    public long d() {
        View view = this.f2361a.get();
        if (view != null) {
            return view.animate().getDuration();
        }
        return 0L;
    }

    public ViewPropertyAnimatorCompat f(float f10) {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().rotation(f10);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat g(long j10) {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().setDuration(j10);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat h(Interpolator interpolator) {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().setInterpolator(interpolator);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat i(ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        View view = this.f2361a.get();
        if (view != null) {
            j(view, viewPropertyAnimatorListener);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat k(long j10) {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().setStartDelay(j10);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat l(final ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener) {
        final View view = this.f2361a.get();
        if (view != null) {
            b.a(view.animate(), viewPropertyAnimatorUpdateListener != null ? new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.core.view.e0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewPropertyAnimatorUpdateListener.this.a(view);
                }
            } : null);
        }
        return this;
    }

    public void m() {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().start();
        }
    }

    public ViewPropertyAnimatorCompat n(float f10) {
        View view = this.f2361a.get();
        if (view != null) {
            view.animate().translationY(f10);
        }
        return this;
    }
}
