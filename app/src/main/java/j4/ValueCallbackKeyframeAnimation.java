package j4;

import java.util.Collections;
import t4.EffectiveValueCallback;
import t4.Keyframe;

/* compiled from: ValueCallbackKeyframeAnimation.java */
/* renamed from: j4.q, reason: use source file name */
/* loaded from: classes.dex */
public class ValueCallbackKeyframeAnimation<K, A> extends BaseKeyframeAnimation<K, A> {

    /* renamed from: i, reason: collision with root package name */
    private final A f13007i;

    public ValueCallbackKeyframeAnimation(EffectiveValueCallback<A> effectiveValueCallback) {
        this(effectiveValueCallback, null);
    }

    @Override // j4.BaseKeyframeAnimation
    float c() {
        return 1.0f;
    }

    @Override // j4.BaseKeyframeAnimation
    public A h() {
        EffectiveValueCallback<A> effectiveValueCallback = this.f12954e;
        A a10 = this.f13007i;
        return effectiveValueCallback.b(0.0f, 0.0f, a10, a10, f(), f(), f());
    }

    @Override // j4.BaseKeyframeAnimation
    A i(Keyframe<K> keyframe, float f10) {
        return h();
    }

    @Override // j4.BaseKeyframeAnimation
    public void k() {
        if (this.f12954e != null) {
            super.k();
        }
    }

    @Override // j4.BaseKeyframeAnimation
    public void m(float f10) {
        this.f12953d = f10;
    }

    public ValueCallbackKeyframeAnimation(EffectiveValueCallback<A> effectiveValueCallback, A a10) {
        super(Collections.emptyList());
        n(effectiveValueCallback);
        this.f13007i = a10;
    }
}
