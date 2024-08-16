package com.android.server.pm;

import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class InstallPackageHelper$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ PackageManagerService f$0;

    public /* synthetic */ InstallPackageHelper$$ExternalSyntheticLambda1(PackageManagerService packageManagerService) {
        this.f$0 = packageManagerService;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return this.f$0.snapshotComputer();
    }
}
