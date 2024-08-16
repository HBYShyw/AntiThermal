package d8;

import android.os.Bundle;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;

/* compiled from: PowerControlDetailFragment.java */
/* renamed from: d8.d, reason: use source file name */
/* loaded from: classes.dex */
public class PowerControlDetailFragment extends BasePreferenceFragment {
    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.function_detail);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.power_control_detail_preference);
    }
}
