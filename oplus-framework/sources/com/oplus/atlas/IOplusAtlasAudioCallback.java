package com.oplus.atlas;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusAtlasAudioCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.atlas.IOplusAtlasAudioCallback";

    void onErrorVoiceChanger(int i) throws RemoteException;

    void onPlaybackStateChanged(int i, int i2) throws RemoteException;

    void onRecordingStateChanged(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusAtlasAudioCallback {
        @Override // com.oplus.atlas.IOplusAtlasAudioCallback
        public void onPlaybackStateChanged(int pid, int state) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasAudioCallback
        public void onRecordingStateChanged(int pid, int state) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasAudioCallback
        public void onErrorVoiceChanger(int state) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusAtlasAudioCallback {
        static final int TRANSACTION_onErrorVoiceChanger = 3;
        static final int TRANSACTION_onPlaybackStateChanged = 1;
        static final int TRANSACTION_onRecordingStateChanged = 2;

        public Stub() {
            attachInterface(this, IOplusAtlasAudioCallback.DESCRIPTOR);
        }

        public static IOplusAtlasAudioCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusAtlasAudioCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusAtlasAudioCallback)) {
                return (IOplusAtlasAudioCallback) iin;
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
                    return "onPlaybackStateChanged";
                case 2:
                    return "onRecordingStateChanged";
                case 3:
                    return "onErrorVoiceChanger";
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
                data.enforceInterface(IOplusAtlasAudioCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusAtlasAudioCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onPlaybackStateChanged(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            onRecordingStateChanged(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            onErrorVoiceChanger(_arg03);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusAtlasAudioCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusAtlasAudioCallback.DESCRIPTOR;
            }

            @Override // com.oplus.atlas.IOplusAtlasAudioCallback
            public void onPlaybackStateChanged(int pid, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasAudioCallback.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(state);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasAudioCallback
            public void onRecordingStateChanged(int pid, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasAudioCallback.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(state);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasAudioCallback
            public void onErrorVoiceChanger(int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasAudioCallback.DESCRIPTOR);
                    _data.writeInt(state);
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
