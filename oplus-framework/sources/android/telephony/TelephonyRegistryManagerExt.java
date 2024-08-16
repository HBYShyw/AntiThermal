package android.telephony;

import android.content.Context;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.TelephonyCallbackExt;
import android.util.ArraySet;
import com.android.internal.telephony.ITelephonyRegistryExt;
import com.oplus.virtualcomm.VirtualCommServiceState;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.ToIntFunction;

/* loaded from: classes.dex */
public class TelephonyRegistryManagerExt {
    private static final String TAG = "TelephonyRegistryManagerExt";
    public static final String TELEPHONY_REGISTRY_EXT = "telephony_registry_ext";
    private static ITelephonyRegistryExt sRegistry;
    private final Context mContext;

    public TelephonyRegistryManagerExt(Context context) {
        this.mContext = context;
        if (sRegistry == null) {
            sRegistry = ITelephonyRegistryExt.Stub.asInterface(ServiceManager.getService(TELEPHONY_REGISTRY_EXT));
        }
    }

    private boolean listenFromCallback(boolean renounceFineLocationAccess, boolean renounceCoarseLocationAccess, int slotId, String pkg, String featureId, TelephonyCallbackExt telephonyCallback, int[] events, boolean notifyNow) {
        try {
            try {
                return sRegistry.listenWithEventList(renounceFineLocationAccess, renounceCoarseLocationAccess, slotId, pkg, featureId, telephonyCallback.callback, events, notifyNow);
            } catch (RemoteException e) {
                return false;
            }
        } catch (RemoteException e2) {
            return false;
        }
    }

    private Set<Integer> getEventsFromCallback(TelephonyCallbackExt telephonyCallback) {
        Set<Integer> eventList = new ArraySet<>();
        if (telephonyCallback instanceof TelephonyCallbackExt.PlmnCarrierConfigListener) {
            eventList.add(1);
        }
        if (telephonyCallback instanceof TelephonyCallbackExt.NRStatusChangedListener) {
            eventList.add(5);
        }
        if (telephonyCallback instanceof TelephonyCallbackExt.VirtualCommEnabledListener) {
            eventList.add(2);
        }
        if (telephonyCallback instanceof TelephonyCallbackExt.VirtualCommServiceStateListener) {
            eventList.add(3);
        }
        if (telephonyCallback instanceof TelephonyCallbackExt.ImsRemainTimeListener) {
            eventList.add(4);
        }
        if (telephonyCallback instanceof TelephonyCallbackExt.SimRecoveryListener) {
            eventList.add(7);
        }
        if (telephonyCallback instanceof TelephonyCallbackExt.PollCsPsRspListener) {
            eventList.add(6);
        }
        if (telephonyCallback instanceof TelephonyCallbackExt.RealtimeOosListener) {
            eventList.add(8);
        }
        return eventList;
    }

    public boolean registerTelephonyCallbackExt(boolean renounceFineLocationAccess, boolean renounceCoarseLocationAccess, Executor executor, int slotId, String pkgName, String attributionTag, TelephonyCallbackExt callback, boolean notifyNow) {
        if (callback != null) {
            callback.init(executor);
            return listenFromCallback(renounceFineLocationAccess, renounceCoarseLocationAccess, slotId, pkgName, attributionTag, callback, getEventsFromCallback(callback).stream().mapToInt(new ToIntFunction() { // from class: android.telephony.TelephonyRegistryManagerExt$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    int intValue;
                    intValue = ((Integer) obj).intValue();
                    return intValue;
                }
            }).toArray(), notifyNow);
        }
        Rlog.e(TAG, "callback is null");
        return false;
    }

    public boolean unregisterTelephonyCallbackExt(int slotId, String pkgName, String attributionTag, TelephonyCallbackExt callback, boolean notifyNow) {
        return listenFromCallback(false, false, slotId, pkgName, attributionTag, callback, new int[0], notifyNow);
    }

    public void notifyPlmnCarrierConfigChanged(int slotIndex, PersistableBundle result) {
        try {
            sRegistry.notifyPlmnCarrierConfigChanged(slotIndex, result);
        } catch (RemoteException e) {
        }
    }

    public void notifyNRIconTypeChanged(int slotIndex, int type) {
        try {
            sRegistry.notifyNRIconTypeChanged(slotIndex, type);
        } catch (RemoteException e) {
        }
    }

    public void notifyForRemainTimeReported(int phoneId, String remainTimeData) {
        try {
            sRegistry.notifyForRemainTimeReported(phoneId, remainTimeData);
        } catch (RemoteException e) {
        }
    }

    public void notifyVirtualCommEnabledChanged(boolean[] enabled) {
        Rlog.e(TAG, "notifyVirtualCommEnabledChanged:");
        try {
            sRegistry.notifyVirtualCommEnabledChanged(enabled);
        } catch (RemoteException e) {
        }
    }

    public void notifyVirtualCommServiceStateChanged(VirtualCommServiceState serviceState) {
        Rlog.e(TAG, "notifyVirtualCommServiceStateChanged:" + serviceState);
        try {
            sRegistry.notifyVirtualCommServiceStateChanged(serviceState);
        } catch (RemoteException e) {
        }
    }

    public void notifyPollCsPsInService(int slotId, int domain) {
        Rlog.d(TAG, "notifyPollCsPsInService slotId = " + slotId + ", domain = " + domain);
        try {
            sRegistry.notifyPollCsPsInService(slotId, domain);
        } catch (RemoteException e) {
        }
    }

    public void notifySimRecovery(int slotId, int stat) {
        Rlog.d(TAG, "notifySimRecovery slotId = " + slotId + ", stat = " + stat);
        try {
            sRegistry.notifySimRecovery(slotId, stat);
        } catch (RemoteException e) {
        }
    }

    public void notifyRealtimeOos(int slotId, boolean oos) {
        Rlog.d(TAG, "notifyRealtimeOos slotId = " + slotId + ", oos = " + oos);
        try {
            sRegistry.notifyRealtimeOos(slotId, oos);
        } catch (RemoteException e) {
        }
    }
}
