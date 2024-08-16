package com.android.server.health;

import android.hardware.health.HealthInfo;
import android.hardware.health.IHealth;
import android.hardware.health.IHealthInfoCallback;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HealthRegCallbackAidl {
    private static final String TAG = "HealthRegCallbackAidl";
    private final IHealthInfoCallback mHalInfoCallback = new HalInfoCallback();
    private final HealthInfoCallback mServiceInfoCallback;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HealthRegCallbackAidl(HealthInfoCallback healthInfoCallback) {
        this.mServiceInfoCallback = healthInfoCallback;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void onRegistration(IHealth iHealth, IHealth iHealth2) {
        if (this.mServiceInfoCallback == null) {
            return;
        }
        Trace.traceBegin(524288L, "HealthUnregisterCallbackAidl");
        try {
            unregisterCallback(iHealth, this.mHalInfoCallback);
            Trace.traceEnd(524288L);
            Trace.traceBegin(524288L, "HealthRegisterCallbackAidl");
            try {
                registerCallback(iHealth2, this.mHalInfoCallback);
            } finally {
            }
        } finally {
        }
    }

    private static void unregisterCallback(IHealth iHealth, IHealthInfoCallback iHealthInfoCallback) {
        if (iHealth == null) {
            return;
        }
        try {
            iHealth.unregisterCallback(iHealthInfoCallback);
        } catch (RemoteException e) {
            Slog.w(TAG, "health: cannot unregister previous callback (transaction error): " + e.getMessage());
        }
    }

    private static void registerCallback(IHealth iHealth, IHealthInfoCallback iHealthInfoCallback) {
        try {
            iHealth.registerCallback(iHealthInfoCallback);
            try {
                iHealth.update();
            } catch (RemoteException e) {
                Slog.e(TAG, "health: cannot update after registering health info callback", e);
            }
        } catch (RemoteException e2) {
            Slog.e(TAG, "health: cannot register callback, framework may cease to receive updates on health / battery info!", e2);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class HalInfoCallback extends IHealthInfoCallback.Stub {
        public String getInterfaceHash() {
            return "d92c40b74b56341959d2ad70271145fdbd70b5c7";
        }

        public int getInterfaceVersion() {
            return 2;
        }

        private HalInfoCallback() {
        }

        public void healthInfoChanged(HealthInfo healthInfo) throws RemoteException {
            HealthRegCallbackAidl.this.mServiceInfoCallback.update(healthInfo);
        }
    }
}
