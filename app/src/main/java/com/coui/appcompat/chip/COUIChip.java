package com.coui.appcompat.chip;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.support.control.R$attr;
import com.support.control.R$color;
import com.support.control.R$style;
import com.support.control.R$styleable;
import m1.COUIEaseInterpolator;
import m1.COUIMoveEaseInterpolator;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIChip extends Chip {

    /* renamed from: i0, reason: collision with root package name */
    private static final int[] f5615i0 = {16842912, R.attr.state_enabled};

    /* renamed from: j0, reason: collision with root package name */
    private static final int[] f5616j0 = {-16842912, R.attr.state_enabled};

    /* renamed from: k0, reason: collision with root package name */
    private static final int[] f5617k0 = {-16842910};
    private int F;
    private int G;
    private int H;
    private int I;
    private int J;
    private int K;
    private int L;
    private int M;
    private int N;
    private float O;
    private boolean P;
    private boolean Q;
    private boolean R;
    private String S;
    private ValueAnimator T;
    private ValueAnimator U;
    private ValueAnimator V;
    private Interpolator W;

    /* renamed from: a0, reason: collision with root package name */
    private Interpolator f5618a0;

    /* renamed from: b0, reason: collision with root package name */
    private int[] f5619b0;

    /* renamed from: c0, reason: collision with root package name */
    private int[][] f5620c0;

    /* renamed from: d0, reason: collision with root package name */
    private int[] f5621d0;

    /* renamed from: e0, reason: collision with root package name */
    private int[][] f5622e0;

    /* renamed from: f0, reason: collision with root package name */
    private int[] f5623f0;

    /* renamed from: g0, reason: collision with root package name */
    private int f5624g0;

    /* renamed from: h0, reason: collision with root package name */
    private Context f5625h0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f5626a;

        a(boolean z10) {
            this.f5626a = z10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            long currentPlayTime = valueAnimator.getCurrentPlayTime();
            COUIChip.this.O = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUIChip.this.Q && this.f5626a && ((float) currentPlayTime) > ((float) valueAnimator.getDuration()) * 0.8f) {
                valueAnimator.cancel();
                COUIChip.this.c0(false);
            } else {
                COUIChip cOUIChip = COUIChip.this;
                cOUIChip.setScale(cOUIChip.O);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f5628a;

        b(boolean z10) {
            this.f5628a = z10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIChip.this.K = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            COUIChip.this.f5621d0[!this.f5628a ? 1 : 0] = COUIChip.this.K;
            COUIChip.this.setChipBackgroundColor(new ColorStateList(COUIChip.this.f5620c0, COUIChip.this.f5621d0));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIChip.this.K == COUIChip.this.G || COUIChip.this.K == COUIChip.this.F) {
                COUIChip.this.f0();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f5631a;

        d(boolean z10) {
            this.f5631a = z10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIChip.this.M = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            COUIChip.this.f5623f0[!this.f5631a ? 1 : 0] = COUIChip.this.M;
            COUIChip.this.setTextColor(new ColorStateList(COUIChip.this.f5622e0, COUIChip.this.f5623f0));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e extends AnimatorListenerAdapter {
        e() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIChip.this.M == COUIChip.this.I || COUIChip.this.M == COUIChip.this.H) {
                COUIChip.this.g0();
            }
        }
    }

    public COUIChip(Context context) {
        this(context, null);
    }

    private void X(boolean z10) {
        ValueAnimator valueAnimator = this.T;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            boolean z11 = !z10 && ((float) this.T.getCurrentPlayTime()) < ((float) this.T.getDuration()) * 0.8f;
            this.Q = z11;
            if (!z11) {
                this.T.cancel();
            }
        }
        if (s()) {
            ValueAnimator valueAnimator2 = this.U;
            if (valueAnimator2 != null && valueAnimator2.isRunning() && z10) {
                this.U.cancel();
            }
            ValueAnimator valueAnimator3 = this.V;
            if (valueAnimator3 != null && valueAnimator3.isRunning() && z10) {
                this.V.cancel();
            }
        }
    }

    private void Y(boolean z10) {
        if (z10 != isChecked()) {
            Z(z10);
        }
    }

    private void Z(boolean z10) {
        if (this.R) {
            if (z10) {
                setTypeface(Typeface.create(this.S, 0));
            } else {
                setTypeface(Typeface.DEFAULT);
            }
        }
    }

    private void a0(boolean z10) {
        ValueAnimator valueAnimator = this.U;
        if (valueAnimator == null) {
            this.U = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(this.K), Integer.valueOf(this.L));
        } else {
            valueAnimator.setIntValues(this.K, this.L);
        }
        this.U.setInterpolator(this.f5618a0);
        this.U.setDuration(200L);
        this.U.addUpdateListener(new b(z10));
        this.U.addListener(new c());
        this.U.start();
    }

    private void b0(MotionEvent motionEvent, boolean z10) {
        int i10;
        getLocationOnScreen(this.f5619b0);
        boolean z11 = motionEvent.getRawX() > ((float) this.f5619b0[0]) && motionEvent.getRawX() < ((float) (this.f5619b0[0] + getWidth())) && motionEvent.getRawY() > ((float) this.f5619b0[1]) && motionEvent.getRawY() < ((float) (this.f5619b0[1] + getHeight()));
        int i11 = this.K;
        int i12 = this.F;
        boolean z12 = i11 == i12 || i11 == this.G || (i10 = this.M) == this.H || i10 == this.I;
        if (!z11) {
            if (z12) {
                return;
            }
            if (z10) {
                this.L = i12;
                this.N = this.H;
            } else {
                this.L = this.G;
                this.N = this.I;
            }
            a0(!z10);
            d0(!z10);
            return;
        }
        if (z12) {
            if (z10) {
                this.K = i12;
                this.L = this.G;
                this.M = this.H;
                this.N = this.I;
            } else {
                this.K = this.G;
                this.L = i12;
                this.M = this.I;
                this.N = this.H;
            }
        } else if (z10) {
            this.L = this.G;
            this.N = this.I;
        } else {
            this.L = i12;
            this.N = this.H;
        }
        a0(z10);
        d0(z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c0(boolean z10) {
        this.Q = false;
        X(z10);
        if (this.Q) {
            return;
        }
        float[] fArr = new float[2];
        fArr[0] = z10 ? 1.0f : this.O;
        fArr[1] = z10 ? 0.9f : 1.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        this.T = ofFloat;
        ofFloat.setInterpolator(this.W);
        this.T.setDuration(z10 ? 200L : 340L);
        this.T.addUpdateListener(new a(z10));
        this.T.start();
    }

    private void d0(boolean z10) {
        ValueAnimator valueAnimator = this.V;
        if (valueAnimator == null) {
            this.V = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(this.M), Integer.valueOf(this.N));
        } else {
            valueAnimator.setIntValues(this.M, this.N);
        }
        this.V.setInterpolator(this.f5618a0);
        this.V.setDuration(200L);
        this.V.addUpdateListener(new d(z10));
        this.V.addListener(new e());
        this.V.start();
    }

    private boolean e0() {
        ViewParent parent = getParent();
        if (parent instanceof ChipGroup) {
            ChipGroup chipGroup = (ChipGroup) parent;
            int i10 = this.K;
            boolean z10 = (i10 == this.F && this.M == this.H) || (i10 == this.G && this.M == this.I);
            if (chipGroup.d() && chipGroup.getCheckedChipIds().size() == 1 && isChecked() && z10) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f0() {
        if (this.f5620c0 == null) {
            this.f5620c0 = new int[2];
        }
        if (this.f5621d0 == null) {
            this.f5621d0 = new int[this.f5620c0.length];
        }
        int[][] iArr = this.f5620c0;
        iArr[0] = f5616j0;
        iArr[1] = f5615i0;
        int[] iArr2 = this.f5621d0;
        iArr2[0] = this.G;
        iArr2[1] = this.F;
        setChipBackgroundColor(new ColorStateList(this.f5620c0, this.f5621d0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g0() {
        if (this.f5622e0 == null) {
            this.f5622e0 = new int[3];
        }
        if (this.f5623f0 == null) {
            this.f5623f0 = new int[this.f5622e0.length];
        }
        int[][] iArr = this.f5622e0;
        iArr[0] = f5616j0;
        iArr[1] = f5615i0;
        iArr[2] = f5617k0;
        int[] iArr2 = this.f5623f0;
        iArr2[0] = this.I;
        iArr2[1] = this.H;
        iArr2[2] = this.J;
        setTextColor(new ColorStateList(this.f5622e0, this.f5623f0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScale(float f10) {
        float max = Math.max(0.9f, Math.min(1.0f, f10));
        setScaleX(max);
        setScaleY(max);
        invalidate();
    }

    @Override // com.google.android.material.chip.Chip, android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean isChecked = isChecked();
        if (isEnabled() && this.P) {
            int action = motionEvent.getAction();
            if (action == 0) {
                c0(true);
            } else if (action == 1 || action == 3) {
                if (s() && e0()) {
                    b0(motionEvent, isChecked);
                }
                c0(false);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // com.google.android.material.chip.Chip, android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z10) {
        Y(z10);
        super.setChecked(z10);
    }

    public void setCheckedBackgroundColor(int i10) {
        if (i10 != this.F) {
            this.F = i10;
            f0();
        }
    }

    public void setCheckedTextColor(int i10) {
        if (i10 != this.H) {
            this.H = i10;
            g0();
        }
    }

    public void setDisabledTextColor(int i10) {
        if (i10 != this.J) {
            this.J = i10;
            g0();
        }
    }

    public void setUncheckedBackgroundColor(int i10) {
        if (i10 != this.G) {
            this.G = i10;
            f0();
        }
    }

    public void setUncheckedTextColor(int i10) {
        if (i10 != this.I) {
            this.I = i10;
            g0();
        }
    }

    public COUIChip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiChipStyle);
    }

    public COUIChip(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Widget_COUI_Chip);
    }

    public COUIChip(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10);
        this.O = 1.0f;
        this.f5619b0 = new int[2];
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f5624g0 = attributeSet.getStyleAttribute();
        } else {
            this.f5624g0 = i10;
        }
        this.f5625h0 = context;
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIChip, i10, i11);
        this.P = obtainStyledAttributes.getBoolean(R$styleable.COUIChip_chipAnimationEnable, true);
        int i12 = R$styleable.COUIChip_checkedBackgroundColor;
        int i13 = com.support.appcompat.R$attr.couiColorPrimaryNeutral;
        this.F = obtainStyledAttributes.getColor(i12, COUIContextUtil.a(context, i13));
        this.G = obtainStyledAttributes.getColor(R$styleable.COUIChip_uncheckedBackgroundColor, COUIContextUtil.a(context, com.support.appcompat.R$attr.couiColorPressBackground));
        this.H = obtainStyledAttributes.getColor(R$styleable.COUIChip_checkedTextColor, getResources().getColor(R$color.chip_checked_text_color));
        this.I = obtainStyledAttributes.getColor(R$styleable.COUIChip_uncheckedTextColor, COUIContextUtil.a(context, i13));
        this.J = obtainStyledAttributes.getColor(R$styleable.COUIChip_disabledTextColor, COUIContextUtil.a(context, com.support.appcompat.R$attr.couiColorDisabledNeutral));
        this.R = obtainStyledAttributes.getBoolean(R$styleable.COUIChip_openCheckedTextFontFamily, false);
        String string = obtainStyledAttributes.getString(R$styleable.COUIChip_checkedFontFamily);
        this.S = string;
        if (this.R && TextUtils.isEmpty(string)) {
            this.S = "sans-serif-medium";
        }
        Z(isChecked());
        if (s()) {
            f0();
            g0();
        }
        if (this.P) {
            this.W = new COUIMoveEaseInterpolator();
            if (s()) {
                this.K = isChecked() ? this.F : this.G;
                this.M = isChecked() ? this.H : this.I;
                this.f5618a0 = new COUIEaseInterpolator();
            }
        }
        obtainStyledAttributes.recycle();
    }
}
