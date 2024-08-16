package j4;

import android.graphics.Matrix;
import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationProperty;
import j4.BaseKeyframeAnimation;
import java.util.Collections;
import m4.AnimatableTransform;
import o4.BaseLayer;
import t4.EffectiveValueCallback;
import t4.Keyframe;
import t4.ScaleXY;

/* compiled from: TransformKeyframeAnimation.java */
/* renamed from: j4.p, reason: use source file name */
/* loaded from: classes.dex */
public class TransformKeyframeAnimation {

    /* renamed from: a, reason: collision with root package name */
    private final Matrix f12993a = new Matrix();

    /* renamed from: b, reason: collision with root package name */
    private final Matrix f12994b;

    /* renamed from: c, reason: collision with root package name */
    private final Matrix f12995c;

    /* renamed from: d, reason: collision with root package name */
    private final Matrix f12996d;

    /* renamed from: e, reason: collision with root package name */
    private final float[] f12997e;

    /* renamed from: f, reason: collision with root package name */
    private BaseKeyframeAnimation<PointF, PointF> f12998f;

    /* renamed from: g, reason: collision with root package name */
    private BaseKeyframeAnimation<?, PointF> f12999g;

    /* renamed from: h, reason: collision with root package name */
    private BaseKeyframeAnimation<ScaleXY, ScaleXY> f13000h;

    /* renamed from: i, reason: collision with root package name */
    private BaseKeyframeAnimation<Float, Float> f13001i;

    /* renamed from: j, reason: collision with root package name */
    private BaseKeyframeAnimation<Integer, Integer> f13002j;

    /* renamed from: k, reason: collision with root package name */
    private FloatKeyframeAnimation f13003k;

    /* renamed from: l, reason: collision with root package name */
    private FloatKeyframeAnimation f13004l;

    /* renamed from: m, reason: collision with root package name */
    private BaseKeyframeAnimation<?, Float> f13005m;

    /* renamed from: n, reason: collision with root package name */
    private BaseKeyframeAnimation<?, Float> f13006n;

    public TransformKeyframeAnimation(AnimatableTransform animatableTransform) {
        this.f12998f = animatableTransform.c() == null ? null : animatableTransform.c().a();
        this.f12999g = animatableTransform.f() == null ? null : animatableTransform.f().a();
        this.f13000h = animatableTransform.h() == null ? null : animatableTransform.h().a();
        this.f13001i = animatableTransform.g() == null ? null : animatableTransform.g().a();
        FloatKeyframeAnimation floatKeyframeAnimation = animatableTransform.i() == null ? null : (FloatKeyframeAnimation) animatableTransform.i().a();
        this.f13003k = floatKeyframeAnimation;
        if (floatKeyframeAnimation != null) {
            this.f12994b = new Matrix();
            this.f12995c = new Matrix();
            this.f12996d = new Matrix();
            this.f12997e = new float[9];
        } else {
            this.f12994b = null;
            this.f12995c = null;
            this.f12996d = null;
            this.f12997e = null;
        }
        this.f13004l = animatableTransform.j() == null ? null : (FloatKeyframeAnimation) animatableTransform.j().a();
        if (animatableTransform.e() != null) {
            this.f13002j = animatableTransform.e().a();
        }
        if (animatableTransform.k() != null) {
            this.f13005m = animatableTransform.k().a();
        } else {
            this.f13005m = null;
        }
        if (animatableTransform.d() != null) {
            this.f13006n = animatableTransform.d().a();
        } else {
            this.f13006n = null;
        }
    }

    private void d() {
        for (int i10 = 0; i10 < 9; i10++) {
            this.f12997e[i10] = 0.0f;
        }
    }

    public void a(BaseLayer baseLayer) {
        baseLayer.h(this.f13002j);
        baseLayer.h(this.f13005m);
        baseLayer.h(this.f13006n);
        baseLayer.h(this.f12998f);
        baseLayer.h(this.f12999g);
        baseLayer.h(this.f13000h);
        baseLayer.h(this.f13001i);
        baseLayer.h(this.f13003k);
        baseLayer.h(this.f13004l);
    }

