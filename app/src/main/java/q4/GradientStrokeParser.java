package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import java.util.ArrayList;
import java.util.Collections;
import m4.AnimatableFloatValue;
import m4.AnimatableGradientColorValue;
import m4.AnimatableIntegerValue;
import m4.AnimatablePointValue;
import n4.GradientStroke;
import n4.GradientType;
import n4.ShapeStroke;
import r4.c;
import t4.Keyframe;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: GradientStrokeParser.java */
/* renamed from: q4.r, reason: use source file name */
/* loaded from: classes.dex */
public class GradientStrokeParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16882a = c.a.a("nm", "g", "o", "t", "s", "e", "w", "lc", "lj", "ml", "hd", "d");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16883b = c.a.a("p", "k");

    /* renamed from: c, reason: collision with root package name */
    private static final c.a f16884c = c.a.a("n", "v");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GradientStroke a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        AnimatableGradientColorValue animatableGradientColorValue;
        ArrayList arrayList = new ArrayList();
        float f10 = 0.0f;
        String str = null;
        GradientType gradientType = null;
        AnimatableGradientColorValue animatableGradientColorValue2 = null;
        AnimatablePointValue animatablePointValue = null;
        AnimatablePointValue animatablePointValue2 = null;
        AnimatableFloatValue animatableFloatValue = null;
        ShapeStroke.b bVar = null;
        ShapeStroke.c cVar2 = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        boolean z10 = false;
        AnimatableIntegerValue animatableIntegerValue = null;
        while (cVar.w()) {
            switch (cVar.i0(f16882a)) {
                case 0:
                    str = cVar.X();
                    break;
                case 1:
                    int i10 = -1;
                    cVar.m();
                    while (cVar.w()) {
                        int i02 = cVar.i0(f16883b);
                        if (i02 != 0) {
                            animatableGradientColorValue = animatableGradientColorValue2;
                            if (i02 != 1) {
                                cVar.j0();
                                cVar.m0();
                            } else {
                                animatableGradientColorValue2 = AnimatableValueParser.g(cVar, effectiveAnimationComposition, i10);
                            }
                        } else {
                            animatableGradientColorValue = animatableGradientColorValue2;
                            i10 = cVar.S();
                        }
                        animatableGradientColorValue2 = animatableGradientColorValue;
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
                    animatableFloatValue = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                    break;
                case 7:
                    bVar = ShapeStroke.b.values()[cVar.S() - 1];
                    break;
                case 8:
                    cVar2 = ShapeStroke.c.values()[cVar.S() - 1];
                    break;
                case 9:
                    f10 = (float) cVar.N();
                    break;
                case 10:
                    z10 = cVar.L();
                    break;
                case 11:
                    cVar.c();
                    while (cVar.w()) {
                        cVar.m();
                        String str2 = null;
                        AnimatableFloatValue animatableFloatValue3 = null;
                        while (cVar.w()) {
                            int i03 = cVar.i0(f16884c);
                            if (i03 != 0) {
                                AnimatableFloatValue animatableFloatValue4 = animatableFloatValue2;
                                if (i03 != 1) {
                                    cVar.j0();
                                    cVar.m0();
                                } else {
                                    animatableFloatValue3 = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                                }
                                animatableFloatValue2 = animatableFloatValue4;
                            } else {
                                str2 = cVar.X();
                            }
                        }
                        AnimatableFloatValue animatableFloatValue5 = animatableFloatValue2;
                        cVar.v();
                        if (str2.equals("o")) {
                            animatableFloatValue2 = animatableFloatValue3;
                        } else {
                            if (str2.equals("d") || str2.equals("g")) {
                                effectiveAnimationComposition.v(true);
                                arrayList.add(animatableFloatValue3);
                            }
                            animatableFloatValue2 = animatableFloatValue5;
                        }
                    }
                    AnimatableFloatValue animatableFloatValue6 = animatableFloatValue2;
                    cVar.u();
                    if (arrayList.size() == 1) {
                        arrayList.add(arrayList.get(0));
                    }
                    animatableFloatValue2 = animatableFloatValue6;
                    break;
                default:
                    cVar.j0();
                    cVar.m0();
                    break;
            }
        }
        if (animatableIntegerValue == null) {
            animatableIntegerValue = new AnimatableIntegerValue(Collections.singletonList(new Keyframe(100)));
        }
        return new GradientStroke(str, gradientType, animatableGradientColorValue2, animatableIntegerValue, animatablePointValue, animatablePointValue2, animatableFloatValue, bVar, cVar2, f10, arrayList, animatableFloatValue2, z10);
    }
}
