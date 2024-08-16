package com.android.server.biometrics.sensors;

import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import java.util.Arrays;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class EnrollClient<T> extends AcquisitionClient<T> implements EnrollmentModifier {
    private static final String TAG = "Biometrics/EnrollClient";
    protected final BiometricUtils mBiometricUtils;
    private long mEnrollmentStartTimeMs;
    protected final byte[] mHardwareAuthToken;
    private final boolean mHasEnrollmentsBeforeStarting;
    protected final int mTimeoutSec;

    /* JADX INFO: Access modifiers changed from: protected */
    public int getOverlayReasonFromEnrollReason(int i) {
        if (i != 1) {
            return i != 2 ? 0 : 2;
        }
        return 1;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 2;
    }

    protected abstract boolean hasReachedEnrollmentLimit();

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    public EnrollClient(Context context, Supplier<T> supplier, IBinder iBinder, ClientMonitorCallbackConverter clientMonitorCallbackConverter, int i, byte[] bArr, String str, BiometricUtils biometricUtils, int i2, int i3, boolean z, BiometricLogger biometricLogger, BiometricContext biometricContext) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, i, str, 0, i3, z, biometricLogger, biometricContext);
        this.mBiometricUtils = biometricUtils;
        this.mHardwareAuthToken = Arrays.copyOf(bArr, bArr.length);
        this.mTimeoutSec = i2;
        this.mHasEnrollmentsBeforeStarting = hasEnrollments();
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollmentStateChanged() {
        return hasEnrollments() != this.mHasEnrollmentsBeforeStarting;
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollments() {
        return !this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty();
    }

    public void onEnrollResult(BiometricAuthenticator.Identifier identifier, int i) {
        if (this.mShouldVibrate) {
            vibrateSuccess();
        }
        ClientMonitorCallbackConverter listener = getListener();
        if (listener != null) {
            try {
                listener.onEnrollResult(identifier, i);
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception", e);
            }
        }
        if (i == 0) {
            this.mBiometricUtils.addBiometricForUser(getContext(), getTargetUserId(), identifier);
            getLogger().logOnEnrolled(getTargetUserId(), System.currentTimeMillis() - this.mEnrollmentStartTimeMs, true);
            this.mCallback.onClientFinished(this, true);
        }
        notifyUserActivity();
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        if (hasReachedEnrollmentLimit()) {
            Slog.e(TAG, "Reached enrollment limit");
            clientMonitorCallback.onClientFinished(this, false);
        } else {
            this.mEnrollmentStartTimeMs = System.currentTimeMillis();
            startHalOperation();
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        getLogger().logOnEnrolled(getTargetUserId(), System.currentTimeMillis() - this.mEnrollmentStartTimeMs, false);
        super.onError(i, i2);
    }
}
