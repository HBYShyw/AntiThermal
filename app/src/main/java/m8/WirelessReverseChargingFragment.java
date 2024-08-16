package m8;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import b6.LocalLog;
import c9.WirelessChargingController;
import com.coui.appcompat.picker.COUINumberPicker;
import com.coui.appcompat.preference.COUIPreference;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.WirelessReverseGuideAnimationPreference;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.fuelgaue.basic.customized.WirelessReverseLevelPicker;
import h8.IReverseChargPagePresenter;
import k8.ReverseChargPagePresenter;
import l8.IReverseChargPageUpdate;
import m8.WirelessReverseChargingFragment;
import t8.PowerUsageManager;
import x5.UploadDataUtil;

/* compiled from: WirelessReverseChargingFragment.java */
/* renamed from: m8.o, reason: use source file name */
/* loaded from: classes2.dex */
public class WirelessReverseChargingFragment extends BasePreferenceFragment implements IReverseChargPageUpdate, WirelessReverseLevelPicker.c {

    /* renamed from: e, reason: collision with root package name */
    private COUIPreference f15132e;

    /* renamed from: f, reason: collision with root package name */
    private WirelessReverseLevelPicker f15133f;

    /* renamed from: g, reason: collision with root package name */
    private COUISwitchPreference f15134g;

    /* renamed from: k, reason: collision with root package name */
    private PowerUsageManager f15138k;

    /* renamed from: r, reason: collision with root package name */
    private IReverseChargPagePresenter f15145r;

    /* renamed from: s, reason: collision with root package name */
    private WirelessReverseGuideAnimationPreference f15146s;

    /* renamed from: h, reason: collision with root package name */
    private Context f15135h = null;

    /* renamed from: i, reason: collision with root package name */
    private Activity f15136i = null;

    /* renamed from: j, reason: collision with root package name */
    private UploadDataUtil f15137j = null;

    /* renamed from: l, reason: collision with root package name */
    private int f15139l = 25;

    /* renamed from: m, reason: collision with root package name */
    private int f15140m = 0;

    /* renamed from: n, reason: collision with root package name */
    private volatile int f15141n = 0;

    /* renamed from: o, reason: collision with root package name */
    private volatile int f15142o = 0;

    /* renamed from: p, reason: collision with root package name */
    private volatile int f15143p = 1;

    /* renamed from: q, reason: collision with root package name */
    private volatile int f15144q = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessReverseChargingFragment.java */
    /* renamed from: m8.o$a */
    /* loaded from: classes2.dex */
    public class a implements Runnable {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b() {
            if (WirelessReverseChargingFragment.this.getListView() == null || WirelessReverseChargingFragment.this.getListView().getAdapter() == null) {
                return;
            }
            WirelessReverseChargingFragment.this.getListView().smoothScrollToPosition(WirelessReverseChargingFragment.this.getListView().getAdapter().getItemCount() - 1);
        }

        @Override // java.lang.Runnable
        public void run() {
            WirelessReverseChargingFragment.this.f15133f.n(WirelessReverseChargingFragment.this.f15139l);
            WirelessReverseChargingFragment.this.f15133f.setVisible(!WirelessReverseChargingFragment.this.f15133f.isVisible());
            WirelessReverseChargingFragment.this.f15136i.getMainThreadHandler().post(new Runnable() { // from class: m8.n
                @Override // java.lang.Runnable
                public final void run() {
                    WirelessReverseChargingFragment.a.this.b();
                }
            });
        }
    }

