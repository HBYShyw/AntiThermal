package com.oplus.oms.split.core.splitcompat;

import com.oplus.oms.split.core.splitinstall.LoadedSplitFetcher;
import java.util.Set;

/* loaded from: classes.dex */
final class LoadedSplitFetcherImpl implements LoadedSplitFetcher {
    private final OplusSplitCompat mSplitCompat;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoadedSplitFetcherImpl(OplusSplitCompat splitCompat) {
        this.mSplitCompat = splitCompat;
    }

    @Override // com.oplus.oms.split.core.splitinstall.LoadedSplitFetcher
    public Set<String> loadedSplits() {
        return this.mSplitCompat.getLoadedSplits();
    }
}
