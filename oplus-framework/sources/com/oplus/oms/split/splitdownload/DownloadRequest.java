package com.oplus.oms.split.splitdownload;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.oms.split.common.SplitLog;
import java.util.Map;

/* loaded from: classes.dex */
public final class DownloadRequest implements Parcelable {
    public static final Parcelable.Creator<DownloadRequest> CREATOR = new Parcelable.Creator<DownloadRequest>() { // from class: com.oplus.oms.split.splitdownload.DownloadRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DownloadRequest createFromParcel(Parcel in) {
            return new DownloadRequest(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DownloadRequest[] newArray(int size) {
            return new DownloadRequest[size];
        }
    };
    private static final String TAG = "DownloadRequest";
    private final String mCurrentVersion;
    private final Map<String, String> mExtra;
    private final String mMd5;
    private final String mModuleName;
    private final String mSaveFileName;
    private final String mSavePath;
    private final long mSize;
    private final String mUrl;

    private DownloadRequest(Parcel in) {
        this.mModuleName = in.readString();
        this.mCurrentVersion = in.readString();
        this.mUrl = in.readString();
        this.mMd5 = in.readString();
        this.mSize = in.readLong();
        this.mExtra = in.readHashMap(getClass().getClassLoader());
        this.mSavePath = in.readString();
        this.mSaveFileName = in.readString();
    }

    private DownloadRequest(Builder builder) {
        this.mModuleName = builder.mModuleName;
        this.mUrl = builder.mUrl;
        this.mMd5 = builder.mMd5;
        this.mSize = builder.mSize;
        this.mExtra = builder.mExtra;
        this.mCurrentVersion = builder.mCurrentVersion;
        this.mSavePath = builder.mSavePath;
        this.mSaveFileName = builder.mSaveFileName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (dest == null) {
            SplitLog.w(TAG, "Parcel is null", new Object[0]);
            return;
        }
        dest.writeString(this.mModuleName);
        dest.writeString(this.mCurrentVersion);
        dest.writeString(this.mUrl);
        dest.writeString(this.mMd5);
        dest.writeLong(this.mSize);
        dest.writeMap(this.mExtra);
        dest.writeString(this.mSavePath);
        dest.writeString(this.mSaveFileName);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getMd5() {
        return this.mMd5;
    }

    public long getSize() {
        return this.mSize;
    }

    public Map<String, String> getExtra() {
        return this.mExtra;
    }

    public String getExtra(String key) {
        return this.mExtra.get(key);
    }

    public String getCurrentVersion() {
        return this.mCurrentVersion;
    }

    public String getModuleName() {
        return this.mModuleName;
    }

    public String getSavePath() {
        return this.mSavePath;
    }

    public String getSaveFileName() {
        return this.mSaveFileName;
    }

    public String toString() {
        return "DownloadRequest{mModuleName='" + this.mModuleName + "', currentVersion='" + this.mCurrentVersion + "', url='" + this.mUrl + "', mMd5='" + this.mMd5 + "', size=" + this.mSize + ", extra=" + this.mExtra + '}';
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private String mCurrentVersion;
        private Map<String, String> mExtra;
        private String mMd5;
        private String mModuleName;
        private String mSaveFileName;
        private String mSavePath;
        private long mSize;
        private String mUrl;

        public Builder moduleName(String moduleName) {
            this.mModuleName = moduleName;
            return this;
        }

        public Builder url(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder md5(String md5) {
            this.mMd5 = md5;
            return this;
        }

        public Builder size(long size) {
            this.mSize = size;
            return this;
        }

        public Builder currentVersion(String currentVersion) {
            this.mCurrentVersion = currentVersion;
            return this;
        }

        public Builder extra(Map<String, String> extra) {
            this.mExtra = extra;
            return this;
        }

        public Builder savePath(String path) {
            this.mSavePath = path;
            return this;
        }

        public Builder saveFileName(String fileName) {
            this.mSaveFileName = fileName;
            return this;
        }

        public DownloadRequest build() {
            return new DownloadRequest(this);
        }
    }
}
