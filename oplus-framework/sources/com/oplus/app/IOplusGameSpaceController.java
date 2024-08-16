package com.oplus.app;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusGameSpaceController extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusGameSpaceController";

    void dispatchGameDock(Bundle bundle) throws RemoteException;

    void gameExiting(String str) throws RemoteException;

    void gameStarting(Intent intent, String str, boolean z) throws RemoteException;

    boolean isGameDockAllowed() throws RemoteException;

    void videoStarting(Intent intent, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusGameSpaceController {
        @Override // com.oplus.app.IOplusGameSpaceController
        public void gameStarting(Intent intent, String pkg, boolean isResume) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusGameSpaceController
        public void gameExiting(String pkg) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusGameSpaceController
        public void videoStarting(Intent intent, String pkg) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusGameSpaceController
        public void dispatchGameDock(Bundle bundle) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusGameSpaceController
        public boolean isGameDockAllowed() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusGameSpaceController {
        static final int TRANSACTION_dispatchGameDock = 4;
        static final int TRANSACTION_gameExiting = 2;
        static final int TRANSACTION_gameStarting = 1;
        static final int TRANSACTION_isGameDockAllowed = 5;
        static final int TRANSACTION_videoStarting = 3;

        public Stub() {
            attachInterface(this, IOplusGameSpaceController.DESCRIPTOR);
        }

        public static IOplusGameSpaceController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusGameSpaceController.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusGameSpaceController)) {
                return (IOplusGameSpaceController) iin;
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
                    return "gameStarting";
                case 2:
                    return "gameExiting";
                case 3:
                    return "videoStarting";
                case 4:
                    return "dispatchGameDock";
                case 5:
                    return "isGameDockAllowed";
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
                data.enforceInterface(IOplusGameSpaceController.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusGameSpaceController.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Intent _arg0 = (Intent) data.readTypedObject(Intent.CREATOR);
                            String _arg1 = data.readString();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            gameStarting(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            gameExiting(_arg02);
                            return true;
                        case 3:
                            Intent _arg03 = (Intent) data.readTypedObject(Intent.CREATOR);
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            videoStarting(_arg03, _arg12);
                            return true;
                        case 4:
                            Bundle _arg04 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            dispatchGameDock(_arg04);
                            return true;
                        case 5:
                            boolean _result = isGameDockAllowed();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusGameSpaceController {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusGameSpaceController.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusGameSpaceController
            public void gameStarting(Intent intent, String pkg, boolean isResume) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameSpaceController.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeString(pkg);
                    _data.writeBoolean(isResume);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusGameSpaceController
            public void gameExiting(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameSpaceController.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusGameSpaceController
            public void videoStarting(Intent intent, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameSpaceController.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeString(pkg);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusGameSpaceController
            public void dispatchGameDock(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameSpaceController.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusGameSpaceController
            public boolean isGameDockAllowed() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGameSpaceController.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
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
            return 4;
        }
    }
}
