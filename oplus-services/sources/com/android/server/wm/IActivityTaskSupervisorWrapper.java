package com.android.server.wm;

import android.content.Intent;
import android.content.pm.ResolveInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityTaskSupervisorWrapper {
    default IActivityTaskSupervisorExt getExtImpl() {
        return new IActivityTaskSupervisorExt() { // from class: com.android.server.wm.IActivityTaskSupervisorWrapper.1
        };
    }

    default ResolveInfo resolveIntent(Intent intent, String str, int i, int i2, int i3, int i4) {
        return new ResolveInfo();
    }
}
