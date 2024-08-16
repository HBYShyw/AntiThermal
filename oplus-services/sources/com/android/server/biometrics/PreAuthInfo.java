package com.android.server.biometrics;

import android.app.admin.DevicePolicyManager;
import android.app.trust.ITrustManager;
import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.PromptInfo;
import android.os.RemoteException;
import android.util.Pair;
import android.util.Slog;
import com.android.server.biometrics.BiometricService;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PreAuthInfo {
    static final int AUTHENTICATOR_OK = 1;
    static final int BIOMETRIC_DISABLED_BY_DEVICE_POLICY = 3;
    static final int BIOMETRIC_HARDWARE_NOT_DETECTED = 6;
    static final int BIOMETRIC_INSUFFICIENT_STRENGTH = 4;
    static final int BIOMETRIC_INSUFFICIENT_STRENGTH_AFTER_DOWNGRADE = 5;
    static final int BIOMETRIC_LOCKOUT_PERMANENT = 11;
    static final int BIOMETRIC_LOCKOUT_TIMED = 10;
    static final int BIOMETRIC_NOT_ENABLED_FOR_APPS = 8;
    static final int BIOMETRIC_NOT_ENROLLED = 7;
    static final int BIOMETRIC_NO_HARDWARE = 2;
    static final int BIOMETRIC_SENSOR_PRIVACY_ENABLED = 12;
    static final int CREDENTIAL_NOT_ENROLLED = 9;
    private static final String TAG = "BiometricService/PreAuthInfo";
    private static IPreAuthInfoExt mPreAuthInfoExt = (IPreAuthInfoExt) ExtLoader.type(IPreAuthInfoExt.class).create();
    final boolean confirmationRequested;
    final Context context;
    final boolean credentialAvailable;
    final boolean credentialRequested;
    final List<BiometricSensor> eligibleSensors;
    final boolean ignoreEnrollmentState;
    final List<Pair<BiometricSensor, Integer>> ineligibleSensors;
    private final boolean mBiometricRequested;
    private final int mBiometricStrengthRequested;
    final int userId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface AuthenticatorStatus {
    }

    private PreAuthInfo(boolean z, int i, boolean z2, List<BiometricSensor> list, List<Pair<BiometricSensor, Integer>> list2, boolean z3, boolean z4, boolean z5, int i2, Context context) {
        this.mBiometricRequested = z;
        this.mBiometricStrengthRequested = i;
        this.credentialRequested = z2;
        this.eligibleSensors = list;
        this.ineligibleSensors = list2;
        this.credentialAvailable = z3;
        this.confirmationRequested = z4;
        this.ignoreEnrollmentState = z5;
        this.userId = i2;
        this.context = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PreAuthInfo create(ITrustManager iTrustManager, DevicePolicyManager devicePolicyManager, BiometricService.SettingObserver settingObserver, List<BiometricSensor> list, int i, PromptInfo promptInfo, String str, boolean z, Context context) throws RemoteException {
        BiometricSensor biometricSensor;
        boolean z2;
        String str2;
        boolean isConfirmationRequested = promptInfo.isConfirmationRequested();
        boolean isBiometricRequested = Utils.isBiometricRequested(promptInfo);
        int publicBiometricStrength = Utils.getPublicBiometricStrength(promptInfo);
        boolean isCredentialRequested = Utils.isCredentialRequested(promptInfo);
        boolean isDeviceSecure = iTrustManager.isDeviceSecure(i, context.getAssociatedDisplayId());
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (isBiometricRequested) {
            for (BiometricSensor biometricSensor2 : list) {
                ArrayList arrayList3 = arrayList2;
                boolean z3 = isConfirmationRequested;
                ArrayList arrayList4 = arrayList;
                int statusForBiometricAuthenticator = getStatusForBiometricAuthenticator(devicePolicyManager, settingObserver, biometricSensor2, i, str, z, publicBiometricStrength, promptInfo.getAllowedSensorIds(), promptInfo.isIgnoreEnrollmentState(), context);
                IPreAuthInfoExt iPreAuthInfoExt = mPreAuthInfoExt;
                if (iPreAuthInfoExt != null) {
                    z2 = isCredentialRequested;
                    str2 = TAG;
                    if (iPreAuthInfoExt.needSkipEligibleSensorAdd(biometricSensor2, i, str, context, promptInfo)) {
                        arrayList3.add(new Pair(biometricSensor2, 8));
                        Slog.d(str2, "add ineligibleSensors, Package: " + str + " Sensor ID: " + biometricSensor2.id + " Modality: " + biometricSensor2.modality + " Status: " + statusForBiometricAuthenticator);
                        arrayList = arrayList4;
                        arrayList2 = arrayList3;
                        isConfirmationRequested = z3;
                        isCredentialRequested = z2;
                    } else {
                        biometricSensor = biometricSensor2;
                    }
                } else {
                    biometricSensor = biometricSensor2;
                    z2 = isCredentialRequested;
                    str2 = TAG;
                }
                Slog.d(str2, "Package: " + str + " Sensor ID: " + biometricSensor.id + " Modality: " + biometricSensor.modality + " Status: " + statusForBiometricAuthenticator);
                if (statusForBiometricAuthenticator == 1 || statusForBiometricAuthenticator == 12) {
                    arrayList4.add(biometricSensor);
                } else {
                    arrayList3.add(new Pair(biometricSensor, Integer.valueOf(statusForBiometricAuthenticator)));
                }
                arrayList = arrayList4;
                arrayList2 = arrayList3;
                isConfirmationRequested = z3;
                isCredentialRequested = z2;
            }
        }
        return new PreAuthInfo(isBiometricRequested, publicBiometricStrength, isCredentialRequested, arrayList, arrayList2, isDeviceSecure, isConfirmationRequested, promptInfo.isIgnoreEnrollmentState(), i, context);
    }

    private static int getStatusForBiometricAuthenticator(DevicePolicyManager devicePolicyManager, BiometricService.SettingObserver settingObserver, BiometricSensor biometricSensor, int i, String str, boolean z, int i2, List<Integer> list, boolean z2, Context context) {
        if (!list.isEmpty() && !list.contains(Integer.valueOf(biometricSensor.id))) {
            return 2;
        }
        boolean isAtLeastStrength = Utils.isAtLeastStrength(biometricSensor.oemStrength, i2);
        boolean isAtLeastStrength2 = Utils.isAtLeastStrength(biometricSensor.getCurrentStrength(), i2);
        if (isAtLeastStrength && !isAtLeastStrength2) {
            return 5;
        }
        if (!isAtLeastStrength) {
            return 4;
        }
        if ("com.coloros.codebook".equals(str) && biometricSensor.modality == 8) {
            Slog.d(TAG, "Package: " + str + " Sensor ID: " + biometricSensor.id + " Modality: " + biometricSensor.modality + " BIOMETRIC_INSUFFICIENT_STRENGTH");
            return 4;
        }
        int i3 = 6;
        if (!biometricSensor.impl.isHardwareDetected(str)) {
            return 6;
        }
        if (!biometricSensor.impl.hasEnrolledTemplates(i, str) && !z2) {
            return 7;
        }
        SensorPrivacyManager sensorPrivacyManager = (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
        if (sensorPrivacyManager != null && biometricSensor.modality == 8 && sensorPrivacyManager.isSensorPrivacyEnabled(2, i)) {
            return 12;
        }
        int lockoutModeForUser = biometricSensor.impl.getLockoutModeForUser(i);
        i3 = 1;
        if (lockoutModeForUser == 1) {
            return 10;
        }
        if (lockoutModeForUser == 2) {
            return 11;
        }
        if (!isEnabledForApp(settingObserver, biometricSensor.modality, i)) {
            return 8;
        }
        if (z && isBiometricDisabledByDevicePolicy(devicePolicyManager, biometricSensor.modality, i)) {
            return 3;
        }
        return i3;
    }

    private static boolean isEnabledForApp(BiometricService.SettingObserver settingObserver, int i, int i2) {
        return settingObserver.getEnabledForApps(i2);
    }

    private static boolean isBiometricDisabledByDevicePolicy(DevicePolicyManager devicePolicyManager, int i, int i2) {
        int mapModalityToDevicePolicyType = mapModalityToDevicePolicyType(i);
        if (mapModalityToDevicePolicyType == 0) {
            throw new IllegalStateException("Modality unknown to devicePolicyManager: " + i);
        }
        boolean z = (devicePolicyManager.getKeyguardDisabledFeatures(null, i2) & mapModalityToDevicePolicyType) != 0;
        Slog.w(TAG, "isBiometricDisabledByDevicePolicy(" + i + "," + i2 + ")=" + z);
        return z;
    }

    private static int mapModalityToDevicePolicyType(int i) {
        if (i == 2) {
            return 32;
        }
        if (i == 4) {
            return 256;
        }
        if (i == 8) {
            return 128;
        }
        Slog.e(TAG, "Error modality=" + i);
        return 0;
    }

    private Pair<BiometricSensor, Integer> calculateErrorByPriority() {
        for (Pair<BiometricSensor, Integer> pair : this.ineligibleSensors) {
            if (((Integer) pair.second).intValue() == 7) {
                return pair;
            }
        }
        return this.ineligibleSensors.get(0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0073, code lost:
    
        if (r0 != false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x009a, code lost:
    
        r1 = 12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0098, code lost:
    
        if (r0 != false) goto L38;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Pair<Integer, Integer> getInternalStatus() {
        SensorPrivacyManager sensorPrivacyManager = (SensorPrivacyManager) this.context.getSystemService(SensorPrivacyManager.class);
        int i = 2;
        int i2 = 0;
        boolean isSensorPrivacyEnabled = sensorPrivacyManager != null ? sensorPrivacyManager.isSensorPrivacyEnabled(2, this.userId) : false;
        boolean z = this.mBiometricRequested;
        if (z && this.credentialRequested) {
            if (this.credentialAvailable || !this.eligibleSensors.isEmpty()) {
                Iterator<BiometricSensor> it = this.eligibleSensors.iterator();
                while (it.hasNext()) {
                    i2 |= it.next().modality;
                }
                if (this.credentialAvailable) {
                    i2 |= 1;
                } else if (i2 == 8) {
                }
                i = 1;
            } else {
                if (!this.ineligibleSensors.isEmpty()) {
                    Pair<BiometricSensor, Integer> calculateErrorByPriority = calculateErrorByPriority();
                    i2 = 0 | ((BiometricSensor) calculateErrorByPriority.first).modality;
                    i = ((Integer) calculateErrorByPriority.second).intValue();
                }
                i = 9;
                i2 = 1;
            }
        } else if (z) {
            if (!this.eligibleSensors.isEmpty()) {
                Iterator<BiometricSensor> it2 = this.eligibleSensors.iterator();
                while (it2.hasNext()) {
                    i2 |= it2.next().modality;
                }
                if (i2 == 8) {
                }
                i = 1;
            } else if (!this.ineligibleSensors.isEmpty()) {
                Pair<BiometricSensor, Integer> calculateErrorByPriority2 = calculateErrorByPriority();
                i2 = 0 | ((BiometricSensor) calculateErrorByPriority2.first).modality;
                i = ((Integer) calculateErrorByPriority2.second).intValue();
            }
        } else if (this.credentialRequested) {
            if (this.credentialAvailable) {
                i = 1;
                i2 = 1;
            }
            i = 9;
            i2 = 1;
        } else {
            Slog.e(TAG, "No authenticators requested");
        }
        Slog.d(TAG, "getCanAuthenticateInternal Modality: " + i2 + " AuthenticatorStatus: " + i);
        return new Pair<>(Integer.valueOf(i2), Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCanAuthenticateResult() {
        return Utils.biometricConstantsToBiometricManager(Utils.authenticatorStatusToBiometricConstant(((Integer) getInternalStatus().second).intValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<Integer, Integer> getPreAuthenticateStatus() {
        Pair<Integer, Integer> internalStatus = getInternalStatus();
        int authenticatorStatusToBiometricConstant = Utils.authenticatorStatusToBiometricConstant(((Integer) internalStatus.second).intValue());
        int intValue = ((Integer) internalStatus.first).intValue();
        switch (((Integer) internalStatus.second).intValue()) {
            case 1:
            case 2:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
                break;
            case 3:
            case 4:
            case 8:
            default:
                intValue = 0;
                break;
        }
        return new Pair<>(Integer.valueOf(intValue), Integer.valueOf(authenticatorStatusToBiometricConstant));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldShowCredential() {
        return this.credentialRequested && this.credentialAvailable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getEligibleModalities() {
        Iterator<BiometricSensor> it = this.eligibleSensors.iterator();
        int i = 0;
        while (it.hasNext()) {
            i |= it.next().modality;
        }
        return (this.credentialRequested && this.credentialAvailable) ? i | 1 : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numSensorsWaitingForCookie() {
        int i = 0;
        for (BiometricSensor biometricSensor : this.eligibleSensors) {
            if (biometricSensor.getSensorState() == 1) {
                Slog.d(TAG, "Sensor ID: " + biometricSensor.id + " Waiting for cookie: " + biometricSensor.getCookie());
                i++;
            }
        }
        return i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("BiometricRequested: " + this.mBiometricRequested + ", StrengthRequested: " + this.mBiometricStrengthRequested + ", CredentialRequested: " + this.credentialRequested);
        sb.append(", Eligible:{");
        Iterator<BiometricSensor> it = this.eligibleSensors.iterator();
        while (it.hasNext()) {
            sb.append(it.next().id);
            sb.append(" ");
        }
        sb.append("}");
        sb.append(", Ineligible:{");
        for (Pair<BiometricSensor, Integer> pair : this.ineligibleSensors) {
            sb.append(pair.first);
            sb.append(":");
            sb.append(pair.second);
            sb.append(" ");
        }
        sb.append("}");
        sb.append(", CredentialAvailable: ");
        sb.append(this.credentialAvailable);
        sb.append(", ");
        return sb.toString();
    }
}
