package q4;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.oplus.anim.EffectiveAnimationComposition;
import j.SparseArrayCompat;
import java.lang.ref.WeakReference;
import r4.c;
import s4.MiscUtils;
import t4.Keyframe;

/* compiled from: KeyframeParser.java */
/* renamed from: q4.u, reason: use source file name */
/* loaded from: classes.dex */
class KeyframeParser {

    /* renamed from: b, reason: collision with root package name */
    private static SparseArrayCompat<WeakReference<Interpolator>> f16889b;

    /* renamed from: a, reason: collision with root package name */
    private static final Interpolator f16888a = new LinearInterpolator();

    /* renamed from: c, reason: collision with root package name */
    static c.a f16890c = c.a.a("t", "s", "e", "o", "i", "h", "to", "ti");

    /* renamed from: d, reason: collision with root package name */
    static c.a f16891d = c.a.a("x", "y");

    KeyframeParser() {
    }

    private static WeakReference<Interpolator> a(int i10) {
        WeakReference<Interpolator> e10;
        synchronized (KeyframeParser.class) {
            e10 = g().e(i10);
        }
        return e10;
    }

    private static Interpolator b(PointF pointF, PointF pointF2) {
        Interpolator linearInterpolator;
        pointF.x = MiscUtils.c(pointF.x, -1.0f, 1.0f);
        pointF.y = MiscUtils.c(pointF.y, -100.0f, 100.0f);
        pointF2.x = MiscUtils.c(pointF2.x, -1.0f, 1.0f);
        float c10 = MiscUtils.c(pointF2.y, -100.0f, 100.0f);
        pointF2.y = c10;
        int j10 = s4.h.j(pointF.x, pointF.y, pointF2.x, c10);
        WeakReference<Interpolator> a10 = a(j10);
        Interpolator interpolator = a10 != null ? a10.get() : null;
        if (a10 == null || interpolator == null) {
            try {
                linearInterpolator = PathInterpolatorCompat.a(pointF.x, pointF.y, pointF2.x, pointF2.y);
            } catch (IllegalArgumentException e10) {
                if ("The Path cannot loop back on itself.".equals(e10.getMessage())) {
                    linearInterpolator = PathInterpolatorCompat.a(Math.min(pointF.x, 1.0f), pointF.y, Math.max(pointF2.x, 0.0f), pointF2.y);
                } else {
                    linearInterpolator = new LinearInterpolator();
                }
            }
            interpolator = linearInterpolator;
            try {
                h(j10, new WeakReference(interpolator));
            } catch (ArrayIndexOutOfBoundsException unused) {
            }
        }
        return interpolator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Keyframe<T> c(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, float f10, ValueParser<T> valueParser, boolean z10, boolean z11) {
        if (z10 && z11) {
            return e(effectiveAnimationComposition, cVar, f10, valueParser);
        }
        if (z10) {
            return d(effectiveAnimationComposition, cVar, f10, valueParser);
        }
        return f(cVar, f10, valueParser);
    }

    private static <T> Keyframe<T> d(EffectiveAnimationComposition effectiveAnimationComposition, r4.c cVar, float f10, ValueParser<T> valueParser) {
        Interpolator interpolator;
        Interpolator interpolator2;
        T t7;
        cVar.m();
        PointF pointF = null;
        boolean z10 = false;
        T t10 = null;
        T t11 = null;
        PointF pointF2 = null;
        PointF pointF3 = null;
        float f11 = 0.0f;
        PointF pointF4 = null;
        while (cVar.w()) {
            switch (cVar.i0(f16890c)) {
                case 0:
                    f11 = (float) cVar.N();
                    break;
                case 1:
                    t11 = valueParser.a(cVar, f10);
                    break;
                case 2:
                    t10 = valueParser.a(cVar, f10);
                    break;
                case 3:
                    pointF = JsonUtils.e(cVar, 1.0f);
                    break;
                case 4:
                    pointF4 = JsonUtils.e(cVar, 1.0f);
                    break;
                case 5:
                    if (cVar.S() != 1) {
                        z10 = false;
                        break;
                    } else {
                        z10 = true;
                        break;
                    }
                case 6:
                    pointF2 = JsonUtils.e(cVar, f10);
                    break;
                case 7:
                    pointF3 = JsonUtils.e(cVar, f10);
                    break;
                default:
                    cVar.m0();
                    break;
            }
        }
        cVar.v();
        if (z10) {
            interpolator2 = f16888a;
            t7 = t11;
        } else {
            if (pointF != null && pointF4 != null) {
                interpolator = b(pointF, pointF4);
            } else {
                interpolator = f16888a;
            }
            interpolator2 = interpolator;
            t7 = t10;
        }
        Keyframe<T> keyframe = new Keyframe<>(effectiveAnimationComposition, t11, t7, interpolator2, f11, null);
        keyframe.f18583o = pointF2;
        keyframe.f18584p = pointF3;
        return keyframe;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0023. Please report as an issue. */
    private static <T> Keyframe<T> e(EffectiveAnimationComposition effectiveAnimationComposition, r4.c cVar, float f10, ValueParser<T> valueParser) {
        Interpolator interpolator;
        Interpolator b10;
        Interpolator b11;
        T t7;
        PointF pointF;
        Keyframe<T> keyframe;
        PointF pointF2;
        float f11;
        PointF pointF3;
        cVar.m();
        PointF pointF4 = null;
        boolean z10 = false;
        PointF pointF5 = null;
        PointF pointF6 = null;
        PointF pointF7 = null;
        T t10 = null;
        PointF pointF8 = null;
        PointF pointF9 = null;
        PointF pointF10 = null;
        float f12 = 0.0f;
        PointF pointF11 = null;
        T t11 = null;
        while (cVar.w()) {
            switch (cVar.i0(f16890c)) {
                case 0:
                    pointF2 = pointF4;
                    f12 = (float) cVar.N();
                    pointF4 = pointF2;
                    break;
                case 1:
                    pointF2 = pointF4;
                    t10 = valueParser.a(cVar, f10);
                    pointF4 = pointF2;
                    break;
                case 2:
                    pointF2 = pointF4;
                    t11 = valueParser.a(cVar, f10);
                    pointF4 = pointF2;
                    break;
                case 3:
                    pointF2 = pointF4;
                    f11 = f12;
                    PointF pointF12 = pointF11;
                    if (cVar.e0() == c.b.BEGIN_OBJECT) {
                        cVar.m();
                        float f13 = 0.0f;
                        float f14 = 0.0f;
                        float f15 = 0.0f;
                        float f16 = 0.0f;
                        while (cVar.w()) {
                            int i02 = cVar.i0(f16891d);
                            if (i02 == 0) {
                                c.b e02 = cVar.e0();
                                c.b bVar = c.b.NUMBER;
                                if (e02 == bVar) {
                                    f15 = (float) cVar.N();
                                    f13 = f15;
                                } else {
                                    cVar.c();
                                    f13 = (float) cVar.N();
                                    f15 = cVar.e0() == bVar ? (float) cVar.N() : f13;
                                    cVar.u();
                                }
                            } else if (i02 != 1) {
                                cVar.m0();
                            } else {
                                c.b e03 = cVar.e0();
                                c.b bVar2 = c.b.NUMBER;
                                if (e03 == bVar2) {
                                    f16 = (float) cVar.N();
                                    f14 = f16;
                                } else {
                                    cVar.c();
                                    f14 = (float) cVar.N();
                                    f16 = cVar.e0() == bVar2 ? (float) cVar.N() : f14;
                                    cVar.u();
                                }
                            }
                        }
                        PointF pointF13 = new PointF(f13, f14);
                        PointF pointF14 = new PointF(f15, f16);
                        cVar.v();
                        pointF8 = pointF14;
                        pointF7 = pointF13;
                        pointF11 = pointF12;
                        f12 = f11;
                        pointF4 = pointF2;
                        break;
                    } else {
                        pointF5 = JsonUtils.e(cVar, f10);
                        f12 = f11;
                        pointF11 = pointF12;
                        pointF4 = pointF2;
                    }
                case 4:
                    if (cVar.e0() == c.b.BEGIN_OBJECT) {
                        cVar.m();
                        float f17 = 0.0f;
                        float f18 = 0.0f;
                        float f19 = 0.0f;
                        float f20 = 0.0f;
                        while (cVar.w()) {
                            PointF pointF15 = pointF11;
                            int i03 = cVar.i0(f16891d);
                            if (i03 != 0) {
                                pointF3 = pointF4;
                                if (i03 != 1) {
                                    cVar.m0();
                                } else {
                                    c.b e04 = cVar.e0();
                                    c.b bVar3 = c.b.NUMBER;
                                    if (e04 == bVar3) {
                                        f20 = (float) cVar.N();
                                        f12 = f12;
                                        f18 = f20;
                                    } else {
                                        float f21 = f12;
                                        cVar.c();
                                        float N = (float) cVar.N();
                                        float N2 = cVar.e0() == bVar3 ? (float) cVar.N() : N;
                                        cVar.u();
                                        f12 = f21;
                                        pointF11 = pointF15;
                                        pointF4 = pointF3;
                                        f20 = N2;
                                        f18 = N;
                                    }
                                }
                            } else {
                                pointF3 = pointF4;
                                float f22 = f12;
                                c.b e05 = cVar.e0();
                                c.b bVar4 = c.b.NUMBER;
                                if (e05 == bVar4) {
                                    f19 = (float) cVar.N();
                                    f12 = f22;
                                    f17 = f19;
                                } else {
                                    cVar.c();
                                    f17 = (float) cVar.N();
                                    f19 = cVar.e0() == bVar4 ? (float) cVar.N() : f17;
                                    cVar.u();
                                    f12 = f22;
                                }
                            }
                            pointF11 = pointF15;
                            pointF4 = pointF3;
                        }
                        pointF2 = pointF4;
                        f11 = f12;
                        PointF pointF16 = new PointF(f17, f18);
                        PointF pointF17 = new PointF(f19, f20);
                        cVar.v();
                        pointF10 = pointF17;
                        pointF9 = pointF16;
                        f12 = f11;
                        pointF4 = pointF2;
                        break;
                    } else {
                        pointF2 = pointF4;
                        pointF6 = JsonUtils.e(cVar, f10);
                        pointF4 = pointF2;
                    }
                case 5:
                    if (cVar.S() != 1) {
                        z10 = false;
                        break;
                    } else {
                        z10 = true;
                        break;
                    }
                case 6:
                    pointF11 = JsonUtils.e(cVar, f10);
                    break;
                case 7:
                    pointF4 = JsonUtils.e(cVar, f10);
                    break;
                default:
                    pointF2 = pointF4;
                    cVar.m0();
                    pointF4 = pointF2;
                    break;
            }
        }
        PointF pointF18 = pointF4;
        float f23 = f12;
        PointF pointF19 = pointF11;
        cVar.v();
        if (z10) {
            interpolator = f16888a;
            t7 = t10;
        } else {
            if (pointF5 != null && pointF6 != null) {
                interpolator = b(pointF5, pointF6);
            } else {
                if (pointF7 != null && pointF8 != null && pointF9 != null && pointF10 != null) {
                    b10 = b(pointF7, pointF9);
                    b11 = b(pointF8, pointF10);
                    t7 = t11;
                    interpolator = null;
                    if (b10 == null && b11 != null) {
                        pointF = pointF19;
                        keyframe = new Keyframe<>(effectiveAnimationComposition, t10, t7, b10, b11, f23, null);
                    } else {
                        pointF = pointF19;
                        keyframe = new Keyframe<>(effectiveAnimationComposition, t10, t7, interpolator, f23, null);
                    }
                    keyframe.f18583o = pointF;
                    keyframe.f18584p = pointF18;
                    return keyframe;
                }
                interpolator = f16888a;
            }
            t7 = t11;
        }
        b10 = null;
        b11 = null;
        if (b10 == null) {
        }
        pointF = pointF19;
        keyframe = new Keyframe<>(effectiveAnimationComposition, t10, t7, interpolator, f23, null);
        keyframe.f18583o = pointF;
        keyframe.f18584p = pointF18;
        return keyframe;
    }

    private static <T> Keyframe<T> f(r4.c cVar, float f10, ValueParser<T> valueParser) {
        return new Keyframe<>(valueParser.a(cVar, f10));
    }

    private static SparseArrayCompat<WeakReference<Interpolator>> g() {
        if (f16889b == null) {
            f16889b = new SparseArrayCompat<>();
        }
        return f16889b;
    }

    private static void h(int i10, WeakReference<Interpolator> weakReference) {
        synchronized (KeyframeParser.class) {
            f16889b.i(i10, weakReference);
        }
    }
}
