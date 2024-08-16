package com.android.server.pm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.UserHandle;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILauncherAppsServiceExt {
    default void addExtraUserHandle(Intent intent, UserHandle userHandle) {
    }

    default boolean checkMultiAppUserState(Context context, UserHandle userHandle) {
        return false;
    }

    default Handler getFgHandler(Handler handler) {
        return handler;
    }
}
