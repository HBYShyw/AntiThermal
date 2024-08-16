package com.oplus.oms.split.core.splitinstall;

import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class LoadedSplitFetcherSingleton {
    private static final AtomicReference<LoadedSplitFetcher> REFERENCE = new AtomicReference<>(null);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LoadedSplitFetcher get() {
        return REFERENCE.get();
    }

    public static void set(LoadedSplitFetcher fetcher) {
        REFERENCE.compareAndSet(null, fetcher);
    }
}
