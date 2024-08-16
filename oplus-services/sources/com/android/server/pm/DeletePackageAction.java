package com.android.server.pm;

import android.os.UserHandle;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DeletePackageAction {
    public final PackageSetting mDeletingPs;
    public final PackageSetting mDisabledPs;
    public final int mFlags;
    public final PackageRemovedInfo mRemovedInfo;
    public final UserHandle mUser;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeletePackageAction(PackageSetting packageSetting, PackageSetting packageSetting2, PackageRemovedInfo packageRemovedInfo, int i, UserHandle userHandle) {
        this.mDeletingPs = packageSetting;
        this.mDisabledPs = packageSetting2;
        this.mRemovedInfo = packageRemovedInfo;
        this.mFlags = i;
        this.mUser = userHandle;
    }
}
