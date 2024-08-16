package com.oplus.app;

import android.app.IAlarmManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusAlarmManager {
    private static final String PERMISSION_SAFE_MANAGER = "com.oplus.permission.safe.SAFE_MANAGER";
    private static final String TAG = "OplusAlarmManager";
    private static volatile OplusAlarmManager sInstance;
    private final IAlarmManager mAlarmManager = IAlarmManager.Stub.asInterface(ServiceManager.getService("alarm"));

    private OplusAlarmManager() {
    }

    public static OplusAlarmManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusAlarmManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusAlarmManager();
                }
            }
        }
        return sInstance;
    }

    public void cancelPoweroffAlarm(Context context, String name) {
        context.enforceCallingOrSelfPermission(PERMISSION_SAFE_MANAGER, TAG);
        try {
            this.mAlarmManager.cancelPoweroffAlarm(name);
        } catch (RemoteException ex) {
            Log.e(TAG, "Unable to cancel power off Alarm Manager!", ex);
        }
    }
}
