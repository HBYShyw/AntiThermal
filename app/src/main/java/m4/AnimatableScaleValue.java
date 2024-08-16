package m4;

import j4.BaseKeyframeAnimation;
import j4.ScaleKeyframeAnimation;
import java.util.List;
import t4.Keyframe;
import t4.ScaleXY;

/* compiled from: AnimatableScaleValue.java */
/* renamed from: m4.g, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableScaleValue extends BaseAnimatableValue<ScaleXY, ScaleXY> {
    public AnimatableScaleValue(List<Keyframe<ScaleXY>> list) {
        super(list);
    }

    @Override // m4.AnimatableValue
    public BaseKeyframeAnimation<ScaleXY, ScaleXY> a() {
        return new ScaleKeyframeAnimation(this.f14927a);
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
