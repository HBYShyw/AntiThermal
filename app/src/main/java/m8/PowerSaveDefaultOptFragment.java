package m8;

import android.content.Context;
import android.os.Bundle;
import androidx.preference.PreferenceCategory;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import h8.IPowerSaveDefaultOptPresenter;
import k8.PowerSaveDefaultOptPresenter;
import l8.IPowerSaveDefaultOptUpdate;

/* compiled from: PowerSaveDefaultOptFragment.java */
/* renamed from: m8.j, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSaveDefaultOptFragment extends BasePreferenceFragment implements IPowerSaveDefaultOptUpdate {

    /* renamed from: h, reason: collision with root package name */
    private IPowerSaveDefaultOptPresenter f15076h;

    /* renamed from: e, reason: collision with root package name */
    private final String f15073e = "default_optimization_category";

    /* renamed from: f, reason: collision with root package name */
    private final String f15074f = "bottom_preference";

    /* renamed from: g, reason: collision with root package name */
    private Context f15075g = null;

    /* renamed from: i, reason: collision with root package name */
    private COUISwitchPreference f15077i = null;

    /* renamed from: j, reason: collision with root package name */
    private COUISwitchPreference f15078j = null;

    /* renamed from: k, reason: collision with root package name */
    private COUISwitchPreference f15079k = null;

    /* renamed from: l, reason: collision with root package name */
    private COUISwitchPreference f15080l = null;

    /* renamed from: m, reason: collision with root package name */
    private COUISwitchPreference f15081m = null;

    /* renamed from: n, reason: collision with root package name */
    private COUIPreferenceCategory f15082n = null;

    @Override // l8.IPowerSaveDefaultOptUpdate
    public void A(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15077i;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }

    @Override // l8.IPowerSaveDefaultOptUpdate
    public void U(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15079k;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }

    @Override // l8.IPowerSaveDefaultOptUpdate
    public void X(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15081m;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }

    @Override // l8.IPowerSaveDefaultOptUpdate
    public void c0(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15078j;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.power_save_default_optimization_new);
    }

    @Override // l8.IPowerSaveDefaultOptUpdate
    public void m() {
        COUISwitchPreference cOUISwitchPreference;
        COUIPreferenceCategory cOUIPreferenceCategory = this.f15082n;
        if (cOUIPreferenceCategory == null || (cOUISwitchPreference = this.f15080l) == null) {
            return;
        }
        cOUIPreferenceCategory.n(cOUISwitchPreference);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.f15075g = context;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        PowerSaveDefaultOptPresenter powerSaveDefaultOptPresenter = new PowerSaveDefaultOptPresenter(this);
        this.f15076h = powerSaveDefaultOptPresenter;
        powerSaveDefaultOptPresenter.onCreate(bundle);
        super.onCreate(bundle);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.power_default_optimization_preference);
        ((PreferenceCategory) findPreference("bottom_preference")).setOrder(Integer.MAX_VALUE);
        this.f15082n = (COUIPreferenceCategory) findPreference("default_optimization_category");
        this.f15078j = (COUISwitchPreference) findPreference("screen_auto_off_switch");
        this.f15077i = (COUISwitchPreference) findPreference("screen_bright_switch");
        this.f15079k = (COUISwitchPreference) findPreference("back_synchronize_switch");
        this.f15080l = (COUISwitchPreference) findPreference("five_g_switch");
        this.f15078j.setOnPreferenceChangeListener(this.f15076h);
        this.f15077i.setOnPreferenceChangeListener(this.f15076h);
        this.f15079k.setOnPreferenceChangeListener(this.f15076h);
        this.f15080l.setOnPreferenceChangeListener(this.f15076h);
        this.f15076h.onCreatePreferences(bundle, str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        LocalLog.a("PowerSaveDefaultOptFragment", "onDestroy");
        this.f15077i = null;
        this.f15078j = null;
        this.f15079k = null;
        this.f15080l = null;
        this.f15075g = null;
        this.f15076h.onDestroy();
        super.onDestroy();
    }

    @Override // l8.IPowerSaveDefaultOptUpdate
    public void r() {
        if (this.f15081m != null || this.f15082n == null) {
            return;
        }
        COUISwitchPreference cOUISwitchPreference = new COUISwitchPreference(this.f15075g);
        this.f15081m = cOUISwitchPreference;
        this.f15082n.d(cOUISwitchPreference);
        this.f15081m.setKey("decrease_screen_refresh");
        this.f15081m.setTitle(getString(R.string.power_save_screen_refresh));
        this.f15081m.setOnPreferenceChangeListener(this.f15076h);
        this.f15081m.setOrder(2147483646);
    }

    @Override // l8.IPowerSaveDefaultOptUpdate
    public void u(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15080l;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }
}
