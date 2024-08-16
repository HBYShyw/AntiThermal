package com.oplus.deepthinker.brightness;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class TrainedBrightnessPoint implements Parcelable {
    public static final Parcelable.Creator<TrainedBrightnessPoint> CREATOR = new Parcelable.Creator<TrainedBrightnessPoint>() { // from class: com.oplus.deepthinker.brightness.TrainedBrightnessPoint.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrainedBrightnessPoint createFromParcel(Parcel source) {
            return new TrainedBrightnessPoint(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrainedBrightnessPoint[] newArray(int size) {
            return new TrainedBrightnessPoint[size];
        }
    };
    public float x;
    public float y;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.y);
    }

    public TrainedBrightnessPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    protected TrainedBrightnessPoint(Parcel in) {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }
}
