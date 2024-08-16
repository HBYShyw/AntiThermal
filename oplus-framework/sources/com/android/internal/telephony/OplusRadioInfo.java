package com.android.internal.telephony;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class OplusRadioInfo {
    private int mArfcn;
    private int mBand;
    private int mCellId;
    private int mLac;
    private int mMcc;
    private int mMnc;
    private ArrayList<OplusNcellInfo> mNcells;
    private int mRat;
    private int mRrstatus;
    private int mRssi;
    private int mSinr;
    private int mTxpower;

    public OplusRadioInfo(int rat, int mcc, int mnc, int lac, int cellId, int arfcn, int band, int rssi, int sinr, int rrstatus, int txpower, ArrayList<OplusNcellInfo> ncells) {
        this.mNcells = new ArrayList<>();
        this.mRat = rat;
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mLac = lac;
        this.mCellId = cellId;
        this.mArfcn = arfcn;
        this.mBand = band;
        this.mRssi = rssi;
        this.mSinr = sinr;
        this.mRrstatus = rrstatus;
        this.mTxpower = txpower;
        this.mNcells = ncells;
    }

    public int getRat() {
        return this.mRat;
    }

    public int getMcc() {
        return this.mMcc;
    }

    public int getMnc() {
        return this.mMnc;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCellId() {
        return this.mCellId;
    }

    public int getArfcn() {
        return this.mArfcn;
    }

    public int getBand() {
        return this.mBand;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public int getSinr() {
        return this.mSinr;
    }

    public int getRrStatus() {
        return this.mRrstatus;
    }

    public int getTxpower() {
        return this.mTxpower;
    }

    public ArrayList<OplusNcellInfo> getNcells() {
        return this.mNcells;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.mNcells.size(); i++) {
            sb.append(this.mNcells.get(i).toString() + ",");
        }
        return "rat=" + this.mRat + ", mcc=" + this.mMcc + ", mnc=" + this.mMnc + ", lac=" + this.mLac + ", ci=" + this.mCellId + ", arfcn=" + this.mArfcn + ", band=" + this.mBand + ", rssi=" + this.mRssi + ", sinr=" + this.mSinr + ", rrstatus=" + this.mRrstatus + ",txpower=" + this.mTxpower + " " + sb.toString();
    }
}
