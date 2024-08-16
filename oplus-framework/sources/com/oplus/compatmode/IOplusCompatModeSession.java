package com.oplus.compatmode;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.compactwindow.IOplusCompactWindowObserver;
import com.oplus.compactwindow.OplusCompactWindowInfo;

/* loaded from: classes.dex */
public interface IOplusCompatModeSession extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.compatmode.IOplusCompatModeSession";

    void compatUIInit() throws RemoteException;

    void forceStopPackageAsUser(String str, int i) throws RemoteException;

    OplusCompactWindowInfo getCompactWindowInfo(int i) throws RemoteException;

    int moveCompatModeWindowToLeft(int i) throws RemoteException;

    int moveCompatModeWindowToRight(int i) throws RemoteException;

    void onRecentAnimationEnd() throws RemoteException;

    void onRecentAnimationStart() throws RemoteException;

    boolean registerCompactWindowObserver(IOplusCompactWindowObserver iOplusCompactWindowObserver) throws RemoteException;

    boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver iOplusCompactWindowObserver) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCompatModeSession {
        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public int moveCompatModeWindowToRight(int taskId) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public int moveCompatModeWindowToLeft(int taskId) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public OplusCompactWindowInfo getCompactWindowInfo(int taskId) throws RemoteException {
            return null;
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public boolean registerCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public void onRecentAnimationStart() throws RemoteException {
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public void onRecentAnimationEnd() throws RemoteException {
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public void compatUIInit() throws RemoteException {
        }

        @Override // com.oplus.compatmode.IOplusCompatModeSession
        public void forceStopPackageAsUser(String pkg, int userId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCompatModeSession {
        static final int TRANSACTION_compatUIInit = 8;
        static final int TRANSACTION_forceStopPackageAsUser = 9;
        static final int TRANSACTION_getCompactWindowInfo = 3;
        static final int TRANSACTION_moveCompatModeWindowToLeft = 2;
        static final int TRANSACTION_moveCompatModeWindowToRight = 1;
        static final int TRANSACTION_onRecentAnimationEnd = 7;
        static final int TRANSACTION_onRecentAnimationStart = 6;
        static final int TRANSACTION_registerCompactWindowObserver = 4;
        static final int TRANSACTION_unregisterCompactWindowObserver = 5;

        public Stub() {
            attachInterface(this, IOplusCompatModeSession.DESCRIPTOR);
        }

        public static IOplusCompatModeSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCompatModeSession.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCompatModeSession)) {
                return (IOplusCompatModeSession) iin;
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
                    return "moveCompatModeWindowToRight";
                case 2:
                    return "moveCompatModeWindowToLeft";
                case 3:
                    return "getCompactWindowInfo";
                case 4:
                    return "registerCompactWindowObserver";
                case 5:
                    return "unregisterCompactWindowObserver";
                case 6:
                    return "onRecentAnimationStart";
                case 7:
                    return "onRecentAnimationEnd";
                case 8:
                    return "compatUIInit";
                case 9:
                    return "forceStopPackageAsUser";
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
                data.enforceInterface(IOplusCompatModeSession.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCompatModeSession.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = moveCompatModeWindowToRight(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result2 = moveCompatModeWindowToLeft(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            OplusCompactWindowInfo _result3 = getCompactWindowInfo(_arg03);
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 4:
                            IOplusCompactWindowObserver _arg04 = IOplusCompactWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result4 = registerCompactWindowObserver(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 5:
                            IOplusCompactWindowObserver _arg05 = IOplusCompactWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result5 = unregisterCompactWindowObserver(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            onRecentAnimationStart();
                            reply.writeNoException();
                            return true;
                        case 7:
                            onRecentAnimationEnd();
                            reply.writeNoException();
                            return true;
                        case 8:
                            compatUIInit();
                            return true;
                        case 9:
                            String _arg06 = data.readString();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            forceStopPackageAsUser(_arg06, _arg1);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCompatModeSession {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCompatModeSession.DESCRIPTOR;
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public int moveCompatModeWindowToRight(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public int moveCompatModeWindowToLeft(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public OplusCompactWindowInfo getCompactWindowInfo(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    OplusCompactWindowInfo _result = (OplusCompactWindowInfo) _reply.readTypedObject(OplusCompactWindowInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public boolean registerCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public void onRecentAnimationStart() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public void onRecentAnimationEnd() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public void compatUIInit() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.compatmode.IOplusCompatModeSession
            public void forceStopPackageAsUser(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCompatModeSession.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 8;
        }
    }
}
