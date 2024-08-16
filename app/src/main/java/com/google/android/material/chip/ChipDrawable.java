package com.google.android.material.chip;

import a4.RippleUtils;
import android.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.text.BidiFormatter;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import p3.MotionSpec;
import q3.CanvasCompat;
import r3.MaterialColors;
import z3.MaterialResources;
import z3.TextAppearance;

/* compiled from: ChipDrawable.java */
/* renamed from: com.google.android.material.chip.a, reason: use source file name */
/* loaded from: classes.dex */
public class ChipDrawable extends MaterialShapeDrawable implements Drawable.Callback, TextDrawableHelper.TextDrawableDelegate {
    private static final int[] N0 = {R.attr.state_enabled};
    private static final ShapeDrawable O0 = new ShapeDrawable(new OvalShape());
    private int A0;
    private ColorFilter B0;
    private PorterDuffColorFilter C0;
    private ColorStateList D;
    private ColorStateList D0;
    private ColorStateList E;
    private PorterDuff.Mode E0;
    private float F;
    private int[] F0;
    private float G;
    private boolean G0;
    private ColorStateList H;
    private ColorStateList H0;
    private float I;
    private WeakReference<a> I0;
    private ColorStateList J;
    private TextUtils.TruncateAt J0;
    private CharSequence K;
    private boolean K0;
    private boolean L;
    private int L0;
    private Drawable M;
    private boolean M0;
    private ColorStateList N;
    private float O;
    private boolean P;
    private boolean Q;
    private Drawable R;
    private Drawable S;
    private ColorStateList T;
    private float U;
    private CharSequence V;
    private boolean W;
    private boolean X;
    private Drawable Y;
    private ColorStateList Z;

    /* renamed from: a0, reason: collision with root package name */
    private MotionSpec f8590a0;

    /* renamed from: b0, reason: collision with root package name */
    private MotionSpec f8591b0;

    /* renamed from: c0, reason: collision with root package name */
    private float f8592c0;

    /* renamed from: d0, reason: collision with root package name */
    private float f8593d0;

    /* renamed from: e0, reason: collision with root package name */
    private float f8594e0;

    /* renamed from: f0, reason: collision with root package name */
    private float f8595f0;

    /* renamed from: g0, reason: collision with root package name */
    private float f8596g0;

    /* renamed from: h0, reason: collision with root package name */
    private float f8597h0;

    /* renamed from: i0, reason: collision with root package name */
    private float f8598i0;

    /* renamed from: j0, reason: collision with root package name */
    private float f8599j0;

    /* renamed from: k0, reason: collision with root package name */
    private final Context f8600k0;

    /* renamed from: l0, reason: collision with root package name */
    private final Paint f8601l0;

    /* renamed from: m0, reason: collision with root package name */
    private final Paint f8602m0;

    /* renamed from: n0, reason: collision with root package name */
    private final Paint.FontMetrics f8603n0;

    /* renamed from: o0, reason: collision with root package name */
    private final RectF f8604o0;

    /* renamed from: p0, reason: collision with root package name */
    private final PointF f8605p0;

    /* renamed from: q0, reason: collision with root package name */
    private final Path f8606q0;

    /* renamed from: r0, reason: collision with root package name */
    private final TextDrawableHelper f8607r0;

    /* renamed from: s0, reason: collision with root package name */
    private int f8608s0;

    /* renamed from: t0, reason: collision with root package name */
    private int f8609t0;

    /* renamed from: u0, reason: collision with root package name */
    private int f8610u0;

    /* renamed from: v0, reason: collision with root package name */
    private int f8611v0;

    /* renamed from: w0, reason: collision with root package name */
    private int f8612w0;

    /* renamed from: x0, reason: collision with root package name */
    private int f8613x0;

    /* renamed from: y0, reason: collision with root package name */
    private boolean f8614y0;

    /* renamed from: z0, reason: collision with root package name */
    private int f8615z0;

    /* compiled from: ChipDrawable.java */
    /* renamed from: com.google.android.material.chip.a$a */
    /* loaded from: classes.dex */
    public interface a {
        void a();
    }

    private ChipDrawable(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.G = -1.0f;
        this.f8601l0 = new Paint(1);
        this.f8603n0 = new Paint.FontMetrics();
        this.f8604o0 = new RectF();
        this.f8605p0 = new PointF();
        this.f8606q0 = new Path();
        this.A0 = 255;
        this.E0 = PorterDuff.Mode.SRC_IN;
        this.I0 = new WeakReference<>(null);
        P(context);
        this.f8600k0 = context;
        TextDrawableHelper textDrawableHelper = new TextDrawableHelper(this);
        this.f8607r0 = textDrawableHelper;
        this.K = "";
        textDrawableHelper.getTextPaint().density = context.getResources().getDisplayMetrics().density;
        this.f8602m0 = null;
        int[] iArr = N0;
        setState(iArr);
        s2(iArr);
        this.K0 = true;
        if (RippleUtils.f36a) {
            O0.setTint(-1);
        }
    }

    private static boolean A1(TextAppearance textAppearance) {
        return (textAppearance == null || textAppearance.i() == null || !textAppearance.i().isStateful()) ? false : true;
    }

    private boolean B0() {
        return this.X && this.Y != null && this.W;
    }

