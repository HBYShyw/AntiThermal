package com.google.android.material.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatImageView;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import c4.ShapeAppearancePathProvider;
import c4.Shapeable;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import d4.MaterialThemeOverlay;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class ShapeableImageView extends AppCompatImageView implements Shapeable {

    /* renamed from: z, reason: collision with root package name */
    private static final int f8921z = R$style.Widget_MaterialComponents_ShapeableImageView;

    /* renamed from: h, reason: collision with root package name */
    private final ShapeAppearancePathProvider f8922h;

    /* renamed from: i, reason: collision with root package name */
    private final RectF f8923i;

    /* renamed from: j, reason: collision with root package name */
    private final RectF f8924j;

    /* renamed from: k, reason: collision with root package name */
    private final Paint f8925k;

    /* renamed from: l, reason: collision with root package name */
    private final Paint f8926l;

    /* renamed from: m, reason: collision with root package name */
    private final Path f8927m;

    /* renamed from: n, reason: collision with root package name */
    private ColorStateList f8928n;

    /* renamed from: o, reason: collision with root package name */
    private MaterialShapeDrawable f8929o;

    /* renamed from: p, reason: collision with root package name */
    private ShapeAppearanceModel f8930p;

    /* renamed from: q, reason: collision with root package name */
    private float f8931q;

    /* renamed from: r, reason: collision with root package name */
    private Path f8932r;

    /* renamed from: s, reason: collision with root package name */
    private int f8933s;

    /* renamed from: t, reason: collision with root package name */
    private int f8934t;

    /* renamed from: u, reason: collision with root package name */
    private int f8935u;

    /* renamed from: v, reason: collision with root package name */
    private int f8936v;

    /* renamed from: w, reason: collision with root package name */
    private int f8937w;

    /* renamed from: x, reason: collision with root package name */
    private int f8938x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f8939y;

    @TargetApi(21)
    /* loaded from: classes.dex */
    class a extends ViewOutlineProvider {

        /* renamed from: a, reason: collision with root package name */
        private final Rect f8940a = new Rect();

        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (ShapeableImageView.this.f8930p == null) {
                return;
            }
            if (ShapeableImageView.this.f8929o == null) {
                ShapeableImageView.this.f8929o = new MaterialShapeDrawable(ShapeableImageView.this.f8930p);
            }
            ShapeableImageView.this.f8923i.round(this.f8940a);
            ShapeableImageView.this.f8929o.setBounds(this.f8940a);
            ShapeableImageView.this.f8929o.getOutline(outline);
        }
    }

    public ShapeableImageView(Context context) {
        this(context, null, 0);
    }

    private void g(Canvas canvas) {
        if (this.f8928n == null) {
            return;
        }
        this.f8925k.setStrokeWidth(this.f8931q);
        int colorForState = this.f8928n.getColorForState(getDrawableState(), this.f8928n.getDefaultColor());
        if (this.f8931q <= 0.0f || colorForState == 0) {
            return;
        }
        this.f8925k.setColor(colorForState);
        canvas.drawPath(this.f8927m, this.f8925k);
    }

    private boolean h() {
        return (this.f8937w == Integer.MIN_VALUE && this.f8938x == Integer.MIN_VALUE) ? false : true;
    }

    private boolean i() {
        return getLayoutDirection() == 1;
    }

    private void j(int i10, int i11) {
        this.f8923i.set(getPaddingLeft(), getPaddingTop(), i10 - getPaddingRight(), i11 - getPaddingBottom());
        this.f8922h.d(this.f8930p, 1.0f, this.f8923i, this.f8927m);
        this.f8932r.rewind();
        this.f8932r.addPath(this.f8927m);
        this.f8924j.set(0.0f, 0.0f, i10, i11);
        this.f8932r.addRect(this.f8924j, Path.Direction.CCW);
    }

    public int getContentPaddingBottom() {
        return this.f8936v;
    }

    public final int getContentPaddingEnd() {
        int i10 = this.f8938x;
        return i10 != Integer.MIN_VALUE ? i10 : i() ? this.f8933s : this.f8935u;
    }

    public int getContentPaddingLeft() {
        int i10;
        int i11;
        if (h()) {
            if (i() && (i11 = this.f8938x) != Integer.MIN_VALUE) {
                return i11;
            }
            if (!i() && (i10 = this.f8937w) != Integer.MIN_VALUE) {
                return i10;
            }
        }
        return this.f8933s;
    }

    public int getContentPaddingRight() {
        int i10;
        int i11;
        if (h()) {
            if (i() && (i11 = this.f8937w) != Integer.MIN_VALUE) {
                return i11;
            }
            if (!i() && (i10 = this.f8938x) != Integer.MIN_VALUE) {
                return i10;
            }
        }
        return this.f8935u;
    }

    public final int getContentPaddingStart() {
        int i10 = this.f8937w;
        return i10 != Integer.MIN_VALUE ? i10 : i() ? this.f8935u : this.f8933s;
    }

    public int getContentPaddingTop() {
        return this.f8934t;
    }

    @Override // android.view.View
    public int getPaddingBottom() {
        return super.getPaddingBottom() - getContentPaddingBottom();
    }

    @Override // android.view.View
    public int getPaddingEnd() {
        return super.getPaddingEnd() - getContentPaddingEnd();
    }

    @Override // android.view.View
    public int getPaddingLeft() {
        return super.getPaddingLeft() - getContentPaddingLeft();
    }

    @Override // android.view.View
    public int getPaddingRight() {
        return super.getPaddingRight() - getContentPaddingRight();
    }

    @Override // android.view.View
    public int getPaddingStart() {
        return super.getPaddingStart() - getContentPaddingStart();
    }

    @Override // android.view.View
    public int getPaddingTop() {
        return super.getPaddingTop() - getContentPaddingTop();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.f8930p;
    }

    public ColorStateList getStrokeColor() {
        return this.f8928n;
    }

    public float getStrokeWidth() {
        return this.f8931q;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLayerType(2, null);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        setLayerType(0, null);
        super.onDetachedFromWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.f8932r, this.f8926l);
        g(canvas);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        if (!this.f8939y && isLayoutDirectionResolved()) {
            this.f8939y = true;
            if (!isPaddingRelative() && !h()) {
                setPadding(super.getPaddingLeft(), super.getPaddingTop(), super.getPaddingRight(), super.getPaddingBottom());
            } else {
                setPaddingRelative(super.getPaddingStart(), super.getPaddingTop(), super.getPaddingEnd(), super.getPaddingBottom());
            }
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        j(i10, i11);
    }

    @Override // android.view.View
    public void setPadding(int i10, int i11, int i12, int i13) {
        super.setPadding(i10 + getContentPaddingLeft(), i11 + getContentPaddingTop(), i12 + getContentPaddingRight(), i13 + getContentPaddingBottom());
    }

    @Override // android.view.View
    public void setPaddingRelative(int i10, int i11, int i12, int i13) {
        super.setPaddingRelative(i10 + getContentPaddingStart(), i11 + getContentPaddingTop(), i12 + getContentPaddingEnd(), i13 + getContentPaddingBottom());
    }

    @Override // c4.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.f8930p = shapeAppearanceModel;
        MaterialShapeDrawable materialShapeDrawable = this.f8929o;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
        j(getWidth(), getHeight());
        invalidate();
        invalidateOutline();
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        this.f8928n = colorStateList;
        invalidate();
    }

    public void setStrokeColorResource(int i10) {
        setStrokeColor(AppCompatResources.a(getContext(), i10));
    }

    public void setStrokeWidth(float f10) {
        if (this.f8931q != f10) {
            this.f8931q = f10;
            invalidate();
        }
    }

    public void setStrokeWidthResource(int i10) {
        setStrokeWidth(getResources().getDimensionPixelSize(i10));
    }

    public ShapeableImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ShapeableImageView(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r0), attributeSet, i10);
        int i11 = f8921z;
        this.f8922h = ShapeAppearancePathProvider.k();
        this.f8927m = new Path();
        this.f8939y = false;
        Context context2 = getContext();
        Paint paint = new Paint();
        this.f8926l = paint;
        paint.setAntiAlias(true);
        paint.setColor(-1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.f8923i = new RectF();
        this.f8924j = new RectF();
        this.f8932r = new Path();
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R$styleable.ShapeableImageView, i10, i11);
        this.f8928n = MaterialResources.a(context2, obtainStyledAttributes, R$styleable.ShapeableImageView_strokeColor);
        this.f8931q = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_strokeWidth, 0);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_contentPadding, 0);
        this.f8933s = dimensionPixelSize;
        this.f8934t = dimensionPixelSize;
        this.f8935u = dimensionPixelSize;
        this.f8936v = dimensionPixelSize;
        this.f8933s = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_contentPaddingLeft, dimensionPixelSize);
        this.f8934t = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_contentPaddingTop, dimensionPixelSize);
        this.f8935u = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_contentPaddingRight, dimensionPixelSize);
        this.f8936v = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_contentPaddingBottom, dimensionPixelSize);
        this.f8937w = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_contentPaddingStart, Integer.MIN_VALUE);
        this.f8938x = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ShapeableImageView_contentPaddingEnd, Integer.MIN_VALUE);
        obtainStyledAttributes.recycle();
        Paint paint2 = new Paint();
        this.f8925k = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setAntiAlias(true);
        this.f8930p = ShapeAppearanceModel.e(context2, attributeSet, i10, i11).m();
        setOutlineProvider(new a());
    }
}
