package c4;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

/* compiled from: ShapeAppearancePathProvider.java */
/* renamed from: c4.n, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeAppearancePathProvider {

    /* renamed from: a, reason: collision with root package name */
    private final o[] f4840a = new o[4];

    /* renamed from: b, reason: collision with root package name */
    private final Matrix[] f4841b = new Matrix[4];

    /* renamed from: c, reason: collision with root package name */
    private final Matrix[] f4842c = new Matrix[4];

    /* renamed from: d, reason: collision with root package name */
    private final PointF f4843d = new PointF();

    /* renamed from: e, reason: collision with root package name */
    private final Path f4844e = new Path();

    /* renamed from: f, reason: collision with root package name */
    private final Path f4845f = new Path();

    /* renamed from: g, reason: collision with root package name */
    private final o f4846g = new o();

    /* renamed from: h, reason: collision with root package name */
    private final float[] f4847h = new float[2];

    /* renamed from: i, reason: collision with root package name */
    private final float[] f4848i = new float[2];

    /* renamed from: j, reason: collision with root package name */
    private final Path f4849j = new Path();

    /* renamed from: k, reason: collision with root package name */
    private final Path f4850k = new Path();

    /* renamed from: l, reason: collision with root package name */
    private boolean f4851l = true;

    /* compiled from: ShapeAppearancePathProvider.java */
    /* renamed from: c4.n$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        static final ShapeAppearancePathProvider f4852a = new ShapeAppearancePathProvider();
    }

    /* compiled from: ShapeAppearancePathProvider.java */
    /* renamed from: c4.n$b */
    /* loaded from: classes.dex */
    public interface b {
        void a(o oVar, Matrix matrix, int i10);

        void b(o oVar, Matrix matrix, int i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ShapeAppearancePathProvider.java */
    /* renamed from: c4.n$c */
    /* loaded from: classes.dex */
    public static final class c {

        /* renamed from: a, reason: collision with root package name */
        public final ShapeAppearanceModel f4853a;

        /* renamed from: b, reason: collision with root package name */
        public final Path f4854b;

        /* renamed from: c, reason: collision with root package name */
        public final RectF f4855c;

        /* renamed from: d, reason: collision with root package name */
        public final b f4856d;

        /* renamed from: e, reason: collision with root package name */
        public final float f4857e;

        c(ShapeAppearanceModel shapeAppearanceModel, float f10, RectF rectF, b bVar, Path path) {
            this.f4856d = bVar;
            this.f4853a = shapeAppearanceModel;
            this.f4857e = f10;
            this.f4855c = rectF;
            this.f4854b = path;
        }
    }

    public ShapeAppearancePathProvider() {
        for (int i10 = 0; i10 < 4; i10++) {
            this.f4840a[i10] = new o();
            this.f4841b[i10] = new Matrix();
            this.f4842c[i10] = new Matrix();
        }
    }

    private float a(int i10) {
        return (i10 + 1) * 90;
    }

    private void b(c cVar, int i10) {
        this.f4847h[0] = this.f4840a[i10].k();
        this.f4847h[1] = this.f4840a[i10].l();
        this.f4841b[i10].mapPoints(this.f4847h);
        if (i10 == 0) {
            Path path = cVar.f4854b;
            float[] fArr = this.f4847h;
            path.moveTo(fArr[0], fArr[1]);
        } else {
            Path path2 = cVar.f4854b;
            float[] fArr2 = this.f4847h;
            path2.lineTo(fArr2[0], fArr2[1]);
        }
        this.f4840a[i10].d(this.f4841b[i10], cVar.f4854b);
        b bVar = cVar.f4856d;
        if (bVar != null) {
            bVar.b(this.f4840a[i10], this.f4841b[i10], i10);
        }
    }

    private void c(c cVar, int i10) {
        int i11 = (i10 + 1) % 4;
        this.f4847h[0] = this.f4840a[i10].i();
        this.f4847h[1] = this.f4840a[i10].j();
        this.f4841b[i10].mapPoints(this.f4847h);
        this.f4848i[0] = this.f4840a[i11].k();
        this.f4848i[1] = this.f4840a[i11].l();
        this.f4841b[i11].mapPoints(this.f4848i);
        float f10 = this.f4847h[0];
        float[] fArr = this.f4848i;
        float max = Math.max(((float) Math.hypot(f10 - fArr[0], r1[1] - fArr[1])) - 0.001f, 0.0f);
        float i12 = i(cVar.f4855c, i10);
        this.f4846g.n(0.0f, 0.0f);
        EdgeTreatment j10 = j(i10, cVar.f4853a);
        j10.b(max, i12, cVar.f4857e, this.f4846g);
        this.f4849j.reset();
        this.f4846g.d(this.f4842c[i10], this.f4849j);
        if (this.f4851l && (j10.a() || l(this.f4849j, i10) || l(this.f4849j, i11))) {
            Path path = this.f4849j;
            path.op(path, this.f4845f, Path.Op.DIFFERENCE);
            this.f4847h[0] = this.f4846g.k();
            this.f4847h[1] = this.f4846g.l();
            this.f4842c[i10].mapPoints(this.f4847h);
            Path path2 = this.f4844e;
            float[] fArr2 = this.f4847h;
            path2.moveTo(fArr2[0], fArr2[1]);
            this.f4846g.d(this.f4842c[i10], this.f4844e);
        } else {
            this.f4846g.d(this.f4842c[i10], cVar.f4854b);
        }
        b bVar = cVar.f4856d;
        if (bVar != null) {
            bVar.a(this.f4846g, this.f4842c[i10], i10);
        }
    }

    private void f(int i10, RectF rectF, PointF pointF) {
        if (i10 == 1) {
            pointF.set(rectF.right, rectF.bottom);
            return;
        }
        if (i10 == 2) {
            pointF.set(rectF.left, rectF.bottom);
        } else if (i10 != 3) {
            pointF.set(rectF.right, rectF.top);
        } else {
            pointF.set(rectF.left, rectF.top);
        }
    }

    private CornerSize g(int i10, ShapeAppearanceModel shapeAppearanceModel) {
        if (i10 == 1) {
            return shapeAppearanceModel.l();
        }
        if (i10 == 2) {
            return shapeAppearanceModel.j();
        }
        if (i10 != 3) {
            return shapeAppearanceModel.t();
        }
        return shapeAppearanceModel.r();
    }

    private CornerTreatment h(int i10, ShapeAppearanceModel shapeAppearanceModel) {
        if (i10 == 1) {
            return shapeAppearanceModel.k();
        }
        if (i10 == 2) {
            return shapeAppearanceModel.i();
        }
        if (i10 != 3) {
            return shapeAppearanceModel.s();
        }
        return shapeAppearanceModel.q();
    }

    private float i(RectF rectF, int i10) {
        float[] fArr = this.f4847h;
        o[] oVarArr = this.f4840a;
        fArr[0] = oVarArr[i10].f4860c;
        fArr[1] = oVarArr[i10].f4861d;
        this.f4841b[i10].mapPoints(fArr);
        if (i10 != 1 && i10 != 3) {
            return Math.abs(rectF.centerY() - this.f4847h[1]);
        }
        return Math.abs(rectF.centerX() - this.f4847h[0]);
    }

    private EdgeTreatment j(int i10, ShapeAppearanceModel shapeAppearanceModel) {
        if (i10 == 1) {
            return shapeAppearanceModel.h();
        }
        if (i10 == 2) {
            return shapeAppearanceModel.n();
        }
        if (i10 != 3) {
            return shapeAppearanceModel.o();
        }
        return shapeAppearanceModel.p();
    }

    public static ShapeAppearancePathProvider k() {
        return a.f4852a;
    }

    private boolean l(Path path, int i10) {
        this.f4850k.reset();
        this.f4840a[i10].d(this.f4841b[i10], this.f4850k);
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        this.f4850k.computeBounds(rectF, true);
        path.op(this.f4850k, Path.Op.INTERSECT);
        path.computeBounds(rectF, true);
        if (rectF.isEmpty()) {
            return rectF.width() > 1.0f && rectF.height() > 1.0f;
        }
        return true;
    }

    private void m(c cVar, int i10) {
        h(i10, cVar.f4853a).b(this.f4840a[i10], 90.0f, cVar.f4857e, cVar.f4855c, g(i10, cVar.f4853a));
        float a10 = a(i10);
        this.f4841b[i10].reset();
        f(i10, cVar.f4855c, this.f4843d);
        Matrix matrix = this.f4841b[i10];
        PointF pointF = this.f4843d;
        matrix.setTranslate(pointF.x, pointF.y);
        this.f4841b[i10].preRotate(a10);
    }

    private void n(int i10) {
        this.f4847h[0] = this.f4840a[i10].i();
        this.f4847h[1] = this.f4840a[i10].j();
        this.f4841b[i10].mapPoints(this.f4847h);
        float a10 = a(i10);
        this.f4842c[i10].reset();
        Matrix matrix = this.f4842c[i10];
        float[] fArr = this.f4847h;
        matrix.setTranslate(fArr[0], fArr[1]);
        this.f4842c[i10].preRotate(a10);
    }

    public void d(ShapeAppearanceModel shapeAppearanceModel, float f10, RectF rectF, Path path) {
        e(shapeAppearanceModel, f10, rectF, null, path);
    }

    public void e(ShapeAppearanceModel shapeAppearanceModel, float f10, RectF rectF, b bVar, Path path) {
        path.rewind();
        this.f4844e.rewind();
        this.f4845f.rewind();
        this.f4845f.addRect(rectF, Path.Direction.CW);
        c cVar = new c(shapeAppearanceModel, f10, rectF, bVar, path);
        for (int i10 = 0; i10 < 4; i10++) {
            m(cVar, i10);
            n(i10);
        }
        for (int i11 = 0; i11 < 4; i11++) {
            b(cVar, i11);
            c(cVar, i11);
        }
        path.close();
        this.f4844e.close();
        if (this.f4844e.isEmpty()) {
            return;
        }
        path.op(this.f4844e, Path.Op.UNION);
    }
}