    public void b(BaseKeyframeAnimation.b bVar) {
        BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = this.f13002j;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.a(bVar);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2 = this.f13005m;
        if (baseKeyframeAnimation2 != null) {
            baseKeyframeAnimation2.a(bVar);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation3 = this.f13006n;
        if (baseKeyframeAnimation3 != null) {
            baseKeyframeAnimation3.a(bVar);
        }
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = this.f12998f;
        if (baseKeyframeAnimation4 != null) {
            baseKeyframeAnimation4.a(bVar);
        }
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation5 = this.f12999g;
        if (baseKeyframeAnimation5 != null) {
            baseKeyframeAnimation5.a(bVar);
        }
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation6 = this.f13000h;
        if (baseKeyframeAnimation6 != null) {
            baseKeyframeAnimation6.a(bVar);
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation7 = this.f13001i;
        if (baseKeyframeAnimation7 != null) {
            baseKeyframeAnimation7.a(bVar);
        }
        FloatKeyframeAnimation floatKeyframeAnimation = this.f13003k;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.a(bVar);
        }
        FloatKeyframeAnimation floatKeyframeAnimation2 = this.f13004l;
        if (floatKeyframeAnimation2 != null) {
            floatKeyframeAnimation2.a(bVar);
        }
    }

    public <T> boolean c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        if (t7 == EffectiveAnimationProperty.f9680f) {
            BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation = this.f12998f;
            if (baseKeyframeAnimation == null) {
                this.f12998f = new ValueCallbackKeyframeAnimation(effectiveValueCallback, new PointF());
                return true;
            }
            baseKeyframeAnimation.n(effectiveValueCallback);
            return true;
        }
        if (t7 == EffectiveAnimationProperty.f9681g) {
            BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation2 = this.f12999g;
            if (baseKeyframeAnimation2 == null) {
                this.f12999g = new ValueCallbackKeyframeAnimation(effectiveValueCallback, new PointF());
                return true;
            }
            baseKeyframeAnimation2.n(effectiveValueCallback);
            return true;
        }
        if (t7 == EffectiveAnimationProperty.f9682h) {
            BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation3 = this.f12999g;
            if (baseKeyframeAnimation3 instanceof SplitDimensionPathKeyframeAnimation) {
                ((SplitDimensionPathKeyframeAnimation) baseKeyframeAnimation3).r(effectiveValueCallback);
                return true;
            }
        }
        if (t7 == EffectiveAnimationProperty.f9683i) {
            BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation4 = this.f12999g;
            if (baseKeyframeAnimation4 instanceof SplitDimensionPathKeyframeAnimation) {
                ((SplitDimensionPathKeyframeAnimation) baseKeyframeAnimation4).s(effectiveValueCallback);
                return true;
            }
        }
        if (t7 == EffectiveAnimationProperty.f9689o) {
            BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation5 = this.f13000h;
            if (baseKeyframeAnimation5 == null) {
                this.f13000h = new ValueCallbackKeyframeAnimation(effectiveValueCallback, new ScaleXY());
                return true;
            }
            baseKeyframeAnimation5.n(effectiveValueCallback);
            return true;
        }
        if (t7 == EffectiveAnimationProperty.f9690p) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation6 = this.f13001i;
            if (baseKeyframeAnimation6 == null) {
                this.f13001i = new ValueCallbackKeyframeAnimation(effectiveValueCallback, Float.valueOf(0.0f));
                return true;
            }
            baseKeyframeAnimation6.n(effectiveValueCallback);
            return true;
        }
        if (t7 == EffectiveAnimationProperty.f9677c) {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation7 = this.f13002j;
            if (baseKeyframeAnimation7 == null) {
                this.f13002j = new ValueCallbackKeyframeAnimation(effectiveValueCallback, 100);
                return true;
            }
            baseKeyframeAnimation7.n(effectiveValueCallback);
            return true;
        }
        if (t7 == EffectiveAnimationProperty.C) {
            BaseKeyframeAnimation<?, Float> baseKeyframeAnimation8 = this.f13005m;
            if (baseKeyframeAnimation8 == null) {
                this.f13005m = new ValueCallbackKeyframeAnimation(effectiveValueCallback, Float.valueOf(100.0f));
                return true;
            }
            baseKeyframeAnimation8.n(effectiveValueCallback);
            return true;
        }
        if (t7 == EffectiveAnimationProperty.D) {
            BaseKeyframeAnimation<?, Float> baseKeyframeAnimation9 = this.f13006n;
            if (baseKeyframeAnimation9 == null) {
                this.f13006n = new ValueCallbackKeyframeAnimation(effectiveValueCallback, Float.valueOf(100.0f));
                return true;
            }
            baseKeyframeAnimation9.n(effectiveValueCallback);
            return true;
        }
        if (t7 == EffectiveAnimationProperty.f9691q) {
            if (this.f13003k == null) {
                this.f13003k = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
            }
            this.f13003k.n(effectiveValueCallback);
            return true;
        }
        if (t7 != EffectiveAnimationProperty.f9692r) {
            return false;
        }
        if (this.f13004l == null) {
            this.f13004l = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
        }
        this.f13004l.n(effectiveValueCallback);
        return true;
    }

    public BaseKeyframeAnimation<?, Float> e() {
        return this.f13006n;
    }

    public Matrix f() {
        float p10;
        this.f12993a.reset();
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation = this.f12999g;
        if (baseKeyframeAnimation != null) {
            PointF h10 = baseKeyframeAnimation.h();
            float f10 = h10.x;
            if (f10 != 0.0f || h10.y != 0.0f) {
                this.f12993a.preTranslate(f10, h10.y);
            }
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.f13001i;
        if (baseKeyframeAnimation2 != null) {
            if (baseKeyframeAnimation2 instanceof ValueCallbackKeyframeAnimation) {
                p10 = baseKeyframeAnimation2.h().floatValue();
            } else {
                p10 = ((FloatKeyframeAnimation) baseKeyframeAnimation2).p();
            }
            if (p10 != 0.0f) {
                this.f12993a.preRotate(p10);
            }
        }
        if (this.f13003k != null) {
            float cos = this.f13004l == null ? 0.0f : (float) Math.cos(Math.toRadians((-r3.p()) + 90.0f));
            float sin = this.f13004l == null ? 1.0f : (float) Math.sin(Math.toRadians((-r5.p()) + 90.0f));
            float tan = (float) Math.tan(Math.toRadians(r0.p()));
            d();
            float[] fArr = this.f12997e;
            fArr[0] = cos;
            fArr[1] = sin;
            float f11 = -sin;
            fArr[3] = f11;
            fArr[4] = cos;
            fArr[8] = 1.0f;
            this.f12994b.setValues(fArr);
            d();
            float[] fArr2 = this.f12997e;
            fArr2[0] = 1.0f;
            fArr2[3] = tan;
            fArr2[4] = 1.0f;
            fArr2[8] = 1.0f;
            this.f12995c.setValues(fArr2);
            d();
            float[] fArr3 = this.f12997e;
            fArr3[0] = cos;
            fArr3[1] = f11;
            fArr3[3] = sin;
            fArr3[4] = cos;
            fArr3[8] = 1.0f;
            this.f12996d.setValues(fArr3);
            this.f12995c.preConcat(this.f12994b);
            this.f12996d.preConcat(this.f12995c);
            this.f12993a.preConcat(this.f12996d);
        }
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation3 = this.f13000h;
        if (baseKeyframeAnimation3 != null) {
            ScaleXY h11 = baseKeyframeAnimation3.h();
            if (h11.b() != 1.0f || h11.c() != 1.0f) {
                this.f12993a.preScale(h11.b(), h11.c());
            }
        }
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = this.f12998f;
        if (baseKeyframeAnimation4 != null) {
            PointF h12 = baseKeyframeAnimation4.h();
            float f12 = h12.x;
            if (f12 != 0.0f || h12.y != 0.0f) {
                this.f12993a.preTranslate(-f12, -h12.y);
            }
        }
        return this.f12993a;
    }

    public Matrix g(float f10) {
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation = this.f12999g;
        PointF h10 = baseKeyframeAnimation == null ? null : baseKeyframeAnimation.h();
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation2 = this.f13000h;
        ScaleXY h11 = baseKeyframeAnimation2 == null ? null : baseKeyframeAnimation2.h();
        this.f12993a.reset();
        if (h10 != null) {
            this.f12993a.preTranslate(h10.x * f10, h10.y * f10);
        }
        if (h11 != null) {
            double d10 = f10;
            this.f12993a.preScale((float) Math.pow(h11.b(), d10), (float) Math.pow(h11.c(), d10));
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation3 = this.f13001i;
        if (baseKeyframeAnimation3 != null) {
            float floatValue = baseKeyframeAnimation3.h().floatValue();
            BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = this.f12998f;
            PointF h12 = baseKeyframeAnimation4 != null ? baseKeyframeAnimation4.h() : null;
            this.f12993a.preRotate(floatValue * f10, h12 == null ? 0.0f : h12.x, h12 != null ? h12.y : 0.0f);
        }
        return this.f12993a;
    }

    public BaseKeyframeAnimation<?, Integer> h() {
        return this.f13002j;
    }

    public BaseKeyframeAnimation<?, Float> i() {
        return this.f13005m;
    }

    public void j(float f10) {
        BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = this.f13002j;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.m(f10);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2 = this.f13005m;
        if (baseKeyframeAnimation2 != null) {
            baseKeyframeAnimation2.m(f10);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation3 = this.f13006n;
        if (baseKeyframeAnimation3 != null) {
            baseKeyframeAnimation3.m(f10);
        }
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = this.f12998f;
        if (baseKeyframeAnimation4 != null) {
            baseKeyframeAnimation4.m(f10);
        }
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation5 = this.f12999g;
        if (baseKeyframeAnimation5 != null) {
            baseKeyframeAnimation5.m(f10);
        }
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation6 = this.f13000h;
        if (baseKeyframeAnimation6 != null) {
            baseKeyframeAnimation6.m(f10);
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation7 = this.f13001i;
        if (baseKeyframeAnimation7 != null) {
            baseKeyframeAnimation7.m(f10);
        }
        FloatKeyframeAnimation floatKeyframeAnimation = this.f13003k;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.m(f10);
        }
        FloatKeyframeAnimation floatKeyframeAnimation2 = this.f13004l;
        if (floatKeyframeAnimation2 != null) {
            floatKeyframeAnimation2.m(f10);
        }
    }
}
