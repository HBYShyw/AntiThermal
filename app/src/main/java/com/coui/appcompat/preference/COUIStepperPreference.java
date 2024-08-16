package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.stepper.COUIStepperView;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.support.list.R$attr;
import com.support.list.R$id;
import com.support.list.R$style;
import com.support.list.R$styleable;
import x2.OnStepChangeListener;

/* loaded from: classes.dex */
public class COUIStepperPreference extends COUIPreference implements OnStepChangeListener {
    private COUIStepperView F;
    private OnStepChangeListener G;
    private int H;
    private int I;
    private int J;
    private int K;

    public COUIStepperPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiStepperPreferenceStyle);
    }

    @Override // x2.OnStepChangeListener
    public void a(int i10, int i11) {
        this.I = i10;
        persistInt(i10);
        if (i10 != i11) {
            callChangeListener(Integer.valueOf(i10));
        }
        OnStepChangeListener onStepChangeListener = this.G;
        if (onStepChangeListener != null) {
            onStepChangeListener.a(i10, i11);
        }
    }

    public void h(int i10) {
        this.F.setCurStep(i10);
    }

    public void i(int i10) {
        this.J = i10;
        this.F.setMaximum(i10);
    }

    public void j(int i10) {
        this.K = i10;
        this.F.setMinimum(i10);
    }

    public void l(int i10) {
        this.H = i10;
        this.F.setUnit(i10);
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        COUIStepperView cOUIStepperView = (COUIStepperView) preferenceViewHolder.a(R$id.stepper);
        this.F = cOUIStepperView;
        if (cOUIStepperView != null) {
            i(this.J);
            j(this.K);
            h(this.I);
            l(this.H);
            this.F.setOnStepChangeListener(this);
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onDetached() {
        super.onDetached();
        COUIStepperView cOUIStepperView = this.F;
        if (cOUIStepperView != null) {
            cOUIStepperView.K();
        }
    }

    @Override // androidx.preference.Preference
    protected Object onGetDefaultValue(TypedArray typedArray, int i10) {
        return Integer.valueOf(typedArray.getInt(i10, 0));
    }

    @Override // androidx.preference.Preference
    protected void onSetInitialValue(Object obj) {
        if (obj == null) {
            obj = 0;
        }
        this.I = getPersistedInt(((Integer) obj).intValue());
    }

    public COUIStepperPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUIStepperPreference);
    }

    public COUIStepperPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIStepperPreference, i10, i11);
        this.J = obtainStyledAttributes.getInt(R$styleable.COUIStepperPreference_couiMaximum, CpuLevelConfig.ThermalCpuLevelPolicy.CPU_POWER_DEFAULT);
        this.K = obtainStyledAttributes.getInt(R$styleable.COUIStepperPreference_couiMinimum, -999);
        this.I = obtainStyledAttributes.getInt(R$styleable.COUIStepperPreference_couiDefStep, 0);
        this.H = obtainStyledAttributes.getInt(R$styleable.COUIStepperPreference_couiUnit, 1);
        obtainStyledAttributes.recycle();
    }
}
