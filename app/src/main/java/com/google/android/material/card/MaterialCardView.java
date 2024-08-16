package com.google.android.material.card;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.FrameLayout;
import androidx.cardview.widget.CardView;
import c.AppCompatResources;
import c4.MaterialShapeUtils;
import c4.ShapeAppearanceModel;
import c4.Shapeable;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import d4.MaterialThemeOverlay;

/* loaded from: classes.dex */
public class MaterialCardView extends CardView implements Checkable, Shapeable {

    /* renamed from: s, reason: collision with root package name */
    private static final int[] f8519s = {R.attr.state_checkable};

    /* renamed from: t, reason: collision with root package name */
    private static final int[] f8520t = {16842912};

    /* renamed from: u, reason: collision with root package name */
    private static final int[] f8521u = {R$attr.state_dragged};

    /* renamed from: v, reason: collision with root package name */
    private static final int f8522v = R$style.Widget_MaterialComponents_CardView;

    /* renamed from: n, reason: collision with root package name */
    private final MaterialCardViewHelper f8523n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f8524o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f8525p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f8526q;

    /* renamed from: r, reason: collision with root package name */
    private a f8527r;

    /* loaded from: classes.dex */
    public interface a {
        void a(MaterialCardView materialCardView, boolean z10);
    }

    public MaterialCardView(Context context) {
        this(context, null);
    }

    private RectF getBoundsAsRectF() {
        RectF rectF = new RectF();
        rectF.set(this.f8523n.j().getBounds());
        return rectF;
    }

    private void h() {
        this.f8523n.i();
    }

    @Override // androidx.cardview.widget.CardView
    public ColorStateList getCardBackgroundColor() {
        return this.f8523n.k();
    }

