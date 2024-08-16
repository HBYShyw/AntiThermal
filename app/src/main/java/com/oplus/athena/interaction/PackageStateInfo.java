package com.oplus.athena.interaction;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.util.Objects;

/* loaded from: classes.dex */
public class PackageStateInfo implements Parcelable {
    public static final Parcelable.Creator<PackageStateInfo> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    protected final String f9754e;

    /* renamed from: f, reason: collision with root package name */
    protected final int f9755f;

    /* renamed from: g, reason: collision with root package name */
    protected final int f9756g;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<PackageStateInfo> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PackageStateInfo createFromParcel(Parcel parcel) {
            return new PackageStateInfo(parcel.readString(), parcel.readInt(), parcel.readInt());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public PackageStateInfo[] newArray(int i10) {
            return new PackageStateInfo[i10];
        }
    }

    public PackageStateInfo(String str, int i10, int i11) {
        this.f9754e = (String) Preconditions.checkStringNotEmpty(str, "packageName cannot be empty");
        this.f9755f = i10;
        this.f9756g = i11;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PackageStateInfo packageStateInfo = (PackageStateInfo) obj;
        return this.f9754e.equals(packageStateInfo.f9754e) && this.f9755f == packageStateInfo.f9755f;
    }

    public int hashCode() {
        return Objects.hash(this.f9754e, Integer.valueOf(this.f9755f));
    }

    public String toString() {
        return "packageName:" + this.f9754e + ", uid:" + this.f9755f + ", appType:" + this.f9756g;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeString(this.f9754e);
        parcel.writeInt(this.f9755f);
        parcel.writeInt(this.f9756g);
    }
}
