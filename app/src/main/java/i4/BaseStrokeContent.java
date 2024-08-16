package i4;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import com.oplus.anim.L;
import h4.LPaint;
import j4.BaseKeyframeAnimation;
import j4.DropShadowKeyframeAnimation;
import j4.FloatKeyframeAnimation;
import j4.IntegerKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import java.util.ArrayList;
import java.util.List;
import l4.KeyPath;
import m4.AnimatableFloatValue;
import m4.AnimatableIntegerValue;
import n4.ShapeTrimPath;
import o4.BaseLayer;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: BaseStrokeContent.java */
/* renamed from: i4.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseStrokeContent implements BaseKeyframeAnimation.b, KeyPathElementContent, DrawingContent {

    /* renamed from: e, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12505e;

    /* renamed from: f, reason: collision with root package name */
    protected final BaseLayer f12506f;

    /* renamed from: h, reason: collision with root package name */
    private final float[] f12508h;

    /* renamed from: i, reason: collision with root package name */
    final Paint f12509i;

    /* renamed from: j, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12510j;

    /* renamed from: k, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Integer> f12511k;

    /* renamed from: l, reason: collision with root package name */
    private final List<BaseKeyframeAnimation<?, Float>> f12512l;

    /* renamed from: m, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12513m;

    /* renamed from: n, reason: collision with root package name */
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> f12514n;

    /* renamed from: o, reason: collision with root package name */
    private BaseKeyframeAnimation<Float, Float> f12515o;

    /* renamed from: p, reason: collision with root package name */
    float f12516p;

    /* renamed from: q, reason: collision with root package name */
    private DropShadowKeyframeAnimation f12517q;

    /* renamed from: a, reason: collision with root package name */
    private final PathMeasure f12501a = new PathMeasure();

    /* renamed from: b, reason: collision with root package name */
    private final Path f12502b = new Path();

    /* renamed from: c, reason: collision with root package name */
    private final Path f12503c = new Path();

    /* renamed from: d, reason: collision with root package name */
    private final RectF f12504d = new RectF();

    /* renamed from: g, reason: collision with root package name */
    private final List<b> f12507g = new ArrayList();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BaseStrokeContent.java */
    /* renamed from: i4.a$b */
    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final List<PathContent> f12518a;

        /* renamed from: b, reason: collision with root package name */
        private final TrimPathContent f12519b;

        private b(TrimPathContent trimPathContent) {
            this.f12518a = new ArrayList();
            this.f12519b = trimPathContent;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseStrokeContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, Paint.Cap cap, Paint.Join join, float f10, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue, List<AnimatableFloatValue> list, AnimatableFloatValue animatableFloatValue2) {
        LPaint lPaint = new LPaint(1);
        this.f12509i = lPaint;
        this.f12516p = 0.0f;
        this.f12505e = effectiveAnimationDrawable;
        this.f12506f = baseLayer;
        lPaint.setStyle(Paint.Style.STROKE);
        lPaint.setStrokeCap(cap);
        lPaint.setStrokeJoin(join);
        lPaint.setStrokeMiter(f10);
        this.f12511k = animatableIntegerValue.a();
        this.f12510j = animatableFloatValue.a();
        if (animatableFloatValue2 == null) {
            this.f12513m = null;
        } else {
            this.f12513m = animatableFloatValue2.a();
        }
        this.f12512l = new ArrayList(list.size());
        this.f12508h = new float[list.size()];
        for (int i10 = 0; i10 < list.size(); i10++) {
            this.f12512l.add(list.get(i10).a());
        }
        baseLayer.h(this.f12511k);
        baseLayer.h(this.f12510j);
        for (int i11 = 0; i11 < this.f12512l.size(); i11++) {
            baseLayer.h(this.f12512l.get(i11));
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation = this.f12513m;
        if (baseKeyframeAnimation != null) {
            baseLayer.h(baseKeyframeAnimation);
        }
        this.f12511k.a(this);
        this.f12510j.a(this);
        for (int i12 = 0; i12 < list.size(); i12++) {
            this.f12512l.get(i12).a(this);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2 = this.f12513m;
        if (baseKeyframeAnimation2 != null) {
            baseKeyframeAnimation2.a(this);
        }
        if (baseLayer.u() != null) {
            BaseKeyframeAnimation<Float, Float> a10 = baseLayer.u().a().a();
            this.f12515o = a10;
            a10.a(this);
            baseLayer.h(this.f12515o);
        }
        if (baseLayer.w() != null) {
            this.f12517q = new DropShadowKeyframeAnimation(this, baseLayer, baseLayer.w());
        }
    }

    private void f(Matrix matrix) {
        L.a("StrokeContent#applyDashPattern");
        if (this.f12512l.isEmpty()) {
            L.b("StrokeContent#applyDashPattern");
            return;
        }
        float h10 = s4.h.h(matrix);
        for (int i10 = 0; i10 < this.f12512l.size(); i10++) {
            this.f12508h[i10] = this.f12512l.get(i10).h().floatValue();
            if (i10 % 2 == 0) {
                float[] fArr = this.f12508h;
                if (fArr[i10] < 1.0f) {
                    fArr[i10] = 1.0f;
                }
            } else {
                float[] fArr2 = this.f12508h;
                if (fArr2[i10] < 0.1f) {
                    fArr2[i10] = 0.1f;
                }
            }
            float[] fArr3 = this.f12508h;
            fArr3[i10] = fArr3[i10] * h10;
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation = this.f12513m;
        this.f12509i.setPathEffect(new DashPathEffect(this.f12508h, baseKeyframeAnimation == null ? 0.0f : h10 * baseKeyframeAnimation.h().floatValue()));
        L.b("StrokeContent#applyDashPattern");
    }

    private void h(Canvas canvas, b bVar, Matrix matrix) {
        L.a("StrokeContent#applyTrimPath");
        if (bVar.f12519b == null) {
            L.b("StrokeContent#applyTrimPath");
            return;
        }
        this.f12502b.reset();
        for (int size = bVar.f12518a.size() - 1; size >= 0; size--) {
            this.f12502b.addPath(((PathContent) bVar.f12518a.get(size)).getPath(), matrix);
        }
        this.f12501a.setPath(this.f12502b, false);
        float length = this.f12501a.getLength();
        while (this.f12501a.nextContour()) {
            length += this.f12501a.getLength();
        }
        float floatValue = (bVar.f12519b.f().h().floatValue() * length) / 360.0f;
        float floatValue2 = ((bVar.f12519b.h().h().floatValue() / 100.0f) * length) + floatValue;
        float floatValue3 = ((bVar.f12519b.d().h().floatValue() / 100.0f) * length) + floatValue;
        float f10 = 0.0f;
        for (int size2 = bVar.f12518a.size() - 1; size2 >= 0; size2--) {
            this.f12503c.set(((PathContent) bVar.f12518a.get(size2)).getPath());
            this.f12503c.transform(matrix);
            this.f12501a.setPath(this.f12503c, false);
            float length2 = this.f12501a.getLength();
            if (floatValue3 > length) {
                float f11 = floatValue3 - length;
                if (f11 < f10 + length2 && f10 < f11) {
                    s4.h.a(this.f12503c, floatValue2 > length ? (floatValue2 - length) / length2 : 0.0f, Math.min(f11 / length2, 1.0f), 0.0f);
                    canvas.drawPath(this.f12503c, this.f12509i);
                    f10 += length2;
                }
            }
            float f12 = f10 + length2;
            if (f12 >= floatValue2 && f10 <= floatValue3) {
                if (f12 <= floatValue3 && floatValue2 < f10) {
                    canvas.drawPath(this.f12503c, this.f12509i);
                } else {
                    s4.h.a(this.f12503c, floatValue2 < f10 ? 0.0f : (floatValue2 - f10) / length2, floatValue3 <= f12 ? (floatValue3 - f10) / length2 : 1.0f, 0.0f);
                    canvas.drawPath(this.f12503c, this.f12509i);
                }
            }
            f10 += length2;
        }
        L.b("StrokeContent#applyTrimPath");
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        this.f12505e.invalidateSelf();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        TrimPathContent trimPathContent = null;
        for (int size = list.size() - 1; size >= 0; size--) {
            Content content = list.get(size);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent2 = (TrimPathContent) content;
                if (trimPathContent2.i() == ShapeTrimPath.a.INDIVIDUALLY) {
                    trimPathContent = trimPathContent2;
                }
            }
        }
        if (trimPathContent != null) {
            trimPathContent.c(this);
        }
        b bVar = null;
        for (int size2 = list2.size() - 1; size2 >= 0; size2--) {
            Content content2 = list2.get(size2);
            if (content2 instanceof TrimPathContent) {
                TrimPathContent trimPathContent3 = (TrimPathContent) content2;
                if (trimPathContent3.i() == ShapeTrimPath.a.INDIVIDUALLY) {
                    if (bVar != null) {
                        this.f12507g.add(bVar);
                    }
                    bVar = new b(trimPathContent3);
                    trimPathContent3.c(this);
                }
            }
            if (content2 instanceof PathContent) {
                if (bVar == null) {
                    bVar = new b(trimPathContent);
                }
                bVar.f12518a.add((PathContent) content2);
            }
        }
        if (bVar != null) {
            this.f12507g.add(bVar);
        }
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation2;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation3;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation4;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation5;
        if (t7 == EffectiveAnimationProperty.f9678d) {
            this.f12511k.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9693s) {
            this.f12510j.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.K) {
            BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12514n;
            if (baseKeyframeAnimation != null) {
                this.f12506f.F(baseKeyframeAnimation);
            }
            if (effectiveValueCallback == null) {
                this.f12514n = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12514n = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.a(this);
            this.f12506f.h(this.f12514n);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9684j) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.f12515o;
            if (baseKeyframeAnimation2 != null) {
                baseKeyframeAnimation2.n(effectiveValueCallback);
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12515o = valueCallbackKeyframeAnimation2;
            valueCallbackKeyframeAnimation2.a(this);
            this.f12506f.h(this.f12515o);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9679e && (dropShadowKeyframeAnimation5 = this.f12517q) != null) {
            dropShadowKeyframeAnimation5.c(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.G && (dropShadowKeyframeAnimation4 = this.f12517q) != null) {
            dropShadowKeyframeAnimation4.f(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.H && (dropShadowKeyframeAnimation3 = this.f12517q) != null) {
            dropShadowKeyframeAnimation3.d(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.I && (dropShadowKeyframeAnimation2 = this.f12517q) != null) {
            dropShadowKeyframeAnimation2.e(effectiveValueCallback);
        } else {
            if (t7 != EffectiveAnimationProperty.J || (dropShadowKeyframeAnimation = this.f12517q) == null) {
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
        L.a("StrokeContent#getBounds");
        this.f12502b.reset();
        for (int i10 = 0; i10 < this.f12507g.size(); i10++) {
            b bVar = this.f12507g.get(i10);
            for (int i11 = 0; i11 < bVar.f12518a.size(); i11++) {
                this.f12502b.addPath(((PathContent) bVar.f12518a.get(i11)).getPath(), matrix);
            }
        }
        this.f12502b.computeBounds(this.f12504d, false);
        float p10 = ((FloatKeyframeAnimation) this.f12510j).p();
        RectF rectF2 = this.f12504d;
        float f10 = p10 / 2.0f;
        rectF2.set(rectF2.left - f10, rectF2.top - f10, rectF2.right + f10, rectF2.bottom + f10);
        rectF.set(this.f12504d);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
        L.b("StrokeContent#getBounds");
    }

    @Override // i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        L.a("StrokeContent#draw");
        if (s4.h.i(matrix)) {
            L.b("StrokeContent#draw");
            return;
        }
        this.f12509i.setAlpha(MiscUtils.d((int) ((((i10 / 255.0f) * ((IntegerKeyframeAnimation) this.f12511k).p()) / 100.0f) * 255.0f), 0, 255));
        this.f12509i.setStrokeWidth(((FloatKeyframeAnimation) this.f12510j).p() * s4.h.h(matrix));
        if (this.f12509i.getStrokeWidth() <= 0.0f) {
            L.b("StrokeContent#draw");
            return;
        }
        f(matrix);
        BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12514n;
        if (baseKeyframeAnimation != null) {
            this.f12509i.setColorFilter(baseKeyframeAnimation.h());
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.f12515o;
        if (baseKeyframeAnimation2 != null) {
            float floatValue = baseKeyframeAnimation2.h().floatValue();
            if (floatValue == 0.0f) {
                this.f12509i.setMaskFilter(null);
            } else if (floatValue != this.f12516p) {
                this.f12509i.setMaskFilter(this.f12506f.v(floatValue));
            }
            this.f12516p = floatValue;
        }
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation = this.f12517q;
        if (dropShadowKeyframeAnimation != null) {
            dropShadowKeyframeAnimation.b(this.f12509i);
        }
        for (int i11 = 0; i11 < this.f12507g.size(); i11++) {
            b bVar = this.f12507g.get(i11);
            if (bVar.f12519b != null) {
                h(canvas, bVar, matrix);
            } else {
                L.a("StrokeContent#buildPath");
                this.f12502b.reset();
                for (int size = bVar.f12518a.size() - 1; size >= 0; size--) {
                    this.f12502b.addPath(((PathContent) bVar.f12518a.get(size)).getPath(), matrix);
                }
                L.b("StrokeContent#buildPath");
                L.a("StrokeContent#drawPath");
                canvas.drawPath(this.f12502b, this.f12509i);
                L.b("StrokeContent#drawPath");
            }
        }
        L.b("StrokeContent#draw");
    }
}
