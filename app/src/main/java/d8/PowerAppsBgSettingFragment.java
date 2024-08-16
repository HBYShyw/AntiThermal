package d8;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import androidx.preference.Preference;
import b6.LocalLog;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.fuelgaue.base.PowerCustomMarkPreference;
import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import f6.f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import u4.IRemoteGuardElfInterface;
import x5.UploadDataUtil;
import y5.AppFeature;
import z5.GuardElfDataManager;

/* compiled from: PowerAppsBgSettingFragment.java */
/* renamed from: d8.b, reason: use source file name */
/* loaded from: classes.dex */
public class PowerAppsBgSettingFragment extends BasePreferenceFragment implements Preference.c {

    /* renamed from: z, reason: collision with root package name */
    private static final List<String> f10790z = new ArrayList(Arrays.asList("jp.co.daj.consumer.ifilter", "jp.co.daj.consumer.ifilter.mb", "jp.co.daj.consumer.ifilter.shop"));

    /* renamed from: i, reason: collision with root package name */
    private String f10795i;

    /* renamed from: j, reason: collision with root package name */
    private String f10796j;

    /* renamed from: o, reason: collision with root package name */
    private IDeviceIdleController f10801o;

    /* renamed from: q, reason: collision with root package name */
    private PowerCustomMarkPreference f10803q;

    /* renamed from: r, reason: collision with root package name */
    private PowerCustomMarkPreference f10804r;

    /* renamed from: s, reason: collision with root package name */
    private PowerCustomMarkPreference f10805s;

    /* renamed from: v, reason: collision with root package name */
    private b f10808v;

    /* renamed from: x, reason: collision with root package name */
    private AppOpsManager f10810x;

    /* renamed from: e, reason: collision with root package name */
    private Activity f10791e = null;

    /* renamed from: f, reason: collision with root package name */
    private Context f10792f = null;

    /* renamed from: g, reason: collision with root package name */
    List<String> f10793g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    List<String> f10794h = new ArrayList();

    /* renamed from: k, reason: collision with root package name */
    private GuardElfDataManager f10797k = null;

    /* renamed from: l, reason: collision with root package name */
    private boolean f10798l = false;

    /* renamed from: m, reason: collision with root package name */
    private boolean f10799m = false;

    /* renamed from: n, reason: collision with root package name */
    private UploadDataUtil f10800n = null;

    /* renamed from: p, reason: collision with root package name */
    private IRemoteGuardElfInterface f10802p = null;

    /* renamed from: t, reason: collision with root package name */
    private List<String> f10806t = new ArrayList();

    /* renamed from: u, reason: collision with root package name */
    private List<String> f10807u = new ArrayList();

    /* renamed from: w, reason: collision with root package name */
    private Object f10809w = new Object();

    /* renamed from: y, reason: collision with root package name */
    ServiceConnection f10811y = new a();

    /* compiled from: PowerAppsBgSettingFragment.java */
    /* renamed from: d8.b$a */
    /* loaded from: classes.dex */
    class a implements ServiceConnection {
        a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLog.a("PowerAppsBgSetting", "RemoteGuardElf connected!");
            PowerAppsBgSettingFragment.this.f10802p = IRemoteGuardElfInterface.a.z(iBinder);
            synchronized (PowerAppsBgSettingFragment.this.f10809w) {
                PowerAppsBgSettingFragment.this.f10809w.notifyAll();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LocalLog.a("PowerAppsBgSetting", "RemoteGuardElf disconnected!");
            PowerAppsBgSettingFragment.this.f10802p = null;
            PowerAppsBgSettingFragment powerAppsBgSettingFragment = PowerAppsBgSettingFragment.this;
            f.b(powerAppsBgSettingFragment.f10811y, powerAppsBgSettingFragment.f10791e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerAppsBgSettingFragment.java */
    /* renamed from: d8.b$b */
    /* loaded from: classes.dex */
    public class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (1 == message.what) {
                int i10 = message.getData().getInt(ThermalWindowConfigInfo.ATTR_POLICY);
                LocalLog.a("PowerAppsBgSetting", "MSG_POLICY_CHANGE: pkg=" + PowerAppsBgSettingFragment.this.f10796j + ", policy=" + i10);
                if (PowerAppsBgSettingFragment.this.f10802p == null) {
                    LocalLog.a("PowerAppsBgSetting", "MSG_POLICY_CHANGE: RemoteGuardElf is null. wait...");
                    synchronized (PowerAppsBgSettingFragment.this.f10809w) {
                        PowerAppsBgSettingFragment powerAppsBgSettingFragment = PowerAppsBgSettingFragment.this;
                        f.b(powerAppsBgSettingFragment.f10811y, powerAppsBgSettingFragment.f10791e);
                        try {
                            PowerAppsBgSettingFragment.this.f10809w.wait(100L);
                        } catch (Exception e10) {
                            LocalLog.b("PowerAppsBgSetting", "MSG_POLICY_CHANGE: wait Exception " + e10);
                        }
                    }
                }
                try {
                    PowerAppsBgSettingFragment.this.f10802p.e(PowerAppsBgSettingFragment.this.f10796j, i10);
                } catch (Exception e11) {
                    LocalLog.b("PowerAppsBgSetting", "MSG_POLICY_CHANGE: Exception " + e11);
                }
            }
        }
    }

