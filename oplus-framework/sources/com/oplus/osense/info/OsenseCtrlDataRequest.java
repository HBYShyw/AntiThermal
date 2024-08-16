package com.oplus.osense.info;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class OsenseCtrlDataRequest implements Parcelable {
    public static final Parcelable.Creator<OsenseCtrlDataRequest> CREATOR = new Parcelable.Creator<OsenseCtrlDataRequest>() { // from class: com.oplus.osense.info.OsenseCtrlDataRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseCtrlDataRequest createFromParcel(Parcel in) {
            return new OsenseCtrlDataRequest(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseCtrlDataRequest[] newArray(int size) {
            return new OsenseCtrlDataRequest[size];
        }
    };
    public int[][] cpuCoreCtrlData;
    public int[] cpuCtrlType;
    public int[][] cpuFreqCtrlData;
    public int[][] cpuMigData;
    public int[][] gpuCoreCtrlData;
    public int[] gpuCtrlType;
    public int[][] gpuFreqCtrlData;
    public int cpuClusterNum = -1;
    public int gpuClusterNum = -1;

    public OsenseCtrlDataRequest() {
    }

    protected OsenseCtrlDataRequest(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cpuClusterNum);
        dest.writeInt(this.gpuClusterNum);
        for (int m = 0; m < this.cpuClusterNum; m++) {
            dest.writeInt(this.cpuCoreCtrlData[m][0]);
            dest.writeInt(this.cpuCoreCtrlData[m][1]);
            dest.writeInt(this.cpuFreqCtrlData[m][0]);
            dest.writeInt(this.cpuFreqCtrlData[m][1]);
            dest.writeInt(this.cpuCtrlType[m]);
        }
        for (int m2 = 0; m2 < this.gpuClusterNum; m2++) {
            dest.writeInt(this.gpuCoreCtrlData[m2][0]);
            dest.writeInt(this.gpuCoreCtrlData[m2][1]);
            dest.writeInt(this.gpuFreqCtrlData[m2][0]);
            dest.writeInt(this.gpuFreqCtrlData[m2][1]);
            dest.writeInt(this.gpuCtrlType[m2]);
        }
        for (int m3 = 0; m3 < this.cpuClusterNum - 1; m3++) {
            dest.writeInt(this.cpuMigData[m3][0]);
            dest.writeInt(this.cpuMigData[m3][1]);
        }
    }

    public void readFromParcel(Parcel in) {
        this.cpuClusterNum = in.readInt();
        int readInt = in.readInt();
        this.gpuClusterNum = readInt;
        int i = this.cpuClusterNum;
        if (i - 1 <= 0 || readInt <= 0) {
            return;
        }
        this.cpuCoreCtrlData = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, i, 2);
        this.cpuFreqCtrlData = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, this.cpuClusterNum, 2);
        this.cpuCtrlType = new int[this.cpuClusterNum];
        this.gpuCoreCtrlData = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, this.gpuClusterNum, 2);
        this.gpuFreqCtrlData = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, this.gpuClusterNum, 2);
        this.gpuCtrlType = new int[this.gpuClusterNum];
        this.cpuMigData = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, this.cpuClusterNum - 1, 2);
        for (int m = 0; m < this.cpuClusterNum; m++) {
            this.cpuCoreCtrlData[m][0] = in.readInt();
            this.cpuCoreCtrlData[m][1] = in.readInt();
            this.cpuFreqCtrlData[m][0] = in.readInt();
            this.cpuFreqCtrlData[m][1] = in.readInt();
            this.cpuCtrlType[m] = in.readInt();
        }
        for (int m2 = 0; m2 < this.gpuClusterNum; m2++) {
            this.gpuCoreCtrlData[m2][0] = in.readInt();
            this.gpuCoreCtrlData[m2][1] = in.readInt();
            this.gpuFreqCtrlData[m2][0] = in.readInt();
            this.gpuFreqCtrlData[m2][1] = in.readInt();
            this.gpuCtrlType[m2] = in.readInt();
        }
        for (int m3 = 0; m3 < this.cpuClusterNum - 1; m3++) {
            this.cpuMigData[m3][0] = in.readInt();
            this.cpuMigData[m3][1] = in.readInt();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
