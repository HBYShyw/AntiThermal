package com.oplus.commscene;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.commscene.ICommSceneListener;

/* loaded from: classes.dex */
public interface ICommScene extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.commscene.ICommScene";

    int inquireSceneState(int i, int i2) throws RemoteException;

    void listenSceneState(ICommSceneListener iCommSceneListener, int[] iArr, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICommScene {
        @Override // com.oplus.commscene.ICommScene
        public void listenSceneState(ICommSceneListener callback, int[] scenes, boolean addFlag) throws RemoteException {
        }

        @Override // com.oplus.commscene.ICommScene
        public int inquireSceneState(int scene, int phoneId) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICommScene {
        static final int TRANSACTION_inquireSceneState = 2;
        static final int TRANSACTION_listenSceneState = 1;

        public Stub() {
            attachInterface(this, ICommScene.DESCRIPTOR);
        }

        public static ICommScene asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICommScene.DESCRIPTOR);
            if (iin != null && (iin instanceof ICommScene)) {
                return (ICommScene) iin;
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
                    return "listenSceneState";
                case 2:
                    return "inquireSceneState";
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
                data.enforceInterface(ICommScene.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICommScene.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ICommSceneListener _arg0 = ICommSceneListener.Stub.asInterface(data.readStrongBinder());
                            int[] _arg1 = data.createIntArray();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            listenSceneState(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = inquireSceneState(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICommScene {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICommScene.DESCRIPTOR;
            }

            @Override // com.oplus.commscene.ICommScene
            public void listenSceneState(ICommSceneListener callback, int[] scenes, boolean addFlag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICommScene.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    _data.writeIntArray(scenes);
                    _data.writeBoolean(addFlag);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.commscene.ICommScene
            public int inquireSceneState(int scene, int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICommScene.DESCRIPTOR);
                    _data.writeInt(scene);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
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
