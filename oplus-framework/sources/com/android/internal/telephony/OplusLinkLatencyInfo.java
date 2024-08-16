package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusLinkLatencyInfo implements Parcelable {
    public static final Parcelable.Creator<OplusLinkLatencyInfo> CREATOR = new Parcelable.Creator<OplusLinkLatencyInfo>() { // from class: com.android.internal.telephony.OplusLinkLatencyInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusLinkLatencyInfo createFromParcel(Parcel in) {
            return new OplusLinkLatencyInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusLinkLatencyInfo[] newArray(int size) {
            return new OplusLinkLatencyInfo[size];
        }
    };
    private long mEffectiveDownlink;
    private long mEffectiveUplink;
    private long mStatus;

    public OplusLinkLatencyInfo() {
        this(0L, 1L, 1L);
    }

    public OplusLinkLatencyInfo(long status, long effectiveUplink, long effectiveDownlink) {
        this.mStatus = status;
        this.mEffectiveUplink = effectiveUplink;
        this.mEffectiveDownlink = effectiveDownlink;
    }

    public OplusLinkLatencyInfo(Parcel in) {
        this.mStatus = in.readLong();
        this.mEffectiveUplink = in.readLong();
        this.mEffectiveDownlink = in.readLong();
    }

    public long getStatus() {
        return this.mStatus;
    }

    public long setStatus(long status) {
        this.mStatus = status;
        return status;
    }

    public long getEffectiveUplink() {
        return this.mEffectiveUplink;
    }

    public long setEffectiveUplink(long uplink) {
        this.mEffectiveUplink = uplink;
        return uplink;
    }

    public long getEffectiveDownlink() {
        return this.mEffectiveDownlink;
    }

    public long setEffectiveDownlink(long downlink) {
        this.mEffectiveDownlink = downlink;
        return downlink;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mStatus);
        dest.writeLong(this.mEffectiveUplink);
        dest.writeLong(this.mEffectiveDownlink);
    }
}
