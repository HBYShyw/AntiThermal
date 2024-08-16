package com.android.server.wm;

import android.app.ActivityManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RunningTasks implements Consumer<Task> {
    static final int FLAG_ALLOWED = 2;
    static final int FLAG_CROSS_USERS = 4;
    static final int FLAG_FILTER_ONLY_VISIBLE_RECENTS = 1;
    static final int FLAG_KEEP_INTENT_EXTRA = 8;
    private boolean mAllowed;
    private int mCallingUid;
    private boolean mCrossUser;
    private boolean mFilterOnlyVisibleRecents;
    private boolean mKeepIntentExtra;
    private ArraySet<Integer> mProfileIds;
    private RecentTasks mRecentTasks;
    private int mUserId;
    private final ArrayList<Task> mTmpSortedTasks = new ArrayList<>();
    private final ArrayList<Task> mTmpVisibleTasks = new ArrayList<>();
    private final ArrayList<Task> mTmpInvisibleTasks = new ArrayList<>();
    private final ArrayList<Task> mTmpFocusedTasks = new ArrayList<>();
    private IRunningTasksExt mRunningTasksExt = (IRunningTasksExt) ExtLoader.type(IRunningTasksExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00c1, code lost:
    
        if (r12 == r11.mWmService.mContext.getPackageManager().getPackageUidAsUser("com.android.launcher", r6.mUserId)) goto L55;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void getTasks(int i, List<ActivityManager.RunningTaskInfo> list, int i2, RecentTasks recentTasks, WindowContainer<?> windowContainer, int i3, ArraySet<Integer> arraySet) {
        int i4;
        boolean z;
        ActivityRecord activityRecord;
        int size;
        if (i <= 0) {
            return;
        }
        this.mCallingUid = i3;
        this.mUserId = UserHandle.getUserId(i3);
        i4 = 0;
        z = true;
        this.mCrossUser = (i2 & 4) == 4;
        this.mProfileIds = arraySet;
        this.mAllowed = (i2 & 2) == 2;
        this.mFilterOnlyVisibleRecents = (i2 & 1) == 1;
        this.mRecentTasks = recentTasks;
        this.mKeepIntentExtra = (i2 & 8) == 8;
        if (windowContainer instanceof RootWindowContainer) {
            ((RootWindowContainer) windowContainer).forAllDisplays(new Consumer() { // from class: com.android.server.wm.RunningTasks$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    RunningTasks.this.lambda$getTasks$0((DisplayContent) obj);
                }
            });
        } else {
            DisplayContent displayContent = windowContainer.getDisplayContent();
            Task task = null;
            if (displayContent != null && (activityRecord = displayContent.mFocusedApp) != null) {
                task = activityRecord.getTask();
            }
            if (task != null && task.isDescendantOf(windowContainer)) {
                this.mTmpFocusedTasks.add(task);
            }
            processTaskInWindowContainer(windowContainer);
        }
        size = this.mTmpVisibleTasks.size();
        for (int i5 = 0; i5 < this.mTmpFocusedTasks.size(); i5++) {
            Task task2 = this.mTmpFocusedTasks.get(i5);
            if (this.mTmpVisibleTasks.remove(task2)) {
                this.mTmpSortedTasks.add(task2);
            }
        }
        if (!this.mTmpVisibleTasks.isEmpty()) {
            this.mTmpSortedTasks.addAll(this.mTmpVisibleTasks);
        }
        if (!this.mTmpInvisibleTasks.isEmpty()) {
            this.mTmpSortedTasks.addAll(this.mTmpInvisibleTasks);
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        z = false;
        int min = Math.min(i, this.mTmpSortedTasks.size());
        long elapsedRealtime = SystemClock.elapsedRealtime();
        while (i4 < min) {
            Task task3 = this.mTmpSortedTasks.get(i4);
            if (z) {
                task3 = this.mRunningTasksExt.replaceByMultiSearchIfNeed(task3, this.mTmpSortedTasks);
            }
            list.add(createRunningTaskInfo(task3, i4 < size ? (min + elapsedRealtime) - i4 : -1L));
            i4++;
        }
        this.mTmpFocusedTasks.clear();
        this.mTmpVisibleTasks.clear();
        this.mTmpInvisibleTasks.clear();
        this.mTmpSortedTasks.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTasks$0(DisplayContent displayContent) {
        ActivityRecord activityRecord = displayContent.mFocusedApp;
        Task task = activityRecord != null ? activityRecord.getTask() : null;
        if (task != null) {
            this.mTmpFocusedTasks.add(task);
        }
        processTaskInWindowContainer(displayContent);
    }

    private void processTaskInWindowContainer(WindowContainer windowContainer) {
        windowContainer.forAllLeafTasks(this, true);
    }

    @Override // java.util.function.Consumer
    public void accept(Task task) {
        int i;
        if (task.getTopNonFinishingActivity() == null) {
            return;
        }
        if (task.effectiveUid == this.mCallingUid || (((i = task.mUserId) == this.mUserId || this.mCrossUser || this.mProfileIds.contains(Integer.valueOf(i))) && this.mAllowed)) {
            if (!this.mFilterOnlyVisibleRecents || task.getActivityType() == 2 || task.getActivityType() == 3 || this.mRecentTasks.isVisibleRecentTask(task)) {
                if (task.isVisible()) {
                    this.mTmpVisibleTasks.add(task);
                } else {
                    this.mTmpInvisibleTasks.add(task);
                }
            }
        }
    }

    private ActivityManager.RunningTaskInfo createRunningTaskInfo(Task task, long j) {
        ActivityManager.RunningTaskInfo runningTaskInfo = new ActivityManager.RunningTaskInfo();
        task.fillTaskInfo(runningTaskInfo, !this.mKeepIntentExtra);
        if (j > 0) {
            runningTaskInfo.lastActiveTime = j;
        }
        runningTaskInfo.id = runningTaskInfo.taskId;
        if (!this.mAllowed) {
            Task.trimIneffectiveInfo(task, runningTaskInfo);
        }
        return runningTaskInfo;
    }
}
