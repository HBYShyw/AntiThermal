package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusMediaEventFilter implements Parcelable {
    public static final Parcelable.Creator<OplusMediaEventFilter> CREATOR = new Parcelable.Creator<OplusMediaEventFilter>() { // from class: com.oplus.media.OplusMediaEventFilter.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaEventFilter createFromParcel(Parcel in) {
            return new OplusMediaEventFilter(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaEventFilter[] newArray(int size) {
            return new OplusMediaEventFilter[size];
        }
    };
    private static final String TAG = "OplusMediaEventFilter";
    private List<String> mDeviceIds;
    private List<Integer> mEventTypes;
    private List<String> mPlayerIds;

    public OplusMediaEventFilter() {
        this.mEventTypes = new ArrayList();
        this.mPlayerIds = new ArrayList();
        this.mDeviceIds = new ArrayList();
    }

    public OplusMediaEventFilter(Parcel in) {
        this.mEventTypes = new ArrayList();
        this.mPlayerIds = new ArrayList();
        this.mDeviceIds = new ArrayList();
        in.readList(this.mEventTypes, getClass().getClassLoader());
        this.mPlayerIds = in.createStringArrayList();
        this.mDeviceIds = in.createStringArrayList();
    }

    public void addEventType(int eventType) {
        this.mEventTypes.add(Integer.valueOf(eventType));
    }

    public void addPlayerId(String playerId) {
        if (playerId == null) {
            Log.e(TAG, "add playerId is null");
        } else {
            this.mPlayerIds.add(playerId);
        }
    }

    public void addDeviceId(String deviceId) {
        if (deviceId == null) {
            Log.e(TAG, "add DeviceId is null");
        } else {
            this.mDeviceIds.add(deviceId);
        }
    }

    public List<Integer> getEventTypes() {
        return this.mEventTypes;
    }

    public List<String> getPlayerIds() {
        return this.mPlayerIds;
    }

    public List<String> getDeviceIds() {
        return this.mDeviceIds;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mEventTypes);
        dest.writeStringList(this.mPlayerIds);
        dest.writeStringList(this.mDeviceIds);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
