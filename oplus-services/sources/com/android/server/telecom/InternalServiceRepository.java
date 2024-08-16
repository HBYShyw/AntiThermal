package com.android.server.telecom;

import android.os.Binder;
import android.os.Process;
import com.android.internal.telecom.IDeviceIdleControllerAdapter;
import com.android.internal.telecom.IInternalServiceRetriever;
import com.android.server.DeviceIdleInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class InternalServiceRepository extends IInternalServiceRetriever.Stub {
    private final DeviceIdleInternal mDeviceIdleController;
    private final IDeviceIdleControllerAdapter.Stub mDeviceIdleControllerAdapter = new IDeviceIdleControllerAdapter.Stub() { // from class: com.android.server.telecom.InternalServiceRepository.1
        public void exemptAppTemporarilyForEvent(String str, long j, int i, String str2) {
            InternalServiceRepository.this.mDeviceIdleController.addPowerSaveTempWhitelistApp(Process.myUid(), str, j, i, true, 0, str2);
        }
    };

    public InternalServiceRepository(DeviceIdleInternal deviceIdleInternal) {
        this.mDeviceIdleController = deviceIdleInternal;
    }

    public IDeviceIdleControllerAdapter getDeviceIdleController() {
        ensureSystemProcess();
        return this.mDeviceIdleControllerAdapter;
    }

    private void ensureSystemProcess() {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("SYSTEM ONLY API.");
        }
    }
}
