package com.oplus.wrapper.content.res;

/* loaded from: classes.dex */
public class Resources {
    private final android.content.res.Resources mResources;

    public Resources(android.content.res.Resources resources) {
        this.mResources = resources;
    }

    public CompatibilityInfo getCompatibilityInfo() {
        if (this.mResources.getCompatibilityInfo() == null) {
            return null;
        }
        return new CompatibilityInfo(this.mResources.getCompatibilityInfo());
    }

    public static boolean resourceHasPackage(int resid) {
        return android.content.res.Resources.resourceHasPackage(resid);
    }
}
