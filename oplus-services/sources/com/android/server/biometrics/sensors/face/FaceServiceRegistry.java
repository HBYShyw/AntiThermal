package com.android.server.biometrics.sensors.face;

import android.hardware.biometrics.IBiometricService;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.face.IFaceAuthenticatorsRegisteredCallback;
import android.hardware.face.IFaceService;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.Utils;
import com.android.server.biometrics.sensors.BiometricServiceRegistry;
import java.util.List;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceServiceRegistry extends BiometricServiceRegistry<ServiceProvider, FaceSensorPropertiesInternal, IFaceAuthenticatorsRegisteredCallback> {
    private static final String TAG = "FaceServiceRegistry";
    private final IFaceService mService;

    public FaceServiceRegistry(IFaceService iFaceService, Supplier<IBiometricService> supplier) {
        super(supplier);
        this.mService = iFaceService;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.BiometricServiceRegistry
    public void registerService(IBiometricService iBiometricService, FaceSensorPropertiesInternal faceSensorPropertiesInternal) {
        try {
            iBiometricService.registerAuthenticator(faceSensorPropertiesInternal.sensorId, 8, Utils.propertyStrengthToAuthenticatorStrength(faceSensorPropertiesInternal.sensorStrength), new FaceAuthenticator(this.mService, faceSensorPropertiesInternal.sensorId));
        } catch (RemoteException unused) {
            Slog.e(TAG, "Remote exception when registering sensorId: " + faceSensorPropertiesInternal.sensorId);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.BiometricServiceRegistry
    public void invokeRegisteredCallback(IFaceAuthenticatorsRegisteredCallback iFaceAuthenticatorsRegisteredCallback, List<FaceSensorPropertiesInternal> list) throws RemoteException {
        iFaceAuthenticatorsRegisteredCallback.onAllAuthenticatorsRegistered(list);
    }
}
