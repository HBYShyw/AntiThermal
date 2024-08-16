package c4;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import b4.ShadowRenderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: ShapePath.java */
/* loaded from: classes.dex */
public class o {

    /* renamed from: a, reason: collision with root package name */
    @Deprecated
    public float f4858a;

    /* renamed from: b, reason: collision with root package name */
    @Deprecated
    public float f4859b;

    /* renamed from: c, reason: collision with root package name */
    @Deprecated
    public float f4860c;

    /* renamed from: d, reason: collision with root package name */
    @Deprecated
    public float f4861d;

    /* renamed from: e, reason: collision with root package name */
    @Deprecated
    public float f4862e;

    /* renamed from: f, reason: collision with root package name */
    @Deprecated
    public float f4863f;

    /* renamed from: g, reason: collision with root package name */
    private final List<f> f4864g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    private final List<g> f4865h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private boolean f4866i;

    /* compiled from: ShapePath.java */
    /* loaded from: classes.dex */
    class a extends g {

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ List f4867b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ Matrix f4868c;

        a(List list, Matrix matrix) {
            this.f4867b = list;
            this.f4868c = matrix;
        }

        @Override // c4.o.g
        public void a(Matrix matrix, ShadowRenderer shadowRenderer, int i10, Canvas canvas) {
            Iterator it = this.f4867b.iterator();
            while (it.hasNext()) {
                ((g) it.next()).a(this.f4868c, shadowRenderer, i10, canvas);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ShapePath.java */
    /* loaded from: classes.dex */
    public static class b extends g {

        /* renamed from: b, reason: collision with root package name */
        private final d f4870b;

        public b(d dVar) {
            this.f4870b = dVar;
        }

        @Override // c4.o.g
        public void a(Matrix matrix, ShadowRenderer shadowRenderer, int i10, Canvas canvas) {
            shadowRenderer.a(canvas, matrix, new RectF(this.f4870b.k(), this.f4870b.o(), this.f4870b.l(), this.f4870b.j()), i10, this.f4870b.m(), this.f4870b.n());
        }
    }

    /* compiled from: ShapePath.java */
    /* loaded from: classes.dex */
    static class c extends g {

        /* renamed from: b, reason: collision with root package name */
        private final e f4871b;

        /* renamed from: c, reason: collision with root package name */
        private final float f4872c;

        /* renamed from: d, reason: collision with root package name */
        private final float f4873d;

        public c(e eVar, float f10, float f11) {
            this.f4871b = eVar;
            this.f4872c = f10;
            this.f4873d = f11;
        }

        @Override // c4.o.g
        public void a(Matrix matrix, ShadowRenderer shadowRenderer, int i10, Canvas canvas) {
            RectF rectF = new RectF(0.0f, 0.0f, (float) Math.hypot(this.f4871b.f4882c - this.f4873d, this.f4871b.f4881b - this.f4872c), 0.0f);
            Matrix matrix2 = new Matrix(matrix);
            matrix2.preTranslate(this.f4872c, this.f4873d);
            matrix2.preRotate(c());
            shadowRenderer.b(canvas, matrix2, rectF, i10);
        }

        float c() {
            return (float) Math.toDegrees(Math.atan((this.f4871b.f4882c - this.f4873d) / (this.f4871b.f4881b - this.f4872c)));
        }
    }

    /* compiled from: ShapePath.java */
    /* loaded from: classes.dex */
    public static class d extends f {

        /* renamed from: h, reason: collision with root package name */
        private static final RectF f4874h = new RectF();

        /* renamed from: b, reason: collision with root package name */
        @Deprecated
        public float f4875b;

        /* renamed from: c, reason: collision with root package name */
        @Deprecated
        public float f4876c;

        /* renamed from: d, reason: collision with root package name */
        @Deprecated
        public float f4877d;

        /* renamed from: e, reason: collision with root package name */
        @Deprecated
        public float f4878e;

        /* renamed from: f, reason: collision with root package name */
        @Deprecated
        public float f4879f;

        /* renamed from: g, reason: collision with root package name */
        @Deprecated
        public float f4880g;

        public d(float f10, float f11, float f12, float f13) {
            q(f10);
            u(f11);
            r(f12);
            p(f13);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float j() {
            return this.f4878e;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float k() {
            return this.f4875b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float l() {
            return this.f4877d;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float m() {
            return this.f4879f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float n() {
            return this.f4880g;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float o() {
            return this.f4876c;
        }

        private void p(float f10) {
            this.f4878e = f10;
        }

        private void q(float f10) {
            this.f4875b = f10;
        }

        private void r(float f10) {
            this.f4877d = f10;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void s(float f10) {
            this.f4879f = f10;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void t(float f10) {
            this.f4880g = f10;
        }

        private void u(float f10) {
            this.f4876c = f10;
        }

        @Override // c4.o.f
        public void a(Matrix matrix, Path path) {
            Matrix matrix2 = this.f4883a;
            matrix.invert(matrix2);
            path.transform(matrix2);
            RectF rectF = f4874h;
            rectF.set(k(), o(), l(), j());
            path.arcTo(rectF, m(), n(), false);
            path.transform(matrix);
        }
    }

    /* compiled from: ShapePath.java */
    /* loaded from: classes.dex */
    public static class e extends f {

        /* renamed from: b, reason: collision with root package name */
        private float f4881b;

        /* renamed from: c, reason: collision with root package name */
        private float f4882c;

        @Override // c4.o.f
        public void a(Matrix matrix, Path path) {
            Matrix matrix2 = this.f4883a;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.lineTo(this.f4881b, this.f4882c);
            path.transform(matrix);
        }
    }

    /* compiled from: ShapePath.java */
    /* loaded from: classes.dex */
    public static abstract class f {

        /* renamed from: a, reason: collision with root package name */
        protected final Matrix f4883a = new Matrix();

        public abstract void a(Matrix matrix, Path path);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ShapePath.java */
    /* loaded from: classes.dex */
    public static abstract class g {

        /* renamed from: a, reason: collision with root package name */
        static final Matrix f4884a = new Matrix();

        g() {
        }

        public abstract void a(Matrix matrix, ShadowRenderer shadowRenderer, int i10, Canvas canvas);

        public final void b(ShadowRenderer shadowRenderer, int i10, Canvas canvas) {
            a(f4884a, shadowRenderer, i10, canvas);
        }
    }

    public o() {
        n(0.0f, 0.0f);
    }

    private void b(float f10) {
        if (g() == f10) {
            return;
        }
        float g6 = ((f10 - g()) + 360.0f) % 360.0f;
        if (g6 > 180.0f) {
            return;
        }
        d dVar = new d(i(), j(), i(), j());
        dVar.s(g());
        dVar.t(g6);
        this.f4865h.add(new b(dVar));
        p(f10);
    }

    private void c(g gVar, float f10, float f11) {
        b(f10);
        this.f4865h.add(gVar);
        p(f11);
    }

    private float g() {
        return this.f4862e;
    }

    private float h() {
        return this.f4863f;
    }

    private void p(float f10) {
        this.f4862e = f10;
    }

    private void q(float f10) {
        this.f4863f = f10;
    }

    private void r(float f10) {
        this.f4860c = f10;
    }

    private void s(float f10) {
        this.f4861d = f10;
    }

    private void t(float f10) {
        this.f4858a = f10;
    }

    private void u(float f10) {
        this.f4859b = f10;
    }

    public void a(float f10, float f11, float f12, float f13, float f14, float f15) {
        d dVar = new d(f10, f11, f12, f13);
        dVar.s(f14);
        dVar.t(f15);
        this.f4864g.add(dVar);
        b bVar = new b(dVar);
        float f16 = f14 + f15;
        boolean z10 = f15 < 0.0f;
        if (z10) {
            f14 = (f14 + 180.0f) % 360.0f;
        }
        c(bVar, f14, z10 ? (180.0f + f16) % 360.0f : f16);
        double d10 = f16;
        r(((f10 + f12) * 0.5f) + (((f12 - f10) / 2.0f) * ((float) Math.cos(Math.toRadians(d10)))));
        s(((f11 + f13) * 0.5f) + (((f13 - f11) / 2.0f) * ((float) Math.sin(Math.toRadians(d10)))));
    }

    public void d(Matrix matrix, Path path) {
        int size = this.f4864g.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4864g.get(i10).a(matrix, path);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e() {
        return this.f4866i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g f(Matrix matrix) {
        b(h());
        return new a(new ArrayList(this.f4865h), new Matrix(matrix));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i() {
        return this.f4860c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        return this.f4861d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float k() {
        return this.f4858a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float l() {
        return this.f4859b;
    }

    public void m(float f10, float f11) {
        e eVar = new e();
        eVar.f4881b = f10;
        eVar.f4882c = f11;
        this.f4864g.add(eVar);
        c cVar = new c(eVar, i(), j());
        c(cVar, cVar.c() + 270.0f, cVar.c() + 270.0f);
        r(f10);
        s(f11);
    }

    public void n(float f10, float f11) {
        o(f10, f11, 270.0f, 0.0f);
    }

    public void o(float f10, float f11, float f12, float f13) {
        t(f10);
        u(f11);
        r(f10);
        s(f11);
        p(f12);
        q((f12 + f13) % 360.0f);
        this.f4864g.clear();
        this.f4865h.clear();
        this.f4866i = false;
    }
}
