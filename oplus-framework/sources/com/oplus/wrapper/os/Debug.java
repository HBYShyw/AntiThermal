package com.oplus.wrapper.os;

import android.os.Debug;

/* loaded from: classes.dex */
public final class Debug {
    private Debug() {
    }

    public static String getCallers(int depth) {
        return android.os.Debug.getCallers(depth);
    }

    /* loaded from: classes.dex */
    public static class MemoryInfo {
        private final Debug.MemoryInfo mMemoryInfo;

        public MemoryInfo(Debug.MemoryInfo memoryInfo) {
            this.mMemoryInfo = memoryInfo;
        }

        public int getTotalUss() {
            return this.mMemoryInfo.getTotalUss();
        }
    }
}
