package com.android.server;

import android.content.Context;
import com.android.server.am.ActivityManagerService;
import com.android.server.input.InputManagerService;
import com.android.server.pm.PackageManagerService;
import com.android.server.policy.PhoneWindowManager;
import com.android.server.utils.TimingsTraceAndSlog;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISystemServerExt {
    default void addCabcService(Context context, TimingsTraceAndSlog timingsTraceAndSlog) {
    }

    default void addCrossDeviceService(Context context, ActivityManagerService activityManagerService, TimingsTraceAndSlog timingsTraceAndSlog) {
    }

    default void addLinearmotorVibratorService(Context context) {
    }

    default void addOplusDevicePolicyService() {
    }

    default void addOplusTestService(Context context) {
    }

    default void addOplusTileManagerService(Context context) {
    }

    default void addOtaDexoptService(Context context, PackageManagerService packageManagerService) {
    }

    default void addRenderAcceleratingService(Context context, TimingsTraceAndSlog timingsTraceAndSlog) {
    }

    default void addStorageHealthInfoService(Context context) {
    }

    default void dynamicFilterServiceSystemReady(TimingsTraceAndSlog timingsTraceAndSlog) {
    }

    default int getSystemThemeStyle() {
        return -1;
    }

    default void initBadBehaviorDefense(Context context) {
    }

    default void initFontsForserializeFontMap() {
    }

    default void initSystemServer(Context context) {
    }

    default void linearVibratorSystemReady() {
    }

    default void runBootProtector(int i) {
    }

    default void setBootstage(boolean z) {
    }

    default void setDataNormalizationManager() {
    }

    default boolean stabilityDynamicLogConfig(PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default void startBootstrapServices() {
    }

    default void startCoreServices() {
    }

    default void startDynamicFilterService(SystemServiceManager systemServiceManager) {
    }

    default boolean startJobSchedulerService() {
        return false;
    }

    default void startOtherServices() {
    }

    default void startUsageStatsService(SystemServiceManager systemServiceManager) {
    }

    default void systemReady() {
    }

    default void systemRunning() {
    }

    default void waitForFutureNoInterrupt() {
    }

    default void writeAgingCriticalEvent() {
    }

    default InputManagerService getInputManagerService(Context context) {
        return new InputManagerService(context);
    }

    default PhoneWindowManager getSubPhoneWindowManager() {
        return new PhoneWindowManager();
    }
}
