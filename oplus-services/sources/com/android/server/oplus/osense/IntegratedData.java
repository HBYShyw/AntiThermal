package com.android.server.oplus.osense;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class IntegratedData implements Parcelable {
    public static final Parcelable.Creator<IntegratedData> CREATOR = new Parcelable.Creator<IntegratedData>() { // from class: com.android.server.oplus.osense.IntegratedData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntegratedData createFromParcel(Parcel parcel) {
            return new IntegratedData(parcel.readInt(), parcel.readLong(), parcel.readBundle());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntegratedData[] newArray(int i) {
            return new IntegratedData[i];
        }
    };
    private String mData;
    private Bundle mInfo;
    private int mResId;
    private long mTime;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public IntegratedData() {
        this(0, 0L, null);
    }

    public IntegratedData(int i, long j, Bundle bundle) {
        this.mResId = i;
        this.mTime = j;
        this.mInfo = bundle;
    }

    public int getResId() {
        return this.mResId;
    }

    public void setResId(int i) {
        this.mResId = i;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long j) {
        this.mTime = j;
    }

    public Bundle getInfo() {
        return this.mInfo;
    }

    public void setInfo(Bundle bundle) {
        this.mInfo = bundle;
    }

    public String toString() {
        return "IntegratedData{mResId=" + this.mResId + ", mTime=" + this.mTime + ", mInfo=" + this.mInfo + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mResId);
        parcel.writeLong(this.mTime);
        parcel.writeBundle(this.mInfo);
    }

    private IntegratedData(Parcel parcel) {
        this.mResId = parcel.readInt();
        this.mTime = parcel.readLong();
        this.mInfo = parcel.readBundle();
    }
}
