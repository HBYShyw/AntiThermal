package com.oplus.deepthinker.sdk.app.userprofile.labels;

import android.util.ArraySet;
import android.util.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.ResidenceLabelUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ResidenceLabel {
    private static final String TAG = "ResidenceLabel";

    @SerializedName("chaos")
    private float accuracy;

    @SerializedName("appearDateNum")
    private int appearDateNum;

    @SerializedName("appearRatioType")
    private int appearRatioType;

    @SerializedName("dayDuration")
    private float averageStayDurationInDaytime;

    @SerializedName("nightDuration")
    private float averageStayDurationInNighttime;

    @SerializedName("duration")
    private float averageStayDurationPerDay;

    @SerializedName("bssids")
    private ArraySet<String> bssids;

    @SerializedName("company")
    private boolean company;

    @SerializedName("durationType")
    private int durationType;

    @SerializedName("home")
    private boolean home;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("locationDateNum")
    private int locationDayNum;

    @SerializedName("locationSize")
    private int locationSize;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("sleepProbability")
    private float sleepProbability;

    @SerializedName("ssids")
    private ArraySet<String> ssids;

    @SerializedName("stayTime")
    private HashMap<String, ArrayList<Pair<Double, Double>>> stayTime;

    public static List<ResidenceLabel> parseResidenceLabels(String str) {
        SDKLog.v(TAG, "parseResidenceLabels labelResult=" + str);
        ArrayList arrayList = new ArrayList();
        try {
            Iterator<JsonElement> it = new JsonParser().parse(str).getAsJsonArray().iterator();
            while (it.hasNext()) {
                ResidenceLabel residenceLabel = (ResidenceLabel) new Gson().fromJson(it.next(), ResidenceLabel.class);
                if (residenceLabel != null) {
                    SDKLog.v(TAG, "parseResidenceLabels, residenceLabel: " + residenceLabel.toString());
                    arrayList.add(residenceLabel);
                }
            }
        } catch (Exception e10) {
            SDKLog.e(TAG, "parseResidenceLabels Exception: " + e10.getMessage());
        }
        return arrayList;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public int getAppearDateNum() {
        return this.appearDateNum;
    }

    public int getAppearRatioType() {
        return this.appearRatioType;
    }

    public float getAverageStayDurationInDaytime() {
        return this.averageStayDurationInDaytime;
    }

    public float getAverageStayDurationInNighttime() {
        return this.averageStayDurationInNighttime;
    }

    public float getAverageStayDurationPerDay() {
        return this.averageStayDurationPerDay;
    }

    public ArraySet<String> getBssids() {
        return this.bssids;
    }

    public int getDurationType() {
        return this.durationType;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public int getLocationDayNum() {
        return this.locationDayNum;
    }

    public int getLocationSize() {
        return this.locationSize;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public float getSleepProbability() {
        return this.sleepProbability;
    }

    public ArraySet<String> getSsids() {
        return this.ssids;
    }

    public HashMap<String, ArrayList<Pair<Double, Double>>> getStayTime() {
        return this.stayTime;
    }

    public boolean isCompany() {
        return this.company;
    }

    public boolean isHome() {
        return this.home;
    }

    public String toFullString() {
        return new GsonBuilder().create().toJson(this);
    }

    public String toString() {
        return ResidenceLabelUtils.getResidenceTypeString(this.appearRatioType, this.durationType) + " Residence, longitude=" + this.longitude + " ,latitude=" + this.latitude + " ,chaos=" + this.accuracy + " ,locationSize=" + this.locationSize + " ,appearDayNum=" + this.appearDateNum + " ,dayDuration=" + this.averageStayDurationInDaytime + " ,nightDuration=" + this.averageStayDurationInNighttime + " ,sleepProbability=" + this.sleepProbability + " ,ssids=" + this.ssids;
    }
}
