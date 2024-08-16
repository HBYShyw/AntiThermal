package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IRfcommOemManager extends IInterface {
    public static final String CLIENT_BT_MAC_ADDRESS_KEY = "deviceintegration:rfcomm.extra.MACADDRESS";
    public static final String CLIENT_BT_NAME_KEY = "deviceintegration:rfcomm.extra.NAME";
    public static final String CONNECTED_ACTION_INTENT_NAME = "deviceintegration.rfcomm.ACTION_BT_SOCKET_CONNECTED";
    public static final String DESCRIPTOR = "android.app.IRfcommOemManager";
    public static final String DISCONNECTED_ACTION_INTENT_NAME = "deviceintegration.rfcomm.ACTION_BT_SOCKET_DISCONNECTED";

    boolean closeAllConnections(int i) throws RemoteException;

    boolean closeClientConnections(int i, String str) throws RemoteException;

    boolean disableRfComm(int i, String str) throws RemoteException;

    boolean enableRfComm(int i, String str) throws RemoteException;

    ParcelFileDescriptor getInputParcelFileDescriptor(int i, String str) throws RemoteException;

    ParcelFileDescriptor getOutputParcelFileDescriptor(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IRfcommOemManager {
        @Override // android.app.IRfcommOemManager
        public boolean enableRfComm(int appUID, String uuid) throws RemoteException {
            return false;
        }

        @Override // android.app.IRfcommOemManager
        public boolean disableRfComm(int appUID, String uuid) throws RemoteException {
            return false;
        }

        @Override // android.app.IRfcommOemManager
        public ParcelFileDescriptor getInputParcelFileDescriptor(int appUID, String macAddress) throws RemoteException {
            return null;
        }

        @Override // android.app.IRfcommOemManager
        public ParcelFileDescriptor getOutputParcelFileDescriptor(int appUID, String macAddress) throws RemoteException {
            return null;
        }

        @Override // android.app.IRfcommOemManager
        public boolean closeAllConnections(int appUID) throws RemoteException {
            return false;
        }

        @Override // android.app.IRfcommOemManager
        public boolean closeClientConnections(int appUID, String macAddress) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IRfcommOemManager {
        static final int TRANSACTION_closeAllConnections = 5;
        static final int TRANSACTION_closeClientConnections = 6;
        static final int TRANSACTION_disableRfComm = 2;
        static final int TRANSACTION_enableRfComm = 1;
        static final int TRANSACTION_getInputParcelFileDescriptor = 3;
        static final int TRANSACTION_getOutputParcelFileDescriptor = 4;

        public Stub() {
            attachInterface(this, IRfcommOemManager.DESCRIPTOR);
        }

        public static IRfcommOemManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IRfcommOemManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IRfcommOemManager)) {
                return (IRfcommOemManager) iin;
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
                    return "enableRfComm";
                case 2:
                    return "disableRfComm";
                case 3:
                    return "getInputParcelFileDescriptor";
                case 4:
                    return "getOutputParcelFileDescriptor";
                case 5:
                    return "closeAllConnections";
                case 6:
                    return "closeClientConnections";
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
                data.enforceInterface(IRfcommOemManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IRfcommOemManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result = enableRfComm(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = disableRfComm(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            ParcelFileDescriptor _result3 = getInputParcelFileDescriptor(_arg03, _arg13);
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            ParcelFileDescriptor _result4 = getOutputParcelFileDescriptor(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeTypedObject(_result4, 1);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = closeAllConnections(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            String _arg15 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result6 = closeClientConnections(_arg06, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRfcommOemManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IRfcommOemManager.DESCRIPTOR;
            }

            @Override // android.app.IRfcommOemManager
            public boolean enableRfComm(int appUID, String uuid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRfcommOemManager.DESCRIPTOR);
                    _data.writeInt(appUID);
                    _data.writeString(uuid);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IRfcommOemManager
            public boolean disableRfComm(int appUID, String uuid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRfcommOemManager.DESCRIPTOR);
                    _data.writeInt(appUID);
                    _data.writeString(uuid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IRfcommOemManager
            public ParcelFileDescriptor getInputParcelFileDescriptor(int appUID, String macAddress) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRfcommOemManager.DESCRIPTOR);
                    _data.writeInt(appUID);
                    _data.writeString(macAddress);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    ParcelFileDescriptor _result = (ParcelFileDescriptor) _reply.readTypedObject(ParcelFileDescriptor.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IRfcommOemManager
            public ParcelFileDescriptor getOutputParcelFileDescriptor(int appUID, String macAddress) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRfcommOemManager.DESCRIPTOR);
                    _data.writeInt(appUID);
                    _data.writeString(macAddress);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    ParcelFileDescriptor _result = (ParcelFileDescriptor) _reply.readTypedObject(ParcelFileDescriptor.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IRfcommOemManager
            public boolean closeAllConnections(int appUID) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRfcommOemManager.DESCRIPTOR);
                    _data.writeInt(appUID);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IRfcommOemManager
            public boolean closeClientConnections(int appUID, String macAddress) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRfcommOemManager.DESCRIPTOR);
                    _data.writeInt(appUID);
                    _data.writeString(macAddress);
                    this.mRemote.transact(6, _data, _reply, 0);
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
            return 5;
        }
    }
}
