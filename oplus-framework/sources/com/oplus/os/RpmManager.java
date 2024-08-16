package com.oplus.os;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import vendor.oplus.hardware.rpmh.IRpmh;
import vendor.oplus.hardware.rpmh.PowerStateSubsystemSleepState;

/* loaded from: classes.dex */
public class RpmManager implements IBinder.DeathRecipient {
    private static final int MAX_TRACE_DEPTH = 5;
    private static final String TAG = "RpmManager";
    private static volatile RpmManager sRpm = null;
    private IRpmh mRpmh;

    private RpmManager() {
        IBinder binder = ServiceManager.getService(IRpmh.DESCRIPTOR + "/default");
        if (binder == null) {
            return;
        }
        try {
            binder.linkToDeath(this, 0);
        } catch (RemoteException e) {
            Log.d(TAG, "get Rpmh service exception " + e.getMessage());
            StackTraceElement[] traces = e.getStackTrace();
            for (int i = 0; i < 5 && i < traces.length; i++) {
                Log.d(TAG, "get Rpmh service exception " + traces[i]);
            }
            this.mRpmh = null;
        }
        this.mRpmh = IRpmh.Stub.asInterface(binder);
    }

    public static RpmManager getInstance() {
        if (sRpm == null || !sRpm.isAvailable()) {
            synchronized (RpmManager.class) {
                if (sRpm == null || !sRpm.isAvailable()) {
                    sRpm = new RpmManager();
                }
            }
        }
        return sRpm;
    }

    public boolean isAvailable() {
        return this.mRpmh != null;
    }

    public ArrayList<SubsystemSleepState> getPowerStateSubsystemSleepStateList() {
        if (this.mRpmh == null) {
            return null;
        }
        ArrayList<PowerStateSubsystemSleepState> powerState = new ArrayList<>();
        ArrayList<SubsystemSleepState> powerStateProxy = new ArrayList<>();
        try {
            this.mRpmh.getPowerStateSubsystemSleepStateList(powerState);
            Iterator<PowerStateSubsystemSleepState> it = powerState.iterator();
            while (it.hasNext()) {
                PowerStateSubsystemSleepState p = it.next();
                SubsystemSleepState proxy = convetToPowerStateSubsystemSleepState(p);
                powerStateProxy.add(proxy);
            }
        } catch (RemoteException e) {
            Log.d(TAG, "getPowerStateSubsystemSleepStateList exception " + e.getMessage());
            StackTraceElement[] traces = e.getStackTrace();
            for (int i = 0; i < 5 && i < traces.length; i++) {
                Log.d(TAG, "getPowerStateSubsystemSleepStateList exception " + traces[i]);
            }
        }
        return powerStateProxy;
    }

    public SubsystemSleepState getPowerStateSubsystemSleepState(String name) {
        if (this.mRpmh == null) {
            return null;
        }
        PowerStateSubsystemSleepState powerState = new PowerStateSubsystemSleepState();
        SubsystemSleepState proxy = new SubsystemSleepState();
        try {
            this.mRpmh.getPowerStateSubsystemSleepState(name, powerState);
            return convetToPowerStateSubsystemSleepState(powerState);
        } catch (RemoteException e) {
            Log.d(TAG, "getPowerStateSubsystemSleepState exception " + e.getMessage());
            StackTraceElement[] traces = e.getStackTrace();
            for (int i = 0; i < 5 && i < traces.length; i++) {
                Log.d(TAG, "getPowerStateSubsystemSleepState exception " + traces[i]);
            }
            return proxy;
        }
    }

    private static SubsystemSleepState convetToPowerStateSubsystemSleepState(PowerStateSubsystemSleepState state) {
        SubsystemSleepState subsystemSleepState = new SubsystemSleepState();
        subsystemSleepState.name = state.name;
        subsystemSleepState.version = state.version;
        subsystemSleepState.residencyInMsecSinceBoot = state.residencyInMsecSinceBoot;
        subsystemSleepState.totalTransitions = state.totalTransitions;
        subsystemSleepState.lastEntryTimestampMs = state.lastEntryTimestampMs;
        subsystemSleepState.supportedOnlyInSuspend = state.supportedOnlyInSuspend;
        return subsystemSleepState;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Log.e(TAG, "Rpmh service died.");
        synchronized (RpmManager.class) {
            this.mRpmh = null;
            sRpm = null;
        }
    }
}
