package com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.SDKLog;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class PredictResult implements Parcelable {
    public static final Parcelable.Creator<PredictResult> CREATOR = new Parcelable.Creator<PredictResult>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PredictResult createFromParcel(Parcel parcel) {
            PredictResult predictResult = new PredictResult();
            predictResult.mPredictTime = parcel.readInt();
            predictResult.mPredictResultMap = parcel.readHashMap(PredictResult.class.getClassLoader());
            return predictResult;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PredictResult[] newArray(int i10) {
            return new PredictResult[i10];
        }
    };
    public static final int INVALID_TIME = -1;
    private static final String TAG = "PredictResult";
    private Map mPredictResultMap;
    private int mPredictTime;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getCountdownTimeByPackage(String str) {
        Object obj = this.mPredictResultMap.get(str);
        if (!(obj instanceof String)) {
            return -1;
        }
        try {
            return Integer.parseInt((String) obj);
        } catch (NumberFormatException e10) {
            SDKLog.e(TAG, "getCountdownTimeByPackage: parse time failed.", e10);
            return -1;
        }
    }

    public Set<String> getPackages() {
        Map map = this.mPredictResultMap;
        if (map == null) {
            return null;
        }
        return map.keySet();
    }

    public Map<String, String> getPredictResultMap() {
        return this.mPredictResultMap;
    }

    public int getPredictTime() {
        return this.mPredictTime;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.mPredictTime);
        parcel.writeMap(this.mPredictResultMap);
    }

    private PredictResult() {
    }

    public PredictResult(int i10, Map map) {
        this.mPredictTime = i10;
        this.mPredictResultMap = map;
    }
}
