package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class ScanThrottleInfo implements Parcelable {
    public static final Parcelable.Creator<ScanThrottleInfo> CREATOR = new Parcelable.Creator<ScanThrottleInfo>() { // from class: android.net.wifi.ScanThrottleInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScanThrottleInfo createFromParcel(Parcel in) {
            ScanThrottleInfo ste = new ScanThrottleInfo();
            ste.mPackageName = in.readString();
            ste.mScanCount = in.readInt();
            ste.mTimeInterval = in.readInt();
            return ste;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScanThrottleInfo[] newArray(int size) {
            return new ScanThrottleInfo[size];
        }
    };
    private static final String TAG = "ScanThrottleInfo";
    private String mPackageName;
    private int mScanCount;
    private long mTimeInterval;

    public ScanThrottleInfo() {
        this.mPackageName = "";
        this.mScanCount = 0;
        this.mTimeInterval = 0L;
    }

    public ScanThrottleInfo(String packageName, int scanCount, long timeInterval) {
        this.mPackageName = packageName;
        this.mScanCount = scanCount;
        this.mTimeInterval = timeInterval;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getScanCount() {
        return this.mScanCount;
    }

    public long getTimeInterval() {
        return this.mTimeInterval;
    }

    public String toString() {
        String str = this.mPackageName;
        String packageName = str != null ? String.valueOf(str.hashCode()) : " null ";
        return " ScanThrottleInfo :" + packageName + "/" + this.mScanCount + " per " + this.mTimeInterval;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mScanCount);
        dest.writeLong(this.mTimeInterval);
    }
}
