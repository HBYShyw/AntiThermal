package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.SDKLog;
import java.util.Arrays;

/* loaded from: classes.dex */
public class DeviceEvent implements Parcelable {
    public static final Parcelable.Creator<DeviceEvent> CREATOR = new Parcelable.Creator<DeviceEvent>() { // from class: com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceEvent createFromParcel(Parcel parcel) {
            return new DeviceEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceEvent[] newArray(int i10) {
            return new DeviceEvent[i10];
        }
    };
    private static final String TAG = "DeviceEvent";
    private int mEventStateType;
    private int mEventType;

    /* loaded from: classes.dex */
    public static class Builder {
        private int mEventType = -1;
        private int mEventStateType = -1;

        public DeviceEvent build() {
            if (this.mEventType == -1) {
                SDKLog.e(DeviceEvent.TAG, "EventType not yet configured.");
            } else if (this.mEventStateType == -1) {
                SDKLog.w(DeviceEvent.TAG, "use default state type.");
                this.mEventStateType = 0;
            }
            return new DeviceEvent(this.mEventType, this.mEventStateType);
        }

        public Builder setEventStateType(int i10) {
            if (i10 != 0 && i10 != 1) {
                SDKLog.e(DeviceEvent.TAG, "Invalid stateType.");
            }
            this.mEventStateType = i10;
            return this;
        }

        public Builder setEventType(int i10) {
            this.mEventType = i10;
            return this;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DeviceEvent)) {
            return false;
        }
        DeviceEvent deviceEvent = (DeviceEvent) obj;
        return this.mEventType == deviceEvent.getEventType() && this.mEventStateType == deviceEvent.getEventStateType();
    }

    public int getEventStateType() {
        return this.mEventStateType;
    }

    public int getEventType() {
        return this.mEventType;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mEventType), Integer.valueOf(this.mEventStateType)});
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.mEventType);
        parcel.writeInt(this.mEventStateType);
    }

    private DeviceEvent(int i10, int i11) {
        this.mEventType = i10;
        this.mEventStateType = i11;
    }

    public DeviceEvent(Parcel parcel) {
        this.mEventType = parcel.readInt();
        this.mEventStateType = parcel.readInt();
    }
}
