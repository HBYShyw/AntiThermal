package i4;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import com.oplus.anim.L;
import h4.LPaint;
import j.LongSparseArray;
import j4.BaseKeyframeAnimation;
import j4.DropShadowKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import java.util.ArrayList;
import java.util.List;
import l4.KeyPath;
import n4.GradientColor;
import n4.GradientFill;
import n4.GradientType;
import o4.BaseLayer;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: GradientFillContent.java */
/* renamed from: i4.h, reason: use source file name */
/* loaded from: classes.dex */
public class GradientFillContent implements DrawingContent, BaseKeyframeAnimation.b, KeyPathElementContent {

    /* renamed from: a, reason: collision with root package name */
    private final String f12553a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f12554b;

    /* renamed from: c, reason: collision with root package name */
    private final BaseLayer f12555c;

    /* renamed from: d, reason: collision with root package name */
    private final LongSparseArray<LinearGradient> f12556d = new LongSparseArray<>();

    /* renamed from: e, reason: collision with root package name */
    private final LongSparseArray<RadialGradient> f12557e = new LongSparseArray<>();

    /* renamed from: f, reason: collision with root package name */
    private final Path f12558f;

    /* renamed from: g, reason: collision with root package name */
    private final Paint f12559g;

    /* renamed from: h, reason: collision with root package name */
    private final RectF f12560h;

    /* renamed from: i, reason: collision with root package name */
    private final List<PathContent> f12561i;

    /* renamed from: j, reason: collision with root package name */
    private final GradientType f12562j;

    /* renamed from: k, reason: collision with root package name */
    private final BaseKeyframeAnimation<GradientColor, GradientColor> f12563k;

    /* renamed from: l, reason: collision with root package name */
    private final BaseKeyframeAnimation<Integer, Integer> f12564l;

    /* renamed from: m, reason: collision with root package name */
    private final BaseKeyframeAnimation<PointF, PointF> f12565m;

    /* renamed from: n, reason: collision with root package name */
    private final BaseKeyframeAnimation<PointF, PointF> f12566n;

    /* renamed from: o, reason: collision with root package name */
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> f12567o;

    /* renamed from: p, reason: collision with root package name */
    private ValueCallbackKeyframeAnimation f12568p;

    /* renamed from: q, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12569q;

    /* renamed from: r, reason: collision with root package name */
    private final int f12570r;

    /* renamed from: s, reason: collision with root package name */
    private BaseKeyframeAnimation<Float, Float> f12571s;

    /* renamed from: t, reason: collision with root package name */
    float f12572t;

    /* renamed from: u, reason: collision with root package name */
    private DropShadowKeyframeAnimation f12573u;

