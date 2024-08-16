package com.oplus.powermanager.fuelgaue.basic.customized.recommend;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.preference.COUIRecommendedPreference;

/* loaded from: classes.dex */
public class BatteryRecommendedPreference extends COUIRecommendedPreference {
    public BatteryRecommendedPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.coui.appcompat.preference.COUIRecommendedPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.itemView;
        if (recyclerView.getAdapter() == null) {
            recyclerView.setFocusable(false);
            recyclerView.setFocusableInTouchMode(false);
        }
        super.onBindViewHolder(preferenceViewHolder);
    }

    public BatteryRecommendedPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
