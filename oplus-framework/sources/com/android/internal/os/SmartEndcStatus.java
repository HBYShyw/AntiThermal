package com.android.internal.os;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.multiuser.IOplusMultiUserStatisticsManager;

/* loaded from: classes.dex */
public class SmartEndcStatus implements Parcelable {
    public static final Parcelable.Creator<SmartEndcStatus> CREATOR = new Parcelable.Creator<SmartEndcStatus>() { // from class: com.android.internal.os.SmartEndcStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SmartEndcStatus createFromParcel(Parcel in) {
            boolean switchOn = in.readBoolean();
            long endcDurTime = in.readLong();
            long noEndcDurTime = in.readLong();
            long enableEndcSettingTime = in.readLong();
            long disableEndcSettingTime = in.readLong();
            int lteSpeed0 = in.readInt();
            int lteSpeed1 = in.readInt();
            int lteSpeed2 = in.readInt();
            int lteSpeed3 = in.readInt();
            int lteSpeed4 = in.readInt();
            int enEndcSpeedHighCnt = in.readInt();
            int enEndcSwitchOffCnt = in.readInt();
            int enEndcLtePoorCnt = in.readInt();
            int enEndcLteJamCnt = in.readInt();
            int enEndcProhibitCnt = in.readInt();
            return new SmartEndcStatus(switchOn, endcDurTime, noEndcDurTime, enableEndcSettingTime, disableEndcSettingTime, lteSpeed0, lteSpeed1, lteSpeed2, lteSpeed3, lteSpeed4, enEndcSpeedHighCnt, enEndcSwitchOffCnt, enEndcLtePoorCnt, enEndcLteJamCnt, enEndcProhibitCnt);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SmartEndcStatus[] newArray(int size) {
            return new SmartEndcStatus[size];
        }
    };
    private long mDisableEndcSettingTime;
    private int mEnEndcLteJamCnt;
    private int mEnEndcLtePoorCnt;
    private int mEnEndcProhibitCnt;
    private int mEnEndcSpeedHighCnt;
    private int mEnEndcSwitchOffCnt;
    private long mEnableEndcSettingTime;
    private long mEndcBearDuration;
    private int mLteSpeedCntL0;
    private int mLteSpeedCntL1;
    private int mLteSpeedCntL2;
    private int mLteSpeedCntL3;
    private int mLteSpeedCntL4;
    private long mNoEndcBearDuration;
    private boolean mSwitchOn;

    public boolean isSwitchOn() {
        return this.mSwitchOn;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public SmartEndcStatus(boolean switchOn, long endcDurTime, long noEndcDurTime, long enableEndcSettingTime, long disableEndcSettingTime, int lteSpeed0, int lteSpeed1, int lteSpeed2, int lteSpeed3, int lteSpeed4, int enEndcSpeedHighCnt, int enEndcSwitchOffCnt, int enEndcLtePoorCnt, int enEndcLteJamCnt, int enEndcProhibitCnt) {
        this.mSwitchOn = switchOn;
        this.mEndcBearDuration = endcDurTime;
        this.mNoEndcBearDuration = noEndcDurTime;
        this.mEnableEndcSettingTime = enableEndcSettingTime;
        this.mDisableEndcSettingTime = disableEndcSettingTime;
        this.mLteSpeedCntL0 = lteSpeed0;
        this.mLteSpeedCntL1 = lteSpeed1;
        this.mLteSpeedCntL2 = lteSpeed2;
        this.mLteSpeedCntL3 = lteSpeed3;
        this.mLteSpeedCntL4 = lteSpeed4;
        this.mEnEndcSpeedHighCnt = enEndcSpeedHighCnt;
        this.mEnEndcSwitchOffCnt = enEndcSwitchOffCnt;
        this.mEnEndcLtePoorCnt = enEndcLtePoorCnt;
        this.mEnEndcLteJamCnt = enEndcLteJamCnt;
        this.mEnEndcProhibitCnt = enEndcProhibitCnt;
    }

    public SmartEndcStatus() {
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mSwitchOn);
        dest.writeLong(this.mEndcBearDuration);
        dest.writeLong(this.mNoEndcBearDuration);
        dest.writeLong(this.mEnableEndcSettingTime);
        dest.writeLong(this.mDisableEndcSettingTime);
        dest.writeInt(this.mLteSpeedCntL0);
        dest.writeInt(this.mLteSpeedCntL1);
        dest.writeInt(this.mLteSpeedCntL2);
        dest.writeInt(this.mLteSpeedCntL3);
        dest.writeInt(this.mLteSpeedCntL4);
        dest.writeInt(this.mEnEndcSpeedHighCnt);
        dest.writeInt(this.mEnEndcSwitchOffCnt);
        dest.writeInt(this.mEnEndcLtePoorCnt);
        dest.writeInt(this.mEnEndcLteJamCnt);
        dest.writeInt(this.mEnEndcProhibitCnt);
    }

