package com.android.server.sensorprivacy;

import android.os.Handler;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.sensorprivacy.SensorPrivacyStateController;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SensorPrivacyStateControllerImpl extends SensorPrivacyStateController {
    private static final String SENSOR_PRIVACY_XML_FILE = "sensor_privacy_impl.xml";
    private static SensorPrivacyStateControllerImpl sInstance;
    private SensorPrivacyStateController.SensorPrivacyListener mListener;
    private Handler mListenerHandler;
    private PersistedState mPersistedState = PersistedState.fromFile(SENSOR_PRIVACY_XML_FILE);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SensorPrivacyStateController getInstance() {
        if (sInstance == null) {
            sInstance = new SensorPrivacyStateControllerImpl();
        }
        return sInstance;
    }

    private SensorPrivacyStateControllerImpl() {
        persistAll();
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    SensorState getStateLocked(int i, int i2, int i3) {
        SensorState state = this.mPersistedState.getState(i, i2, i3);
        if (state != null) {
            return new SensorState(state);
        }
        return getDefaultSensorState();
    }

    private static SensorState getDefaultSensorState() {
        return new SensorState(false);
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void setStateLocked(int i, int i2, int i3, boolean z, Handler handler, SensorPrivacyStateController.SetStateResultCallback setStateResultCallback) {
        SensorState state = this.mPersistedState.getState(i, i2, i3);
        if (state == null) {
            if (!z) {
                SensorPrivacyStateController.sendSetStateCallback(handler, setStateResultCallback, false);
                return;
            } else if (z) {
                SensorState sensorState = new SensorState(true);
                this.mPersistedState.setState(i, i2, i3, sensorState);
                notifyStateChangeLocked(i, i2, i3, sensorState);
                SensorPrivacyStateController.sendSetStateCallback(handler, setStateResultCallback, true);
                return;
            }
        }
        if (state.setEnabled(z)) {
            notifyStateChangeLocked(i, i2, i3, state);
            SensorPrivacyStateController.sendSetStateCallback(handler, setStateResultCallback, true);
        } else {
            SensorPrivacyStateController.sendSetStateCallback(handler, setStateResultCallback, false);
        }
    }

    private void notifyStateChangeLocked(int i, int i2, int i3, SensorState sensorState) {
        Handler handler = this.mListenerHandler;
        if (handler != null && this.mListener != null) {
            handler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl$$ExternalSyntheticLambda0
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((SensorPrivacyStateController.SensorPrivacyListener) obj).onSensorPrivacyChanged(((Integer) obj2).intValue(), ((Integer) obj3).intValue(), ((Integer) obj4).intValue(), (SensorState) obj5);
                }
            }, this.mListener, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), new SensorState(sensorState)));
        }
        schedulePersistLocked();
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void setSensorPrivacyListenerLocked(Handler handler, SensorPrivacyStateController.SensorPrivacyListener sensorPrivacyListener) {
        Objects.requireNonNull(handler);
        Objects.requireNonNull(sensorPrivacyListener);
        if (this.mListener != null) {
            throw new IllegalStateException("Listener is already set");
        }
        this.mListener = sensorPrivacyListener;
        this.mListenerHandler = handler;
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void schedulePersistLocked() {
        this.mPersistedState.schedulePersist();
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void forEachStateLocked(final SensorPrivacyStateController.SensorPrivacyStateConsumer sensorPrivacyStateConsumer) {
        PersistedState persistedState = this.mPersistedState;
        Objects.requireNonNull(sensorPrivacyStateConsumer);
        persistedState.forEachKnownState(new QuadConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl$$ExternalSyntheticLambda1
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                SensorPrivacyStateController.SensorPrivacyStateConsumer.this.accept(((Integer) obj).intValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue(), (SensorState) obj4);
            }
        });
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void resetForTestingImpl() {
        this.mPersistedState.resetForTesting();
        this.mListener = null;
        this.mListenerHandler = null;
        sInstance = null;
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void dumpLocked(DualDumpOutputStream dualDumpOutputStream) {
        this.mPersistedState.dump(dualDumpOutputStream);
    }
}
