package com.android.server.am;

import android.content.res.Resources;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IUserSwitchingDialogExt {
    default String fixSwitchingMessage(int i, String str, int i2, Resources resources) {
        return null;
    }

    default void startFreezingScreenInStartUser(int i, int i2) {
    }

    default void startUserInternalEnter(boolean z, int i, int i2, long j, long j2, long j3, boolean z2) {
    }
}
