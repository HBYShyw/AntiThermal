package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusPlaybackStateData extends OplusMediaEventData {
    public static final Parcelable.Creator<OplusPlaybackStateData> CREATOR = new Parcelable.Creator<OplusPlaybackStateData>() { // from class: com.oplus.media.OplusPlaybackStateData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPlaybackStateData createFromParcel(Parcel in) {
            return new OplusPlaybackStateData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPlaybackStateData[] newArray(int size) {
            return new OplusPlaybackStateData[size];
        }
    };
    public static final int STATE_BUFFERING = 6;
    public static final int STATE_CONNECTING = 8;
    public static final int STATE_ERROR = 7;
    public static final int STATE_FAST_FORWARDING = 4;
    public static final int STATE_NONE = 0;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_REWINDING = 5;
    public static final int STATE_SKIPPING_TO_NEXT = 10;
    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    public static final int STATE_STOPPED = 1;
    private int mState;

    public OplusPlaybackStateData() {
    }

    public OplusPlaybackStateData(int state) {
        this.mState = state;
    }

    public OplusPlaybackStateData(Parcel in) {
        this.mState = in.readInt();
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int state) {
        this.mState = state;
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.mState);
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
