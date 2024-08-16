package android.app;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;

/* loaded from: classes.dex */
public class IOplusApplicationThreadImpl implements IOplusApplicationThread {
    private static final String TAG = "IOplusApplicationThread";
    private final IBinder mRemote;

    private IOplusApplicationThreadImpl(IApplicationThread clientThread) {
        this(clientThread.asBinder());
    }

    private IOplusApplicationThreadImpl(IBinder remote) {
        this.mRemote = remote;
    }

    public static IOplusApplicationThread asInterface(IApplicationThread clientThread) {
        if (clientThread == null) {
            return null;
        }
        return new IOplusApplicationThreadImpl(clientThread);
    }

    public static IOplusApplicationThread asInterface(IBinder remote) {
        return new IOplusApplicationThreadImpl(remote);
    }

    @Override // android.app.IOplusApplicationThread
    public void setDynamicalLogEnable(boolean on) throws RemoteException {
        Log.d(TAG, "setDynamicalLogEnable: client: on " + on);
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusApplicationThread.DESCRIPTOR);
            data.writeInt(on ? 1 : 0);
            this.mRemote.transact(10002, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void setDynamicalLogConfig(List<String> configs) throws RemoteException {
        Log.d(TAG, "setDynamicalLogConfig: client: on " + configs);
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusApplicationThread.DESCRIPTOR);
            if (configs != null) {
                data.writeInt(1);
                data.writeStringList(configs);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(10003, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void scheduleApplicationInfoChangedForSwitchUser(ApplicationInfo ai, int updateFrameworkRes) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusApplicationThread.DESCRIPTOR);
            data.writeParcelable(ai, 0);
            data.writeInt(updateFrameworkRes);
            this.mRemote.transact(10004, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void setZoomSensorState(boolean isZoom) throws RemoteException {
        Log.d(TAG, "setZoomSensorState: isZoom " + isZoom);
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusApplicationThread.DESCRIPTOR);
            data.writeInt(isZoom ? 1 : 0);
            this.mRemote.transact(10005, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void sendGfxTrim(int level) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusApplicationThread.DESCRIPTOR);
            data.writeInt(level);
            this.mRemote.transact(10006, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void sendBracketModeUnitData(OplusBracketModeUnit bracketModeUnit) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusApplicationThread.DESCRIPTOR);
            data.writeParcelable(bracketModeUnit, 0);
            this.mRemote.transact(10007, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void setViewExtractData(Bundle bundle, IBinder activityToken) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusApplicationThread.DESCRIPTOR);
            data.writeBundle(bundle);
            data.writeStrongBinder(activityToken);
            this.mRemote.transact(10008, data, null, 1);
        } finally {
            data.recycle();
        }
    }
}
