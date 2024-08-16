package com.oplus.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOemHookCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.telephony.IOemHookCallback";

    void onAtCmdResp(int i, long j, String str) throws RemoteException;

    void onAtUrcInd(int i, String str) throws RemoteException;

    void onError(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOemHookCallback {
        @Override // com.oplus.telephony.IOemHookCallback
        public void onAtCmdResp(int slotId, long token, String atCmd) throws RemoteException {
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onAtUrcInd(int slotId, String atCmd) throws RemoteException {
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onError(String e) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOemHookCallback {
        static final int TRANSACTION_onAtCmdResp = 1;
        static final int TRANSACTION_onAtUrcInd = 2;
        static final int TRANSACTION_onError = 3;

        public Stub() {
            attachInterface(this, IOemHookCallback.DESCRIPTOR);
        }

        public static IOemHookCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOemHookCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOemHookCallback)) {
                return (IOemHookCallback) iin;
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
                    return "onAtCmdResp";
                case 2:
                    return "onAtUrcInd";
                case 3:
                    return "onError";
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
                data.enforceInterface(IOemHookCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOemHookCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            long _arg1 = data.readLong();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            onAtCmdResp(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            onAtUrcInd(_arg02, _arg12);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            onError(_arg03);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOemHookCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOemHookCallback.DESCRIPTOR;
            }

            @Override // com.oplus.telephony.IOemHookCallback
            public void onAtCmdResp(int slotId, long token, String atCmd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOemHookCallback.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeLong(token);
                    _data.writeString(atCmd);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.telephony.IOemHookCallback
            public void onAtUrcInd(int slotId, String atCmd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOemHookCallback.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(atCmd);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.telephony.IOemHookCallback
            public void onError(String e) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOemHookCallback.DESCRIPTOR);
                    _data.writeString(e);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
