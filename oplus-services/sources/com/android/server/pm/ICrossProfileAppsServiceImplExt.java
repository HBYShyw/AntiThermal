package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ICrossProfileAppsServiceImplExt {
    default boolean interceptInSetInteractAcrossProfilesAppOpForProfileOrThrow(int i, String str) {
        return false;
    }

    default boolean skipProfileInGetTargetUserProfilesUnchecked(int i, String str) {
        return false;
    }
}
