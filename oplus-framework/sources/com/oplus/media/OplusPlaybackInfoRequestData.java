package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusPlaybackInfoRequestData extends OplusMediaEventData {
    public static final Parcelable.Creator<OplusPlaybackInfoRequestData> CREATOR = new Parcelable.Creator<OplusPlaybackInfoRequestData>() { // from class: com.oplus.media.OplusPlaybackInfoRequestData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPlaybackInfoRequestData createFromParcel(Parcel in) {
            return new OplusPlaybackInfoRequestData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPlaybackInfoRequestData[] newArray(int size) {
            return new OplusPlaybackInfoRequestData[size];
        }
    };
    public static final int REQUEST_FAVORITE_STATE = 3;
    public static final int REQUEST_MEDIA_METADATA = 1;
    public static final int REQUEST_PLAYBACK_STATE = 2;
    private int mRequest;

    public OplusPlaybackInfoRequestData() {
    }

    public OplusPlaybackInfoRequestData(Parcel in) {
        this.mRequest = in.readInt();
    }

    public OplusPlaybackInfoRequestData(int request) {
        this.mRequest = request;
    }

    public int getRequest() {
        return this.mRequest;
    }

    public void setRequest(int request) {
        this.mRequest = request;
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.mRequest);
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
