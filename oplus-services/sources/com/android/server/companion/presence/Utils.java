package com.android.server.companion.presence;

import android.bluetooth.BluetoothDevice;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class Utils {
    static String btDeviceToString(BluetoothDevice bluetoothDevice) {
        StringBuilder sb = new StringBuilder(bluetoothDevice.getAddress());
        sb.append(" [name=");
        String name = bluetoothDevice.getName();
        if (name != null) {
            sb.append('\'');
            sb.append(name);
            sb.append('\'');
        } else {
            sb.append("null");
        }
        String alias = bluetoothDevice.getAlias();
        if (alias != null) {
            sb.append(", alias='");
            sb.append(alias);
            sb.append("'");
        }
        sb.append(']');
        return sb.toString();
    }

    private Utils() {
    }
}
