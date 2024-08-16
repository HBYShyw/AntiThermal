package m4;

import android.graphics.PointF;
import j4.BaseKeyframeAnimation;
import j4.PathKeyframeAnimation;
import j4.PointKeyframeAnimation;
import java.util.List;
import t4.Keyframe;

/* compiled from: AnimatablePathValue.java */
/* renamed from: m4.e, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatablePathValue implements AnimatableValue<PointF, PointF> {

    /* renamed from: a, reason: collision with root package name */
    private final List<Keyframe<PointF>> f14911a;

    public AnimatablePathValue(List<Keyframe<PointF>> list) {
        this.f14911a = list;
    }

    @Override // m4.AnimatableValue
    public BaseKeyframeAnimation<PointF, PointF> a() {
        if (this.f14911a.get(0).h()) {
            return new PointKeyframeAnimation(this.f14911a);
        }
        return new PathKeyframeAnimation(this.f14911a);
    }

    @Override // m4.AnimatableValue
    public List<Keyframe<PointF>> b() {
        return this.f14911a;
    }

    @Override // m4.AnimatableValue
    public boolean o() {
        return this.f14911a.size() == 1 && this.f14911a.get(0).h();
    }
}
