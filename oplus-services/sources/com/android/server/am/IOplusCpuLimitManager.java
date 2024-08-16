package com.android.server.am;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.os.Bundle;
import android.os.Message;
import android.os.SharedMemory;
import com.android.server.oplus.osense.IntegratedData;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusCpuLimitManager extends IOplusCommonFeature {
    public static final IOplusCpuLimitManager DEFAULT = new IOplusCpuLimitManager() { // from class: com.android.server.am.IOplusCpuLimitManager.1
    };
    public static final int INVALID_GROUP = -10000;

    default boolean cpuSetEnable() {
        return false;
    }

    default void createProcessInfo(ProcessRecord processRecord) {
    }

    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default SharedMemory getCpuLimitLatestLog(String str) {
        return null;
    }

    default boolean isOclGrpRequestMsgAndSetGroup(Message message) {
        return false;
    }

    default void notifyAppStatusByOSense(IntegratedData integratedData) {
    }

    default void notifyProcessGroupChange(ProcessRecord processRecord, int i, int i2, int i3) {
    }

    default void onInit(IOplusActivityManagerServiceEx iOplusActivityManagerServiceEx) {
    }

    default void removeProcessInfo(ProcessRecord processRecord) {
    }

    default boolean skipSetSchedGroup(ProcessRecord processRecord, String str) {
        return false;
    }

    default void updateConfigList(Bundle bundle) {
    }

    default void updateProcessState(ProcessRecord processRecord, int i) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCpuLimitManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
