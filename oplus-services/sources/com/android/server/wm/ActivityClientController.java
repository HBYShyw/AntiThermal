package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.FullscreenRequestHandler;
import android.app.IActivityClientController;
import android.app.IActivityController;
import android.app.ICompatCameraControlCallback;
import android.app.IRequestFinishCallback;
import android.app.PictureInPictureParams;
import android.app.PictureInPictureUiState;
import android.app.compat.CompatChanges;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.EnterPipRequestedItem;
import android.app.servertransaction.PipStateTransactionItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManagerInternal;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.service.voice.VoiceInteractionManagerInternal;
import android.util.Slog;
import android.view.RemoteAnimationDefinition;
import android.window.SizeConfigurationBuckets;
import android.window.TransitionInfo;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.LocalServices;
import com.android.server.Watchdog;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.uri.NeededUriGrants;
import com.android.server.vr.VrManagerInternal;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.TransitionController;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityClientController extends IActivityClientController.Stub {
    public static final long ACCESS_SHARED_IDENTITY = 259743961;
    private static final String TAG = "ActivityTaskManager";
    public IActivityClientControllerExt mActivityClientControllerExt = (IActivityClientControllerExt) ExtLoader.type(IActivityClientControllerExt.class).base(this).create();
    private AssistUtils mAssistUtils;
    private final Context mContext;
    private final WindowManagerGlobalLock mGlobalLock;
    private final ActivityTaskManagerService mService;
    private final ActivityTaskSupervisor mTaskSupervisor;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityClientController(ActivityTaskManagerService activityTaskManagerService) {
        this.mService = activityTaskManagerService;
        this.mGlobalLock = activityTaskManagerService.mGlobalLock;
        this.mTaskSupervisor = activityTaskManagerService.mTaskSupervisor;
        this.mContext = activityTaskManagerService.mContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        this.mAssistUtils = new AssistUtils(this.mContext);
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        try {
            return super.onTransact(i, parcel, parcel2, i2);
        } catch (RuntimeException e) {
            throw ActivityTaskManagerService.logAndRethrowRuntimeExceptionOnTransact("ActivityClientController", e);
        }
    }

    public void activityIdle(IBinder iBinder, Configuration configuration, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    Trace.traceBegin(32L, "activityIdle");
                    ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                    if (forTokenLocked != null) {
                        this.mTaskSupervisor.activityIdleInternal(forTokenLocked, false, false, configuration);
                        if (z && forTokenLocked.hasProcess()) {
                            forTokenLocked.app.clearProfilerIfNeeded();
                        }
                        this.mService.mSocExt.onEndOfActivityIdle(this.mContext, ActivityRecord.forTokenLocked(iBinder));
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Trace.traceEnd(32L);
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void activityResumed(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord.activityResumedLocked(iBinder, z);
                this.mActivityClientControllerExt.hookActivityResumed(iBinder);
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked != null) {
                    this.mActivityClientControllerExt.activityResumed(iBinder, forTokenLocked.mUserId);
                    forTokenLocked.getWrapper().getExtImpl().interceptActivityOnSecondary(forTokenLocked, this.mTaskSupervisor.getKeyguardController());
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void activityRefreshed(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord.activityRefreshedLocked(iBinder);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void activityTopResumedStateLost() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mTaskSupervisor.handleTopResumedStateReleased(false);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void activityPaused(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Trace.traceBegin(32L, "activityPaused");
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked != null) {
                    this.mActivityClientControllerExt.notifyFlexibleWindowTaskVanish(forTokenLocked, false, true);
                    forTokenLocked.activityPaused(false);
                }
                Trace.traceEnd(32L);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void activityStopped(IBinder iBinder, Bundle bundle, PersistableBundle persistableBundle, CharSequence charSequence) {
        ActivityRecord isInRootTaskLocked;
        String str;
        int i;
        if (ActivityTaskManagerDebugConfig.DEBUG_ALL) {
            Slog.v(TAG, "Activity stopped: token=" + iBinder);
        }
        if (bundle != null && bundle.hasFileDescriptors()) {
            throw new IllegalArgumentException("File descriptors passed in Bundle");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Trace.traceBegin(32L, "activityStopped");
                isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                str = null;
                i = 0;
                if (isInRootTaskLocked != null) {
                    ActivityRecord.State state = ActivityRecord.State.STOPPING;
                    ActivityRecord.State state2 = ActivityRecord.State.RESTARTING_PROCESS;
                    if (!isInRootTaskLocked.isState(state, state2) && this.mTaskSupervisor.hasScheduledRestartTimeouts(isInRootTaskLocked)) {
                        isInRootTaskLocked.setState(state2, "continue-restart");
                    }
                    if (isInRootTaskLocked.attachedToProcess() && isInRootTaskLocked.isState(state2)) {
                        WindowProcessController windowProcessController = isInRootTaskLocked.app;
                        String str2 = windowProcessController.mName;
                        i = windowProcessController.mUid;
                        str = str2;
                    }
                    isInRootTaskLocked.activityStopped(bundle, persistableBundle, charSequence);
                }
                Trace.traceEnd(32L);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        if (str != null) {
            this.mTaskSupervisor.removeRestartTimeouts(isInRootTaskLocked);
            this.mService.mAmInternal.killProcess(str, i, "restartActivityProcess");
        }
        this.mService.mAmInternal.trimApplications();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void activityDestroyed(IBinder iBinder) {
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Slog.v(ActivityTaskManagerService.TAG_SWITCH, "ACTIVITY DESTROYED: " + iBinder);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Trace.traceBegin(32L, "activityDestroyed");
                try {
                    ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                    this.mActivityClientControllerExt.activityDestroyed(forTokenLocked);
                    if (forTokenLocked != null) {
                        forTokenLocked.destroyed("activityDestroyed");
                    }
                } finally {
                    Trace.traceEnd(32L);
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void activityLocalRelaunch(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked != null) {
                    forTokenLocked.startRelaunching();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void activityRelaunched(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked != null) {
                    forTokenLocked.finishRelaunching();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void reportSizeConfigurations(IBinder iBinder, SizeConfigurationBuckets sizeConfigurationBuckets) {
        if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1305412562, 0, (String) null, new Object[]{String.valueOf(iBinder), String.valueOf(sizeConfigurationBuckets)});
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked != null) {
                    isInRootTaskLocked.setSizeConfigurations(sizeConfigurationBuckets);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean moveActivityTaskToBack(IBinder iBinder, boolean z) {
        ActivityTaskManagerService.enforceNotIsolatedCaller("moveActivityTaskToBack");
        Slog.i(TAG, "moveActivityTaskToBack callingPid:" + Binder.getCallingPid() + ", callingUid:" + Binder.getCallingUid());
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    int taskForActivityLocked = ActivityRecord.getTaskForActivityLocked(iBinder, !z);
                    Task anyTaskForId = this.mService.mRootWindowContainer.anyTaskForId(taskForActivityLocked);
                    if (anyTaskForId == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    this.mActivityClientControllerExt.moveActivityTaskToBack(anyTaskForId, iBinder, z);
                    this.mActivityClientControllerExt.notifyFlexibleWindowTaskVanish(ActivityRecord.isInRootTaskLocked(iBinder), true, true);
                    boolean moveTaskToBack = ActivityRecord.getRootTask(iBinder).moveTaskToBack(anyTaskForId);
                    if (!ActivityTaskManagerService.LTW_DISABLE && moveTaskToBack) {
                        this.mActivityClientControllerExt.closeRemoteTask(this.mService, taskForActivityLocked);
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return moveTaskToBack;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean shouldUpRecreateTask(IBinder iBinder, String str) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                boolean shouldUpRecreateTaskLocked = forTokenLocked.getRootTask().shouldUpRecreateTaskLocked(forTokenLocked, str);
                WindowManagerService.resetPriorityAfterLockedSection();
                return shouldUpRecreateTaskLocked;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public boolean navigateUpTo(IBinder iBinder, Intent intent, String str, int i, Intent intent2) {
        boolean navigateUpTo;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                NeededUriGrants collectGrants = this.mService.collectGrants(intent, isInRootTaskLocked);
                NeededUriGrants collectGrants2 = this.mService.collectGrants(intent2, isInRootTaskLocked.resultTo);
                WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        navigateUpTo = isInRootTaskLocked.getRootTask().navigateUpTo(isInRootTaskLocked, intent, str, collectGrants, i, intent2, collectGrants2);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                return navigateUpTo;
            } finally {
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    public boolean releaseActivityInstance(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null && isInRootTaskLocked.isDestroyable()) {
                        isInRootTaskLocked.destroyImmediately("app-req");
                        boolean isState = isInRootTaskLocked.isState(ActivityRecord.State.DESTROYING, ActivityRecord.State.DESTROYED);
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return isState;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return false;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean finishActivity(IBinder iBinder, int i, Intent intent, int i2) {
        long j;
        boolean z;
        ActivityRecord activityRecord;
        boolean z2;
        if (intent != null && intent.hasFileDescriptors()) {
            throw new IllegalArgumentException("File descriptors passed in Intent");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                boolean z3 = true;
                if (isInRootTaskLocked == null) {
                    return true;
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                NeededUriGrants collectGrants = this.mService.collectGrants(intent, isInRootTaskLocked.resultTo);
                WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        if (!isInRootTaskLocked.isInHistory()) {
                            return true;
                        }
                        Task task = isInRootTaskLocked.getTask();
                        ActivityRecord rootActivity = task.getRootActivity();
                        if (rootActivity == null) {
                            Slog.w(TAG, "Finishing task with all activities already finished");
                        }
                        if (this.mService.getLockTaskController().activityBlockedFromFinish(isInRootTaskLocked)) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return false;
                        }
                        if (this.mService.mController != null && (activityRecord = isInRootTaskLocked.getRootTask().topRunningActivity(iBinder, -1)) != null) {
                            try {
                                z2 = this.mService.mController.activityResuming(activityRecord.packageName);
                            } catch (RemoteException unused) {
                                this.mService.mController = null;
                                Watchdog.getInstance().setActivityController((IActivityController) null);
                                z2 = true;
                            }
                            if (!z2) {
                                Slog.i(TAG, "Not finishing activity because controller resumed");
                                this.mActivityClientControllerExt.hookActivityFinishIfResumeNotOK(isInRootTaskLocked);
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                        }
                        WindowProcessController windowProcessController = isInRootTaskLocked.app;
                        if (windowProcessController != null) {
                            windowProcessController.setLastActivityFinishTimeIfNeeded(SystemClock.uptimeMillis());
                        }
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        Trace.traceBegin(32L, "finishActivity");
                        try {
                            isInRootTaskLocked.mActivityRecordSocExt.hookOnWindowsDrawn();
                            z = i2 == 1;
                        } catch (Throwable th) {
                            th = th;
                            j = 32;
                        }
                        try {
                            if (i2 != 2 && (!z || isInRootTaskLocked != rootActivity)) {
                                boolean onBackPressed = this.mActivityClientControllerExt.onBackPressed(isInRootTaskLocked, iBinder);
                                this.mActivityClientControllerExt.notifyFlexibleWindowTaskVanish(isInRootTaskLocked, onBackPressed, onBackPressed);
                                if (!onBackPressed) {
                                    isInRootTaskLocked.finishIfPossible(i, intent, collectGrants, "app-request", true);
                                    z3 = isInRootTaskLocked.finishing;
                                    if (!ActivityTaskManagerService.LTW_DISABLE) {
                                        this.mService.getWrapper().getExtImpl().getRemoteTaskManager().handleFinishActivity(task, isInRootTaskLocked);
                                    }
                                    if (!z3) {
                                        Slog.i(TAG, "Failed to finish by app-request");
                                    }
                                    j = 32;
                                    this.mActivityClientControllerExt.hookActivityFinishEnd(isInRootTaskLocked);
                                    Trace.traceEnd(j);
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                    WindowManagerService.resetPriorityAfterLockedSection();
                                    return z3;
                                }
                                this.mActivityClientControllerExt.hookActivityFinishEnd(isInRootTaskLocked);
                                Trace.traceEnd(32L);
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                            this.mTaskSupervisor.removeTask(task, false, z, "finish-activity", isInRootTaskLocked.getUid(), isInRootTaskLocked.info.name);
                            isInRootTaskLocked.mRelaunchReason = 0;
                            this.mActivityClientControllerExt.hookActivityFinishEnd(isInRootTaskLocked);
                            Trace.traceEnd(j);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return z3;
                        } catch (Throwable th2) {
                            th = th2;
                            this.mActivityClientControllerExt.hookActivityFinishEnd(isInRootTaskLocked);
                            Trace.traceEnd(j);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            throw th;
                        }
                        j = 32;
                    } finally {
                        WindowManagerService.resetPriorityAfterLockedSection();
                    }
                }
            } finally {
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public boolean finishActivityAffinity(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    final ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        if (!this.mService.getLockTaskController().activityBlockedFromFinish(isInRootTaskLocked)) {
                            isInRootTaskLocked.getTask().forAllActivities(new Predicate() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda1
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj) {
                                    boolean finishIfSameAffinity;
                                    finishIfSameAffinity = ActivityRecord.this.finishIfSameAffinity((ActivityRecord) obj);
                                    return finishIfSameAffinity;
                                }
                            }, isInRootTaskLocked, true, true);
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void finishSubActivity(IBinder iBinder, final String str, final int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    final ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.getRootTask().forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda4
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((ActivityRecord) obj).finishIfSubActivity(ActivityRecord.this, str, i);
                            }
                        }, true);
                        this.mService.updateOomAdj();
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setForceSendResultForMediaProjection(IBinder iBinder) {
        this.mService.mAmInternal.enforceCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION", "setForceSendResultForMediaProjection");
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked != null && isInRootTaskLocked.isInHistory()) {
                    isInRootTaskLocked.setForceSendResultForMediaProjection();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean isTopOfTask(IBinder iBinder) {
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                z = isInRootTaskLocked != null && isInRootTaskLocked.getTask().getTopNonFinishingActivity() == isInRootTaskLocked;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public boolean willActivityBeVisible(IBinder iBinder) {
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task rootTask = ActivityRecord.getRootTask(iBinder);
                z = rootTask != null && rootTask.willActivityBeVisible(iBinder);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public int getDisplayId(IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task rootTask = ActivityRecord.getRootTask(iBinder);
                if (rootTask == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return 0;
                }
                int displayId = rootTask.getDisplayId();
                int i = displayId != -1 ? displayId : 0;
                WindowManagerService.resetPriorityAfterLockedSection();
                return i;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getTaskForActivity(IBinder iBinder, boolean z) {
        int taskForActivityLocked;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                taskForActivityLocked = ActivityRecord.getTaskForActivityLocked(iBinder, z);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return taskForActivityLocked;
    }

    public Configuration getTaskConfiguration(IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInAnyTask = ActivityRecord.isInAnyTask(iBinder);
                if (isInAnyTask == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                Configuration configuration = isInAnyTask.getTask().getConfiguration();
                WindowManagerService.resetPriorityAfterLockedSection();
                return configuration;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public IBinder getActivityTokenBelow(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInAnyTask = ActivityRecord.isInAnyTask(iBinder);
                    if (isInAnyTask == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    ActivityRecord activity = isInAnyTask.getTask().getActivity(new Predicate() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda3
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$getActivityTokenBelow$2;
                            lambda$getActivityTokenBelow$2 = ActivityClientController.lambda$getActivityTokenBelow$2((ActivityRecord) obj);
                            return lambda$getActivityTokenBelow$2;
                        }
                    }, isInAnyTask, false, true);
                    if (activity == null || activity.getUid() != isInAnyTask.getUid()) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    IBinder iBinder2 = activity.token;
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return iBinder2;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getActivityTokenBelow$2(ActivityRecord activityRecord) {
        return !activityRecord.finishing;
    }

    public ComponentName getCallingActivity(IBinder iBinder) {
        ComponentName component;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord callingRecord = getCallingRecord(iBinder);
                component = callingRecord != null ? callingRecord.intent.getComponent() : null;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return component;
    }

    public String getCallingPackage(IBinder iBinder) {
        String callingPackage;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord callingRecord = getCallingRecord(iBinder);
                if (ActivityTaskManagerDebugConfig.DEBUG_STACK) {
                    Slog.v(TAG, "getCallingPackage token " + iBinder);
                }
                callingPackage = this.mActivityClientControllerExt.getCallingPackage(callingRecord != null ? callingRecord.info.packageName : null, iBinder, this.mGlobalLock);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return callingPackage;
    }

    private static ActivityRecord getCallingRecord(IBinder iBinder) {
        ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
        if (isInRootTaskLocked != null) {
            return isInRootTaskLocked.resultTo;
        }
        return null;
    }

    public int getLaunchedFromUid(IBinder iBinder) {
        int callingUid = Binder.getCallingUid();
        boolean isInternalCallerGetLaunchedFrom = isInternalCallerGetLaunchedFrom(callingUid);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked == null || !(isInternalCallerGetLaunchedFrom || canGetLaunchedFromLocked(callingUid, forTokenLocked))) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return -1;
                }
                int i = forTokenLocked.launchedFromUid;
                WindowManagerService.resetPriorityAfterLockedSection();
                return i;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public String getLaunchedFromPackage(IBinder iBinder) {
        int callingUid = Binder.getCallingUid();
        boolean isInternalCallerGetLaunchedFrom = isInternalCallerGetLaunchedFrom(callingUid);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked == null || !(isInternalCallerGetLaunchedFrom || canGetLaunchedFromLocked(callingUid, forTokenLocked))) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                String str = forTokenLocked.launchedFromPackage;
                WindowManagerService.resetPriorityAfterLockedSection();
                return str;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean isInternalCallerGetLaunchedFrom(int i) {
        if (UserHandle.getAppId(i) == 1000) {
            return true;
        }
        PackageManagerInternal packageManagerInternal = this.mService.mWindowManager.mPmInternal;
        AndroidPackage androidPackage = packageManagerInternal.getPackage(i);
        if (androidPackage == null) {
            return false;
        }
        if (androidPackage.isSignedWithPlatformKey()) {
            return true;
        }
        String[] knownPackageNames = packageManagerInternal.getKnownPackageNames(2, UserHandle.getUserId(i));
        return knownPackageNames.length > 0 && androidPackage.getPackageName().equals(knownPackageNames[0]);
    }

    private static boolean canGetLaunchedFromLocked(int i, ActivityRecord activityRecord) {
        if (CompatChanges.isChangeEnabled(ACCESS_SHARED_IDENTITY, i)) {
            return activityRecord.mShareIdentity || activityRecord.launchedFromUid == i;
        }
        return false;
    }

    public void setRequestedOrientation(IBinder iBinder, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        Slog.d(TAG, "Requested Orientation r = " + isInRootTaskLocked + ", requestedOrientation = " + ActivityInfo.screenOrientationToString(i) + " (" + i + ")");
                        this.mActivityClientControllerExt.onActivityRequestOrientation();
                        boolean requestedOrientationBefore = this.mActivityClientControllerExt.setRequestedOrientationBefore(isInRootTaskLocked, i, ActivityInfo.isFixedOrientationLandscape(i));
                        EventLogTags.writeWmSetRequestedOrientation(i, isInRootTaskLocked.shortComponentName);
                        isInRootTaskLocked.setRequestedOrientation(i);
                        this.mActivityClientControllerExt.setRequestedOrientation(isInRootTaskLocked, i, ActivityInfo.isFixedOrientationLandscape(i));
                        if (requestedOrientationBefore) {
                            this.mActivityClientControllerExt.setRequestedOrientationAfter(isInRootTaskLocked, i, ActivityInfo.isFixedOrientationLandscape(i));
                        }
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int getRequestedOrientation(IBinder iBinder) {
        int overrideOrientation;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                overrideOrientation = isInRootTaskLocked != null ? isInRootTaskLocked.getOverrideOrientation() : -1;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return overrideOrientation;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean convertFromTranslucent(IBinder iBinder) {
        boolean z;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    Transition createTransition = (isInRootTaskLocked == null || !isInRootTaskLocked.mTransitionController.inPlayingTransition(isInRootTaskLocked) || isInRootTaskLocked.mTransitionController.isCollecting()) ? null : isInRootTaskLocked.mTransitionController.createTransition(4);
                    if (isInRootTaskLocked != null) {
                        z = true;
                        if (isInRootTaskLocked.setOccludesParent(true)) {
                            if (createTransition != null) {
                                if (z) {
                                    isInRootTaskLocked.mTransitionController.requestStartTransition(createTransition, null, null, null);
                                    isInRootTaskLocked.mTransitionController.setReady(isInRootTaskLocked.getDisplayContent());
                                } else {
                                    createTransition.abort();
                                }
                            }
                        }
                    }
                    z = false;
                    if (createTransition != null) {
                    }
                } finally {
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Finally extract failed */
    public boolean convertToTranslucent(IBinder iBinder, Bundle bundle) {
        boolean z;
        ActivityOptions activityOptions;
        SafeActivityOptions fromBundle = SafeActivityOptions.fromBundle(bundle);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    z = false;
                    if (isInRootTaskLocked != null) {
                        ActivityRecord activityBelow = isInRootTaskLocked.getTask().getActivityBelow(isInRootTaskLocked);
                        if (activityBelow != null) {
                            activityBelow.returningOptions = fromBundle != null ? fromBundle.getOptions(isInRootTaskLocked) : null;
                        }
                        Transition createTransition = (((isInRootTaskLocked.mTransitionController.isShellTransitionsEnabled() && isInRootTaskLocked.isEmbedded()) || isInRootTaskLocked.mTransitionController.inPlayingTransition(isInRootTaskLocked)) && !isInRootTaskLocked.mTransitionController.isCollecting()) ? isInRootTaskLocked.mTransitionController.createTransition(3) : null;
                        z = isInRootTaskLocked.setOccludesParent(false);
                        if (createTransition != null) {
                            if (z) {
                                isInRootTaskLocked.mTransitionController.requestStartTransition(createTransition, null, null, null);
                                isInRootTaskLocked.mTransitionController.setReady(isInRootTaskLocked.getDisplayContent());
                                if (activityBelow != null && (activityOptions = activityBelow.returningOptions) != null && activityOptions.getAnimationType() == 5) {
                                    createTransition.setOverrideAnimation(TransitionInfo.AnimationOptions.makeSceneTransitionAnimOptions(), null, null);
                                }
                            } else {
                                createTransition.abort();
                            }
                        }
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isImmersive(IBinder iBinder) {
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked == null) {
                    throw new IllegalArgumentException();
                }
                z = isInRootTaskLocked.immersive;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public void setImmersive(IBinder iBinder, boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked == null) {
                    throw new IllegalArgumentException();
                }
                isInRootTaskLocked.immersive = z;
                if (isInRootTaskLocked.isFocusedActivityOnDisplay()) {
                    if (ProtoLogCache.WM_DEBUG_IMMERSIVE_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IMMERSIVE, -655104359, 0, (String) null, new Object[]{String.valueOf(isInRootTaskLocked)});
                    }
                    this.mService.applyUpdateLockStateLocked(isInRootTaskLocked);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean enterPictureInPictureMode(IBinder iBinder, PictureInPictureParams pictureInPictureParams) {
        boolean enterPictureInPictureMode;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    enterPictureInPictureMode = this.mService.enterPictureInPictureMode(ensureValidPictureInPictureActivityParams("enterPictureInPictureMode", iBinder, pictureInPictureParams), pictureInPictureParams, true);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return enterPictureInPictureMode;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setPictureInPictureParams(IBinder iBinder, PictureInPictureParams pictureInPictureParams) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ensureValidPictureInPictureActivityParams("setPictureInPictureParams", iBinder, pictureInPictureParams).setPictureInPictureParams(pictureInPictureParams);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setShouldDockBigOverlays(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord.forTokenLocked(iBinder).setShouldDockBigOverlays(z);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void splashScreenAttached(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord.splashScreenAttachedLocked(iBinder);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void requestCompatCameraControl(IBinder iBinder, boolean z, boolean z2, ICompatCameraControlCallback iCompatCameraControlCallback) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.updateCameraCompatState(z, z2, iCompatCameraControlCallback);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void updateActivitySpecificConfig(IBinder iBinder, Configuration configuration) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked != null) {
                    forTokenLocked.getWrapper().getExtImpl().updateActivitySpecificConfig(configuration);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    private ActivityRecord ensureValidPictureInPictureActivityParams(String str, IBinder iBinder, PictureInPictureParams pictureInPictureParams) {
        if (!this.mService.mSupportsPictureInPicture) {
            throw new IllegalStateException(str + ": Device doesn't support picture-in-picture mode.");
        }
        ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
        if (forTokenLocked == null) {
            throw new IllegalStateException(str + ": Can't find activity for token=" + iBinder);
        }
        if (!forTokenLocked.supportsPictureInPicture()) {
            throw new IllegalStateException(str + ": Current activity does not support picture-in-picture.");
        }
        float f = this.mContext.getResources().getFloat(R.dimen.conversation_badge_side_margin);
        float f2 = this.mContext.getResources().getFloat(R.dimen.conversation_avatar_size_group_expanded);
        if (pictureInPictureParams.hasSetAspectRatio() && !this.mService.mWindowManager.isValidPictureInPictureAspectRatio(forTokenLocked.mDisplayContent, pictureInPictureParams.getAspectRatioFloat())) {
            throw new IllegalArgumentException(String.format(str + ": Aspect ratio is too extreme (must be between %f and %f).", Float.valueOf(f), Float.valueOf(f2)));
        }
        if (this.mService.mSupportsExpandedPictureInPicture && pictureInPictureParams.hasSetExpandedAspectRatio() && !this.mService.mWindowManager.isValidExpandedPictureInPictureAspectRatio(forTokenLocked.mDisplayContent, pictureInPictureParams.getExpandedAspectRatioFloat())) {
            throw new IllegalArgumentException(String.format(str + ": Expanded aspect ratio is not extreme enough (must not be between %f and %f).", Float.valueOf(f), Float.valueOf(f2)));
        }
        pictureInPictureParams.truncateActions(ActivityTaskManager.getMaxNumPictureInPictureActions(this.mContext));
        return forTokenLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean requestPictureInPictureMode(ActivityRecord activityRecord) {
        if (activityRecord.inPinnedWindowingMode() || !activityRecord.checkEnterPictureInPictureState("requestPictureInPictureMode", false)) {
            return false;
        }
        if (activityRecord.pictureInPictureArgs.isAutoEnterEnabled()) {
            return this.mService.enterPictureInPictureMode(activityRecord, activityRecord.pictureInPictureArgs, false);
        }
        try {
            ClientTransaction obtain = ClientTransaction.obtain(activityRecord.app.getThread(), activityRecord.token);
            obtain.addCallback(EnterPipRequestedItem.obtain());
            this.mService.getLifecycleManager().scheduleTransaction(obtain);
            return true;
        } catch (Exception e) {
            Slog.w(TAG, "Failed to send enter pip requested item: " + activityRecord.intent.getComponent(), e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPictureInPictureStateChanged(ActivityRecord activityRecord, PictureInPictureUiState pictureInPictureUiState) {
        if (!activityRecord.inPinnedWindowingMode()) {
            throw new IllegalStateException("Activity is not in PIP mode");
        }
        try {
            ClientTransaction obtain = ClientTransaction.obtain(activityRecord.app.getThread(), activityRecord.token);
            obtain.addCallback(PipStateTransactionItem.obtain(pictureInPictureUiState));
            this.mService.getLifecycleManager().scheduleTransaction(obtain);
        } catch (Exception e) {
            Slog.w(TAG, "Failed to send pip state transaction item: " + activityRecord.intent.getComponent(), e);
        }
    }

    public void toggleFreeformWindowingMode(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                    if (forTokenLocked == null) {
                        throw new IllegalArgumentException("toggleFreeformWindowingMode: No activity record matching token=" + iBinder);
                    }
                    Task rootTask = forTokenLocked.getRootTask();
                    if (rootTask == null) {
                        throw new IllegalStateException("toggleFreeformWindowingMode: the activity doesn't have a root task");
                    }
                    if (!rootTask.inFreeformWindowingMode() && rootTask.getWindowingMode() != 1) {
                        throw new IllegalStateException("toggleFreeformWindowingMode: You can only toggle between fullscreen and freeform.");
                    }
                    if (rootTask.inFreeformWindowingMode()) {
                        rootTask.setWindowingMode(1);
                        rootTask.setBounds(null);
                    } else {
                        if (!forTokenLocked.supportsFreeform()) {
                            throw new IllegalStateException("This activity is currently not freeform-enabled");
                        }
                        if (rootTask.getParent().inFreeformWindowingMode()) {
                            rootTask.setWindowingMode(0);
                        } else {
                            rootTask.setWindowingMode(5);
                        }
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @FullscreenRequestHandler.RequestResult
    private int validateMultiwindowFullscreenRequestLocked(Task task, int i, ActivityRecord activityRecord) {
        if (task.getParent().getWindowingMode() != 5) {
            return 3;
        }
        if (activityRecord != task.getTopMostActivity()) {
            return 4;
        }
        return i == 1 ? task.getWindowingMode() != 5 ? 1 : 0 : (task.getWindowingMode() == 1 && task.mMultiWindowRestoreWindowingMode != -1) ? 0 : 2;
    }

    public void requestMultiwindowFullscreen(IBinder iBinder, int i, IRemoteCallback iRemoteCallback) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    requestMultiwindowFullscreenLocked(iBinder, i, iRemoteCallback);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void requestMultiwindowFullscreenLocked(IBinder iBinder, final int i, final IRemoteCallback iRemoteCallback) {
        final ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
        if (forTokenLocked == null) {
            return;
        }
        TransitionController transitionController = forTokenLocked.mTransitionController;
        if (!transitionController.isShellTransitionsEnabled()) {
            Task topDisplayFocusedRootTask = this.mService.getTopDisplayFocusedRootTask();
            int validateMultiwindowFullscreenRequestLocked = validateMultiwindowFullscreenRequestLocked(topDisplayFocusedRootTask, i, forTokenLocked);
            reportMultiwindowFullscreenRequestValidatingResult(iRemoteCallback, validateMultiwindowFullscreenRequestLocked);
            if (validateMultiwindowFullscreenRequestLocked == 0) {
                executeMultiWindowFullscreenRequest(i, topDisplayFocusedRootTask);
                return;
            }
            return;
        }
        final Transition transition = new Transition(6, 0, transitionController, this.mService.mWindowManager.mSyncEngine);
        forTokenLocked.mTransitionController.startCollectOrQueue(transition, new TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda2
            @Override // com.android.server.wm.TransitionController.OnStartCollect
            public final void onCollectStarted(boolean z) {
                ActivityClientController.this.lambda$requestMultiwindowFullscreenLocked$3(i, iRemoteCallback, forTokenLocked, transition, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: executeFullscreenRequestTransition, reason: merged with bridge method [inline-methods] */
    public void lambda$requestMultiwindowFullscreenLocked$3(int i, IRemoteCallback iRemoteCallback, ActivityRecord activityRecord, Transition transition, boolean z) {
        Task topDisplayFocusedRootTask = this.mService.getTopDisplayFocusedRootTask();
        int validateMultiwindowFullscreenRequestLocked = validateMultiwindowFullscreenRequestLocked(topDisplayFocusedRootTask, i, activityRecord);
        reportMultiwindowFullscreenRequestValidatingResult(iRemoteCallback, validateMultiwindowFullscreenRequestLocked);
        if (validateMultiwindowFullscreenRequestLocked != 0) {
            if (z) {
                transition.abort();
            }
        } else {
            transition.collect(topDisplayFocusedRootTask);
            executeMultiWindowFullscreenRequest(i, topDisplayFocusedRootTask);
            activityRecord.mTransitionController.requestStartTransition(transition, topDisplayFocusedRootTask, null, null);
            transition.setReady(topDisplayFocusedRootTask, true);
        }
    }

    private static void reportMultiwindowFullscreenRequestValidatingResult(IRemoteCallback iRemoteCallback, @FullscreenRequestHandler.RequestResult int i) {
        if (iRemoteCallback == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("result", i);
        try {
            iRemoteCallback.sendResult(bundle);
        } catch (RemoteException unused) {
            Slog.w(TAG, "client throws an exception back to the server, ignore it");
        }
    }

    private static void executeMultiWindowFullscreenRequest(int i, Task task) {
        int i2;
        if (i == 1) {
            task.mMultiWindowRestoreWindowingMode = task.getRequestedOverrideWindowingMode();
            i2 = 1;
        } else {
            i2 = task.mMultiWindowRestoreWindowingMode;
            task.mMultiWindowRestoreWindowingMode = -1;
        }
        task.setWindowingMode(i2);
        if (i2 == 1) {
            task.setBounds(null);
        }
    }

    public void startLockTaskModeByToken(IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked != null) {
                    this.mService.startLockTaskMode(forTokenLocked.getTask(), false);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void stopLockTaskModeByToken(IBinder iBinder) {
        this.mService.stopLockTaskModeInternal(iBinder, false);
    }

    public void showLockTaskEscapeMessage(IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (ActivityRecord.forTokenLocked(iBinder) != null) {
                    this.mService.getLockTaskController().showLockTaskToast();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setTaskDescription(IBinder iBinder, ActivityManager.TaskDescription taskDescription) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked != null) {
                    isInRootTaskLocked.setTaskDescription(taskDescription);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX WARN: Finally extract failed */
    public boolean showAssistFromActivity(IBinder iBinder, Bundle bundle) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                    Task topDisplayFocusedRootTask = this.mService.getTopDisplayFocusedRootTask();
                    ActivityRecord topNonFinishingActivity = topDisplayFocusedRootTask != null ? topDisplayFocusedRootTask.getTopNonFinishingActivity() : null;
                    if (topNonFinishingActivity != forTokenLocked) {
                        Slog.w(TAG, "showAssistFromActivity failed: caller " + forTokenLocked + " is not current top " + topNonFinishingActivity);
                    } else if (!topNonFinishingActivity.nowVisible) {
                        Slog.w(TAG, "showAssistFromActivity failed: caller " + forTokenLocked + " is not visible");
                    } else {
                        String str = topNonFinishingActivity.launchedFromFeatureId;
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return this.mAssistUtils.showSessionForActiveService(bundle, 8, str, (IVoiceInteractionSessionShowCallback) null, iBinder);
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isRootVoiceInteraction(IBinder iBinder) {
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                z = isInRootTaskLocked != null && isInRootTaskLocked.rootVoiceInteraction;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public void startLocalVoiceInteraction(IBinder iBinder, Bundle bundle) {
        Slog.i(TAG, "Activity tried to startLocalVoiceInteraction");
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task topDisplayFocusedRootTask = this.mService.getTopDisplayFocusedRootTask();
                ActivityRecord topNonFinishingActivity = topDisplayFocusedRootTask != null ? topDisplayFocusedRootTask.getTopNonFinishingActivity() : null;
                if (ActivityRecord.forTokenLocked(iBinder) != topNonFinishingActivity) {
                    throw new SecurityException("Only focused activity can call startVoiceInteraction");
                }
                if (this.mService.mRunningVoice == null && topNonFinishingActivity.getTask().voiceSession == null && topNonFinishingActivity.voiceSession == null) {
                    if (topNonFinishingActivity.pendingVoiceInteractionStart) {
                        Slog.w(TAG, "Pending start of voice interaction already.");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    } else {
                        topNonFinishingActivity.pendingVoiceInteractionStart = true;
                        String str = topNonFinishingActivity.launchedFromFeatureId;
                        WindowManagerService.resetPriorityAfterLockedSection();
                        ((VoiceInteractionManagerInternal) LocalServices.getService(VoiceInteractionManagerInternal.class)).startLocalVoiceInteraction(iBinder, str, bundle);
                        return;
                    }
                }
                Slog.w(TAG, "Already in a voice interaction, cannot start new voice interaction");
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void stopLocalVoiceInteraction(IBinder iBinder) {
        ((VoiceInteractionManagerInternal) LocalServices.getService(VoiceInteractionManagerInternal.class)).stopLocalVoiceInteraction(iBinder);
    }

    public void setShowWhenLocked(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.setShowWhenLocked(z);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setInheritShowWhenLocked(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.setInheritShowWhenLocked(z);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setTurnScreenOn(IBinder iBinder, boolean z) {
        Slog.i(TAG, "setTurnScreenOn callingPid:" + Binder.getCallingPid() + ", callingUid:" + Binder.getCallingUid());
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null && !this.mActivityClientControllerExt.skipSetTurnScreenOn(isInRootTaskLocked, z)) {
                        isInRootTaskLocked.setTurnScreenOn(z);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setAllowCrossUidActivitySwitchFromBelow(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.setAllowCrossUidActivitySwitchFromBelow(z);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void reportActivityFullyDrawn(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        this.mTaskSupervisor.getActivityMetricsLogger().notifyFullyDrawn(isInRootTaskLocked, z);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void overrideActivityTransition(IBinder iBinder, boolean z, int i, int i2, int i3) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked != null) {
                    isInRootTaskLocked.overrideCustomTransition(z, i, i2, i3);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void clearOverrideActivityTransition(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked != null) {
                    isInRootTaskLocked.clearCustomTransition(z);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void overridePendingTransition(IBinder iBinder, String str, int i, int i2, int i3) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (this.mActivityClientControllerExt.ignoringOverridePendingTransition(isInRootTaskLocked)) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (isInRootTaskLocked != null && isInRootTaskLocked.isState(ActivityRecord.State.RESUMED, ActivityRecord.State.PAUSING)) {
                    isInRootTaskLocked.mDisplayContent.mAppTransition.overridePendingAppTransition(str, i, i2, i3, null, null, isInRootTaskLocked.mOverrideTaskTransition);
                    isInRootTaskLocked.mTransitionController.setOverrideAnimation(TransitionInfo.AnimationOptions.makeCustomAnimOptions(str, i, i2, i3, isInRootTaskLocked.mOverrideTaskTransition), null, null);
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                Binder.restoreCallingIdentity(clearCallingIdentity);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public int setVrMode(IBinder iBinder, boolean z, ComponentName componentName) {
        ActivityRecord isInRootTaskLocked;
        this.mService.enforceSystemHasVrFeature();
        VrManagerInternal vrManagerInternal = (VrManagerInternal) LocalServices.getService(VrManagerInternal.class);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
            } finally {
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        if (isInRootTaskLocked == null) {
            throw new IllegalArgumentException();
        }
        int hasVrPackage = vrManagerInternal.hasVrPackage(componentName, isInRootTaskLocked.mUserId);
        if (hasVrPackage != 0) {
            return hasVrPackage;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                if (!z) {
                    componentName = null;
                }
                try {
                    isInRootTaskLocked.requestedVrComponent = componentName;
                    if (isInRootTaskLocked.isFocusedActivityOnDisplay()) {
                        this.mService.applyUpdateVrModeLocked(isInRootTaskLocked);
                    }
                } finally {
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 0;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    public void setRecentsScreenshotEnabled(IBinder iBinder, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.setRecentsScreenshotEnabled(z);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    void restartActivityProcessIfVisible(IBinder iBinder) {
        ActivityTaskManagerService.enforceTaskPermission("restartActivityProcess");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.restartProcessIfVisible();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void invalidateHomeTaskSnapshot(IBinder iBinder) {
        ActivityRecord isInRootTaskLocked;
        if (iBinder == null) {
            ActivityTaskManagerService.enforceTaskPermission("invalidateHomeTaskSnapshot");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (iBinder == null) {
                    Task rootHomeTask = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea().getRootHomeTask();
                    isInRootTaskLocked = rootHomeTask != null ? rootHomeTask.topRunningActivity() : null;
                } else {
                    isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                }
                if (isInRootTaskLocked != null && isInRootTaskLocked.isActivityTypeHome()) {
                    this.mService.mWindowManager.mTaskSnapshotController.removeSnapshotCache(isInRootTaskLocked.getTask().mTaskId);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void dismissKeyguard(IBinder iBinder, IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        if (charSequence != null) {
            this.mService.mAmInternal.enforceCallingPermission("android.permission.SHOW_KEYGUARD_MESSAGE", "dismissKeyguard");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mService.mKeyguardController.dismissKeyguard(iBinder, iKeyguardDismissCallback, charSequence);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void registerRemoteAnimations(IBinder iBinder, RemoteAnimationDefinition remoteAnimationDefinition) {
        this.mService.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "registerRemoteAnimations");
        remoteAnimationDefinition.setCallingPidUid(Binder.getCallingPid(), Binder.getCallingUid());
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.registerRemoteAnimations(remoteAnimationDefinition);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void unregisterRemoteAnimations(IBinder iBinder) {
        this.mService.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "unregisterRemoteAnimations");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        isInRootTaskLocked.unregisterRemoteAnimations();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean isRelativeTaskRootActivity(final ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        TaskFragment taskFragment = activityRecord.getTaskFragment();
        return activityRecord == taskFragment.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$isRelativeTaskRootActivity$4;
                lambda$isRelativeTaskRootActivity$4 = ActivityClientController.lambda$isRelativeTaskRootActivity$4(ActivityRecord.this, (ActivityRecord) obj);
                return lambda$isRelativeTaskRootActivity$4;
            }
        }, false) && activityRecord2.getTaskFragment().getCompanionTaskFragment() == taskFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isRelativeTaskRootActivity$4(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return !activityRecord2.finishing || activityRecord2 == activityRecord;
    }

    private boolean isTopActivityInTaskFragment(ActivityRecord activityRecord) {
        return activityRecord.getTaskFragment().topRunningActivity() == activityRecord;
    }

    private void requestCallbackFinish(IRequestFinishCallback iRequestFinishCallback) {
        try {
            iRequestFinishCallback.requestFinish();
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to invoke request finish callback", e);
        }
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0055 A[Catch: all -> 0x0084, TryCatch #0 {all -> 0x0084, blocks: (B:6:0x000a, B:8:0x0010, B:12:0x0018, B:16:0x0027, B:18:0x0037, B:22:0x0047, B:24:0x0055, B:25:0x0059, B:26:0x005e, B:42:0x0039, B:44:0x003f, B:45:0x0042), top: B:5:0x000a, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onBackPressed(IBinder iBinder, IRequestFinishCallback iRequestFinishCallback) {
        Intent intent;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                    if (isInRootTaskLocked != null) {
                        Task task = isInRootTaskLocked.getTask();
                        ActivityRecord rootActivity = task.getRootActivity(false, true);
                        boolean z = isInRootTaskLocked == rootActivity;
                        if (!z) {
                            if (!isRelativeTaskRootActivity(isInRootTaskLocked, rootActivity)) {
                                requestCallbackFinish(iRequestFinishCallback);
                            }
                            if (z) {
                            }
                            boolean isTopActivityInTaskFragment = isTopActivityInTaskFragment(isInRootTaskLocked);
                            if (!rootActivity.mActivityComponent.equals(task.realActivity)) {
                            }
                            boolean isLaunchSourceType = rootActivity.isLaunchSourceType(2);
                            WindowManagerService.resetPriorityAfterLockedSection();
                            if (intent == null) {
                            }
                            requestCallbackFinish(iRequestFinishCallback);
                            return;
                        }
                        if (this.mService.mWindowOrganizerController.mTaskOrganizerController.handleInterceptBackPressedOnTaskRoot(isInRootTaskLocked.getRootTask())) {
                        }
                        if (z) {
                            isInRootTaskLocked = rootActivity;
                        }
                        boolean isTopActivityInTaskFragment2 = isTopActivityInTaskFragment(isInRootTaskLocked);
                        intent = !rootActivity.mActivityComponent.equals(task.realActivity) ? rootActivity.intent : null;
                        boolean isLaunchSourceType2 = rootActivity.isLaunchSourceType(2);
                        WindowManagerService.resetPriorityAfterLockedSection();
                        if (intent == null && isTopActivityInTaskFragment2 && ((isLaunchSourceType2 && ActivityRecord.isMainIntent(intent)) || this.mActivityClientControllerExt.needMoveTaskToBack(iBinder))) {
                            moveActivityTaskToBack(iBinder, true);
                            return;
                        } else {
                            requestCallbackFinish(iRequestFinishCallback);
                            return;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void enableTaskLocaleOverride(IBinder iBinder) {
        if (UserHandle.getAppId(Binder.getCallingUid()) != 1000) {
            return;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked != null) {
                    forTokenLocked.getTask().mAlignActivityLocaleWithTask = true;
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean isRequestedToLaunchInTaskFragment(IBinder iBinder, IBinder iBinder2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                boolean z = isInRootTaskLocked.mRequestedLaunchingTaskFragmentToken == iBinder2;
                WindowManagerService.resetPriorityAfterLockedSection();
                return z;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }
}
