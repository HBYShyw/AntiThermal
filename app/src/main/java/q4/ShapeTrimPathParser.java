package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableFloatValue;
import n4.ShapeTrimPath;
import r4.c;

/* compiled from: ShapeTrimPathParser.java */
/* renamed from: q4.l0, reason: use source file name */
/* loaded from: classes.dex */
class ShapeTrimPathParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16874a = c.a.a("s", "e", "o", "nm", "m", "hd");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShapeTrimPath a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        boolean z10 = false;
        String str = null;
        ShapeTrimPath.a aVar = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        AnimatableFloatValue animatableFloatValue3 = null;
        while (cVar.w()) {
            int i02 = cVar.i0(f16874a);
            if (i02 == 0) {
                animatableFloatValue = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
            } else if (i02 == 1) {
                animatableFloatValue2 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
            } else if (i02 == 2) {
                animatableFloatValue3 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
            } else if (i02 == 3) {
                str = cVar.X();
            } else if (i02 == 4) {
                aVar = ShapeTrimPath.a.a(cVar.S());
            } else if (i02 != 5) {
                cVar.m0();
            } else {
                z10 = cVar.L();
            }
        }
        return new ShapeTrimPath(str, aVar, animatableFloatValue, animatableFloatValue2, animatableFloatValue3, z10);
    }
}
