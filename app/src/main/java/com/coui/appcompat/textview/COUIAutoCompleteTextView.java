package com.coui.appcompat.textview;

import android.R;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.edittext.COUICutoutDrawable;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$style;
import com.support.appcompat.R$styleable;
import m1.COUIInEaseInterpolator;
import m1.COUILinearInterpolator;
import m1.COUIMoveEaseInterpolator;

/* loaded from: classes.dex */
public class COUIAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    private ColorStateList A;
    private ColorStateList B;
    private int C;
    private int D;
    private int E;
    private boolean F;
    private boolean G;
    private ValueAnimator H;
    private ValueAnimator I;
    private ValueAnimator J;
    private boolean K;
    private boolean L;
    private Paint M;
    private Paint N;
    private int O;
    private int P;
    private int Q;
    private int R;
    private int S;
    private int T;

    /* renamed from: i, reason: collision with root package name */
    private final COUICutoutDrawable.a f7887i;

    /* renamed from: j, reason: collision with root package name */
    private Interpolator f7888j;

    /* renamed from: k, reason: collision with root package name */
    private Interpolator f7889k;

    /* renamed from: l, reason: collision with root package name */
    private CharSequence f7890l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f7891m;

    /* renamed from: n, reason: collision with root package name */
    private CharSequence f7892n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f7893o;

    /* renamed from: p, reason: collision with root package name */
    private GradientDrawable f7894p;

    /* renamed from: q, reason: collision with root package name */
    private int f7895q;

    /* renamed from: r, reason: collision with root package name */
    private int f7896r;

    /* renamed from: s, reason: collision with root package name */
    private float f7897s;

    /* renamed from: t, reason: collision with root package name */
    private float f7898t;

    /* renamed from: u, reason: collision with root package name */
    private float f7899u;

    /* renamed from: v, reason: collision with root package name */
    private float f7900v;

    /* renamed from: w, reason: collision with root package name */
    private int f7901w;

    /* renamed from: x, reason: collision with root package name */
    private int f7902x;

    /* renamed from: y, reason: collision with root package name */
    private int f7903y;

    /* renamed from: z, reason: collision with root package name */
    private RectF f7904z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIAutoCompleteTextView.this.P = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            COUIAutoCompleteTextView.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIAutoCompleteTextView.this.O = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            COUIAutoCompleteTextView.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIAutoCompleteTextView.this.f7887i.R(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    public COUIAutoCompleteTextView(Context context) {
        this(context, null);
    }

    private void A() {
        ViewCompat.A0(this, r() ? getPaddingRight() : getPaddingLeft(), getModePaddingTop(), r() ? getPaddingLeft() : getPaddingRight(), getPaddingBottom());
    }

    private void B() {
        if (this.f7896r == 0 || this.f7894p == null || getRight() == 0) {
            return;
        }
        this.f7894p.setBounds(0, getBoundsTop(), getWidth(), getHeight());
        h();
    }

    private void C() {
        int i10;
        if (this.f7894p == null || (i10 = this.f7896r) == 0 || i10 != 2) {
            return;
        }
        if (!isEnabled()) {
            this.f7903y = this.E;
        } else if (hasFocus()) {
            this.f7903y = this.D;
        } else {
            this.f7903y = this.C;
        }
        h();
    }

    private void e(float f10) {
        if (this.f7887i.w() == f10) {
            return;
        }
        if (this.H == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.H = valueAnimator;
            valueAnimator.setInterpolator(this.f7888j);
            this.H.setDuration(200L);
            this.H.addUpdateListener(new c());
        }
        this.H.setFloatValues(this.f7887i.w(), f10);
        this.H.start();
    }

    private void f() {
        if (this.J == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.J = valueAnimator;
            valueAnimator.setInterpolator(this.f7889k);
            this.J.setDuration(250L);
            this.J.addUpdateListener(new b());
        }
        this.J.setIntValues(255, 0);
        this.J.start();
        this.L = false;
    }

    private void g() {
        if (this.I == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.I = valueAnimator;
            valueAnimator.setInterpolator(this.f7889k);
            this.I.setDuration(250L);
            this.I.addUpdateListener(new a());
        }
        this.O = 255;
        this.I.setIntValues(0, getWidth());
        this.I.start();
        this.L = true;
    }

    private int getBoundsTop() {
        int i10 = this.f7896r;
        if (i10 == 1) {
            return this.R;
        }
        if (i10 != 2) {
            return 0;
        }
        return (int) (this.f7887i.p() / 2.0f);
    }

    private Drawable getBoxBackground() {
        int i10 = this.f7896r;
        if (i10 == 1 || i10 == 2) {
            return this.f7894p;
        }
        return null;
    }

    private float[] getCornerRadiiAsArray() {
        float f10 = this.f7898t;
        float f11 = this.f7897s;
        float f12 = this.f7900v;
        float f13 = this.f7899u;
        return new float[]{f10, f10, f11, f11, f12, f12, f13, f13};
    }

    private int getModePaddingTop() {
        int x10;
        int i10;
        int i11 = this.f7896r;
        if (i11 == 1) {
            x10 = this.R + ((int) this.f7887i.x());
            i10 = this.S;
        } else {
            if (i11 != 2) {
                return 0;
            }
            x10 = this.Q;
            i10 = (int) (this.f7887i.p() / 2.0f);
        }
        return x10 + i10;
    }

    private void h() {
        int i10;
        if (this.f7894p == null) {
            return;
        }
        u();
        int i11 = this.f7901w;
        if (i11 > -1 && (i10 = this.f7903y) != 0) {
            this.f7894p.setStroke(i11, i10);
        }
        this.f7894p.setCornerRadii(getCornerRadiiAsArray());
        invalidate();
    }

    private void i(RectF rectF) {
        float f10 = rectF.left;
        int i10 = this.f7895q;
        rectF.left = f10 - i10;
        rectF.top -= i10;
        rectF.right += i10;
        rectF.bottom += i10;
    }

    private void j() {
        int i10 = this.f7896r;
        if (i10 == 0) {
            this.f7894p = null;
            return;
        }
        if (i10 == 2 && this.f7891m && !(this.f7894p instanceof COUICutoutDrawable)) {
            this.f7894p = new COUICutoutDrawable();
        } else if (this.f7894p == null) {
            this.f7894p = new GradientDrawable();
        }
    }

    private int k() {
        int i10 = this.f7896r;
        if (i10 == 1) {
            return getBoxBackground().getBounds().top;
        }
        if (i10 != 2) {
            return getPaddingTop();
        }
        return getBoxBackground().getBounds().top - l();
    }

    private int l() {
        return (int) (this.f7887i.p() / 2.0f);
    }

    private void m() {
        if (o()) {
            ((COUICutoutDrawable) this.f7894p).e();
        }
    }

    private void n(boolean z10) {
        ValueAnimator valueAnimator = this.H;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.H.cancel();
        }
        if (z10 && this.G) {
            e(1.0f);
        } else {
            this.f7887i.R(1.0f);
        }
        this.F = false;
        if (o()) {
            t();
        }
    }

    private boolean o() {
        return this.f7891m && !TextUtils.isEmpty(this.f7892n) && (this.f7894p instanceof COUICutoutDrawable);
    }

    private void p(boolean z10) {
        if (this.f7894p != null) {
            Log.d("AutoCompleteTextView", "mBoxBackground: " + this.f7894p.getBounds());
        }
        ValueAnimator valueAnimator = this.H;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.H.cancel();
        }
        if (z10 && this.G) {
            e(0.0f);
        } else {
            this.f7887i.R(0.0f);
        }
        if (o() && ((COUICutoutDrawable) this.f7894p).b()) {
            m();
        }
        this.F = true;
    }

    private void q(Context context, AttributeSet attributeSet, int i10) {
        this.f7887i.Y(new COUILinearInterpolator());
        this.f7887i.V(new COUILinearInterpolator());
        this.f7887i.M(8388659);
        this.f7888j = new COUIMoveEaseInterpolator();
        this.f7889k = new COUIInEaseInterpolator();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIEditText, i10, R$style.Widget_COUI_EditText_HintAnim_Line);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUIEditText_couiHintEnabled, false);
        this.f7891m = z10;
        if (!z10) {
            obtainStyledAttributes.recycle();
            return;
        }
        setBackgroundDrawable(null);
        setTopHint(obtainStyledAttributes.getText(R$styleable.COUIEditText_android_hint));
        this.G = obtainStyledAttributes.getBoolean(R$styleable.COUIEditText_couiHintAnimationEnabled, true);
        this.Q = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIEditText_rectModePaddingTop, 0);
        float dimension = obtainStyledAttributes.getDimension(R$styleable.COUIEditText_cornerRadius, 0.0f);
        this.f7897s = dimension;
        this.f7898t = dimension;
        this.f7899u = dimension;
        this.f7900v = dimension;
        int i11 = R$styleable.COUIEditText_couiStrokeColor;
        this.D = obtainStyledAttributes.getColor(i11, -16711936);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIEditText_couiStrokeWidth, 0);
        this.f7901w = dimensionPixelOffset;
        this.f7902x = dimensionPixelOffset;
        this.f7895q = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_label_cutout_padding);
        this.R = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_line_padding_top);
        this.S = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_line_padding_middle);
        this.T = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_rect_padding_middle);
        int i12 = obtainStyledAttributes.getInt(R$styleable.COUIEditText_couiBackgroundMode, 0);
        setBoxBackgroundMode(i12);
        int i13 = R$styleable.COUIEditText_android_textColorHint;
        if (obtainStyledAttributes.hasValue(i13)) {
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(i13);
            this.B = colorStateList;
            this.A = colorStateList;
        }
        this.C = context.getResources().getColor(R$color.coui_textinput_stroke_color_default);
        this.E = context.getResources().getColor(R$color.coui_textinput_stroke_color_disabled);
        v(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIEditText_collapsedTextSize, 0), obtainStyledAttributes.getColorStateList(i11));
        if (i12 == 2) {
            this.f7887i.a0(Typeface.create("sans-serif-medium", 0));
        }
        obtainStyledAttributes.recycle();
        Paint paint = new Paint();
        this.N = paint;
        paint.setColor(this.C);
        this.N.setStrokeWidth(this.f7901w);
        Paint paint2 = new Paint();
        this.M = paint2;
        paint2.setColor(this.D);
        this.M.setStrokeWidth(this.f7901w);
        w();
    }

    private boolean r() {
        return getLayoutDirection() == 1;
    }

    private void s() {
        j();
        B();
    }

    private void setHintInternal(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.f7892n)) {
            return;
        }
        this.f7892n = charSequence;
        this.f7887i.X(charSequence);
        if (this.F) {
            return;
        }
        t();
    }

    private void t() {
        if (o()) {
            RectF rectF = this.f7904z;
            this.f7887i.m(rectF);
            i(rectF);
            ((COUICutoutDrawable) this.f7894p).h(rectF);
        }
    }

    private void u() {
        int i10 = this.f7896r;
        if (i10 != 1) {
            if (i10 == 2 && this.D == 0) {
                this.D = this.B.getColorForState(getDrawableState(), this.B.getDefaultColor());
                return;
            }
            return;
        }
        this.f7901w = 0;
    }

    private void w() {
        s();
        this.f7887i.Q(getTextSize());
        int gravity = getGravity();
        this.f7887i.M((gravity & (-113)) | 48);
        this.f7887i.P(gravity);
        if (this.A == null) {
            this.A = getHintTextColors();
        }
        if (this.f7891m) {
            setHint((CharSequence) null);
            if (TextUtils.isEmpty(this.f7892n)) {
                CharSequence hint = getHint();
                this.f7890l = hint;
                setTopHint(hint);
                setHint((CharSequence) null);
            }
            this.f7893o = true;
        }
        y(false, true);
        A();
    }

    private void y(boolean z10, boolean z11) {
        ColorStateList colorStateList;
        boolean isEnabled = isEnabled();
        boolean z12 = !TextUtils.isEmpty(getText());
        ColorStateList colorStateList2 = this.A;
        if (colorStateList2 != null) {
            this.f7887i.L(colorStateList2);
            this.f7887i.O(this.A);
        }
        if (!isEnabled) {
            this.f7887i.L(ColorStateList.valueOf(this.E));
            this.f7887i.O(ColorStateList.valueOf(this.E));
        } else if (hasFocus() && (colorStateList = this.B) != null) {
            this.f7887i.L(colorStateList);
        }
        if (!z12 && (!isEnabled() || !hasFocus())) {
            if (z11 || !this.F) {
                p(z10);
                return;
            }
            return;
        }
        if (z11 || this.F) {
            n(z10);
        }
    }

    private void z() {
        if (this.f7896r != 1) {
            return;
        }
        if (isEnabled()) {
            if (hasFocus()) {
                if (this.L) {
                    return;
                }
                g();
                return;
            } else {
                if (this.L) {
                    f();
                    return;
                }
                return;
            }
        }
        this.P = 0;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.f7891m) {
            int save = canvas.save();
            canvas.translate(getScrollX(), getScrollY());
            this.f7887i.j(canvas);
            if (this.f7894p != null && this.f7896r == 2) {
                if (getScrollX() != 0) {
                    B();
                }
                this.f7894p.draw(canvas);
            }
            if (this.f7896r == 1) {
                float height = getHeight() - ((int) ((this.f7902x / 2.0d) + 0.5d));
                canvas.drawLine(0.0f, height, getWidth(), height, this.N);
                this.M.setAlpha(this.O);
                canvas.drawLine(0.0f, height, this.P, height, this.M);
            }
            canvas.restoreToCount(save);
        }
        super.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatAutoCompleteTextView, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        if (!this.f7891m) {
            super.drawableStateChanged();
            return;
        }
        if (this.K) {
            return;
        }
        this.K = true;
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        x(ViewCompat.Q(this) && isEnabled());
        z();
        B();
        C();
        COUICutoutDrawable.a aVar = this.f7887i;
        if (aVar != null ? aVar.W(drawableState) | false : false) {
            invalidate();
        }
        this.K = false;
    }

    public int getBoxStrokeColor() {
        return this.D;
    }

    @Override // android.widget.TextView
    public CharSequence getHint() {
        if (this.f7891m) {
            return this.f7892n;
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (this.f7891m) {
            if (this.f7894p != null) {
                B();
            }
            A();
            int compoundPaddingLeft = getCompoundPaddingLeft();
            int width = getWidth() - getCompoundPaddingRight();
            int k10 = k();
            this.f7887i.N(compoundPaddingLeft, getCompoundPaddingTop(), width, getHeight() - getCompoundPaddingBottom());
            this.f7887i.J(compoundPaddingLeft, k10, width, getHeight() - getCompoundPaddingBottom());
            this.f7887i.H();
            if (!o() || this.F) {
                return;
            }
            t();
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
    }

    public void setBoxBackgroundMode(int i10) {
        if (i10 == this.f7896r) {
            return;
        }
        this.f7896r = i10;
        s();
    }

    public void setBoxStrokeColor(int i10) {
        if (this.D != i10) {
            this.D = i10;
            C();
        }
    }

    public void setHintEnabled(boolean z10) {
        if (z10 != this.f7891m) {
            this.f7891m = z10;
            if (!z10) {
                this.f7893o = false;
                if (!TextUtils.isEmpty(this.f7892n) && TextUtils.isEmpty(getHint())) {
                    setHint(this.f7892n);
                }
                setHintInternal(null);
                return;
            }
            CharSequence hint = getHint();
            if (!TextUtils.isEmpty(hint)) {
                if (TextUtils.isEmpty(this.f7892n)) {
                    setTopHint(hint);
                }
                setHint((CharSequence) null);
            }
            this.f7893o = true;
        }
    }

    public void setTopHint(CharSequence charSequence) {
        if (this.f7891m) {
            setHintInternal(charSequence);
        }
    }

    public void setmHintAnimationEnabled(boolean z10) {
        this.G = z10;
    }

    public void v(int i10, ColorStateList colorStateList) {
        this.f7887i.K(i10, colorStateList);
        this.B = this.f7887i.n();
        x(false);
    }

    public void x(boolean z10) {
        y(z10, false);
    }

    public COUIAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.autoCompleteTextViewStyle);
    }

    public COUIAutoCompleteTextView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7887i = new COUICutoutDrawable.a(this);
        this.f7901w = 3;
        this.f7904z = new RectF();
        q(context, attributeSet, i10);
    }
}
