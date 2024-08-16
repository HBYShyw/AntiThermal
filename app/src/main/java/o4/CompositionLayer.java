package o4;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationComposition;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import com.oplus.anim.L;
import j.LongSparseArray;
import j4.BaseKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import l4.KeyPath;
import m4.AnimatableFloatValue;
import o4.e;
import t4.EffectiveValueCallback;

/* compiled from: CompositionLayer.java */
/* renamed from: o4.c, reason: use source file name */
/* loaded from: classes.dex */
public class CompositionLayer extends BaseLayer {
    private BaseKeyframeAnimation<Float, Float> B;
    private final List<BaseLayer> C;
    private final RectF D;
    private final RectF E;
    private final Paint F;

    /* compiled from: CompositionLayer.java */
    /* renamed from: o4.c$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f16214a;

        static {
            int[] iArr = new int[e.b.values().length];
            f16214a = iArr;
            try {
                iArr[e.b.ADD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f16214a[e.b.INVERT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public CompositionLayer(EffectiveAnimationDrawable effectiveAnimationDrawable, e eVar, List<e> list, EffectiveAnimationComposition effectiveAnimationComposition) {
        super(effectiveAnimationDrawable, eVar);
        int i10;
        BaseLayer baseLayer;
        this.C = new ArrayList();
        this.D = new RectF();
        this.E = new RectF();
        this.F = new Paint();
        AnimatableFloatValue u7 = eVar.u();
        if (u7 != null) {
            BaseKeyframeAnimation<Float, Float> a10 = u7.a();
            this.B = a10;
            h(a10);
            this.B.a(this);
        } else {
            this.B = null;
        }
        LongSparseArray longSparseArray = new LongSparseArray(effectiveAnimationComposition.l().size());
        int size = list.size() - 1;
        BaseLayer baseLayer2 = null;
        while (true) {
            if (size < 0) {
                break;
            }
            e eVar2 = list.get(size);
            BaseLayer t7 = BaseLayer.t(this, eVar2, effectiveAnimationDrawable, effectiveAnimationComposition);
            if (t7 != null) {
                longSparseArray.j(t7.x().d(), t7);
                if (baseLayer2 != null) {
                    baseLayer2.H(t7);
                    baseLayer2 = null;
                } else {
                    this.C.add(0, t7);
                    int i11 = a.f16214a[eVar2.h().ordinal()];
                    if (i11 == 1 || i11 == 2) {
                        baseLayer2 = t7;
                    }
                }
            }
            size--;
        }
        for (i10 = 0; i10 < longSparseArray.n(); i10++) {
            BaseLayer baseLayer3 = (BaseLayer) longSparseArray.e(longSparseArray.i(i10));
            if (baseLayer3 != null && (baseLayer = (BaseLayer) longSparseArray.e(baseLayer3.x().j())) != null) {
                baseLayer3.J(baseLayer);
            }
        }
    }

    @Override // o4.BaseLayer
    protected void G(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        for (int i11 = 0; i11 < this.C.size(); i11++) {
            this.C.get(i11).d(keyPath, i10, list, keyPath2);
        }
    }

    @Override // o4.BaseLayer
    public void I(boolean z10) {
        super.I(z10);
        Iterator<BaseLayer> it = this.C.iterator();
        while (it.hasNext()) {
            it.next().I(z10);
        }
    }

    @Override // o4.BaseLayer
    public void K(float f10) {
        super.K(f10);
        if (this.B != null) {
            f10 = ((this.B.h().floatValue() * this.f16200o.b().j()) - this.f16200o.b().q()) / (this.f16199n.q().f() + 0.01f);
        }
        if (this.B == null) {
            f10 -= this.f16200o.r();
        }
        if (this.f16200o.v() != 0.0f && !"__container".equals(this.f16200o.i())) {
            f10 /= this.f16200o.v();
        }
        for (int size = this.C.size() - 1; size >= 0; size--) {
            this.C.get(size).K(f10);
        }
    }

    @Override // o4.BaseLayer, l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        super.c(t7, effectiveValueCallback);
        if (t7 == EffectiveAnimationProperty.E) {
            if (effectiveValueCallback == null) {
                BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation = this.B;
                if (baseKeyframeAnimation != null) {
                    baseKeyframeAnimation.n(null);
                    return;
                }
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.B = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.a(this);
            h(this.B);
        }
    }

    @Override // o4.BaseLayer, i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        super.e(rectF, matrix, z10);
        for (int size = this.C.size() - 1; size >= 0; size--) {
            this.D.set(0.0f, 0.0f, 0.0f, 0.0f);
            this.C.get(size).e(this.D, this.f16198m, true);
            rectF.union(this.D);
        }
    }

    @Override // o4.BaseLayer
    void s(Canvas canvas, Matrix matrix, int i10) {
        L.a("CompositionLayer#draw");
        this.E.set(0.0f, 0.0f, this.f16200o.l(), this.f16200o.k());
        matrix.mapRect(this.E);
        boolean z10 = this.f16199n.J() && this.C.size() > 1 && i10 != 255;
        if (z10) {
            this.F.setAlpha(i10);
            s4.h.n(canvas, this.E, this.F);
        } else {
            canvas.save();
        }
        if (z10) {
            i10 = 255;
        }
        for (int size = this.C.size() - 1; size >= 0; size--) {
            if (!this.E.isEmpty() ? canvas.clipRect(this.E) : true) {
                this.C.get(size).g(canvas, matrix, i10);
            }
        }
        canvas.restore();
        L.b("CompositionLayer#draw");
    }
}
