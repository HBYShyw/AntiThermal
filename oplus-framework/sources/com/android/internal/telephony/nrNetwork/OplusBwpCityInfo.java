package com.android.internal.telephony.nrNetwork;

import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusBwpCityInfo {
    private List<String> mBwpCities = new ArrayList();
    private List<String> mBwpProvinces = new ArrayList();
    private OplusNrModeConstant.SimType mSimType;

    public OplusBwpCityInfo(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public OplusNrModeConstant.SimType getSimType() {
        return this.mSimType;
    }

    public void setSimType(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public List<String> getBwpCities() {
        return this.mBwpCities;
    }

    public void setBwpCities(List<String> bwpCities) {
        this.mBwpCities = bwpCities;
    }

    public List<String> getBwpProvinces() {
        return this.mBwpProvinces;
    }

    public void setBwpProvinces(List<String> bwpProvinces) {
        this.mBwpProvinces = bwpProvinces;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimType = " + this.mSimType);
        sb.append(" ,mBwpCities = ");
        if (this.mSimType != null) {
            for (int i = 0; i < this.mBwpCities.size(); i++) {
                sb.append(this.mBwpCities.get(i));
                sb.append(",");
            }
        }
        sb.append(" ,saProvinceList = ");
        if (this.mBwpProvinces != null) {
            for (int i2 = 0; i2 < this.mBwpProvinces.size(); i2++) {
                sb.append(this.mBwpProvinces.get(i2));
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
