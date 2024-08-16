package i4;

import android.graphics.Path;
import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import j4.BaseKeyframeAnimation;
import java.util.List;
import l4.KeyPath;
import n4.PolystarShape;
import n4.ShapeTrimPath;
import o4.BaseLayer;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: PolystarContent.java */
/* renamed from: i4.n, reason: use source file name */
/* loaded from: classes.dex */
public class PolystarContent implements PathContent, BaseKeyframeAnimation.b, KeyPathElementContent {

    /* renamed from: b, reason: collision with root package name */
    private final String f12591b;

    /* renamed from: c, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12592c;

    /* renamed from: d, reason: collision with root package name */
    private final PolystarShape.a f12593d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f12594e;

    /* renamed from: f, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12595f;

    /* renamed from: g, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, PointF> f12596g;

    /* renamed from: h, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12597h;

    /* renamed from: i, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12598i;

    /* renamed from: j, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12599j;

    /* renamed from: k, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12600k;

    /* renamed from: l, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12601l;

    /* renamed from: n, reason: collision with root package name */
    private boolean f12603n;

    /* renamed from: a, reason: collision with root package name */
    private final Path f12590a = new Path();

    /* renamed from: m, reason: collision with root package name */
    private final CompoundTrimPathContent f12602m = new CompoundTrimPathContent();

