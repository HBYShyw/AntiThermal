package com.oplus.navi.oms;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OmsPluginInfo implements Parcelable {
    public static final Parcelable.Creator<OmsPluginInfo> CREATOR = new Parcelable.Creator<OmsPluginInfo>() { // from class: com.oplus.navi.oms.OmsPluginInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OmsPluginInfo createFromParcel(Parcel in) {
            return new OmsPluginInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OmsPluginInfo[] newArray(int size) {
            return new OmsPluginInfo[size];
        }
    };
    private String mActionName;
    private String mApkName;
    private String mApkPath;
    private ApplicationInfo mApplicaitonInfo;
    private long mVersionCode;
    private String mVersionName;

    protected OmsPluginInfo(Parcel in) {
        if (in != null) {
            this.mActionName = in.readString();
            this.mApkName = in.readString();
            this.mApkPath = in.readString();
            this.mVersionName = in.readString();
            this.mVersionCode = in.readLong();
            this.mApplicaitonInfo = (ApplicationInfo) in.readParcelable(ApplicationInfo.class.getClassLoader());
        }
    }

    public OmsPluginInfo(String action, String apkName, String apkPath, String versionName, long versionCode, ApplicationInfo applicationInfo) {
        this.mActionName = action;
        this.mApkName = apkName;
        this.mApkPath = apkPath;
        this.mVersionName = versionName;
        this.mVersionCode = versionCode;
        this.mApplicaitonInfo = applicationInfo;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeString(this.mActionName);
            parcel.writeString(this.mApkName);
            parcel.writeString(this.mApkPath);
            parcel.writeString(this.mVersionName);
            parcel.writeLong(this.mVersionCode);
            parcel.writeParcelable(this.mApplicaitonInfo, i);
        }
    }

    public String getActionName() {
        return this.mActionName;
    }

    public String getApkName() {
        return this.mApkName;
    }

    public String getApkPath() {
        return this.mApkPath;
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public long getVersionCode() {
        return this.mVersionCode;
    }

    public ApplicationInfo getApplicationInfo() {
        return this.mApplicaitonInfo;
    }
}
