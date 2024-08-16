package com.android.server.location.contexthub;

import android.util.StatsEvent;
import android.util.StatsLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ContextHubStatsLog {
    public static final byte ANNOTATION_ID_DEFAULT_STATE = 6;
    public static final byte ANNOTATION_ID_EXCLUSIVE_STATE = 4;
    public static final byte ANNOTATION_ID_IS_UID = 1;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD = 3;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD_FIRST_UID = 5;
    public static final byte ANNOTATION_ID_STATE_NESTED = 8;
    public static final byte ANNOTATION_ID_TRIGGER_STATE_RESET = 7;
    public static final byte ANNOTATION_ID_TRUNCATE_TIMESTAMP = 2;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED = 401;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_AT_HUB = 5;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_BAD_PARAMS = 2;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_BUSY = 4;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_HAL_UNAVAILABLE = 8;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_SERVICE_INTERNAL_FAILURE = 7;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_TIMEOUT = 6;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_UNINITIALIZED = 3;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_UNKNOWN = 1;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_SUCCESS = 0;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_TYPE__TYPE_LOAD = 1;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_TYPE__TYPE_UNKNOWN = 0;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_TYPE__TYPE_UNLOAD = 2;
    public static final int CONTEXT_HUB_BOOTED = 398;
    public static final int CONTEXT_HUB_LOADED_NANOAPP_SNAPSHOT_REPORTED = 400;
    public static final int CONTEXT_HUB_RESTARTED = 399;

    public static void write(int i, int i2, long j, int i3) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i3);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, int i4) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeInt(i4);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }
}
