package m8;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import b6.LocalLog;
import b9.BatteryPowerHelper;
import b9.PowerSipper;
import b9.PowerUtils;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUIPreference;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUIRecommendedPreference;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.google.android.material.appbar.COUIDividerAppBarLayout;
import com.oplus.battery.R;
import com.oplus.performance.GTModeBroadcastReceiver;
import com.oplus.powermanager.fuelgaue.BatteryHealthActivity;
import com.oplus.powermanager.fuelgaue.IntellPowerSaveScence;
import com.oplus.powermanager.fuelgaue.PowerControlActivity;
import com.oplus.powermanager.fuelgaue.PowerInspectActivity;
import com.oplus.powermanager.fuelgaue.PowerRankingActivity;
import com.oplus.powermanager.fuelgaue.PowerSaveActivity;
import com.oplus.powermanager.fuelgaue.SmartChargeActivity;
import com.oplus.powermanager.fuelgaue.WirelessChargingSettingsActivity;
import com.oplus.powermanager.fuelgaue.WirelessReverseChargingActivity;
import com.oplus.powermanager.fuelgaue.WirelessReverseChargingReminderActivity;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.fuelgaue.base.HighlightPreferenceGroupAdapter;
import com.oplus.powermanager.fuelgaue.basic.customized.BatteryLevelViewPreference;
import com.oplus.powermanager.fuelgaue.basic.customized.BatteryUseTimePreference;
import com.oplus.powermanager.fuelgaue.basic.customized.PowerRankingPreference;
import com.oplus.powermanager.powercurve.graph.PopupTitlePreference;
import com.oplus.powermanager.powercurve.graph.PowerCurvePreference;
import com.oplus.powermanager.powercurve.graph.PowerSipperPreference;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import k8.BatteryPagePresenter;
import l8.IBatteryPageUpdate;
import l8.IPowerRankingUpdate;
import r8.BatterySipperUtils;
import r8.MultiUserBean;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: PowerConsumptionFragment.java */
/* renamed from: m8.e, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerConsumptionFragment extends BasePreferenceFragment implements Preference.c, Preference.d, IBatteryPageUpdate, IPowerRankingUpdate {
    private COUIRecommendedPreference A;
    private View F;
    private BatteryPagePresenter G;
    private boolean H;
    private boolean I;
    private boolean J;
    private boolean K;
    private SharedPreferences S;
    private SharedPreferences.Editor T;

    /* renamed from: f, reason: collision with root package name */
    private PowerRankingPreference f14968f;

    /* renamed from: s, reason: collision with root package name */
    private COUIJumpPreference f14981s;

    /* renamed from: t, reason: collision with root package name */
    private COUIJumpPreference f14982t;

    /* renamed from: y, reason: collision with root package name */
    private BatteryLevelViewPreference f14987y;

    /* renamed from: z, reason: collision with root package name */
    private BatteryUseTimePreference f14988z;

    /* renamed from: e, reason: collision with root package name */
    private COUIJumpPreference f14967e = null;

    /* renamed from: g, reason: collision with root package name */
    private COUIJumpPreference f14969g = null;

    /* renamed from: h, reason: collision with root package name */
    private COUIJumpPreference f14970h = null;

    /* renamed from: i, reason: collision with root package name */
    private COUIJumpPreference f14971i = null;

    /* renamed from: j, reason: collision with root package name */
    private COUISwitchPreference f14972j = null;

    /* renamed from: k, reason: collision with root package name */
    private COUIPreferenceCategory f14973k = null;

    /* renamed from: l, reason: collision with root package name */
    private COUIJumpPreference f14974l = null;

    /* renamed from: m, reason: collision with root package name */
    private COUIPreferenceCategory f14975m = null;

    /* renamed from: n, reason: collision with root package name */
    private COUIJumpPreference f14976n = null;

    /* renamed from: o, reason: collision with root package name */
    private COUIJumpPreference f14977o = null;

    /* renamed from: p, reason: collision with root package name */
    private COUISwitchPreference f14978p = null;

    /* renamed from: q, reason: collision with root package name */
    private COUISwitchPreference f14979q = null;

    /* renamed from: r, reason: collision with root package name */
    private COUISwitchPreference f14980r = null;

    /* renamed from: u, reason: collision with root package name */
    private PowerCurvePreference f14983u = null;

    /* renamed from: v, reason: collision with root package name */
    private PopupTitlePreference f14984v = null;

    /* renamed from: w, reason: collision with root package name */
    private COUIPreference f14985w = null;

    /* renamed from: x, reason: collision with root package name */
    private COUIPreference f14986x = null;
    private UploadDataUtil B = null;
    private Activity C = null;
    private Context D = null;
    private Context E = null;
    private boolean L = false;
    private boolean M = false;
    private long N = 0;
    private long O = 0;
    private HandlerThread P = new HandlerThread("OneKeyPref");
    private Handler Q = null;
    private boolean R = false;

    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$a */
    /* loaded from: classes2.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Intent f14989e;

        a(Intent intent) {
            this.f14989e = intent;
        }

        @Override // java.lang.Runnable
        public void run() {
            PowerConsumptionFragment.this.G.e(this.f14989e);
        }
    }

    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$b */
    /* loaded from: classes2.dex */
    class b extends LinearLayoutManager {
        private final ArrayMap<Integer, Integer> I;

        b(Context context) {
            super(context);
            this.I = new ArrayMap<>();
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
        public void a1(RecyclerView.z zVar) {
            super.a1(zVar);
            int J = J();
            for (int i10 = 0; i10 < J; i10++) {
                this.I.put(Integer.valueOf(i10), Integer.valueOf(I(i10).getHeight()));
            }
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        protected int o2(RecyclerView.z zVar) {
            return 2000;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
        public int u(RecyclerView.z zVar) {
            if (J() == 0) {
                return 0;
            }
            int b22 = b2();
            View C = C(b22);
            int i10 = -((int) (C != null ? C.getY() : 0.0f));
            for (int i11 = 0; i11 < b22; i11++) {
                i10 += this.I.get(Integer.valueOf(i11)) == null ? 0 : this.I.get(Integer.valueOf(i11)).intValue();
            }
            return i10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$c */
    /* loaded from: classes2.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List f14991e;

        c(List list) {
            this.f14991e = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            PowerConsumptionFragment.this.f14968f.d(PowerConsumptionFragment.this.D, this.f14991e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$d */
    /* loaded from: classes2.dex */
    public class d extends Handler {
        d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 102) {
                return;
            }
            PowerConsumptionFragment.this.F0(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$e */
    /* loaded from: classes2.dex */
    public class e implements COUIRecommendedPreference.a {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Intent f14994a;

        e(Intent intent) {
            this.f14994a = intent;
        }

        @Override // com.coui.appcompat.preference.COUIRecommendedPreference.a
        public void a(View view) {
            PowerConsumptionFragment.this.D.startActivity(this.f14994a);
        }
    }

    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$f */
    /* loaded from: classes2.dex */
    class f implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f14996e;

        f(String str) {
            this.f14996e = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PowerConsumptionFragment.this.f14971i != null) {
                if (!f6.f.f0(PowerConsumptionFragment.this.E)) {
                    PowerConsumptionFragment.this.f14971i.setSummary(this.f14996e);
                } else {
                    PowerConsumptionFragment.this.f14971i.setSummary("");
                }
            }
        }
    }

    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$g */
    /* loaded from: classes2.dex */
    class g implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f14998e;

        g(boolean z10) {
            this.f14998e = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            PowerConsumptionFragment.this.f14981s.setAssignment(this.f14998e ? PowerConsumptionFragment.this.D.getResources().getString(R.string.battery_detail_view_power_save_open) : PowerConsumptionFragment.this.D.getResources().getString(R.string.battery_detail_view_power_save_closed));
        }
    }

    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$h */
    /* loaded from: classes2.dex */
    class h implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f15000e;

        h(int i10) {
            this.f15000e = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            LocalLog.a("PowerConsumptionFragment", "updatePowerIssueText " + this.f15000e);
            if (this.f15000e > 0) {
                if (PowerConsumptionFragment.this.R) {
                    PowerConsumptionFragment.this.Q.sendEmptyMessageDelayed(101, 700L);
                    PowerConsumptionFragment.this.R = false;
                } else {
                    PowerConsumptionFragment.this.Q.sendEmptyMessage(101);
                }
            }
        }
    }

    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$i */
    /* loaded from: classes2.dex */
    class i implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f15002e;

        i(boolean z10) {
            this.f15002e = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            LocalLog.a("PowerConsumptionFragment", "flag = " + this.f15002e + " user=" + ActivityManager.getCurrentUser());
            if (ActivityManager.getCurrentUser() != 0) {
                return;
            }
            PowerConsumptionFragment.this.f14988z.e(this.f15002e);
            PowerConsumptionFragment.this.f14988z.setVisible(this.f15002e);
        }
    }

    /* compiled from: PowerConsumptionFragment.java */
    /* renamed from: m8.e$j */
    /* loaded from: classes2.dex */
    class j implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f15004e;

        j(boolean z10) {
            this.f15004e = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f15004e) {
                PowerConsumptionFragment.this.f14978p.setChecked(true);
            } else {
                PowerConsumptionFragment.this.f14978p.setChecked(false);
            }
        }
    }

    private void A0() {
        String string;
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("smart_charge_preference");
        this.f14970h = cOUIJumpPreference;
        if (this.J) {
            boolean J0 = f6.f.J0(this.E);
            if (J0) {
                string = this.D.getResources().getString(R.string.smart_charge_mode_on);
            } else {
                string = this.D.getResources().getString(R.string.smart_charge_mode_off);
            }
            this.f14970h.setAssignment(string);
            this.f14970h.setOnPreferenceClickListener(this);
            LocalLog.a("PowerConsumptionFragment", "initSmartChargeSwitch: isSmartChargeOn=" + J0);
            return;
        }
        cOUIJumpPreference.setVisible(false);
    }

    private void B0() {
        if (AppFeature.F() && this.D.getUserId() == 0) {
            COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) findPreference("speed_charge_switch_pref");
            this.f14978p = cOUISwitchPreference;
            if (cOUISwitchPreference != null) {
                boolean V0 = f6.f.V0(this.D);
                this.f14978p.setVisible(true);
                this.f14978p.setChecked(V0);
                this.f14978p.setOnPreferenceChangeListener(this);
                return;
            }
            LocalLog.b("PowerConsumptionFragment", "SpeedChargeSwitchPreference = null !");
            return;
        }
        LocalLog.a("PowerConsumptionFragment", "not system user or not support");
    }

    private void C0() {
        this.K = y5.b.D();
        this.L = f6.f.i1();
        this.H = y5.b.x();
        this.I = AppFeature.d();
        this.J = AppFeature.p() && this.D.getUserId() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void D0(ArrayList arrayList, Integer num, MultiUserBean multiUserBean) {
        Log.d("PowerConsumptionFragment", "multiUserMap: K:" + num + " power:" + multiUserBean.a() + " time" + multiUserBean.b());
        arrayList.add(new PowerSipper("USER", num.toString(), multiUserBean.b(), multiUserBean.a(), num.intValue()));
    }

    private void E0() {
        if (f6.f.a1(this.E)) {
            COUIJumpPreference cOUIJumpPreference = this.f14971i;
            if (cOUIJumpPreference != null) {
                cOUIJumpPreference.setEnabled(false);
            }
        } else {
            COUIJumpPreference cOUIJumpPreference2 = this.f14971i;
            if (cOUIJumpPreference2 != null) {
                cOUIJumpPreference2.setEnabled(true);
            }
        }
        if (y5.b.E() && GTModeBroadcastReceiver.j(this.D)) {
            if (Settings.System.getIntForUser(this.D.getContentResolver(), "gt_mode_state_setting", 0, 0) == 1) {
                this.f14977o.setSummary(this.D.getString(R.string.rm_gt_mode_notify_title));
            } else {
                this.f14977o.setSummary(this.D.getString(R.string.rm_gt_mode_toast_content));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F0(Message message) {
        long j10 = message.getData().getLong("startTime", 0L);
        long j11 = message.getData().getLong("endTime", 0L);
        boolean z10 = message.getData().getBoolean("isShowScreen", false);
        long j12 = j10 > j11 ? j11 : j10;
        this.N = j12;
        this.O = j11;
        G0(j12, j11, z10);
    }

    private void G0(long j10, long j11, boolean z10) {
        Pair<Long, Long> b10;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        Log.d("PowerConsumptionFragment", "mSipperListStartTime:" + this.N + "mSipperListEndTime:" + this.O);
        long j12 = 0;
        if ((this.N == 0 || this.O == 0) && (b10 = BatteryPowerHelper.b(this.D)) != null) {
            this.N = ((Long) b10.first).longValue();
            this.O = ((Long) b10.second).longValue();
        }
        Context context = this.D;
        long j13 = this.N;
        BatteryPowerHelper.c(context, arrayList, j13 - 60000, j13 + 1200000, false);
        Context context2 = this.D;
        long j14 = this.O;
        BatteryPowerHelper.c(context2, arrayList2, j14 - 60000, j14 + 1200000, false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.N);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(this.O);
        boolean z11 = false;
        while (calendar.getTimeInMillis() < calendar2.getTimeInMillis()) {
            if (calendar.get(11) == 18 && calendar.get(12) == 0) {
                j12 = calendar.getTimeInMillis();
                z11 = true;
            }
            calendar.add(12, 30);
        }
        Log.d("PowerConsumptionFragment", "needAdd:" + z11);
        Log.d("PowerConsumptionFragment", "time:" + j12);
        if (z11) {
            long d10 = BatteryPowerHelper.d(false, j12);
            ArrayList arrayList4 = new ArrayList();
            ArrayList arrayList5 = new ArrayList();
            BatteryPowerHelper.c(this.D, arrayList5, d10 - 300000, d10 + 300000, false);
            BatteryPowerHelper.f(arrayList5, arrayList, arrayList4);
            Log.d("PowerConsumptionFragment", "listBeforeReset size:" + arrayList4.size());
            Log.d("PowerConsumptionFragment", "listReset size:" + arrayList5.size());
            Log.d("PowerConsumptionFragment", "listEnd size:" + arrayList2.size());
            BatteryPowerHelper.e(arrayList4, arrayList2, arrayList3);
        } else {
            BatteryPowerHelper.f(arrayList2, arrayList, arrayList3);
        }
        HashMap hashMap = new HashMap();
        Iterator it = arrayList3.iterator();
        while (it.hasNext()) {
            PowerSipper powerSipper = (PowerSipper) it.next();
            int userId = UserHandle.getUserId(powerSipper.f4616t);
            if (userId != 0 && userId != 999) {
                it.remove();
                MultiUserBean multiUserBean = (MultiUserBean) hashMap.get(Integer.valueOf(userId));
                if (multiUserBean == null) {
                    multiUserBean = new MultiUserBean(userId, powerSipper.f4613q, powerSipper.f4608l);
                } else {
                    multiUserBean.c(multiUserBean.a() + powerSipper.f4613q);
                    multiUserBean.d(Math.min(multiUserBean.b() + powerSipper.f4608l, this.O - this.N));
                }
                hashMap.put(Integer.valueOf(userId), multiUserBean);
            }
        }
        hashMap.forEach(new BiConsumer() { // from class: m8.d
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PowerConsumptionFragment.D0(arrayList3, (Integer) obj, (MultiUserBean) obj2);
            }
        });
        Log.d("PowerConsumptionFragment", "listResult size:" + arrayList3.size());
        if (this.M) {
            arrayList3.sort(PowerSipper.f4600z);
        } else {
            arrayList3.sort(PowerSipper.f4599y);
        }
        this.C.runOnUiThread(new c(BatterySipperUtils.c(this.D, arrayList3, this.M)));
        Log.d("PowerConsumptionFragment", "update PowerRankingPreference");
    }

    private void H0() {
        this.B.a(this.C.getIntent().getStringExtra("source"));
    }

    private void q0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("battery_health_preference");
        this.f14969g = cOUIJumpPreference;
        if (cOUIJumpPreference != null) {
            if (this.I) {
                cOUIJumpPreference.setVisible(true);
                this.f14969g.setOnPreferenceClickListener(this);
            } else {
                cOUIJumpPreference.setVisible(false);
            }
        }
    }

    private void r0() {
        COUIRecommendedPreference cOUIRecommendedPreference = (COUIRecommendedPreference) findPreference("battery_recommended_pref");
        this.A = cOUIRecommendedPreference;
        if (cOUIRecommendedPreference != null) {
            ArrayList arrayList = new ArrayList();
            Intent intent = new Intent("oplus.intent.settings.action.STATUS_BAR_ICON_MANAGER");
            intent.putExtra(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY, "display_power_percent");
            arrayList.add(new COUIRecommendedPreference.c(this.D.getString(R.string.status_bar_battery_level_display), new e(intent)));
            COUIRecommendedPreference cOUIRecommendedPreference2 = this.A;
            if (cOUIRecommendedPreference2 != null) {
                cOUIRecommendedPreference2.c(arrayList);
            }
        }
    }

    private void s0() {
        BatteryUseTimePreference batteryUseTimePreference = (BatteryUseTimePreference) findPreference("battery_use_time_preference");
        this.f14988z = batteryUseTimePreference;
        if (batteryUseTimePreference != null && ActivityManager.getCurrentUser() != 0) {
            this.f14988z.c(true);
            this.f14988z.setVisible(false);
        }
        boolean z10 = Settings.System.getIntForUser(this.D.getContentResolver(), "oplus_battery_settings_plugged_type", -1, 0) != -1;
        BatteryUseTimePreference batteryUseTimePreference2 = this.f14988z;
        if (batteryUseTimePreference2 != null) {
            batteryUseTimePreference2.d(this.D);
            if (!z10 && ActivityManager.getCurrentUser() == 0) {
                this.f14988z.setVisible(true);
            } else {
                this.f14988z.setVisible(false);
            }
        }
    }

    private void t0() {
        String string;
        if (this.f14979q == null) {
            this.f14979q = (COUISwitchPreference) findPreference("smart_charge_protection_switch_in_more");
        }
        if (this.f14980r == null) {
            this.f14980r = (COUISwitchPreference) findPreference("regular_charge_protection_switch_in_more");
        }
        COUISwitchPreference cOUISwitchPreference = this.f14979q;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setVisible(this.D.getUserId() == 0 && !this.I);
            this.f14979q.setOnPreferenceChangeListener(this);
            this.f14979q.setChecked(f6.f.I0(this.D) == 1);
        }
        COUISwitchPreference cOUISwitchPreference2 = this.f14980r;
        if (cOUISwitchPreference2 != null) {
            cOUISwitchPreference2.setVisible(this.D.getUserId() == 0 && AppFeature.D() && !this.I);
            this.f14980r.setOnPreferenceChangeListener(this);
            this.f14980r.setChecked(f6.f.n0(this.D) == 1);
        }
        if (this.H) {
            COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("wireless_reverse_charging_switch");
            this.f14981s = cOUIJumpPreference;
            cOUIJumpPreference.setVisible(true);
            if (f6.f.d1(this.E)) {
                string = this.D.getResources().getString(R.string.battery_detail_view_power_save_open);
            } else {
                string = this.D.getResources().getString(R.string.battery_detail_view_power_save_closed);
            }
            this.f14981s.setAssignment(string);
            this.f14981s.setOnPreferenceClickListener(this);
        } else {
            f6.f.E3("low_power_charging_pref", "1", this.E);
            f6.f.E3("silent_mode_type_state", "0", this.E);
            f6.f.E3("silent_mode_type_custom", "22:00-7:00", this.E);
        }
        if (y5.b.x() || AppFeature.v()) {
            this.f14982t = (COUIJumpPreference) findPreference("wireless_charging_settings_pref");
            if (!y5.b.I()) {
                this.f14982t.setVisible(false);
            } else {
                this.f14982t.setVisible(true);
                this.f14982t.setOnPreferenceClickListener(this);
            }
        }
    }

    private void u0() {
        PowerCurvePreference powerCurvePreference = (PowerCurvePreference) findPreference("curve");
        this.f14983u = powerCurvePreference;
        if (powerCurvePreference != null) {
            powerCurvePreference.e(this);
            this.f14983u.f(false);
        }
    }

    private void v0() {
        this.B = UploadDataUtil.S0(this.E);
        f6.f.y1(this.E);
    }

    private void w0() {
        this.f14977o = (COUIJumpPreference) findPreference("rm_gt_mode_switch_pref");
        if (y5.b.E()) {
            if (GTModeBroadcastReceiver.j(this.D)) {
                this.f14977o.setOnPreferenceClickListener(this);
                if (Settings.System.getIntForUser(this.D.getContentResolver(), "gt_mode_state_setting", 0, 0) == 1) {
                    this.f14977o.setSummary(this.D.getString(R.string.rm_gt_mode_notify_title));
                    return;
                } else {
                    this.f14977o.setSummary(this.D.getString(R.string.rm_gt_mode_toast_content));
                    return;
                }
            }
            this.f14977o.setVisible(false);
            return;
        }
        this.f14977o.setVisible(false);
    }

    private void x0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("intelligent_power_saving_scene_pref");
        if (cOUIJumpPreference != null) {
            cOUIJumpPreference.setOnPreferenceClickListener(this);
        }
    }

    private void y0() {
        this.P.start();
        this.Q = new d(this.P.getLooper());
        PowerRankingPreference powerRankingPreference = (PowerRankingPreference) findPreference("power_comsumption_ranking_pref");
        this.f14968f = powerRankingPreference;
        if (powerRankingPreference != null) {
            powerRankingPreference.c(this);
            this.f14968f.setKey("power_comsumption_ranking_pref");
            this.f14968f.setOnPreferenceClickListener(this);
            if (ActivityManager.getCurrentUser() != 0) {
                this.f14968f.setVisible(false);
                PowerCurvePreference powerCurvePreference = this.f14983u;
                if (powerCurvePreference != null) {
                    powerCurvePreference.setVisible(false);
                }
                COUIPreferenceCategory cOUIPreferenceCategory = (COUIPreferenceCategory) findPreference("power_ranking_category");
                if (cOUIPreferenceCategory != null) {
                    cOUIPreferenceCategory.setVisible(false);
                }
            }
        }
    }

    private void z0() {
        String string;
        if (!y5.b.C() && !f6.f.n1(this.D)) {
            this.f14971i = (COUIJumpPreference) findPreference("power_save_preference");
            boolean f02 = f6.f.f0(this.E);
            if (!f02) {
                this.f14971i.setSummary(this.G.b0(1));
            }
            if (f02) {
                string = this.D.getResources().getString(R.string.battery_detail_view_power_save_open);
            } else {
                string = this.D.getResources().getString(R.string.battery_detail_view_power_save_closed);
            }
            this.f14971i.setAssignment(string);
            this.f14971i.setOnPreferenceClickListener(this);
            LocalLog.a("PowerConsumptionFragment", "initPowerSaveSwitch: isPowerSaveOn=" + f02);
            return;
        }
        ((PreferenceGroup) findPreference("power_save_category")).n(findPreference("power_save_preference"));
        LocalLog.a("PowerConsumptionFragment", "initPowerSaveSwitch: FeaturePowerSaveDisabled is ture.");
    }

    @Override // l8.IPowerRankingUpdate
    public void B(long j10, long j11, boolean z10) {
        Bundle bundle = new Bundle();
        bundle.putLong("startTime", j10);
        bundle.putLong("endTime", j11);
        bundle.putBoolean("isShowScreen", z10);
        Message message = new Message();
        message.setData(bundle);
        message.what = 102;
        this.Q.sendMessage(message);
    }

    @Override // l8.IBatteryPageUpdate
    public void I(boolean z10) {
        this.C.runOnUiThread(new j(z10));
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        String key = preference.getKey();
        String str = (String) preference.getTitle();
        if ("power_comsumption_ranking_pref".equals(key)) {
            Intent intent = new Intent(this.D, (Class<?>) PowerRankingActivity.class);
            intent.setFlags(603979776);
            intent.putExtra("navigate_title_id", R.string.power_usage_details);
            this.D.startActivity(intent);
            return true;
        }
        if ("one_key_opt_preference".equals(key)) {
            UploadDataUtil uploadDataUtil = this.B;
            if (uploadDataUtil != null) {
                uploadDataUtil.g0();
            }
            Intent intent2 = new Intent(this.D, (Class<?>) PowerInspectActivity.class);
            intent2.setFlags(603979776);
            intent2.putExtra("navigate_title_id", R.string.power_usage_details);
            this.D.startActivity(intent2);
            return true;
        }
        if ("intelligent_power_saving_scene_pref".equals(key)) {
            Intent intent3 = new Intent(this.D, (Class<?>) IntellPowerSaveScence.class);
            intent3.setFlags(603979776);
            intent3.putExtra("navigate_title_id", R.string.power_usage_details);
            startActivity(intent3);
            return true;
        }
        if (key.equals("power_save_preference")) {
            Intent intent4 = new Intent(this.D, (Class<?>) PowerSaveActivity.class);
            intent4.setFlags(603979776);
            intent4.putExtra("navigate_title_id", R.string.power_usage_details);
            startActivity(intent4);
            return true;
        }
        if ("wireless_reverse_charging_switch".equals(key)) {
            Intent intent5 = new Intent(this.D, (Class<?>) WirelessReverseChargingActivity.class);
            intent5.setFlags(603979776);
            intent5.putExtra("navigate_title_id", R.string.power_usage_details);
            startActivity(intent5);
            return true;
        }
        if ("rm_gt_mode_switch_pref".equals(key)) {
            Intent intent6 = new Intent();
            intent6.setComponent(new ComponentName("com.oplus.gtmode", "com.oplus.gtmode.GtmodeActivity"));
            intent6.setFlags(603979776);
            startActivity(intent6);
            return true;
        }
        if ("wireless_charging_settings_pref".equals(key)) {
            Intent intent7 = new Intent(this.D, (Class<?>) WirelessChargingSettingsActivity.class);
            intent7.setFlags(603979776);
            intent7.putExtra("navigate_title_id", R.string.power_usage_details);
            startActivity(intent7);
            return true;
        }
        if (key.equals("battery_health_preference")) {
            Intent intent8 = new Intent(this.D, (Class<?>) BatteryHealthActivity.class);
            intent8.setFlags(603979776);
            intent8.putExtra("navigate_title_id", R.string.power_usage_details);
            startActivity(intent8);
            return true;
        }
        if (key.equals("smart_charge_preference")) {
            Intent intent9 = new Intent(this.D, (Class<?>) SmartChargeActivity.class);
            intent9.setFlags(603979776);
            intent9.putExtra("navigate_title_id", R.string.power_usage_details);
            startActivity(intent9);
            return true;
        }
        LocalLog.a("PowerConsumptionFragment", "onPreferenceTreeClick: key = " + key + ", title = " + str);
        return true;
    }

    @Override // l8.IPowerRankingUpdate
    public void O(long j10, long j11) {
        B(j10, j11, this.M);
        this.M = false;
    }

    @Override // l8.IPowerRankingUpdate
    public void Y(List<PowerSipper> list, boolean z10) {
    }

    @Override // l8.IBatteryPageUpdate
    public void Z() {
        this.M = false;
        B(this.N, this.O, false);
    }

    @Override // l8.IBatteryPageUpdate
    public void a0(boolean z10) {
        this.C.runOnUiThread(new g(z10));
    }

    @Override // l8.IBatteryPageUpdate, l8.IPowerRankingUpdate
    public void c(int i10, int i11) {
        this.C.runOnUiThread(new h(i10));
    }

    @Override // l8.IBatteryPageUpdate
    public void d(boolean z10, boolean z11) {
    }

    @Override // androidx.fragment.app.Fragment
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        int i10 = 0;
        for (String str2 : strArr) {
            sb2.append(str2);
            sb2.append(" ");
        }
        LocalLog.l("PowerConsumptionFragment", "Battery debug:" + ((Object) sb2));
        if ("setChargeWattageTest".equals(strArr[0])) {
            try {
            } catch (NumberFormatException unused) {
                printWriter.println("please input Integer");
            }
            if (strArr.length != 2) {
                printWriter.println("please input Integer, or 'exit'");
                return;
            }
            if (strArr[1].equals("exit")) {
                this.G.P(false);
                printWriter.println("Test is ended!");
                return;
            }
            i10 = Integer.parseInt(strArr[1]);
            Intent intent = new Intent("android.intent.action.ADDITIONAL_BATTERY_CHANGED");
            intent.putExtra("wattageTest", true);
            intent.putExtra("chargewattage", i10);
            if (i10 > 100) {
                intent.putExtra("chargertechnology", 20);
            } else if (i10 >= 33) {
                intent.putExtra("chargertechnology", 2);
            } else if (i10 >= 20) {
                intent.putExtra("chargertechnology", 1);
            }
            this.G.P(true);
            if (getActivity() != null) {
                getActivity().getMainThreadHandler().postDelayed(new a(intent), 200);
            }
            printWriter.println("cmd done.");
        }
    }

    @Override // l8.IBatteryPageUpdate
    public void e(boolean z10, boolean z11, boolean z12) {
        COUISwitchPreference cOUISwitchPreference = this.f14972j;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setEnabled(z10);
            this.f14972j.setVisible(z11);
            this.f14972j.setChecked(z12);
        }
    }

    @Override // l8.IBatteryPageUpdate
    public void g(boolean z10, boolean z11, boolean z12) {
        String string;
        COUIJumpPreference cOUIJumpPreference = this.f14971i;
        if (cOUIJumpPreference != null) {
            cOUIJumpPreference.setVisible(z11);
            if (z12) {
                string = this.D.getResources().getString(R.string.battery_detail_view_power_save_open);
            } else {
                string = this.D.getResources().getString(R.string.battery_detail_view_power_save_closed);
            }
            this.f14971i.setAssignment(string);
            if (z12) {
                this.f14971i.setSummary("");
            } else {
                this.f14971i.setSummary(this.G.b0(1));
            }
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.app_name);
    }

    @Override // l8.IBatteryPageUpdate
    public void j(boolean z10) {
        this.C.runOnUiThread(new i(z10));
    }

    @Override // l8.IBatteryPageUpdate
    public void n() {
        this.M = true;
        B(this.N, this.O, true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.C = getActivity();
        this.D = context;
        this.E = context.getApplicationContext();
        this.G = new BatteryPagePresenter(context, this);
        SharedPreferences sharedPreferences = context.getSharedPreferences("battery_ui", 0);
        this.S = sharedPreferences;
        this.T = sharedPreferences.edit();
        String stringExtra = this.C.getIntent().getStringExtra(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY);
        String stringExtra2 = this.C.getIntent().getStringExtra("reason");
        LocalLog.a("PowerConsumptionFragment", "key value = " + stringExtra + ", reason = " + stringExtra2);
        if (stringExtra != null) {
            this.R = true;
        }
        if ("battery_card".equals(stringExtra2)) {
            int i10 = this.S.getInt("card_jump", 0) + 1;
            this.T.putInt("card_jump", i10);
            this.T.apply();
            UploadDataUtil.S0(context).n(String.valueOf(i10));
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        LocalLog.a("PowerConsumptionFragment", "onCreate");
        this.G.a();
        super.onCreate(bundle);
        f6.f.i2(false);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public RecyclerView.p onCreateLayoutManager() {
        return new b(this.D);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        LocalLog.a("PowerConsumptionFragment", "onCreatePreferences");
        addPreferencesFromResource(R.xml.pm_consumption_summary);
        this.G.J();
        v0();
        C0();
        A0();
        z0();
        t0();
        x0();
        u0();
        y0();
        w0();
        this.f14987y = (BatteryLevelViewPreference) findPreference("battery_level_view_preference");
        s0();
        r0();
        q0();
        B0();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        LocalLog.a("PowerConsumptionFragment", "onCreateView");
        this.F = super.onCreateView(layoutInflater, viewGroup, bundle);
        Activity activity = this.C;
        if (activity != null) {
            ((COUIDividerAppBarLayout) activity.findViewById(R.id.consumption_appBarLayout)).bindRecyclerView(getListView());
        }
        return this.F;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        Handler handler = this.Q;
        if (handler != null && handler.hasMessages(101)) {
            this.Q.removeMessages(101);
        }
        LocalLog.a("PowerConsumptionFragment", "onDestroy");
        super.onDestroy();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("PowerConsumptionFragment", "onDetach");
        this.G.onDetach();
        super.onDetach();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        BatterySipperUtils.a();
        LocalLog.a("PowerConsumptionFragment", "onPause");
        super.onPause();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v7 */
    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        ?? booleanValue = preference instanceof COUISwitchPreference ? ((Boolean) obj).booleanValue() : 0;
        String key = preference.getKey();
        if (key == null) {
            return false;
        }
        LocalLog.a("PowerConsumptionFragment", "switch key =" + key + ", check=" + ((boolean) booleanValue));
        if (key.equals("power_save_sub_backlight")) {
            f6.f.B2(this.E, booleanValue);
            return true;
        }
        if (key.equals("power_save_sub_screenoff_time")) {
            f6.f.F2(this.E, booleanValue);
            return true;
        }
        if (key.equals("power_save_sub_sync")) {
            f6.f.G2(this.E, booleanValue);
            return true;
        }
        if (key.equals("high_performance_switch")) {
            this.G.D(booleanValue);
            return true;
        }
        if (key.equals("smart_charge_protection_switch_in_more")) {
            if (booleanValue != 0) {
                this.f14980r.setChecked(false);
                f6.f.J2(this.D, 0);
            }
            f6.f.d3(this.D, booleanValue);
            UploadDataUtil.S0(this.D).x0(Boolean.toString(booleanValue));
            return true;
        }
        if (key.equals("regular_charge_protection_switch_in_more")) {
            if (booleanValue != 0) {
                this.f14979q.setChecked(false);
                f6.f.d3(this.D, 0);
            }
            f6.f.J2(this.D, booleanValue);
            UploadDataUtil.S0(this.D).r0(Boolean.toString(booleanValue));
            return true;
        }
        if (key.equals("wireless_reverse_charging_switch")) {
            f6.f.y3(this.E, booleanValue);
            this.B.R0(booleanValue, "batt_mian_scence_click");
            if (booleanValue == 0) {
                return true;
            }
            Intent intent = new Intent(this.D, (Class<?>) WirelessReverseChargingReminderActivity.class);
            intent.addFlags(268435456);
            startActivity(intent);
            return true;
        }
        if (!key.equals("speed_charge_switch_pref")) {
            return true;
        }
        this.G.F(booleanValue);
        return true;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.c
    public boolean onPreferenceTreeClick(Preference preference) {
        Log.d("PowerConsumptionFragment", "onPreferenceTreeClick");
        if (preference instanceof PowerSipperPreference) {
            String str = (String) preference.getTitle();
            Intent intent = new Intent(this.D, (Class<?>) PowerControlActivity.class);
            PowerSipperPreference powerSipperPreference = (PowerSipperPreference) preference;
            intent.putExtras(PowerUtils.e(str, powerSipperPreference.c()));
            intent.putExtra("navigate_title_id", R.string.power_usage_details);
            intent.putExtra("drainType", powerSipperPreference.c().f4601e);
            intent.putExtra("title", str);
            intent.putExtra("pkgName", powerSipperPreference.c().f4603g);
            intent.putExtra("callingSource", 0);
            intent.setFlags(603979776);
            startActivity(intent);
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        LocalLog.a("PowerConsumptionFragment", "onResume");
        super.onResume();
        this.G.K();
        E0();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        LocalLog.a("PowerConsumptionFragment", "onStart");
        super.onStart();
        this.G.L();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        LocalLog.a("PowerConsumptionFragment", "onViewCreated");
        super.onViewCreated(view, bundle);
        H0();
        this.G.b();
    }

    @Override // l8.IBatteryPageUpdate
    public void w(String str, String str2) {
        this.C.runOnUiThread(new f(str));
    }

    @Override // l8.IBatteryPageUpdate
    public void x() {
        f6.f.V1(true);
        this.f14987y.n();
    }

    @Override // l8.IBatteryPageUpdate
    public void z(boolean z10, boolean z11, boolean z12) {
        String string;
        COUIJumpPreference cOUIJumpPreference = this.f14970h;
        if (cOUIJumpPreference != null) {
            cOUIJumpPreference.setVisible(z11);
            if (z12) {
                string = this.D.getResources().getString(R.string.smart_charge_mode_on);
            } else {
                string = this.D.getResources().getString(R.string.smart_charge_mode_off);
            }
            this.f14970h.setAssignment(string);
        }
    }
}