    public String toString() {
        return "{ sw:" + this.mSwitchOn + ", eddut:" + this.mEndcBearDuration + ", noeddut:" + this.mNoEndcBearDuration + ", et:" + this.mEnableEndcSettingTime + ", det:" + this.mDisableEndcSettingTime + ", ls0:" + this.mLteSpeedCntL0 + ", ls1:" + this.mLteSpeedCntL1 + ", ls2:" + this.mLteSpeedCntL2 + ", ls3:" + this.mLteSpeedCntL3 + ", ls4:" + this.mLteSpeedCntL4 + ", edhct:" + this.mEnEndcSpeedHighCnt + ", edoffct:" + this.mEnEndcSwitchOffCnt + ", edlpct:" + this.mEnEndcLtePoorCnt + ", edljct:" + this.mEnEndcLteJamCnt + ", edphct:" + this.mEnEndcProhibitCnt + "}";
    }

    public String toStringLite() {
        return "{ sw:" + this.mSwitchOn + ", eddut:" + this.mEndcBearDuration + ", noeddut:" + this.mNoEndcBearDuration + ", et:" + this.mEnableEndcSettingTime + ", det:" + this.mDisableEndcSettingTime + ", ls:{" + this.mLteSpeedCntL0 + "," + this.mLteSpeedCntL1 + "," + this.mLteSpeedCntL2 + "," + this.mLteSpeedCntL3 + "," + this.mLteSpeedCntL4 + "}, edhct:" + this.mEnEndcSpeedHighCnt + ", edoffct:" + this.mEnEndcSwitchOffCnt + ", edlpct:" + this.mEnEndcLtePoorCnt + ", edljct:" + this.mEnEndcLteJamCnt + ", edphct:" + this.mEnEndcProhibitCnt + "}";
    }

    public void setSwitchOn(boolean mSwitchOn) {
        this.mSwitchOn = mSwitchOn;
    }

    public long getEndcBearDuration() {
        return this.mEndcBearDuration;
    }

    public void setEndcBearDuration(long mEndcBearDuration) {
        this.mEndcBearDuration = mEndcBearDuration;
    }

    public long getNoEndcBearDuration() {
        return this.mNoEndcBearDuration;
    }

    public void setNoEndcBearDuration(long mNoEndcBearDuration) {
        this.mNoEndcBearDuration = mNoEndcBearDuration;
    }

    public long getEnableEndcSettingTime() {
        return this.mEnableEndcSettingTime;
    }

    public void setEnableEndcSettingTime(long mEnableEndcSettingTime) {
        this.mEnableEndcSettingTime = mEnableEndcSettingTime;
    }

    public long getDisableEndcSettingTime() {
        return this.mDisableEndcSettingTime;
    }

    public void setDisableEndcSettingTime(long mDisableEndcSettingTime) {
        this.mDisableEndcSettingTime = mDisableEndcSettingTime;
    }

