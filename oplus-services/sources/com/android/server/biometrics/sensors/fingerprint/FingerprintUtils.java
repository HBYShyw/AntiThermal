package com.android.server.biometrics.sensors.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.Fingerprint;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.biometrics.sensors.BiometricUtils;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintUtils implements BiometricUtils<Fingerprint> {
    private static final String LEGACY_FINGERPRINT_FILE = "settings_fingerprint.xml";
    private static final Object sInstanceLock = new Object();
    private static SparseArray<FingerprintUtils> sInstances;
    private final String mFileName;
    private FingerprintUtilsWrapper mWrapper = new FingerprintUtilsWrapper();
    private IFingerprintUtilsExt mFingerprintUtilsExt = (IFingerprintUtilsExt) ExtLoader.type(IFingerprintUtilsExt.class).base(this).create();

    @GuardedBy({"this"})
    private final SparseArray<FingerprintUserState> mUserStates = new SparseArray<>();

    public static boolean isKnownAcquiredCode(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
                return true;
            case 8:
            default:
                return false;
        }
    }

    public static boolean isKnownErrorCode(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    public static FingerprintUtils getInstance(int i) {
        return getInstance(i, LEGACY_FINGERPRINT_FILE);
    }

    private static FingerprintUtils getInstance(int i, String str) {
        FingerprintUtils fingerprintUtils;
        synchronized (sInstanceLock) {
            if (sInstances == null) {
                sInstances = new SparseArray<>();
            }
            if (sInstances.get(i) == null) {
                if (str == null) {
                    str = "settings_fingerprint_" + i + ".xml";
                }
                sInstances.put(i, new FingerprintUtils(str));
            }
            fingerprintUtils = sInstances.get(i);
        }
        return fingerprintUtils;
    }

    public static FingerprintUtils getLegacyInstance(int i) {
        return getInstance(i, LEGACY_FINGERPRINT_FILE);
    }

    private FingerprintUtils(String str) {
        this.mFileName = str;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public List<Fingerprint> getBiometricsForUser(Context context, int i) {
        return getStateForUser(context, i).getBiometrics();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void addBiometricForUser(Context context, int i, Fingerprint fingerprint) {
        getStateForUser(context, i).addBiometric(fingerprint);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void removeBiometricForUser(Context context, int i, int i2) {
        getStateForUser(context, i).removeBiometric(i2);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void renameBiometricForUser(Context context, int i, int i2, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        getStateForUser(context, i).renameBiometric(i2, charSequence);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public CharSequence getUniqueName(Context context, int i) {
        return getStateForUser(context, i).getUniqueName();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void setInvalidationInProgress(Context context, int i, boolean z) {
        getStateForUser(context, i).setInvalidationInProgress(z);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public boolean isInvalidationInProgress(Context context, int i) {
        return getStateForUser(context, i).isInvalidationInProgress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FingerprintUserState getStateForUser(Context context, int i) {
        FingerprintUserState fingerprintUserState;
        int hookTargetUserId = this.mWrapper.getExtImpl().hookTargetUserId(i);
        synchronized (this) {
            fingerprintUserState = this.mUserStates.get(hookTargetUserId);
            if (fingerprintUserState == null) {
                fingerprintUserState = new FingerprintUserState(context, hookTargetUserId, this.mFileName);
                this.mUserStates.put(hookTargetUserId, fingerprintUserState);
            }
        }
        return fingerprintUserState;
    }

    public IFingerprintUtilsWrapper getWrapper() {
        return this.mWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class FingerprintUtilsWrapper implements IFingerprintUtilsWrapper {
        private FingerprintUtilsWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IFingerprintUtilsExt getExtImpl() {
            return FingerprintUtils.this.mFingerprintUtilsExt;
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsWrapper
        public FingerprintUserState getStateForUser(Context context, int i) {
            return FingerprintUtils.this.getStateForUser(context, i);
        }
    }
}
