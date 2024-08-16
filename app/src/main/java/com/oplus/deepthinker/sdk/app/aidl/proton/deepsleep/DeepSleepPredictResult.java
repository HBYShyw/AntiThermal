package com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class DeepSleepPredictResult implements Parcelable {
    public static final Parcelable.Creator<DeepSleepPredictResult> CREATOR = new Parcelable.Creator<DeepSleepPredictResult>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeepSleepPredictResult createFromParcel(Parcel parcel) {
            DeepSleepPredictResult deepSleepPredictResult = new DeepSleepPredictResult(null, null);
            String readString = parcel.readString();
            if (readString != null) {
                deepSleepPredictResult.mResultType = PredictResultType.valueOf(readString);
            }
            if (deepSleepPredictResult.mDeepSleepClusterList == null) {
                deepSleepPredictResult.mDeepSleepClusterList = new ArrayList();
            }
            parcel.readTypedList(deepSleepPredictResult.mDeepSleepClusterList, DeepSleepCluster.CREATOR);
            return deepSleepPredictResult;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeepSleepPredictResult[] newArray(int i10) {
            return new DeepSleepPredictResult[i10];
        }
    };
    private static final String TAG = "DeepSleepPredictResult";
    private List<DeepSleepCluster> mDeepSleepClusterList;
    private PredictResultType mResultType;

    public DeepSleepPredictResult(PredictResultType predictResultType, List<DeepSleepCluster> list) {
        PredictResultType predictResultType2 = PredictResultType.PREDICT_RESULT_TYPE_UNKNOWN;
        this.mResultType = predictResultType;
        this.mDeepSleepClusterList = list;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public List<DeepSleepCluster> getDeepSleepClusterList() {
        return this.mDeepSleepClusterList;
    }

    public PredictResultType getResultType() {
        return this.mResultType;
    }

    public void setClusterList(List<DeepSleepCluster> list) {
        this.mDeepSleepClusterList = list;
    }

    public void setResultType(PredictResultType predictResultType) {
        this.mResultType = predictResultType;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder("DeepSleepPredictResult:resultType=" + this.mResultType);
        List<DeepSleepCluster> list = this.mDeepSleepClusterList;
        if (list != null && list.size() > 0) {
            Iterator<DeepSleepCluster> it = this.mDeepSleepClusterList.iterator();
            while (it.hasNext()) {
                sb2.append(it.next().toString());
            }
        }
        return sb2.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        PredictResultType predictResultType = this.mResultType;
        parcel.writeString(predictResultType == null ? null : predictResultType.name());
        parcel.writeTypedList(this.mDeepSleepClusterList);
    }
}
