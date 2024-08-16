package i4;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import j4.BaseKeyframeAnimation;
import j4.ColorKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import n4.ShapeStroke;
import o4.BaseLayer;
import t4.EffectiveValueCallback;

/* compiled from: StrokeContent.java */
/* renamed from: i4.r, reason: use source file name */
/* loaded from: classes.dex */
public class StrokeContent extends BaseStrokeContent {

    /* renamed from: r, reason: collision with root package name */
    private final BaseLayer f12632r;

    /* renamed from: s, reason: collision with root package name */
    private final String f12633s;

    /* renamed from: t, reason: collision with root package name */
    private final boolean f12634t;

    /* renamed from: u, reason: collision with root package name */
    private final BaseKeyframeAnimation<Integer, Integer> f12635u;

    /* renamed from: v, reason: collision with root package name */
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> f12636v;

    public StrokeContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, ShapeStroke shapeStroke) {
        super(effectiveAnimationDrawable, baseLayer, shapeStroke.b().a(), shapeStroke.e().a(), shapeStroke.g(), shapeStroke.i(), shapeStroke.j(), shapeStroke.f(), shapeStroke.d());
        this.f12632r = baseLayer;
        this.f12633s = shapeStroke.h();
        this.f12634t = shapeStroke.k();
        BaseKeyframeAnimation<Integer, Integer> a10 = shapeStroke.c().a();
        this.f12635u = a10;
        a10.a(this);
        baseLayer.h(a10);
    }

    @Override // i4.BaseStrokeContent, l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        super.c(t7, effectiveValueCallback);
        if (t7 == EffectiveAnimationProperty.f9676b) {
            this.f12635u.n(effectiveValueCallback);
            return;
        }
        if (t7 == EffectiveAnimationProperty.K) {
            BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12636v;
            if (baseKeyframeAnimation != null) {
                this.f12632r.F(baseKeyframeAnimation);
            }
            if (effectiveValueCallback == null) {
                this.f12636v = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.f12636v = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.a(this);
            this.f12632r.h(this.f12635u);
        }
    }

    @Override // i4.BaseStrokeContent, i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        if (this.f12634t) {
            return;
        }
        this.f12509i.setColor(((ColorKeyframeAnimation) this.f12635u).p());
        BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.f12636v;
        if (baseKeyframeAnimation != null) {
            this.f12509i.setColorFilter(baseKeyframeAnimation.h());
        }
        super.g(canvas, matrix, i10);
    }

    @Override // i4.Content
    public String getName() {
        return this.f12633s;
    }
}
