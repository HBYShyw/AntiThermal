package n4;

import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.TrimPathContent;
import m4.AnimatableFloatValue;
import o4.BaseLayer;

/* compiled from: ShapeTrimPath.java */
/* renamed from: n4.r, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeTrimPath implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15864a;

    /* renamed from: b, reason: collision with root package name */
    private final a f15865b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableFloatValue f15866c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableFloatValue f15867d;

    /* renamed from: e, reason: collision with root package name */
    private final AnimatableFloatValue f15868e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f15869f;

    /* compiled from: ShapeTrimPath.java */
    /* renamed from: n4.r$a */
    /* loaded from: classes.dex */
    public enum a {
        SIMULTANEOUSLY,
        INDIVIDUALLY;

        public static a a(int i10) {
            if (i10 == 1) {
                return SIMULTANEOUSLY;
            }
            if (i10 == 2) {
                return INDIVIDUALLY;
            }
            throw new IllegalArgumentException("Unknown trim path type " + i10);
        }
    }

    public ShapeTrimPath(String str, a aVar, AnimatableFloatValue animatableFloatValue, AnimatableFloatValue animatableFloatValue2, AnimatableFloatValue animatableFloatValue3, boolean z10) {
        this.f15864a = str;
        this.f15865b = aVar;
        this.f15866c = animatableFloatValue;
        this.f15867d = animatableFloatValue2;
        this.f15868e = animatableFloatValue3;
        this.f15869f = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new TrimPathContent(baseLayer, this);
    }

    public AnimatableFloatValue b() {
        return this.f15867d;
    }

    public String c() {
        return this.f15864a;
    }

    public AnimatableFloatValue d() {
        return this.f15868e;
    }

    public AnimatableFloatValue e() {
        return this.f15866c;
    }

    public a f() {
        return this.f15865b;
    }

    public boolean g() {
        return this.f15869f;
    }

    public String toString() {
        return "Trim Path: {start: " + this.f15866c + ", end: " + this.f15867d + ", offset: " + this.f15868e + "}";
    }
}
