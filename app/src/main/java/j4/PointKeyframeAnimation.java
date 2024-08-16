package j4;

import android.graphics.PointF;
import java.util.List;
import t4.EffectiveValueCallback;
import t4.Keyframe;

/* compiled from: PointKeyframeAnimation.java */
/* renamed from: j4.k, reason: use source file name */
/* loaded from: classes.dex */
public class PointKeyframeAnimation extends KeyframeAnimation<PointF> {

    /* renamed from: i, reason: collision with root package name */
    private final PointF f12983i;

    public PointKeyframeAnimation(List<Keyframe<PointF>> list) {
        super(list);
        this.f12983i = new PointF();
    }

    @Override // j4.BaseKeyframeAnimation
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public PointF i(Keyframe<PointF> keyframe, float f10) {
        return j(keyframe, f10, f10, f10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j4.BaseKeyframeAnimation
    /* renamed from: q, reason: merged with bridge method [inline-methods] */
    public PointF j(Keyframe<PointF> keyframe, float f10, float f11, float f12) {
        PointF pointF;
        PointF pointF2;
        PointF pointF3 = keyframe.f18570b;
        if (pointF3 != null && (pointF = keyframe.f18571c) != null) {
            PointF pointF4 = pointF3;
            PointF pointF5 = pointF;
            EffectiveValueCallback<A> effectiveValueCallback = this.f12954e;
            if (effectiveValueCallback != 0 && (pointF2 = (PointF) effectiveValueCallback.b(keyframe.f18575g, keyframe.f18576h.floatValue(), pointF4, pointF5, f10, e(), f())) != null) {
                return pointF2;
            }
            PointF pointF6 = this.f12983i;
            float f13 = pointF4.x;
            float f14 = f13 + (f11 * (pointF5.x - f13));
            float f15 = pointF4.y;
            pointF6.set(f14, f15 + (f12 * (pointF5.y - f15)));
            return this.f12983i;
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
