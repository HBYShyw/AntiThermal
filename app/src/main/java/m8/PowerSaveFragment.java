package m8;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.PreferenceGroup;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.PowerSaveSecondActivity;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.fuelgaue.basic.customized.PowerSaveLevelPicker;
import h8.IPowerSavePresenter;
import k8.PowerSavePresenter;
import l8.IPowerSaveUpdate;

/* compiled from: PowerSaveFragment.java */
/* renamed from: m8.k, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSaveFragment extends BasePreferenceFragment implements IPowerSaveUpdate {

    /* renamed from: e, reason: collision with root package name */
    private final String f15083e = "PowerSaveFragment";

    /* renamed from: f, reason: collision with root package name */
    private final String f15084f = "open_level_category";

    /* renamed from: g, reason: collision with root package name */
    private final String f15085g = "default_optimization_category";

    /* renamed from: h, reason: collision with root package name */
    private final String f15086h = "bottom_preference";

    /* renamed from: i, reason: collision with root package name */
    private boolean f15087i = false;

    /* renamed from: j, reason: collision with root package name */
    private PreferenceGroup f15088j = null;

    /* renamed from: k, reason: collision with root package name */
    private COUIPreferenceCategory f15089k = null;

    /* renamed from: l, reason: collision with root package name */
    private COUISwitchPreference f15090l = null;

    /* renamed from: m, reason: collision with root package name */
    private COUISwitchPreference f15091m = null;

    /* renamed from: n, reason: collision with root package name */
    private COUISwitchPreference f15092n = null;

    /* renamed from: o, reason: collision with root package name */
    private COUIJumpPreference f15093o = null;

    /* renamed from: p, reason: collision with root package name */
    private COUISwitchPreference f15094p = null;

    /* renamed from: q, reason: collision with root package name */
    private PowerSaveLevelPicker f15095q = null;

    /* renamed from: r, reason: collision with root package name */
    private Context f15096r = null;

    /* renamed from: s, reason: collision with root package name */
    private IPowerSavePresenter f15097s = null;

    /* compiled from: PowerSaveFragment.java */
    /* renamed from: m8.k$a */
    /* loaded from: classes2.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f15098e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ boolean f15099f;

        a(boolean z10, boolean z11) {
            this.f15098e = z10;
            this.f15099f = z11;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f15098e) {
                PowerSaveFragment.this.f15094p.setChecked(this.f15099f);
                if (this.f15099f) {
                    PowerSaveFragment.this.f15094p.setEnabled(false);
                    return;
                } else {
                    PowerSaveFragment.this.f15094p.setEnabled(true);
                    return;
                }
            }
            if (PowerSaveFragment.this.f15094p != null) {
                PowerSaveFragment.this.f15094p.setVisible(false);
            }
        }
    }

    private void e0() {
        if (!f6.f.s1()) {
            ((PreferenceGroup) findPreference("super_power_save_category")).n(findPreference("super_power_save_switch_pref"));
            return;
        }
        this.f15094p = (COUISwitchPreference) findPreference("super_power_save_switch_pref");
        if (f6.f.s1() && !f6.f.r1(this.f15096r)) {
            this.f15094p.setChecked(f6.f.a1(this.f15096r));
            this.f15094p.setSummary(this.f15097s.b0(3));
            this.f15094p.setOnPreferenceChangeListener(this.f15097s);
            return;
        }
        this.f15094p.setVisible(false);
    }

    @Override // l8.IPowerSaveUpdate
    public void F() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException unused) {
            LocalLog.a("PowerSaveFragment", "thread sleep error");
        }
        getActivity().finish();
    }

    @Override // l8.IPowerSaveUpdate
    public void L() {
        if (this.f15089k == null) {
            LocalLog.b("PowerSaveFragment", "addLevelPickerPreference : OpenLevelCategory is null");
            return;
        }
        if (this.f15095q == null) {
            this.f15095q = new PowerSaveLevelPicker(this.f15096r, null, 0, 0);
        }
        this.f15095q.j(false);
        this.f15089k.d(this.f15095q);
        this.f15095q.setOrder(2);
    }

    @Override // l8.IPowerSaveUpdate
    public void S() {
        PowerSaveLevelPicker powerSaveLevelPicker = this.f15095q;
        if (powerSaveLevelPicker != null) {
            powerSaveLevelPicker.j(true);
            this.f15089k.n(this.f15095q);
        }
    }

    @Override // l8.IPowerSaveUpdate
    public void d(boolean z10, boolean z11) {
        getActivity().runOnUiThread(new a(z10, z11));
    }

    @Override // l8.IPowerSaveUpdate
    public void f(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15090l;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.power_save_mode_title);
    }

    @Override // l8.IPowerSaveUpdate
    public void k() {
        this.f15096r.startActivity(new Intent(this.f15096r, (Class<?>) PowerSaveSecondActivity.class));
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.f15096r = context;
        this.f15097s = new PowerSavePresenter(context, this);
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Log.d("PowerSaveFragment", "config change");
        PowerSaveLevelPicker powerSaveLevelPicker = this.f15095q;
        if (powerSaveLevelPicker != null) {
            powerSaveLevelPicker.h(true);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        this.f15097s.onCreate(bundle);
        super.onCreate(bundle);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.power_save_preference);
        this.f15090l = (COUISwitchPreference) findPreference("power_save_switch");
        this.f15092n = (COUISwitchPreference) findPreference("auto_close_switch");
        this.f15090l.setOnPreferenceChangeListener(this.f15097s);
        this.f15092n.setOnPreferenceChangeListener(this.f15097s);
        this.f15089k = (COUIPreferenceCategory) findPreference("open_level_category");
        COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) findPreference("open_level_switch");
        this.f15091m = cOUISwitchPreference;
        cOUISwitchPreference.setOnPreferenceChangeListener(this.f15097s);
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("default_power_optimization");
        this.f15093o = cOUIJumpPreference;
        cOUIJumpPreference.setOnPreferenceClickListener(this.f15097s);
        e0();
        this.f15097s.onCreatePreferences(bundle, str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.f15095q = null;
        this.f15097s.onDestroy();
        super.onDestroy();
    }

    @Override // l8.IPowerSaveUpdate
    public void q(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15091m;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }

    @Override // l8.IPowerSaveUpdate
    public void s(Boolean bool) {
        COUISwitchPreference cOUISwitchPreference = this.f15092n;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setChecked(bool.booleanValue());
        }
    }
}
