package com.oplus.edgetouch;

import android.app.OplusActivityTaskManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusEdgeTouchManager {
    private static final String TAG = "OplusEdgeTouchManager";
    private static volatile OplusEdgeTouchManager sInstance;
    private OplusActivityTaskManager mOAms = OplusActivityTaskManager.getInstance();
    public static int EDGE_TOUCH_VERSION = 1;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public static OplusEdgeTouchManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusEdgeTouchManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusEdgeTouchManager();
                }
            }
        }
        return sInstance;
    }

    private OplusEdgeTouchManager() {
    }

    public boolean isSupport() {
        if (DEBUG) {
            Log.i(TAG, "isSupport");
        }
        try {
            OplusActivityTaskManager oplusActivityTaskManager = this.mOAms;
            if (oplusActivityTaskManager != null) {
                boolean result = oplusActivityTaskManager.isSupportEdgeTouchPrevent();
                return result;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "isSupport remoteException " + e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean writeParam(Context context, String scenePkg, List<String> paramCmdList) {
        if (context == null) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "setParam  callPkg = " + context.getPackageName() + "  scenePkg = " + scenePkg + " paramCmdList = \n" + paramCmdList);
        }
        if (paramCmdList == null || paramCmdList.isEmpty()) {
            return false;
        }
        try {
            OplusActivityTaskManager oplusActivityTaskManager = this.mOAms;
            if (oplusActivityTaskManager != null) {
                boolean result = oplusActivityTaskManager.writeEdgeTouchPreventParam(context.getPackageName(), scenePkg, paramCmdList);
                return result;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "setParam remoteException " + e);
            e.printStackTrace();
        }
        return false;
    }

    public void setDefaultParam(Context context, List<String> paramCmdList) {
        if (context == null) {
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "setDefaultParam  callPkg = " + context.getPackageName() + "  paramCmdList = \n" + paramCmdList);
        }
        if (paramCmdList == null || paramCmdList.isEmpty()) {
            return;
        }
        try {
            OplusActivityTaskManager oplusActivityTaskManager = this.mOAms;
            if (oplusActivityTaskManager != null) {
                oplusActivityTaskManager.setDefaultEdgeTouchPreventParam(context.getPackageName(), paramCmdList);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "setDefaultParam remoteException " + e);
            e.printStackTrace();
        }
    }

    public boolean resetDefaultParam(Context context) {
        if (context == null) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "resetDefaultParam ");
        }
        try {
            OplusActivityTaskManager oplusActivityTaskManager = this.mOAms;
            if (oplusActivityTaskManager != null) {
                return oplusActivityTaskManager.resetDefaultEdgeTouchPreventParam(context.getPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "resetDefaultParam remoteException ");
            e.printStackTrace();
        }
        return false;
    }

    public void setRules(Context context, Map<String, List<String>> rules) {
        if (context == null) {
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "setRules " + rules);
        }
        if (rules == null || rules.isEmpty()) {
            return;
        }
        try {
            OplusActivityTaskManager oplusActivityTaskManager = this.mOAms;
            if (oplusActivityTaskManager != null) {
                oplusActivityTaskManager.setEdgeTouchCallRules(context.getPackageName(), rules);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "resetDefaultParam remoteException ");
            e.printStackTrace();
        }
    }
}
