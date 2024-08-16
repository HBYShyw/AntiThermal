package com.oplus.sceneservice.sdk.dataprovider.bean.scene;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.Keep;
import java.util.Calendar;

@Keep
/* loaded from: classes2.dex */
public class SceneTrainData extends SceneData {
    public static final Parcelable.Creator<SceneTrainData> CREATOR = new a();
    public static final int DEFAULT_EXPIRE_DELAY_IN_HOUR = 24;
    public static final String KEY_ARRIVE_CODE = "ArriveCode";
    public static final String KEY_ARRIVE_DATE = "ArriveDate";
    public static final String KEY_ARRIVE_TIME = "ArriveTime";
    public static final String KEY_ARR_BAIDU_GPS = "ArrBaiduGps";
    public static final String KEY_ARR_CITY_NAME = "ArrCityName";
    public static final String KEY_ARR_DEFAULT_GPS = "ArrDefaultGps";
    public static final String KEY_ARR_GOOGLE_GPS = "ArrGoogleGps";
    public static final String KEY_CHANGE_STATUS = "ChangeStatus";
    public static final String KEY_DATE = "Date";
    public static final String KEY_DEPART_CODE = "DepartCode";
    public static final String KEY_DEP_BAIDU_GPS = "DepBaiduGps";
    public static final String KEY_DEP_CITY_NAME = "DepCityName";
    public static final String KEY_DEP_DEFAULT_GPS = "DepDefaultGps";
    public static final String KEY_DEP_GOOGLE_GPS = "DepGoogleGps";
    public static final String KEY_END_PLACE = "EndPlace";
    public static final String KEY_NO = "No";
    public static final String KEY_PASSENGER_NAME = "PassengerName";
    public static final String KEY_PNR = "PNR";
    public static final String KEY_SEAT = "Seat";
    public static final String KEY_START_PLACE = "StartPlace";
    public static final String KEY_STAUS = "status";
    public static final String KEY_TERMINAL_STATION = "TerminalStation";
    public static final String KEY_TICKET_GATE = "Entrance";
    public static final String KEY_TICKET_GATE_SOURCE = "EntranceSource";
    public static final String KEY_TIME = "Time";
    public static final int RECOMMEND_EXPIRE_DELAY_IN_HOUR = 1;
    public static final String TAG = "SceneTrainData";
    public static final String TICKET_GATE_SOURCE_ONLINE = "2";
    public static final String TICKET_GATE_SOURCE_SMS = "1";

    /* loaded from: classes2.dex */
    class a implements Parcelable.Creator<SceneTrainData> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public SceneTrainData createFromParcel(Parcel parcel) {
            return new SceneTrainData(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public SceneTrainData[] newArray(int i10) {
            return new SceneTrainData[i10];
        }
    }

    public SceneTrainData() {
        setType(2);
    }

    public String getArrBaiduGps() {
        return this.mContent.getString(KEY_ARR_BAIDU_GPS);
    }

    public String getArrCityName() {
        return this.mContent.getString(KEY_ARR_CITY_NAME);
    }

    public String getArrDefaultGps() {
        return this.mContent.getString(KEY_ARR_DEFAULT_GPS);
    }

    public String getArrGoogleGps() {
        return this.mContent.getString(KEY_ARR_GOOGLE_GPS);
    }

    public String getArriveCode() {
        return this.mContent.getString(KEY_ARRIVE_CODE);
    }

    public String getArriveDate() {
        return this.mContent.getString("ArriveDate");
    }

    public String getArriveTime() {
        return this.mContent.getString("ArriveTime");
    }

    public String getChangeStatus() {
        return this.mContent.getString("ChangeStatus");
    }

    public String getDate() {
        return this.mContent.getString("Date");
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
        return this.mContent.getString(KEY_NO) + this.mContent.getString("Date");
    }

    public String getDepBaiduGps() {
        return this.mContent.getString(KEY_DEP_BAIDU_GPS);
    }

    public String getDepCityName() {
        return this.mContent.getString(KEY_DEP_CITY_NAME);
    }

    public String getDepDefaultGps() {
        return this.mContent.getString(KEY_DEP_DEFAULT_GPS);
    }

