package android.app;

import android.app.IOplusClickTopCallback;
import android.app.IOplusStatusBar;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusStatusBarManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusStatusBarManager";

    boolean controlOneHandedMode(int i, String str) throws RemoteException;

    boolean getTopIsFullscreen() throws RemoteException;

    void notifyClickTop() throws RemoteException;

    void notifyMultiWindowFocusChanged(int i) throws RemoteException;

    void registerOplusClickTopCallback(IOplusClickTopCallback iOplusClickTopCallback) throws RemoteException;

    void registerOplusStatusBar(IOplusStatusBar iOplusStatusBar) throws RemoteException;

    boolean setStatusBarFunction(int i, String str) throws RemoteException;

    void toggleSplitScreen(int i) throws RemoteException;

    void topIsFullscreen(boolean z) throws RemoteException;

    void unregisterOplusClickTopCallback(IOplusClickTopCallback iOplusClickTopCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusStatusBarManager {
        @Override // android.app.IOplusStatusBarManager
        public void registerOplusStatusBar(IOplusStatusBar callback) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBarManager
        public void registerOplusClickTopCallback(IOplusClickTopCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBarManager
        public void notifyClickTop() throws RemoteException {
        }

        @Override // android.app.IOplusStatusBarManager
        public void unregisterOplusClickTopCallback(IOplusClickTopCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBarManager
        public boolean getTopIsFullscreen() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusStatusBarManager
        public void toggleSplitScreen(int mode) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBarManager
        public boolean setStatusBarFunction(int functionCode, String pkgName) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusStatusBarManager
        public void topIsFullscreen(boolean fullscreen) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBarManager
        public void notifyMultiWindowFocusChanged(int mode) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBarManager
        public boolean controlOneHandedMode(int cmd, String pkgName) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusStatusBarManager {
        static final int TRANSACTION_controlOneHandedMode = 10;
        static final int TRANSACTION_getTopIsFullscreen = 5;
        static final int TRANSACTION_notifyClickTop = 3;
        static final int TRANSACTION_notifyMultiWindowFocusChanged = 9;
        static final int TRANSACTION_registerOplusClickTopCallback = 2;
        static final int TRANSACTION_registerOplusStatusBar = 1;
        static final int TRANSACTION_setStatusBarFunction = 7;
        static final int TRANSACTION_toggleSplitScreen = 6;
        static final int TRANSACTION_topIsFullscreen = 8;
        static final int TRANSACTION_unregisterOplusClickTopCallback = 4;

        public Stub() {
            attachInterface(this, IOplusStatusBarManager.DESCRIPTOR);
        }

        public static IOplusStatusBarManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusStatusBarManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusStatusBarManager)) {
                return (IOplusStatusBarManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerOplusStatusBar";
                case 2:
                    return "registerOplusClickTopCallback";
                case 3:
                    return "notifyClickTop";
                case 4:
                    return "unregisterOplusClickTopCallback";
                case 5:
                    return "getTopIsFullscreen";
                case 6:
                    return "toggleSplitScreen";
                case 7:
                    return "setStatusBarFunction";
                case 8:
                    return "topIsFullscreen";
                case 9:
                    return "notifyMultiWindowFocusChanged";
                case 10:
                    return "controlOneHandedMode";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusStatusBarManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusStatusBarManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusStatusBar _arg0 = IOplusStatusBar.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOplusStatusBar(_arg0);
                            return true;
                        case 2:
                            IOplusClickTopCallback _arg02 = IOplusClickTopCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOplusClickTopCallback(_arg02);
                            return true;
                        case 3:
                            notifyClickTop();
                            return true;
                        case 4:
                            IOplusClickTopCallback _arg03 = IOplusClickTopCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterOplusClickTopCallback(_arg03);
                            return true;
                        case 5:
                            boolean _result = getTopIsFullscreen();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 6:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            toggleSplitScreen(_arg04);
                            return true;
                        case 7:
                            int _arg05 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = setStatusBarFunction(_arg05, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 8:
                            boolean _arg06 = data.readBoolean();
                            data.enforceNoDataAvail();
                            topIsFullscreen(_arg06);
                            return true;
                        case 9:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyMultiWindowFocusChanged(_arg07);
                            return true;
                        case 10:
                            int _arg08 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result3 = controlOneHandedMode(_arg08, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusStatusBarManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusStatusBarManager.DESCRIPTOR;
            }

            @Override // android.app.IOplusStatusBarManager
            public void registerOplusStatusBar(IOplusStatusBar callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public void registerOplusClickTopCallback(IOplusClickTopCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public void notifyClickTop() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public void unregisterOplusClickTopCallback(IOplusClickTopCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public boolean getTopIsFullscreen() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public void toggleSplitScreen(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public boolean setStatusBarFunction(int functionCode, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeInt(functionCode);
                    _data.writeString(pkgName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public void topIsFullscreen(boolean fullscreen) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeBoolean(fullscreen);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public void notifyMultiWindowFocusChanged(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBarManager
            public boolean controlOneHandedMode(int cmd, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusStatusBarManager.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeString(pkgName);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 9;
        }
    }
}
