package q4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableFloatValue;
import m4.AnimatableIntegerValue;
import m4.AnimatablePathValue;
import m4.AnimatableScaleValue;
import m4.AnimatableSplitDimensionPathValue;
import m4.AnimatableTransform;
import m4.AnimatableValue;
import r4.c;
import t4.Keyframe;
import t4.ScaleXY;

/* compiled from: AnimatableTransformParser.java */
/* renamed from: q4.c, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableTransformParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16838a = c.a.a("a", "p", "s", "rz", "r", "o", "so", "eo", "sk", "sa");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16839b = c.a.a("k");

    private static boolean a(AnimatablePathValue animatablePathValue) {
        return animatablePathValue == null || (animatablePathValue.o() && animatablePathValue.b().get(0).f18570b.equals(0.0f, 0.0f));
    }

    private static boolean b(AnimatableValue<PointF, PointF> animatableValue) {
        return animatableValue == null || (!(animatableValue instanceof AnimatableSplitDimensionPathValue) && animatableValue.o() && animatableValue.b().get(0).f18570b.equals(0.0f, 0.0f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean c(AnimatableFloatValue animatableFloatValue) {
        return animatableFloatValue == null || (animatableFloatValue.o() && ((Float) ((Keyframe) animatableFloatValue.b().get(0)).f18570b).floatValue() == 0.0f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean d(AnimatableScaleValue animatableScaleValue) {
        return animatableScaleValue == null || (animatableScaleValue.o() && ((ScaleXY) ((Keyframe) animatableScaleValue.b().get(0)).f18570b).a(1.0f, 1.0f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean e(AnimatableFloatValue animatableFloatValue) {
        return animatableFloatValue == null || (animatableFloatValue.o() && ((Float) ((Keyframe) animatableFloatValue.b().get(0)).f18570b).floatValue() == 0.0f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean f(AnimatableFloatValue animatableFloatValue) {
        return animatableFloatValue == null || (animatableFloatValue.o() && ((Float) ((Keyframe) animatableFloatValue.b().get(0)).f18570b).floatValue() == 0.0f);
    }

    public static AnimatableTransform g(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        boolean z10;
        boolean z11;
        boolean z12 = false;
        boolean z13 = cVar.e0() == c.b.BEGIN_OBJECT;
        if (z13) {
            cVar.m();
        }
        AnimatableFloatValue animatableFloatValue = null;
        AnimatablePathValue animatablePathValue = null;
        AnimatableValue<PointF, PointF> animatableValue = null;
        AnimatableScaleValue animatableScaleValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        AnimatableFloatValue animatableFloatValue3 = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        AnimatableFloatValue animatableFloatValue4 = null;
        AnimatableFloatValue animatableFloatValue5 = null;
        while (cVar.w()) {
            switch (cVar.i0(f16838a)) {
                case 0:
                    z10 = z12;
                    cVar.m();
                    while (cVar.w()) {
                        if (cVar.i0(f16839b) != 0) {
                            cVar.j0();
                            cVar.m0();
                        } else {
                            animatablePathValue = AnimatablePathValueParser.a(cVar, effectiveAnimationComposition);
                        }
                    }
                    cVar.v();
                    break;
                case 1:
                    animatableValue = AnimatablePathValueParser.b(cVar, effectiveAnimationComposition);
                    continue;
                case 2:
                    animatableScaleValue = AnimatableValueParser.j(cVar, effectiveAnimationComposition);
                    continue;
                case 3:
                    z10 = z12;
                    effectiveAnimationComposition.a("EffectiveAnimation doesn't support 3D layers.");
                    break;
                case 4:
                    AnimatableFloatValue f10 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, z12);
                    if (f10.b().isEmpty()) {
                        f10.b().add(new Keyframe(effectiveAnimationComposition, Float.valueOf(0.0f), Float.valueOf(0.0f), null, 0.0f, Float.valueOf(effectiveAnimationComposition.g())));
                    } else if (((Keyframe) f10.b().get(0)).f18570b == 0) {
                        z11 = false;
                        f10.b().set(0, new Keyframe(effectiveAnimationComposition, Float.valueOf(0.0f), Float.valueOf(0.0f), null, 0.0f, Float.valueOf(effectiveAnimationComposition.g())));
                        z12 = z11;
                        animatableFloatValue = f10;
                        continue;
                    }
                    z11 = false;
                    z12 = z11;
                    animatableFloatValue = f10;
                    continue;
                case 5:
                    animatableIntegerValue = AnimatableValueParser.h(cVar, effectiveAnimationComposition);
                    continue;
                case 6:
                    animatableFloatValue4 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, z12);
                    continue;
                case 7:
                    animatableFloatValue5 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, z12);
                    continue;
                case 8:
                    animatableFloatValue2 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, z12);
                    continue;
                case 9:
                    animatableFloatValue3 = AnimatableValueParser.f(cVar, effectiveAnimationComposition, z12);
                    continue;
                default:
                    z10 = z12;
                    cVar.j0();
                    cVar.m0();
                    break;
            }
            z12 = z10;
        }
        if (z13) {
            cVar.v();
        }
        AnimatablePathValue animatablePathValue2 = a(animatablePathValue) ? null : animatablePathValue;
        AnimatableValue<PointF, PointF> animatableValue2 = b(animatableValue) ? null : animatableValue;
        AnimatableFloatValue animatableFloatValue6 = c(animatableFloatValue) ? null : animatableFloatValue;
        if (d(animatableScaleValue)) {
            animatableScaleValue = null;
        }
        return new AnimatableTransform(animatablePathValue2, animatableValue2, animatableScaleValue, animatableFloatValue6, animatableIntegerValue, animatableFloatValue4, animatableFloatValue5, f(animatableFloatValue2) ? null : animatableFloatValue2, e(animatableFloatValue3) ? null : animatableFloatValue3);
    }
}
