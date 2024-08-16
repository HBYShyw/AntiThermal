package com.oplus.atlas;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import com.oplus.atlas.IOplusMMAtlasService;
import java.util.List;

/* loaded from: classes.dex */
public class OplusAtlasManager {
    public static final String SERVICE_NAME = "multimediaDaemon";
    private static final String TAG = "OplusAtlasManager";
    private static IOplusMMAtlasService sOplusAtlasService;
    private boolean mBindServiceFlag = false;
    private static volatile OplusAtlasManager sInstance = null;
    private static boolean sServiceEnable = true;

    private OplusAtlasManager() {
    }

    public static OplusAtlasManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusAtlasManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusAtlasManager();
                }
            }
        }
        if (SystemProperties.getBoolean("persist.sys.multimedia.atlas.service", false)) {
            sServiceEnable = false;
        } else {
            sServiceEnable = true;
        }
        return sInstance;
    }

    private static IOplusMMAtlasService getService() {
        if (!sServiceEnable) {
            DebugLog.d(TAG, "OplusAtlasService disabled");
            return null;
        }
        IOplusMMAtlasService iOplusMMAtlasService = sOplusAtlasService;
        if (iOplusMMAtlasService != null) {
            return iOplusMMAtlasService;
        }
        IBinder b = ServiceManager.getService("multimediaDaemon");
        IOplusMMAtlasService asInterface = IOplusMMAtlasService.Stub.asInterface(b);
        sOplusAtlasService = asInterface;
        return asInterface;
    }

    public void sendMessage(String info) {
        if (info == null) {
            return;
        }
        String[] args = info.split(":");
        if (args.length != 2) {
            DebugLog.d(TAG, "info = " + info + " format is error,check it");
        } else {
            try {
                Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
            }
        }
    }

    public void setEvent(String event, String info) {
        IOplusMMAtlasService service = getService();
        DebugLog.d(TAG, "setEventInfo info = " + info + " service = " + service);
        if (service == null) {
            return;
        }
        try {
            service.setEvent(event, info);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
        }
    }

    public void setEventToNative(String event, String info) {
        IOplusMMAtlasService service = getService();
        DebugLog.d(TAG, "setEventInfo info = " + info + " service = " + service);
        if (service == null) {
            return;
        }
        try {
            service.setEventToNative(event, info);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
        }
    }

    public void setParameters(String keyValuePairs) {
        IOplusMMAtlasService service = getService();
        DebugLog.d(TAG, "setParameters keyValuePairs = " + keyValuePairs + " service = " + service);
        if (service == null) {
            return;
        }
        try {
            service.setParameters(keyValuePairs);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
        }
    }

    public String getParameters(String keys) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getParameters(keys);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    public boolean interfaceCallPermissionCheck(String function, String callPackageName) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return true;
        }
        try {
            return service.interfaceCallPermissionCheck(function, callPackageName);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return true;
        }
    }

    public void registerServiceCallback(IOplusAtlasServiceCallback callback) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.registerCallback(callback);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
        }
    }

    public void unRegisterServiceCallback(IOplusAtlasServiceCallback callback) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.unRegisterCallback(callback);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
        }
    }

    public void registerAudioCallback(IOplusAtlasAudioCallback callback) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.registerAudioCallback(callback);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
        }
    }

    public void unRegisterAudioCallback(IOplusAtlasAudioCallback callback) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.unRegisterAudioCallback(callback);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
        }
    }

    public boolean checkIsInDaemonlistByName(String module, String packageName) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return false;
        }
        try {
            return service.checkIsInDaemonlistByName(module, packageName);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return false;
        }
    }

    public boolean checkIsInDaemonlistByUid(String module, int uid) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return false;
        }
        try {
            return service.checkIsInDaemonlistByUid(module, uid);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return false;
        }
    }

    public String getPackageNameByUid(int uid) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getPackageNameByUid(uid);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    public String getPackageNameByPid(int pid) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getPackageNameByPid(pid);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    public String getAttributeByAppName(String module, String packageName) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getAttributeByAppName(module, packageName);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    public String getAttributeByAppUid(String module, int uid) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getAttributeByAppUid(module, uid);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    public String getListInfoByAppUid(String module, int uid) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getListInfoByAppUid(module, uid);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    public String getListInfoByAppName(String module, String packageName) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getListInfoByAppName(module, packageName);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    public List<String> getConfigAppList(String module) {
        IOplusMMAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getConfigAppList(module);
        } catch (RemoteException e) {
            DebugLog.e(TAG, "Dead object in info", e);
            return null;
        }
    }
}