    private void B1(AttributeSet attributeSet, int i10, int i11) {
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(this.f8600k0, attributeSet, R$styleable.Chip, i10, i11, new int[0]);
        this.M0 = obtainStyledAttributes.hasValue(R$styleable.Chip_shapeAppearance);
        i2(MaterialResources.a(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_chipSurfaceColor));
        M1(MaterialResources.a(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_chipBackgroundColor));
        a2(obtainStyledAttributes.getDimension(R$styleable.Chip_chipMinHeight, 0.0f));
        int i12 = R$styleable.Chip_chipCornerRadius;
        if (obtainStyledAttributes.hasValue(i12)) {
            O1(obtainStyledAttributes.getDimension(i12, 0.0f));
        }
        e2(MaterialResources.a(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_chipStrokeColor));
        g2(obtainStyledAttributes.getDimension(R$styleable.Chip_chipStrokeWidth, 0.0f));
        F2(MaterialResources.a(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_rippleColor));
        K2(obtainStyledAttributes.getText(R$styleable.Chip_android_text));
        TextAppearance g6 = MaterialResources.g(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_android_textAppearance);
        g6.l(obtainStyledAttributes.getDimension(R$styleable.Chip_android_textSize, g6.j()));
        L2(g6);
        int i13 = obtainStyledAttributes.getInt(R$styleable.Chip_android_ellipsize, 0);
        if (i13 == 1) {
            x2(TextUtils.TruncateAt.START);
        } else if (i13 == 2) {
            x2(TextUtils.TruncateAt.MIDDLE);
        } else if (i13 == 3) {
            x2(TextUtils.TruncateAt.END);
        }
        Z1(obtainStyledAttributes.getBoolean(R$styleable.Chip_chipIconVisible, false));
        if (attributeSet != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconEnabled") != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconVisible") == null) {
            Z1(obtainStyledAttributes.getBoolean(R$styleable.Chip_chipIconEnabled, false));
        }
        S1(MaterialResources.e(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_chipIcon));
        int i14 = R$styleable.Chip_chipIconTint;
        if (obtainStyledAttributes.hasValue(i14)) {
            W1(MaterialResources.a(this.f8600k0, obtainStyledAttributes, i14));
        }
        U1(obtainStyledAttributes.getDimension(R$styleable.Chip_chipIconSize, -1.0f));
        v2(obtainStyledAttributes.getBoolean(R$styleable.Chip_closeIconVisible, false));
        if (attributeSet != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconEnabled") != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconVisible") == null) {
            v2(obtainStyledAttributes.getBoolean(R$styleable.Chip_closeIconEnabled, false));
        }
        j2(MaterialResources.e(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_closeIcon));
        t2(MaterialResources.a(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_closeIconTint));
        o2(obtainStyledAttributes.getDimension(R$styleable.Chip_closeIconSize, 0.0f));
        E1(obtainStyledAttributes.getBoolean(R$styleable.Chip_android_checkable, false));
        L1(obtainStyledAttributes.getBoolean(R$styleable.Chip_checkedIconVisible, false));
        if (attributeSet != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconEnabled") != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconVisible") == null) {
            L1(obtainStyledAttributes.getBoolean(R$styleable.Chip_checkedIconEnabled, false));
        }
        G1(MaterialResources.e(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_checkedIcon));
        int i15 = R$styleable.Chip_checkedIconTint;
        if (obtainStyledAttributes.hasValue(i15)) {
            I1(MaterialResources.a(this.f8600k0, obtainStyledAttributes, i15));
        }
        I2(MotionSpec.c(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_showMotionSpec));
        y2(MotionSpec.c(this.f8600k0, obtainStyledAttributes, R$styleable.Chip_hideMotionSpec));
        c2(obtainStyledAttributes.getDimension(R$styleable.Chip_chipStartPadding, 0.0f));
        C2(obtainStyledAttributes.getDimension(R$styleable.Chip_iconStartPadding, 0.0f));
        A2(obtainStyledAttributes.getDimension(R$styleable.Chip_iconEndPadding, 0.0f));
        P2(obtainStyledAttributes.getDimension(R$styleable.Chip_textStartPadding, 0.0f));
        N2(obtainStyledAttributes.getDimension(R$styleable.Chip_textEndPadding, 0.0f));
        q2(obtainStyledAttributes.getDimension(R$styleable.Chip_closeIconStartPadding, 0.0f));
        l2(obtainStyledAttributes.getDimension(R$styleable.Chip_closeIconEndPadding, 0.0f));
        Q1(obtainStyledAttributes.getDimension(R$styleable.Chip_chipEndPadding, 0.0f));
        E2(obtainStyledAttributes.getDimensionPixelSize(R$styleable.Chip_android_maxWidth, Integer.MAX_VALUE));
        obtainStyledAttributes.recycle();
    }

    public static ChipDrawable C0(Context context, AttributeSet attributeSet, int i10, int i11) {
        ChipDrawable chipDrawable = new ChipDrawable(context, attributeSet, i10, i11);
        chipDrawable.B1(attributeSet, i10, i11);
        return chipDrawable;
    }

    private void D0(Canvas canvas, Rect rect) {
        if (T2()) {
            s0(rect, this.f8604o0);
            RectF rectF = this.f8604o0;
            float f10 = rectF.left;
            float f11 = rectF.top;
            canvas.translate(f10, f11);
            this.Y.setBounds(0, 0, (int) this.f8604o0.width(), (int) this.f8604o0.height());
            this.Y.draw(canvas);
            canvas.translate(-f10, -f11);
        }
    }

    private boolean D1(int[] iArr, int[] iArr2) {
        boolean z10;
        boolean onStateChange = super.onStateChange(iArr);
        ColorStateList colorStateList = this.D;
        int k10 = k(colorStateList != null ? colorStateList.getColorForState(iArr, this.f8608s0) : 0);
        boolean z11 = true;
        if (this.f8608s0 != k10) {
            this.f8608s0 = k10;
            onStateChange = true;
        }
        ColorStateList colorStateList2 = this.E;
        int k11 = k(colorStateList2 != null ? colorStateList2.getColorForState(iArr, this.f8609t0) : 0);
        if (this.f8609t0 != k11) {
            this.f8609t0 = k11;
            onStateChange = true;
        }
        int g6 = MaterialColors.g(k10, k11);
        if ((this.f8610u0 != g6) | (w() == null)) {
            this.f8610u0 = g6;
            a0(ColorStateList.valueOf(g6));
            onStateChange = true;
        }
        ColorStateList colorStateList3 = this.H;
        int colorForState = colorStateList3 != null ? colorStateList3.getColorForState(iArr, this.f8611v0) : 0;
        if (this.f8611v0 != colorForState) {
            this.f8611v0 = colorForState;
            onStateChange = true;
        }
        int colorForState2 = (this.H0 == null || !RippleUtils.e(iArr)) ? 0 : this.H0.getColorForState(iArr, this.f8612w0);
        if (this.f8612w0 != colorForState2) {
            this.f8612w0 = colorForState2;
            if (this.G0) {
                onStateChange = true;
            }
        }
        int colorForState3 = (this.f8607r0.getTextAppearance() == null || this.f8607r0.getTextAppearance().i() == null) ? 0 : this.f8607r0.getTextAppearance().i().getColorForState(iArr, this.f8613x0);
        if (this.f8613x0 != colorForState3) {
            this.f8613x0 = colorForState3;
            onStateChange = true;
        }
        boolean z12 = u1(getState(), 16842912) && this.W;
        if (this.f8614y0 == z12 || this.Y == null) {
            z10 = false;
        } else {
            float t02 = t0();
            this.f8614y0 = z12;
            if (t02 != t0()) {
                onStateChange = true;
                z10 = true;
            } else {
                z10 = false;
                onStateChange = true;
            }
        }
        ColorStateList colorStateList4 = this.D0;
        int colorForState4 = colorStateList4 != null ? colorStateList4.getColorForState(iArr, this.f8615z0) : 0;
        if (this.f8615z0 != colorForState4) {
            this.f8615z0 = colorForState4;
            this.C0 = t3.a.c(this, this.D0, this.E0);
        } else {
            z11 = onStateChange;
        }
        if (z1(this.M)) {
            z11 |= this.M.setState(iArr);
        }
        if (z1(this.Y)) {
            z11 |= this.Y.setState(iArr);
        }
        if (z1(this.R)) {
            int[] iArr3 = new int[iArr.length + iArr2.length];
            System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
            System.arraycopy(iArr2, 0, iArr3, iArr.length, iArr2.length);
            z11 |= this.R.setState(iArr3);
        }
        if (RippleUtils.f36a && z1(this.S)) {
            z11 |= this.S.setState(iArr2);
        }
        if (z11) {
            invalidateSelf();
        }
        if (z10) {
            C1();
        }
        return z11;
    }

    private void E0(Canvas canvas, Rect rect) {
        if (this.M0) {
            return;
        }
        this.f8601l0.setColor(this.f8609t0);
        this.f8601l0.setStyle(Paint.Style.FILL);
        this.f8601l0.setColorFilter(s1());
        this.f8604o0.set(rect);
        canvas.drawRoundRect(this.f8604o0, P0(), P0(), this.f8601l0);
    }

    private void F0(Canvas canvas, Rect rect) {
        if (U2()) {
            s0(rect, this.f8604o0);
            RectF rectF = this.f8604o0;
            float f10 = rectF.left;
            float f11 = rectF.top;
            canvas.translate(f10, f11);
            this.M.setBounds(0, 0, (int) this.f8604o0.width(), (int) this.f8604o0.height());
            this.M.draw(canvas);
            canvas.translate(-f10, -f11);
        }
    }

    private void G0(Canvas canvas, Rect rect) {
        if (this.I <= 0.0f || this.M0) {
            return;
        }
        this.f8601l0.setColor(this.f8611v0);
        this.f8601l0.setStyle(Paint.Style.STROKE);
        if (!this.M0) {
            this.f8601l0.setColorFilter(s1());
        }
        RectF rectF = this.f8604o0;
        float f10 = rect.left;
        float f11 = this.I;
        rectF.set(f10 + (f11 / 2.0f), rect.top + (f11 / 2.0f), rect.right - (f11 / 2.0f), rect.bottom - (f11 / 2.0f));
        float f12 = this.G - (this.I / 2.0f);
        canvas.drawRoundRect(this.f8604o0, f12, f12, this.f8601l0);
    }

    private void H0(Canvas canvas, Rect rect) {
        if (this.M0) {
            return;
        }
        this.f8601l0.setColor(this.f8608s0);
        this.f8601l0.setStyle(Paint.Style.FILL);
        this.f8604o0.set(rect);
        canvas.drawRoundRect(this.f8604o0, P0(), P0(), this.f8601l0);
    }

    private void I0(Canvas canvas, Rect rect) {
        if (V2()) {
            v0(rect, this.f8604o0);
            RectF rectF = this.f8604o0;
            float f10 = rectF.left;
            float f11 = rectF.top;
            canvas.translate(f10, f11);
            this.R.setBounds(0, 0, (int) this.f8604o0.width(), (int) this.f8604o0.height());
            if (RippleUtils.f36a) {
                this.S.setBounds(this.R.getBounds());
                this.S.jumpToCurrentState();
                this.S.draw(canvas);
            } else {
                this.R.draw(canvas);
            }
            canvas.translate(-f10, -f11);
        }
    }

    private void J0(Canvas canvas, Rect rect) {
        this.f8601l0.setColor(this.f8612w0);
        this.f8601l0.setStyle(Paint.Style.FILL);
        this.f8604o0.set(rect);
        if (!this.M0) {
            canvas.drawRoundRect(this.f8604o0, P0(), P0(), this.f8601l0);
        } else {
            g(new RectF(rect), this.f8606q0);
            super.o(canvas, this.f8601l0, this.f8606q0, t());
        }
    }

    private void K0(Canvas canvas, Rect rect) {
        Paint paint = this.f8602m0;
        if (paint != null) {
            paint.setColor(ColorUtils.n(-16777216, 127));
            canvas.drawRect(rect, this.f8602m0);
            if (U2() || T2()) {
                s0(rect, this.f8604o0);
                canvas.drawRect(this.f8604o0, this.f8602m0);
            }
            if (this.K != null) {
                canvas.drawLine(rect.left, rect.exactCenterY(), rect.right, rect.exactCenterY(), this.f8602m0);
            }
            if (V2()) {
                v0(rect, this.f8604o0);
                canvas.drawRect(this.f8604o0, this.f8602m0);
            }
            this.f8602m0.setColor(ColorUtils.n(-65536, 127));
            u0(rect, this.f8604o0);
            canvas.drawRect(this.f8604o0, this.f8602m0);
            this.f8602m0.setColor(ColorUtils.n(-16711936, 127));
            w0(rect, this.f8604o0);
            canvas.drawRect(this.f8604o0, this.f8602m0);
        }
    }

    private void L0(Canvas canvas, Rect rect) {
        if (this.K != null) {
            Paint.Align A0 = A0(rect, this.f8605p0);
            y0(rect, this.f8604o0);
            if (this.f8607r0.getTextAppearance() != null) {
                this.f8607r0.getTextPaint().drawableState = getState();
                this.f8607r0.updateTextPaintDrawState(this.f8600k0);
            }
            this.f8607r0.getTextPaint().setTextAlign(A0);
            int i10 = 0;
            boolean z10 = Math.round(this.f8607r0.getTextWidth(o1().toString())) > Math.round(this.f8604o0.width());
            if (z10) {
                i10 = canvas.save();
                canvas.clipRect(this.f8604o0);
            }
            CharSequence charSequence = this.K;
            if (z10 && this.J0 != null) {
                charSequence = TextUtils.ellipsize(charSequence, this.f8607r0.getTextPaint(), this.f8604o0.width(), this.J0);
            }
            CharSequence charSequence2 = charSequence;
            int length = charSequence2.length();
            PointF pointF = this.f8605p0;
            canvas.drawText(charSequence2, 0, length, pointF.x, pointF.y, this.f8607r0.getTextPaint());
            if (z10) {
                canvas.restoreToCount(i10);
            }
        }
    }

    private boolean T2() {
        return this.X && this.Y != null && this.f8614y0;
    }

    private boolean U2() {
        return this.L && this.M != null;
    }

    private boolean V2() {
        return this.Q && this.R != null;
    }

    private void W2(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    private void X2() {
        this.H0 = this.G0 ? RippleUtils.d(this.J) : null;
    }

    @TargetApi(21)
    private void Y2() {
        this.S = new RippleDrawable(RippleUtils.d(m1()), this.R, O0);
    }

    private float g1() {
        Drawable drawable = this.f8614y0 ? this.Y : this.M;
        float f10 = this.O;
        if (f10 > 0.0f || drawable == null) {
            return f10;
        }
        float ceil = (float) Math.ceil(ViewUtils.dpToPx(this.f8600k0, 24));
        return ((float) drawable.getIntrinsicHeight()) <= ceil ? drawable.getIntrinsicHeight() : ceil;
    }

    private float h1() {
        Drawable drawable = this.f8614y0 ? this.Y : this.M;
        float f10 = this.O;
        return (f10 > 0.0f || drawable == null) ? f10 : drawable.getIntrinsicWidth();
    }

    private void i2(ColorStateList colorStateList) {
        if (this.D != colorStateList) {
            this.D = colorStateList;
            onStateChange(getState());
        }
    }

    private void r0(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setCallback(this);
        DrawableCompat.g(drawable, DrawableCompat.b(this));
        drawable.setLevel(getLevel());
        drawable.setVisible(isVisible(), false);
        if (drawable == this.R) {
            if (drawable.isStateful()) {
                drawable.setState(d1());
            }
            DrawableCompat.i(drawable, this.T);
            return;
        }
        Drawable drawable2 = this.M;
        if (drawable == drawable2 && this.P) {
            DrawableCompat.i(drawable2, this.N);
        }
        if (drawable.isStateful()) {
            drawable.setState(getState());
        }
    }

    private void s0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (U2() || T2()) {
            float f10 = this.f8592c0 + this.f8593d0;
            float h12 = h1();
            if (DrawableCompat.b(this) == 0) {
                float f11 = rect.left + f10;
                rectF.left = f11;
                rectF.right = f11 + h12;
            } else {
                float f12 = rect.right - f10;
                rectF.right = f12;
                rectF.left = f12 - h12;
            }
            float g12 = g1();
            float exactCenterY = rect.exactCenterY() - (g12 / 2.0f);
            rectF.top = exactCenterY;
            rectF.bottom = exactCenterY + g12;
        }
    }

    private ColorFilter s1() {
        ColorFilter colorFilter = this.B0;
        return colorFilter != null ? colorFilter : this.C0;
    }

    private void u0(Rect rect, RectF rectF) {
        rectF.set(rect);
        if (V2()) {
            float f10 = this.f8599j0 + this.f8598i0 + this.U + this.f8597h0 + this.f8596g0;
            if (DrawableCompat.b(this) == 0) {
                rectF.right = rect.right - f10;
            } else {
                rectF.left = rect.left + f10;
            }
        }
    }

    private static boolean u1(int[] iArr, int i10) {
        if (iArr == null) {
            return false;
        }
        for (int i11 : iArr) {
            if (i11 == i10) {
                return true;
            }
        }
        return false;
    }

    private void v0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (V2()) {
            float f10 = this.f8599j0 + this.f8598i0;
            if (DrawableCompat.b(this) == 0) {
                float f11 = rect.right - f10;
                rectF.right = f11;
                rectF.left = f11 - this.U;
            } else {
                float f12 = rect.left + f10;
                rectF.left = f12;
                rectF.right = f12 + this.U;
            }
            float exactCenterY = rect.exactCenterY();
            float f13 = this.U;
            float f14 = exactCenterY - (f13 / 2.0f);
            rectF.top = f14;
            rectF.bottom = f14 + f13;
        }
    }

    private void w0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (V2()) {
            float f10 = this.f8599j0 + this.f8598i0 + this.U + this.f8597h0 + this.f8596g0;
            if (DrawableCompat.b(this) == 0) {
                float f11 = rect.right;
                rectF.right = f11;
                rectF.left = f11 - f10;
            } else {
                int i10 = rect.left;
                rectF.left = i10;
                rectF.right = i10 + f10;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    private void y0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.K != null) {
            float t02 = this.f8592c0 + t0() + this.f8595f0;
            float x02 = this.f8599j0 + x0() + this.f8596g0;
            if (DrawableCompat.b(this) == 0) {
                rectF.left = rect.left + t02;
                rectF.right = rect.right - x02;
            } else {
                rectF.left = rect.left + x02;
                rectF.right = rect.right - t02;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    private static boolean y1(ColorStateList colorStateList) {
        return colorStateList != null && colorStateList.isStateful();
    }

    private float z0() {
        this.f8607r0.getTextPaint().getFontMetrics(this.f8603n0);
        Paint.FontMetrics fontMetrics = this.f8603n0;
        return (fontMetrics.descent + fontMetrics.ascent) / 2.0f;
    }

    private static boolean z1(Drawable drawable) {
        return drawable != null && drawable.isStateful();
    }

    Paint.Align A0(Rect rect, PointF pointF) {
        pointF.set(0.0f, 0.0f);
        Paint.Align align = Paint.Align.LEFT;
        if (this.K != null) {
            float t02 = this.f8592c0 + t0() + this.f8595f0;
            if (DrawableCompat.b(this) == 0) {
                pointF.x = rect.left + t02;
                align = Paint.Align.LEFT;
            } else {
                pointF.x = rect.right - t02;
                align = Paint.Align.RIGHT;
            }
            pointF.y = rect.centerY() - z0();
        }
        return align;
    }

    public void A2(float f10) {
        if (this.f8594e0 != f10) {
            float t02 = t0();
            this.f8594e0 = f10;
            float t03 = t0();
            invalidateSelf();
            if (t02 != t03) {
                C1();
            }
        }
    }

    public void B2(int i10) {
        A2(this.f8600k0.getResources().getDimension(i10));
    }

    protected void C1() {
        a aVar = this.I0.get();
        if (aVar != null) {
            aVar.a();
        }
    }

    public void C2(float f10) {
        if (this.f8593d0 != f10) {
            float t02 = t0();
            this.f8593d0 = f10;
            float t03 = t0();
            invalidateSelf();
            if (t02 != t03) {
                C1();
            }
        }
    }

    public void D2(int i10) {
        C2(this.f8600k0.getResources().getDimension(i10));
    }

    public void E1(boolean z10) {
        if (this.W != z10) {
            this.W = z10;
            float t02 = t0();
            if (!z10 && this.f8614y0) {
                this.f8614y0 = false;
            }
            float t03 = t0();
            invalidateSelf();
            if (t02 != t03) {
                C1();
            }
        }
    }

    public void E2(int i10) {
        this.L0 = i10;
    }

    public void F1(int i10) {
        E1(this.f8600k0.getResources().getBoolean(i10));
    }

    public void F2(ColorStateList colorStateList) {
        if (this.J != colorStateList) {
            this.J = colorStateList;
            X2();
            onStateChange(getState());
        }
    }

    public void G1(Drawable drawable) {
        if (this.Y != drawable) {
            float t02 = t0();
            this.Y = drawable;
            float t03 = t0();
            W2(this.Y);
            r0(this.Y);
            invalidateSelf();
            if (t02 != t03) {
                C1();
            }
        }
    }

    public void G2(int i10) {
        F2(AppCompatResources.a(this.f8600k0, i10));
    }

    public void H1(int i10) {
        G1(AppCompatResources.b(this.f8600k0, i10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H2(boolean z10) {
        this.K0 = z10;
    }

    public void I1(ColorStateList colorStateList) {
        if (this.Z != colorStateList) {
            this.Z = colorStateList;
            if (B0()) {
                DrawableCompat.i(this.Y, colorStateList);
            }
            onStateChange(getState());
        }
    }

    public void I2(MotionSpec motionSpec) {
        this.f8590a0 = motionSpec;
    }

    public void J1(int i10) {
        I1(AppCompatResources.a(this.f8600k0, i10));
    }

    public void J2(int i10) {
        I2(MotionSpec.d(this.f8600k0, i10));
    }

    public void K1(int i10) {
        L1(this.f8600k0.getResources().getBoolean(i10));
    }

    public void K2(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = "";
        }
        if (TextUtils.equals(this.K, charSequence)) {
            return;
        }
        this.K = charSequence;
        this.f8607r0.setTextWidthDirty(true);
        invalidateSelf();
        C1();
    }

    public void L1(boolean z10) {
        if (this.X != z10) {
            boolean T2 = T2();
            this.X = z10;
            boolean T22 = T2();
            if (T2 != T22) {
                if (T22) {
                    r0(this.Y);
                } else {
                    W2(this.Y);
                }
                invalidateSelf();
                C1();
            }
        }
    }

    public void L2(TextAppearance textAppearance) {
        this.f8607r0.setTextAppearance(textAppearance, this.f8600k0);
    }

    public Drawable M0() {
        return this.Y;
    }

    public void M1(ColorStateList colorStateList) {
        if (this.E != colorStateList) {
            this.E = colorStateList;
            onStateChange(getState());
        }
    }

    public void M2(int i10) {
        L2(new TextAppearance(this.f8600k0, i10));
    }

    public ColorStateList N0() {
        return this.Z;
    }

    public void N1(int i10) {
        M1(AppCompatResources.a(this.f8600k0, i10));
    }

    public void N2(float f10) {
        if (this.f8596g0 != f10) {
            this.f8596g0 = f10;
            invalidateSelf();
            C1();
        }
    }

    public ColorStateList O0() {
        return this.E;
    }

    @Deprecated
    public void O1(float f10) {
        if (this.G != f10) {
            this.G = f10;
            setShapeAppearanceModel(D().w(f10));
        }
    }

    public void O2(int i10) {
        N2(this.f8600k0.getResources().getDimension(i10));
    }

    public float P0() {
        return this.M0 ? I() : this.G;
    }

    @Deprecated
    public void P1(int i10) {
        O1(this.f8600k0.getResources().getDimension(i10));
    }

    public void P2(float f10) {
        if (this.f8595f0 != f10) {
            this.f8595f0 = f10;
            invalidateSelf();
            C1();
        }
    }

    public float Q0() {
        return this.f8599j0;
    }

    public void Q1(float f10) {
        if (this.f8599j0 != f10) {
            this.f8599j0 = f10;
            invalidateSelf();
            C1();
        }
    }

    public void Q2(int i10) {
        P2(this.f8600k0.getResources().getDimension(i10));
    }

    public Drawable R0() {
        Drawable drawable = this.M;
        if (drawable != null) {
            return DrawableCompat.k(drawable);
        }
        return null;
    }

    public void R1(int i10) {
        Q1(this.f8600k0.getResources().getDimension(i10));
    }

    public void R2(boolean z10) {
        if (this.G0 != z10) {
            this.G0 = z10;
            X2();
            onStateChange(getState());
        }
    }

    public float S0() {
        return this.O;
    }

    public void S1(Drawable drawable) {
        Drawable R0 = R0();
        if (R0 != drawable) {
            float t02 = t0();
            this.M = drawable != null ? DrawableCompat.l(drawable).mutate() : null;
            float t03 = t0();
            W2(R0);
            if (U2()) {
                r0(this.M);
            }
            invalidateSelf();
            if (t02 != t03) {
                C1();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean S2() {
        return this.K0;
    }

    public ColorStateList T0() {
        return this.N;
    }

    public void T1(int i10) {
        S1(AppCompatResources.b(this.f8600k0, i10));
    }

    public float U0() {
        return this.F;
    }

    public void U1(float f10) {
        if (this.O != f10) {
            float t02 = t0();
            this.O = f10;
            float t03 = t0();
            invalidateSelf();
            if (t02 != t03) {
                C1();
            }
        }
    }

    public float V0() {
        return this.f8592c0;
    }

    public void V1(int i10) {
        U1(this.f8600k0.getResources().getDimension(i10));
    }

    public ColorStateList W0() {
        return this.H;
    }

    public void W1(ColorStateList colorStateList) {
        this.P = true;
        if (this.N != colorStateList) {
            this.N = colorStateList;
            if (U2()) {
                DrawableCompat.i(this.M, colorStateList);
            }
            onStateChange(getState());
        }
    }

    public float X0() {
        return this.I;
    }

    public void X1(int i10) {
        W1(AppCompatResources.a(this.f8600k0, i10));
    }

    public Drawable Y0() {
        Drawable drawable = this.R;
        if (drawable != null) {
            return DrawableCompat.k(drawable);
        }
        return null;
    }

    public void Y1(int i10) {
        Z1(this.f8600k0.getResources().getBoolean(i10));
    }

    public CharSequence Z0() {
        return this.V;
    }

    public void Z1(boolean z10) {
        if (this.L != z10) {
            boolean U2 = U2();
            this.L = z10;
            boolean U22 = U2();
            if (U2 != U22) {
                if (U22) {
                    r0(this.M);
                } else {
                    W2(this.M);
                }
                invalidateSelf();
                C1();
            }
        }
    }

    public float a1() {
        return this.f8598i0;
    }

    public void a2(float f10) {
        if (this.F != f10) {
            this.F = f10;
            invalidateSelf();
            C1();
        }
    }

    public float b1() {
        return this.U;
    }

    public void b2(int i10) {
        a2(this.f8600k0.getResources().getDimension(i10));
    }

    public float c1() {
        return this.f8597h0;
    }

    public void c2(float f10) {
        if (this.f8592c0 != f10) {
            this.f8592c0 = f10;
            invalidateSelf();
            C1();
        }
    }

    public int[] d1() {
        return this.F0;
    }

    public void d2(int i10) {
        c2(this.f8600k0.getResources().getDimension(i10));
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty() || getAlpha() == 0) {
            return;
        }
        int i10 = this.A0;
        int a10 = i10 < 255 ? CanvasCompat.a(canvas, bounds.left, bounds.top, bounds.right, bounds.bottom, i10) : 0;
        H0(canvas, bounds);
        E0(canvas, bounds);
        if (this.M0) {
            super.draw(canvas);
        }
        G0(canvas, bounds);
        J0(canvas, bounds);
        F0(canvas, bounds);
        D0(canvas, bounds);
        if (this.K0) {
            L0(canvas, bounds);
        }
        I0(canvas, bounds);
        K0(canvas, bounds);
        if (this.A0 < 255) {
            canvas.restoreToCount(a10);
        }
    }

    public ColorStateList e1() {
        return this.T;
    }

    public void e2(ColorStateList colorStateList) {
        if (this.H != colorStateList) {
            this.H = colorStateList;
            if (this.M0) {
                m0(colorStateList);
            }
            onStateChange(getState());
        }
    }

    public void f1(RectF rectF) {
        w0(getBounds(), rectF);
    }

    public void f2(int i10) {
        e2(AppCompatResources.a(this.f8600k0, i10));
    }

    public void g2(float f10) {
        if (this.I != f10) {
            this.I = f10;
            this.f8601l0.setStrokeWidth(f10);
            if (this.M0) {
                super.n0(f10);
            }
            invalidateSelf();
        }
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.A0;
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.B0;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return (int) this.F;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return Math.min(Math.round(this.f8592c0 + t0() + this.f8595f0 + this.f8607r0.getTextWidth(o1().toString()) + this.f8596g0 + x0() + this.f8599j0), this.L0);
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    @TargetApi(21)
    public void getOutline(Outline outline) {
        if (this.M0) {
            super.getOutline(outline);
            return;
        }
        Rect bounds = getBounds();
        if (!bounds.isEmpty()) {
            outline.setRoundRect(bounds, this.G);
        } else {
            outline.setRoundRect(0, 0, getIntrinsicWidth(), getIntrinsicHeight(), this.G);
        }
        outline.setAlpha(getAlpha() / 255.0f);
    }

    public void h2(int i10) {
        g2(this.f8600k0.getResources().getDimension(i10));
    }

    public TextUtils.TruncateAt i1() {
        return this.J0;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public boolean isStateful() {
        return y1(this.D) || y1(this.E) || y1(this.H) || (this.G0 && y1(this.H0)) || A1(this.f8607r0.getTextAppearance()) || B0() || z1(this.M) || z1(this.Y) || y1(this.D0);
    }

    public MotionSpec j1() {
        return this.f8591b0;
    }

    public void j2(Drawable drawable) {
        Drawable Y0 = Y0();
        if (Y0 != drawable) {
            float x02 = x0();
            this.R = drawable != null ? DrawableCompat.l(drawable).mutate() : null;
            if (RippleUtils.f36a) {
                Y2();
            }
            float x03 = x0();
            W2(Y0);
            if (V2()) {
                r0(this.R);
            }
            invalidateSelf();
            if (x02 != x03) {
                C1();
            }
        }
    }

    public float k1() {
        return this.f8594e0;
    }

    public void k2(CharSequence charSequence) {
        if (this.V != charSequence) {
            this.V = BidiFormatter.c().h(charSequence);
            invalidateSelf();
        }
    }

    public float l1() {
        return this.f8593d0;
    }

    public void l2(float f10) {
        if (this.f8598i0 != f10) {
            this.f8598i0 = f10;
            invalidateSelf();
            if (V2()) {
                C1();
            }
        }
    }

    public ColorStateList m1() {
        return this.J;
    }

    public void m2(int i10) {
        l2(this.f8600k0.getResources().getDimension(i10));
    }

    public MotionSpec n1() {
        return this.f8590a0;
    }

    public void n2(int i10) {
        j2(AppCompatResources.b(this.f8600k0, i10));
    }

    public CharSequence o1() {
        return this.K;
    }

    public void o2(float f10) {
        if (this.U != f10) {
            this.U = f10;
            invalidateSelf();
            if (V2()) {
                C1();
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int i10) {
        boolean onLayoutDirectionChanged = super.onLayoutDirectionChanged(i10);
        if (U2()) {
            onLayoutDirectionChanged |= DrawableCompat.g(this.M, i10);
        }
        if (T2()) {
            onLayoutDirectionChanged |= DrawableCompat.g(this.Y, i10);
        }
        if (V2()) {
            onLayoutDirectionChanged |= DrawableCompat.g(this.R, i10);
        }
        if (!onLayoutDirectionChanged) {
            return true;
        }
        invalidateSelf();
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i10) {
        boolean onLevelChange = super.onLevelChange(i10);
        if (U2()) {
            onLevelChange |= this.M.setLevel(i10);
        }
        if (T2()) {
            onLevelChange |= this.Y.setLevel(i10);
        }
        if (V2()) {
            onLevelChange |= this.R.setLevel(i10);
        }
        if (onLevelChange) {
            invalidateSelf();
        }
        return onLevelChange;
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        if (this.M0) {
            super.onStateChange(iArr);
        }
        return D1(iArr, d1());
    }

    @Override // com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public void onTextSizeChange() {
        C1();
        invalidateSelf();
    }

    public TextAppearance p1() {
        return this.f8607r0.getTextAppearance();
    }

    public void p2(int i10) {
        o2(this.f8600k0.getResources().getDimension(i10));
    }

    public float q1() {
        return this.f8596g0;
    }

    public void q2(float f10) {
        if (this.f8597h0 != f10) {
            this.f8597h0 = f10;
            invalidateSelf();
            if (V2()) {
                C1();
            }
        }
    }

    public float r1() {
        return this.f8595f0;
    }

    public void r2(int i10) {
        q2(this.f8600k0.getResources().getDimension(i10));
    }

    public boolean s2(int[] iArr) {
        if (Arrays.equals(this.F0, iArr)) {
            return false;
        }
        this.F0 = iArr;
        if (V2()) {
            return D1(getState(), iArr);
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j10) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j10);
        }
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        if (this.A0 != i10) {
            this.A0 = i10;
            invalidateSelf();
        }
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.B0 != colorFilter) {
            this.B0 = colorFilter;
            invalidateSelf();
        }
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        if (this.D0 != colorStateList) {
            this.D0 = colorStateList;
            onStateChange(getState());
        }
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        if (this.E0 != mode) {
            this.E0 = mode;
            this.C0 = t3.a.c(this, this.D0, mode);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z10, boolean z11) {
        boolean visible = super.setVisible(z10, z11);
        if (U2()) {
            visible |= this.M.setVisible(z10, z11);
        }
        if (T2()) {
            visible |= this.Y.setVisible(z10, z11);
        }
        if (V2()) {
            visible |= this.R.setVisible(z10, z11);
        }
        if (visible) {
            invalidateSelf();
        }
        return visible;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float t0() {
        if (U2() || T2()) {
            return this.f8593d0 + h1() + this.f8594e0;
        }
        return 0.0f;
    }

    public boolean t1() {
        return this.G0;
    }

    public void t2(ColorStateList colorStateList) {
        if (this.T != colorStateList) {
            this.T = colorStateList;
            if (V2()) {
                DrawableCompat.i(this.R, colorStateList);
            }
            onStateChange(getState());
        }
    }

    public void u2(int i10) {
        t2(AppCompatResources.a(this.f8600k0, i10));
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }

    public boolean v1() {
        return this.W;
    }

    public void v2(boolean z10) {
        if (this.Q != z10) {
            boolean V2 = V2();
            this.Q = z10;
            boolean V22 = V2();
            if (V2 != V22) {
                if (V22) {
                    r0(this.R);
                } else {
                    W2(this.R);
                }
                invalidateSelf();
                C1();
            }
        }
    }

    public boolean w1() {
        return z1(this.R);
    }

    public void w2(a aVar) {
        this.I0 = new WeakReference<>(aVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float x0() {
        if (V2()) {
            return this.f8597h0 + this.U + this.f8598i0;
        }
        return 0.0f;
    }

    public boolean x1() {
        return this.Q;
    }

    public void x2(TextUtils.TruncateAt truncateAt) {
        this.J0 = truncateAt;
    }

    public void y2(MotionSpec motionSpec) {
        this.f8591b0 = motionSpec;
    }

    public void z2(int i10) {
        y2(MotionSpec.d(this.f8600k0, i10));
    }
}
