package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import r3.MaterialColors;

/* compiled from: LinearDrawingDelegate.java */
/* renamed from: com.google.android.material.progressindicator.i, reason: use source file name */
/* loaded from: classes.dex */
final class LinearDrawingDelegate extends DrawingDelegate<LinearProgressIndicatorSpec> {

    /* renamed from: c, reason: collision with root package name */
    private float f9116c;

    /* renamed from: d, reason: collision with root package name */
    private float f9117d;

    /* renamed from: e, reason: collision with root package name */
    private float f9118e;

    public LinearDrawingDelegate(LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(linearProgressIndicatorSpec);
        this.f9116c = 300.0f;
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void a(Canvas canvas, float f10) {
        Rect clipBounds = canvas.getClipBounds();
        this.f9116c = clipBounds.width();
        float f11 = ((LinearProgressIndicatorSpec) this.f9109a).f9061a;
        canvas.translate(clipBounds.left + (clipBounds.width() / 2.0f), clipBounds.top + (clipBounds.height() / 2.0f) + Math.max(0.0f, (clipBounds.height() - ((LinearProgressIndicatorSpec) this.f9109a).f9061a) / 2.0f));
        if (((LinearProgressIndicatorSpec) this.f9109a).f9060i) {
            canvas.scale(-1.0f, 1.0f);
        }
        if ((this.f9110b.j() && ((LinearProgressIndicatorSpec) this.f9109a).f9065e == 1) || (this.f9110b.i() && ((LinearProgressIndicatorSpec) this.f9109a).f9066f == 2)) {
            canvas.scale(1.0f, -1.0f);
        }
        if (this.f9110b.j() || this.f9110b.i()) {
            canvas.translate(0.0f, (((LinearProgressIndicatorSpec) this.f9109a).f9061a * (f10 - 1.0f)) / 2.0f);
        }
        float f12 = this.f9116c;
        canvas.clipRect((-f12) / 2.0f, (-f11) / 2.0f, f12 / 2.0f, f11 / 2.0f);
        S s7 = this.f9109a;
        this.f9117d = ((LinearProgressIndicatorSpec) s7).f9061a * f10;
        this.f9118e = ((LinearProgressIndicatorSpec) s7).f9062b * f10;
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void b(Canvas canvas, Paint paint, float f10, float f11, int i10) {
        if (f10 == f11) {
            return;
        }
        float f12 = this.f9116c;
        float f13 = this.f9118e;
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(i10);
        float f14 = this.f9117d;
        RectF rectF = new RectF(((-f12) / 2.0f) + (f10 * (f12 - (f13 * 2.0f))), (-f14) / 2.0f, ((-f12) / 2.0f) + (f11 * (f12 - (f13 * 2.0f))) + (f13 * 2.0f), f14 / 2.0f);
        float f15 = this.f9118e;
        canvas.drawRoundRect(rectF, f15, f15, paint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void c(Canvas canvas, Paint paint) {
        int a10 = MaterialColors.a(((LinearProgressIndicatorSpec) this.f9109a).f9064d, this.f9110b.getAlpha());
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(a10);
        float f10 = this.f9116c;
        float f11 = this.f9117d;
        RectF rectF = new RectF((-f10) / 2.0f, (-f11) / 2.0f, f10 / 2.0f, f11 / 2.0f);
        float f12 = this.f9118e;
        canvas.drawRoundRect(rectF, f12, f12, paint);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int d() {
        return ((LinearProgressIndicatorSpec) this.f9109a).f9061a;
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int e() {
        return -1;
    }
}
