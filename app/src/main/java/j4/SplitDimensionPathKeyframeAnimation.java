package j4;

import android.graphics.PointF;
import java.util.Collections;
import t4.EffectiveValueCallback;
import t4.Keyframe;

/* compiled from: SplitDimensionPathKeyframeAnimation.java */
/* renamed from: j4.n, reason: use source file name */
/* loaded from: classes.dex */
public class SplitDimensionPathKeyframeAnimation extends BaseKeyframeAnimation<PointF, PointF> {

    /* renamed from: i, reason: collision with root package name */
    private final PointF f12987i;

    /* renamed from: j, reason: collision with root package name */
    private final PointF f12988j;

    /* renamed from: k, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12989k;

    /* renamed from: l, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12990l;

    /* renamed from: m, reason: collision with root package name */
    protected EffectiveValueCallback<Float> f12991m;

    /* renamed from: n, reason: collision with root package name */
    protected EffectiveValueCallback<Float> f12992n;

    public SplitDimensionPathKeyframeAnimation(BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation, BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2) {
        super(Collections.emptyList());
        this.f12987i = new PointF();
        this.f12988j = new PointF();
        this.f12989k = baseKeyframeAnimation;
        this.f12990l = baseKeyframeAnimation2;
        m(f());
    }

    @Override // j4.BaseKeyframeAnimation
    public void m(float f10) {
        this.f12989k.m(f10);
        this.f12990l.m(f10);
        this.f12987i.set(this.f12989k.h().floatValue(), this.f12990l.h().floatValue());
        for (int i10 = 0; i10 < this.f12950a.size(); i10++) {
            this.f12950a.get(i10).a();
        }
    }

    @Override // j4.BaseKeyframeAnimation
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public PointF h() {
        return i(null, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j4.BaseKeyframeAnimation
    /* renamed from: q, reason: merged with bridge method [inline-methods] */
    public PointF i(Keyframe<PointF> keyframe, float f10) {
        Float f11;
        Keyframe<Float> b10;
        Keyframe<Float> b11;
        Float f12 = null;
        if (this.f12991m == null || (b11 = this.f12989k.b()) == null) {
            f11 = null;
        } else {
            float d10 = this.f12989k.d();
            Float f13 = b11.f18576h;
            EffectiveValueCallback<Float> effectiveValueCallback = this.f12991m;
            float f14 = b11.f18575g;
            f11 = effectiveValueCallback.b(f14, f13 == null ? f14 : f13.floatValue(), b11.f18570b, b11.f18571c, f10, f10, d10);
        }
        if (this.f12992n != null && (b10 = this.f12990l.b()) != null) {
            float d11 = this.f12990l.d();
            Float f15 = b10.f18576h;
            EffectiveValueCallback<Float> effectiveValueCallback2 = this.f12992n;
            float f16 = b10.f18575g;
            f12 = effectiveValueCallback2.b(f16, f15 == null ? f16 : f15.floatValue(), b10.f18570b, b10.f18571c, f10, f10, d11);
        }
        if (f11 == null) {
            this.f12988j.set(this.f12987i.x, 0.0f);
        } else {
            this.f12988j.set(f11.floatValue(), 0.0f);
        }
        if (f12 == null) {
            PointF pointF = this.f12988j;
            pointF.set(pointF.x, this.f12987i.y);
        } else {
            PointF pointF2 = this.f12988j;
            pointF2.set(pointF2.x, f12.floatValue());
        }
        return this.f12988j;
    }

    public void r(EffectiveValueCallback<Float> effectiveValueCallback) {
        EffectiveValueCallback<Float> effectiveValueCallback2 = this.f12991m;
        if (effectiveValueCallback2 != null) {
            effectiveValueCallback2.c(null);
        }
        this.f12991m = effectiveValueCallback;
        if (effectiveValueCallback != null) {
            effectiveValueCallback.c(this);
        }
    }

    public void s(EffectiveValueCallback<Float> effectiveValueCallback) {
        EffectiveValueCallback<Float> effectiveValueCallback2 = this.f12992n;
        if (effectiveValueCallback2 != null) {
            effectiveValueCallback2.c(null);
        }
        this.f12992n = effectiveValueCallback;
        if (effectiveValueCallback != null) {
            effectiveValueCallback.c(this);
        }
    }
}
