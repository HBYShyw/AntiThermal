package j4;

import java.util.List;
import s4.MiscUtils;
import t4.EffectiveValueCallback;
import t4.Keyframe;
import t4.ScaleXY;

/* compiled from: ScaleKeyframeAnimation.java */
/* renamed from: j4.l, reason: use source file name */
/* loaded from: classes.dex */
public class ScaleKeyframeAnimation extends KeyframeAnimation<ScaleXY> {

    /* renamed from: i, reason: collision with root package name */
    private final ScaleXY f12984i;

    public ScaleKeyframeAnimation(List<Keyframe<ScaleXY>> list) {
        super(list);
        this.f12984i = new ScaleXY();
    }

    @Override // j4.BaseKeyframeAnimation
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public ScaleXY i(Keyframe<ScaleXY> keyframe, float f10) {
        ScaleXY scaleXY;
        ScaleXY scaleXY2;
        ScaleXY scaleXY3 = keyframe.f18570b;
        if (scaleXY3 != null && (scaleXY = keyframe.f18571c) != null) {
            ScaleXY scaleXY4 = scaleXY3;
            ScaleXY scaleXY5 = scaleXY;
            EffectiveValueCallback<A> effectiveValueCallback = this.f12954e;
            if (effectiveValueCallback != 0 && (scaleXY2 = (ScaleXY) effectiveValueCallback.b(keyframe.f18575g, keyframe.f18576h.floatValue(), scaleXY4, scaleXY5, f10, e(), f())) != null) {
                return scaleXY2;
            }
            this.f12984i.d(MiscUtils.k(scaleXY4.b(), scaleXY5.b(), f10), MiscUtils.k(scaleXY4.c(), scaleXY5.c(), f10));
            return this.f12984i;
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
