package j4;

import android.graphics.Color;
import android.graphics.Paint;
import j4.BaseKeyframeAnimation;
import o4.BaseLayer;
import q4.DropShadowEffect;
import t4.EffectiveFrameInfo;
import t4.EffectiveValueCallback;

/* compiled from: DropShadowKeyframeAnimation.java */
/* renamed from: j4.c, reason: use source file name */
/* loaded from: classes.dex */
public class DropShadowKeyframeAnimation implements BaseKeyframeAnimation.b {

    /* renamed from: a, reason: collision with root package name */
    private final BaseKeyframeAnimation.b f12964a;

    /* renamed from: b, reason: collision with root package name */
    private final BaseKeyframeAnimation<Integer, Integer> f12965b;

    /* renamed from: c, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12966c;

    /* renamed from: d, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12967d;

    /* renamed from: e, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12968e;

    /* renamed from: f, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12969f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f12970g = true;

    /* compiled from: DropShadowKeyframeAnimation.java */
    /* renamed from: j4.c$a */
    /* loaded from: classes.dex */
    class a extends EffectiveValueCallback<Float> {

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ EffectiveValueCallback f12971d;

        a(EffectiveValueCallback effectiveValueCallback) {
            this.f12971d = effectiveValueCallback;
        }

        @Override // t4.EffectiveValueCallback
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public Float a(EffectiveFrameInfo<Float> effectiveFrameInfo) {
            Float f10 = (Float) this.f12971d.a(effectiveFrameInfo);
            if (f10 == null) {
                return null;
            }
            return Float.valueOf(f10.floatValue() * 2.55f);
        }
    }

    public DropShadowKeyframeAnimation(BaseKeyframeAnimation.b bVar, BaseLayer baseLayer, DropShadowEffect dropShadowEffect) {
        this.f12964a = bVar;
        BaseKeyframeAnimation<Integer, Integer> a10 = dropShadowEffect.a().a();
        this.f12965b = a10;
        a10.a(this);
        baseLayer.h(a10);
        BaseKeyframeAnimation<Float, Float> a11 = dropShadowEffect.d().a();
        this.f12966c = a11;
        a11.a(this);
        baseLayer.h(a11);
        BaseKeyframeAnimation<Float, Float> a12 = dropShadowEffect.b().a();
        this.f12967d = a12;
        a12.a(this);
        baseLayer.h(a12);
        BaseKeyframeAnimation<Float, Float> a13 = dropShadowEffect.c().a();
        this.f12968e = a13;
        a13.a(this);
        baseLayer.h(a13);
        BaseKeyframeAnimation<Float, Float> a14 = dropShadowEffect.e().a();
        this.f12969f = a14;
        a14.a(this);
        baseLayer.h(a14);
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        this.f12970g = true;
        this.f12964a.a();
    }

    public void b(Paint paint) {
        if (this.f12970g) {
            this.f12970g = false;
            double floatValue = this.f12967d.h().floatValue() * 0.017453292519943295d;
            float floatValue2 = this.f12968e.h().floatValue();
            float sin = ((float) Math.sin(floatValue)) * floatValue2;
            float cos = ((float) Math.cos(floatValue + 3.141592653589793d)) * floatValue2;
            int intValue = this.f12965b.h().intValue();
            paint.setShadowLayer(this.f12969f.h().floatValue(), sin, cos, Color.argb(Math.round(this.f12966c.h().floatValue()), Color.red(intValue), Color.green(intValue), Color.blue(intValue)));
        }
    }

    public void c(EffectiveValueCallback<Integer> effectiveValueCallback) {
        this.f12965b.n(effectiveValueCallback);
    }

    public void d(EffectiveValueCallback<Float> effectiveValueCallback) {
        this.f12967d.n(effectiveValueCallback);
    }

    public void e(EffectiveValueCallback<Float> effectiveValueCallback) {
        this.f12968e.n(effectiveValueCallback);
    }

    public void f(EffectiveValueCallback<Float> effectiveValueCallback) {
        if (effectiveValueCallback == null) {
            this.f12966c.n(null);
        } else {
            this.f12966c.n(new a(effectiveValueCallback));
        }
    }

    public void g(EffectiveValueCallback<Float> effectiveValueCallback) {
        this.f12969f.n(effectiveValueCallback);
    }
}
