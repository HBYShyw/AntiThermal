package com.android.server.wm;

import android.app.ITaskStackListener;
import android.os.Message;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskChangeNotificationControllerExt {
    default boolean shouldSkipSendTaskSnapshot(ActivityTaskSupervisor activityTaskSupervisor, ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        return false;
    }
}