    /* compiled from: PolystarContent.java */
    /* renamed from: i4.n$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f12604a;

        static {
            int[] iArr = new int[PolystarShape.a.values().length];
            f12604a = iArr;
            try {
                iArr[PolystarShape.a.STAR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f12604a[PolystarShape.a.POLYGON.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public PolystarContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, PolystarShape polystarShape) {
        this.f12592c = effectiveAnimationDrawable;
        this.f12591b = polystarShape.d();
        PolystarShape.a j10 = polystarShape.j();
        this.f12593d = j10;
        this.f12594e = polystarShape.k();
        BaseKeyframeAnimation<Float, Float> a10 = polystarShape.g().a();
        this.f12595f = a10;
        BaseKeyframeAnimation<PointF, PointF> a11 = polystarShape.h().a();
        this.f12596g = a11;
        BaseKeyframeAnimation<Float, Float> a12 = polystarShape.i().a();
        this.f12597h = a12;
        BaseKeyframeAnimation<Float, Float> a13 = polystarShape.e().a();
        this.f12599j = a13;
        BaseKeyframeAnimation<Float, Float> a14 = polystarShape.f().a();
        this.f12601l = a14;
        PolystarShape.a aVar = PolystarShape.a.STAR;
        if (j10 == aVar) {
            this.f12598i = polystarShape.b().a();
            this.f12600k = polystarShape.c().a();
        } else {
            this.f12598i = null;
            this.f12600k = null;
        }
        baseLayer.h(a10);
        baseLayer.h(a11);
        baseLayer.h(a12);
        baseLayer.h(a13);
        baseLayer.h(a14);
        if (j10 == aVar) {
            baseLayer.h(this.f12598i);
            baseLayer.h(this.f12600k);
        }
        a10.a(this);
        a11.a(this);
        a12.a(this);
        a13.a(this);
        a14.a(this);
        if (j10 == aVar) {
            this.f12598i.a(this);
            this.f12600k.a(this);
        }
    }

    private void f() {
        double d10;
        double d11;
        double d12;
        int i10;
        int floor = (int) Math.floor(this.f12595f.h().floatValue());
        double radians = Math.toRadians((this.f12597h == null ? UserProfileInfo.Constant.NA_LAT_LON : r2.h().floatValue()) - 90.0d);
        double d13 = floor;
        float floatValue = this.f12601l.h().floatValue() / 100.0f;
        float floatValue2 = this.f12599j.h().floatValue();
        double d14 = floatValue2;
        float cos = (float) (Math.cos(radians) * d14);
        float sin = (float) (Math.sin(radians) * d14);
        this.f12590a.moveTo(cos, sin);
        double d15 = (float) (6.283185307179586d / d13);
        double d16 = radians + d15;
        double ceil = Math.ceil(d13);
        int i11 = 0;
        while (i11 < ceil) {
            float cos2 = (float) (Math.cos(d16) * d14);
            double d17 = ceil;
            float sin2 = (float) (d14 * Math.sin(d16));
            if (floatValue != 0.0f) {
                d11 = d14;
                i10 = i11;
                d10 = d16;
                double atan2 = (float) (Math.atan2(sin, cos) - 1.5707963267948966d);
                float cos3 = (float) Math.cos(atan2);
                float sin3 = (float) Math.sin(atan2);
                d12 = d15;
                double atan22 = (float) (Math.atan2(sin2, cos2) - 1.5707963267948966d);
                float f10 = floatValue2 * floatValue * 0.25f;
                this.f12590a.cubicTo(cos - (cos3 * f10), sin - (sin3 * f10), cos2 + (((float) Math.cos(atan22)) * f10), sin2 + (f10 * ((float) Math.sin(atan22))), cos2, sin2);
            } else {
                d10 = d16;
                d11 = d14;
                d12 = d15;
                i10 = i11;
                this.f12590a.lineTo(cos2, sin2);
            }
            d16 = d10 + d12;
            i11 = i10 + 1;
            sin = sin2;
            cos = cos2;
            ceil = d17;
            d14 = d11;
            d15 = d12;
        }
        PointF h10 = this.f12596g.h();
        this.f12590a.offset(h10.x, h10.y);
        this.f12590a.close();
    }

    private void h() {
        double d10;
        int i10;
        double d11;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        double d12;
        float f16;
        float f17;
        float f18;
        float floatValue = this.f12595f.h().floatValue();
        double radians = Math.toRadians((this.f12597h == null ? UserProfileInfo.Constant.NA_LAT_LON : r2.h().floatValue()) - 90.0d);
        double d13 = floatValue;
        float f19 = (float) (6.283185307179586d / d13);
        float f20 = f19 / 2.0f;
        float f21 = floatValue - ((int) floatValue);
        int i11 = (f21 > 0.0f ? 1 : (f21 == 0.0f ? 0 : -1));
        if (i11 != 0) {
            radians += (1.0f - f21) * f20;
        }
        float floatValue2 = this.f12599j.h().floatValue();
        float floatValue3 = this.f12598i.h().floatValue();
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation = this.f12600k;
        float floatValue4 = baseKeyframeAnimation != null ? baseKeyframeAnimation.h().floatValue() / 100.0f : 0.0f;
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2 = this.f12601l;
        float floatValue5 = baseKeyframeAnimation2 != null ? baseKeyframeAnimation2.h().floatValue() / 100.0f : 0.0f;
        if (i11 != 0) {
            f12 = ((floatValue2 - floatValue3) * f21) + floatValue3;
            i10 = i11;
            double d14 = f12;
            d10 = d13;
            f10 = (float) (d14 * Math.cos(radians));
            f11 = (float) (d14 * Math.sin(radians));
            this.f12590a.moveTo(f10, f11);
            d11 = radians + ((f19 * f21) / 2.0f);
        } else {
            d10 = d13;
            i10 = i11;
            double d15 = floatValue2;
            float cos = (float) (Math.cos(radians) * d15);
            float sin = (float) (d15 * Math.sin(radians));
            this.f12590a.moveTo(cos, sin);
            d11 = radians + f20;
            f10 = cos;
            f11 = sin;
            f12 = 0.0f;
        }
        double ceil = Math.ceil(d10) * 2.0d;
        int i12 = 0;
        boolean z10 = false;
        while (true) {
            double d16 = i12;
            if (d16 < ceil) {
                float f22 = z10 ? floatValue2 : floatValue3;
                if (f12 == 0.0f || d16 != ceil - 2.0d) {
                    f13 = f19;
                    f14 = f20;
                } else {
                    f13 = f19;
                    f14 = (f19 * f21) / 2.0f;
                }
                if (f12 == 0.0f || d16 != ceil - 1.0d) {
                    f15 = f20;
                    d12 = d16;
                    f16 = f22;
                } else {
                    f15 = f20;
                    d12 = d16;
                    f16 = f12;
                }
                double d17 = f16;
                double d18 = ceil;
                float cos2 = (float) (d17 * Math.cos(d11));
                float sin2 = (float) (d17 * Math.sin(d11));
                if (floatValue4 == 0.0f && floatValue5 == 0.0f) {
                    this.f12590a.lineTo(cos2, sin2);
                    f17 = floatValue4;
                    f18 = f12;
                } else {
                    f17 = floatValue4;
                    f18 = f12;
                    double atan2 = (float) (Math.atan2(f11, f10) - 1.5707963267948966d);
                    float cos3 = (float) Math.cos(atan2);
                    float sin3 = (float) Math.sin(atan2);
                    double atan22 = (float) (Math.atan2(sin2, cos2) - 1.5707963267948966d);
                    float cos4 = (float) Math.cos(atan22);
                    float sin4 = (float) Math.sin(atan22);
                    float f23 = z10 ? f17 : floatValue5;
                    float f24 = z10 ? floatValue5 : f17;
                    float f25 = (z10 ? floatValue3 : floatValue2) * f23 * 0.47829f;
                    float f26 = cos3 * f25;
                    float f27 = f25 * sin3;
                    float f28 = (z10 ? floatValue2 : floatValue3) * f24 * 0.47829f;
                    float f29 = cos4 * f28;
                    float f30 = f28 * sin4;
                    if (i10 != 0) {
                        if (i12 == 0) {
                            f26 *= f21;
                            f27 *= f21;
                        } else if (d12 == d18 - 1.0d) {
                            f29 *= f21;
                            f30 *= f21;
                        }
                    }
                    this.f12590a.cubicTo(f10 - f26, f11 - f27, cos2 + f29, sin2 + f30, cos2, sin2);
                }
                d11 += f14;
                z10 = !z10;
                i12++;
                f10 = cos2;
                f11 = sin2;
                floatValue4 = f17;
                f12 = f18;
                f20 = f15;
                f19 = f13;
                ceil = d18;
            } else {
                PointF h10 = this.f12596g.h();
                this.f12590a.offset(h10.x, h10.y);
                this.f12590a.close();
                return;
            }
        }
    }

    private void i() {
        this.f12603n = false;
        this.f12592c.invalidateSelf();
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        i();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        for (int i10 = 0; i10 < list.size(); i10++) {
            Content content = list.get(i10);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.i() == ShapeTrimPath.a.SIMULTANEOUSLY) {
                    this.f12602m.a(trimPathContent);
                    trimPathContent.c(this);
                }
            }
        }
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation;
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2;
        if (t7 == EffectiveAnimationProperty.f9697w) {
            this.f12595f.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9698x) {
            this.f12597h.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9688n) {
            this.f12596g.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9699y && (baseKeyframeAnimation2 = this.f12598i) != null) {
            baseKeyframeAnimation2.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9700z) {
            this.f12599j.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.A && (baseKeyframeAnimation = this.f12600k) != null) {
            baseKeyframeAnimation.n(effectiveValueCallback);
        } else if (t7 == EffectiveAnimationProperty.B) {
            this.f12601l.n(effectiveValueCallback);
        }
    }

    @Override // l4.KeyPathElement
    public void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.m(keyPath, i10, list, keyPath2, this);
    }

    @Override // i4.Content
    public String getName() {
        return this.f12591b;
    }

    @Override // i4.PathContent
    public Path getPath() {
        if (this.f12603n) {
            return this.f12590a;
        }
        this.f12590a.reset();
        if (this.f12594e) {
            this.f12603n = true;
            return this.f12590a;
        }
        int i10 = a.f12604a[this.f12593d.ordinal()];
        if (i10 == 1) {
            h();
        } else if (i10 == 2) {
            f();
        }
        this.f12590a.close();
        this.f12602m.b(this.f12590a);
        this.f12603n = true;
        return this.f12590a;
    }
}
