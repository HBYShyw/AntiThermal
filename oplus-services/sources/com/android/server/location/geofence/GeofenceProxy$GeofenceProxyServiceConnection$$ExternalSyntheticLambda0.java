package com.android.server.location.geofence;

import android.os.IBinder;
import com.android.server.servicewatcher.ServiceWatcher;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class GeofenceProxy$GeofenceProxyServiceConnection$$ExternalSyntheticLambda0 implements ServiceWatcher.BinderOperation {
    public final /* synthetic */ GeofenceProxy f$0;

    @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
    public final void run(IBinder iBinder) {
        this.f$0.updateGeofenceHardware(iBinder);
    }
}
