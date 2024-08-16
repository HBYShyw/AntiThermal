package com.android.internal.policy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusOSKeyguardService extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.policy.IOplusOSKeyguardService";

    void onKeyguardDoneForOplus(boolean z) throws RemoteException;

    void sendCommandToApps(String str) throws RemoteException;

    void setNotificationListener(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusOSKeyguardService {
        @Override // com.android.internal.policy.IOplusOSKeyguardService
        public void onKeyguardDoneForOplus(boolean keyguardDone) throws RemoteException {
        }

        @Override // com.android.internal.policy.IOplusOSKeyguardService
        public void setNotificationListener(boolean isChanged) throws RemoteException {
        }

        @Override // com.android.internal.policy.IOplusOSKeyguardService
        public void sendCommandToApps(String command) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusOSKeyguardService {
        static final int TRANSACTION_onKeyguardDoneForOplus = 1;
        static final int TRANSACTION_sendCommandToApps = 3;
        static final int TRANSACTION_setNotificationListener = 2;

        public Stub() {
            attachInterface(this, IOplusOSKeyguardService.DESCRIPTOR);
        }

        public static IOplusOSKeyguardService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusOSKeyguardService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusOSKeyguardService)) {
                return (IOplusOSKeyguardService) iin;
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
                    return "onKeyguardDoneForOplus";
                case 2:
                    return "setNotificationListener";
                case 3:
                    return "sendCommandToApps";
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
                data.enforceInterface(IOplusOSKeyguardService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusOSKeyguardService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onKeyguardDoneForOplus(_arg0);
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setNotificationListener(_arg02);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            sendCommandToApps(_arg03);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusOSKeyguardService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusOSKeyguardService.DESCRIPTOR;
            }

            @Override // com.android.internal.policy.IOplusOSKeyguardService
            public void onKeyguardDoneForOplus(boolean keyguardDone) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusOSKeyguardService.DESCRIPTOR);
                    _data.writeBoolean(keyguardDone);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IOplusOSKeyguardService
            public void setNotificationListener(boolean isChanged) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusOSKeyguardService.DESCRIPTOR);
                    _data.writeBoolean(isChanged);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IOplusOSKeyguardService
            public void sendCommandToApps(String command) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOSKeyguardService.DESCRIPTOR);
                    _data.writeString(command);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
