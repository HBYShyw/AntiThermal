package i4;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import com.oplus.anim.L;
import h4.LPaint;
import j4.BaseKeyframeAnimation;
import j4.ColorKeyframeAnimation;
import j4.DropShadowKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import java.util.ArrayList;
import java.util.List;
import l4.KeyPath;
import n4.ShapeFill;
import o4.BaseLayer;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: FillContent.java */
/* renamed from: i4.g, reason: use source file name */
/* loaded from: classes.dex */
public class FillContent implements DrawingContent, BaseKeyframeAnimation.b, KeyPathElementContent {

    /* renamed from: a, reason: collision with root package name */
    private final Path f12540a;

    /* renamed from: b, reason: collision with root package name */
    private final Paint f12541b;

    /* renamed from: c, reason: collision with root package name */
    private final BaseLayer f12542c;

    /* renamed from: d, reason: collision with root package name */
    private final String f12543d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f12544e;

    /* renamed from: f, reason: collision with root package name */
    private final List<PathContent> f12545f;

    /* renamed from: g, reason: collision with root package name */
    private final BaseKeyframeAnimation<Integer, Integer> f12546g;

    /* renamed from: h, reason: collision with root package name */
    private final BaseKeyframeAnimation<Integer, Integer> f12547h;

    /* renamed from: i, reason: collision with root package name */
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> f12548i;

    /* renamed from: j, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12549j;

    /* renamed from: k, reason: collision with root package name */
    private BaseKeyframeAnimation<Float, Float> f12550k;

    /* renamed from: l, reason: collision with root package name */
    float f12551l;

    /* renamed from: m, reason: collision with root package name */
    private DropShadowKeyframeAnimation f12552m;

    public FillContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, ShapeFill shapeFill) {
        Path path = new Path();
        this.f12540a = path;
        this.f12541b = new LPaint(1);
        this.f12545f = new ArrayList();
        this.f12542c = baseLayer;
        this.f12543d = shapeFill.d();
        this.f12544e = shapeFill.f();
        this.f12549j = effectiveAnimationDrawable;
        if (baseLayer.u() != null) {
            BaseKeyframeAnimation<Float, Float> a10 = baseLayer.u().a().a();
            this.f12550k = a10;
            a10.a(this);
            baseLayer.h(this.f12550k);
        }
        if (baseLayer.w() != null) {
            this.f12552m = new DropShadowKeyframeAnimation(this, baseLayer, baseLayer.w());
        }
        if (shapeFill.b() != null && shapeFill.e() != null) {
            path.setFillType(shapeFill.c());
            BaseKeyframeAnimation<Integer, Integer> a11 = shapeFill.b().a();
            this.f12546g = a11;
            a11.a(this);
            baseLayer.h(a11);
            BaseKeyframeAnimation<Integer, Integer> a12 = shapeFill.e().a();
            this.f12547h = a12;
            a12.a(this);
            baseLayer.h(a12);
            return;
        }
        this.f12546g = null;
        this.f12547h = null;
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        this.f12549j.invalidateSelf();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        for (int i10 = 0; i10 < list2.size(); i10++) {
            Content content = list2.get(i10);
            if (content instanceof PathContent) {
                this.f12545f.add((PathContent) content);
            }
        }
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation2;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation3;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation4;
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation5;
        if (t7 == EffectiveAnimationProperty.f9675a) {
            this.f12546g.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9678d) {
            this.f12547h.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.K) {
            BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12548i;
            if (baseKeyframeAnimation != null) {
                this.f12542c.F(baseKeyframeAnimation);
            }
            if (effectiveValueCallback == null) {
                this.f12548i = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12548i = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.a(this);
            this.f12542c.h(this.f12548i);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9684j) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.f12550k;
            if (baseKeyframeAnimation2 != null) {
                baseKeyframeAnimation2.n(effectiveValueCallback);
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12550k = valueCallbackKeyframeAnimation2;
            valueCallbackKeyframeAnimation2.a(this);
            this.f12542c.h(this.f12550k);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9679e && (dropShadowKeyframeAnimation5 = this.f12552m) != null) {
            dropShadowKeyframeAnimation5.c(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.G && (dropShadowKeyframeAnimation4 = this.f12552m) != null) {
            dropShadowKeyframeAnimation4.f(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.H && (dropShadowKeyframeAnimation3 = this.f12552m) != null) {
            dropShadowKeyframeAnimation3.d(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.I && (dropShadowKeyframeAnimation2 = this.f12552m) != null) {
            dropShadowKeyframeAnimation2.e(effectiveValueCallback);
        } else {
            if (t7 != EffectiveAnimationProperty.J || (dropShadowKeyframeAnimation = this.f12552m) == null) {
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
        this.f12540a.reset();
        for (int i10 = 0; i10 < this.f12545f.size(); i10++) {
            this.f12540a.addPath(this.f12545f.get(i10).getPath(), matrix);
        }
        this.f12540a.computeBounds(rectF, false);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
    }

    @Override // i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        if (this.f12544e) {
            return;
        }
        L.a("FillContent#draw");
        this.f12541b.setColor(((ColorKeyframeAnimation) this.f12546g).p());
        this.f12541b.setAlpha(MiscUtils.d((int) ((((i10 / 255.0f) * this.f12547h.h().intValue()) / 100.0f) * 255.0f), 0, 255));
        BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12548i;
        if (baseKeyframeAnimation != null) {
            this.f12541b.setColorFilter(baseKeyframeAnimation.h());
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.f12550k;
        if (baseKeyframeAnimation2 != null) {
            float floatValue = baseKeyframeAnimation2.h().floatValue();
            if (floatValue == 0.0f) {
                this.f12541b.setMaskFilter(null);
            } else if (floatValue != this.f12551l) {
                this.f12541b.setMaskFilter(this.f12542c.v(floatValue));
            }
            this.f12551l = floatValue;
        }
        DropShadowKeyframeAnimation dropShadowKeyframeAnimation = this.f12552m;
        if (dropShadowKeyframeAnimation != null) {
            dropShadowKeyframeAnimation.b(this.f12541b);
        }
        this.f12540a.reset();
        for (int i11 = 0; i11 < this.f12545f.size(); i11++) {
            this.f12540a.addPath(this.f12545f.get(i11).getPath(), matrix);
        }
        canvas.drawPath(this.f12540a, this.f12541b);
        L.b("FillContent#draw");
    }

    @Override // i4.Content
    public String getName() {
        return this.f12543d;
    }
}
