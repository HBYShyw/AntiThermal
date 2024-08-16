package com.android.server.pm.parsing;

import android.content.pm.PackageParserCacheHelper;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Parcel;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.ApexManager;
import com.android.server.pm.parsing.pkg.PackageImpl;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageCacher {
    private static final String TAG = "PackageCacher";
    public static final AtomicInteger sCachedPackageReadCount = new AtomicInteger();
    private File mCacheDir;

    public PackageCacher(File file) {
        this.mCacheDir = file;
    }

    private String getCacheKey(File file, int i) {
        return file.getName() + '-' + i + '-' + file.getAbsolutePath().hashCode();
    }

    @VisibleForTesting
    protected ParsedPackage fromCacheEntry(byte[] bArr) {
        return fromCacheEntryStatic(bArr);
    }

    @VisibleForTesting
    public static ParsedPackage fromCacheEntryStatic(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        new PackageParserCacheHelper.ReadHelper(obtain).startAndInstall();
        PackageImpl packageImpl = new PackageImpl(obtain);
        obtain.recycle();
        sCachedPackageReadCount.incrementAndGet();
        return packageImpl;
    }

    @VisibleForTesting
    protected byte[] toCacheEntry(ParsedPackage parsedPackage) {
        return toCacheEntryStatic(parsedPackage);
    }

    @VisibleForTesting
    public static byte[] toCacheEntryStatic(ParsedPackage parsedPackage) {
        Parcel obtain = Parcel.obtain();
        PackageParserCacheHelper.WriteHelper writeHelper = new PackageParserCacheHelper.WriteHelper(obtain);
        ((PackageImpl) parsedPackage).writeToParcel(obtain, 0);
        writeHelper.finishAndUninstall();
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        return marshall;
    }

    private static boolean isCacheUpToDate(File file, File file2) {
        try {
            if (file.toPath().startsWith(Environment.getApexDirectory().toPath())) {
                File backingApexFile = ApexManager.getInstance().getBackingApexFile(file);
                if (backingApexFile == null) {
                    Slog.w(TAG, "Failed to find APEX file backing " + file.getAbsolutePath());
                } else {
                    file = backingApexFile;
                }
            }
            return Os.stat(file.getAbsolutePath()).st_mtime < Os.stat(file2.getAbsolutePath()).st_mtime;
        } catch (ErrnoException e) {
            if (e.errno != OsConstants.ENOENT) {
                Slog.w("Error while stating package cache : ", e);
            }
            return false;
        }
    }

    public ParsedPackage getCachedResult(File file, int i) {
        File file2 = new File(this.mCacheDir, getCacheKey(file, i));
        try {
            if (!isCacheUpToDate(file, file2)) {
                return null;
            }
            ParsedPackage fromCacheEntry = fromCacheEntry(IoUtils.readFileAsByteArray(file2.getAbsolutePath()));
            if (file.getAbsolutePath().equals(fromCacheEntry.getPath())) {
                return fromCacheEntry;
            }
            return null;
        } catch (Throwable th) {
            Slog.w(TAG, "Error reading package cache: ", th);
            file2.delete();
            return null;
        }
    }

    public void cacheResult(File file, int i, ParsedPackage parsedPackage) {
        try {
            File file2 = new File(this.mCacheDir, getCacheKey(file, i));
            if (file2.exists() && !file2.delete()) {
                Slog.e(TAG, "Unable to delete cache file: " + file2);
            }
            byte[] cacheEntry = toCacheEntry(parsedPackage);
            if (cacheEntry == null) {
                return;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                try {
                    fileOutputStream.write(cacheEntry);
                    fileOutputStream.close();
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e) {
                Slog.w(TAG, "Error writing cache entry.", e);
                file2.delete();
            }
        } catch (Throwable th3) {
            Slog.w(TAG, "Error saving package cache.", th3);
        }
    }

    public void cleanCachedResult(File file) {
        final String name = file.getName();
        for (File file2 : FileUtils.listFilesOrEmpty(this.mCacheDir, new FilenameFilter() { // from class: com.android.server.pm.parsing.PackageCacher$$ExternalSyntheticLambda0
            @Override // java.io.FilenameFilter
            public final boolean accept(File file3, String str) {
                boolean lambda$cleanCachedResult$0;
                lambda$cleanCachedResult$0 = PackageCacher.lambda$cleanCachedResult$0(name, file3, str);
                return lambda$cleanCachedResult$0;
            }
        })) {
            if (!file2.delete()) {
                Slog.e(TAG, "Unable to clean cache file: " + file2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$cleanCachedResult$0(String str, File file, String str2) {
        return str2.startsWith(str);
    }
}
