package com.android.server.biometrics.sensors;

import android.hardware.biometrics.BiometricManager;
import android.util.Slog;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class MultiBiometricLockoutState {
    private static final String TAG = "MultiBiometricLockoutState";
    private final Map<Integer, Map<Integer, AuthenticatorState>> mCanUserAuthenticate = new HashMap();
    private final Clock mClock;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiBiometricLockoutState(Clock clock) {
        this.mClock = clock;
    }

    private Map<Integer, AuthenticatorState> createUnlockedMap() {
        HashMap hashMap = new HashMap();
        hashMap.put(15, new AuthenticatorState(15, false, false));
        hashMap.put(255, new AuthenticatorState(255, false, false));
        hashMap.put(4095, new AuthenticatorState(4095, false, false));
        return hashMap;
    }

    private Map<Integer, AuthenticatorState> getAuthMapForUser(int i) {
        if (!this.mCanUserAuthenticate.containsKey(Integer.valueOf(i))) {
            this.mCanUserAuthenticate.put(Integer.valueOf(i), createUnlockedMap());
        }
        return this.mCanUserAuthenticate.get(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPermanentLockOut(int i, @BiometricManager.Authenticators.Types int i2) {
        Map<Integer, AuthenticatorState> authMapForUser = getAuthMapForUser(i);
        if (i2 == 15) {
            authMapForUser.get(15).mPermanentlyLockedOut = true;
        } else if (i2 != 255) {
            if (i2 != 4095) {
                Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + i2);
                return;
            }
            authMapForUser.get(4095).mPermanentlyLockedOut = true;
        }
        authMapForUser.get(255).mPermanentlyLockedOut = true;
        authMapForUser.get(4095).mPermanentlyLockedOut = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearPermanentLockOut(int i, @BiometricManager.Authenticators.Types int i2) {
        Map<Integer, AuthenticatorState> authMapForUser = getAuthMapForUser(i);
        if (i2 == 15) {
            authMapForUser.get(15).mPermanentlyLockedOut = false;
        } else if (i2 != 255) {
            if (i2 != 4095) {
                Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + i2);
                return;
            }
            authMapForUser.get(4095).mPermanentlyLockedOut = false;
        }
        authMapForUser.get(255).mPermanentlyLockedOut = false;
        authMapForUser.get(4095).mPermanentlyLockedOut = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTimedLockout(int i, @BiometricManager.Authenticators.Types int i2) {
        Map<Integer, AuthenticatorState> authMapForUser = getAuthMapForUser(i);
        if (i2 == 15) {
            authMapForUser.get(15).mTimedLockout = true;
        } else if (i2 != 255) {
            if (i2 != 4095) {
                Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + i2);
                return;
            }
            authMapForUser.get(4095).mTimedLockout = true;
        }
        authMapForUser.get(255).mTimedLockout = true;
        authMapForUser.get(4095).mTimedLockout = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearTimedLockout(int i, @BiometricManager.Authenticators.Types int i2) {
        Map<Integer, AuthenticatorState> authMapForUser = getAuthMapForUser(i);
        if (i2 == 15) {
            authMapForUser.get(15).mTimedLockout = false;
        } else if (i2 != 255) {
            if (i2 != 4095) {
                Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + i2);
                return;
            }
            authMapForUser.get(4095).mTimedLockout = false;
        }
        authMapForUser.get(255).mTimedLockout = false;
        authMapForUser.get(4095).mTimedLockout = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLockoutState(int i, @BiometricManager.Authenticators.Types int i2) {
        Map<Integer, AuthenticatorState> authMapForUser = getAuthMapForUser(i);
        if (!authMapForUser.containsKey(Integer.valueOf(i2))) {
            Slog.e(TAG, "Error, getLockoutState for unknown strength: " + i2 + " returning LOCKOUT_NONE");
            return 0;
        }
        AuthenticatorState authenticatorState = authMapForUser.get(Integer.valueOf(i2));
        if (authenticatorState.mPermanentlyLockedOut) {
            return 2;
        }
        return authenticatorState.mTimedLockout ? 1 : 0;
    }

    public String toString() {
        final long millis = this.mClock.millis();
        String str = "Permanent Lockouts\n";
        for (Map.Entry<Integer, Map<Integer, AuthenticatorState>> entry : this.mCanUserAuthenticate.entrySet()) {
            str = str + "UserId=" + entry.getKey().intValue() + ", {" + ((String) entry.getValue().entrySet().stream().map(new Function() { // from class: com.android.server.biometrics.sensors.MultiBiometricLockoutState$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$toString$0;
                    lambda$toString$0 = MultiBiometricLockoutState.lambda$toString$0(millis, (Map.Entry) obj);
                    return lambda$toString$0;
                }
            }).collect(Collectors.joining(", "))) + "}\n";
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$toString$0(long j, Map.Entry entry) {
        return ((AuthenticatorState) entry.getValue()).toString(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AuthenticatorState {
        private Integer mAuthenticatorType;
        private boolean mPermanentlyLockedOut;
        private boolean mTimedLockout;

        AuthenticatorState(Integer num, boolean z, boolean z2) {
            this.mAuthenticatorType = num;
            this.mPermanentlyLockedOut = z;
            this.mTimedLockout = z2;
        }

        String toString(long j) {
            return String.format("(%s, permanentLockout=%s, timedLockout=%s)", BiometricManager.authenticatorToStr(this.mAuthenticatorType.intValue()), this.mPermanentlyLockedOut ? "true" : "false", this.mTimedLockout ? "true" : "false");
        }
    }
}
