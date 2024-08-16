package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.AppGlobals;
import android.app.WindowConfiguration;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Slog;
import android.view.RemoteAnimationAdapter;
import android.window.WindowContainerToken;
import com.android.internal.annotations.VisibleForTesting;
import java.util.function.Function;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SafeActivityOptions {
    private static final String TAG = "ActivityTaskManager";
    private ActivityOptions mCallerOptions;
    public ISafeActivityOptionsExt mExt;
    private final int mOriginalCallingPid;
    private final int mOriginalCallingUid;
    private final ActivityOptions mOriginalOptions;
    private int mRealCallingPid;
    private int mRealCallingUid;

    public static SafeActivityOptions fromBundle(Bundle bundle) {
        if (bundle != null) {
            return new SafeActivityOptions(ActivityOptions.fromBundle(bundle));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SafeActivityOptions fromBundle(Bundle bundle, int i, int i2) {
        if (bundle != null) {
            return new SafeActivityOptions(ActivityOptions.fromBundle(bundle), i, i2);
        }
        return null;
    }

    public SafeActivityOptions(ActivityOptions activityOptions) {
        this.mExt = (ISafeActivityOptionsExt) ExtLoader.type(ISafeActivityOptionsExt.class).base(this).create();
        this.mOriginalCallingPid = Binder.getCallingPid();
        this.mOriginalCallingUid = Binder.getCallingUid();
        this.mOriginalOptions = activityOptions;
    }

    private SafeActivityOptions(ActivityOptions activityOptions, int i, int i2) {
        this.mExt = (ISafeActivityOptionsExt) ExtLoader.type(ISafeActivityOptionsExt.class).base(this).create();
        this.mOriginalCallingPid = i;
        this.mOriginalCallingUid = i2;
        this.mOriginalOptions = activityOptions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SafeActivityOptions selectiveCloneLaunchOptions() {
        ActivityOptions cloneLaunchingOptions = cloneLaunchingOptions(this.mOriginalOptions);
        ActivityOptions cloneLaunchingOptions2 = cloneLaunchingOptions(this.mCallerOptions);
        if (cloneLaunchingOptions == null && cloneLaunchingOptions2 == null) {
            return null;
        }
        SafeActivityOptions safeActivityOptions = new SafeActivityOptions(cloneLaunchingOptions, this.mOriginalCallingPid, this.mOriginalCallingUid);
        safeActivityOptions.mCallerOptions = cloneLaunchingOptions2;
        safeActivityOptions.mRealCallingPid = this.mRealCallingPid;
        safeActivityOptions.mRealCallingUid = this.mRealCallingUid;
        return safeActivityOptions;
    }

    private ActivityOptions cloneLaunchingOptions(ActivityOptions activityOptions) {
        if (activityOptions == null) {
            return null;
        }
        return ActivityOptions.makeBasic().setLaunchTaskDisplayArea(activityOptions.getLaunchTaskDisplayArea()).setLaunchDisplayId(activityOptions.getLaunchDisplayId()).setCallerDisplayId(activityOptions.getCallerDisplayId()).setLaunchRootTask(activityOptions.getLaunchRootTask()).setPendingIntentBackgroundActivityStartMode(activityOptions.getPendingIntentBackgroundActivityStartMode()).setPendingIntentCreatorBackgroundActivityStartMode(activityOptions.getPendingIntentCreatorBackgroundActivityStartMode());
    }

    public void setCallerOptions(ActivityOptions activityOptions) {
        this.mRealCallingPid = Binder.getCallingPid();
        this.mRealCallingUid = Binder.getCallingUid();
        this.mCallerOptions = activityOptions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions getOptions(ActivityRecord activityRecord) throws SecurityException {
        return getOptions(activityRecord.intent, activityRecord.info, activityRecord.app, activityRecord.mTaskSupervisor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions getOptions(ActivityTaskSupervisor activityTaskSupervisor) throws SecurityException {
        return getOptions(null, null, null, activityTaskSupervisor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions getOptions(Intent intent, ActivityInfo activityInfo, WindowProcessController windowProcessController, ActivityTaskSupervisor activityTaskSupervisor) throws SecurityException {
        ActivityOptions activityOptions = this.mOriginalOptions;
        if (activityOptions != null) {
            checkPermissions(intent, activityInfo, windowProcessController, activityTaskSupervisor, activityOptions, this.mOriginalCallingPid, this.mOriginalCallingUid);
            setCallingPidUidForRemoteAnimationAdapter(this.mOriginalOptions, this.mOriginalCallingPid, this.mOriginalCallingUid);
        }
        ActivityOptions activityOptions2 = this.mCallerOptions;
        if (activityOptions2 != null) {
            checkPermissions(intent, activityInfo, windowProcessController, activityTaskSupervisor, activityOptions2, this.mRealCallingPid, this.mRealCallingUid);
            setCallingPidUidForRemoteAnimationAdapter(this.mCallerOptions, this.mRealCallingPid, this.mRealCallingUid);
        }
        return mergeActivityOptions(this.mOriginalOptions, this.mCallerOptions);
    }

    private void setCallingPidUidForRemoteAnimationAdapter(ActivityOptions activityOptions, int i, int i2) {
        RemoteAnimationAdapter remoteAnimationAdapter = activityOptions.getRemoteAnimationAdapter();
        if (remoteAnimationAdapter == null) {
            return;
        }
        if (i == WindowManagerService.MY_PID) {
            Slog.wtf(TAG, "Safe activity options constructed after clearing calling id");
        } else {
            remoteAnimationAdapter.setCallingPidUid(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions getOriginalOptions() {
        return this.mOriginalOptions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle popAppVerificationBundle() {
        ActivityOptions activityOptions = this.mOriginalOptions;
        if (activityOptions != null) {
            return activityOptions.popAppVerificationBundle();
        }
        return null;
    }

    private void abort() {
        ActivityOptions activityOptions = this.mOriginalOptions;
        if (activityOptions != null) {
            ActivityOptions.abort(activityOptions);
        }
        ActivityOptions activityOptions2 = this.mCallerOptions;
        if (activityOptions2 != null) {
            ActivityOptions.abort(activityOptions2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void abort(SafeActivityOptions safeActivityOptions) {
        if (safeActivityOptions != null) {
            safeActivityOptions.abort();
        }
    }

    @VisibleForTesting
    ActivityOptions mergeActivityOptions(ActivityOptions activityOptions, ActivityOptions activityOptions2) {
        if (activityOptions == null) {
            return activityOptions2;
        }
        if (activityOptions2 == null) {
            return activityOptions;
        }
        Bundle bundle = activityOptions.toBundle();
        bundle.putAll(activityOptions2.toBundle());
        return ActivityOptions.fromBundle(bundle);
    }

    private void checkPermissions(Intent intent, ActivityInfo activityInfo, WindowProcessController windowProcessController, ActivityTaskSupervisor activityTaskSupervisor, ActivityOptions activityOptions, int i, int i2) {
        final int launchTaskDisplayAreaFeatureId;
        if ((activityOptions.getLaunchTaskId() != -1 || activityOptions.getDisableStartingWindow()) && !this.mExt.isPuttDisplay(activityOptions.getLaunchDisplayId()) && !activityTaskSupervisor.mRecentTasks.isCallerRecents(i2) && ActivityTaskManagerService.checkPermission("android.permission.START_TASKS_FROM_RECENTS", i, i2) == -1) {
            String str = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with launchTaskId=" + activityOptions.getLaunchTaskId();
            Slog.w(TAG, str);
            throw new SecurityException(str);
        }
        if (activityOptions.getTransientLaunch() && !activityTaskSupervisor.mRecentTasks.isCallerRecents(i2) && ActivityTaskManagerService.checkPermission("android.permission.MANAGE_ACTIVITY_TASKS", i, i2) == -1) {
            String str2 = "Permission Denial: starting transient launch from " + windowProcessController + ", pid=" + i + ", uid=" + i2;
            Slog.w(TAG, str2);
            throw new SecurityException(str2);
        }
        WindowContainerToken launchTaskDisplayArea = activityOptions.getLaunchTaskDisplayArea();
        TaskDisplayArea taskDisplayArea = launchTaskDisplayArea != null ? (TaskDisplayArea) WindowContainer.fromBinder(launchTaskDisplayArea.asBinder()) : null;
        if (taskDisplayArea == null && (launchTaskDisplayAreaFeatureId = activityOptions.getLaunchTaskDisplayAreaFeatureId()) != -1) {
            DisplayContent displayContent = activityTaskSupervisor.mRootWindowContainer.getDisplayContent(activityOptions.getLaunchDisplayId() == -1 ? 0 : activityOptions.getLaunchDisplayId());
            if (displayContent != null) {
                taskDisplayArea = (TaskDisplayArea) displayContent.getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.SafeActivityOptions$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        TaskDisplayArea lambda$checkPermissions$0;
                        lambda$checkPermissions$0 = SafeActivityOptions.lambda$checkPermissions$0(launchTaskDisplayAreaFeatureId, (TaskDisplayArea) obj);
                        return lambda$checkPermissions$0;
                    }
                });
            }
        }
        if (activityInfo != null && taskDisplayArea != null && !activityTaskSupervisor.isCallerAllowedToLaunchOnTaskDisplayArea(i, i2, taskDisplayArea, activityInfo)) {
            String str3 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with launchTaskDisplayArea=" + taskDisplayArea;
            Slog.w(TAG, str3);
            throw new SecurityException(str3);
        }
        int launchDisplayId = activityOptions.getLaunchDisplayId();
        if (activityInfo != null && launchDisplayId != -1 && !activityTaskSupervisor.isCallerAllowedToLaunchOnDisplay(i, i2, launchDisplayId, activityInfo)) {
            String str4 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with launchDisplayId=" + launchDisplayId;
            Slog.w(TAG, str4);
            throw new SecurityException(str4);
        }
        boolean lockTaskMode = activityOptions.getLockTaskMode();
        if (activityInfo != null && lockTaskMode && !activityTaskSupervisor.mService.getLockTaskController().isPackageAllowlisted(UserHandle.getUserId(i2), activityInfo.packageName)) {
            String str5 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with lockTaskMode=true";
            Slog.w(TAG, str5);
            throw new SecurityException(str5);
        }
        boolean overrideTaskTransition = activityOptions.getOverrideTaskTransition();
        if (activityInfo != null && overrideTaskTransition && ActivityTaskManagerService.checkPermission("android.permission.START_TASKS_FROM_RECENTS", i, i2) != 0) {
            String str6 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with overrideTaskTransition=true";
            Slog.w(TAG, str6);
            throw new SecurityException(str6);
        }
        boolean dismissKeyguard = activityOptions.getDismissKeyguard();
        if (activityInfo != null && dismissKeyguard && ActivityTaskManagerService.checkPermission("android.permission.CONTROL_KEYGUARD", i, i2) != 0) {
            String str7 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with dismissKeyguard=true";
            Slog.w(TAG, str7);
            throw new SecurityException(str7);
        }
        if (activityOptions.getRemoteAnimationAdapter() != null) {
            ActivityTaskManagerService activityTaskManagerService = activityTaskSupervisor.mService;
            if (ActivityTaskManagerService.checkPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", i, i2) != 0) {
                String str8 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with remoteAnimationAdapter";
                Slog.w(TAG, str8);
                throw new SecurityException(str8);
            }
        }
        if (activityOptions.getRemoteTransition() != null) {
            ActivityTaskManagerService activityTaskManagerService2 = activityTaskSupervisor.mService;
            if (ActivityTaskManagerService.checkPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", i, i2) != 0) {
                String str9 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with remoteTransition";
                Slog.w(TAG, str9);
                throw new SecurityException(str9);
            }
        }
        if (activityOptions.getLaunchedFromBubble() && !isSystemOrSystemUI(i, i2)) {
            String str10 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with launchedFromBubble=true";
            Slog.w(TAG, str10);
            throw new SecurityException(str10);
        }
        int launchActivityType = activityOptions.getLaunchActivityType();
        if (launchActivityType == 0 || isSystemOrSystemUI(i, i2)) {
            return;
        }
        if (launchActivityType == 4 && isAssistant(activityTaskSupervisor.mService, i2)) {
            return;
        }
        String str11 = "Permission Denial: starting " + getIntentString(intent) + " from " + windowProcessController + " (pid=" + i + ", uid=" + i2 + ") with launchActivityType=" + WindowConfiguration.activityTypeToString(activityOptions.getLaunchActivityType());
        Slog.w(TAG, str11);
        throw new SecurityException(str11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TaskDisplayArea lambda$checkPermissions$0(int i, TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea.mFeatureId == i) {
            return taskDisplayArea;
        }
        return null;
    }

    private boolean isAssistant(ActivityTaskManagerService activityTaskManagerService, int i) {
        ComponentName componentName = activityTaskManagerService.mActiveVoiceInteractionServiceComponent;
        if (componentName == null) {
            return false;
        }
        return AppGlobals.getPackageManager().getPackageUid(componentName.getPackageName(), 268435456L, UserHandle.getUserId(i)) == i;
    }

    private boolean isSystemOrSystemUI(int i, int i2) {
        return i2 == 1000 || ActivityTaskManagerService.checkPermission("android.permission.STATUS_BAR_SERVICE", i, i2) == 0;
    }

    private String getIntentString(Intent intent) {
        return intent != null ? intent.toString() : "(no intent)";
    }
}
