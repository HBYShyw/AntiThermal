package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface PackageSessionProvider {
    GentleUpdateHelper getGentleUpdateHelper();

    PackageInstallerSession getSession(int i);

    PackageSessionVerifier getSessionVerifier();
}
