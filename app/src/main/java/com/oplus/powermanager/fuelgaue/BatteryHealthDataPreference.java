package com.oplus.powermanager.fuelgaue;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.battery.R;
import f6.ChargeUtil;
import java.text.NumberFormat;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class BatteryHealthDataPreference extends COUIPreference {
    private Context F;
    private TextView G;

    public BatteryHealthDataPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        h(context);
    }

    private void h(Context context) {
        this.F = context;
        setLayoutResource(R.layout.battery_health_data);
    }

    private void i(View view) {
        view.setForceDarkAllowed(false);
        this.G = (TextView) view.findViewById(R.id.max_capacity_data);
        j();
    }

    public void j() {
        int n10 = ChargeUtil.n(this.F);
        double d10 = n10 / 100.0d;
        boolean a10 = COUIDarkModeUtil.a(this.F);
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        TextView textView = this.G;
        if (textView != null) {
            if (n10 >= 80) {
                textView.setText(percentInstance.format(d10));
                return;
            }
            if (n10 > 0 && n10 < 80) {
                textView.setText(percentInstance.format(d10));
                if (a10) {
                    this.G.setTextColor(Color.parseColor("#EA3939"));
                    return;
                } else {
                    this.G.setTextColor(Color.parseColor("#EC3E50"));
                    return;
                }
            }
            if (n10 == 0) {
                textView.setText(R.string.battery_health_calculate);
                if (a10) {
                    this.G.setTextColor(Color.parseColor("#5476FF"));
                    return;
                } else {
                    this.G.setTextColor(Color.parseColor("#2D40E9"));
                    return;
                }
            }
            textView.setText(R.string.battery_health_obtain_fail);
            if (a10) {
                this.G.setTextColor(Color.parseColor("#EA3939"));
            } else {
                this.G.setTextColor(Color.parseColor("#EC3E50"));
            }
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        i(preferenceViewHolder.itemView);
    }

    public BatteryHealthDataPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        h(context);
    }

    public BatteryHealthDataPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        h(context);
    }

    public BatteryHealthDataPreference(Context context) {
        super(context);
        h(context);
    }
}
