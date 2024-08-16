package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class ReconcileFailure extends PackageManagerException {
    public static ReconcileFailure ofInternalError(String str, int i) {
        return new ReconcileFailure(str, i);
    }

    private ReconcileFailure(String str, int i) {
        super(-110, "Reconcile failed: " + str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReconcileFailure(int i, String str) {
        super(i, "Reconcile failed: " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReconcileFailure(PackageManagerException packageManagerException) {
        this(packageManagerException.error, packageManagerException.getMessage());
    }
}
