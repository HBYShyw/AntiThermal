package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageManagerServiceCompilerMappingExt {
    default boolean checkPropertiesForOplus(int i) {
        return false;
    }

    default boolean getAndCheckValidityForOplus(int i) {
        return false;
    }

    default String[] modifyReasonList(String[] strArr) {
        return strArr;
    }
}
