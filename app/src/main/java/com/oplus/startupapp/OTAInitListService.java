package com.oplus.startupapp;

import aa.StartupDataUtils;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import b6.LocalLog;

/* loaded from: classes2.dex */
public class OTAInitListService extends Service {

    /* loaded from: classes2.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            StartupDataUtils.h(OTAInitListService.this.getApplicationContext()).m();
            OTAInitListService.this.stopSelf();
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        LocalLog.l("StartupManager:OTAInitListService", "onCreate");
        super.onCreate();
        new Thread(new a()).start();
    }

    @Override // android.app.Service
    public void onDestroy() {
        LocalLog.l("StartupManager:OTAInitListService", "onDestroy");
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i10, int i11) {
        return 1;
    }
}
