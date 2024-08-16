package com.coui.appcompat.viewpager;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: COUIFakeDrag.java */
/* renamed from: com.coui.appcompat.viewpager.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUIFakeDrag {

    /* renamed from: a, reason: collision with root package name */
    private final COUIViewPager2 f8138a;

    /* renamed from: b, reason: collision with root package name */
    private final COUIScrollEventAdapter f8139b;

    /* renamed from: c, reason: collision with root package name */
    private final RecyclerView f8140c;

    /* renamed from: d, reason: collision with root package name */
    private VelocityTracker f8141d;

    /* renamed from: e, reason: collision with root package name */
    private int f8142e;

    /* renamed from: f, reason: collision with root package name */
    private float f8143f;

    /* renamed from: g, reason: collision with root package name */
    private int f8144g;

    /* renamed from: h, reason: collision with root package name */
    private long f8145h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUIFakeDrag(COUIViewPager2 cOUIViewPager2, COUIScrollEventAdapter cOUIScrollEventAdapter, RecyclerView recyclerView) {
        this.f8138a = cOUIViewPager2;
        this.f8139b = cOUIScrollEventAdapter;
        this.f8140c = recyclerView;
    }

    private void a(long j10, int i10, float f10, float f11) {
        MotionEvent obtain = MotionEvent.obtain(this.f8145h, j10, i10, f10, f11, 0);
        this.f8141d.addMovement(obtain);
        obtain.recycle();
    }

    private void c() {
        VelocityTracker velocityTracker = this.f8141d;
        if (velocityTracker == null) {
            this.f8141d = VelocityTracker.obtain();
            this.f8142e = ViewConfiguration.get(this.f8138a.getContext()).getScaledMaximumFlingVelocity();
        } else {
            velocityTracker.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b() {
        if (this.f8139b.g()) {
            return false;
        }
        this.f8144g = 0;
        this.f8143f = 0;
        this.f8145h = SystemClock.uptimeMillis();
        c();
        this.f8139b.k();
        if (!this.f8139b.i()) {
            this.f8140c.stopScroll();
        }
        a(this.f8145h, 0, 0.0f, 0.0f);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return this.f8139b.h();
    }
}
