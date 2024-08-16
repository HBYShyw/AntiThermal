package n4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.EllipseContent;
import m4.AnimatablePointValue;
import m4.AnimatableValue;
import o4.BaseLayer;

/* compiled from: CircleShape.java */
/* renamed from: n4.b, reason: use source file name */
/* loaded from: classes.dex */
public class CircleShape implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15753a;

    /* renamed from: b, reason: collision with root package name */
    private final AnimatableValue<PointF, PointF> f15754b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatablePointValue f15755c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f15756d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f15757e;

    public CircleShape(String str, AnimatableValue<PointF, PointF> animatableValue, AnimatablePointValue animatablePointValue, boolean z10, boolean z11) {
        this.f15753a = str;
        this.f15754b = animatableValue;
        this.f15755c = animatablePointValue;
        this.f15756d = z10;
        this.f15757e = z11;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new EllipseContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public String b() {
        return this.f15753a;
    }

    public AnimatableValue<PointF, PointF> c() {
        return this.f15754b;
    }

    public AnimatablePointValue d() {
        return this.f15755c;
    }

    public boolean e() {
        return this.f15757e;
    }

    public boolean f() {
        return this.f15756d;
    }
}
