package com.android.server.wm;

import android.util.Slog;
import com.android.server.wm.ActivityRecord;
import java.util.ArrayList;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class EnsureActivitiesVisibleHelper {
    private boolean mAboveTop;
    private boolean mBehindFullyOccludedContainer;
    private int mConfigChanges;
    private boolean mContainerShouldBeVisible;
    IEnsureActivitiesVisibleHelperExt mHelperExt = (IEnsureActivitiesVisibleHelperExt) ExtLoader.type(IEnsureActivitiesVisibleHelperExt.class).base(this).create();
    private boolean mNotifyClients;
    private boolean mPreserveWindows;
    private ActivityRecord mStarting;
    private final TaskFragment mTaskFragment;
    private ActivityRecord mTopRunningActivity;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EnsureActivitiesVisibleHelper(TaskFragment taskFragment) {
        this.mTaskFragment = taskFragment;
    }

    void reset(ActivityRecord activityRecord, int i, boolean z, boolean z2) {
        this.mStarting = activityRecord;
        ActivityRecord activityRecord2 = this.mTaskFragment.topRunningActivity();
        this.mTopRunningActivity = activityRecord2;
        this.mAboveTop = activityRecord2 != null;
        boolean shouldBeVisible = this.mTaskFragment.shouldBeVisible(this.mStarting);
        this.mContainerShouldBeVisible = shouldBeVisible;
        this.mBehindFullyOccludedContainer = !shouldBeVisible;
        this.mConfigChanges = i;
        this.mPreserveWindows = z;
        this.mNotifyClients = z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void process(ActivityRecord activityRecord, int i, boolean z, boolean z2) {
        reset(activityRecord, i, z, z2);
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(Task.TAG_VISIBILITY, "ensureActivitiesVisible behind " + this.mTopRunningActivity + " configChanges=0x" + Integer.toHexString(i));
        }
        if (this.mTopRunningActivity != null && this.mTaskFragment.asTask() != null) {
            this.mTaskFragment.asTask().checkTranslucentActivityWaiting(this.mTopRunningActivity);
        }
        ActivityRecord activityRecord2 = this.mTopRunningActivity;
        boolean z3 = activityRecord2 != null && !activityRecord2.mLaunchTaskBehind && this.mTaskFragment.canBeResumed(activityRecord) && (activityRecord == null || !activityRecord.isDescendantOf(this.mTaskFragment));
        ArrayList arrayList = null;
        for (int size = this.mTaskFragment.mChildren.size() - 1; size >= 0; size--) {
            if (size >= this.mTaskFragment.mChildren.size()) {
                Slog.e(Task.TAG_VISIBILITY, "mTaskFragment.mChildren exception break");
                return;
            }
            WindowContainer windowContainer = (WindowContainer) this.mTaskFragment.mChildren.get(size);
            TaskFragment asTaskFragment = windowContainer.asTaskFragment();
            if (asTaskFragment != null && asTaskFragment.getTopNonFinishingActivity() != null) {
                asTaskFragment.updateActivityVisibilities(activityRecord, i, z, z2);
                this.mBehindFullyOccludedContainer |= asTaskFragment.getBounds().equals(this.mTaskFragment.getBounds()) && !asTaskFragment.isTranslucent(activityRecord);
                if (this.mAboveTop && this.mTopRunningActivity.getTaskFragment() == asTaskFragment) {
                    this.mAboveTop = false;
                }
                if (!this.mBehindFullyOccludedContainer) {
                    if (arrayList != null && arrayList.contains(asTaskFragment)) {
                        if (!asTaskFragment.isTranslucent(activityRecord) && !asTaskFragment.getAdjacentTaskFragment().isTranslucent(activityRecord)) {
                            this.mBehindFullyOccludedContainer = true;
                        }
                    } else {
                        TaskFragment adjacentTaskFragment = asTaskFragment.getAdjacentTaskFragment();
                        if (adjacentTaskFragment != null) {
                            if (arrayList == null) {
                                arrayList = new ArrayList();
                            }
                            arrayList.add(adjacentTaskFragment);
                        }
                    }
                }
            } else if (windowContainer.asActivityRecord() != null) {
                setActivityVisibilityState(windowContainer.asActivityRecord(), activityRecord, z3);
            }
        }
    }

    private void setActivityVisibilityState(ActivityRecord activityRecord, ActivityRecord activityRecord2, boolean z) {
        boolean z2 = false;
        boolean z3 = activityRecord == this.mTopRunningActivity;
        if (this.mAboveTop && !z3) {
            activityRecord.makeInvisible();
            return;
        }
        this.mAboveTop = false;
        activityRecord.updateVisibilityIgnoringKeyguard(this.mBehindFullyOccludedContainer);
        boolean shouldBeVisibleUnchecked = activityRecord.shouldBeVisibleUnchecked();
        if (activityRecord.visibleIgnoringKeyguard) {
            if (activityRecord.occludesParent()) {
                if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                    Slog.v(Task.TAG_VISIBILITY, "Fullscreen: at " + activityRecord + " containerVisible=" + this.mContainerShouldBeVisible + " behindFullyOccluded=" + this.mBehindFullyOccludedContainer);
                }
                this.mBehindFullyOccludedContainer = true;
            } else {
                this.mBehindFullyOccludedContainer = false;
            }
        } else if (activityRecord.isState(ActivityRecord.State.INITIALIZING)) {
            activityRecord.cancelInitializing();
        }
        if (shouldBeVisibleUnchecked) {
            if (activityRecord.finishing) {
                return;
            }
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(Task.TAG_VISIBILITY, "Make visible? " + activityRecord + " finishing=" + activityRecord.finishing + " state=" + activityRecord.getState());
            }
            if (activityRecord != this.mStarting && this.mNotifyClients) {
                activityRecord.ensureActivityConfiguration(0, this.mPreserveWindows, true);
            }
            if (!activityRecord.attachedToProcess()) {
                ActivityRecord activityRecord3 = this.mStarting;
                int i = this.mConfigChanges;
                if (z && z3) {
                    z2 = true;
                }
                makeVisibleAndRestartIfNeeded(activityRecord3, i, z2, activityRecord);
            } else if (activityRecord.isVisibleRequested()) {
                if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                    Slog.v(Task.TAG_VISIBILITY, "Skipping: already visible at " + activityRecord);
                }
                boolean z4 = activityRecord.mClientVisibilityDeferred;
                if (z4 && this.mNotifyClients) {
                    if (z4) {
                        activityRecord2 = null;
                    }
                    activityRecord.makeActiveIfNeeded(activityRecord2);
                    activityRecord.mClientVisibilityDeferred = false;
                }
                activityRecord.handleAlreadyVisible();
                if (this.mNotifyClients) {
                    activityRecord.makeActiveIfNeeded(this.mStarting);
                }
            } else {
                activityRecord.makeVisibleIfNeeded(this.mStarting, this.mNotifyClients);
            }
            this.mConfigChanges |= activityRecord.configChangeFlags;
        } else {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(Task.TAG_VISIBILITY, "Make invisible? " + activityRecord + " finishing=" + activityRecord.finishing + " state=" + activityRecord.getState() + " containerShouldBeVisible=" + this.mContainerShouldBeVisible + " behindFullyOccludedContainer=" + this.mBehindFullyOccludedContainer + " mLaunchTaskBehind=" + activityRecord.mLaunchTaskBehind);
            }
            activityRecord.makeInvisible();
        }
        if (!this.mBehindFullyOccludedContainer && this.mTaskFragment.isActivityTypeHome() && activityRecord.isRootOfTask()) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(Task.TAG_VISIBILITY, "Home task: at " + this.mTaskFragment + " containerShouldBeVisible=" + this.mContainerShouldBeVisible + " behindOccludedParentContainer=" + this.mBehindFullyOccludedContainer);
            }
            this.mBehindFullyOccludedContainer = true;
        }
    }

    private void makeVisibleAndRestartIfNeeded(ActivityRecord activityRecord, int i, boolean z, ActivityRecord activityRecord2) {
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(Task.TAG_VISIBILITY, "Start and freeze screen for " + activityRecord2);
        }
        if (activityRecord2 != activityRecord) {
            activityRecord2.startFreezingScreenLocked(i);
        }
        if (!activityRecord2.isVisibleRequested() || activityRecord2.mLaunchTaskBehind) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(Task.TAG_VISIBILITY, "Starting and making visible: " + activityRecord2);
            }
            activityRecord2.setVisibility(true);
        }
        if (activityRecord2 != activityRecord) {
            this.mTaskFragment.mTaskSupervisor.startSpecificActivity(activityRecord2, z, true);
        }
    }
}
