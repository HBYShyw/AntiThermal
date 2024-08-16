package com.android.server.powerstats;

import android.content.Context;
import android.os.Message;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class PowerStatsLogTrigger {
    private static final String TAG = "PowerStatsLogTrigger";
    protected Context mContext;
    private PowerStatsLogger mPowerStatsLogger;

    /* JADX INFO: Access modifiers changed from: protected */
    public void logPowerStatsData(int i) {
        Message.obtain(this.mPowerStatsLogger, i).sendToTarget();
    }

    public PowerStatsLogTrigger(Context context, PowerStatsLogger powerStatsLogger) {
        this.mContext = context;
        this.mPowerStatsLogger = powerStatsLogger;
    }
}
