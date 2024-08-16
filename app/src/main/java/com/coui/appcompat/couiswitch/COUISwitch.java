package com.coui.appcompat.couiswitch;

import android.R;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.Switch;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$bool;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$raw;
import com.support.appcompat.R$string;
import com.support.appcompat.R$styleable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import m1.COUILinearInterpolator;
import t2.COUISoundLoadUtil;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUISwitch extends SwitchCompat {
    private int A0;
    private RectF B0;
    private RectF C0;
    private int D0;
    private int E0;
    private float F0;
    private float G0;
    private int H0;
    private int I0;
    private boolean J0;
    private float K0;
    private Paint L0;
    private Paint M0;
    private int N0;
    private int O0;
    private int P0;
    private int Q0;
    private int R0;
    private int S0;
    private int T0;
    private int U0;
    private int V0;
    private int W0;
    private int X0;
    private int Y0;
    private int Z0;

    /* renamed from: a1, reason: collision with root package name */
    private int f5634a1;

    /* renamed from: b0, reason: collision with root package name */
    private COUISoundLoadUtil f5635b0;

    /* renamed from: b1, reason: collision with root package name */
    private int f5636b1;

    /* renamed from: c0, reason: collision with root package name */
    private int f5637c0;

    /* renamed from: c1, reason: collision with root package name */
    private int f5638c1;

    /* renamed from: d0, reason: collision with root package name */
    private int f5639d0;

    /* renamed from: d1, reason: collision with root package name */
    private boolean f5640d1;

    /* renamed from: e0, reason: collision with root package name */
    private boolean f5641e0;

    /* renamed from: e1, reason: collision with root package name */
    private boolean f5642e1;

    /* renamed from: f0, reason: collision with root package name */
    private boolean f5643f0;

    /* renamed from: f1, reason: collision with root package name */
    private ExecutorService f5644f1;

    /* renamed from: g0, reason: collision with root package name */
    private boolean f5645g0;

    /* renamed from: h0, reason: collision with root package name */
    private boolean f5646h0;

    /* renamed from: i0, reason: collision with root package name */
    private String f5647i0;

    /* renamed from: j0, reason: collision with root package name */
    private String f5648j0;

    /* renamed from: k0, reason: collision with root package name */
    private String f5649k0;

    /* renamed from: l0, reason: collision with root package name */
    private b f5650l0;

    /* renamed from: m0, reason: collision with root package name */
    private AccessibilityManager f5651m0;

    /* renamed from: n0, reason: collision with root package name */
    private AnimatorSet f5652n0;

    /* renamed from: o0, reason: collision with root package name */
    private AnimatorSet f5653o0;

    /* renamed from: p0, reason: collision with root package name */
    private AnimatorSet f5654p0;

    /* renamed from: q0, reason: collision with root package name */
    private AnimatorSet f5655q0;

    /* renamed from: r0, reason: collision with root package name */
    private float f5656r0;

    /* renamed from: s0, reason: collision with root package name */
    private float f5657s0;

    /* renamed from: t0, reason: collision with root package name */
    private float f5658t0;

    /* renamed from: u0, reason: collision with root package name */
    private Drawable f5659u0;

    /* renamed from: v0, reason: collision with root package name */
    private Drawable f5660v0;

    /* renamed from: w0, reason: collision with root package name */
    private Drawable f5661w0;

    /* renamed from: x0, reason: collision with root package name */
    private Drawable f5662x0;

    /* renamed from: y0, reason: collision with root package name */
    private Drawable f5663y0;

    /* renamed from: z0, reason: collision with root package name */
    private Drawable f5664z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Thread.sleep(263L);
            } catch (InterruptedException e10) {
                e10.printStackTrace();
            }
            if (COUISwitch.this.f5655q0 == null || !COUISwitch.this.f5655q0.isRunning()) {
                return;
            }
            COUISwitch.this.performHapticFeedback(302);
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        void a();
    }

    public COUISwitch(Context context) {
        this(context, null);
    }

    private void A() {
        E();
        F();
        G();
    }

    private void B(TypedArray typedArray, Context context) {
        this.f5659u0 = typedArray.getDrawable(R$styleable.COUISwitch_loadingDrawable);
        this.f5660v0 = typedArray.getDrawable(R$styleable.COUISwitch_themedLoadingDrawable);
        this.f5661w0 = typedArray.getDrawable(R$styleable.COUISwitch_themedLoadingCheckedBackground);
        this.f5662x0 = typedArray.getDrawable(R$styleable.COUISwitch_themedLoadingUncheckedBackground);
        this.f5663y0 = typedArray.getDrawable(R$styleable.COUISwitch_themedCheckedDrawable);
        this.f5664z0 = typedArray.getDrawable(R$styleable.COUISwitch_themedUncheckedDrawable);
        this.I0 = typedArray.getDimensionPixelSize(R$styleable.COUISwitch_barHeight, 0);
        this.H0 = typedArray.getDimensionPixelSize(R$styleable.COUISwitch_outerCircleStrokeWidth, 0);
        this.N0 = typedArray.getDimensionPixelOffset(R$styleable.COUISwitch_outerCircleWidth, 0);
        this.O0 = typedArray.getDimensionPixelSize(R$styleable.COUISwitch_innerCircleWidth, 0);
        this.P0 = typedArray.getDimensionPixelSize(R$styleable.COUISwitch_circlePadding, 0);
        this.R0 = typedArray.getColor(R$styleable.COUISwitch_innerCircleColor, 0);
        this.S0 = typedArray.getColor(R$styleable.COUISwitch_outerCircleColor, 0);
        this.U0 = typedArray.getColor(R$styleable.COUISwitch_innerCircleUncheckedDisabledColor, 0);
        this.T0 = typedArray.getColor(R$styleable.COUISwitch_outerUnCheckedCircleColor, 0);
        this.V0 = typedArray.getColor(R$styleable.COUISwitch_innerCircleCheckedDisabledColor, 0);
        this.W0 = typedArray.getColor(R$styleable.COUISwitch_outerCircleUncheckedDisabledColor, 0);
        this.X0 = typedArray.getColor(R$styleable.COUISwitch_outerCircleCheckedDisabledColor, 0);
        this.f5636b1 = typedArray.getColor(R$styleable.COUISwitch_barUncheckedDisabledColor, COUIContextUtil.a(context, R$attr.couiColorPrimary) & 1308622847);
        this.J0 = getContext().getResources().getBoolean(R$bool.coui_switch_theme_enable);
    }

    private void C() {
        this.L0 = new Paint(1);
        this.L0.setShadowLayer(8.0f, 0.0f, 4.0f, Color.argb(25, 0, 0, 0));
        this.M0 = new Paint(1);
    }

    private void D(Context context) {
        this.Q0 = context.getResources().getDimensionPixelSize(R$dimen.coui_switch_padding);
        COUISoundLoadUtil a10 = COUISoundLoadUtil.a();
        this.f5635b0 = a10;
        this.f5637c0 = a10.c(context, R$raw.coui_switch_sound_on);
        this.f5639d0 = this.f5635b0.c(context, R$raw.coui_switch_sound_off);
        this.f5647i0 = getResources().getString(R$string.switch_on);
        this.f5648j0 = getResources().getString(R$string.switch_off);
        this.f5649k0 = getResources().getString(R$string.switch_loading);
        this.E0 = (getSwitchMinWidth() - (this.P0 * 2)) - this.N0;
        this.Y0 = COUIContextUtil.a(context, R$attr.couiColorPrimary);
        this.Z0 = COUIContextUtil.a(context, R$attr.couiColorDivider);
        this.f5634a1 = isChecked() ? this.Y0 : this.Z0;
        this.f5638c1 = COUIContextUtil.d(context, R$color.coui_color_press_background);
        setTrackTintMode(PorterDuff.Mode.SRC);
    }

    private void E() {
        Interpolator a10 = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);
        this.f5652n0 = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "circleScale", 1.0f, 0.0f);
        ofFloat.setInterpolator(a10);
        ofFloat.setDuration(433L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "loadingScale", 0.5f, 1.0f);
        ofFloat2.setInterpolator(a10);
        ofFloat2.setDuration(550L);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, "loadingAlpha", 0.0f, 1.0f);
        ofFloat3.setInterpolator(a10);
        ofFloat3.setDuration(550L);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this, "loadingRotation", 0.0f, 360.0f);
        ofFloat4.setRepeatCount(-1);
        ofFloat4.setDuration(800L);
        ofFloat4.setInterpolator(new COUILinearInterpolator());
        this.f5652n0.play(ofFloat).with(ofFloat3).with(ofFloat2).with(ofFloat4);
    }

    private void F() {
        Interpolator a10 = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);
        this.f5653o0 = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "loadingAlpha", 1.0f, 0.0f);
        ofFloat.setInterpolator(a10);
        ofFloat.setDuration(100L);
        this.f5653o0.play(ofFloat);
    }

    private void G() {
        this.f5654p0 = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "loadingRotation", 0.0f, 360.0f);
        ofFloat.setRepeatCount(-1);
        ofFloat.setDuration(800L);
        ofFloat.setInterpolator(new COUILinearInterpolator());
        this.f5654p0.play(ofFloat);
    }

    private boolean I() {
        return getLayoutDirection() == 1;
    }

    private void K() {
        if (J()) {
            if (this.f5644f1 == null) {
                this.f5644f1 = Executors.newSingleThreadExecutor();
            }
            this.f5644f1.execute(new a());
            setTactileFeedbackEnabled(false);
        }
    }

    private void L(boolean z10) {
        this.f5635b0.d(getContext(), z10 ? this.f5637c0 : this.f5639d0, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    private void O() {
        RectF rectF = this.B0;
        float f10 = rectF.left;
        int i10 = this.H0;
        this.C0.set(f10 + i10, rectF.top + i10, rectF.right - i10, rectF.bottom - i10);
    }

    private void P() {
        float f10;
        float f11;
        float f12;
        float f13;
        if (isChecked()) {
            if (I()) {
                f10 = this.P0 + this.D0 + this.Q0;
                f11 = this.N0;
                f12 = this.F0;
                f13 = (f11 * f12) + f10;
            } else {
                f13 = ((getSwitchMinWidth() - this.P0) - (this.E0 - this.D0)) + this.Q0;
                f10 = f13 - (this.N0 * this.F0);
            }
        } else if (I()) {
            int switchMinWidth = (getSwitchMinWidth() - this.P0) - (this.E0 - this.D0);
            int i10 = this.Q0;
            float f14 = switchMinWidth + i10;
            float f15 = i10 + (f14 - (this.N0 * this.F0));
            f13 = f14;
            f10 = f15;
        } else {
            f10 = this.P0 + this.D0 + this.Q0;
            f11 = this.N0;
            f12 = this.F0;
            f13 = (f11 * f12) + f10;
        }
        int i11 = this.I0;
        float f16 = ((i11 - r3) / 2.0f) + this.Q0;
        this.B0.set(f10, f16, f13, this.N0 + f16);
    }

    private int getBarColor() {
        return this.f5634a1;
    }

    private void s(boolean z10) {
        int i10;
        if (this.f5655q0 == null) {
            this.f5655q0 = new AnimatorSet();
        }
        Interpolator a10 = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);
        int i11 = this.D0;
        if (I()) {
            if (!z10) {
                i10 = this.E0;
            }
            i10 = 0;
        } else {
            if (z10) {
                i10 = this.E0;
            }
            i10 = 0;
        }
        this.f5655q0.setInterpolator(a10);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "circleScaleX", 1.0f, 1.3f);
        ofFloat.setDuration(133L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "circleScaleX", 1.3f, 1.0f);
        ofFloat2.setStartDelay(133L);
        ofFloat2.setDuration(250L);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "circleTranslation", i11, i10);
        ofInt.setDuration(383L);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, "innerCircleAlpha", this.K0, z10 ? 0.0f : 1.0f);
        ofFloat3.setDuration(100L);
        ObjectAnimator ofArgb = ObjectAnimator.ofArgb(this, "barColor", getBarColor(), z10 ? this.Y0 : this.Z0);
        ofArgb.setDuration(450L);
        this.f5655q0.play(ofFloat).with(ofFloat2).with(ofInt).with(ofFloat3).with(ofArgb);
        this.f5655q0.start();
    }

    private void setBarColor(int i10) {
        this.f5634a1 = i10;
        invalidate();
    }

    private Drawable t() {
        return H() ? isChecked() ? this.f5661w0 : this.f5662x0 : isChecked() ? this.f5663y0 : this.f5664z0;
    }

    private void u() {
        Drawable trackDrawable = getTrackDrawable();
        if (trackDrawable != null) {
            if (isEnabled()) {
                trackDrawable.setTint(this.f5634a1);
            } else {
                trackDrawable.setTint(isChecked() ? this.f5636b1 : this.f5638c1);
            }
        }
    }

    private void v(Canvas canvas) {
        if (this.f5645g0) {
            canvas.save();
            float f10 = this.f5656r0;
            canvas.scale(f10, f10, this.B0.centerX(), this.B0.centerY());
            canvas.rotate(this.f5658t0, this.B0.centerX(), this.B0.centerY());
            Drawable drawable = this.f5659u0;
            if (drawable != null) {
                RectF rectF = this.B0;
                drawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                this.f5659u0.setAlpha((int) (this.f5657s0 * 255.0f));
                this.f5659u0.draw(canvas);
            }
            canvas.restore();
        }
    }

    private void w(Canvas canvas) {
        canvas.save();
        float f10 = this.G0;
        canvas.scale(f10, f10, this.B0.centerX(), this.B0.centerY());
        this.L0.setColor(isChecked() ? this.S0 : this.T0);
        if (!isEnabled()) {
            this.L0.setColor(isChecked() ? this.X0 : this.W0);
        }
        float f11 = this.N0 / 2.0f;
        canvas.drawRoundRect(this.B0, f11, f11, this.L0);
        canvas.restore();
    }

    private void x(Canvas canvas) {
        canvas.save();
        Drawable t7 = t();
        t7.setAlpha(z());
        int i10 = this.Q0;
        int switchMinWidth = getSwitchMinWidth();
        int i11 = this.Q0;
        t7.setBounds(i10, i10, switchMinWidth + i11, this.I0 + i11);
        t().draw(canvas);
        canvas.restore();
    }

    private void y(Canvas canvas) {
        if (this.f5645g0) {
            int width = (getWidth() - this.N0) / 2;
            int width2 = (getWidth() + this.N0) / 2;
            int height = (getHeight() - this.N0) / 2;
            int height2 = (getHeight() + this.N0) / 2;
            int width3 = getWidth() / 2;
            int height3 = getHeight() / 2;
            canvas.save();
            canvas.rotate(this.f5658t0, width3, height3);
            this.f5660v0.setBounds(width, height, width2, height2);
            this.f5660v0.draw(canvas);
            canvas.restore();
        }
    }

    private int z() {
        return (int) ((isEnabled() ? 1.0f : 0.5f) * 255.0f);
    }

    public boolean H() {
        return this.f5645g0;
    }

    public boolean J() {
        return this.f5643f0;
    }

    public void M() {
        Drawable e10 = ContextCompat.e(getContext(), R$drawable.switch_custom_track_on);
        Drawable e11 = ContextCompat.e(getContext(), R$drawable.switch_custom_track_off);
        Drawable e12 = ContextCompat.e(getContext(), R$drawable.switch_custom_track_on_disable);
        Drawable e13 = ContextCompat.e(getContext(), R$drawable.switch_custom_track_off_disable);
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (this.Y0 != 0) {
            GradientDrawable gradientDrawable = (GradientDrawable) e10.mutate();
            gradientDrawable.setColor(this.Y0);
            stateListDrawable.addState(new int[]{16842912, R.attr.state_enabled}, gradientDrawable);
        } else {
            stateListDrawable.addState(new int[]{16842912, R.attr.state_enabled}, e10);
        }
        if (this.Z0 != 0) {
            GradientDrawable gradientDrawable2 = (GradientDrawable) e11.mutate();
            gradientDrawable2.setColor(this.Z0);
            stateListDrawable.addState(new int[]{-16842912, R.attr.state_enabled}, gradientDrawable2);
        } else {
            stateListDrawable.addState(new int[]{-16842912, R.attr.state_enabled}, e11);
        }
        if (this.f5636b1 != 0) {
            GradientDrawable gradientDrawable3 = (GradientDrawable) e12.mutate();
            gradientDrawable3.setColor(this.f5636b1);
            stateListDrawable.addState(new int[]{-16842910, 16842912}, gradientDrawable3);
        } else {
            stateListDrawable.addState(new int[]{-16842910, 16842912}, e12);
        }
        if (this.f5638c1 != 0) {
            GradientDrawable gradientDrawable4 = (GradientDrawable) e13.mutate();
            gradientDrawable4.setColor(this.f5638c1);
            stateListDrawable.addState(new int[]{-16842910, -16842912}, gradientDrawable4);
        } else {
            stateListDrawable.addState(new int[]{-16842910, -16842912}, e13);
        }
        setTrackDrawable(stateListDrawable);
    }

    public void N(boolean z10, boolean z11) {
        if (z10 == isChecked()) {
            return;
        }
        super.setChecked(z10);
        if (!this.J0) {
            z10 = isChecked();
            AnimatorSet animatorSet = this.f5655q0;
            if (animatorSet != null) {
                animatorSet.removeAllListeners();
                this.f5655q0.cancel();
                this.f5655q0.end();
            }
            if (this.f5642e1 && z11) {
                s(z10);
            } else {
                if (I()) {
                    setCircleTranslation(z10 ? 0 : this.E0);
                } else {
                    setCircleTranslation(z10 ? this.E0 : 0);
                }
                setInnerCircleAlpha(z10 ? 0.0f : 1.0f);
                setBarColor(z10 ? this.Y0 : this.Z0);
            }
        }
        if (this.f5641e0 && this.f5642e1) {
            L(z10);
            this.f5641e0 = false;
        }
        K();
        invalidate();
    }

    public void Q() {
        if (this.f5645g0) {
            return;
        }
        AccessibilityManager accessibilityManager = this.f5651m0;
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            announceForAccessibility(this.f5649k0);
        }
        this.f5645g0 = true;
        if (this.J0) {
            this.f5654p0.start();
        } else {
            this.f5652n0.start();
        }
        b bVar = this.f5650l0;
        if (bVar != null) {
            bVar.a();
        }
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return Switch.class.getName();
    }

    public final int getOuterCircleUncheckedColor() {
        return this.T0;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f5642e1 = true;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f5642e1 = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        if (this.J0) {
            x(canvas);
            y(canvas);
            return;
        }
        super.onDraw(canvas);
        P();
        O();
        u();
        w(canvas);
        v(canvas);
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.f5646h0) {
            accessibilityNodeInfo.setCheckable(false);
            accessibilityNodeInfo.setText(isChecked() ? this.f5647i0 : this.f5648j0);
        } else {
            accessibilityNodeInfo.setText(isChecked() ? this.f5647i0 : this.f5648j0);
        }
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.TextView, android.view.View
    public void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        int switchMinWidth = getSwitchMinWidth();
        int i12 = this.Q0;
        setMeasuredDimension(switchMinWidth + (i12 * 2), this.I0 + (i12 * 2));
        if (this.f5640d1) {
            return;
        }
        this.f5640d1 = true;
        if (I()) {
            this.D0 = isChecked() ? 0 : this.E0;
        } else {
            this.D0 = isChecked() ? this.E0 : 0;
        }
        this.K0 = isChecked() ? 0.0f : 1.0f;
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            this.f5641e0 = true;
            this.f5643f0 = true;
        }
        if (this.f5646h0 && motionEvent.getAction() == 1 && isEnabled()) {
            Q();
            return false;
        }
        if (this.f5645g0) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public final void setBarCheckedColor(int i10) {
        this.Y0 = i10;
        if (isChecked()) {
            this.f5634a1 = this.Y0;
        }
        M();
        invalidate();
    }

    public final void setBarCheckedDisabledColor(int i10) {
        this.f5636b1 = i10;
        M();
        invalidate();
    }

    public final void setBarUnCheckedColor(int i10) {
        this.Z0 = i10;
        if (!isChecked()) {
            this.f5634a1 = this.Z0;
        }
        M();
        invalidate();
    }

    public final void setBarUncheckedDisabledColor(int i10) {
        this.f5638c1 = i10;
        M();
        invalidate();
    }

    @Override // androidx.appcompat.widget.SwitchCompat, android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z10) {
        N(z10, true);
    }

    public void setCheckedDrawable(Drawable drawable) {
        this.f5663y0 = drawable;
    }

    public void setCircleScale(float f10) {
        this.G0 = f10;
        invalidate();
    }

    public void setCircleScaleX(float f10) {
        this.F0 = f10;
        invalidate();
    }

    public void setCircleTranslation(int i10) {
        this.D0 = i10;
        invalidate();
    }

    public void setInnerCircleAlpha(float f10) {
        this.K0 = f10;
        invalidate();
    }

    public void setInnerCircleColor(int i10) {
        this.R0 = i10;
    }

    public void setLoadingAlpha(float f10) {
        this.f5657s0 = f10;
        invalidate();
    }

    public void setLoadingDrawable(Drawable drawable) {
        this.f5659u0 = drawable;
    }

    public void setLoadingRotation(float f10) {
        this.f5658t0 = f10;
        invalidate();
    }

    public void setLoadingScale(float f10) {
        this.f5656r0 = f10;
        invalidate();
    }

    public void setLoadingStyle(boolean z10) {
        this.f5646h0 = z10;
    }

    public void setOnLoadingStateChangedListener(b bVar) {
        this.f5650l0 = bVar;
    }

    public void setOuterCircleColor(int i10) {
        this.S0 = i10;
    }

    public void setOuterCircleStrokeWidth(int i10) {
        this.H0 = i10;
    }

    public final void setOuterCircleUncheckedColor(int i10) {
        this.T0 = i10;
        invalidate();
    }

    public void setShouldPlaySound(boolean z10) {
        this.f5641e0 = z10;
    }

    public void setTactileFeedbackEnabled(boolean z10) {
        this.f5643f0 = z10;
    }

    public void setThemedLoadingCheckedBackground(Drawable drawable) {
        this.f5661w0 = drawable;
    }

    public void setThemedLoadingUncheckedBackground(Drawable drawable) {
        this.f5662x0 = drawable;
    }

    public void setUncheckedDrawable(Drawable drawable) {
        this.f5664z0 = drawable;
    }

    public COUISwitch(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSwitchStyle);
    }

    public COUISwitch(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f5645g0 = false;
        this.f5646h0 = false;
        this.f5655q0 = new AnimatorSet();
        this.B0 = new RectF();
        this.C0 = new RectF();
        this.F0 = 1.0f;
        this.G0 = 1.0f;
        this.f5640d1 = false;
        setSoundEffectsEnabled(false);
        COUIDarkModeUtil.b(this, false);
        this.f5651m0 = (AccessibilityManager) getContext().getSystemService("accessibility");
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.A0 = attributeSet.getStyleAttribute();
        } else {
            this.A0 = i10;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISwitch, i10, 0);
        B(obtainStyledAttributes, context);
        obtainStyledAttributes.recycle();
        A();
        C();
        D(context);
    }
}