    private void i0() {
        try {
            IRemoteGuardElfInterface iRemoteGuardElfInterface = this.f10802p;
            if (iRemoteGuardElfInterface == null) {
                this.f10793g = this.f10797k.e("screenoff_user_not_restrict_third_app_thistime.xml");
                this.f10794h = this.f10797k.e("startinfo_user_not_restrict_thistime.xml");
                LocalLog.a("usage: getUserNotRestrictList", "mRemoteGuardElfService is null.");
            } else {
                this.f10793g = iRemoteGuardElfInterface.t("screenoff_user_not_restrict_third_app_thistime.xml");
                this.f10794h = this.f10802p.t("startinfo_user_not_restrict_thistime.xml");
            }
        } catch (RemoteException unused) {
            LocalLog.a("PowerAppsBgSetting", "getUserNotRestrictList: RemoteException");
        }
    }

    private void j0() {
        if (this.f10799m) {
            this.f10798l = k0(this.f10796j);
        }
        PowerCustomMarkPreference powerCustomMarkPreference = (PowerCustomMarkPreference) findPreference("mark_pref_allow_back_run");
        this.f10803q = powerCustomMarkPreference;
        powerCustomMarkPreference.setOnPreferenceChangeListener(this);
        PowerCustomMarkPreference powerCustomMarkPreference2 = (PowerCustomMarkPreference) findPreference("mark_pref_prohibit_back_run");
        this.f10804r = powerCustomMarkPreference2;
        powerCustomMarkPreference2.setOnPreferenceChangeListener(this);
        PowerCustomMarkPreference powerCustomMarkPreference3 = (PowerCustomMarkPreference) findPreference("mark_pref_intelligently_limit_back_run");
        this.f10805s = powerCustomMarkPreference3;
        powerCustomMarkPreference3.setOnPreferenceChangeListener(this);
        List<String> a10 = AppFeature.a();
        if ((a10 != null && a10.contains(this.f10796j)) || f10790z.contains(this.f10796j)) {
            LocalLog.a("PowerAppsBgSetting", "initView: govList pkg=" + this.f10796j);
            this.f10803q.setChecked(true);
            this.f10804r.setChecked(false);
            this.f10805s.setChecked(false);
            this.f10803q.setEnabled(false);
            this.f10804r.setEnabled(false);
            this.f10805s.setEnabled(false);
            return;
        }
        f.l(this.f10806t, this.f10807u, this.f10792f.getApplicationContext());
        if (this.f10806t.contains(this.f10796j)) {
            this.f10803q.setChecked(true);
            this.f10804r.setChecked(false);
            this.f10805s.setChecked(false);
            this.f10803q.setSelectable(false);
            this.f10804r.setSelectable(true);
            this.f10805s.setSelectable(true);
            return;
        }
        if (this.f10807u.contains(this.f10796j)) {
            this.f10803q.setChecked(false);
            this.f10804r.setChecked(true);
            this.f10805s.setChecked(false);
            this.f10803q.setSelectable(true);
            this.f10804r.setSelectable(false);
            this.f10805s.setSelectable(true);
            return;
        }
        this.f10803q.setChecked(false);
        this.f10804r.setChecked(false);
        this.f10805s.setChecked(true);
        this.f10803q.setSelectable(true);
        this.f10804r.setSelectable(true);
        this.f10805s.setSelectable(false);
    }

    private boolean k0(String str) {
        List<String> list;
        List<String> list2 = this.f10793g;
        return (list2 != null && list2.contains(str)) || ((list = this.f10794h) != null && list.contains(str));
    }

    private void l0(int i10) {
        Message obtainMessage = this.f10808v.obtainMessage();
        obtainMessage.what = 1;
        Bundle bundle = new Bundle();
        bundle.putInt(ThermalWindowConfigInfo.ATTR_POLICY, i10);
        obtainMessage.setData(bundle);
        this.f10808v.sendMessage(obtainMessage);
    }

