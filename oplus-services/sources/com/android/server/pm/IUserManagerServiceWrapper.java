package com.android.server.pm;

import com.android.server.pm.UserManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IUserManagerServiceWrapper {
    default Object getUsersLock() {
        return null;
    }

    default void setUserRestriction(String str, boolean z, int i) {
    }

    default void writeUserLP(UserManagerService.UserData userData) {
    }

    default void writeUserListLP() {
    }
}
