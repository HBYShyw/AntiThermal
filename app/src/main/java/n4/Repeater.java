package n4;

import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.RepeaterContent;
import m4.AnimatableFloatValue;
import m4.AnimatableTransform;
import o4.BaseLayer;

/* compiled from: Repeater.java */
/* renamed from: n4.l, reason: use source file name */
/* loaded from: classes.dex */
public class Repeater implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15823a;

    /* renamed from: b, reason: collision with root package name */
    private final AnimatableFloatValue f15824b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableFloatValue f15825c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableTransform f15826d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f15827e;

    public Repeater(String str, AnimatableFloatValue animatableFloatValue, AnimatableFloatValue animatableFloatValue2, AnimatableTransform animatableTransform, boolean z10) {
        this.f15823a = str;
        this.f15824b = animatableFloatValue;
        this.f15825c = animatableFloatValue2;
        this.f15826d = animatableTransform;
        this.f15827e = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new RepeaterContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public AnimatableFloatValue b() {
        return this.f15824b;
    }

    public String c() {
        return this.f15823a;
    }

    public AnimatableFloatValue d() {
        return this.f15825c;
    }

    public AnimatableTransform e() {
        return this.f15826d;
    }

    public boolean f() {
        return this.f15827e;
    }
}
