package com.android.server.pm.pkg;

import android.content.pm.SigningInfo;
import android.content.pm.overlay.OverlayPaths;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.SparseArray;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageStateImpl implements PackageState {
    private final AndroidPackage mAndroidPackage;
    private final String mApexModuleName;
    private final int mAppId;
    private int mBooleans;
    private final int mCategoryOverride;
    private final String mCpuAbiOverride;
    private final boolean mHasSharedUser;
    private final int mHiddenApiEnforcementPolicy;
    private final long mLastModifiedTime;
    private final long[] mLastPackageUsageTime;
    private final long mLastUpdateTime;
    private final long mLongVersionCode;
    private final Map<String, Set<String>> mMimeGroups;
    private final String mPackageName;
    private final File mPath;
    private final String mPrimaryCpuAbi;
    private final String mSeInfo;
    private final String mSecondaryCpuAbi;
    private final int mSharedUserAppId;
    private final SigningInfo mSigningInfo;
    private final SparseArray<PackageUserState> mUserStates;
    private final List<SharedLibrary> mUsesLibraries;
    private final List<String> mUsesLibraryFiles;
    private final String[] mUsesSdkLibraries;
    private final long[] mUsesSdkLibrariesVersionsMajor;
    private final String[] mUsesStaticLibraries;
    private final long[] mUsesStaticLibrariesVersions;
    private final String mVolumeUuid;

    @Deprecated
    private void __metadata() {
    }

    public static PackageState copy(PackageStateInternal packageStateInternal) {
        return new PackageStateImpl(packageStateInternal, packageStateInternal.getPkg());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class Booleans {
        private static final int APK_IN_UPDATED_APEX = 16384;
        private static final int EXTERNAL_STORAGE = 2;
        private static final int FORCE_QUERYABLE_OVERRIDE = 512;
        private static final int HIDDEN_UNTIL_INSTALLED = 1024;
        private static final int INSTALL_PERMISSIONS_FIXED = 2048;
        private static final int ODM = 256;
        private static final int OEM = 8;
        private static final int PRIVILEGED = 4;
        private static final int PRODUCT = 32;
        private static final int REQUIRED_FOR_SYSTEM_USER = 128;
        private static final int SYSTEM = 1;
        private static final int SYSTEM_EXT = 64;
        private static final int UPDATED_SYSTEM_APP = 8192;
        private static final int UPDATE_AVAILABLE = 4096;
        private static final int VENDOR = 16;

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface Flags {
        }

        private Booleans() {
        }
    }

    private void setBoolean(@Booleans.Flags int i, boolean z) {
        if (z) {
            this.mBooleans = i | this.mBooleans;
        } else {
            this.mBooleans = (~i) & this.mBooleans;
        }
    }

    private boolean getBoolean(@Booleans.Flags int i) {
        return (this.mBooleans & i) != 0;
    }

    private PackageStateImpl(PackageState packageState, AndroidPackage androidPackage) {
        this.mAndroidPackage = androidPackage;
        setBoolean(1, packageState.isSystem());
        setBoolean(2, packageState.isExternalStorage());
        setBoolean(4, packageState.isPrivileged());
        setBoolean(8, packageState.isOem());
        setBoolean(16, packageState.isVendor());
        setBoolean(32, packageState.isProduct());
        setBoolean(64, packageState.isSystemExt());
        setBoolean(128, packageState.isRequiredForSystemUser());
        setBoolean(256, packageState.isOdm());
        this.mPackageName = packageState.getPackageName();
        this.mVolumeUuid = packageState.getVolumeUuid();
        this.mAppId = packageState.getAppId();
        this.mCategoryOverride = packageState.getCategoryOverride();
        this.mCpuAbiOverride = packageState.getCpuAbiOverride();
        this.mHiddenApiEnforcementPolicy = packageState.getHiddenApiEnforcementPolicy();
        this.mLastModifiedTime = packageState.getLastModifiedTime();
        this.mLastUpdateTime = packageState.getLastUpdateTime();
        this.mLongVersionCode = packageState.getVersionCode();
        this.mMimeGroups = Collections.unmodifiableMap(packageState.getMimeGroups());
        this.mPath = packageState.getPath();
        this.mPrimaryCpuAbi = packageState.getPrimaryCpuAbi();
        this.mSecondaryCpuAbi = packageState.getSecondaryCpuAbi();
        this.mSeInfo = packageState.getSeInfo();
        this.mHasSharedUser = packageState.hasSharedUser();
        this.mSharedUserAppId = packageState.getSharedUserAppId();
        this.mUsesSdkLibraries = packageState.getUsesSdkLibraries();
        this.mUsesSdkLibrariesVersionsMajor = packageState.getUsesSdkLibrariesVersionsMajor();
        this.mUsesStaticLibraries = packageState.getUsesStaticLibraries();
        this.mUsesStaticLibrariesVersions = packageState.getUsesStaticLibrariesVersions();
        this.mUsesLibraries = Collections.unmodifiableList(packageState.getSharedLibraryDependencies());
        this.mUsesLibraryFiles = Collections.unmodifiableList(packageState.getUsesLibraryFiles());
        setBoolean(512, packageState.isForceQueryableOverride());
        setBoolean(1024, packageState.isHiddenUntilInstalled());
        setBoolean(2048, packageState.isInstallPermissionsFixed());
        setBoolean(4096, packageState.isUpdateAvailable());
        this.mLastPackageUsageTime = packageState.getLastPackageUsageTime();
        setBoolean(8192, packageState.isUpdatedSystemApp());
        setBoolean(16384, packageState.isApkInUpdatedApex());
        this.mSigningInfo = packageState.getSigningInfo();
        SparseArray<? extends PackageUserState> userStates = packageState.getUserStates();
        int size = userStates.size();
        this.mUserStates = new SparseArray<>(size);
        for (int i = 0; i < size; i++) {
            this.mUserStates.put(userStates.keyAt(i), UserStateImpl.copy(userStates.valueAt(i)));
        }
        this.mApexModuleName = packageState.getApexModuleName();
    }

    @Override // com.android.server.pm.pkg.PackageState
    public PackageUserState getStateForUser(UserHandle userHandle) {
        PackageUserState packageUserState = getUserStates().get(userHandle.getIdentifier());
        return packageUserState == null ? PackageUserState.DEFAULT : packageUserState;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isExternalStorage() {
        return getBoolean(2);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isForceQueryableOverride() {
        return getBoolean(512);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isHiddenUntilInstalled() {
        return getBoolean(1024);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isInstallPermissionsFixed() {
        return getBoolean(2048);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isOdm() {
        return getBoolean(256);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isOem() {
        return getBoolean(8);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isPrivileged() {
        return getBoolean(4);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isProduct() {
        return getBoolean(32);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isRequiredForSystemUser() {
        return getBoolean(128);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isSystem() {
        return getBoolean(1);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isSystemExt() {
        return getBoolean(64);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isUpdateAvailable() {
        return getBoolean(4096);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isUpdatedSystemApp() {
        return getBoolean(8192);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isApkInUpdatedApex() {
        return getBoolean(16384);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isVendor() {
        return getBoolean(16);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long getVersionCode() {
        return this.mLongVersionCode;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean hasSharedUser() {
        return this.mHasSharedUser;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isApex() {
        return getAndroidPackage() != null && getAndroidPackage().isApex();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class UserStateImpl implements PackageUserState {
        private int mBooleans;
        private final long mCeDataInode;
        private final ArraySet<String> mDisabledComponents;
        private final int mDistractionFlags;
        private final ArraySet<String> mEnabledComponents;
        private final int mEnabledState;
        private final long mFirstInstallTimeMillis;
        private final String mHarmfulAppWarning;
        private final int mInstallReason;
        private final String mLastDisableAppCaller;
        private final OverlayPaths mOverlayPaths;
        private final Map<String, OverlayPaths> mSharedLibraryOverlayPaths;
        private final String mSplashScreenTheme;
        private final int mUninstallReason;

        @Deprecated
        private void __metadata() {
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public int getOplusFreezeState() {
            return 0;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean ignorePackageDisabledInIsEnabled(int i, long j) {
            return false;
        }

        public static PackageUserState copy(PackageUserState packageUserState) {
            return new UserStateImpl(packageUserState);
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        private static class Booleans {
            private static final int HIDDEN = 1;
            private static final int INSTALLED = 2;
            private static final int INSTANT_APP = 4;
            private static final int NOT_LAUNCHED = 8;
            private static final int STOPPED = 16;
            private static final int SUSPENDED = 32;
            private static final int VIRTUAL_PRELOAD = 64;

            /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
            public @interface Flags {
            }

            private Booleans() {
            }
        }

        private void setBoolean(@Booleans.Flags int i, boolean z) {
            if (z) {
                this.mBooleans = i | this.mBooleans;
            } else {
                this.mBooleans = (~i) & this.mBooleans;
            }
        }

        private boolean getBoolean(@Booleans.Flags int i) {
            return (this.mBooleans & i) != 0;
        }

        private UserStateImpl(PackageUserState packageUserState) {
            this.mCeDataInode = packageUserState.getCeDataInode();
            this.mDisabledComponents = packageUserState.m2392getDisabledComponents();
            this.mDistractionFlags = packageUserState.getDistractionFlags();
            this.mEnabledComponents = packageUserState.m2393getEnabledComponents();
            this.mEnabledState = packageUserState.getEnabledState();
            this.mHarmfulAppWarning = packageUserState.getHarmfulAppWarning();
            this.mInstallReason = packageUserState.getInstallReason();
            this.mLastDisableAppCaller = packageUserState.getLastDisableAppCaller();
            this.mOverlayPaths = packageUserState.getOverlayPaths();
            this.mSharedLibraryOverlayPaths = packageUserState.getSharedLibraryOverlayPaths();
            this.mUninstallReason = packageUserState.getUninstallReason();
            this.mSplashScreenTheme = packageUserState.getSplashScreenTheme();
            setBoolean(1, packageUserState.isHidden());
            setBoolean(2, packageUserState.isInstalled());
            setBoolean(4, packageUserState.isInstantApp());
            setBoolean(8, packageUserState.isNotLaunched());
            setBoolean(16, packageUserState.isStopped());
            setBoolean(32, packageUserState.isSuspended());
            setBoolean(64, packageUserState.isVirtualPreload());
            this.mFirstInstallTimeMillis = packageUserState.getFirstInstallTimeMillis();
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isHidden() {
            return getBoolean(1);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isInstalled() {
            return getBoolean(2);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isInstantApp() {
            return getBoolean(4);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isNotLaunched() {
            return getBoolean(8);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isStopped() {
            return getBoolean(16);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isSuspended() {
            return getBoolean(32);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isVirtualPreload() {
            return getBoolean(64);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isComponentEnabled(String str) {
            return this.mEnabledComponents.contains(str);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public boolean isComponentDisabled(String str) {
            return this.mDisabledComponents.contains(str);
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public OverlayPaths getAllOverlayPaths() {
            if (this.mOverlayPaths == null && this.mSharedLibraryOverlayPaths == null) {
                return null;
            }
            OverlayPaths.Builder builder = new OverlayPaths.Builder();
            builder.addAll(this.mOverlayPaths);
            Map<String, OverlayPaths> map = this.mSharedLibraryOverlayPaths;
            if (map != null) {
                Iterator<OverlayPaths> it = map.values().iterator();
                while (it.hasNext()) {
                    builder.addAll(it.next());
                }
            }
            return builder.build();
        }

        public int getBooleans() {
            return this.mBooleans;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public long getCeDataInode() {
            return this.mCeDataInode;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        /* renamed from: getDisabledComponents */
        public ArraySet<String> m2392getDisabledComponents() {
            return this.mDisabledComponents;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public int getDistractionFlags() {
            return this.mDistractionFlags;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        /* renamed from: getEnabledComponents */
        public ArraySet<String> m2393getEnabledComponents() {
            return this.mEnabledComponents;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public int getEnabledState() {
            return this.mEnabledState;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public String getHarmfulAppWarning() {
            return this.mHarmfulAppWarning;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public int getInstallReason() {
            return this.mInstallReason;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public String getLastDisableAppCaller() {
            return this.mLastDisableAppCaller;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public OverlayPaths getOverlayPaths() {
            return this.mOverlayPaths;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public Map<String, OverlayPaths> getSharedLibraryOverlayPaths() {
            return this.mSharedLibraryOverlayPaths;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public int getUninstallReason() {
            return this.mUninstallReason;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public String getSplashScreenTheme() {
            return this.mSplashScreenTheme;
        }

        @Override // com.android.server.pm.pkg.PackageUserState
        public long getFirstInstallTimeMillis() {
            return this.mFirstInstallTimeMillis;
        }

        public UserStateImpl setBooleans(int i) {
            this.mBooleans = i;
            return this;
        }
    }

    public int getBooleans() {
        return this.mBooleans;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public AndroidPackage getAndroidPackage() {
        return this.mAndroidPackage;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String getPackageName() {
        return this.mPackageName;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String getVolumeUuid() {
        return this.mVolumeUuid;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getAppId() {
        return this.mAppId;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getCategoryOverride() {
        return this.mCategoryOverride;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String getCpuAbiOverride() {
        return this.mCpuAbiOverride;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getHiddenApiEnforcementPolicy() {
        return this.mHiddenApiEnforcementPolicy;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long getLastModifiedTime() {
        return this.mLastModifiedTime;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public long getLongVersionCode() {
        return this.mLongVersionCode;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public Map<String, Set<String>> getMimeGroups() {
        return this.mMimeGroups;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public File getPath() {
        return this.mPath;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String getPrimaryCpuAbi() {
        return this.mPrimaryCpuAbi;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String getSecondaryCpuAbi() {
        return this.mSecondaryCpuAbi;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String getSeInfo() {
        return this.mSeInfo;
    }

    public boolean isHasSharedUser() {
        return this.mHasSharedUser;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getSharedUserAppId() {
        return this.mSharedUserAppId;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String[] getUsesSdkLibraries() {
        return this.mUsesSdkLibraries;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long[] getUsesSdkLibrariesVersionsMajor() {
        return this.mUsesSdkLibrariesVersionsMajor;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String[] getUsesStaticLibraries() {
        return this.mUsesStaticLibraries;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long[] getUsesStaticLibrariesVersions() {
        return this.mUsesStaticLibrariesVersions;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public List<SharedLibrary> getSharedLibraryDependencies() {
        return this.mUsesLibraries;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public List<String> getUsesLibraryFiles() {
        return this.mUsesLibraryFiles;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long[] getLastPackageUsageTime() {
        return this.mLastPackageUsageTime;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public SigningInfo getSigningInfo() {
        return this.mSigningInfo;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public SparseArray<PackageUserState> getUserStates() {
        return this.mUserStates;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public String getApexModuleName() {
        return this.mApexModuleName;
    }

    public PackageStateImpl setBooleans(int i) {
        this.mBooleans = i;
        return this;
    }
}
