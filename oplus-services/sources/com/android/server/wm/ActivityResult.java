package com.android.server.wm;

import android.app.ResultInfo;
import android.content.Intent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class ActivityResult extends ResultInfo {
    final ActivityRecord mFrom;

    public ActivityResult(ActivityRecord activityRecord, String str, int i, int i2, Intent intent) {
        super(str, i, i2, intent);
        this.mFrom = activityRecord;
    }
}
