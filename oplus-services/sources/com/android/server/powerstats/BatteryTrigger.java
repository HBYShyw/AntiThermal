package com.android.server.powerstats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class BatteryTrigger extends PowerStatsLogTrigger {
    private static final boolean DEBUG = false;
    private static final String TAG = "BatteryTrigger";
    private int mBatteryLevel;
    private final BroadcastReceiver mBatteryLevelReceiver;

    public BatteryTrigger(Context context, PowerStatsLogger powerStatsLogger, boolean z) {
        super(context, powerStatsLogger);
        this.mBatteryLevel = 0;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.powerstats.BatteryTrigger.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                    int intExtra = intent.getIntExtra("level", 0);
                    if (intExtra < BatteryTrigger.this.mBatteryLevel) {
                        BatteryTrigger.this.logPowerStatsData(0);
                    }
                    BatteryTrigger.this.mBatteryLevel = intExtra;
                }
            }
        };
        this.mBatteryLevelReceiver = broadcastReceiver;
        if (z) {
            Intent registerReceiver = this.mContext.registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (registerReceiver != null) {
                this.mBatteryLevel = registerReceiver.getIntExtra("level", 0);
                return;
            }
            Slog.w(TAG, "batteryStatus is null and mBatteryLevel is " + this.mBatteryLevel);
        }
    }
}
