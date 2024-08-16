package com.oplus.sceneservice.sdk.dataprovider.bean.scene;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.Keep;
import java.util.Calendar;
import l9.LogUtils;
import l9.NumberUtils;

@Keep
/* loaded from: classes2.dex */
public class SceneFlightData extends SceneData {
    public static final Parcelable.Creator<SceneFlightData> CREATOR = new a();
    public static final int DEFAULT_EXPIRE_DELAY_IN_HOUR = 24;
    public static final int INVALID_LATITUDE_LONGITUDE = 400;
    public static final String KEY_ARRIVE_COUNTRY_CODE = "ArrCountryCode";
    public static final String KEY_ARRIVE_DATE = "ArriveDate";
    public static final String KEY_ARRIVE_LATITUDE = "arriveLatitude";
    public static final String KEY_ARRIVE_LONGITUDE = "arriveLongitude";
    public static final String KEY_ARRIVE_PLAN_DATE = "PlanArriveDate";
    public static final String KEY_ARRIVE_PLAN_TIME = "PlanArriveTime";
    public static final String KEY_ARRIVE_PLAN_TIME_STAMP = "PlanArriveTimeStamp";
    public static final String KEY_ARRIVE_TIME = "ArriveTime";
    public static final String KEY_BAGGAGE_CAROUSEL = "Baggage";
    public static final String KEY_BOARDING_GATE = "Gate";
    public static final String KEY_CHANGE_STATUS = "ChangeStatus";
    public static final String KEY_CHECK_IN_OFFICE = "CheckIn";
    public static final String KEY_COMPANY_NAME = "CompanyName";
    public static final String KEY_DEPART_COUNTRY_CODE = "DepCountryCode";
    public static final String KEY_END_PLACE = "EndPlace";
    public static final String KEY_END_PLACE_CODE = "EndPlaceCode";
    public static final String KEY_FLIGHT_COMPANY_LOGO_URL = "CompanyLogo";
    public static final String KEY_FLIGHT_NUMBER = "NO";
    public static final String KEY_FLIGHT_STATUS = "FlightStatus";
    public static final String KEY_FLIGHT_TIME = "duration";
    public static final String KEY_ORIGIN = "Origin";
    public static final String KEY_ORIGINAL_DATE = "OriginalDate";
    public static final String KEY_ORIGINAL_NO = "OriginalNO";
    public static final String KEY_PASSENGER_NAME = "PassengerName";
    public static final String KEY_PNR = "PNR";
    public static final String KEY_SEAT_NUM = "Seat";
    public static final String KEY_START_DATE = "StartDate";
    public static final String KEY_START_LATITUDE = "startLatitude";
    public static final String KEY_START_LONGITUDE = "startLongitude";
    public static final String KEY_START_PLACE = "StartPlace";
    public static final String KEY_START_PLACE_CODE = "StartPlaceCode";
    public static final String KEY_START_PLAN_DATE = "PlanStartDate";
    public static final String KEY_START_PLAN_TIME = "PlanStartTime";
    public static final String KEY_START_PLAN_TIME_STAMP = "PlanStartTimeStamp";
    public static final String KEY_START_TIME = "StartTime";
    public static final String KEY_TERMINUS = "Terminus";
    public static final int RECOMMEND_EXPIRE_DELAY_IN_HOUR = 1;
    public static final String TAG = "SceneFlightData";

    /* loaded from: classes2.dex */
    class a implements Parcelable.Creator<SceneFlightData> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public SceneFlightData createFromParcel(Parcel parcel) {
            return new SceneFlightData(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public SceneFlightData[] newArray(int i10) {
            return new SceneFlightData[i10];
        }
    }

    public SceneFlightData() {
        setType(1);
    }

    public String getArrCountryCode() {
        return this.mContent.getString(KEY_ARRIVE_COUNTRY_CODE);
    }

    public String getArriveDate() {
        return this.mContent.getString("ArriveDate");
    }

    public String getArrivePlanDate() {
        return this.mContent.getString(KEY_ARRIVE_PLAN_DATE);
    }

    public String getArrivePlanTime() {
        return this.mContent.getString(KEY_ARRIVE_PLAN_TIME);
    }

    public long getArrivePlanTimeStamp() {
        return NumberUtils.c(this.mContent.getString(KEY_ARRIVE_PLAN_TIME_STAMP), -1L);
    }

    public String getArriveTime() {
        return this.mContent.getString("ArriveTime");
    }

