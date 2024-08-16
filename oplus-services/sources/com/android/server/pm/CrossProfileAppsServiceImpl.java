package com.android.server.pm;

import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.ActivityOptions;
import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.app.IApplicationThread;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.PermissionChecker;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.CrossProfileAppsInternal;
import android.content.pm.ICrossProfileApps;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.server.LocalServices;
import com.android.server.pm.permission.PermissionManagerService;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CrossProfileAppsServiceImpl extends ICrossProfileApps.Stub {
    private static final String TAG = "CrossProfileAppsService";
    private Context mContext;
    private final ICrossProfileAppsServiceImplExt mImplExt;
    private Injector mInjector;
    private final LocalService mLocalService;

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Injector {
        int checkComponentPermission(String str, int i, int i2, boolean z);

        long clearCallingIdentity();

        ActivityManagerInternal getActivityManagerInternal();

        ActivityTaskManagerInternal getActivityTaskManagerInternal();

        AppOpsManager getAppOpsManager();

        int getCallingPid();

        int getCallingUid();

        UserHandle getCallingUserHandle();

        int getCallingUserId();

        DevicePolicyManagerInternal getDevicePolicyManagerInternal();

        IPackageManager getIPackageManager();

        PackageManager getPackageManager();

        PackageManagerInternal getPackageManagerInternal();

        UserManager getUserManager();

        void killUid(int i);

        void restoreCallingIdentity(long j);

        void sendBroadcastAsUser(Intent intent, UserHandle userHandle);

        <T> T withCleanCallingIdentity(FunctionalUtils.ThrowingSupplier<T> throwingSupplier);

        void withCleanCallingIdentity(FunctionalUtils.ThrowingRunnable throwingRunnable);
    }

    public CrossProfileAppsServiceImpl(Context context) {
        this(context, new InjectorImpl(context));
    }

    @VisibleForTesting
    CrossProfileAppsServiceImpl(Context context, Injector injector) {
        this.mLocalService = new LocalService();
        this.mImplExt = (ICrossProfileAppsServiceImplExt) ExtLoader.type(ICrossProfileAppsServiceImplExt.class).create();
        this.mContext = context;
        this.mInjector = injector;
    }

    public List<UserHandle> getTargetUserProfiles(String str) {
        Objects.requireNonNull(str);
        verifyCallingPackage(str);
        DevicePolicyEventLogger.createEvent(125).setStrings(new String[]{str}).write();
        return getTargetUserProfilesUnchecked(str, this.mInjector.getCallingUserId());
    }

    public void startActivityAsUser(IApplicationThread iApplicationThread, String str, String str2, ComponentName componentName, int i, boolean z, IBinder iBinder, Bundle bundle) throws RemoteException {
        Bundle bundle2 = bundle;
        Objects.requireNonNull(str);
        Objects.requireNonNull(componentName);
        verifyCallingPackage(str);
        DevicePolicyEventLogger.createEvent(126).setStrings(new String[]{str}).write();
        int callingUserId = this.mInjector.getCallingUserId();
        int callingUid = this.mInjector.getCallingUid();
        int callingPid = this.mInjector.getCallingPid();
        if (!getTargetUserProfilesUnchecked(str, callingUserId).contains(UserHandle.of(i))) {
            throw new SecurityException(str + " cannot access unrelated user " + i);
        }
        if (!str.equals(componentName.getPackageName())) {
            throw new SecurityException(str + " attempts to start an activity in other package - " + componentName.getPackageName());
        }
        Intent intent = new Intent();
        if (z) {
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            if (iBinder == null || bundle2 != null) {
                intent.addFlags(270532608);
            } else {
                intent.addFlags(DumpState.DUMP_COMPILER_STATS);
            }
            intent.setPackage(componentName.getPackageName());
        } else {
            if (callingUserId != i) {
                if (!hasInteractAcrossProfilesPermission(str, callingUid, callingPid) && !isPermissionGranted("android.permission.START_CROSS_PROFILE_ACTIVITIES", callingUid)) {
                    throw new SecurityException("Attempt to launch activity without one of the required android.permission.INTERACT_ACROSS_PROFILES or android.permission.START_CROSS_PROFILE_ACTIVITIES permissions.");
                }
                if (!isSameProfileGroup(callingUserId, i)) {
                    throw new SecurityException("Attempt to launch activity when target user is not in the same profile group.");
                }
            }
            intent.setComponent(componentName);
        }
        verifyActivityCanHandleIntentAndExported(intent, componentName, callingUid, i);
        if (bundle2 == null) {
            bundle2 = ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle();
        } else {
            bundle2.putAll(ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle());
        }
        intent.setPackage(null);
        intent.setComponent(componentName);
        this.mInjector.getActivityTaskManagerInternal().startActivityAsUser(iApplicationThread, str, str2, intent, iBinder, 0, bundle2, i);
    }

    public void startActivityAsUserByIntent(IApplicationThread iApplicationThread, String str, String str2, Intent intent, int i, IBinder iBinder, Bundle bundle) throws RemoteException {
        Objects.requireNonNull(str);
        Objects.requireNonNull(intent);
        Objects.requireNonNull(intent.getComponent(), "The intent must have a Component set");
        verifyCallingPackage(str);
        int callingUserId = this.mInjector.getCallingUserId();
        int callingUid = this.mInjector.getCallingUid();
        List<UserHandle> targetUserProfilesUnchecked = getTargetUserProfilesUnchecked(str, callingUserId);
        if (callingUserId != i && !targetUserProfilesUnchecked.contains(UserHandle.of(i))) {
            throw new SecurityException(str + " cannot access unrelated user " + i);
        }
        Intent intent2 = new Intent(intent);
        intent2.setPackage(str);
        if (!str.equals(intent2.getComponent().getPackageName())) {
            throw new SecurityException(str + " attempts to start an activity in other package - " + intent2.getComponent().getPackageName());
        }
        if (callingUserId != i && !hasCallerGotInteractAcrossProfilesPermission(str)) {
            throw new SecurityException("Attempt to launch activity without required android.permission.INTERACT_ACROSS_PROFILES permission or target user is not in the same profile group.");
        }
        verifyActivityCanHandleIntent(intent2, callingUid, i);
        this.mInjector.getActivityTaskManagerInternal().startActivityAsUser(iApplicationThread, str, str2, intent2, iBinder, 0, bundle, i);
        logStartActivityByIntent(str);
    }

    private void logStartActivityByIntent(String str) {
        DevicePolicyEventLogger.createEvent(150).setStrings(new String[]{str}).setBoolean(isCallingUserAManagedProfile()).write();
    }

    public boolean canRequestInteractAcrossProfiles(String str) {
        Objects.requireNonNull(str);
        verifyCallingPackage(str);
        return canRequestInteractAcrossProfilesUnchecked(str);
    }

    private boolean canRequestInteractAcrossProfilesUnchecked(String str) {
        int callingUserId = this.mInjector.getCallingUserId();
        int[] enabledProfileIds = this.mInjector.getUserManager().getEnabledProfileIds(callingUserId);
        if (enabledProfileIds.length >= 2 && !isProfileOwner(str, enabledProfileIds)) {
            return hasRequestedAppOpPermission(AppOpsManager.opToPermission(93), str, callingUserId);
        }
        return false;
    }

    private boolean hasRequestedAppOpPermission(String str, String str2, int i) {
        try {
            return ArrayUtils.contains(this.mInjector.getIPackageManager().getAppOpPermissionPackages(str, i), str2);
        } catch (RemoteException unused) {
            Slog.e(TAG, "PackageManager dead. Cannot get permission info");
            return false;
        }
    }

    public boolean canInteractAcrossProfiles(String str) {
        Objects.requireNonNull(str);
        verifyCallingPackage(str);
        List<UserHandle> targetUserProfilesUnchecked = getTargetUserProfilesUnchecked(str, this.mInjector.getCallingUserId());
        return !targetUserProfilesUnchecked.isEmpty() && hasCallerGotInteractAcrossProfilesPermission(str) && haveProfilesGotInteractAcrossProfilesPermission(str, targetUserProfilesUnchecked);
    }

    private boolean hasCallerGotInteractAcrossProfilesPermission(String str) {
        return hasInteractAcrossProfilesPermission(str, this.mInjector.getCallingUid(), this.mInjector.getCallingPid());
    }

    private boolean haveProfilesGotInteractAcrossProfilesPermission(final String str, List<UserHandle> list) {
        for (final UserHandle userHandle : list) {
            int intValue = ((Integer) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda6
                public final Object getOrThrow() {
                    Integer lambda$haveProfilesGotInteractAcrossProfilesPermission$0;
                    lambda$haveProfilesGotInteractAcrossProfilesPermission$0 = CrossProfileAppsServiceImpl.this.lambda$haveProfilesGotInteractAcrossProfilesPermission$0(str, userHandle);
                    return lambda$haveProfilesGotInteractAcrossProfilesPermission$0;
                }
            })).intValue();
            if (intValue == -1 || !hasInteractAcrossProfilesPermission(str, intValue, -1)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$haveProfilesGotInteractAcrossProfilesPermission$0(String str, UserHandle userHandle) throws Exception {
        try {
            return Integer.valueOf(this.mInjector.getPackageManager().getPackageUidAsUser(str, 0, userHandle.getIdentifier()));
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }

    private boolean isCrossProfilePackageAllowlisted(final String str) {
        final int callingUserId = this.mInjector.getCallingUserId();
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda1
            public final Object getOrThrow() {
                Boolean lambda$isCrossProfilePackageAllowlisted$1;
                lambda$isCrossProfilePackageAllowlisted$1 = CrossProfileAppsServiceImpl.this.lambda$isCrossProfilePackageAllowlisted$1(callingUserId, str);
                return lambda$isCrossProfilePackageAllowlisted$1;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isCrossProfilePackageAllowlisted$1(int i, String str) throws Exception {
        return Boolean.valueOf(this.mInjector.getDevicePolicyManagerInternal().getAllCrossProfilePackages(i).contains(str));
    }

    private boolean isCrossProfilePackageAllowlistedByDefault(final String str) {
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda7
            public final Object getOrThrow() {
                Boolean lambda$isCrossProfilePackageAllowlistedByDefault$2;
                lambda$isCrossProfilePackageAllowlistedByDefault$2 = CrossProfileAppsServiceImpl.this.lambda$isCrossProfilePackageAllowlistedByDefault$2(str);
                return lambda$isCrossProfilePackageAllowlistedByDefault$2;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isCrossProfilePackageAllowlistedByDefault$2(String str) throws Exception {
        return Boolean.valueOf(this.mInjector.getDevicePolicyManagerInternal().getDefaultCrossProfilePackages().contains(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<UserHandle> getTargetUserProfilesUnchecked(final String str, final int i) {
        return (List) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda11
            public final Object getOrThrow() {
                List lambda$getTargetUserProfilesUnchecked$3;
                lambda$getTargetUserProfilesUnchecked$3 = CrossProfileAppsServiceImpl.this.lambda$getTargetUserProfilesUnchecked$3(i, str);
                return lambda$getTargetUserProfilesUnchecked$3;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ List lambda$getTargetUserProfilesUnchecked$3(int i, String str) throws Exception {
        int[] enabledProfileIds = this.mInjector.getUserManager().getEnabledProfileIds(i);
        ArrayList arrayList = new ArrayList();
        for (int i2 : enabledProfileIds) {
            if (i2 != i && isPackageEnabled(str, i2) && !this.mImplExt.skipProfileInGetTargetUserProfilesUnchecked(i2, str)) {
                arrayList.add(UserHandle.of(i2));
            }
        }
        return arrayList;
    }

    private boolean isPackageEnabled(final String str, final int i) {
        final int callingUid = this.mInjector.getCallingUid();
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda13
            public final Object getOrThrow() {
                Boolean lambda$isPackageEnabled$4;
                lambda$isPackageEnabled$4 = CrossProfileAppsServiceImpl.this.lambda$isPackageEnabled$4(str, callingUid, i);
                return lambda$isPackageEnabled$4;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isPackageEnabled$4(String str, int i, int i2) throws Exception {
        PackageInfo packageInfo = this.mInjector.getPackageManagerInternal().getPackageInfo(str, 786432L, i, i2);
        return Boolean.valueOf(packageInfo != null && packageInfo.applicationInfo.enabled);
    }

    private void verifyActivityCanHandleIntent(final Intent intent, final int i, final int i2) {
        this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda10
            public final void runOrThrow() {
                CrossProfileAppsServiceImpl.this.lambda$verifyActivityCanHandleIntent$5(intent, i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyActivityCanHandleIntent$5(Intent intent, int i, int i2) throws Exception {
        if (this.mInjector.getPackageManagerInternal().queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 786432L, i, i2).isEmpty()) {
            throw new SecurityException("Activity cannot handle intent");
        }
    }

    private void verifyActivityCanHandleIntentAndExported(final Intent intent, final ComponentName componentName, final int i, final int i2) {
        this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda2
            public final void runOrThrow() {
                CrossProfileAppsServiceImpl.this.lambda$verifyActivityCanHandleIntentAndExported$6(intent, i, i2, componentName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyActivityCanHandleIntentAndExported$6(Intent intent, int i, int i2, ComponentName componentName) throws Exception {
        List queryIntentActivities = this.mInjector.getPackageManagerInternal().queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 786432L, i, i2);
        int size = queryIntentActivities.size();
        for (int i3 = 0; i3 < size; i3++) {
            ActivityInfo activityInfo = ((ResolveInfo) queryIntentActivities.get(i3)).activityInfo;
            if (TextUtils.equals(activityInfo.packageName, componentName.getPackageName()) && TextUtils.equals(activityInfo.name, componentName.getClassName()) && activityInfo.exported) {
                return;
            }
        }
        throw new SecurityException("Attempt to launch activity without  category Intent.CATEGORY_LAUNCHER or activity is not exported" + componentName);
    }

    /* renamed from: setInteractAcrossProfilesAppOp, reason: merged with bridge method [inline-methods] */
    public void lambda$clearInteractAcrossProfilesAppOps$11(int i, String str, int i2) {
        setInteractAcrossProfilesAppOp(str, i2, i);
    }

    private void setInteractAcrossProfilesAppOp(String str, int i, int i2) {
        int callingUid = this.mInjector.getCallingUid();
        if (!isPermissionGranted("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) && !isPermissionGranted("android.permission.INTERACT_ACROSS_USERS", callingUid)) {
            throw new SecurityException("INTERACT_ACROSS_USERS or INTERACT_ACROSS_USERS_FULL is required to set the app-op for interacting across profiles.");
        }
        if (!isPermissionGranted("android.permission.MANAGE_APP_OPS_MODES", callingUid) && !isPermissionGranted("android.permission.CONFIGURE_INTERACT_ACROSS_PROFILES", callingUid)) {
            throw new SecurityException("MANAGE_APP_OPS_MODES or CONFIGURE_INTERACT_ACROSS_PROFILES is required to set the app-op for interacting across profiles.");
        }
        setInteractAcrossProfilesAppOpUnchecked(str, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInteractAcrossProfilesAppOpUnchecked(String str, int i, int i2) {
        if (i == 0 && !canConfigureInteractAcrossProfiles(str, i2)) {
            Slog.e(TAG, "Tried to turn on the appop for interacting across profiles for invalid app " + str);
            return;
        }
        int[] profileIds = this.mInjector.getUserManager().getProfileIds(i2, false);
        int length = profileIds.length;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = profileIds[i3];
            if (isPackageInstalled(str, i4)) {
                setInteractAcrossProfilesAppOpForProfile(str, i, i4, i4 == i2);
            }
        }
    }

    private boolean isPackageInstalled(final String str, final int i) {
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda0
            public final Object getOrThrow() {
                Boolean lambda$isPackageInstalled$7;
                lambda$isPackageInstalled$7 = CrossProfileAppsServiceImpl.this.lambda$isPackageInstalled$7(str, i);
                return lambda$isPackageInstalled$7;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isPackageInstalled$7(String str, int i) throws Exception {
        return Boolean.valueOf(this.mInjector.getPackageManagerInternal().getPackageInfo(str, 786432L, this.mInjector.getCallingUid(), i) != null);
    }

    private void setInteractAcrossProfilesAppOpForProfile(String str, int i, int i2, boolean z) {
        try {
            setInteractAcrossProfilesAppOpForProfileOrThrow(str, i, i2, z);
        } catch (PackageManager.NameNotFoundException e) {
            Slog.e(TAG, "Missing package " + str + " on profile user ID " + i2, e);
        }
    }

    private void setInteractAcrossProfilesAppOpForProfileOrThrow(String str, final int i, int i2, boolean z) throws PackageManager.NameNotFoundException {
        final int packageUidAsUser = this.mInjector.getPackageManager().getPackageUidAsUser(str, 0, i2);
        if (currentModeEquals(i, str, packageUidAsUser)) {
            Slog.i(TAG, "Attempt to set mode to existing value of " + i + " for " + str + " on profile user ID " + i2);
            return;
        }
        if (this.mImplExt.interceptInSetInteractAcrossProfilesAppOpForProfileOrThrow(i2, str)) {
            return;
        }
        boolean hasInteractAcrossProfilesPermission = hasInteractAcrossProfilesPermission(str, packageUidAsUser, -1);
        if (isPermissionGranted("android.permission.CONFIGURE_INTERACT_ACROSS_PROFILES", this.mInjector.getCallingUid())) {
            this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda12
                public final void runOrThrow() {
                    CrossProfileAppsServiceImpl.this.lambda$setInteractAcrossProfilesAppOpForProfileOrThrow$8(packageUidAsUser, i);
                }
            });
        } else {
            this.mInjector.getAppOpsManager().setUidMode(93, packageUidAsUser, i);
        }
        maybeKillUid(str, packageUidAsUser, hasInteractAcrossProfilesPermission);
        sendCanInteractAcrossProfilesChangedBroadcast(str, UserHandle.of(i2));
        maybeLogSetInteractAcrossProfilesAppOp(str, i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setInteractAcrossProfilesAppOpForProfileOrThrow$8(int i, int i2) throws Exception {
        this.mInjector.getAppOpsManager().setUidMode(93, i, i2);
    }

    private void maybeKillUid(String str, int i, boolean z) {
        if (z && !hasInteractAcrossProfilesPermission(str, i, -1)) {
            this.mInjector.killUid(i);
        }
    }

    private void maybeLogSetInteractAcrossProfilesAppOp(String str, int i, boolean z) {
        if (z) {
            DevicePolicyEventLogger.createEvent(139).setStrings(new String[]{str}).setInt(i).setBoolean(appDeclaresCrossProfileAttribute(str)).write();
        }
    }

    private boolean currentModeEquals(final int i, final String str, final int i2) {
        final String permissionToOp = AppOpsManager.permissionToOp("android.permission.INTERACT_ACROSS_PROFILES");
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda3
            public final Object getOrThrow() {
                Boolean lambda$currentModeEquals$9;
                lambda$currentModeEquals$9 = CrossProfileAppsServiceImpl.this.lambda$currentModeEquals$9(i, permissionToOp, i2, str);
                return lambda$currentModeEquals$9;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$currentModeEquals$9(int i, String str, int i2, String str2) throws Exception {
        return Boolean.valueOf(i == this.mInjector.getAppOpsManager().unsafeCheckOpNoThrow(str, i2, str2));
    }

    private void sendCanInteractAcrossProfilesChangedBroadcast(String str, UserHandle userHandle) {
        Intent intent = new Intent("android.content.pm.action.CAN_INTERACT_ACROSS_PROFILES_CHANGED").setPackage(str);
        if (appDeclaresCrossProfileAttribute(str)) {
            intent.addFlags(285212672);
        } else {
            intent.addFlags(1073741824);
        }
        Iterator<ResolveInfo> it = findBroadcastReceiversForUser(intent, userHandle).iterator();
        while (it.hasNext()) {
            intent.setComponent(it.next().getComponentInfo().getComponentName());
            this.mInjector.sendBroadcastAsUser(intent, userHandle);
        }
    }

    private List<ResolveInfo> findBroadcastReceiversForUser(Intent intent, UserHandle userHandle) {
        return this.mInjector.getPackageManager().queryBroadcastReceiversAsUser(intent, 0, userHandle);
    }

    private boolean appDeclaresCrossProfileAttribute(String str) {
        return this.mInjector.getPackageManagerInternal().getPackage(str).isCrossProfile();
    }

    public boolean canConfigureInteractAcrossProfiles(int i, String str) {
        if (this.mInjector.getCallingUserId() != i) {
            this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS");
        }
        return canConfigureInteractAcrossProfiles(str, i);
    }

    private boolean canConfigureInteractAcrossProfiles(String str, int i) {
        if (canUserAttemptToConfigureInteractAcrossProfiles(str, i) && hasOtherProfileWithPackageInstalled(str, i) && hasRequestedAppOpPermission(AppOpsManager.opToPermission(93), str, i)) {
            return isCrossProfilePackageAllowlisted(str);
        }
        return false;
    }

    public boolean canUserAttemptToConfigureInteractAcrossProfiles(int i, String str) {
        if (this.mInjector.getCallingUserId() != i) {
            this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS");
        }
        return canUserAttemptToConfigureInteractAcrossProfiles(str, i);
    }

    private boolean canUserAttemptToConfigureInteractAcrossProfiles(String str, int i) {
        int[] profileIds = this.mInjector.getUserManager().getProfileIds(i, false);
        if (profileIds.length >= 2 && !isProfileOwner(str, profileIds) && hasRequestedAppOpPermission(AppOpsManager.opToPermission(93), str, i)) {
            return !isPlatformSignedAppWithNonUserConfigurablePermission(str, profileIds);
        }
        return false;
    }

    private boolean isPlatformSignedAppWithNonUserConfigurablePermission(String str, int[] iArr) {
        return !isCrossProfilePackageAllowlistedByDefault(str) && isPlatformSignedAppWithAutomaticProfilesPermission(str, iArr);
    }

    private boolean isPlatformSignedAppWithAutomaticProfilesPermission(String str, int[] iArr) {
        for (int i : iArr) {
            int packageUid = this.mInjector.getPackageManagerInternal().getPackageUid(str, 0L, i);
            if (packageUid != -1 && isPermissionGranted("android.permission.INTERACT_ACROSS_PROFILES", packageUid)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasOtherProfileWithPackageInstalled(final String str, final int i) {
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda14
            public final Object getOrThrow() {
                Boolean lambda$hasOtherProfileWithPackageInstalled$10;
                lambda$hasOtherProfileWithPackageInstalled$10 = CrossProfileAppsServiceImpl.this.lambda$hasOtherProfileWithPackageInstalled$10(i, str);
                return lambda$hasOtherProfileWithPackageInstalled$10;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$hasOtherProfileWithPackageInstalled$10(int i, String str) throws Exception {
        for (int i2 : this.mInjector.getUserManager().getProfileIds(i, false)) {
            if (i2 != i && isPackageInstalled(str, i2)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public void resetInteractAcrossProfilesAppOps(int i, List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            resetInteractAcrossProfilesAppOp(i, it.next());
        }
    }

    private void resetInteractAcrossProfilesAppOp(int i, String str) {
        if (canConfigureInteractAcrossProfiles(str, i)) {
            Slog.w(TAG, "Not resetting app-op for package " + str + " since it is still configurable by users.");
            return;
        }
        lambda$clearInteractAcrossProfilesAppOps$11(i, str, AppOpsManager.opToDefaultMode(AppOpsManager.permissionToOp("android.permission.INTERACT_ACROSS_PROFILES")));
    }

    public void clearInteractAcrossProfilesAppOps(final int i) {
        final int opToDefaultMode = AppOpsManager.opToDefaultMode(AppOpsManager.permissionToOp("android.permission.INTERACT_ACROSS_PROFILES"));
        findAllPackageNames().forEach(new Consumer() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CrossProfileAppsServiceImpl.this.lambda$clearInteractAcrossProfilesAppOps$11(i, opToDefaultMode, (String) obj);
            }
        });
    }

    private List<String> findAllPackageNames() {
        return (List) this.mInjector.getPackageManagerInternal().getInstalledApplications(0L, this.mInjector.getCallingUserId(), this.mInjector.getCallingUid()).stream().map(new Function() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String str;
                str = ((ApplicationInfo) obj).packageName;
                return str;
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CrossProfileAppsInternal getLocalService() {
        return this.mLocalService;
    }

    private boolean isSameProfileGroup(final int i, final int i2) {
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda4
            public final Object getOrThrow() {
                Boolean lambda$isSameProfileGroup$13;
                lambda$isSameProfileGroup$13 = CrossProfileAppsServiceImpl.this.lambda$isSameProfileGroup$13(i, i2);
                return lambda$isSameProfileGroup$13;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isSameProfileGroup$13(int i, int i2) throws Exception {
        return Boolean.valueOf(this.mInjector.getUserManager().isSameProfileGroup(i, i2));
    }

    private void verifyCallingPackage(String str) {
        this.mInjector.getAppOpsManager().checkPackage(this.mInjector.getCallingUid(), str);
    }

    private boolean isPermissionGranted(String str, int i) {
        return this.mInjector.checkComponentPermission(str, i, -1, true) == 0;
    }

    private boolean isCallingUserAManagedProfile() {
        return isManagedProfile(this.mInjector.getCallingUserId());
    }

    private boolean isManagedProfile(final int i) {
        return ((Boolean) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda15
            public final Object getOrThrow() {
                Boolean lambda$isManagedProfile$14;
                lambda$isManagedProfile$14 = CrossProfileAppsServiceImpl.this.lambda$isManagedProfile$14(i);
                return lambda$isManagedProfile$14;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isManagedProfile$14(int i) throws Exception {
        return Boolean.valueOf(((UserManager) this.mContext.getSystemService(UserManager.class)).isManagedProfile(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasInteractAcrossProfilesPermission(String str, int i, int i2) {
        return isPermissionGranted("android.permission.INTERACT_ACROSS_USERS_FULL", i) || isPermissionGranted("android.permission.INTERACT_ACROSS_USERS", i) || PermissionChecker.checkPermissionForPreflight(this.mContext, "android.permission.INTERACT_ACROSS_PROFILES", i2, i, str) == 0;
    }

    private boolean isProfileOwner(String str, int[] iArr) {
        for (int i : iArr) {
            if (isProfileOwner(str, i)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProfileOwner(String str, final int i) {
        ComponentName componentName = (ComponentName) this.mInjector.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda8
            public final Object getOrThrow() {
                ComponentName lambda$isProfileOwner$15;
                lambda$isProfileOwner$15 = CrossProfileAppsServiceImpl.this.lambda$isProfileOwner$15(i);
                return lambda$isProfileOwner$15;
            }
        });
        if (componentName == null) {
            return false;
        }
        return componentName.getPackageName().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ComponentName lambda$isProfileOwner$15(int i) throws Exception {
        return this.mInjector.getDevicePolicyManagerInternal().getProfileOwnerAsUser(i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class InjectorImpl implements Injector {
        private Context mContext;

        public InjectorImpl(Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int getCallingUid() {
            return Binder.getCallingUid();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int getCallingPid() {
            return Binder.getCallingPid();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int getCallingUserId() {
            return UserHandle.getCallingUserId();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public UserHandle getCallingUserHandle() {
            return Binder.getCallingUserHandle();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public long clearCallingIdentity() {
            return Binder.clearCallingIdentity();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void restoreCallingIdentity(long j) {
            Binder.restoreCallingIdentity(j);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void withCleanCallingIdentity(FunctionalUtils.ThrowingRunnable throwingRunnable) {
            Binder.withCleanCallingIdentity(throwingRunnable);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public final <T> T withCleanCallingIdentity(FunctionalUtils.ThrowingSupplier<T> throwingSupplier) {
            return (T) Binder.withCleanCallingIdentity(throwingSupplier);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public UserManager getUserManager() {
            return (UserManager) this.mContext.getSystemService(UserManager.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public PackageManagerInternal getPackageManagerInternal() {
            return (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public PackageManager getPackageManager() {
            return this.mContext.getPackageManager();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public AppOpsManager getAppOpsManager() {
            return (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public ActivityManagerInternal getActivityManagerInternal() {
            return (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public ActivityTaskManagerInternal getActivityTaskManagerInternal() {
            return (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public IPackageManager getIPackageManager() {
            return AppGlobals.getPackageManager();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public DevicePolicyManagerInternal getDevicePolicyManagerInternal() {
            return (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {
            this.mContext.sendBroadcastAsUser(intent, userHandle);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int checkComponentPermission(String str, int i, int i2, boolean z) {
            return ActivityManager.checkComponentPermission(str, i, i2, z);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void killUid(int i) {
            PermissionManagerService.killUid(UserHandle.getAppId(i), UserHandle.getUserId(i), "permissions revoked");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class LocalService extends CrossProfileAppsInternal {
        LocalService() {
        }

        public boolean verifyPackageHasInteractAcrossProfilePermission(String str, int i) throws PackageManager.NameNotFoundException {
            PackageManager packageManager = CrossProfileAppsServiceImpl.this.mInjector.getPackageManager();
            Objects.requireNonNull(str);
            ApplicationInfo applicationInfoAsUser = packageManager.getApplicationInfoAsUser(str, 0, i);
            Objects.requireNonNull(applicationInfoAsUser);
            return verifyUidHasInteractAcrossProfilePermission(str, applicationInfoAsUser.uid);
        }

        public boolean verifyUidHasInteractAcrossProfilePermission(String str, int i) {
            Objects.requireNonNull(str);
            return CrossProfileAppsServiceImpl.this.hasInteractAcrossProfilesPermission(str, i, -1);
        }

        public List<UserHandle> getTargetUserProfiles(String str, int i) {
            return CrossProfileAppsServiceImpl.this.getTargetUserProfilesUnchecked(str, i);
        }

        public void setInteractAcrossProfilesAppOp(String str, int i, int i2) {
            CrossProfileAppsServiceImpl.this.setInteractAcrossProfilesAppOpUnchecked(str, i, i2);
        }
    }
}
