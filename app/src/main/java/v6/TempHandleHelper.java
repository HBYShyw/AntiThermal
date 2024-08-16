package v6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.thermalcontrol.ThermalControlUtils;
import s6.ThermalFactory;
import s6.ThermalManager;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: TempHandleHelper.java */
/* renamed from: v6.f, reason: use source file name */
/* loaded from: classes.dex */
public class TempHandleHelper implements IAffairCallback {

    /* renamed from: j, reason: collision with root package name */
    private static final String f19180j = "f";

    /* renamed from: e, reason: collision with root package name */
    private Context f19181e;

    /* renamed from: f, reason: collision with root package name */
    private UploadDataUtil f19182f;

    /* renamed from: g, reason: collision with root package name */
    private ThermalManager f19183g;

    /* renamed from: h, reason: collision with root package name */
    private int f19184h;

    /* renamed from: i, reason: collision with root package name */
    private int f19185i;

    /* compiled from: TempHandleHelper.java */
    /* renamed from: v6.f$b */
    /* loaded from: classes.dex */
    private static class b {

        /* renamed from: a, reason: collision with root package name */
        private static final TempHandleHelper f19186a = new TempHandleHelper();
    }

    public static TempHandleHelper a() {
        return b.f19186a;
    }

    private void b(Bundle bundle) {
        int i10 = bundle.getInt("highTempProtectSrcTemp");
        if (i10 >= 1000) {
            LocalLog.l(f19180j, "handleBatteryTemperature: Exception High Temperature " + i10);
            return;
        }
        this.f19183g.v(bundle);
    }

    public void c() {
        HandlerThread handlerThread = new HandlerThread("ThermalHandler");
        handlerThread.start();
        this.f19183g = ThermalFactory.a(this.f19181e, handlerThread.getLooper());
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        int intExtra;
        if (i10 != 204) {
            return;
        }
        int intExtra2 = intent.getIntExtra("level", 0);
        int intExtra3 = intent.getIntExtra("status", 0);
        int intExtra4 = intent.getIntExtra("plugged", 0);
        int intExtra5 = intent.getIntExtra("scale", 0);
        int intExtra6 = intent.getIntExtra("environment_temp_type", 0);
        ThermalControlUtils.getInstance(this.f19181e).setEnvironmentTemperatureType(intExtra6);
        int intExtra7 = intent.getIntExtra("temperature", 0);
        if (!AppFeature.x()) {
            intExtra = intent.getIntExtra("temperature", 0);
        } else {
            intExtra = intent.getIntExtra("battery_quiet_therm_type", 0);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("current", intExtra2);
        bundle.putInt("total", intExtra5);
        bundle.putInt("status", intExtra3);
        bundle.putInt("plugType", intExtra4);
        bundle.putInt("temperature", intExtra7);
        bundle.putInt("environment_temp_type", intExtra6);
        bundle.putInt("highTempProtectSrcTemp", intExtra);
        if (this.f19184h != intExtra7 || this.f19185i != intExtra2) {
            int intExtra8 = intent.getIntExtra("wireless_reverse_chg_type", 3);
            LocalLog.a(f19180j, "BatteryMonitor: battTemp  = " + intExtra7 + ", highTempProtectSrcTemp=" + intExtra + ", isBoardTempSrc=" + AppFeature.x() + ", level=" + intExtra2 + ", plugType=" + intExtra4 + ", status=" + intExtra3 + ", reverseType=" + intExtra8);
        }
        this.f19185i = intExtra2;
        this.f19184h = intExtra7;
        b(bundle);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
    }

    private TempHandleHelper() {
        this.f19184h = 0;
        this.f19185i = 0;
        Context c10 = GuardElfContext.e().c();
        this.f19181e = c10;
        this.f19182f = UploadDataUtil.S0(c10);
    }
}
