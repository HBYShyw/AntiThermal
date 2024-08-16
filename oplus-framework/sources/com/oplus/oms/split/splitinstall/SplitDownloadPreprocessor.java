package com.oplus.oms.split.splitinstall;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.SharedPreferencesUtil;
import com.oplus.oms.split.common.SignatureValidator;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitdownload.IProvider;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInstallUtil;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SplitDownloadPreprocessor implements Closeable {
    private static final String LOCK_FILENAME = "SplitCopier.lock";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final String TAG = "SplitDownloadPreprocessor";
    private final FileLock mCacheLock;
    private boolean mHasVerified = false;
    private final FileChannel mLockChannel;
    private final RandomAccessFile mLockRaf;
    private final File mSplitDir;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitDownloadPreprocessor(File splitDir) throws IOException, IllegalStateException {
        this.mSplitDir = splitDir;
        File lockFile = new File(splitDir, LOCK_FILENAME);
        RandomAccessFile randomAccessFile = new RandomAccessFile(lockFile, "rw");
        this.mLockRaf = randomAccessFile;
        try {
            FileChannel channel = randomAccessFile.getChannel();
            this.mLockChannel = channel;
            try {
                SplitLog.d(TAG, "Blocking on lock " + lockFile.getPath(), new Object[0]);
                this.mCacheLock = channel.lock();
                SplitLog.d(TAG, lockFile.getPath() + " locked", new Object[0]);
            } catch (IOException | IllegalStateException var5) {
                FileUtil.closeQuietly(this.mLockChannel);
                throw var5;
            }
        } catch (IOException | IllegalStateException var6) {
            FileUtil.closeQuietly(this.mLockRaf);
            throw var6;
        }
    }

    public int install(Context context, SplitVersionInfo info) throws InstallException {
        if (!this.mCacheLock.isValid()) {
            SplitLog.e(TAG, "cacheLock was closed, split: %s", info.getSplitInfo().getSplitName());
            throw new InstallException(-100, new IOException("cacheLock was closed"));
        }
        if (info == null) {
            throw new InstallException(-100, new Throwable("split version info is null"));
        }
        SplitVersionInfo versionInfo = info;
        String splitName = versionInfo.getSplitInfo().getSplitName();
        int from = versionInfo.getFrom();
        SplitLog.i(TAG, "install split, splitName = " + splitName + ", from = " + versionInfo.getFrom(), new Object[0]);
        int versionCode = versionInfo.getInstallVersionCode();
        if (versionCode == -1) {
            String msg = splitName + "get version error";
            SplitLog.e(TAG, msg, new Object[0]);
            throw new InstallException(-100, new Throwable(msg));
        }
        if (from == 2) {
            File downloadFile = SplitPathManager.require().getDownloadApk(splitName, String.valueOf(versionCode), false);
            if (!verifySplitApk(context, downloadFile, false)) {
                deleteCorruptedOrObsoletedSplitApk(downloadFile.getParentFile().getParentFile());
                versionInfo = info.getDefaultVersionInfo();
            }
        }
        int installedVersion = SplitInstallUtil.getSplitInstallVersion(context, splitName);
        if (versionInfo.getFrom() == -1) {
            if (installedVersion > -1) {
                return 0;
            }
            throw new InstallException(-100, new Throwable("split cloud default version info is null"));
        }
        if (versionInfo.getInstallVersionCode() <= installedVersion) {
            SplitLog.i(TAG, "split apk has installed - name = " + splitName, new Object[0]);
            return 0;
        }
        int installType = processCopyFile(context, installedVersion, versionInfo);
        return installType;
    }

    private int processCopyFile(Context context, int installedVersion, SplitVersionInfo versionInfo) throws InstallException {
        int installType = -1;
        String splitName = versionInfo.getSplitInfo().getSplitName();
        int versionCode = versionInfo.getInstallVersionCode();
        int from = versionInfo.getFrom();
        File splitApk = SplitPathManager.require().getSplitApkFile(splitName, versionCode, true);
        boolean success = copySplitFile(context, versionInfo, splitApk);
        if (success) {
            extractLibIfNeed(context, versionInfo.getSplitInfo(), splitApk, versionCode);
            installType = from;
            if (from == 2) {
                File downloadFile = SplitPathManager.require().getSplitDirDownloadDir(splitName, String.valueOf(versionCode), false);
                deleteCorruptedOrObsoletedSplitApk(downloadFile.getParentFile());
            }
            if (installedVersion != -1) {
                SplitInstallUtil.removeSplitInstalledFile(versionInfo.getSplitInfo(), installedVersion, context);
            }
            SplitInstallUtil.putSplitInstallVersion(context, splitName, versionCode);
        }
        return installType;
    }

    private InputStream getSplitApkInputStream(Context context, SplitVersionInfo info) throws InstallException, IOException {
        if (context == null || info == null) {
            return null;
        }
        int from = info.getFrom();
        String splitName = info.getSplitInfo().getSplitName();
        int versionCode = info.getInstallVersionCode();
        switch (from) {
            case 1:
                String assetsFileName = SplitConstants.OMS + File.separator + splitName + "-" + SplitConstants.MASTER + SplitConstants.DOT_ZIP;
                return context.getAssets().open(assetsFileName);
            case 2:
                File downloadFile = SplitPathManager.require().getDownloadApk(splitName, String.valueOf(versionCode), false);
                return new FileInputStream(downloadFile);
            case 3:
                String apkPath = getComponentFeatureApkPath(context, info.getSplitInfo());
                if (TextUtils.isEmpty(apkPath)) {
                    throw new InstallException(-100, new IOException("get " + splitName + " component apk failed"));
                }
                File componentFile = new File(apkPath);
                if (!verifySplitApk(context, componentFile, false)) {
                    throw new InstallException(-11, new IOException("verify signature error"));
                }
                this.mHasVerified = true;
                return new FileInputStream(componentFile);
            case 4:
                IProvider provider = SplitApkLoadStrategy.getInstatnce().getCustomProvider();
                if (provider == null) {
                    throw new InstallException(-100, new Throwable(splitName + "get version error"));
                }
                try {
                    InputStream inputStream = provider.getSplitFileStream(context, splitName);
                    return inputStream;
                } catch (FileNotFoundException e) {
                    throw new InstallException(-100, new Throwable(splitName + "custom file no found"));
                }
            default:
                throw new InstallException(-2, new Throwable(splitName + "get version error"));
        }
    }

    private boolean copySplitFile(Context context, SplitVersionInfo versionInfo, File splitApk) throws InstallException {
        InputStream is;
        SplitLog.d(TAG, "copySplitFile, versionInfo = " + versionInfo.toString(), new Object[0]);
        if (splitApk.exists()) {
            return true;
        }
        int numAttempts = 0;
        boolean isCopySuccessful = false;
        SplitInfo splitInfo = versionInfo.getSplitInfo();
        File tmpDir = SplitPathManager.require().getSplitTmpDir(true);
        try {
            File tmp = File.createTempFile("tmp-" + splitInfo.getSplitName(), SplitConstants.DOT_APK, tmpDir);
            while (!isCopySuccessful && numAttempts < 3) {
                numAttempts++;
                try {
                    is = getSplitApkInputStream(context, versionInfo);
                } catch (IOException e) {
                    SplitLog.w(TAG, "split apk is not existing, attempts times : " + numAttempts, new Object[0]);
                }
                if (is == null) {
                    throw new InstallException(-100, new IOException("get split file error"));
                    break;
                }
                FileUtil.copyFile(is, new FileOutputStream(tmp));
                if (!tmp.renameTo(splitApk)) {
                    SplitLog.i(TAG, "Failed to rename " + tmp.getName() + " to " + splitApk.getName(), new Object[0]);
                } else {
                    isCopySuccessful = true;
                }
                SplitLog.d(TAG, "Copy built-in split " + (isCopySuccessful ? "succeeded" : "failed") + " '" + splitApk.getAbsolutePath() + "': length " + splitApk.length(), new Object[0]);
                if (!isCopySuccessful) {
                    FileUtil.deleteFileSafely(splitApk);
                }
            }
            FileUtil.deleteFileSafely(tmp);
            if (!isCopySuccessful) {
                throw new InstallException(-13, new IOException("Failed to copy built-in file"));
            }
            if (this.mHasVerified || verifySplitApk(context, splitApk, true)) {
                return true;
            }
            throw new InstallException(-11, new IOException("verify signature error"));
        } catch (IOException e2) {
            throw new InstallException(-100, new IOException("Failed to create temp file " + splitApk.getPath()));
        }
    }

    private String getComponentFeatureApkPath(Context context, SplitInfo info) {
        HashMap<String, String> infoForSplit = info.getInfoForSplit();
        if (infoForSplit == null) {
            return null;
        }
        String action = infoForSplit.get("componentAction");
        if (TextUtils.isEmpty(action)) {
            return null;
        }
        String apkPath = (String) SharedPreferencesUtil.getInstance(context).get(action + SplitConstants.SUFFIX_COMPONENT_PATH, "");
        return apkPath;
    }

    private void extractLibIfNeed(Context context, SplitInfo info, File splitApk, int version) throws InstallException {
        try {
            SplitInfo.LibData libData = info.getPrimaryLibData(context);
            if (libData == null) {
                SplitLog.i(TAG, "load libData is null " + info.getSplitName(), new Object[0]);
                return;
            }
            String splitName = info.getSplitName();
            File splitLibDir = SplitPathManager.require().getSplitLibDir(splitName, libData.getAbi(), version, true);
            try {
                extractLib(splitApk, splitLibDir, libData);
            } catch (InstallException e) {
                FileUtil.deleteFileSafely(splitApk);
                throw new InstallException(-13, new IOException("Failed to extractLib " + splitApk.getAbsolutePath()));
            }
        } catch (IOException e2) {
            SplitLog.w(TAG, "load getPrimaryLibData error", new Object[0]);
            FileUtil.deleteFileSafely(splitApk);
            throw new InstallException(-100, new IOException("Failed to getPrimaryLibData " + splitApk.getAbsolutePath()));
        }
    }

    private void extractLib(File splitApk, File libDir, SplitInfo.LibData libData) throws InstallException {
        try {
            SplitLibExtractor extractor = new SplitLibExtractor(splitApk, libDir);
            try {
                List<File> libFiles = extractor.load(libData, false);
                SplitLog.d(TAG, "Succeed to extract libs:  " + libFiles, new Object[0]);
                extractor.close();
            } finally {
            }
        } catch (IOException e) {
            SplitLog.w(TAG, "Failed to load or extract lib files", e);
            throw new InstallException(-13, e);
        }
    }

    private boolean verifySplitApk(Context context, File splitApk, boolean delete) {
        if (context == null || splitApk == null || !splitApk.exists() || !FileUtil.isLegalFile(splitApk)) {
            return false;
        }
        boolean ret = SignatureValidator.validateSplit(context, splitApk);
        if (!ret) {
            SplitLog.w(TAG, "Oops! Failed to check file " + splitApk.getName() + " signature", new Object[0]);
            if (delete) {
                deleteCorruptedOrObsoletedSplitApk(splitApk.getParentFile());
            }
        }
        return ret;
    }

    private void deleteCorruptedOrObsoletedSplitApk(File splitDir) {
        if (splitDir == null || !splitDir.exists()) {
            return;
        }
        FileUtil.deleteDir(splitDir);
        if (splitDir.exists()) {
            SplitLog.w(TAG, "Failed to delete corrupted split files", new Object[0]);
        } else {
            SplitLog.d(TAG, "delete success", new Object[0]);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        FileChannel fileChannel = this.mLockChannel;
        if (fileChannel != null && fileChannel.isOpen()) {
            try {
                this.mLockChannel.close();
            } catch (IOException e) {
                throw new IOException("lockChannel.close error");
            }
        } else {
            SplitLog.d(TAG, "lockChannel may has closed" + this.mLockChannel, new Object[0]);
        }
        RandomAccessFile randomAccessFile = this.mLockRaf;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e2) {
                throw new IOException("lockRaf.close error");
            }
        } else {
            SplitLog.d(TAG, "lockRaf may has closed ", new Object[0]);
        }
        FileLock fileLock = this.mCacheLock;
        if (fileLock != null && fileLock.isValid()) {
            try {
                this.mCacheLock.release();
            } catch (IOException e3) {
                throw new IOException("cacheLock.close error");
            }
        } else {
            SplitLog.d(TAG, "cacheLock may has closed " + this.mCacheLock, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<SplitFile> load(Context context, SplitVersionInfo info, boolean verifySignature) throws IOException {
        if (!this.mCacheLock.isValid()) {
            throw new IllegalStateException("FileCheckerAndCopier was closed");
        }
        List<SplitFile> downloadedSplitApkFiles = new ArrayList<>();
        if (info.getFrom() == 2) {
            File splitApkDir = SplitPathManager.require().getDownloadApk(info.getSplitInfo().getSplitName(), String.valueOf(info.getInstallVersionCode()), false);
            String splitApkFileName = SplitPathManager.require().getSplitApkFileName(info.getSplitInfo().getSplitName());
            SplitFile splitFile = new SplitFile(splitApkDir, splitApkFileName, info.getSplitDownloadInfo().getSize());
            downloadedSplitApkFiles.add(splitFile);
            if (splitFile.exists() && verifySignature) {
                verifySplitApk(context, splitFile, true);
            }
        }
        return downloadedSplitApkFiles;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class SplitFile extends File {
        long mRealSize;

        SplitFile(File parent, String child, long realSize) {
            super(parent, child);
            this.mRealSize = realSize;
        }
    }
}
