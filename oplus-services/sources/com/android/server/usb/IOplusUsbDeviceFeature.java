package com.android.server.usb;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusUsbDeviceFeature extends IOplusCommonFeature {
    public static final IOplusUsbDeviceFeature DEFAULT = new IOplusUsbDeviceFeature() { // from class: com.android.server.usb.IOplusUsbDeviceFeature.1
    };
    public static final String NAME = "IOplusUsbDeviceFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusUsbDeviceFeature;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void init(boolean z, Context context) {
        Log.d(NAME, "default init");
    }

    default boolean getMuiltiUserSwitch() {
        Log.d(NAME, "default getMuiltiUserSwitch");
        return false;
    }

    default void setMuiltiUserSwitch(boolean z) {
        Log.d(NAME, "default setMuiltiUserSwitch");
    }

    default void setUsbAccessoryStartFlag() {
        Log.d(NAME, "default setUsbAccessoryStartFlag");
    }

    default void initUsbAccessoryStartFlag() {
        Log.d(NAME, "default initUsbAccessoryStartFlag");
    }

    default boolean getUsbAccessoryStartFlag(long j) {
        Log.d(NAME, "default getUsbAccessoryStartFlag");
        return false;
    }

    default void initUsbPlugFlag() {
        Log.d(NAME, "default initUsbPlugFlag");
    }

    default void setOplusUsbDeviceManagerCallback(IOplusUsbDeviceManagerCallback iOplusUsbDeviceManagerCallback) {
        Log.d(NAME, "default setOplusUsbDeviceManagerCallback");
    }

    default void setUsbPlugFlag(boolean z) {
        Log.d(NAME, "default setUsbPlugFlag");
    }

    default boolean usbFunctionsShouldForceStart() {
        Log.d(NAME, "default usbFunctionsShouldForceStart");
        return false;
    }

    default boolean usbFunctionsShuoldUseDefault(String str) {
        Log.d(NAME, "default usbFunctionsShuoldUseDefault");
        return false;
    }

    default void printFinishBootInfo(OplusUsbDeviceFinishBootInfo oplusUsbDeviceFinishBootInfo) {
        Log.d(NAME, "default printFinishBootInfo");
    }

    default long getChargingFunctions() {
        Log.d(NAME, "default getChargingFunctions");
        return 0L;
    }

    default boolean isTelecomRequirement(String str) {
        Log.d(NAME, "default isTelecomRequirement");
        return false;
    }

    default void printBootModeForDebug(String str) {
        Log.d(NAME, "default printBootModeForDebug");
    }

    default boolean isNormalBoot() {
        Log.d(NAME, "default isNormalBoot");
        return false;
    }

    default boolean ignoreCTARequirement(String str) {
        Log.d(NAME, "default IgnoreCTARequirement");
        return true;
    }

    default boolean usbFunctionsNotForceRestart(String str) {
        Log.d(NAME, "default usbFunctionsNotForceRestart");
        return false;
    }

    default String resetFunctionsForCTA(String str) {
        Log.d(NAME, "default resetFunctionsForCTA");
        return null;
    }

    default void printFunctionsForDebug(OplusUsbDeviceFunctionInfo oplusUsbDeviceFunctionInfo) {
        Log.d(NAME, "default printFunctionsForDebug");
    }

    default void usbConfigRecord(String str) {
        Log.d(NAME, "default usbConfigRecord");
    }

    default void usbHostRecord(Context context, UsbDevice usbDevice) {
        Log.d(NAME, "default usbHostRecord");
    }

    default void processUserTestHarnessIfNeed(Context context) {
        Log.d(NAME, "default processUserTestHarnessIfNeed");
    }

    default long usbTetheringSwitchOffFunctions(long j, String str) {
        Log.d(NAME, "default usbTetheringSwitchOffFunctions");
        return 0L;
    }

    default boolean isUsbTetheringDisabled(Context context) {
        Log.d(NAME, "default isUsbTetheringDisabled");
        return false;
    }
}
