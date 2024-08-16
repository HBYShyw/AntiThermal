package com.appaac.haptic.sync;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class b implements Parcelable {
    public static final Parcelable.Creator<b> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    public String f5350e;

    /* renamed from: f, reason: collision with root package name */
    public int f5351f;

    /* renamed from: g, reason: collision with root package name */
    public int f5352g;

    public b(Parcel parcel) {
        this.f5350e = parcel.readString();
        this.f5351f = parcel.readInt();
        this.f5352g = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "loop='" + this.f5351f + "',interval='" + this.f5352g + "'," + this.f5350e;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeString(this.f5350e);
        parcel.writeInt(this.f5351f);
        parcel.writeInt(this.f5352g);
    }
}
