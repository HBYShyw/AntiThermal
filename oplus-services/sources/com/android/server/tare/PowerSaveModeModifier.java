package com.android.server.tare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerSaveModeModifier extends Modifier {
    private static final boolean DEBUG;
    private static final String TAG;
    private final InternalResourceService mIrs;
    private final PowerSaveModeTracker mPowerSaveModeTracker = new PowerSaveModeTracker();

    static {
        String str = "TARE-" + PowerSaveModeModifier.class.getSimpleName();
        TAG = str;
        DEBUG = InternalResourceService.DEBUG || Log.isLoggable(str, 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerSaveModeModifier(InternalResourceService internalResourceService) {
        this.mIrs = internalResourceService;
    }

    @Override // com.android.server.tare.Modifier
    public void setup() {
        this.mPowerSaveModeTracker.startTracking(this.mIrs.getContext());
    }

    @Override // com.android.server.tare.Modifier
    public void tearDown() {
        this.mPowerSaveModeTracker.stopTracking(this.mIrs.getContext());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.Modifier
    public long getModifiedCostToProduce(long j) {
        double d;
        if (this.mPowerSaveModeTracker.mPowerSaveModeEnabled) {
            d = 1.5d;
        } else {
            if (!this.mPowerSaveModeTracker.mPowerSaveModeEnabled) {
                return j;
            }
            d = 1.25d;
        }
        return (long) (j * d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.Modifier
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print("power save=");
        indentingPrintWriter.println(this.mPowerSaveModeTracker.mPowerSaveModeEnabled);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class PowerSaveModeTracker extends BroadcastReceiver {
        private boolean mIsSetup;
        private final PowerManager mPowerManager;
        private volatile boolean mPowerSaveModeEnabled;

        private PowerSaveModeTracker() {
            this.mIsSetup = false;
            this.mPowerManager = (PowerManager) PowerSaveModeModifier.this.mIrs.getContext().getSystemService(PowerManager.class);
        }

        public void startTracking(Context context) {
            if (this.mIsSetup) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            context.registerReceiver(this, intentFilter);
            this.mPowerSaveModeEnabled = this.mPowerManager.isPowerSaveMode();
            this.mIsSetup = true;
        }

        public void stopTracking(Context context) {
            if (this.mIsSetup) {
                context.unregisterReceiver(this);
                this.mIsSetup = false;
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.os.action.POWER_SAVE_MODE_CHANGED".equals(intent.getAction())) {
                boolean isPowerSaveMode = this.mPowerManager.isPowerSaveMode();
                if (PowerSaveModeModifier.DEBUG) {
                    Slog.d(PowerSaveModeModifier.TAG, "Power save mode changed to " + isPowerSaveMode + ", fired @ " + SystemClock.elapsedRealtime());
                }
                if (this.mPowerSaveModeEnabled != isPowerSaveMode) {
                    this.mPowerSaveModeEnabled = isPowerSaveMode;
                    PowerSaveModeModifier.this.mIrs.onDeviceStateChanged();
                }
            }
        }
    }
}
