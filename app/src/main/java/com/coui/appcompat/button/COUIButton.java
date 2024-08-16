package com.coui.appcompat.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.R$attr;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.ColorUtils;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;
import h2.COUIPressFeedbackHelper;
import m2.COUIRoundRectUtil;
import s1.OnSizeChangeListener;
import s1.OnTextChangeListener;
import w1.COUIDarkModeUtil;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUIButton extends AppCompatButton {
    private int A;
    private boolean B;
    private OnSizeChangeListener C;
    private OnTextChangeListener D;

    /* renamed from: h, reason: collision with root package name */
    private boolean f5491h;

    /* renamed from: i, reason: collision with root package name */
    private int f5492i;

    /* renamed from: j, reason: collision with root package name */
    private int f5493j;

    /* renamed from: k, reason: collision with root package name */
    private final Paint f5494k;

    /* renamed from: l, reason: collision with root package name */
    private int f5495l;

    /* renamed from: m, reason: collision with root package name */
    private int f5496m;

    /* renamed from: n, reason: collision with root package name */
    private float f5497n;

    /* renamed from: o, reason: collision with root package name */
    private float f5498o;

    /* renamed from: p, reason: collision with root package name */
    private float f5499p;

    /* renamed from: q, reason: collision with root package name */
    private float f5500q;

    /* renamed from: r, reason: collision with root package name */
    private float f5501r;

    /* renamed from: s, reason: collision with root package name */
    private float f5502s;

    /* renamed from: t, reason: collision with root package name */
    private int f5503t;

    /* renamed from: u, reason: collision with root package name */
    private Rect f5504u;

    /* renamed from: v, reason: collision with root package name */
    private RectF f5505v;

    /* renamed from: w, reason: collision with root package name */
    private RectF f5506w;

    /* renamed from: x, reason: collision with root package name */
    private float[] f5507x;

    /* renamed from: y, reason: collision with root package name */
    private int f5508y;

    /* renamed from: z, reason: collision with root package name */
    private COUIPressFeedbackHelper f5509z;

    public COUIButton(Context context) {
        this(context, null);
    }

    private int a(int i10) {
        if (!isEnabled()) {
            return this.f5496m;
        }
        return ColorUtils.i(Color.argb(this.f5509z.o(), 0.0f, 0.0f, 0.0f), this.f5495l);
    }

    private float b(Rect rect) {
        float f10 = this.f5497n;
        return f10 < 0.0f ? ((rect.bottom - rect.top) / 2.0f) - this.f5502s : f10;
    }

    private float c(RectF rectF) {
        float f10 = this.f5497n;
        return f10 < 0.0f ? ((rectF.bottom - rectF.top) / 2.0f) - this.f5502s : f10;
    }

    private int d(int i10) {
        if (!isEnabled()) {
            return i10;
        }
        return Color.argb((int) (this.f5509z.n() * 255.0f), Math.min(255, Color.red(i10)), Math.min(255, Color.green(i10)), Math.min(255, Color.blue(i10)));
    }

    private void e() {
        if (this.B) {
            performHapticFeedback(302);
        }
    }

    private void f() {
        if (this.f5492i == 1) {
            setBackgroundDrawable(null);
        }
    }

    public float getDrawableRadius() {
        return b(this.f5504u);
    }

    public int getRoundType() {
        return this.f5493j;
    }

    @Override // android.view.View
    public int getSolidColor() {
        if (this.f5491h && this.f5492i == 1) {
            return a(this.f5495l);
        }
        return super.getSolidColor();
    }

    public float getStrokeWidth() {
        return this.f5501r;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        if (this.f5491h) {
            int save = canvas.save();
            canvas.translate(getScrollX(), getScrollY());
            this.f5494k.setStyle(Paint.Style.FILL);
            this.f5494k.setAntiAlias(true);
            if (this.f5492i == 1) {
                this.f5494k.setColor(a(this.f5495l));
            } else {
                this.f5494k.setColor(d(this.f5495l));
            }
            if (this.f5493j == 1) {
                float drawableRadius = getDrawableRadius();
                canvas.drawRoundRect(this.f5505v, drawableRadius, drawableRadius, this.f5494k);
                if (this.f5492i != 1) {
                    float c10 = (c(this.f5506w) + this.f5502s) - this.f5501r;
                    this.f5494k.setColor(isEnabled() ? this.f5503t : this.f5496m);
                    this.f5494k.setStrokeWidth(this.f5501r);
                    this.f5494k.setStyle(Paint.Style.STROKE);
                    canvas.drawRoundRect(this.f5506w, c10, c10, this.f5494k);
                }
            } else {
                canvas.drawPath(COUIRoundRectUtil.a().b(this.f5504u, getDrawableRadius()), this.f5494k);
                if (this.f5492i != 1) {
                    this.f5494k.setColor(isEnabled() ? this.f5503t : this.f5496m);
                    this.f5494k.setStrokeWidth(this.f5501r);
                    this.f5494k.setStyle(Paint.Style.STROKE);
                    COUIRoundRectUtil a10 = COUIRoundRectUtil.a();
                    RectF rectF = this.f5506w;
                    canvas.drawPath(a10.c(rectF, (c(rectF) + this.f5502s) - this.f5501r), this.f5494k);
                }
            }
            canvas.restoreToCount(save);
        }
        super.onDraw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        this.f5504u.right = getWidth();
        this.f5504u.bottom = getHeight();
        this.f5505v.set(this.f5504u);
        RectF rectF = this.f5506w;
        float f10 = this.f5504u.top;
        float f11 = this.f5501r;
        rectF.top = f10 + (f11 / 2.0f);
        rectF.left = r2.left + (f11 / 2.0f);
        rectF.right = r2.right - (f11 / 2.0f);
        rectF.bottom = r2.bottom - (f11 / 2.0f);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        OnSizeChangeListener onSizeChangeListener = this.C;
        if (onSizeChangeListener != null) {
            onSizeChangeListener.a(this, i10, i11, i12, i13);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView
    public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        super.onTextChanged(charSequence, i10, i11, i12);
        OnTextChangeListener onTextChangeListener = this.D;
        if (onTextChangeListener != null) {
            onTextChangeListener.a(this, charSequence, i10, i11, i12);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isEnabled() && this.f5491h) {
            int action = motionEvent.getAction();
            if (action == 0) {
                e();
                this.f5509z.m(true);
            } else if (action == 1 || action == 3) {
                e();
                this.f5509z.m(false);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setAnimEnable(boolean z10) {
        this.f5491h = z10;
    }

    public void setAnimType(int i10) {
        this.f5492i = i10;
    }

    public void setDisabledColor(int i10) {
        this.f5496m = i10;
    }

    public void setDrawableColor(int i10) {
        this.f5495l = i10;
    }

    public void setDrawableRadius(int i10) {
        this.f5497n = i10;
    }

    public void setIsNeedVibrate(boolean z10) {
        this.B = z10;
    }

    public void setMaxBrightness(int i10) {
        this.f5498o = i10;
    }

    public void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        this.C = onSizeChangeListener;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.D = onTextChangeListener;
    }

    public void setRoundType(int i10) {
        if (i10 != 0 && i10 != 1) {
            throw new IllegalArgumentException("Invalid roundType" + i10);
        }
        if (this.f5493j != i10) {
            this.f5493j = i10;
            invalidate();
        }
    }

    public void setStrokeColor(int i10) {
        this.f5503t = i10;
    }

    public void setStrokeWidth(float f10) {
        this.f5501r = f10;
    }

    public COUIButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.buttonStyle);
    }

    public COUIButton(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        Paint paint = new Paint(1);
        this.f5494k = paint;
        this.f5497n = 21.0f;
        this.f5499p = 1.0f;
        this.f5500q = 1.0f;
        this.f5504u = new Rect();
        this.f5505v = new RectF();
        this.f5506w = new RectF();
        this.f5507x = new float[3];
        boolean z10 = false;
        this.A = 0;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f5508y = attributeSet.getStyleAttribute();
        } else {
            this.f5508y = i10;
        }
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIButton, i10, 0);
        this.f5491h = obtainStyledAttributes.getBoolean(R$styleable.COUIButton_animEnable, false);
        this.f5492i = obtainStyledAttributes.getInteger(R$styleable.COUIButton_animType, 1);
        this.f5493j = obtainStyledAttributes.getInteger(R$styleable.COUIButton_couiRoundType, 0);
        this.B = obtainStyledAttributes.getBoolean(R$styleable.COUIButton_needVibrate, true);
        if (this.f5491h) {
            this.f5498o = obtainStyledAttributes.getFloat(R$styleable.COUIButton_brightness, 0.8f);
            this.f5497n = obtainStyledAttributes.getDimension(R$styleable.COUIButton_drawableRadius, -1.0f);
            this.f5496m = obtainStyledAttributes.getColor(R$styleable.COUIButton_disabledColor, 0);
            this.f5495l = obtainStyledAttributes.getColor(R$styleable.COUIButton_drawableColor, 0);
            this.f5503t = obtainStyledAttributes.getColor(R$styleable.COUIButton_strokeColor, 0);
            this.A = obtainStyledAttributes.getInteger(R$styleable.COUIButton_pressColor, 0);
            z10 = obtainStyledAttributes.getBoolean(R$styleable.COUIButton_closeLimitTextSize, false);
            f();
        }
        this.f5501r = obtainStyledAttributes.getDimension(R$styleable.COUIButton_strokeWidth, context.getResources().getDimension(R$dimen.coui_bordless_btn_stroke_width));
        obtainStyledAttributes.recycle();
        this.f5502s = getResources().getDimension(R$dimen.coui_button_radius_offset);
        if (!z10) {
            COUIChangeTextUtil.c(this, 4);
        }
        if (this.f5492i == 1) {
            this.f5509z = new COUIPressFeedbackHelper(this, 2);
        } else {
            this.f5509z = new COUIPressFeedbackHelper(this, 1);
        }
        paint.setAntiAlias(true);
    }
}
