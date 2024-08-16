package com.oplus.commscene;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ICommSceneListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.commscene.ICommSceneListener";

    void onSceneStateChanged(int i, int i2, int i3) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICommSceneListener {
        @Override // com.oplus.commscene.ICommSceneListener
        public void onSceneStateChanged(int scene, int state, int phoneId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICommSceneListener {
        static final int TRANSACTION_onSceneStateChanged = 1;

        public Stub() {
            attachInterface(this, ICommSceneListener.DESCRIPTOR);
        }

        public static ICommSceneListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICommSceneListener.DESCRIPTOR);
            if (iin != null && (iin instanceof ICommSceneListener)) {
                return (ICommSceneListener) iin;
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
                    return "onSceneStateChanged";
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
                data.enforceInterface(ICommSceneListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICommSceneListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            onSceneStateChanged(_arg0, _arg1, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICommSceneListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICommSceneListener.DESCRIPTOR;
            }

            @Override // com.oplus.commscene.ICommSceneListener
            public void onSceneStateChanged(int scene, int state, int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ICommSceneListener.DESCRIPTOR);
                    _data.writeInt(scene);
                    _data.writeInt(state);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
