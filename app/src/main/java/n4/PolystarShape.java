package n4;

import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.PolystarContent;
import m4.AnimatableFloatValue;
import m4.AnimatableValue;
import o4.BaseLayer;

/* compiled from: PolystarShape.java */
/* renamed from: n4.j, reason: use source file name */
/* loaded from: classes.dex */
public class PolystarShape implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15804a;

    /* renamed from: b, reason: collision with root package name */
    private final a f15805b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableFloatValue f15806c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableValue<PointF, PointF> f15807d;

    /* renamed from: e, reason: collision with root package name */
    private final AnimatableFloatValue f15808e;

    /* renamed from: f, reason: collision with root package name */
    private final AnimatableFloatValue f15809f;

    /* renamed from: g, reason: collision with root package name */
    private final AnimatableFloatValue f15810g;

    /* renamed from: h, reason: collision with root package name */
    private final AnimatableFloatValue f15811h;

    /* renamed from: i, reason: collision with root package name */
    private final AnimatableFloatValue f15812i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f15813j;

    /* compiled from: PolystarShape.java */
    /* renamed from: n4.j$a */
    /* loaded from: classes.dex */
    public enum a {
        STAR(1),
        POLYGON(2);


        /* renamed from: e, reason: collision with root package name */
        private final int f15817e;

        a(int i10) {
            this.f15817e = i10;
        }

        public static a a(int i10) {
            for (a aVar : values()) {
                if (aVar.f15817e == i10) {
                    return aVar;
                }
            }
            return null;
        }
    }

    public PolystarShape(String str, a aVar, AnimatableFloatValue animatableFloatValue, AnimatableValue<PointF, PointF> animatableValue, AnimatableFloatValue animatableFloatValue2, AnimatableFloatValue animatableFloatValue3, AnimatableFloatValue animatableFloatValue4, AnimatableFloatValue animatableFloatValue5, AnimatableFloatValue animatableFloatValue6, boolean z10) {
        this.f15804a = str;
        this.f15805b = aVar;
        this.f15806c = animatableFloatValue;
        this.f15807d = animatableValue;
        this.f15808e = animatableFloatValue2;
        this.f15809f = animatableFloatValue3;
        this.f15810g = animatableFloatValue4;
        this.f15811h = animatableFloatValue5;
        this.f15812i = animatableFloatValue6;
        this.f15813j = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new PolystarContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public AnimatableFloatValue b() {
        return this.f15809f;
    }

    public AnimatableFloatValue c() {
        return this.f15811h;
    }

    public String d() {
        return this.f15804a;
    }

    public AnimatableFloatValue e() {
        return this.f15810g;
    }

    public AnimatableFloatValue f() {
        return this.f15812i;
    }

    public AnimatableFloatValue g() {
        return this.f15806c;
    }

    public AnimatableValue<PointF, PointF> h() {
        return this.f15807d;
    }

    public AnimatableFloatValue i() {
        return this.f15808e;
    }

    public a j() {
        return this.f15805b;
    }

    public boolean k() {
        return this.f15813j;
    }
}
