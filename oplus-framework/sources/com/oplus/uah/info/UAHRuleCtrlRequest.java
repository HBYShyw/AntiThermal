package com.oplus.uah.info;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class UAHRuleCtrlRequest {
    private ArrayList<UAHResourceInfo> mList;
    private int mRuleId;
    private int mRuleState;

    public UAHRuleCtrlRequest(int ruleId, int ruleState, ArrayList<UAHResourceInfo> list) {
        this.mRuleId = -1;
        this.mRuleState = -1;
        this.mRuleId = ruleId;
        this.mRuleState = ruleState;
        this.mList = list;
    }

    public int getRuleId() {
        return this.mRuleId;
    }

    public int getRuleState() {
        return this.mRuleState;
    }

    public ArrayList<UAHResourceInfo> getList() {
        return this.mList;
    }

    public String toString() {
        return "UahThermalRequest{ruleId='" + this.mRuleId + "'ruleState='" + this.mRuleState + "', list=" + this.mList.toString() + '}';
    }
}
