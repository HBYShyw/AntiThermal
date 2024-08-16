package com.oplus.storage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemProperties;
import b6.LocalLog;
import ha.StorageMonitorService;

/* loaded from: classes2.dex */
public class StorageMonitorDialogService extends Service {
    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i10, int i11) {
        if (intent == null) {
            return 2;
        }
        int intExtra = intent.getIntExtra("msg", -1);
        int intExtra2 = intent.getIntExtra("dataLevel", 0);
        long longExtra = intent.getLongExtra("dataFree", SystemProperties.getLong("sys.data.free.bytes", -1L));
        boolean booleanExtra = intent.getBooleanExtra("withSd", false);
        LocalLog.a("StorageMonitorDialogService", "onStartCommand , " + intExtra + ", " + intExtra2 + ", " + longExtra + ", " + booleanExtra);
        int intExtra3 = intent.getIntExtra("msg", -1);
        if (intExtra3 == 99) {
            StorageMonitorService.Y(getApplicationContext()).P();
            return 2;
        }
        if (intExtra3 == 100) {
            StorageMonitorService.Y(getApplicationContext()).R();
            return 2;
        }
        if (intExtra3 != 110 && intExtra3 != 111) {
            return 2;
        }
        StorageMonitorService.Y(getApplicationContext()).I0(intExtra, intExtra2, longExtra, booleanExtra);
        return 2;
    }
}
