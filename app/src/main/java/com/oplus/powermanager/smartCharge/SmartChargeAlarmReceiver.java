package com.oplus.powermanager.smartCharge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import w7.ChargeAuthenticationController;
import y8.SmartChargeAlarmSetter;
import y8.SmartChargeController;

/* loaded from: classes2.dex */
public class SmartChargeAlarmReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        action.hashCode();
        if (action.equals("com.oplus.app.smart.charge.income.one.day")) {
            SmartChargeController.t(context).u();
            SmartChargeAlarmSetter.b(context).c();
        } else if (action.equals("com.oplus.app.charge.authentication.per.day")) {
            ChargeAuthenticationController.b(context).d();
        }
    }
}
