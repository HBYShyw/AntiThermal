package com.android.server.wm;

import android.content.Context;
import com.mediatek.server.MtkSystemServiceFactory;
import com.mediatek.server.am.AmsExt;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityTaskManagerServiceSocExtImpl implements IActivityTaskManagerServiceSocExt {
    private ActivityRecord lastResumedBeforeActivitySwitch;
    public AmsExt mAmsExt = MtkSystemServiceFactory.getInstance().makeAmsExt();
    ActivityTaskManagerService mService;

    public ActivityTaskManagerServiceSocExtImpl(Object obj) {
        this.mService = (ActivityTaskManagerService) obj;
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onActivityStateChanged(ActivityRecord activityRecord, boolean z) {
        this.mAmsExt.onActivityStateChanged(activityRecord, z);
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onEndOfActivityIdle(Context context, ActivityRecord activityRecord) {
        this.mAmsExt.onEndOfActivityIdle(context, activityRecord);
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onBeforeActivitySwitch(Task task, ActivityRecord activityRecord) {
        ActivityRecord activityRecord2;
        if (task == null || (activityRecord2 = task.topRunningActivityLocked()) == null) {
            return;
        }
        this.mAmsExt.onBeforeActivitySwitch(activityRecord, activityRecord2, true, activityRecord2.getActivityType(), false);
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onAfterActivityResumed(ActivityRecord activityRecord) {
        this.mAmsExt.onAfterActivityResumed(activityRecord);
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void setLastResumedBeforeActivitySwitch(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        if (activityRecord == null) {
            activityRecord = activityRecord2;
        }
        this.lastResumedBeforeActivitySwitch = activityRecord;
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onBeforeActivitySwitch(ActivityRecord activityRecord, boolean z, int i, boolean z2) {
        this.mAmsExt.onBeforeActivitySwitch(this.lastResumedBeforeActivitySwitch, activityRecord, z, i, z2);
    }
}
