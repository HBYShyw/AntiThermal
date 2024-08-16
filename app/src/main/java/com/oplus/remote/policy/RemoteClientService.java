package com.oplus.remote.policy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.ArrayMap;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.remote.policy.IRemoteClientInterface;
import com.oplus.thermalcontrol.ThermalControlUtils;
import f6.ChargeUtil;
import java.util.ArrayList;
import r8.RemoteUtils;
import t8.PowerUsageManager;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;

/* loaded from: classes2.dex */
public class RemoteClientService extends Service implements IAffairCallback {

    /* renamed from: e, reason: collision with root package name */
    private final Object f10438e = new Object();

    /* renamed from: f, reason: collision with root package name */
    private PowerUsageManager f10439f = null;

    /* renamed from: g, reason: collision with root package name */
    private PowerUsageManager.e f10440g = null;

    /* renamed from: h, reason: collision with root package name */
    private Context f10441h = null;

    /* renamed from: i, reason: collision with root package name */
    private int f10442i = -1;

    /* renamed from: j, reason: collision with root package name */
    private ArrayMap<IBinder, c> f10443j = null;

    /* renamed from: k, reason: collision with root package name */
    private int f10444k = 0;

    /* renamed from: l, reason: collision with root package name */
    private RemoteCallbackList<IRemoteClientCallback> f10445l = null;

    /* renamed from: m, reason: collision with root package name */
    private boolean f10446m = false;

    /* renamed from: n, reason: collision with root package name */
    private int f10447n = 0;

    /* renamed from: o, reason: collision with root package name */
    private int f10448o = 1;

    /* renamed from: p, reason: collision with root package name */
    private boolean f10449p = false;

    /* renamed from: q, reason: collision with root package name */
    private IRemoteClientInterface.a f10450q = new a();

