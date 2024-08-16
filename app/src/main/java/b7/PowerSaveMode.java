package b7;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import b6.LocalLog;
import f6.f;

/* compiled from: PowerSaveMode.java */
/* renamed from: b7.b, reason: use source file name */
/* loaded from: classes.dex */
public class PowerSaveMode {

    /* renamed from: e, reason: collision with root package name */
    private static PowerSaveMode f4590e;

    /* renamed from: a, reason: collision with root package name */
    private Context f4591a;

    /* renamed from: b, reason: collision with root package name */
    private a f4592b;

    /* renamed from: c, reason: collision with root package name */
    private PowerManager f4593c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f4594d;

    /* compiled from: PowerSaveMode.java */
    /* renamed from: b7.b$a */
    /* loaded from: classes.dex */
    private class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i10 = message.what;
            if (i10 != 1) {
                if (i10 == 2) {
                    PowerSaveMode powerSaveMode = PowerSaveMode.this;
                    powerSaveMode.i(powerSaveMode.f4591a);
                    return;
                }
                return;
            }
            if (message.getData() == null) {
                return;
            }
            boolean z10 = message.getData().getBoolean("powerSaveEnable");
            boolean z11 = message.getData().getBoolean("recover");
            PowerSaveMode powerSaveMode2 = PowerSaveMode.this;
            powerSaveMode2.f(powerSaveMode2.f4591a, z10, z11);
        }
    }

    private PowerSaveMode(Context context) {
        this.f4591a = context.getApplicationContext();
        this.f4593c = (PowerManager) context.getSystemService("power");
        HandlerThread handlerThread = new HandlerThread("powerSaveMode");
        handlerThread.start();
        this.f4592b = new a(handlerThread.getLooper());
        if (-1 == Settings.System.getIntForUser(this.f4591a.getContentResolver(), "power_save_backlight_switch_state", -1, 0)) {
            int i10 = !y5.b.D() ? 1 : 0;
            Settings.System.putIntForUser(this.f4591a.getContentResolver(), "power_save_backlight_switch_state", i10, 0);
            LocalLog.l("owerSaveMode", "initial powersave backlight state = " + i10);
        }
        this.f4594d = Settings.Global.getInt(this.f4591a.getContentResolver(), "low_power", 0) == 1;
        LocalLog.a("owerSaveMode", "lastHandledState = " + this.f4594d);
    }

    public static synchronized PowerSaveMode d(Context context) {
        PowerSaveMode powerSaveMode;
        synchronized (PowerSaveMode.class) {
            if (f4590e == null) {
                f4590e = new PowerSaveMode(context);
            }
            powerSaveMode = f4590e;
        }
        return powerSaveMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(Context context, boolean z10, boolean z11) {
        if (f.n1(context) && z10) {
            LocalLog.a("owerSaveMode", "setOsPowerSaveMode: FeaturePowerSaveDisabled is true.");
            return;
        }
        if (f.f0(this.f4591a) == z10) {
            LocalLog.a("owerSaveMode", "setOsPowerSaveMode: power save mode is same. enable=" + z10);
            return;
        }
        LocalLog.a("owerSaveMode", "setOsPowerSaveMode: enable=" + z10);
        this.f4593c.setPowerSaveModeEnabled(z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(Context context) {
        boolean z10 = Settings.Global.getInt(this.f4591a.getContentResolver(), "low_power", 0) == 1;
        if (this.f4594d == z10) {
            LocalLog.l("owerSaveMode", "setScreenRefreshRateInternal: ignore isPowerSaveMode = " + z10);
            return;
        }
        this.f4594d = z10;
        boolean z11 = f.i0(context) == 1;
        int h02 = f.h0(context);
        if (z10) {
            Settings.System.putIntForUser(context.getContentResolver(), "power_save_pre_refresh_state", h02, 0);
        } else if (f.h0(context) != 2) {
            Settings.System.putIntForUser(context.getContentResolver(), "power_save_pre_refresh_state", f.h0(context), 0);
        }
        LocalLog.l("owerSaveMode", "setScreenRefreshRateInternal: isSupportRefresh = " + y5.b.H() + ", state = " + z10 + ", currentRefreshState = " + h02 + ", refreshSwitchState = " + z11);
        if (y5.b.H()) {
            if (z11) {
                f.D2(context, z10 ? 2 : Settings.System.getIntForUser(context.getContentResolver(), "power_save_pre_refresh_state", 0, 0));
            } else {
                f.D2(context, Settings.System.getIntForUser(context.getContentResolver(), "power_save_pre_refresh_state", 0, 0));
            }
        }
    }

    public void e() {
    }

    public boolean g(boolean z10, boolean z11, boolean z12) {
        Message obtainMessage = this.f4592b.obtainMessage();
        obtainMessage.what = 1;
        Bundle bundle = new Bundle();
        bundle.putBoolean("powerSaveEnable", z10);
        bundle.putBoolean("startup", z11);
        bundle.putBoolean("recover", z12);
        obtainMessage.setData(bundle);
        this.f4592b.sendMessage(obtainMessage);
        return true;
    }

    public void h(boolean z10) {
        if (this.f4591a.getUserId() != 0) {
            LocalLog.l("owerSaveMode", "not user system, don't set refresh");
            return;
        }
        if (this.f4592b.hasMessages(2)) {
            this.f4592b.removeMessages(2);
        }
        this.f4592b.sendEmptyMessageDelayed(2, 2000L);
        LocalLog.a("owerSaveMode", "setScreenRefreshRate: state = " + z10);
    }
}
