package com.coui.appcompat.reddot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$plurals;
import com.support.appcompat.R$string;
import com.support.appcompat.R$styleable;
import k2.COUIHintRedDotHelper;
import k2.COUIHintRedDotMemento;
import m1.COUIMoveEaseInterpolator;

/* loaded from: classes.dex */
public class COUIHintRedDot extends View {

    /* renamed from: u, reason: collision with root package name */
    public static final Interpolator f7276u = new COUIMoveEaseInterpolator();

    /* renamed from: e, reason: collision with root package name */
    private boolean f7277e;

    /* renamed from: f, reason: collision with root package name */
    private int f7278f;

    /* renamed from: g, reason: collision with root package name */
    private int f7279g;

    /* renamed from: h, reason: collision with root package name */
    private String f7280h;

    /* renamed from: i, reason: collision with root package name */
    private int f7281i;

    /* renamed from: j, reason: collision with root package name */
    private COUIHintRedDotHelper f7282j;

    /* renamed from: k, reason: collision with root package name */
    private RectF f7283k;

    /* renamed from: l, reason: collision with root package name */
    private String f7284l;

    /* renamed from: m, reason: collision with root package name */
    private int f7285m;

    /* renamed from: n, reason: collision with root package name */
    private int f7286n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f7287o;

    /* renamed from: p, reason: collision with root package name */
    private ValueAnimator f7288p;

    /* renamed from: q, reason: collision with root package name */
    private int f7289q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f7290r;

    /* renamed from: s, reason: collision with root package name */
    private ValueAnimator f7291s;

    /* renamed from: t, reason: collision with root package name */
    private Drawable f7292t;

    public COUIHintRedDot(Context context) {
        this(context, null);
    }

    private void a() {
        ValueAnimator valueAnimator = this.f7288p;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f7288p.end();
        }
        ValueAnimator valueAnimator2 = this.f7291s;
        if (valueAnimator2 == null || !valueAnimator2.isRunning()) {
            return;
        }
        this.f7291s.end();
    }

    public COUIHintRedDotMemento b() {
        COUIHintRedDotMemento cOUIHintRedDotMemento = new COUIHintRedDotMemento();
        cOUIHintRedDotMemento.b(getPointMode());
        cOUIHintRedDotMemento.c(getPointNumber());
        cOUIHintRedDotMemento.d(getPointText());
        return cOUIHintRedDotMemento;
    }

    public void c() {
        this.f7277e = true;
    }

    public boolean getIsLaidOut() {
        return this.f7277e;
    }

    public int getPointMode() {
        return this.f7278f;
    }

    public int getPointNumber() {
        return this.f7279g;
    }

    public String getPointText() {
        return this.f7280h;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        a();
        super.onDetachedFromWindow();
        this.f7277e = false;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i10;
        RectF rectF = this.f7283k;
        rectF.left = 0.0f;
        rectF.top = 0.0f;
        rectF.right = getWidth();
        this.f7283k.bottom = getHeight();
        if (this.f7287o && ((i10 = this.f7279g) < 1000 || this.f7281i < 1000)) {
            COUIHintRedDotHelper cOUIHintRedDotHelper = this.f7282j;
            int i11 = this.f7286n;
            cOUIHintRedDotHelper.d(canvas, i10, i11, this.f7281i, 255 - i11, this.f7283k);
            return;
        }
        this.f7282j.f(canvas, this.f7278f, this.f7280h, this.f7283k);
    }

    @Override // android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        this.f7277e = true;
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int o10;
        if (this.f7290r) {
            o10 = this.f7289q;
        } else {
            o10 = this.f7282j.o(this.f7278f, this.f7280h);
        }
        setMeasuredDimension(o10, this.f7282j.m(this.f7278f));
    }

    public void setBgColor(int i10) {
        this.f7282j.q(i10);
    }

    public void setCornerRadius(int i10) {
        this.f7282j.r(i10);
    }

    public void setDotDiameter(int i10) {
        this.f7282j.s(i10);
    }

    public void setEllipsisDiameter(int i10) {
        this.f7282j.t(i10);
    }

    public void setLargeWidth(int i10) {
        this.f7282j.u(i10);
    }

    public void setMediumWidth(int i10) {
        this.f7282j.v(i10);
    }

    public void setPointMode(int i10) {
        if (this.f7278f != i10) {
            this.f7278f = i10;
            if (i10 == 4) {
                setBackground(this.f7292t);
            }
            requestLayout();
            int i11 = this.f7278f;
            if (i11 == 1 || i11 == 4) {
                setContentDescription(this.f7284l);
            } else if (i11 == 0) {
                setContentDescription("");
            }
        }
    }

    public void setPointNumber(int i10) {
        this.f7279g = i10;
        if (i10 != 0) {
            setPointText(String.valueOf(i10));
        } else {
            setPointText("");
        }
        if (i10 > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(",");
            Resources resources = getResources();
            int i11 = this.f7285m;
            int i12 = this.f7279g;
            sb2.append(resources.getQuantityString(i11, i12, Integer.valueOf(i12)));
            setContentDescription(sb2.toString());
        }
    }

    public void setPointText(String str) {
        this.f7280h = str;
        requestLayout();
    }

    public void setSmallWidth(int i10) {
        this.f7282j.w(i10);
    }

    public void setTextColor(int i10) {
        this.f7282j.x(i10);
    }

    public void setTextSize(int i10) {
        this.f7282j.y(i10);
    }

    public void setViewHeight(int i10) {
        this.f7282j.z(i10);
    }

    public COUIHintRedDot(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiHintRedDotStyle);
    }

    public COUIHintRedDot(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7278f = 0;
        this.f7279g = 0;
        this.f7280h = "";
        this.f7281i = 0;
        this.f7286n = 255;
        int[] iArr = R$styleable.COUIHintRedDot;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i10, 0);
        this.f7278f = obtainStyledAttributes.getInteger(R$styleable.COUIHintRedDot_couiHintRedPointMode, 0);
        setPointNumber(obtainStyledAttributes.getInteger(R$styleable.COUIHintRedDot_couiHintRedPointNum, 0));
        this.f7280h = obtainStyledAttributes.getString(R$styleable.COUIHintRedDot_couiHintRedPointText);
        obtainStyledAttributes.recycle();
        this.f7282j = new COUIHintRedDotHelper(context, attributeSet, iArr, i10, 0);
        this.f7283k = new RectF();
        this.f7284l = getResources().getString(R$string.red_dot_description);
        this.f7285m = R$plurals.red_dot_with_number_description;
        Drawable drawable = context.getResources().getDrawable(R$drawable.red_dot_stroke_circle);
        this.f7292t = drawable;
        if (this.f7278f == 4) {
            setBackground(drawable);
        }
    }
}
