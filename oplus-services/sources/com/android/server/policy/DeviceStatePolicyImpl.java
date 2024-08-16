package com.android.server.policy;

import android.content.Context;
import com.android.server.devicestate.DeviceStatePolicy;
import com.android.server.devicestate.DeviceStateProvider;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DeviceStatePolicyImpl extends DeviceStatePolicy {
    private final DeviceStateProvider mProvider;

    public DeviceStatePolicyImpl(Context context) {
        super(context);
        this.mProvider = DeviceStateProviderImpl.create(((DeviceStatePolicy) this).mContext);
    }

    public DeviceStateProvider getDeviceStateProvider() {
        return this.mProvider;
    }

    public void configureDeviceForState(int i, Runnable runnable) {
        runnable.run();
    }
}
