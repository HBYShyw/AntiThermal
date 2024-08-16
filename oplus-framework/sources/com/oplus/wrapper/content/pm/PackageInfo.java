package com.oplus.wrapper.content.pm;

/* loaded from: classes.dex */
public class PackageInfo {
    private final android.content.pm.PackageInfo mPackageInfo;

    public PackageInfo(android.content.pm.PackageInfo packageInfo) {
        this.mPackageInfo = packageInfo;
    }

    public String getOverlayTarget() {
        return this.mPackageInfo.overlayTarget;
    }

    public void setOverlayTarget(String overlayTarget) {
        this.mPackageInfo.overlayTarget = overlayTarget;
    }
}
