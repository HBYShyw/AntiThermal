package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.hardware.biometrics.common.ICancellationSignal;
import android.hardware.biometrics.common.OperationContext;
import android.hardware.fingerprint.FingerprintAuthenticateOptions;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.log.OperationContextExt;
import com.android.server.biometrics.sensors.AcquisitionClient;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.DetectionConsumer;
import com.android.server.biometrics.sensors.SensorOverlays;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintDetectClient extends AcquisitionClient<AidlSession> implements DetectionConsumer {
    private static final String TAG = "FingerprintDetectClient";
    private ICancellationSignal mCancellationSignal;
    private final boolean mIsStrongBiometric;
    private final FingerprintAuthenticateOptions mOptions;
    private final SensorOverlays mSensorOverlays;

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 13;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FingerprintDetectClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, FingerprintAuthenticateOptions fingerprintAuthenticateOptions, BiometricLogger biometricLogger, BiometricContext biometricContext, IUdfpsOverlayController iUdfpsOverlayController, IUdfpsOverlay iUdfpsOverlay, boolean z) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, fingerprintAuthenticateOptions.getUserId(), fingerprintAuthenticateOptions.getOpPackageName(), 0, fingerprintAuthenticateOptions.getSensorId(), true, biometricLogger, biometricContext);
        setRequestId(j);
        this.mIsStrongBiometric = z;
        this.mSensorOverlays = new SensorOverlays(iUdfpsOverlayController, null, iUdfpsOverlay);
        this.mOptions = fingerprintAuthenticateOptions;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mSensorOverlays.hide(getSensorId());
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
        this.mSensorOverlays.show(getSensorId(), 4, this);
        try {
            this.mCancellationSignal = doDetectInteraction();
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when requesting finger detect", e);
            this.mSensorOverlays.hide(getSensorId());
            this.mCallback.onClientFinished(this, false);
        }
    }

    private ICancellationSignal doDetectInteraction() throws RemoteException {
        final AidlSession freshDaemon = getFreshDaemon();
        if (freshDaemon.hasContextMethods()) {
            OperationContextExt operationContext = getOperationContext();
            ICancellationSignal detectInteractionWithContext = freshDaemon.getSession().detectInteractionWithContext(operationContext.toAidlContext(this.mOptions));
            getBiometricContext().subscribe(operationContext, new Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    FingerprintDetectClient.lambda$doDetectInteraction$0(AidlSession.this, (OperationContext) obj);
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
            this.mSensorOverlays.hide(getSensorId());
            this.mCallback.onClientFinished(this, true);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when sending onDetected", e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
