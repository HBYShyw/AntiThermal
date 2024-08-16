package i4;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import j4.BaseKeyframeAnimation;
import j4.TransformKeyframeAnimation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import l4.KeyPath;
import n4.Repeater;
import o4.BaseLayer;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: RepeaterContent.java */
/* renamed from: i4.p, reason: use source file name */
/* loaded from: classes.dex */
public class RepeaterContent implements DrawingContent, PathContent, GreedyContent, BaseKeyframeAnimation.b, KeyPathElementContent {

    /* renamed from: a, reason: collision with root package name */
    private final Matrix f12615a = new Matrix();

    /* renamed from: b, reason: collision with root package name */
    private final Path f12616b = new Path();

    /* renamed from: c, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12617c;

    /* renamed from: d, reason: collision with root package name */
    private final BaseLayer f12618d;

    /* renamed from: e, reason: collision with root package name */
    private final String f12619e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f12620f;

    /* renamed from: g, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12621g;

    /* renamed from: h, reason: collision with root package name */
    private final BaseKeyframeAnimation<Float, Float> f12622h;

    /* renamed from: i, reason: collision with root package name */
    private final TransformKeyframeAnimation f12623i;

    /* renamed from: j, reason: collision with root package name */
    private ContentGroup f12624j;

    public RepeaterContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, Repeater repeater) {
        this.f12617c = effectiveAnimationDrawable;
        this.f12618d = baseLayer;
        this.f12619e = repeater.c();
        this.f12620f = repeater.f();
        BaseKeyframeAnimation<Float, Float> a10 = repeater.b().a();
        this.f12621g = a10;
        baseLayer.h(a10);
        a10.a(this);
        BaseKeyframeAnimation<Float, Float> a11 = repeater.d().a();
        this.f12622h = a11;
        baseLayer.h(a11);
        a11.a(this);
        TransformKeyframeAnimation b10 = repeater.e().b();
        this.f12623i = b10;
        b10.a(baseLayer);
        b10.b(this);
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        this.f12617c.invalidateSelf();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        this.f12624j.b(list, list2);
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        if (this.f12623i.c(t7, effectiveValueCallback)) {
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9695u) {
            this.f12621g.n(effectiveValueCallback);
        } else if (t7 == EffectiveAnimationProperty.f9696v) {
            this.f12622h.n(effectiveValueCallback);
        }
    }

    @Override // l4.KeyPathElement
    public void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.m(keyPath, i10, list, keyPath2, this);
    }

    @Override // i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        this.f12624j.e(rectF, matrix, z10);
    }

    @Override // i4.GreedyContent
    public void f(ListIterator<Content> listIterator) {
        if (this.f12624j != null) {
            return;
        }
        while (listIterator.hasPrevious() && listIterator.previous() != this) {
        }
        ArrayList arrayList = new ArrayList();
        while (listIterator.hasPrevious()) {
            arrayList.add(listIterator.previous());
            listIterator.remove();
        }
        Collections.reverse(arrayList);
        this.f12624j = new ContentGroup(this.f12617c, this.f12618d, "Repeater", this.f12620f, arrayList, null);
    }

    @Override // i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        float floatValue = this.f12621g.h().floatValue();
        float floatValue2 = this.f12622h.h().floatValue();
        float floatValue3 = this.f12623i.i().h().floatValue() / 100.0f;
        float floatValue4 = this.f12623i.e().h().floatValue() / 100.0f;
        for (int i11 = ((int) floatValue) - 1; i11 >= 0; i11--) {
            this.f12615a.set(matrix);
            float f10 = i11;
            this.f12615a.preConcat(this.f12623i.g(f10 + floatValue2));
            this.f12624j.g(canvas, this.f12615a, (int) (i10 * MiscUtils.k(floatValue3, floatValue4, f10 / floatValue)));
        }
    }

    @Override // i4.Content
    public String getName() {
        return this.f12619e;
    }

    @Override // i4.PathContent
    public Path getPath() {
        Path path = this.f12624j.getPath();
        this.f12616b.reset();
        float floatValue = this.f12621g.h().floatValue();
        float floatValue2 = this.f12622h.h().floatValue();
        for (int i10 = ((int) floatValue) - 1; i10 >= 0; i10--) {
            this.f12615a.set(this.f12623i.g(i10 + floatValue2));
            this.f12616b.addPath(path, this.f12615a);
        }
        return this.f12616b;
    }
}
