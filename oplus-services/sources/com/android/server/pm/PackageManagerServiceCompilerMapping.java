package com.android.server.pm;

import android.os.SystemProperties;
import com.android.server.pm.dex.DexoptOptions;
import dalvik.system.DexFile;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageManagerServiceCompilerMapping {
    static int REASON_SHARED_INDEX;
    public static String[] REASON_STRINGS;
    private static IPackageManagerServiceCompilerMappingExt mPmsCMExt;

    static {
        REASON_STRINGS = new String[]{"first-boot", "boot-after-ota", "post-boot", "install", "install-fast", "install-bulk", "install-bulk-secondary", "install-bulk-downgraded", "install-bulk-secondary-downgraded", "bg-dexopt", "ab-ota", "inactive", "cmdline", "boot-after-mainline-update", "shared"};
        REASON_SHARED_INDEX = r0.length - 1;
        IPackageManagerServiceCompilerMappingExt iPackageManagerServiceCompilerMappingExt = (IPackageManagerServiceCompilerMappingExt) ExtLoader.type(IPackageManagerServiceCompilerMappingExt.class).create();
        mPmsCMExt = iPackageManagerServiceCompilerMappingExt;
        REASON_STRINGS = iPackageManagerServiceCompilerMappingExt.modifyReasonList(REASON_STRINGS);
        REASON_SHARED_INDEX = r0.length - 1;
        int i = PackageManagerService.REASON_LAST + 1;
        String[] strArr = REASON_STRINGS;
        if (i != strArr.length) {
            throw new IllegalStateException("REASON_STRINGS not correct");
        }
        if (!"shared".equals(strArr[REASON_SHARED_INDEX])) {
            throw new IllegalStateException("REASON_STRINGS not correct because of shared index");
        }
    }

    private static String getSystemPropertyName(int i) {
        if (i < 0 || i >= REASON_STRINGS.length) {
            throw new IllegalArgumentException("reason " + i + " invalid");
        }
        return "pm.dexopt." + REASON_STRINGS[i];
    }

    private static String getAndCheckValidity(int i) {
        if (mPmsCMExt.getAndCheckValidityForOplus(i)) {
            return "speed-profile";
        }
        String str = SystemProperties.get(getSystemPropertyName(i));
        if (str == null || str.isEmpty() || (!str.equals(DexoptOptions.COMPILER_FILTER_NOOP) && !DexFile.isValidCompilerFilter(str))) {
            throw new IllegalStateException("Value \"" + str + "\" not valid (reason " + REASON_STRINGS[i] + ")");
        }
        if (isFilterAllowedForReason(i, str)) {
            return str;
        }
        throw new IllegalStateException("Value \"" + str + "\" not allowed (reason " + REASON_STRINGS[i] + ")");
    }

    private static boolean isFilterAllowedForReason(int i, String str) {
        return (i == REASON_SHARED_INDEX && DexFile.isProfileGuidedCompilerFilter(str)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkProperties() {
        IllegalStateException illegalStateException = null;
        for (int i = 0; i <= PackageManagerService.REASON_LAST; i++) {
            if (!mPmsCMExt.checkPropertiesForOplus(i)) {
                try {
                    String systemPropertyName = getSystemPropertyName(i);
                    if (systemPropertyName == null || systemPropertyName.isEmpty()) {
                        throw new IllegalStateException("Reason system property name \"" + systemPropertyName + "\" for reason " + REASON_STRINGS[i]);
                        break;
                    }
                    getAndCheckValidity(i);
                } catch (Exception e) {
                    if (illegalStateException == null) {
                        illegalStateException = new IllegalStateException("PMS compiler filter settings are bad.");
                    }
                    illegalStateException.addSuppressed(e);
                }
            }
        }
        if (illegalStateException != null) {
            throw illegalStateException;
        }
    }

    public static String getCompilerFilterForReason(int i) {
        return getAndCheckValidity(i);
    }

    public static String getDefaultCompilerFilter() {
        String str = SystemProperties.get("dalvik.vm.dex2oat-filter");
        return (str == null || str.isEmpty() || !DexFile.isValidCompilerFilter(str) || DexFile.isProfileGuidedCompilerFilter(str)) ? "speed" : str;
    }

    public static String getReasonName(int i) {
        if (i >= 0) {
            String[] strArr = REASON_STRINGS;
            if (i < strArr.length) {
                return strArr[i];
            }
        }
        throw new IllegalArgumentException("reason " + i + " invalid");
    }
}
