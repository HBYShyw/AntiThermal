package android.view;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.direct.OplusDirectFindCmd;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;
import com.oplus.view.analysis.OplusWindowNode;
import java.io.FileDescriptor;
import java.util.List;

/* loaded from: classes.dex */
public class IOplusWindowImpl implements IOplusWindow {
    private static final String TAG = "IOplusWindowImpl";
    private final IBinder mRemote;

    private IOplusWindowImpl(IWindow client) {
        this(client.asBinder());
    }

    private IOplusWindowImpl(IBinder remote) {
        this.mRemote = remote;
    }

    public static IOplusWindow asInterface(IWindow client) {
        if (client == null) {
            return null;
        }
        return new IOplusWindowImpl(client);
    }

    public static IOplusWindow asInterface(IBinder remote) {
        return new IOplusWindowImpl(remote);
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotNotifyConnected(boolean isConnected) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            data.writeInt(isConnected ? 1 : 0);
            this.mRemote.transact(10002, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotDump(FileDescriptor fd, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows, String[] args) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            data.writeFileDescriptor(fd);
            if (systemWindows != null) {
                data.writeInt(1);
                data.writeTypedList(systemWindows);
            } else {
                data.writeInt(0);
            }
            if (floatWindows != null) {
                data.writeInt(1);
                data.writeTypedList(floatWindows);
            } else {
                data.writeInt(0);
            }
            data.writeStringArray(args);
            this.mRemote.transact(10003, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public OplusWindowNode longshotCollectWindow(boolean isStatusBar, boolean isNavigationBar) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        OplusWindowNode result = null;
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            data.writeInt(isStatusBar ? 1 : 0);
            data.writeInt(isNavigationBar ? 1 : 0);
            this.mRemote.transact(10004, data, reply, 1);
            reply.readException();
            if (reply.readInt() != 0) {
                result = OplusWindowNode.CREATOR.createFromParcel(reply);
            }
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotInjectInput(InputEvent event, int mode) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            if (event != null) {
                data.writeInt(1);
                event.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(mode);
            this.mRemote.transact(10005, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotInjectInputBegin() throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            this.mRemote.transact(10006, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotInjectInputEnd() throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            this.mRemote.transact(10007, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void screenshotDump(FileDescriptor fd) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            data.writeFileDescriptor(fd);
            this.mRemote.transact(10009, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.view.IOplusDirectWindow
    public void directFindCmd(OplusDirectFindCmd findCmd) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            if (findCmd != null) {
                data.writeInt(1);
                findCmd.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(10008, data, null, 1);
        } finally {
            data.recycle();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void requestScrollCapture(IOplusScrollCaptureResponseListener listener, Bundle extras) throws RemoteException {
        Parcel data = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusWindow.DESCRIPTOR);
            data.writeStrongBinder(listener != null ? listener.asBinder() : null);
            if (extras != null) {
                data.writeInt(1);
                extras.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(10010, data, null, 1);
        } finally {
            data.recycle();
        }
    }
}
