package com.oplus.wrapper.os;

/* loaded from: classes.dex */
public class Process {
    private Process() {
    }

    public static final int getUidForPid(int pid) {
        return android.os.Process.getUidForPid(pid);
    }

    public static final long getTotalMemory() {
        return android.os.Process.getTotalMemory();
    }

    public static final void readProcLines(String path, String[] reqFields, long[] outSizes) {
        android.os.Process.readProcLines(path, reqFields, outSizes);
    }
}
