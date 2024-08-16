package com.android.server.wm;

import android.common.OplusFeatureList;
import com.android.server.IOplusCommonManagerServiceEx;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusActivityTaskManagerServiceEx extends IOplusCommonManagerServiceEx {
    public static final IOplusActivityTaskManagerServiceEx DEFAULT = new IOplusActivityTaskManagerServiceEx() { // from class: com.android.server.wm.IOplusActivityTaskManagerServiceEx.1
    };
    public static final String NAME = "IOplusActivityTaskManagerServiceEx";

    default ActivityTaskManagerService getActivityTaskManagerService() {
        return null;
    }

    default void hookAtmsConfigurationChang(int i, RootWindowContainer rootWindowContainer, WindowManagerService windowManagerService) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusActivityTaskManagerServiceEx;
    }

    default IOplusActivityTaskManagerServiceEx getDefault() {
        return DEFAULT;
    }

    default IOplusActivityStackSupervisorInner getColorActivityStackSupervisorInner(ActivityStackSupervisor activityStackSupervisor) {
        return new IOplusActivityStackSupervisorInner() { // from class: com.android.server.wm.IOplusActivityTaskManagerServiceEx.2
        };
    }

    default IOplusActivityStackInner getColorActivityStackInner(Task task) {
        return new IOplusActivityStackInner() { // from class: com.android.server.wm.IOplusActivityTaskManagerServiceEx.3
        };
    }
}
