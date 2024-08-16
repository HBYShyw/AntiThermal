package com.android.server.appop;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface AppOpsManagerLocal {
    boolean isUidInForeground(int i);
}
