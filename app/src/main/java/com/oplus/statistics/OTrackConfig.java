package com.oplus.statistics;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class OTrackConfig {
    public static final OTrackConfig DUMMY = new OTrackConfig();
    public static final int ENV_DEBUG = 1;
    public static final int ENV_RELEASE = 0;
    public static final int HEADER_FLAG_GUID = 4;
    public static final int HEADER_FLAG_IMEI = 1;
    public static final int HEADER_FLAG_PCBA = 2;
    private String mAppName;
    private int mEnv;
    private int mHeaderFlag;
    private String mPackageName;
    private String mVersionName;

    /* loaded from: classes2.dex */
    public static class Builder {
        private String appName;
        private int env = 0;
        private int headerFlag;
        private String packageName;
        private String versionName;

        public OTrackConfig build() {
            return new OTrackConfig(this);
        }

        public Builder enableGuidHeader() {
            this.headerFlag |= 4;
            return this;
        }

        public Builder enableImeiHeader() {
            this.headerFlag |= 1;
            return this;
        }

        public Builder enablePcbaHeader() {
            this.headerFlag |= 2;
            return this;
        }

        public Builder setAppName(String str) {
            this.appName = str;
            return this;
        }

        public Builder setEnv(int i10) {
            this.env = i10;
            return this;
        }

        public Builder setPackageName(String str) {
            this.packageName = str;
            return this;
        }

        public Builder setVersionName(String str) {
            this.versionName = str;
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface EnvType {
    }

    public String getAppName() {
        return this.mAppName;
    }

    public int getEnv() {
        return this.mEnv;
    }

    public int getHeaderFlag() {
        return this.mHeaderFlag;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public void setAppName(String str) {
        this.mAppName = str;
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
    }

    public void setVersionName(String str) {
        this.mVersionName = str;
    }

    private OTrackConfig() {
        this.mPackageName = "";
        this.mVersionName = "";
        this.mAppName = "";
    }

    private OTrackConfig(Builder builder) {
        this.mPackageName = "";
        this.mVersionName = "";
        this.mAppName = "";
        this.mEnv = builder.env;
        this.mPackageName = builder.packageName;
        this.mVersionName = builder.versionName;
        this.mAppName = builder.appName;
        this.mHeaderFlag = builder.headerFlag;
    }
}
