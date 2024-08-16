package android.os;

import android.os.IBinder;
import android.util.Log;
import vendor.oplus.hardware.power.powermonitor.IPowerMonitor;

/* loaded from: classes.dex */
public class OplusPowerMonitor {
    private static final boolean DEBUG = true;
    private static final String TAG = "PowerMonitor";
    private static volatile IPowerMonitor sService = null;

    private static IPowerMonitor getPowerMonitorService() {
        if (sService == null) {
            synchronized (OplusPowerMonitor.class) {
                if (sService == null) {
                    try {
                        IBinder binder = ServiceManager.getService("vendor.oplus.hardware.power.powermonitor.IPowerMonitor/default");
                        sService = IPowerMonitor.Stub.asInterface(binder);
                        if (sService != null) {
                            binder.linkToDeath(new IBinder.DeathRecipient() { // from class: android.os.OplusPowerMonitor$$ExternalSyntheticLambda0
                                @Override // android.os.IBinder.DeathRecipient
                                public final void binderDied() {
                                    OplusPowerMonitor.sService = null;
                                }
                            }, 0);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to get powermonitor hal service", e);
                    }
                }
            }
        }
        return sService;
    }

    public static void disableEdTask() {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service != null) {
                service.disableEdTask();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "disable ed task failed.", e);
        }
    }

    public static void enableEdTask() {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service != null) {
                service.enableEdTask();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "enable ed task failed.", e);
        }
    }

    public static int writeHoraeQmi(String buffer) {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service != null) {
                return service.writeHoraeQmi(buffer);
            }
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "write horae_qmi failed.", e);
            return 0;
        }
    }

    public static String getAtdTask() {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service == null) {
                return null;
            }
            String result = service.readAtdTask();
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "get abnormal task info failed.", e);
            return null;
        }
    }

    public static int setAtdEnable(int type) {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service == null) {
                return 0;
            }
            int ret = service.setAtdEnable(type);
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "set atdenable failed.", e);
            return 0;
        }
    }

    public static String getAtdStatus() {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service == null) {
                return null;
            }
            String result = service.getAtdEnable();
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "get atd status failed.", e);
            return null;
        }
    }

    public static int setAtdLevel(int type) {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service == null) {
                return 0;
            }
            int ret = service.setAtdLevel(type);
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "set atd level failed.", e);
            return 0;
        }
    }

    public static String getAtdLevel() {
        try {
            IPowerMonitor service = getPowerMonitorService();
            if (service == null) {
                return null;
            }
            String result = service.getAtdLevel();
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "get atd level failed.", e);
            return null;
        }
    }
}
