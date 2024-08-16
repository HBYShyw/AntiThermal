package com.oplus.deepthinker.brightness;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class TrainedBrightnessResult implements Parcelable {
    public static final Parcelable.Creator<TrainedBrightnessResult> CREATOR = new Parcelable.Creator<TrainedBrightnessResult>() { // from class: com.oplus.deepthinker.brightness.TrainedBrightnessResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrainedBrightnessResult createFromParcel(Parcel source) {
            return new TrainedBrightnessResult(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrainedBrightnessResult[] newArray(int size) {
            return new TrainedBrightnessResult[size];
        }
    };
    private Map<String, List<TrainedBrightnessPoint>> mTrainedBrightnessPoints;

    public TrainedBrightnessResult(Map<String, List<TrainedBrightnessPoint>> trainedBrightnessPoints) {
        this.mTrainedBrightnessPoints = trainedBrightnessPoints;
    }

    public Map<String, List<TrainedBrightnessPoint>> getTrainedBrightnessPoints() {
        return this.mTrainedBrightnessPoints;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mTrainedBrightnessPoints.size());
        for (Map.Entry<String, List<TrainedBrightnessPoint>> entry : this.mTrainedBrightnessPoints.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeTypedList(entry.getValue());
        }
    }

    protected TrainedBrightnessResult(Parcel in) {
        int trainedBrightnessPointsSize = in.readInt();
        this.mTrainedBrightnessPoints = new HashMap(trainedBrightnessPointsSize);
        for (int i = 0; i < trainedBrightnessPointsSize; i++) {
            String key = in.readString();
            List<TrainedBrightnessPoint> value = in.createTypedArrayList(TrainedBrightnessPoint.CREATOR);
            this.mTrainedBrightnessPoints.put(key, value);
        }
    }
}
