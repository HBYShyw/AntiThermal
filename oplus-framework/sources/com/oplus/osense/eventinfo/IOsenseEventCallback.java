package com.oplus.osense.eventinfo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOsenseEventCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.osense.eventinfo.IOsenseEventCallback";

    void onEventSceneChanged(OsenseEventResult osenseEventResult) throws RemoteException;

    void onProcessTerminate(String str) throws RemoteException;

    void onRequestTerminate(int i, String str) throws RemoteException;

    void onTerminateStateChanged(int i, int i2, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOsenseEventCallback {
        @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
        public void onEventSceneChanged(OsenseEventResult osenseEventResult) throws RemoteException {
        }

        @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
        public void onTerminateStateChanged(int pid, int uid, boolean isRegister) throws RemoteException {
        }

        @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
        public void onRequestTerminate(int pid, String reason) throws RemoteException {
        }

        @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
        public void onProcessTerminate(String reason) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOsenseEventCallback {
        static final int TRANSACTION_onEventSceneChanged = 1;
        static final int TRANSACTION_onProcessTerminate = 4;
        static final int TRANSACTION_onRequestTerminate = 3;
        static final int TRANSACTION_onTerminateStateChanged = 2;

        public Stub() {
            attachInterface(this, IOsenseEventCallback.DESCRIPTOR);
        }

        public static IOsenseEventCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOsenseEventCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOsenseEventCallback)) {
                return (IOsenseEventCallback) iin;
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
                    return "onEventSceneChanged";
                case 2:
                    return "onTerminateStateChanged";
                case 3:
                    return "onRequestTerminate";
                case 4:
                    return "onProcessTerminate";
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
                data.enforceInterface(IOsenseEventCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOsenseEventCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OsenseEventResult _arg0 = (OsenseEventResult) data.readTypedObject(OsenseEventResult.CREATOR);
                            data.enforceNoDataAvail();
                            onEventSceneChanged(_arg0);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg1 = data.readInt();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onTerminateStateChanged(_arg02, _arg1, _arg2);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            onRequestTerminate(_arg03, _arg12);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            onProcessTerminate(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOsenseEventCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOsenseEventCallback.DESCRIPTOR;
            }

            @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
            public void onEventSceneChanged(OsenseEventResult osenseEventResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseEventCallback.DESCRIPTOR);
                    _data.writeTypedObject(osenseEventResult, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
            public void onTerminateStateChanged(int pid, int uid, boolean isRegister) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseEventCallback.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeBoolean(isRegister);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
            public void onRequestTerminate(int pid, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseEventCallback.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeString(reason);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
            public void onProcessTerminate(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseEventCallback.DESCRIPTOR);
                    _data.writeString(reason);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
