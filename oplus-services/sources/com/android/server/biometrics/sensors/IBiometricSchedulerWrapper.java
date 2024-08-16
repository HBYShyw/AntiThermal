package com.android.server.biometrics.sensors;

import java.util.Deque;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBiometricSchedulerWrapper {
    default BiometricSchedulerOperation getCurrentOperationWrapper() {
        return null;
    }

    default Deque<BiometricSchedulerOperation> getPendingOperationWrapper() {
        return null;
    }

    default IBiometricSchedulerExt getExtImpl() {
        return new IBiometricSchedulerExt() { // from class: com.android.server.biometrics.sensors.IBiometricSchedulerWrapper.1
        };
    }
}
