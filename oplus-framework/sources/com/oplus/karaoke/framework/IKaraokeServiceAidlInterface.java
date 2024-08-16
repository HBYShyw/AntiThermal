package com.oplus.karaoke.framework;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IKaraokeServiceAidlInterface extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.karaoke.framework.IKaraokeServiceAidlInterface";

    int getVersion() throws RemoteException;

    void resetActiveClient(String str, boolean z) throws RemoteException;

    void setActiveClient(String str) throws RemoteException;

    void setAudioLoopbackOn(boolean z) throws RemoteException;

    void setEqualizerType(int i) throws RemoteException;

    void setHeadsetState(boolean z) throws RemoteException;

    void setMixSoundType(int i) throws RemoteException;

    void setPermitBits(int i, int i2, int i3, String str) throws RemoteException;

    void setRecordingWetSound(boolean z) throws RemoteException;

    void setTones(int i) throws RemoteException;

    void setVolume(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IKaraokeServiceAidlInterface {
        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setHeadsetState(boolean connected) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setActiveClient(String pkgName) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setPermitBits(int authByteArrayToInt, int pid, int uid, String pkgName) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void resetActiveClient(String pkgName, boolean isShowDialog) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public int getVersion() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setRecordingWetSound(boolean isWetSound) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setVolume(int volume) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setEqualizerType(int type) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setMixSoundType(int soundtype) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setTones(int tone) throws RemoteException {
        }

        @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
        public void setAudioLoopbackOn(boolean on) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IKaraokeServiceAidlInterface {
        static final int TRANSACTION_getVersion = 5;
        static final int TRANSACTION_resetActiveClient = 4;
        static final int TRANSACTION_setActiveClient = 2;
        static final int TRANSACTION_setAudioLoopbackOn = 11;
        static final int TRANSACTION_setEqualizerType = 8;
        static final int TRANSACTION_setHeadsetState = 1;
        static final int TRANSACTION_setMixSoundType = 9;
        static final int TRANSACTION_setPermitBits = 3;
        static final int TRANSACTION_setRecordingWetSound = 6;
        static final int TRANSACTION_setTones = 10;
        static final int TRANSACTION_setVolume = 7;

        public Stub() {
            attachInterface(this, IKaraokeServiceAidlInterface.DESCRIPTOR);
        }

        public static IKaraokeServiceAidlInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IKaraokeServiceAidlInterface.DESCRIPTOR);
            if (iin != null && (iin instanceof IKaraokeServiceAidlInterface)) {
                return (IKaraokeServiceAidlInterface) iin;
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
                    return "setHeadsetState";
                case 2:
                    return "setActiveClient";
                case 3:
                    return "setPermitBits";
                case 4:
                    return "resetActiveClient";
                case 5:
                    return "getVersion";
                case 6:
                    return "setRecordingWetSound";
                case 7:
                    return "setVolume";
                case 8:
                    return "setEqualizerType";
                case 9:
                    return "setMixSoundType";
                case 10:
                    return "setTones";
                case 11:
                    return "setAudioLoopbackOn";
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
                data.enforceInterface(IKaraokeServiceAidlInterface.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setHeadsetState(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            setActiveClient(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            setPermitBits(_arg03, _arg1, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            resetActiveClient(_arg04, _arg12);
                            reply.writeNoException();
                            return true;
                        case 5:
                            int _result = getVersion();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 6:
                            boolean _arg05 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setRecordingWetSound(_arg05);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            setVolume(_arg06);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            setEqualizerType(_arg07);
                            reply.writeNoException();
                            return true;
                        case 9:
                            int _arg08 = data.readInt();
                            data.enforceNoDataAvail();
                            setMixSoundType(_arg08);
                            reply.writeNoException();
                            return true;
                        case 10:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            setTones(_arg09);
                            reply.writeNoException();
                            return true;
                        case 11:
                            boolean _arg010 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAudioLoopbackOn(_arg010);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IKaraokeServiceAidlInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IKaraokeServiceAidlInterface.DESCRIPTOR;
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setHeadsetState(boolean connected) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeBoolean(connected);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setActiveClient(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setPermitBits(int authByteArrayToInt, int pid, int uid, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeInt(authByteArrayToInt);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeString(pkgName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void resetActiveClient(String pkgName, boolean isShowDialog) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeBoolean(isShowDialog);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public int getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setRecordingWetSound(boolean isWetSound) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeBoolean(isWetSound);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setVolume(int volume) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeInt(volume);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setEqualizerType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setMixSoundType(int soundtype) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeInt(soundtype);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setTones(int tone) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeInt(tone);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.karaoke.framework.IKaraokeServiceAidlInterface
            public void setAudioLoopbackOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IKaraokeServiceAidlInterface.DESCRIPTOR);
                    _data.writeBoolean(on);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 10;
        }
    }
}