    public int getLteSpeedCntL0() {
        return this.mLteSpeedCntL0;
    }

    public void setLteSpeedCntL0(int mLteSpeedCntL0) {
        this.mLteSpeedCntL0 = mLteSpeedCntL0;
    }

    public int getLteSpeedCntL1() {
        return this.mLteSpeedCntL1;
    }

    public void setLteSpeedCntL1(int mLteSpeedCntL1) {
        this.mLteSpeedCntL1 = mLteSpeedCntL1;
    }

    public int getLteSpeedCntL2() {
        return this.mLteSpeedCntL2;
    }

    public void setLteSpeedCntL2(int mLteSpeedCntL2) {
        this.mLteSpeedCntL2 = mLteSpeedCntL2;
    }

    public int getLteSpeedCntL3() {
        return this.mLteSpeedCntL3;
    }

    public void setLteSpeedCntL3(int mLteSpeedCntL3) {
        this.mLteSpeedCntL3 = mLteSpeedCntL3;
    }

    public int getLteSpeedCntL4() {
        return this.mLteSpeedCntL4;
    }

    public void setLteSpeedCntL4(int mLteSpeedCntL4) {
        this.mLteSpeedCntL4 = mLteSpeedCntL4;
    }

    public int getEnEndcSpeedHighCnt() {
        return this.mEnEndcSpeedHighCnt;
    }

    public void setEnEndcSpeedHighCnt(int mEnEndcSpeedHighCnt) {
        this.mEnEndcSpeedHighCnt = mEnEndcSpeedHighCnt;
    }

    public int getEnEndcSwitchOffCnt() {
        return this.mEnEndcSwitchOffCnt;
    }

    public void setEnEndcSwitchOffCnt(int mEnEndcSwitchOffCnt) {
        this.mEnEndcSwitchOffCnt = mEnEndcSwitchOffCnt;
    }

    public int getEnEndcLtePoorCnt() {
        return this.mEnEndcLtePoorCnt;
    }

    public void setEnEndcLtePoorCnt(int mEnEndcLtePoorCnt) {
        this.mEnEndcLtePoorCnt = mEnEndcLtePoorCnt;
    }

    public int getEnEndcLteJamCnt() {
        return this.mEnEndcLteJamCnt;
    }

    public void setEnEndcLteJamCnt(int mEnEndcLteJamCnt) {
        this.mEnEndcLteJamCnt = mEnEndcLteJamCnt;
    }

    public int getEnEndcProhibitCnt() {
        return this.mEnEndcProhibitCnt;
    }

    public void setEnEndcProhibitCnt(int mEnEndcProhibitCnt) {
        this.mEnEndcProhibitCnt = mEnEndcProhibitCnt;
    }

    public static SmartEndcStatus creatEndcStatusFormIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        SmartEndcStatus status = new SmartEndcStatus(intent.getBooleanExtra(IOplusMultiUserStatisticsManager.SWITCH, false), intent.getLongExtra("EndcDura", 0L), intent.getLongExtra("NoEndcDura", 0L), intent.getLongExtra("EnEndcTime", 0L), intent.getLongExtra("DisEndcTime", 0L), intent.getIntExtra("LteSpeedCntL0", 0), intent.getIntExtra("LteSpeedCntL1", 0), intent.getIntExtra("LteSpeedCntL2", 0), intent.getIntExtra("LteSpeedCntL3", 0), intent.getIntExtra("LteSpeedCntL4", 0), intent.getIntExtra("EnEndcSpeedHighCnt", 0), intent.getIntExtra("EnEndcSwitchOffCnt", 0), intent.getIntExtra("EnEndcLtePoorCnt", 0), intent.getIntExtra("EnEndcLteJamCnt", 0), intent.getIntExtra("EnEndcProhibitCnt", 0));
        return status;
    }
}
