package e4;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import c4.EdgeTreatment;
import c4.MarkerEdgeTreatment;
import c4.MaterialShapeDrawable;
import c4.OffsetEdgeTreatment;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import p3.AnimationUtils;
import r3.MaterialColors;
import z3.MaterialResources;
import z3.TextAppearance;

/* compiled from: TooltipDrawable.java */
/* renamed from: e4.a, reason: use source file name */
/* loaded from: classes.dex */
public class TooltipDrawable extends MaterialShapeDrawable implements TextDrawableHelper.TextDrawableDelegate {
    private static final int U = R$style.Widget_MaterialComponents_Tooltip;
    private static final int V = R$attr.tooltipStyle;
    private CharSequence D;
    private final Context E;
    private final Paint.FontMetrics F;
    private final TextDrawableHelper G;
    private final View.OnLayoutChangeListener H;
    private final Rect I;
    private int J;
    private int K;
    private int L;
    private int M;
    private int N;
    private int O;
    private float P;
    private float Q;
    private final float R;
    private float S;
    private float T;

    /* compiled from: TooltipDrawable.java */
    /* renamed from: e4.a$a */
    /* loaded from: classes.dex */
    class a implements View.OnLayoutChangeListener {
        a() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            TooltipDrawable.this.F0(view);
        }
    }

    private TooltipDrawable(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.F = new Paint.FontMetrics();
        TextDrawableHelper textDrawableHelper = new TextDrawableHelper(this);
        this.G = textDrawableHelper;
        this.H = new a();
        this.I = new Rect();
        this.P = 1.0f;
        this.Q = 1.0f;
        this.R = 0.5f;
        this.S = 0.5f;
        this.T = 1.0f;
        this.E = context;
        textDrawableHelper.getTextPaint().density = context.getResources().getDisplayMetrics().density;
        textDrawableHelper.getTextPaint().setTextAlign(Paint.Align.CENTER);
    }

    private void A0(AttributeSet attributeSet, int i10, int i11) {
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(this.E, attributeSet, R$styleable.Tooltip, i10, i11, new int[0]);
        this.N = this.E.getResources().getDimensionPixelSize(R$dimen.mtrl_tooltip_arrowSize);
        setShapeAppearanceModel(D().v().s(w0()).m());
        D0(obtainStyledAttributes.getText(R$styleable.Tooltip_android_text));
        TextAppearance g6 = MaterialResources.g(this.E, obtainStyledAttributes, R$styleable.Tooltip_android_textAppearance);
        if (g6 != null) {
            int i12 = R$styleable.Tooltip_android_textColor;
            if (obtainStyledAttributes.hasValue(i12)) {
                g6.k(MaterialResources.a(this.E, obtainStyledAttributes, i12));
            }
        }
        E0(g6);
        a0(ColorStateList.valueOf(obtainStyledAttributes.getColor(R$styleable.Tooltip_backgroundTint, MaterialColors.g(ColorUtils.n(MaterialColors.c(this.E, R.attr.colorBackground, TooltipDrawable.class.getCanonicalName()), 229), ColorUtils.n(MaterialColors.c(this.E, R$attr.colorOnBackground, TooltipDrawable.class.getCanonicalName()), 153)))));
        m0(ColorStateList.valueOf(MaterialColors.c(this.E, R$attr.colorSurface, TooltipDrawable.class.getCanonicalName())));
        this.J = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Tooltip_android_padding, 0);
        this.K = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Tooltip_android_minWidth, 0);
        this.L = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Tooltip_android_minHeight, 0);
        this.M = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Tooltip_android_layout_margin, 0);
        obtainStyledAttributes.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F0(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        this.O = iArr[0];
        view.getWindowVisibleDisplayFrame(this.I);
    }

    private float s0() {
        int i10;
        if (((this.I.right - getBounds().right) - this.O) - this.M < 0) {
            i10 = ((this.I.right - getBounds().right) - this.O) - this.M;
        } else {
            if (((this.I.left - getBounds().left) - this.O) + this.M <= 0) {
                return 0.0f;
            }
            i10 = ((this.I.left - getBounds().left) - this.O) + this.M;
        }
        return i10;
    }

    private float t0() {
        this.G.getTextPaint().getFontMetrics(this.F);
        Paint.FontMetrics fontMetrics = this.F;
        return (fontMetrics.descent + fontMetrics.ascent) / 2.0f;
    }

    private float u0(Rect rect) {
        return rect.centerY() - t0();
    }

    public static TooltipDrawable v0(Context context, AttributeSet attributeSet, int i10, int i11) {
        TooltipDrawable tooltipDrawable = new TooltipDrawable(context, attributeSet, i10, i11);
        tooltipDrawable.A0(attributeSet, i10, i11);
        return tooltipDrawable;
    }

    private EdgeTreatment w0() {
        float f10 = -s0();
        float width = ((float) (getBounds().width() - (this.N * Math.sqrt(2.0d)))) / 2.0f;
        return new OffsetEdgeTreatment(new MarkerEdgeTreatment(this.N), Math.min(Math.max(f10, -width), width));
    }

    private void y0(Canvas canvas) {
        if (this.D == null) {
            return;
        }
        int u02 = (int) u0(getBounds());
        if (this.G.getTextAppearance() != null) {
            this.G.getTextPaint().drawableState = getState();
            this.G.updateTextPaintDrawState(this.E);
            this.G.getTextPaint().setAlpha((int) (this.T * 255.0f));
        }
        CharSequence charSequence = this.D;
        canvas.drawText(charSequence, 0, charSequence.length(), r0.centerX(), u02, this.G.getTextPaint());
    }

    private float z0() {
        CharSequence charSequence = this.D;
        if (charSequence == null) {
            return 0.0f;
        }
        return this.G.getTextWidth(charSequence.toString());
    }

    public void B0(View view) {
        if (view == null) {
            return;
        }
        F0(view);
        view.addOnLayoutChangeListener(this.H);
    }

    public void C0(float f10) {
        this.S = 1.2f;
        this.P = f10;
        this.Q = f10;
        this.T = AnimationUtils.b(0.0f, 1.0f, 0.19f, 1.0f, f10);
        invalidateSelf();
    }

    public void D0(CharSequence charSequence) {
        if (TextUtils.equals(this.D, charSequence)) {
            return;
        }
        this.D = charSequence;
        this.G.setTextWidthDirty(true);
        invalidateSelf();
    }

    public void E0(TextAppearance textAppearance) {
        this.G.setTextAppearance(textAppearance, this.E);
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.save();
        float s02 = s0();
        float f10 = (float) (-((this.N * Math.sqrt(2.0d)) - this.N));
        canvas.scale(this.P, this.Q, getBounds().left + (getBounds().width() * 0.5f), getBounds().top + (getBounds().height() * this.S));
        canvas.translate(s02, f10);
        super.draw(canvas);
        y0(canvas);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return (int) Math.max(this.G.getTextPaint().getTextSize(), this.L);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return (int) Math.max((this.J * 2) + z0(), this.K);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        setShapeAppearanceModel(D().v().s(w0()).m());
    }

    @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        return super.onStateChange(iArr);
    }

    @Override // com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public void onTextSizeChange() {
        invalidateSelf();
    }

    public void x0(View view) {
        if (view == null) {
            return;
        }
        view.removeOnLayoutChangeListener(this.H);
    }
}
