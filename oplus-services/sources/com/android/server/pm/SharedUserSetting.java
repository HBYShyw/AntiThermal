package com.android.server.pm;

import android.content.pm.SigningDetails;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.ArrayUtils;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.permission.LegacyPermissionState;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.SharedUserApi;
import com.android.server.pm.pkg.component.ComponentMutateUtils;
import com.android.server.pm.pkg.component.ParsedProcess;
import com.android.server.pm.pkg.component.ParsedProcessImpl;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.utils.SnapshotCache;
import com.android.server.utils.Watchable;
import com.android.server.utils.WatchedArraySet;
import com.android.server.utils.Watcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import libcore.util.EmptyArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SharedUserSetting extends SettingBase implements SharedUserApi {
    int mAppId;
    final WatchedArraySet<PackageSetting> mDisabledPackages;
    private final SnapshotCache<WatchedArraySet<PackageSetting>> mDisabledPackagesSnapshot;
    private final Watcher mObserver;
    private final WatchedArraySet<PackageSetting> mPackages;
    private final SnapshotCache<WatchedArraySet<PackageSetting>> mPackagesSnapshot;
    private final SnapshotCache<SharedUserSetting> mSnapshot;
    final String name;
    final ArrayMap<String, ParsedProcess> processes;
    int seInfoTargetSdkVersion;
    final PackageSignatures signatures;
    Boolean signaturesChanged;
    int uidFlags;
    int uidPrivateFlags;

    private SnapshotCache<SharedUserSetting> makeCache() {
        return new SnapshotCache<SharedUserSetting>(this, this) { // from class: com.android.server.pm.SharedUserSetting.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public SharedUserSetting createSnapshot() {
                return new SharedUserSetting();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SharedUserSetting(String str, int i, int i2) {
        super(i, i2);
        this.mObserver = new Watcher() { // from class: com.android.server.pm.SharedUserSetting.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                SharedUserSetting.this.onChanged();
            }
        };
        this.signatures = new PackageSignatures();
        this.uidFlags = i;
        this.uidPrivateFlags = i2;
        this.name = str;
        this.seInfoTargetSdkVersion = 10000;
        WatchedArraySet<PackageSetting> watchedArraySet = new WatchedArraySet<>();
        this.mPackages = watchedArraySet;
        this.mPackagesSnapshot = new SnapshotCache.Auto(watchedArraySet, watchedArraySet, "SharedUserSetting.packages");
        WatchedArraySet<PackageSetting> watchedArraySet2 = new WatchedArraySet<>();
        this.mDisabledPackages = watchedArraySet2;
        this.mDisabledPackagesSnapshot = new SnapshotCache.Auto(watchedArraySet2, watchedArraySet2, "SharedUserSetting.mDisabledPackages");
        this.processes = new ArrayMap<>();
        registerObservers();
        this.mSnapshot = makeCache();
    }

    private SharedUserSetting(SharedUserSetting sharedUserSetting) {
        super(sharedUserSetting);
        this.mObserver = new Watcher() { // from class: com.android.server.pm.SharedUserSetting.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                SharedUserSetting.this.onChanged();
            }
        };
        PackageSignatures packageSignatures = new PackageSignatures();
        this.signatures = packageSignatures;
        this.name = sharedUserSetting.name;
        this.mAppId = sharedUserSetting.mAppId;
        this.uidFlags = sharedUserSetting.uidFlags;
        this.uidPrivateFlags = sharedUserSetting.uidPrivateFlags;
        this.mPackages = sharedUserSetting.mPackagesSnapshot.snapshot();
        this.mPackagesSnapshot = new SnapshotCache.Sealed();
        this.mDisabledPackages = sharedUserSetting.mDisabledPackagesSnapshot.snapshot();
        this.mDisabledPackagesSnapshot = new SnapshotCache.Sealed();
        packageSignatures.mSigningDetails = sharedUserSetting.signatures.mSigningDetails;
        this.signaturesChanged = sharedUserSetting.signaturesChanged;
        this.processes = new ArrayMap<>(sharedUserSetting.processes);
        this.mSnapshot = new SnapshotCache.Sealed();
    }

    private void registerObservers() {
        this.mPackages.registerObserver(this.mObserver);
        this.mDisabledPackages.registerObserver(this.mObserver);
    }

    @Override // com.android.server.utils.Snappable
    public SharedUserSetting snapshot() {
        return this.mSnapshot.snapshot();
    }

    public String toString() {
        return "SharedUserSetting{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.name + SliceClientPermissions.SliceAuthority.DELIMITER + this.mAppId + "}";
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, this.mAppId);
        protoOutputStream.write(1138166333442L, this.name);
        protoOutputStream.end(start);
    }

    void addProcesses(Map<String, ParsedProcess> map) {
        if (map != null) {
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                ParsedProcess parsedProcess = map.get(it.next());
                ParsedProcess parsedProcess2 = this.processes.get(parsedProcess.getName());
                if (parsedProcess2 == null) {
                    this.processes.put(parsedProcess.getName(), new ParsedProcessImpl(parsedProcess));
                } else {
                    ComponentMutateUtils.addStateFrom(parsedProcess2, parsedProcess);
                }
            }
            onChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removePackage(PackageSetting packageSetting) {
        if (!this.mPackages.remove(packageSetting)) {
            return false;
        }
        if ((getFlags() & packageSetting.getFlags()) != 0) {
            int i = this.uidFlags;
            for (int i2 = 0; i2 < this.mPackages.size(); i2++) {
                i |= this.mPackages.valueAt(i2).getFlags();
            }
            setFlags(i);
        }
        if ((packageSetting.getPrivateFlags() & getPrivateFlags()) != 0) {
            int i3 = this.uidPrivateFlags;
            for (int i4 = 0; i4 < this.mPackages.size(); i4++) {
                i3 |= this.mPackages.valueAt(i4).getPrivateFlags();
            }
            setPrivateFlags(i3);
        }
        updateProcesses();
        onChanged();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addPackage(PackageSetting packageSetting) {
        if (this.mPackages.size() == 0 && packageSetting.getPkg() != null) {
            this.seInfoTargetSdkVersion = packageSetting.getPkg().getTargetSdkVersion();
        }
        if (this.mPackages.add(packageSetting)) {
            setFlags(getFlags() | packageSetting.getFlags());
            setPrivateFlags(getPrivateFlags() | packageSetting.getPrivateFlags());
            onChanged();
        }
        if (packageSetting.getPkg() != null) {
            addProcesses(packageSetting.getPkg().getProcesses());
        }
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public List<AndroidPackage> getPackages() {
        WatchedArraySet<PackageSetting> watchedArraySet = this.mPackages;
        if (watchedArraySet == null || watchedArraySet.size() == 0) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(this.mPackages.size());
        for (int i = 0; i < this.mPackages.size(); i++) {
            PackageSetting valueAt = this.mPackages.valueAt(i);
            if (valueAt != null && valueAt.getPkg() != null) {
                arrayList.add(valueAt.getPkg());
            }
        }
        return arrayList;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public boolean isPrivileged() {
        return (getPrivateFlags() & 8) != 0;
    }

    public boolean isSingleUser() {
        if (this.mPackages.size() != 1 || this.mDisabledPackages.size() > 1) {
            return false;
        }
        if (this.mDisabledPackages.size() != 1) {
            return true;
        }
        AndroidPackageInternal pkg = this.mDisabledPackages.valueAt(0).getPkg();
        return pkg != null && pkg.isLeavingSharedUser();
    }

    public void fixSeInfoLocked() {
        WatchedArraySet<PackageSetting> watchedArraySet = this.mPackages;
        if (watchedArraySet == null || watchedArraySet.size() == 0) {
            return;
        }
        for (int i = 0; i < this.mPackages.size(); i++) {
            PackageSetting valueAt = this.mPackages.valueAt(i);
            if (valueAt != null && valueAt.getPkg() != null && valueAt.getPkg().getTargetSdkVersion() != -1 && valueAt.getPkg().getTargetSdkVersion() < this.seInfoTargetSdkVersion) {
                this.seInfoTargetSdkVersion = valueAt.getPkg().getTargetSdkVersion();
                onChanged();
            }
        }
        for (int i2 = 0; i2 < this.mPackages.size(); i2++) {
            PackageSetting valueAt2 = this.mPackages.valueAt(i2);
            if (valueAt2 != null && valueAt2.getPkg() != null) {
                valueAt2.getPkgState().setOverrideSeInfo(SELinuxMMAC.getSeInfo(valueAt2.getPkg(), isPrivileged() | valueAt2.isPrivileged(), this.seInfoTargetSdkVersion));
                onChanged();
            }
        }
    }

    public void updateProcesses() {
        this.processes.clear();
        for (int size = this.mPackages.size() - 1; size >= 0; size--) {
            AndroidPackageInternal pkg = this.mPackages.valueAt(size).getPkg();
            if (pkg != null) {
                addProcesses(pkg.getProcesses());
            }
        }
    }

    public int[] getNotInstalledUserIds() {
        int[] iArr = null;
        for (int i = 0; i < this.mPackages.size(); i++) {
            int[] notInstalledUserIds = this.mPackages.valueAt(i).getNotInstalledUserIds();
            if (iArr == null) {
                iArr = notInstalledUserIds;
            } else {
                int[] iArr2 = iArr;
                for (int i2 : iArr) {
                    if (!ArrayUtils.contains(notInstalledUserIds, i2)) {
                        iArr2 = ArrayUtils.removeInt(iArr2, i2);
                    }
                }
                iArr = iArr2;
            }
        }
        return iArr == null ? EmptyArray.INT : iArr;
    }

    public SharedUserSetting updateFrom(SharedUserSetting sharedUserSetting) {
        super.copySettingBase(sharedUserSetting);
        this.mAppId = sharedUserSetting.mAppId;
        this.uidFlags = sharedUserSetting.uidFlags;
        this.uidPrivateFlags = sharedUserSetting.uidPrivateFlags;
        this.seInfoTargetSdkVersion = sharedUserSetting.seInfoTargetSdkVersion;
        this.mPackages.clear();
        this.mPackages.addAll(sharedUserSetting.mPackages);
        this.signaturesChanged = sharedUserSetting.signaturesChanged;
        ArrayMap<String, ParsedProcess> arrayMap = sharedUserSetting.processes;
        if (arrayMap != null) {
            int size = arrayMap.size();
            this.processes.clear();
            this.processes.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                ParsedProcessImpl parsedProcessImpl = new ParsedProcessImpl(sharedUserSetting.processes.valueAt(i));
                this.processes.put(parsedProcessImpl.getName(), parsedProcessImpl);
            }
        } else {
            this.processes.clear();
        }
        onChanged();
        return this;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public String getName() {
        return this.name;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getAppId() {
        return this.mAppId;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getUidFlags() {
        return this.uidFlags;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getPrivateUidFlags() {
        return this.uidPrivateFlags;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getSeInfoTargetSdkVersion() {
        return this.seInfoTargetSdkVersion;
    }

    public WatchedArraySet<PackageSetting> getPackageSettings() {
        return this.mPackages;
    }

    public WatchedArraySet<PackageSetting> getDisabledPackageSettings() {
        return this.mDisabledPackages;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public ArraySet<? extends PackageStateInternal> getPackageStates() {
        return this.mPackages.untrackedStorage();
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public ArraySet<? extends PackageStateInternal> getDisabledPackageStates() {
        return this.mDisabledPackages.untrackedStorage();
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public SigningDetails getSigningDetails() {
        return this.signatures.mSigningDetails;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public ArrayMap<String, ParsedProcess> getProcesses() {
        return this.processes;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public LegacyPermissionState getSharedUserLegacyPermissionState() {
        return super.getLegacyPermissionState();
    }
}
