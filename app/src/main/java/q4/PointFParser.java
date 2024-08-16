package q4;

import android.graphics.PointF;
import r4.c;

/* compiled from: PointFParser.java */
/* renamed from: q4.b0, reason: use source file name */
/* loaded from: classes.dex */
public class PointFParser implements ValueParser<PointF> {

    /* renamed from: a, reason: collision with root package name */
    public static final PointFParser f16837a = new PointFParser();

    private PointFParser() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public PointF a(r4.c cVar, float f10) {
        c.b e02 = cVar.e0();
        if (e02 == c.b.BEGIN_ARRAY) {
            return JsonUtils.e(cVar, f10);
        }
        if (e02 == c.b.BEGIN_OBJECT) {
            return JsonUtils.e(cVar, f10);
        }
        if (e02 == c.b.NUMBER) {
            PointF pointF = new PointF(((float) cVar.N()) * f10, ((float) cVar.N()) * f10);
            while (cVar.w()) {
                cVar.m0();
            }
            return pointF;
        }
        throw new IllegalArgumentException("Cannot convert json to point. Next token is " + e02);
    }
}
