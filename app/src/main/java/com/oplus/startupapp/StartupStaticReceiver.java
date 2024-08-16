package com.oplus.startupapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import b6.LocalLog;
import u9.StartupManagerAction;

/* loaded from: classes2.dex */
public class StartupStaticReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            LocalLog.a("StartupManager", " receive boot_completed.");
            StartupManagerAction.o(context).execute(200, intent);
        }
    }
}
