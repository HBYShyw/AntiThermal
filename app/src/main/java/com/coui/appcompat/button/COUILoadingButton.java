package com.coui.appcompat.button;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import androidx.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$string;
import com.support.appcompat.R$styleable;
import m1.COUILinearInterpolator;

/* loaded from: classes.dex */
public class COUILoadingButton extends COUIButton {
    private int E;
    private String F;
    private String G;
    private final String H;
    private final Rect I;
    private final float J;
    private final float K;
    private final float L;
    private boolean M;
    private int N;
    private int O;
    private int P;
    private AnimatorSet Q;
    private f R;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
            if (COUILoadingButton.this.E != 1 || charSequence.toString().equals("")) {
                return;
            }
            COUILoadingButton.this.F = charSequence.toString();
            COUILoadingButton.this.setText("");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUILoadingButton.this.N = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            COUILoadingButton.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUILoadingButton.this.O = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            COUILoadingButton.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUILoadingButton.this.P = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            COUILoadingButton.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e extends AnimatorListenerAdapter {

        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUILoadingButton.this.Q.start();
            }
        }

        e() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUILoadingButton.this.Q == null || COUILoadingButton.this.E != 1) {
                return;
            }
            COUILoadingButton.this.post(new a());
        }
    }

    /* loaded from: classes.dex */
    public interface f {
    }

    public COUILoadingButton(Context context) {
        this(context, null);
    }

    private void m(Canvas canvas, float f10, float f11, float f12, float f13, TextPaint textPaint, int i10) {
        textPaint.setAlpha(i10);
        int save = canvas.save();
        canvas.clipRect(f10, 0.0f, f11, getHeight());
        canvas.drawText(this.H, f12, f13, textPaint);
        canvas.restoreToCount(save);
    }

    private void n(Canvas canvas, TextPaint textPaint) {
        int i10;
        int i11;
        int i12 = this.O;
        if (r()) {
            i10 = this.P;
            i11 = this.N;
        } else {
            i10 = this.N;
            i11 = this.P;
        }
        float measuredHeight = getMeasuredHeight() / 2.0f;
        float measuredWidth = ((getMeasuredWidth() - this.L) / 2.0f) + this.J;
        textPaint.setAlpha(i10);
        canvas.drawCircle(measuredWidth, measuredHeight, this.J, textPaint);
        float f10 = measuredWidth + (this.J * 2.0f) + this.K;
        textPaint.setAlpha(i12);
        canvas.drawCircle(f10, measuredHeight, this.J, textPaint);
        float f11 = f10 + (this.J * 2.0f) + this.K;
        textPaint.setAlpha(i11);
        canvas.drawCircle(f11, measuredHeight, this.J, textPaint);
    }

    private ValueAnimator o(float f10, float f11, long j10, long j11, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f10, f11);
        ofFloat.setDuration(j10);
        ofFloat.setStartDelay(j11);
        ofFloat.addUpdateListener(animatorUpdateListener);
        return ofFloat;
    }

    private void p() {
        b bVar = new b();
        ValueAnimator o10 = o(51.0f, 127.5f, 133L, 0L, bVar);
        ValueAnimator o11 = o(127.5f, 255.0f, 67L, 133L, bVar);
        ValueAnimator o12 = o(255.0f, 127.5f, 67L, 467L, bVar);
        ValueAnimator o13 = o(127.5f, 51.0f, 133L, 533L, bVar);
        c cVar = new c();
        ValueAnimator o14 = o(51.0f, 127.5f, 133L, 333L, cVar);
        ValueAnimator o15 = o(127.5f, 255.0f, 67L, 466L, cVar);
        ValueAnimator o16 = o(255.0f, 127.5f, 67L, 800L, cVar);
        ValueAnimator o17 = o(127.5f, 51.0f, 133L, 866L, cVar);
        d dVar = new d();
        ValueAnimator o18 = o(51.0f, 127.5f, 133L, 666L, dVar);
        ValueAnimator o19 = o(127.5f, 255.0f, 67L, 799L, dVar);
        ValueAnimator o20 = o(255.0f, 127.5f, 67L, 1133L, dVar);
        ValueAnimator o21 = o(127.5f, 51.0f, 133L, 1199L, dVar);
        AnimatorSet animatorSet = new AnimatorSet();
        this.Q = animatorSet;
        animatorSet.playTogether(o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21);
        this.Q.setInterpolator(new COUILinearInterpolator());
        this.Q.addListener(new e());
    }

    private void q() {
        addTextChangedListener(new a());
    }

    private boolean r() {
        return getLayoutDirection() == 1;
    }

    public int getButtonState() {
        return this.E;
    }

    public String getLoadingText() {
        return this.G;
    }

    public boolean getShowLoadingText() {
        return this.M;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        AnimatorSet animatorSet;
        super.onAttachedToWindow();
        if (this.E != 1 || (animatorSet = this.Q) == null || animatorSet.isRunning()) {
            return;
        }
        this.Q.start();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.E == 1) {
            this.Q.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.button.COUIButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        int i10;
        float measuredWidth;
        float f10;
        int i11;
        int i12;
        super.onDraw(canvas);
        if (this.E != 1 || getPaint() == null) {
            return;
        }
        TextPaint paint = getPaint();
        int alpha = paint.getAlpha();
        int save = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        if (this.M) {
            float measureText = paint.measureText(this.G);
            float measureText2 = paint.measureText(this.H);
            if (measureText + measureText2 > (getMeasuredWidth() - getPaddingStart()) - getPaddingEnd()) {
                n(canvas, paint);
                i10 = save;
            } else {
                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                float measuredHeight = ((getMeasuredHeight() + (fontMetrics.bottom - fontMetrics.top)) / 2.0f) - fontMetrics.bottom;
                int i13 = this.O;
                if (r()) {
                    measuredWidth = (((getMeasuredWidth() - measureText) - measureText2) / 2.0f) + measureText2;
                    i11 = this.P;
                    i12 = this.N;
                    f10 = ((getMeasuredWidth() - measureText) - measureText2) / 2.0f;
                } else {
                    measuredWidth = ((getMeasuredWidth() - measureText) - measureText2) / 2.0f;
                    f10 = measureText + measuredWidth;
                    i11 = this.N;
                    i12 = this.P;
                }
                canvas.drawText(this.G, measuredWidth, measuredHeight, paint);
                paint.getTextBounds(this.H, 0, 1, this.I);
                float f11 = f10;
                i10 = save;
                m(canvas, f10, this.I.right + f10, f11, measuredHeight, paint, i11);
                paint.getTextBounds(this.H, 0, 2, this.I);
                m(canvas, r0.right + f10, this.I.right + f10, f11, measuredHeight, paint, i13);
                m(canvas, this.I.right + f10, f10 + measureText2, f11, measuredHeight, paint, i12);
            }
        } else {
            i10 = save;
            n(canvas, paint);
        }
        paint.setAlpha(alpha);
        canvas.restoreToCount(i10);
    }

    public void setLoadingText(String str) {
        if (str == null || !this.M) {
            return;
        }
        this.G = str;
    }

    public void setOnLoadingStateChangeListener(f fVar) {
        this.R = fVar;
    }

    public void setShowLoadingText(boolean z10) {
        this.M = z10;
    }

    public COUILoadingButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.buttonStyle);
    }

    public COUILoadingButton(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.E = 0;
        this.G = "";
        this.I = new Rect();
        this.N = 51;
        this.O = 51;
        this.P = 51;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIButton, i10, 0);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUIButton_isShowLoadingText, false);
        this.M = z10;
        if (z10) {
            String string = obtainStyledAttributes.getString(R$styleable.COUIButton_loadingText);
            this.G = string;
            if (string == null) {
                this.G = "";
            }
        }
        obtainStyledAttributes.recycle();
        this.F = getText().toString();
        this.H = context.getString(R$string.loading_button_dots);
        float dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_loading_btn_circle_radius);
        this.J = dimensionPixelOffset;
        float dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(R$dimen.coui_loading_btn_circle_spacing);
        this.K = dimensionPixelOffset2;
        this.L = (dimensionPixelOffset * 6.0f) + (dimensionPixelOffset2 * 2.0f);
        q();
        p();
    }
}
