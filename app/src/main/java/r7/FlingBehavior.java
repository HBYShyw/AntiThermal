package r7;

import android.graphics.RectF;
import o7.Compat;
import p7.Body;

/* compiled from: FlingBehavior.java */
/* renamed from: r7.h, reason: use source file name */
/* loaded from: classes.dex */
public class FlingBehavior extends ConstraintBehavior {

    /* renamed from: w, reason: collision with root package name */
    private float f17577w;

    /* renamed from: x, reason: collision with root package name */
    private float f17578x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f17579y;

    public FlingBehavior() {
        this(0, (RectF) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.ConstraintBehavior, r7.c
    public void A() {
        super.A();
        float f10 = this.f17578x;
        if (f10 != 0.0f) {
            Body body = this.f17555k;
            this.f17577w = body.f16611t;
            body.n(f10);
            Body body2 = this.f17565p;
            if (body2 != null) {
                body2.n(this.f17578x);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.ConstraintBehavior, r7.c
    public boolean B() {
        float f10 = this.f17577w;
        if (f10 != 0.0f) {
            this.f17555k.n(f10);
            Body body = this.f17565p;
            if (body != null) {
                body.n(this.f17577w);
            }
        }
        return super.B();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void G() {
        if (this.f17579y) {
            return;
        }
        super.G();
    }

    public void f0(float f10, float f11) {
        g0(new RectF(f10, f10, f11, f11));
    }

    public void g0(RectF rectF) {
        super.d0(rectF);
    }

    public FlingBehavior h0(float f10) {
        this.f17578x = f10;
        return this;
    }

    public void i0() {
        A();
    }

    public void j0(float f10) {
        k0(f10, 0.0f);
    }

    public void k0(float f10, float f11) {
        if (o7.b.b()) {
            o7.b.c("FlingBehavior : Fling : start : xVel =:" + f10 + ",yVel =:" + f11);
        }
        this.f17579y = true;
        this.f17555k.d().d(Compat.d(f10), Compat.d(f11));
        i0();
        this.f17579y = false;
    }

    public void l0() {
        B();
    }

    @Override // r7.c
    public int q() {
        return 2;
    }

    public FlingBehavior(float f10, float f11) {
        this(3, f10, f11);
    }

    public FlingBehavior(int i10, float f10, float f11) {
        this(i10, new RectF(f10, f10, f11, f11));
    }

    public FlingBehavior(int i10, RectF rectF) {
        super(i10, rectF);
        this.f17577w = 0.0f;
        this.f17578x = 0.0f;
        this.f17579y = false;
    }
}
