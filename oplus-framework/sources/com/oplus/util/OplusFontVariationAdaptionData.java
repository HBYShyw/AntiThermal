package com.oplus.util;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class OplusFontVariationAdaptionData implements Parcelable {
    public static final Parcelable.Creator<OplusFontVariationAdaptionData> CREATOR = new Parcelable.Creator<OplusFontVariationAdaptionData>() { // from class: com.oplus.util.OplusFontVariationAdaptionData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFontVariationAdaptionData createFromParcel(Parcel in) {
            return new OplusFontVariationAdaptionData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFontVariationAdaptionData[] newArray(int size) {
            return new OplusFontVariationAdaptionData[size];
        }
    };
    private ArrayList<String> mList1 = new ArrayList<>();
    private ArrayList<String> mList2 = new ArrayList<>();
    private String mEnable = "true";

    public OplusFontVariationAdaptionData() {
    }

    public OplusFontVariationAdaptionData(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mEnable);
        out.writeStringList(this.mList1);
        out.writeStringList(this.mList2);
    }

    public void readFromParcel(Parcel in) {
        this.mEnable = in.readString();
        this.mList1 = in.createStringArrayList();
        this.mList2 = in.createStringArrayList();
    }

    public String getEnable() {
        return this.mEnable;
    }

    public void setEnable(String value) {
        this.mEnable = value;
    }

    public ArrayList<String> getAppNameList() {
        return this.mList1;
    }

    public ArrayList<String> getWghtValueList() {
        return this.mList2;
    }
}
