package com.android.server.wm;

import android.util.Slog;
import android.window.TaskSnapshot;
import com.android.server.wm.SnapshotCache;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskSnapshotCache extends SnapshotCache<Task> {
    private final AppSnapshotLoader mLoader;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskSnapshotCache(WindowManagerService windowManagerService, AppSnapshotLoader appSnapshotLoader) {
        super(windowManagerService, "Task");
        this.mLoader = appSnapshotLoader;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.SnapshotCache
    public void putSnapshot(Task task, TaskSnapshot taskSnapshot) {
        SnapshotCache.CacheEntry cacheEntry = this.mRunningCache.get(Integer.valueOf(task.mTaskId));
        if (cacheEntry != null) {
            this.mAppIdMap.remove(cacheEntry.topApp);
        }
        ActivityRecord topMostActivity = task.getTopMostActivity();
        this.mAppIdMap.put(topMostActivity, Integer.valueOf(task.mTaskId));
        this.mRunningCache.put(Integer.valueOf(task.mTaskId), new SnapshotCache.CacheEntry(taskSnapshot, topMostActivity));
        Slog.d("TaskSnapshotCache", "putSnapshot mRunningCache top= " + topMostActivity + ", mTaskId=" + task.mTaskId + ", snapshot=" + taskSnapshot + ", mAppIdMap size= " + this.mAppIdMap.size());
        if (this.mAppIdMap.size() > 1000) {
            this.mAppIdMap.clear();
            this.mRunningCache.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskSnapshot getSnapshot(int i, int i2, boolean z, boolean z2) {
        TaskSnapshot snapshot = getSnapshot(Integer.valueOf(i));
        if (snapshot != null) {
            return snapshot;
        }
        if (z) {
            return tryRestoreFromDisk(i, i2, z2);
        }
        return null;
    }

    private TaskSnapshot tryRestoreFromDisk(int i, int i2, boolean z) {
        return this.mLoader.loadTask(i, i2, z);
    }
}
