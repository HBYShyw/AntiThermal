package com.android.server.biometrics.sensors;

import android.content.Context;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.tool.IBiometricsVibratorUtilsExt;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class AcquisitionClient<T> extends HalClientMonitor<T> implements ErrorConsumer {
    private static final String TAG = "Biometrics/AcquisitionClient";
    private boolean mAlreadyCancelled;
    private final PowerManager mPowerManager;
    private boolean mShouldSendErrorToClient;
    protected final boolean mShouldVibrate;
    private static final VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(50);
    private static final VibrationEffect SUCCESS_VIBRATION_EFFECT = VibrationEffect.get(0);
    private static final VibrationEffect ERROR_VIBRATION_EFFECT = VibrationEffect.get(1);
    public static IBiometricsVibratorUtilsExt.IStaticExt sStaticExt = (IBiometricsVibratorUtilsExt.IStaticExt) ExtLoader.type(IBiometricsVibratorUtilsExt.IStaticExt.class).create();

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean isInterruptable() {
        return true;
    }

    protected abstract void stopHalOperation();

    public AcquisitionClient(Context context, Supplier<T> supplier, IBinder iBinder, ClientMonitorCallbackConverter clientMonitorCallbackConverter, int i, String str, int i2, int i3, boolean z, BiometricLogger biometricLogger, BiometricContext biometricContext) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, i, str, i2, i3, biometricLogger, biometricContext);
        this.mShouldSendErrorToClient = true;
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mShouldVibrate = z;
        sStaticExt.init(context);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
        try {
            getListener().onError(getSensorId(), getCookie(), 1, 0);
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to send error", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        onErrorInternal(i, i2, true);
    }

    public void onUserCanceled() {
        Slog.d(TAG, "onUserCanceled");
        onErrorInternal(10, 0, false);
        stopHalOperation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onErrorInternal(int i, int i2, boolean z) {
        Slog.d(TAG, "onErrorInternal code: " + i + ", finish: " + z);
        if (this.mShouldSendErrorToClient) {
            getLogger().logOnError(getContext(), getOperationContext(), i, i2, getTargetUserId());
            try {
                if (getListener() != null) {
                    this.mShouldSendErrorToClient = false;
                    getListener().onError(getSensorId(), getCookie(), i, i2);
                }
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to invoke sendError", e);
            }
        }
        if (z) {
            ClientMonitorCallback clientMonitorCallback = this.mCallback;
            if (clientMonitorCallback == null) {
                Slog.e(TAG, "Callback is null, perhaps the client hasn't been started yet?");
            } else {
                clientMonitorCallback.onClientFinished(this, false);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void cancel() {
        if (this.mAlreadyCancelled) {
            Slog.w(TAG, "Cancel was already requested");
        } else {
            stopHalOperation();
            this.mAlreadyCancelled = true;
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void cancelWithoutStarting(ClientMonitorCallback clientMonitorCallback) {
        Slog.d(TAG, "cancelWithoutStarting: " + this);
        try {
            if (getListener() != null) {
                getListener().onError(getSensorId(), getCookie(), 5, 0);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to invoke sendError", e);
        }
        clientMonitorCallback.onClientFinished(this, true);
    }

    public void onAcquired(int i, int i2) {
        onAcquiredInternal(i, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void onAcquiredInternal(int i, int i2, boolean z) {
        getLogger().logOnAcquired(getContext(), getOperationContext(), i, i2, getTargetUserId());
        Slog.v(TAG, "Acquired: " + i + " " + i2 + ", shouldSend: " + z);
        if (i == 0) {
            notifyUserActivity();
        }
        try {
            if (getListener() == null || !z) {
                return;
            }
            getListener().onAcquired(getSensorId(), i, i2);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to invoke sendAcquired", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void notifyUserActivity() {
        this.mPowerManager.userActivity(SystemClock.uptimeMillis(), 2, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void vibrateSuccess() {
        Vibrator vibrator;
        if (sStaticExt.vibrateFingerprintSuccess(getContext(), this) || (vibrator = (Vibrator) getContext().getSystemService(Vibrator.class)) == null || !this.mShouldVibrate) {
            return;
        }
        vibrator.vibrate(Process.myUid(), getContext().getOpPackageName(), SUCCESS_VIBRATION_EFFECT, getClass().getSimpleName() + "::success", HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void vibrateError() {
        Vibrator vibrator;
        if (sStaticExt.vibrateFingerprintError(getContext(), this) || (vibrator = (Vibrator) getContext().getSystemService(Vibrator.class)) == null || !this.mShouldVibrate) {
            return;
        }
        vibrator.vibrate(Process.myUid(), getContext().getOpPackageName(), ERROR_VIBRATION_EFFECT, getClass().getSimpleName() + "::error", HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES);
    }
}
