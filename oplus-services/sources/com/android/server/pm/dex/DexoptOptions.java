package com.android.server.pm.dex;

import android.util.Log;
import com.android.server.art.model.DexoptParams;
import com.android.server.pm.PackageManagerServiceCompilerMapping;
import dalvik.system.DexFile;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DexoptOptions {
    public static final String COMPILER_FILTER_NOOP = "skip";
    public static final int DEXOPT_AS_SHARED_LIBRARY = 64;
    public static final int DEXOPT_BOOT_COMPLETE = 4;
    public static final int DEXOPT_CHECK_FOR_PROFILES_UPDATES = 1;
    public static final int DEXOPT_DOWNGRADE = 32;
    public static final int DEXOPT_FORCE = 2;
    public static final int DEXOPT_FOR_RESTORE = 2048;
    public static final int DEXOPT_IDLE_BACKGROUND_JOB = 512;
    public static final int DEXOPT_INSTALL_WITH_DEX_METADATA_FILE = 1024;
    public static final int DEXOPT_ONLY_SECONDARY_DEX = 8;
    private static final String TAG = "DexoptOptions";
    private final int mCompilationReason;
    private final String mCompilerFilter;
    private final int mFlags;
    private final String mPackageName;
    private final String mSplitName;

    public DexoptOptions(String str, String str2, int i) {
        this(str, -1, str2, null, i);
    }

    public DexoptOptions(String str, int i, int i2) {
        this(str, i, PackageManagerServiceCompilerMapping.getCompilerFilterForReason(i), null, i2);
    }

    public DexoptOptions(String str, int i, String str2, String str3, int i2) {
        if ((i2 & (-3696)) != 0) {
            throw new IllegalArgumentException("Invalid flags : " + Integer.toHexString(i2));
        }
        this.mPackageName = str;
        this.mCompilerFilter = str2;
        this.mFlags = i2;
        this.mSplitName = str3;
        this.mCompilationReason = i;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public boolean isCheckForProfileUpdates() {
        return (this.mFlags & 1) != 0;
    }

    public String getCompilerFilter() {
        return this.mCompilerFilter;
    }

    public boolean isForce() {
        return (this.mFlags & 2) != 0;
    }

    public boolean isBootComplete() {
        return (this.mFlags & 4) != 0;
    }

    public boolean isDexoptOnlySecondaryDex() {
        return (this.mFlags & 8) != 0;
    }

    public boolean isDowngrade() {
        return (this.mFlags & 32) != 0;
    }

    public boolean isDexoptAsSharedLibrary() {
        return (this.mFlags & 64) != 0;
    }

    public boolean isDexoptIdleBackgroundJob() {
        return (this.mFlags & 512) != 0;
    }

    public boolean isDexoptInstallWithDexMetadata() {
        return (this.mFlags & 1024) != 0;
    }

    public boolean isDexoptInstallForRestore() {
        return (this.mFlags & 2048) != 0;
    }

    public String getSplitName() {
        return this.mSplitName;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public int getCompilationReason() {
        return this.mCompilationReason;
    }

    public boolean isCompilationEnabled() {
        return !this.mCompilerFilter.equals(COMPILER_FILTER_NOOP);
    }

    public DexoptOptions overrideCompilerFilter(String str) {
        return new DexoptOptions(this.mPackageName, this.mCompilationReason, str, this.mSplitName, this.mFlags);
    }

    public static String convertToArtServiceDexoptReason(int i) {
        switch (i) {
            case 0:
                return "first-boot";
            case 1:
                return "boot-after-ota";
            case 2:
            case 10:
                throw new UnsupportedOperationException("ART Service unsupported compilation reason " + i);
            case 3:
                return "install";
            case 4:
                return "install-fast";
            case 5:
                return "install-bulk";
            case 6:
                return "install-bulk-secondary";
            case 7:
                return "install-bulk-downgraded";
            case 8:
                return "install-bulk-secondary-downgraded";
            case 9:
                return "bg-dexopt";
            case 11:
                return "inactive";
            case 12:
                return "cmdline";
            case 13:
                return "boot-after-mainline-update";
            default:
                throw new IllegalArgumentException("Invalid compilation reason " + i);
        }
    }

    public DexoptParams convertToDexoptParams(int i) {
        if (this.mSplitName != null) {
            throw new UnsupportedOperationException("Request to optimize only split " + this.mSplitName + " for " + this.mPackageName);
        }
        if ((this.mFlags & 1) == 0 && DexFile.isProfileGuidedCompilerFilter(this.mCompilerFilter)) {
            throw new IllegalArgumentException("DEXOPT_CHECK_FOR_PROFILES_UPDATES must be set with profile guided filter");
        }
        int i2 = this.mFlags;
        if ((i2 & 2) != 0) {
            i |= 16;
        }
        int i3 = (i2 & 8) != 0 ? i | 2 : i | 1;
        if ((i2 & 32) != 0) {
            i3 |= 8;
        }
        if ((i2 & 1024) == 0) {
            Log.w(TAG, "DEXOPT_INSTALL_WITH_DEX_METADATA_FILE not set in request to optimise " + this.mPackageName + " - ART Service will unconditionally use a DM file if present.");
        }
        int i4 = this.mFlags;
        return new DexoptParams.Builder(convertToArtServiceDexoptReason(this.mCompilationReason), i3).setCompilerFilter(this.mCompilerFilter).setPriorityClass((i4 & 4) != 0 ? (i4 & 2048) != 0 ? 80 : (i4 & 512) != 0 ? 40 : 60 : 100).build();
    }
}
