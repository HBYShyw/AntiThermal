package android.app;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

/* loaded from: classes.dex */
public class OplusBackgroundTaskManager {
    private static final String DESCRIPTOR = "android.app.IActivityTaskManager";
    public static final int TRANSACTION_first = 70000;
    public static final int TRANSACTION_isTopCanMoveToBackground = 70001;
    public static final int TRANSACTION_playWhenScreenOff = 70002;
    private static volatile OplusBackgroundTaskManager sOplusBackgroundTaskManager;
    private IBinder mRemote = ServiceManager.getService("activity_task");

    private OplusBackgroundTaskManager() {
    }

    public static OplusBackgroundTaskManager getInstance() {
        if (sOplusBackgroundTaskManager == null) {
            synchronized (OplusBackgroundTaskManager.class) {
                if (sOplusBackgroundTaskManager == null) {
                    sOplusBackgroundTaskManager = new OplusBackgroundTaskManager();
                }
            }
        }
        return sOplusBackgroundTaskManager;
    }

    public static boolean isSupport() {
        return true;
    }

    public boolean isTopCanMoveToBackground() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken("android.app.IActivityTaskManager");
            this.mRemote.transact(TRANSACTION_isTopCanMoveToBackground, data, reply, 0);
            reply.readException();
            boolean result = reply.readBoolean();
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void playWhenScreenOff() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken("android.app.IActivityTaskManager");
            this.mRemote.transact(TRANSACTION_playWhenScreenOff, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }
}
