package com.oplus.deepthinker.sdk.app.aidl.proton.periodtopapps;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Map;

/* loaded from: classes.dex */
public class PeriodTopAppsResult implements Parcelable {
    public static final Parcelable.Creator<PeriodTopAppsResult> CREATOR = new Parcelable.Creator<PeriodTopAppsResult>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.periodtopapps.PeriodTopAppsResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PeriodTopAppsResult createFromParcel(Parcel parcel) {
            PeriodTopAppsResult periodTopAppsResult = new PeriodTopAppsResult();
            periodTopAppsResult.mTimePeriodStart = parcel.readFloat();
            periodTopAppsResult.mTimePeriodEnd = parcel.readFloat();
            periodTopAppsResult.mRankPkgnameMap = parcel.readHashMap(PeriodTopAppsResult.class.getClassLoader());
            return periodTopAppsResult;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PeriodTopAppsResult[] newArray(int i10) {
            return new PeriodTopAppsResult[i10];
        }
    };
    private Map mRankPkgnameMap;
    private float mTimePeriodEnd;
    private float mTimePeriodStart;

    public PeriodTopAppsResult(float f10, float f11, Map map) {
        this.mTimePeriodStart = f10;
        this.mTimePeriodEnd = f11;
        this.mRankPkgnameMap = map;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Map getRankPkgnameMap() {
        return this.mRankPkgnameMap;
    }

    public float getTimePeriodEnd() {
        return this.mTimePeriodEnd;
    }

    public float getTimePeriodStart() {
        return this.mTimePeriodStart;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeFloat(this.mTimePeriodStart);
        parcel.writeFloat(this.mTimePeriodEnd);
        parcel.writeMap(this.mRankPkgnameMap);
    }

    public PeriodTopAppsResult() {
    }
}