    private void m0(String str) {
        try {
            IRemoteGuardElfInterface iRemoteGuardElfInterface = this.f10802p;
            if (iRemoteGuardElfInterface == null) {
                this.f10797k.i(str, "screenoff_user_not_restrict_third_app_thistime.xml");
                this.f10797k.i(str, "startinfo_user_not_restrict_thistime.xml");
                LocalLog.a("PowerAppsBgSetting", "removeRedDot: mService is null.");
            } else {
                iRemoteGuardElfInterface.q(str);
            }
        } catch (RemoteException unused) {
            LocalLog.b("remove red dot", "AIDL SERVICE CONN FAILED!");
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return this.f10795i;
    }

    public void n0(String str, int i10) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = this.f10791e.getPackageManager().getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.c("PowerAppsBgSetting", "Cannot find package: " + str, e10);
            applicationInfo = null;
        }
        if (applicationInfo == null) {
            return;
        }
        if (applicationInfo.targetSdkVersion < 26) {
            this.f10810x.setMode(63, applicationInfo.uid, str, i10);
        }
        this.f10810x.setMode(70, applicationInfo.uid, str, i10);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        LocalLog.a("PowerAppsBgSetting", "onAttach");
        super.onAttach(activity);
        this.f10791e = activity;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        HandlerThread handlerThread = new HandlerThread("policysetting");
        handlerThread.start();
        this.f10808v = new b(handlerThread.getLooper());
        this.f10795i = this.f10791e.getIntent().getStringExtra("title");
        this.f10796j = this.f10791e.getIntent().getStringExtra("pkgName");
        this.f10799m = this.f10791e.getIntent().getBooleanExtra("isFromSetting", false);
        this.f10798l = this.f10791e.getIntent().getBooleanExtra("isDotVisible", false);
        LocalLog.a("PowerAppsBgSetting", "onCreate: pkgName = " + this.f10796j + ", mIsRedDotVisible=" + this.f10798l);
        this.f10797k = GuardElfDataManager.d(this.f10791e);
        this.f10800n = UploadDataUtil.S0(this.f10791e);
        this.f10801o = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
        this.f10810x = (AppOpsManager) this.f10791e.getSystemService("appops");
        this.f10792f = getContext();
        synchronized (this.f10809w) {
            f.b(this.f10811y, this.f10791e);
            try {
                this.f10809w.wait(100L);
            } catch (Exception e10) {
                LocalLog.b("PowerAppsBgSetting", "mObjWaitRemoteGuard wait Exception " + e10);
            }
        }
        if (this.f10799m) {
            i0();
        }
        j0();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.pm_app_bg_setting);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("PowerAppsBgSetting", "onDetach");
        super.onDetach();
        this.f10791e.unbindService(this.f10811y);
        this.f10791e = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.f10799m && this.f10798l) {
            m0(this.f10796j);
        }
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            String key = preference.getKey();
            if (key == null) {
                LocalLog.a("PowerAppsBgSetting", "onPreferenceChange: key is null.");
                return false;
            }
            LocalLog.a("PowerAppsBgSetting", "onPreferenceChange: key=" + key + ", check=" + booleanValue);
            if ("mark_pref_allow_back_run".equals(key)) {
                if (booleanValue) {
                    this.f10803q.setSelectable(false);
                    this.f10804r.setChecked(false);
                    this.f10805s.setChecked(false);
                    this.f10804r.setSelectable(true);
                    this.f10805s.setSelectable(true);
                    if (!this.f10806t.contains(this.f10796j)) {
                        this.f10806t.add(this.f10796j);
                    }
                    if (this.f10807u.contains(this.f10796j)) {
                        this.f10807u.remove(this.f10796j);
                    }
                    if (!f.h1(this.f10801o, this.f10796j)) {
                        f.a(this.f10801o, this.f10796j, this.f10792f.getApplicationContext());
                    }
                    l0(1);
                    f.N1(this.f10806t, this.f10807u, this.f10792f.getApplicationContext());
                    n0(this.f10796j, 0);
                    this.f10800n.j0(this.f10796j, "allow_run");
                }
            } else if ("mark_pref_prohibit_back_run".equals(key)) {
                if (booleanValue) {
                    this.f10804r.setSelectable(false);
                    this.f10803q.setChecked(false);
                    this.f10805s.setChecked(false);
                    this.f10803q.setSelectable(true);
                    this.f10805s.setSelectable(true);
                    if (!this.f10807u.contains(this.f10796j)) {
                        this.f10807u.add(this.f10796j);
                    }
                    if (this.f10806t.contains(this.f10796j)) {
                        this.f10806t.remove(this.f10796j);
                    }
                    if (f.h1(this.f10801o, this.f10796j)) {
                        f.E1(this.f10801o, this.f10796j, this.f10792f.getApplicationContext());
                    }
                    l0(2);
                    f.N1(this.f10806t, this.f10807u, this.f10792f.getApplicationContext());
                    n0(this.f10796j, 1);
                    this.f10800n.j0(this.f10796j, "prohibit_run");
                }
            } else if (!"mark_pref_intelligently_limit_back_run".equals(key)) {
                LocalLog.a("PowerAppsBgSetting", "onPreferenceChange: unknown key " + key);
            } else if (booleanValue) {
                this.f10805s.setSelectable(false);
                this.f10803q.setChecked(false);
                this.f10804r.setChecked(false);
                this.f10803q.setSelectable(true);
                this.f10804r.setSelectable(true);
                if (this.f10806t.contains(this.f10796j)) {
                    this.f10806t.remove(this.f10796j);
                }
                if (this.f10807u.contains(this.f10796j)) {
                    this.f10807u.remove(this.f10796j);
                }
                f.M1(this.f10801o, this.f10796j, this.f10792f.getApplicationContext());
                l0(3);
                f.N1(this.f10806t, this.f10807u, this.f10792f.getApplicationContext());
                n0(this.f10796j, 0);
                this.f10800n.j0(this.f10796j, "intelli_limit_run");
            }
            return true;
        }
        LocalLog.a("PowerAppsBgSetting", "onPreferenceChange: value is not Boolean.");
        return false;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }
}
