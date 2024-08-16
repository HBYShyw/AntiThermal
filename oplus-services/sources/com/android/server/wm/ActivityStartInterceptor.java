package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.app.TaskInfo;
import android.app.admin.DevicePolicyManagerInternal;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.BlockedAppActivity;
import com.android.internal.app.HarmfulAppWarningActivity;
import com.android.internal.app.SuspendedAppActivity;
import com.android.internal.app.UnlaunchableAppActivity;
import com.android.server.LocalServices;
import com.android.server.wm.ActivityInterceptorCallback;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityStartInterceptor {
    ActivityInfo mAInfo;
    ActivityOptions mActivityOptions;
    private String mCallingFeatureId;
    private String mCallingPackage;
    int mCallingPid;
    int mCallingUid;
    Task mInTask;
    TaskFragment mInTaskFragment;
    Intent mIntent;
    ResolveInfo mRInfo;
    private int mRealCallingPid;
    private int mRealCallingUid;
    String mResolvedType;
    private final RootWindowContainer mRootWindowContainer;
    private final ActivityTaskManagerService mService;
    private final Context mServiceContext;
    private int mStartFlags;
    private final ActivityTaskSupervisor mSupervisor;
    private int mUserId;
    private UserManager mUserManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStartInterceptor(ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor) {
        this(activityTaskManagerService, activityTaskSupervisor, activityTaskManagerService.mRootWindowContainer, activityTaskManagerService.mContext);
    }

    @VisibleForTesting
    ActivityStartInterceptor(ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor, RootWindowContainer rootWindowContainer, Context context) {
        this.mService = activityTaskManagerService;
        this.mSupervisor = activityTaskSupervisor;
        this.mRootWindowContainer = rootWindowContainer;
        this.mServiceContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStates(int i, int i2, int i3, int i4, String str, String str2) {
        this.mRealCallingPid = i2;
        this.mRealCallingUid = i3;
        this.mUserId = i;
        this.mStartFlags = i4;
        this.mCallingPackage = str;
        this.mCallingFeatureId = str2;
    }

    private IntentSender createIntentSenderForOriginalIntent(int i, int i2) {
        ActivityOptions makeBasic;
        Bundle deferCrossProfileAppsAnimationIfNecessary = deferCrossProfileAppsAnimationIfNecessary();
        TaskFragment launchTaskFragment = getLaunchTaskFragment();
        if (launchTaskFragment != null) {
            if (deferCrossProfileAppsAnimationIfNecessary != null) {
                makeBasic = ActivityOptions.fromBundle(deferCrossProfileAppsAnimationIfNecessary);
            } else {
                makeBasic = ActivityOptions.makeBasic();
            }
            makeBasic.setLaunchTaskFragmentToken(launchTaskFragment.getFragmentToken());
            deferCrossProfileAppsAnimationIfNecessary = makeBasic.toBundle();
        }
        return new IntentSender(this.mService.getIntentSenderLocked(2, this.mCallingPackage, this.mCallingFeatureId, i, this.mUserId, null, null, 0, new Intent[]{this.mIntent}, new String[]{this.mResolvedType}, i2, deferCrossProfileAppsAnimationIfNecessary));
    }

    private TaskFragment getLaunchTaskFragment() {
        IBinder launchTaskFragmentToken;
        TaskFragment taskFragment = this.mInTaskFragment;
        if (taskFragment != null) {
            return taskFragment;
        }
        ActivityOptions activityOptions = this.mActivityOptions;
        if (activityOptions == null || (launchTaskFragmentToken = activityOptions.getLaunchTaskFragmentToken()) == null) {
            return null;
        }
        return TaskFragment.fromTaskFragmentToken(launchTaskFragmentToken, this.mService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean intercept(Intent intent, ResolveInfo resolveInfo, ActivityInfo activityInfo, String str, Task task, TaskFragment taskFragment, int i, int i2, ActivityOptions activityOptions) {
        ActivityInterceptorCallback.ActivityInterceptResult onInterceptActivityLaunch;
        this.mUserManager = UserManager.get(this.mServiceContext);
        this.mIntent = intent;
        this.mCallingPid = i;
        this.mCallingUid = i2;
        this.mRInfo = resolveInfo;
        this.mAInfo = activityInfo;
        this.mResolvedType = str;
        this.mInTask = task;
        this.mInTaskFragment = taskFragment;
        this.mActivityOptions = activityOptions;
        if (interceptQuietProfileIfNeeded() || interceptSuspendedPackageIfNeeded() || interceptLockTaskModeViolationPackageIfNeeded() || interceptHarmfulAppIfNeeded() || interceptLockedManagedProfileIfNeeded()) {
            return true;
        }
        SparseArray<ActivityInterceptorCallback> activityInterceptorCallbacks = this.mService.getActivityInterceptorCallbacks();
        ActivityInterceptorCallback.ActivityInterceptorInfo interceptorInfo = getInterceptorInfo(null);
        for (int i3 = 0; i3 < activityInterceptorCallbacks.size(); i3++) {
            if (shouldInterceptActivityLaunch(activityInterceptorCallbacks.keyAt(i3), interceptorInfo) && (onInterceptActivityLaunch = activityInterceptorCallbacks.valueAt(i3).onInterceptActivityLaunch(interceptorInfo)) != null) {
                this.mIntent = onInterceptActivityLaunch.getIntent();
                this.mActivityOptions = onInterceptActivityLaunch.getActivityOptions();
                this.mCallingPid = this.mRealCallingPid;
                this.mCallingUid = this.mRealCallingUid;
                if (onInterceptActivityLaunch.isActivityResolved()) {
                    return true;
                }
                ResolveInfo resolveIntent = this.mSupervisor.resolveIntent(this.mIntent, null, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
                this.mRInfo = resolveIntent;
                this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, resolveIntent, this.mStartFlags, null);
                return true;
            }
        }
        return false;
    }

    private boolean hasCrossProfileAnimation() {
        ActivityOptions activityOptions = this.mActivityOptions;
        return activityOptions != null && activityOptions.getAnimationType() == 12;
    }

    private Bundle deferCrossProfileAppsAnimationIfNecessary() {
        if (!hasCrossProfileAnimation()) {
            return null;
        }
        this.mActivityOptions = null;
        return ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle();
    }

    private boolean interceptQuietProfileIfNeeded() {
        if (!this.mUserManager.isQuietModeEnabled(UserHandle.of(this.mUserId))) {
            return false;
        }
        if (isKeepProfilesRunningEnabled() && !isPackageSuspended()) {
            return false;
        }
        this.mIntent = UnlaunchableAppActivity.createInQuietModeDialogIntent(this.mUserId, createIntentSenderForOriginalIntent(this.mCallingUid, 1342177280), this.mRInfo);
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        ResolveInfo resolveIntent = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserManager.getProfileParent(this.mUserId).id, 0, this.mRealCallingUid, this.mRealCallingPid);
        this.mRInfo = resolveIntent;
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, resolveIntent, this.mStartFlags, null);
        return true;
    }

    private boolean interceptSuspendedByAdminPackage() {
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        if (devicePolicyManagerInternal == null) {
            return false;
        }
        Intent createShowAdminSupportIntent = devicePolicyManagerInternal.createShowAdminSupportIntent(this.mUserId, true);
        this.mIntent = createShowAdminSupportIntent;
        createShowAdminSupportIntent.putExtra("android.app.extra.RESTRICTION", "policy_suspend_packages");
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        UserInfo profileParent = this.mUserManager.getProfileParent(this.mUserId);
        if (profileParent != null) {
            this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, profileParent.id, 0, this.mRealCallingUid, this.mRealCallingPid);
        } else {
            this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
        }
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
        return true;
    }

    private boolean interceptSuspendedPackageIfNeeded() {
        PackageManagerInternal packageManagerInternalLocked;
        if (!isPackageSuspended() || (packageManagerInternalLocked = this.mService.getPackageManagerInternalLocked()) == null) {
            return false;
        }
        String str = this.mAInfo.applicationInfo.packageName;
        String suspendingPackage = packageManagerInternalLocked.getSuspendingPackage(str, this.mUserId);
        if ("android".equals(suspendingPackage)) {
            return interceptSuspendedByAdminPackage();
        }
        Intent createSuspendedAppInterceptIntent = SuspendedAppActivity.createSuspendedAppInterceptIntent(str, suspendingPackage, packageManagerInternalLocked.getSuspendedDialogInfo(str, suspendingPackage, this.mUserId), hasCrossProfileAnimation() ? ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle() : null, createIntentSenderForOriginalIntent(this.mCallingUid, 67108864), this.mUserId);
        this.mIntent = createSuspendedAppInterceptIntent;
        int i = this.mRealCallingPid;
        this.mCallingPid = i;
        int i2 = this.mRealCallingUid;
        this.mCallingUid = i2;
        this.mResolvedType = null;
        ResolveInfo resolveIntent = this.mSupervisor.resolveIntent(createSuspendedAppInterceptIntent, null, this.mUserId, 0, i2, i);
        this.mRInfo = resolveIntent;
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, resolveIntent, this.mStartFlags, null);
        return true;
    }

    private boolean interceptLockTaskModeViolationPackageIfNeeded() {
        ActivityInfo activityInfo = this.mAInfo;
        if (activityInfo == null || activityInfo.applicationInfo == null) {
            return false;
        }
        LockTaskController lockTaskController = this.mService.getLockTaskController();
        ActivityInfo activityInfo2 = this.mAInfo;
        if (lockTaskController.isActivityAllowed(this.mUserId, activityInfo2.applicationInfo.packageName, ActivityRecord.getLockTaskLaunchMode(activityInfo2, this.mActivityOptions))) {
            return false;
        }
        Intent createIntent = BlockedAppActivity.createIntent(this.mUserId, this.mAInfo.applicationInfo.packageName);
        this.mIntent = createIntent;
        int i = this.mRealCallingPid;
        this.mCallingPid = i;
        int i2 = this.mRealCallingUid;
        this.mCallingUid = i2;
        this.mResolvedType = null;
        ResolveInfo resolveIntent = this.mSupervisor.resolveIntent(createIntent, null, this.mUserId, 0, i2, i);
        this.mRInfo = resolveIntent;
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, resolveIntent, this.mStartFlags, null);
        return true;
    }

    private boolean interceptLockedManagedProfileIfNeeded() {
        Task task;
        Intent interceptWithConfirmCredentialsIfNeeded = interceptWithConfirmCredentialsIfNeeded(this.mAInfo, this.mUserId);
        if (interceptWithConfirmCredentialsIfNeeded == null) {
            return false;
        }
        this.mIntent = interceptWithConfirmCredentialsIfNeeded;
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        TaskFragment launchTaskFragment = getLaunchTaskFragment();
        Task task2 = this.mInTask;
        if (task2 != null) {
            this.mIntent.putExtra("android.intent.extra.TASK_ID", task2.mTaskId);
            this.mInTask = null;
        } else if (launchTaskFragment != null && (task = launchTaskFragment.getTask()) != null) {
            this.mIntent.putExtra("android.intent.extra.TASK_ID", task.mTaskId);
        }
        if (this.mActivityOptions == null) {
            this.mActivityOptions = ActivityOptions.makeBasic();
        }
        ResolveInfo resolveIntent = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserManager.getProfileParent(this.mUserId).id, 0, this.mRealCallingUid, this.mRealCallingPid);
        this.mRInfo = resolveIntent;
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, resolveIntent, this.mStartFlags, null);
        return true;
    }

    private Intent interceptWithConfirmCredentialsIfNeeded(ActivityInfo activityInfo, int i) {
        if (!this.mService.mAmInternal.shouldConfirmCredentials(i)) {
            return null;
        }
        if ((activityInfo.flags & 8388608) != 0 && (this.mUserManager.isUserUnlocked(i) || activityInfo.directBootAware)) {
            return null;
        }
        IntentSender createIntentSenderForOriginalIntent = createIntentSenderForOriginalIntent(this.mCallingUid, 1409286144);
        Intent createConfirmDeviceCredentialIntent = ((KeyguardManager) this.mServiceContext.getSystemService("keyguard")).createConfirmDeviceCredentialIntent(null, null, i, true);
        if (createConfirmDeviceCredentialIntent == null) {
            return null;
        }
        createConfirmDeviceCredentialIntent.setFlags(276840448);
        createConfirmDeviceCredentialIntent.putExtra("android.intent.extra.PACKAGE_NAME", activityInfo.packageName);
        createConfirmDeviceCredentialIntent.putExtra("android.intent.extra.INTENT", createIntentSenderForOriginalIntent);
        return createConfirmDeviceCredentialIntent;
    }

    private boolean interceptHarmfulAppIfNeeded() {
        try {
            CharSequence harmfulAppWarning = this.mService.getPackageManager().getHarmfulAppWarning(this.mAInfo.packageName, this.mUserId);
            if (harmfulAppWarning == null) {
                return false;
            }
            Intent createHarmfulAppWarningIntent = HarmfulAppWarningActivity.createHarmfulAppWarningIntent(this.mServiceContext, this.mAInfo.packageName, createIntentSenderForOriginalIntent(this.mCallingUid, 1409286144), harmfulAppWarning);
            this.mIntent = createHarmfulAppWarningIntent;
            int i = this.mRealCallingPid;
            this.mCallingPid = i;
            int i2 = this.mRealCallingUid;
            this.mCallingUid = i2;
            this.mResolvedType = null;
            ResolveInfo resolveIntent = this.mSupervisor.resolveIntent(createHarmfulAppWarningIntent, null, this.mUserId, 0, i2, i);
            this.mRInfo = resolveIntent;
            this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, resolveIntent, this.mStartFlags, null);
            return true;
        } catch (RemoteException | IllegalArgumentException unused) {
            return false;
        }
    }

    private boolean isPackageSuspended() {
        ApplicationInfo applicationInfo;
        ActivityInfo activityInfo = this.mAInfo;
        return (activityInfo == null || (applicationInfo = activityInfo.applicationInfo) == null || (applicationInfo.flags & 1073741824) == 0) ? false : true;
    }

    private static boolean isKeepProfilesRunningEnabled() {
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        return devicePolicyManagerInternal == null || devicePolicyManagerInternal.isKeepProfilesRunningEnabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityLaunched(TaskInfo taskInfo, final ActivityRecord activityRecord) {
        SparseArray<ActivityInterceptorCallback> activityInterceptorCallbacks = this.mService.getActivityInterceptorCallbacks();
        Objects.requireNonNull(activityRecord);
        ActivityInterceptorCallback.ActivityInterceptorInfo interceptorInfo = getInterceptorInfo(new Runnable() { // from class: com.android.server.wm.ActivityStartInterceptor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ActivityRecord.this.clearOptionsAnimationForSiblings();
            }
        });
        for (int i = 0; i < activityInterceptorCallbacks.size(); i++) {
            if (shouldNotifyOnActivityLaunch(activityInterceptorCallbacks.keyAt(i), interceptorInfo)) {
                activityInterceptorCallbacks.valueAt(i).onActivityLaunched(taskInfo, activityRecord.info, interceptorInfo);
            }
        }
    }

    private ActivityInterceptorCallback.ActivityInterceptorInfo getInterceptorInfo(Runnable runnable) {
        return new ActivityInterceptorCallback.ActivityInterceptorInfo.Builder(this.mCallingUid, this.mCallingPid, this.mRealCallingUid, this.mRealCallingPid, this.mUserId, this.mIntent, this.mRInfo, this.mAInfo).setResolvedType(this.mResolvedType).setCallingPackage(this.mCallingPackage).setCallingFeatureId(this.mCallingFeatureId).setCheckedOptions(this.mActivityOptions).setClearOptionsAnimationRunnable(runnable).build();
    }

    private boolean shouldInterceptActivityLaunch(int i, ActivityInterceptorCallback.ActivityInterceptorInfo activityInterceptorInfo) {
        if (i == 1001) {
            return activityInterceptorInfo.getIntent() != null && activityInterceptorInfo.getIntent().isSandboxActivity(this.mServiceContext);
        }
        return true;
    }

    private boolean shouldNotifyOnActivityLaunch(int i, ActivityInterceptorCallback.ActivityInterceptorInfo activityInterceptorInfo) {
        if (i == 1001) {
            return activityInterceptorInfo.getIntent() != null && activityInterceptorInfo.getIntent().isSandboxActivity(this.mServiceContext);
        }
        return true;
    }
}
