package com.oplus.oms.split.splitinstall;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public final class SplitInstallerExecutor {
    private static final ThreadPoolExecutor sCopyExecutor = new ThreadPoolExecutor(0, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new SplitCopyThread());

    private SplitInstallerExecutor() {
    }

    public static Executor getCopyExecutor() {
        return sCopyExecutor;
    }
}
