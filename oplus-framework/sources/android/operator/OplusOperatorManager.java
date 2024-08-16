package android.operator;

import android.operator.IOplusCotaObserverDelegate;
import android.operator.IOplusOperatorManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.OplusPropertyList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusOperatorManager {
    public static final String ACTION_GET_COTA_IMAGE_PATH = "getCotaImagePath";
    public static final String ACTION_SAVE_COTA_VERSION_INFO = "saveCotaVersionInfo";

    @Deprecated
    public static final boolean SERVICE_ENABLED = true;
    public static final String SERVICE_NAME = "operator";
    private static final String TAG = "OplusOperatorManager";
    private final ArrayList<CotaObserverDelegate> mDelegates = new ArrayList<>();
    private IOplusOperatorManager mService;
    private static volatile OplusOperatorManager sManager = null;
    private static final Map mEmptyMap = Collections.emptyMap();

    /* loaded from: classes.dex */
    public static abstract class CotaObserver {
        public abstract void onCotaExeFinish(int i, int i2, Bundle bundle);
    }

    private OplusOperatorManager(IOplusOperatorManager service) {
        this.mService = service;
    }

    public static OplusOperatorManager getInstance() {
        if (sManager == null) {
            synchronized (OplusOperatorManager.class) {
                if (sManager == null) {
                    IOplusOperatorManager mService = IOplusOperatorManager.Stub.asInterface(ServiceManager.getService("operator"));
                    if (mService != null) {
                        sManager = new OplusOperatorManager(mService);
                    } else {
                        Slog.e(TAG, "Whoops, service not initiated yet , print caller stack " + Debug.getCallers(9));
                        return null;
                    }
                }
            }
        }
        return sManager;
    }

    /* loaded from: classes.dex */
    static class CotaObserverDelegate extends IOplusCotaObserverDelegate.Stub {
        final CotaObserver mObserver;

        CotaObserverDelegate(CotaObserver observer) {
            this.mObserver = observer;
        }

        @Override // android.operator.IOplusCotaObserverDelegate
        public void cotaExeResultSend(int type, int statue, Bundle bundle) {
            this.mObserver.onCotaExeFinish(type, statue, bundle);
        }
    }

    public boolean registerCotaObserver(CotaObserver observer) {
        boolean result;
        synchronized (this.mDelegates) {
            result = false;
            CotaObserverDelegate delegate = new CotaObserverDelegate(observer);
            try {
                IOplusOperatorManager iOplusOperatorManager = this.mService;
                if (iOplusOperatorManager != null) {
                    result = iOplusOperatorManager.registerCotaObserverDelegate(delegate);
                }
                this.mDelegates.add(delegate);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return result;
    }

    public void unregisterCotaObserver(CotaObserver observer) {
        synchronized (this.mDelegates) {
            Iterator<CotaObserverDelegate> i = this.mDelegates.iterator();
            while (i.hasNext()) {
                CotaObserverDelegate delegate = i.next();
                if (delegate.mObserver == observer) {
                    try {
                        IOplusOperatorManager iOplusOperatorManager = this.mService;
                        if (iOplusOperatorManager != null) {
                            iOplusOperatorManager.unregisterCotaObserverDelegate(delegate);
                        }
                        i.remove();
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }
    }

    public void testAidl() {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager != null) {
                iOplusOperatorManager.testAidl();
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "testAidl ", ex);
        }
    }

    public Map getConfigMap(String config) {
        Map configMap = null;
        try {
            try {
                if (this.mService == null) {
                    return null;
                }
                long startTime = System.currentTimeMillis();
                Bundle bundle = new Bundle();
                bundle.putString("config", config);
                configMap = this.mService.getConfigMap(bundle);
                long consumingTime = System.currentTimeMillis() - startTime;
                Log.i(TAG, "getConfigMap " + config + " took " + consumingTime + "ms");
                return configMap;
            } catch (RemoteException ex) {
                Log.e(TAG, "getConfigMap ", ex);
                return configMap;
            }
        } catch (Throwable th) {
            return configMap;
        }
    }

    public void grantCustomizedRuntimePermissions() {
        try {
            if (this.mService != null) {
                long startTime = System.currentTimeMillis();
                this.mService.grantCustomizedRuntimePermissions();
                long consumingTime = System.currentTimeMillis() - startTime;
                Log.i(TAG, "grantCustomizedPermissions  took " + consumingTime + "ms");
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "grantCustomizedPermissions ", ex);
        }
    }

    public void notifySmartCustomizationStart() {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager != null) {
                iOplusOperatorManager.notifySmartCustomizationStart();
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "notifySmartCustomizationStart ", ex);
        }
    }

    public boolean isInSimTriggeredSystemBlackList(String pkgName) {
        try {
            try {
                IOplusOperatorManager iOplusOperatorManager = this.mService;
                if (iOplusOperatorManager == null) {
                    return false;
                }
                boolean result = iOplusOperatorManager.isInSimTriggeredSystemBlackList(pkgName);
                return result;
            } catch (RemoteException ex) {
                Log.e(TAG, "isInSimTriggeredSystemBlackList " + pkgName, ex);
                return false;
            }
        } catch (Throwable th) {
            return false;
        }
    }

    public String getActiveSimOperator() {
        return SystemProperties.get(OplusPropertyList.PROPERTY_OPERATOR_SERVICE_OPTA);
    }

    public String getActiveSimRegion() {
        return SystemProperties.get(OplusPropertyList.PROPERTY_OPERATOR_SERVICE_OPTB);
    }

    public void notifySimSwitch(Bundle data) {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager != null) {
                iOplusOperatorManager.notifySimSwitch(data);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "notifiySimSwitch ", ex);
        }
    }

    public void notifyRegionSwitch(Bundle data) {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager != null) {
                iOplusOperatorManager.notifyRegionSwitch(data);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "notifyRegionSwitch ", ex);
        }
    }

    public void mountCotaImage(Bundle data) {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager != null) {
                iOplusOperatorManager.mountCotaImage(data);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "mountCotaImage ", ex);
        }
    }

    public void notifyCotaMounted() {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager != null) {
                iOplusOperatorManager.notifyCotaMounted();
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "notifyCotaMounted ", ex);
        }
    }

    public int getCotaMountState(String imagePath) {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager == null) {
                return -1;
            }
            int ret = iOplusOperatorManager.getCotaMountState(imagePath);
            return ret;
        } catch (RemoteException ex) {
            Log.e(TAG, "getCotaMountState ", ex);
            return -1;
        }
    }

    public List<String> getCotaAppPackageNameList() {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager == null) {
                return null;
            }
            List<String> apkNameList = iOplusOperatorManager.getCotaAppPackageNameList();
            return apkNameList;
        } catch (RemoteException ex) {
            Log.e(TAG, "getCotaAppPackageNameList ", ex);
            return null;
        }
    }

    public boolean handleCotaCmd(Bundle bundle) {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager == null) {
                return false;
            }
            boolean ret = iOplusOperatorManager.handleCotaCmd(bundle);
            return ret;
        } catch (RemoteException ex) {
            Log.e(TAG, "handleCotaCmd ", ex);
            return false;
        }
    }

    public Bundle getCotaInfo(String action) {
        try {
            IOplusOperatorManager iOplusOperatorManager = this.mService;
            if (iOplusOperatorManager == null) {
                return null;
            }
            Bundle bundle = iOplusOperatorManager.getCotaInfo(action);
            return bundle;
        } catch (RemoteException ex) {
            Log.e(TAG, "getCotaInfo ", ex);
            return null;
        }
    }
}
