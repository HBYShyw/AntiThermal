package v6;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import b6.LocalLog;
import com.oplus.deepsleep.ControllerCenter;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.statistics.record.StatIdManager;
import m9.AlarmSetter;
import p9.PowerMonitor;
import r9.SimplePowerMonitorUtils;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: AffairScreenOnAndOff.java */
/* renamed from: v6.d, reason: use source file name */
/* loaded from: classes.dex */
public class AffairScreenOnAndOff implements IAffairCallback {

    /* renamed from: i, reason: collision with root package name */
    private static Context f19160i = null;

    /* renamed from: j, reason: collision with root package name */
    private static final String f19161j = "d";

    /* renamed from: e, reason: collision with root package name */
    private ControllerCenter f19162e;

    /* renamed from: f, reason: collision with root package name */
    private AlarmSetter f19163f = null;

    /* renamed from: g, reason: collision with root package name */
    private Handler f19164g;

    /* renamed from: h, reason: collision with root package name */
    private HandlerThread f19165h;

    /* compiled from: AffairScreenOnAndOff.java */
    /* renamed from: v6.d$a */
    /* loaded from: classes.dex */
    private class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 11:
                    if (message.arg1 == 0 && SimplePowerMonitorUtils.f17654e && UserHandle.myUserId() == 0) {
                        PowerMonitor h10 = PowerMonitor.h(AffairScreenOnAndOff.f19160i);
                        h10.p();
                        h10.o();
                        if (SimplePowerMonitorUtils.f17656g) {
                            AffairScreenOnAndOff.this.f19163f.b();
                            AffairScreenOnAndOff.this.f19163f.f();
                        }
                    }
                    if (message.arg1 == 1 && UserHandle.myUserId() == 0 && SimplePowerMonitorUtils.f17655f) {
                        PowerMonitor h11 = PowerMonitor.h(AffairScreenOnAndOff.f19160i);
                        h11.q();
                        h11.n(true);
                        if (SimplePowerMonitorUtils.f17657h) {
                            AffairScreenOnAndOff.this.f19163f.c();
                            AffairScreenOnAndOff.this.f19163f.e();
                            return;
                        }
                        return;
                    }
                    return;
                case 12:
                    if (f6.f.t0(AffairScreenOnAndOff.f19160i) || message.arg1 != 1) {
                        return;
                    }
                    if (LocalLog.f()) {
                        LocalLog.a(AffairScreenOnAndOff.f19161j, "status change to INACTIVE after screen is off and battery is unplugged.");
                    }
                    AffairScreenOnAndOff.this.f19162e.onScreenOff(false);
                    return;
                case 13:
                    if (f6.f.t0(AffairScreenOnAndOff.f19160i) || message.arg1 != 1) {
                        return;
                    }
                    if (LocalLog.f()) {
                        LocalLog.a(AffairScreenOnAndOff.f19161j, "recheck charging.");
                    }
                    if (AffairScreenOnAndOff.this.k()) {
                        return;
                    }
                    AffairScreenOnAndOff.g().j(12, 5000L, 1);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AffairScreenOnAndOff.java */
    /* renamed from: v6.d$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private static final AffairScreenOnAndOff f19167a = new AffairScreenOnAndOff();
    }

    public static AffairScreenOnAndOff g() {
        f19160i = GuardElfContext.e().c();
        return b.f19167a;
    }

    private void i() {
        if (GuardElfContext.e().h().isInteractive()) {
            f6.f.P2(f19160i, true);
        } else {
            f6.f.P2(f19160i, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j(int i10, long j10, int i11) {
        Handler handler = this.f19164g;
        if (handler == null) {
            LocalLog.l(f19161j, "handler not init!");
            return;
        }
        if (handler.hasMessages(i10)) {
            this.f19164g.removeMessages(i10);
        }
        Message obtain = Message.obtain();
        obtain.what = i10;
        obtain.arg1 = i11;
        this.f19164g.sendMessageDelayed(obtain, j10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean k() {
        Intent registerReceiver = f19160i.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"), 2);
        if (registerReceiver != null) {
            int intExtra = registerReceiver.getIntExtra("plugged", 0);
            r0 = intExtra != 0;
            if (LocalLog.f()) {
                LocalLog.a(f19161j, "setScreenOffChargingState: plugType=" + intExtra);
            }
        } else if (!f6.f.x(f19160i)) {
            r0 = false;
        }
        if (LocalLog.f()) {
            LocalLog.a(f19161j, "setScreenOffChargingState: isCharging=" + r0);
        }
        if (f6.f.u(f19160i) != r0) {
            f6.f.Y1(f19160i, r0);
        }
        return r0;
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        ControllerCenter controllerCenter;
        if (i10 == 223) {
            String stringExtra = intent.getStringExtra(DeviceDomainManager.ARG_PKG);
            String stringExtra2 = intent.getStringExtra("job");
            String stringExtra3 = intent.getStringExtra("req");
            boolean booleanExtra = intent.getBooleanExtra("autoStop", false);
            if (LocalLog.f()) {
                LocalLog.a(f19161j, "action: " + intent.getAction() + ", pkg=" + stringExtra + ", req=" + stringExtra3 + ", job=" + stringExtra2 + ", autoStop=" + booleanExtra);
            }
            if (stringExtra == null || stringExtra3 == null || (controllerCenter = this.f19162e) == null) {
                return;
            }
            controllerCenter.onAppTrafficChange(stringExtra, stringExtra2, stringExtra3, booleanExtra);
            return;
        }
        if (i10 != 310) {
            switch (i10) {
                case EventType.SCENE_MODE_LOCATION /* 201 */:
                    boolean k10 = k();
                    f6.f.N2(f19160i, System.currentTimeMillis());
                    f6.f.P2(f19160i, false);
                    ControllerCenter controllerCenter2 = this.f19162e;
                    if (controllerCenter2 != null) {
                        controllerCenter2.onScreenOff(k10);
                        if (k10) {
                            j(13, StatIdManager.EXPIRE_TIME_MS, 1);
                        }
                    }
                    if (LocalLog.f()) {
                        LocalLog.a(f19161j, " SCREEN_OFF!!! ");
                    }
                    j(11, 5000L, 1);
                    return;
                case EventType.SCENE_MODE_AUDIO_OUT /* 202 */:
                    if (f6.f.t0(f19160i)) {
                        LocalLog.l(f19161j, "AFFAIR_SCREEN_ON_BROADCAST: getScreenOnState is screenOn, do nothing!!!");
                        return;
                    }
                    f6.f.P2(f19160i, true);
                    if (this.f19164g.hasMessages(13)) {
                        this.f19164g.removeMessages(13);
                    }
                    if (this.f19164g.hasMessages(12)) {
                        this.f19164g.removeMessages(12);
                    }
                    ControllerCenter controllerCenter3 = this.f19162e;
                    if (controllerCenter3 != null) {
                        controllerCenter3.onScreenOn();
                    }
                    if (LocalLog.f()) {
                        LocalLog.a(f19161j, " SCREEN_ON!!! ");
                    }
                    j(11, 5000L, 0);
                    return;
                case EventType.SCENE_MODE_AUDIO_IN /* 203 */:
                    ControllerCenter controllerCenter4 = this.f19162e;
                    if (controllerCenter4 != null) {
                        controllerCenter4.onShutDown();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        ControllerCenter controllerCenter5 = this.f19162e;
        if (controllerCenter5 != null) {
            controllerCenter5.onScreenOn();
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    public void h() {
        i();
        HandlerThread handlerThread = new HandlerThread("ScreenChangeAction");
        this.f19165h = handlerThread;
        handlerThread.start();
        this.f19164g = new a(this.f19165h.getLooper());
        if (SimplePowerMonitorUtils.f17657h) {
            this.f19163f = new AlarmSetter(f19160i);
        }
        registerAction();
        this.f19162e = ControllerCenter.getInstance(f19160i.getApplicationContext());
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_OUT);
        Affair.f().g(this, EventType.SCENE_MODE_LOCATION);
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_IN);
        Affair.f().g(this, 223);
        Affair.f().g(this, 310);
    }
}
