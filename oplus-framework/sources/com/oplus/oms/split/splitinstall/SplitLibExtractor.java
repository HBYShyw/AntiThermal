package com.oplus.oms.split.splitinstall;

import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes.dex */
final class SplitLibExtractor implements Closeable {
    private static final String LOCK_FILENAME = "SplitLib.lock";
    private static final String TAG = "Split:LibExtractor";
    private final FileLock mCacheLock;
    private final File mLibDir;
    private final FileChannel mLockChannel;
    private final RandomAccessFile mLockRaf;
    private final File mSourceApk;

    public SplitLibExtractor(File sourceApk, File libDir) throws IOException {
        this.mSourceApk = sourceApk;
        this.mLibDir = libDir;
        File lockFile = new File(libDir, LOCK_FILENAME);
        RandomAccessFile randomAccessFile = new RandomAccessFile(lockFile, "rw");
        this.mLockRaf = randomAccessFile;
        try {
            FileChannel channel = randomAccessFile.getChannel();
            this.mLockChannel = channel;
            try {
                SplitLog.d(TAG, "Blocking on lock " + lockFile.getPath(), new Object[0]);
                this.mCacheLock = channel.lock();
                SplitLog.d(TAG, lockFile.getPath() + " locked", new Object[0]);
            } catch (IOException | RuntimeException var5) {
                FileUtil.closeQuietly(this.mLockChannel);
                throw var5;
            }
        } catch (RuntimeException var6) {
            FileUtil.closeQuietly(this.mLockRaf);
            throw var6;
        }
    }

