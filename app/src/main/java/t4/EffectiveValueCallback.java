package t4;

import j4.BaseKeyframeAnimation;

/* compiled from: EffectiveValueCallback.java */
/* renamed from: t4.b, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveValueCallback<T> {

    /* renamed from: a, reason: collision with root package name */
    private final EffectiveFrameInfo<T> f18566a;

    /* renamed from: b, reason: collision with root package name */
    private BaseKeyframeAnimation<?, ?> f18567b;

    /* renamed from: c, reason: collision with root package name */
    protected T f18568c;

    public EffectiveValueCallback() {
        this.f18566a = new EffectiveFrameInfo<>();
        this.f18568c = null;
    }

    public T a(EffectiveFrameInfo<T> effectiveFrameInfo) {
        return this.f18568c;
    }

    public final T b(float f10, float f11, T t7, T t10, float f12, float f13, float f14) {
        return a(this.f18566a.a(f10, f11, t7, t10, f12, f13, f14));
    }

    public final void c(BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        this.f18567b = baseKeyframeAnimation;
    }

    public EffectiveValueCallback(T t7) {
        this.f18566a = new EffectiveFrameInfo<>();
        this.f18568c = t7;
    }
}
