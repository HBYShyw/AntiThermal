package o4;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationComposition;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.L;
import h4.LPaint;
import i4.Content;
import i4.DrawingContent;
import j4.BaseKeyframeAnimation;
import j4.FloatKeyframeAnimation;
import j4.MaskKeyframeAnimation;
import j4.TransformKeyframeAnimation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import l4.KeyPath;
import l4.KeyPathElement;
import n4.BlurEffect;
import n4.Mask;
import n4.ShapeData;
import o4.e;
import q4.DropShadowEffect;
import t4.EffectiveValueCallback;

/* compiled from: BaseLayer.java */
/* renamed from: o4.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseLayer implements DrawingContent, BaseKeyframeAnimation.b, KeyPathElement {
    BlurMaskFilter A;

    /* renamed from: a, reason: collision with root package name */
    private final Path f16186a = new Path();

    /* renamed from: b, reason: collision with root package name */
    private final Matrix f16187b = new Matrix();

    /* renamed from: c, reason: collision with root package name */
    private final Paint f16188c = new LPaint(1);

    /* renamed from: d, reason: collision with root package name */
    private final Paint f16189d = new LPaint(1, PorterDuff.Mode.DST_IN);

    /* renamed from: e, reason: collision with root package name */
    private final Paint f16190e = new LPaint(1, PorterDuff.Mode.DST_OUT);

    /* renamed from: f, reason: collision with root package name */
    private final Paint f16191f;

    /* renamed from: g, reason: collision with root package name */
    private final Paint f16192g;

    /* renamed from: h, reason: collision with root package name */
    private final RectF f16193h;

    /* renamed from: i, reason: collision with root package name */
    private final RectF f16194i;

    /* renamed from: j, reason: collision with root package name */
    private final RectF f16195j;

    /* renamed from: k, reason: collision with root package name */
    private final RectF f16196k;

    /* renamed from: l, reason: collision with root package name */
    private final String f16197l;

    /* renamed from: m, reason: collision with root package name */
    final Matrix f16198m;

    /* renamed from: n, reason: collision with root package name */
    final EffectiveAnimationDrawable f16199n;

    /* renamed from: o, reason: collision with root package name */
    final e f16200o;

    /* renamed from: p, reason: collision with root package name */
    private MaskKeyframeAnimation f16201p;

    /* renamed from: q, reason: collision with root package name */
    private FloatKeyframeAnimation f16202q;

    /* renamed from: r, reason: collision with root package name */
    private BaseLayer f16203r;

    /* renamed from: s, reason: collision with root package name */
    private BaseLayer f16204s;

    /* renamed from: t, reason: collision with root package name */
    private List<BaseLayer> f16205t;

    /* renamed from: u, reason: collision with root package name */
    private final List<BaseKeyframeAnimation<?, ?>> f16206u;

    /* renamed from: v, reason: collision with root package name */
    final TransformKeyframeAnimation f16207v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f16208w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f16209x;

    /* renamed from: y, reason: collision with root package name */
    private Paint f16210y;

    /* renamed from: z, reason: collision with root package name */
    float f16211z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BaseLayer.java */
    /* renamed from: o4.b$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f16212a;

        /* renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f16213b;

        static {
            int[] iArr = new int[Mask.a.values().length];
            f16213b = iArr;
            try {
                iArr[Mask.a.MASK_MODE_NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f16213b[Mask.a.MASK_MODE_SUBTRACT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f16213b[Mask.a.MASK_MODE_INTERSECT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f16213b[Mask.a.MASK_MODE_ADD.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[e.a.values().length];
            f16212a = iArr2;
            try {
                iArr2[e.a.SHAPE.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f16212a[e.a.PRE_COMP.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f16212a[e.a.SOLID.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f16212a[e.a.IMAGE.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f16212a[e.a.NULL.ordinal()] = 5;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f16212a[e.a.TEXT.ordinal()] = 6;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f16212a[e.a.UNKNOWN.ordinal()] = 7;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseLayer(EffectiveAnimationDrawable effectiveAnimationDrawable, e eVar) {
        LPaint lPaint = new LPaint(1);
        this.f16191f = lPaint;
        this.f16192g = new LPaint(PorterDuff.Mode.CLEAR);
        this.f16193h = new RectF();
        this.f16194i = new RectF();
        this.f16195j = new RectF();
        this.f16196k = new RectF();
        this.f16198m = new Matrix();
        this.f16206u = new ArrayList();
        this.f16208w = true;
        this.f16211z = 0.0f;
        this.f16199n = effectiveAnimationDrawable;
        this.f16200o = eVar;
        this.f16197l = eVar.i() + "#draw";
        if (eVar.h() == e.b.INVERT) {
            lPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        } else {
            lPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }
        TransformKeyframeAnimation b10 = eVar.w().b();
        this.f16207v = b10;
        b10.b(this);
        if (eVar.g() != null && !eVar.g().isEmpty()) {
            MaskKeyframeAnimation maskKeyframeAnimation = new MaskKeyframeAnimation(eVar.g());
            this.f16201p = maskKeyframeAnimation;
            Iterator<BaseKeyframeAnimation<ShapeData, Path>> it = maskKeyframeAnimation.a().iterator();
            while (it.hasNext()) {
                it.next().a(this);
            }
            for (BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation : this.f16201p.c()) {
                h(baseKeyframeAnimation);
                baseKeyframeAnimation.a(this);
            }
        }
        M();
    }

    private void A(RectF rectF, Matrix matrix) {
        this.f16194i.set(0.0f, 0.0f, 0.0f, 0.0f);
        if (y()) {
            int size = this.f16201p.b().size();
            for (int i10 = 0; i10 < size; i10++) {
                Mask mask = this.f16201p.b().get(i10);
                Path h10 = this.f16201p.a().get(i10).h();
                if (h10 != null) {
                    this.f16186a.set(h10);
                    this.f16186a.transform(matrix);
                    int i11 = a.f16213b[mask.a().ordinal()];
                    if (i11 == 1 || i11 == 2) {
                        return;
                    }
                    if (i11 != 3 && i11 != 4) {
                        this.f16186a.computeBounds(this.f16196k, false);
                        if (i10 == 0) {
                            this.f16194i.set(this.f16196k);
                        } else {
                            RectF rectF2 = this.f16194i;
                            rectF2.set(Math.min(rectF2.left, this.f16196k.left), Math.min(this.f16194i.top, this.f16196k.top), Math.max(this.f16194i.right, this.f16196k.right), Math.max(this.f16194i.bottom, this.f16196k.bottom));
                        }
                    } else {
                        if (mask.d()) {
                            return;
                        }
                        this.f16186a.computeBounds(this.f16196k, false);
                        if (i10 == 0) {
                            this.f16194i.set(this.f16196k);
                        } else {
                            RectF rectF3 = this.f16194i;
                            rectF3.set(Math.min(rectF3.left, this.f16196k.left), Math.min(this.f16194i.top, this.f16196k.top), Math.max(this.f16194i.right, this.f16196k.right), Math.max(this.f16194i.bottom, this.f16196k.bottom));
                        }
                    }
                }
            }
            if (rectF.intersect(this.f16194i)) {
                return;
            }
            rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    private void B(RectF rectF, Matrix matrix) {
        if (z() && this.f16200o.h() != e.b.INVERT) {
            this.f16195j.set(0.0f, 0.0f, 0.0f, 0.0f);
            this.f16203r.e(this.f16195j, matrix, true);
            if (rectF.intersect(this.f16195j)) {
                return;
            }
            rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    private void C() {
        this.f16199n.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void D() {
        L(this.f16202q.p() == 1.0f);
    }

    private void E(float f10) {
        this.f16199n.q().o().a(this.f16200o.i(), f10);
    }

    private void L(boolean z10) {
        if (z10 != this.f16208w) {
            this.f16208w = z10;
            C();
        }
    }

    private void M() {
        if (!this.f16200o.e().isEmpty()) {
            FloatKeyframeAnimation floatKeyframeAnimation = new FloatKeyframeAnimation(this.f16200o.e());
            this.f16202q = floatKeyframeAnimation;
            floatKeyframeAnimation.l();
            this.f16202q.a(new BaseKeyframeAnimation.b() { // from class: o4.a
                @Override // j4.BaseKeyframeAnimation.b
                public final void a() {
                    BaseLayer.this.D();
                }
            });
            L(this.f16202q.h().floatValue() == 1.0f);
            h(this.f16202q);
            return;
        }
        L(true);
    }

    private void i(Canvas canvas, Matrix matrix, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        this.f16186a.set(baseKeyframeAnimation.h());
        this.f16186a.transform(matrix);
        this.f16188c.setAlpha((int) (baseKeyframeAnimation2.h().intValue() * 2.55f));
        canvas.drawPath(this.f16186a, this.f16188c);
    }

    private void j(Canvas canvas, Matrix matrix, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        s4.h.n(canvas, this.f16193h, this.f16189d);
        this.f16186a.set(baseKeyframeAnimation.h());
        this.f16186a.transform(matrix);
        this.f16188c.setAlpha((int) (baseKeyframeAnimation2.h().intValue() * 2.55f));
        canvas.drawPath(this.f16186a, this.f16188c);
        canvas.restore();
    }

    private void k(Canvas canvas, Matrix matrix, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        s4.h.n(canvas, this.f16193h, this.f16188c);
        canvas.drawRect(this.f16193h, this.f16188c);
        this.f16186a.set(baseKeyframeAnimation.h());
        this.f16186a.transform(matrix);
        this.f16188c.setAlpha((int) (baseKeyframeAnimation2.h().intValue() * 2.55f));
        canvas.drawPath(this.f16186a, this.f16190e);
        canvas.restore();
    }

    private void l(Canvas canvas, Matrix matrix, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        s4.h.n(canvas, this.f16193h, this.f16189d);
        canvas.drawRect(this.f16193h, this.f16188c);
        this.f16190e.setAlpha((int) (baseKeyframeAnimation2.h().intValue() * 2.55f));
        this.f16186a.set(baseKeyframeAnimation.h());
        this.f16186a.transform(matrix);
        canvas.drawPath(this.f16186a, this.f16190e);
        canvas.restore();
    }

    private void m(Canvas canvas, Matrix matrix, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        s4.h.n(canvas, this.f16193h, this.f16190e);
        canvas.drawRect(this.f16193h, this.f16188c);
        this.f16190e.setAlpha((int) (baseKeyframeAnimation2.h().intValue() * 2.55f));
        this.f16186a.set(baseKeyframeAnimation.h());
        this.f16186a.transform(matrix);
        canvas.drawPath(this.f16186a, this.f16190e);
        canvas.restore();
    }

    private void n(Canvas canvas, Matrix matrix) {
        L.a("Layer#saveLayer");
        s4.h.o(canvas, this.f16193h, this.f16189d, 19);
        L.b("Layer#saveLayer");
        for (int i10 = 0; i10 < this.f16201p.b().size(); i10++) {
            Mask mask = this.f16201p.b().get(i10);
            BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation = this.f16201p.a().get(i10);
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2 = this.f16201p.c().get(i10);
            int i11 = a.f16213b[mask.a().ordinal()];
            if (i11 != 1) {
                if (i11 == 2) {
                    if (i10 == 0) {
                        this.f16188c.setColor(-16777216);
                        this.f16188c.setAlpha(255);
                        canvas.drawRect(this.f16193h, this.f16188c);
                    }
                    if (mask.d()) {
                        m(canvas, matrix, baseKeyframeAnimation, baseKeyframeAnimation2);
                    } else {
                        o(canvas, matrix, baseKeyframeAnimation);
                    }
                } else if (i11 != 3) {
                    if (i11 == 4) {
                        if (mask.d()) {
                            k(canvas, matrix, baseKeyframeAnimation, baseKeyframeAnimation2);
                        } else {
                            i(canvas, matrix, baseKeyframeAnimation, baseKeyframeAnimation2);
                        }
                    }
                } else if (mask.d()) {
                    l(canvas, matrix, baseKeyframeAnimation, baseKeyframeAnimation2);
                } else {
                    j(canvas, matrix, baseKeyframeAnimation, baseKeyframeAnimation2);
                }
            } else if (p()) {
                this.f16188c.setAlpha(255);
                canvas.drawRect(this.f16193h, this.f16188c);
            }
        }
        L.a("Layer#restoreLayer");
        canvas.restore();
        L.b("Layer#restoreLayer");
    }

    private void o(Canvas canvas, Matrix matrix, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation) {
        this.f16186a.set(baseKeyframeAnimation.h());
        this.f16186a.transform(matrix);
        canvas.drawPath(this.f16186a, this.f16190e);
    }

    private boolean p() {
        if (this.f16201p.a().isEmpty()) {
            return false;
        }
        for (int i10 = 0; i10 < this.f16201p.b().size(); i10++) {
            if (this.f16201p.b().get(i10).a() != Mask.a.MASK_MODE_NONE) {
                return false;
            }
        }
        return true;
    }

    private void q() {
        if (this.f16205t != null) {
            return;
        }
        if (this.f16204s == null) {
            this.f16205t = Collections.emptyList();
            return;
        }
        this.f16205t = new ArrayList();
        for (BaseLayer baseLayer = this.f16204s; baseLayer != null; baseLayer = baseLayer.f16204s) {
            this.f16205t.add(baseLayer);
        }
    }

    private void r(Canvas canvas) {
        L.a("Layer#clearLayer");
        RectF rectF = this.f16193h;
        canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f, this.f16192g);
        L.b("Layer#clearLayer");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BaseLayer t(CompositionLayer compositionLayer, e eVar, EffectiveAnimationDrawable effectiveAnimationDrawable, EffectiveAnimationComposition effectiveAnimationComposition) {
        switch (a.f16212a[eVar.f().ordinal()]) {
            case 1:
                return new ShapeLayer(effectiveAnimationDrawable, eVar, compositionLayer);
            case 2:
                return new CompositionLayer(effectiveAnimationDrawable, eVar, effectiveAnimationComposition.p(eVar.m()), effectiveAnimationComposition);
            case 3:
                return new SolidLayer(effectiveAnimationDrawable, eVar);
            case 4:
                return new ImageLayer(effectiveAnimationDrawable, eVar);
            case 5:
                return new NullLayer(effectiveAnimationDrawable, eVar);
            case 6:
                return new TextLayer(effectiveAnimationDrawable, eVar);
            default:
                s4.e.c("Unknown layer type " + eVar.f());
                return null;
        }
    }

    public void F(BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        this.f16206u.remove(baseKeyframeAnimation);
    }

    void G(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(BaseLayer baseLayer) {
        this.f16203r = baseLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(boolean z10) {
        if (z10 && this.f16210y == null) {
            this.f16210y = new LPaint();
        }
        this.f16209x = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(BaseLayer baseLayer) {
        this.f16204s = baseLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void K(float f10) {
        this.f16207v.j(f10);
        if (this.f16201p != null) {
            for (int i10 = 0; i10 < this.f16201p.a().size(); i10++) {
                this.f16201p.a().get(i10).m(f10);
            }
        }
        FloatKeyframeAnimation floatKeyframeAnimation = this.f16202q;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.m(f10);
        }
        BaseLayer baseLayer = this.f16203r;
        if (baseLayer != null) {
            baseLayer.K(f10);
        }
        for (int i11 = 0; i11 < this.f16206u.size(); i11++) {
            this.f16206u.get(i11).m(f10);
        }
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        C();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
    }

    @Override // l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        this.f16207v.c(t7, effectiveValueCallback);
    }

    @Override // l4.KeyPathElement
    public void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        BaseLayer baseLayer = this.f16203r;
        if (baseLayer != null) {
            KeyPath a10 = keyPath2.a(baseLayer.getName());
            if (keyPath.c(this.f16203r.getName(), i10)) {
                list.add(a10.i(this.f16203r));
            }
            if (keyPath.h(getName(), i10)) {
                this.f16203r.G(keyPath, keyPath.e(this.f16203r.getName(), i10) + i10, list, a10);
            }
        }
        if (keyPath.g(getName(), i10)) {
            if (!"__container".equals(getName())) {
                keyPath2 = keyPath2.a(getName());
                if (keyPath.c(getName(), i10)) {
                    list.add(keyPath2.i(this));
                }
            }
            if (keyPath.h(getName(), i10)) {
                G(keyPath, i10 + keyPath.e(getName(), i10), list, keyPath2);
            }
        }
    }

    @Override // i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        this.f16193h.set(0.0f, 0.0f, 0.0f, 0.0f);
        q();
        this.f16198m.set(matrix);
        if (z10) {
            List<BaseLayer> list = this.f16205t;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.f16198m.preConcat(this.f16205t.get(size).f16207v.f());
                }
            } else {
                BaseLayer baseLayer = this.f16204s;
                if (baseLayer != null) {
                    this.f16198m.preConcat(baseLayer.f16207v.f());
                }
            }
        }
        this.f16198m.preConcat(this.f16207v.f());
    }

    @Override // i4.DrawingContent
    public void g(Canvas canvas, Matrix matrix, int i10) {
        Paint paint;
        L.a(this.f16197l);
        if (this.f16208w && !this.f16200o.x()) {
            q();
            L.a("Layer#parentMatrix");
            this.f16187b.reset();
            this.f16187b.set(matrix);
            for (int size = this.f16205t.size() - 1; size >= 0; size--) {
                this.f16187b.preConcat(this.f16205t.get(size).f16207v.f());
            }
            L.b("Layer#parentMatrix");
            int intValue = (int) ((((i10 / 255.0f) * (this.f16207v.h() == null ? 100 : this.f16207v.h().h().intValue())) / 100.0f) * 255.0f);
            if (!z() && !y()) {
                this.f16187b.preConcat(this.f16207v.f());
                L.a("Layer#drawLayer");
                s(canvas, this.f16187b, intValue);
                L.b("Layer#drawLayer");
                E(L.b(this.f16197l));
                return;
            }
            L.a("Layer#computeBounds");
            e(this.f16193h, this.f16187b, false);
            B(this.f16193h, matrix);
            this.f16187b.preConcat(this.f16207v.f());
            A(this.f16193h, this.f16187b);
            if (!this.f16193h.intersect(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight())) {
                this.f16193h.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
            L.b("Layer#computeBounds");
            if (this.f16193h.width() >= 1.0f && this.f16193h.height() >= 1.0f) {
                L.a("Layer#saveLayer");
                this.f16188c.setAlpha(255);
                s4.h.n(canvas, this.f16193h, this.f16188c);
                L.b("Layer#saveLayer");
                r(canvas);
                L.a("Layer#drawLayer");
                s(canvas, this.f16187b, intValue);
                L.b("Layer#drawLayer");
                if (y()) {
                    n(canvas, this.f16187b);
                }
                if (z()) {
                    L.a("Layer#drawMatte");
                    L.a("Layer#saveLayer");
                    s4.h.o(canvas, this.f16193h, this.f16191f, 19);
                    L.b("Layer#saveLayer");
                    r(canvas);
                    this.f16203r.g(canvas, matrix, intValue);
                    L.a("Layer#restoreLayer");
                    canvas.restore();
                    L.b("Layer#restoreLayer");
                    L.b("Layer#drawMatte");
                }
                L.a("Layer#restoreLayer");
                canvas.restore();
                L.b("Layer#restoreLayer");
            }
            if (this.f16209x && (paint = this.f16210y) != null) {
                paint.setStyle(Paint.Style.STROKE);
                this.f16210y.setColor(-251901);
                this.f16210y.setStrokeWidth(4.0f);
                canvas.drawRect(this.f16193h, this.f16210y);
                this.f16210y.setStyle(Paint.Style.FILL);
                this.f16210y.setColor(1357638635);
                canvas.drawRect(this.f16193h, this.f16210y);
            }
            E(L.b(this.f16197l));
            return;
        }
        L.b(this.f16197l);
    }

    @Override // i4.Content
    public String getName() {
        return this.f16200o.i();
    }

    public void h(BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        if (baseKeyframeAnimation == null) {
            return;
        }
        this.f16206u.add(baseKeyframeAnimation);
    }

    abstract void s(Canvas canvas, Matrix matrix, int i10);

    public BlurEffect u() {
        return this.f16200o.a();
    }

    public BlurMaskFilter v(float f10) {
        if (this.f16211z == f10) {
            return this.A;
        }
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(f10 / 2.0f, BlurMaskFilter.Blur.NORMAL);
        this.A = blurMaskFilter;
        this.f16211z = f10;
        return blurMaskFilter;
    }

    public DropShadowEffect w() {
        return this.f16200o.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e x() {
        return this.f16200o;
    }

    boolean y() {
        MaskKeyframeAnimation maskKeyframeAnimation = this.f16201p;
        return (maskKeyframeAnimation == null || maskKeyframeAnimation.a().isEmpty()) ? false : true;
    }

    boolean z() {
        return this.f16203r != null;
    }
}
