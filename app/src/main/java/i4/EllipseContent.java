package i4;

import android.graphics.Path;
import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import j4.BaseKeyframeAnimation;
import java.util.List;
import l4.KeyPath;
import n4.CircleShape;
import n4.ShapeTrimPath;
import o4.BaseLayer;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: EllipseContent.java */
/* renamed from: i4.f, reason: use source file name */
/* loaded from: classes.dex */
public class EllipseContent implements PathContent, BaseKeyframeAnimation.b, KeyPathElementContent {

    /* renamed from: b, reason: collision with root package name */
    private final String f12533b;

    /* renamed from: c, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12534c;

    /* renamed from: d, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, PointF> f12535d;

    /* renamed from: e, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, PointF> f12536e;

    /* renamed from: f, reason: collision with root package name */
    private final CircleShape f12537f;

    /* renamed from: h, reason: collision with root package name */
    private boolean f12539h;

    /* renamed from: a, reason: collision with root package name */
    private final Path f12532a = new Path();

    /* renamed from: g, reason: collision with root package name */
    private final CompoundTrimPathContent f12538g = new CompoundTrimPathContent();

    public EllipseContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, CircleShape circleShape) {
        this.f12533b = circleShape.b();
        this.f12534c = effectiveAnimationDrawable;
        BaseKeyframeAnimation<PointF, PointF> a10 = circleShape.d().a();
        this.f12535d = a10;
        BaseKeyframeAnimation<PointF, PointF> a11 = circleShape.c().a();
        this.f12536e = a11;
        this.f12537f = circleShape;
        baseLayer.h(a10);
        baseLayer.h(a11);
        a10.a(this);
        a11.a(this);
    }

    private void f() {
        this.f12539h = false;
        this.f12534c.invalidateSelf();
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
                    this.f12538g.a(trimPathContent);
                    trimPathContent.c(this);
                }
            }
        }
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        if (t7 == EffectiveAnimationProperty.f9685k) {
            this.f12535d.n(effectiveValueCallback);
        } else if (t7 == EffectiveAnimationProperty.f9688n) {
            this.f12536e.n(effectiveValueCallback);
        }
    }

    @Override // l4.KeyPathElement
    public void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.m(keyPath, i10, list, keyPath2, this);
    }

    @Override // i4.Content
    public String getName() {
        return this.f12533b;
    }

    @Override // i4.PathContent
    public Path getPath() {
        if (this.f12539h) {
            return this.f12532a;
        }
        this.f12532a.reset();
        if (this.f12537f.e()) {
            this.f12539h = true;
            return this.f12532a;
        }
        PointF h10 = this.f12535d.h();
        float f10 = h10.x / 2.0f;
        float f11 = h10.y / 2.0f;
        float f12 = f10 * 0.55228f;
        float f13 = 0.55228f * f11;
        this.f12532a.reset();
        if (this.f12537f.f()) {
            float f14 = -f11;
            this.f12532a.moveTo(0.0f, f14);
            float f15 = 0.0f - f12;
            float f16 = -f10;
            float f17 = 0.0f - f13;
            this.f12532a.cubicTo(f15, f14, f16, f17, f16, 0.0f);
            float f18 = f13 + 0.0f;
            this.f12532a.cubicTo(f16, f18, f15, f11, 0.0f, f11);
            float f19 = f12 + 0.0f;
            this.f12532a.cubicTo(f19, f11, f10, f18, f10, 0.0f);
            this.f12532a.cubicTo(f10, f17, f19, f14, 0.0f, f14);
        } else {
            float f20 = -f11;
            this.f12532a.moveTo(0.0f, f20);
            float f21 = f12 + 0.0f;
            float f22 = 0.0f - f13;
            this.f12532a.cubicTo(f21, f20, f10, f22, f10, 0.0f);
            float f23 = f13 + 0.0f;
            this.f12532a.cubicTo(f10, f23, f21, f11, 0.0f, f11);
            float f24 = 0.0f - f12;
            float f25 = -f10;
            this.f12532a.cubicTo(f24, f11, f25, f23, f25, 0.0f);
            this.f12532a.cubicTo(f25, f22, f24, f20, 0.0f, f20);
        }
        PointF h11 = this.f12536e.h();
        this.f12532a.offset(h11.x, h11.y);
        this.f12532a.close();
        this.f12538g.b(this.f12532a);
        this.f12539h = true;
        return this.f12532a;
    }
}
