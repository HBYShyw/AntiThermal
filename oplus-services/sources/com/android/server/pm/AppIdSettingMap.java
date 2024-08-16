package com.android.server.pm;

import com.android.server.utils.SnapshotCache;
import com.android.server.utils.WatchedArrayList;
import com.android.server.utils.WatchedSparseArray;
import com.android.server.utils.Watcher;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AppIdSettingMap {
    private int mFirstAvailableAppId;
    private final WatchedArrayList<SettingBase> mNonSystemSettings;
    private final SnapshotCache<WatchedArrayList<SettingBase>> mNonSystemSettingsSnapshot;
    private final WatchedSparseArray<SettingBase> mSystemSettings;
    private final SnapshotCache<WatchedSparseArray<SettingBase>> mSystemSettingsSnapshot;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppIdSettingMap() {
        this.mFirstAvailableAppId = 10000;
        WatchedArrayList<SettingBase> watchedArrayList = new WatchedArrayList<>();
        this.mNonSystemSettings = watchedArrayList;
        this.mNonSystemSettingsSnapshot = new SnapshotCache.Auto(watchedArrayList, watchedArrayList, "AppIdSettingMap.mNonSystemSettings");
        WatchedSparseArray<SettingBase> watchedSparseArray = new WatchedSparseArray<>();
        this.mSystemSettings = watchedSparseArray;
        this.mSystemSettingsSnapshot = new SnapshotCache.Auto(watchedSparseArray, watchedSparseArray, "AppIdSettingMap.mSystemSettings");
    }

    AppIdSettingMap(AppIdSettingMap appIdSettingMap) {
        this.mFirstAvailableAppId = 10000;
        this.mNonSystemSettings = appIdSettingMap.mNonSystemSettingsSnapshot.snapshot();
        this.mNonSystemSettingsSnapshot = new SnapshotCache.Sealed();
        this.mSystemSettings = appIdSettingMap.mSystemSettingsSnapshot.snapshot();
        this.mSystemSettingsSnapshot = new SnapshotCache.Sealed();
    }

    public boolean registerExistingAppId(int i, SettingBase settingBase, Object obj) {
        if (i >= 10000) {
            int i2 = i - 10000;
            for (int size = this.mNonSystemSettings.size(); i2 >= size; size++) {
                this.mNonSystemSettings.add(null);
            }
            if (this.mNonSystemSettings.get(i2) != null) {
                PackageManagerService.reportSettingsProblem(5, "Adding duplicate app id: " + i + " name=" + obj);
                return false;
            }
            this.mNonSystemSettings.set(i2, settingBase);
            return true;
        }
        if (this.mSystemSettings.get(i) != null) {
            PackageManagerService.reportSettingsProblem(5, "Adding duplicate shared id: " + i + " name=" + obj);
            return false;
        }
        this.mSystemSettings.put(i, settingBase);
        return true;
    }

    public SettingBase getSetting(int i) {
        if (i >= 10000) {
            int i2 = i - 10000;
            if (i2 < this.mNonSystemSettings.size()) {
                return this.mNonSystemSettings.get(i2);
            }
            return null;
        }
        return this.mSystemSettings.get(i);
    }

    public void removeSetting(int i) {
        if (i >= 10000) {
            int i2 = i - 10000;
            if (i2 < this.mNonSystemSettings.size()) {
                this.mNonSystemSettings.set(i2, null);
            }
        } else {
            this.mSystemSettings.remove(i);
        }
        setFirstAvailableAppId(i + 1);
    }

    private void setFirstAvailableAppId(int i) {
        if (i > this.mFirstAvailableAppId) {
            this.mFirstAvailableAppId = i;
        }
    }

    public void replaceSetting(int i, SettingBase settingBase) {
        if (i >= 10000) {
            int i2 = i - 10000;
            if (i2 < this.mNonSystemSettings.size()) {
                this.mNonSystemSettings.set(i2, settingBase);
                return;
            }
            PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: calling replaceAppIdLpw to replace SettingBase at appId=" + i + " but nothing is replaced.");
            return;
        }
        this.mSystemSettings.put(i, settingBase);
    }

    public int acquireAndRegisterNewAppId(SettingBase settingBase) {
        int size = this.mNonSystemSettings.size();
        for (int i = this.mFirstAvailableAppId - 10000; i < size; i++) {
            if (this.mNonSystemSettings.get(i) == null) {
                this.mNonSystemSettings.set(i, settingBase);
                return i + 10000;
            }
        }
        if (size > 9999) {
            return -1;
        }
        this.mNonSystemSettings.add(settingBase);
        return size + 10000;
    }

    public AppIdSettingMap snapshot() {
        return new AppIdSettingMap(this);
    }

    public void registerObserver(Watcher watcher) {
        this.mNonSystemSettings.registerObserver(watcher);
        this.mSystemSettings.registerObserver(watcher);
    }
}
