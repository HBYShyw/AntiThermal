package q4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableFloatValue;
import m4.AnimatablePointValue;
import m4.AnimatableValue;
import n4.RectangleShape;
import r4.c;

/* compiled from: RectangleShapeParser.java */
/* renamed from: q4.d0, reason: use source file name */
/* loaded from: classes.dex */
class RectangleShapeParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16841a = c.a.a("nm", "p", "s", "r", "hd");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RectangleShape a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        String str = null;
        AnimatableValue<PointF, PointF> animatableValue = null;
        AnimatablePointValue animatablePointValue = null;
        AnimatableFloatValue animatableFloatValue = null;
        boolean z10 = false;
        while (cVar.w()) {
            int i02 = cVar.i0(f16841a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                animatableValue = AnimatablePathValueParser.b(cVar, effectiveAnimationComposition);
            } else if (i02 == 2) {
                animatablePointValue = AnimatableValueParser.i(cVar, effectiveAnimationComposition);
            } else if (i02 == 3) {
                animatableFloatValue = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
            } else if (i02 != 4) {
                cVar.m0();
            } else {
                z10 = cVar.L();
            }
        }
        return new RectangleShape(str, animatableValue, animatablePointValue, animatableFloatValue, z10);
    }
}
