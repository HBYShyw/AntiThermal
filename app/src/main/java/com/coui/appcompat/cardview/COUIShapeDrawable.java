package com.coui.appcompat.cardView;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;

/* compiled from: COUIShapeDrawable.java */
/* renamed from: com.coui.appcompat.cardView.c, reason: use source file name */
/* loaded from: classes.dex */
class COUIShapeDrawable extends MaterialShapeDrawable {
    int D;

    public COUIShapeDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        super(shapeAppearanceModel);
        this.D = 0;
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    @SuppressLint({"RestrictedApi"})
    public void draw(Canvas canvas) {
        j0(this.D);
        super.draw(canvas);
    }

    public void r0(int i10) {
        this.D = i10;
    }
}
