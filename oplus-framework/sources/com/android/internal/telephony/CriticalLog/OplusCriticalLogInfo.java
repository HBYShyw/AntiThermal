package com.android.internal.telephony.CriticalLog;

/* loaded from: classes.dex */
public class OplusCriticalLogInfo {
    public String mDesc;
    public String mIssue;
    public String mLog;
    public String mTag;
    public int mType;

    public OplusCriticalLogInfo(int type, String logstring, String issue, String desc) {
        this.mType = type;
        this.mLog = logstring;
        this.mIssue = issue;
        this.mDesc = desc;
    }

    public OplusCriticalLogInfo(int type, String logstring, String tagString, String issue, String desc) {
        this.mType = type;
        this.mLog = logstring;
        this.mTag = tagString;
        this.mIssue = issue;
        this.mDesc = desc;
    }
}
