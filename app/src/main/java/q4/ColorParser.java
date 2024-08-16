package q4;

import android.graphics.Color;
import r4.c;

/* compiled from: ColorParser.java */
/* renamed from: q4.g, reason: use source file name */
/* loaded from: classes.dex */
public class ColorParser implements ValueParser<Integer> {

    /* renamed from: a, reason: collision with root package name */
    public static final ColorParser f16847a = new ColorParser();

    private ColorParser() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public Integer a(r4.c cVar, float f10) {
        boolean z10 = cVar.e0() == c.b.BEGIN_ARRAY;
        if (z10) {
            cVar.c();
        }
        double N = cVar.N();
        double N2 = cVar.N();
        double N3 = cVar.N();
        double N4 = cVar.e0() == c.b.NUMBER ? cVar.N() : 1.0d;
        if (z10) {
            cVar.u();
        }
        if (N <= 1.0d && N2 <= 1.0d && N3 <= 1.0d) {
            N *= 255.0d;
            N2 *= 255.0d;
            N3 *= 255.0d;
            if (N4 <= 1.0d) {
                N4 *= 255.0d;
            }
        }
        return Integer.valueOf(Color.argb((int) N4, (int) N, (int) N2, (int) N3));
    }
}
