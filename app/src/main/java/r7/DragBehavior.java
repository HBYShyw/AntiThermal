package r7;

import android.graphics.RectF;
import o7.Compat;
import o7.Vector;
import p7.Body;
import q7.SpringDef;

/* compiled from: DragBehavior.java */
/* renamed from: r7.g, reason: use source file name */
/* loaded from: classes.dex */
public class DragBehavior extends c {

    /* renamed from: o, reason: collision with root package name */
    private Body f17572o;

    /* renamed from: p, reason: collision with root package name */
    private SpringDef f17573p;

    /* renamed from: q, reason: collision with root package name */
    private q7.b f17574q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f17575r = false;

    /* renamed from: s, reason: collision with root package name */
    private boolean f17576s = true;

    public DragBehavior() {
        g();
        SpringDef springDef = new SpringDef();
        this.f17573p = springDef;
        springDef.f16933e = 2000000.0f;
        springDef.f16934f = 100.0f;
    }

    private void L() {
        if (e(this.f17556l)) {
            this.f17557m.i(this.f17554j.f17604d);
            q7.b f10 = f(this.f17573p, this.f17572o);
            this.f17574q = f10;
            if (f10 != null) {
                f10.i(this.f17554j.f17604d);
                this.f17572o.l(true);
            }
        }
    }

    private void M() {
        if (k()) {
            l(this.f17574q);
            this.f17572o.l(false);
        }
    }

    private void N(float f10, float f11) {
        if (o7.b.b()) {
            o7.b.c("DragBehavior : dragTo : x =:" + f10 + ",y =:" + f11);
        }
        if (this.f17557m != null) {
            this.f17554j.f17604d.d(Q(Compat.d(f10)), R(Compat.d(f11)));
            this.f17557m.i(this.f17554j.f17604d);
            q7.b bVar = this.f17574q;
            if (bVar != null) {
                bVar.i(this.f17554j.f17604d);
            }
        }
    }

    private void U(Vector vector) {
        C(this.f17555k, vector);
        Body body = this.f17572o;
        if (body != null) {
            C(body, vector);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void A() {
        super.A();
        L();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public boolean B() {
        M();
        return super.B();
    }

    public void J(float f10, float f11) {
        K(f10, 0.0f, f11, 0.0f);
    }

    public void K(float f10, float f11, float f12, float f13) {
        if (o7.b.b()) {
            o7.b.c("DragBehavior : beginDrag : x =:" + f10 + ",y =:" + f11 + ",currentX =:" + f12 + ",currentY =:" + f13);
        }
        this.f17555k.m(f10 - f12, f11 - f13);
        this.f17555k.y(this);
        this.f17555k.f16596e.f();
        Body body = this.f17572o;
        if (body != null) {
            body.f16596e.f();
        }
        this.f17554j.f17604d.d(Q(Compat.d(f10)), R(Compat.d(f11)));
        U(this.f17554j.f17604d);
        this.f17575r = true;
        A();
    }

    public void O(float f10) {
        P(f10, 0.0f);
    }

    public void P(float f10, float f11) {
        if (o7.b.b()) {
            o7.b.c("DragBehavior : endDrag : xVel =:" + f10 + ",yVel =:" + f11);
        }
        M();
        Body body = this.f17572o;
        if (body != null) {
            Vector vector = body.f16596e;
            float f12 = vector.f16270a;
            f10 = f12 == 0.0f ? 0.0f : (f12 / o7.d.a(f12)) * o7.d.a(f10);
            float f13 = vector.f16271b;
            f11 = f13 == 0.0f ? 0.0f : o7.d.a(f11) * (f13 / o7.d.a(f13));
        }
        this.f17554j.e(f10, f11);
        this.f17575r = false;
        this.f17555k.b(this);
    }

    protected float Q(float f10) {
        RectF rectF;
        if (!this.f17576s && (rectF = this.f17555k.f16600i) != null && (this.f17547c || !rectF.isEmpty())) {
            RectF rectF2 = this.f17555k.f16600i;
            float f11 = rectF2.left;
            if (f10 < f11) {
                return f11;
            }
            float f12 = rectF2.right;
            if (f10 > f12) {
                return f12;
            }
        }
        return f10;
    }

    protected float R(float f10) {
        RectF rectF;
        if (!this.f17576s && (rectF = this.f17555k.f16600i) != null && (this.f17547c || !rectF.isEmpty())) {
            RectF rectF2 = this.f17555k.f16600i;
            float f11 = rectF2.top;
            if (f10 < f11) {
                return f11;
            }
            float f12 = rectF2.bottom;
            if (f10 > f12) {
                return f12;
            }
        }
        return f10;
    }

    public boolean S() {
        return this.f17575r;
    }

    public void T(float f10) {
        N(f10, 0.0f);
    }

    @Override // r7.c
    public int q() {
        return 0;
    }

    @Override // r7.c
    public boolean s() {
        return !this.f17575r;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void u(Body body) {
        super.u(body);
        SpringDef springDef = this.f17573p;
        if (springDef != null) {
            springDef.f16929a = body;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void v() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void x() {
        super.x();
        this.f17555k.k(this.f17556l.f16933e);
        if (this.f17573p != null) {
            Body d10 = d("SimulateTouch", this.f17572o);
            this.f17572o = d10;
            this.f17573p.f16930b = d10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // r7.c
    public void y() {
        super.y();
        Body body = this.f17572o;
        if (body != null) {
            j(body);
        }
    }

    @Override // r7.c
    public <T extends c> T z(float f10, float f11) {
        Body body = this.f17555k;
        if (body != null) {
            body.k(f10);
        }
        return (T) super.z(f10, f11);
    }
}
