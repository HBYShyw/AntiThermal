package com.oplus.oshare;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class OplusOshareDevice implements Parcelable {
    public static final Parcelable.Creator<OplusOshareDevice> CREATOR = new Parcelable.Creator<OplusOshareDevice>() { // from class: com.oplus.oshare.OplusOshareDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusOshareDevice createFromParcel(Parcel in) {
            String name = in.readString();
            String blemac = in.readString();
            String wifimac = in.readString();
            OplusOshareState state = (OplusOshareState) in.readSerializable();
            int virtual = in.readInt();
            BluetoothDevice bluetoothDevice = (BluetoothDevice) in.readParcelable(null);
            int progress = in.readInt();
            String remaindTime = in.readString();
            Bitmap head = (Bitmap) in.readParcelable(null);
            int successNum = in.readInt();
            int totalNum = in.readInt();
            long lastFoundTime = in.readLong();
            int vender = in.readInt();
            String headIconUrl = in.readString();
            OplusOshareDevice set = new OplusOshareDevice();
            set.mName = name;
            set.mBleMac = blemac;
            set.mWifiMac = wifimac;
            set.mState = state;
            set.mVirtual = virtual;
            set.mBluetootchDevice = bluetoothDevice;
            set.mProgress = progress;
            set.mRemainTime = remaindTime;
            set.mHeadIcon = head;
            set.mSucceedNum = successNum;
            set.mTotalNum = totalNum;
            set.mLastFoundTime = lastFoundTime;
            set.mVender = vender;
            set.mHeadIconUrl = headIconUrl;
            return set;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusOshareDevice[] newArray(int size) {
            return new OplusOshareDevice[size];
        }
    };
    public static final int DEFAULT_VIRTUAL = 8;
    private BluetoothDevice mBluetootchDevice;
    private Bitmap mHeadIcon;
    private String mHeadIconUrl;
    private long mLastFoundTime;
    private int mProgress;
    private String mRemainTime;
    private int mSucceedNum;
    private int mTotalNum;
    private int mVender;
    private String mDisplayName = "";
    private String mName = "";
    private String mBleMac = "";
    private String mWifiMac = null;
    private OplusOshareState mState = OplusOshareState.IDLE;
    private int mVirtual = 8;

    public long getLastFoundTime() {
        return this.mLastFoundTime;
    }

    public void setLastFoundTime(long lastFoundTime) {
        this.mLastFoundTime = lastFoundTime;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public int getSucceedNum() {
        return this.mSucceedNum;
    }

    public void setSucceedNum(int succeedNum) {
        this.mSucceedNum = succeedNum;
    }

    public int getTotalNum() {
        return this.mTotalNum;
    }

    public void setTotalNum(int totalNum) {
        this.mTotalNum = totalNum;
    }

    public int getVender() {
        return this.mVender;
    }

    public void setVender(int vender) {
        this.mVender = vender;
    }

    public String getHeadIconUrl() {
        return this.mHeadIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.mHeadIconUrl = headIconUrl;
    }

    public Bitmap getHeadIcon() {
        return this.mHeadIcon;
    }

    public void setHeadIcon(Bitmap bitmap) {
        this.mHeadIcon = bitmap;
    }

    public String getRemainTime() {
        return this.mRemainTime;
    }

    public void setRemainTime(String remainTime) {
        this.mRemainTime = remainTime;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.mBluetootchDevice;
    }

    public boolean isVirtual() {
        return this.mVirtual > 0;
    }

    public void setVirtual(int virtual) {
        this.mVirtual = virtual;
    }

    public int getVirtual() {
        return this.mVirtual;
    }

    public void setBluetootchDevice(BluetoothDevice bluetootchDevice) {
        this.mBluetootchDevice = bluetootchDevice;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBleMac() {
        return this.mBleMac;
    }

    public void setBleMac(String bleMac) {
        this.mBleMac = bleMac;
    }

    public String getWifiMac() {
        return this.mWifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.mWifiMac = wifiMac;
    }

    public OplusOshareState getState() {
        return this.mState;
    }

    public void setState(OplusOshareState state) {
        if (this.mState.equals(OplusOshareState.TRANSIT_SUCCESS) && state.equals(OplusOshareState.TRANSIT_FAILED)) {
            return;
        }
        this.mState = state;
    }

    public String toString() {
        return "Name:" + this.mName + " Virtual:" + this.mVirtual + " State:" + this.mState;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof OplusOshareDevice)) {
            return false;
        }
        OplusOshareDevice other = (OplusOshareDevice) o;
        return compare(this.mName, other.getName()) && compare(this.mBleMac, other.getBleMac()) && compare(this.mWifiMac, other.getWifiMac());
    }

    private boolean compare(String aString, String bString) {
        if (TextUtils.isEmpty(aString) && TextUtils.isEmpty(bString)) {
            return true;
        }
        if (!TextUtils.isEmpty(aString)) {
            return aString.equals(bString);
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mBleMac);
        dest.writeString(this.mWifiMac);
        dest.writeSerializable(this.mState);
        dest.writeInt(this.mVirtual);
        dest.writeParcelable(this.mBluetootchDevice, flags);
        dest.writeInt(this.mProgress);
        dest.writeString(this.mRemainTime);
        dest.writeParcelable(this.mHeadIcon, flags);
        dest.writeInt(this.mSucceedNum);
        dest.writeInt(this.mTotalNum);
        dest.writeLong(this.mLastFoundTime);
        dest.writeInt(this.mVender);
        dest.writeString(this.mHeadIconUrl);
    }

    public void copyFrom(OplusOshareDevice o) {
        this.mBleMac = o.mBleMac;
        this.mBluetootchDevice = o.mBluetootchDevice;
        this.mDisplayName = o.mDisplayName;
        this.mHeadIcon = o.mHeadIcon;
        this.mHeadIconUrl = o.mHeadIconUrl;
        this.mLastFoundTime = o.mLastFoundTime;
        this.mProgress = o.mProgress;
        this.mRemainTime = o.mRemainTime;
        this.mState = o.mState;
        this.mSucceedNum = o.mSucceedNum;
        this.mTotalNum = o.mTotalNum;
        this.mVender = o.mVender;
        this.mWifiMac = o.mWifiMac;
        this.mName = o.mName;
        this.mVirtual = o.mVirtual;
    }
}
