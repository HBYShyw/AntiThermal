package com.android.internal.telephony.nrNetwork;

import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusNrModeCityInfo {
    private String TAG = "OplusNrModeCityInfo";
    private List<String> mSaCityList = new ArrayList();
    private List<String> mSaProvinceList = new ArrayList();
    private OplusNrModeConstant.SimType mSimType;

    public OplusNrModeCityInfo(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public OplusNrModeConstant.SimType getSimType() {
        return this.mSimType;
    }

    public void setSimType(OplusNrModeConstant.SimType mSimType) {
        this.mSimType = mSimType;
    }

    public List<String> getSaCityList() {
        return this.mSaCityList;
    }

    public void setSaCityList(List<String> mSaCityList) {
        this.mSaCityList = mSaCityList;
    }

    public List<String> getSaProvinceList() {
        return this.mSaProvinceList;
    }

    public void setSaProvinceList(List<String> mSaProvinceList) {
        this.mSaProvinceList = mSaProvinceList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimType = " + this.mSimType);
        sb.append(" ,saCityList = ");
        if (this.mSimType != null) {
            for (int i = 0; i < this.mSaCityList.size(); i++) {
                sb.append(this.mSaCityList.get(i));
                sb.append(",");
            }
        }
        sb.append(" ,saProvinceList = ");
        if (this.mSaProvinceList != null) {
            for (int i2 = 0; i2 < this.mSaProvinceList.size(); i2++) {
                sb.append(this.mSaProvinceList.get(i2));
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
