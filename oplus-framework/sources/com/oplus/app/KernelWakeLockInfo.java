package com.oplus.app;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class KernelWakeLockInfo implements Parcelable {
    public static final Parcelable.Creator<KernelWakeLockInfo> CREATOR = new Parcelable.Creator<KernelWakeLockInfo>() { // from class: com.oplus.app.KernelWakeLockInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KernelWakeLockInfo createFromParcel(Parcel aidlSource) {
            KernelWakeLockInfo aidlOut = new KernelWakeLockInfo();
            aidlOut.readFromParcel(aidlSource);
            return aidlOut;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KernelWakeLockInfo[] newArray(int aidlSize) {
            return new KernelWakeLockInfo[aidlSize];
        }
    };
    public String name;
    public long activeCount = 0;
    public long lastChange = 0;
    public long maxTime = 0;
    public long totalTime = 0;
    public boolean isActive = false;
    public long activeTime = 0;
    public boolean isKernelWakelock = false;
    public int pid = 0;
    public long eventCount = 0;
    public long expireCount = 0;
    public long preventSuspendTime = 0;
    public long wakeupCount = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeString(this.name);
        parcel.writeLong(this.activeCount);
        parcel.writeLong(this.lastChange);
        parcel.writeLong(this.maxTime);
        parcel.writeLong(this.totalTime);
        parcel.writeInt(this.isActive ? 1 : 0);
        parcel.writeLong(this.activeTime);
        parcel.writeInt(this.isKernelWakelock ? 1 : 0);
        parcel.writeInt(this.pid);
        parcel.writeLong(this.eventCount);
        parcel.writeLong(this.expireCount);
        parcel.writeLong(this.preventSuspendTime);
        parcel.writeLong(this.wakeupCount);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel aidlParcel) {
        int aidlStartPos = aidlParcel.dataPosition();
        int aidlParcelableSize = aidlParcel.readInt();
        if (aidlParcelableSize >= 0) {
            try {
                if (aidlParcel.dataPosition() - aidlStartPos < aidlParcelableSize) {
                    this.name = aidlParcel.readString();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.activeCount = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.lastChange = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.maxTime = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.totalTime = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    boolean z = true;
                    this.isActive = aidlParcel.readInt() != 0;
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.activeTime = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    if (aidlParcel.readInt() == 0) {
                        z = false;
                    }
                    this.isKernelWakelock = z;
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.pid = aidlParcel.readInt();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.eventCount = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.expireCount = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.preventSuspendTime = aidlParcel.readLong();
                    if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                        return;
                    }
                    this.wakeupCount = aidlParcel.readLong();
                }
            } finally {
                aidlParcel.setDataPosition(aidlStartPos + aidlParcelableSize);
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
