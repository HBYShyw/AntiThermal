package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusMediaEvent implements Parcelable {
    public static final Parcelable.Creator<OplusMediaEvent> CREATOR = new Parcelable.Creator<OplusMediaEvent>() { // from class: com.oplus.media.OplusMediaEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaEvent createFromParcel(Parcel in) {
            return new OplusMediaEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaEvent[] newArray(int size) {
            return new OplusMediaEvent[size];
        }
    };
    public static final int TYPE_FAVORITE_STATE_CHANGED = 5;
    public static final int TYPE_MEDIA_METADATA_CHANGED = 3;
    public static final int TYPE_MEDIA_PLAYER_INFO_CHANGED = 6;
    public static final int TYPE_PLAYBACK_COMMAND = 1;
    public static final int TYPE_PLAYBACK_INFO_REQUEST = 2;
    public static final int TYPE_PLAYBACK_STATE_CHANGED = 4;
    private OplusMediaEventData mData;
    private String mDeviceId;
    private int mEventType;
    private Map<String, Object> mExtras;
    private String mPlayerId;

    /* loaded from: classes.dex */
    public static class Builder {
        private OplusMediaEventData mData;
        private String mDeviceId;
        private int mEventType;
        private String mKey;
        private Object mObj;
        private String mPlayerId;

        public Builder(int eventType) {
            this.mEventType = eventType;
        }

        public Builder setPlayerId(String playerId) {
            this.mPlayerId = playerId;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.mDeviceId = deviceId;
            return this;
        }

        public Builder setData(OplusMediaEventData data) {
            this.mData = data;
            return this;
        }

        public Builder putExtra(String key, Object data) {
            this.mKey = key;
            this.mObj = data;
            return this;
        }

        public OplusMediaEvent build() {
            return new OplusMediaEvent(this);
        }
    }

    public OplusMediaEvent(Parcel in) {
        this.mExtras = new HashMap();
        this.mEventType = in.readInt();
        this.mPlayerId = in.readString();
        this.mDeviceId = in.readString();
        in.readMap(this.mExtras, getClass().getClassLoader());
        this.mData = (OplusMediaEventData) in.readParcelable(getClass().getClassLoader());
    }

    private OplusMediaEvent(Builder builder) {
        this.mExtras = new HashMap();
        this.mEventType = builder.mEventType;
        this.mExtras.put(builder.mKey, builder.mObj);
        this.mDeviceId = builder.mDeviceId;
        this.mData = builder.mData;
        this.mPlayerId = builder.mPlayerId;
    }

    public int getEventType() {
        return this.mEventType;
    }

    public <T extends OplusMediaEventData> T getData() {
        return (T) this.mData;
    }

    public String getPlayerId() {
        return this.mPlayerId;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public Object getExtra(String key) {
        return this.mExtras.get(key);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mEventType);
        dest.writeString(this.mPlayerId);
        dest.writeString(this.mDeviceId);
        dest.writeMap(this.mExtras);
        OplusMediaEventData mediaEventData = this.mData;
        if (mediaEventData instanceof Parcelable) {
            dest.writeParcelable(mediaEventData, flags);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
