package q4;

import android.graphics.Path;
import com.oplus.anim.EffectiveAnimationComposition;
import java.util.Collections;
import m4.AnimatableGradientColorValue;
import m4.AnimatableIntegerValue;
import m4.AnimatablePointValue;
import n4.GradientFill;
import n4.GradientType;
import r4.c;
import t4.Keyframe;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: GradientFillParser.java */
/* renamed from: q4.q, reason: use source file name */
/* loaded from: classes.dex */
public class GradientFillParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16880a = c.a.a("nm", "g", "o", "t", "s", "e", "r", "hd");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16881b = c.a.a("p", "k");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GradientFill a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        AnimatableIntegerValue animatableIntegerValue = null;
        Path.FillType fillType = Path.FillType.WINDING;
        String str = null;
        GradientType gradientType = null;
        AnimatableGradientColorValue animatableGradientColorValue = null;
        AnimatablePointValue animatablePointValue = null;
        AnimatablePointValue animatablePointValue2 = null;
        boolean z10 = false;
        while (cVar.w()) {
            switch (cVar.i0(f16880a)) {
                case 0:
                    str = cVar.X();
                    break;
                case 1:
                    int i10 = -1;
                    cVar.m();
                    while (cVar.w()) {
                        int i02 = cVar.i0(f16881b);
                        if (i02 == 0) {
                            i10 = cVar.S();
                        } else if (i02 != 1) {
                            cVar.j0();
                            cVar.m0();
                        } else {
                            animatableGradientColorValue = AnimatableValueParser.g(cVar, effectiveAnimationComposition, i10);
                        }
                    }
                    cVar.v();
                    break;
                case 2:
                    animatableIntegerValue = AnimatableValueParser.h(cVar, effectiveAnimationComposition);
                    break;
                case 3:
                    gradientType = cVar.S() == 1 ? GradientType.LINEAR : GradientType.RADIAL;
                    break;
                case 4:
                    animatablePointValue = AnimatableValueParser.i(cVar, effectiveAnimationComposition);
                    break;
                case 5:
                    animatablePointValue2 = AnimatableValueParser.i(cVar, effectiveAnimationComposition);
                    break;
                case 6:
                    fillType = cVar.S() == 1 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD;
                    break;
                case 7:
                    z10 = cVar.L();
                    break;
                default:
                    cVar.j0();
                    cVar.m0();
                    break;
            }
        }
        return new GradientFill(str, gradientType, fillType, animatableGradientColorValue, animatableIntegerValue == null ? new AnimatableIntegerValue(Collections.singletonList(new Keyframe(100))) : animatableIntegerValue, animatablePointValue, animatablePointValue2, null, null, z10);
    }
}
