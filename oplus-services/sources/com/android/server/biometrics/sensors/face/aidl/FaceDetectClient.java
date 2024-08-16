package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.common.ICancellationSignal;
import android.hardware.biometrics.common.OperationContext;
import android.hardware.face.FaceAuthenticateOptions;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.log.OperationContextExt;
import com.android.server.biometrics.sensors.AcquisitionClient;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.DetectionConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceDetectClient extends AcquisitionClient<AidlSession> implements DetectionConsumer {
    private static final String TAG = "FaceDetectClient";
    private ICancellationSignal mCancellationSignal;
    private final boolean mIsStrongBiometric;
    private final FaceAuthenticateOptions mOptions;
    private SensorPrivacyManager mSensorPrivacyManager;

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 13;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceDetectClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, FaceAuthenticateOptions faceAuthenticateOptions, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z) {
        this(context, supplier, iBinder, j, clientMonitorCallbackConverter, faceAuthenticateOptions, biometricLogger, biometricContext, z, (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class));
    }

    @VisibleForTesting
    FaceDetectClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, FaceAuthenticateOptions faceAuthenticateOptions, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z, SensorPrivacyManager sensorPrivacyManager) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, faceAuthenticateOptions.getUserId(), faceAuthenticateOptions.getOpPackageName(), 0, faceAuthenticateOptions.getSensorId(), false, biometricLogger, biometricContext);
        setRequestId(j);
        this.mIsStrongBiometric = z;
        this.mSensorPrivacyManager = sensorPrivacyManager;
        this.mOptions = faceAuthenticateOptions;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        unsubscribeBiometricContext();
        ICancellationSignal iCancellationSignal = this.mCancellationSignal;
        if (iCancellationSignal != null) {
            try {
                iCancellationSignal.cancel();
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception", e);
                this.mCallback.onClientFinished(this, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        SensorPrivacyManager sensorPrivacyManager = this.mSensorPrivacyManager;
        if (sensorPrivacyManager != null && sensorPrivacyManager.isSensorPrivacyEnabled(1, 2)) {
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
            return;
        }
        try {
            this.mCancellationSignal = doDetectInteraction();
        } catch (Exception e) {
            Slog.e(TAG, "Remote exception when requesting face detect", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    private ICancellationSignal doDetectInteraction() throws RemoteException {
        final AidlSession freshDaemon = getFreshDaemon();
        if (freshDaemon.hasContextMethods()) {
            OperationContextExt operationContext = getOperationContext();
            ICancellationSignal detectInteractionWithContext = freshDaemon.getSession().detectInteractionWithContext(operationContext.toAidlContext(this.mOptions));
            getBiometricContext().subscribe(operationContext, new Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceDetectClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    FaceDetectClient.lambda$doDetectInteraction$0(AidlSession.this, (OperationContext) obj);
                }
            });
            return detectInteractionWithContext;
        }
        return freshDaemon.getSession().detectInteraction();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$doDetectInteraction$0(AidlSession aidlSession, OperationContext operationContext) {
        try {
            aidlSession.getSession().onContextChanged(operationContext);
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to notify context changed", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.DetectionConsumer
    public void onInteractionDetected() {
        vibrateSuccess();
        try {
            getListener().onDetected(getSensorId(), getTargetUserId(), this.mIsStrongBiometric);
            this.mCallback.onClientFinished(this, true);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when sending onDetected", e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
