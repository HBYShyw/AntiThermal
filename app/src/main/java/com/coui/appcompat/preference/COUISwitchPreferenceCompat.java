package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import androidx.preference.SwitchPreferenceCompat;
import com.coui.appcompat.couiswitch.COUISwitch;
import com.support.list.R$id;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUISwitchPreferenceCompat extends SwitchPreferenceCompat {

    /* renamed from: h, reason: collision with root package name */
    private final b f7036h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f7037i;

    /* renamed from: j, reason: collision with root package name */
    private COUISwitch f7038j;

    /* loaded from: classes.dex */
    private class b implements CompoundButton.OnCheckedChangeListener {
        private b() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z10) {
            if (COUISwitchPreferenceCompat.this.isChecked() == z10) {
                return;
            }
            if (!COUISwitchPreferenceCompat.this.g(Boolean.valueOf(z10))) {
                compoundButton.setChecked(!z10);
            } else {
                COUISwitchPreferenceCompat.this.setChecked(z10);
            }
        }
    }

    public COUISwitchPreferenceCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.switchPreferenceCompatStyle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g(Object obj) {
        if (getOnPreferenceChangeListener() == null) {
            return true;
        }
        return getOnPreferenceChangeListener().onPreferenceChange(this, obj);
    }

    @Override // androidx.preference.SwitchPreferenceCompat, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        View a10 = preferenceViewHolder.a(R$id.coui_preference);
        if (a10 != null) {
            a10.setSoundEffectsEnabled(false);
        }
        View a11 = preferenceViewHolder.a(R$id.switchWidget);
        boolean z10 = a11 instanceof COUISwitch;
        if (z10) {
            COUISwitch cOUISwitch = (COUISwitch) a11;
            cOUISwitch.setOnCheckedChangeListener(null);
            this.f7038j = cOUISwitch;
        }
        super.onBindViewHolder(preferenceViewHolder);
        if (z10) {
            ((COUISwitch) a11).setOnCheckedChangeListener(this.f7036h);
        }
        if (this.f7037i) {
            COUIPreferenceUtils.d(getContext(), preferenceViewHolder);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public void onClick() {
        COUISwitch cOUISwitch = this.f7038j;
        if (cOUISwitch != null) {
            cOUISwitch.setShouldPlaySound(true);
        }
        super.onClick();
    }

    public COUISwitchPreferenceCompat(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUISwitchPreferenceCompat(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10);
        this.f7036h = new b();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, i10, 0);
        this.f7037i = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_couiEnalbeClickSpan, false);
        obtainStyledAttributes.recycle();
    }
}
