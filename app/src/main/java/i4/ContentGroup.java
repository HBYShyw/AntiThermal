package i4;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import h4.LPaint;
import j4.BaseKeyframeAnimation;
import j4.TransformKeyframeAnimation;
import java.util.ArrayList;
import java.util.List;
import l4.KeyPath;
import l4.KeyPathElement;
import m4.AnimatableTransform;
import n4.ContentModel;
import n4.ShapeGroup;
import o4.BaseLayer;
import t4.EffectiveValueCallback;

/* compiled from: ContentGroup.java */
/* renamed from: i4.d, reason: use source file name */
/* loaded from: classes.dex */
public class ContentGroup implements DrawingContent, PathContent, BaseKeyframeAnimation.b, KeyPathElement {

    /* renamed from: a, reason: collision with root package name */
    private final Paint f12521a;

    /* renamed from: b, reason: collision with root package name */
    private final RectF f12522b;

    /* renamed from: c, reason: collision with root package name */
    private final Matrix f12523c;

    /* renamed from: d, reason: collision with root package name */
    private final Path f12524d;

    /* renamed from: e, reason: collision with root package name */
    private final RectF f12525e;

    /* renamed from: f, reason: collision with root package name */
    private final String f12526f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f12527g;

    /* renamed from: h, reason: collision with root package name */
    private final List<Content> f12528h;

    /* renamed from: i, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12529i;

    /* renamed from: j, reason: collision with root package name */
    private List<PathContent> f12530j;

    /* renamed from: k, reason: collision with root package name */
    private TransformKeyframeAnimation f12531k;

    public ContentGroup(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, ShapeGroup shapeGroup) {
        this(effectiveAnimationDrawable, baseLayer, shapeGroup.c(), shapeGroup.d(), f(effectiveAnimationDrawable, baseLayer, shapeGroup.b()), h(shapeGroup.b()));
    }

    private static List<Content> f(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, List<ContentModel> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (int i10 = 0; i10 < list.size(); i10++) {
            Content a10 = list.get(i10).a(effectiveAnimationDrawable, baseLayer);
            if (a10 != null) {
                arrayList.add(a10);
            }
        }
        return arrayList;
    }

    static AnimatableTransform h(List<ContentModel> list) {
        for (int i10 = 0; i10 < list.size(); i10++) {
            ContentModel contentModel = list.get(i10);
            if (contentModel instanceof AnimatableTransform) {
                return (AnimatableTransform) contentModel;
            }
        }
        return null;
    }

    private boolean k() {
        int i10 = 0;
        for (int i11 = 0; i11 < this.f12528h.size(); i11++) {
            if ((this.f12528h.get(i11) instanceof DrawingContent) && (i10 = i10 + 1) >= 2) {
                return true;
            }
        }
        return false;
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        this.f12529i.invalidateSelf();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        ArrayList arrayList = new ArrayList(list.size() + this.f12528h.size());
        arrayList.addAll(list);
        for (int size = this.f12528h.size() - 1; size >= 0; size--) {
            Content content = this.f12528h.get(size);
            content.b(arrayList, this.f12528h.subList(0, size));
            arrayList.add(content);
        }
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        TransformKeyframeAnimation transformKeyframeAnimation = this.f12531k;
        if (transformKeyframeAnimation != null) {
            transformKeyframeAnimation.c(t7, effectiveValueCallback);
        }
    }

