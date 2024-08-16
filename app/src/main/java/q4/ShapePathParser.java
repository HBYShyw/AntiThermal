package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableShapeValue;
import r4.c;

/* compiled from: ShapePathParser.java */
/* renamed from: q4.j0, reason: use source file name */
/* loaded from: classes.dex */
class ShapePathParser {

    /* renamed from: a, reason: collision with root package name */
    static c.a f16860a = c.a.a("nm", "ind", "ks", "hd");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static n4.p a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        int i10 = 0;
        String str = null;
        AnimatableShapeValue animatableShapeValue = null;
        boolean z10 = false;
        while (cVar.w()) {
            int i02 = cVar.i0(f16860a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                i10 = cVar.S();
            } else if (i02 == 2) {
                animatableShapeValue = AnimatableValueParser.k(cVar, effectiveAnimationComposition);
            } else if (i02 != 3) {
                cVar.m0();
            } else {
                z10 = cVar.L();
            }
        }
        return new n4.p(str, i10, animatableShapeValue, z10);
    }
}
