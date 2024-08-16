package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import java.util.List;
import m4.AnimatableColorValue;
import m4.AnimatableFloatValue;
import m4.AnimatableGradientColorValue;
import m4.AnimatableIntegerValue;
import m4.AnimatablePointValue;
import m4.AnimatableScaleValue;
import m4.AnimatableShapeValue;
import m4.AnimatableTextFrame;
import t4.Keyframe;

/* compiled from: AnimatableValueParser.java */
/* renamed from: q4.d, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableValueParser {
    private static <T> List<Keyframe<T>> a(r4.c cVar, float f10, EffectiveAnimationComposition effectiveAnimationComposition, ValueParser<T> valueParser) {
        return KeyframesParser.a(cVar, effectiveAnimationComposition, f10, valueParser, false);
    }

    private static <T> List<Keyframe<T>> b(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, ValueParser<T> valueParser) {
        return KeyframesParser.a(cVar, effectiveAnimationComposition, 1.0f, valueParser, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatableColorValue c(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return new AnimatableColorValue(b(cVar, effectiveAnimationComposition, ColorParser.f16847a));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatableTextFrame d(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return new AnimatableTextFrame(b(cVar, effectiveAnimationComposition, DocumentDataParser.f16852a));
    }

    public static AnimatableFloatValue e(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return f(cVar, effectiveAnimationComposition, true);
    }

    public static AnimatableFloatValue f(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, boolean z10) {
        return new AnimatableFloatValue(a(cVar, z10 ? s4.h.f() : 1.0f, effectiveAnimationComposition, FloatParser.f16875a));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatableGradientColorValue g(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, int i10) {
        return new AnimatableGradientColorValue(b(cVar, effectiveAnimationComposition, new GradientColorParser(i10)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatableIntegerValue h(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return new AnimatableIntegerValue(b(cVar, effectiveAnimationComposition, IntegerParser.f16885a));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatablePointValue i(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return new AnimatablePointValue(KeyframesParser.a(cVar, effectiveAnimationComposition, s4.h.f(), PointFParser.f16837a, true));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatableScaleValue j(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return new AnimatableScaleValue(b(cVar, effectiveAnimationComposition, ScaleXYParser.f16846a));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatableShapeValue k(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return new AnimatableShapeValue(a(cVar, s4.h.f(), effectiveAnimationComposition, ShapeDataParser.f16848a));
    }
}
