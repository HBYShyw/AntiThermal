package com.oplus.neuron;

import android.content.ContentValues;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface INeuronSystemEventListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.neuron.INeuronSystemEventListener";

    void onEvent(int i, ContentValues contentValues) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements INeuronSystemEventListener {
        @Override // com.oplus.neuron.INeuronSystemEventListener
        public void onEvent(int type, ContentValues contentValues) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements INeuronSystemEventListener {
        static final int TRANSACTION_onEvent = 1;

        public Stub() {
            attachInterface(this, INeuronSystemEventListener.DESCRIPTOR);
        }

        public static INeuronSystemEventListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(INeuronSystemEventListener.DESCRIPTOR);
            if (iin != null && (iin instanceof INeuronSystemEventListener)) {
                return (INeuronSystemEventListener) iin;
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
                    return "onEvent";
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
                data.enforceInterface(INeuronSystemEventListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(INeuronSystemEventListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            ContentValues _arg1 = (ContentValues) data.readTypedObject(ContentValues.CREATOR);
                            data.enforceNoDataAvail();
                            onEvent(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INeuronSystemEventListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INeuronSystemEventListener.DESCRIPTOR;
            }

            @Override // com.oplus.neuron.INeuronSystemEventListener
            public void onEvent(int type, ContentValues contentValues) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INeuronSystemEventListener.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(contentValues, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
