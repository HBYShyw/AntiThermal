package com.oplus.oms.split.splitrequest;

import java.util.List;

/* loaded from: classes.dex */
final class SplitDetails {
    private final String mAppVersionName;
    private final String mOmsId;
    private final List<String> mSplitEntryFragments;
    private final SplitInfoListing mSplitInfoListing;
    private final List<SplitUpdateInfo> mUpdateSplits;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitDetails(String omsId, String appVersionName, List<SplitUpdateInfo> updateSplits, List<String> splitEntryFragments, SplitInfoListing splitInfoListing) {
        this.mOmsId = omsId;
        this.mAppVersionName = appVersionName;
        this.mUpdateSplits = updateSplits;
        this.mSplitEntryFragments = splitEntryFragments;
        this.mSplitInfoListing = splitInfoListing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getOmsId() {
        return this.mOmsId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getAppVersionName() {
        return this.mAppVersionName;
    }

    List<SplitUpdateInfo> getUpdateSplits() {
        return this.mUpdateSplits;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getSplitEntryFragments() {
        return this.mSplitEntryFragments;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitInfoListing getSplitInfoListing() {
        return this.mSplitInfoListing;
    }
}
