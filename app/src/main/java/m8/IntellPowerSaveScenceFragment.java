package m8;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.deepsleep.SuperSleepModeActivity;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.powermanager.fuelgaue.PerformanceModeActivity;
import com.oplus.powermanager.fuelgaue.PowerConsumptionOptimizationActivity;
import com.oplus.powermanager.fuelgaue.PowerUsageModelActivity;
import com.oplus.powermanager.fuelgaue.ScreenSaveScene;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import f6.CommonUtil;
import h8.IIntellPowerSaveScenePagePresenter;
import k8.IntellPowerSaveScenePagePresenter;
import l8.IIntellPowerSaveScenceUpdate;
import y5.AppFeature;

/* compiled from: IntellPowerSaveScenceFragment.java */
/* renamed from: m8.c, reason: use source file name */
/* loaded from: classes2.dex */
public class IntellPowerSaveScenceFragment extends BasePreferenceFragment implements Preference.d, IIntellPowerSaveScenceUpdate {

    /* renamed from: e, reason: collision with root package name */
    private COUISwitchPreference f14956e;

    /* renamed from: f, reason: collision with root package name */
    private IIntellPowerSaveScenePagePresenter f14957f;

    /* renamed from: j, reason: collision with root package name */
    private COUIJumpPreference f14961j;

    /* renamed from: l, reason: collision with root package name */
    private COUIJumpPreference f14963l;

    /* renamed from: g, reason: collision with root package name */
    private boolean f14958g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f14959h = false;

    /* renamed from: i, reason: collision with root package name */
    private COUISwitchPreference f14960i = null;

    /* renamed from: k, reason: collision with root package name */
    private COUIJumpPreference f14962k = null;

    /* renamed from: m, reason: collision with root package name */
    private Activity f14964m = null;

    /* renamed from: n, reason: collision with root package name */
    private Context f14965n = null;

    private void d0() {
        if (this.f14960i == null) {
            this.f14960i = (COUISwitchPreference) findPreference("high_performance_switch_in_more");
            if (y5.b.B()) {
                this.f14960i.setVisible(false);
            } else {
                this.f14960i.setVisible(true);
            }
        }
        if (f6.f.a1(this.f14965n)) {
            this.f14960i.setEnabled(false);
        } else {
            this.f14960i.setEnabled(true);
        }
        if (1 == CommonUtil.A()) {
            this.f14960i.setChecked(true);
        } else {
            this.f14960i.setChecked(false);
        }
        this.f14960i.setOnPreferenceChangeListener(this.f14957f);
    }

    private void e0() {
        if (this.f14962k == null) {
            this.f14962k = (COUIJumpPreference) findPreference("performance_mode_in_more");
        }
        if (y5.b.B()) {
            this.f14962k.setVisible(true);
            this.f14962k.setOnPreferenceClickListener(this);
            if (f6.f.a1(this.f14965n)) {
                this.f14962k.setEnabled(false);
            } else {
                this.f14962k.setEnabled(true);
            }
            if (y5.b.A()) {
                this.f14962k.setVisible(false);
                return;
            }
            return;
        }
        this.f14962k.setVisible(false);
    }

