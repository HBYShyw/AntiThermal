package com.android.server.pm;

import android.content.pm.ISessionParamsExt;
import android.content.pm.PackageInstaller;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInstallParamsExt {
    default int getExtraDextopFlags() {
        return 0;
    }

    default int getExtraInstallFlags() {
        return 0;
    }

    default String getExtraSessionInfo() {
        return null;
    }

    default int getInstallerUid() {
        return -1;
    }

    default PackageInstaller.SessionParams getSessionParams() {
        return null;
    }

    default void init(ISessionParamsExt iSessionParamsExt, int i, PackageInstaller.SessionParams sessionParams) {
    }

    default int setExtraDextopFlags(int i) {
        return 0;
    }

    default void setInstallerUid(int i) {
    }

    default void setSessionParams(PackageInstaller.SessionParams sessionParams) {
    }
}
