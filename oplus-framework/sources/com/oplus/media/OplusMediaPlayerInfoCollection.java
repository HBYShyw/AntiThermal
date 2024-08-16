package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusMediaPlayerInfoCollection extends OplusMediaEventData {
    public static final Parcelable.Creator<OplusMediaPlayerInfoCollection> CREATOR = new Parcelable.Creator<OplusMediaPlayerInfoCollection>() { // from class: com.oplus.media.OplusMediaPlayerInfoCollection.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaPlayerInfoCollection createFromParcel(Parcel in) {
            return new OplusMediaPlayerInfoCollection(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaPlayerInfoCollection[] newArray(int size) {
            return new OplusMediaPlayerInfoCollection[size];
        }
    };
    private List<OplusMediaPlayerInfo> mMediaPlayerInfos;

    public OplusMediaPlayerInfoCollection() {
        this.mMediaPlayerInfos = new ArrayList();
    }

    public OplusMediaPlayerInfoCollection(Parcel in) {
        ArrayList arrayList = new ArrayList();
        this.mMediaPlayerInfos = arrayList;
        in.readList(arrayList, getClass().getClassLoader());
    }

    public List<OplusMediaPlayerInfo> getMediaPlayerInfos() {
        return this.mMediaPlayerInfos;
    }

    public void setMediaPlayerInfos(List<OplusMediaPlayerInfo> infos) {
        this.mMediaPlayerInfos = infos;
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mMediaPlayerInfos);
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
