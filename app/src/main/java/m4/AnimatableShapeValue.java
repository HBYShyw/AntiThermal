package m4;

import android.graphics.Path;
import j4.BaseKeyframeAnimation;
import j4.ShapeKeyframeAnimation;
import java.util.List;
import n4.ShapeData;
import t4.Keyframe;

/* compiled from: AnimatableShapeValue.java */
/* renamed from: m4.h, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableShapeValue extends BaseAnimatableValue<ShapeData, Path> {
    public AnimatableShapeValue(List<Keyframe<ShapeData>> list) {
        super(list);
    }

    @Override // m4.AnimatableValue
    public BaseKeyframeAnimation<ShapeData, Path> a() {
        return new ShapeKeyframeAnimation(this.f14927a);
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
