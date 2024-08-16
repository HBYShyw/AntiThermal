package com.oplus.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusMediaControlManager extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.media.IOplusMediaControlManager";

    List<OplusMediaPlayerInfo> getCurrentPlayerInfoList() throws RemoteException;

    List<OplusMediaPlayerInfo> getHistoryMediaPlayerInfoList() throws RemoteException;

    void registerEventObserver(IBinder iBinder, OplusMediaEventFilter oplusMediaEventFilter) throws RemoteException;

    void sendEvent(OplusMediaEvent oplusMediaEvent) throws RemoteException;

    void unregisterEventObserver(IBinder iBinder) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusMediaControlManager {
        @Override // com.oplus.media.IOplusMediaControlManager
        public List<OplusMediaPlayerInfo> getHistoryMediaPlayerInfoList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.media.IOplusMediaControlManager
        public List<OplusMediaPlayerInfo> getCurrentPlayerInfoList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.media.IOplusMediaControlManager
        public void sendEvent(OplusMediaEvent event) throws RemoteException {
        }

        @Override // com.oplus.media.IOplusMediaControlManager
        public void registerEventObserver(IBinder observer, OplusMediaEventFilter filter) throws RemoteException {
        }

        @Override // com.oplus.media.IOplusMediaControlManager
        public void unregisterEventObserver(IBinder observer) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusMediaControlManager {
        static final int TRANSACTION_getCurrentPlayerInfoList = 2;
        static final int TRANSACTION_getHistoryMediaPlayerInfoList = 1;
        static final int TRANSACTION_registerEventObserver = 4;
        static final int TRANSACTION_sendEvent = 3;
        static final int TRANSACTION_unregisterEventObserver = 5;

        public Stub() {
            attachInterface(this, IOplusMediaControlManager.DESCRIPTOR);
        }

        public static IOplusMediaControlManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusMediaControlManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusMediaControlManager)) {
                return (IOplusMediaControlManager) iin;
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
                    return "getHistoryMediaPlayerInfoList";
                case 2:
                    return "getCurrentPlayerInfoList";
                case 3:
                    return "sendEvent";
                case 4:
                    return "registerEventObserver";
                case 5:
                    return "unregisterEventObserver";
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
                data.enforceInterface(IOplusMediaControlManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusMediaControlManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<OplusMediaPlayerInfo> _result = getHistoryMediaPlayerInfoList();
                            reply.writeNoException();
                            reply.writeTypedList(_result, 1);
                            return true;
                        case 2:
                            List<OplusMediaPlayerInfo> _result2 = getCurrentPlayerInfoList();
                            reply.writeNoException();
                            reply.writeTypedList(_result2, 1);
                            return true;
                        case 3:
                            OplusMediaEvent _arg0 = (OplusMediaEvent) data.readTypedObject(OplusMediaEvent.CREATOR);
                            data.enforceNoDataAvail();
                            sendEvent(_arg0);
                            reply.writeNoException();
                            return true;
                        case 4:
                            IBinder _arg02 = data.readStrongBinder();
                            OplusMediaEventFilter _arg1 = (OplusMediaEventFilter) data.readTypedObject(OplusMediaEventFilter.CREATOR);
                            data.enforceNoDataAvail();
                            registerEventObserver(_arg02, _arg1);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IBinder _arg03 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            unregisterEventObserver(_arg03);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusMediaControlManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusMediaControlManager.DESCRIPTOR;
            }

            @Override // com.oplus.media.IOplusMediaControlManager
            public List<OplusMediaPlayerInfo> getHistoryMediaPlayerInfoList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMediaControlManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<OplusMediaPlayerInfo> _result = _reply.createTypedArrayList(OplusMediaPlayerInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.media.IOplusMediaControlManager
            public List<OplusMediaPlayerInfo> getCurrentPlayerInfoList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMediaControlManager.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<OplusMediaPlayerInfo> _result = _reply.createTypedArrayList(OplusMediaPlayerInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.media.IOplusMediaControlManager
            public void sendEvent(OplusMediaEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMediaControlManager.DESCRIPTOR);
                    _data.writeTypedObject(event, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.media.IOplusMediaControlManager
            public void registerEventObserver(IBinder observer, OplusMediaEventFilter filter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMediaControlManager.DESCRIPTOR);
                    _data.writeStrongBinder(observer);
                    _data.writeTypedObject(filter, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.media.IOplusMediaControlManager
            public void unregisterEventObserver(IBinder observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMediaControlManager.DESCRIPTOR);
                    _data.writeStrongBinder(observer);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
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
