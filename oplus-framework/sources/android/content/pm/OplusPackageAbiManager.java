package android.content.pm;

import android.os.Build;
import android.util.Log;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import libcore.io.IoUtils;

/* loaded from: classes.dex */
public class OplusPackageAbiManager {
    private static final int ABI_CHECK_RESULT_32 = 0;
    private static final int ABI_CHECK_RESULT_64 = 1;
    private static final int ABI_CHECK_RESULT_MULTI = 2;
    private static final int ABI_CHECK_RESULT_UNKNOWN = -1;
    private static final int INVALID_ABI_CHECK_RESULT = Integer.MIN_VALUE;
    private static final String LIBS_DIR = "lib";
    private static final String TAG = "OplusPackageAbiManager";
    private OplusPackageManager mPm = new OplusPackageManager();
    private static final List<String> SUPPORTED_ABI_LIST_64 = Arrays.asList(Build.SUPPORTED_64_BIT_ABIS);
    private static final List<String> SUPPORTED_ABI_LIST_32 = Arrays.asList("armeabi-v7a", "armeabi");
    private static volatile OplusPackageAbiManager sInstance = null;

    private OplusPackageAbiManager() {
    }

    public static OplusPackageAbiManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusPackageAbiManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusPackageAbiManager();
                }
            }
        }
        return sInstance;
    }

    public int getAbiCheckResult(File apkFile) {
        if (!apkFile.exists()) {
            Log.w(TAG, "Apk file doesn't exist");
            return -1;
        }
        boolean abi32 = false;
        boolean abi64 = false;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(apkFile);
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();
                String zipEntryName = zipEntry.getName();
                if (zipEntryName.startsWith(LIBS_DIR)) {
                    for (String abi64Item : SUPPORTED_ABI_LIST_64) {
                        if (zipEntryName.startsWith(LIBS_DIR + File.separator + abi64Item)) {
                            abi64 = true;
                        }
                    }
                    for (String abi32Item : SUPPORTED_ABI_LIST_32) {
                        if (zipEntryName.startsWith(LIBS_DIR + File.separator + abi32Item)) {
                            abi32 = true;
                        }
                    }
                }
            }
            return !abi32 ? 1 : !abi64 ? 0 : 2;
        } catch (Exception e) {
            Log.e(TAG, "Got exception while decompress apk file", e);
            return -1;
        } finally {
            IoUtils.closeQuietly(zipFile);
        }
    }

    public int getAbiCheckResult(String packageName) {
        return this.mPm.getAbiCheckResult(packageName);
    }

    public List<String> queryIncompatibleApplist() {
        return this.mPm.queryIncompatibleApplist();
    }
}
