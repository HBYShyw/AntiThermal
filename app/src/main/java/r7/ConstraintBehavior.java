package r7;

import android.graphics.RectF;
import o7.Vector;
import p7.Body;
import q7.SpringDef;

/* compiled from: ConstraintBehavior.java */
/* renamed from: r7.f, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ConstraintBehavior extends c {

    /* renamed from: p, reason: collision with root package name */
    protected Body f17565p;

    /* renamed from: v, reason: collision with root package name */
    protected int f17571v;

    /* renamed from: o, reason: collision with root package name */
    protected final RectF f17564o = new RectF();

    /* renamed from: q, reason: collision with root package name */
    protected boolean f17566q = false;

    /* renamed from: r, reason: collision with root package name */
    protected boolean f17567r = false;

    /* renamed from: s, reason: collision with root package name */
    protected float f17568s = 0.0f;

    /* renamed from: t, reason: collision with root package name */
    protected float f17569t = 0.0f;

    /* renamed from: u, reason: collision with root package name */
    protected int f17570u = 0;

    public ConstraintBehavior(int i10, RectF rectF) {
        this.f17571v = i10;
        d0(rectF);
        if (S()) {
            SpringDef springDef = new SpringDef();
            this.f17556l = springDef;
            springDef.f16933e = 1.0f;
            springDef.f16934f = 0.4f;
        }
    }

    private void L() {
        if (e(this.f17556l)) {
            this.f17557m.h(this.f17568s, this.f17569t);
        }
    }

    private void M() {
        k();
        c0();
    }

    private boolean Q() {
        return this.f17571v == 1;
    }

    private boolean R() {
        return this.f17571v == 3;
    }

    private boolean S() {
        return Q() || R() || T();
    }

    private boolean T() {
        return this.f17571v == 2;
    }

    private void c0() {
        this.f17570u = 0;
        this.f17566q = false;
        this.f17567r = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void A() {
        super.A();
        b0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public boolean B() {
        this.f17555k.b(this);
        if (S()) {
            M();
            this.f17565p.l(false);
        }
        return super.B();
    }

    protected void J() {
        this.f17566q = Z();
        this.f17567r = a0();
        this.f17568s = N(this.f17555k.f().f16270a);
        this.f17569t = O(this.f17555k.f().f16271b);
    }

    protected void K(float f10, float f11) {
        this.f17570u = 0;
        RectF rectF = this.f17555k.f16600i;
        if (rectF != null) {
            if (this.f17547c || !rectF.isEmpty()) {
                RectF rectF2 = this.f17555k.f16600i;
                if (f10 < rectF2.left) {
                    this.f17570u |= 1;
                } else if (f10 > rectF2.right) {
                    this.f17570u |= 4;
                }
                if (f11 < rectF2.top) {
                    this.f17570u |= 2;
                } else if (f11 > rectF2.bottom) {
                    this.f17570u |= 8;
                }
            }
        }
    }

    protected float N(float f10) {
        RectF rectF = this.f17555k.f16600i;
        if (rectF != null && (this.f17547c || !rectF.isEmpty())) {
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

    protected float O(float f10) {
        RectF rectF = this.f17555k.f16600i;
        if (rectF != null && (this.f17547c || !rectF.isEmpty())) {
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

    protected void P() {
        int i10 = this.f17571v;
        if (i10 == 0) {
            this.f17554j.f17604d.e(this.f17555k.f());
            C(this.f17555k, this.f17554j.f17604d);
            return;
        }
        if (i10 == 1) {
            this.f17554j.f17604d.e(this.f17555k.f());
            if (!this.f17566q) {
                this.f17568s = N(this.f17554j.f17604d.f16270a);
            } else {
                this.f17554j.f17604d.f16270a = this.f17565p.f().f16270a;
            }
            if (Z()) {
                this.f17566q = true;
            }
            if (!this.f17567r) {
                this.f17569t = O(this.f17554j.f17604d.f16271b);
            } else {
                this.f17554j.f17604d.f16271b = this.f17565p.f().f16271b;
            }
            if (a0()) {
                this.f17567r = true;
            }
            e0(this.f17554j.f17604d);
            return;
        }
        if (i10 == 2) {
            if (!this.f17566q && !this.f17567r) {
                if (V()) {
                    Body body = this.f17555k;
                    body.o(body.d().b(0.5f).c());
                }
                this.f17554j.f17604d.d(N(this.f17555k.f().f16270a), O(this.f17555k.f().f16271b));
                this.f17568s = N(this.f17554j.f17604d.f16270a);
                this.f17569t = O(this.f17554j.f17604d.f16271b);
            } else {
                this.f17554j.f17604d.e(this.f17565p.f());
            }
            e0(this.f17554j.f17604d);
            return;
        }
        if (i10 != 3) {
            return;
        }
        if (!this.f17566q && !this.f17567r) {
            if (V()) {
                this.f17555k.d().f();
            }
            this.f17554j.f17604d.d(N(this.f17555k.f().f16270a), O(this.f17555k.f().f16271b));
            this.f17568s = N(this.f17554j.f17604d.f16270a);
            this.f17569t = O(this.f17554j.f17604d.f16271b);
        } else {
            this.f17554j.f17604d.e(this.f17565p.f());
        }
        e0(this.f17554j.f17604d);
    }

    protected boolean U() {
        return (this.f17570u & 8) != 0;
    }

    protected boolean V() {
        return this.f17570u != 0;
    }

    protected boolean W() {
        return (this.f17570u & 1) != 0;
    }

    protected boolean X() {
        return (this.f17570u & 4) != 0;
    }

    protected boolean Y() {
        return (this.f17570u & 2) != 0;
    }

    protected boolean Z() {
        return W() || X();
    }

    protected boolean a0() {
        return Y() || U();
    }

    protected void b0() {
        if (this.f17555k.y(this) && S()) {
            K(this.f17555k.f().f16270a, this.f17555k.f().f16271b);
            J();
            this.f17565p.l(true);
            this.f17565p.o(this.f17555k.d());
            C(this.f17565p, this.f17555k.f());
            L();
        }
    }

    public void d0(RectF rectF) {
        if (rectF == null || rectF.isEmpty()) {
            return;
        }
        this.f17564o.set(rectF);
        Body body = this.f17555k;
        if (body != null) {
            body.q(this.f17564o);
            this.f17555k.y(this);
        }
    }

    protected void e0(Vector vector) {
        C(this.f17555k, vector);
        q7.b bVar = this.f17557m;
        if (bVar != null) {
            bVar.h(this.f17568s, this.f17569t);
            C(this.f17565p, vector);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void m() {
        Body body = this.f17555k;
        if (body.f16600i != null) {
            K(body.f().f16270a, this.f17555k.f().f16271b);
        }
        P();
        super.m();
    }

    @Override // r7.c
    public boolean s() {
        if (S()) {
            return super.s();
        }
        return t(this.f17555k.f16596e);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void u(Body body) {
        if (S()) {
            super.u(body);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void v() {
        super.v();
        Body body = this.f17565p;
        if (body != null) {
            C(body, this.f17554j.f17604d);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // r7.c
    public void x() {
        RectF rectF = this.f17564o;
        if (rectF != null && !rectF.isEmpty()) {
            this.f17555k.q(this.f17564o);
            this.f17555k.y(this);
            if (S()) {
                Body body = this.f17555k;
                if (body.f16605n == 50.0f) {
                    body.k(this.f17556l.f16933e);
                }
            }
        }
        if (this.f17556l != null) {
            Body d10 = d("Assist", this.f17565p);
            this.f17565p = d10;
            this.f17556l.f16930b = d10;
        }
    }

    @Override // r7.c
    public void y() {
        super.y();
        this.f17555k.a(this);
        if (S()) {
            M();
            j(this.f17565p);
        }
    }

    @Override // r7.c
    public <T extends c> T z(float f10, float f11) {
        if (this.f17555k != null && S()) {
            Body body = this.f17555k;
            if (body.f16605n == 50.0f) {
                body.k(f10);
            }
        }
        return (T) super.z(f10, f11);
    }
}
