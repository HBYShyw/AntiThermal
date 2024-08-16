package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableFloatValue;
import m4.AnimatableTransform;
import n4.Repeater;
import r4.c;

/* compiled from: RepeaterParser.java */
/* renamed from: q4.e0, reason: use source file name */
/* loaded from: classes.dex */
class RepeaterParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16844a = c.a.a("nm", "c", "o", "tr", "hd");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Repeater a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        boolean z10 = false;
        String str = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        AnimatableTransform animatableTransform = null;
        while (cVar.w()) {
            int i02 = cVar.i0(f16844a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                animatableFloatValue = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
            } else if (i02 == 2) {
                animatableFloatValue2 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
            } else if (i02 == 3) {
                animatableTransform = AnimatableTransformParser.g(cVar, effectiveAnimationComposition);
            } else if (i02 != 4) {
                cVar.m0();
            } else {
                z10 = cVar.L();
            }
        }
        return new Repeater(str, animatableFloatValue, animatableFloatValue2, animatableTransform, z10);
    }
}
