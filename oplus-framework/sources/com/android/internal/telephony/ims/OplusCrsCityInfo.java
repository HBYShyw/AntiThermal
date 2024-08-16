package com.android.internal.telephony.ims;

import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCrsCityInfo {
    private List<String> mCrsCities = new ArrayList();
    private List<String> mCrsProvinces = new ArrayList();
    private OplusNrModeConstant.SimType mSimType;

    public OplusCrsCityInfo(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public OplusNrModeConstant.SimType getSimType() {
        return this.mSimType;
    }

    public void setSimType(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public List<String> getCrsCities() {
        return this.mCrsCities;
    }

    public void setCrsCities(List<String> crsCities) {
        this.mCrsCities = crsCities;
    }

    public List<String> getCrsProvinces() {
        return this.mCrsProvinces;
    }

    public void setCrsProvinces(List<String> crsProvinces) {
        this.mCrsProvinces = crsProvinces;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimType = " + this.mSimType);
        sb.append(" ,mCrsCities = ");
        if (this.mSimType != null) {
            for (int i = 0; i < this.mCrsCities.size(); i++) {
                sb.append(this.mCrsCities.get(i));
                sb.append(",");
            }
        }
        sb.append(" ,saProvinceList = ");
        if (this.mCrsProvinces != null) {
            for (int i2 = 0; i2 < this.mCrsProvinces.size(); i2++) {
                sb.append(this.mCrsProvinces.get(i2));
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
