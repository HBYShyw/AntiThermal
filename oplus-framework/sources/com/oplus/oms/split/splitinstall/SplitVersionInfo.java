package com.oplus.oms.split.splitinstall;

import com.oplus.oms.split.common.Identifiable;
import com.oplus.oms.split.splitdownload.SplitUpdateInfo;
import com.oplus.oms.split.splitrequest.SplitInfo;

/* loaded from: classes.dex */
public class SplitVersionInfo implements Identifiable {
    private SplitVersionInfo mDefaultInfo;
    private int mFrom;
    private int mInstallVersionCode;
    private String mInstallVersionName;
    private SplitUpdateInfo mSplitDownloadInfo;
    private SplitInfo mSplitInfo;

    public SplitVersionInfo(SplitInfo info) {
        this.mSplitInfo = info;
    }

    public void setFrom(int from) {
        this.mFrom = from;
    }

    public void setApkInfo(String url, String md5, long size) {
        SplitInfo splitInfo = this.mSplitInfo;
        if (splitInfo != null) {
            splitInfo.setApkDownloadInfo(url, md5, size);
        }
    }

    public void setDefaultVersionInfo(SplitVersionInfo defaultInfo) {
        this.mDefaultInfo = defaultInfo;
    }

    public SplitVersionInfo getDefaultVersionInfo() {
        return this.mDefaultInfo;
    }

    public void setSplitDownloadInfo(SplitUpdateInfo downloadInfo) {
        this.mSplitDownloadInfo = downloadInfo;
    }

    public SplitUpdateInfo getSplitDownloadInfo() {
        return this.mSplitDownloadInfo;
    }

    public void setInstallVersionName(String versionName) {
        this.mInstallVersionName = versionName;
    }

    public void setInstalledVersionCode(int versionCode) {
        this.mInstallVersionCode = versionCode;
    }

    public String getInstallVersionName() {
        return this.mInstallVersionName;
    }

    public int getInstallVersionCode() {
        return this.mInstallVersionCode;
    }

    public String getInstallVersion() {
        return this.mInstallVersionName + "@" + this.mInstallVersionCode;
    }

    public void updateSplitInfoVersion() {
        SplitInfo splitInfo = this.mSplitInfo;
        if (splitInfo != null) {
            splitInfo.setSplitVersionName(this.mInstallVersionName);
            this.mSplitInfo.setSplitVersionCode(this.mInstallVersionCode);
        }
    }

    public int getFrom() {
        return this.mFrom;
    }

    public SplitInfo getSplitInfo() {
        return this.mSplitInfo;
    }

    public SplitVersionInfo setDefaultVersionInfo() {
        return this.mDefaultInfo;
    }

    public String toString() {
        return "SplitVersionInfo{, mFrom=" + this.mFrom + ", mSplitInfo=" + this.mSplitInfo + ", mDefaultInfo=" + this.mDefaultInfo + '}';
    }

    @Override // com.oplus.oms.split.common.Identifiable
    public String getIdentify() {
        SplitInfo splitInfo = this.mSplitInfo;
        return splitInfo == null ? "" : splitInfo.getIdentify();
    }
}
