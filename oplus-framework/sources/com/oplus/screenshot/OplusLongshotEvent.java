package com.oplus.screenshot;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusLongshotEvent implements Parcelable {
    public static final Parcelable.Creator<OplusLongshotEvent> CREATOR = new Parcelable.Creator<OplusLongshotEvent>() { // from class: com.oplus.screenshot.OplusLongshotEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusLongshotEvent createFromParcel(Parcel in) {
            return new OplusLongshotEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusLongshotEvent[] newArray(int size) {
            return new OplusLongshotEvent[size];
        }
    };
    private static final String TAG = "OplusLongshotEvent";
    private boolean mIsOverScroll;
    private int mOffset;
    private String mSource;

    public OplusLongshotEvent(String source, int offset, boolean isOverScroll) {
        this.mSource = null;
        this.mOffset = 0;
        this.mIsOverScroll = false;
        this.mSource = source;
        this.mOffset = offset;
        this.mIsOverScroll = isOverScroll;
    }

    public OplusLongshotEvent(Parcel in) {
        this.mSource = null;
        this.mOffset = 0;
        this.mIsOverScroll = false;
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mSource);
        parcel.writeInt(this.mOffset);
        parcel.writeInt(this.mIsOverScroll ? 1 : 0);
    }

    public void readFromParcel(Parcel in) {
        this.mSource = in.readString();
        this.mOffset = in.readInt();
        this.mIsOverScroll = in.readInt() == 1;
    }

    public String getSource() {
        return this.mSource;
    }

    public int getOffset() {
        return this.mOffset;
    }

    public boolean isOverScroll() {
        return this.mIsOverScroll;
    }
}
