package com.google.android.material.button;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import c.AppCompatResources;
import c4.MaterialShapeUtils;
import c4.ShapeAppearanceModel;
import c4.Shapeable;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import d4.MaterialThemeOverlay;
import java.util.Iterator;
import java.util.LinkedHashSet;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class MaterialButton extends AppCompatButton implements Checkable, Shapeable {

    /* renamed from: u, reason: collision with root package name */
    private static final int[] f8461u = {R.attr.state_checkable};

    /* renamed from: v, reason: collision with root package name */
    private static final int[] f8462v = {16842912};

    /* renamed from: w, reason: collision with root package name */
    private static final int f8463w = R$style.Widget_MaterialComponents_Button;

    /* renamed from: h, reason: collision with root package name */
    private final MaterialButtonHelper f8464h;

    /* renamed from: i, reason: collision with root package name */
    private final LinkedHashSet<a> f8465i;

    /* renamed from: j, reason: collision with root package name */
    private b f8466j;

    /* renamed from: k, reason: collision with root package name */
    private PorterDuff.Mode f8467k;

    /* renamed from: l, reason: collision with root package name */
    private ColorStateList f8468l;

    /* renamed from: m, reason: collision with root package name */
    private Drawable f8469m;

    /* renamed from: n, reason: collision with root package name */
    private int f8470n;

    /* renamed from: o, reason: collision with root package name */
    private int f8471o;

    /* renamed from: p, reason: collision with root package name */
    private int f8472p;

    /* renamed from: q, reason: collision with root package name */
    private int f8473q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f8474r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f8475s;

    /* renamed from: t, reason: collision with root package name */
    private int f8476t;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        boolean f8477e;

        /* loaded from: classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void readFromParcel(Parcel parcel) {
            this.f8477e = parcel.readInt() == 1;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f8477e ? 1 : 0);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            if (classLoader == null) {
                getClass().getClassLoader();
            }
            readFromParcel(parcel);
        }
    }

    /* loaded from: classes.dex */
    public interface a {
        void a(MaterialButton materialButton, boolean z10);
    }

    /* loaded from: classes.dex */
    interface b {
        void a(MaterialButton materialButton, boolean z10);
    }

    public MaterialButton(Context context) {
        this(context, null);
    }

    private boolean b() {
        int i10 = this.f8476t;
        return i10 == 3 || i10 == 4;
    }

    private boolean c() {
        int i10 = this.f8476t;
        return i10 == 1 || i10 == 2;
    }

    private boolean d() {
        int i10 = this.f8476t;
        return i10 == 16 || i10 == 32;
    }

    private boolean e() {
        return ViewCompat.x(this) == 1;
    }

    private boolean f() {
        MaterialButtonHelper materialButtonHelper = this.f8464h;
        return (materialButtonHelper == null || materialButtonHelper.o()) ? false : true;
    }

    private void g() {
        if (c()) {
            TextViewCompat.i(this, this.f8469m, null, null, null);
        } else if (b()) {
            TextViewCompat.i(this, null, null, this.f8469m, null);
        } else if (d()) {
            TextViewCompat.i(this, null, this.f8469m, null, null);
        }
    }

    private String getA11yClassName() {
        return (a() ? CompoundButton.class : Button.class).getName();
    }

    private Layout.Alignment getActualTextAlignment() {
        int textAlignment = getTextAlignment();
        if (textAlignment == 1) {
            return getGravityTextAlignment();
        }
        if (textAlignment == 6 || textAlignment == 3) {
            return Layout.Alignment.ALIGN_OPPOSITE;
        }
        if (textAlignment != 4) {
            return Layout.Alignment.ALIGN_NORMAL;
        }
        return Layout.Alignment.ALIGN_CENTER;
    }

    private Layout.Alignment getGravityTextAlignment() {
        int gravity = getGravity() & 8388615;
        if (gravity == 1) {
            return Layout.Alignment.ALIGN_CENTER;
        }
        if (gravity != 5 && gravity != 8388613) {
            return Layout.Alignment.ALIGN_NORMAL;
        }
        return Layout.Alignment.ALIGN_OPPOSITE;
    }

    private int getTextHeight() {
        TextPaint paint = getPaint();
        String charSequence = getText().toString();
        if (getTransformationMethod() != null) {
            charSequence = getTransformationMethod().getTransformation(charSequence, this).toString();
        }
        Rect rect = new Rect();
        paint.getTextBounds(charSequence, 0, charSequence.length(), rect);
        return Math.min(rect.height(), getLayout().getHeight());
    }

    private int getTextWidth() {
        TextPaint paint = getPaint();
        String charSequence = getText().toString();
        if (getTransformationMethod() != null) {
            charSequence = getTransformationMethod().getTransformation(charSequence, this).toString();
        }
        return Math.min((int) paint.measureText(charSequence), getLayout().getEllipsizedWidth());
    }

    private void h(boolean z10) {
        Drawable drawable = this.f8469m;
        boolean z11 = true;
        if (drawable != null) {
            Drawable mutate = DrawableCompat.l(drawable).mutate();
            this.f8469m = mutate;
            DrawableCompat.i(mutate, this.f8468l);
            PorterDuff.Mode mode = this.f8467k;
            if (mode != null) {
                DrawableCompat.j(this.f8469m, mode);
            }
            int i10 = this.f8470n;
            if (i10 == 0) {
                i10 = this.f8469m.getIntrinsicWidth();
            }
            int i11 = this.f8470n;
            if (i11 == 0) {
                i11 = this.f8469m.getIntrinsicHeight();
            }
            Drawable drawable2 = this.f8469m;
            int i12 = this.f8471o;
            int i13 = this.f8472p;
            drawable2.setBounds(i12, i13, i10 + i12, i11 + i13);
            this.f8469m.setVisible(true, z10);
        }
        if (z10) {
            g();
            return;
        }
        Drawable[] a10 = TextViewCompat.a(this);
        Drawable drawable3 = a10[0];
        Drawable drawable4 = a10[1];
        Drawable drawable5 = a10[2];
        if ((!c() || drawable3 == this.f8469m) && ((!b() || drawable5 == this.f8469m) && (!d() || drawable4 == this.f8469m))) {
            z11 = false;
        }
        if (z11) {
            g();
        }
    }

    private void i(int i10, int i11) {
        if (this.f8469m == null || getLayout() == null) {
            return;
        }
        if (!c() && !b()) {
            if (d()) {
                this.f8471o = 0;
                if (this.f8476t == 16) {
                    this.f8472p = 0;
                    h(false);
                    return;
                }
                int i12 = this.f8470n;
                if (i12 == 0) {
                    i12 = this.f8469m.getIntrinsicHeight();
                }
                int textHeight = (((((i11 - getTextHeight()) - getPaddingTop()) - i12) - this.f8473q) - getPaddingBottom()) / 2;
                if (this.f8472p != textHeight) {
                    this.f8472p = textHeight;
                    h(false);
                    return;
                }
                return;
            }
            return;
        }
        this.f8472p = 0;
        Layout.Alignment actualTextAlignment = getActualTextAlignment();
        int i13 = this.f8476t;
        if (i13 != 1 && i13 != 3 && ((i13 != 2 || actualTextAlignment != Layout.Alignment.ALIGN_NORMAL) && (i13 != 4 || actualTextAlignment != Layout.Alignment.ALIGN_OPPOSITE))) {
            int i14 = this.f8470n;
            if (i14 == 0) {
                i14 = this.f8469m.getIntrinsicWidth();
            }
            int textWidth = ((((i10 - getTextWidth()) - ViewCompat.B(this)) - i14) - this.f8473q) - ViewCompat.C(this);
            if (actualTextAlignment == Layout.Alignment.ALIGN_CENTER) {
                textWidth /= 2;
            }
            if (e() != (this.f8476t == 4)) {
                textWidth = -textWidth;
            }
            if (this.f8471o != textWidth) {
                this.f8471o = textWidth;
                h(false);
                return;
            }
            return;
        }
        this.f8471o = 0;
        h(false);
    }

    public boolean a() {
        MaterialButtonHelper materialButtonHelper = this.f8464h;
        return materialButtonHelper != null && materialButtonHelper.p();
    }

    @Override // android.view.View
    public ColorStateList getBackgroundTintList() {
        return getSupportBackgroundTintList();
    }

    @Override // android.view.View
    public PorterDuff.Mode getBackgroundTintMode() {
        return getSupportBackgroundTintMode();
    }

    public int getCornerRadius() {
        if (f()) {
            return this.f8464h.b();
        }
        return 0;
    }

    public Drawable getIcon() {
        return this.f8469m;
    }

    public int getIconGravity() {
        return this.f8476t;
    }

    public int getIconPadding() {
        return this.f8473q;
    }

    public int getIconSize() {
        return this.f8470n;
    }

    public ColorStateList getIconTint() {
        return this.f8468l;
    }

    public PorterDuff.Mode getIconTintMode() {
        return this.f8467k;
    }

    public int getInsetBottom() {
        return this.f8464h.c();
    }

    public int getInsetTop() {
        return this.f8464h.d();
    }

    public ColorStateList getRippleColor() {
        if (f()) {
            return this.f8464h.h();
        }
        return null;
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        if (f()) {
            return this.f8464h.i();
        }
        throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
    }

    public ColorStateList getStrokeColor() {
        if (f()) {
            return this.f8464h.j();
        }
        return null;
    }

    public int getStrokeWidth() {
        if (f()) {
            return this.f8464h.k();
        }
        return 0;
    }

    @Override // androidx.appcompat.widget.AppCompatButton
    public ColorStateList getSupportBackgroundTintList() {
        if (f()) {
            return this.f8464h.l();
        }
        return super.getSupportBackgroundTintList();
    }

    @Override // androidx.appcompat.widget.AppCompatButton
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        if (f()) {
            return this.f8464h.m();
        }
        return super.getSupportBackgroundTintMode();
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.f8474r;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (f()) {
            MaterialShapeUtils.f(this, this.f8464h.f());
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i10) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 2);
        if (a()) {
            Button.mergeDrawableStates(onCreateDrawableState, f8461u);
        }
        if (isChecked()) {
            Button.mergeDrawableStates(onCreateDrawableState, f8462v);
        }
        return onCreateDrawableState;
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(getA11yClassName());
        accessibilityEvent.setChecked(isChecked());
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(getA11yClassName());
        accessibilityNodeInfo.setCheckable(a());
        accessibilityNodeInfo.setChecked(isChecked());
        accessibilityNodeInfo.setClickable(isClickable());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        i(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.f8477e);
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f8477e = this.f8474r;
        return savedState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView
    public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        super.onTextChanged(charSequence, i10, i11, i12);
        i(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.View
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override // android.view.View
    public void refreshDrawableState() {
        super.refreshDrawableState();
        if (this.f8469m != null) {
            if (this.f8469m.setState(getDrawableState())) {
                invalidate();
            }
        }
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i10) {
        if (f()) {
            this.f8464h.r(i10);
        } else {
            super.setBackgroundColor(i10);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (f()) {
            if (drawable != getBackground()) {
                Log.w("MaterialButton", "MaterialButton manages its own background to control elevation, shape, color and states. Consider using backgroundTint, shapeAppearance and other attributes where available. A custom background will ignore these attributes and you should consider handling interaction states such as pressed, focused and disabled");
                this.f8464h.s();
                super.setBackgroundDrawable(drawable);
                return;
            }
            getBackground().setState(drawable.getState());
            return;
        }
        super.setBackgroundDrawable(drawable);
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void setBackgroundResource(int i10) {
        setBackgroundDrawable(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        setSupportBackgroundTintList(colorStateList);
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        setSupportBackgroundTintMode(mode);
    }

    public void setCheckable(boolean z10) {
        if (f()) {
            this.f8464h.t(z10);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z10) {
        if (a() && isEnabled() && this.f8474r != z10) {
            this.f8474r = z10;
            refreshDrawableState();
            if (getParent() instanceof MaterialButtonToggleGroup) {
                ((MaterialButtonToggleGroup) getParent()).m(this, this.f8474r);
            }
            if (this.f8475s) {
                return;
            }
            this.f8475s = true;
            Iterator<a> it = this.f8465i.iterator();
            while (it.hasNext()) {
                it.next().a(this, this.f8474r);
            }
            this.f8475s = false;
        }
    }

    public void setCornerRadius(int i10) {
        if (f()) {
            this.f8464h.u(i10);
        }
    }

    public void setCornerRadiusResource(int i10) {
        if (f()) {
            setCornerRadius(getResources().getDimensionPixelSize(i10));
        }
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        super.setElevation(f10);
        if (f()) {
            this.f8464h.f().Z(f10);
        }
    }

    public void setIcon(Drawable drawable) {
        if (this.f8469m != drawable) {
            this.f8469m = drawable;
            h(true);
            i(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconGravity(int i10) {
        if (this.f8476t != i10) {
            this.f8476t = i10;
            i(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconPadding(int i10) {
        if (this.f8473q != i10) {
            this.f8473q = i10;
            setCompoundDrawablePadding(i10);
        }
    }

    public void setIconResource(int i10) {
        setIcon(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
    }

    public void setIconSize(int i10) {
        if (i10 >= 0) {
            if (this.f8470n != i10) {
                this.f8470n = i10;
                h(true);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("iconSize cannot be less than 0");
    }

    public void setIconTint(ColorStateList colorStateList) {
        if (this.f8468l != colorStateList) {
            this.f8468l = colorStateList;
            h(false);
        }
    }

    public void setIconTintMode(PorterDuff.Mode mode) {
        if (this.f8467k != mode) {
            this.f8467k = mode;
            h(false);
        }
    }

    public void setIconTintResource(int i10) {
        setIconTint(AppCompatResources.a(getContext(), i10));
    }

    public void setInsetBottom(int i10) {
        this.f8464h.v(i10);
    }

    public void setInsetTop(int i10) {
        this.f8464h.w(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInternalBackground(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnPressedChangeListenerInternal(b bVar) {
        this.f8466j = bVar;
    }

    @Override // android.view.View
    public void setPressed(boolean z10) {
        b bVar = this.f8466j;
        if (bVar != null) {
            bVar.a(this, z10);
        }
        super.setPressed(z10);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (f()) {
            this.f8464h.x(colorStateList);
        }
    }

    public void setRippleColorResource(int i10) {
        if (f()) {
            setRippleColor(AppCompatResources.a(getContext(), i10));
        }
    }

    @Override // c4.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        if (f()) {
            this.f8464h.y(shapeAppearanceModel);
            return;
        }
        throw new IllegalStateException("Attempted to set ShapeAppearanceModel on a MaterialButton which has an overwritten background.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldDrawSurfaceColorStroke(boolean z10) {
        if (f()) {
            this.f8464h.z(z10);
        }
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        if (f()) {
            this.f8464h.A(colorStateList);
        }
    }

    public void setStrokeColorResource(int i10) {
        if (f()) {
            setStrokeColor(AppCompatResources.a(getContext(), i10));
        }
    }

    public void setStrokeWidth(int i10) {
        if (f()) {
            this.f8464h.B(i10);
        }
    }

    public void setStrokeWidthResource(int i10) {
        if (f()) {
            setStrokeWidth(getResources().getDimensionPixelSize(i10));
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (f()) {
            this.f8464h.C(colorStateList);
        } else {
            super.setSupportBackgroundTintList(colorStateList);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (f()) {
            this.f8464h.D(mode);
        } else {
            super.setSupportBackgroundTintMode(mode);
        }
    }

    @Override // android.view.View
    public void setTextAlignment(int i10) {
        super.setTextAlignment(i10);
        i(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.f8474r);
    }

    public MaterialButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.materialButtonStyle);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MaterialButton(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r6), attributeSet, i10);
        int i11 = f8463w;
        this.f8465i = new LinkedHashSet<>();
        this.f8474r = false;
        this.f8475s = false;
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialButton, i10, i11, new int[0]);
        this.f8473q = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MaterialButton_iconPadding, 0);
        this.f8467k = ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R$styleable.MaterialButton_iconTintMode, -1), PorterDuff.Mode.SRC_IN);
        this.f8468l = MaterialResources.a(getContext(), obtainStyledAttributes, R$styleable.MaterialButton_iconTint);
        this.f8469m = MaterialResources.e(getContext(), obtainStyledAttributes, R$styleable.MaterialButton_icon);
        this.f8476t = obtainStyledAttributes.getInteger(R$styleable.MaterialButton_iconGravity, 1);
        this.f8470n = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MaterialButton_iconSize, 0);
        MaterialButtonHelper materialButtonHelper = new MaterialButtonHelper(this, ShapeAppearanceModel.e(context2, attributeSet, i10, i11).m());
        this.f8464h = materialButtonHelper;
        materialButtonHelper.q(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        setCompoundDrawablePadding(this.f8473q);
        h(this.f8469m != null);
    }
}
