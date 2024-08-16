package com.google.android.material.chip;

import a4.RippleUtils;
import android.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import c4.MaterialShapeUtils;
import c4.ShapeAppearanceModel;
import c4.Shapeable;
import com.google.android.material.R$attr;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.internal.MaterialCheckable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.oplus.statistics.DataTypeConstants;
import d4.MaterialThemeOverlay;
import java.util.List;
import p3.MotionSpec;
import z3.TextAppearance;
import z3.TextAppearanceFontCallback;

/* loaded from: classes.dex */
public class Chip extends AppCompatCheckBox implements ChipDrawable.a, Shapeable, MaterialCheckable<Chip> {
    private static final int B = R$style.Widget_MaterialComponents_Chip_Action;
    private static final Rect C = new Rect();
    private static final int[] D = {R.attr.state_selected};
    private static final int[] E = {R.attr.state_checkable};
    private final TextAppearanceFontCallback A;

    /* renamed from: i, reason: collision with root package name */
    private ChipDrawable f8556i;

    /* renamed from: j, reason: collision with root package name */
    private InsetDrawable f8557j;

    /* renamed from: k, reason: collision with root package name */
    private RippleDrawable f8558k;

    /* renamed from: l, reason: collision with root package name */
    private View.OnClickListener f8559l;

    /* renamed from: m, reason: collision with root package name */
    private CompoundButton.OnCheckedChangeListener f8560m;

    /* renamed from: n, reason: collision with root package name */
    private MaterialCheckable.OnCheckedChangeListener<Chip> f8561n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f8562o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f8563p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f8564q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f8565r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f8566s;

    /* renamed from: t, reason: collision with root package name */
    private int f8567t;

    /* renamed from: u, reason: collision with root package name */
    private int f8568u;

    /* renamed from: v, reason: collision with root package name */
    private CharSequence f8569v;

    /* renamed from: w, reason: collision with root package name */
    private final d f8570w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f8571x;

    /* renamed from: y, reason: collision with root package name */
    private final Rect f8572y;

    /* renamed from: z, reason: collision with root package name */
    private final RectF f8573z;

    /* loaded from: classes.dex */
    class a extends TextAppearanceFontCallback {
        a() {
        }

        @Override // z3.TextAppearanceFontCallback
        public void onFontRetrievalFailed(int i10) {
        }

