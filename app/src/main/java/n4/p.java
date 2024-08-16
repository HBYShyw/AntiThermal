package n4;

import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.ShapeContent;
import m4.AnimatableShapeValue;
import o4.BaseLayer;

/* compiled from: ShapePath.java */
/* loaded from: classes.dex */
public class p implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15840a;

    /* renamed from: b, reason: collision with root package name */
    private final int f15841b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableShapeValue f15842c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f15843d;

    public p(String str, int i10, AnimatableShapeValue animatableShapeValue, boolean z10) {
        this.f15840a = str;
        this.f15841b = i10;
        this.f15842c = animatableShapeValue;
        this.f15843d = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new ShapeContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public String b() {
        return this.f15840a;
    }

    public AnimatableShapeValue c() {
        return this.f15842c;
    }

    public boolean d() {
        return this.f15843d;
    }

    public String toString() {
        return "ShapePath{name=" + this.f15840a + ", index=" + this.f15841b + '}';
    }
}
