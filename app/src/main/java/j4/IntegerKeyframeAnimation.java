package j4;

import java.util.List;
import s4.MiscUtils;
import t4.EffectiveValueCallback;
import t4.Keyframe;

/* compiled from: IntegerKeyframeAnimation.java */
/* renamed from: j4.f, reason: use source file name */
/* loaded from: classes.dex */
public class IntegerKeyframeAnimation extends KeyframeAnimation<Integer> {
    public IntegerKeyframeAnimation(List<Keyframe<Integer>> list) {
        super(list);
    }

    public int p() {
        return q(b(), d());
    }

    int q(Keyframe<Integer> keyframe, float f10) {
        Integer num;
        if (keyframe.f18570b != null && keyframe.f18571c != null) {
            EffectiveValueCallback<A> effectiveValueCallback = this.f12954e;
            if (effectiveValueCallback != 0 && (num = (Integer) effectiveValueCallback.b(keyframe.f18575g, keyframe.f18576h.floatValue(), keyframe.f18570b, keyframe.f18571c, f10, e(), f())) != null) {
                return num.intValue();
            }
            return MiscUtils.l(keyframe.g(), keyframe.d(), f10);
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j4.BaseKeyframeAnimation
    /* renamed from: r, reason: merged with bridge method [inline-methods] */
    public Integer i(Keyframe<Integer> keyframe, float f10) {
        return Integer.valueOf(q(keyframe, f10));
    }
}
