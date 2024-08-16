package com.oplus.putt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.putt.IPuttServerCallback;

/* loaded from: classes.dex */
public interface IPuttClientCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.putt.IPuttClientCallback";

    void onPuttEnter(OplusPuttEnterInfo oplusPuttEnterInfo) throws RemoteException;

    void onPuttExit(OplusPuttExitInfo oplusPuttExitInfo) throws RemoteException;

    void onPuttReplace(OplusPuttEnterInfo oplusPuttEnterInfo, int i) throws RemoteException;

    void onPuttStarted(PuttParams puttParams) throws RemoteException;

    void onPuttTaskShown(OplusPuttEnterInfo oplusPuttEnterInfo) throws RemoteException;

    void setPuttManager(IPuttServerCallback iPuttServerCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPuttClientCallback {
        @Override // com.oplus.putt.IPuttClientCallback
        public void setPuttManager(IPuttServerCallback manager) throws RemoteException {
        }

        @Override // com.oplus.putt.IPuttClientCallback
        public void onPuttStarted(PuttParams params) throws RemoteException {
        }

        @Override // com.oplus.putt.IPuttClientCallback
        public void onPuttEnter(OplusPuttEnterInfo info) throws RemoteException {
        }

        @Override // com.oplus.putt.IPuttClientCallback
        public void onPuttTaskShown(OplusPuttEnterInfo info) throws RemoteException {
        }

        @Override // com.oplus.putt.IPuttClientCallback
        public void onPuttReplace(OplusPuttEnterInfo info, int outTaskId) throws RemoteException {
        }

        @Override // com.oplus.putt.IPuttClientCallback
        public void onPuttExit(OplusPuttExitInfo info) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPuttClientCallback {
        static final int TRANSACTION_onPuttEnter = 3;
        static final int TRANSACTION_onPuttExit = 6;
        static final int TRANSACTION_onPuttReplace = 5;
        static final int TRANSACTION_onPuttStarted = 2;
        static final int TRANSACTION_onPuttTaskShown = 4;
        static final int TRANSACTION_setPuttManager = 1;

        public Stub() {
            attachInterface(this, IPuttClientCallback.DESCRIPTOR);
        }

        public static IPuttClientCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPuttClientCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IPuttClientCallback)) {
                return (IPuttClientCallback) iin;
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
                    return "setPuttManager";
                case 2:
                    return "onPuttStarted";
                case 3:
                    return "onPuttEnter";
                case 4:
                    return "onPuttTaskShown";
                case 5:
                    return "onPuttReplace";
                case 6:
                    return "onPuttExit";
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
                data.enforceInterface(IPuttClientCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IPuttClientCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IPuttServerCallback _arg0 = IPuttServerCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setPuttManager(_arg0);
                            return true;
                        case 2:
                            PuttParams _arg02 = (PuttParams) data.readTypedObject(PuttParams.CREATOR);
                            data.enforceNoDataAvail();
                            onPuttStarted(_arg02);
                            return true;
                        case 3:
                            OplusPuttEnterInfo _arg03 = (OplusPuttEnterInfo) data.readTypedObject(OplusPuttEnterInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onPuttEnter(_arg03);
                            return true;
                        case 4:
                            OplusPuttEnterInfo _arg04 = (OplusPuttEnterInfo) data.readTypedObject(OplusPuttEnterInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onPuttTaskShown(_arg04);
                            return true;
                        case 5:
                            OplusPuttEnterInfo _arg05 = (OplusPuttEnterInfo) data.readTypedObject(OplusPuttEnterInfo.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onPuttReplace(_arg05, _arg1);
                            return true;
                        case 6:
                            OplusPuttExitInfo _arg06 = (OplusPuttExitInfo) data.readTypedObject(OplusPuttExitInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onPuttExit(_arg06);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPuttClientCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPuttClientCallback.DESCRIPTOR;
            }

            @Override // com.oplus.putt.IPuttClientCallback
            public void setPuttManager(IPuttServerCallback manager) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttClientCallback.DESCRIPTOR);
                    _data.writeStrongInterface(manager);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttClientCallback
            public void onPuttStarted(PuttParams params) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttClientCallback.DESCRIPTOR);
                    _data.writeTypedObject(params, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttClientCallback
            public void onPuttEnter(OplusPuttEnterInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttClientCallback.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttClientCallback
            public void onPuttTaskShown(OplusPuttEnterInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttClientCallback.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttClientCallback
            public void onPuttReplace(OplusPuttEnterInfo info, int outTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttClientCallback.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(outTaskId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttClientCallback
            public void onPuttExit(OplusPuttExitInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttClientCallback.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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
