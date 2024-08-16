package com.android.server.pm;

import android.content.pm.parsing.PackageLite;
import android.os.UserHandle;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVerifyingSessionWrapper {
    default InstallSource getInstallSource() {
        return null;
    }

    default PackageLite getPackageLite() {
        return null;
    }

    default int getRet() {
        return 1;
    }

    default UserHandle getUser() {
        return null;
    }
}
