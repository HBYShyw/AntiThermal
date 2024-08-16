package com.oplus.media;

import android.media.session.MediaSession;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusMediaPlayerInfo implements Parcelable {
    public static final Parcelable.Creator<OplusMediaPlayerInfo> CREATOR = new Parcelable.Creator<OplusMediaPlayerInfo>() { // from class: com.oplus.media.OplusMediaPlayerInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaPlayerInfo createFromParcel(Parcel in) {
            return new OplusMediaPlayerInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaPlayerInfo[] newArray(int size) {
            return new OplusMediaPlayerInfo[size];
        }
    };
    private int mCastState;
    private String mDeviceId;
    private int mDeviceType;
    private String mPkgName;
    private String mPlayerId;
    private MediaSession.Token mToken;

    public OplusMediaPlayerInfo() {
    }

    public OplusMediaPlayerInfo(String deviceId, int deviceType, String playerId, String pkgName, int castState, MediaSession.Token token) {
        this.mDeviceId = deviceId;
        this.mDeviceType = deviceType;
        this.mPlayerId = playerId;
        this.mPkgName = pkgName;
        this.mCastState = castState;
        this.mToken = token;
    }

    public OplusMediaPlayerInfo(Parcel in) {
        this.mDeviceId = in.readString();
        this.mDeviceType = in.readInt();
        this.mPlayerId = in.readString();
        this.mPkgName = in.readString();
        this.mCastState = in.readInt();
        this.mToken = (MediaSession.Token) in.readParcelable(MediaSession.Token.class.getClassLoader());
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public int getDeviceType() {
        return this.mDeviceType;
    }

    public String getPlayerId() {
        return this.mPlayerId;
    }

    public String getPackageName() {
        return this.mPkgName;
    }

    public int getCastState() {
        return this.mCastState;
    }

    public MediaSession.Token getToken() {
        return this.mToken;
    }

    public void setPlayerId(String playerId) {
        this.mPlayerId = playerId;
    }

    public void setDeviceId(String deviceId) {
        this.mDeviceId = deviceId;
    }

    public void setPackageName(String pkgName) {
        this.mPkgName = pkgName;
    }

    public void setDeviceType(int type) {
        this.mDeviceType = type;
    }

    public void setCastState(int castState) {
        this.mCastState = castState;
    }

    public void setToken(MediaSession.Token token) {
        this.mToken = token;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDeviceId);
        dest.writeInt(this.mDeviceType);
        dest.writeString(this.mPlayerId);
        dest.writeString(this.mPkgName);
        dest.writeInt(this.mCastState);
        dest.writeParcelable(this.mToken, 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
