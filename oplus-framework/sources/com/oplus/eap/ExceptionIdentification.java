package com.oplus.eap;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public class ExceptionIdentification implements Parcelable {
    public static final Parcelable.Creator<ExceptionIdentification> CREATOR = new Parcelable.Creator<ExceptionIdentification>() { // from class: com.oplus.eap.ExceptionIdentification.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExceptionIdentification createFromParcel(Parcel in) {
            return new ExceptionIdentification(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExceptionIdentification[] newArray(int size) {
            return new ExceptionIdentification[size];
        }
    };
    private int mCount;
    private final String mId;

    public ExceptionIdentification(String id, int count) {
        this.mId = id;
        this.mCount = count;
    }

    protected ExceptionIdentification(Parcel in) {
        this.mId = in.readString();
        this.mCount = in.readInt();
    }

    public String getId() {
        return this.mId;
    }

    public int getCount() {
        return this.mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeInt(this.mCount);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ExceptionIdentification other = (ExceptionIdentification) obj;
        return Objects.equals(this.mId, other.mId);
    }

    public int hashCode() {
        return Objects.hash(this.mId);
    }

    public String toString() {
        return "ExceptionId{id=" + this.mId + ", count=" + this.mCount + "}";
    }
}
