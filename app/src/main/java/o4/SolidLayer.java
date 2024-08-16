package o4;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import h4.LPaint;
import j4.BaseKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import t4.EffectiveValueCallback;

/* compiled from: SolidLayer.java */
/* renamed from: o4.h, reason: use source file name */
/* loaded from: classes.dex */
public class SolidLayer extends BaseLayer {
    private final RectF B;
    private final Paint C;
    private final float[] D;
    private final Path E;
    private final e F;
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> G;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SolidLayer(EffectiveAnimationDrawable effectiveAnimationDrawable, e eVar) {
        super(effectiveAnimationDrawable, eVar);
        this.B = new RectF();
        LPaint lPaint = new LPaint();
        this.C = lPaint;
        this.D = new float[8];
        this.E = new Path();
        this.F = eVar;
        lPaint.setAlpha(0);
        lPaint.setStyle(Paint.Style.FILL);
        lPaint.setColor(eVar.o());
    }

    @Override // o4.BaseLayer, l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        super.c(t7, effectiveValueCallback);
        if (t7 == EffectiveAnimationProperty.K) {
            if (effectiveValueCallback == null) {
                this.G = null;
            } else {
                this.G = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            }
        }
    }

    @Override // o4.BaseLayer, i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        super.e(rectF, matrix, z10);
        this.B.set(0.0f, 0.0f, this.F.q(), this.F.p());
        this.f16198m.mapRect(this.B);
        rectF.set(this.B);
    }

    @Override // o4.BaseLayer
    public void s(Canvas canvas, Matrix matrix, int i10) {
        int alpha = Color.alpha(this.F.o());
        if (alpha == 0) {
            return;
        }
        int intValue = (int) ((i10 / 255.0f) * (((alpha / 255.0f) * (this.f16207v.h() == null ? 100 : this.f16207v.h().h().intValue())) / 100.0f) * 255.0f);
        this.C.setAlpha(intValue);
        BaseKeyframeAnimation<ColorFilter, ColorFilter> baseKeyframeAnimation = this.G;
        if (baseKeyframeAnimation != null) {
            this.C.setColorFilter(baseKeyframeAnimation.h());
        }
        if (intValue > 0) {
            float[] fArr = this.D;
            fArr[0] = 0.0f;
            fArr[1] = 0.0f;
            fArr[2] = this.F.q();
            float[] fArr2 = this.D;
            fArr2[3] = 0.0f;
            fArr2[4] = this.F.q();
            this.D[5] = this.F.p();
            float[] fArr3 = this.D;
            fArr3[6] = 0.0f;
            fArr3[7] = this.F.p();
            matrix.mapPoints(this.D);
            this.E.reset();
            Path path = this.E;
            float[] fArr4 = this.D;
            path.moveTo(fArr4[0], fArr4[1]);
            Path path2 = this.E;
            float[] fArr5 = this.D;
            path2.lineTo(fArr5[2], fArr5[3]);
            Path path3 = this.E;
            float[] fArr6 = this.D;
            path3.lineTo(fArr6[4], fArr6[5]);
            Path path4 = this.E;
            float[] fArr7 = this.D;
            path4.lineTo(fArr7[6], fArr7[7]);
            Path path5 = this.E;
            float[] fArr8 = this.D;
            path5.lineTo(fArr8[0], fArr8[1]);
            this.E.close();
            canvas.drawPath(this.E, this.C);
        }
    }
}
