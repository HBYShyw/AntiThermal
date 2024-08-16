package q4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatablePointValue;
import m4.AnimatableValue;
import n4.CircleShape;
import r4.c;

/* compiled from: CircleShapeParser.java */
/* renamed from: q4.f, reason: use source file name */
/* loaded from: classes.dex */
class CircleShapeParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16845a = c.a.a("nm", "p", "s", "hd", "d");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CircleShape a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, int i10) {
        boolean z10 = i10 == 3;
        boolean z11 = false;
        String str = null;
        AnimatableValue<PointF, PointF> animatableValue = null;
        AnimatablePointValue animatablePointValue = null;
        while (cVar.w()) {
            int i02 = cVar.i0(f16845a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                animatableValue = AnimatablePathValueParser.b(cVar, effectiveAnimationComposition);
            } else if (i02 == 2) {
                animatablePointValue = AnimatableValueParser.i(cVar, effectiveAnimationComposition);
            } else if (i02 == 3) {
                z11 = cVar.L();
            } else if (i02 != 4) {
                cVar.j0();
                cVar.m0();
            } else {
                z10 = cVar.S() == 3;
            }
        }
        return new CircleShape(str, animatableValue, animatablePointValue, z10, z11);
    }
}
