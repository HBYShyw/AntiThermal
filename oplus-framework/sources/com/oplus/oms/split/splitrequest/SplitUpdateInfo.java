package com.oplus.oms.split.splitrequest;

/* loaded from: classes.dex */
public class SplitUpdateInfo {
    private final String mSplitName;
    private final String mSplitUpdateVersion;

    public SplitUpdateInfo(String name, String version) {
        this.mSplitName = name;
        this.mSplitUpdateVersion = version;
    }

    public String getSplitName() {
        return this.mSplitName;
    }

    public String getSplitVersion() {
        return this.mSplitUpdateVersion;
    }
}
