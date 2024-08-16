package com.android.server.am;

import android.app.ApplicationErrorReport;
import android.content.Context;
import com.android.server.am.AppErrorDialog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAppErrorsExt {
    default void doErrorsStatistics(Context context, ProcessRecord processRecord, ApplicationErrorReport.CrashInfo crashInfo) {
    }

    default void doUpload(Context context, String str, String str2, String str3) {
    }

    default long getVmSize(int i, String str) {
        return 0L;
    }

    default String handleAnrAnnotation(ProcessRecord processRecord) {
        return "";
    }

    default void hookHandleAppCrashBegin(ProcessRecord processRecord) {
    }

    default void hookHandleShowAppErrorUi(ProcessErrorStateRecord processErrorStateRecord, AppErrorResult appErrorResult, AppErrorDialog.Data data) {
    }

    default boolean isAppForeground(String str) {
        return false;
    }

    default boolean isPersistProcessRestarting(ProcessRecord processRecord, ActivityManagerService activityManagerService) {
        return false;
    }

    default boolean isShowDialog() {
        return false;
    }

    default boolean isShowOriAnrDialog(ProcessRecord processRecord) {
        return false;
    }

    default boolean isThreadGroupLeader(String str, int i) {
        return false;
    }
}
