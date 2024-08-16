package android.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusGeoFenceEventCallBack extends IInterface {
    public static final String DESCRIPTOR = "android.telephony.IOplusGeoFenceEventCallBack";

    String getCause() throws RemoteException;

    String getCustomerName() throws RemoteException;

    int getSlot() throws RemoteException;

    void onLteGameDelayFenceEvent(int i, int i2) throws RemoteException;

    void onNrDumpFenceEvent(int i, int i2) throws RemoteException;

    void onNrSaDataSlowFenceEvent(int i, int i2) throws RemoteException;

    void onNrSaWeakFenceEvent(int i, int i2) throws RemoteException;

    void onSaGameDelayFenceEvent(int i, int i2) throws RemoteException;

    void onSaLteChangeGameDelayFenceEvent(int i, int i2) throws RemoteException;

    void setCustomerName(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusGeoFenceEventCallBack {
        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public int getSlot() throws RemoteException {
            return 0;
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public String getCause() throws RemoteException {
            return null;
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public String getCustomerName() throws RemoteException {
            return null;
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void setCustomerName(String name) throws RemoteException {
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onNrDumpFenceEvent(int slotId, int evnet) throws RemoteException {
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onNrSaWeakFenceEvent(int slotId, int event) throws RemoteException {
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onNrSaDataSlowFenceEvent(int slotId, int event) throws RemoteException {
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onSaGameDelayFenceEvent(int slotId, int event) throws RemoteException {
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onLteGameDelayFenceEvent(int slotId, int event) throws RemoteException {
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onSaLteChangeGameDelayFenceEvent(int slotId, int event) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusGeoFenceEventCallBack {
        static final int TRANSACTION_getCause = 2;
        static final int TRANSACTION_getCustomerName = 3;
        static final int TRANSACTION_getSlot = 1;
        static final int TRANSACTION_onLteGameDelayFenceEvent = 9;
        static final int TRANSACTION_onNrDumpFenceEvent = 5;
        static final int TRANSACTION_onNrSaDataSlowFenceEvent = 7;
        static final int TRANSACTION_onNrSaWeakFenceEvent = 6;
        static final int TRANSACTION_onSaGameDelayFenceEvent = 8;
        static final int TRANSACTION_onSaLteChangeGameDelayFenceEvent = 10;
        static final int TRANSACTION_setCustomerName = 4;

        public Stub() {
            attachInterface(this, IOplusGeoFenceEventCallBack.DESCRIPTOR);
        }

        public static IOplusGeoFenceEventCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusGeoFenceEventCallBack.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusGeoFenceEventCallBack)) {
                return (IOplusGeoFenceEventCallBack) iin;
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
                    return "getSlot";
                case 2:
                    return "getCause";
                case 3:
                    return "getCustomerName";
                case 4:
                    return "setCustomerName";
                case 5:
                    return "onNrDumpFenceEvent";
                case 6:
                    return "onNrSaWeakFenceEvent";
                case 7:
                    return "onNrSaDataSlowFenceEvent";
                case 8:
                    return "onSaGameDelayFenceEvent";
                case 9:
                    return "onLteGameDelayFenceEvent";
                case 10:
                    return "onSaLteChangeGameDelayFenceEvent";
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
                data.enforceInterface(IOplusGeoFenceEventCallBack.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _result = getSlot();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            String _result2 = getCause();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            String _result3 = getCustomerName();
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 4:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            setCustomerName(_arg0);
                            reply.writeNoException();
                            return true;
                        case 5:
                            int _arg02 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onNrDumpFenceEvent(_arg02, _arg1);
                            reply.writeNoException();
                            return true;
                        case 6:
                            int _arg03 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            onNrSaWeakFenceEvent(_arg03, _arg12);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _arg04 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            onNrSaDataSlowFenceEvent(_arg04, _arg13);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg05 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            onSaGameDelayFenceEvent(_arg05, _arg14);
                            reply.writeNoException();
                            return true;
                        case 9:
                            int _arg06 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            onLteGameDelayFenceEvent(_arg06, _arg15);
                            reply.writeNoException();
                            return true;
                        case 10:
                            int _arg07 = data.readInt();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            onSaLteChangeGameDelayFenceEvent(_arg07, _arg16);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusGeoFenceEventCallBack {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusGeoFenceEventCallBack.DESCRIPTOR;
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public int getSlot() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public String getCause() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public String getCustomerName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public void setCustomerName(String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public void onNrDumpFenceEvent(int slotId, int evnet) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(evnet);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public void onNrSaWeakFenceEvent(int slotId, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(event);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public void onNrSaDataSlowFenceEvent(int slotId, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(event);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public void onSaGameDelayFenceEvent(int slotId, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(event);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public void onLteGameDelayFenceEvent(int slotId, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(event);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.IOplusGeoFenceEventCallBack
            public void onSaLteChangeGameDelayFenceEvent(int slotId, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusGeoFenceEventCallBack.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(event);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
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
