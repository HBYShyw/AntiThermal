package p7;

import android.graphics.RectF;
import o7.Compat;
import o7.Vector;
import q7.Edge;
import r7.c;

/* compiled from: Body.java */
/* renamed from: p7.a, reason: use source file name */
/* loaded from: classes.dex */
public class Body {

    /* renamed from: a, reason: collision with root package name */
    public final Vector f16592a;

    /* renamed from: b, reason: collision with root package name */
    public final Vector f16593b;

    /* renamed from: c, reason: collision with root package name */
    public final Vector f16594c;

    /* renamed from: d, reason: collision with root package name */
    public final Vector f16595d;

    /* renamed from: e, reason: collision with root package name */
    public final Vector f16596e;

    /* renamed from: f, reason: collision with root package name */
    public final Vector f16597f;

    /* renamed from: g, reason: collision with root package name */
    public c f16598g;

    /* renamed from: h, reason: collision with root package name */
    public RectF f16599h;

    /* renamed from: i, reason: collision with root package name */
    public RectF f16600i;

    /* renamed from: j, reason: collision with root package name */
    public Body f16601j;

    /* renamed from: k, reason: collision with root package name */
    public Body f16602k;

    /* renamed from: l, reason: collision with root package name */
    public Edge f16603l;

    /* renamed from: m, reason: collision with root package name */
    public boolean f16604m;

    /* renamed from: n, reason: collision with root package name */
    public float f16605n;

    /* renamed from: o, reason: collision with root package name */
    public float f16606o;

    /* renamed from: p, reason: collision with root package name */
    public float f16607p;

    /* renamed from: q, reason: collision with root package name */
    public float f16608q;

    /* renamed from: r, reason: collision with root package name */
    public float f16609r;

    /* renamed from: s, reason: collision with root package name */
    public float f16610s;

    /* renamed from: t, reason: collision with root package name */
    public float f16611t;

    /* renamed from: u, reason: collision with root package name */
    public int f16612u;

    /* renamed from: v, reason: collision with root package name */
    public int f16613v;

    /* renamed from: w, reason: collision with root package name */
    boolean f16614w;

    /* renamed from: x, reason: collision with root package name */
    boolean f16615x;

    /* renamed from: y, reason: collision with root package name */
    private String f16616y;

    public Body(Vector vector, int i10, int i11, float f10, float f11) {
        Vector vector2 = new Vector();
        this.f16592a = vector2;
        this.f16593b = new Vector();
        this.f16594c = new Vector();
        this.f16595d = new Vector(0.0f, 0.0f);
        this.f16596e = new Vector();
        this.f16597f = new Vector();
        this.f16598g = null;
        this.f16604m = false;
        this.f16605n = 50.0f;
        this.f16614w = false;
        this.f16615x = false;
        this.f16616y = "";
        v(i10);
        s(i11);
        vector2.e(vector);
        this.f16608q = 1.0f;
        t(f10, f11);
        this.f16614w = true;
        this.f16603l = null;
        this.f16601j = null;
        this.f16602k = null;
    }

    private final void j() {
        if (this.f16612u == 0) {
            p(1.0f);
            n(0.0f);
            return;
        }
        p(this.f16606o * this.f16607p * this.f16608q);
        n(Compat.a(this.f16609r));
        if (!this.f16614w || this.f16613v == 1) {
            this.f16593b.d(this.f16606o * 0.5f, this.f16607p * 0.5f);
            this.f16594c.e(this.f16592a).a(this.f16593b);
        }
    }

    private final void p(float f10) {
        if (f10 < 1.0f) {
            f10 = 1.0f;
        }
        this.f16609r = f10;
        this.f16610s = 1.0f / f10;
    }

    private void s(int i10) {
        this.f16613v = i10;
    }

    private void v(int i10) {
        this.f16612u = i10;
    }

    public void a(c cVar) {
        RectF rectF = this.f16599h;
        if (rectF == null || rectF.isEmpty() || this.f16598g != cVar) {
            return;
        }
        this.f16599h = null;
        this.f16600i = null;
        k(50.0f);
    }

