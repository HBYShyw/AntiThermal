package com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class PredictAABResult implements Parcelable {
    public static final Parcelable.Creator<PredictAABResult> CREATOR = new Parcelable.Creator<PredictAABResult>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictAABResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PredictAABResult createFromParcel(Parcel parcel) {
            PredictAABResult predictAABResult = new PredictAABResult();
            predictAABResult.mPredictResultMap = parcel.readHashMap(PredictAABResult.class.getClassLoader());
            return predictAABResult;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PredictAABResult[] newArray(int i10) {
            return new PredictAABResult[i10];
        }
    };
    public static final int INVALID_BUCKET = -1;
    private HashMap mPredictResultMap;

    private void putArrayToMap(ArrayList<String> arrayList, int i10) {
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            this.mPredictResultMap.put(it.next(), Integer.valueOf(i10));
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Set<String> getPackages() {
        HashMap hashMap = this.mPredictResultMap;
        if (hashMap == null) {
            return null;
        }
        return hashMap.keySet();
    }

    public int getPredictAppStandbyBucket(String str) {
        Object obj = this.mPredictResultMap.get(str);
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        return -1;
    }

    public HashMap<String, Integer> getPredictResultMap() {
        return this.mPredictResultMap;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeMap(this.mPredictResultMap);
    }

    public PredictAABResult(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3, ArrayList<String> arrayList4) {
        this.mPredictResultMap = new HashMap();
        putArrayToMap(arrayList, 10);
        putArrayToMap(arrayList2, 20);
        putArrayToMap(arrayList3, 30);
        putArrayToMap(arrayList4, 40);
    }

    private PredictAABResult() {
    }
}