    /* compiled from: WirelessReverseChargingFragment.java */
    /* renamed from: m8.o$b */
    /* loaded from: classes2.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f15148e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f15149f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ int f15150g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ int f15151h;

        b(int i10, int i11, int i12, int i13) {
            this.f15148e = i10;
            this.f15149f = i11;
            this.f15150g = i12;
            this.f15151h = i13;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i10;
            WirelessReverseChargingFragment.this.f15141n = this.f15148e;
            WirelessReverseChargingFragment.this.f15142o = this.f15149f;
            WirelessReverseChargingFragment.this.f15143p = this.f15150g;
            WirelessReverseChargingFragment.this.f15144q = this.f15151h;
            if (WirelessReverseChargingFragment.this.f15141n < WirelessReverseChargingFragment.this.f15139l) {
                WirelessReverseChargingFragment.this.f15134g.setChecked(false);
                WirelessReverseChargingFragment.this.f15134g.setEnabled(false);
                COUISwitchPreference cOUISwitchPreference = WirelessReverseChargingFragment.this.f15134g;
                WirelessReverseChargingFragment wirelessReverseChargingFragment = WirelessReverseChargingFragment.this;
                cOUISwitchPreference.setSummaryOff(wirelessReverseChargingFragment.getString(R.string.below_battery_level_disable, Integer.valueOf(wirelessReverseChargingFragment.f15139l)));
                return;
            }
            if ((WirelessReverseChargingFragment.this.f15143p == 2 || WirelessReverseChargingFragment.this.f15143p == 5) && WirelessReverseChargingFragment.this.f15142o == 4) {
                WirelessReverseChargingFragment.this.f15134g.setChecked(false);
                WirelessReverseChargingFragment.this.f15134g.setEnabled(false);
                WirelessReverseChargingFragment.this.f15134g.setSummaryOff(WirelessReverseChargingFragment.this.getString(R.string.reverse_disabled_on_wireless_charging_toast));
                return;
            }
            int i11 = this.f15149f;
            if ((((i11 == 1 || i11 == 2) && ((i10 = this.f15150g) == 2 || i10 == 5)) || WirelessChargingController.H(WirelessReverseChargingFragment.this.f15135h).K() == 1) && !y5.b.J()) {
                WirelessReverseChargingFragment.this.f15134g.setChecked(false);
                WirelessReverseChargingFragment.this.f15134g.setSummaryOff(WirelessReverseChargingFragment.this.getString(R.string.function_forbbiden_by_wired));
                WirelessReverseChargingFragment.this.f15134g.setEnabled(false);
            } else if (WirelessReverseChargingFragment.this.f15144q >= WirelessReverseChargingFragment.this.f15140m) {
                WirelessReverseChargingFragment.this.f15134g.setChecked(false);
                WirelessReverseChargingFragment.this.f15134g.setEnabled(false);
                WirelessReverseChargingFragment.this.f15134g.setSummaryOff(WirelessReverseChargingFragment.this.getString(R.string.reverse_disable_on_high_temp));
            } else {
                WirelessReverseChargingFragment.this.f15134g.setEnabled(true);
                WirelessReverseChargingFragment.this.f15134g.setSummaryOff((CharSequence) null);
            }
        }
    }

    /* compiled from: WirelessReverseChargingFragment.java */
    /* renamed from: m8.o$c */
    /* loaded from: classes2.dex */
    class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f15153e;

