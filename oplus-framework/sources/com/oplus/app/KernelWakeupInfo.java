package com.oplus.app;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class KernelWakeupInfo implements Parcelable {
    public static final Parcelable.Creator<KernelWakeupInfo> CREATOR = new Parcelable.Creator<KernelWakeupInfo>() { // from class: com.oplus.app.KernelWakeupInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KernelWakeupInfo createFromParcel(Parcel aidlSource) {
            KernelWakeupInfo aidlOut = new KernelWakeupInfo();
            aidlOut.readFromParcel(aidlSource);
            return aidlOut;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KernelWakeupInfo[] newArray(int aidlSize) {
            return new KernelWakeupInfo[aidlSize];
        }
    };
    public long count = 0;
    public String name;

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel aidlParcel, int aidlFlag) {
        int aidlStartPos = aidlParcel.dataPosition();
        aidlParcel.writeInt(0);
        aidlParcel.writeString(this.name);
        aidlParcel.writeLong(this.count);
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
            this.name = aidlParcel.readString();
            if (aidlParcel.dataPosition() - aidlStartPos >= aidlParcelableSize) {
                return;
            }
            this.count = aidlParcel.readLong();
        } finally {
            aidlParcel.setDataPosition(aidlStartPos + aidlParcelableSize);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
