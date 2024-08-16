package com.android.server.stats;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import com.android.server.pm.DumpState;

@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class StatsHelper {
    private StatsHelper() {
    }

    public static void sendStatsdReadyBroadcast(Context context) {
        context.sendBroadcastAsUser(new Intent("android.app.action.STATSD_STARTED").addFlags(DumpState.DUMP_SERVICE_PERMISSIONS), UserHandle.SYSTEM, "android.permission.DUMP");
    }
}
