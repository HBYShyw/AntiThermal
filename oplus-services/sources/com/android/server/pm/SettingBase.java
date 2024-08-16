package com.android.server.pm;

import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.permission.LegacyPermissionState;
import com.android.server.pm.pkg.mutate.PackageStateMutator;
import com.android.server.utils.Snappable;
import com.android.server.utils.Watchable;
import com.android.server.utils.WatchableImpl;
import com.android.server.utils.Watcher;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class SettingBase implements Watchable, Snappable {
    private int mPkgFlags;
    private int mPkgPrivateFlags;
    private final Watchable mWatchable = new WatchableImpl();

    @Deprecated
    protected final LegacyPermissionState mLegacyPermissionsState = new LegacyPermissionState();

    @Override // com.android.server.utils.Watchable
    public void registerObserver(Watcher watcher) {
        this.mWatchable.registerObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(Watcher watcher) {
        this.mWatchable.unregisterObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(Watcher watcher) {
        return this.mWatchable.isRegisteredObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(Watchable watchable) {
        this.mWatchable.dispatchChange(watchable);
    }

    public void onChanged() {
        PackageStateMutator.onPackageStateChanged();
        dispatchChange(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettingBase(int i, int i2) {
        setFlags(i);
        setPrivateFlags(i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettingBase(SettingBase settingBase) {
        if (settingBase != null) {
            copySettingBase(settingBase);
        }
    }

    public final void copySettingBase(SettingBase settingBase) {
        this.mPkgFlags = settingBase.mPkgFlags;
        this.mPkgPrivateFlags = settingBase.mPkgPrivateFlags;
        this.mLegacyPermissionsState.copyFrom(settingBase.mLegacyPermissionsState);
        onChanged();
    }

    @Deprecated
    public LegacyPermissionState getLegacyPermissionState() {
        return this.mLegacyPermissionsState;
    }

    public SettingBase setFlags(int i) {
        this.mPkgFlags = i;
        onChanged();
        return this;
    }

    public SettingBase setPrivateFlags(int i) {
        this.mPkgPrivateFlags = i;
        onChanged();
        return this;
    }

    public int getFlags() {
        return this.mPkgFlags;
    }

    public int getPrivateFlags() {
        return this.mPkgPrivateFlags;
    }
}
