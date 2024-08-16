package m8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISpannablePreference;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.fuelgaue.base.PowerCustomMarkPreference;
import com.oplus.powermanager.fuelgaue.base.WeakColorClickableSpan;
import com.oplus.powermanager.fuelgaue.basic.customized.LowPowerChargTimePicker;
import h8.IWirelessChargSetPagePresenter;
import k8.WirelessChargSetPagePresenter;
import l8.IWirelessChargSetPageUpdate;
import y5.AppFeature;

/* compiled from: WirelessChargingSettingsFragment.java */
/* renamed from: m8.m, reason: use source file name */
/* loaded from: classes2.dex */
public class WirelessChargingSettingsFragment extends BasePreferenceFragment implements IWirelessChargSetPageUpdate {

    /* renamed from: e, reason: collision with root package name */
    private COUIPreferenceCategory f15111e;

    /* renamed from: f, reason: collision with root package name */
    private COUIJumpPreference f15112f;

    /* renamed from: g, reason: collision with root package name */
    private COUISwitchPreference f15113g;

    /* renamed from: h, reason: collision with root package name */
    private PowerCustomMarkPreference f15114h;

    /* renamed from: i, reason: collision with root package name */
    private PowerCustomMarkPreference f15115i;

    /* renamed from: j, reason: collision with root package name */
    private PowerCustomMarkPreference f15116j;

    /* renamed from: k, reason: collision with root package name */
    private COUISpannablePreference f15117k;

    /* renamed from: l, reason: collision with root package name */
    private LowPowerChargTimePicker f15118l;

    /* renamed from: m, reason: collision with root package name */
    private LowPowerChargTimePicker.d f15119m;

    /* renamed from: n, reason: collision with root package name */
    private Context f15120n = null;

    /* renamed from: o, reason: collision with root package name */
    private Activity f15121o = null;

    /* renamed from: p, reason: collision with root package name */
    private long f15122p = 0;

    /* renamed from: q, reason: collision with root package name */
    private long f15123q = 0;

    /* renamed from: r, reason: collision with root package name */
    private boolean f15124r = false;

    /* renamed from: s, reason: collision with root package name */
    private int f15125s = 0;

    /* renamed from: t, reason: collision with root package name */
    private IWirelessChargSetPagePresenter f15126t;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessChargingSettingsFragment.java */
    /* renamed from: m8.m$a */
    /* loaded from: classes2.dex */
    public class a implements LowPowerChargTimePicker.d {
        a() {
        }

        @Override // com.oplus.powermanager.fuelgaue.basic.customized.LowPowerChargTimePicker.d
        public void a() {
            WirelessChargingSettingsFragment.this.f15126t.p();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessChargingSettingsFragment.java */
    /* renamed from: m8.m$b */
    /* loaded from: classes2.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (WirelessChargingSettingsFragment.this.f15124r) {
                WirelessChargingSettingsFragment wirelessChargingSettingsFragment = WirelessChargingSettingsFragment.this;
                wirelessChargingSettingsFragment.f15125s = f6.f.v0(wirelessChargingSettingsFragment.f15120n);
                if (WirelessChargingSettingsFragment.this.f15125s == 0) {
                    WirelessChargingSettingsFragment.this.f15114h.setChecked(true);
                    WirelessChargingSettingsFragment.this.f15114h.setSelectable(false);
                    WirelessChargingSettingsFragment.this.f15115i.setChecked(false);
                    WirelessChargingSettingsFragment.this.f15115i.setSelectable(true);
                    WirelessChargingSettingsFragment.this.f15116j.setChecked(false);
                    WirelessChargingSettingsFragment.this.f15116j.setSelectable(true);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15114h);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15115i);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15116j);
                    WirelessChargingSettingsFragment.this.f15118l.l();
                    WirelessChargingSettingsFragment.this.f15111e.n(WirelessChargingSettingsFragment.this.f15118l);
                    return;
                }
                if (WirelessChargingSettingsFragment.this.f15125s == 1) {
                    WirelessChargingSettingsFragment.this.f15114h.setChecked(false);
                    WirelessChargingSettingsFragment.this.f15114h.setSelectable(true);
                    WirelessChargingSettingsFragment.this.f15115i.setChecked(true);
                    WirelessChargingSettingsFragment.this.f15115i.setSelectable(false);
                    WirelessChargingSettingsFragment.this.f15116j.setChecked(false);
                    WirelessChargingSettingsFragment.this.f15116j.setSelectable(true);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15114h);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15115i);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15116j);
                    WirelessChargingSettingsFragment.this.f15118l.l();
                    WirelessChargingSettingsFragment.this.f15111e.n(WirelessChargingSettingsFragment.this.f15118l);
                    return;
                }
                if (WirelessChargingSettingsFragment.this.f15125s == 2) {
                    WirelessChargingSettingsFragment.this.f15114h.setChecked(false);
                    WirelessChargingSettingsFragment.this.f15114h.setSelectable(true);
                    WirelessChargingSettingsFragment.this.f15115i.setChecked(false);
                    WirelessChargingSettingsFragment.this.f15115i.setSelectable(true);
                    WirelessChargingSettingsFragment.this.f15116j.setChecked(true);
                    WirelessChargingSettingsFragment.this.f15116j.setSelectable(false);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15114h);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15115i);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15116j);
                    WirelessChargingSettingsFragment.this.f15111e.d(WirelessChargingSettingsFragment.this.f15118l);
                    return;
                }
                return;
            }
            WirelessChargingSettingsFragment.this.f15111e.n(WirelessChargingSettingsFragment.this.f15114h);
            WirelessChargingSettingsFragment.this.f15111e.n(WirelessChargingSettingsFragment.this.f15115i);
            WirelessChargingSettingsFragment.this.f15111e.n(WirelessChargingSettingsFragment.this.f15116j);
            WirelessChargingSettingsFragment.this.f15118l.l();
            WirelessChargingSettingsFragment.this.f15111e.n(WirelessChargingSettingsFragment.this.f15118l);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessChargingSettingsFragment.java */
    /* renamed from: m8.m$c */
    /* loaded from: classes2.dex */
    public class c implements WeakColorClickableSpan.SpannableStrClickListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Intent f15129a;

        c(Intent intent) {
            this.f15129a = intent;
        }

        @Override // com.oplus.powermanager.fuelgaue.base.WeakColorClickableSpan.SpannableStrClickListener
        public void onClick(Context context) {
            try {
                WirelessChargingSettingsFragment.this.startActivity(this.f15129a);
            } catch (Exception e10) {
                LocalLog.b("WirelessChargingSettingsFragment", e10.toString());
            }
        }
    }

