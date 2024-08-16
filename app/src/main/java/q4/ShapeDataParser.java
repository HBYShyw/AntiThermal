package q4;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import l4.CubicCurveData;
import n4.ShapeData;
import r4.c;
import s4.MiscUtils;

/* compiled from: ShapeDataParser.java */
/* renamed from: q4.g0, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeDataParser implements ValueParser<ShapeData> {

    /* renamed from: a, reason: collision with root package name */
    public static final ShapeDataParser f16848a = new ShapeDataParser();

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16849b = c.a.a("c", "v", "i", "o");

    private ShapeDataParser() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public ShapeData a(r4.c cVar, float f10) {
        if (cVar.e0() == c.b.BEGIN_ARRAY) {
            cVar.c();
        }
        cVar.m();
        List<PointF> list = null;
        List<PointF> list2 = null;
        List<PointF> list3 = null;
        boolean z10 = false;
        while (cVar.w()) {
            int i02 = cVar.i0(f16849b);
            if (i02 == 0) {
                z10 = cVar.L();
            } else if (i02 == 1) {
                list = JsonUtils.f(cVar, f10);
            } else if (i02 == 2) {
                list2 = JsonUtils.f(cVar, f10);
            } else if (i02 != 3) {
                cVar.j0();
                cVar.m0();
            } else {
                list3 = JsonUtils.f(cVar, f10);
            }
        }
        cVar.v();
        if (cVar.e0() == c.b.END_ARRAY) {
            cVar.u();
        }
        if (list != null && list2 != null && list3 != null) {
            if (list.isEmpty()) {
                return new ShapeData(new PointF(), false, Collections.emptyList());
            }
            int size = list.size();
            PointF pointF = list.get(0);
            ArrayList arrayList = new ArrayList(size);
            for (int i10 = 1; i10 < size; i10++) {
                PointF pointF2 = list.get(i10);
                int i11 = i10 - 1;
                arrayList.add(new CubicCurveData(MiscUtils.a(list.get(i11), list3.get(i11)), MiscUtils.a(pointF2, list2.get(i10)), pointF2));
            }
            if (z10) {
                PointF pointF3 = list.get(0);
                int i12 = size - 1;
                arrayList.add(new CubicCurveData(MiscUtils.a(list.get(i12), list3.get(i12)), MiscUtils.a(pointF3, list2.get(0)), pointF3));
            }
            return new ShapeData(pointF, z10, arrayList);
        }
        throw new IllegalArgumentException("Shape data was missing information.");
    }
}
