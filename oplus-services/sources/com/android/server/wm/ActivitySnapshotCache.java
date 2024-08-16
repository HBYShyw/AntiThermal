package com.android.server.wm;

import android.window.TaskSnapshot;
import com.android.server.wm.SnapshotCache;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivitySnapshotCache extends SnapshotCache<ActivityRecord> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivitySnapshotCache(WindowManagerService windowManagerService) {
        super(windowManagerService, "Activity");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.SnapshotCache
    public void putSnapshot(ActivityRecord activityRecord, TaskSnapshot taskSnapshot) {
        int identityHashCode = System.identityHashCode(activityRecord);
        SnapshotCache.CacheEntry cacheEntry = this.mRunningCache.get(Integer.valueOf(identityHashCode));
        if (cacheEntry != null) {
            this.mAppIdMap.remove(cacheEntry.topApp);
        }
        this.mAppIdMap.put(activityRecord, Integer.valueOf(identityHashCode));
        this.mRunningCache.put(Integer.valueOf(identityHashCode), new SnapshotCache.CacheEntry(taskSnapshot, activityRecord));
    }
}
