package com.android.server.am;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.os.SystemClock;
import android.util.Slog;
import android.util.TimeUtils;
import com.android.internal.annotations.CompositeRWLock;
import com.android.internal.annotations.GuardedBy;
import com.android.server.am.OomAdjuster;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProcessStateRecord {
    private static final boolean TRACE_OOM_ADJ = false;
    private static final int VALUE_FALSE = 0;
    private static final int VALUE_INVALID = -1;
    private static final int VALUE_TRUE = 1;

    @GuardedBy({"mService"})
    private int mAdjSeq;

    @CompositeRWLock({"mService", "mProcLock"})
    private Object mAdjSource;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mAdjSourceProcState;

    @CompositeRWLock({"mService", "mProcLock"})
    private Object mAdjTarget;

    @GuardedBy({"mService"})
    private String mAdjType;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mAdjTypeCode;
    private final ProcessRecord mApp;

    @GuardedBy({"mService"})
    private long mCacheOomRankerRss;

    @GuardedBy({"mService"})
    private long mCacheOomRankerRssTimeMs;

    @GuardedBy({"mService"})
    private int mCacheOomRankerUseCount;

    @GuardedBy({"mService"})
    private boolean mCached;

    @GuardedBy({"mService"})
    private int mCompletedAdjSeq;

    @GuardedBy({"mService"})
    private boolean mContainsCycle;

    @GuardedBy({"mService"})
    private boolean mEmpty;

    @CompositeRWLock({"mService", "mProcLock"})
    private long mFgInteractionTime;

    @GuardedBy({"mService"})
    private Object mForcingToImportant;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mHasForegroundActivities;

    @GuardedBy({"mService"})
    private boolean mHasOverlayUi;

    @GuardedBy({"mService"})
    private boolean mHasShownUi;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mHasStartedServices;

    @GuardedBy({"mService"})
    private boolean mHasTopUi;

    @CompositeRWLock({"mService", "mProcLock"})
    private long mInteractionEventTime;

    @GuardedBy({"mService"})
    private long mLastCanKillOnBgRestrictedAndIdleTime;

    @GuardedBy({"mService"})
    private long mLastInvisibleTime;

    @CompositeRWLock({"mService", "mProcLock"})
    private long mLastStateTime;

    @GuardedBy({"mService"})
    private long mLastTopTime;

    @GuardedBy({"mService"})
    private boolean mNoKillOnBgRestrictedAndIdle;

    @GuardedBy({"mProcLock"})
    private boolean mNotCachedSinceIdle;
    private final ActivityManagerGlobalLock mProcLock;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mProcStateChanged;

    @GuardedBy({"mService"})
    private boolean mReachable;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mRepForegroundActivities;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mReportedInteraction;

    @GuardedBy({"mService"})
    private boolean mRunningRemoteAnimation;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mSavedPriority;
    private final ActivityManagerService mService;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mServiceB;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mServiceHighRam;

    @GuardedBy({"mService"})
    private boolean mSetCached;

    @GuardedBy({"mService"})
    private boolean mSetNoKillOnBgRestrictedAndIdle;

    @GuardedBy({"mService"})
    private boolean mSystemNoUi;

    @CompositeRWLock({"mService", "mProcLock"})
    private long mWhenUnimportant;

    @GuardedBy({"mService"})
    private int mMaxAdj = 1001;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mCurRawAdj = -10000;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mSetRawAdj = -10000;

    @CompositeRWLock({"mService", "mProcLock"})
    public int mCurAdj = -10000;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mSetAdj = -10000;

    @GuardedBy({"mService"})
    private int mVerifiedAdj = -10000;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mCurCapability = 0;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mSetCapability = 0;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mCurSchedGroup = 0;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mSetSchedGroup = 0;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mCurProcState = 20;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mRepProcState = 20;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mCurRawProcState = 20;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mSetProcState = 20;

    @GuardedBy({"mService"})
    private boolean mBackgroundRestricted = false;

    @GuardedBy({"mService"})
    private boolean mCurBoundByNonBgRestrictedApp = false;
    private boolean mSetBoundByNonBgRestrictedApp = false;

    @GuardedBy({"mService"})
    private int mCachedHasActivities = -1;

    @GuardedBy({"mService"})
    private int mCachedIsHeavyWeight = -1;

    @GuardedBy({"mService"})
    private int mCachedHasVisibleActivities = -1;

    @GuardedBy({"mService"})
    private int mCachedIsHomeProcess = -1;

    @GuardedBy({"mService"})
    private int mCachedIsPreviousProcess = -1;

    @GuardedBy({"mService"})
    private int mCachedHasRecentTasks = -1;

    @GuardedBy({"mService"})
    private int mCachedIsReceivingBroadcast = -1;

    @GuardedBy({"mService"})
    private int[] mCachedCompatChanges = {-1, -1, -1};

    @GuardedBy({"mService"})
    private int mCachedAdj = -10000;

    @GuardedBy({"mService"})
    private boolean mCachedForegroundActivities = false;

    @GuardedBy({"mService"})
    private int mCachedProcState = 19;

    @GuardedBy({"mService"})
    private int mCachedSchedGroup = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessStateRecord(ProcessRecord processRecord) {
        this.mApp = processRecord;
        ActivityManagerService activityManagerService = processRecord.mService;
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(long j) {
        this.mLastStateTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setMaxAdj(int i) {
        this.mMaxAdj = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getMaxAdj() {
        return this.mMaxAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCurRawAdj(int i) {
        this.mCurRawAdj = i;
        this.mApp.getWindowProcessController().setPerceptible(i <= 200);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getCurRawAdj() {
        return this.mCurRawAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setSetRawAdj(int i) {
        this.mSetRawAdj = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSetRawAdj() {
        return this.mSetRawAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCurAdj(int i) {
        this.mCurAdj = i;
        this.mApp.getWindowProcessController().setCurrentAdj(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getCurAdj() {
        return this.mCurAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setSetAdj(int i) {
        this.mSetAdj = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSetAdj() {
        return this.mSetAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSetAdjWithServices() {
        int i = this.mSetAdj;
        return (i < 900 || !this.mHasStartedServices) ? i : ProcessList.SERVICE_B_ADJ;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setVerifiedAdj(int i) {
        this.mVerifiedAdj = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getVerifiedAdj() {
        return this.mVerifiedAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCurCapability(int i) {
        this.mCurCapability = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getCurCapability() {
        return this.mCurCapability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setSetCapability(int i) {
        this.mSetCapability = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSetCapability() {
        return this.mSetCapability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCurrentSchedulingGroup(int i) {
        this.mCurSchedGroup = i;
        this.mApp.getWindowProcessController().setCurrentSchedulingGroup(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getCurrentSchedulingGroup() {
        return this.mCurSchedGroup;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setSetSchedGroup(int i) {
        this.mSetSchedGroup = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSetSchedGroup() {
        return this.mSetSchedGroup;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCurProcState(int i) {
        this.mCurProcState = i;
        this.mApp.getWindowProcessController().setCurrentProcState(this.mCurProcState);
        this.mApp.getWrapper().getExtImpl().updateProcessState(this.mApp, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getCurProcState() {
        return this.mCurProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCurRawProcState(int i) {
        this.mCurRawProcState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getCurRawProcState() {
        return this.mCurRawProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setReportedProcState(int i) {
        this.mRepProcState = i;
        this.mApp.getWindowProcessController().setReportedProcState(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getReportedProcState() {
        return this.mRepProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void forceProcessStateUpTo(int i) {
        if (this.mRepProcState > i) {
            ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    setReportedProcState(i);
                    setCurProcState(i);
                    setCurRawProcState(i);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setSetProcState(int i) {
        if (ActivityManager.isProcStateCached(this.mSetProcState) && !ActivityManager.isProcStateCached(i)) {
            this.mCacheOomRankerUseCount++;
        }
        this.mSetProcState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSetProcState() {
        return this.mSetProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setLastStateTime(long j) {
        this.mLastStateTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public long getLastStateTime() {
        return this.mLastStateTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setSavedPriority(int i) {
        this.mSavedPriority = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSavedPriority() {
        return this.mSavedPriority;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setServiceB(boolean z) {
        this.mServiceB = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean isServiceB() {
        return this.mServiceB;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setServiceHighRam(boolean z) {
        this.mServiceHighRam = z;
    }

    @GuardedBy(anyOf = {"mService", "mProcLock"})
    boolean isServiceHighRam() {
        return this.mServiceHighRam;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setNotCachedSinceIdle(boolean z) {
        this.mNotCachedSinceIdle = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean isNotCachedSinceIdle() {
        return this.mNotCachedSinceIdle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setHasStartedServices(boolean z) {
        this.mHasStartedServices = z;
        if (z) {
            this.mApp.mProfile.addHostingComponentType(128);
        } else {
            this.mApp.mProfile.clearHostingComponentType(128);
        }
    }

    @GuardedBy({"mProcLock"})
    boolean hasStartedServices() {
        return this.mHasStartedServices;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setHasForegroundActivities(boolean z) {
        this.mHasForegroundActivities = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean hasForegroundActivities() {
        return this.mHasForegroundActivities;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setRepForegroundActivities(boolean z) {
        this.mRepForegroundActivities = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean hasRepForegroundActivities() {
        return this.mRepForegroundActivities;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setHasShownUi(boolean z) {
        this.mHasShownUi = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean hasShownUi() {
        return this.mHasShownUi;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setHasTopUi(boolean z) {
        this.mHasTopUi = z;
        this.mApp.getWindowProcessController().setHasTopUi(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean hasTopUi() {
        return this.mHasTopUi;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setHasOverlayUi(boolean z) {
        this.mHasOverlayUi = z;
        this.mApp.getWindowProcessController().setHasOverlayUi(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean hasOverlayUi() {
        return this.mHasOverlayUi;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isRunningRemoteAnimation() {
        return this.mRunningRemoteAnimation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setRunningRemoteAnimation(boolean z) {
        if (this.mRunningRemoteAnimation == z) {
            return;
        }
        this.mRunningRemoteAnimation = z;
        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            Slog.i(IActivityManagerServiceExt.TAG, "Setting runningRemoteAnimation=" + z + " for pid=" + this.mApp.getPid());
        }
        this.mService.updateOomAdjLocked(this.mApp, 9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setProcStateChanged(boolean z) {
        this.mProcStateChanged = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean hasProcStateChanged() {
        return this.mProcStateChanged;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setReportedInteraction(boolean z) {
        this.mReportedInteraction = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean hasReportedInteraction() {
        return this.mReportedInteraction;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setInteractionEventTime(long j) {
        this.mInteractionEventTime = j;
        this.mApp.getWindowProcessController().setInteractionEventTime(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public long getInteractionEventTime() {
        return this.mInteractionEventTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setFgInteractionTime(long j) {
        this.mFgInteractionTime = j;
        this.mApp.getWindowProcessController().setFgInteractionTime(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public long getFgInteractionTime() {
        return this.mFgInteractionTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setForcingToImportant(Object obj) {
        this.mForcingToImportant = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public Object getForcingToImportant() {
        return this.mForcingToImportant;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setAdjSeq(int i) {
        this.mAdjSeq = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void decAdjSeq() {
        this.mAdjSeq--;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getAdjSeq() {
        return this.mAdjSeq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setCompletedAdjSeq(int i) {
        this.mCompletedAdjSeq = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void decCompletedAdjSeq() {
        this.mCompletedAdjSeq--;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getCompletedAdjSeq() {
        return this.mCompletedAdjSeq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setContainsCycle(boolean z) {
        this.mContainsCycle = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean containsCycle() {
        return this.mContainsCycle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setWhenUnimportant(long j) {
        this.mWhenUnimportant = j;
        this.mApp.getWindowProcessController().setWhenUnimportant(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public long getWhenUnimportant() {
        return this.mWhenUnimportant;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setLastTopTime(long j) {
        this.mLastTopTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public long getLastTopTime() {
        return this.mLastTopTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setEmpty(boolean z) {
        this.mEmpty = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isEmpty() {
        return this.mEmpty;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setCached(boolean z) {
        this.mCached = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isCached() {
        return this.mCached;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getCacheOomRankerUseCount() {
        return this.mCacheOomRankerUseCount;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setSystemNoUi(boolean z) {
        this.mSystemNoUi = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isSystemNoUi() {
        return this.mSystemNoUi;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setAdjType(String str) {
        this.mAdjType = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public String getAdjType() {
        return this.mAdjType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setAdjTypeCode(int i) {
        this.mAdjTypeCode = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getAdjTypeCode() {
        return this.mAdjTypeCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setAdjSource(Object obj) {
        this.mAdjSource = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public Object getAdjSource() {
        return this.mAdjSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setAdjSourceProcState(int i) {
        this.mAdjSourceProcState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getAdjSourceProcState() {
        return this.mAdjSourceProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setAdjTarget(Object obj) {
        this.mAdjTarget = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public Object getAdjTarget() {
        return this.mAdjTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isReachable() {
        return this.mReachable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setReachable(boolean z) {
        this.mReachable = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void resetCachedInfo() {
        this.mCachedHasActivities = -1;
        this.mCachedIsHeavyWeight = -1;
        this.mCachedHasVisibleActivities = -1;
        this.mCachedIsHomeProcess = -1;
        this.mCachedIsPreviousProcess = -1;
        this.mCachedHasRecentTasks = -1;
        this.mCachedIsReceivingBroadcast = -1;
        this.mCachedAdj = -10000;
        this.mCachedForegroundActivities = false;
        this.mCachedProcState = 19;
        this.mCachedSchedGroup = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedHasActivities() {
        if (this.mCachedHasActivities == -1) {
            boolean hasActivities = this.mApp.getWindowProcessController().hasActivities();
            this.mCachedHasActivities = hasActivities ? 1 : 0;
            if (hasActivities) {
                this.mApp.mProfile.addHostingComponentType(16);
            } else {
                this.mApp.mProfile.clearHostingComponentType(16);
            }
        }
        return this.mCachedHasActivities == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedIsHeavyWeight() {
        if (this.mCachedIsHeavyWeight == -1) {
            this.mCachedIsHeavyWeight = this.mApp.getWindowProcessController().isHeavyWeightProcess() ? 1 : 0;
        }
        return this.mCachedIsHeavyWeight == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedHasVisibleActivities() {
        if (this.mCachedHasVisibleActivities == -1) {
            this.mCachedHasVisibleActivities = this.mApp.getWindowProcessController().hasVisibleActivities() ? 1 : 0;
        }
        return this.mCachedHasVisibleActivities == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedIsHomeProcess() {
        if (this.mCachedIsHomeProcess == -1) {
            if (this.mApp.getWindowProcessController().isHomeProcess()) {
                this.mCachedIsHomeProcess = 1;
                this.mService.mAppProfiler.mHasHomeProcess = true;
            } else {
                this.mCachedIsHomeProcess = 0;
            }
        }
        return this.mCachedIsHomeProcess == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedIsPreviousProcess() {
        if (this.mCachedIsPreviousProcess == -1) {
            if (this.mApp.getWindowProcessController().isPreviousProcess()) {
                this.mCachedIsPreviousProcess = 1;
                this.mService.mAppProfiler.mHasPreviousProcess = true;
            } else {
                this.mCachedIsPreviousProcess = 0;
            }
        }
        return this.mCachedIsPreviousProcess == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedHasRecentTasks() {
        if (this.mCachedHasRecentTasks == -1) {
            this.mCachedHasRecentTasks = this.mApp.getWindowProcessController().hasRecentTasks() ? 1 : 0;
        }
        return this.mCachedHasRecentTasks == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedIsReceivingBroadcast(int[] iArr) {
        if (this.mCachedIsReceivingBroadcast == -1) {
            boolean isReceivingBroadcastLocked = this.mService.isReceivingBroadcastLocked(this.mApp, iArr);
            this.mCachedIsReceivingBroadcast = isReceivingBroadcastLocked ? 1 : 0;
            if (isReceivingBroadcastLocked) {
                this.mCachedSchedGroup = iArr[0];
                this.mApp.mProfile.addHostingComponentType(32);
            } else {
                this.mApp.mProfile.clearHostingComponentType(32);
            }
        }
        return this.mCachedIsReceivingBroadcast == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedCompatChange(int i) {
        int[] iArr = this.mCachedCompatChanges;
        if (iArr[i] == -1) {
            iArr[i] = this.mService.mOomAdjuster.isChangeEnabled(i, this.mApp.info, false) ? 1 : 0;
        }
        return this.mCachedCompatChanges[i] == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void computeOomAdjFromActivitiesIfNecessary(OomAdjuster.ComputeOomAdjWindowCallback computeOomAdjWindowCallback, int i, boolean z, boolean z2, int i2, int i3, int i4, int i5, int i6) {
        if (this.mCachedAdj != -10000) {
            return;
        }
        computeOomAdjWindowCallback.initialize(this.mApp, i, z, z2, i2, i3, i4, i5, i6);
        int min = Math.min(99, this.mApp.getWindowProcessController().computeOomAdjFromActivities(computeOomAdjWindowCallback));
        int i7 = computeOomAdjWindowCallback.adj;
        this.mCachedAdj = i7;
        this.mCachedForegroundActivities = computeOomAdjWindowCallback.foregroundActivities;
        this.mCachedHasVisibleActivities = computeOomAdjWindowCallback.mHasVisibleActivities ? 1 : 0;
        this.mCachedProcState = computeOomAdjWindowCallback.procState;
        this.mCachedSchedGroup = computeOomAdjWindowCallback.schedGroup;
        if (i7 == 100) {
            this.mCachedAdj = i7 + min;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getCachedAdj() {
        return this.mCachedAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean getCachedForegroundActivities() {
        return this.mCachedForegroundActivities;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getCachedProcState() {
        return this.mCachedProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getCachedSchedGroup() {
        return this.mCachedSchedGroup;
    }

    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public String makeAdjReason() {
        if (this.mAdjSource == null && this.mAdjTarget == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append(' ');
        Object obj = this.mAdjTarget;
        if (obj instanceof ComponentName) {
            sb.append(((ComponentName) obj).flattenToShortString());
        } else if (obj != null) {
            sb.append(obj.toString());
        } else {
            sb.append("{null}");
        }
        sb.append("<=");
        Object obj2 = this.mAdjSource;
        if (obj2 instanceof ProcessRecord) {
            sb.append("Proc{");
            sb.append(((ProcessRecord) this.mAdjSource).toShortString());
            sb.append("}");
        } else if (obj2 != null) {
            sb.append(obj2.toString());
        } else {
            sb.append("{null}");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void onCleanupApplicationRecordLSP() {
        int i = 0;
        setHasForegroundActivities(false);
        this.mHasShownUi = false;
        this.mForcingToImportant = null;
        this.mVerifiedAdj = -10000;
        this.mSetAdj = -10000;
        this.mCurAdj = -10000;
        this.mSetRawAdj = -10000;
        this.mCurRawAdj = -10000;
        this.mSetCapability = 0;
        this.mCurCapability = 0;
        this.mSetSchedGroup = 0;
        this.mCurSchedGroup = 0;
        this.mSetProcState = 20;
        this.mCurRawProcState = 20;
        this.mCurProcState = 20;
        while (true) {
            int[] iArr = this.mCachedCompatChanges;
            if (i >= iArr.length) {
                return;
            }
            iArr[i] = -1;
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isBackgroundRestricted() {
        return this.mBackgroundRestricted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setBackgroundRestricted(boolean z) {
        this.mBackgroundRestricted = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isCurBoundByNonBgRestrictedApp() {
        return this.mCurBoundByNonBgRestrictedApp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setCurBoundByNonBgRestrictedApp(boolean z) {
        this.mCurBoundByNonBgRestrictedApp = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isSetBoundByNonBgRestrictedApp() {
        return this.mSetBoundByNonBgRestrictedApp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setSetBoundByNonBgRestrictedApp(boolean z) {
        this.mSetBoundByNonBgRestrictedApp = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void updateLastInvisibleTime(boolean z) {
        if (z) {
            this.mLastInvisibleTime = Long.MAX_VALUE;
        } else if (this.mLastInvisibleTime == Long.MAX_VALUE) {
            this.mLastInvisibleTime = SystemClock.elapsedRealtime();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public long getLastInvisibleTime() {
        return this.mLastInvisibleTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setNoKillOnBgRestrictedAndIdle(boolean z) {
        this.mNoKillOnBgRestrictedAndIdle = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean shouldNotKillOnBgRestrictedAndIdle() {
        return this.mNoKillOnBgRestrictedAndIdle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setSetCached(boolean z) {
        this.mSetCached = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isSetCached() {
        return this.mSetCached;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setSetNoKillOnBgRestrictedAndIdle(boolean z) {
        this.mSetNoKillOnBgRestrictedAndIdle = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isSetNoKillOnBgRestrictedAndIdle() {
        return this.mSetNoKillOnBgRestrictedAndIdle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setLastCanKillOnBgRestrictedAndIdleTime(long j) {
        this.mLastCanKillOnBgRestrictedAndIdleTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public long getLastCanKillOnBgRestrictedAndIdleTime() {
        return this.mLastCanKillOnBgRestrictedAndIdleTime;
    }

    public void setCacheOomRankerRss(long j, long j2) {
        this.mCacheOomRankerRss = j;
        this.mCacheOomRankerRssTimeMs = j2;
    }

    @GuardedBy({"mService"})
    public long getCacheOomRankerRss() {
        return this.mCacheOomRankerRss;
    }

    @GuardedBy({"mService"})
    public long getCacheOomRankerRssTimeMs() {
        return this.mCacheOomRankerRssTimeMs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void dump(PrintWriter printWriter, String str, long j) {
        if (this.mReportedInteraction || this.mFgInteractionTime != 0) {
            printWriter.print(str);
            printWriter.print("reportedInteraction=");
            printWriter.print(this.mReportedInteraction);
            if (this.mInteractionEventTime != 0) {
                printWriter.print(" time=");
                TimeUtils.formatDuration(this.mInteractionEventTime, SystemClock.elapsedRealtime(), printWriter);
            }
            if (this.mFgInteractionTime != 0) {
                printWriter.print(" fgInteractionTime=");
                TimeUtils.formatDuration(this.mFgInteractionTime, SystemClock.elapsedRealtime(), printWriter);
            }
            printWriter.println();
        }
        printWriter.print(str);
        printWriter.print("adjSeq=");
        printWriter.print(this.mAdjSeq);
        printWriter.print(" lruSeq=");
        printWriter.println(this.mApp.getLruSeq());
        printWriter.print(str);
        printWriter.print("oom adj: max=");
        printWriter.print(this.mMaxAdj);
        printWriter.print(" curRaw=");
        printWriter.print(this.mCurRawAdj);
        printWriter.print(" setRaw=");
        printWriter.print(this.mSetRawAdj);
        printWriter.print(" cur=");
        printWriter.print(this.mCurAdj);
        printWriter.print(" set=");
        printWriter.println(this.mSetAdj);
        printWriter.print(str);
        printWriter.print("mCurSchedGroup=");
        printWriter.print(this.mCurSchedGroup);
        printWriter.print(" setSchedGroup=");
        printWriter.print(this.mSetSchedGroup);
        printWriter.print(" systemNoUi=");
        printWriter.println(this.mSystemNoUi);
        printWriter.print(str);
        printWriter.print("curProcState=");
        printWriter.print(getCurProcState());
        printWriter.print(" mRepProcState=");
        printWriter.print(this.mRepProcState);
        printWriter.print(" setProcState=");
        printWriter.print(this.mSetProcState);
        printWriter.print(" lastStateTime=");
        TimeUtils.formatDuration(getLastStateTime(), j, printWriter);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("curCapability=");
        ActivityManager.printCapabilitiesFull(printWriter, this.mCurCapability);
        printWriter.print(" setCapability=");
        ActivityManager.printCapabilitiesFull(printWriter, this.mSetCapability);
        printWriter.println();
        if (this.mBackgroundRestricted) {
            printWriter.print(" backgroundRestricted=");
            printWriter.print(this.mBackgroundRestricted);
            printWriter.print(" boundByNonBgRestrictedApp=");
            printWriter.print(this.mSetBoundByNonBgRestrictedApp);
        }
        printWriter.println();
        if (this.mHasShownUi || this.mApp.mProfile.hasPendingUiClean()) {
            printWriter.print(str);
            printWriter.print("hasShownUi=");
            printWriter.print(this.mHasShownUi);
            printWriter.print(" pendingUiClean=");
            printWriter.println(this.mApp.mProfile.hasPendingUiClean());
        }
        printWriter.print(str);
        printWriter.print("cached=");
        printWriter.print(this.mCached);
        printWriter.print(" empty=");
        printWriter.println(this.mEmpty);
        if (this.mServiceB) {
            printWriter.print(str);
            printWriter.print("serviceb=");
            printWriter.print(this.mServiceB);
            printWriter.print(" serviceHighRam=");
            printWriter.println(this.mServiceHighRam);
        }
        if (this.mNotCachedSinceIdle) {
            printWriter.print(str);
            printWriter.print("notCachedSinceIdle=");
            printWriter.print(this.mNotCachedSinceIdle);
            printWriter.print(" initialIdlePss=");
            printWriter.println(this.mApp.mProfile.getInitialIdlePss());
        }
        if (hasTopUi() || hasOverlayUi() || this.mRunningRemoteAnimation) {
            printWriter.print(str);
            printWriter.print("hasTopUi=");
            printWriter.print(hasTopUi());
            printWriter.print(" hasOverlayUi=");
            printWriter.print(hasOverlayUi());
            printWriter.print(" runningRemoteAnimation=");
            printWriter.println(this.mRunningRemoteAnimation);
        }
        if (this.mHasForegroundActivities || this.mRepForegroundActivities) {
            printWriter.print(str);
            printWriter.print("foregroundActivities=");
            printWriter.print(this.mHasForegroundActivities);
            printWriter.print(" (rep=");
            printWriter.print(this.mRepForegroundActivities);
            printWriter.println(")");
        }
        if (this.mSetProcState > 10) {
            printWriter.print(str);
            printWriter.print("whenUnimportant=");
            TimeUtils.formatDuration(this.mWhenUnimportant - j, printWriter);
            printWriter.println();
        }
        if (this.mLastTopTime > 0) {
            printWriter.print(str);
            printWriter.print("lastTopTime=");
            TimeUtils.formatDuration(this.mLastTopTime, j, printWriter);
            printWriter.println();
        }
        long j2 = this.mLastInvisibleTime;
        if (j2 > 0 && j2 < Long.MAX_VALUE) {
            printWriter.print(str);
            printWriter.print("lastInvisibleTime=");
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long currentTimeMillis = System.currentTimeMillis();
            TimeUtils.dumpTimeWithDelta(printWriter, (currentTimeMillis - elapsedRealtime) + this.mLastInvisibleTime, currentTimeMillis);
            printWriter.println();
        }
        if (this.mHasStartedServices) {
            printWriter.print(str);
            printWriter.print("hasStartedServices=");
            printWriter.println(this.mHasStartedServices);
        }
    }
}
