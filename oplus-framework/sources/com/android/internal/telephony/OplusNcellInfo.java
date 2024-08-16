package com.android.internal.telephony;

/* loaded from: classes.dex */
public class OplusNcellInfo {
    private int mArfcn;
    private int mRat;
    private int mRssi;

    public OplusNcellInfo(int rat, int arfcn, int rssi) {
        this.mRat = -1;
        this.mRat = rat;
        this.mArfcn = arfcn;
        this.mRssi = rssi;
    }

    public int getRat() {
        return this.mRat;
    }

    public int getArfcn() {
        return this.mArfcn;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public String toString() {
        return "[" + this.mRat + " " + this.mArfcn + " " + this.mRssi + "]";
    }
}
