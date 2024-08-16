package com.android.server;

import android.content.Context;
import com.android.server.am.ActivityManagerService;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IWatchdogExt {
    default void addBinderPid(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2, int i) {
    }

    default void addStabilityDebugInAll(boolean z, File file, String str) {
    }

    default void addWatchdogExtNativePids(HashSet<Integer> hashSet) {
    }

    default void catchPsAndBinderinfos() {
    }

    default boolean checkIfNeedCloseWdt() {
        return false;
    }

    default void checkSystemHeapMem() {
    }

    default void dumpStackAndAddDropbox(String str) {
    }

    default void eventDailyPush() {
    }

    default void getBinderBlockTimeMS() {
    }

    default void init(Context context, ActivityManagerService activityManagerService) {
    }

    default boolean isSkipAnrDump() {
        return false;
    }

    default void killMultimediaProcess() {
    }

    default void onProcessBinderCnt() {
    }

    default void processStarted(String str, int i) {
    }

    default void removeTheiaMsg() {
    }

    default void sendTheiaMsg() {
    }

    default void setWatchdogHappenValue(boolean z) {
    }

    default boolean shouldGotoDump() {
        return false;
    }

    default void triggerDetect() {
    }

    default void unfreezeForWatchdog() {
    }

    default void writeEvent(String str) {
    }
}
