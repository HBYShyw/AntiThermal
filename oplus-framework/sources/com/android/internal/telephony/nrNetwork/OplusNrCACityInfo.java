package com.android.internal.telephony.nrNetwork;

import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusNrCACityInfo {
    private List<String> mNrCACities = new ArrayList();
    private List<String> mNrCAProvinces = new ArrayList();
    private OplusNrModeConstant.SimType mSimType;

    public OplusNrCACityInfo(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public OplusNrModeConstant.SimType getSimType() {
        return this.mSimType;
    }

    public void setSimType(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public List<String> getNrCACities() {
        return this.mNrCACities;
    }

    public void setNrCACities(List<String> nrcaCities) {
        this.mNrCACities = nrcaCities;
    }

    public List<String> getNrCAProvinces() {
        return this.mNrCAProvinces;
    }

    public void setNrCAProvinces(List<String> nrcaProvinces) {
        this.mNrCAProvinces = nrcaProvinces;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimType = " + this.mSimType);
        sb.append(" ,mNrCACities = ");
        if (this.mSimType != null) {
            for (int i = 0; i < this.mNrCACities.size(); i++) {
                sb.append(this.mNrCACities.get(i));
                sb.append(",");
            }
        }
        sb.append(" ,saProvinceList = ");
        if (this.mNrCAProvinces != null) {
            for (int i2 = 0; i2 < this.mNrCAProvinces.size(); i2++) {
                sb.append(this.mNrCAProvinces.get(i2));
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
