package com.android.server.utils;

import android.provider.DeviceConfig;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface DeviceConfigInterface {
    public static final DeviceConfigInterface REAL = new DeviceConfigInterface() { // from class: com.android.server.utils.DeviceConfigInterface.1
        @Override // com.android.server.utils.DeviceConfigInterface
        public String getProperty(String str, String str2) {
            return DeviceConfig.getProperty(str, str2);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public String getString(String str, String str2, String str3) {
            return DeviceConfig.getString(str, str2, str3);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public int getInt(String str, String str2, int i) {
            return DeviceConfig.getInt(str, str2, i);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public long getLong(String str, String str2, long j) {
            return DeviceConfig.getLong(str, str2, j);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public boolean getBoolean(String str, String str2, boolean z) {
            return DeviceConfig.getBoolean(str, str2, z);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public float getFloat(String str, String str2, float f) {
            return DeviceConfig.getFloat(str, str2, f);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public void addOnPropertiesChangedListener(String str, Executor executor, DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
            DeviceConfig.addOnPropertiesChangedListener(str, executor, onPropertiesChangedListener);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public void removeOnPropertiesChangedListener(DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
            DeviceConfig.removeOnPropertiesChangedListener(onPropertiesChangedListener);
        }
    };

    void addOnPropertiesChangedListener(String str, Executor executor, DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener);

    boolean getBoolean(String str, String str2, boolean z);

    float getFloat(String str, String str2, float f);

    int getInt(String str, String str2, int i);

    long getLong(String str, String str2, long j);

    String getProperty(String str, String str2);

    String getString(String str, String str2, String str3);

    void removeOnPropertiesChangedListener(DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener);
}
