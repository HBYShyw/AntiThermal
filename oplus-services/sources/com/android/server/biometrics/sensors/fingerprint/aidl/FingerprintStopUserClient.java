package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.os.IBinder;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.StopUserClient;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintStopUserClient extends StopUserClient<AidlSession> {
    private static final String TAG = "FingerprintStopUserClient";

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    public FingerprintStopUserClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, int i, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, StopUserClient.UserStoppedCallback userStoppedCallback) {
        super(context, supplier, iBinder, i, i2, biometricLogger, biometricContext, userStoppedCallback);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        startHalOperation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        try {
            getFreshDaemon().getSession().close();
        } catch (Exception e) {
            Slog.e(TAG, "Remote exception", e);
            getCallback().onClientFinished(this, false);
        }
    }
}
