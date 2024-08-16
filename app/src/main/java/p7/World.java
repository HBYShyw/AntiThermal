package p7;

import o7.Vector;
import q7.Edge;
import q7.SpringDef;

/* compiled from: World.java */
/* renamed from: p7.b, reason: use source file name */
/* loaded from: classes.dex */
public class World {

    /* renamed from: a, reason: collision with root package name */
    private Body f16617a;

    /* renamed from: b, reason: collision with root package name */
    private q7.b f16618b;

    /* renamed from: c, reason: collision with root package name */
    private int f16619c;

    /* renamed from: d, reason: collision with root package name */
    private int f16620d;

    /* renamed from: e, reason: collision with root package name */
    private final Vector f16621e;

    public World() {
        this(new Vector());
    }

    private void e() {
        for (Body body = this.f16617a; body != null; body = body.f16602k) {
            o7.b.c("world has body ====>>> " + body);
        }
    }

    private void g(float f10) {
        for (Body body = this.f16617a; body != null; body = body.f16602k) {
            body.f16615x = false;
        }
        for (q7.b bVar = this.f16618b; bVar != null; bVar = bVar.f16911b) {
            bVar.f16914e = false;
        }
        for (Body body2 = this.f16617a; body2 != null; body2 = body2.f16602k) {
            if (!body2.f16615x && body2.f16604m && body2.h() != 0) {
                h(body2, f10);
                body2.f16615x = true;
                body2.f16597f.f();
            }
        }
    }

    private void h(Body body, float f10) {
        body.x();
        body.f16596e.a(body.f16597f.b(body.f16610s).b(f10));
        body.f16596e.b(1.0f / ((body.f16611t * f10) + 1.0f));
        for (Edge edge = body.f16603l; edge != null; edge = edge.f16909d) {
            q7.b bVar = edge.f16907b;
            if (!bVar.f16914e) {
                bVar.f16914e = true;
                Body body2 = edge.f16906a;
                if (!body2.f16615x && body2.f16604m) {
                    bVar.e(body, f10);
                    for (int i10 = 0; i10 < 4; i10++) {
                        edge.f16907b.j(body);
                    }
                }
            }
        }
        Vector vector = body.f16594c;
        float f11 = vector.f16270a;
        Vector vector2 = body.f16596e;
        vector.f16270a = f11 + (vector2.f16270a * f10);
        vector.f16271b += f10 * vector2.f16271b;
        body.w();
    }

    public Body a(Vector vector, int i10, int i11, float f10, float f11, String str) {
        Body body = new Body(vector, i10, i11, f10, f11);
        body.u(str);
        body.f16601j = null;
        Body body2 = this.f16617a;
        body.f16602k = body2;
        if (body2 != null) {
            body2.f16601j = body;
        }
        this.f16617a = body;
        this.f16619c++;
        if (o7.b.b()) {
            e();
        }
        return body;
    }

    public q7.b b(SpringDef springDef) {
        q7.b a10 = q7.b.a(this, springDef);
        if (a10 == null) {
            return null;
        }
        a10.f16910a = null;
        q7.b bVar = this.f16618b;
        a10.f16911b = bVar;
        if (bVar != null) {
            bVar.f16910a = a10;
        }
        this.f16618b = a10;
        this.f16620d++;
        Edge edge = a10.f16912c;
        edge.f16907b = a10;
        edge.f16906a = a10.c();
        Edge edge2 = a10.f16912c;
        edge2.f16908c = null;
        edge2.f16909d = a10.b().f16603l;
        if (a10.b().f16603l != null) {
            a10.b().f16603l.f16908c = a10.f16912c;
        }
        a10.b().f16603l = a10.f16912c;
        Edge edge3 = a10.f16913d;
        edge3.f16907b = a10;
        edge3.f16906a = a10.b();
        Edge edge4 = a10.f16913d;
        edge4.f16908c = null;
        edge4.f16909d = a10.c().f16603l;
        if (a10.c().f16603l != null) {
            a10.c().f16603l.f16908c = a10.f16913d;
        }
        a10.c().f16603l = a10.f16913d;
        return a10;
    }

    public void c(Body body) {
        if (this.f16619c <= 0) {
            return;
        }
        Edge edge = body.f16603l;
        while (edge != null) {
            Edge edge2 = edge.f16909d;
            q7.b bVar = edge.f16907b;
            if (bVar != null) {
                d(bVar);
            }
            body.f16603l = edge2;
            edge = edge2;
        }
        body.f16603l = null;
        Body body2 = body.f16601j;
        if (body2 != null) {
            body2.f16602k = body.f16602k;
        }
        Body body3 = body.f16602k;
        if (body3 != null) {
            body3.f16601j = body2;
        }
        if (body == this.f16617a) {
            this.f16617a = body3;
        }
        this.f16619c--;
    }

    public void d(q7.b bVar) {
        if (this.f16620d <= 0) {
            return;
        }
        q7.b bVar2 = bVar.f16910a;
        if (bVar2 != null) {
            bVar2.f16911b = bVar.f16911b;
        }
        q7.b bVar3 = bVar.f16911b;
        if (bVar3 != null) {
            bVar3.f16910a = bVar2;
        }
        if (bVar == this.f16618b) {
            this.f16618b = bVar3;
        }
        Body b10 = bVar.b();
        Body c10 = bVar.c();
        Edge edge = bVar.f16912c;
        Edge edge2 = edge.f16908c;
        if (edge2 != null) {
            edge2.f16909d = edge.f16909d;
        }
        Edge edge3 = edge.f16909d;
        if (edge3 != null) {
            edge3.f16908c = edge2;
        }
        if (edge == b10.f16603l) {
            b10.f16603l = edge3;
        }
        edge.f16908c = null;
        edge.f16909d = null;
        Edge edge4 = bVar.f16913d;
        Edge edge5 = edge4.f16908c;
        if (edge5 != null) {
            edge5.f16909d = edge4.f16909d;
        }
        Edge edge6 = edge4.f16909d;
        if (edge6 != null) {
            edge6.f16908c = edge5;
        }
        if (edge4 == c10.f16603l) {
            c10.f16603l = edge6;
        }
        edge4.f16908c = null;
        edge4.f16909d = null;
        this.f16620d--;
    }

    public Vector f() {
        return this.f16621e;
    }

    public void i(float f10) {
        g(f10);
    }

    public World(Vector vector) {
        this.f16621e = vector;
        this.f16617a = null;
        this.f16618b = null;
        this.f16619c = 0;
        this.f16620d = 0;
    }
}
