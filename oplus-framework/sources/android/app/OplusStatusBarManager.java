package android.app;

import android.app.IOplusStatusBar;
import android.app.IOplusStatusBarManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Singleton;

/* loaded from: classes.dex */
public class OplusStatusBarManager {
    public static final int CMD_START_ONE_HANDED_MODE = 1;
    public static final int CMD_STOP_ONE_HANDED_MODE = 2;
    public static final int FLAG_INPUT_METHOD_SHOW = 4;
    public static final int FLAG_KEYGUARD_SHOW = 1;
    public static final int FLAG_SCREEN_ON = 8;
    public static final int FLAG_SCREEN_SHOT_SHOW = 2;
    private static final Singleton<IOplusStatusBarManager> IOplusStatusBarManagerSingleton = new Singleton<IOplusStatusBarManager>() { // from class: android.app.OplusStatusBarManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusStatusBarManager m14create() {
            try {
                IOplusStatusBarManager oplusStatusBarManager = IOplusStatusBarManager.Stub.asInterface(ServiceManager.getService("statusbar").getExtension());
                return oplusStatusBarManager;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };
    public static final int TOGGLE_SPLIT_SCREEN_FROM_MENU = 2;
    public static final int TOGGLE_SPLIT_SCREEN_FROM_NONE = -1;
    public static final int TOGGLE_SPLIT_SCREEN_FROM_RECENT = 3;
    public static final int TOGGLE_SPLIT_SCREEN_FROM_SERVICE = 1;

    public static IOplusStatusBarManager getService() {
        return (IOplusStatusBarManager) IOplusStatusBarManagerSingleton.get();
    }

    public void registerOplusStatusBar(IOplusStatusBar callback) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm != null) {
            osm.registerOplusStatusBar(callback);
        }
    }

    public static void registerOplusStatusBar(OplusBaseStatusBar callback) {
        if (callback == null) {
            return;
        }
        try {
            IOplusStatusBar iosCallback = IOplusStatusBar.Stub.asInterface(callback.asBinder());
            IOplusStatusBarManager osm = getService();
            if (osm != null) {
                osm.registerOplusStatusBar(iosCallback);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerOplusClickTopCallback(IOplusClickTopCallback callback) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm != null) {
            osm.registerOplusClickTopCallback(callback);
        }
    }

    public void notifyClickTop() throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm != null) {
            osm.notifyClickTop();
        }
    }

    public void unregisterOplusClickTopCallback(IOplusClickTopCallback callback) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm != null) {
            osm.unregisterOplusClickTopCallback(callback);
        }
    }

    public boolean getTopIsFullscreen() throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm == null) {
            return false;
        }
        boolean topIsFullscreen = osm.getTopIsFullscreen();
        return topIsFullscreen;
    }

    public void toggleSplitScreen(int mode) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm != null) {
            osm.toggleSplitScreen(mode);
        }
    }

    public boolean setStatusBarFunction(int functionCode, String pkgName) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm == null) {
            return false;
        }
        boolean function = osm.setStatusBarFunction(functionCode, pkgName);
        return function;
    }

    public void topIsFullscreen(boolean topActivityIsFullscreen) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm != null) {
            osm.topIsFullscreen(topActivityIsFullscreen);
        }
    }

    public void notifyMultiWindowFocusChanged(int state) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm != null) {
            osm.notifyMultiWindowFocusChanged(state);
        }
    }

    public boolean controlOneHandedMode(int cmd, String pkgName) throws RemoteException {
        IOplusStatusBarManager osm = getService();
        if (osm == null) {
            return false;
        }
        boolean handed = osm.controlOneHandedMode(cmd, pkgName);
        return handed;
    }
}
