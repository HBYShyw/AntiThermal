package n8;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.UserHandle;
import android.util.ArrayMap;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import f6.CommonUtil;
import java.util.HashMap;
import r9.SimplePowerMonitorUtils;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: PowerConsumptionOptimizationMessage.java */
/* renamed from: n8.g, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerConsumptionOptimizationMessage implements IAffairCallback {

    /* renamed from: i, reason: collision with root package name */
    private static volatile PowerConsumptionOptimizationMessage f15911i;

    /* renamed from: e, reason: collision with root package name */
    private Context f15912e;

    /* renamed from: f, reason: collision with root package name */
    private NotifyUtil f15913f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f15914g = null;

    /* renamed from: h, reason: collision with root package name */
    private BroadcastReceiver f15915h = new a();

    /* compiled from: PowerConsumptionOptimizationMessage.java */
    /* renamed from: n8.g$a */
    /* loaded from: classes2.dex */
    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LocalLog.a("PowerConsumptionOptimizationMessage", "action=" + action);
            if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                boolean z10 = !intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                if (intent.getData() != null) {
                    String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                    LocalLog.a("PowerConsumptionOptimizationMessage", "PowerConsumptionOptimizationMessage: remove=" + z10 + ", pkg=" + schemeSpecificPart);
                    if (z10) {
                        HashMap<String, Integer> B1 = f6.f.B1(PowerConsumptionOptimizationMessage.this.f15912e);
                        HashMap<String, Integer> x12 = f6.f.x1(PowerConsumptionOptimizationMessage.this.f15912e);
                        ArrayMap<String, Integer> A1 = f6.f.A1(PowerConsumptionOptimizationMessage.this.f15912e);
                        B1.remove(schemeSpecificPart);
                        x12.remove(schemeSpecificPart);
                        A1.remove(schemeSpecificPart);
                        f6.f.C3(B1, PowerConsumptionOptimizationMessage.this.f15912e);
                        f6.f.A3(x12, PowerConsumptionOptimizationMessage.this.f15912e);
                        f6.f.B3(A1, PowerConsumptionOptimizationMessage.this.f15912e);
                        PowerConsumptionOptimizationHelper.k(PowerConsumptionOptimizationMessage.this.f15912e).s(schemeSpecificPart);
                        return;
                    }
                    return;
                }
                return;
            }
            if (!"android.intent.action.PACKAGE_ADDED".equals(action) && !"android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(action)) {
                if ("android.intent.action.LOCALE_CHANGED".equals(action) && CommonUtil.T(PowerConsumptionOptimizationMessage.this.f15912e)) {
                    LocalLog.b("PowerConsumptionOptimizationMessage", "skip boot reg");
                    return;
                }
                return;
            }
            if (intent.getData() != null) {
                String schemeSpecificPart2 = intent.getData().getSchemeSpecificPart();
                LocalLog.a("PowerConsumptionOptimizationMessage", "PowerConsumptionOptimizationMessage: add pkg=" + schemeSpecificPart2);
                try {
                    PowerConsumptionOptimizationHelper.k(PowerConsumptionOptimizationMessage.this.f15912e).f(PowerConsumptionOptimizationMessage.this.f15912e.getPackageManager().getApplicationInfo(schemeSpecificPart2, 128));
                } catch (PackageManager.NameNotFoundException e10) {
                    e10.printStackTrace();
                }
                if ("com.oplus.battery:ui".equals(PowerConsumptionOptimizationMessage.this.f15914g) || !SimplePowerMonitorUtils.f17653d || UserHandle.myUserId() != 0 || intent.getDataString() == null) {
                    return;
                }
                String dataString = intent.getDataString();
                if (dataString.length() > 8) {
                    String substring = dataString.substring(8);
                    if (substring.length() > 0) {
                        int l10 = SimplePowerMonitorUtils.l(substring, PowerConsumptionOptimizationMessage.this.f15912e);
                        LocalLog.a("PowerConsumptionOptimizationMessage", "SPM_installed:" + substring + " programe    " + l10);
                        if (SimplePowerMonitorUtils.s(substring, l10, PowerConsumptionOptimizationMessage.this.f15912e)) {
                            LocalLog.a("PowerConsumptionOptimizationMessage", "installed:" + substring + " isNeedMonitorApp: true");
                            SimplePowerMonitorUtils.a(substring, l10);
                        }
                    }
                }
            }
        }
    }

    public PowerConsumptionOptimizationMessage(Context context) {
        this.f15912e = null;
        this.f15912e = context;
    }

    public static PowerConsumptionOptimizationMessage d(Context context) {
        if (f15911i == null) {
            synchronized (PowerConsumptionOptimizationMessage.class) {
                if (f15911i == null) {
                    f15911i = new PowerConsumptionOptimizationMessage(context);
                }
            }
        }
        return f15911i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(String str) {
        PowerConsumptionOptimizationHelper.k(this.f15912e).w(str);
    }

    public void e(String str) {
        this.f15913f = NotifyUtil.v(this.f15912e);
        this.f15914g = str;
        registerAction();
        f6.f.y1(this.f15912e);
        IntentFilter intentFilter = new IntentFilter();
        IntentFilter intentFilter2 = new IntentFilter();
        HandlerThread handlerThread = new HandlerThread("PowerConsumptionOptimizationMessage");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        intentFilter2.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addDataScheme("package");
        this.f15912e.registerReceiver(this.f15915h, intentFilter, "", handler, 2);
        this.f15912e.registerReceiver(this.f15915h, intentFilter2, "oplus.permission.OPLUS_COMPONENT_SAFE", handler, 2);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 == 217) {
            if (CommonUtil.T(this.f15912e)) {
                LocalLog.b("PowerConsumptionOptimizationMessage", "skip boot reg");
                return;
            } else {
                PowerConsumptionOptimizationHelper.k(this.f15912e).x();
                return;
            }
        }
        if (i10 == 1403) {
            String stringExtra = intent.getStringExtra("pkgName");
            boolean booleanExtra = intent.getBooleanExtra("isNotificate", false);
            LocalLog.a("PowerConsumptionOptimizationMessage", "AFFAIR_OPTIMZATION_NO_MORE: isNotificate =  " + booleanExtra);
            f6.f.o2(this.f15912e, stringExtra, booleanExtra);
            this.f15913f.k(stringExtra);
            return;
        }
        if (i10 == 227) {
            final String stringExtra2 = intent.getStringExtra("pkgName");
            this.f15913f.m(stringExtra2);
            CommonUtil.c(this.f15912e);
            ((ActivityManager) this.f15912e.getSystemService("activity")).forceStopPackage(stringExtra2);
            LocalLog.a("PowerConsumptionOptimizationMessage", "force stop " + stringExtra2);
            this.f15912e.getMainThreadHandler().post(new Runnable() { // from class: n8.f
                @Override // java.lang.Runnable
                public final void run() {
                    PowerConsumptionOptimizationMessage.this.f(stringExtra2);
                }
            });
            return;
        }
        if (i10 != 228) {
            return;
        }
        String stringExtra3 = intent.getStringExtra("pkgName");
        this.f15913f.m(stringExtra3);
        CommonUtil.c(this.f15912e);
        ((ActivityManager) this.f15912e.getSystemService("activity")).forceStopPackage(stringExtra3);
        LocalLog.a("PowerConsumptionOptimizationMessage", "force stop " + stringExtra3);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 227);
        Affair.f().g(this, 228);
        Affair.f().g(this, 1403);
        Affair.f().g(this, EventType.SCENE_MODE_LEARNING);
        Affair.f().g(this, 1102);
    }
}
