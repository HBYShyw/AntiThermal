package com.oplus.nec;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOnNecEventListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.nec.IOnNecEventListener";

    String onCollectPwrStatistic(boolean z) throws RemoteException;

    void onNecEventReport(int i, int i2, Bundle bundle) throws RemoteException;

    void onStandbyStart(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOnNecEventListener {
        @Override // com.oplus.nec.IOnNecEventListener
        public void onNecEventReport(int slotId, int eventId, Bundle data) throws RemoteException {
        }

        @Override // com.oplus.nec.IOnNecEventListener
        public void onStandbyStart(boolean disabled) throws RemoteException {
        }

        @Override // com.oplus.nec.IOnNecEventListener
        public String onCollectPwrStatistic(boolean cleanup) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOnNecEventListener {
        static final int TRANSACTION_onCollectPwrStatistic = 3;
        static final int TRANSACTION_onNecEventReport = 1;
        static final int TRANSACTION_onStandbyStart = 2;

        public Stub() {
            attachInterface(this, IOnNecEventListener.DESCRIPTOR);
        }

        public static IOnNecEventListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOnNecEventListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOnNecEventListener)) {
                return (IOnNecEventListener) iin;
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
                    return "onNecEventReport";
                case 2:
                    return "onStandbyStart";
                case 3:
                    return "onCollectPwrStatistic";
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
                data.enforceInterface(IOnNecEventListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOnNecEventListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onNecEventReport(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onStandbyStart(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            boolean _arg03 = data.readBoolean();
                            data.enforceNoDataAvail();
                            String _result = onCollectPwrStatistic(_arg03);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOnNecEventListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOnNecEventListener.DESCRIPTOR;
            }

            @Override // com.oplus.nec.IOnNecEventListener
            public void onNecEventReport(int slotId, int eventId, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOnNecEventListener.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(eventId);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOnNecEventListener
            public void onStandbyStart(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOnNecEventListener.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOnNecEventListener
            public String onCollectPwrStatistic(boolean cleanup) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOnNecEventListener.DESCRIPTOR);
                    _data.writeBoolean(cleanup);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
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