    private SpannableString n0(Context context, int i10, int i11, int i12, Intent intent) {
        String str;
        String string = context.getString(i11);
        if (y5.b.J()) {
            str = "";
        } else {
            str = "\n" + context.getString(i12);
        }
        StringBuffer stringBuffer = new StringBuffer(context.getString(i10, string));
        stringBuffer.append(str);
        SpannableString spannableString = new SpannableString(stringBuffer);
        WeakColorClickableSpan weakColorClickableSpan = new WeakColorClickableSpan(context, intent);
        weakColorClickableSpan.setStatusBarClickListener(new c(intent));
        int length = string.length();
        int lastIndexOf = stringBuffer.lastIndexOf(string);
        spannableString.setSpan(weakColorClickableSpan, lastIndexOf, length + lastIndexOf, 33);
        return spannableString;
    }

    private void o0() {
        this.f15124r = f6.f.W(this.f15120n);
    }

    private void p0() {
        String str;
        this.f15117k.setVisible(true);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(this.f15120n.getResources().getString(R.string.store_host)));
        intent.setFlags(335544320);
        intent.putExtra("navigate_title_id", R.string.power_usage_details);
        if (f6.f.Q(this.f15120n) && !AppFeature.D() && !Build.BRAND.equals("OnePlus")) {
            this.f15117k.setSummary(n0(this.f15120n, R.string.charging_base_settings_note1, R.string.charging_base_settings_note2, R.string.wireless_charge_forbbiden_by_wired_otg, intent));
            return;
        }
        if (Build.BRAND.equals("OnePlus")) {
            return;
        }
        COUISpannablePreference cOUISpannablePreference = this.f15117k;
        StringBuilder sb2 = new StringBuilder();
        Context context = this.f15120n;
        sb2.append(context.getString(R.string.charging_base_settings_note1, context.getString(R.string.charging_base_settings_note2)));
        if (y5.b.J()) {
            str = "";
        } else {
            str = "\n" + this.f15120n.getString(R.string.wireless_charge_forbbiden_by_wired_otg);
        }
        sb2.append(str);
        cOUISpannablePreference.setSummary(sb2.toString());
    }

    private void q0() {
        this.f15111e = (COUIPreferenceCategory) findPreference("charging_base_settings_category");
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("wireless_charging_guide_pref");
        this.f15112f = cOUIJumpPreference;
        if (cOUIJumpPreference != null) {
            if (AppFeature.v()) {
                this.f15112f.setVisible(true);
                this.f15112f.setOnPreferenceClickListener(this.f15126t);
            } else {
                this.f15112f.setVisible(false);
            }
        }
        this.f15113g = (COUISwitchPreference) findPreference("low_power_charging_pref");
        this.f15114h = (PowerCustomMarkPreference) findPreference("sleep_optimize_pref");
        this.f15115i = (PowerCustomMarkPreference) findPreference("slient_alwayson_pref");
        this.f15116j = (PowerCustomMarkPreference) findPreference("custom_optimize_pref");
        this.f15118l = (LowPowerChargTimePicker) findPreference("fixed_time_picker_pref");
        this.f15117k = (COUISpannablePreference) findPreference("wireless_charging_note_pref");
        if (y5.b.x() && AppFeature.w() != 1) {
            this.f15113g.setVisible(true);
            this.f15113g.setOnPreferenceChangeListener(this.f15126t);
            this.f15113g.setChecked(this.f15124r);
            this.f15114h.setVisible(true);
            this.f15114h.setOnPreferenceChangeListener(this.f15126t);
            this.f15115i.setVisible(true);
            this.f15115i.setOnPreferenceChangeListener(this.f15126t);
            this.f15116j.setVisible(true);
            this.f15116j.setOnPreferenceChangeListener(this.f15126t);
            this.f15118l.setVisible(true);
            a aVar = new a();
            this.f15119m = aVar;
            this.f15118l.u(aVar);
            R();
            p0();
            return;
        }
        this.f15113g.setVisible(false);
        this.f15114h.setVisible(false);
        this.f15115i.setVisible(false);
        this.f15116j.setVisible(false);
        this.f15118l.setVisible(false);
        this.f15117k.setVisible(false);
    }

    @Override // l8.IWirelessChargSetPageUpdate
    public void P(boolean z10) {
        this.f15124r = z10;
    }

    @Override // l8.IWirelessChargSetPageUpdate
    public void R() {
        this.f15121o.runOnUiThread(new b());
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.wireless_charging_settings);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        LocalLog.a("WirelessChargingSettingsFragment", "onAttach");
        super.onAttach(context);
        this.f15120n = context.getApplicationContext();
        this.f15121o = getActivity();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f15126t = new WirelessChargSetPagePresenter(this);
        o0();
        q0();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.wireless_settings_scene);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("WirelessChargingSettingsFragment", "onDetach");
        this.f15120n = null;
        super.onDetach();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }
}