    public String getDepGoogleGps() {
        return this.mContent.getString(KEY_DEP_GOOGLE_GPS);
    }

    public String getDepartCode() {
        return this.mContent.getString(KEY_DEPART_CODE);
    }

    public String getEndPlace() {
        return this.mContent.getString("EndPlace");
    }

    public String getNo() {
        return this.mContent.getString(KEY_NO);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public String getOnlineKey() {
        return this.mContent.getString(KEY_NO);
    }

    public String getPNR() {
        return this.mContent.getString("PNR");
    }

    public String getPassengerName() {
        return this.mContent.getString("PassengerName");
    }

    public String getSeat() {
        return this.mContent.getString("Seat");
    }

    public String getStartPlace() {
        return this.mContent.getString("StartPlace");
    }

    public String getStatus() {
        return this.mContent.getString("status");
    }

    public String getTerminalStation() {
        return this.mContent.getString(KEY_TERMINAL_STATION);
    }

    public String getTicketGate() {
        return this.mContent.getString(KEY_TICKET_GATE);
    }

    public String getTicketGateSource() {
        return this.mContent.getString(KEY_TICKET_GATE_SOURCE);
    }

    public String getTime() {
        return this.mContent.getString(KEY_TIME);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public boolean isValid() {
        return (TextUtils.isEmpty(getNo()) || TextUtils.isEmpty(getDate())) ? false : true;
    }

    public void setArrBaiduGps(String str) {
        this.mContent.putString(KEY_ARR_BAIDU_GPS, str);
    }

    public void setArrCityName(String str) {
        this.mContent.putString(KEY_ARR_CITY_NAME, str);
    }

    public void setArrDefaultGps(String str) {
        this.mContent.putString(KEY_ARR_DEFAULT_GPS, str);
    }

    public void setArrGoogleGps(String str) {
        this.mContent.putString(KEY_ARR_GOOGLE_GPS, str);
    }

    public void setArriveCode(String str) {
        this.mContent.putString(KEY_ARRIVE_CODE, str);
    }

    public void setArriveDate(String str) {
        this.mContent.putString("ArriveDate", str);
    }

    public void setArriveTime(String str) {
        this.mContent.putString("ArriveTime", str);
    }

    public void setChangeStatus(String str) {
        this.mContent.putString("ChangeStatus", str);
    }

    public void setDate(String str) {
        this.mContent.putString("Date", str);
    }

    public void setDepBaiduGps(String str) {
        this.mContent.putString(KEY_DEP_BAIDU_GPS, str);
    }

    public void setDepCityName(String str) {
        this.mContent.putString(KEY_DEP_CITY_NAME, str);
    }

    public void setDepDefaultGps(String str) {
        this.mContent.putString(KEY_DEP_DEFAULT_GPS, str);
    }

    public void setDepGoogleGps(String str) {
        this.mContent.putString(KEY_DEP_GOOGLE_GPS, str);
    }

    public void setDepartCode(String str) {
        this.mContent.putString(KEY_DEPART_CODE, str);
    }

    public void setEndPlace(String str) {
        this.mContent.putString("EndPlace", str);
    }

    public void setNo(String str) {
        this.mContent.putString(KEY_NO, str);
    }

    public void setPNR(String str) {
        this.mContent.putString("PNR", str);
    }

    public void setPassengerName(String str) {
        this.mContent.putString("PassengerName", str);
    }

    public void setSeat(String str) {
        this.mContent.putString("Seat", str);
    }

    public void setStartPlace(String str) {
        this.mContent.putString("StartPlace", str);
    }

    public void setStatus(String str) {
        this.mContent.putString("status", str);
    }

    public void setTerminalStation(String str) {
        this.mContent.putString(KEY_TERMINAL_STATION, str);
    }

    public void setTicketGate(String str) {
        this.mContent.putString(KEY_TICKET_GATE, str);
    }

    public void setTicketGateSource(String str) {
        this.mContent.putString(KEY_TICKET_GATE_SOURCE, str);
    }

    public void setTime(String str) {
        this.mContent.putString(KEY_TIME, str);
    }

    public SceneTrainData(Parcel parcel) {
        super(parcel);
    }
}