    public List<File> load(SplitInfo.LibData libData, boolean forceReload) throws IOException {
        List<File> files;
        if (libData == null) {
            return null;
        }
        if (!this.mCacheLock.isValid()) {
            throw new IllegalStateException("SplitLibExtractor was closed");
        }
        if (!forceReload) {
            try {
                files = loadExistingExtractions(libData.getLibs());
            } catch (IOException e) {
                SplitLog.w(TAG, "Failed to reload existing extracted lib files, falling back to fresh extraction " + e.getMessage(), new Object[0]);
                files = performExtractions(libData);
            }
        } else {
            files = performExtractions(libData);
        }
        SplitLog.d(TAG, "load found " + files.size() + " lib files", new Object[0]);
        return files;
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01b9 A[Catch: all -> 0x0299, TryCatch #3 {all -> 0x0299, blocks: (B:3:0x000e, B:4:0x0035, B:6:0x003b, B:9:0x0051, B:12:0x0058, B:19:0x0060, B:22:0x0067, B:24:0x007f, B:99:0x008d, B:27:0x0091, B:34:0x00dd, B:43:0x00ef, B:45:0x00f8, B:48:0x0126, B:49:0x0179, B:52:0x018c, B:54:0x01b9, B:56:0x01c2, B:61:0x01e8, B:65:0x0154, B:75:0x014c, B:82:0x0149, B:90:0x01f5, B:95:0x0206, B:96:0x0228, B:102:0x0229, B:103:0x0249, B:114:0x0252, B:118:0x026a, B:119:0x0298), top: B:2:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01e8 A[Catch: all -> 0x0299, TryCatch #3 {all -> 0x0299, blocks: (B:3:0x000e, B:4:0x0035, B:6:0x003b, B:9:0x0051, B:12:0x0058, B:19:0x0060, B:22:0x0067, B:24:0x007f, B:99:0x008d, B:27:0x0091, B:34:0x00dd, B:43:0x00ef, B:45:0x00f8, B:48:0x0126, B:49:0x0179, B:52:0x018c, B:54:0x01b9, B:56:0x01c2, B:61:0x01e8, B:65:0x0154, B:75:0x014c, B:82:0x0149, B:90:0x01f5, B:95:0x0206, B:96:0x0228, B:102:0x0229, B:103:0x0249, B:114:0x0252, B:118:0x026a, B:119:0x0298), top: B:2:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x018a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<File> performExtractions(SplitInfo.LibData libData) throws IOException {
        String str;
        String libPrefix;
        FileOutputStream fos;
        Throwable th;
        SplitLibExtractor splitLibExtractor = this;
        String str2 = "lib/";
        ZipFile sourceZip = new ZipFile(splitLibExtractor.mSourceApk);
        try {
            String libPrefix2 = "lib/" + libData.getAbi() + "/";
            Enumeration<? extends ZipEntry> e = sourceZip.entries();
            List<File> libFiles = new ArrayList<>();
            loop0: while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                String entryName = entry.getName();
                if (entryName.charAt(0) == 'l' && entryName.startsWith(str2)) {
                    if (!entryName.endsWith(SplitConstants.DOT_SO)) {
                        splitLibExtractor = this;
                    } else if (entryName.startsWith(libPrefix2)) {
                        String libName = entryName.substring(entryName.lastIndexOf(47) + 1);
                        SplitInfo.LibData.Lib lib = splitLibExtractor.findLib(libName, libData.getLibs());
                        if (lib == null) {
                            throw new IOException("Failed to find " + libName + " in split-info");
                        }
                        File extractedLib = new File(splitLibExtractor.mLibDir, libName);
                        if (!extractedLib.exists()) {
                            SplitLog.d(TAG, "Extraction is needed for lib: " + extractedLib.getAbsolutePath(), new Object[0]);
                            boolean isExtractionSuccessful = false;
                            File tempDir = SplitPathManager.require().getSplitTmpDir(true);
                            File tmp = File.createTempFile("tmp-" + libName, "", tempDir);
                            int numAttempts = 0;
                            while (numAttempts < 3 && !isExtractionSuccessful) {
                                int numAttempts2 = numAttempts + 1;
                                try {
                                    FileOutputStream fos2 = new FileOutputStream(tmp);
                                    try {
                                        str = str2;
                                        fos = fos2;
                                    } catch (Throwable th2) {
                                        str = str2;
                                        fos = fos2;
                                        libPrefix = libPrefix2;
                                        th = th2;
                                    }
                                } catch (IOException e2) {
                                    copyError = e2;
                                    str = str2;
                                }
                                try {
                                    FileUtil.copyFile(sourceZip.getInputStream(entry), fos);
                                    try {
                                        fos.close();
                                        if (!tmp.renameTo(extractedLib)) {
                                            libPrefix = libPrefix2;
                                            try {
                                                SplitLog.w(TAG, "Failed to rename \"" + tmp.getName() + "\" to \"" + extractedLib.getName() + "\"", new Object[0]);
                                            } catch (IOException e3) {
                                                copyError = e3;
                                                SplitLog.w(TAG, "Failed to extract so :" + libName + ", attempts times : " + numAttempts2, new Object[0]);
                                                SplitLog.d(TAG, "Extraction " + (isExtractionSuccessful ? "succeeded" : "failed") + " '" + extractedLib.getAbsolutePath() + "': length " + extractedLib.length(), new Object[0]);
                                                if (isExtractionSuccessful) {
                                                }
                                                numAttempts = numAttempts2;
                                                libPrefix2 = libPrefix;
                                                str2 = str;
                                            }
                                        } else {
                                            libPrefix = libPrefix2;
                                            isExtractionSuccessful = true;
                                        }
                                    } catch (IOException e4) {
                                        copyError = e4;
                                        libPrefix = libPrefix2;
                                        SplitLog.w(TAG, "Failed to extract so :" + libName + ", attempts times : " + numAttempts2, new Object[0]);
                                        SplitLog.d(TAG, "Extraction " + (isExtractionSuccessful ? "succeeded" : "failed") + " '" + extractedLib.getAbsolutePath() + "': length " + extractedLib.length(), new Object[0]);
                                        if (isExtractionSuccessful) {
                                        }
                                        numAttempts = numAttempts2;
                                        libPrefix2 = libPrefix;
                                        str2 = str;
                                    }
                                    SplitLog.d(TAG, "Extraction " + (isExtractionSuccessful ? "succeeded" : "failed") + " '" + extractedLib.getAbsolutePath() + "': length " + extractedLib.length(), new Object[0]);
                                    if (isExtractionSuccessful) {
                                        libFiles.add(extractedLib);
                                    } else {
                                        FileUtil.deleteFileSafely(extractedLib);
                                        if (extractedLib.exists()) {
                                            SplitLog.d(TAG, "Failed to delete extracted lib that has been corrupted'" + extractedLib.getPath() + "'", new Object[0]);
                                        }
                                    }
                                    numAttempts = numAttempts2;
                                    libPrefix2 = libPrefix;
                                    str2 = str;
                                } catch (Throwable th3) {
                                    libPrefix = libPrefix2;
                                    th = th3;
                                    try {
                                        fos.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                    throw th;
                                    break loop0;
                                }
                            }
                            String str3 = str2;
                            String libPrefix3 = libPrefix2;
                            FileUtil.deleteFileSafely(tmp);
                            if (!isExtractionSuccessful) {
                                throw new IOException("Could not create lib file " + extractedLib.getAbsolutePath() + ")");
                            }
                            splitLibExtractor = this;
                            libPrefix2 = libPrefix3;
                            str2 = str3;
                        } else {
                            libFiles.add(extractedLib);
                        }
                    }
                }
            }
            FileUtil.closeQuietly(sourceZip);
            if (libFiles.size() != libData.getLibs().size()) {
                throw new IOException("Number of extracted so files is mismatch, expected: " + libData.getLibs().size() + " ,but: " + libFiles.size());
            }
            sourceZip.close();
            return libFiles;
        } catch (Throwable th5) {
            try {
                sourceZip.close();
                throw th5;
            } catch (Throwable th6) {
                th5.addSuppressed(th6);
                throw th5;
            }
        }
    }

    private SplitInfo.LibData.Lib findLib(String libName, List<SplitInfo.LibData.Lib> libs) {
        if (libs == null || libs.isEmpty()) {
            return null;
        }
        for (SplitInfo.LibData.Lib lib : libs) {
            if (lib.getName().equals(libName)) {
                return lib;
            }
        }
        return null;
    }

    private List<File> loadExistingExtractions(List<SplitInfo.LibData.Lib> libs) throws IOException {
        SplitLog.d(TAG, "loading existing lib files", new Object[0]);
        if (libs == null) {
            SplitLog.w(TAG, "loading existing lib files null", new Object[0]);
            return Collections.emptyList();
        }
        File[] files = this.mLibDir.listFiles();
        if (files == null || files.length <= 0) {
            throw new IOException("Missing extracted lib file '" + this.mLibDir.getPath() + "'");
        }
        List<File> libFiles = new ArrayList<>(files.length);
        for (SplitInfo.LibData.Lib lib : libs) {
            boolean hasSo = false;
            for (File file : files) {
                if (lib.getName().equals(file.getName())) {
                    hasSo = true;
                    libFiles.add(file);
                }
            }
            if (!hasSo) {
                throw new IOException("Invalid extracted lib: file " + lib.getName() + "is not existing!");
            }
        }
        SplitLog.d(TAG, "Existing lib files loaded", new Object[0]);
        return libFiles;
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
}
