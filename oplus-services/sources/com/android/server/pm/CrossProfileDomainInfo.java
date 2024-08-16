package com.android.server.pm;

import android.content.pm.ResolveInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class CrossProfileDomainInfo {
    int mHighestApprovalLevel;
    ResolveInfo mResolveInfo;
    int mTargetUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CrossProfileDomainInfo(ResolveInfo resolveInfo, int i, int i2) {
        this.mResolveInfo = resolveInfo;
        this.mHighestApprovalLevel = i;
        this.mTargetUserId = i2;
    }

    CrossProfileDomainInfo(ResolveInfo resolveInfo, int i) {
        this.mTargetUserId = -2;
        this.mResolveInfo = resolveInfo;
        this.mHighestApprovalLevel = i;
    }

    public String toString() {
        return "CrossProfileDomainInfo{resolveInfo=" + this.mResolveInfo + ", highestApprovalLevel=" + this.mHighestApprovalLevel + ", targetUserId= " + this.mTargetUserId + '}';
    }
}
