package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusFavoriteStateData extends OplusMediaEventData {
    public static final Parcelable.Creator<OplusFavoriteStateData> CREATOR = new Parcelable.Creator<OplusFavoriteStateData>() { // from class: com.oplus.media.OplusFavoriteStateData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFavoriteStateData createFromParcel(Parcel in) {
            return new OplusFavoriteStateData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFavoriteStateData[] newArray(int size) {
            return new OplusFavoriteStateData[size];
        }
    };
    public static final int FAVORITE_STATE_LIKE = 1;
    public static final int FAVORITE_STATE_UNKNOWN = 0;
    public static final int FAVORITE_STATE_UNLIKE = 2;
    private int mState;

    public OplusFavoriteStateData() {
    }

    public OplusFavoriteStateData(int state) {
        this.mState = state;
    }

    public OplusFavoriteStateData(Parcel in) {
        this.mState = in.readInt();
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int state) {
        this.mState = state;
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mState);
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
