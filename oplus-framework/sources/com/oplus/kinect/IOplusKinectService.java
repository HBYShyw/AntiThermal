package com.oplus.kinect;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.kinect.IOplusKinectObserver;

/* loaded from: classes.dex */
public interface IOplusKinectService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.kinect.IOplusKinectService";

    boolean registerKinectObserver(IOplusKinectObserver iOplusKinectObserver, int i) throws RemoteException;

    boolean unregisterKinectObserver(IOplusKinectObserver iOplusKinectObserver) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusKinectService {
        @Override // com.oplus.kinect.IOplusKinectService
        public boolean registerKinectObserver(IOplusKinectObserver observer, int config) throws RemoteException {
            return false;
        }

        @Override // com.oplus.kinect.IOplusKinectService
        public boolean unregisterKinectObserver(IOplusKinectObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusKinectService {
        static final int TRANSACTION_registerKinectObserver = 1;
        static final int TRANSACTION_unregisterKinectObserver = 2;

        public Stub() {
            attachInterface(this, IOplusKinectService.DESCRIPTOR);
        }

        public static IOplusKinectService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusKinectService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusKinectService)) {
                return (IOplusKinectService) iin;
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
                    return "registerKinectObserver";
                case 2:
                    return "unregisterKinectObserver";
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
                data.enforceInterface(IOplusKinectService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusKinectService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusKinectObserver _arg0 = IOplusKinectObserver.Stub.asInterface(data.readStrongBinder());
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = registerKinectObserver(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            IOplusKinectObserver _arg02 = IOplusKinectObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result2 = unregisterKinectObserver(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusKinectService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusKinectService.DESCRIPTOR;
            }

            @Override // com.oplus.kinect.IOplusKinectService
            public boolean registerKinectObserver(IOplusKinectObserver observer, int config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusKinectService.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    _data.writeInt(config);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.kinect.IOplusKinectService
            public boolean unregisterKinectObserver(IOplusKinectObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusKinectService.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(2, _data, _reply, 0);
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
            return 1;
        }
    }
}
