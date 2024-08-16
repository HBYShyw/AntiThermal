package com.android.server.biometrics;

import android.content.Context;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.IBiometricAuthenticator;
import android.hardware.biometrics.IBiometricSensorReceiver;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BiometricSensor {
    static final int STATE_AUTHENTICATING = 3;
    static final int STATE_CANCELING = 4;
    static final int STATE_COOKIE_RETURNED = 2;
    static final int STATE_STOPPED = 5;
    static final int STATE_UNKNOWN = 0;
    static final int STATE_WAITING_FOR_COOKIE = 1;
    private static final String TAG = "BiometricService/Sensor";
    public final int id;
    public final IBiometricAuthenticator impl;
    private final Context mContext;
    private int mCookie;
    private int mError;
    private int mSensorState;

    @BiometricManager.Authenticators.Types
    private int mUpdatedStrength;
    public final int modality;

    @BiometricManager.Authenticators.Types
    public final int oemStrength;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface SensorState {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean confirmationAlwaysRequired(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean confirmationSupported();

    /* JADX INFO: Access modifiers changed from: package-private */
    public BiometricSensor(Context context, int i, int i2, @BiometricManager.Authenticators.Types int i3, IBiometricAuthenticator iBiometricAuthenticator) {
        this.mContext = context;
        this.id = i;
        this.modality = i2;
        this.oemStrength = i3;
        this.impl = iBiometricAuthenticator;
        this.mUpdatedStrength = i3;
        goToStateUnknown();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goToStateUnknown() {
        this.mSensorState = 0;
        this.mCookie = 0;
        this.mError = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goToStateWaitingForCookie(boolean z, IBinder iBinder, long j, int i, IBiometricSensorReceiver iBiometricSensorReceiver, String str, long j2, int i2, boolean z2) throws RemoteException {
        this.mCookie = i2;
        this.impl.prepareForAuthentication(z, iBinder, j, i, iBiometricSensorReceiver, str, j2, i2, z2);
        this.mSensorState = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goToStateCookieReturnedIfCookieMatches(int i) {
        if (i == this.mCookie) {
            Slog.d(TAG, "Sensor(" + this.id + ") matched cookie: " + i);
            this.mSensorState = 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startSensor() throws RemoteException {
        this.impl.startPreparedClient(this.mCookie);
        this.mSensorState = 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goToStateCancelling(IBinder iBinder, String str, long j) throws RemoteException {
        if (this.mSensorState != 4) {
            this.impl.cancelAuthenticationFromService(iBinder, str, j);
            this.mSensorState = 4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goToStoppedStateIfCookieMatches(int i, int i2) {
        if (i == this.mCookie) {
            Slog.d(TAG, "Sensor(" + this.id + ") now in STATE_STOPPED");
            this.mError = i2;
            this.mSensorState = 5;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @BiometricManager.Authenticators.Types
    public int getCurrentStrength() {
        return this.mUpdatedStrength | this.oemStrength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSensorState() {
        return this.mSensorState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCookie() {
        return this.mCookie;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateStrength(@BiometricManager.Authenticators.Types int i) {
        this.mUpdatedStrength = i;
        Slog.d(TAG, ("updateStrength: Before(" + this + ")") + " After(" + this + ")");
    }

    public String toString() {
        return "ID(" + this.id + "), oemStrength: " + this.oemStrength + ", updatedStrength: " + this.mUpdatedStrength + ", modality " + this.modality + ", state: " + this.mSensorState + ", cookie: " + this.mCookie;
    }
}
