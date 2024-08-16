package r7;

import java.util.HashMap;
import o7.Compat;
import o7.Vector;
import p7.Body;
import q7.SpringDef;

/* compiled from: BaseBehavior.java */
/* loaded from: classes.dex */
public abstract class c {

    /* renamed from: f, reason: collision with root package name */
    protected HashMap<String, FloatPropertyHolder> f17550f;

    /* renamed from: h, reason: collision with root package name */
    protected Runnable f17552h;

    /* renamed from: i, reason: collision with root package name */
    protected Runnable f17553i;

    /* renamed from: j, reason: collision with root package name */
    protected UIItem f17554j;

    /* renamed from: k, reason: collision with root package name */
    protected Body f17555k;

    /* renamed from: l, reason: collision with root package name */
    protected SpringDef f17556l;

    /* renamed from: n, reason: collision with root package name */
    protected Object f17558n;

    /* renamed from: a, reason: collision with root package name */
    protected float f17545a = 1.0f;

    /* renamed from: b, reason: collision with root package name */
    protected boolean f17546b = false;

    /* renamed from: c, reason: collision with root package name */
    protected boolean f17547c = false;

    /* renamed from: d, reason: collision with root package name */
    protected boolean f17548d = false;

    /* renamed from: e, reason: collision with root package name */
    protected FloatPropertyHolder f17549e = null;

    /* renamed from: g, reason: collision with root package name */
    protected PhysicalAnimator f17551g = null;

    /* renamed from: m, reason: collision with root package name */
    protected q7.b f17557m = null;

    /* JADX INFO: Access modifiers changed from: protected */
    public c() {
        w();
    }

    private void E(UIItem uIItem, FloatPropertyHolder floatPropertyHolder) {
        floatPropertyHolder.e(uIItem);
    }

    private void H() {
        PhysicalAnimator physicalAnimator = this.f17551g;
        if (physicalAnimator != null && this.f17555k == null) {
            UIItem n10 = physicalAnimator.n(this.f17558n);
            this.f17554j = n10;
            PhysicalAnimator physicalAnimator2 = this.f17551g;
            FloatPropertyHolder floatPropertyHolder = this.f17549e;
            this.f17555k = physicalAnimator2.m(n10, floatPropertyHolder != null ? floatPropertyHolder.f17580a : 1);
            x();
            if (o7.b.b()) {
                o7.b.c("verifyBodyProperty : mActiveUIItem =:" + this.f17554j + ",mPropertyBody =:" + this.f17555k + ",this =:" + this);
            }
        }
    }

    private void a(FloatPropertyHolder floatPropertyHolder) {
        if (this.f17550f == null) {
            this.f17550f = new HashMap<>(1);
        }
        if (this.f17549e == null) {
            this.f17549e = floatPropertyHolder;
            H();
        }
        this.f17550f.put(floatPropertyHolder.f17581b, floatPropertyHolder);
        this.f17545a = o7.d.b(this.f17545a, floatPropertyHolder.f17582c);
    }

