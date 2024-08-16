package q4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationComposition;
import java.util.ArrayList;
import m4.AnimatableFloatValue;
import m4.AnimatablePathValue;
import m4.AnimatableSplitDimensionPathValue;
import m4.AnimatableValue;
import r4.c;
import t4.Keyframe;

/* compiled from: AnimatablePathValueParser.java */
/* renamed from: q4.a, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatablePathValueParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16833a = c.a.a("k", "x", "y");

    public static AnimatablePathValue a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        ArrayList arrayList = new ArrayList();
        if (cVar.e0() == c.b.BEGIN_ARRAY) {
            cVar.c();
            while (cVar.w()) {
                arrayList.add(PathKeyframeParser.a(cVar, effectiveAnimationComposition));
            }
            cVar.u();
            KeyframesParser.b(arrayList);
        } else {
            arrayList.add(new Keyframe(JsonUtils.e(cVar, s4.h.f())));
        }
        return new AnimatablePathValue(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AnimatableValue<PointF, PointF> b(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        cVar.m();
        AnimatablePathValue animatablePathValue = null;
        AnimatableFloatValue animatableFloatValue = null;
        boolean z10 = false;
        AnimatableFloatValue animatableFloatValue2 = null;
        while (cVar.e0() != c.b.END_OBJECT) {
            int i02 = cVar.i0(f16833a);
            if (i02 == 0) {
                animatablePathValue = a(cVar, effectiveAnimationComposition);
            } else if (i02 != 1) {
                if (i02 != 2) {
                    cVar.j0();
                    cVar.m0();
                } else if (cVar.e0() == c.b.STRING) {
                    cVar.m0();
                    z10 = true;
                } else {
                    animatableFloatValue = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                }
            } else if (cVar.e0() == c.b.STRING) {
                cVar.m0();
                z10 = true;
            } else {
                animatableFloatValue2 = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
            }
        }
        cVar.v();
        if (z10) {
            effectiveAnimationComposition.a("EffectiveAnimation doesn't support expressions.");
        }
        return animatablePathValue != null ? animatablePathValue : new AnimatableSplitDimensionPathValue(animatableFloatValue2, animatableFloatValue);
    }
}