        c(boolean z10) {
            this.f15153e = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (WirelessReverseChargingFragment.this.f15134g.isChecked() != this.f15153e) {
                WirelessReverseChargingFragment.this.f15134g.setChecked(this.f15153e);
            }
        }
    }

    private void r0(COUINumberPicker cOUINumberPicker) {
        int value = (cOUINumberPicker.getValue() * 5) + 25;
        this.f15139l = value;
        this.f15132e.setAssignment(getString(R.string.less_than_percent, Integer.valueOf(value)));
        f6.f.x3(this.f15135h, this.f15139l);
        if (this.f15141n < this.f15139l) {
            f6.f.y3(this.f15135h, false);
            this.f15137j.R0(false, "change_threshold");
            this.f15134g.setEnabled(false);
            this.f15134g.setSummaryOff(getString(R.string.below_battery_level_disable, Integer.valueOf(this.f15139l)));
            return;
        }
        if ((this.f15143p == 2 || this.f15143p == 5) && this.f15142o == 4) {
            this.f15134g.setChecked(false);
            this.f15134g.setEnabled(false);
            this.f15134g.setSummaryOff(getString(R.string.reverse_disabled_on_wireless_charging_toast));
            return;
        }
        if ((((this.f15142o == 1 || this.f15142o == 2) && (this.f15143p == 2 || this.f15143p == 5)) || WirelessChargingController.H(this.f15135h).K() == 1) && !y5.b.J()) {
            this.f15134g.setChecked(false);
            this.f15134g.setEnabled(false);
            this.f15134g.setSummaryOff(getString(R.string.function_forbbiden_by_wired));
        } else if (this.f15144q >= this.f15140m) {
            this.f15134g.setChecked(false);
            this.f15134g.setEnabled(false);
            this.f15134g.setSummaryOff(getString(R.string.reverse_disable_on_high_temp));
        } else {
            this.f15134g.setEnabled(true);
            this.f15134g.setSummaryOff((CharSequence) null);
        }
    }

    private void s0() {
        this.f15146s = (WirelessReverseGuideAnimationPreference) findPreference("guide_animation");
        this.f15139l = f6.f.c1(this.f15135h);
        this.f15140m = f6.f.o0(this.f15135h);
        this.f15142o = this.f15138k.z();
        this.f15141n = this.f15138k.r();
        this.f15143p = this.f15138k.t();
        this.f15144q = this.f15138k.u();
        COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) findPreference("wireless_reverse_charging_pref");
        this.f15134g = cOUISwitchPreference;
        cOUISwitchPreference.setOnPreferenceChangeListener(this.f15145r);
        if (this.f15141n < this.f15139l) {
            this.f15137j.R0(false, "battLevel_below_threshold_oncreate");
            this.f15134g.setEnabled(false);
            this.f15134g.setSummaryOff(getString(R.string.below_battery_level_disable, Integer.valueOf(this.f15139l)));
        } else if ((this.f15143p == 5 || this.f15143p == 2) && this.f15142o == 4) {
            this.f15134g.setEnabled(false);
            this.f15134g.setSummaryOff(getString(R.string.reverse_disabled_on_wireless_charging_toast));
        } else if ((((this.f15142o == 1 || this.f15142o == 2) && (this.f15143p == 2 || this.f15143p == 5)) || WirelessChargingController.H(this.f15135h).K() == 1) && !y5.b.J()) {
            this.f15134g.setChecked(false);
            this.f15134g.setSummaryOff(getString(R.string.function_forbbiden_by_wired));
            this.f15134g.setEnabled(false);
        } else if (this.f15144q >= this.f15140m) {
            this.f15134g.setSummaryOff(getString(R.string.reverse_disable_on_high_temp));
            this.f15134g.setEnabled(false);
        }
        this.f15134g.setSummaryOn(getResources().getQuantityString(R.plurals.wait_time_auto_turnoff, 2, 2));
        this.f15134g.setChecked(f6.f.d1(this.f15135h));
        COUIPreference cOUIPreference = (COUIPreference) findPreference("battery_level_toolow_disable_pref");
        this.f15132e = cOUIPreference;
        cOUIPreference.setAssignment(getString(R.string.less_than_percent, Integer.valueOf(this.f15139l)));
        this.f15132e.setOnPreferenceClickListener(this.f15145r);
        WirelessReverseLevelPicker wirelessReverseLevelPicker = (WirelessReverseLevelPicker) findPreference("battery_level_picker_pref");
        this.f15133f = wirelessReverseLevelPicker;
        if (wirelessReverseLevelPicker != null) {
            wirelessReverseLevelPicker.o(this);
            this.f15133f.n(this.f15139l);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.basic.customized.WirelessReverseLevelPicker.c
    public void T(COUINumberPicker cOUINumberPicker, int i10) {
        r0(cOUINumberPicker);
    }

    @Override // l8.IReverseChargPageUpdate
    public void W() {
        this.f15136i.runOnUiThread(new a());
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.wireless_reverse_charging_title);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        LocalLog.a("WirelessReverseChargingFragment", "onAttach");
        super.onAttach(context);
        this.f15135h = context.getApplicationContext();
        this.f15136i = getActivity();
        this.f15138k = PowerUsageManager.x(this.f15135h);
        this.f15137j = UploadDataUtil.S0(this.f15135h);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        ReverseChargPagePresenter reverseChargPagePresenter = new ReverseChargPagePresenter(this);
        this.f15145r = reverseChargPagePresenter;
        reverseChargPagePresenter.a();
        super.onCreate(bundle);
        s0();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.wireless_reverse_scene);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        WirelessReverseGuideAnimationPreference wirelessReverseGuideAnimationPreference = this.f15146s;
        if (wirelessReverseGuideAnimationPreference != null) {
            wirelessReverseGuideAnimationPreference.l();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("WirelessReverseChargingFragment", "onDetach");
        this.f15145r.onDetach();
        super.onDetach();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        WirelessReverseGuideAnimationPreference wirelessReverseGuideAnimationPreference = this.f15146s;
        if (wirelessReverseGuideAnimationPreference != null) {
            wirelessReverseGuideAnimationPreference.j();
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.f15145r.b();
    }

    @Override // l8.IReverseChargPageUpdate
    public void t(int i10, int i11, int i12, int i13) {
        this.f15136i.runOnUiThread(new b(i12, i11, i10, i13));
    }

    @Override // l8.IReverseChargPageUpdate
    public void v(boolean z10) {
        this.f15136i.runOnUiThread(new c(z10));
    }
}
