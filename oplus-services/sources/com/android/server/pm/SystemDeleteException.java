package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class SystemDeleteException extends Exception {
    final PackageManagerException mReason;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemDeleteException(PackageManagerException packageManagerException) {
        this.mReason = packageManagerException;
    }
}
