package i4;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import j4.BaseKeyframeAnimation;
import j4.FloatKeyframeAnimation;
import java.util.List;
import l4.KeyPath;
import n4.RectangleShape;
import n4.ShapeTrimPath;
import o4.BaseLayer;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: RectangleContent.java */
/* renamed from: i4.o, reason: use source file name */
/* loaded from: classes.dex */
public class RectangleContent implements BaseKeyframeAnimation.b, KeyPathElementContent, PathContent {

    /* renamed from: c, reason: collision with root package name */
    private final String f12607c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f12608d;

    /* renamed from: e, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12609e;

    /* renamed from: f, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, PointF> f12610f;

    /* renamed from: g, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, PointF> f12611g;

    /* renamed from: h, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12612h;

    /* renamed from: j, reason: collision with root package name */
    private boolean f12614j;

    /* renamed from: a, reason: collision with root package name */
    private final Path f12605a = new Path();

    /* renamed from: b, reason: collision with root package name */
    private final RectF f12606b = new RectF();

    /* renamed from: i, reason: collision with root package name */
    private final CompoundTrimPathContent f12613i = new CompoundTrimPathContent();

    public RectangleContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, RectangleShape rectangleShape) {
        this.f12607c = rectangleShape.c();
        this.f12608d = rectangleShape.f();
        this.f12609e = effectiveAnimationDrawable;
        BaseKeyframeAnimation<PointF, PointF> a10 = rectangleShape.d().a();
        this.f12610f = a10;
        BaseKeyframeAnimation<PointF, PointF> a11 = rectangleShape.e().a();
        this.f12611g = a11;
        BaseKeyframeAnimation<Float, Float> a12 = rectangleShape.b().a();
        this.f12612h = a12;
        baseLayer.h(a10);
        baseLayer.h(a11);
        baseLayer.h(a12);
        a10.a(this);
        a11.a(this);
        a12.a(this);
    }

    private void f() {
        this.f12614j = false;
        this.f12609e.invalidateSelf();
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        f();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        for (int i10 = 0; i10 < list.size(); i10++) {
            Content content = list.get(i10);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.i() == ShapeTrimPath.a.SIMULTANEOUSLY) {
                    this.f12613i.a(trimPathContent);
                    trimPathContent.c(this);
                }
            }
        }
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        if (t7 == EffectiveAnimationProperty.f9686l) {
            this.f12611g.n(effectiveValueCallback);
        } else if (t7 == EffectiveAnimationProperty.f9688n) {
            this.f12610f.n(effectiveValueCallback);
        } else if (t7 == EffectiveAnimationProperty.f9687m) {
            this.f12612h.n(effectiveValueCallback);
        }
    }

    @Override // l4.KeyPathElement
    public void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.m(keyPath, i10, list, keyPath2, this);
    }

    @Override // i4.Content
    public String getName() {
        return this.f12607c;
    }

    @Override // i4.PathContent
    public Path getPath() {
        if (this.f12614j) {
            return this.f12605a;
        }
        this.f12605a.reset();
        if (this.f12608d) {
            this.f12614j = true;
            return this.f12605a;
        }
        PointF h10 = this.f12611g.h();
        float f10 = h10.x / 2.0f;
        float f11 = h10.y / 2.0f;
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation = this.f12612h;
        float p10 = baseKeyframeAnimation == null ? 0.0f : ((FloatKeyframeAnimation) baseKeyframeAnimation).p();
        float min = Math.min(f10, f11);
        if (p10 > min) {
            p10 = min;
        }
        PointF h11 = this.f12610f.h();
        this.f12605a.moveTo(h11.x + f10, (h11.y - f11) + p10);
        this.f12605a.lineTo(h11.x + f10, (h11.y + f11) - p10);
        if (p10 > 0.0f) {
            RectF rectF = this.f12606b;
            float f12 = h11.x;
            float f13 = p10 * 2.0f;
            float f14 = h11.y;
            rectF.set((f12 + f10) - f13, (f14 + f11) - f13, f12 + f10, f14 + f11);
            this.f12605a.arcTo(this.f12606b, 0.0f, 90.0f, false);
        }
        this.f12605a.lineTo((h11.x - f10) + p10, h11.y + f11);
        if (p10 > 0.0f) {
            RectF rectF2 = this.f12606b;
            float f15 = h11.x;
            float f16 = h11.y;
            float f17 = p10 * 2.0f;
            rectF2.set(f15 - f10, (f16 + f11) - f17, (f15 - f10) + f17, f16 + f11);
            this.f12605a.arcTo(this.f12606b, 90.0f, 90.0f, false);
        }
        this.f12605a.lineTo(h11.x - f10, (h11.y - f11) + p10);
        if (p10 > 0.0f) {
            RectF rectF3 = this.f12606b;
            float f18 = h11.x;
            float f19 = h11.y;
            float f20 = p10 * 2.0f;
            rectF3.set(f18 - f10, f19 - f11, (f18 - f10) + f20, (f19 - f11) + f20);
            this.f12605a.arcTo(this.f12606b, 180.0f, 90.0f, false);
        }
        this.f12605a.lineTo((h11.x + f10) - p10, h11.y - f11);
        if (p10 > 0.0f) {
            RectF rectF4 = this.f12606b;
            float f21 = h11.x;
            float f22 = p10 * 2.0f;
            float f23 = h11.y;
            rectF4.set((f21 + f10) - f22, f23 - f11, f21 + f10, (f23 - f11) + f22);
            this.f12605a.arcTo(this.f12606b, 270.0f, 90.0f, false);
        }
        this.f12605a.close();
        this.f12613i.b(this.f12605a);
        this.f12614j = true;
        return this.f12605a;
    }
}
