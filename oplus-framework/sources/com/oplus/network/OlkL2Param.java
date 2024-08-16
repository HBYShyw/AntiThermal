package com.oplus.network;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class OlkL2Param implements Parcelable {
    public static final Parcelable.Creator<OlkL2Param> CREATOR = new Parcelable.Creator<OlkL2Param>() { // from class: com.oplus.network.OlkL2Param.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OlkL2Param createFromParcel(Parcel in) {
            int mNetworkid = in.readInt();
            int mMCS = in.readInt();
            int mBandwidth_ratio = in.readInt();
            int mRadioOnMs = in.readInt();
            int mRssi = in.readInt();
            int mTx_retry_rate = in.readInt();
            int mTx_lost_rate = in.readInt();
            int mOPLUS_L3_SCORE = in.readInt();
            int mDevice_number = in.readInt();
            int mBw = in.readInt();
            int mTemperature = in.readInt();
            return new OlkL2Param(mNetworkid, mMCS, mBandwidth_ratio, mRadioOnMs, mRssi, mTx_retry_rate, mTx_lost_rate, mOPLUS_L3_SCORE, mDevice_number, mBw, mTemperature);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OlkL2Param[] newArray(int size) {
            return new OlkL2Param[size];
        }
    };
    private static final String TAG = "OlkL2Param";
    public int mBandwidth_ratio;
    public int mBw;
    public int mDevice_number;
    public int mMCS;
    public int mNetworkid;
    public int mOPLUS_L3_SCORE;
    public int mRadioOnMs;
    public int mRssi;
    public int mTemperature;
    public int mTx_lost_rate;
    public int mTx_retry_rate;

    public OlkL2Param(int networkid, int MCS, int bandwidth_ratio, int radioOnMs, int rssi, int tx_retry_rate, int rx_retry_rate, int OPLUS_L3_SCORE, int device_number, int bw, int temperature) {
        this.mNetworkid = networkid;
        this.mMCS = MCS;
        this.mBandwidth_ratio = bandwidth_ratio;
        this.mRadioOnMs = radioOnMs;
        this.mRssi = rssi;
        this.mTx_retry_rate = tx_retry_rate;
        this.mTx_lost_rate = rx_retry_rate;
        this.mOPLUS_L3_SCORE = OPLUS_L3_SCORE;
        this.mDevice_number = device_number;
        this.mBw = bw;
        this.mTemperature = temperature;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mNetworkid);
        dest.writeInt(this.mMCS);
        dest.writeInt(this.mBandwidth_ratio);
        dest.writeInt(this.mRadioOnMs);
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mTx_retry_rate);
        dest.writeInt(this.mTx_lost_rate);
        dest.writeInt(this.mOPLUS_L3_SCORE);
        dest.writeInt(this.mDevice_number);
        dest.writeInt(this.mBw);
        dest.writeInt(this.mTemperature);
    }

    public String toString() {
        return "OlkL2Param{mNetworkid='" + this.mNetworkid + "', mMCS='" + this.mMCS + "', mBandwidth_ratio='" + this.mBandwidth_ratio + "', mRadioOnMs='" + this.mRadioOnMs + "', mRssi='" + this.mRssi + "', mTx_retry_rate='" + this.mTx_retry_rate + "', mTx_lost_rate='" + this.mTx_lost_rate + "', mOPLUS_L3_SCORE='" + this.mOPLUS_L3_SCORE + "', mDevice_number='" + this.mDevice_number + "', mBw='" + this.mBw + "', mTemperature='" + this.mTemperature + "'}";
    }

    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("mNetworkid", Integer.valueOf(this.mNetworkid));
        map.put("mMCS", Integer.valueOf(this.mMCS));
        map.put("mBandwidth_ratio", Integer.valueOf(this.mBandwidth_ratio));
        map.put("mRadioOnMs", Integer.valueOf(this.mRadioOnMs));
        map.put("mRssi", Integer.valueOf(this.mRssi));
        map.put("mTx_retry_rate", Integer.valueOf(this.mTx_retry_rate));
        map.put("mTx_lost_rate", Integer.valueOf(this.mTx_lost_rate));
        map.put("mOPLUS_L3_SCORE", Integer.valueOf(this.mOPLUS_L3_SCORE));
        map.put("mDevice_number", Integer.valueOf(this.mDevice_number));
        map.put("mBw", Integer.valueOf(this.mBw));
        map.put("mTemperature", Integer.valueOf(this.mTemperature));
        return map;
    }

    public OlkL2Param(Map<String, Integer> map) {
        if (map == null) {
            return;
        }
        this.mNetworkid = map.get("mNetworkid").intValue();
        this.mMCS = map.get("mMCS").intValue();
        this.mBandwidth_ratio = map.get("mBandwidth_ratio").intValue();
        this.mRadioOnMs = map.get("mRadioOnMs").intValue();
        this.mRssi = map.get("mRssi").intValue();
        this.mTx_retry_rate = map.get("mTx_retry_rate").intValue();
        this.mTx_lost_rate = map.get("mTx_lost_rate").intValue();
        this.mOPLUS_L3_SCORE = map.get("mOPLUS_L3_SCORE").intValue();
        this.mDevice_number = map.get("mDevice_number").intValue();
        this.mBw = map.get("mBw").intValue();
        this.mTemperature = map.get("mTemperature").intValue();
    }

    public OlkL2Param() {
        this.mNetworkid = 0;
        this.mMCS = 0;
        this.mBandwidth_ratio = 0;
        this.mRadioOnMs = 0;
        this.mRssi = 0;
        this.mTx_retry_rate = 0;
        this.mTx_lost_rate = 0;
        this.mOPLUS_L3_SCORE = 0;
        this.mDevice_number = 0;
        this.mBw = 0;
        this.mTemperature = 0;
    }
}
