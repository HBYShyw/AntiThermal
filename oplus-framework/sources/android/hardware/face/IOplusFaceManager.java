package android.hardware.face;

import android.hardware.face.IFaceCommandCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusFaceManager extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.face.IOplusFaceManager";

    List<Face> getEnrolledPalms(int i, int i2, String str) throws RemoteException;

    int getFaceProcessMemory() throws RemoteException;

    int getFailedAttempts() throws RemoteException;

    long getLockoutAttemptDeadline(int i) throws RemoteException;

    boolean hasEnrolledPalms(int i, int i2, String str) throws RemoteException;

    int regsiterFaceCmdCallback(IFaceCommandCallback iFaceCommandCallback) throws RemoteException;

    void resetFaceDaemon() throws RemoteException;

    int sendFaceCmd(int i, int i2, byte[] bArr) throws RemoteException;

    int unregsiterFaceCmdCallback(IFaceCommandCallback iFaceCommandCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFaceManager {
        @Override // android.hardware.face.IOplusFaceManager
        public long getLockoutAttemptDeadline(int userId) throws RemoteException {
            return 0L;
        }

        @Override // android.hardware.face.IOplusFaceManager
        public int getFailedAttempts() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.face.IOplusFaceManager
        public int sendFaceCmd(int sensorId, int cmdId, byte[] inbuf) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.face.IOplusFaceManager
        public void resetFaceDaemon() throws RemoteException {
        }

        @Override // android.hardware.face.IOplusFaceManager
        public int getFaceProcessMemory() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.face.IOplusFaceManager
        public int regsiterFaceCmdCallback(IFaceCommandCallback callback) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.face.IOplusFaceManager
        public int unregsiterFaceCmdCallback(IFaceCommandCallback callback) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.face.IOplusFaceManager
        public boolean hasEnrolledPalms(int sensorId, int userId, String opPackageName) throws RemoteException {
            return false;
        }

        @Override // android.hardware.face.IOplusFaceManager
        public List<Face> getEnrolledPalms(int sensorId, int userId, String opPackageName) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFaceManager {
        static final int TRANSACTION_getEnrolledPalms = 9;
        static final int TRANSACTION_getFaceProcessMemory = 5;
        static final int TRANSACTION_getFailedAttempts = 2;
        static final int TRANSACTION_getLockoutAttemptDeadline = 1;
        static final int TRANSACTION_hasEnrolledPalms = 8;
        static final int TRANSACTION_regsiterFaceCmdCallback = 6;
        static final int TRANSACTION_resetFaceDaemon = 4;
        static final int TRANSACTION_sendFaceCmd = 3;
        static final int TRANSACTION_unregsiterFaceCmdCallback = 7;

        public Stub() {
            attachInterface(this, IOplusFaceManager.DESCRIPTOR);
        }

        public static IOplusFaceManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFaceManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFaceManager)) {
                return (IOplusFaceManager) iin;
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
                    return "getLockoutAttemptDeadline";
                case 2:
                    return "getFailedAttempts";
                case 3:
                    return "sendFaceCmd";
                case 4:
                    return "resetFaceDaemon";
                case 5:
                    return "getFaceProcessMemory";
                case 6:
                    return "regsiterFaceCmdCallback";
                case 7:
                    return "unregsiterFaceCmdCallback";
                case 8:
                    return "hasEnrolledPalms";
                case 9:
                    return "getEnrolledPalms";
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
                data.enforceInterface(IOplusFaceManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFaceManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result = getLockoutAttemptDeadline(_arg0);
                            reply.writeNoException();
                            reply.writeLong(_result);
                            return true;
                        case 2:
                            int _result2 = getFailedAttempts();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            int _arg02 = data.readInt();
                            int _arg1 = data.readInt();
                            byte[] _arg2 = data.createByteArray();
                            data.enforceNoDataAvail();
                            int _result3 = sendFaceCmd(_arg02, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            resetFaceDaemon();
                            reply.writeNoException();
                            return true;
                        case 5:
                            int _result4 = getFaceProcessMemory();
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 6:
                            IFaceCommandCallback _arg03 = IFaceCommandCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result5 = regsiterFaceCmdCallback(_arg03);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 7:
                            IFaceCommandCallback _arg04 = IFaceCommandCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result6 = unregsiterFaceCmdCallback(_arg04);
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 8:
                            int _arg05 = data.readInt();
                            int _arg12 = data.readInt();
                            String _arg22 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result7 = hasEnrolledPalms(_arg05, _arg12, _arg22);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 9:
                            int _arg06 = data.readInt();
                            int _arg13 = data.readInt();
                            String _arg23 = data.readString();
                            data.enforceNoDataAvail();
                            List<Face> _result8 = getEnrolledPalms(_arg06, _arg13, _arg23);
                            reply.writeNoException();
                            reply.writeTypedList(_result8, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFaceManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFaceManager.DESCRIPTOR;
            }

            @Override // android.hardware.face.IOplusFaceManager
            public long getLockoutAttemptDeadline(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public int getFailedAttempts() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public int sendFaceCmd(int sensorId, int cmdId, byte[] inbuf) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    _data.writeInt(cmdId);
                    _data.writeByteArray(inbuf);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public void resetFaceDaemon() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public int getFaceProcessMemory() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public int regsiterFaceCmdCallback(IFaceCommandCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public int unregsiterFaceCmdCallback(IFaceCommandCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public boolean hasEnrolledPalms(int sensorId, int userId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IOplusFaceManager
            public List<Face> getEnrolledPalms(int sensorId, int userId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFaceManager.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    List<Face> _result = _reply.createTypedArrayList(Face.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 8;
        }
    }
}
