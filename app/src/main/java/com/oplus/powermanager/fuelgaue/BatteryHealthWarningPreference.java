package com.oplus.powermanager.fuelgaue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.BatteryHealthWarningPreference;
import f6.ChargeUtil;

/* loaded from: classes.dex */
public class BatteryHealthWarningPreference extends COUIPreference {
    private Context F;
    private TextView G;

    public BatteryHealthWarningPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        i(context);
    }

    private void i(Context context) {
        this.F = context;
        setLayoutResource(R.layout.battery_health_warning);
    }

    private void j(View view) {
        view.setForceDarkAllowed(false);
        this.G = (TextView) view.findViewById(R.id.battery_health_warning_text);
        view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() { // from class: y7.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                BatteryHealthWarningPreference.this.l(view2);
            }
        });
        m();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l(View view) {
        setVisible(false);
    }

    public void m() {
        int n10 = ChargeUtil.n(this.F);
        TextView textView = this.G;
        if (textView != null) {
            textView.setSelected(true);
            if (n10 > 0) {
                if (n10 < 80) {
                    this.G.setText(R.string.battery_health_value_too_low);
                }
            } else {
                if (n10 == -3) {
                    this.G.setText(R.string.battery_health_obtain_unknown_content);
                    return;
                }
                if (n10 == -2) {
                    this.G.setText(R.string.battery_health_obtain_abnormal_content);
                } else if (n10 == -1) {
                    this.G.setText(R.string.battery_health_obtain_fail_content);
                } else {
                    if (n10 != 0) {
                        return;
                    }
                    this.G.setText(R.string.battery_health_calculate_content);
                }
            }
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        j(preferenceViewHolder.itemView);
    }

    public BatteryHealthWarningPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        i(context);
    }

    public BatteryHealthWarningPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        i(context);
    }

    public BatteryHealthWarningPreference(Context context) {
        super(context);
        i(context);
    }
}
