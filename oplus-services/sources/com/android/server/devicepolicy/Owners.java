package com.android.server.devicepolicy;

import android.app.ActivityManagerInternal;
import android.app.AppOpsManagerInternal;
import android.app.admin.SystemUpdateInfo;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.pm.PackageManagerInternal;
import android.content.pm.UserInfo;
import android.os.Binder;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.devicepolicy.OwnersData;
import com.android.server.pm.UserManagerInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToIntFunction;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Owners {
    private static final boolean DEBUG = false;
    private static final String TAG = "DevicePolicyManagerService";
    private final ActivityManagerInternal mActivityManagerInternal;
    private final ActivityTaskManagerInternal mActivityTaskManagerInternal;

    @GuardedBy({"mData"})
    private final OwnersData mData;
    private final DeviceStateCacheImpl mDeviceStateCache;
    private final PackageManagerInternal mPackageManagerInternal;
    private boolean mSystemReady;
    private final UserManager mUserManager;
    private final UserManagerInternal mUserManagerInternal;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public Owners(UserManager userManager, UserManagerInternal userManagerInternal, PackageManagerInternal packageManagerInternal, ActivityTaskManagerInternal activityTaskManagerInternal, ActivityManagerInternal activityManagerInternal, DeviceStateCacheImpl deviceStateCacheImpl, PolicyPathProvider policyPathProvider) {
        this.mUserManager = userManager;
        this.mUserManagerInternal = userManagerInternal;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mActivityTaskManagerInternal = activityTaskManagerInternal;
        this.mActivityManagerInternal = activityManagerInternal;
        this.mDeviceStateCache = deviceStateCacheImpl;
        this.mData = new OwnersData(policyPathProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void load() {
        synchronized (this.mData) {
            int[] array = this.mUserManager.getAliveUsers().stream().mapToInt(new ToIntFunction() { // from class: com.android.server.devicepolicy.Owners$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    int i;
                    i = ((UserInfo) obj).id;
                    return i;
                }
            }).toArray();
            this.mData.load(array);
            int i = 0;
            if (DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                if (hasDeviceOwner()) {
                    OwnersData ownersData = this.mData;
                    this.mDeviceStateCache.setDeviceOwnerType(ownersData.mDeviceOwnerTypes.getOrDefault(ownersData.mDeviceOwner.packageName, 0).intValue());
                } else {
                    this.mDeviceStateCache.setDeviceOwnerType(-1);
                }
                int length = array.length;
                while (i < length) {
                    int i2 = array[i];
                    this.mDeviceStateCache.setHasProfileOwner(i2, hasProfileOwner(i2));
                    i++;
                }
            } else {
                this.mUserManagerInternal.setDeviceManaged(hasDeviceOwner());
                int length2 = array.length;
                while (i < length2) {
                    int i3 = array[i];
                    this.mUserManagerInternal.setUserManaged(i3, hasProfileOwner(i3));
                    i++;
                }
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    @GuardedBy({"mData"})
    private void notifyChangeLocked() {
        pushToDevicePolicyManager();
        pushToPackageManagerLocked();
        pushToActivityManagerLocked();
        pushToAppOpsLocked();
    }

    private void pushToDevicePolicyManager() {
        DevicePolicyManagerService.invalidateBinderCaches();
    }

    @GuardedBy({"mData"})
    private void pushToPackageManagerLocked() {
        SparseArray<String> sparseArray = new SparseArray<>();
        for (int size = this.mData.mProfileOwners.size() - 1; size >= 0; size--) {
            sparseArray.put(this.mData.mProfileOwners.keyAt(size).intValue(), this.mData.mProfileOwners.valueAt(size).packageName);
        }
        OwnersData ownersData = this.mData;
        OwnersData.OwnerInfo ownerInfo = ownersData.mDeviceOwner;
        this.mPackageManagerInternal.setDeviceAndProfileOwnerPackages(ownersData.mDeviceOwnerUserId, ownerInfo != null ? ownerInfo.packageName : null, sparseArray);
    }

    @GuardedBy({"mData"})
    private void pushDeviceOwnerUidToActivityTaskManagerLocked() {
        this.mActivityTaskManagerInternal.setDeviceOwnerUid(getDeviceOwnerUidLocked());
    }

    @GuardedBy({"mData"})
    private void pushProfileOwnerUidsToActivityTaskManagerLocked() {
        this.mActivityTaskManagerInternal.setProfileOwnerUids(getProfileOwnerUidsLocked());
    }

    @GuardedBy({"mData"})
    private void pushToActivityManagerLocked() {
        this.mActivityManagerInternal.setDeviceOwnerUid(getDeviceOwnerUidLocked());
        ArraySet arraySet = new ArraySet();
        for (int size = this.mData.mProfileOwners.size() - 1; size >= 0; size--) {
            int packageUid = this.mPackageManagerInternal.getPackageUid(this.mData.mProfileOwners.valueAt(size).packageName, 4333568L, this.mData.mProfileOwners.keyAt(size).intValue());
            if (packageUid >= 0) {
                arraySet.add(Integer.valueOf(packageUid));
            }
        }
        this.mActivityManagerInternal.setProfileOwnerUid(arraySet);
    }

    @GuardedBy({"mData"})
    int getDeviceOwnerUidLocked() {
        OwnersData ownersData = this.mData;
        OwnersData.OwnerInfo ownerInfo = ownersData.mDeviceOwner;
        if (ownerInfo != null) {
            return this.mPackageManagerInternal.getPackageUid(ownerInfo.packageName, 4333568L, ownersData.mDeviceOwnerUserId);
        }
        return -1;
    }

    @GuardedBy({"mData"})
    Set<Integer> getProfileOwnerUidsLocked() {
        ArraySet arraySet = new ArraySet();
        for (int i = 0; i < this.mData.mProfileOwners.size(); i++) {
            arraySet.add(Integer.valueOf(this.mPackageManagerInternal.getPackageUid(this.mData.mProfileOwners.valueAt(i).packageName, 4333568L, this.mData.mProfileOwners.keyAt(i).intValue())));
        }
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDeviceOwnerPackageName() {
        String str;
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mDeviceOwner;
            str = ownerInfo != null ? ownerInfo.packageName : null;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDeviceOwnerUserId() {
        int i;
        synchronized (this.mData) {
            i = this.mData.mDeviceOwnerUserId;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<Integer, ComponentName> getDeviceOwnerUserIdAndComponent() {
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            if (ownersData.mDeviceOwner == null) {
                return null;
            }
            return Pair.create(Integer.valueOf(ownersData.mDeviceOwnerUserId), this.mData.mDeviceOwner.admin);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComponentName getDeviceOwnerComponent() {
        ComponentName componentName;
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mDeviceOwner;
            componentName = ownerInfo != null ? ownerInfo.admin : null;
        }
        return componentName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDeviceOwnerRemoteBugreportUri() {
        String str;
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mDeviceOwner;
            str = ownerInfo != null ? ownerInfo.remoteBugreportUri : null;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDeviceOwnerRemoteBugreportHash() {
        String str;
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mDeviceOwner;
            str = ownerInfo != null ? ownerInfo.remoteBugreportHash : null;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceOwner(ComponentName componentName, int i) {
        if (i < 0) {
            Slog.e(TAG, "Invalid user id for device owner user: " + i);
            return;
        }
        synchronized (this.mData) {
            this.mData.mDeviceOwner = new OwnersData.OwnerInfo(componentName, null, null, true);
            this.mData.mDeviceOwnerUserId = i;
            if (DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                OwnersData ownersData = this.mData;
                this.mDeviceStateCache.setDeviceOwnerType(ownersData.mDeviceOwnerTypes.getOrDefault(ownersData.mDeviceOwner.packageName, 0).intValue());
            } else {
                this.mUserManagerInternal.setDeviceManaged(true);
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearDeviceOwner() {
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            ownersData.mDeviceOwnerTypes.remove(ownersData.mDeviceOwner.packageName);
            OwnersData ownersData2 = this.mData;
            ownersData2.mDeviceOwner = null;
            ownersData2.mDeviceOwnerUserId = -10000;
            if (DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                this.mDeviceStateCache.setDeviceOwnerType(-1);
            } else {
                this.mUserManagerInternal.setDeviceManaged(false);
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setProfileOwner(ComponentName componentName, int i) {
        synchronized (this.mData) {
            this.mData.mProfileOwners.put(Integer.valueOf(i), new OwnersData.OwnerInfo(componentName, null, null, false));
            if (DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                this.mDeviceStateCache.setHasProfileOwner(i, true);
            } else {
                this.mUserManagerInternal.setUserManaged(i, true);
            }
            notifyChangeLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeProfileOwner(int i) {
        synchronized (this.mData) {
            this.mData.mProfileOwners.remove(Integer.valueOf(i));
            if (DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                this.mDeviceStateCache.setHasProfileOwner(i, false);
            } else {
                this.mUserManagerInternal.setUserManaged(i, false);
            }
            notifyChangeLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void transferProfileOwner(ComponentName componentName, int i) {
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mProfileOwners.get(Integer.valueOf(i));
            this.mData.mProfileOwners.put(Integer.valueOf(i), new OwnersData.OwnerInfo(componentName, ownerInfo.remoteBugreportUri, ownerInfo.remoteBugreportHash, ownerInfo.isOrganizationOwnedDevice));
            notifyChangeLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void transferDeviceOwnership(ComponentName componentName) {
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            Integer remove = ownersData.mDeviceOwnerTypes.remove(ownersData.mDeviceOwner.packageName);
            OwnersData ownersData2 = this.mData;
            OwnersData.OwnerInfo ownerInfo = ownersData2.mDeviceOwner;
            ownersData2.mDeviceOwner = new OwnersData.OwnerInfo(componentName, ownerInfo.remoteBugreportUri, ownerInfo.remoteBugreportHash, ownerInfo.isOrganizationOwnedDevice);
            if (remove != null) {
                OwnersData ownersData3 = this.mData;
                ownersData3.mDeviceOwnerTypes.put(ownersData3.mDeviceOwner.packageName, remove);
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComponentName getProfileOwnerComponent(int i) {
        ComponentName componentName;
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mProfileOwners.get(Integer.valueOf(i));
            componentName = ownerInfo != null ? ownerInfo.admin : null;
        }
        return componentName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getProfileOwnerPackage(int i) {
        String str;
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mProfileOwners.get(Integer.valueOf(i));
            str = ownerInfo != null ? ownerInfo.packageName : null;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isProfileOwnerOfOrganizationOwnedDevice(int i) {
        boolean z;
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mProfileOwners.get(Integer.valueOf(i));
            z = ownerInfo != null ? ownerInfo.isOrganizationOwnedDevice : false;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<Integer> getProfileOwnerKeys() {
        Set<Integer> keySet;
        synchronized (this.mData) {
            keySet = this.mData.mProfileOwners.keySet();
        }
        return keySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<OwnerShellData> listAllOwners() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            OwnersData.OwnerInfo ownerInfo = ownersData.mDeviceOwner;
            if (ownerInfo != null) {
                arrayList.add(OwnerShellData.forDeviceOwner(ownersData.mDeviceOwnerUserId, ownerInfo.admin));
            }
            for (int i = 0; i < this.mData.mProfileOwners.size(); i++) {
                arrayList.add(OwnerShellData.forUserProfileOwner(this.mData.mProfileOwners.keyAt(i).intValue(), this.mData.mProfileOwners.valueAt(i).admin));
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemUpdatePolicy getSystemUpdatePolicy() {
        SystemUpdatePolicy systemUpdatePolicy;
        synchronized (this.mData) {
            systemUpdatePolicy = this.mData.mSystemUpdatePolicy;
        }
        return systemUpdatePolicy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSystemUpdatePolicy(SystemUpdatePolicy systemUpdatePolicy) {
        synchronized (this.mData) {
            this.mData.mSystemUpdatePolicy = systemUpdatePolicy;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearSystemUpdatePolicy() {
        synchronized (this.mData) {
            this.mData.mSystemUpdatePolicy = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<LocalDate, LocalDate> getSystemUpdateFreezePeriodRecord() {
        Pair<LocalDate, LocalDate> pair;
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            pair = new Pair<>(ownersData.mSystemUpdateFreezeStart, ownersData.mSystemUpdateFreezeEnd);
        }
        return pair;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSystemUpdateFreezePeriodRecordAsString() {
        String systemUpdateFreezePeriodRecordAsString;
        synchronized (this.mData) {
            systemUpdateFreezePeriodRecordAsString = this.mData.getSystemUpdateFreezePeriodRecordAsString();
        }
        return systemUpdateFreezePeriodRecordAsString;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setSystemUpdateFreezePeriodRecord(LocalDate localDate, LocalDate localDate2) {
        boolean z;
        boolean z2;
        synchronized (this.mData) {
            z = true;
            if (Objects.equals(this.mData.mSystemUpdateFreezeStart, localDate)) {
                z2 = false;
            } else {
                this.mData.mSystemUpdateFreezeStart = localDate;
                z2 = true;
            }
            if (Objects.equals(this.mData.mSystemUpdateFreezeEnd, localDate2)) {
                z = z2;
            } else {
                this.mData.mSystemUpdateFreezeEnd = localDate2;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasDeviceOwner() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mDeviceOwner != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDeviceOwnerUserId(int i) {
        boolean z;
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            z = ownersData.mDeviceOwner != null && ownersData.mDeviceOwnerUserId == i;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDefaultDeviceOwnerUserId(int i) {
        boolean z;
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            z = ownersData.mDeviceOwner != null && ownersData.mDeviceOwnerUserId == i && getDeviceOwnerType(getDeviceOwnerPackageName()) == 0;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFinancedDeviceOwnerUserId(int i) {
        boolean z;
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            if (ownersData.mDeviceOwner != null && ownersData.mDeviceOwnerUserId == i) {
                z = true;
                if (getDeviceOwnerType(getDeviceOwnerPackageName()) == 1) {
                }
            }
            z = false;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasProfileOwner(int i) {
        boolean z;
        synchronized (this.mData) {
            z = getProfileOwnerComponent(i) != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceOwnerRemoteBugreportUriAndHash(String str, String str2) {
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mDeviceOwner;
            if (ownerInfo != null) {
                ownerInfo.remoteBugreportUri = str;
                ownerInfo.remoteBugreportHash = str2;
            }
            writeDeviceOwner();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setProfileOwnerOfOrganizationOwnedDevice(int i, boolean z) {
        synchronized (this.mData) {
            OwnersData.OwnerInfo ownerInfo = this.mData.mProfileOwners.get(Integer.valueOf(i));
            if (ownerInfo != null) {
                ownerInfo.isOrganizationOwnedDevice = z;
            } else {
                Slog.e(TAG, String.format("No profile owner for user %d to set org-owned flag.", Integer.valueOf(i)));
            }
            writeProfileOwner(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceOwnerType(String str, int i, boolean z) {
        synchronized (this.mData) {
            if (!hasDeviceOwner()) {
                Slog.e(TAG, "Attempting to set a device owner type when there is no device owner");
            } else if (!z && isDeviceOwnerTypeSetForDeviceOwner(str)) {
                Slog.e(TAG, "Setting the device owner type more than once is only allowed for test only admins");
            } else {
                this.mData.mDeviceOwnerTypes.put(str, Integer.valueOf(i));
                writeDeviceOwner();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDeviceOwnerType(String str) {
        synchronized (this.mData) {
            if (!isDeviceOwnerTypeSetForDeviceOwner(str)) {
                return 0;
            }
            return this.mData.mDeviceOwnerTypes.get(str).intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDeviceOwnerTypeSetForDeviceOwner(String str) {
        boolean z;
        synchronized (this.mData) {
            z = !this.mData.mDeviceOwnerTypes.isEmpty() && this.mData.mDeviceOwnerTypes.containsKey(str);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeDeviceOwner() {
        synchronized (this.mData) {
            pushToDevicePolicyManager();
            this.mData.writeDeviceOwner();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeProfileOwner(int i) {
        synchronized (this.mData) {
            pushToDevicePolicyManager();
            this.mData.writeProfileOwner(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean saveSystemUpdateInfo(SystemUpdateInfo systemUpdateInfo) {
        synchronized (this.mData) {
            if (Objects.equals(systemUpdateInfo, this.mData.mSystemUpdateInfo)) {
                return false;
            }
            OwnersData ownersData = this.mData;
            ownersData.mSystemUpdateInfo = systemUpdateInfo;
            ownersData.writeDeviceOwner();
            return true;
        }
    }

    public SystemUpdateInfo getSystemUpdateInfo() {
        SystemUpdateInfo systemUpdateInfo;
        synchronized (this.mData) {
            systemUpdateInfo = this.mData.mSystemUpdateInfo;
        }
        return systemUpdateInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markMigrationToPolicyEngine() {
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            ownersData.mMigratedToPolicyEngine = true;
            ownersData.writeDeviceOwner();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMigratedToPolicyEngine() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mMigratedToPolicyEngine;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markPostUpgradeMigration() {
        synchronized (this.mData) {
            OwnersData ownersData = this.mData;
            ownersData.mPoliciesMigratedPostUpdate = true;
            ownersData.writeDeviceOwner();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMigratedPostUpdate() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mPoliciesMigratedPostUpdate;
        }
        return z;
    }

    @GuardedBy({"mData"})
    void pushToAppOpsLocked() {
        int deviceOwnerUidLocked;
        if (this.mSystemReady) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SparseIntArray sparseIntArray = new SparseIntArray();
                if (this.mData.mDeviceOwner != null && (deviceOwnerUidLocked = getDeviceOwnerUidLocked()) >= 0) {
                    sparseIntArray.put(this.mData.mDeviceOwnerUserId, deviceOwnerUidLocked);
                }
                ArrayMap<Integer, OwnersData.OwnerInfo> arrayMap = this.mData.mProfileOwners;
                if (arrayMap != null) {
                    for (int size = arrayMap.size() - 1; size >= 0; size--) {
                        int packageUid = this.mPackageManagerInternal.getPackageUid(this.mData.mProfileOwners.valueAt(size).packageName, 4333568L, this.mData.mProfileOwners.keyAt(size).intValue());
                        if (packageUid >= 0) {
                            sparseIntArray.put(this.mData.mProfileOwners.keyAt(size).intValue(), packageUid);
                        }
                    }
                }
                AppOpsManagerInternal appOpsManagerInternal = (AppOpsManagerInternal) LocalServices.getService(AppOpsManagerInternal.class);
                if (appOpsManagerInternal != null) {
                    if (sparseIntArray.size() <= 0) {
                        sparseIntArray = null;
                    }
                    appOpsManagerInternal.setDeviceAndProfileOwners(sparseIntArray);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void systemReady() {
        synchronized (this.mData) {
            this.mSystemReady = true;
            pushToActivityManagerLocked();
            pushToAppOpsLocked();
        }
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mData) {
            this.mData.dump(indentingPrintWriter);
        }
    }

    @VisibleForTesting
    File getDeviceOwnerFile() {
        return this.mData.getDeviceOwnerFile();
    }

    @VisibleForTesting
    File getProfileOwnerFile(int i) {
        return this.mData.getProfileOwnerFile(i);
    }
}
