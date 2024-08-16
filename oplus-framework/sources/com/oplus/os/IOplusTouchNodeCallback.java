package com.oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusTouchNodeCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.os.IOplusTouchNodeCallback";

    void onTouchNodeChange(int i, long j, int i2, int i3, int i4, String str) throws RemoteException;

    void onTouchNodeChangeOneWay(int i, long j, int i2, int i3, int i4, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusTouchNodeCallback {
        @Override // com.oplus.os.IOplusTouchNodeCallback
        public void onTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusTouchNodeCallback
        public void onTouchNodeChangeOneWay(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusTouchNodeCallback {
        static final int TRANSACTION_onTouchNodeChange = 1;
        static final int TRANSACTION_onTouchNodeChangeOneWay = 2;

        public Stub() {
            attachInterface(this, IOplusTouchNodeCallback.DESCRIPTOR);
        }

        public static IOplusTouchNodeCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusTouchNodeCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusTouchNodeCallback)) {
                return (IOplusTouchNodeCallback) iin;
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
                    return "onTouchNodeChange";
                case 2:
                    return "onTouchNodeChangeOneWay";
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
                data.enforceInterface(IOplusTouchNodeCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusTouchNodeCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            long _arg1 = data.readLong();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            int _arg4 = data.readInt();
                            String _arg5 = data.readString();
                            data.enforceNoDataAvail();
                            onTouchNodeChange(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            long _arg12 = data.readLong();
                            int _arg22 = data.readInt();
                            int _arg32 = data.readInt();
                            int _arg42 = data.readInt();
                            String _arg52 = data.readString();
                            data.enforceNoDataAvail();
                            onTouchNodeChangeOneWay(_arg02, _arg12, _arg22, _arg32, _arg42, _arg52);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusTouchNodeCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusTouchNodeCallback.DESCRIPTOR;
            }

            @Override // com.oplus.os.IOplusTouchNodeCallback
            public void onTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTouchNodeCallback.DESCRIPTOR);
                    _data.writeInt(clientFlag);
                    _data.writeLong(time);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeInt(data);
                    _data.writeString(info);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusTouchNodeCallback
            public void onTouchNodeChangeOneWay(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTouchNodeCallback.DESCRIPTOR);
                    _data.writeInt(clientFlag);
                    _data.writeLong(time);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeInt(data);
                    _data.writeString(info);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
