package com.android.server.biometrics.log;

import com.android.internal.logging.InstanceId;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BiometricContextSessionInfo {
    private final InstanceId mId;
    private final AtomicInteger mOrder = new AtomicInteger(0);

    /* JADX INFO: Access modifiers changed from: package-private */
    public BiometricContextSessionInfo(InstanceId instanceId) {
        this.mId = instanceId;
    }

    public int getId() {
        return this.mId.getId();
    }

    public int getOrder() {
        return this.mOrder.get();
    }

    public int getOrderAndIncrement() {
        return this.mOrder.getAndIncrement();
    }

    public String toString() {
        return "[sid: " + this.mId.getId() + "]";
    }
}
