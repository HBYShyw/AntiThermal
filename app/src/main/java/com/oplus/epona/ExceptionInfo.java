package com.oplus.epona;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ExceptionInfo implements Parcelable {
    public static final Parcelable.Creator<ExceptionInfo> CREATOR = new Parcelable.Creator<ExceptionInfo>() { // from class: com.oplus.epona.ExceptionInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExceptionInfo createFromParcel(Parcel parcel) {
            return new ExceptionInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExceptionInfo[] newArray(int i10) {
            return new ExceptionInfo[i10];
        }
    };
    private String mMessage;
    private String mName;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public String getName() {
        return this.mName;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeString(this.mName);
        parcel.writeString(this.mMessage);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ExceptionInfo(Throwable th) {
        this.mName = th.getClass().getName();
        this.mMessage = th.getMessage();
    }

    private ExceptionInfo(Parcel parcel) {
        this.mName = parcel.readString();
        this.mMessage = parcel.readString();
    }
}
