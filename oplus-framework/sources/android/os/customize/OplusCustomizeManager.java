package android.os.customize;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.customize.IOplusCustomizeService;
import android.util.ArrayMap;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusCustomizeManager {
    public static final String SERVICE_NAME = "opluscustomize";
    private static final String TAG = "OplusCustomizeManager";
    private static volatile OplusCustomizeManager sInstance;
    private IOplusCustomizeService mCustService;
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static final ArrayMap<String, IBinder> mDeviceManagerCache = new ArrayMap<>();

    private OplusCustomizeManager() {
    }

    public static final OplusCustomizeManager getInstance() {
        OplusCustomizeManager oplusCustomizeManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeManager();
                }
                oplusCustomizeManager = sInstance;
            }
            return oplusCustomizeManager;
        }
        return sInstance;
    }

    public boolean isPlatformSigned(int uid) {
        getOplusCustomizeManagerService();
        IOplusCustomizeService iOplusCustomizeService = this.mCustService;
        if (iOplusCustomizeService != null) {
            try {
                boolean ret = iOplusCustomizeService.isPlatformSigned(uid);
                return ret;
            } catch (RemoteException e) {
                Log.i(TAG, "getOplusMdmManager error!");
                e.printStackTrace();
                return false;
            }
        }
        throw new SecurityException("OplusCustomizeService is not ready");
    }

    public void checkPermission() throws SecurityException {
        getOplusCustomizeManagerService();
        IOplusCustomizeService iOplusCustomizeService = this.mCustService;
        if (iOplusCustomizeService != null) {
            try {
                iOplusCustomizeService.checkPermission();
                return;
            } catch (RemoteException e) {
                Log.i(TAG, "getOplusMdmManager error!");
                e.printStackTrace();
                return;
            }
        }
        throw new SecurityException("OplusCustomizeService is not ready");
    }

    public boolean isOTAUpdated() throws SecurityException {
        getOplusCustomizeManagerService();
        IOplusCustomizeService iOplusCustomizeService = this.mCustService;
        if (iOplusCustomizeService != null) {
            try {
                boolean ret = iOplusCustomizeService.isOTAUpdated();
                return ret;
            } catch (RemoteException e) {
                Log.i(TAG, "getOplusMdmManager error!");
                e.printStackTrace();
                return false;
            }
        }
        throw new SecurityException("OplusCustomizeService is not ready");
    }

    private IOplusCustomizeService getOplusCustomizeManagerService() {
        IOplusCustomizeService iOplusCustomizeService;
        synchronized (mServiceLock) {
            if (this.mCustService == null) {
                this.mCustService = IOplusCustomizeService.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
            }
            iOplusCustomizeService = this.mCustService;
        }
        return iOplusCustomizeService;
    }

    public boolean isSimUnlockedState() {
        getOplusCustomizeManagerService();
        IOplusCustomizeService iOplusCustomizeService = this.mCustService;
        if (iOplusCustomizeService != null) {
            try {
                boolean ret = iOplusCustomizeService.isSimUnlockedState();
                return ret;
            } catch (RemoteException e) {
                Log.i(TAG, "isSimUnlockedState error!");
                e.printStackTrace();
                return false;
            }
        }
        throw new SecurityException("OplusCustomizeService is not ready");
    }

    public final IBinder getDeviceManagerServiceByName(String strManagerName) {
        ArrayMap<String, IBinder> arrayMap = mDeviceManagerCache;
        synchronized (arrayMap) {
            getOplusCustomizeManagerService();
            if (this.mCustService != null) {
                try {
                    if (arrayMap.containsKey(strManagerName)) {
                        return arrayMap.get(strManagerName);
                    }
                    IBinder manager = this.mCustService.getDeviceManagerServiceByName(strManagerName);
                    if (manager != null) {
                        arrayMap.put(strManagerName, manager);
                    }
                    return manager;
                } catch (RemoteException e) {
                    Log.i(TAG, "getOplusMdmManager error!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
