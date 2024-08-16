package com.android.server.usage;

import android.app.usage.BroadcastResponseStats;
import android.util.ArrayMap;
import com.android.internal.util.IndentingPrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class UserBroadcastResponseStats {
    private ArrayMap<BroadcastEvent, BroadcastResponseStats> mResponseStats = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastResponseStats getBroadcastResponseStats(BroadcastEvent broadcastEvent) {
        return this.mResponseStats.get(broadcastEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastResponseStats getOrCreateBroadcastResponseStats(BroadcastEvent broadcastEvent) {
        BroadcastResponseStats broadcastResponseStats = this.mResponseStats.get(broadcastEvent);
        if (broadcastResponseStats != null) {
            return broadcastResponseStats;
        }
        BroadcastResponseStats broadcastResponseStats2 = new BroadcastResponseStats(broadcastEvent.getTargetPackage(), broadcastEvent.getIdForResponseEvent());
        this.mResponseStats.put(broadcastEvent, broadcastResponseStats2);
        return broadcastResponseStats2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void populateAllBroadcastResponseStats(List<BroadcastResponseStats> list, String str, long j) {
        for (int size = this.mResponseStats.size() - 1; size >= 0; size--) {
            BroadcastEvent keyAt = this.mResponseStats.keyAt(size);
            if ((j == 0 || j == keyAt.getIdForResponseEvent()) && (str == null || str.equals(keyAt.getTargetPackage()))) {
                list.add(this.mResponseStats.valueAt(size));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearBroadcastResponseStats(String str, long j) {
        for (int size = this.mResponseStats.size() - 1; size >= 0; size--) {
            BroadcastEvent keyAt = this.mResponseStats.keyAt(size);
            if ((j == 0 || j == keyAt.getIdForResponseEvent()) && (str == null || str.equals(keyAt.getTargetPackage()))) {
                this.mResponseStats.removeAt(size);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageRemoved(String str) {
        for (int size = this.mResponseStats.size() - 1; size >= 0; size--) {
            if (this.mResponseStats.keyAt(size).getTargetPackage().equals(str)) {
                this.mResponseStats.removeAt(size);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        for (int i = 0; i < this.mResponseStats.size(); i++) {
            BroadcastEvent keyAt = this.mResponseStats.keyAt(i);
            BroadcastResponseStats valueAt = this.mResponseStats.valueAt(i);
            indentingPrintWriter.print(keyAt);
            indentingPrintWriter.print(" -> ");
            indentingPrintWriter.println(valueAt);
        }
    }
}
