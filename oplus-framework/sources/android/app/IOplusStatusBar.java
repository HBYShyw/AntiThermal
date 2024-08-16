package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusStatusBar extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusStatusBar";

    void controlOneHandedMode(int i, String str) throws RemoteException;

    void notifyInputMethodKeyboardPosition(boolean z) throws RemoteException;

    void notifyMultiWindowFocusChanged(int i) throws RemoteException;

    void setStatusBarFunction(int i, String str) throws RemoteException;

    void toggleSplitScreen(int i) throws RemoteException;

    void topIsFullscreen(boolean z) throws RemoteException;

    void updateNavBarVisibility(int i) throws RemoteException;

    void updateNavBarVisibilityWithPkg(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusStatusBar {
        @Override // android.app.IOplusStatusBar
        public void topIsFullscreen(boolean fullscreen) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBar
        public void notifyMultiWindowFocusChanged(int state) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBar
        public void toggleSplitScreen(int mode) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBar
        public void setStatusBarFunction(int functionCode, String pkgName) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBar
        public void updateNavBarVisibility(int navBarVis) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBar
        public void updateNavBarVisibilityWithPkg(int navBarVis, String pkgName) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBar
        public void notifyInputMethodKeyboardPosition(boolean raise) throws RemoteException {
        }

        @Override // android.app.IOplusStatusBar
        public void controlOneHandedMode(int cmd, String pkgName) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusStatusBar {
        static final int TRANSACTION_controlOneHandedMode = 8;
        static final int TRANSACTION_notifyInputMethodKeyboardPosition = 7;
        static final int TRANSACTION_notifyMultiWindowFocusChanged = 2;
        static final int TRANSACTION_setStatusBarFunction = 4;
        static final int TRANSACTION_toggleSplitScreen = 3;
        static final int TRANSACTION_topIsFullscreen = 1;
        static final int TRANSACTION_updateNavBarVisibility = 5;
        static final int TRANSACTION_updateNavBarVisibilityWithPkg = 6;

        public Stub() {
            attachInterface(this, IOplusStatusBar.DESCRIPTOR);
        }

        public static IOplusStatusBar asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusStatusBar.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusStatusBar)) {
                return (IOplusStatusBar) iin;
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
                    return "topIsFullscreen";
                case 2:
                    return "notifyMultiWindowFocusChanged";
                case 3:
                    return "toggleSplitScreen";
                case 4:
                    return "setStatusBarFunction";
                case 5:
                    return "updateNavBarVisibility";
                case 6:
                    return "updateNavBarVisibilityWithPkg";
                case 7:
                    return "notifyInputMethodKeyboardPosition";
                case 8:
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
                data.enforceInterface(IOplusStatusBar.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusStatusBar.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            topIsFullscreen(_arg0);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyMultiWindowFocusChanged(_arg02);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            toggleSplitScreen(_arg03);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            setStatusBarFunction(_arg04, _arg1);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            updateNavBarVisibility(_arg05);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            updateNavBarVisibilityWithPkg(_arg06, _arg12);
                            return true;
                        case 7:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            notifyInputMethodKeyboardPosition(_arg07);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            controlOneHandedMode(_arg08, _arg13);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusStatusBar {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusStatusBar.DESCRIPTOR;
            }

            @Override // android.app.IOplusStatusBar
            public void topIsFullscreen(boolean fullscreen) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeBoolean(fullscreen);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBar
            public void notifyMultiWindowFocusChanged(int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBar
            public void toggleSplitScreen(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBar
            public void setStatusBarFunction(int functionCode, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeInt(functionCode);
                    _data.writeString(pkgName);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBar
            public void updateNavBarVisibility(int navBarVis) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeInt(navBarVis);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBar
            public void updateNavBarVisibilityWithPkg(int navBarVis, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeInt(navBarVis);
                    _data.writeString(pkgName);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBar
            public void notifyInputMethodKeyboardPosition(boolean raise) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeBoolean(raise);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusStatusBar
            public void controlOneHandedMode(int cmd, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusStatusBar.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeString(pkgName);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 7;
        }
    }
}
