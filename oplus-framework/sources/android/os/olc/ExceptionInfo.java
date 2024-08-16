package android.os.olc;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class ExceptionInfo implements Parcelable {
    public static final Parcelable.Creator<ExceptionInfo> CREATOR = new Parcelable.Creator<ExceptionInfo>() { // from class: android.os.olc.ExceptionInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExceptionInfo createFromParcel(Parcel in) {
            return new ExceptionInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExceptionInfo[] newArray(int size) {
            return new ExceptionInfo[size];
        }
    };
    private long mAtomicLogs;
    private int mExceptionId;
    private int mExceptionLevel;
    private int mExceptionType;
    private String mLogParmas;
    private long mTime;

    public ExceptionInfo() {
    }

    public ExceptionInfo(Parcel in) {
        this.mTime = in.readLong();
        this.mExceptionId = in.readInt();
        this.mExceptionType = in.readInt();
        this.mExceptionLevel = in.readInt();
        this.mAtomicLogs = in.readLong();
        this.mLogParmas = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mTime);
        parcel.writeInt(this.mExceptionId);
        parcel.writeInt(this.mExceptionType);
        parcel.writeInt(this.mExceptionLevel);
        parcel.writeLong(this.mAtomicLogs);
        parcel.writeString(this.mLogParmas);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return this.mExceptionId;
    }

    public void setId(int id) {
        this.mExceptionId = id;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public int getExceptionType() {
        return this.mExceptionType;
    }

    public void setExceptionType(int exceptionType) {
        this.mExceptionType = exceptionType;
    }

    public int getExceptionLevel() {
        return this.mExceptionLevel;
    }

    public void setExceptionLevel(int exceptionLevel) {
        this.mExceptionLevel = exceptionLevel;
    }

    public long getAtomicLogs() {
        return this.mAtomicLogs;
    }

    public void setAtomicLogs(long atomicLogs) {
        this.mAtomicLogs = atomicLogs;
    }

    public String getLogParmas() {
        return this.mLogParmas;
    }

    public void setLogParmas(String logParmas) {
        this.mLogParmas = logParmas;
    }
}
