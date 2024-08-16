package com.coui.appcompat.panel;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.core.content.res.ResourcesCompat;
import com.support.panel.R$color;
import com.support.panel.R$dimen;
import m1.COUIEaseInterpolator;

/* loaded from: classes.dex */
public class COUIPanelBarView extends View {

    /* renamed from: e, reason: collision with root package name */
    private boolean f6596e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f6597f;

    /* renamed from: g, reason: collision with root package name */
    private float f6598g;

    /* renamed from: h, reason: collision with root package name */
    private float f6599h;

    /* renamed from: i, reason: collision with root package name */
    private float f6600i;

    /* renamed from: j, reason: collision with root package name */
    private float f6601j;

    /* renamed from: k, reason: collision with root package name */
    private float f6602k;

    /* renamed from: l, reason: collision with root package name */
    private float f6603l;

    /* renamed from: m, reason: collision with root package name */
    private float f6604m;

    /* renamed from: n, reason: collision with root package name */
    private float f6605n;

    /* renamed from: o, reason: collision with root package name */
    private int f6606o;

    /* renamed from: p, reason: collision with root package name */
    private int f6607p;

    /* renamed from: q, reason: collision with root package name */
    private int f6608q;

    /* renamed from: r, reason: collision with root package name */
    private int f6609r;

    /* renamed from: s, reason: collision with root package name */
    private int f6610s;

    /* renamed from: t, reason: collision with root package name */
    private int f6611t;

    /* renamed from: u, reason: collision with root package name */
    private int f6612u;

    /* renamed from: v, reason: collision with root package name */
    private int f6613v;

    /* renamed from: w, reason: collision with root package name */
    private Paint f6614w;

    /* renamed from: x, reason: collision with root package name */
    private Path f6615x;

    /* renamed from: y, reason: collision with root package name */
    private ObjectAnimator f6616y;

    public COUIPanelBarView(Context context) {
        super(context);
        this.f6596e = false;
        this.f6597f = false;
        this.f6598g = 0.0f;
        this.f6599h = 0.0f;
        this.f6600i = 0.0f;
        this.f6601j = 0.0f;
        this.f6602k = 0.0f;
        this.f6603l = 0.0f;
        this.f6604m = 0.0f;
        this.f6605n = 0.0f;
        this.f6610s = 0;
        this.f6611t = 0;
        this.f6612u = 0;
        this.f6613v = -1;
        b(context);
    }

    private void a(Canvas canvas) {
        g();
        this.f6615x.reset();
        this.f6615x.moveTo(this.f6599h, this.f6600i);
        this.f6615x.lineTo(this.f6601j, this.f6602k);
        this.f6615x.lineTo(this.f6603l, this.f6604m);
        canvas.drawPath(this.f6615x, this.f6614w);
    }

