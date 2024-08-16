package com.android.server.biometrics.sensors;

import android.hardware.biometrics.BiometricManager;
import android.util.ArrayMap;
import java.util.Collections;
import java.util.Map;
import java.util.function.IntFunction;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class AuthResultCoordinator {
    static final int AUTHENTICATOR_DEFAULT = 0;
    static final int AUTHENTICATOR_PERMANENT_LOCKED = 1;
    static final int AUTHENTICATOR_TIMED_LOCKED = 2;
    static final int AUTHENTICATOR_UNLOCKED = 4;
    private static final String TAG = "AuthResultCoordinator";
    private final Map<Integer, Integer> mAuthenticatorState;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AuthResultCoordinator() {
        ArrayMap arrayMap = new ArrayMap();
        this.mAuthenticatorState = arrayMap;
        arrayMap.put(15, 0);
        arrayMap.put(255, 0);
        arrayMap.put(4095, 0);
    }

    private void updateState(@BiometricManager.Authenticators.Types int i, IntFunction<Integer> intFunction) {
        if (i == 15) {
            this.mAuthenticatorState.put(15, intFunction.apply(this.mAuthenticatorState.get(15).intValue()));
        } else if (i != 255) {
            if (i != 4095) {
                return;
            }
            this.mAuthenticatorState.put(4095, intFunction.apply(this.mAuthenticatorState.get(4095).intValue()));
        }
        this.mAuthenticatorState.put(255, intFunction.apply(this.mAuthenticatorState.get(255).intValue()));
        this.mAuthenticatorState.put(4095, intFunction.apply(this.mAuthenticatorState.get(4095).intValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$authenticatedFor$0(int i) {
        return Integer.valueOf(i | 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void authenticatedFor(@BiometricManager.Authenticators.Types int i) {
        if (i == 15) {
            updateState(i, new IntFunction() { // from class: com.android.server.biometrics.sensors.AuthResultCoordinator$$ExternalSyntheticLambda2
                @Override // java.util.function.IntFunction
                public final Object apply(int i2) {
                    Integer lambda$authenticatedFor$0;
                    lambda$authenticatedFor$0 = AuthResultCoordinator.lambda$authenticatedFor$0(i2);
                    return lambda$authenticatedFor$0;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$lockedOutFor$1(int i) {
        return Integer.valueOf(i | 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void lockedOutFor(@BiometricManager.Authenticators.Types int i) {
        updateState(i, new IntFunction() { // from class: com.android.server.biometrics.sensors.AuthResultCoordinator$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i2) {
                Integer lambda$lockedOutFor$1;
                lambda$lockedOutFor$1 = AuthResultCoordinator.lambda$lockedOutFor$1(i2);
                return lambda$lockedOutFor$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$lockOutTimed$2(int i) {
        return Integer.valueOf(i | 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void lockOutTimed(@BiometricManager.Authenticators.Types int i) {
        updateState(i, new IntFunction() { // from class: com.android.server.biometrics.sensors.AuthResultCoordinator$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final Object apply(int i2) {
                Integer lambda$lockOutTimed$2;
                lambda$lockOutTimed$2 = AuthResultCoordinator.lambda$lockOutTimed$2(i2);
                return lambda$lockOutTimed$2;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<Integer, Integer> getResult() {
        return Collections.unmodifiableMap(this.mAuthenticatorState);
    }
}
