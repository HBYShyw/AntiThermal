package com.android.server.bluetooth;

import android.annotation.RequiresPermission;
import android.bluetooth.IBluetooth;
import android.content.AttributionSource;
import android.os.Bundle;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBluetoothManagerServiceWrapper {
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_PRIVILEGED"})
    default void OnBrEdrDown(AttributionSource attributionSource) {
    }

    default void clearBleApps() {
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    default void enableBluetooth(boolean z, AttributionSource attributionSource) {
    }

    default IBluetooth getBluetooth() {
        return null;
    }

    default ReentrantReadWriteLock getBluetoothLock() {
        return null;
    }

    default boolean getEnable() {
        return true;
    }

    default Object getHandler() {
        return null;
    }

    default boolean getNameAddressOnly() {
        return true;
    }

    default boolean getQuietEnable() {
        return true;
    }

    default void handleDisable() {
    }

    default void handleEnable(boolean z) {
    }

    default void persistBluetoothSetting(int i) {
    }

    default void propagateForegroundUserId(int i) {
    }

    default void setEnableExternal(boolean z) {
    }

    default void setNameAddressOnly(boolean z) {
    }

    default void storeNameAndAddress(String str, String str2) {
    }

    default void unbindAndFinish() {
    }

    default boolean waitForState(Set<Integer> set) {
        return true;
    }

    default Bundle syncEnableDisableFlag() {
        return new Bundle();
    }
}