    private void b(Context context) {
        this.f6606o = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_panel_bar_width);
        this.f6607p = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_panel_bar_height);
        this.f6608q = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_panel_bar_margin_top);
        this.f6605n = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_panel_drag_bar_max_offset);
        this.f6612u = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_panel_normal_padding_top_tiny_screen);
        this.f6609r = ResourcesCompat.d(context.getResources(), R$color.coui_panel_bar_view_color, null);
        this.f6614w = new Paint();
        this.f6615x = new Path();
        Paint paint = new Paint(1);
        this.f6614w = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.f6614w.setStrokeCap(Paint.Cap.ROUND);
        this.f6614w.setDither(true);
        this.f6614w.setStrokeWidth(this.f6607p);
        this.f6614w.setColor(this.f6609r);
    }

    private void c() {
        if (this.f6596e) {
            return;
        }
        ObjectAnimator objectAnimator = this.f6616y;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.f6616y.cancel();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "barOffset", this.f6598g, 0.0f);
        this.f6616y = ofFloat;
        ofFloat.setDuration((Math.abs(this.f6598g) / (this.f6605n * 2.0f)) * 167.0f);
        this.f6616y.setInterpolator(new COUIEaseInterpolator());
        this.f6616y.start();
        this.f6613v = 0;
    }

    private void d() {
        if (this.f6596e) {
            return;
        }
        ObjectAnimator objectAnimator = this.f6616y;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.f6616y.cancel();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "barOffset", this.f6598g, this.f6605n);
        this.f6616y = ofFloat;
        ofFloat.setDuration((Math.abs(this.f6605n - this.f6598g) / (this.f6605n * 2.0f)) * 167.0f);
        this.f6616y.setInterpolator(new COUIEaseInterpolator());
        this.f6616y.start();
        this.f6613v = 1;
    }

    private void e() {
        if (this.f6596e) {
            return;
        }
        ObjectAnimator objectAnimator = this.f6616y;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.f6616y.cancel();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "barOffset", this.f6598g, -this.f6605n);
        this.f6616y = ofFloat;
        ofFloat.setDuration((Math.abs(this.f6605n + this.f6598g) / (this.f6605n * 2.0f)) * 167.0f);
        this.f6616y.setInterpolator(new LinearInterpolator());
        this.f6616y.start();
        this.f6613v = -1;
    }

    private void g() {
        float f10 = this.f6598g / 2.0f;
        int i10 = this.f6607p;
        this.f6599h = i10 / 2.0f;
        float f11 = (i10 / 2.0f) - f10;
        this.f6600i = f11;
        int i11 = this.f6606o;
        this.f6601j = (i11 / 2.0f) + (i10 / 2.0f);
        this.f6602k = (i10 / 2.0f) + f10;
        this.f6603l = i11 + (i10 / 2.0f);
        this.f6604m = f11;
    }

    private void h() {
        if (this.f6597f) {
            int i10 = this.f6610s;
            if (i10 > 0 && this.f6598g <= 0.0f && this.f6613v != 1) {
                d();
            } else {
                if (i10 >= 0 || this.f6598g < 0.0f || this.f6613v == -1 || this.f6611t < this.f6612u) {
                    return;
                }
                e();
            }
        }
    }

    private void setBarOffset(float f10) {
        this.f6598g = f10;
        invalidate();
    }

    public void f() {
        c();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0.0f, this.f6608q);
        a(canvas);
    }

    public void setBarColor(int i10) {
        this.f6609r = i10;
        this.f6614w.setColor(i10);
        invalidate();
    }

    public void setIsBeingDragged(boolean z10) {
        if (this.f6597f != z10) {
            this.f6597f = z10;
            if (z10) {
                return;
            }
            f();
        }
    }

    public void setIsFixed(boolean z10) {
        this.f6596e = z10;
    }

    public void setPanelOffset(int i10) {
        if (this.f6596e) {
            return;
        }
        int i11 = this.f6610s;
        if (i11 * i10 > 0) {
            this.f6610s = i11 + i10;
        } else {
            this.f6610s = i10;
        }
        this.f6611t += i10;
        if (Math.abs(this.f6610s) > 5 || (this.f6610s > 0 && this.f6611t < this.f6612u)) {
            h();
        }
    }

    public COUIPanelBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6596e = false;
        this.f6597f = false;
        this.f6598g = 0.0f;
        this.f6599h = 0.0f;
        this.f6600i = 0.0f;
        this.f6601j = 0.0f;
        this.f6602k = 0.0f;
        this.f6603l = 0.0f;
        this.f6604m = 0.0f;
        this.f6605n = 0.0f;
        this.f6610s = 0;
        this.f6611t = 0;
        this.f6612u = 0;
        this.f6613v = -1;
        b(context);
    }

    public COUIPanelBarView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6596e = false;
        this.f6597f = false;
        this.f6598g = 0.0f;
        this.f6599h = 0.0f;
        this.f6600i = 0.0f;
        this.f6601j = 0.0f;
        this.f6602k = 0.0f;
        this.f6603l = 0.0f;
        this.f6604m = 0.0f;
        this.f6605n = 0.0f;
        this.f6610s = 0;
        this.f6611t = 0;
        this.f6612u = 0;
        this.f6613v = -1;
        b(context);
    }
}
