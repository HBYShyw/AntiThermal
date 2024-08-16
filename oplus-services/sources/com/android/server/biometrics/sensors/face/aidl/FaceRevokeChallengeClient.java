package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.os.IBinder;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.RevokeChallengeClient;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceRevokeChallengeClient extends RevokeChallengeClient<AidlSession> {
    private static final String TAG = "FaceRevokeChallengeClient";
    private final long mChallenge;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceRevokeChallengeClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, long j) {
        super(context, supplier, iBinder, i, str, i2, biometricLogger, biometricContext);
        this.mChallenge = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        try {
            getFreshDaemon().getSession().revokeChallenge(this.mChallenge);
        } catch (Exception e) {
            Slog.e(TAG, "Unable to revokeChallenge", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onChallengeRevoked(int i, int i2, long j) {
        this.mCallback.onClientFinished(this, j == this.mChallenge);
    }
}
