package com.oplus.oms.split.splitrequest;

import java.util.LinkedHashMap;

/* loaded from: classes.dex */
final class SplitInfoListing {
    private final LinkedHashMap<String, SplitInfo> mSplitInfoMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitInfoListing(LinkedHashMap<String, SplitInfo> splitInfoMap) {
        this.mSplitInfoMap = splitInfoMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LinkedHashMap<String, SplitInfo> getSplitInfoMap() {
        return this.mSplitInfoMap;
    }
}