    private Body i(Vector vector, int i10, int i11, float f10, float f11, String str) {
        return this.f17551g.f(vector, i10, i11, f10, f11, str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void A() {
        if (this.f17547c) {
            return;
        }
        G();
        F();
        v();
        m();
        this.f17551g.A(this);
        this.f17551g.w(this);
        this.f17547c = true;
        Runnable runnable = this.f17552h;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean B() {
        if (!this.f17547c) {
            return false;
        }
        if (q() != 0) {
            this.f17554j.f17607g.f();
        }
        this.f17551g.y(this);
        this.f17547c = false;
        Runnable runnable = this.f17553i;
        if (runnable == null) {
            return true;
        }
        runnable.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void C(Body body, Vector vector) {
        body.r(vector);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D() {
        HashMap<String, FloatPropertyHolder> hashMap = this.f17550f;
        if (hashMap == null) {
            return;
        }
        for (FloatPropertyHolder floatPropertyHolder : hashMap.values()) {
            if (floatPropertyHolder != null) {
                E(this.f17554j, floatPropertyHolder);
            }
        }
    }

    protected void F() {
        HashMap<String, FloatPropertyHolder> hashMap = this.f17550f;
        if (hashMap == null) {
            UIItem uIItem = this.f17554j;
            uIItem.c(uIItem.a().f17597a, this.f17554j.a().f17598b);
            return;
        }
        for (FloatPropertyHolder floatPropertyHolder : hashMap.values()) {
            if (floatPropertyHolder != null) {
                floatPropertyHolder.f(this.f17554j);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void G() {
        if (this.f17548d) {
            this.f17548d = false;
            this.f17555k.d().d(Compat.d(this.f17554j.f17607g.f16270a), Compat.d(this.f17554j.f17607g.f16271b));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends c> T I(FloatPropertyHolder... floatPropertyHolderArr) {
        for (FloatPropertyHolder floatPropertyHolder : floatPropertyHolderArr) {
            a(floatPropertyHolder);
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends c> T b(Object obj) {
        this.f17558n = obj;
        H();
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c c(PhysicalAnimator physicalAnimator) {
        this.f17551g = physicalAnimator;
        H();
        u(this.f17551g.l());
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Body d(String str, Body body) {
        if (body == null) {
            Body body2 = this.f17555k;
            Vector vector = body2.f16592a;
            int h10 = body2.h();
            int g6 = this.f17555k.g();
            Body body3 = this.f17555k;
            body = i(vector, h10, g6, body3.f16606o, body3.f16607p, str);
        } else {
            Body body4 = this.f17555k;
            body.t(body4.f16606o, body4.f16607p);
        }
        body.o(this.f17555k.d());
        body.l(false);
        return body;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean e(SpringDef springDef) {
        if (this.f17546b) {
            return false;
        }
        q7.b f10 = f(springDef, this.f17555k);
        this.f17557m = f10;
        if (f10 == null) {
            return false;
        }
        this.f17546b = true;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public q7.b f(SpringDef springDef, Body body) {
        if (springDef == null || body == null) {
            return null;
        }
        springDef.f16931c.e(body.i());
        return this.f17551g.g(springDef);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void g() {
        h(4.0f, 0.2f);
    }

    protected void h(float f10, float f11) {
        SpringDef springDef = new SpringDef();
        this.f17556l = springDef;
        springDef.f16933e = 4.0f;
        springDef.f16934f = 0.2f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean j(Body body) {
        return this.f17551g.j(body);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean k() {
        if (!this.f17546b) {
            return false;
        }
        l(this.f17557m);
        this.f17557m = null;
        this.f17546b = false;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void l(q7.b bVar) {
        this.f17551g.k(bVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m() {
        this.f17554j.f(Compat.c(this.f17555k.f().f16270a - this.f17555k.c().f16270a), Compat.c(this.f17555k.f().f16271b - this.f17555k.c().f16271b));
    }

    public Object n() {
        FloatPropertyHolder floatPropertyHolder = this.f17549e;
        if (floatPropertyHolder == null) {
            if (p() != null) {
                return Float.valueOf(p().f17597a);
            }
            return null;
        }
        return Float.valueOf(o(this.f17554j, floatPropertyHolder));
    }

    protected float o(Object obj, FloatPropertyHolder floatPropertyHolder) {
        return floatPropertyHolder.a(obj);
    }

    public Transform p() {
        UIItem uIItem = this.f17554j;
        if (uIItem != null) {
            return uIItem.a();
        }
        return null;
    }

    public abstract int q();

    protected boolean r(Vector vector) {
        q7.b bVar = this.f17557m;
        if (bVar != null) {
            return Compat.b(o7.d.a(bVar.d().f16270a - vector.f16270a) + o7.d.a(this.f17557m.d().f16271b - vector.f16271b));
        }
        return true;
    }

    public boolean s() {
        return t(this.f17555k.f16596e) && r(this.f17555k.f());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean t(Vector vector) {
        return Compat.b(o7.d.a(vector.f16270a)) && Compat.b(o7.d.a(vector.f16271b));
    }

    public String toString() {
        return "Behavior{type=" + q() + ", mValueThreshold=" + this.f17545a + ", mTarget=" + this.f17558n + ", mPropertyBody=" + this.f17555k + "}@" + hashCode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void u(Body body) {
        SpringDef springDef = this.f17556l;
        if (springDef != null) {
            springDef.f16929a = body;
            body.l(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void v() {
        UIItem uIItem = this.f17554j;
        uIItem.f17604d.d((Compat.d(uIItem.f17605e.f16270a) + this.f17555k.c().f16270a) / this.f17545a, (Compat.d(this.f17554j.f17605e.f16271b) + this.f17555k.c().f16271b) / this.f17545a);
        C(this.f17555k, this.f17554j.f17604d);
    }

    protected void w() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void x() {
        SpringDef springDef = this.f17556l;
        if (springDef != null) {
            springDef.f16930b = this.f17555k;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y() {
        if (o7.b.b()) {
            o7.b.c("onRemove mIsStarted =:" + this.f17547c + ",this =:" + this);
        }
        this.f17553i = null;
        B();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends c> T z(float f10, float f11) {
        SpringDef springDef = this.f17556l;
        if (springDef != null) {
            springDef.f16933e = f10;
            springDef.f16934f = f11;
            q7.b bVar = this.f17557m;
            if (bVar != null) {
                bVar.g(f10);
                this.f17557m.f(f11);
            }
        }
        return this;
    }
}
