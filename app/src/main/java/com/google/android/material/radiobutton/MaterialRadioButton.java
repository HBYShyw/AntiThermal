package com.google.android.material.radiobutton;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import d4.MaterialThemeOverlay;
import r3.MaterialColors;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class MaterialRadioButton extends AppCompatRadioButton {

    /* renamed from: k, reason: collision with root package name */
    private static final int f9140k = R$style.Widget_MaterialComponents_CompoundButton_RadioButton;

    /* renamed from: l, reason: collision with root package name */
    private static final int[][] f9141l = {new int[]{R.attr.state_enabled, 16842912}, new int[]{R.attr.state_enabled, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};

    /* renamed from: i, reason: collision with root package name */
    private ColorStateList f9142i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f9143j;

    public MaterialRadioButton(Context context) {
        this(context, null);
    }

    private ColorStateList getMaterialThemeColorsTintList() {
        if (this.f9142i == null) {
            int d10 = MaterialColors.d(this, R$attr.colorControlActivated);
            int d11 = MaterialColors.d(this, R$attr.colorOnSurface);
            int d12 = MaterialColors.d(this, R$attr.colorSurface);
            int[][] iArr = f9141l;
            int[] iArr2 = new int[iArr.length];
            iArr2[0] = MaterialColors.h(d12, d10, 1.0f);
            iArr2[1] = MaterialColors.h(d12, d11, 0.54f);
            iArr2[2] = MaterialColors.h(d12, d11, 0.38f);
            iArr2[3] = MaterialColors.h(d12, d11, 0.38f);
            this.f9142i = new ColorStateList(iArr, iArr2);
        }
        return this.f9142i;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f9143j && CompoundButtonCompat.b(this) == null) {
            setUseMaterialThemeColors(true);
        }
    }

    public void setUseMaterialThemeColors(boolean z10) {
        this.f9143j = z10;
        if (z10) {
            CompoundButtonCompat.c(this, getMaterialThemeColorsTintList());
        } else {
            CompoundButtonCompat.c(this, null);
        }
    }

    public MaterialRadioButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.radioButtonStyle);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MaterialRadioButton(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r4), attributeSet, i10);
        int i11 = f9140k;
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialRadioButton, i10, i11, new int[0]);
        int i12 = R$styleable.MaterialRadioButton_buttonTint;
        if (obtainStyledAttributes.hasValue(i12)) {
            CompoundButtonCompat.c(this, MaterialResources.a(context2, obtainStyledAttributes, i12));
        }
        this.f9143j = obtainStyledAttributes.getBoolean(R$styleable.MaterialRadioButton_useMaterialThemeColors, false);
        obtainStyledAttributes.recycle();
    }
}
