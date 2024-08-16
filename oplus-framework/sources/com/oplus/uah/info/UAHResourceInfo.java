package com.oplus.uah.info;

/* loaded from: classes.dex */
public class UAHResourceInfo {
    private int mResId;
    private String mResValue;

    public UAHResourceInfo(int resId, String resValue) {
        this.mResId = resId;
        this.mResValue = resValue;
    }

    public int getResId() {
        return this.mResId;
    }

    public void setResId(int resId) {
        this.mResId = resId;
    }

    public String getResValue() {
        return this.mResValue;
    }

    public void setResValue(String resValue) {
        this.mResValue = resValue;
    }
}
