package n4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.RectangleContent;
import m4.AnimatableFloatValue;
import m4.AnimatableValue;
import o4.BaseLayer;

/* compiled from: RectangleShape.java */
/* renamed from: n4.k, reason: use source file name */
/* loaded from: classes.dex */
public class RectangleShape implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15818a;

    /* renamed from: b, reason: collision with root package name */
    private final AnimatableValue<PointF, PointF> f15819b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableValue<PointF, PointF> f15820c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableFloatValue f15821d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f15822e;

    public RectangleShape(String str, AnimatableValue<PointF, PointF> animatableValue, AnimatableValue<PointF, PointF> animatableValue2, AnimatableFloatValue animatableFloatValue, boolean z10) {
        this.f15818a = str;
        this.f15819b = animatableValue;
        this.f15820c = animatableValue2;
        this.f15821d = animatableFloatValue;
        this.f15822e = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new RectangleContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public AnimatableFloatValue b() {
        return this.f15821d;
    }

    public String c() {
        return this.f15818a;
    }

    public AnimatableValue<PointF, PointF> d() {
        return this.f15819b;
    }

    public AnimatableValue<PointF, PointF> e() {
        return this.f15820c;
    }

    public boolean f() {
        return this.f15822e;
    }

    public String toString() {
        return "RectangleShape{position=" + this.f15819b + ", size=" + this.f15820c + '}';
    }
}
