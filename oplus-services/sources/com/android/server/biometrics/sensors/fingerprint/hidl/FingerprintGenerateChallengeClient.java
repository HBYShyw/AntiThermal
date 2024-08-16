package com.android.server.biometrics.sensors.fingerprint.hidl;

import android.content.Context;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.GenerateChallengeClient;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintGenerateChallengeClient extends GenerateChallengeClient<IBiometricsFingerprint> {
    private static final String TAG = "FingerprintGenerateChallengeClient";

    /* JADX INFO: Access modifiers changed from: package-private */
    public FingerprintGenerateChallengeClient(Context context, Supplier<IBiometricsFingerprint> supplier, IBinder iBinder, ClientMonitorCallbackConverter clientMonitorCallbackConverter, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, i, str, i2, biometricLogger, biometricContext);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        try {
            long preEnroll = getFreshDaemon().preEnroll();
            try {
                ClientMonitorCallbackConverter listener = getListener();
                if (listener == null) {
                    Slog.e(TAG, "Listener is null in onChallengeGenerated");
                    this.mCallback.onClientFinished(this, false);
                } else {
                    listener.onChallengeGenerated(getSensorId(), getTargetUserId(), preEnroll);
                    this.mCallback.onClientFinished(this, true);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception", e);
                this.mCallback.onClientFinished(this, false);
            }
        } catch (RemoteException e2) {
            Slog.e(TAG, "preEnroll failed", e2);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
