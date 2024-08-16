package m4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import j4.TransformKeyframeAnimation;
import n4.ContentModel;
import o4.BaseLayer;

/* compiled from: AnimatableTransform.java */
/* renamed from: m4.l, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatableTransform implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final AnimatablePathValue f14918a;

    /* renamed from: b, reason: collision with root package name */
    private final AnimatableValue<PointF, PointF> f14919b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableScaleValue f14920c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableFloatValue f14921d;

    /* renamed from: e, reason: collision with root package name */
    private final AnimatableIntegerValue f14922e;

    /* renamed from: f, reason: collision with root package name */
    private final AnimatableFloatValue f14923f;

    /* renamed from: g, reason: collision with root package name */
    private final AnimatableFloatValue f14924g;

    /* renamed from: h, reason: collision with root package name */
    private final AnimatableFloatValue f14925h;

    /* renamed from: i, reason: collision with root package name */
    private final AnimatableFloatValue f14926i;

    public AnimatableTransform() {
        this(null, null, null, null, null, null, null, null, null);
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return null;
    }

    public TransformKeyframeAnimation b() {
        return new TransformKeyframeAnimation(this);
    }

    public AnimatablePathValue c() {
        return this.f14918a;
    }

    public AnimatableFloatValue d() {
        return this.f14926i;
    }

    public AnimatableIntegerValue e() {
        return this.f14922e;
    }

    public AnimatableValue<PointF, PointF> f() {
        return this.f14919b;
    }

    public AnimatableFloatValue g() {
        return this.f14921d;
    }

    public AnimatableScaleValue h() {
        return this.f14920c;
    }

    public AnimatableFloatValue i() {
        return this.f14923f;
    }

    public AnimatableFloatValue j() {
        return this.f14924g;
    }

    public AnimatableFloatValue k() {
        return this.f14925h;
    }

    public AnimatableTransform(AnimatablePathValue animatablePathValue, AnimatableValue<PointF, PointF> animatableValue, AnimatableScaleValue animatableScaleValue, AnimatableFloatValue animatableFloatValue, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue2, AnimatableFloatValue animatableFloatValue3, AnimatableFloatValue animatableFloatValue4, AnimatableFloatValue animatableFloatValue5) {
        this.f14918a = animatablePathValue;
        this.f14919b = animatableValue;
        this.f14920c = animatableScaleValue;
        this.f14921d = animatableFloatValue;
        this.f14922e = animatableIntegerValue;
        this.f14925h = animatableFloatValue2;
        this.f14926i = animatableFloatValue3;
        this.f14923f = animatableFloatValue4;
        this.f14924g = animatableFloatValue5;
    }
}
