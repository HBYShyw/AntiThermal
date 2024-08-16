package com.oplus.sceneservice.sdk.dataprovider.bean;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
/* loaded from: classes2.dex */
public class PhoneStatusInfo {
    public double mCurrentLatitude;
    public double mCurrentLongitude;
    public long mLastSuccessfulUpdateLocationTime;
    public String mConnectedWifiName = "";
    public String mConnectedWifiBssid = "";
    public String mAroundWifiName_1 = "";
    public String mAroundWifiName_2 = "";
    public String mAroundWifiName_3 = "";
    public String mAroundWifiName_4 = "";
    public String mAroundWifiName_5 = "";
    public String mAroundWifiBssid_1 = "";
    public String mAroundWifiBssid_2 = "";
    public String mAroundWifiBssid_3 = "";
    public String mAroundWifiBssid_4 = "";
    public String mAroundWifiBssid_5 = "";
    public String mCurrentLocationInfo = "";
    public String mLastLocationInfo = "";

    @Keep
    /* loaded from: classes2.dex */
    public static class LocationInfoBean {

        @SerializedName("admin")
        public String mAdmin;

        @SerializedName("countryCode")
        public String mCountryCode;

        @SerializedName("countryName")
        public String mCountryName;

        @SerializedName("level")
        public int mLevel;

        @SerializedName("subAdmin")
        public String mSubAdmin;

        @SerializedName("timeStamp")
        public long mTimeStamp;

        public String toString() {
            return "LocationInfoBean{mAdmin='" + this.mAdmin + "', mSubAdmin='" + this.mSubAdmin + "', mCountryName='" + this.mCountryName + "', mCountryCode='" + this.mCountryCode + "', mLevel=" + this.mLevel + ", mTimeStamp=" + this.mTimeStamp + '}';
        }
    }

    public String toString() {
        return "PhoneStatusInfo{mCurrentLatitude=" + this.mCurrentLatitude + ", mCurrentLongitude=" + this.mCurrentLongitude + ", mConnectedWifiName='" + this.mConnectedWifiName + "', mConnectedWifiBssid='" + this.mConnectedWifiBssid + "', mAroundWifiName_1='" + this.mAroundWifiName_1 + "', mAroundWifiName_2='" + this.mAroundWifiName_2 + "', mAroundWifiName_3='" + this.mAroundWifiName_3 + "', mAroundWifiName_4='" + this.mAroundWifiName_4 + "', mAroundWifiName_5='" + this.mAroundWifiName_5 + "', mAroundWifiBssid_1='" + this.mAroundWifiBssid_1 + "', mAroundWifiBssid_2='" + this.mAroundWifiBssid_2 + "', mAroundWifiBssid_3='" + this.mAroundWifiBssid_3 + "', mAroundWifiBssid_4='" + this.mAroundWifiBssid_4 + "', mAroundWifiBssid_5='" + this.mAroundWifiBssid_5 + "', mLastSuccessfulUpdateLocationTime=" + this.mLastSuccessfulUpdateLocationTime + "', mCurrentLocationInfo=" + this.mCurrentLocationInfo + "', mLastLocationInfo=" + this.mLastLocationInfo + '}';
    }
}
