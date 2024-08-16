package com.android.server.pm;

import com.android.server.pm.ApkChecksums;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class PackageManagerService$$ExternalSyntheticLambda41 implements ApkChecksums.Injector.Producer {
    public final /* synthetic */ PackageManagerServiceInjector f$0;

    public /* synthetic */ PackageManagerService$$ExternalSyntheticLambda41(PackageManagerServiceInjector packageManagerServiceInjector) {
        this.f$0 = packageManagerServiceInjector;
    }

    @Override // com.android.server.pm.ApkChecksums.Injector.Producer
    public final Object produce() {
        return this.f$0.getIncrementalManager();
    }
}
