package com.android.server.am;

import android.os.Trace;
import java.util.UUID;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TraceErrorLogger {
    private static final String COUNTER_PREFIX = "ErrorId:";
    private static final int PLACEHOLDER_VALUE = 1;

    public boolean isAddErrorIdEnabled() {
        return true;
    }

    public UUID generateErrorId() {
        return UUID.randomUUID();
    }

    public void addProcessInfoAndErrorIdToTrace(String str, int i, UUID uuid) {
        Trace.traceCounter(64L, COUNTER_PREFIX + str + " " + i + "#" + uuid.toString(), 1);
    }

    public void addSubjectToTrace(String str, UUID uuid) {
        Trace.traceCounter(64L, String.format("Subject(for ErrorId %s):%s", uuid.toString(), str), 1);
    }
}
