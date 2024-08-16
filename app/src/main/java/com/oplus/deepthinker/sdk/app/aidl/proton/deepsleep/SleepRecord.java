package com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class SleepRecord implements Parcelable {
    public static final Parcelable.Creator<SleepRecord> CREATOR = new Parcelable.Creator<SleepRecord>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.SleepRecord.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepRecord createFromParcel(Parcel parcel) {
            SleepRecord sleepRecord = new SleepRecord(0L, 0L);
            sleepRecord.mSleepTime = parcel.readLong();
            sleepRecord.mWakeTime = parcel.readLong();
            return sleepRecord;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepRecord[] newArray(int i10) {
            return new SleepRecord[i10];
        }
    };
    private long mSleepTime;
    private long mWakeTime;

    public SleepRecord(long j10, long j11) {
        this.mSleepTime = j10;
        this.mWakeTime = j11;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public long getSleepTime() {
        return this.mSleepTime;
    }

    public long getWakeTime() {
        return this.mWakeTime;
    }

    public void setSleepTime(long j10) {
        this.mSleepTime = j10;
    }

    public void setWakeTime(long j10) {
        this.mWakeTime = j10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeLong(this.mSleepTime);
        parcel.writeLong(this.mWakeTime);
    }
}