    public String getBaggageCarousel() {
        return this.mContent.getString(KEY_BAGGAGE_CAROUSEL);
    }

    public String getBoardingGate() {
        return this.mContent.getString(KEY_BOARDING_GATE);
    }

    public String getChangeStatus() {
        return this.mContent.getString("ChangeStatus");
    }

    public String getCheckInOffice() {
        return this.mContent.getString(KEY_CHECK_IN_OFFICE);
    }

    public String getCompanyLogoUrl() {
        return this.mContent.getString(KEY_FLIGHT_COMPANY_LOGO_URL);
    }

    public String getCompanyName() {
        return this.mContent.getString(KEY_COMPANY_NAME);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    protected long getDefaultExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getOccurTime());
        calendar.add(10, 24);
        return calendar.getTimeInMillis();
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    protected String getDefaultMatchKey() {
        return this.mContent.getString(KEY_FLIGHT_NUMBER) + this.mContent.getString(KEY_START_DATE);
    }

    public String getDepCountryCode() {
        return this.mContent.getString(KEY_DEPART_COUNTRY_CODE);
    }

    public String getEndPlace() {
        return this.mContent.getString("EndPlace");
    }

    public String getEndPlaceCode() {
        return this.mContent.getString(KEY_END_PLACE_CODE);
    }

    public String getFlightNumber() {
        return this.mContent.getString(KEY_FLIGHT_NUMBER);
    }

    public String getFlightStatus() {
        return this.mContent.getString(KEY_FLIGHT_STATUS);
    }

