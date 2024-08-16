package com.android.server.pm;

import android.content.pm.UserInfo;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageSettingExt {
    default void afterCreateWithoutOriginInCreateNewSetting(int i, boolean z, List<UserInfo> list) {
    }

    default void afterSetForNonSysAppInCreateNewSetting(UserInfo userInfo) {
    }

    default boolean interceptSetInstalledInUpdatePackageSetting(UserInfo userInfo) {
        return false;
    }
}
