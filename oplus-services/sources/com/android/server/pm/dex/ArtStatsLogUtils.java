package com.android.server.pm.dex;

import android.os.SystemClock;
import android.util.Slog;
import android.util.jar.StrictJarFile;
import com.android.internal.art.ArtStatsLog;
import com.android.server.pm.PackageManagerService;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ArtStatsLogUtils {
    private static final int ART_COMPILATION_FILTER_FAKE_RUN_FROM_APK_FALLBACK = 14;
    private static final int ART_COMPILATION_FILTER_FAKE_RUN_FROM_VDEX_FALLBACK = 15;
    private static final int ART_COMPILATION_REASON_INSTALL_BULK_DOWNGRADED = 15;
    private static final int ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY = 14;
    private static final int ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY_DOWNGRADED = 16;
    private static final Map<Integer, Integer> COMPILATION_REASON_MAP;
    private static final Map<String, Integer> COMPILE_FILTER_MAP;
    private static final Map<String, Integer> ISA_MAP;
    private static final String PROFILE_DEX_METADATA = "primary.prof";
    private static final Map<Integer, Integer> STATUS_MAP;
    private static final String TAG = "ArtStatsLogUtils";
    private static final String VDEX_DEX_METADATA = "primary.vdex";

    static {
        HashMap hashMap = new HashMap();
        COMPILATION_REASON_MAP = hashMap;
        hashMap.put(0, 3);
        hashMap.put(1, 17);
        hashMap.put(2, 11);
        hashMap.put(3, 5);
        hashMap.put(4, 12);
        hashMap.put(5, 13);
        hashMap.put(6, 14);
        hashMap.put(7, 15);
        hashMap.put(8, 16);
        hashMap.put(9, 6);
        hashMap.put(10, 7);
        hashMap.put(11, 8);
        hashMap.put(12, 19);
        hashMap.put(Integer.valueOf(PackageManagerService.REASON_SHARED), 9);
        HashMap hashMap2 = new HashMap();
        COMPILE_FILTER_MAP = hashMap2;
        hashMap2.put("error", 1);
        hashMap2.put("unknown", 2);
        hashMap2.put("assume-verified", 3);
        hashMap2.put("extract", 4);
        hashMap2.put("verify", 5);
        hashMap2.put("quicken", 6);
        hashMap2.put("space-profile", 7);
        hashMap2.put("space", 8);
        hashMap2.put("speed-profile", 9);
        hashMap2.put("speed", 10);
        hashMap2.put("everything-profile", 11);
        hashMap2.put("everything", 12);
        hashMap2.put("run-from-apk", 13);
        hashMap2.put("run-from-apk-fallback", 14);
        hashMap2.put("run-from-vdex-fallback", 15);
        HashMap hashMap3 = new HashMap();
        ISA_MAP = hashMap3;
        hashMap3.put("arm", 1);
        hashMap3.put("arm64", 2);
        hashMap3.put("x86", 3);
        hashMap3.put("x86_64", 4);
        hashMap3.put("mips", 5);
        hashMap3.put("mips64", 6);
        STATUS_MAP = Map.of(-1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 1, 6, 7);
    }

    public static void writeStatsLog(ArtStatsLogger artStatsLogger, long j, String str, int i, long j2, String str2, int i2, int i3, int i4, String str3, String str4) {
        int dexMetadataType = getDexMetadataType(str2);
        artStatsLogger.write(j, i, i2, str, 10, i3, dexMetadataType, i4, str3);
        artStatsLogger.write(j, i, i2, str, 11, getDexBytes(str4), dexMetadataType, i4, str3);
        artStatsLogger.write(j, i, i2, str, 12, j2, dexMetadataType, i4, str3);
    }

    public static int getApkType(final String str, String str2, String[] strArr) {
        if (str.equals(str2)) {
            return 1;
        }
        return Arrays.stream(strArr).anyMatch(new Predicate() { // from class: com.android.server.pm.dex.ArtStatsLogUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getApkType$0;
                lambda$getApkType$0 = ArtStatsLogUtils.lambda$getApkType$0(str, (String) obj);
                return lambda$getApkType$0;
            }
        }) ? 2 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getApkType$0(String str, String str2) {
        return str2.equals(str);
    }

    private static long getDexBytes(String str) {
        StrictJarFile strictJarFile = null;
        try {
            try {
                StrictJarFile strictJarFile2 = new StrictJarFile(str, false, false);
                try {
                    Iterator it = strictJarFile2.iterator();
                    Matcher matcher = Pattern.compile("classes(\\d)*[.]dex").matcher("");
                    long j = 0;
                    while (it.hasNext()) {
                        ZipEntry zipEntry = (ZipEntry) it.next();
                        matcher.reset(zipEntry.getName());
                        if (matcher.matches()) {
                            j += zipEntry.getSize();
                        }
                    }
                    try {
                        strictJarFile2.close();
                    } catch (IOException unused) {
                    }
                    return j;
                } catch (IOException unused2) {
                    strictJarFile = strictJarFile2;
                    Slog.e(TAG, "Error when parsing APK " + str);
                    if (strictJarFile == null) {
                        return -1L;
                    }
                    try {
                        strictJarFile.close();
                        return -1L;
                    } catch (IOException unused3) {
                        return -1L;
                    }
                } catch (Throwable th) {
                    th = th;
                    strictJarFile = strictJarFile2;
                    if (strictJarFile != null) {
                        try {
                            strictJarFile.close();
                        } catch (IOException unused4) {
                        }
                    }
                    throw th;
                }
            } catch (IOException unused5) {
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static int getDexMetadataType(String str) {
        if (str == null) {
            return 4;
        }
        StrictJarFile strictJarFile = null;
        try {
            try {
                StrictJarFile strictJarFile2 = new StrictJarFile(str, false, false);
                try {
                    boolean findFileName = findFileName(strictJarFile2, PROFILE_DEX_METADATA);
                    boolean findFileName2 = findFileName(strictJarFile2, VDEX_DEX_METADATA);
                    if (findFileName && findFileName2) {
                        try {
                            strictJarFile2.close();
                            return 3;
                        } catch (IOException unused) {
                            return 3;
                        }
                    }
                    if (findFileName) {
                        try {
                            strictJarFile2.close();
                            return 1;
                        } catch (IOException unused2) {
                            return 1;
                        }
                    }
                    if (!findFileName2) {
                        try {
                            strictJarFile2.close();
                        } catch (IOException unused3) {
                        }
                        return 0;
                    }
                    try {
                        strictJarFile2.close();
                        return 2;
                    } catch (IOException unused4) {
                        return 2;
                    }
                } catch (IOException unused5) {
                    strictJarFile = strictJarFile2;
                    Slog.e(TAG, "Error when parsing dex metadata " + str);
                    if (strictJarFile == null) {
                        return 5;
                    }
                    try {
                        strictJarFile.close();
                        return 5;
                    } catch (IOException unused6) {
                        return 5;
                    }
                } catch (Throwable th) {
                    th = th;
                    strictJarFile = strictJarFile2;
                    if (strictJarFile != null) {
                        try {
                            strictJarFile.close();
                        } catch (IOException unused7) {
                        }
                    }
                    throw th;
                }
            } catch (IOException unused8) {
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static boolean findFileName(StrictJarFile strictJarFile, String str) throws IOException {
        Iterator it = strictJarFile.iterator();
        while (it.hasNext()) {
            if (((ZipEntry) it.next()).getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ArtStatsLogger {
        public void write(long j, int i, int i2, String str, int i3, long j2, int i4, int i5, String str2) {
            ArtStatsLog.write(332, j, i, ((Integer) ArtStatsLogUtils.COMPILE_FILTER_MAP.getOrDefault(str, 2)).intValue(), ((Integer) ArtStatsLogUtils.COMPILATION_REASON_MAP.getOrDefault(Integer.valueOf(i2), 2)).intValue(), SystemClock.uptimeMillis(), 1, i3, j2, i4, i5, ((Integer) ArtStatsLogUtils.ISA_MAP.getOrDefault(str2, 0)).intValue(), 0, 0);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class BackgroundDexoptJobStatsLogger {
        public void write(int i, int i2, long j) {
            ArtStatsLog.write(467, ((Integer) ArtStatsLogUtils.STATUS_MAP.getOrDefault(Integer.valueOf(i), 0)).intValue(), i2, j, 0L);
        }
    }
}
