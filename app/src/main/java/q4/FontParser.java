package q4;

import l4.Font;
import r4.c;

/* compiled from: FontParser.java */
/* renamed from: q4.o, reason: use source file name */
/* loaded from: classes.dex */
class FontParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16878a = c.a.a("fFamily", "fName", "fStyle", "ascent");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Font a(r4.c cVar) {
        cVar.m();
        String str = null;
        String str2 = null;
        float f10 = 0.0f;
        String str3 = null;
        while (cVar.w()) {
            int i02 = cVar.i0(f16878a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                str3 = cVar.X();
            } else if (i02 == 2) {
                str2 = cVar.X();
            } else if (i02 != 3) {
                cVar.j0();
                cVar.m0();
            } else {
                f10 = (float) cVar.N();
            }
        }
        cVar.v();
        return new Font(str, str3, str2, f10);
    }
}
