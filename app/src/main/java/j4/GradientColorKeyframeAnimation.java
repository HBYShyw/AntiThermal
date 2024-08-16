package j4;

import java.util.List;
import n4.GradientColor;
import t4.Keyframe;

/* compiled from: GradientColorKeyframeAnimation.java */
/* renamed from: j4.e, reason: use source file name */
/* loaded from: classes.dex */
public class GradientColorKeyframeAnimation extends KeyframeAnimation<GradientColor> {

    /* renamed from: i, reason: collision with root package name */
    private final GradientColor f12973i;

    public GradientColorKeyframeAnimation(List<Keyframe<GradientColor>> list) {
        super(list);
        GradientColor gradientColor = list.get(0).f18570b;
        int c10 = gradientColor != null ? gradientColor.c() : 0;
        this.f12973i = new GradientColor(new float[c10], new int[c10]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j4.BaseKeyframeAnimation
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public GradientColor i(Keyframe<GradientColor> keyframe, float f10) {
        this.f12973i.d(keyframe.f18570b, keyframe.f18571c, f10);
        return this.f12973i;
    }
}
