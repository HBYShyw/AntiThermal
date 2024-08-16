package com.coui.appcompat.preference;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import c4.MaterialShapeDrawable;
import m2.COUIRoundRectUtil;

/* compiled from: COUIRecommendedDrawable.java */
/* renamed from: com.coui.appcompat.preference.j, reason: use source file name */
/* loaded from: classes.dex */
public class COUIRecommendedDrawable extends MaterialShapeDrawable {
    private float D;
    private int E;
    private Paint F = new Paint(1);
    private Path G = new Path();

    public COUIRecommendedDrawable(float f10, int i10) {
        this.D = f10;
        this.E = i10;
        this.F.setColor(this.E);
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.G.reset();
        Path b10 = COUIRoundRectUtil.a().b(getBounds(), this.D);
        this.G = b10;
        canvas.drawPath(b10, this.F);
    }
}
