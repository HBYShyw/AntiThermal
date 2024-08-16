package com.oplus.screenshot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.oplus.Telephony;
import com.oplus.screenshot.IOplusLongshotCallback;

/* loaded from: classes.dex */
public interface IOplusLongshot extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenshot.IOplusLongshot";

    void notifyOverScroll(OplusLongshotEvent oplusLongshotEvent) throws RemoteException;

    void start(IOplusLongshotCallback iOplusLongshotCallback) throws RemoteException;

    void stop() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusLongshot {
        @Override // com.oplus.screenshot.IOplusLongshot
        public void start(IOplusLongshotCallback callback) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusLongshot
        public void stop() throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusLongshot
        public void notifyOverScroll(OplusLongshotEvent event) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusLongshot {
        static final int TRANSACTION_notifyOverScroll = 3;
        static final int TRANSACTION_start = 1;
        static final int TRANSACTION_stop = 2;

        public Stub() {
            attachInterface(this, IOplusLongshot.DESCRIPTOR);
        }

        public static IOplusLongshot asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusLongshot.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusLongshot)) {
                return (IOplusLongshot) iin;
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
                    return Telephony.BaseMmsColumns.START;
                case 2:
                    return "stop";
                case 3:
                    return "notifyOverScroll";
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
                data.enforceInterface(IOplusLongshot.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusLongshot.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusLongshotCallback _arg0 = IOplusLongshotCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            start(_arg0);
                            return true;
                        case 2:
                            stop();
                            return true;
                        case 3:
                            OplusLongshotEvent _arg02 = (OplusLongshotEvent) data.readTypedObject(OplusLongshotEvent.CREATOR);
                            data.enforceNoDataAvail();
                            notifyOverScroll(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusLongshot {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusLongshot.DESCRIPTOR;
            }

            @Override // com.oplus.screenshot.IOplusLongshot
            public void start(IOplusLongshotCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusLongshot.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusLongshot
            public void stop() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusLongshot.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusLongshot
            public void notifyOverScroll(OplusLongshotEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusLongshot.DESCRIPTOR);
                    _data.writeTypedObject(event, 0);
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
