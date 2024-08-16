package v4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;
import b6.LocalLog;
import b7.PowerModeUnion;
import b7.PowerSaveMode;
import com.oplus.battery.OplusBatteryApp;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.performance.GTModeBroadcastReceiver;
import com.oplus.simplepowermonitor.alarm.NetworkStatusReceiver;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.thermalcontrol.ThermalControlUtils;
import com.oplus.thermalcontrol.ThermalControllerCenter;
import d6.ConfigUpdateUtil;
import f6.CommonUtil;
import f6.StatusUtils;
import ha.StorageMonitor;
import ha.StorageMonitorService;
import ia.AppInfoUtils;
import m9.AlarmSetter;
import o9.AffairAudioAbnormal;
import o9.AffairScreenAbnormal;
import o9.AffairSubUserNotify;
import p6.AABResultObserver;
import p9.PowerMonitor;
import r9.SimplePowerMonitorUtils;
import v6.AffairDcsHelper;
import v6.AffairResControl;
import v6.AffairRusHelper;
import v6.AffairScreenOnAndOff;
import v6.ScreenoffStartInfoNotifyHelper;
import v6.TempHandleHelper;
import w4.Affair;
import x4.AppSwitchManager;
import x4.AppSwitchObserver;
import x5.UploadDataUtil;
import y6.PowerConsumeManager;

/* compiled from: OplusBatteryMonitor.java */
/* renamed from: v4.f, reason: use source file name */
/* loaded from: classes.dex */
public class OplusBatteryMonitor {

    /* renamed from: b, reason: collision with root package name */
    private Context f19088b;

    /* renamed from: e, reason: collision with root package name */
    private ConfigUpdateUtil f19091e;

    /* renamed from: f, reason: collision with root package name */
    private StatusUtils f19092f;

    /* renamed from: g, reason: collision with root package name */
    private PowerManager f19093g;

    /* renamed from: h, reason: collision with root package name */
    private PowerManager.WakeLock f19094h;

    /* renamed from: o, reason: collision with root package name */
    private Handler f19101o;

    /* renamed from: p, reason: collision with root package name */
    private AppSwitchManager f19102p;

    /* renamed from: s, reason: collision with root package name */
    private ContentObserver f19105s;

    /* renamed from: a, reason: collision with root package name */
    private boolean f19087a = OplusBatteryApp.f9757h;

    /* renamed from: d, reason: collision with root package name */
    private volatile boolean f19090d = false;

    /* renamed from: i, reason: collision with root package name */
    private g f19095i = null;

    /* renamed from: j, reason: collision with root package name */
    private f f19096j = null;

    /* renamed from: k, reason: collision with root package name */
    private h f19097k = null;

    /* renamed from: l, reason: collision with root package name */
    private d f19098l = null;

    /* renamed from: m, reason: collision with root package name */
    private e f19099m = null;

    /* renamed from: n, reason: collision with root package name */
    private NetworkStatusReceiver f19100n = null;

    /* renamed from: q, reason: collision with root package name */
    private AppSwitchObserver f19103q = new b();

    /* renamed from: r, reason: collision with root package name */
    private BroadcastReceiver f19104r = new GTModeBroadcastReceiver();

    /* renamed from: t, reason: collision with root package name */
    private BroadcastReceiver f19106t = new c();

