package m4;

import android.graphics.PointF;
import j4.BaseKeyframeAnimation;
import j4.SplitDimensionPathKeyframeAnimation;
import java.util.List;
import t4.Keyframe;

/* compiled from: AnimatableSplitDimensionPathValue.java */
/* renamed from: m4.i, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableSplitDimensionPathValue implements AnimatableValue<PointF, PointF> {

    /* renamed from: a, reason: collision with root package name */
    private final AnimatableFloatValue f14912a;

    /* renamed from: b, reason: collision with root package name */
    private final AnimatableFloatValue f14913b;

    public AnimatableSplitDimensionPathValue(AnimatableFloatValue animatableFloatValue, AnimatableFloatValue animatableFloatValue2) {
        this.f14912a = animatableFloatValue;
        this.f14913b = animatableFloatValue2;
    }

    @Override // m4.AnimatableValue
    public BaseKeyframeAnimation<PointF, PointF> a() {
        return new SplitDimensionPathKeyframeAnimation(this.f14912a.a(), this.f14913b.a());
    }

    @Override // m4.AnimatableValue
    public List<Keyframe<PointF>> b() {
        throw new UnsupportedOperationException("Cannot call getKeyframes on AnimatableSplitDimensionPathValue.");
    }

    @Override // m4.AnimatableValue
    public boolean o() {
        return this.f14912a.o() && this.f14913b.o();
    }
}
