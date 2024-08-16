package com.google.android.material.circularreveal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.circularreveal.CircularRevealWidget;

/* compiled from: CircularRevealHelper.java */
/* renamed from: com.google.android.material.circularreveal.b, reason: use source file name */
/* loaded from: classes.dex */
public class CircularRevealHelper {

    /* renamed from: j, reason: collision with root package name */
    public static final int f8621j = 2;

    /* renamed from: a, reason: collision with root package name */
    private final a f8622a;

    /* renamed from: b, reason: collision with root package name */
    private final View f8623b;

    /* renamed from: c, reason: collision with root package name */
    private final Path f8624c;

    /* renamed from: d, reason: collision with root package name */
    private final Paint f8625d;

    /* renamed from: e, reason: collision with root package name */
    private final Paint f8626e;

    /* renamed from: f, reason: collision with root package name */
    private CircularRevealWidget.e f8627f;

    /* renamed from: g, reason: collision with root package name */
    private Drawable f8628g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f8629h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f8630i;

    /* compiled from: CircularRevealHelper.java */
    /* renamed from: com.google.android.material.circularreveal.b$a */
    /* loaded from: classes.dex */
    public interface a {
        void c(Canvas canvas);

        boolean d();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CircularRevealHelper(a aVar) {
        this.f8622a = aVar;
        View view = (View) aVar;
        this.f8623b = view;
        view.setWillNotDraw(false);
        this.f8624c = new Path();
        this.f8625d = new Paint(7);
        Paint paint = new Paint(1);
        this.f8626e = paint;
        paint.setColor(0);
    }

    private void d(Canvas canvas) {
        if (o()) {
            Rect bounds = this.f8628g.getBounds();
            float width = this.f8627f.f8635a - (bounds.width() / 2.0f);
            float height = this.f8627f.f8636b - (bounds.height() / 2.0f);
            canvas.translate(width, height);
            this.f8628g.draw(canvas);
            canvas.translate(-width, -height);
        }
    }

    private float g(CircularRevealWidget.e eVar) {
        return w3.a.b(eVar.f8635a, eVar.f8636b, 0.0f, 0.0f, this.f8623b.getWidth(), this.f8623b.getHeight());
    }

    private void i() {
        if (f8621j == 1) {
            this.f8624c.rewind();
            CircularRevealWidget.e eVar = this.f8627f;
            if (eVar != null) {
                this.f8624c.addCircle(eVar.f8635a, eVar.f8636b, eVar.f8637c, Path.Direction.CW);
            }
        }
        this.f8623b.invalidate();
    }

    private boolean n() {
        CircularRevealWidget.e eVar = this.f8627f;
        boolean z10 = eVar == null || eVar.a();
        return f8621j == 0 ? !z10 && this.f8630i : !z10;
    }

    private boolean o() {
        return (this.f8629h || this.f8628g == null || this.f8627f == null) ? false : true;
    }

    private boolean p() {
        return (this.f8629h || Color.alpha(this.f8626e.getColor()) == 0) ? false : true;
    }

    public void a() {
        if (f8621j == 0) {
            this.f8629h = true;
            this.f8630i = false;
            this.f8623b.buildDrawingCache();
            Bitmap drawingCache = this.f8623b.getDrawingCache();
            if (drawingCache == null && this.f8623b.getWidth() != 0 && this.f8623b.getHeight() != 0) {
                drawingCache = Bitmap.createBitmap(this.f8623b.getWidth(), this.f8623b.getHeight(), Bitmap.Config.ARGB_8888);
                this.f8623b.draw(new Canvas(drawingCache));
            }
            if (drawingCache != null) {
                Paint paint = this.f8625d;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                paint.setShader(new BitmapShader(drawingCache, tileMode, tileMode));
            }
            this.f8629h = false;
            this.f8630i = true;
        }
    }

    public void b() {
        if (f8621j == 0) {
            this.f8630i = false;
            this.f8623b.destroyDrawingCache();
            this.f8625d.setShader(null);
            this.f8623b.invalidate();
        }
    }

    public void c(Canvas canvas) {
        if (n()) {
            int i10 = f8621j;
            if (i10 == 0) {
                CircularRevealWidget.e eVar = this.f8627f;
                canvas.drawCircle(eVar.f8635a, eVar.f8636b, eVar.f8637c, this.f8625d);
                if (p()) {
                    CircularRevealWidget.e eVar2 = this.f8627f;
                    canvas.drawCircle(eVar2.f8635a, eVar2.f8636b, eVar2.f8637c, this.f8626e);
                }
            } else if (i10 == 1) {
                int save = canvas.save();
                canvas.clipPath(this.f8624c);
                this.f8622a.c(canvas);
                if (p()) {
                    canvas.drawRect(0.0f, 0.0f, this.f8623b.getWidth(), this.f8623b.getHeight(), this.f8626e);
                }
                canvas.restoreToCount(save);
            } else if (i10 == 2) {
                this.f8622a.c(canvas);
                if (p()) {
                    canvas.drawRect(0.0f, 0.0f, this.f8623b.getWidth(), this.f8623b.getHeight(), this.f8626e);
                }
            } else {
                throw new IllegalStateException("Unsupported strategy " + i10);
            }
        } else {
            this.f8622a.c(canvas);
            if (p()) {
                canvas.drawRect(0.0f, 0.0f, this.f8623b.getWidth(), this.f8623b.getHeight(), this.f8626e);
            }
        }
        d(canvas);
    }

    public Drawable e() {
        return this.f8628g;
    }

    public int f() {
        return this.f8626e.getColor();
    }

    public CircularRevealWidget.e h() {
        CircularRevealWidget.e eVar = this.f8627f;
        if (eVar == null) {
            return null;
        }
        CircularRevealWidget.e eVar2 = new CircularRevealWidget.e(eVar);
        if (eVar2.a()) {
            eVar2.f8637c = g(eVar2);
        }
        return eVar2;
    }

    public boolean j() {
        return this.f8622a.d() && !n();
    }

    public void k(Drawable drawable) {
        this.f8628g = drawable;
        this.f8623b.invalidate();
    }

    public void l(int i10) {
        this.f8626e.setColor(i10);
        this.f8623b.invalidate();
    }

    public void m(CircularRevealWidget.e eVar) {
        if (eVar == null) {
            this.f8627f = null;
        } else {
            CircularRevealWidget.e eVar2 = this.f8627f;
            if (eVar2 == null) {
                this.f8627f = new CircularRevealWidget.e(eVar);
            } else {
                eVar2.c(eVar);
            }
            if (w3.a.c(eVar.f8637c, g(eVar), 1.0E-4f)) {
                this.f8627f.f8637c = Float.MAX_VALUE;
            }
        }
        i();
    }
}
