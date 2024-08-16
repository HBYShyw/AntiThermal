package m4;

import j4.TextKeyframeAnimation;
import java.util.List;
import l4.DocumentData;
import t4.Keyframe;

/* compiled from: AnimatableTextFrame.java */
/* renamed from: m4.j, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableTextFrame extends BaseAnimatableValue<DocumentData, DocumentData> {
    public AnimatableTextFrame(List<Keyframe<DocumentData>> list) {
        super(list);
    }

    @Override // m4.BaseAnimatableValue, m4.AnimatableValue
    public /* bridge */ /* synthetic */ List b() {
        return super.b();
    }

    @Override // m4.AnimatableValue
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public TextKeyframeAnimation a() {
        return new TextKeyframeAnimation(this.f14927a);
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
