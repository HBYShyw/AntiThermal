package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInstallArgsExt {
    default int getExtraDextopFlags() {
        return 0;
    }

    default int getExtraInstallFlags() {
        return 0;
    }

    default String getExtraSessionInfo() {
        return null;
    }

    default String getPackageName() {
        return null;
    }

    default void init(IInstallParamsExt iInstallParamsExt) {
    }

    default int setExtraDextopFlags(int i) {
        return 0;
    }

    default void setPackageName(String str) {
    }
}
