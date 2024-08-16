package d8;

import android.app.Activity;
import android.os.Bundle;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.fuelgaue.base.PowerConDetailPreference;
import f6.CommonUtil;
import java.text.NumberFormat;

/* compiled from: PowerConsumptionDetailFragment.java */
/* renamed from: d8.c, reason: use source file name */
/* loaded from: classes.dex */
public class PowerConsumptionDetailFragment extends BasePreferenceFragment {

    /* renamed from: e, reason: collision with root package name */
    private Activity f10814e = null;

    /* renamed from: f, reason: collision with root package name */
    private String f10815f;

    /* renamed from: g, reason: collision with root package name */
    private COUIPreferenceCategory f10816g;

    /* renamed from: h, reason: collision with root package name */
    private int[] f10817h;

    /* renamed from: i, reason: collision with root package name */
    private double[] f10818i;

    private void d0(COUIPreferenceCategory cOUIPreferenceCategory, CharSequence charSequence, CharSequence charSequence2) {
        PowerConDetailPreference powerConDetailPreference = new PowerConDetailPreference(this.f10814e);
        powerConDetailPreference.setTitle(charSequence);
        powerConDetailPreference.setSummary(charSequence2);
        powerConDetailPreference.setSelectable(false);
        cOUIPreferenceCategory.d(powerConDetailPreference);
    }

    private void e0() {
        Bundle extras = this.f10814e.getIntent().getExtras();
        if (extras != null) {
            this.f10815f = extras.getString("title");
            this.f10817h = extras.getIntArray("types");
            this.f10818i = extras.getDoubleArray("values");
            return;
        }
        LocalLog.b("PowerConsumptionDetailFragment", "createDetails failed: args is null");
    }

    private void f0() {
        String str;
        this.f10816g = (COUIPreferenceCategory) findPreference("pm_consumption_analysis");
        if (this.f10817h == null || this.f10818i == null) {
            return;
        }
        int i10 = 0;
        while (true) {
            int[] iArr = this.f10817h;
            if (i10 >= iArr.length) {
                return;
            }
            if (!h0(iArr[i10], this.f10818i[i10])) {
                String string = getString(this.f10817h[i10]);
                switch (this.f10817h[i10]) {
                    case R.string.usage_type_actual_power /* 2131755713 */:
                    case R.string.usage_type_computed_power /* 2131755715 */:
                    case R.string.usage_type_total_battery_capacity /* 2131755725 */:
                        str = NumberFormat.getInstance().format((long) this.f10818i[i10]) + " " + getString(R.string.mah_new);
                        break;
                    case R.string.usage_type_background_activity /* 2131755714 */:
                    case R.string.usage_type_cpu /* 2131755716 */:
                    case R.string.usage_type_foreground_activity /* 2131755721 */:
                    case R.string.usage_type_gps /* 2131755722 */:
                    case R.string.usage_type_on_time /* 2131755724 */:
                    default:
                        str = CommonUtil.f(this.f10814e, this.f10818i[i10], true);
                        break;
                    case R.string.usage_type_data_recv /* 2131755717 */:
                    case R.string.usage_type_data_send /* 2131755718 */:
                    case R.string.usage_type_data_wifi_recv /* 2131755719 */:
                    case R.string.usage_type_data_wifi_send /* 2131755720 */:
                        str = Long.toString((long) this.f10818i[i10]);
                        break;
                    case R.string.usage_type_no_coverage /* 2131755723 */:
                        str = CommonUtil.h((int) Math.floor(this.f10818i[i10]));
                        break;
                }
                d0(this.f10816g, string, str);
            }
            i10++;
        }
    }

    private void g0() {
        f0();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000b. Please report as an issue. */
    private boolean h0(int i10, double d10) {
        switch (i10) {
            case R.string.usage_type_actual_power /* 2131755713 */:
            case R.string.usage_type_data_recv /* 2131755717 */:
            case R.string.usage_type_data_send /* 2131755718 */:
            case R.string.usage_type_data_wifi_recv /* 2131755719 */:
            case R.string.usage_type_data_wifi_send /* 2131755720 */:
            case R.string.usage_type_no_coverage /* 2131755723 */:
            case R.string.usage_type_total_battery_capacity /* 2131755725 */:
                if (d10 >= 1.0d) {
                    return false;
                }
                return true;
            case R.string.usage_type_background_activity /* 2131755714 */:
            case R.string.usage_type_cpu /* 2131755716 */:
            case R.string.usage_type_foreground_activity /* 2131755721 */:
            case R.string.usage_type_gps /* 2131755722 */:
            case R.string.usage_type_on_time /* 2131755724 */:
            case R.string.usage_type_wake_lock /* 2131755726 */:
                if (((int) Math.floor(d10 / 1000.0d)) >= 1) {
                    return false;
                }
                d0(this.f10816g, getString(i10), "< " + getString(R.string.battery_history_seconds, 1));
                return true;
            case R.string.usage_type_computed_power /* 2131755715 */:
                if (d10 >= 1.0d) {
                    return false;
                }
                String string = getString(i10);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("< ");
                sb2.append(NumberFormat.getInstance().format(1L) + " " + getString(R.string.mah_new));
                d0(this.f10816g, string, sb2.toString());
                return true;
            default:
                if (((int) Math.floor(d10 / 1000.0d)) >= 1) {
                    return false;
                }
                return true;
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return this.f10815f;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        LocalLog.a("PowerConsumptionDetailFragment", "onAttach");
        super.onAttach(activity);
        this.f10814e = activity;
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.pm_power_consumption_detail);
        e0();
        g0();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("PowerConsumptionDetailFragment", "onDetach");
        super.onDetach();
        this.f10814e = null;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }
}
