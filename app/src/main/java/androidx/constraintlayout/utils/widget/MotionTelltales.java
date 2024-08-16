package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewParent;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.R$styleable;

/* loaded from: classes.dex */
public class MotionTelltales extends MockView {

    /* renamed from: p, reason: collision with root package name */
    private Paint f1806p;

    /* renamed from: q, reason: collision with root package name */
    MotionLayout f1807q;

    /* renamed from: r, reason: collision with root package name */
    float[] f1808r;

    /* renamed from: s, reason: collision with root package name */
    Matrix f1809s;

    /* renamed from: t, reason: collision with root package name */
    int f1810t;

    /* renamed from: u, reason: collision with root package name */
    int f1811u;

    /* renamed from: v, reason: collision with root package name */
    float f1812v;

    public MotionTelltales(Context context) {
        super(context);
        this.f1806p = new Paint();
        this.f1808r = new float[2];
        this.f1809s = new Matrix();
        this.f1810t = 0;
        this.f1811u = -65281;
        this.f1812v = 0.25f;
        a(context, null);
    }

    private void a(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MotionTelltales);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.MotionTelltales_telltales_tailColor) {
                    this.f1811u = obtainStyledAttributes.getColor(index, this.f1811u);
                } else if (index == R$styleable.MotionTelltales_telltales_velocityMode) {
                    this.f1810t = obtainStyledAttributes.getInt(index, this.f1810t);
                } else if (index == R$styleable.MotionTelltales_telltales_tailScale) {
                    this.f1812v = obtainStyledAttributes.getFloat(index, this.f1812v);
                }
            }
        }
        this.f1806p.setColor(this.f1811u);
        this.f1806p.setStrokeWidth(5.0f);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // androidx.constraintlayout.utils.widget.MockView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getMatrix().invert(this.f1809s);
        if (this.f1807q == null) {
            ViewParent parent = getParent();
            if (parent instanceof MotionLayout) {
                this.f1807q = (MotionLayout) parent;
                return;
            }
            return;
        }
        int width = getWidth();
        int height = getHeight();
        float[] fArr = {0.1f, 0.25f, 0.5f, 0.75f, 0.9f};
        for (int i10 = 0; i10 < 5; i10++) {
            float f10 = fArr[i10];
            for (int i11 = 0; i11 < 5; i11++) {
                float f11 = fArr[i11];
                this.f1807q.l0(this, f11, f10, this.f1808r, this.f1810t);
                this.f1809s.mapVectors(this.f1808r);
                float f12 = width * f11;
                float f13 = height * f10;
                float[] fArr2 = this.f1808r;
                float f14 = fArr2[0];
                float f15 = this.f1812v;
                float f16 = f13 - (fArr2[1] * f15);
                this.f1809s.mapVectors(fArr2);
                canvas.drawLine(f12, f13, f12 - (f14 * f15), f16, this.f1806p);
            }
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        postInvalidate();
    }

    public void setText(CharSequence charSequence) {
        this.f1800j = charSequence.toString();
        requestLayout();
    }

    public MotionTelltales(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1806p = new Paint();
        this.f1808r = new float[2];
        this.f1809s = new Matrix();
        this.f1810t = 0;
        this.f1811u = -65281;
        this.f1812v = 0.25f;
        a(context, attributeSet);
    }

    public MotionTelltales(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1806p = new Paint();
        this.f1808r = new float[2];
        this.f1809s = new Matrix();
        this.f1810t = 0;
        this.f1811u = -65281;
        this.f1812v = 0.25f;
        a(context, attributeSet);
    }
}
