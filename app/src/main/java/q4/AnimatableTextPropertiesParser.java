package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableColorValue;
import m4.AnimatableFloatValue;
import m4.AnimatableTextProperties;
import r4.c;

/* compiled from: AnimatableTextPropertiesParser.java */
/* renamed from: q4.b, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableTextPropertiesParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16835a = c.a.a("a");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16836b = c.a.a("fc", "sc", "sw", "t");

    public static AnimatableTextProperties a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        cVar.m();
        AnimatableTextProperties animatableTextProperties = null;
        while (cVar.w()) {
            if (cVar.i0(f16835a) != 0) {
                cVar.j0();
                cVar.m0();
            } else {
                animatableTextProperties = b(cVar, effectiveAnimationComposition);
            }
        }
        cVar.v();
        return animatableTextProperties == null ? new AnimatableTextProperties(null, null, null, null) : animatableTextProperties;
    }

    private static AnimatableTextProperties b(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        cVar.m();
        AnimatableColorValue animatableColorValue = null;
        AnimatableColorValue animatableColorValue2 = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        while (cVar.w()) {
            int i02 = cVar.i0(f16836b);
            if (i02 == 0) {
                animatableColorValue = AnimatableValueParser.c(cVar, effectiveAnimationComposition);
            } else if (i02 == 1) {
                animatableColorValue2 = AnimatableValueParser.c(cVar, effectiveAnimationComposition);
            } else if (i02 == 2) {
                animatableFloatValue = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
            } else if (i02 != 3) {
                cVar.j0();
                cVar.m0();
            } else {
                animatableFloatValue2 = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
            }
        }
        cVar.v();
        return new AnimatableTextProperties(animatableColorValue, animatableColorValue2, animatableFloatValue, animatableFloatValue2);
    }
}