    public String getFlightTime() {
        return this.mContent.getString("duration");
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public String getOnlineKey() {
        return this.mContent.getString(KEY_FLIGHT_NUMBER);
    }

    public String getOrigin() {
        return this.mContent.getString(KEY_ORIGIN);
    }

    public String getOriginalDate() {
        return this.mContent.getString(KEY_ORIGINAL_DATE);
    }

    public String getOriginalNo() {
        return this.mContent.getString(KEY_ORIGINAL_NO);
    }

    public String getPNR() {
        return this.mContent.getString("PNR");
    }

    public String getPassengerName() {
        return this.mContent.getString("PassengerName");
    }

    public String getSeatNum() {
        return this.mContent.getString("Seat");
    }

    public String getStartDate() {
        return this.mContent.getString(KEY_START_DATE);
    }

    public double getStartLatitude() {
        try {
            return Double.parseDouble(this.mContent.getString(KEY_START_LATITUDE));
        } catch (Exception e10) {
            LogUtils.b(TAG, "getStartLatitude, error = " + e10);
            return 400.0d;
        }
    }

    public double getStartLongitude() {
        try {
            return Double.parseDouble(this.mContent.getString(KEY_START_LONGITUDE));
        } catch (Exception e10) {
            LogUtils.b(TAG, "getStartLongitude, error = " + e10);
            return 400.0d;
        }
    }

    public String getStartPlace() {
        return this.mContent.getString("StartPlace");
    }

    public String getStartPlaceCode() {
        return this.mContent.getString(KEY_START_PLACE_CODE);
    }

    public String getStartPlanDate() {
        return this.mContent.getString(KEY_START_PLAN_DATE);
    }

    public String getStartPlanTime() {
        return this.mContent.getString(KEY_START_PLAN_TIME);
    }

    public long getStartPlanTimeStamp() {
        return NumberUtils.c(this.mContent.getString(KEY_START_PLAN_TIME_STAMP), -1L);
    }

    public String getStartTime() {
        return this.mContent.getString(KEY_START_TIME);
    }

    public double getTerminalLatitude() {
        try {
            return Double.parseDouble(this.mContent.getString(KEY_ARRIVE_LATITUDE));
        } catch (Exception e10) {
            LogUtils.b(TAG, "getTerminalLatitude, error = " + e10);
            return 400.0d;
        }
    }

    public double getTerminalLongitude() {
        try {
            return Double.parseDouble(this.mContent.getString(KEY_ARRIVE_LONGITUDE));
        } catch (Exception e10) {
            LogUtils.b(TAG, "getTerminalLongitude, error = " + e10);
            return 400.0d;
        }
    }

    public String getTerminus() {
        return this.mContent.getString(KEY_TERMINUS);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public boolean isValid() {
        return (TextUtils.isEmpty(this.mContent.getString(KEY_FLIGHT_NUMBER)) || TextUtils.isEmpty(this.mContent.getString(KEY_START_DATE))) ? false : true;
    }

    public void setArrCountryCode(String str) {
        this.mContent.putString(KEY_ARRIVE_COUNTRY_CODE, str);
    }

    public void setArriveDate(String str) {
        this.mContent.putString("ArriveDate", str);
    }

    public void setArrivePlanDate(String str) {
        this.mContent.putString(KEY_ARRIVE_PLAN_DATE, str);
    }

    public void setArrivePlanTime(String str) {
        this.mContent.putString(KEY_ARRIVE_PLAN_TIME, str);
    }

    public void setArrivePlanTimeStamp(long j10) {
        this.mContent.putString(KEY_ARRIVE_PLAN_TIME_STAMP, String.valueOf(j10));
    }

    public void setArriveTime(String str) {
        this.mContent.putString("ArriveTime", str);
    }

    public void setBaggageCarousel(String str) {
        this.mContent.putString(KEY_BAGGAGE_CAROUSEL, str);
    }

    public void setBoardingGate(String str) {
        this.mContent.putString(KEY_BOARDING_GATE, str);
    }

    public void setChangeStatus(String str) {
        this.mContent.putString("ChangeStatus", str);
    }

    public void setCheckInOffice(String str) {
        this.mContent.putString(KEY_CHECK_IN_OFFICE, str);
    }

    public void setCompanyLogoUrl(String str) {
        this.mContent.putString(KEY_FLIGHT_COMPANY_LOGO_URL, str);
    }

    public void setCompanyName(String str) {
        this.mContent.putString(KEY_COMPANY_NAME, str);
    }

    public void setDepCountryCode(String str) {
        this.mContent.putString(KEY_DEPART_COUNTRY_CODE, str);
    }

    public void setDepartLatitude(String str) {
        this.mContent.putString(KEY_START_LATITUDE, str);
    }

    public void setDepartLongitude(String str) {
        this.mContent.putString(KEY_START_LONGITUDE, str);
    }

    public void setEndPlace(String str) {
        this.mContent.putString("EndPlace", str);
    }

    public void setEndPlaceCode(String str) {
        this.mContent.putString(KEY_END_PLACE_CODE, str);
    }

    public void setFlightNumber(String str) {
        this.mContent.putString(KEY_FLIGHT_NUMBER, str);
    }

    public void setFlightStatus(String str) {
        this.mContent.putString(KEY_FLIGHT_STATUS, str);
    }

    public void setFlightTime(String str) {
        this.mContent.putString("duration", str);
    }

    public void setOrigin(String str) {
        this.mContent.putString(KEY_ORIGIN, str);
    }

    public void setOriginalDate(String str) {
        this.mContent.putString(KEY_ORIGINAL_DATE, str);
    }

    public void setOriginalNo(String str) {
        this.mContent.putString(KEY_ORIGINAL_NO, str);
    }

    public void setPNR(String str) {
        this.mContent.putString("PNR", str);
    }

    public void setPassengerName(String str) {
        this.mContent.putString("PassengerName", str);
    }

    public void setSeatNum(String str) {
        this.mContent.putString("Seat", str);
    }

    public void setStartDate(String str) {
        this.mContent.putString(KEY_START_DATE, str);
    }

    public void setStartPlace(String str) {
        this.mContent.putString("StartPlace", str);
    }

    public void setStartPlaceCode(String str) {
        this.mContent.putString(KEY_START_PLACE_CODE, str);
    }

    public void setStartPlanDate(String str) {
        this.mContent.putString(KEY_START_PLAN_DATE, str);
    }

    public void setStartPlanTime(String str) {
        this.mContent.putString(KEY_START_PLAN_TIME, str);
    }

    public void setStartPlanTimeStamp(long j10) {
        this.mContent.putString(KEY_START_PLAN_TIME_STAMP, String.valueOf(j10));
    }

    public void setStartTime(String str) {
        this.mContent.putString(KEY_START_TIME, str);
    }

    public void setTerminalLatitude(String str) {
        this.mContent.putString(KEY_ARRIVE_LATITUDE, str);
    }

    public void setTerminalLongitude(String str) {
        this.mContent.putString(KEY_ARRIVE_LONGITUDE, str);
    }

    public void setTerminus(String str) {
        this.mContent.putString(KEY_TERMINUS, str);
    }

    public SceneFlightData(Parcel parcel) {
        super(parcel);
    }
}
