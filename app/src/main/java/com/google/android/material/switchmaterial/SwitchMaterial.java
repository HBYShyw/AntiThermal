package com.google.android.material.switchmaterial;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import d4.MaterialThemeOverlay;
import r3.MaterialColors;
import u3.ElevationOverlayProvider;

/* loaded from: classes.dex */
public class SwitchMaterial extends SwitchCompat {

    /* renamed from: f0, reason: collision with root package name */
    private static final int f9255f0 = R$style.Widget_MaterialComponents_CompoundButton_Switch;

    /* renamed from: g0, reason: collision with root package name */
    private static final int[][] f9256g0 = {new int[]{R.attr.state_enabled, 16842912}, new int[]{R.attr.state_enabled, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};

    /* renamed from: b0, reason: collision with root package name */
    private final ElevationOverlayProvider f9257b0;

    /* renamed from: c0, reason: collision with root package name */
    private ColorStateList f9258c0;

    /* renamed from: d0, reason: collision with root package name */
    private ColorStateList f9259d0;

    /* renamed from: e0, reason: collision with root package name */
    private boolean f9260e0;

    public SwitchMaterial(Context context) {
        this(context, null);
    }

    private ColorStateList getMaterialThemeColorsThumbTintList() {
        if (this.f9258c0 == null) {
            int d10 = MaterialColors.d(this, R$attr.colorSurface);
            int d11 = MaterialColors.d(this, R$attr.colorControlActivated);
            float dimension = getResources().getDimension(R$dimen.mtrl_switch_thumb_elevation);
            if (this.f9257b0.e()) {
                dimension += ViewUtils.getParentAbsoluteElevation(this);
            }
            int c10 = this.f9257b0.c(d10, dimension);
            int[][] iArr = f9256g0;
            int[] iArr2 = new int[iArr.length];
            iArr2[0] = MaterialColors.h(d10, d11, 1.0f);
            iArr2[1] = c10;
            iArr2[2] = MaterialColors.h(d10, d11, 0.38f);
            iArr2[3] = c10;
            this.f9258c0 = new ColorStateList(iArr, iArr2);
        }
        return this.f9258c0;
    }

    private ColorStateList getMaterialThemeColorsTrackTintList() {
        if (this.f9259d0 == null) {
            int[][] iArr = f9256g0;
            int[] iArr2 = new int[iArr.length];
            int d10 = MaterialColors.d(this, R$attr.colorSurface);
            int d11 = MaterialColors.d(this, R$attr.colorControlActivated);
            int d12 = MaterialColors.d(this, R$attr.colorOnSurface);
            iArr2[0] = MaterialColors.h(d10, d11, 0.54f);
            iArr2[1] = MaterialColors.h(d10, d12, 0.32f);
            iArr2[2] = MaterialColors.h(d10, d11, 0.12f);
            iArr2[3] = MaterialColors.h(d10, d12, 0.12f);
            this.f9259d0 = new ColorStateList(iArr, iArr2);
        }
        return this.f9259d0;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f9260e0 && getThumbTintList() == null) {
            setThumbTintList(getMaterialThemeColorsThumbTintList());
        }
        if (this.f9260e0 && getTrackTintList() == null) {
            setTrackTintList(getMaterialThemeColorsTrackTintList());
        }
    }

    public void setUseMaterialThemeColors(boolean z10) {
        this.f9260e0 = z10;
        if (z10) {
            setThumbTintList(getMaterialThemeColorsThumbTintList());
            setTrackTintList(getMaterialThemeColorsTrackTintList());
        } else {
            setThumbTintList(null);
            setTrackTintList(null);
        }
    }

    public SwitchMaterial(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.switchStyle);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SwitchMaterial(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r4), attributeSet, i10);
        int i11 = f9255f0;
        Context context2 = getContext();
        this.f9257b0 = new ElevationOverlayProvider(context2);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.SwitchMaterial, i10, i11, new int[0]);
        this.f9260e0 = obtainStyledAttributes.getBoolean(R$styleable.SwitchMaterial_useMaterialThemeColors, false);
        obtainStyledAttributes.recycle();
    }
}
