package com.oplus.wrapper.content.res;

/* loaded from: classes.dex */
public class CompatibilityInfo {
    private final android.content.res.CompatibilityInfo mCompatibilityInfo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompatibilityInfo(android.content.res.CompatibilityInfo compatibilityInfo) {
        this.mCompatibilityInfo = compatibilityInfo;
    }

    public float getApplicationScale() {
        return this.mCompatibilityInfo.applicationScale;
    }
}
