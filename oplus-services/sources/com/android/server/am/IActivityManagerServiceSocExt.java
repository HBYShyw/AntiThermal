package com.android.server.am;

import android.os.Handler;
import android.os.Message;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IActivityManagerServiceSocExt {
    default void addAnrManagerService() {
    }

    default void addPidLocked(ProcessRecord processRecord) {
    }

    default void appDiedLocked(ProcessRecord processRecord, int i) {
    }

    default void compactAllSystem() {
    }

    default boolean delayMessage(Handler handler, Message message, int i, int i2) {
        return false;
    }

    default Object getAmsExt() {
        return null;
    }

    default Object getAnrManager() {
        return null;
    }

    default boolean isAnrDeferrable() {
        return false;
    }

    default void onAddErrorToDropBox(String str, String str2, int i) {
    }

    default void onNotifyAppCrash(int i, int i2, String str) {
    }

    default void perfHint(ProcessRecord processRecord, int i) {
    }

    default void removePidLocked(ProcessRecord processRecord) {
    }

    default void startAnrManagerService(int i) {
    }

    default void updateForceStopKillFlag() {
    }

    default void writeBootCompletedEvent() {
    }
}
