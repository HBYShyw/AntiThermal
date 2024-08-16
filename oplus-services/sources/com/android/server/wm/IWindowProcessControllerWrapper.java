package com.android.server.wm;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowProcessControllerWrapper {
    default ActivityTaskManagerService getAtm() {
        return null;
    }

    default IWindowProcessControllerExt getExtImpl() {
        return new IWindowProcessControllerExt() { // from class: com.android.server.wm.IWindowProcessControllerWrapper.1
        };
    }

    default ArrayList<String> getPkgList() {
        return new ArrayList<>();
    }

    default ArrayList<ActivityRecord> getActivities() {
        return new ArrayList<>();
    }
}
