package com.oplus.oms.split.common;

import java.util.List;

/* loaded from: classes.dex */
public class SplitInfoData {
    private String mMainProcessName;
    private String mSplitName;
    private List<String> mSubList;

    public SplitInfoData(String splitName, String mainProcessName, List<String> subList) {
        this.mSplitName = splitName;
        this.mMainProcessName = mainProcessName;
        this.mSubList = subList;
    }

    public void setSplitName(String name) {
        this.mSplitName = name;
    }

    public void setMainProcessName(String name) {
        this.mMainProcessName = name;
    }

    public void setSubProcessList(List<String> list) {
        this.mSubList = list;
    }

    public List<String> getSubProcessList() {
        return this.mSubList;
    }

    public String getMainProcessName() {
        return this.mMainProcessName;
    }

    public String getSplitName() {
        return this.mSplitName;
    }

    public String toString() {
        return "SplitInfoData{mSplitName='" + this.mSplitName + "', mMainProcessName='" + this.mMainProcessName + "', mSubList=" + this.mSubList + '}';
    }
}
