package m8;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.SmartChargeIncomePreference;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import h8.ISmartChargePresenter;
import k8.SmartChargePresenter;
import l8.ISmartChargeUpdate;
import y5.AppFeature;

/* compiled from: SmartChargeFragment.java */
/* renamed from: m8.l, reason: use source file name */
/* loaded from: classes2.dex */
public class SmartChargeFragment extends BasePreferenceFragment implements ISmartChargeUpdate {

    /* renamed from: e, reason: collision with root package name */
    private final String f15101e = "SmartChargeFragment";

    /* renamed from: f, reason: collision with root package name */
    private COUISwitchPreference f15102f = null;

    /* renamed from: g, reason: collision with root package name */
    private COUIPreferenceCategory f15103g = null;

    /* renamed from: h, reason: collision with root package name */
    private COUISwitchPreference f15104h = null;

    /* renamed from: i, reason: collision with root package name */
    private COUIJumpPreference f15105i = null;

    /* renamed from: j, reason: collision with root package name */
    private Context f15106j = null;

    /* renamed from: k, reason: collision with root package name */
    private ISmartChargePresenter f15107k = null;

    /* renamed from: l, reason: collision with root package name */
    private SmartChargeIncomePreference f15108l = null;

    /* renamed from: m, reason: collision with root package name */
    private Preference f15109m = null;

    /* renamed from: n, reason: collision with root package name */
    private Preference f15110n = null;

    private void d0() {
        boolean J0 = f6.f.J0(this.f15106j);
        COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) findPreference("smart_charge_switch");
        this.f15102f = cOUISwitchPreference;
        cOUISwitchPreference.setChecked(f6.f.J0(this.f15106j));
        this.f15102f.setOnPreferenceChangeListener(this.f15107k);
        this.f15108l = (SmartChargeIncomePreference) findPreference("income");
        this.f15109m = findPreference("view_one");
        this.f15110n = findPreference("view_two");
        this.f15103g = (COUIPreferenceCategory) findPreference("customize_charge");
        this.f15104h = (COUISwitchPreference) findPreference("customize_charge_switch");
        this.f15105i = (COUIJumpPreference) findPreference("customize_charge_apps");
        SmartChargeIncomePreference smartChargeIncomePreference = this.f15108l;
        if (smartChargeIncomePreference != null) {
            smartChargeIncomePreference.setVisible(J0);
            if (f6.f.J0(this.f15106j)) {
                this.f15108l.l();
            }
        }
        Preference preference = this.f15109m;
        if (preference != null) {
            preference.setVisible(J0);
        }
        Preference preference2 = this.f15110n;
        if (preference2 != null) {
            preference2.setVisible(J0);
        }
        if (this.f15103g != null) {
            if (AppFeature.C()) {
                this.f15103g.setVisible(J0);
                COUISwitchPreference cOUISwitchPreference2 = this.f15104h;
                if (cOUISwitchPreference2 != null) {
                    cOUISwitchPreference2.setChecked(f6.f.A(this.f15106j));
                    this.f15104h.setOnPreferenceChangeListener(this.f15107k);
                }
                COUIJumpPreference cOUIJumpPreference = this.f15105i;
                if (cOUIJumpPreference != null) {
                    cOUIJumpPreference.setVisible(f6.f.A(this.f15106j));
                    this.f15105i.setOnPreferenceClickListener(this.f15107k);
                    return;
                }
                return;
            }
            this.f15103g.setVisible(false);
        }
    }

    @Override // l8.ISmartChargeUpdate
    public void C(Boolean bool) {
        COUIJumpPreference cOUIJumpPreference = this.f15105i;
        if (cOUIJumpPreference != null) {
            cOUIJumpPreference.setVisible(bool.booleanValue());
        }
    }

    @Override // l8.ISmartChargeUpdate
    public void N() {
        getFragmentManager().m().s(R.id.fragment_container, new CustomizeChargeFragment(), "customize_charge_fragment").g(null).i();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.smart_charge_mode_title);
    }

    @Override // l8.ISmartChargeUpdate
    public void o(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15102f;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
        if (this.f15108l != null) {
            if (bool.booleanValue()) {
                this.f15108l.n();
                this.f15108l.l();
            } else {
                this.f15108l.m();
            }
            this.f15108l.setVisible(bool.booleanValue());
        }
        Preference preference = this.f15109m;
        if (preference != null) {
            preference.setVisible(bool.booleanValue());
        }
        Preference preference2 = this.f15110n;
        if (preference2 != null) {
            preference2.setVisible(bool.booleanValue());
        }
        if (this.f15103g != null) {
            if (AppFeature.C()) {
                this.f15103g.setVisible(bool.booleanValue());
            } else {
                this.f15103g.setVisible(false);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        LocalLog.a("SmartChargeFragment", "onAttach");
        super.onAttach(context);
        this.f15106j = context;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        LocalLog.a("SmartChargeFragment", "onCreate");
        SmartChargePresenter smartChargePresenter = new SmartChargePresenter(this);
        this.f15107k = smartChargePresenter;
        smartChargePresenter.a();
        super.onCreate(bundle);
        d0();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        LocalLog.a("SmartChargeFragment", "onCreatePreferences");
        addPreferencesFromResource(R.xml.smart_charge_preference);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        LocalLog.a("SmartChargeFragment", "onDestroy");
        if (this.f15108l != null && f6.f.J0(this.f15106j)) {
            this.f15108l.m();
        }
        super.onDestroy();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        LocalLog.a("SmartChargeFragment", "onDestroyView");
        super.onDestroyView();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("SmartChargeFragment", "onDetach");
        this.f15107k.onDetach();
        super.onDetach();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        LocalLog.a("SmartChargeFragment", "onResume");
        SmartChargeIncomePreference smartChargeIncomePreference = this.f15108l;
        if (smartChargeIncomePreference != null) {
            smartChargeIncomePreference.n();
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        LocalLog.a("SmartChargeFragment", "onViewCreated");
        super.onViewCreated(view, bundle);
        this.f15107k.b();
    }
}
