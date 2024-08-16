package androidx.appcompat.view;

import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: ViewPropertyAnimatorCompatSet.java */
/* renamed from: androidx.appcompat.view.h, reason: use source file name */
/* loaded from: classes.dex */
public class ViewPropertyAnimatorCompatSet {

    /* renamed from: c, reason: collision with root package name */
    private Interpolator f615c;

    /* renamed from: d, reason: collision with root package name */
    ViewPropertyAnimatorListener f616d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f617e;

    /* renamed from: b, reason: collision with root package name */
    private long f614b = -1;

    /* renamed from: f, reason: collision with root package name */
    private final ViewPropertyAnimatorListenerAdapter f618f = new a();

    /* renamed from: a, reason: collision with root package name */
    final ArrayList<ViewPropertyAnimatorCompat> f613a = new ArrayList<>();

    /* compiled from: ViewPropertyAnimatorCompatSet.java */
    /* renamed from: androidx.appcompat.view.h$a */
    /* loaded from: classes.dex */
    class a extends ViewPropertyAnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f619a = false;

        /* renamed from: b, reason: collision with root package name */
        private int f620b = 0;

        a() {
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void b(View view) {
            int i10 = this.f620b + 1;
            this.f620b = i10;
            if (i10 == ViewPropertyAnimatorCompatSet.this.f613a.size()) {
                ViewPropertyAnimatorListener viewPropertyAnimatorListener = ViewPropertyAnimatorCompatSet.this.f616d;
                if (viewPropertyAnimatorListener != null) {
                    viewPropertyAnimatorListener.b(null);
                }
                d();
            }
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
        public void c(View view) {
            if (this.f619a) {
                return;
            }
            this.f619a = true;
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = ViewPropertyAnimatorCompatSet.this.f616d;
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.c(null);
            }
        }

        void d() {
            this.f620b = 0;
            this.f619a = false;
            ViewPropertyAnimatorCompatSet.this.b();
        }
    }

    public void a() {
        if (this.f617e) {
            Iterator<ViewPropertyAnimatorCompat> it = this.f613a.iterator();
            while (it.hasNext()) {
                it.next().c();
            }
            this.f617e = false;
        }
    }

    void b() {
        this.f617e = false;
    }

    public ViewPropertyAnimatorCompatSet c(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) {
        if (!this.f617e) {
            this.f613a.add(viewPropertyAnimatorCompat);
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet d(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2) {
        this.f613a.add(viewPropertyAnimatorCompat);
        viewPropertyAnimatorCompat2.k(viewPropertyAnimatorCompat.d());
        this.f613a.add(viewPropertyAnimatorCompat2);
        return this;
    }

    public ViewPropertyAnimatorCompatSet e(long j10) {
        if (!this.f617e) {
            this.f614b = j10;
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet f(Interpolator interpolator) {
        if (!this.f617e) {
            this.f615c = interpolator;
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet g(ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (!this.f617e) {
            this.f616d = viewPropertyAnimatorListener;
        }
        return this;
    }

    public void h() {
        if (this.f617e) {
            return;
        }
        Iterator<ViewPropertyAnimatorCompat> it = this.f613a.iterator();
        while (it.hasNext()) {
            ViewPropertyAnimatorCompat next = it.next();
            long j10 = this.f614b;
            if (j10 >= 0) {
                next.g(j10);
            }
            Interpolator interpolator = this.f615c;
            if (interpolator != null) {
                next.h(interpolator);
            }
            if (this.f616d != null) {
                next.i(this.f618f);
            }
            next.m();
        }
        this.f617e = true;
    }
}
