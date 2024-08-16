package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import r3.MaterialColors;

/* compiled from: CircularDrawingDelegate.java */
/* renamed from: com.google.android.material.progressindicator.b, reason: use source file name */
/* loaded from: classes.dex */
final class CircularDrawingDelegate extends DrawingDelegate<CircularProgressIndicatorSpec> {

    /* renamed from: c, reason: collision with root package name */
    private int f9067c;

    /* renamed from: d, reason: collision with root package name */
    private float f9068d;

    /* renamed from: e, reason: collision with root package name */
    private float f9069e;

    /* renamed from: f, reason: collision with root package name */
    private float f9070f;

    public CircularDrawingDelegate(CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        super(circularProgressIndicatorSpec);
        this.f9067c = 1;
    }

    private void h(Canvas canvas, Paint paint, float f10, float f11, float f12) {
        canvas.save();
        canvas.rotate(f12);
        float f13 = this.f9070f;
        float f14 = f10 / 2.0f;
        canvas.drawRoundRect(new RectF(f13 - f14, f11, f13 + f14, -f11), f11, f11, paint);
        canvas.restore();
    }

    private int i() {
        S s7 = this.f9109a;
        return ((CircularProgressIndicatorSpec) s7).f9054g + (((CircularProgressIndicatorSpec) s7).f9055h * 2);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void a(Canvas canvas, float f10) {
        S s7 = this.f9109a;
        float f11 = (((CircularProgressIndicatorSpec) s7).f9054g / 2.0f) + ((CircularProgressIndicatorSpec) s7).f9055h;
        canvas.translate(f11, f11);
        canvas.rotate(-90.0f);
        float f12 = -f11;
        canvas.clipRect(f12, f12, f11, f11);
        this.f9067c = ((CircularProgressIndicatorSpec) this.f9109a).f9056i == 0 ? 1 : -1;
        this.f9068d = ((CircularProgressIndicatorSpec) r5).f9061a * f10;
        this.f9069e = ((CircularProgressIndicatorSpec) r5).f9062b * f10;
        this.f9070f = (((CircularProgressIndicatorSpec) r5).f9054g - ((CircularProgressIndicatorSpec) r5).f9061a) / 2.0f;
        if ((this.f9110b.j() && ((CircularProgressIndicatorSpec) this.f9109a).f9065e == 2) || (this.f9110b.i() && ((CircularProgressIndicatorSpec) this.f9109a).f9066f == 1)) {
            this.f9070f += ((1.0f - f10) * ((CircularProgressIndicatorSpec) this.f9109a).f9061a) / 2.0f;
        } else if ((this.f9110b.j() && ((CircularProgressIndicatorSpec) this.f9109a).f9065e == 1) || (this.f9110b.i() && ((CircularProgressIndicatorSpec) this.f9109a).f9066f == 2)) {
            this.f9070f -= ((1.0f - f10) * ((CircularProgressIndicatorSpec) this.f9109a).f9061a) / 2.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void b(Canvas canvas, Paint paint, float f10, float f11, int i10) {
        if (f10 == f11) {
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        paint.setColor(i10);
        paint.setStrokeWidth(this.f9068d);
        int i11 = this.f9067c;
        float f12 = f10 * 360.0f * i11;
        float f13 = (f11 >= f10 ? f11 - f10 : (1.0f + f11) - f10) * 360.0f * i11;
        float f14 = this.f9070f;
        canvas.drawArc(new RectF(-f14, -f14, f14, f14), f12, f13, false, paint);
        if (this.f9069e <= 0.0f || Math.abs(f13) >= 360.0f) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        h(canvas, paint, this.f9068d, this.f9069e, f12);
        h(canvas, paint, this.f9068d, this.f9069e, f12 + f13);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void c(Canvas canvas, Paint paint) {
        int a10 = MaterialColors.a(((CircularProgressIndicatorSpec) this.f9109a).f9064d, this.f9110b.getAlpha());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        paint.setColor(a10);
        paint.setStrokeWidth(this.f9068d);
        float f10 = this.f9070f;
        canvas.drawArc(new RectF(-f10, -f10, f10, f10), 0.0f, 360.0f, false, paint);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int d() {
        return i();
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int e() {
        return i();
    }
}
