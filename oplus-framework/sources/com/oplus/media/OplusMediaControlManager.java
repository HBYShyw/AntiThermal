package com.oplus.media;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Singleton;
import com.oplus.media.IOplusMediaControlManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusMediaControlManager {
    private static final String SERVICE_NAME = "oplus_media_control";
    private static final String TAG = "OplusMediaControlManager";
    private static final Singleton<IOplusMediaControlManager> SERVICE_SINGLETON = new Singleton<IOplusMediaControlManager>() { // from class: com.oplus.media.OplusMediaControlManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusMediaControlManager m715create() {
            try {
                IOplusMediaControlManager mediaControlManager = IOplusMediaControlManager.Stub.asInterface(ServiceManager.getService(OplusMediaControlManager.SERVICE_NAME));
                if (mediaControlManager == null) {
                    Log.e(OplusMediaControlManager.TAG, "mediaControlManager is null");
                    return null;
                }
                return mediaControlManager;
            } catch (Exception exception) {
                Log.e(OplusMediaControlManager.TAG, "create singleton failed:" + exception.getMessage());
                return null;
            }
        }
    };
    private static OplusMediaControlManager sInstance = null;

    private static IOplusMediaControlManager getService() {
        return (IOplusMediaControlManager) SERVICE_SINGLETON.get();
    }

    public static OplusMediaControlManager getInstance() {
        if (sInstance == null) {
            sInstance = new OplusMediaControlManager();
        }
        return sInstance;
    }

    public void sendEvent(OplusMediaEvent event) {
        if (event == null) {
            Log.e(TAG, "event is null");
            return;
        }
        IOplusMediaControlManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusMediaControlManagerService");
            return;
        }
        try {
            service.sendEvent(event);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerEventObserver(OplusMediaEventObserver observer, OplusMediaEventFilter filter) {
        if (observer == null || filter == null) {
            Log.e(TAG, "observer or filter is null");
            return;
        }
        IOplusMediaControlManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusMediaControlManagerService");
            return;
        }
        try {
            service.registerEventObserver(observer, filter);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<OplusMediaPlayerInfo> getCurrentPlayerInfoList() {
        List<OplusMediaPlayerInfo> infoLists = new ArrayList<>();
        IOplusMediaControlManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusMediaControlManagerService");
            return infoLists;
        }
        try {
            List<OplusMediaPlayerInfo> infoLists2 = service.getCurrentPlayerInfoList();
            return infoLists2;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<OplusMediaPlayerInfo> getHistoryMediaPlayerInfoList() {
        List<OplusMediaPlayerInfo> historyInfoLists = new ArrayList<>();
        IOplusMediaControlManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusMediaControlManagerService");
            return historyInfoLists;
        }
        try {
            List<OplusMediaPlayerInfo> historyInfoLists2 = service.getHistoryMediaPlayerInfoList();
            return historyInfoLists2;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterEventObserver(OplusMediaEventObserver observer) {
        if (observer == null) {
            Log.e(TAG, "observer is null");
        }
        IOplusMediaControlManager service = getService();
        if (service == null) {
            Log.e(TAG, "can not find OplusMediaControlManagerService");
            return;
        }
        try {
            service.unregisterEventObserver(observer);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
