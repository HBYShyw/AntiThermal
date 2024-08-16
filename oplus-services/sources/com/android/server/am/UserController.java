package com.android.server.am;

import android.R;
import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppGlobals;
import android.app.BroadcastOptions;
import android.app.IStopUserCallback;
import android.app.IUserSwitchObserver;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.appwidget.AppWidgetManagerInternal;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.PermissionChecker;
import android.content.pm.IPackageManager;
import android.content.pm.PackagePartitions;
import android.content.pm.UserInfo;
import android.content.pm.UserProperties;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IProgressListener;
import android.os.IRemoteCallback;
import android.os.IUserManager;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.IStorageManager;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.IntArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.Preconditions;
import com.android.internal.widget.LockPatternUtils;
import com.android.server.FactoryResetter;
import com.android.server.FgThread;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.LocalServices;
import com.android.server.SystemServiceManager;
import com.android.server.am.IUserControllerExt;
import com.android.server.am.UserController;
import com.android.server.am.UserState;
import com.android.server.pm.UserJourneyLogger;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.UserManagerService;
import com.android.server.utils.Slogf;
import com.android.server.utils.TimingsTraceAndSlog;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.WindowManagerService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UserController implements Handler.Callback {
    static final int CLEAR_USER_JOURNEY_SESSION_MSG = 200;
    static final int COMPLETE_USER_SWITCH_MSG = 130;
    static final int CONTINUE_USER_SWITCH_MSG = 20;
    static final int DEFAULT_USER_SWITCH_TIMEOUT_MS = 3000;
    private static final int DISMISS_KEYGUARD_TIMEOUT_MS = 2000;
    static final int FOREGROUND_PROFILE_CHANGED_MSG = 70;
    private static final int LONG_USER_SWITCH_OBSERVER_WARNING_TIME_MS = 500;
    private static final int NO_ARG2 = 0;
    static final int REPORT_LOCKED_BOOT_COMPLETE_MSG = 110;
    static final int REPORT_USER_SWITCH_COMPLETE_MSG = 80;
    static final int REPORT_USER_SWITCH_MSG = 10;
    static final int START_PROFILES_MSG = 40;
    static final int START_USER_SWITCH_FG_MSG = 120;
    static final int START_USER_SWITCH_UI_MSG = 1000;
    private static final String TAG = "ActivityManager";
    private static final int USER_COMPLETED_EVENT_DELAY_MS = 5000;
    static final int USER_COMPLETED_EVENT_MSG = 140;
    static final int USER_CURRENT_MSG = 60;
    private static final int USER_JOURNEY_TIMEOUT_MS = 90000;
    static final int USER_START_MSG = 50;
    private static final int USER_SWITCH_CALLBACKS_TIMEOUT_MS = 5000;
    static final int USER_SWITCH_CALLBACKS_TIMEOUT_MSG = 90;
    static final int USER_SWITCH_TIMEOUT_MSG = 30;
    static final int USER_UNLOCKED_MSG = 105;
    static final int USER_UNLOCK_MSG = 100;
    public static IUserControllerExt.IStaticExt mStaticExt = (IUserControllerExt.IStaticExt) ExtLoader.type(IUserControllerExt.IStaticExt.class).create();
    private volatile boolean mAllowUserUnlocking;
    volatile boolean mBootCompleted;

    @GuardedBy({"mCompletedEventTypes"})
    private final SparseIntArray mCompletedEventTypes;

    @GuardedBy({"mLock"})
    private volatile ArraySet<String> mCurWaitingUserSwitchCallbacks;

    @GuardedBy({"mLock"})
    private int[] mCurrentProfileIds;

    @GuardedBy({"mLock"})
    private volatile int mCurrentUserId;

    @GuardedBy({"mLock"})
    private boolean mDelayUserDataLocking;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private boolean mInitialized;
    private final Injector mInjector;

    @GuardedBy({"mLock"})
    private boolean mIsBroadcastSentForSystemUserStarted;

    @GuardedBy({"mLock"})
    private boolean mIsBroadcastSentForSystemUserStarting;

    @GuardedBy({"mLock"})
    private final ArrayList<Integer> mLastActiveUsers;
    private volatile long mLastUserUnlockingUptime;
    private final Object mLock;
    private final LockPatternUtils mLockPatternUtils;

    @GuardedBy({"mLock"})
    private int mMaxRunningUsers;

    @GuardedBy({"mLock"})
    private final List<PendingUserStart> mPendingUserStarts;

    @GuardedBy({"mLock"})
    private int[] mStartedUserArray;

    @GuardedBy({"mLock"})
    private final SparseArray<UserState> mStartedUsers;

    @ActivityManager.StopUserOnSwitch
    @GuardedBy({"mLock"})
    private int mStopUserOnSwitch;

    @GuardedBy({"mLock"})
    private String mSwitchingFromSystemUserMessage;

    @GuardedBy({"mLock"})
    private String mSwitchingToSystemUserMessage;

    @GuardedBy({"mLock"})
    private volatile int mTargetUserId;

    @GuardedBy({"mLock"})
    private ArraySet<String> mTimeoutUserSwitchCallbacks;
    private final Handler mUiHandler;
    public IUserControllerExt mUserControllerExt;
    private final UserManagerInternal.UserLifecycleListener mUserLifecycleListener;

    @GuardedBy({"mLock"})
    private final ArrayList<Integer> mUserLru;

    @GuardedBy({"mLock"})
    private final SparseIntArray mUserProfileGroupIds;
    private final RemoteCallbackList<IUserSwitchObserver> mUserSwitchObservers;

    @GuardedBy({"mLock"})
    private boolean mUserSwitchUiEnabled;
    private UserControllerWrapper mWrapper;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserController(ActivityManagerService activityManagerService) {
        this(new Injector(activityManagerService));
        this.mUserControllerExt.setInjector(activityManagerService, this.mLock, this.mStartedUsers);
    }

    @VisibleForTesting
    UserController(Injector injector) {
        this.mLock = new Object();
        this.mCurrentUserId = 0;
        this.mTargetUserId = -10000;
        SparseArray<UserState> sparseArray = new SparseArray<>();
        this.mStartedUsers = sparseArray;
        ArrayList<Integer> arrayList = new ArrayList<>();
        this.mUserLru = arrayList;
        this.mStartedUserArray = new int[]{0};
        this.mCurrentProfileIds = new int[0];
        this.mUserProfileGroupIds = new SparseIntArray();
        this.mUserSwitchObservers = new RemoteCallbackList<>();
        this.mUserSwitchUiEnabled = true;
        this.mLastActiveUsers = new ArrayList<>();
        this.mCompletedEventTypes = new SparseIntArray();
        this.mStopUserOnSwitch = -1;
        this.mLastUserUnlockingUptime = 0L;
        this.mPendingUserStarts = new ArrayList();
        this.mUserLifecycleListener = new UserManagerInternal.UserLifecycleListener() { // from class: com.android.server.am.UserController.1
            public void onUserCreated(UserInfo userInfo, Object obj) {
                UserController.this.onUserAdded(userInfo);
            }

            public void onUserRemoved(UserInfo userInfo) {
                UserController.this.onUserRemoved(userInfo.id);
            }
        };
        this.mWrapper = new UserControllerWrapper();
        this.mUserControllerExt = (IUserControllerExt) ExtLoader.type(IUserControllerExt.class).create();
        this.mInjector = injector;
        this.mHandler = injector.getHandler(this);
        this.mUiHandler = injector.getUiHandler(this);
        UserState userState = new UserState(UserHandle.SYSTEM);
        userState.mUnlockProgress.addListener(new UserProgressListener());
        sparseArray.put(0, userState);
        arrayList.add(0);
        this.mLockPatternUtils = injector.getLockPatternUtils();
        updateStartedUserArrayLU();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInitialConfig(boolean z, int i, boolean z2) {
        synchronized (this.mLock) {
            this.mUserSwitchUiEnabled = z;
            this.mMaxRunningUsers = i;
            this.mDelayUserDataLocking = z2;
            this.mInitialized = true;
        }
    }

    private boolean isUserSwitchUiEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mUserSwitchUiEnabled;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMaxRunningUsers() {
        int i;
        synchronized (this.mLock) {
            i = this.mMaxRunningUsers;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStopUserOnSwitch(@ActivityManager.StopUserOnSwitch int i) {
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") == -1 && this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") == -1) {
            throw new SecurityException("You either need MANAGE_USERS or INTERACT_ACROSS_USERS permission to call setStopUserOnSwitch()");
        }
        synchronized (this.mLock) {
            Slogf.i("ActivityManager", "setStopUserOnSwitch(): %d -> %d", new Object[]{Integer.valueOf(this.mStopUserOnSwitch), Integer.valueOf(i)});
            this.mStopUserOnSwitch = i;
        }
    }

    private boolean shouldStopUserOnSwitch() {
        synchronized (this.mLock) {
            int i = this.mStopUserOnSwitch;
            if (i != -1) {
                boolean z = i == 1;
                Slogf.i("ActivityManager", "shouldStopUserOnSwitch(): returning overridden value (%b)", new Object[]{Boolean.valueOf(z)});
                return z;
            }
            int i2 = SystemProperties.getInt("fw.stop_bg_users_on_switch", -1);
            if (i2 == -1) {
                return this.mDelayUserDataLocking;
            }
            return i2 == 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishUserSwitch(final UserState userState) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$finishUserSwitch$0(userState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserSwitch$0(UserState userState) {
        finishUserBoot(userState);
        startProfiles();
        synchronized (this.mLock) {
            stopRunningUsersLU(this.mMaxRunningUsers);
        }
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    List<Integer> getRunningUsersLU() {
        int i;
        ArrayList arrayList = new ArrayList();
        Iterator<Integer> it = this.mUserLru.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            UserState userState = this.mStartedUsers.get(next.intValue());
            if (userState != null && (i = userState.state) != 4 && i != 5) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    @GuardedBy({"mLock"})
    private void stopRunningUsersLU(int i) {
        List<Integer> runningUsersLU = getRunningUsersLU();
        Iterator<Integer> it = runningUsersLU.iterator();
        while (runningUsersLU.size() > i && it.hasNext()) {
            Integer next = it.next();
            if (next.intValue() != 0 && next.intValue() != this.mCurrentUserId && !this.mUserControllerExt.checkUserIfNeed(next.intValue()) && stopUsersLU(next.intValue(), false, true, null, null) == 0) {
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canStartMoreUsers() {
        boolean z;
        synchronized (this.mLock) {
            z = getRunningUsersLU().size() < this.mMaxRunningUsers;
        }
        return z;
    }

    private void finishUserBoot(UserState userState) {
        finishUserBoot(userState, null);
    }

    private void finishUserBoot(UserState userState, IIntentReceiver iIntentReceiver) {
        int identifier = userState.mHandle.getIdentifier();
        EventLog.writeEvent(EventLogTags.UC_FINISH_USER_BOOT, identifier);
        synchronized (this.mLock) {
            if (this.mStartedUsers.get(identifier) != userState) {
                return;
            }
            if (userState.setState(0, 1)) {
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(identifier, 4, 0);
                this.mInjector.getUserManagerInternal().setUserState(identifier, userState.state);
                if (identifier == 0 && !this.mInjector.isRuntimeRestarted() && !this.mInjector.isFirstBootOrUpgrade()) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 12, elapsedRealtime);
                    if (elapsedRealtime > 120000) {
                        Slogf.wtf("SystemServerTiming", "finishUserBoot took too long. elapsedTimeMs=" + elapsedRealtime);
                    }
                }
                if (!this.mInjector.getUserManager().isPreCreated(identifier)) {
                    Handler handler = this.mHandler;
                    handler.sendMessage(handler.obtainMessage(110, identifier, 0));
                    if (this.mAllowUserUnlocking) {
                        sendLockedBootCompletedBroadcast(iIntentReceiver, identifier);
                    }
                }
            }
            if (this.mInjector.getUserManager().isProfile(identifier)) {
                UserInfo profileParent = this.mInjector.getUserManager().getProfileParent(identifier);
                if (profileParent != null && isUserRunning(profileParent.id, 4)) {
                    Slogf.d("ActivityManager", "User " + identifier + " (parent " + profileParent.id + "): attempting unlock because parent is unlocked");
                    maybeUnlockUser(identifier);
                    return;
                }
                Slogf.d("ActivityManager", "User " + identifier + " (parent " + (profileParent == null ? "<null>" : String.valueOf(profileParent.id)) + "): delaying unlock because parent is locked");
                return;
            }
            maybeUnlockUser(identifier);
        }
    }

    private void sendLockedBootCompletedBroadcast(IIntentReceiver iIntentReceiver, int i) {
        Intent intent = new Intent("android.intent.action.LOCKED_BOOT_COMPLETED", (Uri) null);
        intent.putExtra("android.intent.extra.user_handle", i);
        intent.addFlags(-1996488704);
        this.mInjector.broadcastIntent(intent, null, iIntentReceiver, 0, null, null, new String[]{"android.permission.RECEIVE_BOOT_COMPLETED"}, -1, getTemporaryAppAllowlistBroadcastOptions(202).toBundle(), true, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), i);
        this.mUserControllerExt.hookAgingUserBoot(i);
    }

    private boolean finishUserUnlocking(final UserState userState) {
        final int identifier = userState.mHandle.getIdentifier();
        EventLog.writeEvent(EventLogTags.UC_FINISH_USER_UNLOCKING, identifier);
        this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(identifier, 5, 1);
        if (!StorageManager.isUserKeyUnlocked(identifier)) {
            return false;
        }
        synchronized (this.mLock) {
            if (this.mStartedUsers.get(identifier) == userState && userState.state == 1) {
                userState.mUnlockProgress.start();
                userState.mUnlockProgress.setProgress(5, this.mInjector.getContext().getString(R.string.autofill_address_type_use_my_re));
                this.mUserControllerExt.hookFgHandler(FgThread.getHandler()).post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserController.this.lambda$finishUserUnlocking$1(identifier, userState);
                    }
                });
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserUnlocking$1(int i, UserState userState) {
        if (!StorageManager.isUserKeyUnlocked(i)) {
            Slogf.w("ActivityManager", "User key got locked unexpectedly, leaving user locked.");
            return;
        }
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        timingsTraceAndSlog.traceBegin("UM.onBeforeUnlockUser-" + i);
        this.mUserControllerExt.ormsUnlockUserBoost(2000);
        this.mInjector.getUserManager().onBeforeUnlockUser(i);
        timingsTraceAndSlog.traceEnd();
        synchronized (this.mLock) {
            if (userState.setState(1, 2)) {
                this.mInjector.getUserManagerInternal().setUserState(i, userState.state);
                userState.mUnlockProgress.setProgress(20);
                this.mUserControllerExt.ormsUnlockUserBoost(30000);
                this.mLastUserUnlockingUptime = SystemClock.uptimeMillis();
                this.mHandler.obtainMessage(100, i, 0, userState).sendToTarget();
            }
        }
    }

    private void finishUserUnlocked(final UserState userState) {
        UserInfo profileParent;
        int identifier = userState.mHandle.getIdentifier();
        EventLog.writeEvent(EventLogTags.UC_FINISH_USER_UNLOCKED, identifier);
        if (StorageManager.isUserKeyUnlocked(identifier)) {
            synchronized (this.mLock) {
                if (this.mStartedUsers.get(userState.mHandle.getIdentifier()) != userState) {
                    return;
                }
                if (userState.setState(2, 3)) {
                    this.mInjector.getUserManagerInternal().setUserState(identifier, userState.state);
                    userState.mUnlockProgress.finish();
                    this.mUserControllerExt.reUnlockMultiAppUser(identifier);
                    this.mUserControllerExt.setUnlockedForDexopt();
                    if (identifier == 0) {
                        this.mInjector.startPersistentApps(262144);
                    }
                    this.mInjector.installEncryptionUnawareProviders(identifier);
                    if (!this.mInjector.getUserManager().isPreCreated(identifier)) {
                        Intent intent = new Intent("android.intent.action.USER_UNLOCKED");
                        intent.putExtra("android.intent.extra.user_handle", identifier);
                        intent.addFlags(1342177280);
                        this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), identifier);
                    }
                    UserInfo userInfo = getUserInfo(identifier);
                    if (userInfo.isProfile() && (profileParent = this.mInjector.getUserManager().getProfileParent(identifier)) != null) {
                        broadcastProfileAccessibleStateChanged(identifier, profileParent.id, "android.intent.action.PROFILE_ACCESSIBLE");
                        if (userInfo.isManagedProfile()) {
                            Intent intent2 = new Intent("android.intent.action.MANAGED_PROFILE_UNLOCKED");
                            intent2.putExtra("android.intent.extra.USER", UserHandle.of(identifier));
                            intent2.addFlags(1342177280);
                            this.mInjector.broadcastIntent(intent2, null, null, 0, null, null, null, -1, null, false, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), profileParent.id);
                        }
                    }
                    UserInfo userInfo2 = getUserInfo(identifier);
                    if (!Objects.equals(userInfo2.lastLoggedInFingerprint, PackagePartitions.FINGERPRINT) || SystemProperties.getBoolean("persist.pm.mock-upgrade", false)) {
                        this.mInjector.sendPreBootBroadcast(identifier, userInfo2.isManagedProfile(), new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda13
                            @Override // java.lang.Runnable
                            public final void run() {
                                UserController.this.lambda$finishUserUnlocked$2(userState);
                            }
                        });
                    } else {
                        lambda$finishUserUnlocked$2(userState);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: finishUserUnlockedCompleted, reason: merged with bridge method [inline-methods] */
    public void lambda$finishUserUnlocked$2(UserState userState) {
        final int identifier = userState.mHandle.getIdentifier();
        EventLog.writeEvent(EventLogTags.UC_FINISH_USER_UNLOCKED_COMPLETED, identifier);
        synchronized (this.mLock) {
            if (this.mStartedUsers.get(userState.mHandle.getIdentifier()) != userState) {
                return;
            }
            final UserInfo userInfo = getUserInfo(identifier);
            if (userInfo != null && StorageManager.isUserKeyUnlocked(identifier)) {
                this.mInjector.getUserManager().onUserLoggedIn(identifier);
                final Runnable runnable = new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserController.this.lambda$finishUserUnlockedCompleted$3(userInfo);
                    }
                };
                if (!userInfo.isInitialized()) {
                    Slogf.d("ActivityManager", "Initializing user #" + identifier);
                    if (userInfo.preCreated) {
                        runnable.run();
                    } else if (identifier != 0) {
                        Intent intent = new Intent("android.intent.action.USER_INITIALIZE");
                        intent.addFlags(AudioFormat.EVRCB);
                        this.mInjector.broadcastIntent(intent, null, new IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.2
                            public void performReceive(Intent intent2, int i, String str, Bundle bundle, boolean z, boolean z2, int i2) {
                                runnable.run();
                            }
                        }, 0, null, null, null, -1, null, true, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), identifier);
                    }
                }
                if (userInfo.preCreated) {
                    Slogf.i("ActivityManager", "Stopping pre-created user " + userInfo.toFullString());
                    stopUser(userInfo.id, true, false, null, null);
                    return;
                }
                this.mInjector.startUserWidgets(identifier);
                this.mHandler.obtainMessage(105, identifier, 0).sendToTarget();
                Slogf.i("ActivityManager", "Posting BOOT_COMPLETED user #" + identifier);
                if (identifier == 0 && !this.mInjector.isRuntimeRestarted() && !this.mInjector.isFirstBootOrUpgrade()) {
                    FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 13, SystemClock.elapsedRealtime());
                }
                final Intent intent2 = new Intent("android.intent.action.BOOT_COMPLETED", (Uri) null);
                intent2.putExtra("android.intent.extra.user_handle", identifier);
                intent2.addFlags(-1996488704);
                final int callingUid = Binder.getCallingUid();
                final int callingPid = Binder.getCallingPid();
                this.mUserControllerExt.hookFgHandler(FgThread.getHandler()).post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserController.this.lambda$finishUserUnlockedCompleted$4(intent2, identifier, callingUid, callingPid);
                    }
                });
                this.mUserControllerExt.triggerBootCompleteBroadcast(identifier);
                this.mUserControllerExt.recordRootState();
                this.mUserControllerExt.hookAgingUserUnlockedCompleted(identifier);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserUnlockedCompleted$3(UserInfo userInfo) {
        this.mInjector.getUserManager().makeInitialized(userInfo.id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserUnlockedCompleted$4(Intent intent, final int i, int i2, int i3) {
        this.mInjector.broadcastIntent(intent, null, new IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.3
            public void performReceive(Intent intent2, int i4, String str, Bundle bundle, boolean z, boolean z2, int i5) throws RemoteException {
                Slogf.i("ActivityManager", "Finished processing BOOT_COMPLETED for u" + i);
                UserController.this.mBootCompleted = true;
            }
        }, 0, null, null, new String[]{"android.permission.RECEIVE_BOOT_COMPLETED"}, -1, getTemporaryAppAllowlistBroadcastOptions(200).toBundle(), true, false, ActivityManagerService.MY_PID, 1000, i2, i3, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.am.UserController$4, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass4 implements UserState.KeyEvictedCallback {
        final /* synthetic */ int val$userStartMode;

        AnonymousClass4(int i) {
            this.val$userStartMode = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$keyEvicted$0(int i, int i2) {
            UserController.this.startUser(i, i2);
        }

        @Override // com.android.server.am.UserState.KeyEvictedCallback
        public void keyEvicted(final int i) {
            Handler handler = UserController.this.mHandler;
            final int i2 = this.val$userStartMode;
            handler.post(new Runnable() { // from class: com.android.server.am.UserController$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UserController.AnonymousClass4.this.lambda$keyEvicted$0(i, i2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int restartUser(int i, @UserManagerInternal.UserStartMode int i2) {
        return stopUser(i, true, false, null, new AnonymousClass4(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stopProfile(int i) {
        boolean z;
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") == -1 && this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == -1) {
            throw new SecurityException("You either need MANAGE_USERS or INTERACT_ACROSS_USERS_FULL permission to stop a profile");
        }
        UserInfo userInfo = getUserInfo(i);
        if (userInfo == null || !userInfo.isProfile()) {
            throw new IllegalArgumentException("User " + i + " is not a profile");
        }
        enforceShellRestriction("no_debugging_features", i);
        synchronized (this.mLock) {
            z = stopUsersLU(i, true, false, null, null) == 0;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int stopUser(int i, boolean z, boolean z2, IStopUserCallback iStopUserCallback, UserState.KeyEvictedCallback keyEvictedCallback) {
        int stopUsersLU;
        checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "stopUser");
        Preconditions.checkArgument(i >= 0, "Invalid user id %d", new Object[]{Integer.valueOf(i)});
        enforceShellRestriction("no_debugging_features", i);
        synchronized (this.mLock) {
            stopUsersLU = stopUsersLU(i, z, z2, iStopUserCallback, keyEvictedCallback);
        }
        return stopUsersLU;
    }

    @GuardedBy({"mLock"})
    private int stopUsersLU(int i, boolean z, boolean z2, IStopUserCallback iStopUserCallback, UserState.KeyEvictedCallback keyEvictedCallback) {
        if (i == 0) {
            return -3;
        }
        if (isCurrentUserLU(i)) {
            return -2;
        }
        if (this.mUserControllerExt.checkUserIfNeed(i)) {
            return -4;
        }
        int[] usersToStopLU = getUsersToStopLU(i);
        for (int i2 : usersToStopLU) {
            if (i2 == 0 || isCurrentUserLU(i2)) {
                if (ActivityManagerDebugConfig.DEBUG_MU) {
                    Slogf.i("ActivityManager", "stopUsersLocked cannot stop related user " + i2);
                }
                if (!z) {
                    return -4;
                }
                Slogf.i("ActivityManager", "Force stop user " + i + ". Related users will not be stopped");
                stopSingleUserLU(i, z2, iStopUserCallback, keyEvictedCallback);
                return 0;
            }
        }
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.i("ActivityManager", "stopUsersLocked usersToStop=" + Arrays.toString(usersToStopLU));
        }
        int length = usersToStopLU.length;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = usersToStopLU[i3];
            UserState.KeyEvictedCallback keyEvictedCallback2 = null;
            IStopUserCallback iStopUserCallback2 = i4 == i ? iStopUserCallback : null;
            if (i4 == i) {
                keyEvictedCallback2 = keyEvictedCallback;
            }
            stopSingleUserLU(i4, z2, iStopUserCallback2, keyEvictedCallback2);
        }
        return 0;
    }

    @GuardedBy({"mLock"})
    private void stopSingleUserLU(final int i, final boolean z, final IStopUserCallback iStopUserCallback, UserState.KeyEvictedCallback keyEvictedCallback) {
        ArrayList arrayList;
        Slogf.i("ActivityManager", "stopSingleUserLU userId=" + i);
        final UserState userState = this.mStartedUsers.get(i);
        this.mUserControllerExt.userRemoved(i);
        if (userState == null) {
            if (this.mDelayUserDataLocking) {
                if (z && keyEvictedCallback != null) {
                    Slogf.wtf("ActivityManager", "allowDelayedLocking set with KeyEvictedCallback, ignore it and lock user:" + i, new RuntimeException());
                    z = false;
                }
                if (!z && this.mLastActiveUsers.remove(Integer.valueOf(i))) {
                    if (keyEvictedCallback != null) {
                        arrayList = new ArrayList(1);
                        arrayList.add(keyEvictedCallback);
                    } else {
                        arrayList = null;
                    }
                    dispatchUserLocking(i, arrayList);
                }
            }
            if (iStopUserCallback != null) {
                this.mHandler.post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserController.lambda$stopSingleUserLU$5(iStopUserCallback, i);
                    }
                });
                return;
            }
            return;
        }
        logUserJourneyBegin(i, 5);
        if (iStopUserCallback != null) {
            userState.mStopCallbacks.add(iStopUserCallback);
        }
        if (keyEvictedCallback != null) {
            userState.mKeyEvictedCallbacks.add(keyEvictedCallback);
        }
        int i2 = userState.state;
        if (i2 == 4 || i2 == 5) {
            return;
        }
        userState.setState(4);
        UserManagerInternal userManagerInternal = this.mInjector.getUserManagerInternal();
        userManagerInternal.setUserState(i, userState.state);
        userManagerInternal.unassignUserFromDisplayOnStop(i);
        updateStartedUserArrayLU();
        final Runnable runnable = new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$stopSingleUserLU$7(i, userState, z);
            }
        };
        if (this.mInjector.getUserManager().isPreCreated(i)) {
            runnable.run();
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    UserController.this.lambda$stopSingleUserLU$8(i, runnable);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$stopSingleUserLU$5(IStopUserCallback iStopUserCallback, int i) {
        try {
            iStopUserCallback.userStopped(i);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopSingleUserLU$7(final int i, final UserState userState, final boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$stopSingleUserLU$6(i, userState, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopSingleUserLU$8(int i, final Runnable runnable) {
        Intent intent = new Intent("android.intent.action.USER_STOPPING");
        intent.addFlags(1073741824);
        intent.putExtra("android.intent.extra.user_handle", i);
        intent.putExtra("android.intent.extra.SHUTDOWN_USERSPACE_ONLY", true);
        IIntentReceiver iIntentReceiver = new IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.5
            public void performReceive(Intent intent2, int i2, String str, Bundle bundle, boolean z, boolean z2, int i3) {
                runnable.run();
            }
        };
        this.mInjector.clearBroadcastQueueForUser(i);
        this.mInjector.broadcastIntent(intent, null, iIntentReceiver, 0, null, null, new String[]{"android.permission.INTERACT_ACROSS_USERS"}, -1, null, true, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: finishUserStopping, reason: merged with bridge method [inline-methods] */
    public void lambda$stopSingleUserLU$6(int i, final UserState userState, final boolean z) {
        EventLog.writeEvent(EventLogTags.UC_FINISH_USER_STOPPING, i);
        synchronized (this.mLock) {
            if (userState.state != 4) {
                UserJourneyLogger.UserJourneySession logUserJourneyFinishWithError = this.mInjector.getUserJourneyLogger().logUserJourneyFinishWithError(-1, getUserInfo(i), 5, 3);
                if (logUserJourneyFinishWithError != null) {
                    this.mHandler.removeMessages(200, logUserJourneyFinishWithError);
                } else {
                    this.mInjector.getUserJourneyLogger().logUserJourneyFinishWithError(-1, getUserInfo(i), 5, 0);
                }
                return;
            }
            userState.setState(5);
            this.mInjector.getUserManagerInternal().setUserState(i, userState.state);
            this.mInjector.batteryStatsServiceNoteEvent(16391, Integer.toString(i), i);
            this.mInjector.getSystemServiceManager().onUserStopping(i);
            final Runnable runnable = new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    UserController.this.lambda$finishUserStopping$10(userState, z);
                }
            };
            if (this.mInjector.getUserManager().isPreCreated(i)) {
                runnable.run();
                return;
            }
            this.mInjector.broadcastIntent(new Intent("android.intent.action.ACTION_SHUTDOWN"), null, new IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.6
                public void performReceive(Intent intent, int i2, String str, Bundle bundle, boolean z2, boolean z3, int i3) {
                    runnable.run();
                }
            }, 0, null, null, null, -1, null, true, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserStopping$10(final UserState userState, final boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$finishUserStopping$9(userState, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* renamed from: finishUserStopped, reason: merged with bridge method [inline-methods] */
    public void lambda$finishUserStopping$9(UserState userState, boolean z) {
        ArrayList arrayList;
        ArrayList arrayList2;
        boolean z2;
        boolean z3;
        int i;
        int identifier = userState.mHandle.getIdentifier();
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.i("ActivityManager", "finishUserStopped(%d): allowDelayedLocking=%b", new Object[]{Integer.valueOf(identifier), Boolean.valueOf(z)});
        }
        EventLog.writeEvent(EventLogTags.UC_FINISH_USER_STOPPED, identifier);
        UserInfo userInfo = getUserInfo(identifier);
        synchronized (this.mLock) {
            arrayList = new ArrayList(userState.mStopCallbacks);
            arrayList2 = new ArrayList(userState.mKeyEvictedCallbacks);
            z2 = true;
            z3 = false;
            if (this.mStartedUsers.get(identifier) == userState && userState.state == 5) {
                Slogf.i("ActivityManager", "Removing user state from UserController.mStartedUsers for user #" + identifier + " as a result of user being stopped");
                this.mStartedUsers.remove(identifier);
                this.mMaxRunningUsers = this.mUserControllerExt.decreaseCountIfNeed(this.mMaxRunningUsers, identifier);
                this.mUserLru.remove(Integer.valueOf(identifier));
                updateStartedUserArrayLU();
                if (z && !arrayList2.isEmpty()) {
                    Slogf.wtf("ActivityManager", "Delayed locking enabled while KeyEvictedCallbacks not empty, userId:" + identifier + " callbacks:" + arrayList2);
                    z = false;
                }
                i = updateUserToLockLU(identifier, z);
                if (i != -10000) {
                    z3 = true;
                }
            }
            i = identifier;
            z3 = true;
            z2 = false;
        }
        if (z2) {
            Slogf.i("ActivityManager", "Removing user state from UserManager.mUserStates for user #" + identifier + " as a result of user being stopped");
            this.mInjector.getUserManagerInternal().removeUserState(identifier);
            this.mInjector.activityManagerOnUserStopped(identifier);
            forceStopUser(identifier, "finish user");
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            IStopUserCallback iStopUserCallback = (IStopUserCallback) it.next();
            if (z2) {
                try {
                    iStopUserCallback.userStopped(identifier);
                } catch (RemoteException unused) {
                }
            } else {
                iStopUserCallback.userStopAborted(identifier);
            }
        }
        if (z2) {
            this.mInjector.systemServiceManagerOnUserStopped(identifier);
            this.mInjector.taskSupervisorRemoveUser(identifier);
            if (userInfo.isEphemeral() && !userInfo.preCreated) {
                this.mInjector.getUserManager().removeUserEvenWhenDisallowed(identifier);
            }
            UserJourneyLogger.UserJourneySession logUserJourneyFinish = this.mInjector.getUserJourneyLogger().logUserJourneyFinish(-1, userInfo, 5);
            if (logUserJourneyFinish != null) {
                this.mHandler.removeMessages(200, logUserJourneyFinish);
            }
            if (z3) {
                dispatchUserLocking(i, arrayList2);
            }
            resumePendingUserStarts(identifier);
            return;
        }
        UserJourneyLogger.UserJourneySession finishAndClearIncompleteUserJourney = this.mInjector.getUserJourneyLogger().finishAndClearIncompleteUserJourney(identifier, 5);
        if (finishAndClearIncompleteUserJourney != null) {
            this.mHandler.removeMessages(200, finishAndClearIncompleteUserJourney);
        }
    }

    private void resumePendingUserStarts(int i) {
        synchronized (this.mLock) {
            ArrayList arrayList = new ArrayList();
            for (final PendingUserStart pendingUserStart : this.mPendingUserStarts) {
                if (pendingUserStart.userId == i) {
                    Slogf.i("ActivityManager", "resumePendingUserStart for" + pendingUserStart);
                    this.mHandler.post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda12
                        @Override // java.lang.Runnable
                        public final void run() {
                            UserController.this.lambda$resumePendingUserStarts$11(pendingUserStart);
                        }
                    });
                    arrayList.add(pendingUserStart);
                }
            }
            this.mPendingUserStarts.removeAll(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resumePendingUserStarts$11(PendingUserStart pendingUserStart) {
        startUser(pendingUserStart.userId, pendingUserStart.userStartMode, pendingUserStart.unlockListener);
    }

    private void dispatchUserLocking(final int i, final List<UserState.KeyEvictedCallback> list) {
        this.mUserControllerExt.hookFgHandler(FgThread.getHandler()).post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$dispatchUserLocking$12(i, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchUserLocking$12(int i, List list) {
        synchronized (this.mLock) {
            if (this.mStartedUsers.get(i) != null) {
                Slogf.w("ActivityManager", "User was restarted, skipping key eviction");
                return;
            }
            try {
                Slogf.i("ActivityManager", "Locking CE storage for user #" + i);
                this.mInjector.getStorageManager().lockUserKey(i);
                if (list == null) {
                    return;
                }
                for (int i2 = 0; i2 < list.size(); i2++) {
                    ((UserState.KeyEvictedCallback) list.get(i2)).keyEvicted(i);
                }
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    @GuardedBy({"mLock"})
    private int updateUserToLockLU(int i, boolean z) {
        if (!this.mDelayUserDataLocking || !z || getUserInfo(i).isEphemeral() || hasUserRestriction("no_run_in_background", i)) {
            return i;
        }
        this.mLastActiveUsers.remove(Integer.valueOf(i));
        this.mLastActiveUsers.add(0, Integer.valueOf(i));
        if (this.mStartedUsers.size() + this.mLastActiveUsers.size() > this.mMaxRunningUsers) {
            int intValue = this.mLastActiveUsers.get(r4.size() - 1).intValue();
            this.mLastActiveUsers.remove(r2.size() - 1);
            Slogf.i("ActivityManager", "finishUserStopped, stopping user:" + i + " lock user:" + intValue);
            return intValue;
        }
        Slogf.i("ActivityManager", "finishUserStopped, user:" + i + ", skip locking");
        return -10000;
    }

    @GuardedBy({"mLock"})
    private int[] getUsersToStopLU(int i) {
        int size = this.mStartedUsers.size();
        IntArray intArray = new IntArray();
        intArray.add(i);
        int i2 = this.mUserProfileGroupIds.get(i, -10000);
        for (int i3 = 0; i3 < size; i3++) {
            int identifier = this.mStartedUsers.valueAt(i3).mHandle.getIdentifier();
            boolean z = i2 != -10000 && i2 == this.mUserProfileGroupIds.get(identifier, -10000);
            boolean z2 = identifier == i;
            if (z && !z2) {
                intArray.add(identifier);
            }
        }
        return intArray.toArray();
    }

    private void forceStopUser(int i, String str) {
        UserInfo profileParent;
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.i("ActivityManager", "forceStopUser(%d): %s", new Object[]{Integer.valueOf(i), str});
        }
        this.mInjector.activityManagerForceStopPackage(i, str);
        if (this.mInjector.getUserManager().isPreCreated(i)) {
            return;
        }
        Intent intent = new Intent("android.intent.action.USER_STOPPED");
        intent.addFlags(1342177280);
        intent.putExtra("android.intent.extra.user_handle", i);
        this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), -1);
        if (!getUserInfo(i).isProfile() || (profileParent = this.mInjector.getUserManager().getProfileParent(i)) == null) {
            return;
        }
        broadcastProfileAccessibleStateChanged(i, profileParent.id, "android.intent.action.PROFILE_INACCESSIBLE");
    }

    private void stopGuestOrEphemeralUserIfBackground(int i) {
        int i2;
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.i("ActivityManager", "Stop guest or ephemeral user if background: " + i);
        }
        synchronized (this.mLock) {
            UserState userState = this.mStartedUsers.get(i);
            if (i != 0 && i != this.mCurrentUserId && userState != null && (i2 = userState.state) != 4 && i2 != 5) {
                UserInfo userInfo = getUserInfo(i);
                if (userInfo.isEphemeral()) {
                    ((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).onEphemeralUserStop(i);
                }
                if (userInfo.isGuest() || userInfo.isEphemeral()) {
                    synchronized (this.mLock) {
                        stopUsersLU(i, true, false, null, null);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleStartProfiles() {
        this.mUserControllerExt.hookFgHandler(FgThread.getHandler()).post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$scheduleStartProfiles$13();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleStartProfiles$13() {
        if (this.mHandler.hasMessages(40)) {
            return;
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(40), 1000L);
    }

    private void startProfiles() {
        int currentUserId = getCurrentUserId();
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.i("ActivityManager", "startProfilesLocked");
        }
        int i = 0;
        List<UserInfo> profiles = this.mInjector.getUserManager().getProfiles(currentUserId, false);
        ArrayList arrayList = new ArrayList(profiles.size());
        for (UserInfo userInfo : profiles) {
            if ((userInfo.flags & 16) == 16 && userInfo.id != currentUserId && shouldStartWithParent(userInfo)) {
                arrayList.add(userInfo);
            }
        }
        int size = arrayList.size();
        while (i < size && i < this.mUserControllerExt.modifyIfWorkProfileExist(getMaxRunningUsers(), arrayList) - 1) {
            startUser(((UserInfo) arrayList.get(i)).id, 3);
            i++;
        }
        if (i < size) {
            Slogf.w("ActivityManager", "More profiles than MAX_RUNNING_USERS");
        }
    }

    private boolean shouldStartWithParent(UserInfo userInfo) {
        UserProperties userProperties = getUserProperties(userInfo.id);
        return userProperties != null && userProperties.getStartWithParent() && (!userInfo.isQuietModeEnabled() || ((DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class)).isKeepProfilesRunningEnabled());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission(anyOf = {"android.permission.MANAGE_USERS", "android.permission.INTERACT_ACROSS_USERS_FULL"})
    public boolean startProfile(int i, boolean z, IProgressListener iProgressListener) {
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") == -1 && this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == -1) {
            throw new SecurityException("You either need MANAGE_USERS or INTERACT_ACROSS_USERS_FULL permission to start a profile");
        }
        UserInfo userInfo = getUserInfo(i);
        if (userInfo == null || !userInfo.isProfile()) {
            throw new IllegalArgumentException("User " + i + " is not a profile");
        }
        if (!userInfo.isEnabled() && !z) {
            Slogf.w("ActivityManager", "Cannot start disabled profile #%d", new Object[]{Integer.valueOf(i)});
            return false;
        }
        return startUserNoChecks(i, 0, 3, iProgressListener);
    }

    @VisibleForTesting
    boolean startUser(int i, @UserManagerInternal.UserStartMode int i2) {
        return startUser(i, i2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startUser(int i, @UserManagerInternal.UserStartMode int i2, IProgressListener iProgressListener) {
        checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "startUser");
        return startUserNoChecks(i, 0, i2, iProgressListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startUserVisibleOnDisplay(int i, int i2, IProgressListener iProgressListener) {
        checkCallingHasOneOfThosePermissions("startUserOnDisplay", "android.permission.MANAGE_USERS", "android.permission.INTERACT_ACROSS_USERS");
        try {
            return startUserNoChecks(i, i2, 3, iProgressListener);
        } catch (RuntimeException e) {
            Slogf.e("ActivityManager", "startUserOnSecondaryDisplay(%d, %d) failed: %s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), e});
            return false;
        }
    }

    private boolean startUserNoChecks(int i, int i2, @UserManagerInternal.UserStartMode int i3, IProgressListener iProgressListener) {
        String str;
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        StringBuilder sb = new StringBuilder();
        sb.append("UserController.startUser-");
        sb.append(i);
        if (i2 == 0) {
            str = "";
        } else {
            str = "-display-" + i2;
        }
        sb.append(str);
        sb.append("-");
        sb.append(i3 == 1 ? "fg" : "bg");
        sb.append("-start-mode-");
        sb.append(i3);
        timingsTraceAndSlog.traceBegin(sb.toString());
        try {
            return startUserInternal(i, i2, i3, iProgressListener, timingsTraceAndSlog);
        } finally {
            timingsTraceAndSlog.traceEnd();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0362 A[Catch: all -> 0x040a, TryCatch #3 {all -> 0x040a, blocks: (B:13:0x0075, B:15:0x0080, B:17:0x0086, B:19:0x0090, B:22:0x0095, B:24:0x0099, B:25:0x009c, B:29:0x00a3, B:31:0x00a8, B:32:0x00b8, B:34:0x00c6, B:38:0x00e2, B:40:0x00e8, B:45:0x012e, B:47:0x0148, B:51:0x016f, B:53:0x0175, B:54:0x017a, B:55:0x0182, B:64:0x01fd, B:65:0x0202, B:67:0x0207, B:68:0x021b, B:70:0x0246, B:71:0x024f, B:75:0x0259, B:77:0x0271, B:79:0x0286, B:81:0x028e, B:84:0x029b, B:86:0x02a3, B:87:0x02b2, B:88:0x02d7, B:90:0x02e5, B:91:0x02fd, B:95:0x0302, B:97:0x0331, B:99:0x0335, B:100:0x035a, B:102:0x0362, B:103:0x0392, B:107:0x039e, B:114:0x03b8, B:116:0x03c1, B:119:0x03f6, B:122:0x03ea, B:123:0x03d8, B:124:0x03b1, B:130:0x0308, B:133:0x030c, B:134:0x0323, B:138:0x0328, B:142:0x0330, B:143:0x02ad, B:149:0x02bd, B:150:0x02be, B:151:0x02cb, B:158:0x0406, B:168:0x0409, B:169:0x010b, B:171:0x010f, B:153:0x02cc, B:154:0x02d6, B:93:0x02fe, B:94:0x0301, B:57:0x0183, B:59:0x018e, B:61:0x01ec, B:62:0x01fa, B:159:0x01b7, B:161:0x01bb, B:162:0x01e3, B:73:0x0250, B:74:0x0258, B:136:0x0324, B:137:0x0327), top: B:12:0x0075, inners: #0, #1, #2, #4, #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0398  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x039e A[Catch: all -> 0x040a, TryCatch #3 {all -> 0x040a, blocks: (B:13:0x0075, B:15:0x0080, B:17:0x0086, B:19:0x0090, B:22:0x0095, B:24:0x0099, B:25:0x009c, B:29:0x00a3, B:31:0x00a8, B:32:0x00b8, B:34:0x00c6, B:38:0x00e2, B:40:0x00e8, B:45:0x012e, B:47:0x0148, B:51:0x016f, B:53:0x0175, B:54:0x017a, B:55:0x0182, B:64:0x01fd, B:65:0x0202, B:67:0x0207, B:68:0x021b, B:70:0x0246, B:71:0x024f, B:75:0x0259, B:77:0x0271, B:79:0x0286, B:81:0x028e, B:84:0x029b, B:86:0x02a3, B:87:0x02b2, B:88:0x02d7, B:90:0x02e5, B:91:0x02fd, B:95:0x0302, B:97:0x0331, B:99:0x0335, B:100:0x035a, B:102:0x0362, B:103:0x0392, B:107:0x039e, B:114:0x03b8, B:116:0x03c1, B:119:0x03f6, B:122:0x03ea, B:123:0x03d8, B:124:0x03b1, B:130:0x0308, B:133:0x030c, B:134:0x0323, B:138:0x0328, B:142:0x0330, B:143:0x02ad, B:149:0x02bd, B:150:0x02be, B:151:0x02cb, B:158:0x0406, B:168:0x0409, B:169:0x010b, B:171:0x010f, B:153:0x02cc, B:154:0x02d6, B:93:0x02fe, B:94:0x0301, B:57:0x0183, B:59:0x018e, B:61:0x01ec, B:62:0x01fa, B:159:0x01b7, B:161:0x01bb, B:162:0x01e3, B:73:0x0250, B:74:0x0258, B:136:0x0324, B:137:0x0327), top: B:12:0x0075, inners: #0, #1, #2, #4, #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x03a9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x03c1 A[Catch: all -> 0x040a, TryCatch #3 {all -> 0x040a, blocks: (B:13:0x0075, B:15:0x0080, B:17:0x0086, B:19:0x0090, B:22:0x0095, B:24:0x0099, B:25:0x009c, B:29:0x00a3, B:31:0x00a8, B:32:0x00b8, B:34:0x00c6, B:38:0x00e2, B:40:0x00e8, B:45:0x012e, B:47:0x0148, B:51:0x016f, B:53:0x0175, B:54:0x017a, B:55:0x0182, B:64:0x01fd, B:65:0x0202, B:67:0x0207, B:68:0x021b, B:70:0x0246, B:71:0x024f, B:75:0x0259, B:77:0x0271, B:79:0x0286, B:81:0x028e, B:84:0x029b, B:86:0x02a3, B:87:0x02b2, B:88:0x02d7, B:90:0x02e5, B:91:0x02fd, B:95:0x0302, B:97:0x0331, B:99:0x0335, B:100:0x035a, B:102:0x0362, B:103:0x0392, B:107:0x039e, B:114:0x03b8, B:116:0x03c1, B:119:0x03f6, B:122:0x03ea, B:123:0x03d8, B:124:0x03b1, B:130:0x0308, B:133:0x030c, B:134:0x0323, B:138:0x0328, B:142:0x0330, B:143:0x02ad, B:149:0x02bd, B:150:0x02be, B:151:0x02cb, B:158:0x0406, B:168:0x0409, B:169:0x010b, B:171:0x010f, B:153:0x02cc, B:154:0x02d6, B:93:0x02fe, B:94:0x0301, B:57:0x0183, B:59:0x018e, B:61:0x01ec, B:62:0x01fa, B:159:0x01b7, B:161:0x01bb, B:162:0x01e3, B:73:0x0250, B:74:0x0258, B:136:0x0324, B:137:0x0327), top: B:12:0x0075, inners: #0, #1, #2, #4, #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x03e8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x03d8 A[Catch: all -> 0x040a, TryCatch #3 {all -> 0x040a, blocks: (B:13:0x0075, B:15:0x0080, B:17:0x0086, B:19:0x0090, B:22:0x0095, B:24:0x0099, B:25:0x009c, B:29:0x00a3, B:31:0x00a8, B:32:0x00b8, B:34:0x00c6, B:38:0x00e2, B:40:0x00e8, B:45:0x012e, B:47:0x0148, B:51:0x016f, B:53:0x0175, B:54:0x017a, B:55:0x0182, B:64:0x01fd, B:65:0x0202, B:67:0x0207, B:68:0x021b, B:70:0x0246, B:71:0x024f, B:75:0x0259, B:77:0x0271, B:79:0x0286, B:81:0x028e, B:84:0x029b, B:86:0x02a3, B:87:0x02b2, B:88:0x02d7, B:90:0x02e5, B:91:0x02fd, B:95:0x0302, B:97:0x0331, B:99:0x0335, B:100:0x035a, B:102:0x0362, B:103:0x0392, B:107:0x039e, B:114:0x03b8, B:116:0x03c1, B:119:0x03f6, B:122:0x03ea, B:123:0x03d8, B:124:0x03b1, B:130:0x0308, B:133:0x030c, B:134:0x0323, B:138:0x0328, B:142:0x0330, B:143:0x02ad, B:149:0x02bd, B:150:0x02be, B:151:0x02cb, B:158:0x0406, B:168:0x0409, B:169:0x010b, B:171:0x010f, B:153:0x02cc, B:154:0x02d6, B:93:0x02fe, B:94:0x0301, B:57:0x0183, B:59:0x018e, B:61:0x01ec, B:62:0x01fa, B:159:0x01b7, B:161:0x01bb, B:162:0x01e3, B:73:0x0250, B:74:0x0258, B:136:0x0324, B:137:0x0327), top: B:12:0x0075, inners: #0, #1, #2, #4, #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x039a  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0309  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x02e5 A[Catch: all -> 0x040a, TryCatch #3 {all -> 0x040a, blocks: (B:13:0x0075, B:15:0x0080, B:17:0x0086, B:19:0x0090, B:22:0x0095, B:24:0x0099, B:25:0x009c, B:29:0x00a3, B:31:0x00a8, B:32:0x00b8, B:34:0x00c6, B:38:0x00e2, B:40:0x00e8, B:45:0x012e, B:47:0x0148, B:51:0x016f, B:53:0x0175, B:54:0x017a, B:55:0x0182, B:64:0x01fd, B:65:0x0202, B:67:0x0207, B:68:0x021b, B:70:0x0246, B:71:0x024f, B:75:0x0259, B:77:0x0271, B:79:0x0286, B:81:0x028e, B:84:0x029b, B:86:0x02a3, B:87:0x02b2, B:88:0x02d7, B:90:0x02e5, B:91:0x02fd, B:95:0x0302, B:97:0x0331, B:99:0x0335, B:100:0x035a, B:102:0x0362, B:103:0x0392, B:107:0x039e, B:114:0x03b8, B:116:0x03c1, B:119:0x03f6, B:122:0x03ea, B:123:0x03d8, B:124:0x03b1, B:130:0x0308, B:133:0x030c, B:134:0x0323, B:138:0x0328, B:142:0x0330, B:143:0x02ad, B:149:0x02bd, B:150:0x02be, B:151:0x02cb, B:158:0x0406, B:168:0x0409, B:169:0x010b, B:171:0x010f, B:153:0x02cc, B:154:0x02d6, B:93:0x02fe, B:94:0x0301, B:57:0x0183, B:59:0x018e, B:61:0x01ec, B:62:0x01fa, B:159:0x01b7, B:161:0x01bb, B:162:0x01e3, B:73:0x0250, B:74:0x0258, B:136:0x0324, B:137:0x0327), top: B:12:0x0075, inners: #0, #1, #2, #4, #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0335 A[Catch: all -> 0x040a, TryCatch #3 {all -> 0x040a, blocks: (B:13:0x0075, B:15:0x0080, B:17:0x0086, B:19:0x0090, B:22:0x0095, B:24:0x0099, B:25:0x009c, B:29:0x00a3, B:31:0x00a8, B:32:0x00b8, B:34:0x00c6, B:38:0x00e2, B:40:0x00e8, B:45:0x012e, B:47:0x0148, B:51:0x016f, B:53:0x0175, B:54:0x017a, B:55:0x0182, B:64:0x01fd, B:65:0x0202, B:67:0x0207, B:68:0x021b, B:70:0x0246, B:71:0x024f, B:75:0x0259, B:77:0x0271, B:79:0x0286, B:81:0x028e, B:84:0x029b, B:86:0x02a3, B:87:0x02b2, B:88:0x02d7, B:90:0x02e5, B:91:0x02fd, B:95:0x0302, B:97:0x0331, B:99:0x0335, B:100:0x035a, B:102:0x0362, B:103:0x0392, B:107:0x039e, B:114:0x03b8, B:116:0x03c1, B:119:0x03f6, B:122:0x03ea, B:123:0x03d8, B:124:0x03b1, B:130:0x0308, B:133:0x030c, B:134:0x0323, B:138:0x0328, B:142:0x0330, B:143:0x02ad, B:149:0x02bd, B:150:0x02be, B:151:0x02cb, B:158:0x0406, B:168:0x0409, B:169:0x010b, B:171:0x010f, B:153:0x02cc, B:154:0x02d6, B:93:0x02fe, B:94:0x0301, B:57:0x0183, B:59:0x018e, B:61:0x01ec, B:62:0x01fa, B:159:0x01b7, B:161:0x01bb, B:162:0x01e3, B:73:0x0250, B:74:0x0258, B:136:0x0324, B:137:0x0327), top: B:12:0x0075, inners: #0, #1, #2, #4, #5 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean startUserInternal(int i, int i2, @UserManagerInternal.UserStartMode int i3, IProgressListener iProgressListener, TimingsTraceAndSlog timingsTraceAndSlog) {
        UserState userState;
        boolean z;
        int i4;
        int i5;
        boolean z2;
        boolean z3;
        int i6;
        int i7;
        long j;
        boolean z4;
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.i("ActivityManager", "Starting user %d on display %d with mode  %s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), UserManagerInternal.userStartModeToString(i3)});
        }
        int i8 = i3 == 1 ? 1 : 0;
        boolean z5 = i2 != 0;
        if (z5) {
            Preconditions.checkArgument(i8 ^ 1, "Cannot start user %d in foreground AND on secondary display (%d)", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        }
        EventLog.writeEvent(EventLogTags.UC_START_USER_INTERNAL, Integer.valueOf(i), Integer.valueOf(i8), Integer.valueOf(i2));
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            timingsTraceAndSlog.traceBegin("getStartedUserState");
            int currentUserId = getCurrentUserId();
            if (currentUserId == i) {
                UserState startedUserState = getStartedUserState(i);
                if (startedUserState == null) {
                    Slogf.wtf("ActivityManager", "Current user has no UserState");
                } else if (i != 0 || startedUserState.state != 0) {
                    if (startedUserState.state == 3) {
                        notifyFinished(i, iProgressListener);
                    }
                    timingsTraceAndSlog.traceEnd();
                    return true;
                }
            }
            timingsTraceAndSlog.traceEnd();
            if (i8 != 0) {
                timingsTraceAndSlog.traceBegin("clearAllLockedTasks");
                this.mInjector.clearAllLockedTasks("startUser");
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("getUserInfo");
            UserInfo userInfo = getUserInfo(i);
            timingsTraceAndSlog.traceEnd();
            if (userInfo == null) {
                Slogf.w("ActivityManager", "No user info for user #" + i);
                return false;
            }
            if (i8 != 0 && userInfo.isProfile()) {
                Slogf.w("ActivityManager", "Cannot switch to User #" + i + ": not a full user");
                return false;
            }
            if ((i8 != 0 || z5) && userInfo.preCreated) {
                Slogf.w("ActivityManager", "Cannot start pre-created user #" + i + " in foreground or on secondary display");
                return false;
            }
            timingsTraceAndSlog.traceBegin("assignUserToDisplayOnStart");
            int assignUserToDisplayOnStart = this.mInjector.getUserManagerInternal().assignUserToDisplayOnStart(i, userInfo.profileGroupId, i3, i2);
            timingsTraceAndSlog.traceEnd();
            if (assignUserToDisplayOnStart == -1) {
                Slogf.e("ActivityManager", "%s user(%d) / display (%d) assignment failed: %s", new Object[]{UserManagerInternal.userStartModeToString(i3), Integer.valueOf(i), Integer.valueOf(i2), UserManagerInternal.userAssignmentResultToString(assignUserToDisplayOnStart)});
                return false;
            }
            if (i8 != 0 && isUserSwitchUiEnabled()) {
                this.mUserControllerExt.startFreezingScreenIfNeeded(currentUserId, i);
            }
            timingsTraceAndSlog.traceBegin("updateStartedUserArrayStarting");
            synchronized (this.mLock) {
                UserState userState2 = this.mStartedUsers.get(i);
                if (userState2 == null) {
                    UserState userState3 = new UserState(UserHandle.of(i));
                    userState3.mUnlockProgress.addListener(new UserProgressListener());
                    this.mStartedUsers.put(i, userState3);
                    this.mMaxRunningUsers = this.mUserControllerExt.increaseCountIfNeed(this.mMaxRunningUsers, i);
                    updateStartedUserArrayLU();
                    userState = userState3;
                    z = true;
                } else {
                    if (userState2.state == 5) {
                        Slogf.i("ActivityManager", "User #" + i + " is shutting down - will start after full shutdown");
                        this.mPendingUserStarts.add(new PendingUserStart(i, i3, iProgressListener));
                        timingsTraceAndSlog.traceEnd();
                        return true;
                    }
                    userState = userState2;
                    z = false;
                }
                boolean z6 = z;
                Integer valueOf = Integer.valueOf(i);
                this.mUserLru.remove(valueOf);
                this.mUserLru.add(valueOf);
                if (iProgressListener != null) {
                    userState.mUnlockProgress.addListener(iProgressListener);
                }
                timingsTraceAndSlog.traceEnd();
                if (z) {
                    timingsTraceAndSlog.traceBegin("setUserState");
                    this.mInjector.getUserManagerInternal().setUserState(i, userState.state);
                    timingsTraceAndSlog.traceEnd();
                }
                long elapsedRealtime2 = SystemClock.elapsedRealtime();
                UserState userState4 = userState;
                this.mUserControllerExt.startUserInternalEnter(i8, currentUserId, i, elapsedRealtime, -1L, -1L, i8);
                timingsTraceAndSlog.traceBegin("updateConfigurationAndProfileIds");
                if (i8 == 0) {
                    i4 = currentUserId;
                    Integer valueOf2 = Integer.valueOf(this.mCurrentUserId);
                    updateProfileRelatedCaches();
                    synchronized (this.mLock) {
                        this.mUserLru.remove(valueOf2);
                        this.mUserLru.add(valueOf2);
                    }
                    timingsTraceAndSlog.traceEnd();
                    long elapsedRealtime3 = SystemClock.elapsedRealtime() - elapsedRealtime2;
                    i5 = userState4.state;
                    if (i5 == 4) {
                    }
                    if (userState4.state == 0) {
                    }
                    timingsTraceAndSlog.traceBegin("sendMessages");
                    if (i8 != 0) {
                    }
                    if (userInfo.preCreated) {
                    }
                    if (i == 0) {
                    }
                    if (!z3) {
                    }
                    i6 = callingPid;
                    i7 = callingUid;
                    sendUserStartedBroadcast(i, i7, i6);
                    timingsTraceAndSlog.traceEnd();
                    long elapsedRealtime4 = SystemClock.elapsedRealtime();
                    if (i8 != 0) {
                    }
                    if (!z3) {
                    }
                    timingsTraceAndSlog.traceBegin("sendRestartBroadcast");
                    sendUserStartingBroadcast(i, i7, i6);
                    timingsTraceAndSlog.traceEnd();
                    this.mUserControllerExt.startUserInternalExit(j, elapsedRealtime3, i4, i, elapsedRealtime);
                    return true;
                }
                this.mInjector.reportGlobalUsageEvent(16);
                synchronized (this.mLock) {
                    this.mCurrentUserId = i;
                    this.mTargetUserId = -10000;
                    z4 = this.mUserSwitchUiEnabled;
                }
                this.mInjector.updateUserConfiguration();
                updateProfileRelatedCaches();
                this.mInjector.getWindowManager().setCurrentUser(i);
                this.mInjector.reportCurWakefulnessUsageEvent();
                if (z4) {
                    this.mInjector.getWindowManager().setSwitchingUser(true);
                    if (this.mInjector.getKeyguardManager().isDeviceSecure(i)) {
                        if (!this.mUserControllerExt.isMultiSystemUserId(i)) {
                            i4 = currentUserId;
                            if (this.mUserControllerExt.isMultiSystemUserId(i4)) {
                            }
                            this.mInjector.lockDeviceNowAndWaitForKeyguardShown();
                            this.mUserControllerExt.setWaitForKeyguardShown(true);
                            timingsTraceAndSlog.traceEnd();
                            long elapsedRealtime32 = SystemClock.elapsedRealtime() - elapsedRealtime2;
                            i5 = userState4.state;
                            if (i5 == 4) {
                                timingsTraceAndSlog.traceBegin("updateStateStopping");
                                userState4.setState(userState4.lastState);
                                this.mInjector.getUserManagerInternal().setUserState(i, userState4.state);
                                synchronized (this.mLock) {
                                    updateStartedUserArrayLU();
                                }
                                timingsTraceAndSlog.traceEnd();
                                z6 = true;
                            } else if (i5 == 5) {
                                timingsTraceAndSlog.traceBegin("updateStateShutdown");
                                userState4.setState(0);
                                this.mInjector.getUserManagerInternal().setUserState(i, userState4.state);
                                synchronized (this.mLock) {
                                    updateStartedUserArrayLU();
                                }
                                timingsTraceAndSlog.traceEnd();
                                z6 = true;
                            }
                            if (userState4.state == 0) {
                                timingsTraceAndSlog.traceBegin("updateStateBooting");
                                this.mInjector.getUserManager().onBeforeStartUser(i);
                                Handler handler = this.mHandler;
                                z2 = false;
                                handler.sendMessage(handler.obtainMessage(50, i, 0));
                                timingsTraceAndSlog.traceEnd();
                                this.mUserControllerExt.userStart(i);
                            } else {
                                z2 = false;
                            }
                            timingsTraceAndSlog.traceBegin("sendMessages");
                            if (i8 != 0) {
                                Handler handler2 = this.mHandler;
                                handler2.sendMessage(handler2.obtainMessage(60, i, i4));
                                this.mHandler.removeMessages(10);
                                this.mHandler.removeMessages(30);
                                Handler handler3 = this.mHandler;
                                handler3.sendMessage(handler3.obtainMessage(10, i4, i, userState4));
                                Handler handler4 = this.mHandler;
                                handler4.sendMessageDelayed(handler4.obtainMessage(30, i4, i, userState4), getUserSwitchTimeoutMs());
                            }
                            z3 = userInfo.preCreated ? z2 : z6;
                            if (i == 0 && this.mInjector.isHeadlessSystemUserMode()) {
                                z2 = true;
                            }
                            if (!z3 && !z2) {
                                i6 = callingPid;
                                i7 = callingUid;
                                timingsTraceAndSlog.traceEnd();
                                long elapsedRealtime42 = SystemClock.elapsedRealtime();
                                if (i8 != 0) {
                                    timingsTraceAndSlog.traceBegin("moveUserToForeground");
                                    moveUserToForeground(userState4, i);
                                    timingsTraceAndSlog.traceEnd();
                                    j = SystemClock.elapsedRealtime() - elapsedRealtime42;
                                    this.mUserControllerExt.switchUser(i, i4);
                                } else {
                                    timingsTraceAndSlog.traceBegin("finishUserBoot");
                                    finishUserBoot(userState4);
                                    timingsTraceAndSlog.traceEnd();
                                    j = 0;
                                }
                                if (!z3 || z2) {
                                    timingsTraceAndSlog.traceBegin("sendRestartBroadcast");
                                    sendUserStartingBroadcast(i, i7, i6);
                                    timingsTraceAndSlog.traceEnd();
                                }
                                this.mUserControllerExt.startUserInternalExit(j, elapsedRealtime32, i4, i, elapsedRealtime);
                                return true;
                            }
                            i6 = callingPid;
                            i7 = callingUid;
                            sendUserStartedBroadcast(i, i7, i6);
                            timingsTraceAndSlog.traceEnd();
                            long elapsedRealtime422 = SystemClock.elapsedRealtime();
                            if (i8 != 0) {
                            }
                            if (!z3) {
                            }
                            timingsTraceAndSlog.traceBegin("sendRestartBroadcast");
                            sendUserStartingBroadcast(i, i7, i6);
                            timingsTraceAndSlog.traceEnd();
                            this.mUserControllerExt.startUserInternalExit(j, elapsedRealtime32, i4, i, elapsedRealtime);
                            return true;
                        }
                        i4 = currentUserId;
                        if (!this.mUserControllerExt.getWaitForKeyguardShown()) {
                            this.mInjector.getWindowManager().lockDeviceNow();
                            this.mUserControllerExt.setWaitForKeyguardShown(true);
                            timingsTraceAndSlog.traceEnd();
                            long elapsedRealtime322 = SystemClock.elapsedRealtime() - elapsedRealtime2;
                            i5 = userState4.state;
                            if (i5 == 4) {
                            }
                            if (userState4.state == 0) {
                            }
                            timingsTraceAndSlog.traceBegin("sendMessages");
                            if (i8 != 0) {
                            }
                            if (userInfo.preCreated) {
                            }
                            if (i == 0) {
                                z2 = true;
                            }
                            if (!z3) {
                                i6 = callingPid;
                                i7 = callingUid;
                                timingsTraceAndSlog.traceEnd();
                                long elapsedRealtime4222 = SystemClock.elapsedRealtime();
                                if (i8 != 0) {
                                }
                                if (!z3) {
                                }
                                timingsTraceAndSlog.traceBegin("sendRestartBroadcast");
                                sendUserStartingBroadcast(i, i7, i6);
                                timingsTraceAndSlog.traceEnd();
                                this.mUserControllerExt.startUserInternalExit(j, elapsedRealtime322, i4, i, elapsedRealtime);
                                return true;
                            }
                            i6 = callingPid;
                            i7 = callingUid;
                            sendUserStartedBroadcast(i, i7, i6);
                            timingsTraceAndSlog.traceEnd();
                            long elapsedRealtime42222 = SystemClock.elapsedRealtime();
                            if (i8 != 0) {
                            }
                            if (!z3) {
                            }
                            timingsTraceAndSlog.traceBegin("sendRestartBroadcast");
                            sendUserStartingBroadcast(i, i7, i6);
                            timingsTraceAndSlog.traceEnd();
                            this.mUserControllerExt.startUserInternalExit(j, elapsedRealtime322, i4, i, elapsedRealtime);
                            return true;
                        }
                        this.mInjector.lockDeviceNowAndWaitForKeyguardShown();
                        this.mUserControllerExt.setWaitForKeyguardShown(true);
                        timingsTraceAndSlog.traceEnd();
                        long elapsedRealtime3222 = SystemClock.elapsedRealtime() - elapsedRealtime2;
                        i5 = userState4.state;
                        if (i5 == 4) {
                        }
                        if (userState4.state == 0) {
                        }
                        timingsTraceAndSlog.traceBegin("sendMessages");
                        if (i8 != 0) {
                        }
                        if (userInfo.preCreated) {
                        }
                        if (i == 0) {
                        }
                        if (!z3) {
                        }
                        i6 = callingPid;
                        i7 = callingUid;
                        sendUserStartedBroadcast(i, i7, i6);
                        timingsTraceAndSlog.traceEnd();
                        long elapsedRealtime422222 = SystemClock.elapsedRealtime();
                        if (i8 != 0) {
                        }
                        if (!z3) {
                        }
                        timingsTraceAndSlog.traceBegin("sendRestartBroadcast");
                        sendUserStartingBroadcast(i, i7, i6);
                        timingsTraceAndSlog.traceEnd();
                        this.mUserControllerExt.startUserInternalExit(j, elapsedRealtime3222, i4, i, elapsedRealtime);
                        return true;
                    }
                }
                i4 = currentUserId;
                timingsTraceAndSlog.traceEnd();
                long elapsedRealtime32222 = SystemClock.elapsedRealtime() - elapsedRealtime2;
                i5 = userState4.state;
                if (i5 == 4) {
                }
                if (userState4.state == 0) {
                }
                timingsTraceAndSlog.traceBegin("sendMessages");
                if (i8 != 0) {
                }
                if (userInfo.preCreated) {
                }
                if (i == 0) {
                }
                if (!z3) {
                }
                i6 = callingPid;
                i7 = callingUid;
                sendUserStartedBroadcast(i, i7, i6);
                timingsTraceAndSlog.traceEnd();
                long elapsedRealtime4222222 = SystemClock.elapsedRealtime();
                if (i8 != 0) {
                }
                if (!z3) {
                }
                timingsTraceAndSlog.traceBegin("sendRestartBroadcast");
                sendUserStartingBroadcast(i, i7, i6);
                timingsTraceAndSlog.traceEnd();
                this.mUserControllerExt.startUserInternalExit(j, elapsedRealtime32222, i4, i, elapsedRealtime);
                return true;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    void startUserInForeground(int i) {
        if (startUser(i, 1)) {
            return;
        }
        this.mInjector.getWindowManager().setSwitchingUser(false);
        this.mTargetUserId = -10000;
        dismissUserSwitchDialog(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean unlockUser(int i, IProgressListener iProgressListener) {
        checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "unlockUser");
        EventLog.writeEvent(EventLogTags.UC_UNLOCK_USER, i);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return maybeUnlockUser(i, iProgressListener);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private static void notifyFinished(int i, IProgressListener iProgressListener) {
        if (iProgressListener == null) {
            return;
        }
        try {
            iProgressListener.onFinished(i, (Bundle) null);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeUnlockUser(int i) {
        return maybeUnlockUser(i, null);
    }

    private boolean maybeUnlockUser(int i, IProgressListener iProgressListener) {
        UserState userState;
        int size;
        int[] iArr;
        if (!this.mAllowUserUnlocking) {
            Slogf.i("ActivityManager", "Not unlocking user %d yet because boot hasn't completed", new Object[]{Integer.valueOf(i)});
            notifyFinished(i, iProgressListener);
            return false;
        }
        this.mUserControllerExt.ormsUnlockUserBoost(500);
        if (!StorageManager.isUserKeyUnlocked(i)) {
            this.mLockPatternUtils.unlockUserKeyIfUnsecured(i);
        }
        synchronized (this.mLock) {
            userState = this.mStartedUsers.get(i);
            if (userState != null) {
                userState.mUnlockProgress.addListener(iProgressListener);
            }
        }
        if (userState == null) {
            notifyFinished(i, iProgressListener);
            return false;
        }
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        timingsTraceAndSlog.traceBegin("finishUserUnlocking-" + i);
        boolean finishUserUnlocking = finishUserUnlocking(userState);
        timingsTraceAndSlog.traceEnd();
        if (!finishUserUnlocking) {
            notifyFinished(i, iProgressListener);
            return false;
        }
        synchronized (this.mLock) {
            size = this.mStartedUsers.size();
            iArr = new int[size];
            for (int i2 = 0; i2 < size; i2++) {
                iArr[i2] = this.mStartedUsers.keyAt(i2);
            }
        }
        for (int i3 = 0; i3 < size; i3++) {
            int i4 = iArr[i3];
            UserInfo profileParent = this.mInjector.getUserManager().getProfileParent(i4);
            if (profileParent != null && profileParent.id == i && i4 != i) {
                Slogf.d("ActivityManager", "User " + i4 + " (parent " + profileParent.id + "): attempting unlock because parent was just unlocked");
                maybeUnlockUser(i4);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean switchUser(int i) {
        int callingUid = Binder.getCallingUid();
        enforceShellRestriction("no_debugging_features", i);
        EventLog.writeEvent(EventLogTags.UC_SWITCH_USER, i);
        int currentUserId = getCurrentUserId();
        UserInfo userInfo = getUserInfo(i);
        if (i == currentUserId) {
            Slogf.i("ActivityManager", "user #" + i + " is already the current user");
            return true;
        }
        if (userInfo == null) {
            Slogf.w("ActivityManager", "No user info for user #" + i);
            return false;
        }
        if (!userInfo.supportsSwitchTo()) {
            Slogf.w("ActivityManager", "Cannot switch to User #" + i + ": not supported");
            return false;
        }
        if (FactoryResetter.isFactoryResetting()) {
            Slogf.w("ActivityManager", "Cannot switch to User #" + i + ": factory reset in progress");
            return false;
        }
        synchronized (this.mLock) {
            if (!this.mInitialized) {
                Slogf.e("ActivityManager", "Cannot switch to User #" + i + ": UserController not ready yet");
                return false;
            }
            this.mTargetUserId = i;
            boolean z = this.mUserSwitchUiEnabled;
            this.mUserControllerExt.switchUser(z, getUserInfo(currentUserId), userInfo, callingUid);
            this.mUserControllerExt.ormsSwitchUserBoost(5000);
            if (z) {
                Pair pair = new Pair(getUserInfo(currentUserId), userInfo);
                this.mUiHandler.removeMessages(1000);
                Handler handler = this.mUiHandler;
                handler.sendMessage(handler.obtainMessage(1000, pair));
            } else {
                sendStartUserSwitchFgMessage(i);
            }
            return true;
        }
    }

    private void sendStartUserSwitchFgMessage(int i) {
        this.mHandler.removeMessages(120);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(120, i, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissUserSwitchDialog(final Runnable runnable) {
        this.mUiHandler.post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$dismissUserSwitchDialog$14(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissUserSwitchDialog$14(Runnable runnable) {
        this.mInjector.dismissUserSwitchingDialog(runnable);
    }

    private void showUserSwitchDialog(final Pair<UserInfo, UserInfo> pair) {
        if (this.mUserControllerExt.hookShowUserSwitchDialog((UserInfo) pair.first, (UserInfo) pair.second)) {
            return;
        }
        this.mInjector.showUserSwitchingDialog((UserInfo) pair.first, (UserInfo) pair.second, getSwitchingFromSystemUserMessageUnchecked(), getSwitchingToSystemUserMessageUnchecked(), new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$showUserSwitchDialog$15(pair);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showUserSwitchDialog$15(Pair pair) {
        sendStartUserSwitchFgMessage(((UserInfo) pair.second).id);
    }

    private void dispatchForegroundProfileChanged(int i) {
        int beginBroadcast = this.mUserSwitchObservers.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mUserSwitchObservers.getBroadcastItem(i2).onForegroundProfileSwitch(i);
            } catch (RemoteException unused) {
            }
        }
        this.mUserSwitchObservers.finishBroadcast();
    }

    @VisibleForTesting
    void dispatchUserSwitchComplete(int i, int i2) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        timingsTraceAndSlog.traceBegin("dispatchUserSwitchComplete-" + i2);
        this.mInjector.getWindowManager().setSwitchingUser(false);
        int beginBroadcast = this.mUserSwitchObservers.beginBroadcast();
        for (int i3 = 0; i3 < beginBroadcast; i3++) {
            try {
                timingsTraceAndSlog.traceBegin("onUserSwitchComplete-" + i2 + " #" + i3 + " " + this.mUserSwitchObservers.getBroadcastCookie(i3));
                this.mUserSwitchObservers.getBroadcastItem(i3).onUserSwitchComplete(i2);
                timingsTraceAndSlog.traceEnd();
            } catch (RemoteException unused) {
            }
        }
        this.mUserSwitchObservers.finishBroadcast();
        timingsTraceAndSlog.traceBegin("sendUserSwitchBroadcasts-" + i + "-" + i2);
        sendUserSwitchBroadcasts(i, i2);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceEnd();
    }

    private void dispatchLockedBootComplete(int i) {
        int beginBroadcast = this.mUserSwitchObservers.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mUserSwitchObservers.getBroadcastItem(i2).onLockedBootComplete(i);
            } catch (RemoteException unused) {
            }
        }
        this.mUserSwitchObservers.finishBroadcast();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x001f A[Catch: all -> 0x001a, TryCatch #0 {all -> 0x001a, blocks: (B:22:0x0011, B:10:0x001f, B:12:0x0023, B:13:0x0033, B:15:0x0035, B:17:0x0039, B:18:0x0049, B:19:0x0052), top: B:21:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0035 A[Catch: all -> 0x001a, TryCatch #0 {all -> 0x001a, blocks: (B:22:0x0011, B:10:0x001f, B:12:0x0023, B:13:0x0033, B:15:0x0035, B:17:0x0039, B:18:0x0049, B:19:0x0052), top: B:21:0x0011 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void stopUserOnSwitchIfEnforced(int i) {
        boolean z;
        if (i == 0) {
            return;
        }
        boolean hasUserRestriction = hasUserRestriction("no_run_in_background", i);
        synchronized (this.mLock) {
            if (!hasUserRestriction) {
                try {
                    if (!shouldStopUserOnSwitch()) {
                        z = false;
                        if (z) {
                            if (ActivityManagerDebugConfig.DEBUG_MU) {
                                Slogf.i("ActivityManager", "stopUserOnSwitchIfEnforced() NOT stopping %d and related users", new Object[]{Integer.valueOf(i)});
                            }
                            return;
                        } else {
                            if (ActivityManagerDebugConfig.DEBUG_MU) {
                                Slogf.i("ActivityManager", "stopUserOnSwitchIfEnforced() stopping %d and related users", new Object[]{Integer.valueOf(i)});
                            }
                            stopUsersLU(i, false, true, null, null);
                            return;
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            z = true;
            if (z) {
            }
        }
    }

    private void timeoutUserSwitch(UserState userState, int i, int i2) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog("ActivityManager");
        timingsTraceAndSlog.traceBegin("timeoutUserSwitch-" + i + "-to-" + i2);
        synchronized (this.mLock) {
            Slogf.e("ActivityManager", "User switch timeout: from " + i + " to " + i2);
            this.mTimeoutUserSwitchCallbacks = this.mCurWaitingUserSwitchCallbacks;
            this.mUserControllerExt.timeoutUserSwitch(this.mCurWaitingUserSwitchCallbacks, userState, i, i2);
            this.mHandler.removeMessages(90);
            sendContinueUserSwitchLU(userState, i, i2);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(90, i, i2), 5000L);
        }
        timingsTraceAndSlog.traceEnd();
    }

    private void timeoutUserSwitchCallbacks(int i, int i2) {
        synchronized (this.mLock) {
            ArraySet<String> arraySet = this.mTimeoutUserSwitchCallbacks;
            if (arraySet != null && !arraySet.isEmpty()) {
                Slogf.wtf("ActivityManager", "User switch timeout: from " + i + " to " + i2 + ". Observers that didn't respond: " + this.mTimeoutUserSwitchCallbacks);
                this.mTimeoutUserSwitchCallbacks = null;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.android.server.am.UserController] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v21 */
    /* JADX WARN: Type inference failed for: r2v22 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r3v10, types: [android.util.TimingsTraceLog] */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v3, types: [long] */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r4v8, types: [java.lang.String] */
    @VisibleForTesting
    void dispatchUserSwitch(final UserState userState, final int i, final int i2) {
        TimingsTraceAndSlog timingsTraceAndSlog;
        UserController userController;
        ?? r2;
        final long j;
        final AtomicInteger atomicInteger;
        final ArraySet<String> arraySet;
        int i3;
        IRemoteCallback.Stub stub;
        ?? r4;
        UserController userController2 = this;
        int i4 = i2;
        TimingsTraceAndSlog timingsTraceAndSlog2 = new TimingsTraceAndSlog();
        timingsTraceAndSlog2.traceBegin("dispatchUserSwitch-" + i + "-to-" + i4);
        EventLog.writeEvent(EventLogTags.UC_DISPATCH_USER_SWITCH, Integer.valueOf(i), Integer.valueOf(i2));
        userController2.mUserControllerExt.dispatchSwitch(userState, i, i4);
        int beginBroadcast = userController2.mUserSwitchObservers.beginBroadcast();
        if (beginBroadcast > 0) {
            int i5 = 0;
            for (int i6 = 0; i6 < beginBroadcast; i6++) {
                String str = "#" + i6 + " " + userController2.mUserSwitchObservers.getBroadcastCookie(i6);
                StringBuilder sb = new StringBuilder();
                r4 = "onBeforeUserSwitching-";
                sb.append("onBeforeUserSwitching-");
                sb.append(str);
                timingsTraceAndSlog2.traceBegin(sb.toString());
                try {
                    userController2.mUserSwitchObservers.getBroadcastItem(i6).onBeforeUserSwitching(i4);
                } catch (RemoteException unused) {
                } catch (Throwable th) {
                    timingsTraceAndSlog2.traceEnd();
                    throw th;
                }
                timingsTraceAndSlog2.traceEnd();
            }
            ArraySet<String> arraySet2 = new ArraySet<>();
            synchronized (userController2.mLock) {
                r2 = 1;
                userState.switching = true;
                userController2.mCurWaitingUserSwitchCallbacks = arraySet2;
            }
            AtomicInteger atomicInteger2 = new AtomicInteger(beginBroadcast);
            long userSwitchTimeoutMs = getUserSwitchTimeoutMs();
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            int i7 = r4;
            while (true) {
                int i8 = i5;
                if (i8 >= beginBroadcast) {
                    timingsTraceAndSlog = timingsTraceAndSlog2;
                    userController = userController2;
                    break;
                }
                final ?? elapsedRealtime2 = SystemClock.elapsedRealtime();
                try {
                    final String str2 = "#" + i8 + " " + userController2.mUserSwitchObservers.getBroadcastCookie(i8);
                    synchronized (userController2.mLock) {
                        try {
                            arraySet2.add(str2);
                        } finally {
                            th = th;
                            while (true) {
                                try {
                                    break;
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                            }
                            break;
                        }
                    }
                    j = userSwitchTimeoutMs;
                    atomicInteger = atomicInteger2;
                    arraySet = arraySet2;
                    TimingsTraceAndSlog timingsTraceAndSlog3 = timingsTraceAndSlog2;
                    i3 = beginBroadcast;
                    try {
                        stub = new IRemoteCallback.Stub() { // from class: com.android.server.am.UserController.7
                            public void sendResult(Bundle bundle) throws RemoteException {
                                synchronized (UserController.this.mLock) {
                                    long elapsedRealtime3 = SystemClock.elapsedRealtime() - elapsedRealtime2;
                                    if (elapsedRealtime3 > 500) {
                                        Slogf.w("ActivityManager", "User switch slowed down by observer " + str2 + ": result took " + elapsedRealtime3 + " ms to process.");
                                    }
                                    long elapsedRealtime4 = SystemClock.elapsedRealtime() - elapsedRealtime;
                                    UserController.this.mUserControllerExt.dispatchSwitchSendResult(elapsedRealtime4, str2, i, i2);
                                    if (elapsedRealtime4 > j) {
                                        Slogf.e("ActivityManager", "User switch timeout: observer " + str2 + "'s result was received " + elapsedRealtime4 + " ms after dispatchUserSwitch.");
                                    }
                                    TimingsTraceAndSlog timingsTraceAndSlog4 = new TimingsTraceAndSlog("ActivityManager");
                                    timingsTraceAndSlog4.traceBegin("onUserSwitchingReply-" + str2);
                                    arraySet.remove(str2);
                                    if (atomicInteger.decrementAndGet() == 0 && arraySet == UserController.this.mCurWaitingUserSwitchCallbacks) {
                                        UserController.this.sendContinueUserSwitchLU(userState, i, i2);
                                    }
                                    timingsTraceAndSlog4.traceEnd();
                                }
                            }
                        };
                        timingsTraceAndSlog3.traceBegin("onUserSwitching-" + str2);
                        r2 = this;
                        elapsedRealtime2 = timingsTraceAndSlog3;
                    } catch (RemoteException unused2) {
                        r2 = this;
                        i8 = i2;
                        elapsedRealtime2 = timingsTraceAndSlog3;
                    }
                } catch (RemoteException unused3) {
                    i7 = i8;
                    j = userSwitchTimeoutMs;
                    atomicInteger = atomicInteger2;
                    arraySet = arraySet2;
                    elapsedRealtime2 = timingsTraceAndSlog2;
                    i3 = beginBroadcast;
                    i8 = i4;
                    r2 = userController2;
                }
                try {
                    i7 = i8;
                    try {
                        i8 = i2;
                        try {
                            r2.mUserSwitchObservers.getBroadcastItem(i7).onUserSwitching(i8, stub);
                            elapsedRealtime2.traceEnd();
                        } catch (RemoteException unused4) {
                        }
                    } catch (RemoteException unused5) {
                        i8 = i2;
                    }
                } catch (RemoteException unused6) {
                    i8 = i2;
                    r2 = r2;
                    elapsedRealtime2 = elapsedRealtime2;
                    i7 = i8;
                    i5 = i7 + 1;
                    userController2 = r2;
                    timingsTraceAndSlog2 = elapsedRealtime2;
                    i4 = i8;
                    userSwitchTimeoutMs = j;
                    atomicInteger2 = atomicInteger;
                    arraySet2 = arraySet;
                    beginBroadcast = i3;
                    r2 = r2;
                    i7 = i7;
                }
                i5 = i7 + 1;
                userController2 = r2;
                timingsTraceAndSlog2 = elapsedRealtime2;
                i4 = i8;
                userSwitchTimeoutMs = j;
                atomicInteger2 = atomicInteger;
                arraySet2 = arraySet;
                beginBroadcast = i3;
                r2 = r2;
                i7 = i7;
            }
        } else {
            timingsTraceAndSlog = timingsTraceAndSlog2;
            userController = userController2;
            synchronized (userController.mLock) {
                sendContinueUserSwitchLU(userState, i, i2);
            }
        }
        userController.mUserSwitchObservers.finishBroadcast();
        timingsTraceAndSlog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void sendContinueUserSwitchLU(UserState userState, int i, int i2) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog("ActivityManager");
        timingsTraceAndSlog.traceBegin("sendContinueUserSwitchLU-" + i + "-to-" + i2);
        this.mCurWaitingUserSwitchCallbacks = null;
        this.mHandler.removeMessages(30);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(20, i, i2, userState));
        timingsTraceAndSlog.traceEnd();
    }

    @VisibleForTesting
    void continueUserSwitch(UserState userState, int i, int i2) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        timingsTraceAndSlog.traceBegin("continueUserSwitch-" + i + "-to-" + i2);
        this.mUserControllerExt.continueUserSwitch(userState, i, i2);
        EventLog.writeEvent(EventLogTags.UC_CONTINUE_USER_SWITCH, Integer.valueOf(i), Integer.valueOf(i2));
        this.mHandler.removeMessages(130);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(130, i, i2));
        userState.switching = false;
        stopGuestOrEphemeralUserIfBackground(i);
        stopUserOnSwitchIfEnforced(i);
        timingsTraceAndSlog.traceEnd();
    }

    @VisibleForTesting
    void completeUserSwitch(final int i, final int i2) {
        final boolean isUserSwitchUiEnabled = isUserSwitchUiEnabled();
        boolean z = isUserSwitchUiEnabled && !this.mInjector.getKeyguardManager().isDeviceSecure(i2);
        final Injector injector = this.mInjector;
        Objects.requireNonNull(injector);
        await(z, new Consumer() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                UserController.Injector.this.dismissKeyguard((Runnable) obj);
            }
        }, new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$completeUserSwitch$17(isUserSwitchUiEnabled, i, i2);
            }
        });
        this.mUserControllerExt.stopFreezingScreenIfNeeded(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$completeUserSwitch$17(boolean z, final int i, final int i2) {
        await(z, new Consumer() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                UserController.this.dismissUserSwitchDialog((Runnable) obj);
            }
        }, new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                UserController.this.lambda$completeUserSwitch$16(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$completeUserSwitch$16(int i, int i2) {
        this.mHandler.removeMessages(80);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(80, i, i2));
    }

    private void await(boolean z, Consumer<Runnable> consumer, Runnable runnable) {
        if (z) {
            consumer.accept(runnable);
        } else {
            runnable.run();
        }
    }

    private void moveUserToForeground(UserState userState, int i) {
        if (this.mInjector.taskSupervisorSwitchUser(i, userState)) {
            this.mInjector.startHomeActivity(i, "moveUserToForeground");
        } else {
            this.mInjector.taskSupervisorResumeFocusedStackTopActivity();
        }
        EventLogTags.writeAmSwitchUser(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendUserStartedBroadcast(int i, int i2, int i3) {
        if (i == 0) {
            synchronized (this.mLock) {
                if (this.mIsBroadcastSentForSystemUserStarted) {
                    return;
                } else {
                    this.mIsBroadcastSentForSystemUserStarted = true;
                }
            }
        }
        Intent intent = new Intent("android.intent.action.USER_STARTED");
        intent.addFlags(1342177280);
        intent.putExtra("android.intent.extra.user_handle", i);
        this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, false, ActivityManagerService.MY_PID, 1000, i2, i3, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendUserStartingBroadcast(int i, int i2, int i3) {
        if (i == 0) {
            synchronized (this.mLock) {
                if (this.mIsBroadcastSentForSystemUserStarting) {
                    return;
                } else {
                    this.mIsBroadcastSentForSystemUserStarting = true;
                }
            }
        }
        Intent intent = new Intent("android.intent.action.USER_STARTING");
        intent.addFlags(1073741824);
        intent.putExtra("android.intent.extra.user_handle", i);
        this.mInjector.broadcastIntent(intent, null, new IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.8
            public void performReceive(Intent intent2, int i4, String str, Bundle bundle, boolean z, boolean z2, int i5) throws RemoteException {
            }
        }, 0, null, null, new String[]{"android.permission.INTERACT_ACROSS_USERS"}, -1, null, true, false, ActivityManagerService.MY_PID, 1000, i2, i3, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendUserSwitchBroadcasts(int i, int i2) {
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        String str = "android.intent.extra.USER";
        String str2 = "android.intent.extra.user_handle";
        int i3 = 1342177280;
        if (i >= 0) {
            try {
                List profiles = this.mInjector.getUserManager().getProfiles(i, false);
                int size = profiles.size();
                int i4 = 0;
                while (i4 < size) {
                    int i5 = ((UserInfo) profiles.get(i4)).id;
                    Intent intent = new Intent("android.intent.action.USER_BACKGROUND");
                    intent.addFlags(i3);
                    intent.putExtra(str2, i5);
                    intent.putExtra(str, UserHandle.of(i5));
                    this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, false, ActivityManagerService.MY_PID, 1000, callingUid, callingPid, i5);
                    i4++;
                    size = size;
                    str2 = str2;
                    str = str;
                    i3 = 1342177280;
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
        String str3 = str2;
        String str4 = str;
        if (i2 >= 0) {
            boolean z = false;
            List profiles2 = this.mInjector.getUserManager().getProfiles(i2, false);
            int size2 = profiles2.size();
            int i6 = 0;
            while (i6 < size2) {
                int i7 = ((UserInfo) profiles2.get(i6)).id;
                Intent intent2 = new Intent("android.intent.action.USER_FOREGROUND");
                intent2.addFlags(1342177280);
                String str5 = str3;
                intent2.putExtra(str5, i7);
                String str6 = str4;
                intent2.putExtra(str6, UserHandle.of(i7));
                this.mInjector.broadcastIntent(intent2, null, null, 0, null, null, null, -1, null, false, false, ActivityManagerService.MY_PID, 1000, callingUid, callingPid, i7);
                i6++;
                size2 = size2;
                z = z;
                str4 = str6;
                str3 = str5;
            }
            Intent intent3 = new Intent("android.intent.action.USER_SWITCHED");
            intent3.addFlags(1342177280);
            intent3.putExtra(str3, i2);
            intent3.putExtra(str4, UserHandle.of(i2));
            Injector injector = this.mInjector;
            String[] strArr = new String[1];
            strArr[z ? 1 : 0] = "android.permission.MANAGE_USERS";
            injector.broadcastIntent(intent3, null, null, 0, null, null, strArr, -1, null, false, false, ActivityManagerService.MY_PID, 1000, callingUid, callingPid, -1);
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    private void broadcastProfileAccessibleStateChanged(int i, int i2, String str) {
        Intent intent = new Intent(str);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i));
        intent.addFlags(1342177280);
        this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, false, ActivityManagerService.MY_PID, 1000, Binder.getCallingUid(), Binder.getCallingPid(), i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:41:0x013e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int handleIncomingUser(int i, int i2, int i3, boolean z, int i4, String str, String str2) {
        int i5;
        int i6;
        int userId = UserHandle.getUserId(i2);
        if (userId == i3) {
            return i3;
        }
        if (this.mUserControllerExt.hookHandleIncomingUser(i2, i3)) {
            if (ActivityManagerDebugConfig.DEBUG_MU) {
                Slog.v("ActivityManager", "multi app -> handleIncomingUser: bypass user[" + i3 + "] for uid[" + i2 + "(" + str2 + ")]");
            }
            return i3;
        }
        int unsafeConvertIncomingUser = unsafeConvertIncomingUser(i3);
        if (i2 != 0 && i2 != 1000) {
            boolean isSameProfileGroup = isSameProfileGroup(userId, unsafeConvertIncomingUser);
            boolean z2 = true;
            if (this.mInjector.isCallerRecents(i2) && isSameProfileGroup) {
                i6 = 1;
                i5 = 2;
            } else {
                i5 = 2;
                if (this.mInjector.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS_FULL", i, i2, -1, true) != 0) {
                    if (i4 != 2) {
                        if (!canInteractWithAcrossProfilesPermission(i4, isSameProfileGroup, i, i2, str2)) {
                            if (this.mInjector.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS", i, i2, -1, true) == 0) {
                                if (i4 == 0 || i4 == 3) {
                                    i6 = 1;
                                    z2 = true;
                                } else {
                                    i6 = 1;
                                    if (i4 != 1) {
                                        throw new IllegalArgumentException("Unknown mode: " + i4);
                                    }
                                    z2 = isSameProfileGroup;
                                }
                            }
                        }
                    }
                    z2 = false;
                    i6 = 1;
                }
                i6 = 1;
                z2 = true;
            }
            if (!z2) {
                if (i3 != -3) {
                    StringBuilder sb = new StringBuilder(128);
                    sb.append("Permission Denial: ");
                    sb.append(str);
                    if (str2 != null) {
                        sb.append(" from ");
                        sb.append(str2);
                    }
                    sb.append(" asks to run as user ");
                    sb.append(i3);
                    sb.append(" but is calling from uid ");
                    UserHandle.formatUid(sb, i2);
                    sb.append("; this requires ");
                    sb.append("android.permission.INTERACT_ACROSS_USERS_FULL");
                    if (i4 != i5) {
                        if (i4 == 0 || i4 == 3 || (i4 == i6 && isSameProfileGroup)) {
                            sb.append(" or ");
                            sb.append("android.permission.INTERACT_ACROSS_USERS");
                        }
                        if (isSameProfileGroup && i4 == 3) {
                            sb.append(" or ");
                            sb.append("android.permission.INTERACT_ACROSS_PROFILES");
                        }
                    }
                    String sb2 = sb.toString();
                    Slogf.w("ActivityManager", sb2);
                    throw new SecurityException(sb2);
                }
                if (!z) {
                    ensureNotSpecialUser(userId);
                }
                if (i2 == 2000 || userId < 0 || !hasUserRestriction("no_debugging_features", userId)) {
                    return userId;
                }
                throw new SecurityException("Shell does not have permission to access user " + userId + "\n " + Debug.getCallers(3));
            }
        }
        userId = unsafeConvertIncomingUser;
        if (!z) {
        }
        if (i2 == 2000) {
        }
        return userId;
    }

    private boolean canInteractWithAcrossProfilesPermission(int i, boolean z, int i2, int i3, String str) {
        if (i == 3 && z) {
            return this.mInjector.checkPermissionForPreflight("android.permission.INTERACT_ACROSS_PROFILES", i2, i3, str);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int unsafeConvertIncomingUser(int i) {
        return (i == -2 || i == -3) ? getCurrentUserId() : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureNotSpecialUser(int i) {
        if (i >= 0) {
            return;
        }
        throw new IllegalArgumentException("Call does not support special user #" + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver, String str) {
        Objects.requireNonNull(str, "Observer name cannot be null");
        checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "registerUserSwitchObserver");
        this.mUserSwitchObservers.register(iUserSwitchObserver, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendForegroundProfileChanged(int i) {
        this.mHandler.removeMessages(70);
        this.mHandler.obtainMessage(70, i, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) {
        this.mUserSwitchObservers.unregister(iUserSwitchObserver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserState getStartedUserState(int i) {
        UserState userState;
        synchronized (this.mLock) {
            userState = this.mStartedUsers.get(i);
        }
        return userState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasStartedUserState(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mStartedUsers.get(i) != null;
        }
        return z;
    }

    @GuardedBy({"mLock"})
    private void updateStartedUserArrayLU() {
        int i = 0;
        for (int i2 = 0; i2 < this.mStartedUsers.size(); i2++) {
            int i3 = this.mStartedUsers.valueAt(i2).state;
            if (i3 != 4 && i3 != 5) {
                i++;
            }
        }
        this.mStartedUserArray = new int[i];
        int i4 = 0;
        for (int i5 = 0; i5 < this.mStartedUsers.size(); i5++) {
            int i6 = this.mStartedUsers.valueAt(i5).state;
            if (i6 != 4 && i6 != 5) {
                this.mStartedUserArray[i4] = this.mStartedUsers.keyAt(i5);
                i4++;
            }
        }
    }

    @VisibleForTesting
    void setAllowUserUnlocking(boolean z) {
        this.mAllowUserUnlocking = z;
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.d("ActivityManager", new Exception(), "setAllowUserUnlocking(%b)", new Object[]{Boolean.valueOf(z)});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBootComplete(IIntentReceiver iIntentReceiver) {
        SparseArray<UserState> clone;
        setAllowUserUnlocking(true);
        synchronized (this.mLock) {
            clone = this.mStartedUsers.clone();
        }
        Preconditions.checkArgument(clone.keyAt(0) == 0);
        for (int i = 0; i < clone.size(); i++) {
            int keyAt = clone.keyAt(i);
            UserState valueAt = clone.valueAt(i);
            if (!this.mInjector.isHeadlessSystemUserMode()) {
                finishUserBoot(valueAt, iIntentReceiver);
            } else {
                sendLockedBootCompletedBroadcast(iIntentReceiver, keyAt);
                maybeUnlockUser(keyAt);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slogf.d("ActivityManager", "onSystemReady()");
        }
        this.mInjector.getUserManagerInternal().addUserLifecycleListener(this.mUserLifecycleListener);
        updateProfileRelatedCaches();
        this.mInjector.reportCurWakefulnessUsageEvent();
        this.mUserControllerExt.onSystemReady();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemUserStarting() {
        if (this.mInjector.isHeadlessSystemUserMode()) {
            return;
        }
        this.mInjector.onUserStarting(0);
        this.mInjector.onSystemUserVisibilityChanged(true);
    }

    private void updateProfileRelatedCaches() {
        List profiles = this.mInjector.getUserManager().getProfiles(getCurrentUserId(), false);
        int size = profiles.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = ((UserInfo) profiles.get(i)).id;
        }
        List users = this.mInjector.getUserManager().getUsers(false);
        synchronized (this.mLock) {
            this.mCurrentProfileIds = iArr;
            this.mUserProfileGroupIds.clear();
            for (int i2 = 0; i2 < users.size(); i2++) {
                UserInfo userInfo = (UserInfo) users.get(i2);
                int i3 = userInfo.profileGroupId;
                if (i3 != -10000) {
                    this.mUserProfileGroupIds.put(userInfo.id, i3);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getStartedUserArray() {
        int[] iArr;
        synchronized (this.mLock) {
            iArr = this.mStartedUserArray;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserRunning(int i, int i2) {
        UserState startedUserState = getStartedUserState(i);
        if (startedUserState == null) {
            return false;
        }
        if ((i2 & 1) != 0) {
            return true;
        }
        if ((i2 & 2) != 0) {
            int i3 = startedUserState.state;
            return i3 == 0 || i3 == 1;
        }
        if ((i2 & 8) != 0) {
            int i4 = startedUserState.state;
            if (i4 == 2 || i4 == 3) {
                return true;
            }
            if (i4 == 4 || i4 == 5) {
                return StorageManager.isUserKeyUnlocked(i);
            }
            return false;
        }
        if ((i2 & 4) != 0) {
            int i5 = startedUserState.state;
            if (i5 == 3) {
                return true;
            }
            if (i5 == 4 || i5 == 5) {
                return StorageManager.isUserKeyUnlocked(i);
            }
            return false;
        }
        int i6 = startedUserState.state;
        return (i6 == 4 || i6 == 5) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSystemUserStarted() {
        synchronized (this.mLock) {
            UserState userState = this.mStartedUsers.get(0);
            if (userState == null) {
                return false;
            }
            int i = userState.state;
            return i == 1 || i == 2 || i == 3;
        }
    }

    private void checkGetCurrentUserPermissions() {
        if (this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") == 0 || this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0) {
            return;
        }
        String str = "Permission Denial: getCurrentUser() from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " requires android.permission.INTERACT_ACROSS_USERS";
        Slogf.w("ActivityManager", str);
        throw new SecurityException(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserInfo getCurrentUser() {
        UserInfo currentUserLU;
        checkGetCurrentUserPermissions();
        if (this.mTargetUserId == -10000) {
            return getUserInfo(this.mCurrentUserId);
        }
        synchronized (this.mLock) {
            currentUserLU = getCurrentUserLU();
        }
        return currentUserLU;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentUserIdChecked() {
        checkGetCurrentUserPermissions();
        if (this.mTargetUserId == -10000) {
            return this.mCurrentUserId;
        }
        return getCurrentOrTargetUserId();
    }

    @GuardedBy({"mLock"})
    private UserInfo getCurrentUserLU() {
        return getUserInfo(getCurrentOrTargetUserIdLU());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentOrTargetUserId() {
        int currentOrTargetUserIdLU;
        synchronized (this.mLock) {
            currentOrTargetUserIdLU = getCurrentOrTargetUserIdLU();
        }
        return currentOrTargetUserIdLU;
    }

    @GuardedBy({"mLock"})
    private int getCurrentOrTargetUserIdLU() {
        return this.mTargetUserId != -10000 ? this.mTargetUserId : this.mCurrentUserId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<Integer, Integer> getCurrentAndTargetUserIds() {
        Pair<Integer, Integer> pair;
        synchronized (this.mLock) {
            pair = new Pair<>(Integer.valueOf(this.mCurrentUserId), Integer.valueOf(this.mTargetUserId));
        }
        return pair;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public int getCurrentUserIdLU() {
        return this.mCurrentUserId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentUserId() {
        int i;
        synchronized (this.mLock) {
            i = this.mCurrentUserId;
        }
        return i;
    }

    @GuardedBy({"mLock"})
    private boolean isCurrentUserLU(int i) {
        return i == getCurrentOrTargetUserIdLU();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getUsers() {
        UserManagerService userManager = this.mInjector.getUserManager();
        return userManager != null ? userManager.getUserIds() : new int[]{0};
    }

    private UserInfo getUserInfo(int i) {
        return this.mInjector.getUserManager().getUserInfo(i);
    }

    private UserProperties getUserProperties(int i) {
        return this.mInjector.getUserManagerInternal().getUserProperties(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getUserIds() {
        return this.mInjector.getUserManager().getUserIds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] expandUserId(int i) {
        if (i != -1) {
            return new int[]{i};
        }
        return getUsers();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean exists(int i) {
        return this.mInjector.getUserManager().exists(i);
    }

    private void checkCallingPermission(String str, String str2) {
        checkCallingHasOneOfThosePermissions(str2, str);
    }

    private void checkCallingHasOneOfThosePermissions(String str, String... strArr) {
        for (String str2 : strArr) {
            if (this.mInjector.checkCallingPermission(str2) == 0) {
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Permission denial: ");
        sb.append(str);
        sb.append("() from pid=");
        sb.append(Binder.getCallingPid());
        sb.append(", uid=");
        sb.append(Binder.getCallingUid());
        sb.append(" requires ");
        sb.append(strArr.length == 1 ? strArr[0] : "one of " + Arrays.toString(strArr));
        String sb2 = sb.toString();
        Slogf.w("ActivityManager", sb2);
        throw new SecurityException(sb2);
    }

    private void enforceShellRestriction(String str, int i) {
        if (Binder.getCallingUid() == 2000) {
            if (i < 0 || hasUserRestriction(str, i)) {
                throw new SecurityException("Shell does not have permission to access user " + i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasUserRestriction(String str, int i) {
        return this.mInjector.getUserManager().hasUserRestriction(str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSameProfileGroup(int i, int i2) {
        boolean z = true;
        if (i == i2) {
            return true;
        }
        synchronized (this.mLock) {
            int i3 = this.mUserProfileGroupIds.get(i, -10000);
            int i4 = this.mUserProfileGroupIds.get(i2, -10000);
            if (i3 == -10000 || i3 != i4) {
                z = false;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserOrItsParentRunning(int i) {
        synchronized (this.mLock) {
            if (isUserRunning(i, 0)) {
                return true;
            }
            int i2 = this.mUserProfileGroupIds.get(i, -10000);
            if (i2 == -10000) {
                return false;
            }
            return isUserRunning(i2, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCurrentProfile(int i) {
        boolean contains;
        synchronized (this.mLock) {
            contains = ArrayUtils.contains(this.mCurrentProfileIds, i);
        }
        return contains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getCurrentProfileIds() {
        int[] iArr;
        synchronized (this.mLock) {
            iArr = this.mCurrentProfileIds;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserAdded(UserInfo userInfo) {
        if (userInfo.isProfile()) {
            synchronized (this.mLock) {
                if (userInfo.profileGroupId == this.mCurrentUserId) {
                    this.mCurrentProfileIds = ArrayUtils.appendInt(this.mCurrentProfileIds, userInfo.id);
                }
                this.mUserProfileGroupIds.put(userInfo.id, userInfo.profileGroupId);
            }
        }
    }

    void onUserRemoved(int i) {
        synchronized (this.mLock) {
            for (int size = this.mUserProfileGroupIds.size() - 1; size >= 0; size--) {
                if (this.mUserProfileGroupIds.keyAt(size) == i || this.mUserProfileGroupIds.valueAt(size) == i) {
                    this.mUserProfileGroupIds.removeAt(size);
                }
            }
            this.mCurrentProfileIds = ArrayUtils.removeInt(this.mCurrentProfileIds, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean shouldConfirmCredentials(int i) {
        UserProperties userProperties;
        if (getStartedUserState(i) == null || (userProperties = getUserProperties(i)) == null || !userProperties.isCredentialShareableWithParent()) {
            return false;
        }
        if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
            KeyguardManager keyguardManager = this.mInjector.getKeyguardManager();
            return keyguardManager.isDeviceLocked(i) && keyguardManager.isDeviceSecure(i);
        }
        return isUserRunning(i, 2);
    }

    boolean isLockScreenDisabled(int i) {
        return this.mLockPatternUtils.isLockScreenDisabled(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSwitchingFromSystemUserMessage(String str) {
        synchronized (this.mLock) {
            this.mSwitchingFromSystemUserMessage = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSwitchingToSystemUserMessage(String str) {
        synchronized (this.mLock) {
            this.mSwitchingToSystemUserMessage = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSwitchingFromSystemUserMessage() {
        checkHasManageUsersPermission("getSwitchingFromSystemUserMessage()");
        return getSwitchingFromSystemUserMessageUnchecked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSwitchingToSystemUserMessage() {
        checkHasManageUsersPermission("getSwitchingToSystemUserMessage()");
        return getSwitchingToSystemUserMessageUnchecked();
    }

    private String getSwitchingFromSystemUserMessageUnchecked() {
        String str;
        synchronized (this.mLock) {
            str = this.mSwitchingFromSystemUserMessage;
        }
        return str;
    }

    private String getSwitchingToSystemUserMessageUnchecked() {
        String str;
        synchronized (this.mLock) {
            str = this.mSwitchingToSystemUserMessage;
        }
        return str;
    }

    private void checkHasManageUsersPermission(String str) {
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") != -1) {
            return;
        }
        throw new SecurityException("You need MANAGE_USERS permission to call " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        synchronized (this.mLock) {
            long start = protoOutputStream.start(j);
            int i = 0;
            for (int i2 = 0; i2 < this.mStartedUsers.size(); i2++) {
                UserState valueAt = this.mStartedUsers.valueAt(i2);
                long start2 = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1120986464257L, valueAt.mHandle.getIdentifier());
                valueAt.dumpDebug(protoOutputStream, 1146756268034L);
                protoOutputStream.end(start2);
            }
            int i3 = 0;
            while (true) {
                int[] iArr = this.mStartedUserArray;
                if (i3 >= iArr.length) {
                    break;
                }
                protoOutputStream.write(2220498092034L, iArr[i3]);
                i3++;
            }
            for (int i4 = 0; i4 < this.mUserLru.size(); i4++) {
                protoOutputStream.write(2220498092035L, this.mUserLru.get(i4).intValue());
            }
            if (this.mUserProfileGroupIds.size() > 0) {
                for (int i5 = 0; i5 < this.mUserProfileGroupIds.size(); i5++) {
                    long start3 = protoOutputStream.start(2246267895812L);
                    protoOutputStream.write(1120986464257L, this.mUserProfileGroupIds.keyAt(i5));
                    protoOutputStream.write(1120986464258L, this.mUserProfileGroupIds.valueAt(i5));
                    protoOutputStream.end(start3);
                }
            }
            protoOutputStream.write(1120986464261L, this.mCurrentUserId);
            while (true) {
                int[] iArr2 = this.mCurrentProfileIds;
                if (i < iArr2.length) {
                    protoOutputStream.write(2220498092038L, iArr2[i]);
                    i++;
                } else {
                    protoOutputStream.end(start);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println("  mStartedUsers:");
            for (int i = 0; i < this.mStartedUsers.size(); i++) {
                UserState valueAt = this.mStartedUsers.valueAt(i);
                printWriter.print("    User #");
                printWriter.print(valueAt.mHandle.getIdentifier());
                printWriter.print(": ");
                valueAt.dump("", printWriter);
            }
            printWriter.print("  mStartedUserArray: [");
            for (int i2 = 0; i2 < this.mStartedUserArray.length; i2++) {
                if (i2 > 0) {
                    printWriter.print(", ");
                }
                printWriter.print(this.mStartedUserArray[i2]);
            }
            printWriter.println("]");
            printWriter.print("  mUserLru: [");
            for (int i3 = 0; i3 < this.mUserLru.size(); i3++) {
                if (i3 > 0) {
                    printWriter.print(", ");
                }
                printWriter.print(this.mUserLru.get(i3));
            }
            printWriter.println("]");
            if (this.mUserProfileGroupIds.size() > 0) {
                printWriter.println("  mUserProfileGroupIds:");
                for (int i4 = 0; i4 < this.mUserProfileGroupIds.size(); i4++) {
                    printWriter.print("    User #");
                    printWriter.print(this.mUserProfileGroupIds.keyAt(i4));
                    printWriter.print(" -> profile #");
                    printWriter.println(this.mUserProfileGroupIds.valueAt(i4));
                }
            }
            printWriter.println("  mCurrentProfileIds:" + Arrays.toString(this.mCurrentProfileIds));
            printWriter.println("  mCurrentUserId:" + this.mCurrentUserId);
            printWriter.println("  mTargetUserId:" + this.mTargetUserId);
            printWriter.println("  mLastActiveUsers:" + this.mLastActiveUsers);
            printWriter.println("  mDelayUserDataLocking:" + this.mDelayUserDataLocking);
            printWriter.println("  mAllowUserUnlocking:" + this.mAllowUserUnlocking);
            printWriter.println("  shouldStopUserOnSwitch():" + shouldStopUserOnSwitch());
            printWriter.println("  mStopUserOnSwitch:" + this.mStopUserOnSwitch);
            printWriter.println("  mMaxRunningUsers:" + this.mMaxRunningUsers);
            printWriter.println("  mUserSwitchUiEnabled:" + this.mUserSwitchUiEnabled);
            printWriter.println("  mInitialized:" + this.mInitialized);
            printWriter.println("  mIsBroadcastSentForSystemUserStarted:" + this.mIsBroadcastSentForSystemUserStarted);
            printWriter.println("  mIsBroadcastSentForSystemUserStarting:" + this.mIsBroadcastSentForSystemUserStarting);
            if (this.mSwitchingFromSystemUserMessage != null) {
                printWriter.println("  mSwitchingFromSystemUserMessage: " + this.mSwitchingFromSystemUserMessage);
            }
            if (this.mSwitchingToSystemUserMessage != null) {
                printWriter.println("  mSwitchingToSystemUserMessage: " + this.mSwitchingToSystemUserMessage);
            }
            printWriter.println("  mLastUserUnlockingUptime: " + this.mLastUserUnlockingUptime);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 10:
                dispatchUserSwitch((UserState) message.obj, message.arg1, message.arg2);
                return false;
            case 20:
                continueUserSwitch((UserState) message.obj, message.arg1, message.arg2);
                return false;
            case 30:
                timeoutUserSwitch((UserState) message.obj, message.arg1, message.arg2);
                return false;
            case 40:
                startProfiles();
                return false;
            case 50:
                this.mInjector.batteryStatsServiceNoteEvent(32775, Integer.toString(message.arg1), message.arg1);
                logUserJourneyBegin(message.arg1, 3);
                this.mInjector.onUserStarting(message.arg1);
                scheduleOnUserCompletedEvent(message.arg1, 1, 5000);
                this.mInjector.getUserJourneyLogger().logUserJourneyFinish(-1, getUserInfo(message.arg1), 3);
                return false;
            case 60:
                this.mInjector.batteryStatsServiceNoteEvent(16392, Integer.toString(message.arg2), message.arg2);
                this.mInjector.batteryStatsServiceNoteEvent(32776, Integer.toString(message.arg1), message.arg1);
                this.mInjector.getSystemServiceManager().onUserSwitching(message.arg2, message.arg1);
                scheduleOnUserCompletedEvent(message.arg1, 4, 5000);
                return false;
            case 70:
                dispatchForegroundProfileChanged(message.arg1);
                return false;
            case 80:
                dispatchUserSwitchComplete(message.arg1, message.arg2);
                UserJourneyLogger.UserJourneySession logUserSwitchJourneyFinish = this.mInjector.getUserJourneyLogger().logUserSwitchJourneyFinish(message.arg1, getUserInfo(message.arg2));
                if (logUserSwitchJourneyFinish == null) {
                    return false;
                }
                this.mHandler.removeMessages(200, logUserSwitchJourneyFinish);
                return false;
            case 90:
                timeoutUserSwitchCallbacks(message.arg1, message.arg2);
                return false;
            case 100:
                final int i = message.arg1;
                this.mInjector.getSystemServiceManager().onUserUnlocking(i);
                this.mUserControllerExt.hookFgHandler(FgThread.getHandler()).post(new Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserController.this.lambda$handleMessage$18(i);
                    }
                });
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(message.arg1, 5, 2);
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(message.arg1, 6, 1);
                TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
                timingsTraceAndSlog.traceBegin("finishUserUnlocked-" + i);
                finishUserUnlocked((UserState) message.obj);
                timingsTraceAndSlog.traceEnd();
                return false;
            case 105:
                this.mInjector.getSystemServiceManager().onUserUnlocked(message.arg1);
                scheduleOnUserCompletedEvent(message.arg1, 2, this.mCurrentUserId != message.arg1 ? 1000 : 5000);
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(message.arg1, 6, 2);
                return false;
            case 110:
                dispatchLockedBootComplete(message.arg1);
                return false;
            case 120:
                logUserJourneyBegin(message.arg1, 2);
                startUserInForeground(message.arg1);
                return false;
            case 130:
                completeUserSwitch(message.arg1, message.arg2);
                return false;
            case 140:
                reportOnUserCompletedEvent((Integer) message.obj);
                return false;
            case 200:
                this.mInjector.getUserJourneyLogger().finishAndClearIncompleteUserJourney(message.arg1, message.arg2);
                this.mHandler.removeMessages(200, message.obj);
                return false;
            case 1000:
                Pair<UserInfo, UserInfo> pair = (Pair) message.obj;
                logUserJourneyBegin(((UserInfo) pair.second).id, 1);
                showUserSwitchDialog(pair);
                return false;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleMessage$18(int i) {
        this.mInjector.loadUserRecents(i);
    }

    @VisibleForTesting
    void scheduleOnUserCompletedEvent(int i, int i2, int i3) {
        if (i2 != 0) {
            synchronized (this.mCompletedEventTypes) {
                SparseIntArray sparseIntArray = this.mCompletedEventTypes;
                sparseIntArray.put(i, i2 | sparseIntArray.get(i, 0));
            }
        }
        Integer valueOf = Integer.valueOf(i);
        this.mHandler.removeEqualMessages(140, valueOf);
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(140, valueOf), i3);
    }

    @VisibleForTesting
    void reportOnUserCompletedEvent(Integer num) {
        int i;
        int i2;
        this.mHandler.removeEqualMessages(140, num);
        synchronized (this.mCompletedEventTypes) {
            i = 0;
            i2 = this.mCompletedEventTypes.get(num.intValue(), 0);
            this.mCompletedEventTypes.delete(num.intValue());
        }
        synchronized (this.mLock) {
            UserState userState = this.mStartedUsers.get(num.intValue());
            if (userState != null && userState.state != 5) {
                i = 1;
            }
            if (userState != null && userState.state == 3) {
                i |= 2;
            }
            if (num.intValue() == this.mCurrentUserId) {
                i |= 4;
            }
        }
        Slogf.i("ActivityManager", "reportOnUserCompletedEvent(%d): stored=%s, eligible=%s", new Object[]{num, Integer.toBinaryString(i2), Integer.toBinaryString(i)});
        this.mInjector.systemServiceManagerOnUserCompletedEvent(num.intValue(), i2 & i);
    }

    private void logUserJourneyBegin(int i, @UserJourneyLogger.UserJourney int i2) {
        UserJourneyLogger.UserJourneySession finishAndClearIncompleteUserJourney = this.mInjector.getUserJourneyLogger().finishAndClearIncompleteUserJourney(i, i2);
        if (finishAndClearIncompleteUserJourney != null) {
            if (ActivityManagerDebugConfig.DEBUG_MU) {
                Slogf.d("ActivityManager", "Starting a new journey: " + i2 + " with session id: " + finishAndClearIncompleteUserJourney);
            }
            this.mHandler.removeMessages(200, finishAndClearIncompleteUserJourney);
        }
        UserJourneyLogger.UserJourneySession logUserJourneyBegin = this.mInjector.getUserJourneyLogger().logUserJourneyBegin(i, i2);
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(200, i, i2, logUserJourneyBegin), 90000L);
    }

    private BroadcastOptions getTemporaryAppAllowlistBroadcastOptions(int i) {
        ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        long bootTimeTempAllowListDuration = activityManagerInternal != null ? activityManagerInternal.getBootTimeTempAllowListDuration() : IDeviceIdleControllerExt.ADVANCE_TIME;
        BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
        makeBasic.setTemporaryAppAllowlist(bootTimeTempAllowListDuration, 0, i, "");
        return makeBasic;
    }

    private static int getUserSwitchTimeoutMs() {
        String str = SystemProperties.get("debug.usercontroller.user_switch_timeout_ms");
        if (TextUtils.isEmpty(str)) {
            return 3000;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return 3000;
        }
    }

    public long getLastUserUnlockingUptime() {
        return this.mLastUserUnlockingUptime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class UserProgressListener extends IProgressListener.Stub {
        private volatile long mUnlockStarted;

        private UserProgressListener() {
        }

        public void onStarted(int i, Bundle bundle) throws RemoteException {
            Slogf.d("ActivityManager", "Started unlocking user " + i);
            this.mUnlockStarted = SystemClock.uptimeMillis();
        }

        public void onProgress(int i, int i2, Bundle bundle) throws RemoteException {
            Slogf.d("ActivityManager", "Unlocking user " + i + " progress " + i2);
        }

        public void onFinished(int i, Bundle bundle) throws RemoteException {
            long uptimeMillis = SystemClock.uptimeMillis() - this.mUnlockStarted;
            if (i == 0) {
                new TimingsTraceAndSlog().logDuration("SystemUserUnlock", uptimeMillis);
                return;
            }
            new TimingsTraceAndSlog().logDuration("User" + i + "Unlock", uptimeMillis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class PendingUserStart {
        public final IProgressListener unlockListener;
        public final int userId;

        @UserManagerInternal.UserStartMode
        public final int userStartMode;

        PendingUserStart(int i, @UserManagerInternal.UserStartMode int i2, IProgressListener iProgressListener) {
            this.userId = i;
            this.userStartMode = i2;
            this.unlockListener = iProgressListener;
        }

        public String toString() {
            return "PendingUserStart{userId=" + this.userId + ", userStartMode=" + UserManagerInternal.userStartModeToString(this.userStartMode) + ", unlockListener=" + this.unlockListener + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        private Handler mHandler;
        private final ActivityManagerService mService;
        private UserManagerService mUserManager;
        private UserManagerInternal mUserManagerInternal;

        @GuardedBy({"mUserSwitchingDialogLock"})
        private UserSwitchingDialog mUserSwitchingDialog;
        private final Object mUserSwitchingDialogLock = new Object();

        Injector(ActivityManagerService activityManagerService) {
            this.mService = activityManagerService;
        }

        protected Handler getHandler(Handler.Callback callback) {
            Handler handler = new Handler(this.mService.mHandlerThread.getLooper(), callback);
            this.mHandler = handler;
            return handler;
        }

        protected Handler getUiHandler(Handler.Callback callback) {
            Handler hookGetUiHandler = UserController.mStaticExt.hookGetUiHandler(callback);
            return hookGetUiHandler != null ? hookGetUiHandler : new Handler(this.mService.mUiHandler.getLooper(), callback);
        }

        protected UserJourneyLogger getUserJourneyLogger() {
            return getUserManager().getUserJourneyLogger();
        }

        protected Context getContext() {
            return this.mService.mContext;
        }

        protected LockPatternUtils getLockPatternUtils() {
            return new LockPatternUtils(getContext());
        }

        protected int broadcastIntent(Intent intent, String str, IIntentReceiver iIntentReceiver, int i, String str2, Bundle bundle, String[] strArr, int i2, Bundle bundle2, boolean z, boolean z2, int i3, int i4, int i5, int i6, int i7) {
            int broadcastIntentLocked;
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (intExtra == -10000) {
                intExtra = i7;
            }
            EventLog.writeEvent(EventLogTags.UC_SEND_USER_BROADCAST, Integer.valueOf(intExtra), intent.getAction());
            ActivityManagerService activityManagerService = this.mService;
            boolean z3 = activityManagerService.mEnableModernQueue ? false : z;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    broadcastIntentLocked = this.mService.broadcastIntentLocked(null, null, null, intent, str, iIntentReceiver, i, str2, bundle, strArr, null, null, i2, bundle2, z3, z2, i3, i4, i5, i6, i7);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
            return broadcastIntentLocked;
        }

        int checkCallingPermission(String str) {
            return this.mService.checkCallingPermission(str);
        }

        WindowManagerService getWindowManager() {
            return this.mService.mWindowManager;
        }

        ActivityTaskManagerInternal getActivityTaskManagerInternal() {
            return this.mService.mAtmInternal;
        }

        void activityManagerOnUserStopped(int i) {
            ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).onUserStopped(i);
        }

        void systemServiceManagerOnUserStopped(int i) {
            getSystemServiceManager().onUserStopped(i);
        }

        void systemServiceManagerOnUserCompletedEvent(int i, int i2) {
            getSystemServiceManager().onUserCompletedEvent(i, i2);
        }

        protected UserManagerService getUserManager() {
            if (this.mUserManager == null) {
                this.mUserManager = IUserManager.Stub.asInterface(ServiceManager.getService("user"));
            }
            return this.mUserManager;
        }

        UserManagerInternal getUserManagerInternal() {
            if (this.mUserManagerInternal == null) {
                this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
            }
            return this.mUserManagerInternal;
        }

        KeyguardManager getKeyguardManager() {
            return (KeyguardManager) this.mService.mContext.getSystemService(KeyguardManager.class);
        }

        void batteryStatsServiceNoteEvent(int i, String str, int i2) {
            this.mService.mBatteryStatsService.noteEvent(i, str, i2);
        }

        boolean isRuntimeRestarted() {
            return getSystemServiceManager().isRuntimeRestarted();
        }

        SystemServiceManager getSystemServiceManager() {
            return this.mService.mSystemServiceManager;
        }

        boolean isFirstBootOrUpgrade() {
            IPackageManager packageManager = AppGlobals.getPackageManager();
            try {
                if (!packageManager.isFirstBoot()) {
                    if (!packageManager.isDeviceUpgrading()) {
                        return false;
                    }
                }
                return true;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        void sendPreBootBroadcast(int i, boolean z, final Runnable runnable) {
            EventLog.writeEvent(EventLogTags.UC_SEND_USER_BROADCAST, Integer.valueOf(i), "android.intent.action.PRE_BOOT_COMPLETED");
            new PreBootBroadcaster(this.mService, i, null, z) { // from class: com.android.server.am.UserController.Injector.1
                @Override // com.android.server.am.PreBootBroadcaster
                public void onFinished() {
                    runnable.run();
                }
            }.sendNext();
        }

        void activityManagerForceStopPackage(int i, String str) {
            ActivityManagerService activityManagerService = this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    this.mService.forceStopPackageLocked(null, -1, false, false, true, false, false, i, str);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }

        int checkComponentPermission(String str, int i, int i2, int i3, boolean z) {
            return ActivityManagerService.checkComponentPermission(str, i, i2, i3, z);
        }

        boolean checkPermissionForPreflight(String str, int i, int i2, String str2) {
            return PermissionChecker.checkPermissionForPreflight(getContext(), str, i, i2, str2) == 0;
        }

        protected void startHomeActivity(int i, String str) {
            this.mService.mAtmInternal.startHomeActivity(i, str);
        }

        void startUserWidgets(final int i) {
            final AppWidgetManagerInternal appWidgetManagerInternal = (AppWidgetManagerInternal) LocalServices.getService(AppWidgetManagerInternal.class);
            if (appWidgetManagerInternal != null) {
                UserController.mStaticExt.hookFgHandler(FgThread.getHandler()).post(new Runnable() { // from class: com.android.server.am.UserController$Injector$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        appWidgetManagerInternal.unlockUser(i);
                    }
                });
            }
        }

        void updateUserConfiguration() {
            this.mService.mAtmInternal.updateUserConfiguration();
        }

        void clearBroadcastQueueForUser(int i) {
            ActivityManagerService activityManagerService = this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    this.mService.clearBroadcastQueueForUserLocked(i);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }

        void loadUserRecents(int i) {
            this.mService.mAtmInternal.loadRecentTasksForUser(i);
        }

        void startPersistentApps(int i) {
            this.mService.startPersistentApps(i);
        }

        void installEncryptionUnawareProviders(int i) {
            this.mService.mCpHelper.installEncryptionUnawareProviders(i);
        }

        void dismissUserSwitchingDialog(Runnable runnable) {
            synchronized (this.mUserSwitchingDialogLock) {
                UserSwitchingDialog userSwitchingDialog = this.mUserSwitchingDialog;
                if (userSwitchingDialog != null) {
                    userSwitchingDialog.dismiss(runnable);
                    this.mUserSwitchingDialog = null;
                } else if (runnable != null) {
                    runnable.run();
                }
            }
        }

        void showUserSwitchingDialog(UserInfo userInfo, UserInfo userInfo2, String str, String str2, Runnable runnable) {
            if (this.mService.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
                Slogf.w("ActivityManager", "Showing user switch dialog on UserController, it could cause a race condition if it's shown by CarSystemUI as well");
            }
            synchronized (this.mUserSwitchingDialogLock) {
                dismissUserSwitchingDialog(null);
                UserSwitchingDialog userSwitchingDialog = new UserSwitchingDialog(this.mService.mContext, userInfo, userInfo2, str, str2, getWindowManager());
                this.mUserSwitchingDialog = userSwitchingDialog;
                userSwitchingDialog.show(runnable);
            }
        }

        void reportGlobalUsageEvent(int i) {
            this.mService.reportGlobalUsageEvent(i);
        }

        void reportCurWakefulnessUsageEvent() {
            this.mService.reportCurWakefulnessUsageEvent();
        }

        void taskSupervisorRemoveUser(int i) {
            this.mService.mAtmInternal.removeUser(i);
        }

        protected boolean taskSupervisorSwitchUser(int i, UserState userState) {
            return this.mService.mAtmInternal.switchUser(i, userState);
        }

        protected void taskSupervisorResumeFocusedStackTopActivity() {
            this.mService.mAtmInternal.resumeTopActivities(false);
        }

        protected void clearAllLockedTasks(String str) {
            this.mService.mAtmInternal.clearLockedTasks(str);
        }

        boolean isCallerRecents(int i) {
            return this.mService.mAtmInternal.isCallerRecents(i);
        }

        protected IStorageManager getStorageManager() {
            return IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void dismissKeyguard(final Runnable runnable) {
            final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            final Runnable runnable2 = new Runnable() { // from class: com.android.server.am.UserController$Injector$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    UserController.Injector.lambda$dismissKeyguard$1(atomicBoolean, runnable);
                }
            };
            this.mHandler.postDelayed(runnable2, 2000L);
            getWindowManager().dismissKeyguard(new IKeyguardDismissCallback.Stub() { // from class: com.android.server.am.UserController.Injector.2
                public void onDismissError() throws RemoteException {
                    Injector.this.mHandler.post(runnable2);
                }

                public void onDismissSucceeded() throws RemoteException {
                    Injector.this.mHandler.post(runnable2);
                }

                public void onDismissCancelled() throws RemoteException {
                    Injector.this.mHandler.post(runnable2);
                }
            }, (CharSequence) null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$dismissKeyguard$1(AtomicBoolean atomicBoolean, Runnable runnable) {
            if (atomicBoolean.getAndSet(false)) {
                runnable.run();
            }
        }

        boolean isHeadlessSystemUserMode() {
            return UserManager.isHeadlessSystemUserMode();
        }

        boolean isUsersOnSecondaryDisplaysEnabled() {
            return UserManager.isVisibleBackgroundUsersEnabled();
        }

        void onUserStarting(int i) {
            getSystemServiceManager().onUserStarting(TimingsTraceAndSlog.newAsyncLog(), i);
        }

        void onSystemUserVisibilityChanged(boolean z) {
            getUserManagerInternal().onSystemUserVisibilityChanged(z);
        }

        void lockDeviceNowAndWaitForKeyguardShown() {
            if (getWindowManager().isKeyguardLocked()) {
                return;
            }
            TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
            timingsTraceAndSlog.traceBegin("lockDeviceNowAndWaitForKeyguardShown");
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ActivityTaskManagerInternal.ScreenObserver screenObserver = new ActivityTaskManagerInternal.ScreenObserver() { // from class: com.android.server.am.UserController.Injector.3
                public void onAwakeStateChanged(boolean z) {
                }

                public void onKeyguardStateChanged(boolean z) {
                    if (z) {
                        countDownLatch.countDown();
                    }
                }
            };
            getActivityTaskManagerInternal().registerScreenObserver(screenObserver);
            getWindowManager().lockDeviceNow();
            try {
                try {
                    if (countDownLatch.await(20L, TimeUnit.SECONDS)) {
                    } else {
                        throw new RuntimeException("Keyguard is not shown in 20 seconds");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                getActivityTaskManagerInternal().unregisterScreenObserver(screenObserver);
                timingsTraceAndSlog.traceEnd();
            }
        }
    }

    public IUserControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class UserControllerWrapper implements IUserControllerWrapper {
        private UserControllerWrapper() {
        }

        @Override // com.android.server.am.IUserControllerWrapper
        public SparseIntArray getUserProfileGroupIds() {
            return UserController.this.mUserProfileGroupIds;
        }

        @Override // com.android.server.am.IUserControllerWrapper
        public void startUserInForeground(int i) {
            UserController.this.startUserInForeground(i);
        }

        @Override // com.android.server.am.IUserControllerWrapper
        public boolean maybeUnlockUser(int i) {
            return UserController.this.maybeUnlockUser(i);
        }
    }
}
