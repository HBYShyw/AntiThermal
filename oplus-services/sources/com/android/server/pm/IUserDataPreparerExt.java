package com.android.server.pm;

import android.content.pm.UserInfo;
import java.io.File;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IUserDataPreparerExt {
    default void destroyUserData(int i, int i2) {
    }

    default void prepareUserData(int i, int i2, int i3) {
    }

    default void reconcileUsers(String str, List<UserInfo> list, List<File> list2) {
    }
}
