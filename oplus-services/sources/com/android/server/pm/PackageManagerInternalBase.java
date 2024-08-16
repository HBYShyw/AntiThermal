package com.android.server.pm;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.AuxiliaryResolveInfo;
import android.content.pm.IOnChecksumsReadyListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ProcessInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.SuspendDialogInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageStateUtils;
import com.android.server.pm.pkg.SharedUserApi;
import com.android.server.pm.pkg.component.ParsedMainComponent;
import com.android.server.pm.pkg.mutate.PackageStateMutator;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class PackageManagerInternalBase extends PackageManagerInternal {
    private final PackageManagerService mService;

    protected abstract ApexManager getApexManager();

    protected abstract AppDataHelper getAppDataHelper();

    protected abstract Context getContext();

    protected abstract DexManager getDexManager();

    protected abstract DistractingPackageHelper getDistractingPackageHelper();

    protected abstract InstantAppRegistry getInstantAppRegistry();

    protected abstract PackageObserverHelper getPackageObserverHelper();

    protected abstract PermissionManagerServiceInternal getPermissionManager();

    protected abstract ProtectedPackages getProtectedPackages();

    protected abstract ResolveIntentHelper getResolveIntentHelper();

    protected abstract SuspendPackageHelper getSuspendPackageHelper();

    protected abstract UserNeedsBadgingCache getUserNeedsBadging();

    public PackageManagerInternalBase(PackageManagerService packageManagerService) {
        this.mService = packageManagerService;
    }

    /* renamed from: snapshot, reason: merged with bridge method [inline-methods] */
    public final Computer m2045snapshot() {
        return this.mService.snapshotComputer();
    }

    @Deprecated
    public final List<ApplicationInfo> getInstalledApplications(long j, int i, int i2) {
        return m2045snapshot().getInstalledApplications(j, i, i2);
    }

    @Deprecated
    public final boolean isInstantApp(String str, int i) {
        return m2045snapshot().isInstantApp(str, i);
    }

    @Deprecated
    public final String getInstantAppPackageName(int i) {
        return m2045snapshot().getInstantAppPackageName(i);
    }

    @Deprecated
    public final boolean filterAppAccess(AndroidPackage androidPackage, int i, int i2) {
        return m2045snapshot().filterAppAccess(androidPackage, i, i2);
    }

    @Deprecated
    public final boolean filterAppAccess(String str, int i, int i2, boolean z) {
        return m2045snapshot().filterAppAccess(str, i, i2, z);
    }

    @Deprecated
    public final boolean filterAppAccess(int i, int i2) {
        return m2045snapshot().filterAppAccess(i, i2);
    }

    @Deprecated
    public final int[] getVisibilityAllowList(String str, int i) {
        return m2045snapshot().getVisibilityAllowList(str, i);
    }

    @Deprecated
    public final boolean canQueryPackage(int i, String str) {
        return m2045snapshot().canQueryPackage(i, str);
    }

    @Deprecated
    public final AndroidPackage getPackage(String str) {
        return m2045snapshot().getPackage(str);
    }

    @Deprecated
    public final AndroidPackage getAndroidPackage(String str) {
        return m2045snapshot().getPackage(str);
    }

    @Deprecated
    public final AndroidPackage getPackage(int i) {
        return m2045snapshot().getPackage(i);
    }

    @Deprecated
    public final List<AndroidPackage> getPackagesForAppId(int i) {
        return m2045snapshot().getPackagesForAppId(i);
    }

    @Deprecated
    public final PackageStateInternal getPackageStateInternal(String str) {
        return m2045snapshot().getPackageStateInternal(str);
    }

    @Deprecated
    public final ArrayMap<String, ? extends PackageStateInternal> getPackageStates() {
        return m2045snapshot().getPackageStates();
    }

    @Deprecated
    public final void removePackageListObserver(PackageManagerInternal.PackageListObserver packageListObserver) {
        getPackageObserverHelper().removeObserver(packageListObserver);
    }

    @Deprecated
    public final PackageStateInternal getDisabledSystemPackage(String str) {
        return m2045snapshot().getDisabledSystemPackage(str);
    }

    @Deprecated
    public final String[] getKnownPackageNames(int i, int i2) {
        return this.mService.getKnownPackageNamesInternal(m2045snapshot(), i, i2);
    }

    @Deprecated
    public final void setKeepUninstalledPackages(List<String> list) {
        this.mService.setKeepUninstalledPackagesInternal(m2045snapshot(), list);
    }

    @Deprecated
    public final boolean isPermissionsReviewRequired(String str, int i) {
        return getPermissionManager().isPermissionsReviewRequired(str, i);
    }

    @Deprecated
    public final PackageInfo getPackageInfo(String str, long j, int i, int i2) {
        return m2045snapshot().getPackageInfoInternal(str, -1L, j, i, i2);
    }

    @Deprecated
    public final Bundle getSuspendedPackageLauncherExtras(String str, int i) {
        return getSuspendPackageHelper().getSuspendedPackageLauncherExtras(m2045snapshot(), str, i, Binder.getCallingUid());
    }

    @Deprecated
    public final boolean isPackageSuspended(String str, int i) {
        return getSuspendPackageHelper().isPackageSuspended(m2045snapshot(), str, i, Binder.getCallingUid());
    }

    @Deprecated
    public final void removeNonSystemPackageSuspensions(String str, int i) {
        getSuspendPackageHelper().removeSuspensionsBySuspendingPackage(m2045snapshot(), new String[]{str}, new Predicate() { // from class: com.android.server.pm.PackageManagerInternalBase$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$removeNonSystemPackageSuspensions$0;
                lambda$removeNonSystemPackageSuspensions$0 = PackageManagerInternalBase.lambda$removeNonSystemPackageSuspensions$0((String) obj);
                return lambda$removeNonSystemPackageSuspensions$0;
            }
        }, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeNonSystemPackageSuspensions$0(String str) {
        return !PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str);
    }

    @Deprecated
    public final void removeDistractingPackageRestrictions(String str, int i) {
        getDistractingPackageHelper().removeDistractingPackageRestrictions(m2045snapshot(), new String[]{str}, i);
    }

    @Deprecated
    public final void removeAllDistractingPackageRestrictions(int i) {
        this.mService.removeAllDistractingPackageRestrictions(m2045snapshot(), i);
    }

    @Deprecated
    public final String getSuspendingPackage(String str, int i) {
        return getSuspendPackageHelper().getSuspendingPackage(m2045snapshot(), str, i, Binder.getCallingUid());
    }

    @Deprecated
    public final SuspendDialogInfo getSuspendedDialogInfo(String str, String str2, int i) {
        return getSuspendPackageHelper().getSuspendedDialogInfo(m2045snapshot(), str, str2, i, Binder.getCallingUid());
    }

    @Deprecated
    public final int getDistractingPackageRestrictions(String str, int i) {
        PackageStateInternal packageStateInternal = getPackageStateInternal(str);
        if (packageStateInternal == null) {
            return 0;
        }
        return packageStateInternal.getUserStateOrDefault(i).getDistractionFlags();
    }

    @Deprecated
    public final int getPackageUid(String str, long j, int i) {
        return m2045snapshot().getPackageUidInternal(str, j, i, 1000);
    }

    @Deprecated
    public final ApplicationInfo getApplicationInfo(String str, long j, int i, int i2) {
        return m2045snapshot().getApplicationInfoInternal(str, j, i, i2);
    }

    @Deprecated
    public final ActivityInfo getActivityInfo(ComponentName componentName, long j, int i, int i2) {
        return m2045snapshot().getActivityInfoInternal(componentName, j, i, i2);
    }

    @Deprecated
    public final List<ResolveInfo> queryIntentActivities(Intent intent, String str, long j, int i, int i2) {
        return m2045snapshot().queryIntentActivitiesInternal(intent, str, j, i, i2);
    }

    @Deprecated
    public final List<ResolveInfo> queryIntentReceivers(Intent intent, String str, long j, int i, int i2, boolean z) {
        return getResolveIntentHelper().queryIntentReceiversInternal(m2045snapshot(), intent, str, j, i2, i, z);
    }

    @Deprecated
    public final List<ResolveInfo> queryIntentServices(Intent intent, long j, int i, int i2) {
        return m2045snapshot().queryIntentServicesInternal(intent, intent.resolveTypeIfNeeded(getContext().getContentResolver()), j, i2, i, false);
    }

    @Deprecated
    public final ComponentName getHomeActivitiesAsUser(List<ResolveInfo> list, int i) {
        return m2045snapshot().getHomeActivitiesAsUser(list, i);
    }

    @Deprecated
    public final ComponentName getDefaultHomeActivity(int i) {
        return m2045snapshot().getDefaultHomeActivity(i);
    }

    @Deprecated
    public final ComponentName getSystemUiServiceComponent() {
        return ComponentName.unflattenFromString(getContext().getResources().getString(R.string.dlg_ok));
    }

    @Deprecated
    public final void setOwnerProtectedPackages(int i, List<String> list) {
        getProtectedPackages().setOwnerProtectedPackages(i, list);
    }

    @Deprecated
    public final boolean isPackageDataProtected(int i, String str) {
        return getProtectedPackages().isPackageDataProtected(i, str);
    }

    @Deprecated
    public final boolean isPackageStateProtected(String str, int i) {
        return getProtectedPackages().isPackageStateProtected(i, str);
    }

    @Deprecated
    public final boolean isPackageEphemeral(int i, String str) {
        PackageStateInternal packageStateInternal = getPackageStateInternal(str);
        return packageStateInternal != null && packageStateInternal.getUserStateOrDefault(i).isInstantApp();
    }

    @Deprecated
    public final boolean wasPackageEverLaunched(String str, int i) {
        if (getPackageStateInternal(str) == null) {
            throw new IllegalArgumentException("Unknown package: " + str);
        }
        return !r1.getUserStateOrDefault(i).isNotLaunched();
    }

    @Deprecated
    public final boolean isEnabledAndMatches(ParsedMainComponent parsedMainComponent, long j, int i) {
        return PackageStateUtils.isEnabledAndMatches(getPackageStateInternal(parsedMainComponent.getPackageName()), parsedMainComponent, j, i);
    }

    @Deprecated
    public final boolean userNeedsBadging(int i) {
        return getUserNeedsBadging().get(i);
    }

    @Deprecated
    public final String getNameForUid(int i) {
        return m2045snapshot().getNameForUid(i);
    }

    @Deprecated
    public final void requestInstantAppResolutionPhaseTwo(AuxiliaryResolveInfo auxiliaryResolveInfo, Intent intent, String str, String str2, String str3, boolean z, Bundle bundle, int i) {
        this.mService.requestInstantAppResolutionPhaseTwo(auxiliaryResolveInfo, intent, str, str2, str3, z, bundle, i);
    }

    @Deprecated
    public final void grantImplicitAccess(int i, Intent intent, int i2, int i3, boolean z) {
        grantImplicitAccess(i, intent, i2, i3, z, false);
    }

    @Deprecated
    public final void grantImplicitAccess(int i, Intent intent, int i2, int i3, boolean z, boolean z2) {
        this.mService.grantImplicitAccess(m2045snapshot(), i, intent, i2, i3, z, z2);
    }

    @Deprecated
    public final boolean isInstantAppInstallerComponent(ComponentName componentName) {
        ActivityInfo activityInfo = this.mService.mInstantAppInstallerActivity;
        return activityInfo != null && activityInfo.getComponentName().equals(componentName);
    }

    @Deprecated
    public final void pruneInstantApps() {
        getInstantAppRegistry().pruneInstantApps(m2045snapshot());
    }

    @Deprecated
    public final String getSetupWizardPackageName() {
        return this.mService.mSetupWizardPackage;
    }

    @Deprecated
    public final ResolveInfo resolveIntent(Intent intent, String str, long j, long j2, int i, boolean z, int i2) {
        return getResolveIntentHelper().resolveIntentInternal(m2045snapshot(), intent, str, j, j2, i, z, i2);
    }

    @Deprecated
    public final ResolveInfo resolveIntentExported(Intent intent, String str, long j, long j2, int i, boolean z, int i2, int i3) {
        return getResolveIntentHelper().resolveIntentInternal(m2045snapshot(), intent, str, j, j2, i, z, i2, true, i3);
    }

    @Deprecated
    public final ResolveInfo resolveService(Intent intent, String str, long j, int i, int i2) {
        return getResolveIntentHelper().resolveServiceInternal(m2045snapshot(), intent, str, j, i, i2);
    }

    @Deprecated
    public final ProviderInfo resolveContentProvider(String str, long j, int i, int i2) {
        return m2045snapshot().resolveContentProvider(str, j, i, i2);
    }

    @Deprecated
    public final int getUidTargetSdkVersion(int i) {
        return m2045snapshot().getUidTargetSdkVersion(i);
    }

    @Deprecated
    public final int getPackageTargetSdkVersion(String str) {
        PackageStateInternal packageStateInternal = getPackageStateInternal(str);
        if (packageStateInternal == null || packageStateInternal.getPkg() == null) {
            return 10000;
        }
        return packageStateInternal.getPkg().getTargetSdkVersion();
    }

    @Deprecated
    public final boolean canAccessInstantApps(int i, int i2) {
        return m2045snapshot().canViewInstantApps(i, i2);
    }

    @Deprecated
    public final boolean canAccessComponent(int i, ComponentName componentName, int i2) {
        return m2045snapshot().canAccessComponent(i, componentName, i2);
    }

    @Deprecated
    public final boolean hasInstantApplicationMetadata(String str, int i) {
        return getInstantAppRegistry().hasInstantApplicationMetadata(str, i);
    }

    @Deprecated
    public final SparseArray<String> getAppsWithSharedUserIds() {
        return m2045snapshot().getAppsWithSharedUserIds();
    }

    @Deprecated
    public final String[] getSharedUserPackagesForPackage(String str, int i) {
        return m2045snapshot().getSharedUserPackagesForPackage(str, i);
    }

    @Deprecated
    public final ArrayMap<String, ProcessInfo> getProcessesForUid(int i) {
        return m2045snapshot().getProcessesForUid(i);
    }

    @Deprecated
    public final int[] getPermissionGids(String str, int i) {
        return getPermissionManager().getPermissionGids(str, i);
    }

    @Deprecated
    public final void freeStorage(String str, long j, int i) throws IOException {
        this.mService.freeStorage(str, j, i);
    }

    @Deprecated
    public final void freeAllAppCacheAboveQuota(String str) throws IOException {
        this.mService.freeAllAppCacheAboveQuota(str);
    }

    @Deprecated
    public final void forEachPackageSetting(Consumer<PackageSetting> consumer) {
        this.mService.forEachPackageSetting(consumer);
    }

    @Deprecated
    public final void forEachPackageState(Consumer<PackageStateInternal> consumer) {
        this.mService.forEachPackageState(m2045snapshot(), consumer);
    }

    @Deprecated
    public final void forEachPackage(Consumer<AndroidPackage> consumer) {
        this.mService.forEachPackage(m2045snapshot(), consumer);
    }

    @Deprecated
    public final void forEachInstalledPackage(Consumer<AndroidPackage> consumer, int i) {
        this.mService.forEachInstalledPackage(m2045snapshot(), consumer, i);
    }

    @Deprecated
    public final ArraySet<String> getEnabledComponents(String str, int i) {
        PackageStateInternal packageStateInternal = getPackageStateInternal(str);
        if (packageStateInternal == null) {
            return new ArraySet<>();
        }
        return packageStateInternal.getUserStateOrDefault(i).m2393getEnabledComponents();
    }

    @Deprecated
    public final ArraySet<String> getDisabledComponents(String str, int i) {
        PackageStateInternal packageStateInternal = getPackageStateInternal(str);
        if (packageStateInternal == null) {
            return new ArraySet<>();
        }
        return packageStateInternal.getUserStateOrDefault(i).m2392getDisabledComponents();
    }

    @Deprecated
    public final int getApplicationEnabledState(String str, int i) {
        PackageStateInternal packageStateInternal = getPackageStateInternal(str);
        if (packageStateInternal == null) {
            return 0;
        }
        return packageStateInternal.getUserStateOrDefault(i).getEnabledState();
    }

    @Deprecated
    public final int getComponentEnabledSetting(ComponentName componentName, int i, int i2) {
        return m2045snapshot().getComponentEnabledSettingInternal(componentName, i, i2);
    }

    @Deprecated
    public final void setEnableRollbackCode(int i, int i2) {
        this.mService.setEnableRollbackCode(i, i2);
    }

    @Deprecated
    public final void finishPackageInstall(int i, boolean z) {
        this.mService.finishPackageInstall(i, z);
    }

    @Deprecated
    public final boolean isApexPackage(String str) {
        return m2045snapshot().isApexPackage(str);
    }

    @Deprecated
    public final List<String> getApksInApex(String str) {
        return getApexManager().getApksInApex(str);
    }

    @Deprecated
    public final boolean isCallerInstallerOfRecord(AndroidPackage androidPackage, int i) {
        return m2045snapshot().isCallerInstallerOfRecord(androidPackage, i);
    }

    @Deprecated
    public final List<String> getMimeGroup(String str, String str2) {
        return this.mService.getMimeGroupInternal(m2045snapshot(), str, str2);
    }

    @Deprecated
    public final boolean isSystemPackage(String str) {
        return str.equals(this.mService.ensureSystemPackageName(m2045snapshot(), str));
    }

    @Deprecated
    public final void unsuspendForSuspendingPackage(String str, int i) {
        this.mService.unsuspendForSuspendingPackage(m2045snapshot(), str, i);
    }

    @Deprecated
    public final boolean isSuspendingAnyPackages(String str, int i) {
        return m2045snapshot().isSuspendingAnyPackages(str, i);
    }

    @Deprecated
    public final void requestChecksums(String str, boolean z, int i, int i2, List list, IOnChecksumsReadyListener iOnChecksumsReadyListener, int i3, Executor executor, Handler handler) {
        this.mService.requestChecksumsInternal(m2045snapshot(), str, z, i, i2, list, iOnChecksumsReadyListener, i3, executor, handler);
    }

    @Deprecated
    public final boolean isPackageFrozen(String str, int i, int i2) {
        return m2045snapshot().getPackageStartability(this.mService.getSafeMode(), str, i, i2) == 3;
    }

    @Deprecated
    public final long deleteOatArtifactsOfPackage(String str) {
        return this.mService.deleteOatArtifactsOfPackage(m2045snapshot(), str);
    }

    @Deprecated
    public final void reconcileAppsData(int i, int i2, boolean z) {
        getAppDataHelper().reconcileAppsData(i, i2, z);
    }

    public ArraySet<PackageStateInternal> getSharedUserPackages(int i) {
        return m2045snapshot().getSharedUserPackages(i);
    }

    public SharedUserApi getSharedUserApi(int i) {
        return m2045snapshot().getSharedUser(i);
    }

    public boolean isUidPrivileged(int i) {
        return m2045snapshot().isUidPrivileged(i);
    }

    public int checkUidSignaturesForAllUsers(int i, int i2) {
        return m2045snapshot().checkUidSignaturesForAllUsers(i, i2);
    }

    public void setPackageStoppedState(String str, boolean z, int i) {
        this.mService.setPackageStoppedState(m2045snapshot(), str, z, i);
    }

    @Deprecated
    public final PackageStateMutator.InitialState recordInitialState() {
        return this.mService.recordInitialState();
    }

    @Deprecated
    public final PackageStateMutator.Result commitPackageStateMutation(PackageStateMutator.InitialState initialState, Consumer<PackageStateMutator> consumer) {
        return this.mService.commitPackageStateMutation(initialState, consumer);
    }

    @Deprecated
    public final void shutdown() {
        this.mService.shutdown();
    }
}
