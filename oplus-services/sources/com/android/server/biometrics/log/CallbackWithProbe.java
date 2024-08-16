package com.android.server.biometrics.log;

import com.android.server.biometrics.log.Probe;
import com.android.server.biometrics.sensors.BaseClientMonitor;
import com.android.server.biometrics.sensors.ClientMonitorCallback;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CallbackWithProbe<T extends Probe> implements ClientMonitorCallback {
    private final T mProbe;
    private final boolean mStartWithClient;

    public CallbackWithProbe(T t, boolean z) {
        this.mProbe = t;
        this.mStartWithClient = z;
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public void onClientStarted(BaseClientMonitor baseClientMonitor) {
        if (this.mStartWithClient) {
            this.mProbe.enable();
        }
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z) {
        this.mProbe.destroy();
    }

    public T getProbe() {
        return this.mProbe;
    }
}