        @Override // z3.TextAppearanceFontCallback
        public void onFontRetrieved(Typeface typeface, boolean z10) {
            Chip chip = Chip.this;
            chip.setText(chip.f8556i.S2() ? Chip.this.f8556i.o1() : Chip.this.getText());
            Chip.this.requestLayout();
            Chip.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    class b implements CompoundButton.OnCheckedChangeListener {
        b() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z10) {
            if (Chip.this.f8561n != null) {
                Chip.this.f8561n.onCheckedChanged(Chip.this, z10);
            }
            if (Chip.this.f8560m != null) {
                Chip.this.f8560m.onCheckedChanged(compoundButton, z10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends ViewOutlineProvider {
        c() {
        }

        @Override // android.view.ViewOutlineProvider
        @TargetApi(21)
        public void getOutline(View view, Outline outline) {
            if (Chip.this.f8556i != null) {
                Chip.this.f8556i.getOutline(outline);
            } else {
                outline.setAlpha(0.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class d extends ExploreByTouchHelper {
        d(Chip chip) {
            super(chip);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            return (Chip.this.o() && Chip.this.getCloseIconTouchBounds().contains(f10, f11)) ? 1 : 0;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            list.add(0);
            if (Chip.this.o() && Chip.this.t() && Chip.this.f8559l != null) {
                list.add(1);
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (i11 != 16) {
                return false;
            }
            if (i10 == 0) {
                return Chip.this.performClick();
            }
            if (i10 == 1) {
                return Chip.this.u();
            }
            return false;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.T(Chip.this.s());
            accessibilityNodeInfoCompat.W(Chip.this.isClickable());
            accessibilityNodeInfoCompat.V(Chip.this.getAccessibilityClassName());
            accessibilityNodeInfoCompat.y0(Chip.this.getText());
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (i10 == 1) {
                CharSequence closeIconContentDescription = Chip.this.getCloseIconContentDescription();
                if (closeIconContentDescription != null) {
                    accessibilityNodeInfoCompat.Z(closeIconContentDescription);
                } else {
                    CharSequence text = Chip.this.getText();
                    Context context = Chip.this.getContext();
                    int i11 = R$string.mtrl_chip_close_icon_content_description;
                    Object[] objArr = new Object[1];
                    objArr[0] = TextUtils.isEmpty(text) ? "" : text;
                    accessibilityNodeInfoCompat.Z(context.getString(i11, objArr).trim());
                }
                accessibilityNodeInfoCompat.Q(Chip.this.getCloseIconTouchBoundsInt());
                accessibilityNodeInfoCompat.b(AccessibilityNodeInfoCompat.a.f2322i);
                accessibilityNodeInfoCompat.b0(Chip.this.isEnabled());
                return;
            }
            accessibilityNodeInfoCompat.Z("");
            accessibilityNodeInfoCompat.Q(Chip.C);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onVirtualViewKeyboardFocusChanged(int i10, boolean z10) {
            if (i10 == 1) {
                Chip.this.f8565r = z10;
                Chip.this.refreshDrawableState();
            }
        }
    }

    public Chip(Context context) {
        this(context, null);
    }

    private void A() {
        this.f8558k = new RippleDrawable(RippleUtils.d(this.f8556i.m1()), getBackgroundDrawable(), null);
        this.f8556i.R2(false);
        ViewCompat.p0(this, this.f8558k);
        B();
    }

    private void B() {
        ChipDrawable chipDrawable;
        if (TextUtils.isEmpty(getText()) || (chipDrawable = this.f8556i) == null) {
            return;
        }
        int Q0 = (int) (chipDrawable.Q0() + this.f8556i.q1() + this.f8556i.x0());
        int V0 = (int) (this.f8556i.V0() + this.f8556i.r1() + this.f8556i.t0());
        if (this.f8557j != null) {
            Rect rect = new Rect();
            this.f8557j.getPadding(rect);
            V0 += rect.left;
            Q0 += rect.right;
        }
        ViewCompat.A0(this, V0, getPaddingTop(), Q0, getPaddingBottom());
    }

    private void C() {
        TextPaint paint = getPaint();
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            paint.drawableState = chipDrawable.getState();
        }
        TextAppearance textAppearance = getTextAppearance();
        if (textAppearance != null) {
            textAppearance.n(getContext(), paint, this.A);
        }
    }

    private void D(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "background") != null) {
            Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
        }
        if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft") == null) {
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableStart") == null) {
                if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableEnd") == null) {
                    if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableRight") == null) {
                        if (attributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "singleLine", true) && attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "lines", 1) == 1 && attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minLines", 1) == 1 && attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", 1) == 1) {
                            if (attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "gravity", 8388627) != 8388627) {
                                Log.w("Chip", "Chip text must be vertically center and start aligned");
                                return;
                            }
                            return;
                        }
                        throw new UnsupportedOperationException("Chip does not support multi-line text");
                    }
                    throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
                }
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            }
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RectF getCloseIconTouchBounds() {
        this.f8573z.setEmpty();
        if (o() && this.f8559l != null) {
            this.f8556i.f1(this.f8573z);
        }
        return this.f8573z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getCloseIconTouchBoundsInt() {
        RectF closeIconTouchBounds = getCloseIconTouchBounds();
        this.f8572y.set((int) closeIconTouchBounds.left, (int) closeIconTouchBounds.top, (int) closeIconTouchBounds.right, (int) closeIconTouchBounds.bottom);
        return this.f8572y;
    }

    private TextAppearance getTextAppearance() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.p1();
        }
        return null;
    }

