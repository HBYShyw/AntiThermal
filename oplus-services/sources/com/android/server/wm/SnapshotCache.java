package com.android.server.wm;

import android.os.Debug;
import android.util.ArrayMap;
import android.util.Slog;
import android.window.TaskSnapshot;
import com.android.server.wm.WindowContainer;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public abstract class SnapshotCache<TYPE extends WindowContainer> {
    private static final String TAG = "SnapshotCache";
    protected final String mName;
    protected final WindowManagerService mService;
    protected final ArrayMap<ActivityRecord, Integer> mAppIdMap = new ArrayMap<>();
    protected final ArrayMap<Integer, CacheEntry> mRunningCache = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void putSnapshot(TYPE type, TaskSnapshot taskSnapshot);

    /* JADX INFO: Access modifiers changed from: package-private */
    public SnapshotCache(WindowManagerService windowManagerService, String str) {
        this.mService = windowManagerService;
        this.mName = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearRunningCache() {
        Slog.d(TAG, "clearRunningCache, current size= " + this.mRunningCache.size() + ", callers=" + Debug.getCallers(5));
        this.mRunningCache.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final TaskSnapshot getSnapshot(Integer num) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                CacheEntry cacheEntry = this.mRunningCache.get(num);
                if (cacheEntry == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                TaskSnapshot taskSnapshot = cacheEntry.snapshot;
                WindowManagerService.resetPriorityAfterLockedSection();
                return taskSnapshot;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppRemoved(ActivityRecord activityRecord) {
        Integer num = this.mAppIdMap.get(activityRecord);
        if (num != null) {
            removeRunningEntry(num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppDied(ActivityRecord activityRecord) {
        Integer num = this.mAppIdMap.get(activityRecord);
        if (num != null) {
            removeRunningEntry(num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onIdRemoved(Integer num) {
        removeRunningEntry(num);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRunningEntry(Integer num) {
        CacheEntry cacheEntry = this.mRunningCache.get(num);
        if (cacheEntry != null) {
            Slog.d(TAG, "removeRunningEntry id: " + num + ", callers=" + Debug.getCallers(3));
            this.mAppIdMap.remove(cacheEntry.topApp);
            this.mRunningCache.remove(num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        String str3 = str2 + "  ";
        printWriter.println(str + "SnapshotCache " + this.mName);
        for (int size = this.mRunningCache.size() + (-1); size >= 0; size += -1) {
            CacheEntry valueAt = this.mRunningCache.valueAt(size);
            printWriter.println(str2 + "Entry token=" + this.mRunningCache.keyAt(size));
            printWriter.println(str3 + "topApp=" + valueAt.topApp);
            printWriter.println(str3 + "snapshot=" + valueAt.snapshot);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class CacheEntry {
        final TaskSnapshot snapshot;
        final ActivityRecord topApp;

        /* JADX INFO: Access modifiers changed from: package-private */
        public CacheEntry(TaskSnapshot taskSnapshot, ActivityRecord activityRecord) {
            this.snapshot = taskSnapshot;
            this.topApp = activityRecord;
        }
    }
}
