package m4;

import android.graphics.PointF;
import j4.BaseKeyframeAnimation;
import j4.PointKeyframeAnimation;
import java.util.List;
import t4.Keyframe;

/* compiled from: AnimatablePointValue.java */
/* renamed from: m4.f, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatablePointValue extends BaseAnimatableValue<PointF, PointF> {
    public AnimatablePointValue(List<Keyframe<PointF>> list) {
        super(list);
    }

    @Override // m4.AnimatableValue
    public BaseKeyframeAnimation<PointF, PointF> a() {
        return new PointKeyframeAnimation(this.f14927a);
    }

    @Override // m4.BaseAnimatableValue, m4.AnimatableValue
    public /* bridge */ /* synthetic */ List b() {
        return super.b();
    }

    @Override // m4.BaseAnimatableValue, m4.AnimatableValue
    public /* bridge */ /* synthetic */ boolean o() {
        return super.o();
    }

    @Override // m4.BaseAnimatableValue
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }
}
