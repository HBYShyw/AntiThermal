package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOppoMotorManager extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.app.IOppoMotorManager";

    void breathLedLoopEffect(int i) throws RemoteException;

    int downMotorByPrivacyApp(String str, int i, IBinder iBinder) throws RemoteException;

    int downMotorBySystemApp(String str, IBinder iBinder) throws RemoteException;

    int getMotorStateBySystemApp() throws RemoteException;

    int upMotorBySystemApp(String str, IBinder iBinder) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOppoMotorManager {
        @Override // com.android.internal.app.IOppoMotorManager
        public int getMotorStateBySystemApp() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.app.IOppoMotorManager
        public int downMotorBySystemApp(String tag, IBinder token) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.app.IOppoMotorManager
        public int upMotorBySystemApp(String tag, IBinder token) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.app.IOppoMotorManager
        public int downMotorByPrivacyApp(String tag, int delayDownTime, IBinder token) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.app.IOppoMotorManager
        public void breathLedLoopEffect(int effect) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOppoMotorManager {
        static final int TRANSACTION_breathLedLoopEffect = 5;
        static final int TRANSACTION_downMotorByPrivacyApp = 4;
        static final int TRANSACTION_downMotorBySystemApp = 2;
        static final int TRANSACTION_getMotorStateBySystemApp = 1;
        static final int TRANSACTION_upMotorBySystemApp = 3;

        public Stub() {
            attachInterface(this, IOppoMotorManager.DESCRIPTOR);
        }

        public static IOppoMotorManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOppoMotorManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOppoMotorManager)) {
                return (IOppoMotorManager) iin;
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
                    return "getMotorStateBySystemApp";
                case 2:
                    return "downMotorBySystemApp";
                case 3:
                    return "upMotorBySystemApp";
                case 4:
                    return "downMotorByPrivacyApp";
                case 5:
                    return "breathLedLoopEffect";
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
                data.enforceInterface(IOppoMotorManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOppoMotorManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _result = getMotorStateBySystemApp();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            String _arg0 = data.readString();
                            IBinder _arg1 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            int _result2 = downMotorBySystemApp(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            String _arg02 = data.readString();
                            IBinder _arg12 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            int _result3 = upMotorBySystemApp(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            String _arg03 = data.readString();
                            int _arg13 = data.readInt();
                            IBinder _arg2 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            int _result4 = downMotorByPrivacyApp(_arg03, _arg13, _arg2);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 5:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            breathLedLoopEffect(_arg04);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOppoMotorManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOppoMotorManager.DESCRIPTOR;
            }

            @Override // com.android.internal.app.IOppoMotorManager
            public int getMotorStateBySystemApp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOppoMotorManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IOppoMotorManager
            public int downMotorBySystemApp(String tag, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOppoMotorManager.DESCRIPTOR);
                    _data.writeString(tag);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IOppoMotorManager
            public int upMotorBySystemApp(String tag, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOppoMotorManager.DESCRIPTOR);
                    _data.writeString(tag);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IOppoMotorManager
            public int downMotorByPrivacyApp(String tag, int delayDownTime, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOppoMotorManager.DESCRIPTOR);
                    _data.writeString(tag);
                    _data.writeInt(delayDownTime);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IOppoMotorManager
            public void breathLedLoopEffect(int effect) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOppoMotorManager.DESCRIPTOR);
                    _data.writeInt(effect);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 4;
        }
    }
}
