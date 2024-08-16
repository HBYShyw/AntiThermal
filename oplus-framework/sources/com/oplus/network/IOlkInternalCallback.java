package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOlkInternalCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOlkInternalCallback";

    void olkClearSocketPriorityCallback(String str, OlkStreamParam olkStreamParam) throws RemoteException;

    void olkSetApBandwithCallback(String str, int i, int i2) throws RemoteException;

    void olkSetApStateCallback(String str, boolean z) throws RemoteException;

    void olkSetRealTimeEventCallback(String str, int i) throws RemoteException;

    void olkSetSocketPriorityCallback(String str, OlkStreamParam olkStreamParam) throws RemoteException;

    void olkUpdateCellularEnable(String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOlkInternalCallback {
        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetApStateCallback(String pkgName, boolean delay) throws RemoteException {
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetApBandwithCallback(String pkgName, int rxBw, int txBw) throws RemoteException {
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetSocketPriorityCallback(String pkgName, OlkStreamParam param) throws RemoteException {
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkClearSocketPriorityCallback(String pkgName, OlkStreamParam param) throws RemoteException {
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetRealTimeEventCallback(String pkgName, int event) throws RemoteException {
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkUpdateCellularEnable(String pkgName, boolean enable) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOlkInternalCallback {
        static final int TRANSACTION_olkClearSocketPriorityCallback = 4;
        static final int TRANSACTION_olkSetApBandwithCallback = 2;
        static final int TRANSACTION_olkSetApStateCallback = 1;
        static final int TRANSACTION_olkSetRealTimeEventCallback = 5;
        static final int TRANSACTION_olkSetSocketPriorityCallback = 3;
        static final int TRANSACTION_olkUpdateCellularEnable = 6;

        public Stub() {
            attachInterface(this, IOlkInternalCallback.DESCRIPTOR);
        }

        public static IOlkInternalCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOlkInternalCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOlkInternalCallback)) {
                return (IOlkInternalCallback) iin;
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
                    return "olkSetApStateCallback";
                case 2:
                    return "olkSetApBandwithCallback";
                case 3:
                    return "olkSetSocketPriorityCallback";
                case 4:
                    return "olkClearSocketPriorityCallback";
                case 5:
                    return "olkSetRealTimeEventCallback";
                case 6:
                    return "olkUpdateCellularEnable";
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
                data.enforceInterface(IOlkInternalCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOlkInternalCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            olkSetApStateCallback(_arg0, _arg1);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            int _arg12 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            olkSetApBandwithCallback(_arg02, _arg12, _arg2);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            OlkStreamParam _arg13 = (OlkStreamParam) data.readTypedObject(OlkStreamParam.CREATOR);
                            data.enforceNoDataAvail();
                            olkSetSocketPriorityCallback(_arg03, _arg13);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            OlkStreamParam _arg14 = (OlkStreamParam) data.readTypedObject(OlkStreamParam.CREATOR);
                            data.enforceNoDataAvail();
                            olkClearSocketPriorityCallback(_arg04, _arg14);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            olkSetRealTimeEventCallback(_arg05, _arg15);
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            boolean _arg16 = data.readBoolean();
                            data.enforceNoDataAvail();
                            olkUpdateCellularEnable(_arg06, _arg16);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOlkInternalCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOlkInternalCallback.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOlkInternalCallback
            public void olkSetApStateCallback(String pkgName, boolean delay) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOlkInternalCallback.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeBoolean(delay);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlkInternalCallback
            public void olkSetApBandwithCallback(String pkgName, int rxBw, int txBw) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOlkInternalCallback.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(rxBw);
                    _data.writeInt(txBw);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlkInternalCallback
            public void olkSetSocketPriorityCallback(String pkgName, OlkStreamParam param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOlkInternalCallback.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeTypedObject(param, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlkInternalCallback
            public void olkClearSocketPriorityCallback(String pkgName, OlkStreamParam param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOlkInternalCallback.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeTypedObject(param, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlkInternalCallback
            public void olkSetRealTimeEventCallback(String pkgName, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOlkInternalCallback.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(event);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlkInternalCallback
            public void olkUpdateCellularEnable(String pkgName, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOlkInternalCallback.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 5;
        }
    }
}
