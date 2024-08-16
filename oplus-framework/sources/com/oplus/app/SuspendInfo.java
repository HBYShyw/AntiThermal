package com.oplus.app;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class SuspendInfo implements Parcelable {
    public static final Parcelable.Creator<SuspendInfo> CREATOR = new Parcelable.Creator<SuspendInfo>() { // from class: com.oplus.app.SuspendInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuspendInfo createFromParcel(Parcel aidlSource) {
            SuspendInfo aidlOut = new SuspendInfo();
            aidlOut.readFromParcel(aidlSource);
            return aidlOut;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuspendInfo[] newArray(int aidlSize) {
            return new SuspendInfo[aidlSize];
        }
    };
    public long suspendAttemptCount = 0;
    public long failedSuspendCount = 0;
    public long shortSuspendCount = 0;
    public long suspendTimeMillis = 0;
    public long shortSuspendTimeMillis = 0;
    public long suspendOverheadTimeMillis = 0;
    public long failedSuspendOverheadTimeMillis = 0;
    public long newBackoffCount = 0;
    public long backoffContinueCount = 0;
    public long sleepTimeMillis = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel aidlParcel, int aidlFlag) {
        int aidlStartPos = aidlParcel.dataPosition();
        aidlParcel.writeInt(0);
        aidlParcel.writeLong(this.suspendAttemptCount);
        aidlParcel.writeLong(this.failedSuspendCount);
        aidlParcel.writeLong(this.shortSuspendCount);
        aidlParcel.writeLong(this.suspendTimeMillis);
        aidlParcel.writeLong(this.shortSuspendTimeMillis);
        aidlParcel.writeLong(this.suspendOverheadTimeMillis);
        aidlParcel.writeLong(this.failedSuspendOverheadTimeMillis);
        aidlParcel.writeLong(this.newBackoffCount);
        aidlParcel.writeLong(this.backoffContinueCount);
        aidlParcel.writeLong(this.sleepTimeMillis);
        int aidlEndPos = aidlParcel.dataPosition();
        aidlParcel.setDataPosition(aidlStartPos);
        aidlParcel.writeInt(aidlEndPos - aidlStartPos);
        aidlParcel.setDataPosition(aidlEndPos);
    }

    public final void readFromParcel(Parcel aidlParcel) {
        int aidlStartPos = aidlParcel.dataPosition();
        int aidlParcelableSize = aidlParcel.readInt();
        if (aidlParcelableSize < 0) {
            return;
        }
        try {
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.suspendAttemptCount = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.failedSuspendCount = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.shortSuspendCount = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.suspendTimeMillis = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.shortSuspendTimeMillis = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.suspendOverheadTimeMillis = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.failedSuspendOverheadTimeMillis = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.newBackoffCount = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.backoffContinueCount = aidlParcel.readLong();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.sleepTimeMillis = aidlParcel.readLong();
        } finally {
            aidlParcel.setDataPosition(aidlStartPos + aidlParcelableSize);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
