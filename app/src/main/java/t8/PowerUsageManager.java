package t8;

import a7.PowerConsumeStats;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.ArrayMap;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.battery.R;
import com.oplus.multiuser.OplusMultiUserManager;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import u8.AODPowerIssue;
import u8.AONPowerIssue;
import u8.AbnormalAppsPowerIssue;
import u8.ApplicationsPowerIssue;
import u8.AssociateStartPowerIssue;
import u8.AutoRunPowerIssue;
import u8.BackgroudAlivePowerUseIssue;
import u8.BasicPowerIssue;
import u8.BrightnessPowerIssue;
import u8.CurvedDisplayPowerIssue;
import u8.DarkModePowerIssue;
import u8.EarthquakeWarningPowerIssue;
import u8.FiveGPowerIssue;
import u8.GPSPowerIssue;
import u8.HighPerformIssue;
import u8.HotSpotPowerIssue;
import u8.NotificationBrightScreenPowerIssue;
import u8.ScreenOffPowerIssue;
import u8.ScreenRefreshRatePowerIssue;
import v4.GuardElfContext;

/* compiled from: PowerUsageManager.java */
/* renamed from: t8.f, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerUsageManager {

    /* renamed from: q, reason: collision with root package name */
    private static PowerUsageManager f18678q;

    /* renamed from: f, reason: collision with root package name */
    private d f18684f;

    /* renamed from: h, reason: collision with root package name */
    private IBatteryStats f18686h;

    /* renamed from: o, reason: collision with root package name */
    private boolean f18693o;

    /* renamed from: a, reason: collision with root package name */
    private final ArrayList<e> f18679a = new ArrayList<>();

    /* renamed from: b, reason: collision with root package name */
    private final ArrayMap<c, Boolean> f18680b = new ArrayMap<>();

    /* renamed from: c, reason: collision with root package name */
    private final Object f18681c = new Object();

    /* renamed from: d, reason: collision with root package name */
    private ArrayMap<Integer, BasicPowerIssue> f18682d = new ArrayMap<>();

    /* renamed from: g, reason: collision with root package name */
    private BroadcastReceiver f18685g = null;

    /* renamed from: i, reason: collision with root package name */
    private boolean f18687i = false;

    /* renamed from: j, reason: collision with root package name */
    private long[] f18688j = new long[4];

    /* renamed from: k, reason: collision with root package name */
    private volatile int f18689k = 0;

    /* renamed from: l, reason: collision with root package name */
    private volatile int f18690l = 0;

    /* renamed from: m, reason: collision with root package name */
    private volatile int f18691m = 1;

    /* renamed from: n, reason: collision with root package name */
    private volatile int f18692n = 0;

    /* renamed from: p, reason: collision with root package name */
    private ArrayList<PowerConsumeStats.b> f18694p = new ArrayList<>();

    /* renamed from: e, reason: collision with root package name */
    private Context f18683e = GuardElfContext.e().c();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerUsageManager.java */
    /* renamed from: t8.f$a */
    /* loaded from: classes2.dex */
    public class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("level", 0);
            int intExtra2 = intent.getIntExtra("plugged", 0);
            int intExtra3 = intent.getIntExtra("status", 1);
            int intExtra4 = intent.getIntExtra("temperature", 0);
            if ((intExtra != PowerUsageManager.this.f18689k || intExtra2 != PowerUsageManager.this.f18690l || intExtra3 != PowerUsageManager.this.f18691m || intExtra4 != PowerUsageManager.this.f18692n) && LocalLog.f()) {
                LocalLog.a("PowerUsageManager", "got battery level from update = " + intExtra + ", type = " + intExtra2 + ", status = " + intExtra3 + ", temperature = " + intExtra4);
            }
            PowerUsageManager.this.f18689k = intExtra;
            PowerUsageManager.this.f18690l = intExtra2;
            PowerUsageManager.this.f18691m = intExtra3;
            PowerUsageManager.this.f18692n = intExtra4;
            synchronized (PowerUsageManager.this.f18680b) {
                Iterator it = PowerUsageManager.this.f18680b.keySet().iterator();
                while (it.hasNext()) {
                    ((c) it.next()).a(intent);
                }
            }
        }
    }

    /* compiled from: PowerUsageManager.java */
    /* renamed from: t8.f$b */
    /* loaded from: classes2.dex */
    class b implements Comparator<BasicPowerIssue> {
        b() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(BasicPowerIssue basicPowerIssue, BasicPowerIssue basicPowerIssue2) {
            return (int) (basicPowerIssue2.f() - basicPowerIssue.f());
        }
    }

    /* compiled from: PowerUsageManager.java */
    /* renamed from: t8.f$c */
    /* loaded from: classes2.dex */
    public interface c {
        void a(Intent intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerUsageManager.java */
    /* renamed from: t8.f$d */
    /* loaded from: classes2.dex */
    public class d extends AsyncTask<Void, Void, Void> {
        private d() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x010b, code lost:
        
            if ((android.os.SystemClock.elapsedRealtime() - r12.f18913c) >= 300000) goto L40;
         */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Void doInBackground(Void... voidArr) {
            double d10;
            ArrayList arrayList;
            ArrayList arrayList2;
            double d11;
            boolean z10;
            int i10;
            int i11 = 0;
            while (PowerUsageManager.this.f18689k <= 0 && (i10 = i11 + 1) < 3) {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e10) {
                    e10.printStackTrace();
                }
                i11 = i10;
            }
            int i12 = PowerUsageManager.this.f18689k;
            double n10 = f6.f.n(PowerUsageManager.this.f18683e);
            LocalLog.a("PowerUsageManager", "CheckAsyncTask level : " + i12 + ", capacity : " + n10);
            if (i12 == 0 || n10 == UserProfileInfo.Constant.NA_LAT_LON) {
                return null;
            }
            double d12 = (n10 / 100.0d) * i12;
            long h10 = BatteryRemainTimeCalculator.e(PowerUsageManager.this.f18683e).h(i12, -1);
            LocalLog.a("PowerUsageManager", "CheckAsyncTask  phoneRemainPower = " + d12);
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) PowerUsageManager.this.f18683e.getSystemService("activity")).getRunningAppProcesses();
            int myUserId = UserHandle.myUserId();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                int userId = UserHandle.getUserId(runningAppProcessInfo.uid);
                if (userId == UserHandle.myUserId() || (userId == 999 && myUserId == 0)) {
                    if (runningAppProcessInfo.processState <= 2) {
                        arrayList3.add(runningAppProcessInfo);
                    } else {
                        arrayList4.add(runningAppProcessInfo);
                    }
                }
            }
            ArrayList arrayList5 = new ArrayList();
            synchronized (PowerUsageManager.this.f18681c) {
                int i13 = 0;
                while (i13 < PowerUsageManager.this.f18682d.size()) {
                    BasicPowerIssue basicPowerIssue = (BasicPowerIssue) PowerUsageManager.this.f18682d.valueAt(i13);
                    if (basicPowerIssue.f18913c > 0) {
                        d11 = d12;
                    } else {
                        d11 = d12;
                        z10 = System.currentTimeMillis() - basicPowerIssue.f18912b >= 300000;
                    }
                    if (z10) {
                        LocalLog.a("PowerUsageManager", "addIssue " + basicPowerIssue.e());
                        arrayList5.add(basicPowerIssue);
                    }
                    i13++;
                    d12 = d11;
                }
                d10 = d12;
            }
            Iterator it = arrayList5.iterator();
            while (it.hasNext()) {
                BasicPowerIssue basicPowerIssue2 = (BasicPowerIssue) it.next();
                int g6 = basicPowerIssue2.g();
                if (basicPowerIssue2.q()) {
                    arrayList = arrayList3;
                    arrayList2 = arrayList4;
                    basicPowerIssue2.i(PowerUsageManager.this.f18686h, PowerUsageManager.this.A(), d10, h10);
                } else {
                    arrayList = arrayList3;
                    arrayList2 = arrayList4;
                    basicPowerIssue2.j(PowerUsageManager.this.f18686h, arrayList, arrayList2, d10, h10);
                }
                if (!PowerUsageManager.this.f18693o && basicPowerIssue2.g() != 0) {
                    basicPowerIssue2.l(g6);
                }
                arrayList3 = arrayList;
                arrayList4 = arrayList2;
            }
            synchronized (PowerUsageManager.this.f18679a) {
                Iterator it2 = PowerUsageManager.this.f18679a.iterator();
                while (it2.hasNext()) {
                    ((e) it2.next()).g(i12);
                }
            }
            return null;
        }

        /* synthetic */ d(PowerUsageManager powerUsageManager, a aVar) {
            this();
        }
    }

    /* compiled from: PowerUsageManager.java */
    /* renamed from: t8.f$e */
    /* loaded from: classes2.dex */
    public interface e {
        void d();

        void g(int i10);
    }

    private PowerUsageManager() {
        int i10 = 0;
        boolean z10 = ActivityManager.getCurrentUser() == 0;
        boolean z11 = !OplusMultiUserManager.getInstance().isMultiSystemUserId(ActivityManager.getCurrentUser());
        boolean z12 = z10;
        ApplicationsPowerIssue applicationsPowerIssue = new ApplicationsPowerIssue(this.f18683e, PowerIssueKey.APP.b(), R.string.one_key_application_issue_title_new, z12, z11);
        GPSPowerIssue gPSPowerIssue = new GPSPowerIssue(this.f18683e, PowerIssueKey.GPS.b(), R.string.one_key_location_issue, z12, z11);
        AutoRunPowerIssue autoRunPowerIssue = new AutoRunPowerIssue(this.f18683e, PowerIssueKey.AUTO_RUN.b(), R.string.one_key_autorun_issue_title, z12, z11);
        AssociateStartPowerIssue associateStartPowerIssue = new AssociateStartPowerIssue(this.f18683e, PowerIssueKey.ASSOCIATE.b(), R.string.one_key_associate_issue_title, z12, z11);
        BackgroudAlivePowerUseIssue backgroudAlivePowerUseIssue = new BackgroudAlivePowerUseIssue(this.f18683e, PowerIssueKey.BACKGROUND.b(), R.string.one_key_background_issue_title, z12, z11);
        HighPerformIssue highPerformIssue = new HighPerformIssue(this.f18683e, PowerIssueKey.HIGH_PERFORM.b(), R.string.one_key_high_perform_title_new, z12, z11);
        AONPowerIssue aONPowerIssue = new AONPowerIssue(this.f18683e, PowerIssueKey.AON.b(), R.string.one_key_aon_issue_title, z12, z11);
        FiveGPowerIssue fiveGPowerIssue = new FiveGPowerIssue(this.f18683e, PowerIssueKey.FIVE_G.b(), R.string.one_key_5g_issue_title, z12, z11);
        BrightnessPowerIssue brightnessPowerIssue = new BrightnessPowerIssue(this.f18683e, PowerIssueKey.BRIGHTNESS.b(), R.string.one_key_brightness_issue_title, z12, z11);
        HotSpotPowerIssue hotSpotPowerIssue = new HotSpotPowerIssue(this.f18683e, PowerIssueKey.HOTSPOT.b(), R.string.one_key_hotspot_issue_title, z12, z11);
        ScreenRefreshRatePowerIssue screenRefreshRatePowerIssue = new ScreenRefreshRatePowerIssue(this.f18683e, PowerIssueKey.SCREENREFRESHRATE.b(), R.string.one_key_screenrefreshrate_issue_title, z12, z11);
        AODPowerIssue aODPowerIssue = new AODPowerIssue(this.f18683e, PowerIssueKey.AOD.b(), R.string.one_key_aod_issue_title, z12, z11);
        ScreenOffPowerIssue screenOffPowerIssue = new ScreenOffPowerIssue(this.f18683e, PowerIssueKey.SCREEN_OFF.b(), R.string.one_key_screen_off_issue_title_new, z12, z11);
        CurvedDisplayPowerIssue curvedDisplayPowerIssue = new CurvedDisplayPowerIssue(this.f18683e, PowerIssueKey.CURVEDDISPLAY.b(), R.string.one_key_curveddisplay_off_issue_title_new, z12, z11);
        DarkModePowerIssue darkModePowerIssue = new DarkModePowerIssue(this.f18683e, PowerIssueKey.DARK_MODE.b(), R.string.one_key_dark_mode_issue_title, z12, z11);
        NotificationBrightScreenPowerIssue notificationBrightScreenPowerIssue = new NotificationBrightScreenPowerIssue(this.f18683e, PowerIssueKey.NOTIFICATIONBRIGHTSCREEN.b(), R.string.one_key_notification_issue_title, z12, z11);
        EarthquakeWarningPowerIssue earthquakeWarningPowerIssue = new EarthquakeWarningPowerIssue(this.f18683e, PowerIssueKey.EARTHQUAKEWARNING.b(), R.string.one_key_earthquakewarning_title, z12, z11);
        if (highPerformIssue.o() && !y5.b.A()) {
            highPerformIssue.k(1);
            this.f18682d.put(0, highPerformIssue);
            i10 = 1;
        }
        if (applicationsPowerIssue.o()) {
            applicationsPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), applicationsPowerIssue);
            i10++;
        }
        if (autoRunPowerIssue.o()) {
            autoRunPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), autoRunPowerIssue);
            i10++;
        }
        if (associateStartPowerIssue.o()) {
            associateStartPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), associateStartPowerIssue);
            i10++;
        }
        if (backgroudAlivePowerUseIssue.o()) {
            backgroudAlivePowerUseIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), backgroudAlivePowerUseIssue);
            i10++;
        }
        if (gPSPowerIssue.o()) {
            gPSPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), gPSPowerIssue);
            i10++;
        }
        if (aONPowerIssue.o()) {
            aONPowerIssue.k(2);
            this.f18682d.put(Integer.valueOf(i10), aONPowerIssue);
            i10++;
        }
        if (fiveGPowerIssue.o()) {
            fiveGPowerIssue.k(2);
            this.f18682d.put(Integer.valueOf(i10), fiveGPowerIssue);
            i10++;
        }
        if (screenRefreshRatePowerIssue.o()) {
            screenRefreshRatePowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), screenRefreshRatePowerIssue);
            i10++;
        }
        if (brightnessPowerIssue.o()) {
            brightnessPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), brightnessPowerIssue);
            i10++;
        }
        if (screenOffPowerIssue.o()) {
            screenOffPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), screenOffPowerIssue);
            i10++;
        }
        if (hotSpotPowerIssue.o()) {
            hotSpotPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), hotSpotPowerIssue);
            i10++;
        }
        if (aODPowerIssue.o()) {
            aODPowerIssue.k(2);
            this.f18682d.put(Integer.valueOf(i10), aODPowerIssue);
            i10++;
        }
        if (curvedDisplayPowerIssue.o()) {
            curvedDisplayPowerIssue.k(1);
            this.f18682d.put(Integer.valueOf(i10), curvedDisplayPowerIssue);
            i10++;
        }
        if (darkModePowerIssue.o()) {
            darkModePowerIssue.k(2);
            this.f18682d.put(Integer.valueOf(i10), darkModePowerIssue);
            i10++;
        }
        if (notificationBrightScreenPowerIssue.o()) {
            notificationBrightScreenPowerIssue.k(2);
            this.f18682d.put(Integer.valueOf(i10), notificationBrightScreenPowerIssue);
            i10++;
        }
        if (earthquakeWarningPowerIssue.o()) {
            earthquakeWarningPowerIssue.k(2);
            this.f18682d.put(Integer.valueOf(i10), earthquakeWarningPowerIssue);
        }
        this.f18686h = IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
        B();
    }

    private void B() {
        if (this.f18685g != null || this.f18687i) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        a aVar = new a();
        this.f18685g = aVar;
        this.f18687i = true;
        Intent registerReceiver = this.f18683e.registerReceiver(aVar, intentFilter, 2);
        if (registerReceiver != null) {
            this.f18689k = registerReceiver.getIntExtra("level", 0);
            this.f18690l = registerReceiver.getIntExtra("plugged", 0);
            this.f18691m = registerReceiver.getIntExtra("status", 1);
            this.f18692n = registerReceiver.getIntExtra("temperature", 0);
            if (LocalLog.f()) {
                LocalLog.a("PowerUsageManager", "got mBatLevel from register level = " + this.f18689k + ",type = " + this.f18690l + ",status = " + this.f18691m + ", temperature = " + this.f18692n);
            }
            synchronized (this.f18680b) {
                for (c cVar : this.f18680b.keySet()) {
                    Boolean bool = this.f18680b.get(cVar);
                    if (bool != null && !bool.booleanValue()) {
                        cVar.a(registerReceiver);
                    }
                }
            }
        }
    }

    public static synchronized PowerUsageManager x(Context context) {
        PowerUsageManager powerUsageManager;
        synchronized (PowerUsageManager.class) {
            if (f18678q == null) {
                f18678q = new PowerUsageManager();
            }
            powerUsageManager = f18678q;
        }
        return powerUsageManager;
    }

    ArrayList<PowerConsumeStats.b> A() {
        synchronized (this.f18681c) {
            BasicPowerIssue basicPowerIssue = this.f18682d.get(0);
            if (!"abnormalapp".equals(basicPowerIssue.e())) {
                return null;
            }
            return ((AbnormalAppsPowerIssue) basicPowerIssue).r();
        }
    }

    public void C(c cVar) {
        int size;
        synchronized (this.f18680b) {
            this.f18680b.remove(cVar);
            size = this.f18680b.size();
        }
        if (size == 0) {
            try {
                BroadcastReceiver broadcastReceiver = this.f18685g;
                if (broadcastReceiver == null || !this.f18687i) {
                    return;
                }
                this.f18687i = false;
                this.f18683e.unregisterReceiver(broadcastReceiver);
                this.f18685g = null;
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }
    }

    public void D(e eVar) {
        synchronized (this.f18679a) {
            this.f18679a.remove(eVar);
        }
    }

    public boolean E() {
        F(true);
        return true;
    }

    public boolean F(boolean z10) {
        this.f18693o = z10;
        synchronized (this.f18681c) {
            d dVar = this.f18684f;
            a aVar = null;
            if (dVar != null) {
                dVar.cancel(true);
                this.f18684f = null;
            }
            d dVar2 = new d(this, aVar);
            this.f18684f = dVar2;
            dVar2.execute(new Void[0]);
        }
        synchronized (this.f18679a) {
            Iterator<e> it = this.f18679a.iterator();
            while (it.hasNext()) {
                it.next().d();
            }
        }
        return true;
    }

    public void p(c cVar, boolean z10) {
        synchronized (this.f18680b) {
            if (this.f18680b.containsKey(cVar)) {
                return;
            }
            this.f18680b.put(cVar, Boolean.valueOf(z10));
            B();
        }
    }

    public void q(e eVar) {
        synchronized (this.f18679a) {
            if (!this.f18679a.contains(eVar)) {
                this.f18679a.add(eVar);
            }
        }
    }

    public int r() {
        return this.f18689k;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0047, code lost:
    
        if ((android.os.SystemClock.elapsedRealtime() - r16.f18682d.valueAt(r10).f18913c) < 300000) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long s(int i10, int i11) {
        boolean z10;
        long c10 = BatteryRemainTimeCalculator.e(this.f18683e).c(i10, i11);
        long j10 = 0;
        if (c10 != 0) {
            synchronized (this.f18681c) {
                int i12 = 0;
                while (i12 < this.f18682d.size()) {
                    if (this.f18682d.valueAt(i12).f18913c <= j10) {
                        if (System.currentTimeMillis() - this.f18682d.valueAt(i12).f18912b < 300000) {
                            z10 = true;
                        }
                        z10 = false;
                    }
                    if (z10 && this.f18682d.valueAt(i12).g() == 3) {
                        c10 += this.f18682d.valueAt(i12).f();
                    }
                    i12++;
                    j10 = 0;
                }
            }
        }
        if (i11 == 3) {
            Settings.System.putLongForUser(this.f18683e.getContentResolver(), "remain_time_on_super_powersave", c10, 0);
        }
        if (i11 == 1) {
            Settings.System.putLongForUser(this.f18683e.getContentResolver(), "remain_time_on_powersave", c10, UserHandle.myUserId());
        }
        return c10;
    }

    public int t() {
        return this.f18691m;
    }

    public int u() {
        return this.f18692n;
    }

    public ArrayMap<Integer, BasicPowerIssue> v() {
        ArrayMap<Integer, BasicPowerIssue> arrayMap;
        synchronized (this.f18681c) {
            arrayMap = this.f18682d;
        }
        return arrayMap;
    }

    public long w() {
        return s(this.f18689k, 0) - s(this.f18689k, 2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00a4, code lost:
    
        r17.add(r13);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int[] y(ArrayList<BasicPowerIssue> arrayList, ArrayList<BasicPowerIssue> arrayList2) {
        boolean z10 = arrayList != null;
        boolean z11 = arrayList2 != null;
        int[] iArr = {0, 0, 0};
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        synchronized (this.f18681c) {
            for (int i10 = 0; i10 < this.f18682d.size(); i10++) {
                BasicPowerIssue basicPowerIssue = this.f18682d.get(Integer.valueOf(i10));
                if (basicPowerIssue.n()) {
                    LocalLog.a("PowerUsageManager", "getIssues : " + basicPowerIssue.e() + ", " + basicPowerIssue.g());
                    if (basicPowerIssue.g() != 0 && basicPowerIssue.g() != 3) {
                        if (basicPowerIssue.d() == 1) {
                            arrayList3.add(basicPowerIssue);
                        } else if (basicPowerIssue.d() == 2) {
                            arrayList4.add(basicPowerIssue);
                        }
                    }
                } else {
                    LocalLog.a("PowerUsageManager", basicPowerIssue.e() + " should not show");
                }
            }
            arrayList3.sort(new b());
            if (z11) {
                if (arrayList3.size() != 0) {
                    arrayList2.addAll(arrayList3);
                    arrayList2.addAll(arrayList4);
                    iArr[0] = 1;
                    iArr[1] = arrayList3.size() + arrayList4.size();
                    iArr[2] = arrayList3.size() + arrayList4.size();
                } else if (arrayList4.size() != 0) {
                    arrayList2.addAll(arrayList4);
                    iArr[0] = 2;
                    iArr[1] = arrayList3.size() + arrayList4.size();
                    iArr[2] = arrayList3.size() + arrayList4.size();
                }
            }
        }
        return iArr;
    }

    public int z() {
        return this.f18690l;
    }
}
