package com.oplus.powermanager.fuelgaue.base;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

/* loaded from: classes.dex */
public class PowerConDetailPreference extends Preference {
    public PowerConDetailPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.a(R.id.title);
        if (textView != null) {
            textView.setTextColor(getContext().getColor(com.oplus.battery.R.color.coui_preference_title_color_normal));
        }
    }

    public PowerConDetailPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }

    public PowerConDetailPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PowerConDetailPreference(Context context) {
        super(context);
    }
}
