package com.android.server.wm;

import android.app.ActivityThread;
import android.app.BackgroundStartPrivileges;
import android.app.IActivityController;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.app.servertransaction.ConfigurationChangeItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Build;
import android.os.Debug;
import android.os.InputConstants;
import android.os.LocaleList;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.Watchdog;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.ActivityTaskManagerService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowProcessController extends ConfigurationContainer<ConfigurationContainer> implements ConfigurationContainerListener {
    private static final int ACTIVITY_STATE_FLAG_HAS_ACTIVITY_IN_VISIBLE_TASK = 4194304;
    private static final int ACTIVITY_STATE_FLAG_HAS_RESUMED = 2097152;
    private static final int ACTIVITY_STATE_FLAG_IS_PAUSING_OR_PAUSED = 131072;
    private static final int ACTIVITY_STATE_FLAG_IS_STOPPING = 262144;
    private static final int ACTIVITY_STATE_FLAG_IS_STOPPING_FINISHING = 524288;
    private static final int ACTIVITY_STATE_FLAG_IS_VISIBLE = 65536;
    private static final int ACTIVITY_STATE_FLAG_IS_WINDOW_VISIBLE = 1048576;
    private static final int ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER = 65535;
    private static final int CACHED_CONFIG_PROC_STATE = 16;
    private static final int MAX_RAPID_ACTIVITY_LAUNCH_COUNT = 500;
    private static final long RAPID_ACTIVITY_LAUNCH_MS = 300;
    private static final int REMOTE_ACTIVITY_FLAG_EMBEDDED_ACTIVITY = 2;
    private static final int REMOTE_ACTIVITY_FLAG_HOST_ACTIVITY = 1;
    private static final long RESET_RAPID_ACTIVITY_LAUNCH_MS = 1500;
    private final ActivityTaskManagerService mAtm;
    private final BackgroundLaunchProcessController mBgLaunchController;
    private ActivityRecord mConfigActivityRecord;
    private volatile boolean mCrashing;
    private volatile int mCurSchedGroup;
    private volatile boolean mDebugging;
    private DisplayArea mDisplayArea;
    private volatile long mFgInteractionTime;
    private volatile boolean mHasActivities;
    private volatile boolean mHasCachedConfiguration;
    private volatile boolean mHasClientActivities;
    private volatile boolean mHasForegroundServices;
    private volatile boolean mHasImeService;
    private volatile boolean mHasOverlayUi;
    private boolean mHasPendingConfigurationChange;
    private volatile boolean mHasRecentTasks;
    private volatile boolean mHasTopUi;
    private ArrayList<ActivityRecord> mInactiveActivities;
    final ApplicationInfo mInfo;
    private volatile boolean mInstrumenting;
    private volatile boolean mInstrumentingWithBackgroundActivityStartPrivileges;
    private volatile long mInteractionEventTime;
    private volatile boolean mIsActivityConfigOverrideAllowed;
    private volatile long mLastActivityFinishTime;
    private volatile long mLastActivityLaunchTime;
    private final WindowProcessListener mListener;
    final String mName;
    private volatile boolean mNotResponding;
    public final Object mOwner;
    private int mPauseConfigurationDispatchCount;
    private volatile boolean mPendingUiClean;
    private volatile boolean mPerceptible;
    private volatile boolean mPersistent;
    public volatile int mPid;
    private int mRapidActivityLaunchCount;
    private ArrayMap<ActivityRecord, int[]> mRemoteActivities;
    private volatile String mRequiredAbi;
    private boolean mRunningRecentsAnimation;
    private boolean mRunningRemoteAnimation;
    private IApplicationThread mThread;
    public final int mUid;
    final int mUserId;
    private volatile boolean mUsingWrapper;
    int mVrThreadTid;
    private volatile long mWhenUnimportant;
    private static final String TAG = "ActivityTaskManager";
    private static final String TAG_RELEASE = TAG + ActivityTaskManagerDebugConfig.POSTFIX_RELEASE;
    private static final String TAG_CONFIGURATION = TAG + ActivityTaskManagerDebugConfig.POSTFIX_CONFIGURATION;
    private static final boolean isAgingVersion = "1".equals(SystemProperties.get("persist.sys.agingtest", "0"));

    @GuardedBy({"itself"})
    private final ArrayList<String> mPkgList = new ArrayList<>(1);
    private volatile int mCurProcState = 20;
    private volatile int mRepProcState = 20;
    private volatile int mCurAdj = -10000;
    private volatile int mInstrumentationSourceUid = -1;
    private final ArrayList<ActivityRecord> mActivities = new ArrayList<>();
    private final ArrayList<Task> mRecentTasks = new ArrayList<>();
    private ActivityRecord mPreQTopResumedActivity = null;
    private final Configuration mLastReportedConfiguration = new Configuration();
    private IWindowProcessControllerWrapper mWindowProcessControllerWrapper = new WindowProcessControllerWrapper();
    private int mLastTopActivityDeviceId = 0;
    private volatile int mActivityStateFlags = ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER;
    private IWindowProcessControllerExt mWindProcessConExt = (IWindowProcessControllerExt) ExtLoader.type(IWindowProcessControllerExt.class).base(this).create();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface ComputeOomAdjCallback {
        void onOtherActivity();

        void onPausedActivity();

        void onStoppingActivity(boolean z);

        void onVisibleActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateTopResumingActivityInProcessIfNeeded$1(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return activityRecord2 == activityRecord;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected ConfigurationContainer getChildAt(int i) {
        return null;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected int getChildCount() {
        return 0;
    }

    public WindowProcessController(final ActivityTaskManagerService activityTaskManagerService, ApplicationInfo applicationInfo, String str, int i, int i2, Object obj, WindowProcessListener windowProcessListener) {
        this.mIsActivityConfigOverrideAllowed = true;
        this.mInfo = applicationInfo;
        this.mName = str;
        this.mUid = i;
        this.mUserId = i2;
        this.mOwner = obj;
        this.mListener = windowProcessListener;
        this.mAtm = activityTaskManagerService;
        Objects.requireNonNull(activityTaskManagerService);
        this.mBgLaunchController = new BackgroundLaunchProcessController(new IntPredicate() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda7
            @Override // java.util.function.IntPredicate
            public final boolean test(int i3) {
                return ActivityTaskManagerService.this.hasActiveVisibleWindow(i3);
            }
        }, activityTaskManagerService.getBackgroundActivityStartCallback());
        if (applicationInfo.packageName.equals(activityTaskManagerService.getSysUiServiceComponentLocked().getPackageName()) || UserHandle.getAppId(i) == 1000) {
            this.mIsActivityConfigOverrideAllowed = false;
        }
        onConfigurationChanged(activityTaskManagerService.getGlobalConfiguration());
        activityTaskManagerService.mPackageConfigPersister.updateConfigIfNeeded(this, i2, applicationInfo.packageName);
    }

    public void setPid(int i) {
        this.mPid = i;
    }

    public int getPid() {
        return this.mPid;
    }

    public ArrayList<ActivityRecord> getActivities() {
        ArrayList<ActivityRecord> arrayList;
        synchronized (this.mAtm.mGlobalLockWithoutBoost) {
            arrayList = this.mActivities;
        }
        return arrayList;
    }

    public void setThread(IApplicationThread iApplicationThread) {
        synchronized (this.mAtm.mGlobalLockWithoutBoost) {
            this.mThread = iApplicationThread;
            if (iApplicationThread != null) {
                setLastReportedConfiguration(getConfiguration());
            } else {
                this.mAtm.mVisibleActivityProcessTracker.removeProcess(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IApplicationThread getThread() {
        return this.mThread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasThread() {
        return this.mThread != null;
    }

    public void setCurrentSchedulingGroup(int i) {
        this.mCurSchedGroup = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentSchedulingGroup() {
        return this.mCurSchedGroup;
    }

    public void setCurrentProcState(int i) {
        this.mCurProcState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentProcState() {
        return this.mCurProcState;
    }

    public void setCurrentAdj(int i) {
        this.mCurAdj = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentAdj() {
        return this.mCurAdj;
    }

    public void setReportedProcState(int i) {
        Configuration configuration;
        int i2 = this.mRepProcState;
        this.mRepProcState = i;
        IApplicationThread iApplicationThread = this.mThread;
        if (i2 < 16 || i >= 16 || iApplicationThread == null || !this.mHasCachedConfiguration) {
            return;
        }
        synchronized (this.mLastReportedConfiguration) {
            configuration = new Configuration(this.mLastReportedConfiguration);
        }
        scheduleConfigurationChange(iApplicationThread, configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getReportedProcState() {
        return this.mRepProcState;
    }

    public void setCrashing(boolean z) {
        this.mCrashing = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleAppCrash() {
        ArrayList arrayList = new ArrayList(this.mActivities);
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord = (ActivityRecord) arrayList.get(size);
            Slog.w(TAG, "  Force finishing activity " + activityRecord.mActivityComponent.flattenToShortString());
            activityRecord.detachFromProcess();
            activityRecord.mDisplayContent.requestTransitionAndLegacyPrepare(2, 16);
            activityRecord.destroyIfPossible("handleAppCrashed");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCrashing() {
        return this.mCrashing;
    }

    public void setNotResponding(boolean z) {
        this.mNotResponding = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNotResponding() {
        return this.mNotResponding;
    }

    public void setPersistent(boolean z) {
        this.mPersistent = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPersistent() {
        return this.mPersistent;
    }

    public void setHasForegroundServices(boolean z) {
        this.mHasForegroundServices = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServices() {
        return this.mHasForegroundServices;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundActivities() {
        return this.mAtm.mTopApp == this || (this.mActivityStateFlags & 458752) != 0;
    }

    public void setHasClientActivities(boolean z) {
        this.mHasClientActivities = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasClientActivities() {
        return this.mHasClientActivities;
    }

    public void setHasTopUi(boolean z) {
        this.mHasTopUi = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasTopUi() {
        return this.mHasTopUi;
    }

    public void setHasOverlayUi(boolean z) {
        this.mHasOverlayUi = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasOverlayUi() {
        return this.mHasOverlayUi;
    }

    public void setPendingUiClean(boolean z) {
        this.mPendingUiClean = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPendingUiClean() {
        return this.mPendingUiClean;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean registeredForDisplayAreaConfigChanges() {
        return this.mDisplayArea != null;
    }

    @VisibleForTesting
    boolean registeredForActivityConfigChanges() {
        return this.mConfigActivityRecord != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postPendingUiCleanMsg(boolean z) {
        this.mAtm.mH.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda8
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((WindowProcessListener) obj).setPendingUiClean(((Boolean) obj2).booleanValue());
            }
        }, this.mListener, Boolean.valueOf(z)));
    }

    public void setInteractionEventTime(long j) {
        this.mInteractionEventTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getInteractionEventTime() {
        return this.mInteractionEventTime;
    }

    public void setFgInteractionTime(long j) {
        this.mFgInteractionTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getFgInteractionTime() {
        return this.mFgInteractionTime;
    }

    public void setWhenUnimportant(long j) {
        this.mWhenUnimportant = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getWhenUnimportant() {
        return this.mWhenUnimportant;
    }

    public void setRequiredAbi(String str) {
        this.mRequiredAbi = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getRequiredAbi() {
        return this.mRequiredAbi;
    }

    @VisibleForTesting
    DisplayArea getDisplayArea() {
        return this.mDisplayArea;
    }

    public void setDebugging(boolean z) {
        this.mDebugging = z;
    }

    boolean isDebugging() {
        return this.mDebugging;
    }

    public void setUsingWrapper(boolean z) {
        this.mUsingWrapper = z;
    }

    boolean isUsingWrapper() {
        return this.mUsingWrapper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasEverLaunchedActivity() {
        return this.mLastActivityLaunchTime > 0;
    }

    void setLastActivityLaunchTime(ActivityRecord activityRecord) {
        long j = activityRecord.lastLaunchTime;
        if (j > this.mLastActivityLaunchTime) {
            updateRapidActivityLaunch(activityRecord, j, this.mLastActivityLaunchTime);
            this.mLastActivityLaunchTime = j;
        } else if (j < this.mLastActivityLaunchTime) {
            Slog.w(TAG, "Tried to set launchTime (" + j + ") < mLastActivityLaunchTime (" + this.mLastActivityLaunchTime + ")");
        }
    }

    void updateRapidActivityLaunch(ActivityRecord activityRecord, long j, long j2) {
        if (this.mInstrumenting || this.mDebugging || j2 <= 0) {
            return;
        }
        long j3 = j - j2;
        if (j3 < RAPID_ACTIVITY_LAUNCH_MS) {
            this.mRapidActivityLaunchCount++;
        } else if (j3 >= RESET_RAPID_ACTIVITY_LAUNCH_MS) {
            this.mRapidActivityLaunchCount = 0;
        }
        if (this.mRapidActivityLaunchCount > MAX_RAPID_ACTIVITY_LAUNCH_COUNT) {
            if (this.mWindProcessConExt.hookUpdateRapidActivityLaunchSkipApp(this.mName)) {
                this.mRapidActivityLaunchCount = 0;
                return;
            }
            Slog.w(TAG, "Killing " + this.mPid + " because of rapid activity launch");
            activityRecord.getRootTask().moveTaskToBack(activityRecord.getTask());
            this.mAtm.mH.post(new Runnable() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    WindowProcessController.this.lambda$updateRapidActivityLaunch$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRapidActivityLaunch$0() {
        this.mAtm.mAmInternal.killProcess(this.mName, this.mUid, "rapidActivityLaunch");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastActivityFinishTimeIfNeeded(long j) {
        if (j <= this.mLastActivityFinishTime || !hasActivityInVisibleTask()) {
            return;
        }
        this.mLastActivityFinishTime = j;
    }

    public void addOrUpdateBackgroundStartPrivileges(Binder binder, BackgroundStartPrivileges backgroundStartPrivileges) {
        Objects.requireNonNull(binder, "entity");
        Objects.requireNonNull(backgroundStartPrivileges, "backgroundStartPrivileges");
        Preconditions.checkArgument(backgroundStartPrivileges.allowsAny(), "backgroundStartPrivileges does not allow anything");
        this.mBgLaunchController.addOrUpdateAllowBackgroundStartPrivileges(binder, backgroundStartPrivileges);
    }

    public void removeBackgroundStartPrivileges(Binder binder) {
        Objects.requireNonNull(binder, "entity");
        this.mBgLaunchController.removeAllowBackgroundStartPrivileges(binder);
    }

    public boolean areBackgroundFgsStartsAllowed() {
        return areBackgroundActivityStartsAllowed(this.mAtm.getBalAppSwitchesState(), true) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int areBackgroundActivityStartsAllowed(int i) {
        return areBackgroundActivityStartsAllowed(i, false);
    }

    private int areBackgroundActivityStartsAllowed(int i, boolean z) {
        return this.mBgLaunchController.areBackgroundActivityStartsAllowed(this.mPid, this.mUid, this.mInfo.packageName, i, z, hasActivityInVisibleTask(), this.mInstrumentingWithBackgroundActivityStartPrivileges, this.mAtm.getLastStopAppSwitchesTime(), this.mLastActivityLaunchTime, this.mLastActivityFinishTime);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canCloseSystemDialogsByToken() {
        return this.mBgLaunchController.canCloseSystemDialogsByToken(this.mUid);
    }

    public void clearBoundClientUids() {
        this.mBgLaunchController.clearBalOptInBoundClientUids();
    }

    public void addBoundClientUid(int i, String str, long j) {
        this.mBgLaunchController.addBoundClientUid(i, str, j);
    }

    public void setInstrumenting(boolean z, int i, boolean z2) {
        Preconditions.checkArgument(z || i == -1);
        this.mInstrumenting = z;
        this.mInstrumentationSourceUid = i;
        this.mInstrumentingWithBackgroundActivityStartPrivileges = z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInstrumenting() {
        return this.mInstrumenting;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInstrumentationSourceUid() {
        return this.mInstrumentationSourceUid;
    }

    public void setPerceptible(boolean z) {
        this.mPerceptible = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPerceptible() {
        return this.mPerceptible;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected ConfigurationContainer getParent() {
        return this.mAtm.mRootWindowContainer;
    }

    public void addPackage(String str) {
        synchronized (this.mPkgList) {
            if (!this.mPkgList.contains(str)) {
                this.mPkgList.add(str);
            }
        }
    }

    public void clearPackageList() {
        synchronized (this.mPkgList) {
            this.mPkgList.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsPackage(String str) {
        boolean contains;
        synchronized (this.mPkgList) {
            contains = this.mPkgList.contains(str);
        }
        return contains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getPackageList() {
        ArrayList arrayList;
        synchronized (this.mPkgList) {
            arrayList = new ArrayList(this.mPkgList);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addActivityIfNeeded(ActivityRecord activityRecord) {
        setLastActivityLaunchTime(activityRecord);
        if (this.mActivities.contains(activityRecord)) {
            return;
        }
        this.mActivities.add(activityRecord);
        this.mHasActivities = true;
        ArrayList<ActivityRecord> arrayList = this.mInactiveActivities;
        if (arrayList != null) {
            arrayList.remove(activityRecord);
        }
        updateActivityConfigurationListener();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeActivity(ActivityRecord activityRecord, boolean z) {
        if (z) {
            ArrayList<ActivityRecord> arrayList = this.mInactiveActivities;
            if (arrayList == null) {
                ArrayList<ActivityRecord> arrayList2 = new ArrayList<>();
                this.mInactiveActivities = arrayList2;
                arrayList2.add(activityRecord);
            } else if (!arrayList.contains(activityRecord)) {
                this.mInactiveActivities.add(activityRecord);
            }
        } else {
            ArrayList<ActivityRecord> arrayList3 = this.mInactiveActivities;
            if (arrayList3 != null) {
                arrayList3.remove(activityRecord);
            }
        }
        this.mActivities.remove(activityRecord);
        this.mHasActivities = !this.mActivities.isEmpty();
        updateActivityConfigurationListener();
    }

    void clearActivities() {
        this.mInactiveActivities = null;
        this.mActivities.clear();
        this.mHasActivities = false;
        updateActivityConfigurationListener();
    }

    public boolean hasActivities() {
        return this.mHasActivities;
    }

    public boolean hasVisibleActivities() {
        return (this.mActivityStateFlags & ACTIVITY_STATE_FLAG_IS_VISIBLE) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasActivityInVisibleTask() {
        return (this.mActivityStateFlags & ACTIVITY_STATE_FLAG_HAS_ACTIVITY_IN_VISIBLE_TASK) != 0;
    }

    public boolean hasActivitiesOrRecentTasks() {
        return this.mHasActivities || this.mHasRecentTasks;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskDisplayArea getTopActivityDisplayArea() {
        if (this.mActivities.isEmpty()) {
            return null;
        }
        int size = this.mActivities.size() - 1;
        ActivityRecord activityRecord = this.mActivities.get(size);
        TaskDisplayArea displayArea = activityRecord.getDisplayArea();
        for (int i = size - 1; i >= 0; i--) {
            ActivityRecord activityRecord2 = this.mActivities.get(i);
            TaskDisplayArea displayArea2 = activityRecord2.getDisplayArea();
            if (activityRecord2.compareTo((WindowContainer) activityRecord) > 0 && displayArea2 != null) {
                activityRecord = activityRecord2;
                displayArea = displayArea2;
            }
        }
        return displayArea;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateTopResumingActivityInProcessIfNeeded(final ActivityRecord activityRecord) {
        TaskFragment taskFragment;
        ActivityRecord activity;
        Task task = activityRecord.getTask();
        boolean z = task == null || task.getWrapper().getExtImpl().dontPauseAfterQActivityIfNeed(task);
        if ((this.mInfo.targetSdkVersion >= 29 && z) || this.mPreQTopResumedActivity == activityRecord) {
            return true;
        }
        if (task != null && task.getWrapper().getExtImpl().supportMultiResume(activityRecord)) {
            return true;
        }
        if (!activityRecord.isAttached()) {
            return false;
        }
        if (activityRecord.getWindowingMode() == 120 && z) {
            return true;
        }
        ActivityRecord activityRecord2 = this.mPreQTopResumedActivity;
        DisplayContent displayContent = (activityRecord2 == null || !activityRecord2.isAttached()) ? null : this.mPreQTopResumedActivity.mDisplayContent;
        boolean z2 = (displayContent != null && this.mPreQTopResumedActivity.isVisibleRequested() && this.mPreQTopResumedActivity.isFocusable()) ? false : true;
        DisplayContent displayContent2 = activityRecord.mDisplayContent;
        if (!z2 && displayContent.compareTo((WindowContainer) displayContent2) < 0) {
            z2 = true;
        }
        boolean z3 = (z2 || (activity = displayContent.getActivity(new Predicate() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$updateTopResumingActivityInProcessIfNeeded$1;
                lambda$updateTopResumingActivityInProcessIfNeeded$1 = WindowProcessController.lambda$updateTopResumingActivityInProcessIfNeeded$1(ActivityRecord.this, (ActivityRecord) obj);
                return lambda$updateTopResumingActivityInProcessIfNeeded$1;
            }
        }, true, this.mPreQTopResumedActivity)) == null || activity == this.mPreQTopResumedActivity) ? z2 : true;
        if (z3) {
            ActivityRecord activityRecord3 = this.mPreQTopResumedActivity;
            if (activityRecord3 != null && activityRecord3.isState(ActivityRecord.State.RESUMED) && (taskFragment = this.mPreQTopResumedActivity.getTaskFragment()) != null) {
                ActivityRecord pausingActivity = taskFragment.getPausingActivity();
                if (this.mWindProcessConExt.canSetPreQTopResumedActivity(taskFragment, this.mInfo.targetSdkVersion)) {
                    this.mPreQTopResumedActivity = activityRecord;
                }
                boolean shouldBeVisible = taskFragment.shouldBeVisible(null);
                Slog.d(TAG, "updateTopResumingActivityInProcess pkg=" + this.mInfo.packageName + ",v=" + this.mInfo.targetSdkVersion + ",dont=" + z + "," + activityRecord + ",mPreQTopResumedActivity=" + this.mPreQTopResumedActivity + ",taskFrag taskFragPausingActivity=" + pausingActivity);
                taskFragment.startPausing(shouldBeVisible, false, activityRecord, "top-resumed-changed");
            }
            this.mPreQTopResumedActivity = activityRecord;
        }
        return z3;
    }

    public void stopFreezingActivities() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int size = this.mActivities.size();
                while (size > 0) {
                    size--;
                    this.mActivities.get(size).stopFreezingScreenLocked(true);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishActivities() {
        ArrayList arrayList = new ArrayList(this.mActivities);
        for (int i = 0; i < arrayList.size(); i++) {
            ActivityRecord activityRecord = (ActivityRecord) arrayList.get(i);
            if (!activityRecord.finishing && activityRecord.isInRootTaskLocked()) {
                activityRecord.finishIfPossible("finish-heavy", true);
            }
        }
    }

    public boolean isInterestingToUser() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int size = this.mActivities.size();
                for (int i = 0; i < size; i++) {
                    if (this.mActivities.get(i).isInterestingToUserLocked()) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return true;
                    }
                }
                if (hasEmbeddedWindow()) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                return false;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean hasEmbeddedWindow() {
        ArrayMap<ActivityRecord, int[]> arrayMap = this.mRemoteActivities;
        if (arrayMap == null) {
            return false;
        }
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            if ((this.mRemoteActivities.valueAt(size)[0] & 1) != 0 && this.mRemoteActivities.keyAt(size).isInterestingToUserLocked()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRunningActivity(String str) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (int size = this.mActivities.size() - 1; size >= 0; size--) {
                    if (str.equals(this.mActivities.get(size).packageName)) {
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAppSpecificSettingsForAllActivitiesInPackage(String str, Integer num, LocaleList localeList, @Configuration.GrammaticalGender int i) {
        for (int size = this.mActivities.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord = this.mActivities.get(size);
            if (str.equals(activityRecord.packageName) && activityRecord.applyAppSpecificConfig(num, localeList, Integer.valueOf(i)) && activityRecord.isVisibleRequested()) {
                activityRecord.ensureActivityConfiguration(0, true);
            }
        }
    }

    public void clearPackagePreferredForHomeActivities() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (int size = this.mActivities.size() - 1; size >= 0; size--) {
                    ActivityRecord activityRecord = this.mActivities.get(size);
                    if (activityRecord.isActivityTypeHome()) {
                        Log.i(TAG, "Clearing package preferred activities from " + activityRecord.packageName);
                        try {
                            ActivityThread.getPackageManager().clearPackagePreferredActivities(activityRecord.packageName);
                        } catch (RemoteException unused) {
                        }
                    }
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasStartedActivity(ActivityRecord activityRecord) {
        for (int size = this.mActivities.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord2 = this.mActivities.get(size);
            if (activityRecord != activityRecord2 && !activityRecord2.mAppStopped) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasResumedActivity() {
        return (this.mActivityStateFlags & ACTIVITY_STATE_FLAG_HAS_RESUMED) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateIntentForHeavyWeightActivity(Intent intent) {
        if (this.mActivities.isEmpty()) {
            return;
        }
        ActivityRecord activityRecord = this.mActivities.get(0);
        intent.putExtra("cur_app", activityRecord.packageName);
        intent.putExtra("cur_task", activityRecord.getTask().mTaskId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldKillProcessForRemovedTask(Task task) {
        for (int i = 0; i < this.mActivities.size(); i++) {
            ActivityRecord activityRecord = this.mActivities.get(i);
            if (!activityRecord.mAppStopped) {
                return false;
            }
            Task task2 = activityRecord.getTask();
            if (task != null && task2 != null && task.mTaskId != task2.mTaskId && task2.inRecents) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseSomeActivities(String str) {
        if (ActivityTaskManagerDebugConfig.DEBUG_RELEASE) {
            Slog.d(TAG_RELEASE, "Trying to release some activities in " + this);
        }
        ArrayList arrayList = null;
        for (int i = 0; i < this.mActivities.size(); i++) {
            ActivityRecord activityRecord = this.mActivities.get(i);
            if (activityRecord.finishing || activityRecord.isState(ActivityRecord.State.DESTROYING, ActivityRecord.State.DESTROYED)) {
                if (ActivityTaskManagerDebugConfig.DEBUG_RELEASE) {
                    Slog.d(TAG_RELEASE, "Abort release; already destroying: " + activityRecord);
                    return;
                }
                return;
            }
            if (activityRecord.isVisibleRequested() || !activityRecord.mAppStopped || !activityRecord.hasSavedState() || !activityRecord.isDestroyable() || activityRecord.isState(ActivityRecord.State.STARTED, ActivityRecord.State.RESUMED, ActivityRecord.State.PAUSING, ActivityRecord.State.PAUSED, ActivityRecord.State.STOPPING)) {
                if (ActivityTaskManagerDebugConfig.DEBUG_RELEASE) {
                    Slog.d(TAG_RELEASE, "Not releasing in-use activity: " + activityRecord);
                }
            } else if (activityRecord.getParent() != null) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(activityRecord);
            }
        }
        if (arrayList != null) {
            arrayList.sort(new Comparator() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda10
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return ((ActivityRecord) obj).compareTo((WindowContainer) obj2);
                }
            });
            int max = Math.max(arrayList.size(), 1);
            do {
                ActivityRecord activityRecord2 = (ActivityRecord) arrayList.remove(0);
                Slog.v(TAG_RELEASE, "Destroying " + activityRecord2 + " in state " + activityRecord2.getState() + " for reason " + str + " " + Debug.getCallers(5));
                activityRecord2.destroyImmediately(str);
                max += -1;
            } while (max > 0);
        }
    }

    public void getDisplayContextsWithErrorDialogs(List<Context> list) {
        if (list == null) {
            return;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                RootWindowContainer rootWindowContainer = this.mAtm.mWindowManager.mRoot;
                rootWindowContainer.getDisplayContextsWithNonToastVisibleWindows(this.mPid, list);
                for (int size = this.mActivities.size() - 1; size >= 0; size--) {
                    ActivityRecord activityRecord = this.mActivities.get(size);
                    Context displayUiContext = rootWindowContainer.getDisplayUiContext(activityRecord.getDisplayId());
                    if (displayUiContext != null && activityRecord.isVisibleRequested() && !list.contains(displayUiContext)) {
                        list.add(displayUiContext);
                    }
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addHostActivity(ActivityRecord activityRecord) {
        int[] remoteActivityFlags = getRemoteActivityFlags(activityRecord);
        remoteActivityFlags[0] = remoteActivityFlags[0] | 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeHostActivity(ActivityRecord activityRecord) {
        removeRemoteActivityFlags(activityRecord, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addEmbeddedActivity(ActivityRecord activityRecord) {
        int[] remoteActivityFlags = getRemoteActivityFlags(activityRecord);
        remoteActivityFlags[0] = remoteActivityFlags[0] | 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeEmbeddedActivity(ActivityRecord activityRecord) {
        removeRemoteActivityFlags(activityRecord, 2);
    }

    private int[] getRemoteActivityFlags(ActivityRecord activityRecord) {
        if (this.mRemoteActivities == null) {
            this.mRemoteActivities = new ArrayMap<>();
        }
        int[] iArr = this.mRemoteActivities.get(activityRecord);
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[1];
        this.mRemoteActivities.put(activityRecord, iArr2);
        return iArr2;
    }

    private void removeRemoteActivityFlags(ActivityRecord activityRecord, int i) {
        int indexOfKey;
        ArrayMap<ActivityRecord, int[]> arrayMap = this.mRemoteActivities;
        if (arrayMap != null && (indexOfKey = arrayMap.indexOfKey(activityRecord)) >= 0) {
            int[] valueAt = this.mRemoteActivities.valueAt(indexOfKey);
            int i2 = (~i) & valueAt[0];
            valueAt[0] = i2;
            if (i2 == 0) {
                this.mRemoteActivities.removeAt(indexOfKey);
            }
        }
    }

    public int computeOomAdjFromActivities(ComputeOomAdjCallback computeOomAdjCallback) {
        int i = this.mActivityStateFlags;
        if ((ACTIVITY_STATE_FLAG_IS_VISIBLE & i) != 0) {
            computeOomAdjCallback.onVisibleActivity();
        } else if ((ACTIVITY_STATE_FLAG_IS_PAUSING_OR_PAUSED & i) != 0) {
            computeOomAdjCallback.onPausedActivity();
        } else if ((ACTIVITY_STATE_FLAG_IS_STOPPING & i) != 0) {
            computeOomAdjCallback.onStoppingActivity((ACTIVITY_STATE_FLAG_IS_STOPPING_FINISHING & i) != 0);
        } else {
            computeOomAdjCallback.onOtherActivity();
        }
        return i & ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void computeProcessActivityState() {
        int i;
        ActivityRecord.State state;
        int i2;
        ActivityRecord.State state2 = ActivityRecord.State.DESTROYED;
        boolean hasResumedActivity = hasResumedActivity();
        boolean z = (this.mActivityStateFlags & 1114112) != 0;
        int i3 = Integer.MAX_VALUE;
        boolean z2 = true;
        int i4 = 0;
        boolean z3 = false;
        for (int size = this.mActivities.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord = this.mActivities.get(size);
            if (activityRecord.isVisible()) {
                i4 |= ACTIVITY_STATE_FLAG_IS_WINDOW_VISIBLE;
            }
            Task task = activityRecord.getTask();
            if (task != null && task.mLayerRank != -1) {
                i4 |= ACTIVITY_STATE_FLAG_HAS_ACTIVITY_IN_VISIBLE_TASK;
            }
            if (activityRecord.isVisibleRequested()) {
                if (activityRecord.isState(ActivityRecord.State.RESUMED)) {
                    i4 |= ACTIVITY_STATE_FLAG_HAS_RESUMED;
                }
                if (task != null && i3 > 0 && (i2 = task.mLayerRank) >= 0 && i3 > i2) {
                    i3 = i2;
                }
                z3 = true;
            } else if (!z3 && state2 != (state = ActivityRecord.State.PAUSING)) {
                if (!activityRecord.isState(state, ActivityRecord.State.PAUSED)) {
                    state = ActivityRecord.State.STOPPING;
                    if (activityRecord.isState(state)) {
                        z2 &= activityRecord.finishing;
                    }
                }
                state2 = state;
            }
        }
        ArrayMap<ActivityRecord, int[]> arrayMap = this.mRemoteActivities;
        if (arrayMap != null) {
            for (int size2 = arrayMap.size() - 1; size2 >= 0; size2--) {
                if ((this.mRemoteActivities.valueAt(size2)[0] & 2) != 0 && this.mRemoteActivities.keyAt(size2).isVisibleRequested()) {
                    i4 |= ACTIVITY_STATE_FLAG_IS_VISIBLE;
                }
            }
        }
        int i5 = (ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER & i3) | i4;
        if (z3) {
            i5 |= ACTIVITY_STATE_FLAG_IS_VISIBLE;
        } else {
            if (state2 == ActivityRecord.State.PAUSING) {
                i = ACTIVITY_STATE_FLAG_IS_PAUSING_OR_PAUSED;
            } else if (state2 == ActivityRecord.State.STOPPING) {
                i5 |= ACTIVITY_STATE_FLAG_IS_STOPPING;
                if (z2) {
                    i = ACTIVITY_STATE_FLAG_IS_STOPPING_FINISHING;
                }
            }
            i5 |= i;
        }
        this.mActivityStateFlags = i5;
        boolean z4 = (i5 & 1114112) != 0;
        if (!z && z4) {
            this.mAtm.mVisibleActivityProcessTracker.onAnyActivityVisible(this);
            return;
        }
        if (z && !z4) {
            this.mAtm.mVisibleActivityProcessTracker.onAllActivitiesInvisible(this);
        } else if (z && !hasResumedActivity && hasResumedActivity()) {
            this.mAtm.mVisibleActivityProcessTracker.onActivityResumedWhileVisible(this);
        }
    }

    private void prepareOomAdjustment() {
        this.mAtm.mRootWindowContainer.rankTaskLayers();
        this.mAtm.mTaskSupervisor.computeProcessActivityStateBatch();
    }

    public int computeRelaunchReason() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (int size = this.mActivities.size() - 1; size >= 0; size--) {
                    int i = this.mActivities.get(size).mRelaunchReason;
                    if (i != 0) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return i;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                return 0;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public long getInputDispatchingTimeoutMillis() {
        long j;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                j = (isInstrumenting() || isUsingWrapper()) ? 60000L : InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearProfilerIfNeeded() {
        this.mAtm.mH.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((WindowProcessListener) obj).clearProfilerIfNeeded();
            }
        }, this.mListener));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateProcessInfo(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z4) {
            addToPendingTop();
        }
        if (z3) {
            prepareOomAdjustment();
        }
        this.mAtm.mH.sendMessageAtFrontOfQueue(PooledLambda.obtainMessage(new WindowProcessController$$ExternalSyntheticLambda9(), this.mListener, Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleUpdateOomAdj() {
        ActivityTaskManagerService.H h = this.mAtm.mH;
        WindowProcessController$$ExternalSyntheticLambda9 windowProcessController$$ExternalSyntheticLambda9 = new WindowProcessController$$ExternalSyntheticLambda9();
        WindowProcessListener windowProcessListener = this.mListener;
        Boolean bool = Boolean.FALSE;
        h.sendMessage(PooledLambda.obtainMessage(windowProcessController$$ExternalSyntheticLambda9, windowProcessListener, bool, bool, Boolean.TRUE));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToPendingTop() {
        this.mAtm.mAmInternal.addPendingTopUid(this.mUid, this.mPid, this.mThread);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateServiceConnectionActivities() {
        this.mAtm.mH.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((WindowProcessListener) obj).updateServiceConnectionActivities();
            }
        }, this.mListener));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPendingUiCleanAndForceProcessStateUpTo(int i) {
        this.mAtm.mH.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda11
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((WindowProcessListener) obj).setPendingUiCleanAndForceProcessStateUpTo(((Integer) obj2).intValue());
            }
        }, this.mListener, Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRemoved() {
        return this.mListener.isRemoved();
    }

    private boolean shouldSetProfileProc() {
        WindowProcessController windowProcessController;
        String str = this.mAtm.mProfileApp;
        return str != null && str.equals(this.mName) && ((windowProcessController = this.mAtm.mProfileProc) == null || windowProcessController == this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProfilerInfo createProfilerInfoIfNeeded() {
        ProfilerInfo profilerInfo = this.mAtm.mProfilerInfo;
        if (profilerInfo == null || profilerInfo.profileFile == null || !shouldSetProfileProc()) {
            return null;
        }
        ParcelFileDescriptor parcelFileDescriptor = profilerInfo.profileFd;
        if (parcelFileDescriptor != null) {
            try {
                profilerInfo.profileFd = parcelFileDescriptor.dup();
            } catch (IOException unused) {
                profilerInfo.closeFd();
            }
        }
        return new ProfilerInfo(profilerInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStartActivity(int i, ActivityInfo activityInfo) {
        String str = ((activityInfo.flags & 1) == 0 || !"android".equals(activityInfo.packageName)) ? activityInfo.packageName : null;
        if (i == 2) {
            this.mAtm.mAmInternal.addPendingTopUid(this.mUid, this.mPid, this.mThread);
        }
        prepareOomAdjustment();
        this.mAtm.mH.sendMessageAtFrontOfQueue(PooledLambda.obtainMessage(new QuintConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda0
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                ((WindowProcessListener) obj).onStartActivity(((Integer) obj2).intValue(), ((Boolean) obj3).booleanValue(), (String) obj4, ((Long) obj5).longValue());
            }
        }, this.mListener, Integer.valueOf(i), Boolean.valueOf(shouldSetProfileProc()), str, Long.valueOf(activityInfo.applicationInfo.longVersionCode)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appDied(String str) {
        this.mAtm.mH.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((WindowProcessListener) obj).appDied((String) obj2);
            }
        }, this.mListener, str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleAppDied() {
        this.mAtm.mTaskSupervisor.removeHistoryRecords(this);
        ArrayList<ActivityRecord> arrayList = this.mInactiveActivities;
        boolean z = false;
        boolean z2 = (arrayList == null || arrayList.isEmpty()) ? false : true;
        ArrayList<ActivityRecord> arrayList2 = (this.mHasActivities || z2) ? new ArrayList<>() : this.mActivities;
        if (this.mHasActivities) {
            arrayList2.addAll(this.mActivities);
        }
        if (z2) {
            arrayList2.addAll(this.mInactiveActivities);
        }
        if (isRemoved() && this.mWindProcessConExt.shouldMakeActivityFinishing(this.mInfo.packageName, this.mUserId)) {
            for (int size = arrayList2.size() - 1; size >= 0; size--) {
                arrayList2.get(size).makeFinishingLocked();
            }
        }
        for (int size2 = arrayList2.size() - 1; size2 >= 0; size2--) {
            ActivityRecord activityRecord = arrayList2.get(size2);
            if (activityRecord.isVisibleRequested() || activityRecord.isVisible()) {
                z = true;
            }
            TaskFragment taskFragment = activityRecord.getTaskFragment();
            if (taskFragment != null) {
                z |= taskFragment.handleAppDied(this);
            }
            activityRecord.handleAppDied();
        }
        clearRecentTasks();
        clearActivities();
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerDisplayAreaConfigurationListener(DisplayArea displayArea) {
        if (displayArea == null || displayArea.containsListener(this)) {
            return;
        }
        unregisterConfigurationListeners();
        this.mDisplayArea = displayArea;
        displayArea.registerConfigurationChangeListener(this);
    }

    @VisibleForTesting
    void unregisterDisplayAreaConfigurationListener() {
        DisplayArea displayArea = this.mDisplayArea;
        if (displayArea == null) {
            return;
        }
        displayArea.unregisterConfigurationChangeListener(this);
        this.mDisplayArea = null;
        onMergedOverrideConfigurationChanged(Configuration.EMPTY);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerActivityConfigurationListener(ActivityRecord activityRecord) {
        if (activityRecord == null || activityRecord.containsListener(this) || !this.mIsActivityConfigOverrideAllowed) {
            return;
        }
        unregisterConfigurationListeners();
        this.mConfigActivityRecord = activityRecord;
        activityRecord.registerConfigurationChangeListener(this);
    }

    private void unregisterActivityConfigurationListener() {
        ActivityRecord activityRecord = this.mConfigActivityRecord;
        if (activityRecord == null) {
            return;
        }
        activityRecord.unregisterConfigurationChangeListener(this);
        this.mConfigActivityRecord = null;
        onMergedOverrideConfigurationChanged(Configuration.EMPTY);
    }

    private void unregisterConfigurationListeners() {
        unregisterActivityConfigurationListener();
        unregisterDisplayAreaConfigurationListener();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        unregisterConfigurationListeners();
    }

    private void updateActivityConfigurationListener() {
        if (this.mIsActivityConfigOverrideAllowed) {
            for (int size = this.mActivities.size() - 1; size >= 0; size--) {
                ActivityRecord activityRecord = this.mActivities.get(size);
                if (!activityRecord.finishing && !activityRecord.getWrapper().getExtImpl().isActivityConfigOverrideDisable(activityRecord, this)) {
                    registerActivityConfigurationListener(activityRecord);
                    return;
                }
            }
            unregisterActivityConfigurationListener();
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(Configuration configuration) {
        boolean z;
        super.onConfigurationChanged(configuration);
        int topActivityDeviceId = getTopActivityDeviceId();
        if (topActivityDeviceId != this.mLastTopActivityDeviceId) {
            this.mLastTopActivityDeviceId = topActivityDeviceId;
            z = true;
        } else {
            z = false;
        }
        Configuration configuration2 = getConfiguration();
        if ((!z) & this.mLastReportedConfiguration.equals(configuration2)) {
            if (Build.IS_DEBUGGABLE && this.mHasImeService) {
                Slog.w(TAG_CONFIGURATION, "Current config: " + configuration2 + " unchanged for IME proc " + this.mName);
                return;
            }
            return;
        }
        if (this.mPauseConfigurationDispatchCount > 0) {
            this.mHasPendingConfigurationChange = true;
        } else {
            dispatchConfiguration(configuration2);
        }
    }

    private int getTopActivityDeviceId() {
        DisplayContent displayContent;
        ActivityRecord topNonFinishingActivity = getTopNonFinishingActivity();
        if (topNonFinishingActivity == null || (displayContent = topNonFinishingActivity.mDisplayContent) == null) {
            return 0;
        }
        return this.mAtm.mTaskSupervisor.getDeviceIdForDisplayId(displayContent.mDisplayId);
    }

    private ActivityRecord getTopNonFinishingActivity() {
        if (this.mActivities.isEmpty()) {
            return null;
        }
        for (int size = this.mActivities.size() - 1; size >= 0; size--) {
            if (!this.mActivities.get(size).finishing) {
                return this.mActivities.get(size);
            }
        }
        return null;
    }

    @Override // com.android.server.wm.ConfigurationContainerListener
    public void onMergedOverrideConfigurationChanged(Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public void resolveOverrideConfiguration(Configuration configuration) {
        Configuration requestedOverrideConfiguration = getRequestedOverrideConfiguration();
        int i = requestedOverrideConfiguration.assetsSeq;
        if (i != 0 && configuration.assetsSeq > i) {
            requestedOverrideConfiguration.assetsSeq = 0;
        }
        super.resolveOverrideConfiguration(configuration);
        Configuration resolvedOverrideConfiguration = getResolvedOverrideConfiguration();
        resolvedOverrideConfiguration.windowConfiguration.setActivityType(0);
        this.mWindProcessConExt.resolveOverrideConfiguration(resolvedOverrideConfiguration, this.mConfigActivityRecord);
        resolvedOverrideConfiguration.seq = configuration.seq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchConfiguration(Configuration configuration) {
        ApplicationInfo applicationInfo;
        this.mHasPendingConfigurationChange = false;
        if (this.mThread == null) {
            if (Build.IS_DEBUGGABLE && this.mHasImeService && !isAgingVersion) {
                Slog.w(TAG_CONFIGURATION, "Unable to send config for IME proc " + this.mName + ": no app thread");
                return;
            }
            return;
        }
        configuration.seq = this.mAtm.increaseConfigurationSeqLocked();
        setLastReportedConfiguration(configuration);
        if (this.mRepProcState >= 16 && (applicationInfo = this.mInfo) != null && !applicationInfo.isSystemApp()) {
            this.mHasCachedConfiguration = true;
            if (this.mRepProcState >= 16) {
                return;
            }
        }
        scheduleConfigurationChange(this.mThread, configuration);
    }

    private void scheduleConfigurationChange(IApplicationThread iApplicationThread, Configuration configuration) {
        if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1049367566, 0, (String) null, new Object[]{String.valueOf(this.mName), String.valueOf(configuration)});
        }
        if (Build.IS_DEBUGGABLE && this.mHasImeService) {
            Slog.v(TAG_CONFIGURATION, "Sending to IME proc " + this.mName + " new config " + configuration);
        }
        this.mHasCachedConfiguration = false;
        try {
            this.mAtm.getLifecycleManager().scheduleTransaction(iApplicationThread, ConfigurationChangeItem.obtain(configuration, this.mLastTopActivityDeviceId));
        } catch (Exception e) {
            Slog.e(TAG_CONFIGURATION, "Failed to schedule configuration change: " + this.mOwner, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastReportedConfiguration(Configuration configuration) {
        synchronized (this.mLastReportedConfiguration) {
            this.mLastReportedConfiguration.setTo(configuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pauseConfigurationDispatch() {
        this.mPauseConfigurationDispatchCount++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resumeConfigurationDispatch() {
        int i = this.mPauseConfigurationDispatchCount;
        if (i == 0) {
            return false;
        }
        this.mPauseConfigurationDispatchCount = i - 1;
        return this.mHasPendingConfigurationChange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAssetConfiguration(int i) {
        if (!this.mHasActivities || !this.mIsActivityConfigOverrideAllowed) {
            Configuration configuration = new Configuration(getRequestedOverrideConfiguration());
            configuration.assetsSeq = i;
            onRequestedOverrideConfigurationChanged(configuration);
            return;
        }
        for (int size = this.mActivities.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord = this.mActivities.get(size);
            Configuration configuration2 = new Configuration(activityRecord.getRequestedOverrideConfiguration());
            configuration2.assetsSeq = i;
            activityRecord.onRequestedOverrideConfigurationChanged(configuration2);
            Slog.w(TAG, "updateAssetConfiguration assetSeq = " + i + "; r = " + activityRecord + "; fullConfiguration = " + activityRecord.getConfiguration() + "; requestOverrideConfig = " + activityRecord.getRequestedOverrideConfiguration());
            if (activityRecord.isVisibleRequested()) {
                activityRecord.ensureActivityConfiguration(0, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Configuration prepareConfigurationForLaunchingActivity() {
        Configuration configuration = getConfiguration();
        if (this.mHasPendingConfigurationChange) {
            this.mHasPendingConfigurationChange = false;
            configuration.seq = this.mAtm.increaseConfigurationSeqLocked();
        }
        this.mHasCachedConfiguration = false;
        return configuration;
    }

    public long getCpuTime() {
        return this.mListener.getCpuTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRecentTask(Task task) {
        this.mRecentTasks.add(task);
        this.mHasRecentTasks = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRecentTask(Task task) {
        this.mRecentTasks.remove(task);
        this.mHasRecentTasks = !this.mRecentTasks.isEmpty();
    }

    public boolean hasRecentTasks() {
        return this.mHasRecentTasks;
    }

    void clearRecentTasks() {
        for (int size = this.mRecentTasks.size() - 1; size >= 0; size--) {
            this.mRecentTasks.get(size).clearRootProcess();
        }
        this.mRecentTasks.clear();
        this.mHasRecentTasks = false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x002f, code lost:
    
        if (r5.mPid != com.android.server.wm.WindowManagerService.MY_PID) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void appEarlyNotResponding(String str, Runnable runnable) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindProcessConExt.hookappEarlyNotRespondingForAging()) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                IActivityController iActivityController = this.mAtm.mController;
                if (iActivityController == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                Runnable runnable2 = null;
                try {
                    if (iActivityController.appEarlyNotResponding(this.mName, this.mPid, str) < 0) {
                    }
                    runnable = null;
                    runnable2 = runnable;
                } catch (RemoteException unused) {
                    this.mAtm.mController = null;
                    Watchdog.getInstance().setActivityController((IActivityController) null);
                    this.mWindProcessConExt.hookappEarlyNotRespondingPrecess(this.mAtm);
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                if (runnable2 != null) {
                    runnable2.run();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0032, code lost:
    
        if (r6.mPid != com.android.server.wm.WindowManagerService.MY_PID) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean appNotResponding(String str, Runnable runnable, Runnable runnable2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindProcessConExt.hookappNotRespondingForAgine()) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                IActivityController iActivityController = this.mAtm.mController;
                if (iActivityController == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                try {
                    int appNotResponding = iActivityController.appNotResponding(this.mName, this.mPid, str);
                    if (appNotResponding != 0) {
                        if (appNotResponding < 0) {
                        }
                        runnable = runnable2;
                    } else {
                        runnable = null;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    if (runnable == null) {
                        return false;
                    }
                    runnable.run();
                    return true;
                } catch (RemoteException unused) {
                    this.mAtm.mController = null;
                    Watchdog.getInstance().setActivityController((IActivityController) null);
                    this.mWindProcessConExt.hookappNotRespondingProcess(this.mAtm);
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void onServiceStarted(ServiceInfo serviceInfo) {
        String str = serviceInfo.permission;
        if (str == null) {
            return;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -769871357:
                if (str.equals("android.permission.BIND_VOICE_INTERACTION")) {
                    c = 0;
                    break;
                }
                break;
            case 1412417858:
                if (str.equals("android.permission.BIND_ACCESSIBILITY_SERVICE")) {
                    c = 1;
                    break;
                }
                break;
            case 1448369304:
                if (str.equals("android.permission.BIND_INPUT_METHOD")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
                break;
            case 2:
                this.mHasImeService = true;
                break;
            default:
                return;
        }
        this.mIsActivityConfigOverrideAllowed = false;
        unregisterActivityConfigurationListener();
    }

    public void onTopProcChanged() {
        if (this.mAtm.mVrController.isInterestingToSchedGroup()) {
            this.mAtm.mH.post(new Runnable() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    WindowProcessController.this.lambda$onTopProcChanged$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTopProcChanged$2() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAtm.mVrController.onTopProcChangedLocked(this);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean isHomeProcess() {
        return this == this.mAtm.mHomeProcess;
    }

    public boolean isPreviousProcess() {
        return this == this.mAtm.mPreviousProcess;
    }

    public boolean isHeavyWeightProcess() {
        return this == this.mAtm.mHeavyWeightProcess;
    }

    public boolean isFactoryTestProcess() {
        ComponentName componentName;
        ActivityTaskManagerService activityTaskManagerService = this.mAtm;
        int i = activityTaskManagerService.mFactoryTest;
        if (i == 0) {
            return false;
        }
        if (i == 1 && (componentName = activityTaskManagerService.mTopComponent) != null && this.mName.equals(componentName.getPackageName())) {
            return true;
        }
        return i == 2 && (this.mInfo.flags & 16) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRunningRecentsAnimation(boolean z) {
        if (this.mRunningRecentsAnimation == z) {
            return;
        }
        this.mRunningRecentsAnimation = z;
        updateRunningRemoteOrRecentsAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRunningRemoteAnimation(boolean z) {
        if (this.mRunningRemoteAnimation == z) {
            return;
        }
        this.mRunningRemoteAnimation = z;
        updateRunningRemoteOrRecentsAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateRunningRemoteOrRecentsAnimation() {
        this.mAtm.mH.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((WindowProcessListener) obj).setRunningRemoteAnimation(((Boolean) obj2).booleanValue());
            }
        }, this.mListener, Boolean.valueOf(isRunningRemoteTransition())));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRunningRemoteTransition() {
        return this.mRunningRecentsAnimation || this.mRunningRemoteAnimation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRunningAnimationUnsafe() {
        this.mListener.setRunningRemoteAnimation(true);
    }

    public String toString() {
        Object obj = this.mOwner;
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    public void dump(PrintWriter printWriter, String str) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mActivities.size() > 0) {
                    printWriter.print(str);
                    printWriter.println("Activities:");
                    for (int i = 0; i < this.mActivities.size(); i++) {
                        printWriter.print(str);
                        printWriter.print("  - ");
                        printWriter.println(this.mActivities.get(i));
                    }
                }
                ArrayMap<ActivityRecord, int[]> arrayMap = this.mRemoteActivities;
                if (arrayMap != null && !arrayMap.isEmpty()) {
                    printWriter.print(str);
                    printWriter.println("Remote Activities:");
                    for (int size = this.mRemoteActivities.size() - 1; size >= 0; size--) {
                        printWriter.print(str);
                        printWriter.print("  - ");
                        printWriter.print(this.mRemoteActivities.keyAt(size));
                        printWriter.print(" flags=");
                        int i2 = this.mRemoteActivities.valueAt(size)[0];
                        if ((i2 & 1) != 0) {
                            printWriter.print("host ");
                        }
                        if ((i2 & 2) != 0) {
                            printWriter.print("embedded");
                        }
                        printWriter.println();
                    }
                }
                if (this.mRecentTasks.size() > 0) {
                    printWriter.println(str + "Recent Tasks:");
                    for (int i3 = 0; i3 < this.mRecentTasks.size(); i3++) {
                        printWriter.println(str + "  - " + this.mRecentTasks.get(i3));
                    }
                }
                if (this.mVrThreadTid != 0) {
                    printWriter.print(str);
                    printWriter.print("mVrThreadTid=");
                    printWriter.println(this.mVrThreadTid);
                }
                this.mBgLaunchController.dump(printWriter, str);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        printWriter.println(str + " Configuration=" + getConfiguration());
        printWriter.println(str + " OverrideConfiguration=" + getRequestedOverrideConfiguration());
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" mLastReportedConfiguration=");
        sb.append(this.mHasCachedConfiguration ? "(cached) " + this.mLastReportedConfiguration : this.mLastReportedConfiguration);
        printWriter.println(sb.toString());
        int i4 = this.mActivityStateFlags;
        if (i4 != ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER) {
            printWriter.print(str + " mActivityStateFlags=");
            if ((ACTIVITY_STATE_FLAG_IS_WINDOW_VISIBLE & i4) != 0) {
                printWriter.print("W|");
            }
            if ((ACTIVITY_STATE_FLAG_IS_VISIBLE & i4) != 0) {
                printWriter.print("V|");
                if ((ACTIVITY_STATE_FLAG_HAS_RESUMED & i4) != 0) {
                    printWriter.print("R|");
                }
            } else if ((ACTIVITY_STATE_FLAG_IS_PAUSING_OR_PAUSED & i4) != 0) {
                printWriter.print("P|");
            } else if ((ACTIVITY_STATE_FLAG_IS_STOPPING & i4) != 0) {
                printWriter.print("S|");
                if ((ACTIVITY_STATE_FLAG_IS_STOPPING_FINISHING & i4) != 0) {
                    printWriter.print("F|");
                }
            }
            if ((ACTIVITY_STATE_FLAG_HAS_ACTIVITY_IN_VISIBLE_TASK & i4) != 0) {
                printWriter.print("VT|");
            }
            int i5 = i4 & ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER;
            if (i5 != ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER) {
                printWriter.print("taskLayer=" + i5);
            }
            printWriter.println();
        }
        printWriter.println(str + " mWaitActivityToAttach=" + this.mWindProcessConExt.waitActivityToAttach());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        this.mListener.dumpDebug(protoOutputStream, j);
    }

    public IWindowProcessControllerWrapper getWrapper() {
        return this.mWindowProcessControllerWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class WindowProcessControllerWrapper implements IWindowProcessControllerWrapper {
        private WindowProcessControllerWrapper() {
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public IWindowProcessControllerExt getExtImpl() {
            return WindowProcessController.this.mWindProcessConExt;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public ArrayList<String> getPkgList() {
            return WindowProcessController.this.mPkgList;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public ArrayList<ActivityRecord> getActivities() {
            return WindowProcessController.this.mActivities;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public ActivityTaskManagerService getAtm() {
            return WindowProcessController.this.mAtm;
        }
    }
}
