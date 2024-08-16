package com.oplus.wrapper.content.pm;

/* loaded from: classes.dex */
public class ResolveInfo {
    private final android.content.pm.ResolveInfo mResolveInfo;

    public ResolveInfo(android.content.pm.ResolveInfo resolveInfo) {
        this.mResolveInfo = resolveInfo;
    }

    public android.content.pm.ComponentInfo getComponentInfo() {
        return this.mResolveInfo.getComponentInfo();
    }

    public boolean isSystem() {
        return this.mResolveInfo.system;
    }

    public int getTargetUserId() {
        return this.mResolveInfo.targetUserId;
    }
}
