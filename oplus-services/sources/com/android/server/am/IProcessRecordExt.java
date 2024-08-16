package com.android.server.am;

import android.content.Context;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessRecordExt {
    public static final int UX_STATE_BG = 1;
    public static final int UX_STATE_DEFAULT = 0;
    public static final int UX_STATE_TOP = 3;
    public static final int UX_STATE_VISIBLE = 2;

    default void callOrmsSetSceneActionForRemoteAnimation(boolean z) {
    }

    default void createProcessInfo(ProcessRecord processRecord) {
    }

    default void detectForgroundExceptions(String[] strArr, Context context, int i, String str) {
    }

    default void dump(PrintWriter printWriter, String str, long j) {
    }

    default String getAnrAnnotation() {
        return null;
    }

    default String getExecutingCpn() {
        return null;
    }

    default boolean getIsANR() {
        return false;
    }

    default int getOldSchedGroup() {
        return 0;
    }

    default int getOplusReceiverRecordListSize() {
        return 0;
    }

    default int getUxState() {
        return 0;
    }

    default boolean getVirtualFreeze() {
        return false;
    }

    default boolean isExplicitDisableRestart() {
        return false;
    }

    default boolean isRPLaunch() {
        return false;
    }

    default boolean isWaitingPermissionChoice() {
        return false;
    }

    default void resetUxState() {
    }

    default void saveAmKillRecordToList(long j, int i, String str, String str2) {
    }

    default void setAnrAnnotation(String str) {
    }

    default void setExplicitDisableRestart(boolean z) {
    }

    default void setIsANR(boolean z) {
    }

    default void setOldSchedGroup(int i) {
    }

    default void setRPLaunch(boolean z) {
    }

    default void setUxState(int i) {
    }

    default void setVirtualFreeze(boolean z) {
    }

    default void setWaitingPermissionChoice(boolean z) {
    }

    default void updateExecutingComponent(ProcessRecord processRecord, String str, int i) {
    }

    default void updateProcessState(ProcessRecord processRecord, int i) {
    }
}
