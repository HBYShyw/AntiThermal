package com.android.server.biometrics.sensors;

import android.app.ActivityTaskManager;
import android.app.TaskStackListener;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.hardware.biometrics.AuthenticateOptions;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.security.KeyStore;
import android.util.EventLog;
import android.util.Slog;
import com.android.server.biometrics.Utils;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import java.util.ArrayList;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class AuthenticationClient<T, O extends AuthenticateOptions> extends AcquisitionClient<T> implements AuthenticationConsumer {
    public static final int STATE_NEW = 0;
    public static final int STATE_STARTED = 1;
    public static final int STATE_STARTED_PAUSED = 2;
    public static final int STATE_STARTED_PAUSED_ATTEMPTED = 3;
    public static final int STATE_STOPPED = 4;
    private static final String TAG = "Biometrics/AuthenticationClient";
    private final ActivityTaskManager mActivityTaskManager;
    private final boolean mAllowBackgroundAuthentication;
    private boolean mAuthAttempted;
    private boolean mAuthSuccess;
    private final BiometricManager mBiometricManager;
    private final boolean mIsRestricted;
    private final boolean mIsStrongBiometric;
    private final LockoutTracker mLockoutTracker;
    protected final long mOperationId;
    private final O mOptions;
    private final boolean mRequireConfirmation;
    private final int mSensorStrength;
    private final boolean mShouldUseLockoutTracker;
    private long mStartTimeMs;

    @State
    protected int mState;
    private final TaskStackListener mTaskStackListener;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface State {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 3;
    }

    public int handleFailedAttempt(int i) {
        return 0;
    }

    protected abstract void handleLifecycleAfterAuth(boolean z);

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    public abstract boolean wasUserDetected();

    public AuthenticationClient(Context context, Supplier<T> supplier, IBinder iBinder, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j, boolean z, O o, int i, boolean z2, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z3, TaskStackListener taskStackListener, LockoutTracker lockoutTracker, boolean z4, boolean z5, int i2) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, o.getUserId(), o.getOpPackageName(), i, o.getSensorId(), z5, biometricLogger, biometricContext);
        this.mState = 0;
        this.mAuthSuccess = false;
        this.mIsStrongBiometric = z3;
        this.mOperationId = j;
        this.mRequireConfirmation = z2;
        this.mActivityTaskManager = getActivityTaskManager();
        this.mBiometricManager = (BiometricManager) context.getSystemService(BiometricManager.class);
        this.mTaskStackListener = taskStackListener;
        this.mLockoutTracker = lockoutTracker;
        this.mIsRestricted = z;
        this.mAllowBackgroundAuthentication = z4;
        this.mShouldUseLockoutTracker = lockoutTracker != null;
        this.mSensorStrength = i2;
        this.mOptions = o;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getStartTimeMs() {
        return this.mStartTimeMs;
    }

    protected ActivityTaskManager getActivityTaskManager() {
        return ActivityTaskManager.getInstance();
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor, android.os.IBinder.DeathRecipient
    public void binderDied() {
        binderDiedInternal(!isBiometricPrompt());
    }

    public long getOperationId() {
        return this.mOperationId;
    }

    public boolean isRestricted() {
        return this.mIsRestricted;
    }

    public boolean isKeyguard() {
        return Utils.isKeyguard(getContext(), getOwnerString());
    }

    private boolean isSettings() {
        return Utils.isSettings(getContext(), getOwnerString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public O getOptions() {
        return this.mOptions;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean isCryptoOperation() {
        return this.mOperationId != 0;
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(BiometricAuthenticator.Identifier identifier, boolean z, ArrayList<Byte> arrayList) {
        boolean z2 = z;
        getLogger().logOnAuthenticated(getContext(), getOperationContext(), z, this.mRequireConfirmation, getTargetUserId(), isBiometricPrompt());
        ClientMonitorCallbackConverter listener = getListener();
        Slog.v(TAG, "onAuthenticated(" + z2 + "), ID:" + identifier.getBiometricId() + ", Owner: " + getOwnerString() + ", isBP: " + isBiometricPrompt() + ", listener: " + listener + ", requireConfirmation: " + this.mRequireConfirmation + ", user: " + getTargetUserId() + ", clientMonitor: " + this);
        PerformanceTracker instanceForSensorId = PerformanceTracker.getInstanceForSensorId(getSensorId());
        if (isCryptoOperation()) {
            instanceForSensorId.incrementCryptoAuthForUser(getTargetUserId(), z2);
        } else if (instanceForSensorId != null) {
            instanceForSensorId.incrementAuthForUser(getTargetUserId(), z2);
        }
        if (this.mAllowBackgroundAuthentication) {
            Slog.w(TAG, "Allowing background authentication, this is allowed only for platform or test invocations");
        }
        boolean isBackground = (this.mAllowBackgroundAuthentication || !z2 || Utils.isKeyguard(getContext(), getOwnerString()) || Utils.isSystem(getContext(), getOwnerString())) ? false : Utils.isBackground(getOwnerString());
        if (isBackground) {
            Slog.e(TAG, "Failing possible background authentication");
            ApplicationInfo applicationInfo = getContext().getApplicationInfo();
            Object[] objArr = new Object[3];
            objArr[0] = "159249069";
            objArr[1] = Integer.valueOf(applicationInfo != null ? applicationInfo.uid : -1);
            objArr[2] = "Attempted background authentication";
            EventLog.writeEvent(1397638484, objArr);
            z2 = false;
        }
        if (z2) {
            if (isBackground) {
                ApplicationInfo applicationInfo2 = getContext().getApplicationInfo();
                Object[] objArr2 = new Object[3];
                objArr2[0] = "159249069";
                objArr2[1] = Integer.valueOf(applicationInfo2 != null ? applicationInfo2.uid : -1);
                objArr2[2] = "Successful background authentication!";
                EventLog.writeEvent(1397638484, objArr2);
            }
            this.mAuthSuccess = true;
            markAlreadyDone();
            TaskStackListener taskStackListener = this.mTaskStackListener;
            if (taskStackListener != null) {
                this.mActivityTaskManager.unregisterTaskStackListener(taskStackListener);
            }
            byte[] bArr = new byte[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                bArr[i] = arrayList.get(i).byteValue();
            }
            if (this.mIsStrongBiometric) {
                this.mBiometricManager.resetLockoutTimeBound(getToken(), getContext().getOpPackageName(), getSensorId(), getTargetUserId(), bArr);
            }
            if (!isBiometricPrompt() && this.mIsStrongBiometric) {
                int addAuthToken = KeyStore.getInstance().addAuthToken(bArr);
                if (addAuthToken != 1) {
                    Slog.d(TAG, "Error adding auth token : " + addAuthToken);
                } else {
                    Slog.d(TAG, "addAuthToken: " + addAuthToken);
                }
            } else {
                Slog.d(TAG, "Skipping addAuthToken");
            }
            try {
                if (listener != null) {
                    if (!this.mIsRestricted) {
                        listener.onAuthenticationSucceeded(getSensorId(), identifier, bArr, getTargetUserId(), this.mIsStrongBiometric);
                    } else {
                        listener.onAuthenticationSucceeded(getSensorId(), null, bArr, getTargetUserId(), this.mIsStrongBiometric);
                    }
                    if (this.mShouldVibrate) {
                        vibrateSuccess();
                    }
                } else {
                    Slog.e(TAG, "Received successful auth, but client was not listening");
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Unable to notify listener", e);
                this.mCallback.onClientFinished(this, false);
                return;
            }
        } else if (isBackground) {
            Slog.e(TAG, "cancelling due to background auth");
            cancel();
        } else {
            if (this.mShouldUseLockoutTracker && handleFailedAttempt(getTargetUserId()) != 0) {
                markAlreadyDone();
            }
            try {
                if (listener != null) {
                    listener.onAuthenticationFailed(getSensorId());
                    if (this.mShouldVibrate) {
                        vibrateError();
                    }
                } else {
                    Slog.e(TAG, "listener is NULL!!!");
                }
            } catch (RemoteException e2) {
                Slog.e(TAG, "Unable to notify listener", e2);
                this.mCallback.onClientFinished(this, false);
                return;
            }
        }
        handleLifecycleAfterAuth(z2);
    }

    private void sendCancelOnly(ClientMonitorCallbackConverter clientMonitorCallbackConverter) {
        if (clientMonitorCallbackConverter == null) {
            Slog.e(TAG, "Unable to sendAuthenticationCanceled, listener null");
            return;
        }
        try {
            clientMonitorCallbackConverter.onError(getSensorId(), getCookie(), 5, 0);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(int i, int i2) {
        super.onAcquired(i, i2);
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        super.onError(i, i2);
        this.mState = 4;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        int lockoutStateFor;
        super.start(clientMonitorCallback);
        if (this.mShouldUseLockoutTracker) {
            lockoutStateFor = this.mLockoutTracker.getLockoutModeForUser(getTargetUserId());
        } else {
            lockoutStateFor = getBiometricContext().getAuthSessionCoordinator().getLockoutStateFor(getTargetUserId(), this.mSensorStrength);
        }
        if (lockoutStateFor != 0) {
            Slog.v(TAG, "In lockout mode(" + lockoutStateFor + ") ; disallowing authentication");
            onError(lockoutStateFor == 1 ? 7 : 9, 0);
            return;
        }
        TaskStackListener taskStackListener = this.mTaskStackListener;
        if (taskStackListener != null) {
            this.mActivityTaskManager.registerTaskStackListener(taskStackListener);
        }
        Slog.d(TAG, "Requesting auth for " + getOwnerString());
        this.mStartTimeMs = System.currentTimeMillis();
        this.mAuthAttempted = true;
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void cancel() {
        super.cancel();
        TaskStackListener taskStackListener = this.mTaskStackListener;
        if (taskStackListener != null) {
            this.mActivityTaskManager.unregisterTaskStackListener(taskStackListener);
        }
    }

    @State
    public int getState() {
        return this.mState;
    }

    public boolean wasAuthAttempted() {
        return this.mAuthAttempted;
    }

    public boolean wasAuthSuccessful() {
        return this.mAuthSuccess;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getSensorStrength() {
        return this.mSensorStrength;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LockoutTracker getLockoutTracker() {
        return this.mLockoutTracker;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getShowOverlayReason() {
        if (isKeyguard()) {
            return 4;
        }
        if (isBiometricPrompt()) {
            return 3;
        }
        return isSettings() ? 6 : 5;
    }
}
