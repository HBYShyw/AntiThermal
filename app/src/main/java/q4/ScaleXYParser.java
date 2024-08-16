package q4;

import r4.c;
import t4.ScaleXY;

/* compiled from: ScaleXYParser.java */
/* renamed from: q4.f0, reason: use source file name */
/* loaded from: classes.dex */
public class ScaleXYParser implements ValueParser<ScaleXY> {

    /* renamed from: a, reason: collision with root package name */
    public static final ScaleXYParser f16846a = new ScaleXYParser();

    private ScaleXYParser() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public ScaleXY a(r4.c cVar, float f10) {
        boolean z10 = cVar.e0() == c.b.BEGIN_ARRAY;
        if (z10) {
            cVar.c();
        }
        float N = (float) cVar.N();
        float N2 = (float) cVar.N();
        while (cVar.w()) {
            cVar.m0();
        }
        if (z10) {
            cVar.u();
        }
        return new ScaleXY((N / 100.0f) * f10, (N2 / 100.0f) * f10);
    }
}
