package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import java.util.ArrayList;
import l4.FontCharacter;
import n4.ShapeGroup;
import r4.c;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FontCharacterParser.java */
/* renamed from: q4.n, reason: use source file name */
/* loaded from: classes.dex */
public class FontCharacterParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16876a = c.a.a("ch", "size", "w", "style", "fFamily", "data");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16877b = c.a.a("shapes");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FontCharacter a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        ArrayList arrayList = new ArrayList();
        cVar.m();
        String str = null;
        String str2 = null;
        double d10 = 0.0d;
        double d11 = 0.0d;
        char c10 = 0;
        while (cVar.w()) {
            int i02 = cVar.i0(f16876a);
            if (i02 == 0) {
                c10 = cVar.X().charAt(0);
            } else if (i02 == 1) {
                d10 = cVar.N();
            } else if (i02 == 2) {
                d11 = cVar.N();
            } else if (i02 == 3) {
                str = cVar.X();
            } else if (i02 == 4) {
                str2 = cVar.X();
            } else if (i02 != 5) {
                cVar.j0();
                cVar.m0();
            } else {
                cVar.m();
                while (cVar.w()) {
                    if (cVar.i0(f16877b) != 0) {
                        cVar.j0();
                        cVar.m0();
                    } else {
                        cVar.c();
                        while (cVar.w()) {
                            arrayList.add((ShapeGroup) ContentModelParser.a(cVar, effectiveAnimationComposition));
                        }
                        cVar.u();
                    }
                }
                cVar.v();
            }
        }
        cVar.v();
        return new FontCharacter(arrayList, c10, d10, d11, str, str2);
    }
}
