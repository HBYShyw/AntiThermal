package com.android.server.am;

import android.common.OplusFeatureList;
import android.os.Message;
import com.android.server.IOplusCommonManagerServiceEx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusActivityManagerServiceEx extends IOplusCommonManagerServiceEx {
    public static final IOplusActivityManagerServiceEx DEFAULT = new IOplusActivityManagerServiceEx() { // from class: com.android.server.am.IOplusActivityManagerServiceEx.1
    };
    public static final String NAME = "IOplusActivityManagerServiceEx";

    default void deleteProcInfoArray(int i) {
    }

    default void enableWmShellProtoLogs(String[] strArr, PrintWriter printWriter, FileDescriptor fileDescriptor) {
    }

    default void forceStopPackageWithoutRestart(String str, String str2) {
    }

    default ActivityManagerService getActivityManagerService() {
        return null;
    }

    default ProcessRecord getProcessRecordLocked(String str, int i, boolean z) {
        return null;
    }

    default void handleMessage(Message message, int i) {
    }

    default void putProcInfoArray(int i, int i2) {
    }

    default void startPersistentApp(String str) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusActivityManagerServiceEx;
    }

    default IOplusActivityManagerServiceEx getDefault() {
        return DEFAULT;
    }
}
