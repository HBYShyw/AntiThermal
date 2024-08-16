package o4;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.ContentGroup;
import java.util.Collections;
import java.util.List;
import l4.KeyPath;
import n4.BlurEffect;
import n4.ShapeGroup;
import q4.DropShadowEffect;

/* compiled from: ShapeLayer.java */
/* renamed from: o4.g, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeLayer extends BaseLayer {
    private final ContentGroup B;
    private final CompositionLayer C;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShapeLayer(EffectiveAnimationDrawable effectiveAnimationDrawable, e eVar, CompositionLayer compositionLayer) {
        super(effectiveAnimationDrawable, eVar);
        this.C = compositionLayer;
        ContentGroup contentGroup = new ContentGroup(effectiveAnimationDrawable, this, new ShapeGroup("__container", eVar.n(), false));
        this.B = contentGroup;
        contentGroup.b(Collections.emptyList(), Collections.emptyList());
    }

    @Override // o4.BaseLayer
    protected void G(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2) {
        this.B.d(keyPath, i10, list, keyPath2);
    }

    @Override // o4.BaseLayer, i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        super.e(rectF, matrix, z10);
        this.B.e(rectF, this.f16198m, z10);
    }

    @Override // o4.BaseLayer
    void s(Canvas canvas, Matrix matrix, int i10) {
        this.B.g(canvas, matrix, i10);
    }

    @Override // o4.BaseLayer
    public BlurEffect u() {
        BlurEffect u7 = super.u();
        return u7 != null ? u7 : this.C.u();
    }

    @Override // o4.BaseLayer
    public DropShadowEffect w() {
        DropShadowEffect w10 = super.w();
        return w10 != null ? w10 : this.C.w();
    }
}
