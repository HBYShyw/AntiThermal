package com.oplus.nas.cybersense.sdk;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.nas.cybersense.sdk.ICyberSenseCallback;

/* loaded from: classes.dex */
public interface ICyberSense extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.nas.cybersense.sdk.ICyberSense";

    int listenCyberSenseEvent(String str, ICyberSenseCallback iCyberSenseCallback, EventConfig eventConfig) throws RemoteException;

    Bundle mockCmd(int i, Bundle bundle) throws RemoteException;

    void reportNetworkOptimizeResult(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICyberSense {
        @Override // com.oplus.nas.cybersense.sdk.ICyberSense
        public int listenCyberSenseEvent(String pkgName, ICyberSenseCallback cb, EventConfig config) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.nas.cybersense.sdk.ICyberSense
        public void reportNetworkOptimizeResult(String result) throws RemoteException {
        }

        @Override // com.oplus.nas.cybersense.sdk.ICyberSense
        public Bundle mockCmd(int type, Bundle data) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICyberSense {
        static final int TRANSACTION_listenCyberSenseEvent = 2;
        static final int TRANSACTION_mockCmd = 4;
        static final int TRANSACTION_reportNetworkOptimizeResult = 3;

        public Stub() {
            attachInterface(this, ICyberSense.DESCRIPTOR);
        }

        public static ICyberSense asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICyberSense.DESCRIPTOR);
            if (iin != null && (iin instanceof ICyberSense)) {
                return (ICyberSense) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 2:
                    return "listenCyberSenseEvent";
                case 3:
                    return "reportNetworkOptimizeResult";
                case 4:
                    return "mockCmd";
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
                data.enforceInterface(ICyberSense.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICyberSense.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 2:
                            String _arg0 = data.readString();
                            ICyberSenseCallback _arg1 = ICyberSenseCallback.Stub.asInterface(data.readStrongBinder());
                            EventConfig _arg2 = (EventConfig) data.readTypedObject(EventConfig.CREATOR);
                            data.enforceNoDataAvail();
                            int _result = listenCyberSenseEvent(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 3:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            reportNetworkOptimizeResult(_arg02);
                            reply.writeNoException();
                            return true;
                        case 4:
                            int _arg03 = data.readInt();
                            Bundle _arg12 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result2 = mockCmd(_arg03, _arg12);
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICyberSense {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICyberSense.DESCRIPTOR;
            }

            @Override // com.oplus.nas.cybersense.sdk.ICyberSense
            public int listenCyberSenseEvent(String pkgName, ICyberSenseCallback cb, EventConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICyberSense.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongInterface(cb);
                    _data.writeTypedObject(config, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nas.cybersense.sdk.ICyberSense
            public void reportNetworkOptimizeResult(String result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICyberSense.DESCRIPTOR);
                    _data.writeString(result);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nas.cybersense.sdk.ICyberSense
            public Bundle mockCmd(int type, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICyberSense.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
