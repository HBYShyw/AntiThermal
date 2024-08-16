package com.oplus.battery;

import a7.PowerConsumeStats;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.EventLog;
import b6.LocalLog;
import b9.DataMoveHelper;
import c6.NotifyUtil;
import c9.WirelessChargingController;
import com.android.internal.os.ProcessCpuTracker;
import com.oplus.battery.OplusBatteryService;
import com.oplus.deepsleep.ControllerCenter;
import com.oplus.deepsleep.DeepSleepUtils;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.modulehub.oassist.BatteryAssistService;
import com.oplus.modulehub.smartdoze.SmartDozeService;
import com.oplus.powermanager.powercurve.SaveBatteryStatsReceiver;
import com.oplus.statistics.util.TimeInfoUtil;
import com.oplus.thermalcontrol.HeatSourceController;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.thermalcontrol.ThermalControlUtils;
import com.oplus.thermalcontrol.ThermalControllerCenter;
import ea.DeepThinkerProxy;
import ea.StateManager;
import f6.ChargeUtil;
import f6.CommonUtil;
import f6.f;
import g7.OGuardRusHelper;
import ha.StorageMonitorService;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import n8.PowerConsumptionOptimizationHelper;
import org.json.JSONException;
import org.json.JSONObject;
import p6.AppStandbyStatistic;
import r6.ProcessCpuManager;
import s6.ThermalFactory;
import u9.StartupManagerAction;
import v4.CustmizeAllowBgRunable;
import v4.OplusBatteryHandler;
import v4.OplusBatteryMonitor;
import w4.Affair;
import w4.IAffairCallback;
import w6.PluginMethodCaller;
import w6.PluginSupporter;
import w6.RegionPluginUtil;
import w7.ChargeAuthenticationController;
import x6.BMDiaUtil;
import x7.ChargeProtectionController;
import x7.SmartChargeProtectionController;
import x8.DatabaseHelper;
import y5.AppFeature;
import y6.PowerConsumeManager;
import y8.SmartChargeController;
import z8.SuperPowersaveController;

/* loaded from: classes.dex */
public class OplusBatteryService extends Service {

    /* renamed from: e, reason: collision with root package name */
    private HandlerThread f9764e;

    /* renamed from: f, reason: collision with root package name */
    private DeepSleepUtils f9765f;

    /* renamed from: i, reason: collision with root package name */
    private PendingIntent f9768i;

    /* renamed from: j, reason: collision with root package name */
    private PendingIntent f9769j;

    /* renamed from: g, reason: collision with root package name */
    private OplusBatteryHandler f9766g = null;

    /* renamed from: h, reason: collision with root package name */
    private OplusBatteryMonitor f9767h = null;

    /* renamed from: k, reason: collision with root package name */
    private IAffairCallback f9770k = new a();

    /* renamed from: l, reason: collision with root package name */
    private Messenger f9771l = new Messenger(new c());

    /* renamed from: m, reason: collision with root package name */
    private BroadcastReceiver f9772m = new d();

    /* loaded from: classes.dex */
    class a implements IAffairCallback {
        a() {
        }

        public void a() {
            Affair.f().i(this, 905);
        }

        @Override // w4.IAffairCallback
        public void execute(int i10, Bundle bundle) {
            if (i10 == 905 && bundle.getBoolean("device_provisioned_state")) {
                OplusBatteryService.this.g(false);
                StartupManagerAction.o(OplusBatteryService.this.getApplicationContext()).x();
                a();
            }
        }

        @Override // w4.IAffairCallback
        public void registerAction() {
            Affair.f().g(this, 905);
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Configuration f9774e;

        b(Configuration configuration) {
            this.f9774e = configuration;
        }

        @Override // java.lang.Runnable
        public void run() {
            StorageMonitorService.Y(OplusBatteryService.this.getApplicationContext()).t0(this.f9774e);
        }
    }

    /* loaded from: classes.dex */
    class c extends Handler {

        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PowerConsumeManager.b(OplusBatteryService.this.getBaseContext()).d();
            }
        }