    public void b(c cVar) {
        c cVar2;
        RectF rectF = this.f16600i;
        if (rectF == null || (cVar2 = this.f16598g) == null || cVar2 != cVar) {
            return;
        }
        rectF.setEmpty();
    }

    public final Vector c() {
        return this.f16595d;
    }

    public final Vector d() {
        return this.f16596e;
    }

    public final float e() {
        return this.f16609r;
    }

    public final Vector f() {
        return this.f16592a;
    }

    public int g() {
        return this.f16613v;
    }

    public int h() {
        return this.f16612u;
    }

    public final Vector i() {
        return this.f16594c;
    }

    public void k(float f10) {
        this.f16605n = f10;
    }

    public void l(boolean z10) {
        this.f16604m = z10;
    }

    public final void m(float f10, float f11) {
        this.f16595d.d(Compat.d(f10), Compat.d(f11));
    }

    public final void n(float f10) {
        this.f16611t = f10;
    }

    public final void o(Vector vector) {
        if (this.f16612u == 0) {
            return;
        }
        this.f16596e.e(vector);
    }

    public void q(RectF rectF) {
        if (rectF == null || rectF.isEmpty()) {
            return;
        }
        if (this.f16599h == null) {
            this.f16599h = new RectF();
        }
        this.f16599h.set(Compat.d(rectF.left), Compat.d(rectF.top), Compat.d(rectF.right), Compat.d(rectF.bottom));
    }

    public final void r(Vector vector) {
        this.f16592a.e(vector);
        this.f16594c.e(vector).a(this.f16593b);
    }

    public void t(float f10, float f11) {
        this.f16606o = f10;
        this.f16607p = f11;
        j();
    }

    public String toString() {
        return "Body{mType=" + this.f16612u + ", mProperty=" + this.f16613v + ", mLinearVelocity=" + this.f16596e + ", mLinearDamping=" + this.f16611t + ", mPosition=" + this.f16592a + ", mHookPosition=" + this.f16595d + ", mTag='" + this.f16616y + "'}@" + hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(String str) {
        this.f16616y = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w() {
        Vector vector = this.f16592a;
        Vector vector2 = this.f16594c;
        float f10 = vector2.f16270a;
        Vector vector3 = this.f16593b;
        vector.d(f10 - vector3.f16270a, vector2.f16271b - vector3.f16271b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x() {
        c cVar;
        RectF rectF = this.f16600i;
        if (rectF == null || rectF.isEmpty() || (cVar = this.f16598g) == null || cVar.q() != 0) {
            return;
        }
        RectF rectF2 = this.f16600i;
        float f10 = rectF2.left;
        float f11 = rectF2.right;
        float f12 = rectF2.top;
        float f13 = rectF2.bottom;
        Vector vector = this.f16592a;
        float f14 = vector.f16270a;
        if (f14 < f10) {
            this.f16597f.f16270a = f10 - f14;
        } else if (f14 > f11) {
            this.f16597f.f16270a = f11 - f14;
        }
        float f15 = vector.f16271b;
        if (f15 < f12) {
            this.f16597f.f16271b = f12 - f15;
        } else if (f15 > f13) {
            this.f16597f.f16271b = f13 - f15;
        }
        float f16 = this.f16605n * 6.2831855f;
        this.f16597f.b(this.f16609r * f16 * f16 * 1.0f);
    }

    public boolean y(c cVar) {
        RectF rectF = this.f16599h;
        if (rectF == null || rectF.isEmpty()) {
            return false;
        }
        this.f16598g = cVar;
        if (this.f16600i == null) {
            this.f16600i = new RectF();
        }
        RectF rectF2 = this.f16600i;
        RectF rectF3 = this.f16599h;
        float f10 = rectF3.left;
        Vector vector = this.f16595d;
        float f11 = vector.f16270a;
        float f12 = rectF3.top;
        float f13 = vector.f16271b;
        rectF2.set(f10 + f11, f12 + f13, rectF3.right - (this.f16606o - f11), rectF3.bottom - (this.f16607p - f13));
        return true;
    }
}
