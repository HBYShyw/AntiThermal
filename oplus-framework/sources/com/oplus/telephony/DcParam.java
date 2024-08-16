package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class DcParam implements Parcelable {
    public static final Parcelable.Creator<DcParam> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.DcParam.1
        @Override // android.os.Parcelable.Creator
        public DcParam createFromParcel(Parcel in) {
            return new DcParam(in);
        }

        @Override // android.os.Parcelable.Creator
        public DcParam[] newArray(int size) {
            return new DcParam[size];
        }
    };
    public static final int DCNR_RESTRICTED = 0;
    public static final int DCNR_UNRESTRICTED = 1;
    public static final int ENDC_AVAILABLE = 1;
    public static final int ENDC_UNAVAILABLE = 0;
    public static final int INVALID = -1;
    private static final String TAG = "DcParam";
    private int mDcnr;
    private int mEndc;

    public DcParam(int endc, int dcnr) {
        this.mEndc = 0;
        this.mDcnr = 0;
        this.mEndc = endc;
        this.mDcnr = dcnr;
    }

    public DcParam(Parcel in) {
        this.mEndc = 0;
        this.mDcnr = 0;
        this.mEndc = in.readInt();
        this.mDcnr = in.readInt();
    }

    public int getEndc() {
        return this.mEndc;
    }

    public int getDcnr() {
        return this.mDcnr;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mEndc);
        out.writeInt(this.mDcnr);
    }

    public void readFromParcel(Parcel in) {
        this.mEndc = in.readInt();
        this.mDcnr = in.readInt();
    }

    public String toString() {
        return "DcParam: Endc: " + getEndc() + " Dcnr: " + getDcnr();
    }
}
