package com.coui.appcompat.progressbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import androidx.appcompat.widget.n0;
import androidx.core.graphics.ColorUtils;
import b3.COUITintUtil;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$string;
import com.support.appcompat.R$styleable;
import java.util.Locale;
import m1.COUIMoveEaseInterpolator;
import m2.COUIRoundRectUtil;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUIInstallLoadProgress extends COUILoadProgress {
    private static final int[] M0 = {R$attr.couiColorPrimary, R$attr.couiColorSecondary};
    private final String A;
    private float A0;
    private final boolean B;
    private boolean B0;
    private TextPaint C;
    private int C0;
    private String D;
    private float[] D0;
    private int E;
    private ValueAnimator E0;
    private int F;
    private ValueAnimator F0;
    private ColorStateList G;
    private Interpolator G0;
    private int H;
    private Interpolator H0;
    private String I;
    private int I0;
    private Paint.FontMetricsInt J;
    private Context J0;
    private int K;
    private boolean K0;
    private Paint L;
    private boolean L0;
    private int M;
    private boolean N;
    private Path O;
    private int P;
    private int Q;
    private float R;
    private int S;
    private int T;
    private Bitmap U;
    private Bitmap V;
    private Bitmap W;

    /* renamed from: a0, reason: collision with root package name */
    private Paint f7141a0;

    /* renamed from: b0, reason: collision with root package name */
    private Paint f7142b0;

    /* renamed from: c0, reason: collision with root package name */
    private Paint f7143c0;

    /* renamed from: d0, reason: collision with root package name */
    private Drawable f7144d0;

    /* renamed from: e0, reason: collision with root package name */
    private int f7145e0;

    /* renamed from: f0, reason: collision with root package name */
    private int f7146f0;

    /* renamed from: g0, reason: collision with root package name */
    private int f7147g0;

    /* renamed from: h0, reason: collision with root package name */
    private int f7148h0;

    /* renamed from: i0, reason: collision with root package name */
    private int f7149i0;

    /* renamed from: j0, reason: collision with root package name */
    private int f7150j0;

    /* renamed from: k0, reason: collision with root package name */
    private ColorStateList f7151k0;

    /* renamed from: l0, reason: collision with root package name */
    private int f7152l0;

    /* renamed from: m0, reason: collision with root package name */
    private ColorStateList f7153m0;

    /* renamed from: n0, reason: collision with root package name */
    private int f7154n0;

    /* renamed from: o0, reason: collision with root package name */
    private boolean f7155o0;

    /* renamed from: p0, reason: collision with root package name */
    private int f7156p0;

    /* renamed from: q0, reason: collision with root package name */
    private ColorStateList f7157q0;

    /* renamed from: r0, reason: collision with root package name */
    private int f7158r0;

    /* renamed from: s0, reason: collision with root package name */
    private float f7159s0;

    /* renamed from: t0, reason: collision with root package name */
    private float f7160t0;

    /* renamed from: u0, reason: collision with root package name */
    private float f7161u0;

    /* renamed from: v0, reason: collision with root package name */
    private Locale f7162v0;

    /* renamed from: w0, reason: collision with root package name */
    private int f7163w0;

    /* renamed from: x0, reason: collision with root package name */
    private int f7164x0;

    /* renamed from: y0, reason: collision with root package name */
    private int f7165y0;

    /* renamed from: z0, reason: collision with root package name */
    private int f7166z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.this.f7160t0 = ((Float) valueAnimator.getAnimatedValue("brightnessHolder")).floatValue();
            if (COUIInstallLoadProgress.this.L0 && ((float) valueAnimator.getCurrentPlayTime()) > ((float) valueAnimator.getDuration()) * 0.4f) {
                COUIInstallLoadProgress.this.L0 = false;
                COUIInstallLoadProgress.this.D(true);
                return;
            }
            float floatValue = ((Float) valueAnimator.getAnimatedValue("narrowHolderX")).floatValue();
            float floatValue2 = ((Float) valueAnimator.getAnimatedValue("narrowHolderY")).floatValue();
            float floatValue3 = ((Float) valueAnimator.getAnimatedValue("narrowHolderFont")).floatValue();
            if (floatValue < COUIInstallLoadProgress.this.getMeasuredWidth() * 0.005f && floatValue2 < COUIInstallLoadProgress.this.getMeasuredHeight() * 0.005f) {
                floatValue = COUIInstallLoadProgress.this.getMeasuredWidth() * 0.005f;
                floatValue2 = COUIInstallLoadProgress.this.getMeasuredHeight() * 0.005f;
            }
            COUIInstallLoadProgress.this.f7166z0 = (int) (floatValue + 0.5d);
            COUIInstallLoadProgress.this.f7165y0 = (int) (floatValue2 + 0.5d);
            COUIInstallLoadProgress.this.A0 = floatValue3;
            COUIInstallLoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.this.R = ((Float) valueAnimator.getAnimatedValue("circleRadiusHolder")).floatValue();
            COUIInstallLoadProgress.this.f7160t0 = ((Float) valueAnimator.getAnimatedValue("circleBrightnessHolder")).floatValue();
            if (COUIInstallLoadProgress.this.L0 && ((float) valueAnimator.getCurrentPlayTime()) > ((float) valueAnimator.getDuration()) * 0.4f) {
                COUIInstallLoadProgress.this.L0 = false;
                COUIInstallLoadProgress.this.D(true);
            } else {
                COUIInstallLoadProgress.this.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.this.f7160t0 = ((Float) valueAnimator.getAnimatedValue("brightnessHolder")).floatValue();
            float floatValue = ((Float) valueAnimator.getAnimatedValue("narrowHolderX")).floatValue();
            float floatValue2 = ((Float) valueAnimator.getAnimatedValue("narrowHolderY")).floatValue();
            COUIInstallLoadProgress.this.A0 = ((Float) valueAnimator.getAnimatedValue("narrowHolderFont")).floatValue();
            COUIInstallLoadProgress.this.f7166z0 = (int) (floatValue + 0.5d);
            COUIInstallLoadProgress.this.f7165y0 = (int) (floatValue2 + 0.5d);
            COUIInstallLoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f7170a;

        d(boolean z10) {
            this.f7170a = z10;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f7170a) {
                COUIInstallLoadProgress.super.performClick();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInstallLoadProgress.this.R = ((Float) valueAnimator.getAnimatedValue("circleRadiusHolder")).floatValue();
            COUIInstallLoadProgress.this.f7160t0 = ((Float) valueAnimator.getAnimatedValue("circleBrightnessHolder")).floatValue();
            COUIInstallLoadProgress.this.S = ((Integer) valueAnimator.getAnimatedValue("circleInAlphaHolder")).intValue();
            COUIInstallLoadProgress.this.T = ((Integer) valueAnimator.getAnimatedValue("circleOutAlphaHolder")).intValue();
            COUIInstallLoadProgress.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends AnimatorListenerAdapter {
        f() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIInstallLoadProgress.super.performClick();
        }
    }

    public COUIInstallLoadProgress(Context context) {
        this(context, null);
    }

    private void A(Canvas canvas, float f10, float f11, float f12, float f13, boolean z10, float f14, float f15) {
        canvas.translate(f14, f15);
        RectF rectF = new RectF(f10, f11, f12, f13);
        this.L.setColor(this.f7151k0 == null ? t(this.f7149i0) : this.f7152l0);
        if (!z10) {
            this.L.setColor(this.f7153m0 == null ? t(this.f7150j0) : this.f7154n0);
        }
        Path c10 = COUIRoundRectUtil.a().c(rectF, ((f13 - f11) / 2.0f) - this.f7159s0);
        this.O = c10;
        canvas.drawPath(c10, this.L);
        canvas.translate(-f14, -f15);
    }

    private void B(Canvas canvas, float f10, float f11, float f12, float f13) {
        if (this.D != null) {
            this.C.setTextSize(this.E * this.A0);
            float measureText = this.C.measureText(this.D);
            float f14 = this.H + (((f12 - measureText) - (r1 * 2)) / 2.0f);
            Paint.FontMetricsInt fontMetricsInt = this.J;
            int i10 = fontMetricsInt.bottom;
            float f15 = ((f13 - (i10 - r0)) / 2.0f) - fontMetricsInt.top;
            canvas.drawText(this.D, f14, f15, this.C);
            if (this.N) {
                this.C.setColor(this.f7158r0);
                canvas.save();
                if (!n0.b(this)) {
                    canvas.clipRect(f10, f11, this.M, f13);
                } else {
                    canvas.clipRect(f12 - this.M, f11, f12, f13);
                }
                canvas.drawText(this.D, f14, f15, this.C);
                canvas.restore();
                this.N = false;
            }
        }
    }

    private void C() {
        if (this.K0) {
            performHapticFeedback(302);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D(boolean z10) {
        C();
        if (this.B0) {
            q(false);
            if (this.L0) {
                return;
            }
            int i10 = this.f7148h0;
            if (i10 == 0 || i10 == 1) {
                ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat("brightnessHolder", this.f7160t0, 1.0f), PropertyValuesHolder.ofFloat("narrowHolderX", this.f7166z0, 0.0f), PropertyValuesHolder.ofFloat("narrowHolderY", this.f7165y0, 0.0f), PropertyValuesHolder.ofFloat("narrowHolderFont", this.A0, 1.0f));
                this.F0 = ofPropertyValuesHolder;
                ofPropertyValuesHolder.setInterpolator(this.H0);
                this.F0.setDuration(340L);
                this.F0.addUpdateListener(new c());
                this.F0.addListener(new d(z10));
                this.F0.start();
            } else if (i10 == 2) {
                ValueAnimator ofPropertyValuesHolder2 = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat("circleRadiusHolder", this.R, this.Q), PropertyValuesHolder.ofFloat("circleBrightnessHolder", this.f7160t0, 1.0f), PropertyValuesHolder.ofInt("circleInAlphaHolder", 0, 255), PropertyValuesHolder.ofInt("circleOutAlphaHolder", 255, 0));
                this.F0 = ofPropertyValuesHolder2;
                ofPropertyValuesHolder2.setInterpolator(this.H0);
                this.F0.setDuration(340L);
                this.F0.addUpdateListener(new e());
                this.F0.addListener(new f());
                this.F0.start();
            }
            this.B0 = false;
        }
    }

    private void E() {
        if (this.B0) {
            return;
        }
        q(true);
        int i10 = this.f7148h0;
        if (i10 == 0 || i10 == 1) {
            ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat("brightnessHolder", 1.0f, this.f7161u0), PropertyValuesHolder.ofFloat("narrowHolderX", 0.0f, getMeasuredWidth() * 0.05f), PropertyValuesHolder.ofFloat("narrowHolderY", 0.0f, getMeasuredHeight() * 0.05f), PropertyValuesHolder.ofFloat("narrowHolderFont", 1.0f, 0.92f));
            this.E0 = ofPropertyValuesHolder;
            ofPropertyValuesHolder.setInterpolator(this.G0);
            this.E0.setDuration(200L);
            this.E0.addUpdateListener(new a());
            this.E0.start();
        } else if (i10 == 2) {
            ValueAnimator ofPropertyValuesHolder2 = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat("circleRadiusHolder", this.R, this.Q * 0.9f), PropertyValuesHolder.ofFloat("circleBrightnessHolder", this.f7160t0, this.f7161u0));
            this.E0 = ofPropertyValuesHolder2;
            ofPropertyValuesHolder2.setInterpolator(this.G0);
            this.E0.setDuration(200L);
            this.E0.addUpdateListener(new b());
            this.E0.start();
        }
        this.B0 = true;
    }

    private void a() {
        if (this.f7148h0 == 2) {
            return;
        }
        TextPaint textPaint = new TextPaint(1);
        this.C = textPaint;
        textPaint.setAntiAlias(true);
        int i10 = this.F;
        if (i10 == 0) {
            i10 = this.E;
        }
        int i11 = this.f7163w0;
        this.f7164x0 = i11;
        if (i11 == -1) {
            this.f7164x0 = this.G.getColorForState(getDrawableState(), COUIContextUtil.b(getContext(), R$attr.couiDefaultTextColor, 0));
        }
        this.C.setTextSize(i10);
        COUIChangeTextUtil.a(this.C, true);
        this.J = this.C.getFontMetricsInt();
        p();
    }

    private void p() {
        String v7 = v(this.D, this.f7146f0);
        if (v7.length() <= 0 || v7.length() >= this.D.length()) {
            return;
        }
        this.D = x(v(v7, (this.f7146f0 - (this.H * 2)) - ((int) this.C.measureText(this.I)))) + this.I;
    }

    private void q(boolean z10) {
        ValueAnimator valueAnimator = this.E0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            boolean z11 = !z10 && ((float) this.E0.getCurrentPlayTime()) < ((float) this.E0.getDuration()) * 0.4f;
            this.L0 = z11;
            if (!z11) {
                this.E0.cancel();
            }
        }
        ValueAnimator valueAnimator2 = this.F0;
        if (valueAnimator2 == null || !valueAnimator2.isRunning()) {
            return;
        }
        this.F0.cancel();
    }

    private int r(Context context, float f10) {
        return (int) ((f10 * context.getResources().getDisplayMetrics().density) + 0.5d);
    }

    private Bitmap s(int i10) {
        Drawable drawable = getContext().getDrawable(i10);
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    private int t(int i10) {
        if (!isEnabled()) {
            return this.C0;
        }
        ColorUtils.f(i10, this.D0);
        float[] fArr = this.D0;
        fArr[2] = fArr[2] * this.f7160t0;
        int a10 = ColorUtils.a(fArr);
        int red = Color.red(a10);
        int green = Color.green(a10);
        int blue = Color.blue(a10);
        int alpha = Color.alpha(i10);
        if (red > 255) {
            red = 255;
        }
        if (green > 255) {
            green = 255;
        }
        if (blue > 255) {
            blue = 255;
        }
        return Color.argb(alpha, red, green, blue);
    }

    private int u(int i10, float f10, boolean z10) {
        return i10 - (z10 ? r(getContext(), f10) : r(getContext(), f10) * 2);
    }

    private String v(String str, int i10) {
        int breakText = this.C.breakText(str, true, i10, null);
        return (breakText == 0 || breakText == str.length()) ? str : str.substring(0, breakText - 1);
    }

    private static boolean w(String str) {
        int i10 = 0;
        for (int i11 = 0; i11 < str.length(); i11++) {
            if (Character.toString(str.charAt(i11)).matches("^[一-龥]{1}$")) {
                i10++;
            }
        }
        return i10 > 0;
    }

    private String x(String str) {
        int lastIndexOf;
        return (w(str) || (lastIndexOf = str.lastIndexOf(32)) <= 0) ? str : str.substring(0, lastIndexOf);
    }

    private boolean y(Locale locale) {
        return "zh".equalsIgnoreCase(locale.getLanguage());
    }

    private void z(Canvas canvas, float f10, float f11, boolean z10, Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap.isRecycled() || bitmap2 == null || bitmap2.isRecycled()) {
            return;
        }
        this.f7141a0.setColor(this.f7151k0 == null ? t(this.f7149i0) : this.f7152l0);
        if (!z10) {
            this.f7141a0.setColor(this.f7153m0 == null ? t(this.f7150j0) : this.f7154n0);
        }
        float f12 = this.R;
        Path c10 = COUIRoundRectUtil.a().c(new RectF(f10 - f12, f11 - f12, f10 + f12, f11 + f12), this.K);
        this.O = c10;
        canvas.drawPath(c10, this.f7141a0);
        int width = (this.f7145e0 - bitmap.getWidth()) / 2;
        int height = (this.f7147g0 - bitmap.getHeight()) / 2;
        this.f7142b0.setAlpha(this.S);
        this.f7143c0.setAlpha(this.T);
        float f13 = width;
        float f14 = height;
        canvas.drawBitmap(bitmap, f13, f14, this.f7142b0);
        canvas.drawBitmap(bitmap2, f13, f14, this.f7143c0);
        canvas.save();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.progressbar.COUILoadProgress, androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return ProgressBar.class.getName();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f7148h0 == 2) {
            Bitmap bitmap = this.U;
            if (bitmap == null || bitmap.isRecycled()) {
                Bitmap s7 = s(R$drawable.coui_install_load_progress_circle_load);
                this.U = s7;
                this.U = COUITintUtil.a(s7, this.f7151k0 == null ? this.f7149i0 : this.f7152l0);
            }
            Bitmap bitmap2 = this.V;
            if (bitmap2 == null || bitmap2.isRecycled()) {
                this.V = s(R$drawable.coui_install_load_progress_circle_reload);
            }
            Bitmap bitmap3 = this.W;
            if (bitmap3 == null || bitmap3.isRecycled()) {
                this.W = s(R$drawable.coui_install_load_progress_circle_pause);
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Locale locale = Locale.getDefault();
        if (this.f7148h0 != 0 || this.f7162v0.getLanguage().equalsIgnoreCase(locale.getLanguage())) {
            return;
        }
        this.f7162v0 = locale;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_install_download_progress_width_in_foreign_language);
        if (!y(this.f7162v0)) {
            this.f7145e0 += dimensionPixelSize;
            this.f7146f0 += dimensionPixelSize;
        } else {
            this.f7145e0 -= dimensionPixelSize;
            this.f7146f0 -= dimensionPixelSize;
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.progressbar.COUILoadProgress, android.view.View
    public void onDetachedFromWindow() {
        if (this.f7148h0 == 2) {
            Bitmap bitmap = this.U;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.U.recycle();
            }
            Bitmap bitmap2 = this.W;
            if (bitmap2 != null && !bitmap2.isRecycled()) {
                this.W.recycle();
            }
            Bitmap bitmap3 = this.V;
            if (bitmap3 != null && !bitmap3.isRecycled()) {
                this.V.recycle();
            }
        }
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.progressbar.COUILoadProgress, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        boolean z10;
        float f10;
        int i10;
        super.onDraw(canvas);
        float f11 = this.f7166z0;
        float f12 = this.f7165y0;
        float width = getWidth() - this.f7166z0;
        float height = getHeight() - this.f7165y0;
        int i11 = this.f7182k;
        if (i11 == 3) {
            if (this.f7148h0 == 2) {
                z(canvas, (float) ((this.f7145e0 * 1.0d) / 2.0d), (float) ((this.f7147g0 * 1.0d) / 2.0d), true, this.V, this.W);
                return;
            }
            A(canvas, f11, f12, width, height, true, 0.0f, 0.0f);
            this.C.setColor(this.f7155o0 ? this.f7156p0 : this.f7158r0);
            this.N = false;
            B(canvas, f11, f12, this.f7145e0, this.f7147g0);
            return;
        }
        if (i11 == 0) {
            int i12 = this.f7148h0;
            if (i12 == 2) {
                z(canvas, (float) ((this.f7145e0 * 1.0d) / 2.0d), (float) ((this.f7147g0 * 1.0d) / 2.0d), false, this.U, this.W);
                z10 = true;
            } else if (i12 == 1) {
                z10 = true;
                A(canvas, f11, f12, width, height, true, 0.0f, 0.0f);
            } else {
                z10 = true;
                A(canvas, f11, f12, width, height, false, 0.0f, 0.0f);
            }
            int i13 = this.f7148h0;
            if (i13 == z10) {
                this.C.setColor(this.f7155o0 ? this.f7156p0 : this.f7158r0);
            } else if (i13 == 0) {
                this.C.setColor(this.f7157q0 == null ? this.f7149i0 : this.f7156p0);
            }
        } else {
            z10 = true;
        }
        int i14 = this.f7182k;
        if (i14 == z10 || i14 == 2) {
            if (this.f7148h0 != 2) {
                if (this.f7185n) {
                    f10 = this.f7186o;
                    i10 = this.f7184m;
                } else {
                    f10 = this.f7183l;
                    i10 = this.f7184m;
                }
                this.M = (int) ((f10 / i10) * this.f7145e0);
                A(canvas, f11, f12, width, height, false, 0.0f, 0.0f);
                canvas.save();
                if (!n0.b(this)) {
                    canvas.clipRect(f11, f12, this.M, this.f7147g0);
                } else {
                    canvas.translate(0.0f, 0.0f);
                    canvas.clipRect((width - this.M) + 0.0f, f12, width, this.f7147g0);
                    canvas.translate(-0.0f, 0.0f);
                }
                if (this.f7148h0 != 2) {
                    A(canvas, f11, f12, width, height, true, 0.0f, 0.0f);
                    canvas.restore();
                }
                this.N = z10;
                this.C.setColor(this.f7157q0 == null ? this.f7149i0 : this.f7156p0);
            } else if (i14 == z10) {
                z(canvas, (float) ((this.f7145e0 * 1.0d) / 2.0d), (float) ((this.f7147g0 * 1.0d) / 2.0d), true, this.W, this.V);
            } else if (i14 == 2) {
                z(canvas, (float) ((this.f7145e0 * 1.0d) / 2.0d), (float) ((this.f7147g0 * 1.0d) / 2.0d), true, this.V, this.W);
            }
        }
        if (this.f7148h0 != 2) {
            B(canvas, f11, f12, this.f7145e0, this.f7147g0);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setItemCount(this.f7184m);
        accessibilityEvent.setCurrentItemIndex(this.f7183l);
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        String str;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        int i10 = this.f7182k;
        if ((i10 == 0 || i10 == 3 || i10 == 2) && (str = this.D) != null) {
            accessibilityNodeInfo.setContentDescription(str);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i10, int i11) {
        setMeasuredDimension(this.f7145e0, this.f7147g0);
        a();
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            C();
            E();
        } else if (action == 1) {
            D(true);
        } else if (action == 3) {
            D(false);
        }
        return true;
    }

    @Deprecated
    public void setBtnTextColor(int i10) {
        this.f7156p0 = i10;
        this.f7155o0 = true;
        invalidate();
    }

    public void setBtnTextColorStateList(ColorStateList colorStateList) {
        this.f7157q0 = colorStateList;
        if (colorStateList == null) {
            setBtnTextColor(-1);
        } else {
            setBtnTextColor(colorStateList.getDefaultColor());
        }
    }

    public void setDefaultTextSize(int i10) {
        this.E = i10;
    }

    public void setDisabledColor(int i10) {
        this.C0 = i10;
    }

    public void setLoadStyle(int i10) {
        if (i10 == 2) {
            this.f7148h0 = 2;
            Paint paint = new Paint(1);
            this.f7141a0 = paint;
            paint.setAntiAlias(true);
            Paint paint2 = new Paint(1);
            this.f7142b0 = paint2;
            paint2.setAntiAlias(true);
            Paint paint3 = new Paint(1);
            this.f7143c0 = paint3;
            paint3.setAntiAlias(true);
            this.U = s(R$drawable.coui_install_load_progress_circle_load);
            this.V = s(R$drawable.coui_install_load_progress_circle_reload);
            this.W = s(R$drawable.coui_install_load_progress_circle_pause);
            int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_install_download_progress_default_circle_radius);
            this.P = dimensionPixelSize;
            int u7 = u(dimensionPixelSize, 1.5f, true);
            this.Q = u7;
            this.R = u7;
            return;
        }
        this.f7148h0 = i10;
        this.L = new Paint(1);
    }

    public void setMaxBrightness(int i10) {
        this.f7161u0 = i10;
    }

    public void setText(String str) {
        if (str.equals(this.D)) {
            return;
        }
        this.D = str;
        if (this.C != null) {
            p();
        }
        invalidate();
    }

    @Override // android.widget.TextView
    public void setTextColor(int i10) {
        if (i10 != 0) {
            this.f7163w0 = i10;
        }
    }

    public void setTextId(int i10) {
        setText(getResources().getString(i10));
    }

    public void setTextPadding(int i10) {
        this.H = i10;
    }

    public void setTextSize(int i10) {
        if (i10 != 0) {
            this.F = i10;
        }
    }

    @Deprecated
    public void setThemeColor(int i10) {
        this.f7152l0 = i10;
        Bitmap bitmap = this.U;
        if (bitmap == null || bitmap.isRecycled()) {
            this.U = s(R$drawable.coui_install_load_progress_circle_load);
        }
        this.U = COUITintUtil.a(this.U, this.f7152l0);
        invalidate();
    }

    public void setThemeColorStateList(ColorStateList colorStateList) {
        this.f7151k0 = colorStateList;
        if (colorStateList == null) {
            setThemeColor(-1);
        } else {
            setThemeColor(colorStateList.getDefaultColor());
        }
    }

    @Deprecated
    public void setThemeSecondaryColor(int i10) {
        this.f7154n0 = i10;
        invalidate();
    }

    public void setThemeSecondaryColorStateList(ColorStateList colorStateList) {
        this.f7153m0 = colorStateList;
        if (colorStateList == null) {
            setThemeSecondaryColor(-1);
        } else {
            setThemeSecondaryColor(colorStateList.getDefaultColor());
        }
    }

    public void setTouchModeHeight(int i10) {
        this.f7147g0 = i10;
    }

    public void setTouchModeWidth(int i10) {
        this.f7145e0 = i10;
    }

    public COUIInstallLoadProgress(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiInstallLoadProgressStyle);
    }

    public COUIInstallLoadProgress(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.A = "COUIInstallLoadProgress";
        this.B = true;
        this.C = null;
        this.F = 0;
        this.H = 0;
        this.I = null;
        this.J = null;
        this.K = 0;
        this.L = null;
        this.M = 0;
        this.N = false;
        this.P = 0;
        this.Q = 0;
        this.R = 0.0f;
        this.S = 255;
        this.T = 0;
        this.f7141a0 = null;
        this.f7142b0 = null;
        this.f7143c0 = null;
        this.f7144d0 = null;
        this.f7148h0 = 0;
        this.f7155o0 = false;
        this.f7160t0 = 1.0f;
        this.f7163w0 = -1;
        this.f7165y0 = 0;
        this.f7166z0 = 0;
        this.A0 = 1.0f;
        this.D0 = new float[3];
        COUIDarkModeUtil.b(this, false);
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.I0 = attributeSet.getStyleAttribute();
        } else {
            this.I0 = i10;
        }
        this.J0 = context;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(M0);
        this.f7149i0 = obtainStyledAttributes.getColor(0, 0);
        this.f7150j0 = obtainStyledAttributes.getColor(1, 0);
        obtainStyledAttributes.recycle();
        this.f7162v0 = Locale.getDefault();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUILoadProgress, i10, 0);
        this.f7158r0 = getResources().getColor(R$color.coui_install_load_progress_text_color_in_progress);
        this.K0 = obtainStyledAttributes2.getBoolean(R$styleable.COUILoadProgress_loadingButtonNeedVibrate, false);
        Drawable drawable = obtainStyledAttributes2.getDrawable(R$styleable.COUILoadProgress_couiDefaultDrawable);
        if (drawable != null) {
            setButtonDrawable(drawable);
        }
        setState(obtainStyledAttributes2.getInteger(R$styleable.COUILoadProgress_couiState, 0));
        obtainStyledAttributes2.recycle();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_install_download_progress_textsize);
        TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(attributeSet, R$styleable.COUIInstallLoadProgress, i10, 0);
        setLoadStyle(obtainStyledAttributes3.getInteger(R$styleable.COUIInstallLoadProgress_couiStyle, 0));
        this.f7144d0 = obtainStyledAttributes3.getDrawable(R$styleable.COUIInstallLoadProgress_couiInstallGiftBg);
        this.f7147g0 = obtainStyledAttributes3.getDimensionPixelSize(R$styleable.COUIInstallLoadProgress_couiInstallViewHeight, 0);
        int dimensionPixelOffset = obtainStyledAttributes3.getDimensionPixelOffset(R$styleable.COUIInstallLoadProgress_couiInstallViewWidth, 0);
        this.f7145e0 = dimensionPixelOffset;
        this.f7146f0 = u(dimensionPixelOffset, 1.5f, false);
        this.f7161u0 = obtainStyledAttributes3.getFloat(R$styleable.COUIInstallLoadProgress_brightness, 0.8f);
        this.C0 = obtainStyledAttributes3.getColor(R$styleable.COUIInstallLoadProgress_disabledColor, 0);
        this.G0 = new COUIMoveEaseInterpolator();
        this.H0 = new COUIMoveEaseInterpolator();
        int i11 = this.f7148h0;
        if (i11 != 2) {
            if (i11 == 1) {
                this.K = getResources().getDimensionPixelSize(R$dimen.coui_install_download_progress_round_border_radius);
            } else {
                this.K = getResources().getDimensionPixelSize(R$dimen.coui_install_download_progress_round_border_radius_small);
                if (!y(this.f7162v0)) {
                    int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.coui_install_download_progress_width_in_foreign_language);
                    this.f7145e0 += dimensionPixelSize2;
                    this.f7146f0 += dimensionPixelSize2;
                }
            }
            this.G = obtainStyledAttributes3.getColorStateList(R$styleable.COUIInstallLoadProgress_couiInstallDefaultColor);
            this.H = obtainStyledAttributes3.getDimensionPixelOffset(R$styleable.COUIInstallLoadProgress_couiInstallPadding, 0);
            this.D = obtainStyledAttributes3.getString(R$styleable.COUIInstallLoadProgress_couiInstallTextview);
            this.E = obtainStyledAttributes3.getDimensionPixelSize(R$styleable.COUIInstallLoadProgress_couiInstallTextsize, dimensionPixelSize);
            this.E = (int) COUIChangeTextUtil.e(this.E, getResources().getConfiguration().fontScale, 2);
            if (this.I == null) {
                this.I = getResources().getString(R$string.coui_install_load_progress_apostrophe);
            }
        } else {
            this.K = getResources().getDimensionPixelSize(R$dimen.coui_install_download_progress_circle_round_border_radius);
        }
        setThemeColorStateList(obtainStyledAttributes3.getColorStateList(R$styleable.COUIInstallLoadProgress_couiThemeColor));
        setThemeSecondaryColorStateList(obtainStyledAttributes3.getColorStateList(R$styleable.COUIInstallLoadProgress_couiThemeColorSecondary));
        setBtnTextColorStateList(obtainStyledAttributes3.getColorStateList(R$styleable.COUIInstallLoadProgress_couiThemeTextColor));
        obtainStyledAttributes3.recycle();
        this.f7159s0 = getResources().getDimension(R$dimen.coui_install_download_progress_round_border_radius_offset);
    }
}
