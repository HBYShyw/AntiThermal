package com.android.server.wm;

import android.util.ArraySet;
import android.util.SparseArray;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class WindowProcessControllerMap {
    private final SparseArray<WindowProcessController> mPidMap = new SparseArray<>();
    private final Map<Integer, ArraySet<WindowProcessController>> mUidMap = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowProcessController getProcess(int i) {
        return this.mPidMap.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArraySet<WindowProcessController> getProcesses(int i) {
        return this.mUidMap.get(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseArray<WindowProcessController> getPidMap() {
        return this.mPidMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void put(int i, WindowProcessController windowProcessController) {
        WindowProcessController windowProcessController2 = this.mPidMap.get(i);
        if (windowProcessController2 != null) {
            removeProcessFromUidMap(windowProcessController2);
        }
        this.mPidMap.put(i, windowProcessController);
        int i2 = windowProcessController.mUid;
        ArraySet<WindowProcessController> orDefault = this.mUidMap.getOrDefault(Integer.valueOf(i2), new ArraySet<>());
        orDefault.add(windowProcessController);
        this.mUidMap.put(Integer.valueOf(i2), orDefault);
        windowProcessController.getWrapper().getExtImpl().shouldUpdateProcessConfig(windowProcessController, windowProcessController.getWrapper().getAtm());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(int i) {
        WindowProcessController windowProcessController = this.mPidMap.get(i);
        if (windowProcessController != null) {
            this.mPidMap.remove(i);
            removeProcessFromUidMap(windowProcessController);
            windowProcessController.destroy();
        }
    }

    private void removeProcessFromUidMap(WindowProcessController windowProcessController) {
        if (windowProcessController == null) {
            return;
        }
        int i = windowProcessController.mUid;
        ArraySet<WindowProcessController> arraySet = this.mUidMap.get(Integer.valueOf(i));
        if (arraySet != null) {
            arraySet.remove(windowProcessController);
            if (arraySet.isEmpty()) {
                this.mUidMap.remove(Integer.valueOf(i));
            }
        }
    }
}
