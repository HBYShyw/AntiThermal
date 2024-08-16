package com.android.server.wm;

import android.hardware.display.DisplayManagerInternal;
import android.util.ArraySet;
import android.util.SparseArray;
import android.view.DisplayInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class PossibleDisplayInfoMapper {
    private static final boolean DEBUG = false;
    private static final String TAG = "PossibleDisplayInfoMapper";
    private final SparseArray<Set<DisplayInfo>> mDisplayInfos = new SparseArray<>();
    private final DisplayManagerInternal mDisplayManagerInternal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PossibleDisplayInfoMapper(DisplayManagerInternal displayManagerInternal) {
        this.mDisplayManagerInternal = displayManagerInternal;
    }

    public List<DisplayInfo> getPossibleDisplayInfos(int i) {
        updatePossibleDisplayInfos(i);
        if (!this.mDisplayInfos.contains(i)) {
            return new ArrayList();
        }
        return List.copyOf(this.mDisplayInfos.get(i));
    }

    public void updatePossibleDisplayInfos(int i) {
        updateDisplayInfos(this.mDisplayManagerInternal.getPossibleDisplayInfo(i));
    }

    public void removePossibleDisplayInfos(int i) {
        this.mDisplayInfos.remove(i);
    }

    private void updateDisplayInfos(Set<DisplayInfo> set) {
        this.mDisplayInfos.clear();
        for (DisplayInfo displayInfo : set) {
            Set<DisplayInfo> set2 = this.mDisplayInfos.get(displayInfo.displayId, new ArraySet());
            set2.add(displayInfo);
            this.mDisplayInfos.put(displayInfo.displayId, set2);
        }
    }
}
