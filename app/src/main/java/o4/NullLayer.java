package o4;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;

/* compiled from: NullLayer.java */
/* renamed from: o4.f, reason: use source file name */
/* loaded from: classes.dex */
public class NullLayer extends BaseLayer {
    /* JADX INFO: Access modifiers changed from: package-private */
    public NullLayer(EffectiveAnimationDrawable effectiveAnimationDrawable, e eVar) {
        super(effectiveAnimationDrawable, eVar);
    }

    @Override // o4.BaseLayer, i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        super.e(rectF, matrix, z10);
        rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override // o4.BaseLayer
    void s(Canvas canvas, Matrix matrix, int i10) {
    }
}
