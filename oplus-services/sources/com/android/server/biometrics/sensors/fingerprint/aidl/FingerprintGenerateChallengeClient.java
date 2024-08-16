package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.GenerateChallengeClient;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintGenerateChallengeClient extends GenerateChallengeClient<AidlSession> {
    private static final String TAG = "FingerprintGenerateChallengeClient";

    /* JADX INFO: Access modifiers changed from: package-private */
    public FingerprintGenerateChallengeClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, ClientMonitorCallbackConverter clientMonitorCallbackConverter, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, i, str, i2, biometricLogger, biometricContext);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        try {
            getFreshDaemon().getSession().generateChallenge();
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to generateChallenge", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onChallengeGenerated(int i, int i2, long j) {
        try {
            ClientMonitorCallbackConverter listener = getListener();
            if (listener == null) {
                Slog.e(TAG, "Listener is null in onChallengeGenerated");
                this.mCallback.onClientFinished(this, false);
            } else {
                listener.onChallengeGenerated(i, i2, j);
                this.mCallback.onClientFinished(this, true);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to send challenge", e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
