package android.app;

import android.app.IOplusNotificationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Singleton;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusNotificationManager {
    private static final Singleton<IOplusNotificationManager> MANAGER_SINGLETON = new Singleton<IOplusNotificationManager>() { // from class: android.app.OplusNotificationManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusNotificationManager m13create() {
            try {
                IOplusNotificationManager oplusNotificationManager = IOplusNotificationManager.Stub.asInterface(NotificationManager.getService().asBinder().getExtension());
                return oplusNotificationManager;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };
    private static final String TAG = "OplusNotificationManager";

    public static IOplusNotificationManager getService() {
        return (IOplusNotificationManager) MANAGER_SINGLETON.get();
    }

    public boolean canModifyNotificationPermission(String pkg, int uid) {
        if (pkg == null || pkg.equals("")) {
            return false;
        }
        IOplusNotificationManager service = getService();
        try {
            return service.canModifyNotificationPermission(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean shouldInterceptSound(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.shouldInterceptSound(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean shouldKeepAlive(String pkg, int userId) {
        IOplusNotificationManager service = getService();
        try {
            return service.shouldKeepAlive(pkg, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getNavigationMode(String pkg, int userId) {
        IOplusNotificationManager service = getService();
        try {
            return service.getNavigationMode(pkg, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isDriveNavigationMode(String pkg, int userId) {
        IOplusNotificationManager service = getService();
        try {
            return service.isDriveNavigationMode(pkg, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isNavigationMode(int userId) {
        IOplusNotificationManager service = getService();
        try {
            return service.isNavigationMode(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getEnableNavigationApps(int userId) {
        IOplusNotificationManager service = getService();
        try {
            return service.getEnableNavigationApps(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSuppressedByDriveMode(int userId) {
        IOplusNotificationManager service = getService();
        try {
            return service.isSuppressedByDriveMode(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSuppressedByDriveMode(boolean mode, int userId) {
        IOplusNotificationManager service = getService();
        try {
            service.setSuppressedByDriveMode(mode, userId);
            service.checkDriveMode(mode, Binder.getCallingPid(), Binder.getCallingUid());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getOpenid(String pkg, int uid, String type) {
        return getStdid(pkg, uid, type);
    }

    public void clearOpenid(String pkg, int uid, String type) {
        clearStdid(pkg, uid, type);
    }

    public boolean checkGetOpenid(String pkg, int uid, String type) {
        return checkGetStdid(pkg, uid, type);
    }

    public String getStdid(String pkg, int uid, String type) {
        IOplusNotificationManager service = getService();
        try {
            return service.getStdid(pkg, uid, type);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearStdid(String pkg, int uid, String type) {
        IOplusNotificationManager service = getService();
        try {
            service.clearStdid(pkg, uid, type);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean checkGetStdid(String pkg, int uid, String type) {
        IOplusNotificationManager service = getService();
        try {
            return service.checkGetStdid(pkg, uid, type);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setBadgeOption(String pkg, int uid, int option) {
        IOplusNotificationManager service = getService();
        try {
            service.setBadgeOption(pkg, uid, option);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getBadgeOption(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.getBadgeOption(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isNumbadgeSupport(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.isNumbadgeSupport(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setNumbadgeSupport(String pkg, int uid, boolean support) {
        IOplusNotificationManager service = getService();
        try {
            service.setNumbadgeSupport(pkg, uid, support);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getStowOption(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.getStowOption(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setStowOption(String pkg, int uid, int option) {
        IOplusNotificationManager service = getService();
        try {
            service.setStowOption(pkg, uid, option);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getDynamicRingtone(String uri, String pkg) {
        IOplusNotificationManager service = getService();
        try {
            return service.getDynamicRingtone(uri, pkg);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getAppBanner(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.getAppBanner(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setAppBanner(String pkg, int uid, boolean enabled) {
        IOplusNotificationManager service = getService();
        try {
            service.setAppBanner(pkg, uid, enabled);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getAppVisibility(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.getAppVisibility(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setAppVisibility(String pkg, int uid, int option) {
        IOplusNotificationManager service = getService();
        try {
            service.setAppVisibility(pkg, uid, option);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setAppRingtonePermission(String pkg, int uid, boolean permissionGranted) {
        IOplusNotificationManager service = getService();
        try {
            service.setAppRingtonePermission(pkg, uid, permissionGranted);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isAppRingtonePermissionGranted(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.isAppRingtonePermissionGranted(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setAppVibrationPermission(String pkg, int uid, boolean permissionGranted) {
        IOplusNotificationManager service = getService();
        try {
            service.setAppVibrationPermission(pkg, uid, permissionGranted);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isAppVibrationPermissionGranted(String pkg, int uid) {
        IOplusNotificationManager service = getService();
        try {
            return service.isAppVibrationPermissionGranted(pkg, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Map<String, Bundle> getAllAppsNotificationPermissions() throws RemoteException {
        IOplusNotificationManager service = getService();
        try {
            return service.getAllAppsNotificationPermissions();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
