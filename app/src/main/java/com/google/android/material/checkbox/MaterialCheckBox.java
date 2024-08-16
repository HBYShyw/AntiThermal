package com.google.android.material.checkbox;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import d4.MaterialThemeOverlay;
import r3.MaterialColors;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class MaterialCheckBox extends AppCompatCheckBox {

    /* renamed from: l, reason: collision with root package name */
    private static final int f8551l = R$style.Widget_MaterialComponents_CompoundButton_CheckBox;

    /* renamed from: m, reason: collision with root package name */
    private static final int[][] f8552m = {new int[]{R.attr.state_enabled, 16842912}, new int[]{R.attr.state_enabled, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};

    /* renamed from: i, reason: collision with root package name */
    private ColorStateList f8553i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f8554j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f8555k;

    public MaterialCheckBox(Context context) {
        this(context, null);
    }

    private ColorStateList getMaterialThemeColorsTintList() {
        if (this.f8553i == null) {
            int[][] iArr = f8552m;
            int[] iArr2 = new int[iArr.length];
            int d10 = MaterialColors.d(this, R$attr.colorControlActivated);
            int d11 = MaterialColors.d(this, R$attr.colorSurface);
            int d12 = MaterialColors.d(this, R$attr.colorOnSurface);
            iArr2[0] = MaterialColors.h(d11, d10, 1.0f);
            iArr2[1] = MaterialColors.h(d11, d12, 0.54f);
            iArr2[2] = MaterialColors.h(d11, d12, 0.38f);
            iArr2[3] = MaterialColors.h(d11, d12, 0.38f);
            this.f8553i = new ColorStateList(iArr, iArr2);
        }
        return this.f8553i;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f8554j && CompoundButtonCompat.b(this) == null) {
            setUseMaterialThemeColors(true);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        Drawable a10;
        if (this.f8555k && TextUtils.isEmpty(getText()) && (a10 = CompoundButtonCompat.a(this)) != null) {
            int width = ((getWidth() - a10.getIntrinsicWidth()) / 2) * (ViewUtils.isLayoutRtl(this) ? -1 : 1);
            int save = canvas.save();
            canvas.translate(width, 0.0f);
            super.onDraw(canvas);
            canvas.restoreToCount(save);
            if (getBackground() != null) {
                Rect bounds = a10.getBounds();
                DrawableCompat.f(getBackground(), bounds.left + width, bounds.top, bounds.right + width, bounds.bottom);
                return;
            }
            return;
        }
        super.onDraw(canvas);
    }

    public void setCenterIfNoTextEnabled(boolean z10) {
        this.f8555k = z10;
    }

    public void setUseMaterialThemeColors(boolean z10) {
        this.f8554j = z10;
        if (z10) {
            CompoundButtonCompat.c(this, getMaterialThemeColorsTintList());
        } else {
            CompoundButtonCompat.c(this, null);
        }
    }

    public MaterialCheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.checkboxStyle);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MaterialCheckBox(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r4), attributeSet, i10);
        int i11 = f8551l;
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialCheckBox, i10, i11, new int[0]);
        int i12 = R$styleable.MaterialCheckBox_buttonTint;
        if (obtainStyledAttributes.hasValue(i12)) {
            CompoundButtonCompat.c(this, MaterialResources.a(context2, obtainStyledAttributes, i12));
        }
        this.f8554j = obtainStyledAttributes.getBoolean(R$styleable.MaterialCheckBox_useMaterialThemeColors, false);
        this.f8555k = obtainStyledAttributes.getBoolean(R$styleable.MaterialCheckBox_centerIfNoTextEnabled, true);
        obtainStyledAttributes.recycle();
    }
}