        c() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message == null) {
                return;
            }
            LocalLog.a("Messenger", "service receive msg " + message.arg1);
            int i10 = message.arg1;
            if (i10 != 1111) {
                if (i10 == 1112) {
                    OplusBatteryService.this.f9766g.post(new a());
                    return;
                } else if (i10 == 1113) {
                    f.y3(OplusBatteryService.this.getBaseContext(), true);
                    return;
                } else {
                    if (i10 == 1114) {
                        f.y3(OplusBatteryService.this.getBaseContext(), false);
                        return;
                    }
                    return;
                }
            }
            ArrayList<PowerConsumeStats.b> a10 = PowerConsumeManager.b(OplusBatteryService.this.getApplicationContext()).a();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("server send abnormals,items size is ");
            sb2.append(a10 != null ? a10.size() : -1);
            LocalLog.a("Messenger", sb2.toString());
            Message obtain = Message.obtain();
            obtain.arg1 = 1111;
            Bundle bundle = new Bundle();
            bundle.putSerializable("abnormals", a10);
            obtain.setData(bundle);
            try {
                message.replyTo.send(obtain);
            } catch (RemoteException e10) {
                e10.printStackTrace();
            }
        }
    }

    /* loaded from: classes.dex */
    class d extends BroadcastReceiver {
        d() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            LocalLog.a("ACMEALARM", "receive " + intent.getAction());
            AlarmManager alarmManager = (AlarmManager) OplusBatteryService.this.getSystemService("alarm");
            if ("com.action.guardelf.exact".equals(intent.getAction())) {
                alarmManager.setExact(0, System.currentTimeMillis() + 140000, OplusBatteryService.this.f9768i);
            } else if ("com.action.guardelf.inexact".equals(intent.getAction())) {
                alarmManager.set(0, System.currentTimeMillis() + 200000, OplusBatteryService.this.f9769j);
            }
        }
    }

    private boolean e() {
        return SystemProperties.getBoolean("sys.deepsleep.allow.debug", false);
    }

    private void f() {
        if (this.f9765f.getDeepSleepResetAirPlaneStatus() && this.f9765f.getAirPlaneModeStatus()) {
            this.f9765f.setAirPlaneModeStatus(false);
            this.f9765f.setDeepSleepResetAirPlaneStatus(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g(boolean z10) {
        this.f9767h.i();
        PluginSupporter.m().p();
        if (LocalLog.f()) {
            LocalLog.a("DeepSleep_Test", "initAfterBootReg");
        }
    }

    private void j() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.guardelf.exact");
        intentFilter.addAction("com.action.guardelf.inexact");
        registerReceiver(this.f9772m, intentFilter, "oplus.permission.OPLUS_COMPONENT_SAFE", null, 2);
    }

    private void k() {
        unregisterReceiver(this.f9772m);
    }

    /* JADX WARN: Removed duplicated region for block: B:133:0x0287  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01f9  */
    @Override // android.app.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        float f10;
        int i15;
        boolean z10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float parseFloat;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        int i28;
        boolean z11;
        boolean z12;
        String str;
        String str2;
        String str3;
        String str4;
        boolean z13;
        StorageMonitorService Y;
        long currentTimeMillis;
        long j10;
        if (strArr.length <= 0) {
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        for (String str5 : strArr) {
            sb2.append(str5);
            sb2.append(" ");
        }
        LocalLog.l("DeepSleep_Test", "Battery debug:" + ((Object) sb2));
        ControllerCenter controllerCenter = ControllerCenter.getInstance(getApplicationContext());
        String str6 = strArr[0];
        if ("setSleepDuration".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not allow");
                LocalLog.l("DeepSleep_Test", "set sleep duration cmd not allowed");
                return;
            }
            if (strArr.length == 3) {
                currentTimeMillis = Long.parseLong(strArr[1]);
                j10 = Long.parseLong(strArr[2]);
            } else {
                currentTimeMillis = System.currentTimeMillis();
                j10 = TimeInfoUtil.MILLISECOND_OF_A_DAY + currentTimeMillis;
            }
            if (controllerCenter != null) {
                controllerCenter.setTestSleepDuration(currentTimeMillis, j10);
            }
            printWriter.println("cmd done.");
            return;
        }
        if ("forceStep".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not allow");
                LocalLog.l("DeepSleep_Test", "force step cmd not allowed");
                return;
            } else {
                if (controllerCenter != null) {
                    controllerCenter.testForceStep();
                }
                printWriter.println("cmd done.");
                return;
            }
        }
        if ("restoreNetworkReq".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not allow");
                LocalLog.l("DeepSleep_Test", "restore net req cmd not allowed");
                return;
            }
            Intent intent = new Intent("oplus.intent.action.DEEP_SLEEP_RESTORE_NETWORK");
            intent.setPackage("com.oplus.battery");
            intent.putExtra(DeviceDomainManager.ARG_PKG, "com.test");
            sendBroadcast(intent, "oplus.permission.OPLUS_COMPONENT_SAFE");
            printWriter.println("cmd done.");
            return;
        }
        if ("status".equals(str6)) {
            if (controllerCenter != null) {
                controllerCenter.dumpStatus(printWriter);
                return;
            }
            return;
        }
        if ("testSleepDurationSelect".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not aliow");
                LocalLog.l("DeepSleep_Test", "test sleep duration select cmd not allowed");
                return;
            } else {
                if (controllerCenter != null) {
                    controllerCenter.testSleepDurationSelect();
                }
                printWriter.println("cmd done.");
                return;
            }
        }
        if ("lowStorageTaskTerminate".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not aliow");
                LocalLog.l("DeepSleep_Test", "low storage task cmd not allowed");
                return;
            }
            if (strArr.length == 3) {
                String str7 = strArr[1];
                String str8 = strArr[2];
                Intent intent2 = new Intent("oplus.intent.action.TASK_TERMINATION_FOR_LOW_STORAGE");
                intent2.putExtra("package", str7);
                intent2.putExtra("space", str8);
                sendBroadcast(intent2, "oplus.permission.OPLUS_COMPONENT_SAFE");
                LocalLog.a("DeepSleep_Test", "lowStorageTaskTerminate: pkg=" + str7 + ", space=" + str8);
            } else if (strArr.length == 4) {
                String str9 = strArr[1];
                String str10 = strArr[2];
                if ("force".equals(strArr[3]) && (Y = StorageMonitorService.Y(this)) != null) {
                    Y.W(str10);
                }
                LocalLog.a("DeepSleep_Test", "lowStorageTaskTerminate: froce. pkg=" + str9 + ", space=" + str10);
            }
            printWriter.println("cmd done.");
            return;
        }
        if ("appTrafficStart".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not aliow");
                LocalLog.l("DeepSleep_Test", "app traffic start cmd not allowed");
                return;
            }
            if (strArr.length == 4) {
                str4 = strArr[1];
                str3 = strArr[2];
                if ("autostop".equals(strArr[3])) {
                    z13 = true;
                    Intent intent3 = new Intent("oplus.intent.action.DEEP_SLEEP_ESPECIAL_TRAFFIC_REQ");
                    intent3.setPackage("com.oplus.battery");
                    intent3.putExtra("req", "start");
                    intent3.putExtra(DeviceDomainManager.ARG_PKG, str4);
                    if (z13) {
                        intent3.putExtra("autoStop", true);
                    }
                    if (str3 != null) {
                        intent3.putExtra("job", str3);
                    }
                    sendBroadcast(intent3, "oplus.permission.OPLUS_COMPONENT_SAFE");
                    printWriter.println("cmd done.");
                    return;
                }
            } else if (strArr.length == 3) {
                str4 = strArr[1];
                str3 = strArr[2];
            } else {
                if (strArr.length != 2) {
                    return;
                }
                str3 = null;
                str4 = strArr[1];
            }
            z13 = false;
            Intent intent32 = new Intent("oplus.intent.action.DEEP_SLEEP_ESPECIAL_TRAFFIC_REQ");
            intent32.setPackage("com.oplus.battery");
            intent32.putExtra("req", "start");
            intent32.putExtra(DeviceDomainManager.ARG_PKG, str4);
            if (z13) {
            }
            if (str3 != null) {
            }
            sendBroadcast(intent32, "oplus.permission.OPLUS_COMPONENT_SAFE");
            printWriter.println("cmd done.");
            return;
        }
        if ("appTrafficStop".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not alow");
                LocalLog.l("DeepSleep_Test", "app traffic stop cmd not allowed");
                return;
            }
            if (strArr.length == 3) {
                str2 = strArr[1];
                str = strArr[2];
            } else {
                if (strArr.length != 2) {
                    return;
                }
                str = null;
                str2 = strArr[1];
            }
            Intent intent4 = new Intent("oplus.intent.action.DEEP_SLEEP_ESPECIAL_TRAFFIC_REQ");
            intent4.setPackage("com.oplus.battery");
            intent4.putExtra("req", "stop");
            intent4.putExtra(DeviceDomainManager.ARG_PKG, str2);
            if (str != null) {
                intent4.putExtra("job", str);
            }
            sendBroadcast(intent4, "oplus.permission.OPLUS_COMPONENT_SAFE");
            printWriter.println("cmd done.");
            return;
        }
        if ("testModeOn".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not allow");
                LocalLog.l("DeepSleep_Test", "test mode on cmd not allowed");
                return;
            }
            if (strArr.length == 2) {
                z11 = true;
                if ("ignoreTraffic".equals(strArr[1])) {
                    z12 = true;
                    if (controllerCenter != null) {
                        controllerCenter.setTestMode(z11, z12);
                    }
                    printWriter.println("cmd done.");
                    return;
                }
            } else {
                z11 = true;
            }
            z12 = false;
            if (controllerCenter != null) {
            }
            printWriter.println("cmd done.");
            return;
        }
        if ("testModeOff".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not allow");
                return;
            }
            if (controllerCenter != null) {
                controllerCenter.setTestMode(false, false);
            }
            printWriter.println("cmd done.");
            return;
        }
        if ("simuAIPredict".equals(str6)) {
            if (!e()) {
                printWriter.println("cmd not allow");
                return;
            }
            if (controllerCenter != null) {
                controllerCenter.setSimuAIPredict(true);
            }
            printWriter.println("cmd done.");
            return;
        }
        if ("highTempProtect".equals(str6)) {
            printWriter.println(ThermalFactory.a(this, getMainLooper()).d());
            return;
        }
        if (!"get-panel-temp".equals(str6) && !"getToleranceComparedTemp".equals(str6)) {
            if (!str6.contains("set-panel-temp:") && !str6.contains("setToleranceComparedTemp:")) {
                if (!"reset-panel-temp".equals(str6) && !"resetToleranceComparedTemp".equals(str6)) {
                    if (str6.contains("setHighTempTriggerTemp:")) {
                        String[] split = str6.split(":");
                        if (split.length <= 0) {
                            printWriter.println("setHighTempTriggerTemp Error Parameter");
                            return;
                        }
                        int parseInt = Integer.parseInt(split[split.length - 1]);
                        int E = ThermalFactory.a(this, getMainLooper()).E(parseInt);
                        printWriter.println("setHighTempTriggerTemp " + parseInt);
                        if (E < 0) {
                            printWriter.println("BundleBatteryBak is null. trigger later");
                        } else {
                            printWriter.println("trigger now");
                        }
                        LocalLog.l("ThermalElfService", "setTriggerTemp " + parseInt);
                        return;
                    }
                    if ("resetHighTempTriggerTemp".equals(str6)) {
                        ThermalFactory.a(this, getMainLooper()).E(-1);
                        printWriter.println("reset trigger temp");
                        LocalLog.l("ThermalElfService", "resetHighTempTriggerTemp");
                        return;
                    }
                    if ("setEnvTempHigh".equals(str6)) {
                        if (ThermalFactory.a(this, getMainLooper()).C(true) < 0) {
                            printWriter.println("BundleBatteryBak is null. trigger later");
                            return;
                        } else {
                            printWriter.println("trigger now");
                            return;
                        }
                    }
                    if ("setEnvTempLow".equals(str6)) {
                        if (ThermalFactory.a(this, getMainLooper()).C(false) < 0) {
                            printWriter.println("BundleBatteryBak is null. trigger later");
                            return;
                        } else {
                            printWriter.println("trigger now");
                            return;
                        }
                    }
                    if ("resetEnvTemp".equals(str6)) {
                        ThermalFactory.a(this, getMainLooper()).A();
                        return;
                    }
                    if ("testPowerUpload".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        } else {
                            printWriter.println("cmd done.");
                            return;
                        }
                    }
                    if ("clearSdEverMount".equals(str6)) {
                        StorageMonitorService Y2 = StorageMonitorService.Y(this);
                        if (Y2 != null) {
                            Y2.S();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("sdStatus".equals(str6)) {
                        StorageMonitorService Y3 = StorageMonitorService.Y(this);
                        if (Y3 != null) {
                            Y3.V(printWriter);
                            return;
                        }
                        return;
                    }
                    if ("deepsleeppredict".equals(str6)) {
                        if (LocalLog.f()) {
                            LocalLog.d("DeepSleep_Test", "the dump function of deepsleep is called ");
                            StringBuilder sb3 = new StringBuilder("input:");
                            for (String str11 : strArr) {
                                sb3.append(str11);
                                sb3.append("_");
                            }
                            LocalLog.d("DeepSleep_Test", sb3.toString());
                            if (strArr.length > 1) {
                                String path = Environment.getExternalStorageDirectory().getPath();
                                String str12 = strArr[1];
                                File databasePath = getDatabasePath("DeepSleep.db");
                                LocalLog.d("DeepSleep_Test", databasePath.toString());
                                File sharedPreferencesPath = getSharedPreferencesPath("deep_sleep_predict");
                                if ("pushdb".equals(str12)) {
                                    String str13 = path + "/deepsleep/test.db";
                                    LocalLog.d("DeepSleep_Test", str13);
                                    File file = new File(str13);
                                    if (file.exists()) {
                                        if (FileUtils.copyFile(file, databasePath)) {
                                            LocalLog.l("DeepSleep_Test", "push db success!");
                                            System.exit(0);
                                            return;
                                        } else {
                                            LocalLog.l("DeepSleep_Test", "push db failed!");
                                            return;
                                        }
                                    }
                                    LocalLog.d("DeepSleep_Test", "db not exist,don't push");
                                    return;
                                }
                                if ("pull".equals(str12)) {
                                    LocalLog.d("DeepSleep_Test", sharedPreferencesPath.toString());
                                    File createDir = FileUtils.createDir(new File(path), "deepsleep");
                                    if (createDir != null) {
                                        File file2 = new File(createDir, "DeepSleep.db");
                                        File file3 = new File(createDir, "deep_sleep_predict.xml");
                                        LocalLog.d("DeepSleep_Test", file3.toString());
                                        if (databasePath.exists()) {
                                            LocalLog.d("DeepSleep_Test", "pull db success:" + FileUtils.copyFile(databasePath, file2));
                                        } else {
                                            LocalLog.d("DeepSleep_Test", "db not exist,don't pull");
                                        }
                                        if (sharedPreferencesPath.exists()) {
                                            LocalLog.d("DeepSleep_Test", "pull xml success:" + FileUtils.copyFile(sharedPreferencesPath, file3));
                                            return;
                                        }
                                        LocalLog.d("DeepSleep_Test", "xml not exist,don't pull");
                                        return;
                                    }
                                    LocalLog.l("DeepSleep_Test", "make dir failed!");
                                    return;
                                }
                                if ("pushxml".equals(str12)) {
                                    String str14 = path + "/deepsleep/result.xml";
                                    LocalLog.d("DeepSleep_Test", str14);
                                    File file4 = new File(str14);
                                    if (file4.exists()) {
                                        if (FileUtils.copyFile(file4, sharedPreferencesPath)) {
                                            LocalLog.l("DeepSleep_Test", "push xml success!");
                                            System.exit(0);
                                            return;
                                        } else {
                                            LocalLog.l("DeepSleep_Test", "push xml failed!");
                                            return;
                                        }
                                    }
                                    LocalLog.d("DeepSleep_Test", "xml not exist,don't push");
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if ("get_aab".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        RegionPluginUtil o10 = PluginSupporter.m().o();
                        if (o10 != null) {
                            o10.e(0L, false);
                            return;
                        }
                        return;
                    }
                    if ("get_cpu".equals(str6)) {
                        if (strArr.length != 2) {
                            printWriter.println("args is not right");
                            return;
                        }
                        try {
                            int parseInt2 = Integer.parseInt(strArr[1]);
                            if (parseInt2 > 0) {
                                printWriter.println("pid " + parseInt2 + " got cpu time " + new ProcessCpuTracker(false).getCpuTimeForPid(parseInt2));
                                return;
                            }
                            return;
                        } catch (Exception unused) {
                            printWriter.println("please input the right pid");
                            return;
                        }
                    }
                    if ("setSDflag".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        try {
                            if ("true".equals(strArr[1])) {
                                StorageMonitorService.U0(true);
                            } else if ("false".equals(strArr[1])) {
                                StorageMonitorService.U0(false);
                            }
                            return;
                        } catch (Exception unused2) {
                            printWriter.println("please input true or false in the end");
                            return;
                        }
                    }
                    if ("setHighPerDiaFlag".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        try {
                            if ("true".equals(strArr[1])) {
                                BMDiaUtil.f(getApplicationContext()).i(true);
                                printWriter.println("set true");
                            } else if ("false".equals(strArr[1])) {
                                BMDiaUtil.f(getApplicationContext()).i(false);
                                printWriter.println("set false");
                            }
                            return;
                        } catch (Exception unused3) {
                            printWriter.println("please input true or false in the end");
                            return;
                        }
                    }
                    if ("getAudioInList".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        String str15 = "";
                        for (int i29 = 0; i29 < ProcessCpuManager.k(getApplicationContext()).size(); i29++) {
                            str15 = str15 + String.valueOf(ProcessCpuManager.k(getApplicationContext()).get(i29)) + " ";
                        }
                        printWriter.println("audio in pid list " + str15);
                        return;
                    }
                    if ("forceGetAIPredict".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        if (controllerCenter != null) {
                            controllerCenter.testForceGetAIPredict();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSleepFirstFailTime".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Calendar calendar = Calendar.getInstance();
                        try {
                            int parseInt3 = Integer.parseInt(strArr[1]);
                            int parseInt4 = Integer.parseInt(strArr[2]);
                            calendar.set(11, parseInt3);
                            calendar.set(12, parseInt4);
                            calendar.set(13, 0);
                            calendar.set(14, 0);
                        } catch (Exception unused4) {
                            printWriter.println("please input right time 0-24:0-59");
                        }
                        long timeInMillis = calendar.getTimeInMillis();
                        if (controllerCenter != null) {
                            controllerCenter.setSleepFirstFailTime(timeInMillis);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSleepLastFailTime".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Calendar calendar2 = Calendar.getInstance();
                        try {
                            int parseInt5 = Integer.parseInt(strArr[1]);
                            int parseInt6 = Integer.parseInt(strArr[2]);
                            calendar2.set(11, parseInt5);
                            calendar2.set(12, parseInt6);
                            calendar2.set(13, 0);
                            calendar2.set(14, 0);
                        } catch (Exception unused5) {
                            printWriter.println("please input right time 0-24:0-59");
                        }
                        long timeInMillis2 = calendar2.getTimeInMillis();
                        if (controllerCenter != null) {
                            controllerCenter.setSleepLastFailTime(timeInMillis2);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSleepSuccess".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            LocalLog.l("DeepSleep_Test", "set sleep fail cnt cmd not allowed");
                            return;
                        } else {
                            if (controllerCenter != null) {
                                controllerCenter.setSleepSuccess();
                            }
                            printWriter.println("cmd done.");
                            return;
                        }
                    }
                    if ("setSleepFailCount".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            LocalLog.l("DeepSleep_Test", "set sleep fail cnt cmd not allowed");
                            return;
                        }
                        try {
                            i28 = Integer.parseInt(strArr[1]);
                        } catch (Exception unused6) {
                            printWriter.println("please input Integer");
                            i28 = 0;
                        }
                        if (controllerCenter != null) {
                            controllerCenter.setSleepFailCount(i28 > 7 ? 7 : i28 < 0 ? 0 : i28);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setDebug".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        try {
                            if ("true".equals(strArr[1])) {
                                CommonUtil.f0(true);
                                printWriter.println("set true");
                            } else if ("false".equals(strArr[1])) {
                                CommonUtil.f0(false);
                                printWriter.println("set false");
                            }
                            return;
                        } catch (Exception unused7) {
                            printWriter.println("please input true or false in the end");
                            return;
                        }
                    }
                    if ("setThermalControlTest".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            try {
                                i27 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused8) {
                                printWriter.println("please input Integer");
                                i27 = 0;
                            }
                            if (i27 == 1) {
                                ThermalControllerCenter.getInstance(getApplicationContext()).setThermalControlTest(true);
                            } else {
                                ThermalControllerCenter.getInstance(getApplicationContext()).setThermalControlTest(false);
                            }
                            printWriter.println("setThermalControlTest = " + i27);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setTempLevel".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            try {
                                i26 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused9) {
                                printWriter.println("please input Integer");
                                i26 = 0;
                            }
                            JSONObject jSONObject = new JSONObject();
                            boolean z14 = false;
                            for (int i30 = 2; i30 < strArr.length; i30++) {
                                String[] split2 = strArr[i30].split("-");
                                if (split2.length != 2) {
                                    printWriter.println("heat source key or value error, input example: cpu-0.5 gpu-0.1");
                                } else {
                                    try {
                                        JSONObject jSONObject2 = new JSONObject();
                                        jSONObject2.put("keyVal", (int) (Float.parseFloat(split2[1]) * 1000.0f));
                                        jSONObject.put(split2[0], jSONObject2);
                                        z14 = true;
                                    } catch (NumberFormatException | JSONException e10) {
                                        printWriter.println("put heat source to json error, e=" + e10);
                                    }
                                }
                            }
                            ThermalControllerCenter.getInstance(getApplicationContext()).sendTempGearChangedMessageForTest(i26, z14 ? jSONObject.toString() : "");
                            EventLog.writeEvent(2723, "ThermalControl in test, setTempLevel:" + i26);
                            printWriter.println("Temp level = " + i26);
                            if (z14) {
                                printWriter.println("HeatSource = " + jSONObject.toString());
                            } else {
                                printWriter.println("HeatSource empty, not update");
                            }
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("getThermalStatus".equals(str6)) {
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            printWriter.println("Thermal status = " + ThermalControlUtils.getInstance(this).getCurrentThermalStatus());
                            printWriter.println("Thermal Temperature = " + ThermalControlUtils.getInstance(this).getCurrentTemperature(false));
                            printWriter.println("cmd done.");
                            return;
                        }
                        return;
                    }
                    if ("setTsensorSource".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            try {
                                i25 = Integer.parseInt(strArr[1]) * 1000;
                            } catch (Exception unused10) {
                                printWriter.println("please input Integer");
                                i25 = 0;
                            }
                            ThermalControllerCenter.getInstance(getApplicationContext()).sendTsensorTempChangedMessage(i25);
                            printWriter.println("Tsensor cpu = " + i25);
                            return;
                        }
                        return;
                    }
                    if ("HeatSourceController".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        } else {
                            if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                                HeatSourceController.getInstance(getApplicationContext()).dump(fileDescriptor, printWriter, strArr);
                                return;
                            }
                            return;
                        }
                    }
                    if ("setChargingLevel".equals(str6)) {
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            printWriter.println("set charging level = " + strArr[1]);
                            ThermalControlUtils.getInstance(this).setChargingLevel(Integer.parseInt(strArr[1]), 0, "test");
                            return;
                        }
                        return;
                    }
                    if ("getThermalConfig".equals(str6)) {
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            printWriter.println(ThermalControlConfig.getInstance(this).getThermalConfig());
                            return;
                        }
                        return;
                    }
                    if ("getAmbientTemperature".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            int ambientTemperature = ThermalControlUtils.getInstance(getApplicationContext()).getAmbientTemperature();
                            int ambientTempState = ThermalControlUtils.getInstance(getApplicationContext()).getAmbientTempState();
                            printWriter.println("Ambient Temperature = " + ambientTemperature);
                            printWriter.println("Ambient State = " + ambientTempState);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setAmbientTemperature".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            try {
                                i24 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused11) {
                                printWriter.println("please input Integer");
                                i24 = 0;
                            }
                            ThermalControlUtils.getInstance(getApplicationContext()).setAmbientTemperature(i24);
                            printWriter.println("Environment Temperature = " + i24);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("sendChargeModeChanged".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            try {
                                i23 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused12) {
                                printWriter.println("please input Integer");
                                i23 = 0;
                            }
                            ThermalControlUtils.getInstance(getApplicationContext()).sendChargeModeChanged(i23);
                            printWriter.println("Charge Mode = " + i23);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("getCurThermalPolicy".equals(str6)) {
                        if (ThermalControlUtils.getInstance(this).getUserForeground()) {
                            printWriter.println("Thermal policy = " + ThermalControllerCenter.getInstance(getApplicationContext()).getCurThermalPolicy());
                            return;
                        }
                        return;
                    }
                    if ("setDynamicLog".equals(str6)) {
                        try {
                            PluginMethodCaller n10 = PluginSupporter.m().n();
                            if ("on".equals(strArr[1])) {
                                LocalLog.i(true);
                                if (n10 != null) {
                                    n10.j(Boolean.TRUE);
                                }
                                printWriter.println("Dynamic Log already turn on");
                                return;
                            }
                            if ("off".equals(strArr[1])) {
                                LocalLog.i(false);
                                if (n10 != null) {
                                    n10.j(Boolean.FALSE);
                                }
                                printWriter.println("Dynamic Log already turn off");
                                return;
                            }
                            return;
                        } catch (Exception unused13) {
                            printWriter.println("please input on or off in the end");
                            return;
                        }
                    }
                    if ("setThreeDay".equals(str6)) {
                        try {
                            if ("true".equals(strArr[1])) {
                                PowerConsumptionOptimizationHelper.k(this).u(true);
                                printWriter.println("set true");
                            } else if ("false".equals(strArr[1])) {
                                PowerConsumptionOptimizationHelper.k(this).u(false);
                                printWriter.println("set false");
                            }
                            return;
                        } catch (Exception unused14) {
                            printWriter.println("please input true or false in the end");
                            return;
                        }
                    }
                    if ("testBatteryCard".equals(str6)) {
                        try {
                            if ("level".equals(strArr[1])) {
                                Intent intent5 = new Intent("com.oplus.battery.OplusBatteryCardService");
                                intent5.setPackage(getPackageName());
                                intent5.putExtra("level", Integer.parseInt(strArr[2]));
                                startService(intent5);
                                printWriter.println("set level " + strArr[2]);
                                return;
                            }
                            return;
                        } catch (Exception unused15) {
                            printWriter.println("please input right command");
                            return;
                        }
                    }
                    if ("testPowerCurve".equals(str6)) {
                        try {
                            if ("delete".equals(strArr[1])) {
                                DatabaseHelper.u(this).b();
                                printWriter.println("delete complete");
                            } else if ("view".equals(strArr[1])) {
                                printWriter.println("view complete");
                                printWriter.println("value:\n" + DatabaseHelper.u(this).w());
                            }
                            return;
                        } catch (Exception unused16) {
                            printWriter.println("please input delete or view in the end");
                            return;
                        }
                    }
                    if ("powercontrol".equals(str6)) {
                        try {
                            if ("whitelist".equals(strArr[1])) {
                                Iterator<String> it = CustmizeAllowBgRunable.i(this).j().iterator();
                                while (it.hasNext()) {
                                    printWriter.println(it.next());
                                }
                                return;
                            }
                            return;
                        } catch (Exception unused17) {
                            printWriter.println("please input something in the end");
                            return;
                        }
                    }
                    if ("acme_alarm".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            LocalLog.l("DeepSleep_Test", "acme alarm cmd not allowed");
                            return;
                        }
                        if (strArr.length == 2) {
                            AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
                            if (this.f9768i == null) {
                                Intent intent6 = new Intent();
                                intent6.setAction("com.action.guardelf.exact");
                                intent6.setPackage("com.oplus.battery");
                                this.f9768i = PendingIntent.getBroadcast(this, 0, intent6, 67108864);
                            }
                            if (this.f9769j == null) {
                                Intent intent7 = new Intent();
                                intent7.setAction("com.action.guardelf.inexact");
                                intent7.setPackage("com.oplus.battery");
                                this.f9769j = PendingIntent.getBroadcast(this, 0, intent7, 67108864);
                            }
                            if ("on".equals(strArr[1])) {
                                printWriter.println("acme alarm start");
                                j();
                                alarmManager.setExact(0, System.currentTimeMillis() + 140000, this.f9768i);
                                alarmManager.set(0, System.currentTimeMillis() + 200000, this.f9769j);
                                return;
                            }
                            printWriter.println("acme alarm stop");
                            alarmManager.cancel(this.f9768i);
                            alarmManager.cancel(this.f9769j);
                            k();
                            return;
                        }
                        return;
                    }
                    if ("bluettothPanConnected".equals(str6)) {
                        if (this.f9765f.isBluetoothPanConnected()) {
                            printWriter.println("Connected");
                            return;
                        } else {
                            printWriter.println("Not Connected");
                            return;
                        }
                    }
                    if ("deepsleepRcd".equals(str6)) {
                        if (controllerCenter != null) {
                            controllerCenter.dumpRcdEvent(printWriter);
                            return;
                        }
                        return;
                    }
                    if ("getInUsePackagesList".equals(str6)) {
                        printWriter.println("listInUse: " + this.f9765f.getInUsePackagesList());
                        return;
                    }
                    if ("testAABuploadAppStandbyData".equals(str6)) {
                        new AppStandbyStatistic().a(getApplicationContext());
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setUIsohValue".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Context applicationContext = getApplicationContext();
                        if (AppFeature.d()) {
                            try {
                                i22 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused18) {
                                printWriter.println("please input Integer");
                                i22 = 0;
                            }
                            f.T1(applicationContext, true);
                            f.R1(applicationContext, i22);
                            printWriter.println("sohValue = " + i22);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("resetUIsohValue".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Context applicationContext2 = getApplicationContext();
                        if (AppFeature.d()) {
                            f.T1(applicationContext2, false);
                            printWriter.println("resetUIsohValue success ");
                            printWriter.println("cmd done.");
                            return;
                        }
                        return;
                    }
                    if ("setSmartChargeTestMode".equals(str6)) {
                        Context applicationContext3 = getApplicationContext();
                        if (AppFeature.n() && applicationContext3.getUserId() == 0) {
                            SmartChargeController.t(applicationContext3).J();
                            printWriter.println("setSmartChargeTestMode");
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeIncomeMinute".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Context applicationContext4 = getApplicationContext();
                        if (AppFeature.n() && applicationContext4.getUserId() == 0) {
                            try {
                                i21 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused19) {
                                printWriter.println("please input Integer");
                                i21 = 0;
                            }
                            f.U2(applicationContext4, f.z0(applicationContext4) + (i21 * 60));
                            printWriter.println("smartChargeIncomeMinute = " + i21);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeIncomePercent".equals(str6)) {
                        Context applicationContext5 = getApplicationContext();
                        if (AppFeature.n() && applicationContext5.getUserId() == 0) {
                            try {
                                i20 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused20) {
                                printWriter.println("please input Integer");
                                i20 = 0;
                            }
                            SmartChargeController.t(applicationContext5).G(i20);
                            printWriter.println("smartChargeIncomePercent = " + i20);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeJourneyInterfaceValue".equals(str6)) {
                        Context applicationContext6 = getApplicationContext();
                        if (AppFeature.n() && applicationContext6.getUserId() == 0) {
                            try {
                                i19 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused21) {
                                printWriter.println("please input Integer");
                                i19 = 0;
                            }
                            SmartChargeController.t(applicationContext6).H(i19);
                            printWriter.println("setSmartChargeJourneyInterfaceValue = " + i19);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeLevel".equals(str6)) {
                        Context applicationContext7 = getApplicationContext();
                        if (AppFeature.n() && applicationContext7.getUserId() == 0) {
                            try {
                                i18 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused22) {
                                printWriter.println("please input Integer");
                                i18 = 0;
                            }
                            SmartChargeController.t(applicationContext7).I(i18);
                            printWriter.println("smartChargeLevel = " + i18);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeTime".equals(str6)) {
                        Context applicationContext8 = getApplicationContext();
                        if (AppFeature.n() && applicationContext8.getUserId() == 0) {
                            try {
                                i17 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused23) {
                                printWriter.println("please input Integer");
                                i17 = 0;
                            }
                            SmartChargeController.t(applicationContext8).K(i17);
                            printWriter.println("setSmartChargeTime = " + i17);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("resetSmartChargeTestMode".equals(str6)) {
                        Context applicationContext9 = getApplicationContext();
                        if (AppFeature.n() && applicationContext9.getUserId() == 0) {
                            SmartChargeController.t(applicationContext9).E();
                            printWriter.println("resetSmartChargeTestMode");
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeNotification".equals(str6)) {
                        Context applicationContext10 = getApplicationContext();
                        if (AppFeature.n() && applicationContext10.getUserId() == 0) {
                            NotifyUtil.v(applicationContext10).N();
                            printWriter.println("setSmartChargeNotification");
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeNotificationHighTemp".equals(str6)) {
                        Context applicationContext11 = getApplicationContext();
                        if (AppFeature.n() && applicationContext11.getUserId() == 0) {
                            NotifyUtil.v(applicationContext11).M();
                            printWriter.println("setSmartChargeNotificationHighTemp");
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("triggerSmartChargeProtectionEnterNotify".equals(str6)) {
                        Context applicationContext12 = getApplicationContext();
                        if (AppFeature.q() && applicationContext12.getUserId() == 0) {
                            NotifyUtil.v(applicationContext12).B();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("triggerLongChargeProtectionRecommendOpen".equals(str6)) {
                        Context applicationContext13 = getApplicationContext();
                        if (AppFeature.q() && applicationContext13.getUserId() == 0) {
                            NotifyUtil.v(applicationContext13).P();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("triggerLongChargeProtectionRecommendClose".equals(str6)) {
                        Context applicationContext14 = getApplicationContext();
                        if (AppFeature.q() && applicationContext14.getUserId() == 0) {
                            NotifyUtil.v(applicationContext14).O();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setLongChargeProtectionTestMode".equals(str6)) {
                        Context applicationContext15 = getApplicationContext();
                        if (AppFeature.q() && applicationContext15.getUserId() == 0) {
                            SmartChargeProtectionController.J(applicationContext15).h0();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setLongChargeProtectionBatteryLevel".equals(str6)) {
                        Context applicationContext16 = getApplicationContext();
                        if (AppFeature.q() && applicationContext16.getUserId() == 0) {
                            try {
                                i16 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused24) {
                                printWriter.println("please input Integer");
                                i16 = 0;
                            }
                            SmartChargeProtectionController.J(applicationContext16).g0(i16);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    float f17 = 0.0f;
                    if ("setLongChargeProtectionAiData".equals(str6)) {
                        Context applicationContext17 = getApplicationContext();
                        if (AppFeature.q() && applicationContext17.getUserId() == 0) {
                            long j11 = 0;
                            try {
                                parseFloat = Float.parseFloat(strArr[1]);
                                try {
                                    f12 = Float.parseFloat(strArr[2]);
                                } catch (Exception unused25) {
                                    f12 = 0.0f;
                                    f13 = 0.0f;
                                }
                            } catch (Exception unused26) {
                                f11 = 0.0f;
                                f12 = 0.0f;
                                f13 = 0.0f;
                            }
                            try {
                                f13 = Float.parseFloat(strArr[3]);
                                try {
                                    f17 = Float.parseFloat(strArr[4]);
                                    j11 = Long.parseLong(strArr[5]);
                                    f16 = parseFloat;
                                    f15 = f12;
                                    f14 = f17;
                                } catch (Exception unused27) {
                                    float f18 = f17;
                                    f17 = parseFloat;
                                    f11 = f18;
                                    printWriter.println("please input Integer");
                                    f14 = f11;
                                    f15 = f12;
                                    f16 = f17;
                                    SmartChargeProtectionController.J(applicationContext17).c0(f16, f15, f13, f14, j11 * 60000);
                                    printWriter.println("cmd done.");
                                    return;
                                }
                            } catch (Exception unused28) {
                                f13 = 0.0f;
                                f17 = parseFloat;
                                f11 = f13;
                                printWriter.println("please input Integer");
                                f14 = f11;
                                f15 = f12;
                                f16 = f17;
                                SmartChargeProtectionController.J(applicationContext17).c0(f16, f15, f13, f14, j11 * 60000);
                                printWriter.println("cmd done.");
                                return;
                            }
                            SmartChargeProtectionController.J(applicationContext17).c0(f16, f15, f13, f14, j11 * 60000);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("resetLongChargeProtectionTestMode".equals(str6)) {
                        Context applicationContext18 = getApplicationContext();
                        if (AppFeature.q() && applicationContext18.getUserId() == 0) {
                            SmartChargeProtectionController.J(applicationContext18).a0();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("triggerHighPowerNotification".equals(str6)) {
                        Context applicationContext19 = getApplicationContext();
                        try {
                            z10 = Boolean.parseBoolean(strArr[1]);
                        } catch (Exception unused29) {
                            printWriter.println("please input Integer");
                            z10 = true;
                        }
                        NotifyUtil.v(applicationContext19).D("com.example.highpoweconsumptiontestapk", z10);
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("resetWirelessChargingNotificate".equals(str6)) {
                        Context applicationContext20 = getApplicationContext();
                        if (AppFeature.v()) {
                            f.r2(applicationContext20, false);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("triggerTestChargeAuthenticationController".equals(str6)) {
                        ChargeAuthenticationController.b(getApplicationContext()).d();
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setSmartChargeIncomeSecond".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Context applicationContext21 = getApplicationContext();
                        if (AppFeature.n() && applicationContext21.getUserId() == 0) {
                            try {
                                i15 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused30) {
                                printWriter.println("please input Integer");
                                i15 = 0;
                            }
                            f.U2(applicationContext21, f.z0(applicationContext21) + i15);
                            printWriter.println("smartChargeIncomeMinute = " + i15);
                            return;
                        }
                        return;
                    }
                    if ("setChargeTimeRemaining".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        long j12 = -99;
                        try {
                            j12 = Long.parseLong(strArr[1]);
                        } catch (Exception unused31) {
                            printWriter.println("please input Long");
                        }
                        ChargeUtil.r(j12, getApplicationContext());
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setAiRestDataHourWhileTestMode".equals(str6)) {
                        Context applicationContext22 = getApplicationContext();
                        if (AppFeature.q() && applicationContext22.getUserId() == 0) {
                            try {
                                f10 = Float.parseFloat(strArr[1]);
                            } catch (Exception unused32) {
                                f10 = 0.0f;
                            }
                            try {
                                f17 = Float.parseFloat(strArr[2]);
                            } catch (Exception unused33) {
                                printWriter.println("please input Integer");
                                SmartChargeProtectionController.J(applicationContext22).d0(f10, f17);
                                printWriter.println("cmd done.");
                                return;
                            }
                            SmartChargeProtectionController.J(applicationContext22).d0(f10, f17);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setBmsHeatStatus".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Context applicationContext23 = getApplicationContext();
                        try {
                            i14 = Integer.parseInt(strArr[1]);
                        } catch (Exception unused34) {
                            printWriter.println("please input Integer");
                            i14 = 0;
                        }
                        SmartChargeProtectionController.J(applicationContext23).e0(i14);
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setLongChargeProtectionChargeWattage".equals(str6)) {
                        Context applicationContext24 = getApplicationContext();
                        if (AppFeature.q() && applicationContext24.getUserId() == 0) {
                            try {
                                i13 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused35) {
                                printWriter.println("please input Integer");
                                i13 = 0;
                            }
                            SmartChargeProtectionController.J(applicationContext24).f0(i13);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("getAIRestData".equals(str6)) {
                        Context applicationContext25 = getApplicationContext();
                        if (AppFeature.q() && applicationContext25.getUserId() == 0) {
                            try {
                                i12 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused36) {
                                printWriter.println("please input Integer");
                                i12 = 0;
                            }
                            SmartChargeProtectionController.J(applicationContext25).G(i12, i12, true);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("handleSlowCharge".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        }
                        Context applicationContext26 = getApplicationContext();
                        try {
                            i11 = Integer.parseInt(strArr[1]);
                        } catch (Exception unused37) {
                            printWriter.println("please input Integer");
                            i11 = 0;
                        }
                        SmartChargeProtectionController.J(applicationContext26).N(i11);
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("getAILeaveHomeData".equals(str6)) {
                        Context applicationContext27 = getApplicationContext();
                        if (AppFeature.q() && applicationContext27.getUserId() == 0) {
                            try {
                                i10 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused38) {
                                printWriter.println("please input Integer");
                                i10 = 0;
                            }
                            SmartChargeProtectionController.J(applicationContext27).H(i10);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("setAiLeaveHomeTimeWhileTestMode".equals(str6)) {
                        Context applicationContext28 = getApplicationContext();
                        if (AppFeature.n() && applicationContext28.getUserId() == 0) {
                            try {
                                f17 = Float.parseFloat(strArr[1]);
                            } catch (Exception unused39) {
                                printWriter.println("please input Integer");
                            }
                            SmartChargeController.t(applicationContext28).F(f17);
                            printWriter.println("AiLeaveHomeTime = " + f17);
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    if ("testBatteryAssist".equals(str6)) {
                        if (!e()) {
                            printWriter.println("cmd not allow");
                            return;
                        } else {
                            if (ActivityManager.getCurrentUser() != getUserId()) {
                                printWriter.println("not current user");
                                return;
                            }
                            if (strArr.length >= 2) {
                                BatteryAssistService.testNoParamApi(getApplicationContext(), strArr[1]);
                            }
                            printWriter.println("cmd done.");
                            return;
                        }
                    }
                    if ("setTriggerForOneMaintence".equals(str6)) {
                        if (controllerCenter != null) {
                            controllerCenter.setTriggerForOneMaintence();
                        }
                        printWriter.println("cmd done.");
                        return;
                    }
                    return;
                }
                ThermalFactory.a(this, getMainLooper()).D(-999);
                printWriter.println(str6);
                LocalLog.l("ThermalElfService", "reset Tolerance Compared Temp");
                return;
            }
            String[] split3 = str6.split(":");
            if (split3.length <= 0) {
                printWriter.println("Error Parameter");
                return;
            }
            int parseInt7 = Integer.parseInt(split3[split3.length - 1]);
            ThermalFactory.a(this, getMainLooper()).D(parseInt7);
            printWriter.println(str6 + " " + parseInt7);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("set Tolerance Compared Temp ");
            sb4.append(parseInt7);
            LocalLog.l("ThermalElfService", sb4.toString());
            return;
        }
        printWriter.println(str6 + " " + ThermalFactory.a(this, getMainLooper()).o());
    }

    public void h() {
        long currentTimeMillis = System.currentTimeMillis();
        ControllerCenter.getInstance(getApplicationContext());
        ProcessCpuManager.j(getApplicationContext());
        OplusBatteryMonitor oplusBatteryMonitor = new OplusBatteryMonitor(this);
        this.f9767h = oplusBatteryMonitor;
        oplusBatteryMonitor.o();
        if (!CommonUtil.T(this)) {
            g(true);
        } else {
            this.f9770k.registerAction();
        }
        this.f9765f = DeepSleepUtils.getInstance(this);
        f();
        if (y5.b.x()) {
            WirelessChargingController.H(this).Q();
        }
        if (AppFeature.q() && UserHandle.myUserId() == 0) {
            SmartChargeProtectionController.J(this).i0();
        } else if (!AppFeature.h() && UserHandle.myUserId() == 0) {
            ChargeProtectionController.N(this).X();
        }
        SuperPowersaveController.Z(this).d0();
        this.f9765f.setAllListeners();
        StateManager.f(this);
        DeepThinkerProxy.j(this).k();
        ThermalControllerCenter.getInstance(this).onStart(this.f9764e.getLooper());
        this.f9766g.post(new Runnable() { // from class: v4.g
            @Override // java.lang.Runnable
            public final void run() {
                OplusBatteryService.this.i();
            }
        });
        if (LocalLog.f()) {
            LocalLog.a("DeepSleep_Test", "initCore done use time:" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }

    public void i() {
        long currentTimeMillis = System.currentTimeMillis();
        SaveBatteryStatsReceiver.p(this);
        SaveBatteryStatsReceiver.q(this);
        OGuardRusHelper.f(this);
        if (!new DataMoveHelper(this).a()) {
            LocalLog.a("DeepSleep_Test", "already do data move");
        }
        if (Settings.System.getFloatForUser(getContentResolver(), "settings_battery_init_density", -100.0f, 0) == -100.0f) {
            Settings.System.putFloatForUser(getContentResolver(), "settings_battery_init_density", getResources().getDisplayMetrics().density, 0);
        }
        if (AppFeature.n()) {
            SmartChargeController.t(this).L();
        }
        if (AppFeature.F()) {
            if (f.V0(this)) {
                f.q3(this, true);
            } else {
                f.q3(this, false);
            }
        }
        if (AppFeature.r() && !AppFeature.D()) {
            ChargeAuthenticationController.b(this).f();
        }
        if (UserHandle.myUserId() == 0) {
            SmartDozeService.I(this);
        }
        if (LocalLog.f()) {
            LocalLog.a("DeepSleep_Test", "initOther done use time:" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f9771l.getBinder();
    }

    @Override // android.app.Service, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        OplusBatteryHandler oplusBatteryHandler = this.f9766g;
        if (oplusBatteryHandler != null) {
            oplusBatteryHandler.post(new b(configuration));
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("OplusBatteryHandler");
        this.f9764e = handlerThread;
        handlerThread.start();
        this.f9766g = new OplusBatteryHandler(this, this.f9764e.getLooper());
        h();
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (y5.b.x()) {
            WirelessChargingController.H(this).M();
        }
        if (AppFeature.q() && UserHandle.myUserId() == 0) {
            SmartChargeProtectionController.J(this).S();
        } else if (!AppFeature.h() && UserHandle.myUserId() == 0) {
            ChargeProtectionController.N(this).S();
        }
        SuperPowersaveController.Z(this).c0();
        this.f9765f.removeAllListeners();
        ThermalControllerCenter.getInstance(this).onDestory();
        if (AppFeature.n()) {
            SmartChargeController.t(this).z();
        }
        if (AppFeature.r() && !AppFeature.D()) {
            ChargeAuthenticationController.b(this).c();
        }
        this.f9767h.p();
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i10, int i11) {
        return super.onStartCommand(intent, i10, i11);
    }
}
