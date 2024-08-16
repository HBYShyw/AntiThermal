package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusMediaEventData implements Parcelable {
    public static final Parcelable.Creator<OplusMediaEventData> CREATOR = new Parcelable.Creator<OplusMediaEventData>() { // from class: com.oplus.media.OplusMediaEventData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaEventData createFromParcel(Parcel in) {
            return new OplusMediaEventData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaEventData[] newArray(int size) {
            return new OplusMediaEventData[size];
        }
    };

    public OplusMediaEventData() {
    }

    public OplusMediaEventData(Parcel in) {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
    }
}
