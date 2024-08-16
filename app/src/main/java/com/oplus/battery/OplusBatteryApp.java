package com.oplus.battery;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.oguard.data.database.OGuardDataBase;
import com.oplus.oms.split.core.Oms;
import com.oplus.oms.split.core.SplitConfiguration;
import com.oplus.oms.split.splitdownload.Downloader;
import com.oplus.oms.split.splitdownload.IProvider;
import com.oplus.powermanager.wirelesscharg.WirelessReverseControService;
import com.oplus.statistics.gen.root_battery.TrackApi_20089;
import com.oplus.thermalcontrol.ThermalControlConfig;
import d6.ConfigUpdateUtil;
import g7.AppInfoManager;
import h6.AppFeatureCache;
import h6.AppFeatureProviderUtils;
import j7.AppPowerRecord;
import java.util.List;
import n8.PowerConsumptionOptimizationMessage;
import s8.PowerSaveConfig;
import s8.PowerSaveHelper;
import u9.StartupManagerAction;
import v4.CustmizeAllowBgRunable;
import v4.GuardElfContext;
import v8.PhoneScreenOnTime;
import v8.PhoneUsageTime;
import w5.OplusBatteryConstants;
import x5.UploadDataUtil;
import y5.AppFeature;
import z5.GuardElfDataManager;
import z5.LocalFileUtil;

/* loaded from: classes.dex */
public class OplusBatteryApp extends Application {

    /* renamed from: h, reason: collision with root package name */
    public static final boolean f9757h = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* renamed from: e, reason: collision with root package name */
    private Context f9758e;

    /* renamed from: f, reason: collision with root package name */
    private ConfigUpdateUtil f9759f;

    /* renamed from: g, reason: collision with root package name */
    private NotifyUtil f9760g = null;

    /* loaded from: classes.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f9761e;

        a(String str) {
            this.f9761e = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            AppFeatureProviderUtils.a(OplusBatteryApp.this.f9758e.getContentResolver(), OplusBatteryConstants.f19361m, AppFeatureCache.c.CACHE_AND_DB);
            PowerConsumptionOptimizationMessage.d(OplusBatteryApp.this.f9758e).e(this.f9761e);
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ThermalControlConfig.getInstance(OplusBatteryApp.this.f9758e);
            OGuardDataBase.v(OplusBatteryApp.this.f9758e).u().c(new AppPowerRecord());
            AppInfoManager.m(OplusBatteryApp.this.f9758e);
            StartupManagerAction.o(OplusBatteryApp.this.f9758e).w();
            LocalFileUtil.c().g();
            LocalFileUtil.c().j(OplusBatteryApp.this.getApplicationContext());
            LocalFileUtil.c().i(OplusBatteryApp.this.getApplicationContext());
            if (UserHandle.myUserId() == 0) {
                PowerSaveHelper.m(OplusBatteryApp.this.f9758e);
                PowerSaveConfig.f(OplusBatteryApp.this.f9758e);
                PhoneUsageTime.a(OplusBatteryApp.this.f9758e).b();
                PhoneScreenOnTime.a(OplusBatteryApp.this.f9758e).b();
            }
            GuardElfDataManager.d(OplusBatteryApp.this.f9758e);
            UploadDataUtil.S0(OplusBatteryApp.this.f9758e);
            OplusBatteryApp oplusBatteryApp = OplusBatteryApp.this;
            oplusBatteryApp.f9759f = ConfigUpdateUtil.n(oplusBatteryApp.f9758e);
            OplusBatteryApp.this.f9759f.G();
            try {
                OplusBatteryApp.this.startService(new Intent(OplusBatteryApp.this.f9758e, (Class<?>) OplusBatteryService.class));
            } catch (Exception e10) {
                LocalLog.c("OplusBatteryApp", "OplusBatteryApp: Can't start oplus battery service", e10);
            }
            if (AppFeature.c()) {
                try {
                    Settings.System.getIntForUser(OplusBatteryApp.this.getContentResolver(), "lcd_cabc_mode", 0);
                } catch (Settings.SettingNotFoundException unused) {
                    Settings.System.putIntForUser(OplusBatteryApp.this.getContentResolver(), "lcd_cabc_mode", 1, 0);
                }
            }
            PackageManager packageManager = OplusBatteryApp.this.getPackageManager();
            ComponentName componentName = new ComponentName(OplusBatteryApp.this.f9758e, (Class<?>) WirelessReverseControService.class);
            if (!y5.b.x()) {
                packageManager.setComponentEnabledSetting(componentName, 2, 1);
            } else {
                packageManager.setComponentEnabledSetting(componentName, 1, 1);
            }
            CustmizeAllowBgRunable.i(OplusBatteryApp.this.f9758e).k();
        }
    }

    private void d() {
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        SplitConfiguration.Builder newBuilder = SplitConfiguration.newBuilder();
        newBuilder.queryStartUp(true);
        newBuilder.downloader((Downloader) null);
        newBuilder.splitLoadMode(1);
        newBuilder.workProcesses((List) null);
        newBuilder.localFirst(true);
        newBuilder.customProvider((IProvider) null);
        newBuilder.build();
        Oms.onAttachBaseContext(context, newBuilder.build());
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        long currentTimeMillis = System.currentTimeMillis();
        String processName = Application.getProcessName();
        if ("com.oplus.battery:remote".equals(processName)) {
            LocalLog.l("OplusBatteryApp", "done for OTA,not need to init Application");
            return;
        }
        GuardElfContext.f(getApplicationContext());
        this.f9758e = getApplicationContext();
        d();
        this.f9760g = NotifyUtil.v(this.f9758e);
        if ("com.oplus.battery".equals(processName)) {
            this.f9760g.u();
        }
        if (processName == null) {
            LocalLog.a("OplusBatteryApp", "OplusBatteryApp: onCreate process is null!!!!!!");
            return;
        }
        LocalLog.a("OplusBatteryApp", "OplusBatteryApp: onCreate process=" + processName);
        TrackApi_20089.init(this.f9758e);
        if (!"com.oplus.battery:card".equals(processName)) {
            AppFeatureProviderUtils.a(this.f9758e.getContentResolver(), OplusBatteryConstants.f19361m, AppFeatureCache.c.CACHE_ONLY);
            AppFeature.b(this.f9758e);
            y5.b.a(this.f9758e);
            new Thread(new a(processName)).start();
            LocalLog.e(this.f9758e);
        }
        if ("com.oplus.battery".equals(processName) || "com.oplus.persist.system".equals(processName)) {
            Oms.onApplicationCreate(this);
            new Thread(new b()).start();
        }
        if (LocalLog.f()) {
            LocalLog.d("OplusBatteryApp", "create application done use time:" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }
}
