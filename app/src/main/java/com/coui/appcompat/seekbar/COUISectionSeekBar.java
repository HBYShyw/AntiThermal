package com.coui.appcompat.seekbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.oplus.os.LinearmotorVibrator;
import com.support.control.R$attr;
import com.support.control.R$color;
import com.support.control.R$dimen;
import com.support.control.R$style;
import java.math.BigDecimal;
import java.math.RoundingMode;
import k3.VibrateUtils;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUISectionSeekBar extends COUISeekBar {
    private final PorterDuffXfermode K0;
    private float L0;
    private float M0;
    private boolean N0;
    private float O0;
    private float P0;
    private float Q0;
    private boolean R0;
    private ValueAnimator S0;
    private int T0;
    private float U0;
    private int V0;
    private float W0;
    private float X0;
    private float Y0;
    private int Z0;

    /* renamed from: a1, reason: collision with root package name */
    private int f7545a1;

    /* loaded from: classes.dex */
    class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int intValue = ((Integer) valueAnimator.getAnimatedValue("activeAlpha")).intValue();
            int intValue2 = ((Integer) valueAnimator.getAnimatedValue("inactiveAlpha")).intValue();
            COUISectionSeekBar.this.Z0 = Color.argb(intValue, 0, 0, 0);
            COUISectionSeekBar.this.f7545a1 = Color.argb(intValue2, 255, 255, 255);
            COUISectionSeekBar.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISectionSeekBar.this.P0 = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            COUISectionSeekBar.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISectionSeekBar.this.U0 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            COUISectionSeekBar cOUISectionSeekBar = COUISectionSeekBar.this;
            cOUISectionSeekBar.P0 = cOUISectionSeekBar.M0 + (COUISectionSeekBar.this.U0 * 0.4f) + (COUISectionSeekBar.this.Q0 * 0.6f);
            COUISectionSeekBar cOUISectionSeekBar2 = COUISectionSeekBar.this;
            cOUISectionSeekBar2.O0 = cOUISectionSeekBar2.P0;
            COUISectionSeekBar.this.invalidate();
            COUISectionSeekBar cOUISectionSeekBar3 = COUISectionSeekBar.this;
            int i10 = cOUISectionSeekBar3.f7569l;
            boolean z10 = true;
            if (cOUISectionSeekBar3.L0 - COUISectionSeekBar.this.M0 > 0.0f) {
                float f10 = COUISectionSeekBar.this.P0;
                COUISectionSeekBar cOUISectionSeekBar4 = COUISectionSeekBar.this;
                i10 = Math.round(f10 / (cOUISectionSeekBar4.f7577p ? cOUISectionSeekBar4.getMoveSectionWidth() : cOUISectionSeekBar4.getSectionWidth()));
            } else if (COUISectionSeekBar.this.L0 - COUISectionSeekBar.this.M0 < 0.0f) {
                float f11 = (int) COUISectionSeekBar.this.P0;
                i10 = (int) Math.ceil(f11 / (COUISectionSeekBar.this.f7577p ? r0.getMoveSectionWidth() : r0.getSectionWidth()));
            } else {
                z10 = false;
            }
            if (COUISectionSeekBar.this.isLayoutRtl() && z10) {
                i10 = COUISectionSeekBar.this.f7573n - i10;
            }
            COUISectionSeekBar.this.m(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Animator.AnimatorListener {
        d() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (COUISectionSeekBar.this.N0) {
                COUISectionSeekBar.this.J();
                COUISectionSeekBar.this.N0 = false;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUISectionSeekBar.this.N0) {
                COUISectionSeekBar.this.J();
                COUISectionSeekBar.this.N0 = false;
            }
            if (COUISectionSeekBar.this.R0) {
                COUISectionSeekBar.this.R0 = false;
                COUISectionSeekBar cOUISectionSeekBar = COUISectionSeekBar.this;
                cOUISectionSeekBar.x0(cOUISectionSeekBar.U, true);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISectionSeekBar.this.Y0 = ((Float) valueAnimator.getAnimatedValue("markRadius")).floatValue();
            int intValue = ((Integer) valueAnimator.getAnimatedValue("activeAlpha")).intValue();
            int intValue2 = ((Integer) valueAnimator.getAnimatedValue("inactiveAlpha")).intValue();
            COUISectionSeekBar.this.Z0 = Color.argb(intValue, 0, 0, 0);
            COUISectionSeekBar.this.f7545a1 = Color.argb(intValue2, 255, 255, 255);
            COUISectionSeekBar.this.invalidate();
        }
    }

    public COUISectionSeekBar(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getMoveSectionWidth() {
        return getSeekBarMoveWidth() / this.f7573n;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getSectionWidth() {
        return getSeekBarNormalWidth() / this.f7573n;
    }

    private int getSeekBarMoveWidth() {
        return (int) (((getWidth() - getStart()) - getEnd()) - ((this.L * this.f7553c0) * 2.0f));
    }

    private int getSeekBarNormalWidth() {
        return (int) (((getWidth() - getStart()) - getEnd()) - (this.L * 2.0f));
    }

    private void s0() {
        int seekBarWidth = getSeekBarWidth();
        this.P0 = ((this.f7569l * seekBarWidth) * 1.0f) / this.f7573n;
        if (isLayoutRtl()) {
            this.P0 = seekBarWidth - this.P0;
        }
    }

    private float t0(int i10) {
        float f10 = (i10 * r0) / this.f7573n;
        float seekBarMoveWidth = getSeekBarMoveWidth();
        float max = Math.max(0.0f, Math.min(f10, seekBarMoveWidth));
        return isLayoutRtl() ? seekBarMoveWidth - max : max;
    }

    private int u0(float f10) {
        int seekBarWidth = getSeekBarWidth();
        if (isLayoutRtl()) {
            f10 = seekBarWidth - f10;
        }
        return Math.max(0, Math.min(Math.round((f10 * this.f7573n) / seekBarWidth), this.f7573n));
    }

    private float v0(int i10) {
        float f10 = (i10 * r0) / this.f7573n;
        float seekBarNormalWidth = getSeekBarNormalWidth();
        float max = Math.max(0.0f, Math.min(f10, seekBarNormalWidth));
        return isLayoutRtl() ? seekBarNormalWidth - max : max;
    }

    private float w0(MotionEvent motionEvent) {
        return Math.min(Math.max(0.0f, (motionEvent.getX() - getPaddingLeft()) - this.M), getSeekBarWidth());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void x0(float f10, boolean z10) {
        float v02 = v0(this.f7569l);
        float U = U(f10, v02);
        float sectionWidth = getSectionWidth();
        int round = this.f7577p ? (int) (U / sectionWidth) : Math.round(U / sectionWidth);
        ValueAnimator valueAnimator = this.S0;
        if (valueAnimator != null && valueAnimator.isRunning() && this.L0 == (round * sectionWidth) + v02) {
            return;
        }
        float f11 = round * sectionWidth;
        this.Q0 = f11;
        this.O0 = v02;
        this.L0 = v02;
        float f12 = this.P0 - v02;
        this.N0 = true;
        y0(v02, f11 + v02, f12, z10 ? 100 : 0);
    }

    private void y0(float f10, float f11, float f12, int i10) {
        ValueAnimator valueAnimator;
        if (this.P0 != f11 && ((valueAnimator = this.S0) == null || !valueAnimator.isRunning() || this.L0 != f11)) {
            this.L0 = f11;
            this.M0 = f10;
            if (this.S0 == null) {
                ValueAnimator valueAnimator2 = new ValueAnimator();
                this.S0 = valueAnimator2;
                valueAnimator2.setInterpolator(PathInterpolatorCompat.a(0.0f, 0.0f, 0.25f, 1.0f));
                this.S0.addUpdateListener(new c());
                this.S0.addListener(new d());
            }
            this.S0.cancel();
            if (this.S0.isRunning()) {
                return;
            }
            this.S0.setDuration(i10);
            this.S0.setFloatValues(f12, f11 - f10);
            this.S0.start();
            return;
        }
        if (this.N0) {
            J();
            this.N0 = false;
        }
    }

    private void z0(float f10) {
        float U = U(f10, this.W0);
        float f11 = U < 0.0f ? U - 0.1f : U + 0.1f;
        float moveSectionWidth = getMoveSectionWidth();
        int floatValue = (int) new BigDecimal(Float.toString(f11)).divide(new BigDecimal(Float.toString(moveSectionWidth)), RoundingMode.FLOOR).floatValue();
        float f12 = floatValue * moveSectionWidth;
        if (isLayoutRtl()) {
            floatValue = -floatValue;
        }
        this.Q0 = f11;
        if (Math.abs((this.V0 + floatValue) - this.f7569l) > 0) {
            float f13 = this.W0;
            y0(f13, f12 + f13, this.U0, 100);
        } else {
            this.P0 = this.W0 + f12 + ((this.Q0 - f12) * 0.6f);
            invalidate();
        }
        this.U = f10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.seekbar.COUISeekBar
    public void H(ValueAnimator valueAnimator) {
        super.H(valueAnimator);
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f10 = this.X0;
        this.Y0 = f10 + (animatedFraction * ((2.0f * f10) - f10));
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar
    protected boolean L() {
        if (this.f7563i == null) {
            LinearmotorVibrator e10 = VibrateUtils.e(getContext());
            this.f7563i = e10;
            this.f7561h = e10 != null;
        }
        Object obj = this.f7563i;
        if (obj == null) {
            return false;
        }
        VibrateUtils.j((LinearmotorVibrator) obj, 0, this.f7569l, this.f7573n, 200, 2000);
        return true;
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar
    protected void M() {
        if (this.f7557f) {
            if ((this.f7561h && this.f7559g && L()) || performHapticFeedback(308)) {
                return;
            }
            performHapticFeedback(302);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.seekbar.COUISeekBar
    public void O() {
        super.O();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setValues(PropertyValuesHolder.ofFloat("markRadius", this.Y0, this.X0), PropertyValuesHolder.ofInt("activeAlpha", Color.alpha(this.Z0), 0), PropertyValuesHolder.ofInt("inactiveAlpha", Color.alpha(this.f7545a1), 0));
        valueAnimator.setDuration(183L);
        valueAnimator.setInterpolator(this.f7551a0);
        valueAnimator.addUpdateListener(new e());
        valueAnimator.cancel();
        valueAnimator.start();
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar
    public void Q(int i10, boolean z10, boolean z11) {
        if (this.f7569l != Math.max(0, Math.min(i10, this.f7573n))) {
            if (z10) {
                n(i10, false);
                s0();
                j(i10);
                return;
            }
            n(i10, false);
            if (getWidth() != 0) {
                s0();
                float f10 = this.P0;
                this.O0 = f10;
                this.L0 = f10;
                invalidate();
            }
        }
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar
    protected void j(int i10) {
        AnimatorSet animatorSet = this.T;
        if (animatorSet == null) {
            this.T = new AnimatorSet();
        } else {
            animatorSet.cancel();
        }
        ValueAnimator ofInt = ValueAnimator.ofInt((int) this.W, (int) this.P0);
        ofInt.addUpdateListener(new b());
        ofInt.setInterpolator(this.f7552b0);
        long abs = (Math.abs(r0 - r7) / getSeekBarWidth()) * 483.0f;
        if (abs < 150) {
            abs = 150;
        }
        this.T.setDuration(abs);
        this.T.play(ofInt);
        this.T.start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.seekbar.COUISeekBar
    public void o(Canvas canvas, float f10) {
        float start;
        float f11;
        int i10;
        float width = (getWidth() - getEnd()) - this.M;
        int seekBarCenterY = getSeekBarCenterY();
        if (isLayoutRtl()) {
            f11 = getStart() + this.M + f10;
            start = getStart() + this.M + this.P0;
        } else {
            start = getStart() + this.M;
            f11 = this.P0 + start;
        }
        if (this.f7554d0) {
            this.V.setColor(this.f7585t);
            RectF rectF = this.Q;
            float f12 = seekBarCenterY;
            float f13 = this.F;
            rectF.set(start, f12 - f13, f11, f12 + f13);
            canvas.drawRect(this.Q, this.V);
            if (isLayoutRtl()) {
                RectF rectF2 = this.R;
                float f14 = this.F;
                RectF rectF3 = this.Q;
                rectF2.set(width - f14, rectF3.top, f14 + width, rectF3.bottom);
                canvas.drawArc(this.R, -90.0f, 180.0f, true, this.V);
            } else {
                RectF rectF4 = this.R;
                float f15 = this.F;
                RectF rectF5 = this.Q;
                rectF4.set(start - f15, rectF5.top, start + f15, rectF5.bottom);
                canvas.drawArc(this.R, 90.0f, 180.0f, true, this.V);
            }
        }
        int saveLayer = canvas.saveLayer(null, null, 31);
        this.V.setXfermode(this.K0);
        if (this.f7554d0) {
            i10 = isLayoutRtl() ? this.f7545a1 : this.Z0;
        } else {
            i10 = this.f7545a1;
        }
        this.V.setColor(i10);
        float start2 = getStart() + this.M;
        float f16 = width - start2;
        int i11 = 0;
        boolean z10 = false;
        while (true) {
            int i12 = this.f7573n;
            if (i11 > i12) {
                break;
            }
            if (this.f7554d0 && !z10 && ((i11 * f16) / i12) + start2 > getStart() + this.M + this.P0) {
                this.V.setColor(isLayoutRtl() ? this.Z0 : this.f7545a1);
                z10 = true;
            }
            canvas.drawCircle(((i11 * f16) / this.f7573n) + start2, seekBarCenterY, this.Y0, this.V);
            i11++;
        }
        this.V.setXfermode(null);
        canvas.restoreToCount(saveLayer);
        this.W = this.P0;
        if (this.f7556e0) {
            float start3 = getStart() + this.M;
            this.V.setColor(this.f7589v);
            canvas.drawCircle(start3 + Math.min(this.P0, getSeekBarWidth()), seekBarCenterY, this.K, this.V);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.seekbar.COUISeekBar, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        this.P0 = -1.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.seekbar.COUISeekBar
    public void p(Canvas canvas) {
        int i10;
        if (this.P0 == -1.0f) {
            s0();
        }
        int seekBarCenterY = getSeekBarCenterY();
        int saveLayer = canvas.saveLayer(null, null, 31);
        super.p(canvas);
        this.V.setXfermode(this.K0);
        float start = getStart() + this.M;
        float width = ((getWidth() - getEnd()) - this.M) - start;
        if (this.f7554d0) {
            i10 = isLayoutRtl() ? this.f7587u : this.f7585t;
        } else {
            i10 = this.f7587u;
        }
        this.V.setColor(i10);
        int i11 = 0;
        boolean z10 = false;
        while (true) {
            int i12 = this.f7573n;
            if (i11 <= i12) {
                if (this.f7554d0 && !z10 && ((i11 * width) / i12) + start > getStart() + this.P0) {
                    this.V.setColor(isLayoutRtl() ? this.f7585t : this.f7587u);
                    z10 = true;
                }
                canvas.drawCircle(((i11 * width) / this.f7573n) + start, seekBarCenterY, this.X0, this.V);
                i11++;
            } else {
                this.V.setXfermode(null);
                canvas.restoreToCount(saveLayer);
                return;
            }
        }
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar, android.widget.AbsSeekBar, android.widget.ProgressBar
    public void setMax(int i10) {
        if (i10 < getMin()) {
            i10 = getMin();
        }
        if (i10 != this.f7573n) {
            setLocalMax(i10);
            if (this.f7569l > i10) {
                setProgress(i10);
            }
            s0();
        }
        invalidate();
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar
    protected void x(MotionEvent motionEvent) {
        float w02 = w0(motionEvent);
        this.f7567k = w02;
        this.U = w02;
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar
    protected void y(MotionEvent motionEvent) {
        float w02 = w0(motionEvent);
        if (this.f7577p) {
            float f10 = this.U;
            if (w02 - f10 > 0.0f) {
                r2 = 1;
            } else if (w02 - f10 >= 0.0f) {
                r2 = 0;
            }
            if (r2 == (-this.T0)) {
                this.T0 = r2;
                int i10 = this.V0;
                int i11 = this.f7569l;
                if (i10 != i11) {
                    this.V0 = i11;
                    this.W0 = t0(i11);
                    this.U0 = 0.0f;
                }
                ValueAnimator valueAnimator = this.S0;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
            }
            z0(w02);
        } else {
            if (!W(motionEvent, this)) {
                return;
            }
            if (Math.abs(w02 - this.f7567k) > this.f7565j) {
                R();
                V();
                int u02 = u0(this.f7567k);
                this.V0 = u02;
                m(u02);
                float t02 = t0(this.V0);
                this.W0 = t02;
                this.U0 = 0.0f;
                this.P0 = t02;
                invalidate();
                z0(w02);
                this.T0 = w02 - this.f7567k > 0.0f ? 1 : -1;
            }
        }
        this.U = w02;
    }

    @Override // com.coui.appcompat.seekbar.COUISeekBar
    protected void z(MotionEvent motionEvent) {
        float w02 = w0(motionEvent);
        if (this.f7577p) {
            ValueAnimator valueAnimator = this.S0;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.R0 = true;
            }
            if (!this.R0) {
                x0(w02, true);
            }
            K(false);
            setPressed(false);
            O();
            return;
        }
        if (W(motionEvent, this)) {
            x0(w02, false);
        }
        i(w02);
        O();
    }

    public COUISectionSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSectionSeekBarStyle);
    }

    public COUISectionSeekBar(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, COUIContextUtil.e(context) ? R$style.COUISectionSeekBar_Dark : R$style.COUISectionSeekBar);
    }

    public COUISectionSeekBar(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.K0 = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        this.N0 = false;
        this.P0 = -1.0f;
        this.R0 = false;
        this.V0 = -1;
        this.W0 = 0.0f;
        this.X0 = 0.0f;
        this.Y0 = 0.0f;
        float dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_section_seekbar_tick_mark_radius);
        this.X0 = dimensionPixelSize;
        this.Y0 = dimensionPixelSize;
        this.Z0 = 0;
        this.f7545a1 = 0;
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("activeAlpha", 0, Color.alpha(COUIContextUtil.d(getContext(), R$color.coui_seekbar_mark_active_anim_end)));
        PropertyValuesHolder ofInt2 = PropertyValuesHolder.ofInt("inactiveAlpha", 0, Color.alpha(COUIContextUtil.d(getContext(), R$color.coui_seekbar_mark_inactive_anim_end)));
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setValues(ofInt, ofInt2);
        valueAnimator.addUpdateListener(new a());
        this.S.play(valueAnimator);
    }
}
