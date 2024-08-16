package com.oplus.oms.split.splitload.extension.fakecomponents;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/* loaded from: classes.dex */
public class FakeService extends Service {
    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
