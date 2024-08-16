package com.oplus.deepthinker.sdk.app.userprofile.labels;

import com.oplus.deepthinker.sdk.app.SDKLog;

/* loaded from: classes.dex */
public class StayLocationLabel {
    private static final String EMPTY_LABEL = "null";
    private static final int INDEX_ABSCISSA = 0;
    private static final int INDEX_APPEAR_DATE_NUM = 7;
    private static final int INDEX_DAY_DURATION = 5;
    private static final int INDEX_DURATION = 4;
    private static final int INDEX_NIGHT_DURATION = 6;
    private static final int INDEX_ORDINATE = 1;
    private static final int INDEX_RADIUS = 3;
    private static final int INDEX_SIZE = 2;
    public static final int KEY_LOCATION_ONE = 100;
    public static final int KEY_LOCATION_TWO = 101;
    private static final String TAG = "StayLocationLabel";
    private int mAppearDateNum;
    private double[] mCoordinate;
    private double mDayDuration;
    private double mDuration;
    private double mNightDuration;
    private float mRadius;
    private int mSize;
    private int mTag;

    public StayLocationLabel(double d10, double d11, int i10, float f10, double d12, double d13, double d14, int i11) {
        this.mCoordinate = r0;
        double[] dArr = {d10, d11};
        this.mSize = i10;
        this.mRadius = f10;
        this.mDuration = d12;
        this.mDayDuration = d13;
        this.mNightDuration = d14;
        this.mAppearDateNum = i11;
    }

    public static StayLocationLabel parseResidenceLabel(String str) {
        if (str == null || EMPTY_LABEL.equals(str)) {
            return null;
        }
        try {
            String[] split = str.split(",");
            return new StayLocationLabel(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Integer.parseInt(split[2]), Float.parseFloat(split[3]), Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]), Integer.parseInt(split[7]));
        } catch (Exception e10) {
            SDKLog.w(TAG, "parseResidenceLabel Exception: " + e10.getMessage());
            return null;
        }
    }

    public double getAbscissa() {
        return this.mCoordinate[0];
    }

    public int getAppearDateNum() {
        return this.mAppearDateNum;
    }

    public double getDayDuration() {
        return this.mDayDuration;
    }

    public double getDuration() {
        return this.mDuration;
    }

    public double getNightDuration() {
        return this.mNightDuration;
    }

    public double getOrdinate() {
        return this.mCoordinate[1];
    }

    public float getRadius() {
        return this.mRadius;
    }

    public int getSize() {
        return this.mSize;
    }

    public int getTag() {
        return this.mTag;
    }

    public void setTag(int i10) {
        this.mTag = i10;
    }

    public String toString() {
        return "StayLocationLabel{mCoordinate[0]=" + this.mCoordinate[0] + ", mCoordinate[1]=" + this.mCoordinate[1] + ", mSize=" + this.mSize + ", mRadius=" + this.mRadius + ", mDuration=" + this.mDuration + ", mDayDuration=" + this.mDayDuration + ", mNightDuration=" + this.mNightDuration + ", mAppearDateNum=" + this.mAppearDateNum + ", mTag=" + this.mTag + '}';
    }
}
