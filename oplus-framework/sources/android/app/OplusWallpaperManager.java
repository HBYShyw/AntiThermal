package android.app;

import android.app.IOplusWallpaperManager;
import android.app.IWallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Singleton;

/* loaded from: classes.dex */
public class OplusWallpaperManager {
    public static final int FLAG_FLAT_DISPLAY = 16;
    public static final int FLAG_FOLD_DISPLAY = 32;
    public static final int PHYSICAL_DISPLAY_MAIN = 1;
    public static final int PHYSICAL_DISPLAY_SUB = 2;
    private static final Singleton<IOplusWallpaperManager> SERVICE_SINGLETON = new Singleton<IOplusWallpaperManager>() { // from class: android.app.OplusWallpaperManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusWallpaperManager m17create() {
            try {
                IBinder binder = ServiceManager.getService("wallpaper");
                IWallpaperManager wm = IWallpaperManager.Stub.asInterface(binder);
                IBinder extBinder = wm.asBinder().getExtension();
                Log.d(OplusWallpaperManager.TAG, "get wallpaper manager service extension: " + extBinder);
                return IOplusWallpaperManager.Stub.asInterface(extBinder);
            } catch (Exception exception) {
                Log.e(OplusWallpaperManager.TAG, "create OplusWallpaperManagerServiceEnhance singleton failed:" + exception.getMessage());
                return null;
            }
        }
    };
    private static final String TAG = "OplusWallpaperManager";

    private static IOplusWallpaperManager getService() {
        return (IOplusWallpaperManager) SERVICE_SINGLETON.get();
    }

    public static boolean setWallpaperComponent(Context context, ComponentName name, int which) {
        IOplusWallpaperManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusWallpaperManagerService");
            return false;
        }
        try {
            service.setFoldWallpaperComponentChecked(name, context.getOpPackageName(), context.getUserId(), which);
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static WallpaperInfo getWallpaperInfo(int userId, int which) {
        IOplusWallpaperManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusWallpaperManagerService");
            return null;
        }
        try {
            return service.getFoldWallpaperInfo(userId, which);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean rebindWallpaperComponent(ComponentName componentName, int physicalId) {
        IOplusWallpaperManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusWallpaperManagerService");
            return false;
        }
        try {
            return service.rebindWallpaperComponent(componentName, physicalId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean isWallpaperSupportBackup(int userId, int which) {
        IOplusWallpaperManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusWallpaperManagerService");
            return false;
        }
        try {
            return service.isWallpaperSupportBackup(userId, which);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
