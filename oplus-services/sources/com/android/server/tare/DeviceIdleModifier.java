package com.android.server.tare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.IndentingPrintWriter;
import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DeviceIdleModifier extends Modifier {
    private static final boolean DEBUG;
    private static final String TAG;
    private final DeviceIdleTracker mDeviceIdleTracker = new DeviceIdleTracker();
    private final InternalResourceService mIrs;
    private final PowerManager mPowerManager;

    static {
        String str = "TARE-" + DeviceIdleModifier.class.getSimpleName();
        TAG = str;
        DEBUG = InternalResourceService.DEBUG || Log.isLoggable(str, 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceIdleModifier(InternalResourceService internalResourceService) {
        this.mIrs = internalResourceService;
        this.mPowerManager = (PowerManager) internalResourceService.getContext().getSystemService(PowerManager.class);
    }

    @Override // com.android.server.tare.Modifier
    public void setup() {
        this.mDeviceIdleTracker.startTracking(this.mIrs.getContext());
    }

    @Override // com.android.server.tare.Modifier
    public void tearDown() {
        this.mDeviceIdleTracker.stopTracking(this.mIrs.getContext());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.Modifier
    public long getModifiedCostToProduce(long j) {
        double d;
        if (this.mDeviceIdleTracker.mDeviceIdle) {
            d = 1.2d;
        } else {
            if (!this.mDeviceIdleTracker.mDeviceLightIdle) {
                return j;
            }
            d = 1.1d;
        }
        return (long) (j * d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.Modifier
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print("idle=");
        indentingPrintWriter.println(this.mDeviceIdleTracker.mDeviceIdle);
        indentingPrintWriter.print("lightIdle=");
        indentingPrintWriter.println(this.mDeviceIdleTracker.mDeviceLightIdle);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class DeviceIdleTracker extends BroadcastReceiver {
        private volatile boolean mDeviceIdle;
        private volatile boolean mDeviceLightIdle;
        private boolean mIsSetup = false;

        DeviceIdleTracker() {
        }

        void startTracking(Context context) {
            if (this.mIsSetup) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
            intentFilter.addAction("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
            context.registerReceiver(this, intentFilter);
            this.mDeviceIdle = DeviceIdleModifier.this.mPowerManager.isDeviceIdleMode();
            this.mDeviceLightIdle = DeviceIdleModifier.this.mPowerManager.isLightDeviceIdleMode();
            this.mIsSetup = true;
        }

        void stopTracking(Context context) {
            if (this.mIsSetup) {
                context.unregisterReceiver(this);
                this.mIsSetup = false;
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.os.action.DEVICE_IDLE_MODE_CHANGED".equals(action)) {
                if (this.mDeviceIdle != DeviceIdleModifier.this.mPowerManager.isDeviceIdleMode()) {
                    this.mDeviceIdle = DeviceIdleModifier.this.mPowerManager.isDeviceIdleMode();
                    DeviceIdleModifier.this.mIrs.onDeviceStateChanged();
                    return;
                }
                return;
            }
            if (!"android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED".equals(action) || this.mDeviceLightIdle == DeviceIdleModifier.this.mPowerManager.isLightDeviceIdleMode()) {
                return;
            }
            this.mDeviceLightIdle = DeviceIdleModifier.this.mPowerManager.isLightDeviceIdleMode();
            DeviceIdleModifier.this.mIrs.onDeviceStateChanged();
        }
    }
}
