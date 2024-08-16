package com.oplus.commscene;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.bluetooth.OplusBluetoothQoSData$$ExternalSyntheticLambda0;
import com.oplus.commscene.ICommScene;
import java.util.Set;

/* loaded from: classes.dex */
public final class CommSceneManager {
    public static final String COMMSCENE_SERVICE = "commscene";
    private static final String TAG = "CommSceneManager";
    private static volatile CommSceneManager sInstance;
    private static ICommScene sService = null;

    private CommSceneManager() {
        if (sService == null) {
            try {
                sService = ICommScene.Stub.asInterface(ServiceManager.getServiceOrThrow(COMMSCENE_SERVICE));
            } catch (ServiceManager.ServiceNotFoundException e) {
                sService = null;
                Log.e(TAG, "failed to get service!");
            }
        }
    }

    public static CommSceneManager getInstance() {
        if (sInstance == null) {
            synchronized (CommSceneManager.class) {
                if (sInstance == null) {
                    sInstance = new CommSceneManager();
                }
            }
        }
        return sInstance;
    }

    public void listenSceneState(ICommSceneListener callback, Set<Integer> scenes, boolean addFlag) {
        try {
            if (!scenes.isEmpty()) {
                ICommScene iCommScene = sService;
                if (iCommScene != null) {
                    iCommScene.listenSceneState(callback, scenes.stream().mapToInt(new OplusBluetoothQoSData$$ExternalSyntheticLambda0()).toArray(), addFlag);
                    return;
                } else {
                    Log.e(TAG, "sService is null");
                    return;
                }
            }
            ICommScene iCommScene2 = sService;
            if (iCommScene2 != null) {
                iCommScene2.listenSceneState(callback, null, addFlag);
            } else {
                Log.e(TAG, "sService is null");
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int inquireSceneState(int scene, int phoneId) {
        try {
            ICommScene iCommScene = sService;
            if (iCommScene != null) {
                return iCommScene.inquireSceneState(scene, phoneId);
            }
            Log.e(TAG, "sService is null");
            return -1;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
