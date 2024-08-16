package com.oplus.oms.split.splitinstall;

import com.oplus.oms.split.splitdownload.IProvider;

/* loaded from: classes.dex */
public class SplitApkLoadStrategy {
    private boolean mLocalFirst = false;
    private IProvider mProvider;

    public void setCustomProvider(IProvider provider) {
        this.mProvider = provider;
    }

    public IProvider getCustomProvider() {
        return this.mProvider;
    }

    public void setLocalFirstStrategyStatus(boolean needSet) {
        this.mLocalFirst = needSet;
    }

    public boolean getLocalFirstStrategyStatus() {
        return this.mLocalFirst;
    }

    /* loaded from: classes.dex */
    private static class InnerHolder {
        private static final SplitApkLoadStrategy sInstance = new SplitApkLoadStrategy();

        private InnerHolder() {
        }
    }

    public static SplitApkLoadStrategy getInstatnce() {
        return InnerHolder.sInstance;
    }
}
