package s6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import b6.LocalLog;
import c6.NotifyUtil;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.multiapp.OplusMultiAppManager;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.thermalcontrol.ThermalControlUtils;
import com.oplus.zoomwindow.OplusZoomWindowManager;
import f6.CommonUtil;
import f6.StatusUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import t6.PresenterFactory;
import x1.COUIAlertDialogBuilder;
import x8.DatabaseHelper;
import y5.AppFeature;

/* compiled from: ThermalHandler.java */
/* renamed from: s6.g, reason: use source file name */
/* loaded from: classes.dex */
public class ThermalHandler extends Handler {

    /* renamed from: a, reason: collision with root package name */
    private Context f18097a;

    /* renamed from: b, reason: collision with root package name */
    private Context f18098b;

    /* renamed from: c, reason: collision with root package name */
    private AlertDialog f18099c;

    /* renamed from: d, reason: collision with root package name */
    private AlertDialog f18100d;

    /* renamed from: e, reason: collision with root package name */
    private AlertDialog f18101e;

    /* renamed from: f, reason: collision with root package name */
    private int f18102f;

    /* renamed from: g, reason: collision with root package name */
    private int f18103g;

    /* renamed from: h, reason: collision with root package name */
    private int f18104h;

    /* renamed from: i, reason: collision with root package name */
    private int f18105i;

    /* renamed from: j, reason: collision with root package name */
    private int f18106j;

    /* renamed from: k, reason: collision with root package name */
    private SharedPreferences f18107k;

    /* renamed from: l, reason: collision with root package name */
    private SharedPreferences.Editor f18108l;

    /* renamed from: m, reason: collision with root package name */
    private PowerManager f18109m;

    /* renamed from: n, reason: collision with root package name */
    private PowerManager.WakeLock f18110n;

    /* renamed from: o, reason: collision with root package name */
    private ThermalManager f18111o;

    /* renamed from: p, reason: collision with root package name */
    private ThermalUploader f18112p;

    /* renamed from: q, reason: collision with root package name */
    private BroadcastReceiver f18113q;

