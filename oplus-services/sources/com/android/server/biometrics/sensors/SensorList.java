package com.android.server.biometrics.sensors;

import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.os.RemoteException;
import android.util.Slog;
import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SensorList<T> {
    private static final String TAG = "SensorList";
    private final IActivityManager mActivityManager;
    private final SparseArray<T> mSensors = new SparseArray<>();

    public SensorList(IActivityManager iActivityManager) {
        this.mActivityManager = iActivityManager;
    }

    public void addSensor(int i, T t, int i2, SynchronousUserSwitchObserver synchronousUserSwitchObserver) {
        this.mSensors.put(i, t);
        registerUserSwitchObserver(i2, synchronousUserSwitchObserver);
    }

    private void registerUserSwitchObserver(int i, SynchronousUserSwitchObserver synchronousUserSwitchObserver) {
        try {
            this.mActivityManager.registerUserSwitchObserver(synchronousUserSwitchObserver, TAG);
            if (i == -10000) {
                synchronousUserSwitchObserver.onUserSwitching(0);
            }
        } catch (RemoteException unused) {
            Slog.e(TAG, "Unable to register user switch observer");
        }
    }

    public T valueAt(int i) {
        return this.mSensors.valueAt(i);
    }

    public T get(int i) {
        return this.mSensors.get(i);
    }

    public int keyAt(int i) {
        return this.mSensors.keyAt(i);
    }

    public int size() {
        return this.mSensors.size();
    }

    public boolean contains(int i) {
        return this.mSensors.contains(i);
    }
}
