package com.android.server.usage;

import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.LongArrayQueue;
import android.util.TimeUtils;
import com.android.internal.util.IndentingPrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class UserBroadcastEvents {
    private ArrayMap<String, ArraySet<BroadcastEvent>> mBroadcastEvents = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArraySet<BroadcastEvent> getBroadcastEvents(String str) {
        return this.mBroadcastEvents.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArraySet<BroadcastEvent> getOrCreateBroadcastEvents(String str) {
        ArraySet<BroadcastEvent> arraySet = this.mBroadcastEvents.get(str);
        if (arraySet != null) {
            return arraySet;
        }
        ArraySet<BroadcastEvent> arraySet2 = new ArraySet<>();
        this.mBroadcastEvents.put(str, arraySet2);
        return arraySet2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageRemoved(String str) {
        this.mBroadcastEvents.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUidRemoved(int i) {
        clear(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear(int i) {
        for (int size = this.mBroadcastEvents.size() - 1; size >= 0; size--) {
            ArraySet<BroadcastEvent> valueAt = this.mBroadcastEvents.valueAt(size);
            for (int size2 = valueAt.size() - 1; size2 >= 0; size2--) {
                if (valueAt.valueAt(size2).getSourceUid() == i) {
                    valueAt.removeAt(size2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        for (int i = 0; i < this.mBroadcastEvents.size(); i++) {
            String keyAt = this.mBroadcastEvents.keyAt(i);
            ArraySet<BroadcastEvent> valueAt = this.mBroadcastEvents.valueAt(i);
            indentingPrintWriter.println(keyAt + ":");
            indentingPrintWriter.increaseIndent();
            if (valueAt.size() == 0) {
                indentingPrintWriter.println("<empty>");
            } else {
                for (int i2 = 0; i2 < valueAt.size(); i2++) {
                    BroadcastEvent valueAt2 = valueAt.valueAt(i2);
                    indentingPrintWriter.println(valueAt2);
                    indentingPrintWriter.increaseIndent();
                    LongArrayQueue timestampsMs = valueAt2.getTimestampsMs();
                    for (int i3 = 0; i3 < timestampsMs.size(); i3++) {
                        if (i3 > 0) {
                            indentingPrintWriter.print(',');
                        }
                        TimeUtils.formatDuration(timestampsMs.get(i3), indentingPrintWriter);
                    }
                    indentingPrintWriter.println();
                    indentingPrintWriter.decreaseIndent();
                }
            }
            indentingPrintWriter.decreaseIndent();
        }
    }
}
