package o4;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import h4.LPaint;
import j4.BaseKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import t4.EffectiveValueCallback;

/* compiled from: ImageLayer.java */
/* renamed from: o4.d, reason: use source file name */
/* loaded from: classes.dex */
public class ImageLayer extends BaseLayer {
    private final Paint B;
    private final Rect C;
    private final Rect D;
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> E;
    private BaseKeyframeAnimation<Bitmap, Bitmap> F;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImageLayer(EffectiveAnimationDrawable effectiveAnimationDrawable, e eVar) {
        super(effectiveAnimationDrawable, eVar);
        this.B = new LPaint(3);
        this.C = new Rect();
        this.D = new Rect();
    }

    private Bitmap N() {
        Bitmap h10;
        BaseKeyframeAnimation<Bitmap, Bitmap> baseKeyframeAnimation = this.F;
        return (baseKeyframeAnimation == null || (h10 = baseKeyframeAnimation.h()) == null) ? this.f16199n.u(this.f16200o.m()) : h10;
    }

    @Override // o4.BaseLayer, l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        super.c(t7, effectiveValueCallback);
        if (t7 == EffectiveAnimationProperty.K) {
            if (effectiveValueCallback == null) {
                this.E = null;
                return;
            } else {
                this.E = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
                return;
            }
        }
        if (t7 == EffectiveAnimationProperty.N) {
            if (effectiveValueCallback == null) {
                this.F = null;
            } else {
                this.F = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            }
        }
    }

    @Override // o4.BaseLayer, i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        super.e(rectF, matrix, z10);
        if (N() != null) {
            rectF.set(0.0f, 0.0f, r3.getWidth() * s4.h.f(), r3.getHeight() * s4.h.f());
            this.f16198m.mapRect(rectF);
        }
    }

    @Override // o4.BaseLayer
    public void s(Canvas canvas, Matrix matrix, int i10) {
        Bitmap N = N();
        if (N == null || N.isRecycled()) {
            return;
        }
        float f10 = s4.h.f();
        this.B.setAlpha(i10);
        BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.E;
        if (baseKeyframeAnimation != null) {
            this.B.setColorFilter(baseKeyframeAnimation.h());
        }
        canvas.save();
        canvas.concat(matrix);
        this.C.set(0, 0, N.getWidth(), N.getHeight());
        this.D.set(0, 0, (int) (N.getWidth() * f10), (int) (N.getHeight() * f10));
        canvas.drawBitmap(N, this.C, this.D, this.B);
        canvas.restore();
    }
}
