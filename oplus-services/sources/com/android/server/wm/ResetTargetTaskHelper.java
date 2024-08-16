package com.android.server.wm;

import android.app.ActivityOptions;
import android.os.Debug;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ResetTargetTaskHelper implements Consumer<Task>, Predicate<ActivityRecord> {
    private int mActivityReparentPosition;
    private boolean mCanMoveOptions;
    private boolean mForceReset;
    private boolean mIsTargetTask;
    private ActivityRecord mRoot;
    private Task mTargetRootTask;
    private Task mTargetTask;
    private boolean mTargetTaskFound;
    private Task mTask;
    private ActivityOptions mTopOptions;
    private ArrayList<ActivityRecord> mResultActivities = new ArrayList<>();
    private ArrayList<ActivityRecord> mAllActivities = new ArrayList<>();
    private ArrayList<ActivityRecord> mPendingReparentActivities = new ArrayList<>();

    private void reset(Task task) {
        this.mTask = task;
        this.mRoot = null;
        this.mCanMoveOptions = true;
        this.mResultActivities.clear();
        this.mAllActivities.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions process(Task task, boolean z) {
        this.mTopOptions = null;
        this.mForceReset = z;
        this.mTargetTask = task;
        this.mTargetTaskFound = false;
        this.mTargetRootTask = task.getRootTask();
        this.mActivityReparentPosition = -1;
        task.mWmService.mRoot.forAllLeafTasks(this, true);
        processPendingReparentActivities();
        reset(null);
        return this.mTopOptions;
    }

    @Override // java.util.function.Consumer
    public void accept(Task task) {
        reset(task);
        ActivityRecord rootActivity = task.getRootActivity(true);
        this.mRoot = rootActivity;
        if (rootActivity == null) {
            return;
        }
        boolean z = task == this.mTargetTask;
        this.mIsTargetTask = z;
        if (z) {
            this.mTargetTaskFound = true;
        }
        task.forAllActivities((Predicate<ActivityRecord>) this);
    }

    @Override // java.util.function.Predicate
    public boolean test(ActivityRecord activityRecord) {
        String str;
        ActivityRecord activityBelow;
        String str2;
        if (activityRecord == this.mRoot) {
            return true;
        }
        this.mAllActivities.add(activityRecord);
        int i = activityRecord.info.flags;
        boolean z = (i & 2) != 0;
        boolean z2 = (i & 64) != 0;
        boolean z3 = (activityRecord.intent.getFlags() & 524288) != 0;
        if (this.mIsTargetTask) {
            if (!z && !z3) {
                if (activityRecord.resultTo != null) {
                    this.mResultActivities.add(activityRecord);
                    return false;
                }
                if (z2 && (str2 = activityRecord.taskAffinity) != null && !str2.equals(this.mTask.affinity)) {
                    this.mPendingReparentActivities.add(activityRecord);
                    return false;
                }
            }
            if (this.mForceReset || z || z3) {
                if (z3) {
                    finishActivities(this.mAllActivities, "clearWhenTaskReset");
                } else {
                    this.mResultActivities.add(activityRecord);
                    finishActivities(this.mResultActivities, "reset-task");
                }
                this.mResultActivities.clear();
                return false;
            }
            this.mResultActivities.clear();
            return false;
        }
        if (activityRecord.resultTo != null) {
            this.mResultActivities.add(activityRecord);
            return false;
        }
        if (this.mTargetTaskFound && z2 && (str = this.mTargetTask.affinity) != null && str.equals(activityRecord.taskAffinity)) {
            this.mResultActivities.add(activityRecord);
            if (this.mForceReset || z) {
                finishActivities(this.mResultActivities, "move-affinity");
            } else {
                if (this.mActivityReparentPosition == -1) {
                    this.mActivityReparentPosition = this.mTargetTask.getChildCount();
                }
                processResultActivities(activityRecord, this.mTargetTask, this.mActivityReparentPosition, false, false);
                if (activityRecord.info.launchMode == 1 && (activityBelow = this.mTargetTask.getActivityBelow(activityRecord)) != null && activityBelow.intent.getComponent().equals(activityRecord.intent.getComponent())) {
                    activityBelow.finishIfPossible("replace", false);
                }
            }
        }
        return false;
    }

    private void finishActivities(ArrayList<ActivityRecord> arrayList, String str) {
        boolean z = this.mCanMoveOptions;
        while (!arrayList.isEmpty()) {
            ActivityRecord remove = arrayList.remove(0);
            if (!remove.finishing) {
                z = takeOption(remove, z);
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_TASKS, -1704402370, 0, (String) null, new Object[]{String.valueOf(remove)});
                }
                remove.finishIfPossible(str, false);
            }
        }
    }

    private void processResultActivities(ActivityRecord activityRecord, Task task, int i, boolean z, boolean z2) {
        boolean z3 = this.mCanMoveOptions;
        while (!this.mResultActivities.isEmpty()) {
            ActivityRecord remove = this.mResultActivities.remove(0);
            if (!z || !remove.finishing) {
                if (z2) {
                    z3 = takeOption(remove, z3);
                }
                if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -1638958146, 0, (String) null, new Object[]{String.valueOf(remove), String.valueOf(this.mTask), String.valueOf(task), String.valueOf(Debug.getCallers(4))});
                }
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_TASKS, -1198579104, 0, (String) null, new Object[]{String.valueOf(remove), String.valueOf(activityRecord)});
                }
                remove.reparent(task, i, "resetTargetTaskIfNeeded");
            }
        }
    }

    private void processPendingReparentActivities() {
        Task reuseOrCreateTask;
        if (this.mPendingReparentActivities.isEmpty()) {
            return;
        }
        Task task = this.mTargetRootTask;
        ActivityTaskManagerService activityTaskManagerService = task.mAtmService;
        TaskDisplayArea displayArea = task.getDisplayArea();
        int windowingMode = this.mTargetRootTask.getWindowingMode();
        int activityType = this.mTargetRootTask.getActivityType();
        while (!this.mPendingReparentActivities.isEmpty()) {
            ActivityRecord remove = this.mPendingReparentActivities.remove(0);
            boolean alwaysCreateRootTask = DisplayContent.alwaysCreateRootTask(windowingMode, activityType);
            Task bottomMostTask = alwaysCreateRootTask ? displayArea.getBottomMostTask() : this.mTargetRootTask.getBottomMostTask();
            if (bottomMostTask == null || !remove.taskAffinity.equals(bottomMostTask.affinity)) {
                bottomMostTask = null;
            } else if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_TASKS, -877494781, 0, (String) null, new Object[]{String.valueOf(remove), String.valueOf(bottomMostTask)});
            }
            if (bottomMostTask == null) {
                if (alwaysCreateRootTask) {
                    reuseOrCreateTask = displayArea.getOrCreateRootTask(windowingMode, activityType, false);
                } else {
                    reuseOrCreateTask = this.mTargetRootTask.reuseOrCreateTask(remove.info, null, false);
                }
                bottomMostTask = reuseOrCreateTask;
                bottomMostTask.affinityIntent = remove.intent;
            }
            remove.reparent(bottomMostTask, 0, "resetTargetTaskIfNeeded");
            activityTaskManagerService.mTaskSupervisor.mRecentTasks.add(bottomMostTask);
        }
    }

    private boolean takeOption(ActivityRecord activityRecord, boolean z) {
        this.mCanMoveOptions = false;
        if (!z || this.mTopOptions != null) {
            return z;
        }
        ActivityOptions options = activityRecord.getOptions();
        this.mTopOptions = options;
        if (options == null) {
            return z;
        }
        activityRecord.clearOptionsAnimation();
        return false;
    }
}
