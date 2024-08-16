package com.android.server.biometrics.sensors.face.hidl;

import android.content.Context;
import android.hardware.biometrics.face.V1_0.IBiometricsFace;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.HalClientMonitor;
import java.io.File;
import java.util.Map;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceUpdateActiveUserClient extends HalClientMonitor<IBiometricsFace> {
    private static final String FACE_DATA_DIR = "facedata";
    private static final String TAG = "FaceUpdateActiveUserClient";
    private final Map<Integer, Long> mAuthenticatorIds;
    private final boolean mHasEnrolledBiometrics;

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 1;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceUpdateActiveUserClient(Context context, Supplier<IBiometricsFace> supplier, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z, Map<Integer, Long> map) {
        super(context, supplier, null, null, i, str, 0, i2, biometricLogger, biometricContext);
        this.mHasEnrolledBiometrics = z;
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
        File file = new File(Environment.getDataVendorDeDirectory(getTargetUserId()), FACE_DATA_DIR);
        if (!file.exists()) {
            Slog.e(TAG, "vold has not created the directory?");
            this.mCallback.onClientFinished(this, false);
            return;
        }
        try {
            IBiometricsFace freshDaemon = getFreshDaemon();
            freshDaemon.setActiveUser(getTargetUserId(), file.getAbsolutePath());
            this.mAuthenticatorIds.put(Integer.valueOf(getTargetUserId()), Long.valueOf(this.mHasEnrolledBiometrics ? freshDaemon.getAuthenticatorId().value : 0L));
            this.mCallback.onClientFinished(this, true);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to setActiveUser: " + e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
