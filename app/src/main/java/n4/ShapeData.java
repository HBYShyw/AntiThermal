package n4;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;
import l4.CubicCurveData;
import s4.MiscUtils;

/* compiled from: ShapeData.java */
/* renamed from: n4.m, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeData {

    /* renamed from: a, reason: collision with root package name */
    private final List<CubicCurveData> f15828a;

    /* renamed from: b, reason: collision with root package name */
    private PointF f15829b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f15830c;

    public ShapeData(PointF pointF, boolean z10, List<CubicCurveData> list) {
        this.f15829b = pointF;
        this.f15830c = z10;
        this.f15828a = new ArrayList(list);
    }

    private void e(float f10, float f11) {
        if (this.f15829b == null) {
            this.f15829b = new PointF();
        }
        this.f15829b.set(f10, f11);
    }

    public List<CubicCurveData> a() {
        return this.f15828a;
    }

    public PointF b() {
        return this.f15829b;
    }

    public void c(ShapeData shapeData, ShapeData shapeData2, float f10) {
        if (this.f15829b == null) {
            this.f15829b = new PointF();
        }
        this.f15830c = shapeData.d() || shapeData2.d();
        if (shapeData.a().size() != shapeData2.a().size()) {
            s4.e.c("Curves must have the same number of control points. Shape 1: " + shapeData.a().size() + "\tShape 2: " + shapeData2.a().size());
        }
        int min = Math.min(shapeData.a().size(), shapeData2.a().size());
        if (this.f15828a.size() < min) {
            for (int size = this.f15828a.size(); size < min; size++) {
                this.f15828a.add(new CubicCurveData());
            }
        } else if (this.f15828a.size() > min) {
            for (int size2 = this.f15828a.size() - 1; size2 >= min; size2--) {
                List<CubicCurveData> list = this.f15828a;
                list.remove(list.size() - 1);
            }
        }
        PointF b10 = shapeData.b();
        PointF b11 = shapeData2.b();
        e(MiscUtils.k(b10.x, b11.x, f10), MiscUtils.k(b10.y, b11.y, f10));
        for (int size3 = this.f15828a.size() - 1; size3 >= 0; size3--) {
            CubicCurveData cubicCurveData = shapeData.a().get(size3);
            CubicCurveData cubicCurveData2 = shapeData2.a().get(size3);
            PointF a10 = cubicCurveData.a();
            PointF b12 = cubicCurveData.b();
            PointF c10 = cubicCurveData.c();
            PointF a11 = cubicCurveData2.a();
            PointF b13 = cubicCurveData2.b();
            PointF c11 = cubicCurveData2.c();
            this.f15828a.get(size3).d(MiscUtils.k(a10.x, a11.x, f10), MiscUtils.k(a10.y, a11.y, f10));
            this.f15828a.get(size3).e(MiscUtils.k(b12.x, b13.x, f10), MiscUtils.k(b12.y, b13.y, f10));
            this.f15828a.get(size3).f(MiscUtils.k(c10.x, c11.x, f10), MiscUtils.k(c10.y, c11.y, f10));
        }
    }

    public boolean d() {
        return this.f15830c;
    }

    public String toString() {
        return "ShapeData{numCurves=" + this.f15828a.size() + "closed=" + this.f15830c + '}';
    }

    public ShapeData() {
        this.f15828a = new ArrayList();
    }
}
