package com.oplus.oms.split.splitreport.model;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class SplitReporterInfo {
    private static final long INVALID_TIME = -1;
    private String mAction;
    private String mName;
    private String mProcessName;
    private String mType;
    private String mVersion;
    private int mResultCode = 0;
    private long mActionStartTime = -1;
    private long mTimeCost = -1;

    public SplitReporterInfo() {
    }

    public SplitReporterInfo(String name) {
        this.mName = name;
    }

    public SplitReporterInfo(String name, String version) {
        this.mName = name;
        this.mVersion = version;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getAction() {
        return this.mAction;
    }

    public void setAction(String action) {
        this.mAction = action;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public void setResultCode(int resultCode) {
        this.mResultCode = resultCode;
    }

    public long getActionStartTime() {
        return this.mActionStartTime;
    }

    public void setActionStartTime(long actionStartTime) {
        this.mActionStartTime = actionStartTime;
    }

    public long getTimeCost() {
        return this.mTimeCost;
    }

    public void setTimeCost(long timeCost) {
        this.mTimeCost = timeCost;
    }

    public void calcTimeCost(long endTime) {
        long j = this.mActionStartTime;
        if (j != -1) {
            this.mTimeCost = endTime - j;
        }
    }

    public String getProcessName() {
        return this.mProcessName;
    }

    public void setProcessName(String processName) {
        this.mProcessName = processName;
    }

    public String toReporterString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (!TextUtils.isEmpty(this.mName)) {
            builder.append("n:").append(this.mName);
        }
        if (!TextUtils.isEmpty(this.mVersion)) {
            builder.append(",").append("v:").append(this.mVersion);
        }
        if (!TextUtils.isEmpty(this.mAction)) {
            builder.append(",").append("a:").append(this.mAction);
        }
        if (!TextUtils.isEmpty(this.mType)) {
            builder.append(",").append("t:").append(this.mType);
        }
        builder.append(",").append("r:").append(this.mResultCode);
        if (this.mTimeCost >= 0) {
            builder.append(",").append("c:").append(this.mTimeCost);
        }
        if (!TextUtils.isEmpty(this.mProcessName)) {
            builder.append(",").append("p:").append(this.mProcessName);
        }
        builder.append("}");
        return builder.toString();
    }

    public String toString() {
        return "SplitReporterInfo{mName='" + this.mName + "', mVersion='" + this.mVersion + "', mAction='" + this.mAction + "', mType='" + this.mType + "', mResultCode=" + this.mResultCode + ", mActionStartTime=" + this.mActionStartTime + ", mTimeCost=" + this.mTimeCost + ", mProcessName='" + this.mProcessName + "'}";
    }
}
