package com.android.server.pm.pkg.mutate;

import android.content.ComponentName;
import android.content.pm.overlay.OverlayPaths;
import android.util.ArraySet;
import com.android.server.pm.PackageSetting;
import com.android.server.pm.pkg.PackageUserStateImpl;
import com.android.server.pm.pkg.SuspendParams;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageStateMutator {
    private static final AtomicLong sStateChangeSequence = new AtomicLong();
    private final Function<String, PackageSetting> mActiveStateFunction;
    private final Function<String, PackageSetting> mDisabledStateFunction;
    private final StateWriteWrapper mStateWrite = new StateWriteWrapper();
    private final ArraySet<PackageSetting> mChangedStates = new ArraySet<>();

    public PackageStateMutator(Function<String, PackageSetting> function, Function<String, PackageSetting> function2) {
        this.mActiveStateFunction = function;
        this.mDisabledStateFunction = function2;
    }

    public static void onPackageStateChanged() {
        sStateChangeSequence.incrementAndGet();
    }

    public PackageStateWrite forPackage(String str) {
        return setState(this.mActiveStateFunction.apply(str));
    }

    public PackageStateWrite forPackageNullable(String str) {
        PackageSetting apply = this.mActiveStateFunction.apply(str);
        setState(apply);
        if (apply == null) {
            return null;
        }
        return setState(apply);
    }

    public PackageStateWrite forDisabledSystemPackage(String str) {
        return setState(this.mDisabledStateFunction.apply(str));
    }

    public PackageStateWrite forDisabledSystemPackageNullable(String str) {
        PackageSetting apply = this.mDisabledStateFunction.apply(str);
        if (apply == null) {
            return null;
        }
        return setState(apply);
    }

    public InitialState initialState(int i) {
        return new InitialState(i, sStateChangeSequence.get());
    }

    public Result generateResult(InitialState initialState, int i) {
        if (initialState == null) {
            return Result.SUCCESS;
        }
        boolean z = i != initialState.mPackageSequence;
        boolean z2 = sStateChangeSequence.get() != initialState.mStateSequence;
        if (z && z2) {
            return Result.PACKAGES_AND_STATE_CHANGED;
        }
        if (z) {
            return Result.PACKAGES_CHANGED;
        }
        if (z2) {
            return Result.STATE_CHANGED;
        }
        return Result.SUCCESS;
    }

    public void onFinished() {
        for (int i = 0; i < this.mChangedStates.size(); i++) {
            this.mChangedStates.valueAt(i).onChanged();
        }
    }

    private StateWriteWrapper setState(PackageSetting packageSetting) {
        if (packageSetting != null) {
            this.mChangedStates.add(packageSetting);
        }
        return this.mStateWrite.setState(packageSetting);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class InitialState {
        private final int mPackageSequence;
        private final long mStateSequence;

        public InitialState(int i, long j) {
            this.mPackageSequence = i;
            this.mStateSequence = j;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Result {
        private final boolean mCommitted;
        private final boolean mPackagesChanged;
        private final boolean mSpecificPackageNull;
        private final boolean mStateChanged;
        public static final Result SUCCESS = new Result(true, false, false, false);
        public static final Result PACKAGES_CHANGED = new Result(false, true, false, false);
        public static final Result STATE_CHANGED = new Result(false, false, true, false);
        public static final Result PACKAGES_AND_STATE_CHANGED = new Result(false, true, true, false);
        public static final Result SPECIFIC_PACKAGE_NULL = new Result(false, false, true, true);

        public Result(boolean z, boolean z2, boolean z3, boolean z4) {
            this.mCommitted = z;
            this.mPackagesChanged = z2;
            this.mStateChanged = z3;
            this.mSpecificPackageNull = z4;
        }

        public boolean isCommitted() {
            return this.mCommitted;
        }

        public boolean isPackagesChanged() {
            return this.mPackagesChanged;
        }

        public boolean isStateChanged() {
            return this.mStateChanged;
        }

        public boolean isSpecificPackageNull() {
            return this.mSpecificPackageNull;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class StateWriteWrapper implements PackageStateWrite {
        private PackageSetting mState;
        private final UserStateWriteWrapper mUserStateWrite;

        private StateWriteWrapper() {
            this.mUserStateWrite = new UserStateWriteWrapper();
        }

        public StateWriteWrapper setState(PackageSetting packageSetting) {
            this.mState = packageSetting;
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageUserStateWrite userState(int i) {
            PackageSetting packageSetting = this.mState;
            PackageUserStateImpl orCreateUserState = packageSetting == null ? null : packageSetting.getOrCreateUserState(i);
            if (orCreateUserState != null) {
                orCreateUserState.setWatchable(this.mState);
            }
            return this.mUserStateWrite.setStates(orCreateUserState);
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public void onChanged() {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.onChanged();
            }
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setLastPackageUsageTime(int i, long j) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.getTransientState().setLastPackageUsageTimeInMills(i, j);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setHiddenUntilInstalled(boolean z) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.getTransientState().setHiddenUntilInstalled(z);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setRequiredForSystemUser(boolean z) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                if (z) {
                    packageSetting.setPrivateFlags(packageSetting.getPrivateFlags() | 512);
                } else {
                    packageSetting.setPrivateFlags(packageSetting.getPrivateFlags() & (-513));
                }
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setMimeGroup(String str, ArraySet<String> arraySet) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.setMimeGroup(str, arraySet);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setCategoryOverride(int i) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.setCategoryOverride(i);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setUpdateAvailable(boolean z) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.setUpdateAvailable(z);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setLoadingProgress(float f) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.setLoadingProgress(f);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setLoadingCompletedTime(long j) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.setLoadingCompletedTime(j);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setOverrideSeInfo(String str) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.getTransientState().setOverrideSeInfo(str);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setInstaller(String str, int i) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.setInstallerPackage(str, i);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public PackageStateWrite setUpdateOwner(String str) {
            PackageSetting packageSetting = this.mState;
            if (packageSetting != null) {
                packageSetting.setUpdateOwnerPackage(str);
            }
            return this;
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        private static class UserStateWriteWrapper implements PackageUserStateWrite {
            private PackageUserStateImpl mUserState;

            private UserStateWriteWrapper() {
            }

            public UserStateWriteWrapper setStates(PackageUserStateImpl packageUserStateImpl) {
                this.mUserState = packageUserStateImpl;
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setInstalled(boolean z) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setInstalled(z);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setUninstallReason(int i) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setUninstallReason(i);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setDistractionFlags(int i) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setDistractionFlags(i);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite putSuspendParams(String str, SuspendParams suspendParams) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.putSuspendParams(str, suspendParams);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite removeSuspension(String str) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.removeSuspension(str);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setHidden(boolean z) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setHidden(z);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setStopped(boolean z) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setStopped(z);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setNotLaunched(boolean z) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setNotLaunched(z);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setOverlayPaths(OverlayPaths overlayPaths) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setOverlayPaths(overlayPaths);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setOverlayPathsForLibrary(String str, OverlayPaths overlayPaths) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setSharedLibraryOverlayPaths(str, overlayPaths);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setHarmfulAppWarning(String str) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setHarmfulAppWarning(str);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setSplashScreenTheme(String str) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl != null) {
                    packageUserStateImpl.setSplashScreenTheme(str);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public PackageUserStateWrite setComponentLabelIcon(ComponentName componentName, String str, Integer num) {
                PackageUserStateImpl packageUserStateImpl = this.mUserState;
                if (packageUserStateImpl == null) {
                    return null;
                }
                packageUserStateImpl.overrideLabelAndIcon(componentName, str, num);
                return null;
            }
        }
    }
}