    public ColorStateList getCardForegroundColor() {
        return this.f8523n.l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCardViewRadius() {
        return super.getRadius();
    }

    public Drawable getCheckedIcon() {
        return this.f8523n.m();
    }

    public int getCheckedIconGravity() {
        return this.f8523n.n();
    }

    public int getCheckedIconMargin() {
        return this.f8523n.o();
    }

    public int getCheckedIconSize() {
        return this.f8523n.p();
    }

    public ColorStateList getCheckedIconTint() {
        return this.f8523n.q();
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingBottom() {
        return this.f8523n.A().bottom;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingLeft() {
        return this.f8523n.A().left;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingRight() {
        return this.f8523n.A().right;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingTop() {
        return this.f8523n.A().top;
    }

    public float getProgress() {
        return this.f8523n.u();
    }

    @Override // androidx.cardview.widget.CardView
    public float getRadius() {
        return this.f8523n.s();
    }

    public ColorStateList getRippleColor() {
        return this.f8523n.v();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.f8523n.w();
    }

    @Deprecated
    public int getStrokeColor() {
        return this.f8523n.x();
    }

    public ColorStateList getStrokeColorStateList() {
        return this.f8523n.y();
    }

    public int getStrokeWidth() {
        return this.f8523n.z();
    }

    public boolean i() {
        MaterialCardViewHelper materialCardViewHelper = this.f8523n;
        return materialCardViewHelper != null && materialCardViewHelper.D();
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.f8525p;
    }

    public boolean j() {
        return this.f8526q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(int i10, int i11, int i12, int i13) {
        super.f(i10, i11, i12, i13);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.f(this, this.f8523n.j());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int i10) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 3);
        if (i()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, f8519s);
        }
        if (isChecked()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, f8520t);
        }
        if (j()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, f8521u);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("androidx.cardview.widget.CardView");
        accessibilityEvent.setChecked(isChecked());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("androidx.cardview.widget.CardView");
        accessibilityNodeInfo.setCheckable(i());
        accessibilityNodeInfo.setClickable(isClickable());
        accessibilityNodeInfo.setChecked(isChecked());
    }

    @Override // androidx.cardview.widget.CardView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        this.f8523n.H(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (this.f8524o) {
            if (!this.f8523n.C()) {
                Log.i("MaterialCardView", "Setting a custom background is not supported.");
                this.f8523n.I(true);
            }
            super.setBackgroundDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundInternal(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardBackgroundColor(int i10) {
        this.f8523n.J(ColorStateList.valueOf(i10));
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardElevation(float f10) {
        super.setCardElevation(f10);
        this.f8523n.d0();
    }

    public void setCardForegroundColor(ColorStateList colorStateList) {
        this.f8523n.K(colorStateList);
    }

    public void setCheckable(boolean z10) {
        this.f8523n.L(z10);
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z10) {
        if (this.f8525p != z10) {
            toggle();
        }
    }

    public void setCheckedIcon(Drawable drawable) {
        this.f8523n.N(drawable);
    }

    public void setCheckedIconGravity(int i10) {
        if (this.f8523n.n() != i10) {
            this.f8523n.O(i10);
        }
    }

    public void setCheckedIconMargin(int i10) {
        this.f8523n.P(i10);
    }

    public void setCheckedIconMarginResource(int i10) {
        if (i10 != -1) {
            this.f8523n.P(getResources().getDimensionPixelSize(i10));
        }
    }

    public void setCheckedIconResource(int i10) {
        this.f8523n.N(AppCompatResources.b(getContext(), i10));
    }

    public void setCheckedIconSize(int i10) {
        this.f8523n.Q(i10);
    }

    public void setCheckedIconSizeResource(int i10) {
        if (i10 != 0) {
            this.f8523n.Q(getResources().getDimensionPixelSize(i10));
        }
    }

    public void setCheckedIconTint(ColorStateList colorStateList) {
        this.f8523n.R(colorStateList);
    }

    @Override // android.view.View
    public void setClickable(boolean z10) {
        super.setClickable(z10);
        MaterialCardViewHelper materialCardViewHelper = this.f8523n;
        if (materialCardViewHelper != null) {
            materialCardViewHelper.b0();
        }
    }

    public void setDragged(boolean z10) {
        if (this.f8526q != z10) {
            this.f8526q = z10;
            refreshDrawableState();
            h();
            invalidate();
        }
    }

    @Override // androidx.cardview.widget.CardView
    public void setMaxCardElevation(float f10) {
        super.setMaxCardElevation(f10);
        this.f8523n.f0();
    }

    public void setOnCheckedChangeListener(a aVar) {
        this.f8527r = aVar;
    }

    @Override // androidx.cardview.widget.CardView
    public void setPreventCornerOverlap(boolean z10) {
        super.setPreventCornerOverlap(z10);
        this.f8523n.f0();
        this.f8523n.c0();
    }

    public void setProgress(float f10) {
        this.f8523n.T(f10);
    }

    @Override // androidx.cardview.widget.CardView
    public void setRadius(float f10) {
        super.setRadius(f10);
        this.f8523n.S(f10);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        this.f8523n.U(colorStateList);
    }

    public void setRippleColorResource(int i10) {
        this.f8523n.U(AppCompatResources.a(getContext(), i10));
    }

    @Override // c4.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        setClipToOutline(shapeAppearanceModel.u(getBoundsAsRectF()));
        this.f8523n.V(shapeAppearanceModel);
    }

    public void setStrokeColor(int i10) {
        setStrokeColor(ColorStateList.valueOf(i10));
    }

    public void setStrokeWidth(int i10) {
        this.f8523n.X(i10);
        invalidate();
    }

    @Override // androidx.cardview.widget.CardView
    public void setUseCompatPadding(boolean z10) {
        super.setUseCompatPadding(z10);
        this.f8523n.f0();
        this.f8523n.c0();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        if (i() && isEnabled()) {
            this.f8525p = !this.f8525p;
            refreshDrawableState();
            h();
            this.f8523n.M(this.f8525p);
            a aVar = this.f8527r;
            if (aVar != null) {
                aVar.a(this, this.f8525p);
            }
        }
    }

    public MaterialCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.materialCardViewStyle);
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardBackgroundColor(ColorStateList colorStateList) {
        this.f8523n.J(colorStateList);
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        this.f8523n.W(colorStateList);
        invalidate();
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MaterialCardView(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r6), attributeSet, i10);
        int i11 = f8522v;
        this.f8525p = false;
        this.f8526q = false;
        this.f8524o = true;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(getContext(), attributeSet, R$styleable.MaterialCardView, i10, i11, new int[0]);
        MaterialCardViewHelper materialCardViewHelper = new MaterialCardViewHelper(this, attributeSet, i10, i11);
        this.f8523n = materialCardViewHelper;
        materialCardViewHelper.J(super.getCardBackgroundColor());
        materialCardViewHelper.Y(super.getContentPaddingLeft(), super.getContentPaddingTop(), super.getContentPaddingRight(), super.getContentPaddingBottom());
        materialCardViewHelper.G(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }
}
