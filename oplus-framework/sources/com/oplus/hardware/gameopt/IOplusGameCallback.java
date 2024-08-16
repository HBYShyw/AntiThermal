package com.oplus.hardware.gameopt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusGameCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.hardware.gameopt.IOplusGameCallback";

    void onBigDataReport(String str) throws RemoteException;

    void onCommonCall(String str, int i) throws RemoteException;

    void onFrameLimit(String str) throws RemoteException;

    void onFrameProduce(String str) throws RemoteException;

    void onInputCollect(String str) throws RemoteException;

    void onNotify(String str) throws RemoteException;

    void onNotifyTemp(String str) throws RemoteException;

    void onReadTop(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusGameCallback {
        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onNotify(String info) throws RemoteException {
        }

        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onReadTop(String info) throws RemoteException {
        }

        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onFrameLimit(String info) throws RemoteException {
        }

        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onBigDataReport(String info) throws RemoteException {
        }

        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onNotifyTemp(String info) throws RemoteException {
        }

        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onFrameProduce(String info) throws RemoteException {
        }

        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onInputCollect(String info) throws RemoteException {
        }

        @Override // com.oplus.hardware.gameopt.IOplusGameCallback
        public void onCommonCall(String info, int type) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusGameCallback {
        static final int TRANSACTION_onBigDataReport = 4;
        static final int TRANSACTION_onCommonCall = 8;
        static final int TRANSACTION_onFrameLimit = 3;
        static final int TRANSACTION_onFrameProduce = 6;
        static final int TRANSACTION_onInputCollect = 7;
        static final int TRANSACTION_onNotify = 1;
        static final int TRANSACTION_onNotifyTemp = 5;
        static final int TRANSACTION_onReadTop = 2;

        public Stub() {
            attachInterface(this, IOplusGameCallback.DESCRIPTOR);
        }

        public static IOplusGameCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusGameCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusGameCallback)) {
                return (IOplusGameCallback) iin;
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
                    return "onNotify";
                case 2:
                    return "onReadTop";
                case 3:
                    return "onFrameLimit";
                case 4:
                    return "onBigDataReport";
                case 5:
                    return "onNotifyTemp";
                case 6:
                    return "onFrameProduce";
                case 7:
                    return "onInputCollect";
                case 8:
                    return "onCommonCall";
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
                data.enforceInterface(IOplusGameCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusGameCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            onNotify(_arg0);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            onReadTop(_arg02);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            onFrameLimit(_arg03);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            onBigDataReport(_arg04);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            onNotifyTemp(_arg05);
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            onFrameProduce(_arg06);
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            data.enforceNoDataAvail();
                            onInputCollect(_arg07);
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onCommonCall(_arg08, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusGameCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusGameCallback.DESCRIPTOR;
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onNotify(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onReadTop(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onFrameLimit(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onBigDataReport(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onNotifyTemp(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onFrameProduce(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onInputCollect(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.gameopt.IOplusGameCallback
            public void onCommonCall(String info, int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGameCallback.DESCRIPTOR);
                    _data.writeString(info);
                    _data.writeInt(type);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 7;
        }
    }
}
