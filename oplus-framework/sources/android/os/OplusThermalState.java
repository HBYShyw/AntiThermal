package android.os;

import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusThermalState implements Parcelable {
    public static final Parcelable.Creator<OplusThermalState> CREATOR = new Parcelable.Creator<OplusThermalState>() { // from class: android.os.OplusThermalState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusThermalState createFromParcel(Parcel in) {
            int pluginType = in.readInt();
            int fcc = in.readInt();
            int batteryRm = in.readInt();
            int thermalHeat = in.readInt();
            int thermalHeat1 = in.readInt();
            int thermalHeat2 = in.readInt();
            int thermalHeat3 = in.readInt();
            int fast2Normal = in.readInt();
            int chargeId = in.readInt();
            boolean isFastCharge = in.readInt() == 1;
            int batteryCurrent = in.readInt();
            int batteryLevel = in.readInt();
            int batteryTemperature = in.readInt();
            return new OplusThermalState(pluginType, fcc, batteryRm, thermalHeat, thermalHeat1, thermalHeat2, thermalHeat3, fast2Normal, chargeId, isFastCharge, batteryCurrent, batteryLevel, batteryTemperature);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusThermalState[] newArray(int size) {
            return new OplusThermalState[size];
        }
    };
    int mBatteryCurrent;
    int mBatteryLevel;
    int mBatteryRm;
    int mBatteryTemperature;
    int mChargeId;
    int mFast2Normal;
    int mFcc;
    boolean mIsFastCharge;
    int mPlugType;
    int mThermalHeat;
    int mThermalHeat1;
    int mThermalHeat2;
    int mThermalHeat3;

    public OplusThermalState(int plugType, int fcc, int batteryRm, int thermalHeat, int thermalHeat1, int thermalHeat2, int thermalHeat3, int fast2Normal, int chargeId, boolean isFastCharge, int batteryCurrent, int batteryLevel, int batteryTemperature) {
        this.mPlugType = plugType;
        this.mFcc = fcc;
        this.mBatteryRm = batteryRm;
        this.mThermalHeat = thermalHeat;
        this.mThermalHeat1 = thermalHeat1;
        this.mThermalHeat2 = thermalHeat2;
        this.mThermalHeat3 = thermalHeat3;
        this.mFast2Normal = fast2Normal;
        this.mChargeId = chargeId;
        this.mIsFastCharge = isFastCharge;
        this.mBatteryCurrent = batteryCurrent;
        this.mBatteryLevel = batteryLevel;
        this.mBatteryTemperature = batteryTemperature;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusThermalState{");
        sb.append("pluginType:").append(this.mPlugType);
        sb.append(", fcc:").append(this.mFcc);
        sb.append(", mBatteryRm:").append(this.mBatteryRm);
        sb.append(", mThermalHeat:").append(this.mThermalHeat);
        sb.append(", mThermalHeat1:").append(this.mThermalHeat1);
        sb.append(", mThermalHeat2:").append(this.mThermalHeat2);
        sb.append(", mThermalHeat3:").append(this.mThermalHeat3);
        sb.append(", mFast2Normal:").append(this.mFast2Normal);
        sb.append(", mChargeId:").append(this.mChargeId);
        sb.append(", mIsFastCharge:").append(this.mIsFastCharge);
        sb.append(", mBatteryCurrent:").append(this.mBatteryCurrent);
        sb.append(", mBatteryLevel:").append(this.mBatteryLevel);
        sb.append(", mBatteryTemperature:").append(this.mBatteryTemperature);
        sb.append("}");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlugType);
        parcel.writeInt(this.mFcc);
        parcel.writeInt(this.mBatteryRm);
        parcel.writeInt(this.mThermalHeat);
        parcel.writeInt(this.mThermalHeat1);
        parcel.writeInt(this.mThermalHeat2);
        parcel.writeInt(this.mThermalHeat3);
        parcel.writeInt(this.mFast2Normal);
        parcel.writeInt(this.mChargeId);
        parcel.writeInt(this.mIsFastCharge ? 1 : 0);
        parcel.writeInt(this.mBatteryCurrent);
        parcel.writeInt(this.mBatteryLevel);
        parcel.writeInt(this.mBatteryTemperature);
    }

    public int getPlugType() {
        return this.mPlugType;
    }

    public int getFcc() {
        return this.mFcc;
    }

    public int getBatteryRm() {
        return this.mBatteryRm;
    }

    public int getThermalHeat(int index) {
        switch (index) {
            case 0:
                return this.mThermalHeat;
            case 1:
                return this.mThermalHeat1;
            case 2:
                return this.mThermalHeat2;
            case 3:
                return this.mThermalHeat3;
            default:
                return -1;
        }
    }

    public int getFast2Normal() {
        return this.mFast2Normal;
    }

    public int getChargeId() {
        return this.mChargeId;
    }

    public boolean getIsFastCharge() {
        return this.mIsFastCharge;
    }

    public int getBatteryCurrent() {
        return this.mBatteryCurrent;
    }

    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }

    public int getBatteryTemperature() {
        return this.mBatteryTemperature;
    }
}