    /* renamed from: r, reason: collision with root package name */
    private Handler f18114r;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ThermalHandler.java */
    /* renamed from: s6.g$a */
    /* loaded from: classes.dex */
    public class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            ThermalHandler.this.removeMessages(1022);
            LocalLog.l("ThermalHandler", "onDialogclick HighTemp now!");
            ThermalHandler.this.C();
        }
    }

    /* compiled from: ThermalHandler.java */
    /* renamed from: s6.g$b */
    /* loaded from: classes.dex */
    class b extends BroadcastReceiver {
        b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("oplus.intent.action.ACTION_THERMAL_NOTIFY_FIRST".equals(action)) {
                if (ThermalControlConfig.getInstance(ThermalHandler.this.f18097a).isThermalControlEnable()) {
                    PresenterFactory.a(ThermalHandler.this.f18097a, 1).d();
                } else {
                    PresenterFactory.a(ThermalHandler.this.f18097a, 4).d();
                }
                ThermalHandler.this.removeMessages(DataTypeConstants.USER_ACTION);
                ThermalHandler.this.f18108l.putBoolean("first_step_in", true);
                ThermalHandler.this.f18108l.putBoolean("first_step_notify", false);
                ThermalHandler.this.f18108l.commit();
                LocalLog.l("ThermalHandler", "notification clicked!");
                return;
            }
            if ("oplus.intent.action.ACTION_THERMAL_NOTIFY_SECOND".equals(action)) {
                ThermalHandler.this.f18108l.putBoolean("second_step_notify", false);
                ThermalHandler.this.f18108l.commit();
            }
        }
    }

    /* compiled from: ThermalHandler.java */
    /* renamed from: s6.g$c */
    /* loaded from: classes.dex */
    class c extends Handler {
        c(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i10 = message.what;
            if (i10 == 1016) {
                ThermalHandler.this.L();
                return;
            }
            if (i10 == 1017) {
                if (ThermalHandler.this.f18099c != null) {
                    ThermalHandler.this.f18099c.l(ThermalHandler.this.f18097a.getString(R.string.high_temperature_shutdown_message, Integer.valueOf(ThermalHandler.this.f18102f)));
                }
            } else if (i10 == 1021) {
                ThermalHandler.this.I();
            } else {
                if (i10 != 1023 || ThermalHandler.this.f18100d == null) {
                    return;
                }
                ThermalHandler.this.f18100d.i(-1).setText(ThermalHandler.this.f18097a.getString(R.string.high_temperature_dialog_auto, Integer.valueOf(ThermalHandler.this.f18103g)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ThermalHandler.java */
    /* renamed from: s6.g$d */
    /* loaded from: classes.dex */
    public class d implements DialogInterface.OnClickListener {
        d() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            ThermalHandler.this.removeMessages(DataTypeConstants.DYNAMIC_EVENT_TYPE);
            LocalLog.l("ThermalHandler", "onDialogclick shutdown now!");
            ThermalHandler.this.K();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ThermalHandler.java */
    /* renamed from: s6.g$e */
    /* loaded from: classes.dex */
    public class e implements DialogInterface.OnClickListener {
        e() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            LocalLog.l("ThermalHandler", "onDialogclick OK!");
        }
    }

    public ThermalHandler(Looper looper, Context context, ThermalManager thermalManager) {
        super(looper);
        this.f18106j = -1;
        this.f18113q = new b();
        this.f18114r = new c(Looper.getMainLooper());
        this.f18097a = context;
        this.f18098b = context.createDeviceProtectedStorageContext();
        this.f18112p = new ThermalUploader(this.f18097a, looper);
        this.f18111o = thermalManager;
        SharedPreferences sharedPreferences = this.f18098b.getSharedPreferences("high_temperature", 0);
        this.f18107k = sharedPreferences;
        this.f18108l = sharedPreferences.edit();
        PowerManager powerManager = (PowerManager) this.f18097a.getSystemService("power");
        this.f18109m = powerManager;
        PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(268435466, ":Thermal");
        this.f18110n = newWakeLock;
        newWakeLock.setReferenceCounted(false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("oplus.intent.action.ACTION_THERMAL_NOTIFY_FIRST");
        intentFilter.addAction("oplus.intent.action.ACTION_THERMAL_NOTIFY_SECOND");
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        this.f18097a.registerReceiver(this.f18113q, intentFilter, 2);
        if (Settings.System.getIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_protect", 0, 0) == 1) {
            NotifyUtil.v(this.f18097a).F();
        }
    }

    private void A() {
        LocalLog.l("ThermalHandler", "handleThirdStepIn");
        if (this.f18100d != null) {
            LocalLog.l("ThermalHandler", "handleThirdStepIn: HighTempDialog is not null");
            return;
        }
        this.f18108l.putString("hightemp_foreground_pkg", CommonUtil.y());
        this.f18108l.putBoolean("third_step_in", true);
        this.f18108l.commit();
        this.f18114r.sendEmptyMessage(1021);
        sendEmptyMessage(1025);
    }

    private void B() {
        LocalLog.l("ThermalHandler", "handleThirdStepOut");
        if (hasMessages(1022)) {
            removeMessages(1022);
        }
        AlertDialog alertDialog = this.f18100d;
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                this.f18100d.dismiss();
            }
            this.f18100d = null;
        }
        this.f18108l.putBoolean("third_step_in", false);
        this.f18108l.commit();
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_safety_state", 0, this.f18097a.getUserId());
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_safety_state", 0, 0);
        long currentTimeMillis = System.currentTimeMillis();
        long j10 = this.f18107k.getLong("hightemp_enter_time", 0L);
        if (currentTimeMillis <= j10 || j10 == 0) {
            return;
        }
        String string = this.f18107k.getString("hightemp_foreground_pkg", "UNKONWN");
        LocalLog.l("ThermalHandler", "foregroundPkg=" + string + ",currentRTCTime=" + currentTimeMillis + ",enterTime=" + j10);
        this.f18112p.e(string, (currentTimeMillis - j10) / 1000);
        this.f18108l.putLong("hightemp_enter_time", 0L);
        this.f18108l.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C() {
        LocalLog.l("ThermalHandler", "High Temperature mode!");
        this.f18108l.putLong("hightemp_enter_time", System.currentTimeMillis());
        this.f18108l.commit();
        this.f18111o.B();
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_safety_state", 1, this.f18097a.getUserId());
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_safety_state", 1, 0);
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        this.f18097a.startActivity(intent);
        o(this.f18097a);
    }

    private boolean D() {
        return AppFeature.z() || AppFeature.A() || "true".equals(SystemProperties.get("persist.sys.oplus.eng.full.aging"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void F(ThermalControlUtils.WindowInfo windowInfo, int i10, String str, String str2, String str3, String str4, DialogInterface dialogInterface, int i11) {
        n(windowInfo.uid, windowInfo.pkg, windowInfo.mode, i10);
        this.f18112p.d(windowInfo.pkg, "close", str, str2, str3, str4);
        this.f18106j = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void G(ThermalControlUtils.WindowInfo windowInfo, String str, String str2, String str3, String str4, DialogInterface dialogInterface, int i10) {
        this.f18112p.d(windowInfo.pkg, "cancel", str, str2, str3, str4);
        this.f18106j = -1;
        LocalLog.l("ThermalHandler", "onDialog click cancel");
    }

    private int H(String str) {
        int i10 = -1;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("setChargerCycle", String.class).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), str)).intValue();
            LocalLog.l("ThermalHandler", "setChargerCycle:" + str + ", return:" + i10);
            return i10;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            LocalLog.b("ThermalHandler", "setChargerCycle fail:" + e10);
            return i10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void I() {
        int j10 = this.f18111o.j();
        LocalLog.l("ThermalHandler", "highTempDialogTime=" + j10);
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(p(this.f18097a), f6.f.k(this.f18097a));
        cOUIAlertDialogBuilder.t(this.f18097a.getString(R.string.high_temperature_dialog_title, Integer.valueOf(j10)));
        cOUIAlertDialogBuilder.h(this.f18097a.getString(R.string.high_temperature_dialog_message, Integer.valueOf(j10)));
        cOUIAlertDialogBuilder.p(this.f18097a.getString(R.string.high_temperature_dialog_auto, Integer.valueOf(j10)), new a());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f18100d = a10;
        Window window = a10.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        attributes.type = 2009;
        window.setAttributes(attributes);
        this.f18100d.show();
        this.f18103g = j10;
        sendEmptyMessageDelayed(1022, 1000L);
        this.f18110n.acquire(j10 * 1000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: J, reason: merged with bridge method [inline-methods] */
    public void E(final ThermalControlUtils.WindowInfo windowInfo, final int i10, final String str, final String str2, final String str3, final String str4) {
        String str5;
        LocalLog.l("ThermalHandler", "showZoomWindowCloseDialog, " + windowInfo + " fg:" + str + " splitFg:" + str2 + " tempLevel:" + str3 + " ambient:" + str4);
        AlertDialog alertDialog = this.f18101e;
        if (alertDialog != null && alertDialog.isShowing() && windowInfo.uid == this.f18106j) {
            return;
        }
        if (CommonUtil.W(windowInfo.pkg) && UserHandle.getUserId(windowInfo.uid) == 999) {
            str5 = OplusMultiAppManager.getInstance().getMultiAppAlias(windowInfo.pkg);
        } else {
            String str6 = DatabaseHelper.u(this.f18097a).c().get(windowInfo.pkg);
            if (TextUtils.isEmpty(str6)) {
                PackageManager packageManager = this.f18097a.getPackageManager();
                try {
                    str5 = packageManager.getApplicationLabel(packageManager.getApplicationInfo(windowInfo.pkg, 128)).toString();
                } catch (PackageManager.NameNotFoundException e10) {
                    LocalLog.b("ThermalHandler", e10.toString());
                }
            }
            str5 = str6;
        }
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(p(this.f18097a), f6.f.k(this.f18097a));
        cOUIAlertDialogBuilder.t(this.f18097a.getString(R.string.zoom_window_close_dialog_message, str5));
        cOUIAlertDialogBuilder.p(this.f18097a.getString(R.string.zoom_window_close_confirm), new DialogInterface.OnClickListener() { // from class: s6.d
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i11) {
                ThermalHandler.this.F(windowInfo, i10, str, str2, str3, str4, dialogInterface, i11);
            }
        });
        cOUIAlertDialogBuilder.a0(R.string.zoom_window_close_cancel, new DialogInterface.OnClickListener() { // from class: s6.e
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i11) {
                ThermalHandler.this.G(windowInfo, str, str2, str3, str4, dialogInterface, i11);
            }
        });
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f18101e = a10;
        Window window = a10.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e11) {
            e11.printStackTrace();
        }
        attributes.type = 2009;
        window.setAttributes(attributes);
        this.f18101e.show();
        this.f18106j = windowInfo.uid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K() {
        LocalLog.l("ThermalHandler", "High Temperature Shutdown!");
        if (this.f18110n.isHeld()) {
            this.f18110n.release();
        }
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_protect", 0, this.f18097a.getUserId());
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_protect", 0, 0);
        LocalLog.l("ThermalHandler", "handleShutDown: reset hightemp SETTING_PROVIDER_KEY_HIGH_TEMPE_PROTECT");
        Intent intent = new Intent();
        intent.setAction("com.android.internal.intent.action.REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.putExtra("android.intent.extra.USER_REQUESTED_SHUTDOWN", true);
        intent.addFlags(268435456);
        this.f18097a.startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L() {
        int n10 = this.f18111o.n();
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(p(this.f18097a), f6.f.k(this.f18097a));
        cOUIAlertDialogBuilder.h(this.f18097a.getString(R.string.high_temperature_shutdown_message, Integer.valueOf(n10)));
        cOUIAlertDialogBuilder.e0(R.string.high_temperature_shutdown_now, new d());
        cOUIAlertDialogBuilder.a0(R.string.high_temperature_shutdown_auto, new e());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f18099c = a10;
        Window window = a10.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        attributes.type = 2009;
        window.setAttributes(attributes);
        this.f18099c.show();
        this.f18108l.putBoolean("final_step_in", true);
        this.f18108l.commit();
        this.f18102f = n10;
        sendEmptyMessageDelayed(DataTypeConstants.DYNAMIC_EVENT_TYPE, 1000L);
        this.f18110n.acquire(n10 * 1000);
    }

    private void n(int i10, String str, int i11, int i12) {
        if (i11 == 100) {
            OplusZoomWindowManager.getInstance().hideZoomWindow(14);
        }
        if (i12 == 1) {
            CommonUtil.a0(this.f18097a, i10, str, "hightemperature.manualforcestop");
        } else {
            if (i12 != 2) {
                return;
            }
            CommonUtil.e(this.f18097a, i10, str, "hightemperature.manualforcestop");
        }
    }

    private void o(Context context) {
        LocalLog.a("ThermalHandler", "enableMemoryClear");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("com.oplus.battery");
        arrayList.add("com.oplus.onetrace");
        Intent w10 = CommonUtil.w(context, new Intent("oplus.intent.action.REQUEST_APP_CLEAN_RUNNING"));
        StringBuilder sb2 = new StringBuilder();
        sb2.append("targetIntent == ");
        sb2.append(w10 == null);
        LocalLog.a("clear", sb2.toString());
        if (w10 == null) {
            return;
        }
        w10.putExtra("IsShowCleanFinishToast", false);
        w10.putExtra("clean_trash", false);
        w10.putExtra("clear_lock", true);
        w10.putExtra("clear_front", true);
        w10.putStringArrayListExtra("filterapplist", arrayList);
        w10.putExtra("caller_package", "com.oplus.battery.hightemperature");
        w10.putExtra("reason", "com.oplus.battery.hightemperature");
        context.startService(w10);
    }

    private Context p(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.Theme_Demo);
        COUIThemeOverlay.i().b(contextThemeWrapper);
        return contextThemeWrapper;
    }

    private void q() {
        if (this.f18107k.getBoolean("first_step_in", false)) {
            u();
            LocalLog.l("ThermalHandler", "AppLaunched:FirstStepOut");
        }
        if (this.f18107k.getBoolean("second_step_in", false)) {
            y();
            LocalLog.l("ThermalHandler", "AppLaunched: SecondStepOut");
        }
        if (this.f18107k.getBoolean("final_step_in", false)) {
            s();
            LocalLog.l("ThermalHandler", "AppLaunched: FinalStepOut");
        }
    }

    private void r() {
        int g6 = this.f18111o.g();
        LocalLog.l("ThermalHandler", "handleFinalStep. finalStepTemp=" + g6 + ", highTempProtectSrcTemp" + this.f18104h);
        if (this.f18104h < g6) {
            LocalLog.l("ThermalHandler", "handleFinalStep cancel");
            return;
        }
        if (this.f18099c != null) {
            LocalLog.l("ThermalHandler", "handleFinalStep: shutdownDialog is not null");
            return;
        }
        LocalLog.l("ThermalHandler", "handleFinalStep. shutdownTime=" + this.f18111o.n());
        this.f18114r.sendEmptyMessage(1016);
        this.f18112p.f(2, this.f18104h, this.f18105i);
    }

    private void s() {
        if (this.f18107k.getBoolean("final_step_in", false)) {
            removeMessages(DataTypeConstants.DYNAMIC_EVENT_TYPE);
            this.f18108l.putBoolean("final_step_in", false);
            this.f18108l.commit();
        }
    }

    private void t() {
        if (hasMessages(DataTypeConstants.USER_ACTION)) {
            LocalLog.l("ThermalHandler", "handleFirstStepIn: first step cancel. has MSG_FIRST_STEP_CLICK.");
            return;
        }
        if (this.f18107k.getBoolean("first_step_in", false)) {
            LocalLog.l("ThermalHandler", "handleFirstStepIn: first step cancel. first step has in.");
            return;
        }
        if (!ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable()) {
            int h10 = this.f18111o.h();
            if (this.f18104h < h10) {
                LocalLog.l("ThermalHandler", "handleFirstStepIn: first step cancel. temperature=" + this.f18104h + ", firstStepin" + h10);
                return;
            }
            LocalLog.l("ThermalHandler", "handleFirstStepIn temperature=" + this.f18104h + " firstStepin=" + h10);
        }
        this.f18108l.putBoolean("first_step_notify", true);
        this.f18108l.commit();
        int e10 = this.f18111o.e();
        if (ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable()) {
            PresenterFactory.a(this.f18097a, 1).a(e10);
        } else {
            PresenterFactory.a(this.f18097a, 4).a(e10);
        }
        sendEmptyMessageDelayed(1018, 1000L);
        sendEmptyMessageDelayed(DataTypeConstants.USER_ACTION, e10 * 1000);
        this.f18112p.f(0, this.f18104h, this.f18105i);
    }

    private void u() {
        boolean z10 = this.f18107k.getBoolean("first_step_in", false);
        if (hasMessages(1018)) {
            removeMessages(1018);
        }
        removeMessages(DataTypeConstants.USER_ACTION);
        if (z10) {
            if (ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable()) {
                PresenterFactory.a(this.f18097a, 1).b();
            } else {
                PresenterFactory.a(this.f18097a, 4).b();
            }
            this.f18108l.putBoolean("first_step_in", false);
            this.f18108l.commit();
            LocalLog.l("ThermalHandler", "handleFirstStepOut");
            return;
        }
        if (ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable()) {
            PresenterFactory.a(this.f18097a, 1).e();
        } else {
            PresenterFactory.a(this.f18097a, 4).e();
        }
    }

    private void v() {
        LocalLog.l("ThermalHandler", "handleFirstStepUpdate");
        this.f18108l.putBoolean("first_step_in", true);
        this.f18108l.commit();
        if (ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable()) {
            PresenterFactory.a(this.f18097a, 1).d();
            PresenterFactory.a(this.f18097a, 1).c();
        } else {
            PresenterFactory.a(this.f18097a, 4).d();
            PresenterFactory.a(this.f18097a, 4).c();
        }
    }

    private void w() {
        LocalLog.l("ThermalHandler", "handleHighTempDialogUpdate: mHighTempDialogUpdateTime" + this.f18103g);
        int i10 = this.f18103g;
        if (i10 > 1) {
            this.f18103g = i10 - 1;
            AlertDialog alertDialog = this.f18100d;
            if (alertDialog != null && alertDialog.isShowing()) {
                this.f18114r.sendEmptyMessage(1023);
            }
            sendEmptyMessageDelayed(1022, 1000L);
            return;
        }
        AlertDialog alertDialog2 = this.f18100d;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.f18100d.dismiss();
        }
        C();
    }

    private void x() {
        if (!ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable()) {
            if (this.f18104h < this.f18111o.m()) {
                LocalLog.l("ThermalHandler", "second step cancel");
                return;
            }
        }
        LocalLog.l("ThermalHandler", "handleSecondStepIn");
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_protect", 1, this.f18097a.getUserId());
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_protect", 1, 0);
        this.f18108l.putBoolean("second_step_in", true);
        this.f18108l.putBoolean("second_step_notify", true);
        this.f18108l.commit();
        if (ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable()) {
            PresenterFactory.a(this.f18097a, 1).d();
        } else {
            PresenterFactory.a(this.f18097a, 4).d();
        }
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "settings_thermal_high", 1, -2);
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "settings_thermal_high", 1, 0);
        if (!this.f18107k.getBoolean("third_step_in", false)) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(268435456);
            this.f18097a.startActivity(intent);
            o(this.f18097a);
        }
        if (ThermalControlConfig.getInstance(this.f18097a).isSafetyOptimizeEnabled()) {
            if (D()) {
                H("user_enable");
            } else {
                H("user_disable");
            }
        }
        NotifyUtil.v(this.f18097a).F();
        this.f18112p.f(1, this.f18104h, this.f18105i);
        sendEmptyMessage(1025);
    }

    private void y() {
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_protect", 0, this.f18097a.getUserId());
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "oplus_settings_hightemp_protect", 0, 0);
        LocalLog.l("ThermalHandler", "handleSecondStepOut: reset hightemp protection");
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "settings_thermal_high", 0, -2);
        Settings.System.putIntForUser(this.f18097a.getContentResolver(), "settings_thermal_high", 0, 0);
        LocalLog.l("ThermalHandler", "handleSecondStepOut: reset thermal high");
        H("user_enable");
        if (this.f18107k.getBoolean("second_step_in", false)) {
            this.f18108l.putBoolean("second_step_in", false);
            this.f18108l.commit();
            NotifyUtil.v(this.f18097a).l();
            LocalLog.l("ThermalHandler", "SecondStepOut");
        }
    }

    private void z() {
        LocalLog.l("ThermalHandler", "handleShutdownUpdate: mShutdownUpdateTime=" + this.f18102f);
        try {
            this.f18097a.unregisterReceiver(this.f18113q);
        } catch (Exception unused) {
            LocalLog.a("ThermalHandler", "Already Unregistered !");
        }
        int i10 = this.f18102f;
        if (i10 > 1) {
            this.f18102f = i10 - 1;
            AlertDialog alertDialog = this.f18099c;
            if (alertDialog != null && alertDialog.isShowing()) {
                this.f18114r.sendEmptyMessage(1017);
            }
            sendEmptyMessageDelayed(DataTypeConstants.DYNAMIC_EVENT_TYPE, 1000L);
            return;
        }
        AlertDialog alertDialog2 = this.f18099c;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.f18099c.dismiss();
        }
        K();
    }

    public void M(int i10, int i11) {
        this.f18104h = i10;
        this.f18105i = i11;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        long f10;
        switch (message.what) {
            case DataTypeConstants.USER_ACTION /* 1001 */:
                if (!ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable() && !this.f18109m.isInteractive()) {
                    this.f18109m.wakeUp(SystemClock.uptimeMillis(), 0, "HightempProtect");
                    sendEmptyMessageDelayed(DataTypeConstants.USER_ACTION, 1000L);
                    LocalLog.a("ThermalHandler", "MSG_FIRST_STEP_CLICK: is screen off. wakeup.");
                    return;
                }
                v();
                return;
            case DataTypeConstants.APP_LOG /* 1002 */:
                t();
                return;
            case DataTypeConstants.PAGE_VISIT /* 1003 */:
                u();
                return;
            case DataTypeConstants.EXCEPTION /* 1004 */:
                x();
                return;
            case DataTypeConstants.SPECIAL_APP_START /* 1005 */:
                y();
                return;
            case DataTypeConstants.COMMON /* 1006 */:
                r();
                return;
            case DataTypeConstants.DYNAMIC_EVENT_TYPE /* 1007 */:
                z();
                return;
            case DataTypeConstants.STATIC_EVENT_TYPE /* 1008 */:
            case DataTypeConstants.DEBUG_TYPE /* 1009 */:
            case DataTypeConstants.COMMON_BATCH /* 1010 */:
            case 1016:
            case 1017:
            case 1021:
            case 1023:
            default:
                return;
            case 1011:
                if (hasMessages(DataTypeConstants.APP_LOG) || this.f18107k.getBoolean("first_step_in", false)) {
                    return;
                }
                f10 = ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable() ? 0L : this.f18111o.f();
                sendEmptyMessageDelayed(DataTypeConstants.APP_LOG, f10);
                LocalLog.l("ThermalHandler", "MSG FIRST STEP time=" + f10);
                return;
            case 1012:
                if (hasMessages(DataTypeConstants.EXCEPTION) || this.f18107k.getBoolean("second_step_in", false)) {
                    return;
                }
                f10 = ThermalControlConfig.getInstance(this.f18097a).isThermalControlEnable() ? 0L : this.f18111o.f();
                sendEmptyMessageDelayed(DataTypeConstants.EXCEPTION, f10);
                LocalLog.l("ThermalHandler", "MSG SECOND STEP time=" + f10);
                return;
            case 1013:
                if (hasMessages(DataTypeConstants.COMMON) || this.f18107k.getBoolean("final_step_in", false)) {
                    return;
                }
                long f11 = this.f18111o.f();
                sendEmptyMessageDelayed(DataTypeConstants.COMMON, f11);
                LocalLog.l("ThermalHandler", "MSG FINAL STEP time=" + f11);
                return;
            case 1014:
                s();
                return;
            case 1015:
                q();
                return;
            case 1018:
                if (!hasMessages(DataTypeConstants.USER_ACTION) || this.f18107k.getBoolean("first_step_in", false)) {
                    return;
                }
                boolean b10 = StatusUtils.a(this.f18097a).b(this.f18097a);
                LocalLog.a("ThermalHandler", "handleFirstStepIn: MSG_FIRST_STEP_CHECK.");
                if (b10) {
                    sendEmptyMessageDelayed(1018, 1000L);
                    return;
                } else {
                    u();
                    return;
                }
            case DataTypeConstants.PERIOD_DATA /* 1019 */:
                A();
                return;
            case DataTypeConstants.SETTING_KEY /* 1020 */:
                B();
                return;
            case 1022:
                w();
                return;
            case 1024:
                final ThermalControlUtils.WindowInfo createFromBundle = ThermalControlUtils.WindowInfo.createFromBundle(message.getData());
                final int i10 = message.getData().getInt("close_policy");
                final String string = message.getData().getString("foreground_pkg");
                final String string2 = message.getData().getString("split_foreground_pkg");
                final String string3 = message.getData().getString("tempLevel");
                final String string4 = message.getData().getString("ambient_temperature");
                this.f18097a.getMainThreadHandler().post(new Runnable() { // from class: s6.f
                    @Override // java.lang.Runnable
                    public final void run() {
                        ThermalHandler.this.E(createFromBundle, i10, string, string2, string3, string4);
                    }
                });
                return;
            case 1025:
                AlertDialog alertDialog = this.f18101e;
                if (alertDialog == null || !alertDialog.isShowing()) {
                    return;
                }
                this.f18101e.dismiss();
                this.f18101e = null;
                return;
        }
    }
}
