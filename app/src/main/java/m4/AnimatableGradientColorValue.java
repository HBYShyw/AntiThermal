package m4;

import j4.BaseKeyframeAnimation;
import j4.GradientColorKeyframeAnimation;
import java.util.List;
import n4.GradientColor;
import t4.Keyframe;

/* compiled from: AnimatableGradientColorValue.java */
/* renamed from: m4.c, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableGradientColorValue extends BaseAnimatableValue<GradientColor, GradientColor> {
    public AnimatableGradientColorValue(List<Keyframe<GradientColor>> list) {
        super(list);
    }

    @Override // m4.AnimatableValue
    public BaseKeyframeAnimation<GradientColor, GradientColor> a() {
        return new GradientColorKeyframeAnimation(this.f14927a);
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
