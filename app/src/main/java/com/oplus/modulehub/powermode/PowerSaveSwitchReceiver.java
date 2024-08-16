package com.oplus.modulehub.powermode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import b6.LocalLog;
import b7.PowerSaveMode;
import f6.f;
import x5.UploadDataUtil;

/* loaded from: classes.dex */
public class PowerSaveSwitchReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String str;
        if ("com.oplus.ACTION_SAVEMODE_CONTROL_SYSTEMUI_TO_POWERMANAGER".equals(intent.getAction())) {
            int i10 = -1;
            try {
                i10 = intent.getIntExtra("state", -1);
                str = intent.getStringExtra("source");
            } catch (Exception unused) {
                str = "unknown";
            }
            boolean z10 = true;
            if (1 != i10) {
                if (i10 != 0) {
                    LocalLog.b("PowerSaveSwitchReceiver", " invalid state=" + i10);
                    return;
                }
                z10 = false;
            }
            Context applicationContext = context.getApplicationContext();
            PowerSaveMode d10 = PowerSaveMode.d(context);
            UploadDataUtil.S0(context);
            boolean f02 = f.f0(applicationContext);
            LocalLog.l("PowerSaveSwitchReceiver", "shouldOn=" + z10 + ", source=" + str + ", isSaveModeon=" + f02);
            if (f02 != z10) {
                d10.g(z10, false, false);
            }
        }
    }
}
