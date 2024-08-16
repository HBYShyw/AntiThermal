package com.android.server.pm;

import android.util.ExceptionUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class PrepareFailure extends PackageManagerException {
    public String mConflictingPackage;
    public String mConflictingPermission;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PrepareFailure(int i) {
        super(i, "Failed to prepare for install.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PrepareFailure(int i, String str) {
        super(i, str);
    }

    public static PrepareFailure ofInternalError(String str, int i) {
        return new PrepareFailure(-110, str, i);
    }

    private PrepareFailure(int i, String str, int i2) {
        super(i, str, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PrepareFailure(String str, Exception exc) {
        super(((PackageManagerException) exc).error, ExceptionUtils.getCompleteMessage(str, exc));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PrepareFailure conflictsWithExistingPermission(String str, String str2) {
        this.mConflictingPermission = str;
        this.mConflictingPackage = str2;
        return this;
    }
}
