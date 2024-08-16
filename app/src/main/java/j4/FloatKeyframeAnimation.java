package j4;

import java.util.List;
import s4.MiscUtils;
import t4.EffectiveValueCallback;
import t4.Keyframe;

/* compiled from: FloatKeyframeAnimation.java */
/* renamed from: j4.d, reason: use source file name */
/* loaded from: classes.dex */
public class FloatKeyframeAnimation extends KeyframeAnimation<Float> {
    public FloatKeyframeAnimation(List<Keyframe<Float>> list) {
        super(list);
    }

    public float p() {
        return q(b(), d());
    }

    float q(Keyframe<Float> keyframe, float f10) {
        Float f11;
        if (keyframe.f18570b != null && keyframe.f18571c != null) {
            EffectiveValueCallback<A> effectiveValueCallback = this.f12954e;
            if (effectiveValueCallback != 0 && (f11 = (Float) effectiveValueCallback.b(keyframe.f18575g, keyframe.f18576h.floatValue(), keyframe.f18570b, keyframe.f18571c, f10, e(), f())) != null) {
                return f11.floatValue();
            }
            return MiscUtils.k(keyframe.f(), keyframe.c(), f10);
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j4.BaseKeyframeAnimation
    /* renamed from: r, reason: merged with bridge method [inline-methods] */
    public Float i(Keyframe<Float> keyframe, float f10) {
        return Float.valueOf(q(keyframe, f10));
    }
}
