package androidx.viewpager2.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: FakeDrag.java */
/* renamed from: androidx.viewpager2.widget.d, reason: use source file name */
/* loaded from: classes.dex */
final class FakeDrag {

    /* renamed from: a, reason: collision with root package name */
    private final ViewPager2 f4275a;

    /* renamed from: b, reason: collision with root package name */
    private final ScrollEventAdapter f4276b;

    /* renamed from: c, reason: collision with root package name */
    private final RecyclerView f4277c;

    /* renamed from: d, reason: collision with root package name */
    private VelocityTracker f4278d;

    /* renamed from: e, reason: collision with root package name */
    private int f4279e;

    /* renamed from: f, reason: collision with root package name */
    private float f4280f;

    /* renamed from: g, reason: collision with root package name */
    private int f4281g;

    /* renamed from: h, reason: collision with root package name */
    private long f4282h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FakeDrag(ViewPager2 viewPager2, ScrollEventAdapter scrollEventAdapter, RecyclerView recyclerView) {
        this.f4275a = viewPager2;
        this.f4276b = scrollEventAdapter;
        this.f4277c = recyclerView;
    }

    private void a(long j10, int i10, float f10, float f11) {
        MotionEvent obtain = MotionEvent.obtain(this.f4282h, j10, i10, f10, f11, 0);
        this.f4278d.addMovement(obtain);
        obtain.recycle();
    }

    private void c() {
        VelocityTracker velocityTracker = this.f4278d;
        if (velocityTracker == null) {
            this.f4278d = VelocityTracker.obtain();
            this.f4279e = ViewConfiguration.get(this.f4275a.getContext()).getScaledMaximumFlingVelocity();
        } else {
            velocityTracker.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b() {
        if (this.f4276b.g()) {
            return false;
        }
        this.f4281g = 0;
        this.f4280f = 0;
        this.f4282h = SystemClock.uptimeMillis();
        c();
        this.f4276b.k();
        if (!this.f4276b.i()) {
            this.f4277c.stopScroll();
        }
        a(this.f4282h, 0, 0.0f, 0.0f);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        if (!this.f4276b.h()) {
            return false;
        }
        this.f4276b.m();
        VelocityTracker velocityTracker = this.f4278d;
        velocityTracker.computeCurrentVelocity(1000, this.f4279e);
        if (this.f4277c.fling((int) velocityTracker.getXVelocity(), (int) velocityTracker.getYVelocity())) {
            return true;
        }
        this.f4275a.p();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e(float f10) {
        if (!this.f4276b.h()) {
            return false;
        }
        float f11 = this.f4280f - f10;
        this.f4280f = f11;
        int round = Math.round(f11 - this.f4281g);
        this.f4281g += round;
        long uptimeMillis = SystemClock.uptimeMillis();
        boolean z10 = this.f4275a.getOrientation() == 0;
        int i10 = z10 ? round : 0;
        int i11 = z10 ? 0 : round;
        float f12 = z10 ? this.f4280f : 0.0f;
        float f13 = z10 ? 0.0f : this.f4280f;
        this.f4277c.scrollBy(i10, i11);
        a(uptimeMillis, 2, f12, f13);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f() {
        return this.f4276b.h();
    }
}
