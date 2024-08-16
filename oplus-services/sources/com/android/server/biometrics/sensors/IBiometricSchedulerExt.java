package com.android.server.biometrics.sensors;

import android.os.Handler;
import android.os.IBinder;
import java.util.Deque;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBiometricSchedulerExt {
    default void cancelExpandClientIfNeed(BaseClientMonitor baseClientMonitor, BiometricSchedulerOperation biometricSchedulerOperation) {
    }

    default Handler createHandlerWithNewLooper() {
        return null;
    }

    default void setPendingClientToCancelState(BiometricSchedulerOperation biometricSchedulerOperation, Deque<BiometricSchedulerOperation> deque, IBinder iBinder) {
    }

    default void tryToCancelPendingClient(Deque<BiometricSchedulerOperation> deque, IBinder iBinder) {
    }
}
