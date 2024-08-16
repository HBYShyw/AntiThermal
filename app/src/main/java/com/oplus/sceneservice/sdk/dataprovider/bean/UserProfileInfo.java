package com.oplus.sceneservice.sdk.dataprovider.bean;

import android.text.TextUtils;
import androidx.annotation.Keep;

@Keep
/* loaded from: classes2.dex */
public class UserProfileInfo {
    public String mCompanyAddress;
    public String mCompanyLatLonType;
    public double mCompanyLatitude;
    public double mCompanyLongitude;
    public String mCompanyWifiBssid;
    public String mCompanyWifiName;
    public int mDefaultMapApp;
    public String mDiffTag;
    public String mEndSleepTime;
    public String mHomeAddress;
    public String mHomeLatLonType;
    public double mHomeLatitude;
    public double mHomeLongitude;
    public String mHomeWifiBssid;
    public String mHomeWifiName;
    public int mId;
    public String mLeaveCompanyTime;
    public String mLeaveHomeTime;
    public double mResidentLatitude;
    public double mResidentLongitude;
    public String mStartSleepTime;
    public String mTag;
    public int mTravelMode;
    public int mUserProfileModify;
    public int mLeaveHomeHour = -1;
    public int mLeaveHomeMin = -1;
    public int mLeaveCompanyHour = -1;
    public int mLeaveCompanyMin = -1;
    public int mArriveHomeHour = -1;
    public int mArriveHomeMin = -1;
    public int mArriveCompanyHour = -1;
    public int mArriveCompanyMin = -1;
    public String mHomeTag = "1";
    public String mCompanyTag = "1";

    @Keep
    /* loaded from: classes2.dex */
    public class Constant {
        public static final String AMAP = "amap_map";
        public static final String BAIDU = "baidu_map";
        public static final int MAP_BAIDU_VALURE = 1;
        public static final int MAP_GAODE_VALURE = 2;
        public static final int MAP_INVALID_VALURE = 0;
        public static final double NA_LAT_LON = 0.0d;
        public static final String TAG_MANUAL = "1";
        public static final String TAG_NONE = "2";
        public static final String TAG_PURE_MANUAL = "4";
        public static final String TAG_SMART = "0";
        public static final int TIME_INVALID = -1;
        public static final int TRAVEL_MODE_BY_BIKE = 4;
        public static final int TRAVEL_MODE_BY_BUS = 2;
        public static final int TRAVEL_MODE_BY_CAR = 1;
        public static final int TRAVEL_MODE_BY_NET_TAXI = 5;
        public static final int TRAVEL_MODE_BY_TAXI = 3;
        public static final String WGS84 = "wgs84";
        public static final String WIFI_NONE = "wifi_none";

        public Constant() {
        }
    }

    public static boolean isLocationValid(double d10, double d11) {
        return Math.abs(d10 - Constant.NA_LAT_LON) > Constant.NA_LAT_LON && Math.abs(d11 - Constant.NA_LAT_LON) > Constant.NA_LAT_LON;
    }

    public static boolean isWifiValid(String str) {
        return (TextUtils.isEmpty(str) || TextUtils.equals(str, Constant.WIFI_NONE)) ? false : true;
    }

    public boolean isCompanyLocationValid() {
        return !TextUtils.isEmpty(this.mCompanyLatLonType) && isLocationValid(this.mCompanyLatitude, this.mCompanyLongitude);
    }

    public boolean isHomeLocationValid() {
        return !TextUtils.isEmpty(this.mHomeLatLonType) && isLocationValid(this.mHomeLatitude, this.mHomeLongitude);
    }

    public String toString() {
        return "UserProfileInfo{mId=" + this.mId + ", mTag='" + this.mTag + "', mTravelMode=" + this.mTravelMode + ", mDefaultMapApp=" + this.mDefaultMapApp + ", mHomeLatitude=" + this.mHomeLatitude + ", mHomeLongitude=" + this.mHomeLongitude + ", mHomeLatLonType='" + this.mHomeLatLonType + "', mHomeAddress='" + this.mHomeAddress + "', mCompanyLatitude=" + this.mCompanyLatitude + ", mCompanyLongitude=" + this.mCompanyLongitude + ", mCompanyLatLonType='" + this.mCompanyLatLonType + "', mCompanyAddress='" + this.mCompanyAddress + "', mResidentLongitude=" + this.mResidentLongitude + ", mResidentLatitude=" + this.mResidentLatitude + ", mLeaveHomeHour=" + this.mLeaveHomeHour + ", mLeaveHomeMin=" + this.mLeaveHomeMin + ", mLeaveCompanyHour=" + this.mLeaveCompanyHour + ", mLeaveCompanyMin=" + this.mLeaveCompanyMin + ", mArriveHomeHour=" + this.mArriveHomeHour + ", mArriveHomeMin=" + this.mArriveHomeMin + ", mArriveCompanyHour=" + this.mArriveCompanyHour + ", mArriveCompanyMin=" + this.mArriveCompanyMin + ", mStartSleepTime='" + this.mStartSleepTime + "', mEndSleepTime='" + this.mEndSleepTime + "', mHomeWifiName='" + this.mHomeWifiName + "', mHomeWifiBssid='" + this.mHomeWifiBssid + "', mCompanyWifiName='" + this.mCompanyWifiName + "', mCompanyWifiBssid='" + this.mCompanyWifiBssid + "', mUserProfileModify=" + this.mUserProfileModify + ", mDiffTag='" + this.mDiffTag + "', mHomeTag='" + this.mHomeTag + "', mCompanyTag='" + this.mCompanyTag + "', mLeaveHomeTime='" + this.mLeaveHomeTime + "', mLeaveCompanyTime='" + this.mLeaveCompanyTime + "'}";
    }
}
