package n4;

import android.graphics.Path;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.GradientFillContent;
import m4.AnimatableFloatValue;
import m4.AnimatableGradientColorValue;
import m4.AnimatableIntegerValue;
import m4.AnimatablePointValue;
import o4.BaseLayer;

/* compiled from: GradientFill.java */
/* renamed from: n4.e, reason: use source file name */
/* loaded from: classes.dex */
public class GradientFill implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final GradientType f15760a;

    /* renamed from: b, reason: collision with root package name */
    private final Path.FillType f15761b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableGradientColorValue f15762c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableIntegerValue f15763d;

    /* renamed from: e, reason: collision with root package name */
    private final AnimatablePointValue f15764e;

    /* renamed from: f, reason: collision with root package name */
    private final AnimatablePointValue f15765f;

    /* renamed from: g, reason: collision with root package name */
    private final String f15766g;

    /* renamed from: h, reason: collision with root package name */
    private final AnimatableFloatValue f15767h;

    /* renamed from: i, reason: collision with root package name */
    private final AnimatableFloatValue f15768i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f15769j;

    public GradientFill(String str, GradientType gradientType, Path.FillType fillType, AnimatableGradientColorValue animatableGradientColorValue, AnimatableIntegerValue animatableIntegerValue, AnimatablePointValue animatablePointValue, AnimatablePointValue animatablePointValue2, AnimatableFloatValue animatableFloatValue, AnimatableFloatValue animatableFloatValue2, boolean z10) {
        this.f15760a = gradientType;
        this.f15761b = fillType;
        this.f15762c = animatableGradientColorValue;
        this.f15763d = animatableIntegerValue;
        this.f15764e = animatablePointValue;
        this.f15765f = animatablePointValue2;
        this.f15766g = str;
        this.f15767h = animatableFloatValue;
        this.f15768i = animatableFloatValue2;
        this.f15769j = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new GradientFillContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public AnimatablePointValue b() {
        return this.f15765f;
    }

    public Path.FillType c() {
        return this.f15761b;
    }

    public AnimatableGradientColorValue d() {
        return this.f15762c;
    }

    public GradientType e() {
        return this.f15760a;
    }

    public String f() {
        return this.f15766g;
    }

    public AnimatableIntegerValue g() {
        return this.f15763d;
    }

    public AnimatablePointValue h() {
        return this.f15764e;
    }

    public boolean i() {
        return this.f15769j;
    }
}
