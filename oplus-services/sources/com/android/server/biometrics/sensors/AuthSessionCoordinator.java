package com.android.server.biometrics.sensors;

import android.hardware.biometrics.BiometricManager;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.time.Clock;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AuthSessionCoordinator implements AuthSessionListener {
    private static final String TAG = "AuthSessionCoordinator";
    private final Set<Integer> mAuthOperations;
    private AuthResultCoordinator mAuthResultCoordinator;
    private boolean mIsAuthenticating;
    private final MultiBiometricLockoutState mMultiBiometricLockoutState;
    private final RingBuffer mRingBuffer;
    private int mUserId;

    public AuthSessionCoordinator() {
        this(SystemClock.elapsedRealtimeClock());
    }

    @VisibleForTesting
    AuthSessionCoordinator(Clock clock) {
        this.mAuthOperations = new HashSet();
        this.mAuthResultCoordinator = new AuthResultCoordinator();
        this.mMultiBiometricLockoutState = new MultiBiometricLockoutState(clock);
        this.mRingBuffer = new RingBuffer(100);
    }

    void onAuthSessionStarted(int i) {
        this.mAuthOperations.clear();
        this.mUserId = i;
        this.mIsAuthenticating = true;
        this.mAuthResultCoordinator = new AuthResultCoordinator();
        this.mRingBuffer.addApiCall("internal : onAuthSessionStarted(" + i + ")");
    }

    void endAuthSession() {
        Map<Integer, Integer> result = this.mAuthResultCoordinator.getResult();
        Iterator it = Arrays.asList(4095, 255, 15).iterator();
        while (it.hasNext()) {
            int intValue = ((Integer) it.next()).intValue();
            Integer num = result.get(Integer.valueOf(intValue));
            if ((num.intValue() & 4) == 4) {
                this.mMultiBiometricLockoutState.clearPermanentLockOut(this.mUserId, intValue);
                this.mMultiBiometricLockoutState.clearTimedLockout(this.mUserId, intValue);
            } else if ((num.intValue() & 1) == 1) {
                this.mMultiBiometricLockoutState.setPermanentLockOut(this.mUserId, intValue);
            } else if ((num.intValue() & 2) == 2) {
                this.mMultiBiometricLockoutState.setTimedLockout(this.mUserId, intValue);
            }
        }
        if (this.mAuthOperations.isEmpty()) {
            this.mRingBuffer.addApiCall("internal : onAuthSessionEnded(" + this.mUserId + ")");
            clearSession();
        }
    }

    private void clearSession() {
        this.mIsAuthenticating = false;
        this.mAuthOperations.clear();
    }

    public int getLockoutStateFor(int i, @BiometricManager.Authenticators.Types int i2) {
        return this.mMultiBiometricLockoutState.getLockoutState(i, i2);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void authStartedFor(int i, int i2, long j) {
        this.mRingBuffer.addApiCall("authStartedFor(userId=" + i + ", sensorId=" + i2 + ", requestId=" + j + ")");
        if (!this.mIsAuthenticating) {
            onAuthSessionStarted(i);
        }
        if (this.mAuthOperations.contains(Integer.valueOf(i2))) {
            Slog.e(TAG, "Error, authStartedFor(" + i2 + ") without being finished");
            return;
        }
        if (this.mUserId != i) {
            Slog.e(TAG, "Error authStartedFor(" + i + ") Incorrect userId, expected" + this.mUserId + ", ignoring...");
            return;
        }
        this.mAuthOperations.add(Integer.valueOf(i2));
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void lockedOutFor(int i, @BiometricManager.Authenticators.Types int i2, int i3, long j) {
        String str = "lockOutFor(userId=" + i + ", biometricStrength=" + i2 + ", sensorId=" + i3 + ", requestId=" + j + ")";
        this.mRingBuffer.addApiCall(str);
        this.mAuthResultCoordinator.lockedOutFor(i2);
        attemptToFinish(i, i3, str);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void lockOutTimed(int i, @BiometricManager.Authenticators.Types int i2, int i3, long j, long j2) {
        String str = "lockOutTimedFor(userId=" + i + ", biometricStrength=" + i2 + ", sensorId=" + i3 + "time=" + j + ", requestId=" + j2 + ")";
        this.mRingBuffer.addApiCall(str);
        this.mAuthResultCoordinator.lockOutTimed(i2);
        attemptToFinish(i, i3, str);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void authEndedFor(int i, @BiometricManager.Authenticators.Types int i2, int i3, long j, boolean z) {
        String str = "authEndedFor(userId=" + i + " ,biometricStrength=" + i2 + ", sensorId=" + i3 + ", requestId=" + j + ", wasSuccessful=" + z + ")";
        this.mRingBuffer.addApiCall(str);
        if (z) {
            this.mAuthResultCoordinator.authenticatedFor(i2);
        }
        attemptToFinish(i, i3, str);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void resetLockoutFor(int i, @BiometricManager.Authenticators.Types int i2, long j) {
        this.mRingBuffer.addApiCall("resetLockoutFor(userId=" + i + " ,biometricStrength=" + i2 + ", requestId=" + j + ")");
        if (i2 == 15) {
            clearSession();
            this.mMultiBiometricLockoutState.clearPermanentLockOut(i, i2);
            this.mMultiBiometricLockoutState.clearTimedLockout(i, i2);
        }
    }

    private void attemptToFinish(int i, int i2, String str) {
        boolean z;
        boolean z2 = true;
        if (this.mAuthOperations.contains(Integer.valueOf(i2))) {
            z = false;
        } else {
            Slog.e(TAG, "Error unable to find auth operation : " + str);
            z = true;
        }
        if (i != this.mUserId) {
            Slog.e(TAG, "Error mismatched userId, expected=" + this.mUserId + " for " + str);
        } else {
            z2 = z;
        }
        if (z2) {
            return;
        }
        this.mAuthOperations.remove(Integer.valueOf(i2));
        if (this.mIsAuthenticating) {
            endAuthSession();
        }
    }

    public String toString() {
        return this.mRingBuffer + "\n" + this.mMultiBiometricLockoutState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class RingBuffer {
        private int mApiCallNumber;
        private final String[] mApiCalls;
        private int mCurr;
        private final int mSize;

        RingBuffer(int i) {
            if (i <= 0) {
                Slog.wtf(AuthSessionCoordinator.TAG, "Cannot initialize ring buffer of size: " + i);
            }
            this.mApiCalls = new String[i];
            this.mCurr = 0;
            this.mSize = i;
            this.mApiCallNumber = 0;
        }

        void addApiCall(String str) {
            String[] strArr = this.mApiCalls;
            int i = this.mCurr;
            strArr[i] = str;
            int i2 = i + 1;
            this.mCurr = i2;
            this.mCurr = i2 % this.mSize;
            this.mApiCallNumber++;
        }

        public String toString() {
            int i = this.mApiCallNumber;
            int i2 = this.mSize;
            int i3 = 0;
            int i4 = i > i2 ? i - i2 : 0;
            String str = "";
            while (true) {
                int i5 = this.mSize;
                if (i3 >= i5) {
                    return str;
                }
                int i6 = (this.mCurr + i3) % i5;
                if (this.mApiCalls[i6] != null) {
                    str = str + String.format("#%-5d %s\n", Integer.valueOf(i4), this.mApiCalls[i6]);
                    i4++;
                }
                i3++;
            }
        }
    }
}
