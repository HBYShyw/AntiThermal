package com.android.server.hdmi;

import android.provider.DeviceConfig;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DeviceConfigWrapper {
    private static final String TAG = "DeviceConfigWrapper";

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getBoolean(String str, boolean z) {
        return DeviceConfig.getBoolean("hdmi_control", str, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOnPropertiesChangedListener(Executor executor, DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        DeviceConfig.addOnPropertiesChangedListener("hdmi_control", executor, onPropertiesChangedListener);
    }
}
