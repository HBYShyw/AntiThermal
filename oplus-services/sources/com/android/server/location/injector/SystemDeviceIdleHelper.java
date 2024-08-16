package com.android.server.location.injector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.PowerManager;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemDeviceIdleHelper extends DeviceIdleHelper {
    private final Context mContext;
    private PowerManager mPowerManager;
    private BroadcastReceiver mReceiver;
    private boolean mRegistrationRequired;
    private boolean mSystemReady;

    public SystemDeviceIdleHelper(Context context) {
        this.mContext = context;
    }

    public synchronized void onSystemReady() {
        this.mSystemReady = true;
        PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
        Objects.requireNonNull(powerManager);
        PowerManager powerManager2 = powerManager;
        this.mPowerManager = powerManager;
        onRegistrationStateChanged();
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper
    protected synchronized void registerInternal() {
        this.mRegistrationRequired = true;
        onRegistrationStateChanged();
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper
    protected synchronized void unregisterInternal() {
        this.mRegistrationRequired = false;
        onRegistrationStateChanged();
    }

    private void onRegistrationStateChanged() {
        BroadcastReceiver broadcastReceiver;
        if (this.mSystemReady) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                boolean z = this.mRegistrationRequired;
                if (z && this.mReceiver == null) {
                    BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() { // from class: com.android.server.location.injector.SystemDeviceIdleHelper.1
                        @Override // android.content.BroadcastReceiver
                        public void onReceive(Context context, Intent intent) {
                            SystemDeviceIdleHelper.this.notifyDeviceIdleChanged();
                        }
                    };
                    this.mContext.registerReceiver(broadcastReceiver2, new IntentFilter("android.os.action.DEVICE_IDLE_MODE_CHANGED"), null, FgThread.getHandler());
                    this.mReceiver = broadcastReceiver2;
                } else if (!z && (broadcastReceiver = this.mReceiver) != null) {
                    this.mReceiver = null;
                    this.mContext.unregisterReceiver(broadcastReceiver);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper
    public boolean isDeviceIdle() {
        Preconditions.checkState(this.mPowerManager != null);
        return this.mPowerManager.isDeviceIdleMode();
    }
}
