package com.oplus.miragewindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.miragewindow.IOplusCastScreenStateObserver;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusMirageWindowSession extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.miragewindow.IOplusMirageWindowSession";

    void addCastScreenState(OplusCastScreenState oplusCastScreenState) throws RemoteException;

    List<OplusCastScreenState> getCastScreenStateList() throws RemoteException;

    boolean registerCastScreenStateObserver(IOplusCastScreenStateObserver iOplusCastScreenStateObserver) throws RemoteException;

    void removeCastScreenState() throws RemoteException;

    void setMirageDisplayId(int i) throws RemoteException;

    boolean unregisterCastScreenStateObserver(IOplusCastScreenStateObserver iOplusCastScreenStateObserver) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusMirageWindowSession {
        @Override // com.oplus.miragewindow.IOplusMirageWindowSession
        public void addCastScreenState(OplusCastScreenState state) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowSession
        public List<OplusCastScreenState> getCastScreenStateList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowSession
        public void removeCastScreenState() throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowSession
        public boolean registerCastScreenStateObserver(IOplusCastScreenStateObserver observer) throws RemoteException {
            return false;
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowSession
        public boolean unregisterCastScreenStateObserver(IOplusCastScreenStateObserver observer) throws RemoteException {
            return false;
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowSession
        public void setMirageDisplayId(int id) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusMirageWindowSession {
        static final int TRANSACTION_addCastScreenState = 1;
        static final int TRANSACTION_getCastScreenStateList = 2;
        static final int TRANSACTION_registerCastScreenStateObserver = 4;
        static final int TRANSACTION_removeCastScreenState = 3;
        static final int TRANSACTION_setMirageDisplayId = 6;
        static final int TRANSACTION_unregisterCastScreenStateObserver = 5;

        public Stub() {
            attachInterface(this, IOplusMirageWindowSession.DESCRIPTOR);
        }

        public static IOplusMirageWindowSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusMirageWindowSession.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusMirageWindowSession)) {
                return (IOplusMirageWindowSession) iin;
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
                    return "addCastScreenState";
                case 2:
                    return "getCastScreenStateList";
                case 3:
                    return "removeCastScreenState";
                case 4:
                    return "registerCastScreenStateObserver";
                case 5:
                    return "unregisterCastScreenStateObserver";
                case 6:
                    return "setMirageDisplayId";
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
                data.enforceInterface(IOplusMirageWindowSession.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusMirageWindowSession.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusCastScreenState _arg0 = (OplusCastScreenState) data.readTypedObject(OplusCastScreenState.CREATOR);
                            data.enforceNoDataAvail();
                            addCastScreenState(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            List<OplusCastScreenState> _result = getCastScreenStateList();
                            reply.writeNoException();
                            reply.writeTypedList(_result, 1);
                            return true;
                        case 3:
                            removeCastScreenState();
                            reply.writeNoException();
                            return true;
                        case 4:
                            IOplusCastScreenStateObserver _arg02 = IOplusCastScreenStateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result2 = registerCastScreenStateObserver(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 5:
                            IOplusCastScreenStateObserver _arg03 = IOplusCastScreenStateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result3 = unregisterCastScreenStateObserver(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 6:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            setMirageDisplayId(_arg04);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusMirageWindowSession {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusMirageWindowSession.DESCRIPTOR;
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowSession
            public void addCastScreenState(OplusCastScreenState state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowSession.DESCRIPTOR);
                    _data.writeTypedObject(state, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowSession
            public List<OplusCastScreenState> getCastScreenStateList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowSession.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<OplusCastScreenState> _result = _reply.createTypedArrayList(OplusCastScreenState.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowSession
            public void removeCastScreenState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowSession.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowSession
            public boolean registerCastScreenStateObserver(IOplusCastScreenStateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowSession.DESCRIPTOR);
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

            @Override // com.oplus.miragewindow.IOplusMirageWindowSession
            public boolean unregisterCastScreenStateObserver(IOplusCastScreenStateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowSession.DESCRIPTOR);
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

            @Override // com.oplus.miragewindow.IOplusMirageWindowSession
            public void setMirageDisplayId(int id) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowSession.DESCRIPTOR);
                    _data.writeInt(id);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 5;
        }
    }
}
