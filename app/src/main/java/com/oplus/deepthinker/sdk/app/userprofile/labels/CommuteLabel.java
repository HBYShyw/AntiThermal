package com.oplus.deepthinker.sdk.app.userprofile.labels;

import android.util.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oplus.deepthinker.sdk.app.SDKLog;

/* loaded from: classes.dex */
public class CommuteLabel {
    private static final String TAG = "CommuteLabel";
    private Pair<Double, Double> mArriveCompanyTime;
    private Pair<Double, Double> mArriveHomeTime;
    private Pair<Double, Double> mLeaveCompanyTime;
    private Pair<Double, Double> mLeaveHomeTime;

    public static CommuteLabel parseCommuteLabel(String str) {
        SDKLog.d(TAG, "parseCommuteLabel labelResult=" + str);
        try {
            CommuteLabel commuteLabel = (CommuteLabel) new Gson().fromJson(str, CommuteLabel.class);
            if (commuteLabel != null) {
                SDKLog.d(TAG, "parseCommuteLabel, commuteLabel: " + commuteLabel.toString());
            }
            return commuteLabel;
        } catch (Exception e10) {
            SDKLog.e(TAG, "parseCommuteLabel Exception: " + e10.getMessage());
            return null;
        }
    }

    public Pair<Double, Double> getArriveCompanyTime() {
        return this.mArriveCompanyTime;
    }

    public Pair<Double, Double> getArriveHomeTime() {
        return this.mArriveHomeTime;
    }

    public Pair<Double, Double> getLeaveCompanyTime() {
        return this.mLeaveCompanyTime;
    }

    public Pair<Double, Double> getLeaveHomeTime() {
        return this.mLeaveHomeTime;
    }

    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
