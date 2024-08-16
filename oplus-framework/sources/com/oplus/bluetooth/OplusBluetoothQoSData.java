package com.oplus.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusBluetoothQoSData implements Parcelable {
    private static final String BUNDLE_KEY_HASH_MAP = "HashMap";
    public static final Parcelable.Creator<OplusBluetoothQoSData> CREATOR = new Parcelable.Creator<OplusBluetoothQoSData>() { // from class: com.oplus.bluetooth.OplusBluetoothQoSData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusBluetoothQoSData createFromParcel(Parcel source) {
            int aclLink = source.readInt();
            int scanMode = source.readInt();
            float retxRate = source.readFloat();
            int l2cBytes = source.readInt();
            boolean musicPlaying = source.readInt() == 1;
            Bundle bundle = source.readBundle();
            HashMap<String, Integer> rssi = (HashMap) bundle.getSerializable(OplusBluetoothQoSData.BUNDLE_KEY_HASH_MAP);
            ArrayList<Integer> advItv = source.readArrayList(Integer.class.getClassLoader(), Integer.class);
            return new OplusBluetoothQoSData(aclLink, scanMode, advItv, rssi, retxRate, l2cBytes, musicPlaying);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusBluetoothQoSData[] newArray(int size) {
            return new OplusBluetoothQoSData[size];
        }
    };
    public static final int RSSI_NOT_FOUND = 1;
    public static final int SCAN_MODE_AMBIENT_DISCOVERY = 3;
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_CUSTOM_BALANCED = 8;
    public static final int SCAN_MODE_CUSTOM_LOW_POWER = 7;
    public static final int SCAN_MODE_CUSTOM_ULTRA_LOW_POWER = 10;
    public static final int SCAN_MODE_DISABLE = -2;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;
    public static final int SCAN_MODE_OPPORTUNISTIC = -1;
    public static final int SCAN_MODE_SUPER_LOW = 9;
    private List<Integer> mAdvInterval;
    private int mAsyncConnLinkNums;
    private int mCurrentScanMode;
    private boolean mIsMusicPlaying;
    private int mL2capTxBytes;
    private float mRetransmissionRate;
    private HashMap<String, Integer> mRssiValue;

    private OplusBluetoothQoSData(int aclLink, int scanMode, List<Integer> advItv, HashMap<String, Integer> rssi, float retxRate, int l2cBytes, boolean musicState) {
        this.mAsyncConnLinkNums = 0;
        this.mCurrentScanMode = -1;
        this.mRetransmissionRate = 0.0f;
        this.mL2capTxBytes = 0;
        this.mIsMusicPlaying = false;
        this.mAsyncConnLinkNums = aclLink;
        this.mCurrentScanMode = scanMode;
        this.mAdvInterval = List.copyOf(advItv);
        this.mRssiValue = rssi;
        this.mRetransmissionRate = retxRate;
        this.mL2capTxBytes = l2cBytes;
        this.mIsMusicPlaying = musicState;
    }

    public int getTxBytes() {
        return this.mL2capTxBytes;
    }

    public int getAclNums() {
        return this.mAsyncConnLinkNums;
    }

    public boolean isMusicPlaying() {
        return this.mIsMusicPlaying;
    }

    public float getRetransmissionRate() {
        return this.mRetransmissionRate;
    }

    public int getBleScanMode() {
        return this.mCurrentScanMode;
    }

    public List<Integer> getBleAdvertiseIntervals() {
        return List.copyOf(this.mAdvInterval);
    }

    public int getRSSIOfDevice(BluetoothDevice device) {
        if (device == null) {
            return 1;
        }
        String targetDeviceAddres = device.getAddress();
        if (!this.mRssiValue.containsKey(targetDeviceAddres)) {
            return 1;
        }
        return this.mRssiValue.get(targetDeviceAddres).intValue();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusBluetoothQoSData:>>> [AclNums]:").append(String.valueOf(this.mAsyncConnLinkNums)).append(", [LeScanMode]:").append(String.valueOf(this.mCurrentScanMode)).append(", [ReTxRate]:").append(String.valueOf(this.mRetransmissionRate)).append(", [IsMusicPlaying]:").append(String.valueOf(this.mIsMusicPlaying)).append(", ");
        sb.append("[RSSI]:");
        HashMap<String, Integer> hashMap = this.mRssiValue;
        if (hashMap == null) {
            sb.append("null, ");
        } else {
            for (Integer rssi : hashMap.values()) {
                sb.append(String.valueOf(rssi) + ", ");
            }
        }
        sb.append("[AdvIntervals]:");
        List<Integer> list = this.mAdvInterval;
        if (list == null) {
            sb.append("null, ");
        } else {
            for (Integer itv : list) {
                sb.append(String.valueOf(itv) + ", ");
            }
        }
        sb.append("[TxBytes]:").append(String.valueOf(this.mL2capTxBytes));
        sb.append(" <<<");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mAsyncConnLinkNums);
        parcel.writeInt(this.mCurrentScanMode);
        parcel.writeFloat(this.mRetransmissionRate);
        parcel.writeInt(this.mL2capTxBytes);
        parcel.writeInt(this.mIsMusicPlaying ? 1 : 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_HASH_MAP, this.mRssiValue);
        parcel.writeBundle(bundle);
        parcel.writeIntArray(this.mAdvInterval.stream().mapToInt(new OplusBluetoothQoSData$$ExternalSyntheticLambda0()).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private List<Integer> mAdvItvs;
        private int mScanMode = -1;
        private int mAclNums = 0;
        private float mRetxRate = 0.0f;
        private HashMap<String, Integer> mRssiValues = null;
        private int mL2cBytes = 0;
        private boolean mMusicPlaying = false;

        public Builder setLeScanMode(int scanMode) {
            this.mScanMode = scanMode;
            return this;
        }

        public Builder setAclNums(int acl) {
            this.mAclNums = acl;
            return this;
        }

        public Builder setRetransmissionRate(float retxRate) {
            this.mRetxRate = retxRate;
            return this;
        }

        public Builder setRSSIValues(HashMap<String, Integer> rssi) {
            if (this.mRssiValues == null) {
                this.mRssiValues = new HashMap<>();
            }
            this.mRssiValues.putAll(rssi);
            return this;
        }

        public Builder setBleAdvertiseIntervals(List<Integer> intervals) {
            this.mAdvItvs = List.copyOf(intervals);
            return this;
        }

        public Builder setTxBytes(int txBytes) {
            this.mL2cBytes = txBytes;
            return this;
        }

        public Builder setMusicPlaying(boolean isPlaying) {
            this.mMusicPlaying = isPlaying;
            return this;
        }

        public OplusBluetoothQoSData build() {
            return new OplusBluetoothQoSData(this.mAclNums, this.mScanMode, this.mAdvItvs, this.mRssiValues, this.mRetxRate, this.mL2cBytes, this.mMusicPlaying);
        }
    }
}
