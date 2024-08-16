package com.coui.appcompat.seekbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.core.content.ContextCompat;
import com.support.control.R$attr;
import com.support.control.R$color;
import com.support.control.R$dimen;
import com.support.control.R$style;
import com.support.control.R$styleable;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIIntentSeekBar extends COUISeekBar {
    private int K0;
    private int L0;
    private float M0;
    private boolean N0;

    public COUIIntentSeekBar(Context context) {
        this(context, null);
    }

    private void a0(Canvas canvas) {
        float start;
        float seekBarWidth = getSeekBarWidth();
        int seekBarCenterY = getSeekBarCenterY();
        if (isLayoutRtl()) {
            start = ((getStart() + this.M) + seekBarWidth) - (this.f7555e * seekBarWidth);
        } else {
            start = getStart() + this.M + (this.f7555e * seekBarWidth);
        }
        float f10 = this.K;
        float f11 = start - f10;
        float f12 = start + f10;
        this.V.setColor(this.f7589v);
        if (this.f7577p && !this.N0) {
            float f13 = this.M0;
            float f14 = seekBarCenterY;
            float f15 = this.K;
            canvas.drawRoundRect(f11 - f13, (f14 - f15) - f13, f12 + f13, f14 + f15 + f13, f15 + f13, f15 + f13, this.V);
        } else {
            float f16 = seekBarCenterY;
            float f17 = this.K;
            canvas.drawRoundRect(f11, f16 - f17, f12, f16 + f17, f17, f17, this.V);
        }
        this.W = f11 + ((f12 - f11) / 2.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.coui.appcompat.seekbar.COUISeekBar
    public void J() {
        super.J();
        this.f7571m = this.f7569l;
    }

    @Override // android.widget.ProgressBar
    public int getSecondaryProgress() {
        return this.K0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.seekbar.COUISeekBar
    public void o(Canvas canvas, float f10) {
        float f11;
        float f12;
        float f13;
        float f14;
        if (this.f7554d0) {
            int seekBarCenterY = getSeekBarCenterY();
            getWidth();
            getEnd();
            int i10 = this.f7573n - this.f7575o;
            if (isLayoutRtl()) {
                f13 = getStart() + this.M + f10;
                int i11 = this.f7571m;
                int i12 = this.f7575o;
                float f15 = i10;
                float f16 = f13 - (((i11 - i12) * f10) / f15);
                f14 = f13 - (((this.K0 - i12) * f10) / f15);
                f11 = f16;
                f12 = f13;
            } else {
                float start = this.M + getStart();
                int i13 = this.f7571m;
                int i14 = this.f7575o;
                float f17 = i10;
                float f18 = (((i13 - i14) * f10) / f17) + start;
                float f19 = start + (((this.K0 - i14) * f10) / f17);
                f11 = start;
                f12 = f18;
                f13 = f19;
                f14 = f11;
            }
            this.V.setColor(this.L0);
            float f20 = this.F;
            float f21 = seekBarCenterY;
            this.Q.set(f14 - f20, f21 - f20, f13 + f20, f20 + f21);
            RectF rectF = this.Q;
            float f22 = this.F;
            canvas.drawRoundRect(rectF, f22, f22, this.V);
            if (this.N0) {
                super.o(canvas, f10);
            } else {
                this.V.setColor(this.f7585t);
                RectF rectF2 = this.Q;
                float f23 = this.F;
                rectF2.set(f11 - f23, f21 - f23, f12 + f23, f21 + f23);
                RectF rectF3 = this.Q;
                float f24 = this.F;
                canvas.drawRoundRect(rectF3, f24, f24, this.V);
            }
            a0(canvas);
        }
    }

    public void setFollowThumb(boolean z10) {
        this.N0 = z10;
    }

    @Override // android.widget.ProgressBar
    public void setSecondaryProgress(int i10) {
        if (i10 >= 0) {
            this.K0 = Math.max(this.f7575o, Math.min(i10, this.f7573n));
            invalidate();
        }
    }

    public void setSecondaryProgressColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.L0 = v(this, colorStateList, ContextCompat.c(getContext(), R$color.coui_seekbar_secondary_progress_color));
            invalidate();
        }
    }

    public COUIIntentSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiIntentSeekBarStyle);
    }

    public COUIIntentSeekBar(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, COUIContextUtil.e(context) ? R$style.COUIIntentSeekBar_Dark : R$style.COUIIntentSeekBar);
    }

    public COUIIntentSeekBar(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.K0 = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIIntentSeekBar, i10, i11);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R$styleable.COUIIntentSeekBar_couiSeekBarSecondaryProgressColor);
        this.N0 = obtainStyledAttributes.getBoolean(R$styleable.COUIIntentSeekBar_couiSeekBarIsFollowThumb, false);
        obtainStyledAttributes.recycle();
        this.L0 = v(this, colorStateList, COUIContextUtil.d(getContext(), R$color.coui_seekbar_progress_color_normal));
        this.M0 = getResources().getDimensionPixelSize(R$dimen.coui_seekbar_intent_thumb_out_shade_radius);
    }
}
