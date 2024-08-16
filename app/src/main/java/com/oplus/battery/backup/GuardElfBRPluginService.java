package com.oplus.battery.backup;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.oplus.backup.sdk.component.BRPluginService;

/* loaded from: classes.dex */
public class GuardElfBRPluginService extends BRPluginService {
    private static final String TAG = "GuardElfBRPluginService";

    @Override // com.oplus.backup.sdk.component.BRPluginService, android.app.Service
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return super.onBind(intent);
    }

    @Override // com.oplus.backup.sdk.component.BRPluginService, android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // com.oplus.backup.sdk.component.BRPluginService, android.app.Service
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
