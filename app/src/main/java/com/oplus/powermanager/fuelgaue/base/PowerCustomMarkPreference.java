package com.oplus.powermanager.fuelgaue.base;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.preference.COUIMarkPreference;

/* loaded from: classes.dex */
public class PowerCustomMarkPreference extends COUIMarkPreference {
    public PowerCustomMarkPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
    }

    @Override // com.coui.appcompat.preference.COUIMarkPreference, androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.a(R.id.title);
        if (textView != null) {
            textView.setTextColor(getContext().getColor(com.oplus.battery.R.color.coui_color_primary_neutral));
        }
    }

    public PowerCustomMarkPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }

    public PowerCustomMarkPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PowerCustomMarkPreference(Context context) {
        super(context);
    }
}
