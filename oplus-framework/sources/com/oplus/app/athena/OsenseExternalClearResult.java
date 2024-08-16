package com.oplus.app.athena;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OsenseExternalClearResult implements Parcelable {
    public static final Parcelable.Creator<OsenseExternalClearResult> CREATOR = new Parcelable.Creator<OsenseExternalClearResult>() { // from class: com.oplus.app.athena.OsenseExternalClearResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseExternalClearResult createFromParcel(Parcel in) {
            return new OsenseExternalClearResult(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseExternalClearResult[] newArray(int size) {
            return new OsenseExternalClearResult[size];
        }
    };
    public static final int FAIL_CLEAR_FLAG = 1;
    public static final int FORCE_STOP_STRATEGY = 2;
    public static final int KILL_STRATEGY = 1;
    public static final int NOT_SUPPORT_CLEAR_FLAG = 3;
    public static final int PARAM_INVALID_FLAG = 4;
    public static final int PART_SUCCESS_CLEAR_FLAG = 2;
    public static final int PROCESS_NOT_EXIST_FLAG = 5;
    public static final int SUCCESS_CLEAR_FLAG = 0;
    public List<ClearInfoItemResult> mClearInfoItemResult;
    public int mFlag;

    public OsenseExternalClearResult() {
        this.mClearInfoItemResult = new ArrayList();
    }

    protected OsenseExternalClearResult(Parcel in) {
        this.mClearInfoItemResult = new ArrayList();
        this.mFlag = in.readInt();
        this.mClearInfoItemResult = in.createTypedArrayList(ClearInfoItemResult.CREATOR);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mFlag);
        dest.writeTypedList(this.mClearInfoItemResult);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "OsenseExternalClearResult{mFlag=" + this.mFlag + ", mClearInfoItemResult=" + this.mClearInfoItemResult + '}';
    }

    /* loaded from: classes.dex */
    public static class ClearInfoItemResult implements Parcelable {
        public static final Parcelable.Creator<ClearInfoItemResult> CREATOR = new Parcelable.Creator<ClearInfoItemResult>() { // from class: com.oplus.app.athena.OsenseExternalClearResult.ClearInfoItemResult.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ClearInfoItemResult createFromParcel(Parcel in) {
                return new ClearInfoItemResult(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ClearInfoItemResult[] newArray(int size) {
                return new ClearInfoItemResult[size];
            }
        };
        public int mFlag;
        public int mKeepType;
        public List<String> mProcessList;
        public String mSource;
        public int mStrategy;

        public ClearInfoItemResult() {
            this.mSource = null;
            this.mFlag = 0;
            this.mKeepType = -1;
            this.mStrategy = -1;
            this.mProcessList = new ArrayList();
        }

        protected ClearInfoItemResult(Parcel in) {
            this.mSource = null;
            this.mFlag = 0;
            this.mKeepType = -1;
            this.mStrategy = -1;
            this.mProcessList = new ArrayList();
            this.mSource = in.readString16NoHelper();
            this.mFlag = in.readInt();
            this.mKeepType = in.readInt();
            this.mStrategy = in.readInt();
            this.mProcessList = in.createStringArrayList();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString16NoHelper(this.mSource);
            dest.writeInt(this.mFlag);
            dest.writeInt(this.mKeepType);
            dest.writeInt(this.mStrategy);
            dest.writeStringList(this.mProcessList);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public void addProcessInfo(int pid, String procName) {
            this.mProcessList.add(pid + "|" + procName);
        }

        public String toString() {
            return "ClearInfoItemResult{mSource='" + this.mSource + "', mFlag=" + this.mFlag + ", mKeepType=" + this.mKeepType + ", mStrategy=" + this.mStrategy + ", mProcessList=" + this.mProcessList + '}';
        }
    }
}
