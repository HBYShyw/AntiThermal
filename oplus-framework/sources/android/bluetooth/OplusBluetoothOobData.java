package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusBluetoothOobData implements Parcelable {
    public static final Parcelable.Creator<OplusBluetoothOobData> CREATOR = new Parcelable.Creator<OplusBluetoothOobData>() { // from class: android.bluetooth.OplusBluetoothOobData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusBluetoothOobData createFromParcel(Parcel in) {
            return new OplusBluetoothOobData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusBluetoothOobData[] newArray(int size) {
            return new OplusBluetoothOobData[size];
        }
    };
    private int mFlag;
    private byte[] mLeBluetoothDeviceAddress;
    private byte[] mLeSecureConnectionsConfirmation;
    private byte[] mLeSecureConnectionsRandom;
    private int mRole;
    private byte[] mSecurityManagerTk;

    public OplusBluetoothOobData() {
    }

    private OplusBluetoothOobData(Parcel in) {
        this.mLeBluetoothDeviceAddress = in.createByteArray();
        this.mSecurityManagerTk = in.createByteArray();
        this.mLeSecureConnectionsConfirmation = in.createByteArray();
        this.mLeSecureConnectionsRandom = in.createByteArray();
        this.mRole = in.readInt();
        this.mFlag = in.readInt();
    }

    public byte[] getLeBluetoothDeviceAddress() {
        return this.mLeBluetoothDeviceAddress;
    }

    public void setLeBluetoothDeviceAddress(byte[] leBluetoothDeviceAddress) {
        this.mLeBluetoothDeviceAddress = leBluetoothDeviceAddress;
    }

    public byte[] getSecurityManagerTk() {
        return this.mSecurityManagerTk;
    }

    public void setSecurityManagerTk(byte[] securityManagerTk) {
        this.mSecurityManagerTk = securityManagerTk;
    }

    public byte[] getLeSecureConnectionsConfirmation() {
        return this.mLeSecureConnectionsConfirmation;
    }

    public void setLeSecureConnectionsConfirmation(byte[] leSecureConnectionsConfirmation) {
        this.mLeSecureConnectionsConfirmation = leSecureConnectionsConfirmation;
    }

    public byte[] getLeSecureConnectionsRandom() {
        return this.mLeSecureConnectionsRandom;
    }

    public int getRole() {
        return this.mRole;
    }

    public void setRole(int role) {
        this.mRole = role;
    }

    public int getFlag() {
        return this.mFlag;
    }

    public void setFlag(int flag) {
        this.mFlag = flag;
    }

    public void setLeSecureConnectionsRandom(byte[] leSecureConnectionsRandom) {
        this.mLeSecureConnectionsRandom = leSecureConnectionsRandom;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeByteArray(this.mLeBluetoothDeviceAddress);
        out.writeByteArray(this.mSecurityManagerTk);
        out.writeByteArray(this.mLeSecureConnectionsConfirmation);
        out.writeByteArray(this.mLeSecureConnectionsRandom);
        out.writeInt(this.mRole);
        out.writeInt(this.mFlag);
    }
}