    /* loaded from: classes2.dex */
    class a extends IRemoteClientInterface.a {
        a() {
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public void a(IRemoteClientCallback iRemoteClientCallback) {
            synchronized (RemoteClientService.this.f10443j) {
                if (!RemoteClientService.this.f10443j.containsKey(iRemoteClientCallback.asBinder())) {
                    c cVar = new c(iRemoteClientCallback);
                    iRemoteClientCallback.asBinder().linkToDeath(cVar, 0);
                    RemoteClientService.this.f10443j.put(iRemoteClientCallback.asBinder(), cVar);
                    RemoteClientService.this.f10445l.register(iRemoteClientCallback);
                    LocalLog.a("RemoteClientService", "register callback " + iRemoteClientCallback.asBinder());
                } else {
                    LocalLog.a("RemoteClientService", "already register callback " + iRemoteClientCallback.asBinder());
                }
            }
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public int h() {
            RemoteClientService remoteClientService;
            int i10;
            if (RemoteClientService.this.f10439f == null) {
                LocalLog.a("RemoteClientService", "PowerUsageManager mInstance is null !");
                return -1;
            }
            RemoteClientService.this.o(RemoteClientService.this.f10439f.E());
            synchronized (RemoteClientService.this.f10438e) {
                LocalLog.a("RemoteClientService", "start check power save advice " + RemoteClientService.this.f10449p);
                while (!RemoteClientService.this.f10449p) {
                    try {
                        try {
                            RemoteClientService.this.f10438e.wait();
                        } catch (InterruptedException e10) {
                            LocalLog.b("RemoteClientService", "check advice " + e10.toString());
                            LocalLog.a("RemoteClientService", "check advice over");
                            remoteClientService = RemoteClientService.this;
                        }
                    } catch (Throwable th) {
                        LocalLog.a("RemoteClientService", "check advice over");
                        int unused = RemoteClientService.this.f10442i;
                        throw th;
                    }
                }
                RemoteClientService.this.f10449p = false;
                LocalLog.a("RemoteClientService", "check advice over");
                remoteClientService = RemoteClientService.this;
                i10 = remoteClientService.f10442i;
            }
            return i10;
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public int o() {
            return ChargeUtil.n(RemoteClientService.this.f10441h);
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public Bundle p() {
            return RemoteUtils.c();
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public void r(IRemoteClientCallback iRemoteClientCallback) {
            synchronized (RemoteClientService.this.f10443j) {
                if (RemoteClientService.this.f10443j.containsKey(iRemoteClientCallback.asBinder())) {
                    c cVar = (c) RemoteClientService.this.f10443j.remove(iRemoteClientCallback.asBinder());
                    RemoteClientService.this.f10445l.unregister(iRemoteClientCallback);
                    cVar.f10453a.asBinder().unlinkToDeath(cVar, 0);
                    LocalLog.a("RemoteClientService", "unregister callback " + iRemoteClientCallback.asBinder());
                } else {
                    LocalLog.a("RemoteClientService", "the callback not register " + iRemoteClientCallback.asBinder());
                }
            }
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public int w() {
            LocalLog.a("RemoteClientService", "charging " + RemoteClientService.this.f10446m + ", type " + RemoteClientService.this.f10444k);
            if (RemoteClientService.this.f10446m) {
                return RemoteClientService.this.f10448o == 5 ? -4 : -1;
            }
            return RemoteClientService.this.f10444k;
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public long y() {
            long s7;
            if (RemoteClientService.this.f10439f == null) {
                s7 = -100;
            } else {
                s7 = RemoteClientService.this.f10446m ? -1L : RemoteClientService.this.f10439f.s(RemoteClientService.this.f10439f.r(), -1);
            }
            LocalLog.a("RemoteClientService", "left time = " + s7);
            return s7;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class b implements PowerUsageManager.e {
        b() {
        }

        @Override // t8.PowerUsageManager.e
        public void d() {
        }

        @Override // t8.PowerUsageManager.e
        public void g(int i10) {
            synchronized (RemoteClientService.this.f10438e) {
                RemoteClientService.this.f10442i = RemoteClientService.this.f10439f.y(null, new ArrayList<>())[2];
                LocalLog.a("RemoteClientService", "issue size = " + RemoteClientService.this.f10442i);
                RemoteClientService.this.f10449p = true;
                RemoteClientService.this.f10438e.notify();
            }
        }
    }

    /* loaded from: classes2.dex */
    private class c implements IBinder.DeathRecipient {

        /* renamed from: a, reason: collision with root package name */
        IRemoteClientCallback f10453a;

        c(IRemoteClientCallback iRemoteClientCallback) {
            this.f10453a = iRemoteClientCallback;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (RemoteClientService.this.f10443j) {
                if (RemoteClientService.this.f10443j.containsKey(this.f10453a.asBinder())) {
                    RemoteClientService.this.f10443j.remove(this.f10453a.asBinder());
                    RemoteClientService.this.f10445l.unregister(this.f10453a);
                    LocalLog.a("RemoteClientService", "remove callback " + this.f10453a.asBinder());
                }
            }
        }
    }

    private void n(int i10) {
        if (this.f10446m && i10 != -1) {
            LocalLog.a("RemoteClientService", "is charging");
            return;
        }
        int beginBroadcast = this.f10445l.beginBroadcast();
        LocalLog.a("RemoteClientService", "list size = " + beginBroadcast + ", callback type " + i10);
        for (int i11 = 0; i11 < beginBroadcast; i11++) {
            try {
                this.f10445l.getBroadcastItem(i11).d(i10);
            } catch (RemoteException e10) {
                LocalLog.b("RemoteClientService", e10.toString());
            }
        }
        this.f10445l.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o(boolean z10) {
        if (z10 && this.f10440g == null) {
            b bVar = new b();
            this.f10440g = bVar;
            this.f10439f.q(bVar);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 != 204) {
            return;
        }
        int intExtra = intent.getIntExtra("plugged", 0);
        int intExtra2 = intent.getIntExtra("status", 1);
        LocalLog.a("RemoteClientService", "plug type " + intExtra + ", mPlugType " + this.f10447n + ", status " + intExtra2 + ", mStatus " + this.f10448o);
        if (intExtra != this.f10447n) {
            this.f10447n = intExtra;
            if (intExtra == 0) {
                this.f10446m = false;
                n(this.f10444k);
            } else {
                this.f10446m = true;
                n(-1);
            }
        }
        if (intExtra2 != this.f10448o) {
            this.f10448o = intExtra2;
            if (5 == intExtra2) {
                n(-4);
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        LocalLog.a("RemoteClientService", "onBind");
        return this.f10450q;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        LocalLog.a("RemoteClientService", "onCreate");
        Context c10 = GuardElfContext.e().c();
        this.f10441h = c10;
        this.f10439f = PowerUsageManager.x(c10);
        this.f10443j = new ArrayMap<>();
        this.f10445l = new RemoteCallbackList<>();
        boolean isCharging = ThermalControlUtils.getInstance(this.f10441h).isCharging();
        this.f10446m = isCharging;
        if (isCharging) {
            this.f10447n = 1;
        }
        if (Settings.System.getIntForUser(this.f10441h.getContentResolver(), "super_powersave_mode_state", 0, 0) == 1) {
            this.f10444k = -3;
        } else if (Settings.Global.getInt(this.f10441h.getContentResolver(), "low_power", 0) == 1) {
            this.f10444k = -2;
        }
        LocalLog.a("RemoteClientService", "charging = " + this.f10446m + ", type = " + this.f10444k);
        registerAction();
    }

    @Override // android.app.Service
    public void onDestroy() {
        p();
        LocalLog.a("RemoteClientService", "onDestroy");
        super.onDestroy();
    }

    public void p() {
        Affair.f().i(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().i(this, 901);
        Affair.f().i(this, 903);
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().g(this, 901);
        Affair.f().g(this, 903);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        if (i10 == 901) {
            if (bundle.getBoolean("powersave_state")) {
                if (this.f10444k != -3) {
                    this.f10444k = -2;
                    n(-2);
                    return;
                }
                return;
            }
            this.f10444k = 0;
            n(0);
            return;
        }
        if (i10 != 903) {
            return;
        }
        boolean z10 = Settings.Global.getInt(this.f10441h.getContentResolver(), "low_power", 0) == 1;
        if (bundle.getBoolean("s_powersave_state")) {
            this.f10444k = -3;
            n(-3);
        } else if (z10) {
            this.f10444k = -2;
            n(-2);
        } else {
            this.f10444k = 0;
            n(0);
        }
    }
}
