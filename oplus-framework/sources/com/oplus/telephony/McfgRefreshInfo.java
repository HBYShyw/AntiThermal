package com.oplus.telephony;

/* loaded from: classes.dex */
public class McfgRefreshInfo {
    public static final int MCFG_CLIENT_REFRESH = 2;
    public static final int MCFG_REFRESH_COMPLETE = 1;
    public static final int MCFG_REFRESH_START = 0;
    private static final String TAG = "McfgRefreshInfo";
    private int mMcfgState;
    private int mSubId;

    public McfgRefreshInfo(int subId, int state) {
        this.mSubId = subId;
        this.mMcfgState = state;
    }

    public int getSubId() {
        return this.mSubId;
    }

    public int getMcfgState() {
        return this.mMcfgState;
    }

    public String toString() {
        return "McfgRefreshInfo{ mSubId= " + this.mSubId + " mMcfgState= " + this.mMcfgState + "}";
    }
}
