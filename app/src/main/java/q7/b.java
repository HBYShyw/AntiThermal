package q7;

import o7.Mat22;
import o7.Vector;
import p7.Body;
import p7.World;

/* compiled from: Spring.java */
/* loaded from: classes.dex */
public class b {

    /* renamed from: c, reason: collision with root package name */
    public Edge f16912c;

    /* renamed from: d, reason: collision with root package name */
    public Edge f16913d;

    /* renamed from: e, reason: collision with root package name */
    public boolean f16914e;

    /* renamed from: f, reason: collision with root package name */
    private float f16915f;

    /* renamed from: g, reason: collision with root package name */
    private float f16916g;

    /* renamed from: i, reason: collision with root package name */
    private float f16918i;

    /* renamed from: k, reason: collision with root package name */
    private float f16920k;

    /* renamed from: l, reason: collision with root package name */
    private Body f16921l;

    /* renamed from: m, reason: collision with root package name */
    private Body f16922m;

    /* renamed from: n, reason: collision with root package name */
    private final Vector f16923n;

    /* renamed from: o, reason: collision with root package name */
    private final Vector f16924o;

    /* renamed from: p, reason: collision with root package name */
    private final Vector f16925p;

    /* renamed from: q, reason: collision with root package name */
    private final Vector f16926q;

    /* renamed from: r, reason: collision with root package name */
    private final Vector f16927r;

    /* renamed from: s, reason: collision with root package name */
    private final Mat22 f16928s;

    /* renamed from: a, reason: collision with root package name */
    public b f16910a = null;

    /* renamed from: b, reason: collision with root package name */
    public b f16911b = null;

    /* renamed from: h, reason: collision with root package name */
    private float f16917h = 0.0f;

    /* renamed from: j, reason: collision with root package name */
    private float f16919j = 0.0f;

    private b(Vector vector, SpringDef springDef) {
        Vector vector2 = new Vector();
        this.f16923n = vector2;
        this.f16924o = new Vector();
        Vector vector3 = new Vector();
        this.f16925p = vector3;
        this.f16926q = new Vector();
        this.f16928s = new Mat22();
        this.f16927r = vector;
        this.f16921l = springDef.f16929a;
        this.f16922m = springDef.f16930b;
        this.f16914e = false;
        this.f16912c = new Edge();
        this.f16913d = new Edge();
        if (springDef.f16933e < 0.0f || springDef.f16932d < 0.0f || springDef.f16934f < 0.0f) {
            return;
        }
        vector3.e(springDef.f16931c);
        vector2.e(vector3).g(this.f16922m.f());
        this.f16918i = springDef.f16932d;
        this.f16915f = springDef.f16933e;
        this.f16916g = springDef.f16934f;
    }

    public static b a(World world, SpringDef springDef) {
        return new b(world.f(), springDef);
    }

    public final Body b() {
        return this.f16921l;
    }

    public final Body c() {
        return this.f16922m;
    }

    public Vector d() {
        return this.f16925p;
    }

    public void e(Body body, float f10) {
        this.f16920k = body.f16610s;
        float f11 = this.f16915f * 6.2831855f;
        float e10 = body.e() * 2.0f * this.f16916g * f11;
        float e11 = body.e() * f11 * f11 * f10;
        float f12 = e10 + e11;
        if (f12 > 1.1920929E-7f) {
            this.f16919j = f10 * f12;
        }
        float f13 = this.f16919j;
        if (f13 != 0.0f) {
            this.f16919j = 1.0f / f13;
        }
        float f14 = this.f16919j;
        this.f16917h = e11 * f14;
        Mat22 mat22 = this.f16928s;
        Vector vector = mat22.f16268a;
        float f15 = this.f16920k;
        vector.f16270a = f15 + f14;
        mat22.f16269b.f16271b = f15 + f14;
        mat22.a();
        this.f16924o.e(body.f16594c).g(this.f16923n).g(this.f16925p).b(this.f16917h);
        Vector vector2 = body.f16596e;
        float f16 = vector2.f16270a;
        float f17 = this.f16920k;
        Vector vector3 = this.f16926q;
        vector2.f16270a = f16 + (vector3.f16270a * f17);
        vector2.f16271b += f17 * vector3.f16271b;
    }

    public void f(float f10) {
        this.f16916g = f10;
    }

    public void g(float f10) {
        this.f16915f = f10;
    }

    public void h(float f10, float f11) {
        Vector vector = this.f16925p;
        vector.f16270a = f10;
        vector.f16271b = f11;
    }

    public void i(Vector vector) {
        this.f16925p.e(vector);
    }

    public void j(Body body) {
        this.f16927r.e(this.f16926q);
        this.f16927r.b(this.f16919j).a(this.f16924o).a(body.f16596e).c();
        Mat22 mat22 = this.f16928s;
        Vector vector = this.f16927r;
        Mat22.b(mat22, vector, vector);
        this.f16926q.a(this.f16927r);
        body.f16596e.a(this.f16927r.b(this.f16920k));
    }
}
