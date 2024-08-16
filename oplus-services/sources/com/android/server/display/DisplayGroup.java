package com.android.server.display;

import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayGroup {
    private int mChangeCount;
    private final List<LogicalDisplay> mDisplays = new ArrayList();
    private final int mGroupId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayGroup(int i) {
        this.mGroupId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getGroupId() {
        return this.mGroupId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addDisplayLocked(LogicalDisplay logicalDisplay) {
        if (containsLocked(logicalDisplay)) {
            return;
        }
        this.mChangeCount++;
        this.mDisplays.add(logicalDisplay);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsLocked(LogicalDisplay logicalDisplay) {
        return this.mDisplays.contains(logicalDisplay);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeDisplayLocked(LogicalDisplay logicalDisplay) {
        this.mChangeCount++;
        return this.mDisplays.remove(logicalDisplay);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEmptyLocked() {
        return this.mDisplays.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getChangeCountLocked() {
        return this.mChangeCount;
    }

    int getSizeLocked() {
        return this.mDisplays.size();
    }

    int getIdLocked(int i) {
        return this.mDisplays.get(i).getDisplayIdLocked();
    }
}
