package com.oplus.wrapper.content.pm;

/* loaded from: classes.dex */
public class ApplicationInfo {
    private final android.content.pm.ApplicationInfo mApplicationInfo;

    public ApplicationInfo(android.content.pm.ApplicationInfo applicationInfo) {
        this.mApplicationInfo = applicationInfo;
    }

    public int getVersionCode() {
        return this.mApplicationInfo.versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.mApplicationInfo.versionCode = versionCode;
    }

    public String getBaseCodePath() {
        return this.mApplicationInfo.getBaseCodePath();
    }

    public boolean isSystemApp() {
        return this.mApplicationInfo.isSystemApp();
    }

    public long getLongVersionCode() {
        return this.mApplicationInfo.longVersionCode;
    }

    public void setLongVersionCode(long versionCode) {
        this.mApplicationInfo.setVersionCode(versionCode);
    }

    public String getPrimaryCpuAbi() {
        return this.mApplicationInfo.primaryCpuAbi;
    }

    public String getSecondaryCpuAbi() {
        return this.mApplicationInfo.secondaryCpuAbi;
    }
}
