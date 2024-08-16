package com.android.server.sensorprivacy;

import android.os.Handler;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.sensorprivacy.SensorPrivacyStateController;
import java.util.function.BiConsumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class SensorPrivacyStateController {
    private static SensorPrivacyStateController sInstance;
    AllSensorStateController mAllSensorStateController = AllSensorStateController.getInstance();
    private final Object mLock = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface AllSensorPrivacyListener {
        void onAllSensorPrivacyChanged(boolean z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SensorPrivacyListener {
        void onSensorPrivacyChanged(int i, int i2, int i3, SensorState sensorState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SensorPrivacyStateConsumer {
        void accept(int i, int i2, int i3, SensorState sensorState);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    interface SetStateResultCallback {
        void callback(boolean z);
    }

    @GuardedBy({"mLock"})
    abstract void dumpLocked(DualDumpOutputStream dualDumpOutputStream);

    @GuardedBy({"mLock"})
    abstract void forEachStateLocked(SensorPrivacyStateConsumer sensorPrivacyStateConsumer);

    @GuardedBy({"mLock"})
    abstract SensorState getStateLocked(int i, int i2, int i3);

    abstract void resetForTestingImpl();

    @GuardedBy({"mLock"})
    abstract void schedulePersistLocked();

    @GuardedBy({"mLock"})
    abstract void setSensorPrivacyListenerLocked(Handler handler, SensorPrivacyListener sensorPrivacyListener);

    @GuardedBy({"mLock"})
    abstract void setStateLocked(int i, int i2, int i3, boolean z, Handler handler, SetStateResultCallback setStateResultCallback);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SensorPrivacyStateController getInstance() {
        if (sInstance == null) {
            sInstance = SensorPrivacyStateControllerImpl.getInstance();
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SensorState getState(int i, int i2, int i3) {
        SensorState stateLocked;
        synchronized (this.mLock) {
            stateLocked = getStateLocked(i, i2, i3);
        }
        return stateLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setState(int i, int i2, int i3, boolean z, Handler handler, SetStateResultCallback setStateResultCallback) {
        synchronized (this.mLock) {
            setStateLocked(i, i2, i3, z, handler, setStateResultCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSensorPrivacyListener(Handler handler, SensorPrivacyListener sensorPrivacyListener) {
        synchronized (this.mLock) {
            setSensorPrivacyListenerLocked(handler, sensorPrivacyListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getAllSensorState() {
        boolean allSensorStateLocked;
        synchronized (this.mLock) {
            allSensorStateLocked = this.mAllSensorStateController.getAllSensorStateLocked();
        }
        return allSensorStateLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAllSensorState(boolean z) {
        synchronized (this.mLock) {
            this.mAllSensorStateController.setAllSensorStateLocked(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAllSensorPrivacyListener(Handler handler, AllSensorPrivacyListener allSensorPrivacyListener) {
        synchronized (this.mLock) {
            this.mAllSensorStateController.setAllSensorPrivacyListenerLocked(handler, allSensorPrivacyListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void persistAll() {
        synchronized (this.mLock) {
            this.mAllSensorStateController.schedulePersistLocked();
            schedulePersistLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachState(SensorPrivacyStateConsumer sensorPrivacyStateConsumer) {
        synchronized (this.mLock) {
            forEachStateLocked(sensorPrivacyStateConsumer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(DualDumpOutputStream dualDumpOutputStream) {
        synchronized (this.mLock) {
            this.mAllSensorStateController.dumpLocked(dualDumpOutputStream);
            dumpLocked(dualDumpOutputStream);
        }
        dualDumpOutputStream.flush();
    }

    public void atomic(Runnable runnable) {
        synchronized (this.mLock) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendSetStateCallback(Handler handler, SetStateResultCallback setStateResultCallback, boolean z) {
        handler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyStateController$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((SensorPrivacyStateController.SetStateResultCallback) obj).callback(((Boolean) obj2).booleanValue());
            }
        }, setStateResultCallback, Boolean.valueOf(z)));
    }

    void resetForTesting() {
        this.mAllSensorStateController.resetForTesting();
        resetForTestingImpl();
        sInstance = null;
    }
}
