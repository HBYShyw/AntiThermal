package com.google.android.material.timepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ClockHandView extends View {

    /* renamed from: e, reason: collision with root package name */
    private ValueAnimator f9498e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f9499f;

    /* renamed from: g, reason: collision with root package name */
    private float f9500g;

    /* renamed from: h, reason: collision with root package name */
    private float f9501h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f9502i;

    /* renamed from: j, reason: collision with root package name */
    private int f9503j;

    /* renamed from: k, reason: collision with root package name */
    private final List<d> f9504k;

    /* renamed from: l, reason: collision with root package name */
    private final int f9505l;

    /* renamed from: m, reason: collision with root package name */
    private final float f9506m;

    /* renamed from: n, reason: collision with root package name */
    private final Paint f9507n;

    /* renamed from: o, reason: collision with root package name */
    private final RectF f9508o;

    /* renamed from: p, reason: collision with root package name */
    private final int f9509p;

    /* renamed from: q, reason: collision with root package name */
    private float f9510q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f9511r;

    /* renamed from: s, reason: collision with root package name */
    private c f9512s;

    /* renamed from: t, reason: collision with root package name */
    private double f9513t;

    /* renamed from: u, reason: collision with root package name */
    private int f9514u;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ClockHandView.this.m(((Float) valueAnimator.getAnimatedValue()).floatValue(), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            animator.end();
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a(float f10, boolean z10);
    }

    /* loaded from: classes.dex */
    public interface d {
        void c(float f10, boolean z10);
    }

    public ClockHandView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.materialClockStyle);
    }

    private void c(Canvas canvas) {
        int height = getHeight() / 2;
        float width = getWidth() / 2;
        float cos = (this.f9514u * ((float) Math.cos(this.f9513t))) + width;
        float f10 = height;
        float sin = (this.f9514u * ((float) Math.sin(this.f9513t))) + f10;
        this.f9507n.setStrokeWidth(0.0f);
        canvas.drawCircle(cos, sin, this.f9505l, this.f9507n);
        double sin2 = Math.sin(this.f9513t);
        double cos2 = Math.cos(this.f9513t);
        this.f9507n.setStrokeWidth(this.f9509p);
        canvas.drawLine(width, f10, r1 + ((int) (cos2 * r6)), height + ((int) (r6 * sin2)), this.f9507n);
        canvas.drawCircle(width, f10, this.f9506m, this.f9507n);
    }

    private int e(float f10, float f11) {
        int degrees = ((int) Math.toDegrees(Math.atan2(f11 - (getHeight() / 2), f10 - (getWidth() / 2)))) + 90;
        return degrees < 0 ? degrees + 360 : degrees;
    }

    private Pair<Float, Float> h(float f10) {
        float f11 = f();
        if (Math.abs(f11 - f10) > 180.0f) {
            if (f11 > 180.0f && f10 < 180.0f) {
                f10 += 360.0f;
            }
            if (f11 < 180.0f && f10 > 180.0f) {
                f11 += 360.0f;
            }
        }
        return new Pair<>(Float.valueOf(f11), Float.valueOf(f10));
    }

    private boolean i(float f10, float f11, boolean z10, boolean z11, boolean z12) {
        float e10 = e(f10, f11);
        boolean z13 = false;
        boolean z14 = f() != e10;
        if (z11 && z14) {
            return true;
        }
        if (!z14 && !z10) {
            return false;
        }
        if (z12 && this.f9499f) {
            z13 = true;
        }
        l(e10, z13);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m(float f10, boolean z10) {
        float f11 = f10 % 360.0f;
        this.f9510q = f11;
        this.f9513t = Math.toRadians(f11 - 90.0f);
        int height = getHeight() / 2;
        float width = (getWidth() / 2) + (this.f9514u * ((float) Math.cos(this.f9513t)));
        float sin = height + (this.f9514u * ((float) Math.sin(this.f9513t)));
        RectF rectF = this.f9508o;
        int i10 = this.f9505l;
        rectF.set(width - i10, sin - i10, width + i10, sin + i10);
        Iterator<d> it = this.f9504k.iterator();
        while (it.hasNext()) {
            it.next().c(f11, z10);
        }
        invalidate();
    }

    public void b(d dVar) {
        this.f9504k.add(dVar);
    }

    public RectF d() {
        return this.f9508o;
    }

    public float f() {
        return this.f9510q;
    }

    public int g() {
        return this.f9505l;
    }

    public void j(int i10) {
        this.f9514u = i10;
        invalidate();
    }

    public void k(float f10) {
        l(f10, false);
    }

    public void l(float f10, boolean z10) {
        ValueAnimator valueAnimator = this.f9498e;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (!z10) {
            m(f10, false);
            return;
        }
        Pair<Float, Float> h10 = h(f10);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(((Float) h10.first).floatValue(), ((Float) h10.second).floatValue());
        this.f9498e = ofFloat;
        ofFloat.setDuration(200L);
        this.f9498e.addUpdateListener(new a());
        this.f9498e.addListener(new b());
        this.f9498e.start();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        c(canvas);
    }

    @Override // android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        k(f());
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z10;
        boolean z11;
        boolean z12;
        c cVar;
        int actionMasked = motionEvent.getActionMasked();
        float x10 = motionEvent.getX();
        float y4 = motionEvent.getY();
        if (actionMasked == 0) {
            this.f9500g = x10;
            this.f9501h = y4;
            this.f9502i = true;
            this.f9511r = false;
            z10 = false;
            z11 = false;
            z12 = true;
        } else if (actionMasked == 1 || actionMasked == 2) {
            int i10 = (int) (x10 - this.f9500g);
            int i11 = (int) (y4 - this.f9501h);
            this.f9502i = (i10 * i10) + (i11 * i11) > this.f9503j;
            boolean z13 = this.f9511r;
            z10 = actionMasked == 1;
            z12 = false;
            z11 = z13;
        } else {
            z10 = false;
            z11 = false;
            z12 = false;
        }
        boolean i12 = i(x10, y4, z11, z12, z10) | this.f9511r;
        this.f9511r = i12;
        if (i12 && z10 && (cVar = this.f9512s) != null) {
            cVar.a(e(x10, y4), this.f9502i);
        }
        return true;
    }

    public ClockHandView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f9504k = new ArrayList();
        Paint paint = new Paint();
        this.f9507n = paint;
        this.f9508o = new RectF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ClockHandView, i10, R$style.Widget_MaterialComponents_TimePicker_Clock);
        this.f9514u = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ClockHandView_materialCircleRadius, 0);
        this.f9505l = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ClockHandView_selectorSize, 0);
        this.f9509p = getResources().getDimensionPixelSize(R$dimen.material_clock_hand_stroke_width);
        this.f9506m = r6.getDimensionPixelSize(R$dimen.material_clock_hand_center_dot_radius);
        int color = obtainStyledAttributes.getColor(R$styleable.ClockHandView_clockHandColor, 0);
        paint.setAntiAlias(true);
        paint.setColor(color);
        k(0.0f);
        this.f9503j = ViewConfiguration.get(context).getScaledTouchSlop();
        ViewCompat.w0(this, 2);
        obtainStyledAttributes.recycle();
    }
}
