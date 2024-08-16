package com.android.server.bluetooth;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusBluetoothManagerServiceExt {
    public static final int DCS_EVT_RECORD_ADAPTER_STATE_CHANGE = 2;
    public static final int DCS_EVT_RECORD_APP_CALL = 1;
    public static final int DCS_EVT_RECORD_ERROR_EVENT = 3;
    public static final int DCS_EVT_SET_BLE_APP_ACCOUNT = 4;
    public static final String FLAG_ENABLE = "enable";
    public static final String FLAG_ENABLE_EXTERNAL = "enable_external";
    public static final String FLAG_QUITE_ENABLE = "quite_enable";
    public static final String NAME = "IOplusBluetoothManagerServiceExt";
    public static final int RECORD_ADAPTER_STATE_CHANGE = 4;
    public static final int RECORD_BLE_START_TIMEOUT = 8;
    public static final int RECORD_BLE_STOP_TIMEOUT = 7;
    public static final int RECORD_BLUETOOTH_CRASH = 17;
    public static final int RECORD_BREDR_CLEANUP_TIMEOUT = 10;
    public static final int RECORD_BREDR_START_TIMEOUT = 5;
    public static final int RECORD_BREDR_STOP_TIMEOUT = 6;
    public static final int RECORD_BT_BIND_FAILURE = 16;
    public static final int RECORD_BT_BIND_TIMEOUT = 13;
    public static final int RECORD_BT_FORCEKILL_TIMEOUT = 11;
    public static final int RECORD_BT_LE_SERVICE_UP_TIMEOUT = 15;
    public static final int RECORD_BT_UNBIND_TIMEOUT = 14;
    public static final int RECORD_CALLED_DISABLE = 19;
    public static final int RECORD_CALLED_ENABLE = 18;
    public static final int RECORD_DISABLE = 2;
    public static final int RECORD_DISABLE_BLE = 21;
    public static final int RECORD_ENABLE = 1;
    public static final int RECORD_ENABLE_BLE = 20;
    public static final int RECORD_ENABLE_QUIET = 3;
    public static final int RECORD_STACK_DISABLE_ERROR = 12;
    public static final int RECORD_STACK_DISABLE_TIMEOUT = 9;

    default void forceKillBluetoothProcess() {
    }

    default boolean isOplusCusomizeBluetoothEnabled() {
        return false;
    }

    default boolean oplusCheckDisablePermitted(String str) {
        return true;
    }

    default boolean oplusCheckEnablePermitted(String str) {
        return true;
    }

    default void oplusClearBleApp(String str) {
    }

    default void oplusDcsEventReport(int i, int i2, int i3, Object obj, Bundle bundle) {
    }

    default void oplusFactoryReset() {
    }

    default void oplusHandleOnBootPhase() {
    }

    default boolean oplusOnTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        return false;
    }

    default boolean oplusPropagateForegroundUserId(int i) {
        return false;
    }

    default void oplusRemoveSaveRemoteNameAndAddressMsg() {
    }

    default boolean oplusSaveRemoteNameAndAddress() {
        return false;
    }

    default void setContext(Context context) {
    }
}
