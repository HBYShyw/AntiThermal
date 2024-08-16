package com.android.server;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import com.android.server.twilight.TwilightManager;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IUiModeManagerServiceExt {
    default void darkModeDumpUiModeManagerServiceMessage(PrintWriter printWriter, TwilightManager twilightManager) {
    }

    default boolean darkModeGetAutoFirst() {
        return true;
    }

    default int darkModeGetSuperSaveUiMode(int i) {
        return i;
    }

    default void darkModeInitSettings(Context context) {
    }

    default boolean darkModeIsSuperSaveMode() {
        return false;
    }

    default void darkModeNightModeChange(UiModeManagerService uiModeManagerService, int i, int i2) {
    }

    default void darkModeOnStartInit(Context context, UiModeManagerService uiModeManagerService) {
    }

    default boolean darkModeOverrideComputedNightMode(int i, boolean z, boolean z2) {
        return z2;
    }

    default void darkModeRegisterShutdownReceiver(UiModeManagerService uiModeManagerService, Handler handler) {
    }

    default void darkModeRegisterThermalProtect(UiModeManagerService uiModeManagerService) {
    }

    default void darkModeSetValueForState(Context context, int i, int i2) {
    }

    default boolean darkModeShouldHideSaveMode() {
        return false;
    }

    default void fontUpdateConfigurationInUIMode(Context context, Configuration configuration, int i) {
    }

    default void init(Context context, UiModeManagerService uiModeManagerService) {
    }

    default void notifyFlingerUiMode(int i) {
    }

    default void persistNightModeStatistics(Context context, int i) {
    }

    default void upCommonStatistics(Context context, int i, int i2, int i3) {
    }
}
