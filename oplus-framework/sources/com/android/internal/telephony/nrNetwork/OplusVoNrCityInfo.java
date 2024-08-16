package com.android.internal.telephony.nrNetwork;

import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusVoNrCityInfo {
    private OplusNrModeConstant.SimType mSimType;
    private List<String> mVoNrCities = new ArrayList();
    private List<String> mVoNrProvinces = new ArrayList();

    public OplusVoNrCityInfo(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public OplusNrModeConstant.SimType getSimType() {
        return this.mSimType;
    }

    public void setSimType(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public List<String> getVoNrCities() {
        return this.mVoNrCities;
    }

    public void setVoNrCities(List<String> vonrCities) {
        this.mVoNrCities = vonrCities;
    }

    public List<String> getVoNrProvinces() {
        return this.mVoNrProvinces;
    }

    public void setVoNrProvinces(List<String> vonrProvinces) {
        this.mVoNrProvinces = vonrProvinces;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimType = " + this.mSimType);
        sb.append(" ,mVoNrCities = ");
        if (this.mSimType != null) {
            for (int i = 0; i < this.mVoNrCities.size(); i++) {
                sb.append(this.mVoNrCities.get(i));
                sb.append(",");
            }
        }
        sb.append(" ,saProvinceList = ");
        if (this.mVoNrProvinces != null) {
            for (int i2 = 0; i2 < this.mVoNrProvinces.size(); i2++) {
                sb.append(this.mVoNrProvinces.get(i2));
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
