package com.android.server.biometrics.sensors.fingerprint;

import android.hardware.fingerprint.IFingerprintClientActiveCallback;
import android.os.RemoteException;
import android.util.Slog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class GestureAvailabilityDispatcher {
    private static final String TAG = "GestureAvailabilityTracker";
    private boolean mIsActive;
    private final CopyOnWriteArrayList<IFingerprintClientActiveCallback> mClientActiveCallbacks = new CopyOnWriteArrayList<>();
    private final Map<Integer, Boolean> mActiveSensors = new HashMap();

    public boolean isAnySensorActive() {
        return this.mIsActive;
    }

    public void markSensorActive(int i, boolean z) {
        boolean z2;
        this.mActiveSensors.put(Integer.valueOf(i), Boolean.valueOf(z));
        boolean z3 = this.mIsActive;
        Iterator<Boolean> it = this.mActiveSensors.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                z2 = false;
                break;
            } else if (it.next().booleanValue()) {
                z2 = true;
                break;
            }
        }
        if (z3 != z2) {
            Slog.d(TAG, "Notifying gesture availability, active=" + this.mIsActive);
            this.mIsActive = z2;
            notifyClientActiveCallbacks(z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerCallback(IFingerprintClientActiveCallback iFingerprintClientActiveCallback) {
        this.mClientActiveCallbacks.add(iFingerprintClientActiveCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeCallback(IFingerprintClientActiveCallback iFingerprintClientActiveCallback) {
        this.mClientActiveCallbacks.remove(iFingerprintClientActiveCallback);
    }

    private void notifyClientActiveCallbacks(boolean z) {
        Iterator<IFingerprintClientActiveCallback> it = this.mClientActiveCallbacks.iterator();
        while (it.hasNext()) {
            IFingerprintClientActiveCallback next = it.next();
            try {
                next.onClientActiveChanged(z);
            } catch (RemoteException unused) {
                this.mClientActiveCallbacks.remove(next);
            }
        }
    }
}
