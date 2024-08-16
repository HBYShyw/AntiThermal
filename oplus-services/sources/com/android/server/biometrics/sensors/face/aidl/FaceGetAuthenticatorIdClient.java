package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.HalClientMonitor;
import java.util.Map;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceGetAuthenticatorIdClient extends HalClientMonitor<AidlSession> {
    private static final String TAG = "FaceGetAuthenticatorIdClient";
    private final Map<Integer, Long> mAuthenticatorIds;

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 5;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceGetAuthenticatorIdClient(Context context, Supplier<AidlSession> supplier, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, Map<Integer, Long> map) {
        super(context, supplier, null, null, i, str, 0, i2, biometricLogger, biometricContext);
        this.mAuthenticatorIds = map;
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
            if (getFreshDaemon().getSession() != null) {
                getFreshDaemon().getSession().getAuthenticatorId();
            }
        } catch (Exception e) {
            Slog.e(TAG, "Remote exception", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAuthenticatorIdRetrieved(long j) {
        this.mAuthenticatorIds.put(Integer.valueOf(getTargetUserId()), Long.valueOf(j));
        this.mCallback.onClientFinished(this, true);
    }
}
