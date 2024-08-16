package t4;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import com.oplus.anim.EffectiveAnimationComposition;

/* compiled from: Keyframe.java */
/* renamed from: t4.c, reason: use source file name */
/* loaded from: classes.dex */
public class Keyframe<T> {

    /* renamed from: a, reason: collision with root package name */
    private final EffectiveAnimationComposition f18569a;

    /* renamed from: b, reason: collision with root package name */
    public final T f18570b;

    /* renamed from: c, reason: collision with root package name */
    public T f18571c;

    /* renamed from: d, reason: collision with root package name */
    public final Interpolator f18572d;

    /* renamed from: e, reason: collision with root package name */
    public final Interpolator f18573e;

    /* renamed from: f, reason: collision with root package name */
    public final Interpolator f18574f;

    /* renamed from: g, reason: collision with root package name */
    public final float f18575g;

    /* renamed from: h, reason: collision with root package name */
    public Float f18576h;

    /* renamed from: i, reason: collision with root package name */
    private float f18577i;

    /* renamed from: j, reason: collision with root package name */
    private float f18578j;

    /* renamed from: k, reason: collision with root package name */
    private int f18579k;

    /* renamed from: l, reason: collision with root package name */
    private int f18580l;

    /* renamed from: m, reason: collision with root package name */
    private float f18581m;

    /* renamed from: n, reason: collision with root package name */
    private float f18582n;

    /* renamed from: o, reason: collision with root package name */
    public PointF f18583o;

    /* renamed from: p, reason: collision with root package name */
    public PointF f18584p;

    public Keyframe(EffectiveAnimationComposition effectiveAnimationComposition, T t7, T t10, Interpolator interpolator, float f10, Float f11) {
        this.f18577i = -3987645.8f;
        this.f18578j = -3987645.8f;
        this.f18579k = 784923401;
        this.f18580l = 784923401;
        this.f18581m = Float.MIN_VALUE;
        this.f18582n = Float.MIN_VALUE;
        this.f18583o = null;
        this.f18584p = null;
        this.f18569a = effectiveAnimationComposition;
        this.f18570b = t7;
        this.f18571c = t10;
        this.f18572d = interpolator;
        this.f18573e = null;
        this.f18574f = null;
        this.f18575g = f10;
        this.f18576h = f11;
    }

    public boolean a(float f10) {
        return f10 >= e() && f10 < b();
    }

    public float b() {
        if (this.f18569a == null) {
            return 1.0f;
        }
        if (this.f18582n == Float.MIN_VALUE) {
            if (this.f18576h == null) {
                this.f18582n = 1.0f;
            } else {
                this.f18582n = e() + ((this.f18576h.floatValue() - this.f18575g) / this.f18569a.f());
            }
        }
        return this.f18582n;
    }

    public float c() {
        if (this.f18578j == -3987645.8f) {
            this.f18578j = ((Float) this.f18571c).floatValue();
        }
        return this.f18578j;
    }

    public int d() {
        if (this.f18580l == 784923401) {
            this.f18580l = ((Integer) this.f18571c).intValue();
        }
        return this.f18580l;
    }

    public float e() {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f18569a;
        if (effectiveAnimationComposition == null) {
            return 0.0f;
        }
        if (this.f18581m == Float.MIN_VALUE) {
            this.f18581m = (this.f18575g - effectiveAnimationComposition.q()) / this.f18569a.f();
        }
        return this.f18581m;
    }

    public float f() {
        if (this.f18577i == -3987645.8f) {
            this.f18577i = ((Float) this.f18570b).floatValue();
        }
        return this.f18577i;
    }

    public int g() {
        if (this.f18579k == 784923401) {
            this.f18579k = ((Integer) this.f18570b).intValue();
        }
        return this.f18579k;
    }

    public boolean h() {
        return this.f18572d == null && this.f18573e == null && this.f18574f == null;
    }

    public String toString() {
        return "Keyframe{startValue=" + this.f18570b + ", endValue=" + this.f18571c + ", startFrame=" + this.f18575g + ", endFrame=" + this.f18576h + ", interpolator=" + this.f18572d + '}';
    }

    public Keyframe(EffectiveAnimationComposition effectiveAnimationComposition, T t7, T t10, Interpolator interpolator, Interpolator interpolator2, float f10, Float f11) {
        this.f18577i = -3987645.8f;
        this.f18578j = -3987645.8f;
        this.f18579k = 784923401;
        this.f18580l = 784923401;
        this.f18581m = Float.MIN_VALUE;
        this.f18582n = Float.MIN_VALUE;
        this.f18583o = null;
        this.f18584p = null;
        this.f18569a = effectiveAnimationComposition;
        this.f18570b = t7;
        this.f18571c = t10;
        this.f18572d = null;
        this.f18573e = interpolator;
        this.f18574f = interpolator2;
        this.f18575g = f10;
        this.f18576h = f11;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Keyframe(EffectiveAnimationComposition effectiveAnimationComposition, T t7, T t10, Interpolator interpolator, Interpolator interpolator2, Interpolator interpolator3, float f10, Float f11) {
        this.f18577i = -3987645.8f;
        this.f18578j = -3987645.8f;
        this.f18579k = 784923401;
        this.f18580l = 784923401;
        this.f18581m = Float.MIN_VALUE;
        this.f18582n = Float.MIN_VALUE;
        this.f18583o = null;
        this.f18584p = null;
        this.f18569a = effectiveAnimationComposition;
        this.f18570b = t7;
        this.f18571c = t10;
        this.f18572d = interpolator;
        this.f18573e = interpolator2;
        this.f18574f = interpolator3;
        this.f18575g = f10;
        this.f18576h = f11;
    }

    public Keyframe(T t7) {
        this.f18577i = -3987645.8f;
        this.f18578j = -3987645.8f;
        this.f18579k = 784923401;
        this.f18580l = 784923401;
        this.f18581m = Float.MIN_VALUE;
        this.f18582n = Float.MIN_VALUE;
        this.f18583o = null;
        this.f18584p = null;
        this.f18569a = null;
        this.f18570b = t7;
        this.f18571c = t7;
        this.f18572d = null;
        this.f18573e = null;
        this.f18574f = null;
        this.f18575g = Float.MIN_VALUE;
        this.f18576h = Float.valueOf(Float.MAX_VALUE);
    }
}
