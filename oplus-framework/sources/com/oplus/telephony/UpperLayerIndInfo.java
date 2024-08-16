package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class UpperLayerIndInfo implements Parcelable {
    public static final Parcelable.Creator<UpperLayerIndInfo> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.UpperLayerIndInfo.1
        @Override // android.os.Parcelable.Creator
        public UpperLayerIndInfo createFromParcel(Parcel in) {
            return new UpperLayerIndInfo(in);
        }

        @Override // android.os.Parcelable.Creator
        public UpperLayerIndInfo[] newArray(int size) {
            return new UpperLayerIndInfo[size];
        }
    };
    public static final int INVALID = -1;
    public static final int PLMN_INFO_LIST_AVAILABLE = 1;
    public static final int PLMN_INFO_LIST_UNAVAILABLE = 0;
    private static final String TAG = "UpperLayerIndInfo";
    public static final int UPPER_LAYER_IND_INFO_AVAILABLE = 1;
    public static final int UPPER_LAYER_IND_INFO_UNAVAILABLE = 0;
    private int mPlmnInfoListAvailable;
    private int mUpperLayerIndInfoAvailable;

    public UpperLayerIndInfo(int plmn, int ulinfo) {
        this.mPlmnInfoListAvailable = 0;
        this.mUpperLayerIndInfoAvailable = 0;
        this.mPlmnInfoListAvailable = plmn;
        this.mUpperLayerIndInfoAvailable = ulinfo;
    }

    public UpperLayerIndInfo(Parcel in) {
        this.mPlmnInfoListAvailable = 0;
        this.mUpperLayerIndInfoAvailable = 0;
        this.mPlmnInfoListAvailable = in.readInt();
        this.mUpperLayerIndInfoAvailable = in.readInt();
    }

    public int getPlmnInfoListAvailable() {
        return this.mPlmnInfoListAvailable;
    }

    public int getUpperLayerIndInfoAvailable() {
        return this.mUpperLayerIndInfoAvailable;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mPlmnInfoListAvailable);
        out.writeInt(this.mUpperLayerIndInfoAvailable);
    }

    public void readFromParcel(Parcel in) {
        this.mPlmnInfoListAvailable = in.readInt();
        this.mUpperLayerIndInfoAvailable = in.readInt();
    }

    public String toString() {
        return "UpperLayerIndInfo: PLMN: " + getPlmnInfoListAvailable() + " UpperLayerIndInfo: " + getUpperLayerIndInfoAvailable();
    }
}