    /* renamed from: c, reason: collision with root package name */
    private boolean f19089c = false;

    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            OplusBatteryMonitor oplusBatteryMonitor = OplusBatteryMonitor.this;
            oplusBatteryMonitor.m(oplusBatteryMonitor.f19101o);
            new QuickScreenStatusListener().z();
            OplusBatteryMonitor oplusBatteryMonitor2 = OplusBatteryMonitor.this;
            oplusBatteryMonitor2.n(oplusBatteryMonitor2.f19101o);
        }
    }

    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$b */
    /* loaded from: classes.dex */
    class b implements AppSwitchObserver {
        b() {
        }

        @Override // x4.AppSwitchObserver
        public void a(String str, String str2, String str3) {
            synchronized (OplusBatteryMonitor.this) {
                if (LocalLog.f()) {
                    LocalLog.a("APP_SWITCH", "ACTION_ROM_APP_CHANGE, prePkg=" + str + ", nextPkg=" + str2 + ", nextActivity=" + str3);
                }
                if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str3)) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("pre_app_pkgname", str);
                bundle.putString("next_app_pkgname", str2);
                bundle.putString("next_activity", str3);
                if (ThermalControlUtils.getInstance(OplusBatteryMonitor.this.f19088b).getUserForeground()) {
                    ThermalControlUtils.getInstance(OplusBatteryMonitor.this.f19088b).setLastForegroundPkg(str2);
                    ThermalControlUtils.getInstance(OplusBatteryMonitor.this.f19088b).setLastForegroundActivity(str3);
                }
                Affair.f().e(300, bundle);
            }
        }
    }

    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$c */
    /* loaded from: classes.dex */
    class c extends BroadcastReceiver {
        c() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (OplusBatteryMonitor.this.f19089c && !"android.intent.action.BOOT_COMPLETED".equals(action)) {
                LocalLog.a("OplusBatteryMonitor", "machine is shutting down, reject broadcast.");
                return;
            }
            if (!"android.intent.action.BATTERY_CHANGED".equals(action) && !"android.intent.action.TIME_TICK".equals(action) && LocalLog.f()) {
                LocalLog.a("OplusBatteryMonitor", " onReciver  action=" + action);
            }
            if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
                if (OplusBatteryMonitor.this.f19089c) {
                    return;
                }
                OplusBatteryMonitor.this.f19089c = true;
                Affair.f().d(EventType.SCENE_MODE_AUDIO_IN, intent);
                return;
            }
            if ("android.intent.action.SCREEN_ON".equals(action)) {
                LocalLog.l("OplusBatteryMonitor", " onReceiver  action=" + action);
                Affair.f().d(EventType.SCENE_MODE_AUDIO_OUT, intent);
                return;
            }
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                LocalLog.l("OplusBatteryMonitor", " onReceiver  action=" + action);
                Affair.f().d(EventType.SCENE_MODE_LOCATION, intent);
                return;
            }
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                Affair.f().d(EventType.SCENE_MODE_CAMERA, intent);
                Affair.f().h(EventType.SCENE_MODE_CAMERA, intent);
                return;
            }
            if ("android.intent.action.OPLUS_GUARD_ELF_MONITOR".equals(action)) {
                Affair.f().d(EventType.SCENE_MODE_HOLIDAY, intent);
                return;
            }
            if ("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS".equals(action)) {
                Affair.f().d(EventType.SCENE_MODE_VPN, intent);
                return;
            }
            if ("oplus.android.intent.action.FORBID_INSTALL".equals(action)) {
                Toast.makeText(OplusBatteryMonitor.this.f19088b, OplusBatteryMonitor.this.f19088b.getString(R.string.toast_forbid_install), 1).show();
                return;
            }
            if ("android.intent.action.OPLUS_APP_FROZEN_DCS_UPLOADE".equals(action)) {
                Affair.f().d(EventType.SCENE_MODE_AUDIO_CALL, intent);
                return;
            }
            if ("android.intent.action.OPLUS_GUARD_ELF_ANR_MONITOR".equals(action)) {
                Affair.f().d(EventType.SCENE_MODE_VIDEO_CALL, intent);
                return;
            }
            if ("android.intent.action.OPLUS_GUARD_TIME_INFO".equals(action)) {
                Affair.f().d(EventType.SCENE_MODE_FILE_DOWNLOAD, intent);
                return;
            }
            if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                if (OplusBatteryMonitor.this.f19089c) {
                    OplusBatteryMonitor.this.f19089c = false;
                }
                Affair.f().d(200, intent);
                return;
            }
            if (!"oplus.intent.action.OPLUS_STARTUP_APP_MONITOR".equals(action) && !"android.intent.action.OPLUS_ACTIVITY_CALL_MONITOR".equals(action) && !"android.intent.action.OPLUS_ACTIVITY_INTERCEPT_MONITOR".equals(action)) {
                if ("com.oplus.action.OPLUSBATTERY_NOTIFY_RESRITCTED_APP".equals(action)) {
                    Affair.f().d(EventType.SCENE_MODE_CONFERENCE, intent);
                    return;
                }
                if ("oplus.intent.action.CPU_MONITOR_NOTIFY".equals(action)) {
                    Affair.f().d(EventType.SCENE_MODE_BT_DEVICE, intent);
                    return;
                }
                if ("android.intent.action.DATE_CHANGED".equals(action)) {
                    Affair.f().d(EventType.SCENE_MODE_READING, intent);
                    return;
                }
                if ("android.intent.action.TIME_SET".equals(action)) {
                    UploadDataUtil.S0(OplusBatteryMonitor.this.f19088b).Y0();
                    Affair.f().d(230, intent);
                    return;
                }
                if ("android.intent.action.TIME_TICK".equals(action)) {
                    Affair.f().d(231, intent);
                    return;
                }
                if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                    Affair.f().d(1102, intent);
                    return;
                }
                if (!"android.intent.action.PACKAGE_ADDED".equals(action) && !"android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(action)) {
                    if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                        Affair.f().d(224, intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_HIGH_PERFORMANCE".equals(action)) {
                        Affair.f().d(DataTypeConstants.USER_ACTION, intent);
                        return;
                    }
                    if ("android.intent.action.FORCE_CLOSE_ALL_PROCESS".equals(action)) {
                        Affair.f().d(EventType.SCENE_MODE_RECORDING, intent);
                        return;
                    }
                    if ("oplus.intent.action.DEEP_SLEEP_ESPECIAL_TRAFFIC_REQ".equals(action)) {
                        Affair.f().d(223, intent);
                        return;
                    }
                    if ("android.intent.action.LOCALE_CHANGED".equals(action)) {
                        LocalLog.a("OplusBatteryMonitor", "LOCALE_CHANGED!");
                        Affair.f().d(EventType.SCENE_SHORT_VIDEO, intent);
                        StorageMonitorService.Y(OplusBatteryMonitor.this.f19088b).U(intent);
                        return;
                    }
                    if ("oplus.intent.action.BATTERY_PLUGGED_CHANGED".equals(action)) {
                        Affair.f().d(EventType.SCENE_MODE_VIDEO, intent);
                        return;
                    }
                    if ("oplus.intent.action.BATTERY_DATA_UPDATE".equals(action)) {
                        Affair.f().d(EventType.SCENE_MODE_DOWNLOAD, intent);
                        return;
                    }
                    if ("oplus.power.monitor.action.PUSH_BROADCAST_INTENT".equals(action)) {
                        Affair.f().d(EventType.SCENE_MODE_FILE_UPLOAD, intent);
                        return;
                    }
                    if ("oplus.power.monitor.action.BEAT_BROADCAST_INTENT".equals(action)) {
                        Affair.f().d(EventType.SCENE_MODE_MUSIC_PLAY, intent);
                        return;
                    }
                    if ("android.intent.action.USER_PRESENT".equals(action)) {
                        Affair.f().d(EventType.SCENE_MODE_LEARNING, intent);
                        return;
                    }
                    if ("android.intent.action.USER_SWITCHED".equals(action)) {
                        Affair.f().d(EventType.SCENE_MODE_NAVIGATION, intent);
                        return;
                    }
                    if ("android.intent.action.USER_BACKGROUND".equals(action)) {
                        Affair.f().d(225, intent);
                        StorageMonitorService.Y(OplusBatteryMonitor.this.f19088b).U(intent);
                        return;
                    }
                    if ("android.intent.action.USER_FOREGROUND".equals(action)) {
                        Affair.f().d(226, intent);
                        StorageMonitorService.Y(OplusBatteryMonitor.this.f19088b).U(intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_F".equals(action)) {
                        Affair.f().d(227, intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_NF".equals(action)) {
                        Affair.f().d(228, intent);
                        return;
                    }
                    if ("oplus.intent.action.high_power_consumption_notification.nomore".equals(action)) {
                        Affair.f().d(1403, intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_FIRST".equals(action)) {
                        Affair.f().d(1201, intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_SECOND".equals(action)) {
                        Affair.f().d(1202, intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_THIRD".equals(action)) {
                        Affair.f().d(1210, intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_THERMAL_CONTROL_DIS_WINDOW".equals(action)) {
                        Affair.f().d(1211, intent);
                        return;
                    }
                    if ("oplus.intent.action.OPLUS_CAMERA_OPTION".equals(action)) {
                        Affair.f().d(1203, intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_THERMAL_CONTROL_USER_SYSTEM".equals(action)) {
                        Affair.f().d(1204, intent);
                        return;
                    }
                    if ("android.intent.action.ADDITIONAL_BATTERY_CHANGED".equals(action)) {
                        Affair.f().d(229, intent);
                        return;
                    }
                    if ("oplus.intent.action.DEEPTHINKER_EVENTFOUNTAIN_STARTUP".equals(action)) {
                        Affair.f().d(1208, intent);
                        return;
                    }
                    if ("oplus.intent.action.TASK_TERMINATION_FOR_LOW_STORAGE".equals(action)) {
                        StorageMonitorService.Y(OplusBatteryMonitor.this.f19088b).U(intent);
                        return;
                    }
                    if ("oplus.intent.action.DATA_LEVEL_CHANGE".equals(action)) {
                        StorageMonitorService.Y(OplusBatteryMonitor.this.f19088b).U(intent);
                        return;
                    }
                    if ("oplus.intent.action.DIALOG_DATA".equals(action)) {
                        StorageMonitorService.Y(OplusBatteryMonitor.this.f19088b).U(intent);
                        return;
                    }
                    if ("oplus.intent.action.DIALOG_SD".equals(action)) {
                        StorageMonitorService.Y(OplusBatteryMonitor.this.f19088b).U(intent);
                        return;
                    }
                    if ("oplus.intent.action.ACTION_AAB_RESULT_CHANGE".equals(action)) {
                        Affair.f().d(1301, intent);
                        return;
                    }
                    if ("oplus.intent.action.UPDATERESTOREFILE".equals(action)) {
                        LocalLog.a("OplusBatteryMonitor", "OplusBatteryConstants.ACTION_RESTROE_BROADCAST");
                        Affair.f().d(1401, intent);
                        return;
                    }
                    if ("android.media.ACTION_AUDIO_SILENCE_PLAYBACK".equals(action)) {
                        return;
                    }
                    if ("oplus.intent.action.SCREENON_WAKELOCK_POWER_NOTIFY".equals(action)) {
                        LocalLog.a("OplusBatteryMonitor", "OplusBatteryConstants.ACTION_OPLUS_SCREENON_WAKELOCK_NOTIFY");
                        Affair.f().d(1206, intent);
                        return;
                    }
                    if ("oplus.intent.action.OPLUS_SUB_USER_NOTIFY".equals(action)) {
                        LocalLog.a("OplusBatteryMonitor", "OplusBatteryConstants.ACTION_OPLUS_SUB_USER_NOTIFY");
                        if (UserHandle.myUserId() != 0) {
                            LocalLog.a("OplusBatteryMonitor", "OplusBatteryConstants.ACTION_OPLUS_SUB_USER_NOTIFY: UserHandle.myUserId() != 0");
                            Affair.f().d(1207, intent);
                            return;
                        }
                        return;
                    }
                    if ("android.intent.action.TIME_SET".equals(action)) {
                        if (LocalLog.f()) {
                            LocalLog.a("OplusBatteryMonitor", " onReciver  action=" + action);
                        }
                        Affair.f().d(1209, intent);
                        return;
                    }
                    if ("oplus.intent.action_ENDURANCE_SCENE_CHANGE".equals(action)) {
                        Affair.f().d(1404, intent);
                        return;
                    }
                    return;
                }
                Affair.f().d(1101, intent);
                return;
            }
            Affair.f().d(EventType.SCENE_MODE_GAME, intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$d */
    /* loaded from: classes.dex */
    public class d extends ContentObserver {
        public d(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            int i10 = Settings.Global.getInt(OplusBatteryMonitor.this.f19088b.getContentResolver(), "device_provisioned", 0);
            Bundle bundle = new Bundle();
            bundle.putBoolean("device_provisioned_state", i10 == 1);
            Affair.f().e(905, bundle);
        }
    }

    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$e */
    /* loaded from: classes.dex */
    public static class e extends ContentObserver {

        /* renamed from: a, reason: collision with root package name */
        private Context f19111a;

        /* renamed from: b, reason: collision with root package name */
        private Handler f19112b;

        /* renamed from: c, reason: collision with root package name */
        private ContentObserver f19113c;

        /* renamed from: d, reason: collision with root package name */
        private ContentObserver f19114d;

        public e(Handler handler, Context context) {
            super(handler);
            this.f19111a = context;
            this.f19112b = handler;
        }

        private void a(Context context, Handler handler) {
            if (y5.b.E()) {
                this.f19113c = new GTModeBroadcastReceiver.c(handler, this.f19111a);
                this.f19111a.getContentResolver().registerContentObserver(Settings.Global.getUriFor("low_power"), false, this.f19113c, -2);
                this.f19114d = new GTModeBroadcastReceiver.d(handler, this.f19111a);
                this.f19111a.getContentResolver().registerContentObserver(Settings.System.getUriFor("super_powersave_mode_state"), false, this.f19114d, 0);
            }
        }

        private void b() {
            if (y5.b.E()) {
                if (this.f19113c != null) {
                    this.f19111a.getContentResolver().unregisterContentObserver(this.f19113c);
                }
                if (this.f19114d != null) {
                    this.f19111a.getContentResolver().unregisterContentObserver(this.f19114d);
                }
            }
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (Settings.System.getIntForUser(this.f19111a.getContentResolver(), "gt_mode_state_setting", 0, 0) == 1) {
                a(this.f19111a, this.f19112b);
                GTModeBroadcastReceiver.b(this.f19111a, true);
                GTModeBroadcastReceiver.a(this.f19111a, 1920056319);
                ThermalControllerCenter.getInstance(this.f19111a).reloadGTstate(true);
                Settings.Global.putLong(this.f19111a.getContentResolver(), "gtmode_state_time", SystemClock.elapsedRealtime());
                return;
            }
            b();
            GTModeBroadcastReceiver.b(this.f19111a, false);
            ThermalControllerCenter.getInstance(this.f19111a).reloadGTstate(false);
            Settings.Global.putLong(this.f19111a.getContentResolver(), "gtmode_state_time", 0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$f */
    /* loaded from: classes.dex */
    public class f extends ContentObserver {
        public f(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean C = CommonUtil.C(OplusBatteryMonitor.this.f19088b);
            Bundle bundle = new Bundle();
            bundle.putBoolean("highpref_state", C);
            Affair.f().e(902, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$g */
    /* loaded from: classes.dex */
    public class g extends ContentObserver {
        public g(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean z11 = Settings.Global.getInt(OplusBatteryMonitor.this.f19088b.getContentResolver(), "low_power", 0) == 1;
            Bundle bundle = new Bundle();
            bundle.putBoolean("powersave_state", z11);
            Affair.f().e(901, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: OplusBatteryMonitor.java */
    /* renamed from: v4.f$h */
    /* loaded from: classes.dex */
    public class h extends ContentObserver {
        public h(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean a12 = f6.f.a1(OplusBatteryMonitor.this.f19088b);
            Bundle bundle = new Bundle();
            bundle.putBoolean("s_powersave_state", a12);
            Affair.f().e(903, bundle);
        }
    }

    public OplusBatteryMonitor(Context context) {
        this.f19091e = null;
        this.f19092f = null;
        this.f19093g = null;
        this.f19094h = null;
        this.f19088b = context;
        this.f19091e = ConfigUpdateUtil.n(context);
        this.f19092f = StatusUtils.a(this.f19088b);
        PowerManager powerManager = (PowerManager) this.f19088b.getSystemService("power");
        this.f19093g = powerManager;
        PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, "OplusBatteryMonitor:partial_wke_lock");
        this.f19094h = newWakeLock;
        newWakeLock.setReferenceCounted(false);
    }

    private void h() {
        PowerModeUnion.e().g();
        TempHandleHelper.a().c();
        AffairDcsHelper.g().k();
        ScreenoffStartInfoNotifyHelper.b().e();
        AffairScreenOnAndOff.g().h();
        AffairResControl.n().u();
        BatteryStatsManager.i().n();
        AffairRusHelper.a().c();
        StorageMonitor.a().b();
        AffairAudioAbnormal.a().b();
        AffairScreenAbnormal.a().b();
        AffairSubUserNotify.a().b();
    }

    private void j() {
        StorageMonitorService.Y(this.f19088b).f0();
        PowerSaveMode.d(this.f19088b).e();
        PowerConsumeManager.b(this.f19088b).e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void k() {
        AppInfoUtils.d().e();
        if (SimplePowerMonitorUtils.f17653d && UserHandle.myUserId() == 0) {
            SimplePowerMonitorUtils.f(this.f19088b);
            AlarmSetter alarmSetter = new AlarmSetter(this.f19088b);
            LocalLog.a("OplusBatteryMonitor", "do initHighPowerNotification");
            PowerMonitor.i().clear();
            SimplePowerMonitorUtils.k(this.f19088b, PowerMonitor.i());
            PowerMonitor.h(this.f19088b).o();
            if (SimplePowerMonitorUtils.f17658i) {
                alarmSetter.d();
                alarmSetter.g();
            }
            if (ThermalControlUtils.getInstance(this.f19088b).isScreenOn() && SimplePowerMonitorUtils.f17656g) {
                alarmSetter.f();
            }
            if (!ThermalControlUtils.getInstance(this.f19088b).isScreenOn() && SimplePowerMonitorUtils.f17657h) {
                alarmSetter.e();
            }
        }
        LocalLog.a("OplusBatteryMonitor", "do not initHighPowerNotification");
    }

    private void l() {
        AppSwitchManager appSwitchManager = new AppSwitchManager(this.f19088b);
        this.f19102p = appSwitchManager;
        appSwitchManager.b(this.f19103q);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m(Handler handler) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.OPLUS_GUARD_ELF_MONITOR");
        intentFilter.addAction("oplus.android.intent.action.FORBID_INSTALL");
        intentFilter.addAction("android.intent.action.OPLUS_APP_FROZEN_DCS_UPLOADE");
        intentFilter.addAction("android.intent.action.OPLUS_GUARD_ELF_ANR_MONITOR");
        intentFilter.addAction("android.intent.action.OPLUS_GUARD_TIME_INFO");
        intentFilter.addAction("oplus.intent.action.UPDATERESTOREFILE");
        intentFilter.addAction("oplus.intent.action.OPLUS_STARTUP_APP_MONITOR");
        intentFilter.addAction("android.intent.action.OPLUS_ACTIVITY_CALL_MONITOR");
        intentFilter.addAction("android.intent.action.OPLUS_ACTIVITY_INTERCEPT_MONITOR");
        intentFilter.addAction("com.oplus.action.OPLUSBATTERY_NOTIFY_RESRITCTED_APP");
        intentFilter.addAction("oplus.intent.action.CPU_MONITOR_NOTIFY");
        intentFilter.addAction("android.intent.action.FORCE_CLOSE_ALL_PROCESS");
        intentFilter.addAction("oplus.intent.action.DEEP_SLEEP_ESPECIAL_TRAFFIC_REQ");
        intentFilter.addAction("android.intent.action.USER_BACKGROUND");
        intentFilter.addAction("android.intent.action.USER_FOREGROUND");
        intentFilter.addAction("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_F");
        intentFilter.addAction("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_NF");
        intentFilter.addAction("oplus.intent.action.high_power_consumption_notification.nomore");
        intentFilter.addAction("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_FIRST");
        intentFilter.addAction("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_SECOND");
        intentFilter.addAction("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_THIRD");
        intentFilter.addAction("oplus.intent.action.ACTION_THERMAL_CONTROL_DIS_WINDOW");
        intentFilter.addAction("oplus.intent.action.ACTION_THERMAL_CONTROL_USER_SYSTEM");
        intentFilter.addAction("oplus.intent.action.OPLUS_CAMERA_OPTION");
        intentFilter.addAction("oplus.intent.action.DEEPTHINKER_EVENTFOUNTAIN_STARTUP");
        intentFilter.addAction("oplus.intent.action.TASK_TERMINATION_FOR_LOW_STORAGE");
        intentFilter.addAction("oplus.intent.action.DIALOG_DATA");
        intentFilter.addAction("oplus.intent.action.DIALOG_SD");
        intentFilter.addAction("oplus.intent.action.DATA_LEVEL_CHANGE");
        intentFilter.addAction("android.media.ACTION_AUDIO_SILENCE_PLAYBACK");
        intentFilter.addAction("oplus.intent.action.SCREENON_WAKELOCK_POWER_NOTIFY");
        intentFilter.addAction("oplus.intent.action.OPLUS_SUB_USER_NOTIFY");
        intentFilter.addAction("oplus.intent.action.ACTION_AAB_RESULT_CHANGE");
        intentFilter.addAction("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS");
        this.f19088b.registerReceiver(this.f19106t, intentFilter, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.ADDITIONAL_BATTERY_CHANGED");
        this.f19088b.registerReceiver(this.f19106t, intentFilter2, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter3.addAction("android.intent.action.DATE_CHANGED");
        intentFilter3.addAction("android.intent.action.TIME_SET");
        intentFilter3.addAction("android.intent.action.TIME_TICK");
        this.f19088b.registerReceiver(this.f19106t, intentFilter3, null, handler, 2);
        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter4.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter4.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        intentFilter4.addDataScheme("package");
        this.f19088b.registerReceiver(this.f19106t, intentFilter4, null, handler, 2);
        IntentFilter intentFilter5 = new IntentFilter();
        intentFilter5.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.f19088b.registerReceiver(this.f19106t, intentFilter5, null, handler, 2);
        IntentFilter intentFilter6 = new IntentFilter();
        intentFilter6.addAction("oplus.intent.action.ACTION_HIGH_PERFORMANCE");
        this.f19088b.registerReceiver(this.f19106t, intentFilter6, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        IntentFilter intentFilter7 = new IntentFilter();
        intentFilter7.addAction("android.intent.action.LOCALE_CHANGED");
        this.f19088b.registerReceiver(this.f19106t, intentFilter7, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        IntentFilter intentFilter8 = new IntentFilter();
        intentFilter8.addAction("oplus.intent.action.BATTERY_PLUGGED_CHANGED");
        intentFilter8.addAction("oplus.intent.action.BATTERY_DATA_UPDATE");
        this.f19088b.registerReceiver(this.f19106t, intentFilter8, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        IntentFilter intentFilter9 = new IntentFilter();
        intentFilter9.addAction("oplus.power.monitor.action.PUSH_BROADCAST_INTENT");
        intentFilter9.addAction("oplus.power.monitor.action.BEAT_BROADCAST_INTENT");
        this.f19088b.registerReceiver(this.f19106t, intentFilter9, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        IntentFilter intentFilter10 = new IntentFilter();
        intentFilter10.addAction("oplus.intent.action_ENDURANCE_SCENE_CHANGE");
        this.f19088b.registerReceiver(this.f19106t, intentFilter10, null, handler, 4);
        this.f19100n = new NetworkStatusReceiver();
        IntentFilter intentFilter11 = new IntentFilter();
        intentFilter11.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter11.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter11.addAction("android.net.wifi.STATE_CHANGE");
        this.f19088b.registerReceiver(this.f19100n, intentFilter11, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        if (y5.b.E()) {
            IntentFilter intentFilter12 = new IntentFilter();
            intentFilter12.addAction("gt_mode_broadcast_intent_open_action");
            intentFilter12.addAction("gt_mode_broadcast_intent_close_action");
            intentFilter12.addAction("gtmode_intent_open_action");
            intentFilter12.addAction("android.intent.action.ACTION_SHUTDOWN");
            this.f19088b.registerReceiver(this.f19104r, intentFilter12, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
        }
        this.f19090d = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n(Handler handler) {
        AABResultObserver.b(this.f19088b).c();
        this.f19095i = new g(handler);
        this.f19088b.getContentResolver().registerContentObserver(Settings.Global.getUriFor("low_power"), false, this.f19095i, -2);
        if (f6.f.i1()) {
            this.f19096j = new f(handler);
            this.f19088b.getContentResolver().registerContentObserver(Settings.System.getUriFor("high_performance_mode_on"), false, this.f19096j, 0);
        }
        this.f19097k = new h(handler);
        this.f19088b.getContentResolver().registerContentObserver(Settings.System.getUriFor("super_powersave_mode_state"), false, this.f19097k, 0);
        this.f19098l = new d(handler);
        this.f19088b.getContentResolver().registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this.f19098l, -2);
        if (y5.b.E()) {
            this.f19105s = new e(handler, this.f19088b);
            this.f19088b.getContentResolver().registerContentObserver(Settings.System.getUriFor("gt_mode_state_setting"), false, this.f19105s, 0);
        }
    }

    private void q() {
        AppSwitchManager appSwitchManager = this.f19102p;
        if (appSwitchManager != null) {
            appSwitchManager.c(this.f19103q);
        }
    }

    public void i() {
        this.f19101o.post(new Runnable() { // from class: v4.e
            @Override // java.lang.Runnable
            public final void run() {
                OplusBatteryMonitor.this.k();
            }
        });
    }

    public void o() {
        LocalLog.a("OplusBatteryMonitor", "startMonitor");
        HandlerThread handlerThread = new HandlerThread("OplusBatteryMonitor");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        this.f19101o = handler;
        handler.post(new a());
        l();
        j();
        h();
    }

    public void p() {
        if (this.f19090d) {
            this.f19088b.unregisterReceiver(this.f19106t);
            this.f19090d = false;
        }
        if (this.f19095i != null) {
            this.f19088b.getContentResolver().unregisterContentObserver(this.f19095i);
        }
        if (this.f19096j != null) {
            this.f19088b.getContentResolver().unregisterContentObserver(this.f19096j);
        }
        if (this.f19097k != null) {
            this.f19088b.getContentResolver().unregisterContentObserver(this.f19097k);
        }
        if (this.f19098l != null) {
            this.f19088b.getContentResolver().unregisterContentObserver(this.f19098l);
        }
        q();
        Affair.f().c();
        AABResultObserver.b(this.f19088b).d();
        if (!y5.b.E() || this.f19105s == null) {
            return;
        }
        this.f19088b.getContentResolver().unregisterContentObserver(this.f19105s);
    }
}
