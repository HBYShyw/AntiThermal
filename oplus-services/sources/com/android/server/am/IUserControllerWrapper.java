package com.android.server.am;

import android.util.SparseIntArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IUserControllerWrapper {
    default boolean maybeUnlockUser(int i) {
        return false;
    }

    default void startUserInForeground(int i) {
    }

    default IUserControllerExt getExtImpl() {
        return new IUserControllerExt() { // from class: com.android.server.am.IUserControllerWrapper.1
        };
    }

    default SparseIntArray getUserProfileGroupIds() {
        return new SparseIntArray();
    }
}
