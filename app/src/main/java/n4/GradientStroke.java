package n4;

import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.GradientStrokeContent;
import java.util.List;
import m4.AnimatableFloatValue;
import m4.AnimatableGradientColorValue;
import m4.AnimatableIntegerValue;
import m4.AnimatablePointValue;
import n4.ShapeStroke;
import o4.BaseLayer;

/* compiled from: GradientStroke.java */
/* renamed from: n4.f, reason: use source file name */
/* loaded from: classes.dex */
public class GradientStroke implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15770a;

    /* renamed from: b, reason: collision with root package name */
    private final GradientType f15771b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableGradientColorValue f15772c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableIntegerValue f15773d;

    /* renamed from: e, reason: collision with root package name */
    private final AnimatablePointValue f15774e;

    /* renamed from: f, reason: collision with root package name */
    private final AnimatablePointValue f15775f;

    /* renamed from: g, reason: collision with root package name */
    private final AnimatableFloatValue f15776g;

    /* renamed from: h, reason: collision with root package name */
    private final ShapeStroke.b f15777h;

    /* renamed from: i, reason: collision with root package name */
    private final ShapeStroke.c f15778i;

    /* renamed from: j, reason: collision with root package name */
    private final float f15779j;

    /* renamed from: k, reason: collision with root package name */
    private final List<AnimatableFloatValue> f15780k;

    /* renamed from: l, reason: collision with root package name */
    private final AnimatableFloatValue f15781l;

    /* renamed from: m, reason: collision with root package name */
    private final boolean f15782m;

    public GradientStroke(String str, GradientType gradientType, AnimatableGradientColorValue animatableGradientColorValue, AnimatableIntegerValue animatableIntegerValue, AnimatablePointValue animatablePointValue, AnimatablePointValue animatablePointValue2, AnimatableFloatValue animatableFloatValue, ShapeStroke.b bVar, ShapeStroke.c cVar, float f10, List<AnimatableFloatValue> list, AnimatableFloatValue animatableFloatValue2, boolean z10) {
        this.f15770a = str;
        this.f15771b = gradientType;
        this.f15772c = animatableGradientColorValue;
        this.f15773d = animatableIntegerValue;
        this.f15774e = animatablePointValue;
        this.f15775f = animatablePointValue2;
        this.f15776g = animatableFloatValue;
        this.f15777h = bVar;
        this.f15778i = cVar;
        this.f15779j = f10;
        this.f15780k = list;
        this.f15781l = animatableFloatValue2;
        this.f15782m = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new GradientStrokeContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public ShapeStroke.b b() {
        return this.f15777h;
    }

    public AnimatableFloatValue c() {
        return this.f15781l;
    }

    public AnimatablePointValue d() {
        return this.f15775f;
    }

    public AnimatableGradientColorValue e() {
        return this.f15772c;
    }

    public GradientType f() {
        return this.f15771b;
    }

    public ShapeStroke.c g() {
        return this.f15778i;
    }

    public List<AnimatableFloatValue> h() {
        return this.f15780k;
    }

    public float i() {
        return this.f15779j;
    }

    public String j() {
        return this.f15770a;
    }

    public AnimatableIntegerValue k() {
        return this.f15773d;
    }

    public AnimatablePointValue l() {
        return this.f15774e;
    }

    public AnimatableFloatValue m() {
        return this.f15776g;
    }

    public boolean n() {
        return this.f15782m;
    }
}
