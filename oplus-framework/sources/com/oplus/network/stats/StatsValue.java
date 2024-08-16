package com.oplus.network.stats;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class StatsValue implements Parcelable {
    public static final Parcelable.Creator<StatsValue> CREATOR = new Parcelable.Creator<StatsValue>() { // from class: com.oplus.network.stats.StatsValue.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatsValue createFromParcel(Parcel in) {
            long rxBytes = in.readLong();
            long rxPackets = in.readLong();
            long txBytes = in.readLong();
            long txPackets = in.readLong();
            long tcpRxPackets = in.readLong();
            long tcpTxPackets = in.readLong();
            return new StatsValue(rxBytes, rxPackets, txBytes, txPackets, tcpRxPackets, tcpTxPackets);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatsValue[] newArray(int size) {
            return new StatsValue[size];
        }
    };
    public long mRxBytes;
    public long mRxPackets;
    public long mTcpRxPackets;
    public long mTcpTxPackets;
    public long mTxBytes;
    public long mTxPackets;

    public StatsValue(long mRxBytes, long mRxPackets, long mTxBytes, long mTxPackets, long mTcpRxPackets, long mTcpTxPackets) {
        this.mRxBytes = mRxBytes;
        this.mRxPackets = mRxPackets;
        this.mTxBytes = mTxBytes;
        this.mTxPackets = mTxPackets;
        this.mTcpRxPackets = mTcpRxPackets;
        this.mTcpTxPackets = mTcpTxPackets;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mRxBytes);
        parcel.writeLong(this.mRxPackets);
        parcel.writeLong(this.mTxBytes);
        parcel.writeLong(this.mTxPackets);
        parcel.writeLong(this.mTcpRxPackets);
        parcel.writeLong(this.mTcpTxPackets);
    }

    public String toString() {
        return "StatsValue{mRxBytes=" + this.mRxBytes + ", mRxPackets=" + this.mRxPackets + ", mTxBytes=" + this.mTxBytes + ", mTxPackets=" + this.mTxPackets + ", mTcpRxPackets=" + this.mTcpRxPackets + ", mTcpTxPackets=" + this.mTcpTxPackets + '}';
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof StatsValue) && this.mRxBytes == ((StatsValue) obj).mRxBytes && this.mTxBytes == ((StatsValue) obj).mTxBytes && this.mRxPackets == ((StatsValue) obj).mRxPackets && this.mTxPackets == ((StatsValue) obj).mTxPackets && this.mTcpRxPackets == ((StatsValue) obj).mTcpRxPackets && this.mTcpTxPackets == ((StatsValue) obj).mTcpTxPackets;
    }
}
