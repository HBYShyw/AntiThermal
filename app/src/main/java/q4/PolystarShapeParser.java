package q4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableFloatValue;
import m4.AnimatableValue;
import n4.PolystarShape;
import r4.c;

/* compiled from: PolystarShapeParser.java */
/* renamed from: q4.c0, reason: use source file name */
/* loaded from: classes.dex */
class PolystarShapeParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16840a = c.a.a("nm", "sy", "pt", "p", "r", "or", "os", "ir", "is", "hd");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PolystarShape a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        boolean z10 = false;
        String str = null;
        PolystarShape.a aVar = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableValue<PointF, PointF> animatableValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        AnimatableFloatValue animatableFloatValue3 = null;
        AnimatableFloatValue animatableFloatValue4 = null;
        AnimatableFloatValue animatableFloatValue5 = null;
        AnimatableFloatValue animatableFloatValue6 = null;
        while (cVar.w()) {
            switch (cVar.i0(f16840a)) {
                case 0:
                    str = cVar.X();
                    break;
                case 1:
                    aVar = PolystarShape.a.a(cVar.S());
                    break;
                case 2:
                    animatableFloatValue = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
                    break;
                case 3:
                    animatableValue = AnimatablePathValueParser.b(cVar, effectiveAnimationComposition);
                    break;
                case 4:
                    animatableFloatValue2 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
                    break;
                case 5:
                    animatableFloatValue4 = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                    break;
                case 6:
                    animatableFloatValue6 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
                    break;
                case 7:
                    animatableFloatValue3 = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                    break;
                case 8:
                    animatableFloatValue5 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
                    break;
                case 9:
                    z10 = cVar.L();
                    break;
                default:
                    cVar.j0();
                    cVar.m0();
                    break;
            }
        }
        return new PolystarShape(str, aVar, animatableFloatValue, animatableValue, animatableFloatValue2, animatableFloatValue3, animatableFloatValue4, animatableFloatValue5, animatableFloatValue6, z10);
    }
}
