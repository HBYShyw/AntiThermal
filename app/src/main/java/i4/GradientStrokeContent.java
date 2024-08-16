package i4;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import j.LongSparseArray;
import j4.BaseKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import n4.GradientColor;
import n4.GradientStroke;
import n4.GradientType;
import o4.BaseLayer;
import t4.EffectiveValueCallback;

/* compiled from: GradientStrokeContent.java */
/* renamed from: i4.i, reason: use source file name */
/* loaded from: classes.dex */
public class GradientStrokeContent extends BaseStrokeContent {
    private final BaseKeyframeAnimation<PointF, PointF> A;
    private ValueCallbackKeyframeAnimation B;

    /* renamed from: r, reason: collision with root package name */
    private final String f12574r;

    /* renamed from: s, reason: collision with root package name */
    private final boolean f12575s;

    /* renamed from: t, reason: collision with root package name */
    private final LongSparseArray<LinearGradient> f12576t;

    /* renamed from: u, reason: collision with root package name */
    private final LongSparseArray<RadialGradient> f12577u;

    /* renamed from: v, reason: collision with root package name */
    private final RectF f12578v;

    /* renamed from: w, reason: collision with root package name */
    private final GradientType f12579w;

    /* renamed from: x, reason: collision with root package name */
    private final int f12580x;

    /* renamed from: y, reason: collision with root package name */
    private final BaseKeyframeAnimation<GradientColor, GradientColor> f12581y;

    /* renamed from: z, reason: collision with root package name */
    private final BaseKeyframeAnimation<PointF, PointF> f12582z;

    public GradientStrokeContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, GradientStroke gradientStroke) {
        super(effectiveAnimationDrawable, baseLayer, gradientStroke.b().a(), gradientStroke.g().a(), gradientStroke.i(), gradientStroke.k(), gradientStroke.m(), gradientStroke.h(), gradientStroke.c());
        this.f12576t = new LongSparseArray<>();
        this.f12577u = new LongSparseArray<>();
        this.f12578v = new RectF();
        this.f12574r = gradientStroke.j();
        this.f12579w = gradientStroke.f();
        this.f12575s = gradientStroke.n();
        this.f12580x = (int) (effectiveAnimationDrawable.q().e() / 32.0f);
        BaseKeyframeAnimation<GradientColor, GradientColor> a10 = gradientStroke.e().a();
        this.f12581y = a10;
        a10.a(this);
        baseLayer.h(a10);
        BaseKeyframeAnimation<PointF, PointF> a11 = gradientStroke.l().a();
        this.f12582z = a11;
        a11.a(this);
        baseLayer.h(a11);
        BaseKeyframeAnimation<PointF, PointF> a12 = gradientStroke.d().a();
        this.A = a12;
        a12.a(this);
        baseLayer.h(a12);
    }

    private int[] i(int[] iArr) {
        ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.B;
        if (valueCallbackKeyframeAnimation != null) {
            Integer[] numArr = (Integer[]) valueCallbackKeyframeAnimation.h();
            int i10 = 0;
            if (iArr.length == numArr.length) {
                while (i10 < iArr.length) {
                    iArr[i10] = numArr[i10].intValue();
                    i10++;
                }
            } else {
                iArr = new int[numArr.length];
                while (i10 < numArr.length) {
                    iArr[i10] = numArr[i10].intValue();
                    i10++;
                }
            }
        }
        return iArr;
    }

    private int j() {
        int round = Math.round(this.f12582z.f() * this.f12580x);
        int round2 = Math.round(this.A.f() * this.f12580x);
        int round3 = Math.round(this.f12581y.f() * this.f12580x);
        int i10 = round != 0 ? 527 * round : 17;
        if (round2 != 0) {
            i10 = i10 * 31 * round2;
        }
        return round3 != 0 ? i10 * 31 * round3 : i10;
    }

    private LinearGradient k() {
        long j10 = j();
        LinearGradient e10 = this.f12576t.e(j10);
        if (e10 != null) {
            return e10;
        }
        PointF h10 = this.f12582z.h();
        PointF h11 = this.A.h();
        GradientColor h12 = this.f12581y.h();
        LinearGradient linearGradient = new LinearGradient(h10.x, h10.y, h11.x, h11.y, i(h12.a()), h12.b(), Shader.TileMode.CLAMP);
        this.f12576t.j(j10, linearGradient);
        return linearGradient;
    }

    private RadialGradient l() {
        long j10 = j();
        RadialGradient e10 = this.f12577u.e(j10);
        if (e10 != null) {
            return e10;
        }
        PointF h10 = this.f12582z.h();
        PointF h11 = this.A.h();
        GradientColor h12 = this.f12581y.h();
        int[] i10 = i(h12.a());
        float[] b10 = h12.b();
        RadialGradient radialGradient = new RadialGradient(h10.x, h10.y, (float) Math.hypot(h11.x - r7, h11.y - r8), i10, b10, Shader.TileMode.CLAMP);
        this.f12577u.j(j10, radialGradient);
        return radialGradient;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // i4.BaseStrokeContent, l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        super.c(t7, effectiveValueCallback);
        if (t7 == EffectiveAnimationProperty.L) {
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.B;
            if (valueCallbackKeyframeAnimation != null) {
                this.f12506f.F(valueCallbackKeyframeAnimation);
            }
            if (effectiveValueCallback == null) {
                this.B = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.B = valueCallbackKeyframeAnimation2;
            valueCallbackKeyframeAnimation2.a(this);
            this.f12506f.h(this.B);
        }
    }

    @Override // i4.BaseStrokeContent, i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        Shader l10;
        if (this.f12575s) {
            return;
        }
        e(this.f12578v, matrix, false);
        if (this.f12579w == GradientType.LINEAR) {
            l10 = k();
        } else {
            l10 = l();
        }
        l10.setLocalMatrix(matrix);
        this.f12509i.setShader(l10);
        super.g(canvas, matrix, i10);
    }

    @Override // i4.Content
    public String getName() {
        return this.f12574r;
    }
}
