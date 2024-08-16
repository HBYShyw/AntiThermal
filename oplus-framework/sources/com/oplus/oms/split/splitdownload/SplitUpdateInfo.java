package com.oplus.oms.split.splitdownload;

/* loaded from: classes.dex */
public class SplitUpdateInfo {
    private String mMd5;
    private long mSize;
    private String mSplitName;
    private String mUrl;
    private int mVersionCode;
    private String mVersionName;

    public String getSplitName() {
        return this.mSplitName;
    }

    public void setSplitName(String splitName) {
        this.mSplitName = splitName;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public long getSize() {
        return this.mSize;
    }

    public void setSize(long size) {
        this.mSize = size;
    }

    public String getMd5() {
        return this.mMd5;
    }

    public void setMd5(String md5) {
        this.mMd5 = md5;
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public void setVersionName(String versionName) {
        this.mVersionName = versionName;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void setVersionCode(int versionCode) {
        this.mVersionCode = versionCode;
    }
}
