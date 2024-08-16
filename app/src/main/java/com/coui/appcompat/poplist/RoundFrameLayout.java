package com.coui.appcompat.poplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class RoundFrameLayout extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private Path f6909e;

    /* renamed from: f, reason: collision with root package name */
    private Paint f6910f;

    /* renamed from: g, reason: collision with root package name */
    private RectF f6911g;

    /* renamed from: h, reason: collision with root package name */
    private float f6912h;

    public RoundFrameLayout(Context context) {
        this(context, null);
    }

    private void a(Canvas canvas) {
        canvas.save();
        canvas.clipPath(b());
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    private Path b() {
        this.f6909e.reset();
        Path path = this.f6909e;
        RectF rectF = this.f6911g;
        float f10 = this.f6912h;
        path.addRoundRect(rectF, f10, f10, Path.Direction.CW);
        return this.f6909e;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        a(canvas);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        this.f6911g.set(getPaddingLeft(), getPaddingTop(), i10 - getPaddingRight(), i11 - getPaddingBottom());
    }

    public void setRadius(float f10) {
        this.f6912h = f10;
        postInvalidate();
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RoundFrameLayout);
        this.f6912h = obtainStyledAttributes.getDimension(R$styleable.RoundFrameLayout_rfRadius, 0.0f);
        obtainStyledAttributes.recycle();
        this.f6909e = new Path();
        this.f6910f = new Paint(1);
        this.f6911g = new RectF();
        this.f6910f.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }
}
