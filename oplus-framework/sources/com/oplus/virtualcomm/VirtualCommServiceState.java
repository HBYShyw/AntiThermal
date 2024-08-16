package com.oplus.virtualcomm;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public class VirtualCommServiceState implements Parcelable {
    public static final int CONNECTION_TYPE_AP = 16;
    public static final int CONNECTION_TYPE_BLE = 4;
    public static final int CONNECTION_TYPE_BT = 1;
    public static final int CONNECTION_TYPE_P2P = 2;
    public static final Parcelable.Creator<VirtualCommServiceState> CREATOR = new Parcelable.Creator<VirtualCommServiceState>() { // from class: com.oplus.virtualcomm.VirtualCommServiceState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VirtualCommServiceState createFromParcel(Parcel in) {
            return new VirtualCommServiceState(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VirtualCommServiceState[] newArray(int size) {
            return new VirtualCommServiceState[size];
        }
    };
    static final String LOG_TAG = "VirtualCommState";
    public static final int STATE_IN_SERVICE = 0;
    public static final int STATE_NOT_AVAILABLE = 2;
    public static final int STATE_OUT_OF_SERVICE = 1;
    private int mConnectType;
    private String mLocalDeviceName;
    private String mPeerDeviceModel;
    private String mPeerDeviceName;
    private int mState;

    public VirtualCommServiceState() {
        this.mState = 2;
        this.mPeerDeviceName = null;
        this.mPeerDeviceModel = null;
        this.mLocalDeviceName = null;
        this.mConnectType = 0;
    }

    public VirtualCommServiceState(VirtualCommServiceState s) {
        this.mState = 2;
        this.mPeerDeviceName = null;
        this.mPeerDeviceModel = null;
        this.mLocalDeviceName = null;
        this.mConnectType = 0;
        copyFrom(s);
    }

    private void copyFrom(VirtualCommServiceState s) {
        this.mState = s.mState;
        this.mPeerDeviceName = s.mPeerDeviceName;
        this.mPeerDeviceModel = s.mPeerDeviceModel;
        this.mLocalDeviceName = s.mLocalDeviceName;
        this.mConnectType = s.mConnectType;
    }

    public VirtualCommServiceState(Parcel in) {
        this.mState = 2;
        this.mPeerDeviceName = null;
        this.mPeerDeviceModel = null;
        this.mLocalDeviceName = null;
        this.mConnectType = 0;
        this.mState = in.readInt();
        this.mPeerDeviceName = in.readString();
        this.mPeerDeviceModel = in.readString();
        this.mLocalDeviceName = in.readString();
        this.mConnectType = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mState);
        out.writeString(this.mPeerDeviceName);
        out.writeString(this.mPeerDeviceModel);
        out.writeString(this.mLocalDeviceName);
        out.writeInt(this.mConnectType);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public void setPeerDeviceName(String peerName) {
        this.mPeerDeviceName = peerName;
    }

    public void setPeerDeviceModel(String peerModel) {
        this.mPeerDeviceModel = peerModel;
    }

    public void setLocalDeviceName(String localName) {
        this.mLocalDeviceName = localName;
    }

    public void setConnectType(int connectType) {
        this.mConnectType = connectType;
    }

    public int getState() {
        return this.mState;
    }

    public String getPeerDeviceName() {
        return this.mPeerDeviceName;
    }

    public String getPeerDeviceModel() {
        return this.mPeerDeviceModel;
    }

    public String getLocalDeviceName() {
        return this.mPeerDeviceName;
    }

    public int getConnectType() {
        return this.mConnectType;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VirtualCommServiceState that = (VirtualCommServiceState) o;
        if (this.mState == that.mState && this.mConnectType == that.mConnectType && Objects.equals(this.mPeerDeviceName, that.mPeerDeviceName) && Objects.equals(this.mLocalDeviceName, that.mLocalDeviceName)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mState), this.mPeerDeviceName, this.mLocalDeviceName, Integer.valueOf(this.mConnectType));
    }

    public String toString() {
        return "VirtualCommServiceState{mState=" + this.mState + ", mPeerDeviceName='" + this.mPeerDeviceName + "', mLocalDeviceName='" + this.mLocalDeviceName + "', mConnectType=" + this.mConnectType + '}';
    }
}
