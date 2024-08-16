package com.oplus.wrapper.libcore.io;

/* loaded from: classes.dex */
public class IoUtils {
    private IoUtils() {
    }

    public static void closeQuietly(AutoCloseable closeable) {
        libcore.io.IoUtils.closeQuietly(closeable);
    }
}
