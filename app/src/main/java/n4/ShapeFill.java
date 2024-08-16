package n4;

import android.graphics.Path;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.FillContent;
import m4.AnimatableColorValue;
import m4.AnimatableIntegerValue;
import o4.BaseLayer;

/* compiled from: ShapeFill.java */
/* renamed from: n4.n, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeFill implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f15831a;

    /* renamed from: b, reason: collision with root package name */
    private final Path.FillType f15832b;

    /* renamed from: c, reason: collision with root package name */
    private final String f15833c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableColorValue f15834d;

    /* renamed from: e, reason: collision with root package name */
    private final AnimatableIntegerValue f15835e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f15836f;

    public ShapeFill(String str, boolean z10, Path.FillType fillType, AnimatableColorValue animatableColorValue, AnimatableIntegerValue animatableIntegerValue, boolean z11) {
        this.f15833c = str;
        this.f15831a = z10;
        this.f15832b = fillType;
        this.f15834d = animatableColorValue;
        this.f15835e = animatableIntegerValue;
        this.f15836f = z11;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new FillContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public AnimatableColorValue b() {
        return this.f15834d;
    }

    public Path.FillType c() {
        return this.f15832b;
    }

    public String d() {
        return this.f15833c;
    }

    public AnimatableIntegerValue e() {
        return this.f15835e;
    }

    public boolean f() {
        return this.f15836f;
    }

    public String toString() {
        return "ShapeFill{color=, fillEnabled=" + this.f15831a + '}';
    }
}
