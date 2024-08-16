package android.location;

import android.location.IOplusLocationManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.List;

/* loaded from: classes.dex */
public class OplusLocationManager {
    private final String TAG = "OplusLocationManager";

    public List<String> getInUsePackagesList() {
        try {
            IOplusLocationManager oplusLocMgr = IOplusLocationManager.Stub.asInterface(ServiceManager.getService("OplusLocationManager"));
            if (oplusLocMgr == null) {
                return null;
            }
            List<String> result = oplusLocMgr.getInUsePackagesList();
            return result;
        } catch (RemoteException e) {
            Log.e("OplusLocationManager", "Running getPackageList error!");
            return null;
        }
    }

    public void setDebugOn() {
        try {
            IOplusLocationManager oplusLocMgr = IOplusLocationManager.Stub.asInterface(ServiceManager.getService("OplusLocationManager"));
            if (oplusLocMgr != null) {
                oplusLocMgr.setDebugOn();
            }
        } catch (RemoteException e) {
            Log.e("OplusLocationManager", "Running setDebugOn error!");
        }
    }

    public void setDebugOff() {
        try {
            IOplusLocationManager oplusLocMgr = IOplusLocationManager.Stub.asInterface(ServiceManager.getService("OplusLocationManager"));
            if (oplusLocMgr != null) {
                oplusLocMgr.setDebugOff();
            }
        } catch (RemoteException e) {
            Log.e("OplusLocationManager", "Running setDebugOff error!");
        }
    }

    public void setDebugDump() {
        try {
            IOplusLocationManager oplusLocMgr = IOplusLocationManager.Stub.asInterface(ServiceManager.getService("OplusLocationManager"));
            if (oplusLocMgr != null) {
                oplusLocMgr.setDebugDump();
            }
        } catch (RemoteException e) {
            Log.e("OplusLocationManager", "Running setDebugDump error!");
        }
    }

    public boolean freezeLocationProcess(String pkg, boolean isFreeze, int uid) {
        try {
            IOplusLocationManager oplusLocMgr = IOplusLocationManager.Stub.asInterface(ServiceManager.getService("OplusLocationManager"));
            if (oplusLocMgr != null) {
                return oplusLocMgr.freezeLocationProcess(pkg, isFreeze, uid);
            }
            return false;
        } catch (RemoteException e) {
            Log.e("OplusLocationManager", "Running freezeLocationProcess error!");
            return false;
        }
    }
}