    private void f0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("power_consumption_optimization");
        this.f14961j = cOUIJumpPreference;
        if (cOUIJumpPreference == null) {
            LocalLog.a("IntellPowerSaveScence", "no Power Consumption Optimization pref!");
        } else if (y5.b.k()) {
            this.f14961j.setVisible(false);
        } else {
            this.f14961j.setVisible(true);
            this.f14961j.setOnPreferenceClickListener(this);
        }
    }

    private void g0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("power_control_pref");
        this.f14963l = cOUIJumpPreference;
        cOUIJumpPreference.setOnPreferenceClickListener(this);
        if (!y5.b.I() || y5.b.k()) {
            this.f14963l.setVisible(false);
        }
    }

    private void h0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("app_freeze_pref");
        if (cOUIJumpPreference != null) {
            if (y5.b.m()) {
                cOUIJumpPreference.setVisible(true);
                cOUIJumpPreference.setOnPreferenceClickListener(this);
            } else {
                cOUIJumpPreference.setVisible(false);
            }
        }
    }

    private void i0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("intelligent_rm_sleep_mode");
        if (cOUIJumpPreference == null) {
            LocalLog.a("IntellPowerSaveScence", "no rm sleep mode pref!");
            return;
        }
        if (!y5.b.D()) {
            cOUIJumpPreference.setVisible(false);
            return;
        }
        if (!y5.b.I()) {
            cOUIJumpPreference.setVisible(false);
        } else {
            cOUIJumpPreference.setVisible(true);
        }
        cOUIJumpPreference.setOnPreferenceClickListener(this);
    }

    private void j0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("screen_save_preference");
        if (cOUIJumpPreference != null) {
            if (AppFeature.c()) {
                cOUIJumpPreference.setVisible(true);
                cOUIJumpPreference.setOnPreferenceClickListener(this);
            } else {
                cOUIJumpPreference.setVisible(false);
            }
        }
    }

    private void k0() {
        COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) findPreference("intelligent_deep_sleep_mode");
        this.f14956e = cOUISwitchPreference;
        if (cOUISwitchPreference == null) {
            LocalLog.a("IntellPowerSaveScence", "no sleep mode switch pref!");
            return;
        }
        if (!AppFeature.f() && !f6.f.q1(this.f14965n)) {
            if (y5.b.D()) {
                this.f14956e.setVisible(false);
                return;
            }
            if (!y5.b.I()) {
                this.f14956e.setVisible(false);
            } else {
                this.f14956e.setVisible(true);
            }
            this.f14956e.setOnPreferenceChangeListener(this.f14957f);
            boolean B = f6.f.B(this.f14964m);
            LocalLog.a("IntellPowerSaveScence", "sleepSwitchState=" + B);
            this.f14956e.setChecked(B);
            return;
        }
        this.f14956e.setChecked(false);
        this.f14956e.setSelectable(false);
        LocalLog.a("IntellPowerSaveScence", "Feature Disable DeepSleep");
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        String key = preference.getKey();
        if ("power_consumption_optimization".equals(key)) {
            Intent intent = new Intent(this.f14965n, (Class<?>) PowerConsumptionOptimizationActivity.class);
            intent.setFlags(603979776);
            intent.putExtra("navigate_title_id", R.string.power_usage_details);
            intent.putExtra("click_in", true);
            startActivity(intent);
        } else if (key.equals("power_control_pref")) {
            Intent intent2 = new Intent(this.f14965n, (Class<?>) PowerUsageModelActivity.class);
            intent2.setFlags(603979776);
            intent2.putExtra("navigate_title_id", R.string.power_usage_details);
            intent2.putExtra("click_in", true);
            startActivity(intent2);
        } else if ("performance_mode_in_more".equals(key)) {
            Intent intent3 = new Intent(this.f14965n, (Class<?>) PerformanceModeActivity.class);
            intent3.setFlags(603979776);
            intent3.putExtra("navigate_title_id", R.string.performance_mode_title);
            startActivity(intent3);
        } else if ("screen_save_preference".equals(key)) {
            Intent intent4 = new Intent(this.f14965n, (Class<?>) ScreenSaveScene.class);
            intent4.setFlags(603979776);
            intent4.putExtra("navigate_title_id", R.string.power_usage_details);
            startActivity(intent4);
        } else if ("intelligent_rm_sleep_mode".equals(key)) {
            Intent intent5 = new Intent(this.f14965n, (Class<?>) SuperSleepModeActivity.class);
            intent5.setFlags(603979776);
            intent5.putExtra("navigate_title_id", R.string.intelligent_sleep_mode);
            startActivity(intent5);
        } else if ("app_freeze_pref".equals(key)) {
            Intent intent6 = new Intent("oplus.intent.action.settings.APP_FORZEN_OPLUS_SETTINGS");
            intent6.setFlags(603979776);
            intent6.putExtra("navigate_title_text", getString(R.string.power_usage_details));
            LocalLog.a("IntellPowerSaveScence", "onPreferenceTreeClick: app freeze");
            try {
                startActivity(intent6);
            } catch (ActivityNotFoundException unused) {
                LocalLog.a("IntellPowerSaveScence", "start freeze AppFrozenSettingsActivity ActivityNotFoundException");
            }
        }
        return true;
    }

    @Override // l8.IIntellPowerSaveScenceUpdate
    public void e(boolean z10, boolean z11, boolean z12) {
        COUISwitchPreference cOUISwitchPreference = this.f14960i;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setEnabled(z10);
            if (y5.b.B()) {
                this.f14960i.setVisible(false);
            } else {
                this.f14960i.setVisible(z11);
            }
            this.f14960i.setChecked(z12);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.battery_ui_optimization_more_settings_new);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        LocalLog.a("IntellPowerSaveScence", "onAttach");
        super.onAttach(context);
        this.f14965n = context;
        this.f14964m = getActivity();
        this.f14957f = new IntellPowerSaveScenePagePresenter(context, this);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.intell_powe_save_scene);
        boolean i12 = f6.f.i1();
        this.f14958g = i12;
        if (i12) {
            d0();
            e0();
            this.f14957f.E(EventType.SCENE_MODE_AUDIO_OUT);
        } else {
            COUISwitchPreference cOUISwitchPreference = this.f14960i;
            if (cOUISwitchPreference != null) {
                cOUISwitchPreference.setVisible(false);
            }
        }
        f0();
        k0();
        g0();
        i0();
        h0();
        j0();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("IntellPowerSaveScence", "onDetach");
        if (this.f14958g) {
            this.f14957f.h(EventType.SCENE_MODE_AUDIO_OUT);
        }
        this.f14964m = null;
        this.f14965n = null;
        super.onDetach();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }
}
