package com.oplus.oms.split.splitinstall;

import java.util.concurrent.ThreadFactory;

/* loaded from: classes.dex */
final class SplitCopyThread implements ThreadFactory {
    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable r) {
        return new Thread(r, "split_copy_thread");
    }
}
