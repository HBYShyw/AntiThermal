package com.oplus.putt;

import android.app.OplusActivityTaskManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public class OplusPuttManager {
    public static final int DISMISS_SPLIT_SCREEN_FROM_PUTT_MODE = 9;
    public static final String PUTT_SERVICE_ACTION = "oplus.intent.action.PUTT_SERVICE";
    private static volatile OplusPuttManager sInstance;
    private OplusActivityTaskManager mOplusAtm = OplusActivityTaskManager.getInstance();

    private OplusPuttManager() {
    }

    public static OplusPuttManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusPuttManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusPuttManager();
                }
            }
        }
        return sInstance;
    }

    public boolean registerPuttObserver(IPuttObserver observer) throws RemoteException {
        return this.mOplusAtm.registerPuttObserver(observer);
    }

    public boolean unregisterPuttObserver(IPuttObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterPuttObserver(observer);
    }

    public boolean registerPuttEventObserver(PuttEventObserver observer) throws RemoteException {
        return this.mOplusAtm.registerPuttEventObserver(observer);
    }

    public boolean unregisterPuttEventObserver(PuttEventObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterPuttEventObserver(observer);
    }

    public boolean startOnePutt(int taskId, int enterAction, Bundle options) throws RemoteException {
        PuttParams params = PuttParams.createOnePuttParams(taskId, enterAction, options);
        return this.mOplusAtm.startPutt(params);
    }

    public boolean stopOnePutt(int exitAction, Bundle options) throws RemoteException {
        return this.mOplusAtm.stopPutt(PuttParams.PUTT_HASH, exitAction, options);
    }

    public boolean startTransientStateActivity(int enterAction, Intent intent, Bundle options) throws RemoteException {
        PuttParams params = PuttParams.createTransientStateParams(enterAction, intent, options);
        return this.mOplusAtm.startPutt(params);
    }

    public boolean startPuttActivity(String puttHash, int enterAction, String pkg, String service, Intent intent, Bundle options) throws RemoteException {
        PuttParams params = PuttParams.createPuttParams(puttHash, enterAction, pkg, service, intent, options);
        return this.mOplusAtm.startPutt(params);
    }

    public boolean stopPutt(String puttHash, int exitAction, Bundle options) throws RemoteException {
        return this.mOplusAtm.stopPutt(puttHash, exitAction, options);
    }

    public List<OplusPuttEnterInfo> getEnterPuttAppInfos() throws RemoteException {
        return this.mOplusAtm.getEnterPuttAppInfos();
    }

    public boolean removePuttTask(int exitAction, String pkg, int puttTaskId) throws RemoteException {
        return this.mOplusAtm.removePuttTask(exitAction, pkg, puttTaskId);
    }

    public boolean isSupportPuttMode(int type, String target, int userId, String callPkg, Bundle options) throws RemoteException {
        return this.mOplusAtm.isSupportPuttMode(type, target, userId, callPkg, options);
    }
}