    @Override // l4.KeyPathElement
    public void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        if (keyPath.g(getName(), i10) || "__container".equals(getName())) {
            if (!"__container".equals(getName())) {
                keyPath2 = keyPath2.a(getName());
                if (keyPath.c(getName(), i10)) {
                    list.add(keyPath2.i(this));
                }
            }
            if (keyPath.h(getName(), i10)) {
                int e10 = i10 + keyPath.e(getName(), i10);
                for (int i11 = 0; i11 < this.f12528h.size(); i11++) {
                    Content content = this.f12528h.get(i11);
                    if (content instanceof KeyPathElement) {
                        ((KeyPathElement) content).d(keyPath, e10, list, keyPath2);
                    }
                }
            }
        }
    }

    @Override // i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        this.f12523c.set(matrix);
        TransformKeyframeAnimation transformKeyframeAnimation = this.f12531k;
        if (transformKeyframeAnimation != null) {
            this.f12523c.preConcat(transformKeyframeAnimation.f());
        }
        this.f12525e.set(0.0f, 0.0f, 0.0f, 0.0f);
        for (int size = this.f12528h.size() - 1; size >= 0; size--) {
            Content content = this.f12528h.get(size);
            if (content instanceof DrawingContent) {
                ((DrawingContent) content).e(this.f12525e, this.f12523c, z10);
                rectF.union(this.f12525e);
            }
        }
    }

    @Override // i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        if (this.f12527g) {
            return;
        }
        this.f12523c.set(matrix);
        TransformKeyframeAnimation transformKeyframeAnimation = this.f12531k;
        if (transformKeyframeAnimation != null) {
            this.f12523c.preConcat(transformKeyframeAnimation.f());
            i10 = (int) (((((this.f12531k.h() == null ? 100 : this.f12531k.h().h().intValue()) / 100.0f) * i10) / 255.0f) * 255.0f);
        }
        boolean z10 = this.f12529i.J() && k() && i10 != 255;
        if (z10) {
            this.f12522b.set(0.0f, 0.0f, 0.0f, 0.0f);
            e(this.f12522b, this.f12523c, true);
            this.f12521a.setAlpha(i10);
            s4.h.n(canvas, this.f12522b, this.f12521a);
        }
        if (z10) {
            i10 = 255;
        }
        for (int size = this.f12528h.size() - 1; size >= 0; size--) {
            Content content = this.f12528h.get(size);
            if (content instanceof DrawingContent) {
                ((DrawingContent) content).g(canvas, this.f12523c, i10);
            }
        }
        if (z10) {
            canvas.restore();
        }
    }

    @Override // i4.Content
    public String getName() {
        return this.f12526f;
    }

    @Override // i4.PathContent
    public Path getPath() {
        this.f12523c.reset();
        TransformKeyframeAnimation transformKeyframeAnimation = this.f12531k;
        if (transformKeyframeAnimation != null) {
            this.f12523c.set(transformKeyframeAnimation.f());
        }
        this.f12524d.reset();
        if (this.f12527g) {
            return this.f12524d;
        }
        for (int size = this.f12528h.size() - 1; size >= 0; size--) {
            Content content = this.f12528h.get(size);
            if (content instanceof PathContent) {
                this.f12524d.addPath(((PathContent) content).getPath(), this.f12523c);
            }
        }
        return this.f12524d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<PathContent> i() {
        if (this.f12530j == null) {
            this.f12530j = new ArrayList();
            for (int i10 = 0; i10 < this.f12528h.size(); i10++) {
                Content content = this.f12528h.get(i10);
                if (content instanceof PathContent) {
                    this.f12530j.add((PathContent) content);
                }
            }
        }
        return this.f12530j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Matrix j() {
        TransformKeyframeAnimation transformKeyframeAnimation = this.f12531k;
        if (transformKeyframeAnimation != null) {
            return transformKeyframeAnimation.f();
        }
        this.f12523c.reset();
        return this.f12523c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentGroup(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, String str, boolean z10, List<Content> list, AnimatableTransform animatableTransform) {
        this.f12521a = new LPaint();
        this.f12522b = new RectF();
        this.f12523c = new Matrix();
        this.f12524d = new Path();
        this.f12525e = new RectF();
        this.f12526f = str;
        this.f12529i = effectiveAnimationDrawable;
        this.f12527g = z10;
        this.f12528h = list;
        if (animatableTransform != null) {
            TransformKeyframeAnimation b10 = animatableTransform.b();
            this.f12531k = b10;
            b10.a(baseLayer);
            this.f12531k.b(this);
        }
        ArrayList arrayList = new ArrayList();
        for (int size = list.size() - 1; size >= 0; size--) {
            Content content = list.get(size);
            if (content instanceof GreedyContent) {
                arrayList.add((GreedyContent) content);
            }
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            ((GreedyContent) arrayList.get(size2)).f(list.listIterator(list.size()));
        }
    }
}
