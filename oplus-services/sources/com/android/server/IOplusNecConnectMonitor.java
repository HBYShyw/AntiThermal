package com.android.server;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusNecConnectMonitor extends IOplusCommonFeature {
    public static final IOplusNecConnectMonitor DEFAULT = new IOplusNecConnectMonitor() { // from class: com.android.server.IOplusNecConnectMonitor.1
    };
    public static final String NAME = "IOplusNecConnectMonitor";

    default void addDnsRecord(int i, int i2, int i3, long j, long j2) {
    }

    default void addHttpRecord(String str, int i) {
    }

    default void addNetStackRecord(int i, String str, boolean z) {
    }

    default void addTcpStateRecord(int i, int i2, int i3, int i4) {
    }

    default void addTcpSynRecord(int i, int i2, int i3, int i4, long j, long j2) {
    }

    default Executor getNecExecutor() {
        return null;
    }

    default void onConnectEvent(int i, int i2, int i3, String str, int i4, int i5) {
    }

    default void onDnsEvent(int i, int i2, int i3, int i4, String str, String[] strArr, int i5, int i6) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusNecConnectMonitor;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
