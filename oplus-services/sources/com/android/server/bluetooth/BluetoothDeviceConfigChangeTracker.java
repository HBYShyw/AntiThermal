package com.android.server.bluetooth;

import android.provider.DeviceConfig;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BluetoothDeviceConfigChangeTracker {
    private static final String TAG = "BluetoothDeviceConfigChangeTracker";
    private final HashMap<String, String> mCurrFlags;

    public BluetoothDeviceConfigChangeTracker(DeviceConfig.Properties properties) {
        this.mCurrFlags = getFlags(properties);
    }

    public boolean shouldRestartWhenPropertiesUpdated(DeviceConfig.Properties properties) {
        boolean z = false;
        if (!properties.getNamespace().equals("bluetooth")) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : properties.getKeyset()) {
            arrayList.add(str + "='" + properties.getString(str, "") + "'");
        }
        Log.d(TAG, "shouldRestartWhenPropertiesUpdated: " + String.join(",", arrayList));
        for (String str2 : properties.getKeyset()) {
            if (isInitFlag(str2).booleanValue()) {
                String str3 = this.mCurrFlags.get(str2);
                String string = properties.getString(str2, "");
                if (!string.equals(str3)) {
                    Log.d(TAG, "Property " + str2 + " changed from " + str3 + " -> " + string);
                    this.mCurrFlags.put(str2, string);
                    z = true;
                }
            }
        }
        return z;
    }

    private HashMap<String, String> getFlags(DeviceConfig.Properties properties) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str : properties.getKeyset()) {
            if (isInitFlag(str).booleanValue()) {
                hashMap.put(str, properties.getString(str, ""));
            }
        }
        return hashMap;
    }

    private Boolean isInitFlag(String str) {
        return Boolean.valueOf(str.startsWith("INIT_"));
    }
}
