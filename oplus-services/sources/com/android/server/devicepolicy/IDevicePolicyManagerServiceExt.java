package com.android.server.devicepolicy;

import android.content.ComponentName;
import android.content.Context;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDevicePolicyManagerServiceExt {
    default int checkPakcageState(Context context, ComponentName componentName) {
        return 0;
    }

    default String getCustCallerPackage(ComponentName componentName, String str) {
        return str;
    }

    default boolean isCustInvokeInterface(Context context) {
        return false;
    }

    default boolean isCustomDevicePolicyEnabled(Context context) {
        return false;
    }

    default boolean isDisabledDeactivateMdmPackage(Context context, String str) {
        return false;
    }

    default boolean shouldShiftToNullParamForGetCallerIdentity(Context context, ComponentName componentName) {
        return false;
    }

    default boolean shouldSkipDumpPerUserData() {
        return false;
    }

    default void updateSwitchState(boolean z) {
    }

    default void updateWhiteList(List<String> list) {
    }
}
