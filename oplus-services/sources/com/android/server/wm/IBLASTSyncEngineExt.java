package com.android.server.wm;

import android.util.ArraySet;
import com.android.server.wm.BLASTSyncEngine;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IBLASTSyncEngineExt {
    default void dump(PrintWriter printWriter, String str, BLASTSyncEngine bLASTSyncEngine, ArrayList<BLASTSyncEngine.SyncGroup> arrayList) {
    }

    default void logOutUnfinishedcontainerInfo(BLASTSyncEngine.SyncGroup syncGroup, WindowContainer windowContainer) {
    }

    default boolean onSurfacePlacement(int i, WindowContainer windowContainer, ArraySet<WindowContainer> arraySet) {
        return false;
    }

    default void onTimeout(WindowManagerService windowManagerService) {
    }

    default void onTimeout(WindowManagerService windowManagerService, BLASTSyncEngine.SyncGroup syncGroup) {
    }

    default boolean skipAddToSync(WindowContainer windowContainer, BLASTSyncEngine.SyncGroup syncGroup, BLASTSyncEngine.SyncGroup syncGroup2) {
        return false;
    }

    default boolean tryFinishAheadIfNeed(int i, BLASTSyncEngine.SyncGroup syncGroup, ArraySet<WindowContainer> arraySet) {
        return false;
    }
}
