package com.oplus.oms.split.core.splitinstall;

import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class SplitSessionLoaderSingleton {
    private static final AtomicReference<SplitSessionLoader> REFERENCE = new AtomicReference<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SplitSessionLoader get() {
        return REFERENCE.get();
    }

    public static void set(SplitSessionLoader loader) {
        REFERENCE.compareAndSet(null, loader);
    }
}
