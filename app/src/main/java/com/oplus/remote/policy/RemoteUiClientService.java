package com.oplus.remote.policy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import b6.LocalLog;
import com.oplus.remote.policy.IRemoteClientInterface;
import f6.ChargeUtil;
import java.util.ArrayList;
import t8.PowerUsageManager;
import v4.GuardElfContext;

/* loaded from: classes2.dex */
public class RemoteUiClientService extends Service {

    /* renamed from: e, reason: collision with root package name */
    private Context f10455e = null;

    /* renamed from: f, reason: collision with root package name */
    private PowerUsageManager f10456f = null;

    /* renamed from: g, reason: collision with root package name */
    private PowerUsageManager.e f10457g = null;

    /* renamed from: h, reason: collision with root package name */
    private final Object f10458h = new Object();

    /* renamed from: i, reason: collision with root package name */
    private int f10459i = -1;

    /* renamed from: j, reason: collision with root package name */
    private boolean f10460j = false;

    /* renamed from: k, reason: collision with root package name */
    private IRemoteClientInterface.a f10461k = new a();

    /* loaded from: classes2.dex */
    class a extends IRemoteClientInterface.a {
        a() {
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public void a(IRemoteClientCallback iRemoteClientCallback) {
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public int h() {
            RemoteUiClientService remoteUiClientService;
            int i10;
            if (RemoteUiClientService.this.f10456f == null) {
                LocalLog.a("RemoteUiClientService", "PowerUsageManager mInstance is null !");
                return -1;
            }
            RemoteUiClientService.this.g();
            synchronized (RemoteUiClientService.this.f10458h) {
                RemoteUiClientService.this.f10456f.E();
                try {
                    try {
                        RemoteUiClientService.this.f10458h.wait(3000L);
                        LocalLog.a("RemoteUiClientService", "check advice over");
                        remoteUiClientService = RemoteUiClientService.this;
                    } catch (Throwable th) {
                        LocalLog.a("RemoteUiClientService", "check advice over");
                        int unused = RemoteUiClientService.this.f10459i;
                        throw th;
                    }
                } catch (InterruptedException e10) {
                    LocalLog.b("RemoteUiClientService", "check advice " + e10.toString());
                    LocalLog.a("RemoteUiClientService", "check advice over");
                    remoteUiClientService = RemoteUiClientService.this;
                }
                i10 = remoteUiClientService.f10459i;
            }
            return i10;
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public int o() {
            return ChargeUtil.n(RemoteUiClientService.this.f10455e);
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public Bundle p() {
            return null;
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public void r(IRemoteClientCallback iRemoteClientCallback) {
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public int w() {
            return 0;
        }

        @Override // com.oplus.remote.policy.IRemoteClientInterface
        public long y() {
            return 0L;
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
            synchronized (RemoteUiClientService.this.f10458h) {
                int[] y4 = RemoteUiClientService.this.f10456f.y(null, new ArrayList<>());
                RemoteUiClientService.this.f10459i = y4[0] == 1 ? y4[1] : 0;
                LocalLog.a("RemoteUiClientService", "issue size = " + RemoteUiClientService.this.f10459i);
                RemoteUiClientService.this.f10458h.notify();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        if (this.f10457g == null) {
            b bVar = new b();
            this.f10457g = bVar;
            this.f10456f.q(bVar);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        LocalLog.a("RemoteUiClientService", "onBind");
        return this.f10461k;
    }

    @Override // android.app.Service
    public void onCreate() {
        Context c10 = GuardElfContext.e().c();
        this.f10455e = c10;
        this.f10456f = PowerUsageManager.x(c10);
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.f10456f.D(this.f10457g);
        super.onDestroy();
    }
}
