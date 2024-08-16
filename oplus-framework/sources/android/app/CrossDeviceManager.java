package android.app;

import android.app.ICrossDeviceService;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Singleton;
import android.view.InputEvent;
import android.view.Surface;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class CrossDeviceManager {
    public static final int APP_FEATURE_TYPE_MULTIPLE_APPS = 2;
    public static final int APP_FEATURE_TYPE_PHONE_SCREEN_ONLY = 1;
    public static final int APP_FEATURE_TYPE_UNKNOWN = 0;
    private static final int CROSS_DEVICE_VERSION = 1;
    public static final String FEATURE_AUDIO_STREAM = "feature_audio_stream";
    public static final String FEATURE_BASIC = "feature_basic";
    private static final String FEATURE_FOLD = "oplus.hardware.type.fold";
    public static final String FEATURE_OEM_RFCOMM = "feature_oem_rfcomm";
    public static final String FEATURE_PROXIMITY = "feature_proximity";
    public static final String FEATURE_RECENT_APPS = "feature_recent_apps";
    public static final String FEATURE_REMOTE_APPS = "feature_remote_apps";
    private static final int LTW_DEFAULT_FPS = 15;
    private static final int LTW_MD_FPS = 30;
    private static final int LTW_VD_FPS = 30;
    private static final String TAG = "CrossDeviceManager";
    private static final boolean LIGHT_OS = SystemProperties.getBoolean("ro.oplus.lightos", false);
    private static final List<String> CROSS_DEVICE_ALLOW_LIST = new ArrayList(Arrays.asList("com.microsoft.deviceintegrationservice"));
    private static final Singleton<ICrossDeviceService> SINGLETON = new Singleton<ICrossDeviceService>() { // from class: android.app.CrossDeviceManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public ICrossDeviceService m3create() {
            IBinder binder = ServiceManager.getService(RemoteTaskConstants.CROSS_DEVICE_SERVICE);
            return ICrossDeviceService.Stub.asInterface(binder);
        }
    };

    public static ICrossDeviceService getService() {
        return (ICrossDeviceService) SINGLETON.get();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean isFeatureSupported(String featureName) {
        char c;
        switch (featureName.hashCode()) {
            case -1186258523:
                if (featureName.equals(FEATURE_BASIC)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -604912974:
                if (featureName.equals(FEATURE_AUDIO_STREAM)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 519895057:
                if (featureName.equals(FEATURE_OEM_RFCOMM)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1042369142:
                if (featureName.equals(FEATURE_PROXIMITY)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1698422530:
                if (featureName.equals(FEATURE_REMOTE_APPS)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1744246285:
                if (featureName.equals(FEATURE_RECENT_APPS)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return true;
            case 1:
                return (OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.fold") || LIGHT_OS) ? false : true;
            case 2:
            case 3:
            case 4:
                return true;
            case 5:
                return true;
            default:
                return false;
        }
    }

    public static boolean isThreeStageVolumeDevice() {
        if (OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_ALERT_SLIDER)) {
            return true;
        }
        return false;
    }

    public static int getMaxFps(int appsFeatureType) {
        switch (appsFeatureType) {
            case 1:
                return 30;
            case 2:
                return 30;
            default:
                return 15;
        }
    }

    public static boolean isCallerAllowed(Context context) {
        Context appContext = context.getApplicationContext();
        int result = appContext.checkCallingOrSelfPermission("deviceintegration.permission.MANAGE_CROSS_DEVICE");
        if (result != 0) {
            return false;
        }
        int callingUid = Binder.getCallingUid();
        String callingPackage = appContext.getPackageManager().getNameForUid(callingUid);
        if (!CROSS_DEVICE_ALLOW_LIST.contains(callingPackage)) {
            return false;
        }
        return true;
    }

    public static int getCrossDeviceVersion() {
        return 1;
    }

    public void injectInputEvent(InputEvent event, int injectInputEventModeAsync) {
        try {
            getService().injectInputEvent(event, injectInputEventModeAsync);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getTopRunningPackageName() {
        try {
            return getService().getTopRunningPackageName();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void wakeUp(long time) {
        try {
            getService().wakeUp(time);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void notifyConnectionStateChange(boolean isConnected) {
        Log.d(TAG, "Connection State Changed: " + (isConnected ? "Connected" : "Disconnected"));
    }

    public void addRemoteTaskCallback(IRemoteTaskCallback callback) {
        try {
            getService().addRemoteTaskCallback(callback);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeRemoteTaskCallback(IRemoteTaskCallback callback) {
        try {
            getService().removeRemoteTaskCallback(callback);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void updateBackgroundActivityList(List<String> activityList) {
        try {
            getService().updateBackgroundActivityList(activityList);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void updateSurface(String uuid, Surface surface) {
        try {
            getService().updateSurface(uuid, surface);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setDisplayInfo(int width, int height, int densityDpi) {
        try {
            getService().updateDisplayInfo(width, height, densityDpi);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getThermalStatus() {
        try {
            return getService().getThermalStatus();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isResumableFromOverheat() {
        try {
            return getService().isResumableFromOverheat();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public IRfcommOemManager getRfcommOemManager() {
        try {
            return getService().getRfcommOemManager();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setPermissionGranted(int uid) {
        try {
            getService().setPermissionGranted(uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getPermissionGranted(int uid) {
        try {
            return getService().getPermissionGranted(uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSecurityToken(String securityToken) {
        try {
            getService().setSecurityToken(securityToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerTaskChangeListener(ITaskChangeListener listener) {
        try {
            getService().registerTaskChangeListener(listener);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterTaskChangeListener(ITaskChangeListener listener) {
        try {
            getService().unregisterTaskChangeListener(listener);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public IProximityService getProximityService() {
        try {
            return getService().getProximityService();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
