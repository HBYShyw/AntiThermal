package m4;

import j4.BaseKeyframeAnimation;
import j4.IntegerKeyframeAnimation;
import java.util.List;
import t4.Keyframe;

/* compiled from: AnimatableIntegerValue.java */
/* renamed from: m4.d, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableIntegerValue extends BaseAnimatableValue<Integer, Integer> {
    public AnimatableIntegerValue(List<Keyframe<Integer>> list) {
        super(list);
    }

    @Override // m4.AnimatableValue
    public BaseKeyframeAnimation<Integer, Integer> a() {
        return new IntegerKeyframeAnimation(this.f14927a);
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
