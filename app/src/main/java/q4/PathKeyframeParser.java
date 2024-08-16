package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import j4.PathKeyframe;
import r4.c;

/* compiled from: PathKeyframeParser.java */
/* renamed from: q4.z, reason: use source file name */
/* loaded from: classes.dex */
class PathKeyframeParser {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static PathKeyframe a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        return new PathKeyframe(effectiveAnimationComposition, KeyframeParser.c(cVar, effectiveAnimationComposition, s4.h.f(), a0.f16834a, cVar.e0() == c.b.BEGIN_OBJECT, false));
    }
}
