package m8;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import b6.LocalLog;
import b9.PowerUtils;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.google.android.material.appbar.COUIDividerAppBarLayout;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.PowerControlActivity;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.powercurve.graph.SipperFSPreference;
import f6.CommonUtil;
import h8.IPowerControlPresenter;
import java.text.NumberFormat;
import java.util.HashMap;
import k8.PowerControlPresenter;
import r9.SimplePowerMonitorUtils;
import v4.CustmizeAllowBgRunable;
import x1.COUIAlertDialogBuilder;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: PowerControlFragment.java */
/* renamed from: m8.f, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerControlFragment extends BasePreferenceFragment implements Preference.c, View.OnClickListener, Preference.d {
    private AlertDialog D;
    private String F;
    private IPowerControlPresenter G;

    /* renamed from: u, reason: collision with root package name */
    private String f15022u;

    /* renamed from: v, reason: collision with root package name */
    private String f15023v;

    /* renamed from: w, reason: collision with root package name */
    private int[] f15024w;

    /* renamed from: x, reason: collision with root package name */
    private double[] f15025x;

    /* renamed from: e, reason: collision with root package name */
    private COUIPreferenceCategory f15006e = null;

    /* renamed from: f, reason: collision with root package name */
    private COUIPreferenceCategory f15007f = null;

    /* renamed from: g, reason: collision with root package name */
    private COUIPreferenceCategory f15008g = null;

    /* renamed from: h, reason: collision with root package name */
    private COUIPreferenceCategory f15009h = null;

    /* renamed from: i, reason: collision with root package name */
    private COUISwitchPreference f15010i = null;

    /* renamed from: j, reason: collision with root package name */
    private COUISwitchPreference f15011j = null;

    /* renamed from: k, reason: collision with root package name */
    private COUISwitchPreference f15012k = null;

    /* renamed from: l, reason: collision with root package name */
    private COUISwitchPreference f15013l = null;

    /* renamed from: m, reason: collision with root package name */
    private Preference f15014m = null;

    /* renamed from: n, reason: collision with root package name */
    private Preference f15015n = null;

    /* renamed from: o, reason: collision with root package name */
    private Preference f15016o = null;

    /* renamed from: p, reason: collision with root package name */
    private Preference f15017p = null;

    /* renamed from: q, reason: collision with root package name */
    private Preference f15018q = null;

    /* renamed from: r, reason: collision with root package name */
    private Preference f15019r = null;

    /* renamed from: s, reason: collision with root package name */
    private SipperFSPreference f15020s = null;

    /* renamed from: t, reason: collision with root package name */
    private Activity f15021t = null;

    /* renamed from: y, reason: collision with root package name */
    private int f15026y = -1;

    /* renamed from: z, reason: collision with root package name */
    private boolean f15027z = false;
    private boolean A = false;
    private AlertDialog B = null;
    private AlertDialog C = null;
    private UploadDataUtil E = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerControlFragment.java */
    /* renamed from: m8.f$a */
    /* loaded from: classes2.dex */
    public class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            PowerControlFragment.this.f15010i.setChecked(false);
            PowerControlFragment.this.f15027z = false;
            PowerControlFragment.this.G.a();
            HashMap hashMap = new HashMap();
            hashMap.put("foreground_act_switch_close", PowerControlFragment.this.F);
            PowerControlFragment.this.E.i0(hashMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerControlFragment.java */
    /* renamed from: m8.f$b */
    /* loaded from: classes2.dex */
    public class b implements DialogInterface.OnClickListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            PowerControlFragment.this.f15010i.setChecked(true);
            PowerControlFragment.this.f15027z = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerControlFragment.java */
    /* renamed from: m8.f$c */
    /* loaded from: classes2.dex */
    public class c implements DialogInterface.OnClickListener {
        c() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            PowerControlFragment.this.f15011j.setChecked(true);
            PowerControlFragment.this.f15010i.setChecked(true);
            PowerControlFragment.this.f15010i.setEnabled(false);
            PowerControlFragment.this.A = false;
            PowerControlFragment.this.G.i();
            HashMap hashMap = new HashMap();
            hashMap.put("background_act_switch_open", PowerControlFragment.this.F);
            PowerControlFragment.this.E.i0(hashMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerControlFragment.java */
    /* renamed from: m8.f$d */
    /* loaded from: classes2.dex */
    public class d implements DialogInterface.OnClickListener {
        d() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            PowerControlFragment.this.f15011j.setChecked(false);
            PowerControlFragment.this.f15010i.setEnabled(true);
            PowerControlFragment.this.A = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerControlFragment.java */
    /* renamed from: m8.f$e */
    /* loaded from: classes2.dex */
    public class e implements DialogInterface.OnClickListener {
        e() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            LocalLog.a("PowerControlFragment", "handleAutoStartDisable");
            PowerControlFragment.this.f15013l.setChecked(false);
        }
    }

    private void l0() {
        if (this.A) {
            return;
        }
        this.A = true;
        Activity activity = this.f15021t;
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(activity, f6.f.k(activity));
        cOUIAlertDialogBuilder.t(getString(R.string.allow_app_background, this.f15022u));
        cOUIAlertDialogBuilder.Y(R.string.allow_app_warning);
        cOUIAlertDialogBuilder.e0(R.string.allow_text, new c());
        cOUIAlertDialogBuilder.a0(R.string.force_stop_cancel, new d());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.C = a10;
        Window window = a10.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.C.show();
    }

    private void m0() {
        if (this.f15027z) {
            return;
        }
        this.f15027z = true;
        Activity activity = this.f15021t;
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(activity, f6.f.k(activity));
        cOUIAlertDialogBuilder.t(getString(R.string.prohibit_app_foreground, this.f15022u));
        cOUIAlertDialogBuilder.Y(R.string.prohibit_app_warning);
        cOUIAlertDialogBuilder.e0(R.string.prohibit_text, new a());
        cOUIAlertDialogBuilder.a0(R.string.force_stop_cancel, new b());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.B = a10;
        Window window = a10.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.B.show();
    }

    private void n0() {
        String str;
        if (this.f15024w == null || this.f15025x == null) {
            return;
        }
        int i10 = 0;
        while (true) {
            int[] iArr = this.f15024w;
            if (i10 >= iArr.length) {
                return;
            }
            if (!s0(iArr[i10], this.f15025x[i10])) {
                String string = getString(this.f15024w[i10]);
                if (this.f15024w[i10] != R.string.usage_type_computed_power) {
                    str = CommonUtil.f(this.f15021t, this.f15025x[i10], true);
                } else {
                    str = NumberFormat.getInstance().format((long) this.f15025x[i10]) + " " + getString(R.string.mah_new);
                }
                r0(this.f15024w[i10], string, str);
            }
            i10++;
        }
    }

    private void o0() {
        Activity activity = this.f15021t;
        if (activity != null) {
            Bundle extras = activity.getIntent().getExtras();
            if (extras != null) {
                this.f15022u = extras.getString("title");
                this.f15024w = extras.getIntArray("types");
                this.f15025x = extras.getDoubleArray("values");
            }
            this.f15023v = this.f15021t.getIntent().getStringExtra("drainType");
            this.F = this.f15021t.getIntent().getStringExtra("pkgName");
            this.f15026y = this.f15021t.getIntent().getIntExtra("callingSource", -1);
        }
        LocalLog.a("PowerControlFragment", "getArgs: Title=" + this.f15022u + " DrainType=" + this.f15023v + " PkgName=" + this.F);
    }

    private void p0() {
        if (this.D == null) {
            Activity activity = this.f15021t;
            COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(activity, f6.f.k(activity));
            if (AppFeature.D()) {
                cOUIAlertDialogBuilder.t(this.f15021t.getString(R.string.startup_forbid_dialog_title_device, new Object[]{20}));
            } else {
                cOUIAlertDialogBuilder.t(this.f15021t.getString(R.string.startup_forbid_dialog_title_device, new Object[]{5}));
            }
            cOUIAlertDialogBuilder.e0(R.string.auto_start_disable_confirm, new e());
            cOUIAlertDialogBuilder.d(false);
            AlertDialog a10 = cOUIAlertDialogBuilder.a();
            this.D = a10;
            Window window = a10.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            try {
                f6.f.n2(attributes, 1);
            } catch (Exception e10) {
                e10.printStackTrace();
            }
            window.setAttributes(attributes);
        }
        if (this.D.isShowing()) {
            return;
        }
        this.D.show();
    }

    private void q0() {
        this.f15006e = (COUIPreferenceCategory) findPreference("power_analysis_category");
        this.f15007f = (COUIPreferenceCategory) findPreference("power_limit_category");
        this.f15008g = (COUIPreferenceCategory) findPreference("start_limit_category");
        this.f15009h = (COUIPreferenceCategory) findPreference("force_stop_cat");
        this.f15014m = findPreference("fore_act_time");
        this.f15015n = findPreference("back_act_time");
        this.f15016o = findPreference("keep_active_time");
        this.f15018q = findPreference("wlan_upload_stream");
        this.f15019r = findPreference("wlan_download_stream");
        this.f15017p = findPreference("computed_power");
        this.f15020s = (SipperFSPreference) findPreference("force_stop");
        this.f15010i = (COUISwitchPreference) findPreference("foreground_act_switch");
        this.f15011j = (COUISwitchPreference) findPreference("background_act_switch");
        this.f15012k = (COUISwitchPreference) findPreference("passive_start_switch");
        this.f15013l = (COUISwitchPreference) findPreference("initiative_start_switch");
        this.f15010i.setOnPreferenceChangeListener(this);
        this.f15011j.setOnPreferenceChangeListener(this);
        this.f15012k.setOnPreferenceChangeListener(this);
        this.f15013l.setOnPreferenceChangeListener(this);
        this.f15020s.d(this);
        this.f15020s.setOnPreferenceClickListener(this);
        int d10 = PowerUtils.d(this.f15021t.getApplicationContext(), this.F);
        int c10 = PowerUtils.c(this.f15021t.getApplicationContext(), this.F);
        boolean z10 = (d10 & 1) != 0;
        boolean z11 = ((c10 & 1) == 0 && (c10 & 8) == 0) ? false : true;
        if (y5.b.I()) {
            if ("APP".equals(this.f15023v)) {
                if (PowerUtils.g(this.F)) {
                    this.f15007f.setVisible(true);
                    this.f15008g.setVisible(false);
                    this.f15010i.setChecked(true);
                    this.f15010i.setSelectable(false);
                    this.f15011j.setChecked(true);
                    this.f15011j.setSelectable(false);
                    this.f15018q.setVisible(true);
                    this.f15019r.setVisible(true);
                    LocalLog.l("PowerControlFragment", "init View: custom mTitle=" + this.f15022u + " mDrainType=" + this.f15023v);
                    return;
                }
                if (PowerUtils.i(this.f15021t.getApplicationContext(), this.F)) {
                    this.f15007f.setVisible(true);
                } else {
                    this.f15007f.setVisible(false);
                }
                this.f15008g.setVisible(true);
                int b10 = this.G.b();
                if (1 == b10) {
                    this.f15010i.setChecked(true);
                    this.f15010i.setEnabled(false);
                    this.f15011j.setChecked(true);
                } else if (2 == b10) {
                    this.f15010i.setChecked(false);
                    this.f15011j.setChecked(false);
                } else {
                    this.f15010i.setChecked(true);
                    this.f15011j.setChecked(false);
                }
                if (z10) {
                    this.f15013l.setVisible(false);
                } else {
                    this.f15013l.setChecked((d10 & 4) != 0);
                }
                if (z11) {
                    this.f15012k.setVisible(false);
                } else {
                    this.f15012k.setChecked(c10 == 4);
                }
                if (z10 && z11) {
                    this.f15008g.setVisible(false);
                }
                if (CustmizeAllowBgRunable.i(this.f15021t.getApplicationContext()).j().contains(this.F)) {
                    this.f15007f.setEnabled(false);
                    this.f15010i.setChecked(true);
                    this.f15011j.setChecked(true);
                }
            } else {
                this.f15007f.setVisible(false);
                this.f15008g.setVisible(false);
                this.f15014m.setVisible(false);
                this.f15015n.setVisible(false);
                this.f15016o.setVisible(false);
                this.f15018q.setVisible(false);
                this.f15019r.setVisible(false);
                this.f15009h.setVisible(false);
            }
        } else {
            this.f15007f.setVisible(false);
        }
        LocalLog.a("PowerControlFragment", "init View: mTitle=" + this.f15022u + " mDrainType=" + this.f15023v);
    }

    private void r0(int i10, String str, String str2) {
        switch (i10) {
            case R.string.battery_detail_view_keep_active_time /* 2131755069 */:
                this.f15016o.setTitle(str2);
                return;
            case R.string.battery_detail_view_wlan_download_stream /* 2131755072 */:
                this.f15019r.setTitle(str2);
                return;
            case R.string.battery_detail_view_wlan_upload_stream /* 2131755073 */:
                this.f15018q.setTitle(str2);
                return;
            case R.string.usage_type_background_activity /* 2131755714 */:
                this.f15015n.setTitle(str2);
                return;
            case R.string.usage_type_computed_power /* 2131755715 */:
                this.f15017p.setTitle(str2);
                LocalLog.a("PowerControlFragment", "computed power=" + str2);
                return;
            case R.string.usage_type_foreground_activity /* 2131755721 */:
            case R.string.usage_type_on_time /* 2131755724 */:
                this.f15014m.setTitle(str2);
                return;
            default:
                return;
        }
    }

    private boolean s0(int i10, double d10) {
        switch (i10) {
            case R.string.battery_detail_view_keep_active_time /* 2131755069 */:
            case R.string.usage_type_background_activity /* 2131755714 */:
            case R.string.usage_type_foreground_activity /* 2131755721 */:
                if (((int) Math.floor(d10 / 1000.0d)) >= 1) {
                    return false;
                }
                r0(i10, getString(i10), "< " + getString(R.string.battery_history_seconds, 1));
                break;
            case R.string.battery_detail_view_wlan_download_stream /* 2131755072 */:
            case R.string.battery_detail_view_wlan_upload_stream /* 2131755073 */:
                r0(i10, getString(i10), PowerUtils.b((long) d10));
                break;
            case R.string.usage_type_computed_power /* 2131755715 */:
                if (d10 >= 1.0d) {
                    return false;
                }
                String string = getString(i10);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("< ");
                sb2.append(NumberFormat.getInstance().format(1L) + " " + getString(R.string.mah_new));
                r0(i10, string, sb2.toString());
                break;
            default:
                if (((int) Math.floor(d10 / 1000.0d)) >= 1) {
                    return false;
                }
                break;
        }
        return true;
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        onClick(null);
        return true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.f15021t = getActivity();
        o0();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        LocalLog.a("PowerControlFragment", "onClick");
        IPowerControlPresenter iPowerControlPresenter = this.G;
        if (iPowerControlPresenter != null) {
            iPowerControlPresenter.g();
        }
        this.f15020s.e();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        PowerControlPresenter powerControlPresenter = new PowerControlPresenter(this.F, (PowerControlActivity) this.f15021t);
        this.G = powerControlPresenter;
        powerControlPresenter.onCreate(bundle);
        super.onCreate(bundle);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.power_control_preference);
        this.E = UploadDataUtil.S0(this.f15021t);
        q0();
        if (this.f15026y == -1) {
            this.f15006e.setVisible(false);
        } else {
            n0();
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        Activity activity = this.f15021t;
        if (activity != null) {
            ((COUIDividerAppBarLayout) activity.findViewById(R.id.custom_appBarLayout)).bindRecyclerView(getListView());
        }
        return onCreateView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.G.j();
        super.onDestroy();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        this.G.h();
        super.onPause();
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            String key = preference.getKey();
            if (key == null) {
                LocalLog.b("PowerControlFragment", "onPreferenceChange: key is null.");
                return false;
            }
            LocalLog.l("PowerControlFragment", "onPreferenceChange: key=" + key + ", check=" + booleanValue + ", pkg=" + this.F);
            HashMap hashMap = new HashMap();
            if ("foreground_act_switch".equals(key)) {
                if (!booleanValue) {
                    m0();
                } else {
                    this.G.e(true);
                    hashMap.put("foreground_act_switch_open", this.F);
                }
            } else if ("background_act_switch".equals(key)) {
                Intent intent = new Intent();
                intent.setAction("oplus.intent.action.high_power_consumption_notification.nomore");
                intent.setPackage("com.oplus.battery");
                intent.putExtra("pkgName", this.F);
                intent.putExtra("isNotificate", false);
                getContext().sendBroadcast(intent, "oplus.permission.OPLUS_COMPONENT_SAFE");
                if (booleanValue) {
                    l0();
                } else {
                    this.f15010i.setEnabled(true);
                    this.G.e(false);
                    hashMap.put("background_act_switch_close", this.F);
                    Intent intent2 = getActivity().getIntent();
                    String stringExtra = intent2.getStringExtra("pkgName");
                    String stringExtra2 = intent2.getStringExtra("target");
                    HashMap<String, Integer> B1 = f6.f.B1(getContext());
                    if (B1.containsKey(stringExtra) && B1.get(stringExtra).intValue() == 2 && !SimplePowerMonitorUtils.j().contains(stringExtra) && stringExtra2 != null && stringExtra2.equals("notify")) {
                        LocalLog.l("PowerControlFragment", "Do not notify");
                        Intent intent3 = new Intent();
                        intent3.putExtra("pkgName", stringExtra);
                        intent3.setAction("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_F");
                        getContext().sendBroadcast(intent3, "oplus.permission.OPLUS_COMPONENT_SAFE");
                    }
                }
            } else if ("passive_start_switch".equals(key)) {
                this.G.f(booleanValue);
                if (booleanValue) {
                    hashMap.put("associate_switch_open", this.F);
                } else {
                    hashMap.put("associate_switch_close", this.F);
                }
            } else if ("initiative_start_switch".equals(key)) {
                if (booleanValue) {
                    if ((PowerUtils.d(this.f15021t.getApplicationContext(), this.F) & 8) != 0) {
                        p0();
                        return true;
                    }
                }
                this.G.d(booleanValue);
                if (booleanValue) {
                    hashMap.put("auto_start_switch_open", this.F);
                } else {
                    hashMap.put("auto_start_switch_close", this.F);
                }
            }
            this.E.i0(hashMap);
            return true;
        }
        LocalLog.b("PowerControlFragment", "onPreferenceChange: value is not Boolean.");
        return false;
    }
}
