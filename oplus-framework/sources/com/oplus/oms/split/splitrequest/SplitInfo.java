package com.oplus.oms.split.splitrequest;

import android.content.Context;
import com.oplus.oms.split.common.AbiUtil;
import com.oplus.oms.split.common.Identifiable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class SplitInfo implements Identifiable, Cloneable {
    private static final String TAG = "SplitInfo";
    private final List<ApkData> mApkDataList;
    private final String mAppVersion;
    private final boolean mBuiltIn;
    private final List<String> mDependencies;
    private final int mDexNumber;
    private final HashMap<String, String> mInfoForSplit;
    private final boolean mIsMultiDex;
    private final List<LibData> mLibDataList;
    private final int mMinSdkVersion;
    private final AtomicReference<LibData> mPrimaryLibData = new AtomicReference<>();
    private final String mSplitName;
    private int mSplitVersionCode;
    private String mSplitVersionName;
    private final List<String> mWorkProcesses;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitInfo(String splitName, String appVersion, int splitVersionCode, String splitVersionName, boolean builtIn, int minSdkVersion, int dexNumber, List<String> workProcesses, List<String> dependencies, List<ApkData> apkDataList, List<LibData> libDataList, HashMap<String, String> infoForSplit) {
        this.mSplitName = splitName;
        this.mAppVersion = appVersion;
        this.mSplitVersionCode = splitVersionCode;
        this.mSplitVersionName = splitVersionName;
        this.mBuiltIn = builtIn;
        this.mMinSdkVersion = minSdkVersion;
        this.mIsMultiDex = dexNumber > 1;
        this.mDexNumber = dexNumber;
        this.mWorkProcesses = workProcesses;
        this.mDependencies = dependencies;
        this.mLibDataList = libDataList;
        this.mApkDataList = apkDataList;
        this.mInfoForSplit = infoForSplit;
    }

    public List<ApkData> getApkDataList() {
        return this.mApkDataList;
    }

    public List<LibData> getLibDataList() {
        return this.mLibDataList;
    }

    public HashMap<String, String> getInfoForSplit() {
        return this.mInfoForSplit;
    }

    public void setSplitVersionCode(int splitVersionCode) {
        this.mSplitVersionCode = splitVersionCode;
    }

    public int getSplitVersionCode() {
        return this.mSplitVersionCode;
    }

    public void setSplitVersionName(String splitVersionName) {
        this.mSplitVersionName = splitVersionName;
    }

    public String getSplitVersionName() {
        return this.mSplitVersionName;
    }

    public String getSplitVersionNameAndVersionCode() {
        return this.mSplitVersionName + "@" + this.mSplitVersionCode;
    }

    public String getSplitName() {
        return this.mSplitName;
    }

    public boolean isBuiltIn() {
        return this.mBuiltIn;
    }

    public LibData getPrimaryLibData(Context context) throws IOException {
        if (this.mPrimaryLibData.get() != null) {
            return this.mPrimaryLibData.get();
        }
        if (this.mLibDataList == null) {
            return null;
        }
        String baseAbi = AbiUtil.getBasePrimaryAbi(context);
        List<String> splitABIs = new ArrayList<>();
        Iterator<LibData> it = this.mLibDataList.iterator();
        while (it.hasNext()) {
            splitABIs.add(it.next().mAbi);
        }
        String splitPrimaryAbi = AbiUtil.findSplitPrimaryAbi(baseAbi, splitABIs);
        if (splitPrimaryAbi == null) {
            throw new IOException("No supported abi for split " + this.mSplitName + ",baseAbi:" + baseAbi);
        }
        Iterator<LibData> it2 = this.mLibDataList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            LibData libData = it2.next();
            if (libData.mAbi.equals(splitPrimaryAbi)) {
                this.mPrimaryLibData.compareAndSet(null, libData);
                break;
            }
        }
        return this.mPrimaryLibData.get();
    }

    public List<String> getDependencies() {
        return this.mDependencies;
    }

    public boolean isMultiDex() {
        return this.mIsMultiDex;
    }

    public boolean hasDex() {
        return this.mDexNumber > 0;
    }

    public List<String> getWorkProcesses() {
        return this.mWorkProcesses;
    }

    public int getMinSdkVersion() {
        return this.mMinSdkVersion;
    }

    public String getAppVersion() {
        return this.mAppVersion;
    }

    public long getApkTotalSize() {
        List<ApkData> apkDataList = getApkDataList();
        long totalSize = 0;
        if (apkDataList != null && !apkDataList.isEmpty()) {
            for (ApkData apkData : apkDataList) {
                totalSize += apkData.mSize;
            }
        }
        return totalSize;
    }

    public void setApkDownloadInfo(String url, String md5, long size) {
        List<ApkData> list = this.mApkDataList;
        if (list != null && !list.isEmpty()) {
            this.mApkDataList.get(0).setUrl(url);
            this.mApkDataList.get(0).setMd5(md5);
            this.mApkDataList.get(0).setSize(size);
        }
    }

    public long getMasterApkSize() {
        List<ApkData> list = this.mApkDataList;
        if (list != null && !list.isEmpty()) {
            return this.mApkDataList.get(0).mSize;
        }
        return 0L;
    }

    public String getMasterApkUrl() {
        List<ApkData> list = this.mApkDataList;
        if (list != null && !list.isEmpty()) {
            return this.mApkDataList.get(0).getUrl();
        }
        return null;
    }

    public String getMasterApkMd5() {
        List<ApkData> list = this.mApkDataList;
        if (list != null && !list.isEmpty()) {
            return this.mApkDataList.get(0).getMd5();
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class LibData {
        private final String mAbi;
        private final List<Lib> mLibs;

        /* JADX INFO: Access modifiers changed from: package-private */
        public LibData(String abi, List<Lib> libs) {
            this.mAbi = abi;
            this.mLibs = libs;
        }

        public String getAbi() {
            return this.mAbi;
        }

        public List<Lib> getLibs() {
            return this.mLibs;
        }

        /* loaded from: classes.dex */
        public static class Lib {
            private final String mMd5;
            private final String mName;
            private final long mSize;

            /* JADX INFO: Access modifiers changed from: package-private */
            public Lib(String name, String md5, long size) {
                this.mName = name;
                this.mMd5 = md5;
                this.mSize = size;
            }

            public String getName() {
                return this.mName;
            }

            public String getMd5() {
                return this.mMd5;
            }

            public long getSize() {
                return this.mSize;
            }
        }
    }

    @Override // com.oplus.oms.split.common.Identifiable
    public String getIdentify() {
        return this.mSplitName + "@" + this.mSplitVersionName + "@" + this.mSplitVersionCode;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return "SplitInfo{splitName='" + this.mSplitName + "', splitVersionCode='" + this.mSplitVersionCode + "', splitVersionName='" + this.mSplitVersionName + "', extra=" + this.mInfoForSplit + ", workProcesses=" + this.mWorkProcesses + '}';
    }

    /* loaded from: classes.dex */
    public static class ApkData {
        private String mAbi;
        private String mMd5;
        private long mSize;
        private String mUrl;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ApkData(String abi, String url, String md5, long size) {
            this.mAbi = abi;
            this.mUrl = url;
            this.mMd5 = md5;
            this.mSize = size;
        }

        public String getAbi() {
            return this.mAbi;
        }

        public void setAbi(String str) {
            this.mAbi = str;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public void setUrl(String str) {
            this.mUrl = str;
        }

        public String getMd5() {
            return this.mMd5;
        }

        public void setMd5(String str) {
            this.mMd5 = str;
        }

        public long getSize() {
            return this.mSize;
        }

        public void setSize(long apkSize) {
            this.mSize = apkSize;
        }
    }
}
