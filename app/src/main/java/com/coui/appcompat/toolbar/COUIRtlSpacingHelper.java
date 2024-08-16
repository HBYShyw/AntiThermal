package com.coui.appcompat.toolbar;

/* compiled from: COUIRtlSpacingHelper.java */
/* renamed from: com.coui.appcompat.toolbar.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIRtlSpacingHelper {

    /* renamed from: a, reason: collision with root package name */
    private int f8020a = 0;

    /* renamed from: b, reason: collision with root package name */
    private int f8021b = 0;

    /* renamed from: c, reason: collision with root package name */
    private int f8022c = Integer.MIN_VALUE;

    /* renamed from: d, reason: collision with root package name */
    private int f8023d = Integer.MIN_VALUE;

    /* renamed from: e, reason: collision with root package name */
    private int f8024e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f8025f = 0;

    /* renamed from: g, reason: collision with root package name */
    private boolean f8026g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f8027h = false;

    public int a() {
        return this.f8026g ? this.f8020a : this.f8021b;
    }

    public int b() {
        return this.f8020a;
    }

    public int c() {
        return this.f8021b;
    }

    public int d() {
        return this.f8026g ? this.f8021b : this.f8020a;
    }

    public void e(int i10, int i11) {
        this.f8027h = false;
        if (i10 != Integer.MIN_VALUE) {
            this.f8024e = i10;
            this.f8020a = i10;
        }
        if (i11 != Integer.MIN_VALUE) {
            this.f8025f = i11;
            this.f8021b = i11;
        }
    }

    public void f(boolean z10) {
        if (z10 == this.f8026g) {
            return;
        }
        this.f8026g = z10;
        if (!this.f8027h) {
            this.f8020a = this.f8024e;
            this.f8021b = this.f8025f;
            return;
        }
        if (z10) {
            int i10 = this.f8023d;
            if (i10 == Integer.MIN_VALUE) {
                i10 = this.f8024e;
            }
            this.f8020a = i10;
            int i11 = this.f8022c;
            if (i11 == Integer.MIN_VALUE) {
                i11 = this.f8025f;
            }
            this.f8021b = i11;
            return;
        }
        int i12 = this.f8022c;
        if (i12 == Integer.MIN_VALUE) {
            i12 = this.f8024e;
        }
        this.f8020a = i12;
        int i13 = this.f8023d;
        if (i13 == Integer.MIN_VALUE) {
            i13 = this.f8025f;
        }
        this.f8021b = i13;
    }

    public void g(int i10, int i11) {
        this.f8022c = i10;
        this.f8023d = i11;
        this.f8027h = true;
        if (this.f8026g) {
            if (i11 != Integer.MIN_VALUE) {
                this.f8020a = i11;
            }
            if (i10 != Integer.MIN_VALUE) {
                this.f8021b = i10;
                return;
            }
            return;
        }
        if (i10 != Integer.MIN_VALUE) {
            this.f8020a = i10;
        }
        if (i11 != Integer.MIN_VALUE) {
            this.f8021b = i11;
        }
    }
}
