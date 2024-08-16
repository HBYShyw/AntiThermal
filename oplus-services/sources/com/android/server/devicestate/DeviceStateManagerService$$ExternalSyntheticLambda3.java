package com.android.server.devicestate;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final /* synthetic */ class DeviceStateManagerService$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ OverrideRequestController f$0;

    public /* synthetic */ DeviceStateManagerService$$ExternalSyntheticLambda3(OverrideRequestController overrideRequestController) {
        this.f$0 = overrideRequestController;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.cancelRequest((OverrideRequest) obj);
    }
}