    public GradientFillContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, GradientFill gradientFill) {
        Path path = new Path();
        this.f12558f = path;
        this.f12559g = new LPaint(1);
        this.f12560h = new RectF();
        this.f12561i = new ArrayList();
        this.f12572t = 0.0f;
        this.f12555c = baseLayer;
        this.f12553a = gradientFill.f();
        this.f12554b = gradientFill.i();
        this.f12569q = effectiveAnimationDrawable;
        this.f12562j = gradientFill.e();
        path.setFillType(gradientFill.c());
        this.f12570r = (int) (effectiveAnimationDrawable.q().e() / 32.0f);
        BaseKeyframeAnimation<GradientColor, GradientColor> a10 = gradientFill.d().a();
        this.f12563k = a10;
        a10.a(this);
        baseLayer.h(a10);
        BaseKeyframeAnimation<Integer, Integer> a11 = gradientFill.g().a();
        this.f12564l = a11;
        a11.a(this);
        baseLayer.h(a11);
        BaseKeyframeAnimation<PointF, PointF> a12 = gradientFill.h().a();
        this.f12565m = a12;
        a12.a(this);
        baseLayer.h(a12);
        BaseKeyframeAnimation<PointF, PointF> a13 = gradientFill.b().a();
        this.f12566n = a13;
        a13.a(this);
        baseLayer.h(a13);
        if (baseLayer.u() != null) {
            BaseKeyframeAnimation<Float, Float> a14 = baseLayer.u().a().a();
            this.f12571s = a14;
            a14.a(this);
            baseLayer.h(this.f12571s);
        }
        if (baseLayer.w() != null) {
            this.f12573u = new DropShadowKeyframeAnimation(this, baseLayer, baseLayer.w());
        }
    }

    private int[] f(int[] iArr) {
        ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.f12568p;
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

    private int h() {
        int round = Math.round(this.f12565m.f() * this.f12570r);
        int round2 = Math.round(this.f12566n.f() * this.f12570r);
        int round3 = Math.round(this.f12563k.f() * this.f12570r);
        int i10 = round != 0 ? 527 * round : 17;
        if (round2 != 0) {
            i10 = i10 * 31 * round2;
        }
        return round3 != 0 ? i10 * 31 * round3 : i10;
    }

    private LinearGradient i() {
        long h10 = h();
        LinearGradient e10 = this.f12556d.e(h10);
        if (e10 != null) {
            return e10;
        }
        PointF h11 = this.f12565m.h();
        PointF h12 = this.f12566n.h();
        GradientColor h13 = this.f12563k.h();
        LinearGradient linearGradient = new LinearGradient(h11.x, h11.y, h12.x, h12.y, f(h13.a()), h13.b(), Shader.TileMode.CLAMP);
        this.f12556d.j(h10, linearGradient);
        return linearGradient;
    }

    private RadialGradient j() {
        long h10 = h();
        RadialGradient e10 = this.f12557e.e(h10);
        if (e10 != null) {
            return e10;
        }
        PointF h11 = this.f12565m.h();
        PointF h12 = this.f12566n.h();
        GradientColor h13 = this.f12563k.h();
        int[] f10 = f(h13.a());
        float[] b10 = h13.b();
        float f11 = h11.x;
        float f12 = h11.y;
        float hypot = (float) Math.hypot(h12.x - f11, h12.y - f12);
        if (hypot <= 0.0f) {
            hypot = 0.001f;
        }
        RadialGradient radialGradient = new RadialGradient(f11, f12, hypot, f10, b10, Shader.TileMode.CLAMP);
        this.f12557e.j(h10, radialGradient);
        return radialGradient;
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        this.f12569q.invalidateSelf();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        for (int i10 = 0; i10 < list2.size(); i10++) {
            Content content = list2.get(i10);
            if (content instanceof PathContent) {
                this.f12561i.add((PathContent) content);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation2;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation3;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation4;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation5;
        if (t7 == EffectiveAnimationProperty.f9678d) {
            this.f12564l.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.K) {
            BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12567o;
            if (baseKeyframeAnimation != null) {
                this.f12555c.F(baseKeyframeAnimation);
            }
            if (effectiveValueCallback == null) {
                this.f12567o = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12567o = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.a(this);
            this.f12555c.h(this.f12567o);
            return;
        }
        if (t7 == EffectiveAnimationProperty.L) {
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = this.f12568p;
            if (valueCallbackKeyframeAnimation2 != null) {
                this.f12555c.F(valueCallbackKeyframeAnimation2);
            }
            if (effectiveValueCallback == null) {
                this.f12568p = null;
                return;
            }
            this.f12556d.a();
            this.f12557e.a();
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation3 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12568p = valueCallbackKeyframeAnimation3;
            valueCallbackKeyframeAnimation3.a(this);
            this.f12555c.h(this.f12568p);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9684j) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.f12571s;
            if (baseKeyframeAnimation2 != null) {
                baseKeyframeAnimation2.n(effectiveValueCallback);
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation4 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12571s = valueCallbackKeyframeAnimation4;
            valueCallbackKeyframeAnimation4.a(this);
            this.f12555c.h(this.f12571s);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9679e && (dropShadowKeyframeAnimation5 = this.f12573u) != null) {
            dropShadowKeyframeAnimation5.c(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.G && (dropShadowKeyframeAnimation4 = this.f12573u) != null) {
            dropShadowKeyframeAnimation4.f(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.H && (dropShadowKeyframeAnimation3 = this.f12573u) != null) {
            dropShadowKeyframeAnimation3.d(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.I && (dropShadowKeyframeAnimation2 = this.f12573u) != null) {
            dropShadowKeyframeAnimation2.e(effectiveValueCallback);
        } else {
            if (t7 != EffectiveAnimationProperty.J || (dropShadowKeyframeAnimation = this.f12573u) == null) {
                return;
            }
            dropShadowKeyframeAnimation.g(effectiveValueCallback);
        }
    }

    @Override // l4.KeyPathElement
    public void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.m(keyPath, i10, list, keyPath2, this);
    }

    @Override // i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        this.f12558f.reset();
        for (int i10 = 0; i10 < this.f12561i.size(); i10++) {
            this.f12558f.addPath(this.f12561i.get(i10).getPath(), matrix);
        }
        this.f12558f.computeBounds(rectF, false);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
    }

    @Override // i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        Shader j10;
        if (this.f12554b) {
            return;
        }
        L.a("GradientFillContent#draw");
        this.f12558f.reset();
        for (int i11 = 0; i11 < this.f12561i.size(); i11++) {
            this.f12558f.addPath(this.f12561i.get(i11).getPath(), matrix);
        }
        this.f12558f.computeBounds(this.f12560h, false);
        if (this.f12562j == GradientType.LINEAR) {
            j10 = i();
        } else {
            j10 = j();
        }
        j10.setLocalMatrix(matrix);
        this.f12559g.setShader(j10);
        BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12567o;
        if (baseKeyframeAnimation != null) {
            this.f12559g.setColorFilter(baseKeyframeAnimation.h());
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.f12571s;
        if (baseKeyframeAnimation2 != null) {
            float floatValue = baseKeyframeAnimation2.h().floatValue();
            if (floatValue == 0.0f) {
                this.f12559g.setMaskFilter(null);
            } else if (floatValue != this.f12572t) {
                this.f12559g.setMaskFilter(new BlurMaskFilter(floatValue, BlurMaskFilter.Blur.NORMAL));
            }
            this.f12572t = floatValue;
        }
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation = this.f12573u;
        if (dropShadowKeyframeAnimation != null) {
            dropShadowKeyframeAnimation.b(this.f12559g);
        }
        this.f12559g.setAlpha(MiscUtils.d((int) ((((i10 / 255.0f) * this.f12564l.h().intValue()) / 100.0f) * 255.0f), 0, 255));
        canvas.drawPath(this.f12558f, this.f12559g);
        L.b("GradientFillContent#draw");
    }

    @Override // i4.Content
    public String getName() {
        return this.f12553a;
    }
}
