package com.oplus.deepsleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;

/* loaded from: classes.dex */
public class RestoreNetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "RestoreNetworkReq";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            String str = "";
            String action = intent.getAction();
            ControllerCenter controllerCenter = ControllerCenter.getInstance(context.getApplicationContext());
            LocalLog.a(TAG, "action: " + action);
            if ("oplus.intent.action.DEEP_SLEEP_RESTORE_NETWORK".equals(action)) {
                str = intent.getStringExtra(DeviceDomainManager.ARG_PKG);
            } else if ("oplus.intent.action.idle_maintenance".equals(action)) {
                str = "oplus.intent.action.idle_maintenance";
            }
            if (controllerCenter != null) {
                controllerCenter.onRestoreNetworkReq(str);
            }
        } catch (Exception unused) {
        }
    }
}
