package android.debug;

import android.os.RemoteException;

/* loaded from: classes.dex */
public abstract class IDebugLogManager {
    public static String TAG_BASE = "alwayson.";

    public abstract void setLogDump() throws RemoteException;

    public abstract void setLogOff() throws RemoteException;

    public abstract void setLogOn(long j, String str) throws RemoteException;
}
