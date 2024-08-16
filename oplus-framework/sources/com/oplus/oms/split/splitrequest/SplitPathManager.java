package com.oplus.oms.split.splitrequest;

import android.content.Context;
import com.oplus.oms.split.common.SplitBaseInfoProvider;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class SplitPathManager {
    public static final String DEFAULT = "default";
    public static final String DOWNLOAD = "download";
    private static final String LIB_DIR = "nativeLib";
    private static final String TAG = "SplitPathManager";
    public static final String UPDATE = "update";
    private static final AtomicReference<SplitPathManager> sSplitPathManagerRef = new AtomicReference<>();
    private final File mRootDir;

    private SplitPathManager(File rootDir, String omsId) {
        this.mRootDir = new File(rootDir, omsId);
    }

    public static void install(Context context) {
        sSplitPathManagerRef.compareAndSet(null, create(context));
    }

    private static SplitPathManager create(Context context) {
        File baseRootDir = context.getDir(SplitConstants.OMS, 0);
        String omsId = SplitBaseInfoProvider.getOmsId();
        return new SplitPathManager(baseRootDir, omsId);
    }

    public static SplitPathManager require() {
        AtomicReference<SplitPathManager> atomicReference = sSplitPathManagerRef;
        if (atomicReference.get() == null) {
            throw new OMSRunTimeException("SplitPathManager must be initialized firstly!");
        }
        return atomicReference.get();
    }

    public File getSplitDirDownloadDir(String splitName, String version, boolean isWriteMode) {
        File splitDownloaderDir = new File(getSplitRootDirDownload(splitName, isWriteMode), version);
        if (isWriteMode && !splitDownloaderDir.exists() && !splitDownloaderDir.mkdirs()) {
            SplitLog.w(TAG, "getSplitDirDownload error", new Object[0]);
        }
        return splitDownloaderDir;
    }

    public File getDownloadApk(String splitName, String version, boolean isWriteMode) {
        return new File(getSplitDirDownloadDir(splitName, version, isWriteMode), getSplitApkFileName(splitName));
    }

    public File getSplitRootDirDownload(String splitName, boolean isWriteMode) {
        File splitRootDir = new File(getSplitDir(splitName), "download");
        if (isWriteMode && !splitRootDir.exists() && !splitRootDir.mkdirs()) {
            SplitLog.w(TAG, "getSplitRootDirDownload error", new Object[0]);
        }
        return splitRootDir;
    }

    public File getSplitDir(String splitName) {
        return getSplitDir(splitName, false);
    }

    public File getSplitDir(String splitName, boolean isWriteMode) {
        File splitDir = new File(this.mRootDir, splitName);
        if (isWriteMode && !splitDir.exists() && !splitDir.mkdirs()) {
            SplitLog.w(TAG, "getSplitDir error", new Object[0]);
        }
        return splitDir;
    }

    public File getSplitApkDir(String splitName, int version, boolean isWriteMode) {
        File splitApkDir = new File(getSplitDir(splitName, isWriteMode), String.valueOf(version));
        if (isWriteMode && !splitApkDir.exists() && !splitApkDir.mkdirs()) {
            SplitLog.w(TAG, "getSplitAPkDir error", new Object[0]);
        }
        return splitApkDir;
    }

    public File getSplitApkFile(String splitName, int version, boolean isWriteMode) {
        return new File(getSplitApkDir(splitName, version, isWriteMode), getSplitApkFileName(splitName));
    }

    public String getSplitApkFileName(String splitName) {
        return splitName + "-" + SplitConstants.MASTER + SplitConstants.DOT_APK;
    }

    public File getSplitLibDir(String slitName, String abi, int version) {
        return getSplitLibDir(slitName, abi, version, false);
    }

    public File getSplitLibDir(String slitName, String abi, int version, boolean isWriteMode) {
        File libDir = new File(getSplitApkDir(slitName, version, isWriteMode), LIB_DIR + File.separator + abi);
        if (isWriteMode && !libDir.exists() && !libDir.mkdirs()) {
            SplitLog.w(TAG, "getSplitLibDir error", new Object[0]);
        }
        return libDir;
    }

    public File getSplitTmpDir(boolean isWriteMode) {
        File tmpDir = new File(this.mRootDir, "tmp");
        if (isWriteMode && !tmpDir.exists() && !tmpDir.mkdirs()) {
            SplitLog.w(TAG, "getSplitTmpDir error", new Object[0]);
        }
        return tmpDir;
    }
}