    private void k(ChipDrawable chipDrawable) {
        chipDrawable.w2(this);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [boolean, int] */
    private int[] l() {
        ?? isEnabled = isEnabled();
        int i10 = isEnabled;
        if (this.f8565r) {
            i10 = isEnabled + 1;
        }
        int i11 = i10;
        if (this.f8564q) {
            i11 = i10 + 1;
        }
        int i12 = i11;
        if (this.f8563p) {
            i12 = i11 + 1;
        }
        int i13 = i12;
        if (isChecked()) {
            i13 = i12 + 1;
        }
        int[] iArr = new int[i13];
        int i14 = 0;
        if (isEnabled()) {
            iArr[0] = 16842910;
            i14 = 1;
        }
        if (this.f8565r) {
            iArr[i14] = 16842908;
            i14++;
        }
        if (this.f8564q) {
            iArr[i14] = 16843623;
            i14++;
        }
        if (this.f8563p) {
            iArr[i14] = 16842919;
            i14++;
        }
        if (isChecked()) {
            iArr[i14] = 16842913;
        }
        return iArr;
    }

    private void n() {
        if (getBackgroundDrawable() == this.f8557j && this.f8556i.getCallback() == null) {
            this.f8556i.setCallback(this.f8557j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean o() {
        ChipDrawable chipDrawable = this.f8556i;
        return (chipDrawable == null || chipDrawable.Y0() == null) ? false : true;
    }

    private void p(Context context, AttributeSet attributeSet, int i10) {
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.Chip, i10, B, new int[0]);
        this.f8566s = obtainStyledAttributes.getBoolean(R$styleable.Chip_ensureMinTouchTargetSize, false);
        this.f8568u = (int) Math.ceil(obtainStyledAttributes.getDimension(R$styleable.Chip_chipMinTouchTargetSize, (float) Math.ceil(ViewUtils.dpToPx(getContext(), 48))));
        obtainStyledAttributes.recycle();
    }

    private void q() {
        setOutlineProvider(new c());
    }

    private void r(int i10, int i11, int i12, int i13) {
        this.f8557j = new InsetDrawable((Drawable) this.f8556i, i10, i11, i12, i13);
    }

    private void setCloseIconHovered(boolean z10) {
        if (this.f8564q != z10) {
            this.f8564q = z10;
            refreshDrawableState();
        }
    }

    private void setCloseIconPressed(boolean z10) {
        if (this.f8563p != z10) {
            this.f8563p = z10;
            refreshDrawableState();
        }
    }

    private void v() {
        if (this.f8557j != null) {
            this.f8557j = null;
            setMinWidth(0);
            setMinHeight((int) getChipMinHeight());
            z();
        }
    }

    private void x(ChipDrawable chipDrawable) {
        if (chipDrawable != null) {
            chipDrawable.w2(null);
        }
    }

    private void y() {
        if (o() && t() && this.f8559l != null) {
            ViewCompat.l0(this, this.f8570w);
            this.f8571x = true;
        } else {
            ViewCompat.l0(this, null);
            this.f8571x = false;
        }
    }

    private void z() {
        if (RippleUtils.f36a) {
            A();
            return;
        }
        this.f8556i.R2(true);
        ViewCompat.p0(this, getBackgroundDrawable());
        B();
        n();
    }

    @Override // com.google.android.material.chip.ChipDrawable.a
    public void a() {
        m(this.f8568u);
        requestLayout();
        invalidateOutline();
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (this.f8571x) {
            return this.f8570w.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!this.f8571x) {
            return super.dispatchKeyEvent(keyEvent);
        }
        if (!this.f8570w.dispatchKeyEvent(keyEvent) || this.f8570w.getKeyboardFocusedVirtualViewId() == Integer.MIN_VALUE) {
            return super.dispatchKeyEvent(keyEvent);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        ChipDrawable chipDrawable = this.f8556i;
        if ((chipDrawable == null || !chipDrawable.w1()) ? false : this.f8556i.s2(l())) {
            invalidate();
        }
    }

    @Override // android.widget.CheckBox, android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        if (!TextUtils.isEmpty(this.f8569v)) {
            return this.f8569v;
        }
        if (!s()) {
            return isClickable() ? "android.widget.Button" : "android.view.View";
        }
        ViewParent parent = getParent();
        return ((parent instanceof ChipGroup) && ((ChipGroup) parent).e()) ? "android.widget.RadioButton" : "android.widget.CompoundButton";
    }

    public Drawable getBackgroundDrawable() {
        InsetDrawable insetDrawable = this.f8557j;
        return insetDrawable == null ? this.f8556i : insetDrawable;
    }

    public Drawable getCheckedIcon() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.M0();
        }
        return null;
    }

