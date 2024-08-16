package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIHorizontalProgressBar extends ProgressBar {

    /* renamed from: p, reason: collision with root package name */
    private static final int f7127p = Color.argb(12, 0, 0, 0);

    /* renamed from: q, reason: collision with root package name */
    private static final int f7128q = Color.parseColor("#FF2AD181");

    /* renamed from: r, reason: collision with root package name */
    private static final int[] f7129r = {R$attr.couiSeekBarProgressColorDisabled};

    /* renamed from: e, reason: collision with root package name */
    private Paint f7130e;

    /* renamed from: f, reason: collision with root package name */
    private ColorStateList f7131f;

    /* renamed from: g, reason: collision with root package name */
    private ColorStateList f7132g;

    /* renamed from: h, reason: collision with root package name */
    private RectF f7133h;

    /* renamed from: i, reason: collision with root package name */
    private RectF f7134i;

    /* renamed from: j, reason: collision with root package name */
    private int f7135j;

    /* renamed from: k, reason: collision with root package name */
    private Path f7136k;

    /* renamed from: l, reason: collision with root package name */
    private Path f7137l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f7138m;

    /* renamed from: n, reason: collision with root package name */
    private int f7139n;

    /* renamed from: o, reason: collision with root package name */
    private Context f7140o;

    public COUIHorizontalProgressBar(Context context) {
        this(context, null);
    }

    private int a(ColorStateList colorStateList, int i10) {
        return colorStateList == null ? i10 : colorStateList.getColorForState(getDrawableState(), i10);
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onDraw(Canvas canvas) {
        this.f7137l.reset();
        this.f7136k.reset();
        int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        this.f7130e.setColor(a(this.f7131f, f7127p));
        this.f7133h.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        Path path = this.f7136k;
        RectF rectF = this.f7133h;
        int i10 = this.f7135j;
        path.addRoundRect(rectF, i10, i10, Path.Direction.CCW);
        canvas.clipPath(this.f7136k);
        RectF rectF2 = this.f7133h;
        int i11 = this.f7135j;
        canvas.drawRoundRect(rectF2, i11, i11, this.f7130e);
        float progress = getProgress() / getMax();
        if (isLayoutRtl()) {
            this.f7134i.set(Math.round((getWidth() - getPaddingRight()) - (progress * width)), getPaddingTop(), r1 + width, getHeight() - getPaddingBottom());
        } else {
            this.f7134i.set(Math.round(getPaddingLeft() - ((1.0f - progress) * width)), getPaddingTop(), r1 + width, getHeight() - getPaddingBottom());
        }
        this.f7130e.setColor(a(this.f7132g, f7128q));
        this.f7137l.addRoundRect(this.f7134i, this.f7135j, 0.0f, Path.Direction.CCW);
        this.f7137l.op(this.f7136k, Path.Op.INTERSECT);
        canvas.drawPath(this.f7137l, this.f7130e);
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        int paddingRight = (i10 - getPaddingRight()) - getPaddingLeft();
        int paddingTop = (i11 - getPaddingTop()) - getPaddingBottom();
        if (this.f7138m) {
            this.f7135j = paddingRight >= paddingTop ? paddingTop / 2 : paddingRight / 2;
        } else {
            this.f7135j = 0;
        }
    }

    public void setBackgroundColor(ColorStateList colorStateList) {
        this.f7131f = colorStateList;
    }

    public void setProgressColor(ColorStateList colorStateList) {
        this.f7132g = colorStateList;
    }

    public COUIHorizontalProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiHorizontalProgressBarStyle);
    }

    public COUIHorizontalProgressBar(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, R$attr.couiHorizontalProgressBarStyle);
        this.f7130e = new Paint();
        this.f7133h = new RectF();
        this.f7134i = new RectF();
        this.f7135j = Integer.MAX_VALUE;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f7139n = attributeSet.getStyleAttribute();
        } else {
            this.f7139n = i10;
        }
        this.f7140o = context;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(f7129r);
        obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUIHorizontalProgressBar, i10, 0);
        this.f7131f = obtainStyledAttributes2.getColorStateList(R$styleable.COUIHorizontalProgressBar_couiHorizontalProgressBarBackgroundColor);
        this.f7132g = obtainStyledAttributes2.getColorStateList(R$styleable.COUIHorizontalProgressBar_couiHorizontalProgressBarProgressColor);
        this.f7138m = obtainStyledAttributes2.getBoolean(R$styleable.COUIHorizontalProgressBar_couiHorizontalProgressNeedRadius, true);
        obtainStyledAttributes2.recycle();
        this.f7130e.setDither(true);
        this.f7130e.setAntiAlias(true);
        setLayerType(1, this.f7130e);
        this.f7136k = new Path();
        this.f7137l = new Path();
    }
}
