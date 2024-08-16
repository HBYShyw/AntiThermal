package com.google.android.material.textfield;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;

/* compiled from: CutoutDrawable.java */
/* renamed from: com.google.android.material.textfield.c, reason: use source file name */
/* loaded from: classes.dex */
class CutoutDrawable extends MaterialShapeDrawable {
    private final Paint D;
    private final RectF E;

    CutoutDrawable() {
        this(null);
    }

    private void v0() {
        this.D.setStyle(Paint.Style.FILL_AND_STROKE);
        this.D.setColor(-1);
        this.D.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // c4.MaterialShapeDrawable
    public void q(Canvas canvas) {
        if (this.E.isEmpty()) {
            super.q(canvas);
            return;
        }
        canvas.save();
        canvas.clipOutRect(this.E);
        super.q(canvas);
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean r0() {
        return !this.E.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s0() {
        t0(0.0f, 0.0f, 0.0f, 0.0f);
    }

    void t0(float f10, float f11, float f12, float f13) {
        RectF rectF = this.E;
        if (f10 == rectF.left && f11 == rectF.top && f12 == rectF.right && f13 == rectF.bottom) {
            return;
        }
        rectF.set(f10, f11, f12, f13);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u0(RectF rectF) {
        t0(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CutoutDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        super(shapeAppearanceModel == null ? new ShapeAppearanceModel() : shapeAppearanceModel);
        this.D = new Paint(1);
        v0();
        this.E = new RectF();
    }
}
