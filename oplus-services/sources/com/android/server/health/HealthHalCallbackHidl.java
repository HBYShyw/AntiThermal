package com.android.server.health;

import android.hardware.health.Translate;
import android.hardware.health.V2_0.HealthInfo;
import android.hardware.health.V2_0.IHealth;
import android.hardware.health.V2_0.Result;
import android.hardware.health.V2_1.IHealthInfoCallback;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Slog;
import com.android.server.health.HealthServiceWrapperHidl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class HealthHalCallbackHidl extends IHealthInfoCallback.Stub implements HealthServiceWrapperHidl.Callback {
    private static final String TAG = HealthHalCallbackHidl.class.getSimpleName();
    private HealthInfoCallback mCallback;

    private static void traceBegin(String str) {
        Trace.traceBegin(524288L, str);
    }

    private static void traceEnd() {
        Trace.traceEnd(524288L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HealthHalCallbackHidl(HealthInfoCallback healthInfoCallback) {
        this.mCallback = healthInfoCallback;
    }

    public void healthInfoChanged(HealthInfo healthInfo) {
        android.hardware.health.V2_1.HealthInfo healthInfo2 = new android.hardware.health.V2_1.HealthInfo();
        healthInfo2.legacy = healthInfo;
        healthInfo2.batteryCapacityLevel = -1;
        healthInfo2.batteryChargeTimeToFullNowSeconds = -1L;
        this.mCallback.update(Translate.h2aTranslate(healthInfo2));
    }

    public void healthInfoChanged_2_1(android.hardware.health.V2_1.HealthInfo healthInfo) {
        this.mCallback.update(Translate.h2aTranslate(healthInfo));
    }

    @Override // com.android.server.health.HealthServiceWrapperHidl.Callback
    public void onRegistration(IHealth iHealth, IHealth iHealth2, String str) {
        int registerCallback;
        if (iHealth2 == null) {
            return;
        }
        traceBegin("HealthUnregisterCallback");
        if (iHealth != null) {
            try {
                try {
                    int unregisterCallback = iHealth.unregisterCallback(this);
                    if (unregisterCallback != 0) {
                        Slog.w(TAG, "health: cannot unregister previous callback: " + Result.toString(unregisterCallback));
                    }
                } catch (RemoteException e) {
                    Slog.w(TAG, "health: cannot unregister previous callback (transaction error): " + e.getMessage());
                }
            } finally {
            }
        }
        traceEnd();
        traceBegin("HealthRegisterCallback");
        try {
            try {
                registerCallback = iHealth2.registerCallback(this);
            } catch (RemoteException e2) {
                Slog.e(TAG, "health: cannot register callback (transaction error): " + e2.getMessage());
            }
            if (registerCallback != 0) {
                Slog.w(TAG, "health: cannot register callback: " + Result.toString(registerCallback));
                return;
            }
            iHealth2.update();
        } finally {
        }
    }
}
