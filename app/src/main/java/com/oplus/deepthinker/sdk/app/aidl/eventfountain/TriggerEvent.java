package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class TriggerEvent implements Parcelable {
    public static final Parcelable.Creator<TriggerEvent> CREATOR = new Parcelable.Creator<TriggerEvent>() { // from class: com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TriggerEvent createFromParcel(Parcel parcel) {
            return new TriggerEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TriggerEvent[] newArray(int i10) {
            return new TriggerEvent[i10];
        }
    };
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_UID = "uid";
    public static final String GAME_SCENE_ID = "scene_id";
    public static final int INPUT_ACTIVITY_PAUSED = 30;
    public static final int INPUT_ACTIVITY_RESUMED = 11;
    public static final int INPUT_CAMERA_START = 18;
    public static final int INPUT_CAMERA_STOP = 19;
    public static final int INPUT_FILE_DOWNLOAD_START = 31;
    public static final int INPUT_FILE_DOWNLOAD_STOP = 32;
    public static final int INPUT_GAME_SCENE = 37;
    public static final String INPUT_GPS_EXTRA_HASH = "location_receiver_hash";
    public static final int INPUT_GPS_OFF = 15;
    public static final int INPUT_GPS_ON = 14;
    public static final int INPUT_LOCATION_REQUEST_OFF = 13;
    public static final int INPUT_LOCATION_REQUEST_ON = 12;
    public static final int INPUT_LOCATION_STATE = 35;
    public static final int INPUT_NOTIFICATION = 36;
    public static final int INPUT_NOTIFY_POSTED = 28;
    public static final int INPUT_NOTIFY_REMOVED = 29;
    public static final int INPUT_PROCESS_DIED = 22;
    public static final int INPUT_PROCESS_FRONT = 21;
    public static final int INPUT_SENSOR_START = 16;
    public static final int INPUT_SENSOR_STOP = 17;
    public static final int INPUT_VIDEO_START = 26;
    public static final int INPUT_VIDEO_STOP = 27;
    public static final int INPUT_VPN_TRANSPORT_CREATE = 33;
    public static final int INPUT_VPN_TRANSPORT_DESTROY = 34;
    public static final int INPUT_WAKELOCK_ACQUIRED = 24;
    public static final int INPUT_WAKELOCK_RELEASED = 25;
    public static final String LOCATION_STATUS_EXTRA = "status";
    public static final int LOCATION_STATUS_START = 1;
    public static final int LOCATION_STATUS_STOP = 0;
    public static final int NOTIFICATION_EVENT_CONNECT = -1;
    public static final int NOTIFICATION_EVENT_DISCONNECT = -2;
    public static final int NOTIFICATION_EVENT_POST = 1;
    public static final int NOTIFICATION_EVENT_REMOVE = 0;
    public static final String NOTIFICATION_ID = "id";
    public static final String NOTIFICATION_ONGOING = "isOngoing";
    public static final String NOTIFICATION_TAG = "tag";
    public static final String NOTIFICATION_TYPE = "event_type";
    public static final String NOTIFICATION_USER = "userHandle";
    public static final int TRIGGER_NEXT_APP = 10001;
    private int mEventId;
    private Bundle mExtraData;
    private int mPid;
    private String mPkgName;

    public TriggerEvent(int i10, int i11, String str, Bundle bundle) {
        this.mEventId = i10;
        this.mPid = i11;
        this.mPkgName = str;
        this.mExtraData = bundle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getEventId() {
        return this.mEventId;
    }

    public Bundle getExtraData() {
        return this.mExtraData;
    }

    public int getPid() {
        return this.mPid;
    }

    public String getPkgName() {
        return this.mPkgName;
    }

    public void setExtraData(Bundle bundle) {
        this.mExtraData = bundle;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder("TriggerEvent :");
        sb2.append("\teventId is :");
        sb2.append(this.mEventId);
        sb2.append("\tpid is : ");
        sb2.append(this.mPid);
        sb2.append("\t\tpackageName is : ");
        sb2.append(this.mPkgName);
        if (this.mExtraData != null) {
            sb2.append("\tExtraData is : ");
            sb2.append(this.mExtraData.toString());
        }
        return sb2.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.mEventId);
        parcel.writeInt(this.mPid);
        parcel.writeString(this.mPkgName);
        parcel.writeBundle(this.mExtraData);
    }

    public TriggerEvent(Parcel parcel) {
        this.mEventId = parcel.readInt();
        this.mPid = parcel.readInt();
        this.mPkgName = parcel.readString();
        this.mExtraData = parcel.readBundle(getClass().getClassLoader());
    }
}
