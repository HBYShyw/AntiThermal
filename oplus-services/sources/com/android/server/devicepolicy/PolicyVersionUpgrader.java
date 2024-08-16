package com.android.server.devicepolicy;

import android.content.ComponentName;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.JournaledFile;
import com.android.server.devicepolicy.OwnersData;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PolicyVersionUpgrader {
    private static final String LOG_TAG = "DevicePolicyManager";
    private static final boolean VERBOSE_LOG = false;
    private final PolicyPathProvider mPathProvider;
    private final PolicyUpgraderDataProvider mProvider;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public PolicyVersionUpgrader(PolicyUpgraderDataProvider policyUpgraderDataProvider, PolicyPathProvider policyPathProvider) {
        this.mProvider = policyUpgraderDataProvider;
        this.mPathProvider = policyPathProvider;
    }

    public void upgradePolicy(int i) {
        int readVersion = readVersion();
        if (readVersion >= i) {
            Slog.i(LOG_TAG, String.format("Current version %d, latest version %d, not upgrading.", Integer.valueOf(readVersion), Integer.valueOf(i)));
            return;
        }
        int[] usersForUpgrade = this.mProvider.getUsersForUpgrade();
        OwnersData loadOwners = loadOwners(usersForUpgrade);
        SparseArray<DevicePolicyData> loadAllUsersData = loadAllUsersData(usersForUpgrade, readVersion, loadOwners);
        if (readVersion == 0) {
            Slog.i(LOG_TAG, String.format("Upgrading from version %d", Integer.valueOf(readVersion)));
            readVersion = 1;
        }
        if (readVersion == 1) {
            Slog.i(LOG_TAG, String.format("Upgrading from version %d", Integer.valueOf(readVersion)));
            upgradeSensorPermissionsAccess(usersForUpgrade, loadOwners, loadAllUsersData);
            readVersion = 2;
        }
        if (readVersion == 2) {
            Slog.i(LOG_TAG, String.format("Upgrading from version %d", Integer.valueOf(readVersion)));
            upgradeProtectedPackages(loadOwners, loadAllUsersData);
            readVersion = 3;
        }
        if (readVersion == 3) {
            Slog.i(LOG_TAG, String.format("Upgrading from version %d", Integer.valueOf(readVersion)));
            upgradePackageSuspension(usersForUpgrade, loadOwners, loadAllUsersData);
            readVersion = 4;
        }
        if (readVersion == 4) {
            Slog.i(LOG_TAG, String.format("Upgrading from version %d", Integer.valueOf(readVersion)));
            initializeEffectiveKeepProfilesRunning(loadAllUsersData);
            readVersion = 5;
        }
        if (readVersion == 5) {
            Slog.i(LOG_TAG, String.format("Upgrading from version %d", Integer.valueOf(readVersion)));
            readVersion = 6;
        }
        writePoliciesAndVersion(usersForUpgrade, loadAllUsersData, loadOwners, readVersion);
    }

    private void upgradeSensorPermissionsAccess(int[] iArr, OwnersData ownersData, SparseArray<DevicePolicyData> sparseArray) {
        OwnersData.OwnerInfo ownerInfo;
        for (int i : iArr) {
            DevicePolicyData devicePolicyData = sparseArray.get(i);
            if (devicePolicyData != null) {
                Iterator<ActiveAdmin> it = devicePolicyData.mAdminList.iterator();
                while (it.hasNext()) {
                    ActiveAdmin next = it.next();
                    if (ownersData.mDeviceOwnerUserId == i && (ownerInfo = ownersData.mDeviceOwner) != null && ownerInfo.admin.equals(next.info.getComponent())) {
                        Slog.i(LOG_TAG, String.format("Marking Device Owner in user %d for permission grant ", Integer.valueOf(i)));
                        next.mAdminCanGrantSensorsPermissions = true;
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0052  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void upgradeProtectedPackages(OwnersData ownersData, SparseArray<DevicePolicyData> sparseArray) {
        List<String> list;
        ActiveAdmin activeAdmin;
        if (ownersData.mDeviceOwner == null) {
            return;
        }
        DevicePolicyData devicePolicyData = sparseArray.get(ownersData.mDeviceOwnerUserId);
        if (devicePolicyData == null) {
            Slog.e(LOG_TAG, "No policy data for do user");
            return;
        }
        ArrayMap<String, List<String>> arrayMap = ownersData.mDeviceOwnerProtectedPackages;
        List<String> list2 = null;
        if (arrayMap != null) {
            list = arrayMap.get(ownersData.mDeviceOwner.packageName);
            if (list != null) {
                Slog.i(LOG_TAG, "Found protected packages in Owners");
            }
            ownersData.mDeviceOwnerProtectedPackages = null;
        } else {
            if (devicePolicyData.mUserControlDisabledPackages != null) {
                Slog.i(LOG_TAG, "Found protected packages in DevicePolicyData");
                list = devicePolicyData.mUserControlDisabledPackages;
                devicePolicyData.mUserControlDisabledPackages = null;
            }
            activeAdmin = devicePolicyData.mAdminMap.get(ownersData.mDeviceOwner.admin);
            if (activeAdmin != null) {
                Slog.e(LOG_TAG, "DO admin not found in DO user");
                return;
            } else {
                if (list2 != null) {
                    activeAdmin.protectedPackages = new ArrayList(list2);
                    return;
                }
                return;
            }
        }
        list2 = list;
        activeAdmin = devicePolicyData.mAdminMap.get(ownersData.mDeviceOwner.admin);
        if (activeAdmin != null) {
        }
    }

    private void upgradePackageSuspension(int[] iArr, OwnersData ownersData, SparseArray<DevicePolicyData> sparseArray) {
        OwnersData.OwnerInfo ownerInfo = ownersData.mDeviceOwner;
        if (ownerInfo != null) {
            saveSuspendedPackages(sparseArray, ownersData.mDeviceOwnerUserId, ownerInfo.admin);
        }
        for (int i = 0; i < ownersData.mProfileOwners.size(); i++) {
            saveSuspendedPackages(sparseArray, ownersData.mProfileOwners.keyAt(i).intValue(), ownersData.mProfileOwners.valueAt(i).admin);
        }
    }

    private void saveSuspendedPackages(SparseArray<DevicePolicyData> sparseArray, int i, ComponentName componentName) {
        DevicePolicyData devicePolicyData = sparseArray.get(i);
        if (devicePolicyData == null) {
            Slog.e(LOG_TAG, "No policy data for owner user, cannot migrate suspended packages");
            return;
        }
        ActiveAdmin activeAdmin = devicePolicyData.mAdminMap.get(componentName);
        if (activeAdmin == null) {
            Slog.e(LOG_TAG, "No admin for owner, cannot migrate suspended packages");
            return;
        }
        List<String> platformSuspendedPackages = this.mProvider.getPlatformSuspendedPackages(i);
        activeAdmin.suspendedPackages = platformSuspendedPackages;
        Slog.i(LOG_TAG, String.format("Saved %d packages suspended by %s in user %d", Integer.valueOf(platformSuspendedPackages.size()), componentName, Integer.valueOf(i)));
    }

    private void initializeEffectiveKeepProfilesRunning(SparseArray<DevicePolicyData> sparseArray) {
        DevicePolicyData devicePolicyData = sparseArray.get(0);
        if (devicePolicyData == null) {
            return;
        }
        devicePolicyData.mEffectiveKeepProfilesRunning = false;
        Slog.i(LOG_TAG, "Keep profile running effective state set to false");
    }

    private OwnersData loadOwners(int[] iArr) {
        OwnersData ownersData = new OwnersData(this.mPathProvider);
        ownersData.load(iArr);
        return ownersData;
    }

    private void writePoliciesAndVersion(int[] iArr, SparseArray<DevicePolicyData> sparseArray, OwnersData ownersData, int i) {
        boolean z = true;
        for (int i2 : iArr) {
            z = z && writeDataForUser(i2, sparseArray.get(i2));
        }
        boolean z2 = z && ownersData.writeDeviceOwner();
        for (int i3 : iArr) {
            z2 = z2 && ownersData.writeProfileOwner(i3);
        }
        if (z2) {
            writeVersion(i);
        } else {
            Slog.e(LOG_TAG, String.format("Error: Failed upgrading policies to version %d", Integer.valueOf(i)));
        }
    }

    private SparseArray<DevicePolicyData> loadAllUsersData(int[] iArr, int i, OwnersData ownersData) {
        SparseArray<DevicePolicyData> sparseArray = new SparseArray<>();
        for (int i2 : iArr) {
            sparseArray.append(i2, loadDataForUser(i2, i, getOwnerForUser(ownersData, i2)));
        }
        return sparseArray;
    }

    private ComponentName getOwnerForUser(OwnersData ownersData, int i) {
        OwnersData.OwnerInfo ownerInfo;
        if (ownersData.mDeviceOwnerUserId == i && (ownerInfo = ownersData.mDeviceOwner) != null) {
            return ownerInfo.admin;
        }
        if (ownersData.mProfileOwners.containsKey(Integer.valueOf(i))) {
            return ownersData.mProfileOwners.get(Integer.valueOf(i)).admin;
        }
        return null;
    }

    private DevicePolicyData loadDataForUser(int i, int i2, ComponentName componentName) {
        DevicePolicyData devicePolicyData = new DevicePolicyData(i);
        if (i2 == 5 && i == 0) {
            devicePolicyData.mEffectiveKeepProfilesRunning = true;
        }
        DevicePolicyData.load(devicePolicyData, this.mProvider.makeDevicePoliciesJournaledFile(i), this.mProvider.getAdminInfoSupplier(i), componentName);
        return devicePolicyData;
    }

    private boolean writeDataForUser(int i, DevicePolicyData devicePolicyData) {
        return DevicePolicyData.store(devicePolicyData, this.mProvider.makeDevicePoliciesJournaledFile(i));
    }

    private JournaledFile getVersionFile() {
        return this.mProvider.makePoliciesVersionJournaledFile(0);
    }

    private int readVersion() {
        try {
            return Integer.parseInt(Files.readAllLines(getVersionFile().chooseForRead().toPath(), Charset.defaultCharset()).get(0));
        } catch (IOException | IndexOutOfBoundsException | NumberFormatException e) {
            Slog.e(LOG_TAG, "Error reading version", e);
            return 0;
        }
    }

    private void writeVersion(int i) {
        JournaledFile versionFile = getVersionFile();
        try {
            Files.write(versionFile.chooseForWrite().toPath(), String.format("%d", Integer.valueOf(i)).getBytes(), new OpenOption[0]);
            versionFile.commit();
        } catch (IOException e) {
            Slog.e(LOG_TAG, String.format("Writing version %d failed", Integer.valueOf(i)), e);
            versionFile.rollback();
        }
    }
}