    public ColorStateList getCheckedIconTint() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.N0();
        }
        return null;
    }

    public ColorStateList getChipBackgroundColor() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.O0();
        }
        return null;
    }

    public float getChipCornerRadius() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return Math.max(0.0f, chipDrawable.P0());
        }
        return 0.0f;
    }

    public Drawable getChipDrawable() {
        return this.f8556i;
    }

    public float getChipEndPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.Q0();
        }
        return 0.0f;
    }

    public Drawable getChipIcon() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.R0();
        }
        return null;
    }

    public float getChipIconSize() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.S0();
        }
        return 0.0f;
    }

    public ColorStateList getChipIconTint() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.T0();
        }
        return null;
    }

    public float getChipMinHeight() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.U0();
        }
        return 0.0f;
    }

    public float getChipStartPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.V0();
        }
        return 0.0f;
    }

    public ColorStateList getChipStrokeColor() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.W0();
        }
        return null;
    }

    public float getChipStrokeWidth() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.X0();
        }
        return 0.0f;
    }

    @Deprecated
    public CharSequence getChipText() {
        return getText();
    }

    public Drawable getCloseIcon() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.Y0();
        }
        return null;
    }

    public CharSequence getCloseIconContentDescription() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.Z0();
        }
        return null;
    }

    public float getCloseIconEndPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.a1();
        }
        return 0.0f;
    }

    public float getCloseIconSize() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.b1();
        }
        return 0.0f;
    }

    public float getCloseIconStartPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.c1();
        }
        return 0.0f;
    }

    public ColorStateList getCloseIconTint() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.e1();
        }
        return null;
    }

    @Override // android.widget.TextView
    public TextUtils.TruncateAt getEllipsize() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.i1();
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    public void getFocusedRect(Rect rect) {
        if (this.f8571x && (this.f8570w.getKeyboardFocusedVirtualViewId() == 1 || this.f8570w.getAccessibilityFocusedVirtualViewId() == 1)) {
            rect.set(getCloseIconTouchBoundsInt());
        } else {
            super.getFocusedRect(rect);
        }
    }

    public MotionSpec getHideMotionSpec() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.j1();
        }
        return null;
    }

    public float getIconEndPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.k1();
        }
        return 0.0f;
    }

    public float getIconStartPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.l1();
        }
        return 0.0f;
    }

    public ColorStateList getRippleColor() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.m1();
        }
        return null;
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.f8556i.D();
    }

    public MotionSpec getShowMotionSpec() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.n1();
        }
        return null;
    }

    public float getTextEndPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.q1();
        }
        return 0.0f;
    }

    public float getTextStartPadding() {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            return chipDrawable.r1();
        }
        return 0.0f;
    }

    public boolean m(int i10) {
        this.f8568u = i10;
        if (!w()) {
            if (this.f8557j != null) {
                v();
            } else {
                z();
            }
            return false;
        }
        int max = Math.max(0, i10 - this.f8556i.getIntrinsicHeight());
        int max2 = Math.max(0, i10 - this.f8556i.getIntrinsicWidth());
        if (max2 <= 0 && max <= 0) {
            if (this.f8557j != null) {
                v();
            } else {
                z();
            }
            return false;
        }
        int i11 = max2 > 0 ? max2 / 2 : 0;
        int i12 = max > 0 ? max / 2 : 0;
        if (this.f8557j != null) {
            Rect rect = new Rect();
            this.f8557j.getPadding(rect);
            if (rect.top == i12 && rect.bottom == i12 && rect.left == i11 && rect.right == i11) {
                z();
                return true;
            }
        }
        if (getMinHeight() != i10) {
            setMinHeight(i10);
        }
        if (getMinWidth() != i10) {
            setMinWidth(i10);
        }
        r(i11, i12, i11, i12);
        z();
        return true;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.f(this, this.f8556i);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i10) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 2);
        if (isChecked()) {
            CheckBox.mergeDrawableStates(onCreateDrawableState, D);
        }
        if (s()) {
            CheckBox.mergeDrawableStates(onCreateDrawableState, E);
        }
        return onCreateDrawableState;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean z10, int i10, Rect rect) {
        super.onFocusChanged(z10, i10, rect);
        if (this.f8571x) {
            this.f8570w.onFocusChanged(z10, i10, rect);
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 7) {
            setCloseIconHovered(getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()));
        } else if (actionMasked == 10) {
            setCloseIconHovered(false);
        }
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(getAccessibilityClassName());
        accessibilityNodeInfo.setCheckable(s());
        accessibilityNodeInfo.setClickable(isClickable());
        if (getParent() instanceof ChipGroup) {
            ChipGroup chipGroup = (ChipGroup) getParent();
            AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo).Y(AccessibilityNodeInfoCompat.c.a(chipGroup.getRowIndex(this), 1, chipGroup.isSingleLine() ? chipGroup.c(this) : -1, 1, false, isChecked()));
        }
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    @TargetApi(24)
    public PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int i10) {
        if (getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) && isEnabled()) {
            return PointerIcon.getSystemIcon(getContext(), DataTypeConstants.APP_LOG);
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    @TargetApi(17)
    public void onRtlPropertiesChanged(int i10) {
        super.onRtlPropertiesChanged(i10);
        if (this.f8567t != i10) {
            this.f8567t = i10;
            B();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001e, code lost:
    
        if (r0 != 3) goto L22;
     */
    @Override // android.widget.TextView, android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z10;
        int actionMasked = motionEvent.getActionMasked();
        boolean contains = getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY());
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (this.f8563p) {
                        if (!contains) {
                            setCloseIconPressed(false);
                        }
                        z10 = true;
                    }
                }
                z10 = false;
            } else if (this.f8563p) {
                u();
                z10 = true;
                setCloseIconPressed(false);
            }
            z10 = false;
            setCloseIconPressed(false);
        } else {
            if (contains) {
                setCloseIconPressed(true);
                z10 = true;
            }
            z10 = false;
        }
        return z10 || super.onTouchEvent(motionEvent);
    }

    public boolean s() {
        ChipDrawable chipDrawable = this.f8556i;
        return chipDrawable != null && chipDrawable.v1();
    }

    public void setAccessibilityClassName(CharSequence charSequence) {
        this.f8569v = charSequence;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        if (drawable != getBackgroundDrawable() && drawable != this.f8558k) {
            Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
        } else {
            super.setBackground(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i10) {
        Log.w("Chip", "Do not set the background color; Chip manages its own background drawable.");
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (drawable != getBackgroundDrawable() && drawable != this.f8558k) {
            Log.w("Chip", "Do not set the background drawable; Chip manages its own background drawable.");
        } else {
            super.setBackgroundDrawable(drawable);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundResource(int i10) {
        Log.w("Chip", "Do not set the background resource; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        Log.w("Chip", "Do not set the background tint list; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        Log.w("Chip", "Do not set the background tint mode; Chip manages its own background drawable.");
    }

    public void setCheckable(boolean z10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.E1(z10);
        }
    }

    public void setCheckableResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.F1(i10);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable == null) {
            this.f8562o = z10;
        } else if (chipDrawable.v1()) {
            super.setChecked(z10);
        }
    }

    public void setCheckedIcon(Drawable drawable) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.G1(drawable);
        }
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean z10) {
        setCheckedIconVisible(z10);
    }

    @Deprecated
    public void setCheckedIconEnabledResource(int i10) {
        setCheckedIconVisible(i10);
    }

    public void setCheckedIconResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.H1(i10);
        }
    }

    public void setCheckedIconTint(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.I1(colorStateList);
        }
    }

    public void setCheckedIconTintResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.J1(i10);
        }
    }

    public void setCheckedIconVisible(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.K1(i10);
        }
    }

    public void setChipBackgroundColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.M1(colorStateList);
        }
    }

    public void setChipBackgroundColorResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.N1(i10);
        }
    }

    @Deprecated
    public void setChipCornerRadius(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.O1(f10);
        }
    }

    @Deprecated
    public void setChipCornerRadiusResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.P1(i10);
        }
    }

    public void setChipDrawable(ChipDrawable chipDrawable) {
        ChipDrawable chipDrawable2 = this.f8556i;
        if (chipDrawable2 != chipDrawable) {
            x(chipDrawable2);
            this.f8556i = chipDrawable;
            chipDrawable.H2(false);
            k(this.f8556i);
            m(this.f8568u);
        }
    }

    public void setChipEndPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.Q1(f10);
        }
    }

    public void setChipEndPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.R1(i10);
        }
    }

    public void setChipIcon(Drawable drawable) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.S1(drawable);
        }
    }

    @Deprecated
    public void setChipIconEnabled(boolean z10) {
        setChipIconVisible(z10);
    }

    @Deprecated
    public void setChipIconEnabledResource(int i10) {
        setChipIconVisible(i10);
    }

    public void setChipIconResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.T1(i10);
        }
    }

    public void setChipIconSize(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.U1(f10);
        }
    }

    public void setChipIconSizeResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.V1(i10);
        }
    }

    public void setChipIconTint(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.W1(colorStateList);
        }
    }

    public void setChipIconTintResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.X1(i10);
        }
    }

    public void setChipIconVisible(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.Y1(i10);
        }
    }

    public void setChipMinHeight(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.a2(f10);
        }
    }

    public void setChipMinHeightResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.b2(i10);
        }
    }

    public void setChipStartPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.c2(f10);
        }
    }

    public void setChipStartPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.d2(i10);
        }
    }

    public void setChipStrokeColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.e2(colorStateList);
        }
    }

    public void setChipStrokeColorResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.f2(i10);
        }
    }

    public void setChipStrokeWidth(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.g2(f10);
        }
    }

    public void setChipStrokeWidthResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.h2(i10);
        }
    }

    @Deprecated
    public void setChipText(CharSequence charSequence) {
        setText(charSequence);
    }

    @Deprecated
    public void setChipTextResource(int i10) {
        setText(getResources().getString(i10));
    }

    public void setCloseIcon(Drawable drawable) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.j2(drawable);
        }
        y();
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.k2(charSequence);
        }
    }

    @Deprecated
    public void setCloseIconEnabled(boolean z10) {
        setCloseIconVisible(z10);
    }

    @Deprecated
    public void setCloseIconEnabledResource(int i10) {
        setCloseIconVisible(i10);
    }

    public void setCloseIconEndPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.l2(f10);
        }
    }

    public void setCloseIconEndPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.m2(i10);
        }
    }

    public void setCloseIconResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.n2(i10);
        }
        y();
    }

    public void setCloseIconSize(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.o2(f10);
        }
    }

    public void setCloseIconSizeResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.p2(i10);
        }
    }

    public void setCloseIconStartPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.q2(f10);
        }
    }

    public void setCloseIconStartPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.r2(i10);
        }
    }

    public void setCloseIconTint(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.t2(colorStateList);
        }
    }

    public void setCloseIconTintResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.u2(i10);
        }
    }

    public void setCloseIconVisible(int i10) {
        setCloseIconVisible(getResources().getBoolean(i10));
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i10, int i11, int i12, int i13) {
        if (i10 != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i12 == 0) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(i10, i11, i12, i13);
            return;
        }
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int i10, int i11, int i12, int i13) {
        if (i10 != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i12 == 0) {
            super.setCompoundDrawablesWithIntrinsicBounds(i10, i11, i12, i13);
            return;
        }
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        super.setElevation(f10);
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.Z(f10);
        }
    }

    @Override // android.widget.TextView
    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.f8556i == null) {
            return;
        }
        if (truncateAt != TextUtils.TruncateAt.MARQUEE) {
            super.setEllipsize(truncateAt);
            ChipDrawable chipDrawable = this.f8556i;
            if (chipDrawable != null) {
                chipDrawable.x2(truncateAt);
                return;
            }
            return;
        }
        throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
    }

    public void setEnsureMinTouchTargetSize(boolean z10) {
        this.f8566s = z10;
        m(this.f8568u);
    }

    @Override // android.widget.TextView
    public void setGravity(int i10) {
        if (i10 != 8388627) {
            Log.w("Chip", "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(i10);
        }
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.y2(motionSpec);
        }
    }

    public void setHideMotionSpecResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.z2(i10);
        }
    }

    public void setIconEndPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.A2(f10);
        }
    }

    public void setIconEndPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.B2(i10);
        }
    }

    public void setIconStartPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.C2(f10);
        }
    }

    public void setIconStartPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.D2(i10);
        }
    }

    @Override // com.google.android.material.internal.MaterialCheckable
    public void setInternalOnCheckedChangeListener(MaterialCheckable.OnCheckedChangeListener<Chip> onCheckedChangeListener) {
        this.f8561n = onCheckedChangeListener;
    }

    @Override // android.view.View
    public void setLayoutDirection(int i10) {
        if (this.f8556i == null) {
            return;
        }
        super.setLayoutDirection(i10);
    }

    @Override // android.widget.TextView
    public void setLines(int i10) {
        if (i10 <= 1) {
            super.setLines(i10);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setMaxLines(int i10) {
        if (i10 <= 1) {
            super.setMaxLines(i10);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setMaxWidth(int i10) {
        super.setMaxWidth(i10);
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.E2(i10);
        }
    }

    @Override // android.widget.TextView
    public void setMinLines(int i10) {
        if (i10 <= 1) {
            super.setMinLines(i10);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.f8560m = onCheckedChangeListener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener onClickListener) {
        this.f8559l = onClickListener;
        y();
    }

    public void setRippleColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.F2(colorStateList);
        }
        if (this.f8556i.t1()) {
            return;
        }
        A();
    }

    public void setRippleColorResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.G2(i10);
            if (this.f8556i.t1()) {
                return;
            }
            A();
        }
    }

    @Override // c4.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.f8556i.setShapeAppearanceModel(shapeAppearanceModel);
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.I2(motionSpec);
        }
    }

    public void setShowMotionSpecResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.J2(i10);
        }
    }

    @Override // android.widget.TextView
    public void setSingleLine(boolean z10) {
        if (z10) {
            super.setSingleLine(z10);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable == null) {
            return;
        }
        if (charSequence == null) {
            charSequence = "";
        }
        super.setText(chipDrawable.S2() ? null : charSequence, bufferType);
        ChipDrawable chipDrawable2 = this.f8556i;
        if (chipDrawable2 != null) {
            chipDrawable2.K2(charSequence);
        }
    }

    public void setTextAppearance(TextAppearance textAppearance) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.L2(textAppearance);
        }
        C();
    }

    public void setTextAppearanceResource(int i10) {
        setTextAppearance(getContext(), i10);
    }

    public void setTextEndPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.N2(f10);
        }
    }

    public void setTextEndPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.O2(i10);
        }
    }

    public void setTextStartPadding(float f10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.P2(f10);
        }
    }

    public void setTextStartPaddingResource(int i10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.Q2(i10);
        }
    }

    public boolean t() {
        ChipDrawable chipDrawable = this.f8556i;
        return chipDrawable != null && chipDrawable.x1();
    }

    public boolean u() {
        boolean z10 = false;
        playSoundEffect(0);
        View.OnClickListener onClickListener = this.f8559l;
        if (onClickListener != null) {
            onClickListener.onClick(this);
            z10 = true;
        }
        if (this.f8571x) {
            this.f8570w.sendEventForVirtualView(1, 1);
        }
        return z10;
    }

    public boolean w() {
        return this.f8566s;
    }

    public Chip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.chipStyle);
    }

    public void setCloseIconVisible(boolean z10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.v2(z10);
        }
        y();
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Chip(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r4), attributeSet, i10);
        int i11 = B;
        this.f8572y = new Rect();
        this.f8573z = new RectF();
        this.A = new a();
        Context context2 = getContext();
        D(attributeSet);
        ChipDrawable C0 = ChipDrawable.C0(context2, attributeSet, i10, i11);
        p(context2, attributeSet, i10);
        setChipDrawable(C0);
        C0.Z(ViewCompat.t(this));
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.Chip, i10, i11, new int[0]);
        boolean hasValue = obtainStyledAttributes.hasValue(R$styleable.Chip_shapeAppearance);
        obtainStyledAttributes.recycle();
        this.f8570w = new d(this);
        y();
        if (!hasValue) {
            q();
        }
        setChecked(this.f8562o);
        setText(C0.o1());
        setEllipsize(C0.i1());
        C();
        if (!this.f8556i.S2()) {
            setLines(1);
            setHorizontallyScrolling(true);
        }
        setGravity(8388627);
        B();
        if (w()) {
            setMinHeight(this.f8568u);
        }
        this.f8567t = ViewCompat.x(this);
        super.setOnCheckedChangeListener(new b());
    }

    public void setCheckedIconVisible(boolean z10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.L1(z10);
        }
    }

    public void setChipIconVisible(boolean z10) {
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.Z1(z10);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i10) {
        super.setTextAppearance(context, i10);
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.M2(i10);
        }
        C();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(int i10) {
        super.setTextAppearance(i10);
        ChipDrawable chipDrawable = this.f8556i;
        if (chipDrawable != null) {
            chipDrawable.M2(i10);
        }
        C();
    }
}
