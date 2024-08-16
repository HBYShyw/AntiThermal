package com.oplus.quicksettings;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.quicksettings.Tile;
import android.util.Singleton;
import com.oplus.quicksettings.IOplusTileManagerService;
import com.oplus.quicksettings.IOplusTileService;

/* loaded from: classes.dex */
public class OplusTileManager {
    public static final String ACTION_OPLUS_TILE_INIT = "com.oplus.quicksettings.action.OPLUS_TILE";
    public static final boolean DEBUG = false;
    private static final Singleton<OplusTileManager> INSTANCE = new Singleton<OplusTileManager>() { // from class: com.oplus.quicksettings.OplusTileManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public OplusTileManager m889create() {
            return new OplusTileManager();
        }
    };
    public static final String KEY_TYPE_ACTION = "type_action";
    public static final String SERVICE_NAME = "oplus_tile";
    public static final String TAG = "OplusTileManager";
    public static final int TYPE_ACTION_CLICK = 1;
    public static final int TYPE_ACTION_INIT = 0;

    public static OplusTileManager getInstance() {
        return (OplusTileManager) INSTANCE.get();
    }

    private IOplusTileManagerService getService() {
        IBinder service = ServiceManager.getService(SERVICE_NAME);
        if (service == null) {
            return null;
        }
        return IOplusTileManagerService.Stub.asInterface(service);
    }

    public void registerOplusTileService(OplusBaseTileService oplusBaseTileService) throws RemoteException {
        if (oplusBaseTileService == null) {
            return;
        }
        IOplusTileService oplusTileService = IOplusTileService.Stub.asInterface(oplusBaseTileService.asBinder());
        IOplusTileManagerService service = getService();
        if (service != null) {
            service.registerOplusTileService(oplusTileService);
        }
    }

    public void updateOplusTile(Tile tile, ComponentName name) throws RemoteException {
        IOplusTileManagerService service = getService();
        if (service != null) {
            service.updateOplusTile(tile, name);
        }
    }

    public Tile getOplusTile(ComponentName name) throws RemoteException {
        IOplusTileManagerService service = getService();
        if (service != null) {
            return service.getOplusTile(name);
        }
        return null;
    }

    public void updateRequest(Bundle bundle, ComponentName name) throws RemoteException {
        IOplusTileManagerService service = getService();
        if (service != null) {
            service.updateRequest(bundle, name);
        }
    }

    public void setDeathRecipientToken(IBinder binder, ComponentName name) throws RemoteException {
        IOplusTileManagerService service = getService();
        if (service != null) {
            service.setDeathRecipientToken(binder, name);
        }
    }
}
