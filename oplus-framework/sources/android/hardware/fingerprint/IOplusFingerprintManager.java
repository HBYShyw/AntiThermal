package android.hardware.fingerprint;

import android.hardware.fingerprint.IFingerprintCommandCallback;
import android.hardware.fingerprint.IOplusFingerprintServiceReceiver;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusFingerprintManager extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.fingerprint.IOplusFingerprintManager";

    void cancelFingerprintExtraInfo(IBinder iBinder, String str, String str2, long j) throws RemoteException;

    void cancelTouchEventListener(IBinder iBinder, String str, String str2, long j) throws RemoteException;

    int continueEnroll(int i) throws RemoteException;

    int getCurrentIconStatus(IBinder iBinder, String str) throws RemoteException;

    List<OplusFingerprint> getEnrolledFingerprints(int i, int i2) throws RemoteException;

    int getEnrollmentTotalTimes(int i) throws RemoteException;

    int getFailedAttempts() throws RemoteException;

    long getFingerprintExtraInfo(IBinder iBinder, String str, String str2, int i, IOplusFingerprintServiceReceiver iOplusFingerprintServiceReceiver, int i2, int i3) throws RemoteException;

    long getLockoutAttemptDeadline(int i) throws RemoteException;

    void hideFingerprintIcon(IBinder iBinder, String str) throws RemoteException;

    boolean isFingerprintPay(String str) throws RemoteException;

    boolean needForceUseFingerprintFirst(String str) throws RemoteException;

    int pauseEnroll(int i) throws RemoteException;

    int regsiterFingerprintCmdCallback(IFingerprintCommandCallback iFingerprintCommandCallback) throws RemoteException;

    int sendFingerprintCmd(int i, int i2, byte[] bArr) throws RemoteException;

    void setFingerKeymode(int i, int i2) throws RemoteException;

    int setFingerprintFlags(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    long setTouchEventListener(IBinder iBinder, String str, String str2, int i, IOplusFingerprintServiceReceiver iOplusFingerprintServiceReceiver, int i2) throws RemoteException;

    void showFingerprintIcon(IBinder iBinder, String str) throws RemoteException;

    int unregsiterFingerprintCmdCallback(IFingerprintCommandCallback iFingerprintCommandCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFingerprintManager {
        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int regsiterFingerprintCmdCallback(IFingerprintCommandCallback callback) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int unregsiterFingerprintCmdCallback(IFingerprintCommandCallback callback) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public void setFingerKeymode(int enable, int sensorId) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int pauseEnroll(int sensorId) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int continueEnroll(int sensorId) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int getEnrollmentTotalTimes(int sensorId) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public long getLockoutAttemptDeadline(int userId) throws RemoteException {
            return 0L;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int getFailedAttempts() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int sendFingerprintCmd(int sensorId, int cmdId, byte[] inbuf) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public long getFingerprintExtraInfo(IBinder token, String opPackageName, String attributionTag, int userId, IOplusFingerprintServiceReceiver receiver, int type, int sensorId) throws RemoteException {
            return 0L;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public void cancelFingerprintExtraInfo(IBinder token, String opPkgName, String attributionTag, long requestId) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public long setTouchEventListener(IBinder token, String opPackageName, String attributionTag, int userId, IOplusFingerprintServiceReceiver receiver, int sensorId) throws RemoteException {
            return 0L;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public void cancelTouchEventListener(IBinder token, String opPkgName, String attributionTag, long requestId) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public void showFingerprintIcon(IBinder mToken, String opPkgName) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public void hideFingerprintIcon(IBinder mToken, String opPkgName) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int getCurrentIconStatus(IBinder mToken, String opPkgName) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public boolean needForceUseFingerprintFirst(String opPkgName) throws RemoteException {
            return false;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public boolean isFingerprintPay(String opPkgName) throws RemoteException {
            return false;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public List<OplusFingerprint> getEnrolledFingerprints(int userId, int sensorId) throws RemoteException {
            return null;
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintManager
        public int setFingerprintFlags(int fingerId, int groupId, int flags, int userId, int sensorId) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFingerprintManager {
        static final int TRANSACTION_cancelFingerprintExtraInfo = 11;
        static final int TRANSACTION_cancelTouchEventListener = 13;
        static final int TRANSACTION_continueEnroll = 5;
        static final int TRANSACTION_getCurrentIconStatus = 16;
        static final int TRANSACTION_getEnrolledFingerprints = 19;
        static final int TRANSACTION_getEnrollmentTotalTimes = 6;
        static final int TRANSACTION_getFailedAttempts = 8;
        static final int TRANSACTION_getFingerprintExtraInfo = 10;
        static final int TRANSACTION_getLockoutAttemptDeadline = 7;
        static final int TRANSACTION_hideFingerprintIcon = 15;
        static final int TRANSACTION_isFingerprintPay = 18;
        static final int TRANSACTION_needForceUseFingerprintFirst = 17;
        static final int TRANSACTION_pauseEnroll = 4;
        static final int TRANSACTION_regsiterFingerprintCmdCallback = 1;
        static final int TRANSACTION_sendFingerprintCmd = 9;
        static final int TRANSACTION_setFingerKeymode = 3;
        static final int TRANSACTION_setFingerprintFlags = 20;
        static final int TRANSACTION_setTouchEventListener = 12;
        static final int TRANSACTION_showFingerprintIcon = 14;
        static final int TRANSACTION_unregsiterFingerprintCmdCallback = 2;

        public Stub() {
            attachInterface(this, IOplusFingerprintManager.DESCRIPTOR);
        }

        public static IOplusFingerprintManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFingerprintManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFingerprintManager)) {
                return (IOplusFingerprintManager) iin;
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
                    return "regsiterFingerprintCmdCallback";
                case 2:
                    return "unregsiterFingerprintCmdCallback";
                case 3:
                    return "setFingerKeymode";
                case 4:
                    return "pauseEnroll";
                case 5:
                    return "continueEnroll";
                case 6:
                    return "getEnrollmentTotalTimes";
                case 7:
                    return "getLockoutAttemptDeadline";
                case 8:
                    return "getFailedAttempts";
                case 9:
                    return "sendFingerprintCmd";
                case 10:
                    return "getFingerprintExtraInfo";
                case 11:
                    return "cancelFingerprintExtraInfo";
                case 12:
                    return "setTouchEventListener";
                case 13:
                    return "cancelTouchEventListener";
                case 14:
                    return "showFingerprintIcon";
                case 15:
                    return "hideFingerprintIcon";
                case 16:
                    return "getCurrentIconStatus";
                case 17:
                    return "needForceUseFingerprintFirst";
                case 18:
                    return "isFingerprintPay";
                case 19:
                    return "getEnrolledFingerprints";
                case 20:
                    return "setFingerprintFlags";
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
                data.enforceInterface(IOplusFingerprintManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFingerprintManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IFingerprintCommandCallback _arg0 = IFingerprintCommandCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result = regsiterFingerprintCmdCallback(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            IFingerprintCommandCallback _arg02 = IFingerprintCommandCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result2 = unregsiterFingerprintCmdCallback(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            setFingerKeymode(_arg03, _arg1);
                            reply.writeNoException();
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result3 = pauseEnroll(_arg04);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result4 = continueEnroll(_arg05);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result5 = getEnrollmentTotalTimes(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result6 = getLockoutAttemptDeadline(_arg07);
                            reply.writeNoException();
                            reply.writeLong(_result6);
                            return true;
                        case 8:
                            int _result7 = getFailedAttempts();
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 9:
                            int _arg08 = data.readInt();
                            int _arg12 = data.readInt();
                            byte[] _arg2 = data.createByteArray();
                            data.enforceNoDataAvail();
                            int _result8 = sendFingerprintCmd(_arg08, _arg12, _arg2);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 10:
                            IBinder _arg09 = data.readStrongBinder();
                            String _arg13 = data.readString();
                            String _arg22 = data.readString();
                            int _arg3 = data.readInt();
                            IOplusFingerprintServiceReceiver _arg4 = IOplusFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            int _arg5 = data.readInt();
                            int _arg6 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result9 = getFingerprintExtraInfo(_arg09, _arg13, _arg22, _arg3, _arg4, _arg5, _arg6);
                            reply.writeNoException();
                            reply.writeLong(_result9);
                            return true;
                        case 11:
                            IBinder _arg010 = data.readStrongBinder();
                            String _arg14 = data.readString();
                            String _arg23 = data.readString();
                            long _arg32 = data.readLong();
                            data.enforceNoDataAvail();
                            cancelFingerprintExtraInfo(_arg010, _arg14, _arg23, _arg32);
                            reply.writeNoException();
                            return true;
                        case 12:
                            IBinder _arg011 = data.readStrongBinder();
                            String _arg15 = data.readString();
                            String _arg24 = data.readString();
                            int _arg33 = data.readInt();
                            IOplusFingerprintServiceReceiver _arg42 = IOplusFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            int _arg52 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result10 = setTouchEventListener(_arg011, _arg15, _arg24, _arg33, _arg42, _arg52);
                            reply.writeNoException();
                            reply.writeLong(_result10);
                            return true;
                        case 13:
                            IBinder _arg012 = data.readStrongBinder();
                            String _arg16 = data.readString();
                            String _arg25 = data.readString();
                            long _arg34 = data.readLong();
                            data.enforceNoDataAvail();
                            cancelTouchEventListener(_arg012, _arg16, _arg25, _arg34);
                            reply.writeNoException();
                            return true;
                        case 14:
                            IBinder _arg013 = data.readStrongBinder();
                            String _arg17 = data.readString();
                            data.enforceNoDataAvail();
                            showFingerprintIcon(_arg013, _arg17);
                            reply.writeNoException();
                            return true;
                        case 15:
                            IBinder _arg014 = data.readStrongBinder();
                            String _arg18 = data.readString();
                            data.enforceNoDataAvail();
                            hideFingerprintIcon(_arg014, _arg18);
                            reply.writeNoException();
                            return true;
                        case 16:
                            IBinder _arg015 = data.readStrongBinder();
                            String _arg19 = data.readString();
                            data.enforceNoDataAvail();
                            int _result11 = getCurrentIconStatus(_arg015, _arg19);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 17:
                            String _arg016 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result12 = needForceUseFingerprintFirst(_arg016);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 18:
                            String _arg017 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result13 = isFingerprintPay(_arg017);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 19:
                            int _arg018 = data.readInt();
                            int _arg110 = data.readInt();
                            data.enforceNoDataAvail();
                            List<OplusFingerprint> _result14 = getEnrolledFingerprints(_arg018, _arg110);
                            reply.writeNoException();
                            reply.writeTypedList(_result14, 1);
                            return true;
                        case 20:
                            int _arg019 = data.readInt();
                            int _arg111 = data.readInt();
                            int _arg26 = data.readInt();
                            int _arg35 = data.readInt();
                            int _arg43 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result15 = setFingerprintFlags(_arg019, _arg111, _arg26, _arg35, _arg43);
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFingerprintManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFingerprintManager.DESCRIPTOR;
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int regsiterFingerprintCmdCallback(IFingerprintCommandCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int unregsiterFingerprintCmdCallback(IFingerprintCommandCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public void setFingerKeymode(int enable, int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(enable);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int pauseEnroll(int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int continueEnroll(int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int getEnrollmentTotalTimes(int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public long getLockoutAttemptDeadline(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int getFailedAttempts() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int sendFingerprintCmd(int sensorId, int cmdId, byte[] inbuf) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    _data.writeInt(cmdId);
                    _data.writeByteArray(inbuf);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public long getFingerprintExtraInfo(IBinder token, String opPackageName, String attributionTag, int userId, IOplusFingerprintServiceReceiver receiver, int type, int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    _data.writeString(attributionTag);
                    _data.writeInt(userId);
                    _data.writeStrongInterface(receiver);
                    _data.writeInt(type);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public void cancelFingerprintExtraInfo(IBinder token, String opPkgName, String attributionTag, long requestId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPkgName);
                    _data.writeString(attributionTag);
                    _data.writeLong(requestId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public long setTouchEventListener(IBinder token, String opPackageName, String attributionTag, int userId, IOplusFingerprintServiceReceiver receiver, int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    _data.writeString(attributionTag);
                    _data.writeInt(userId);
                    _data.writeStrongInterface(receiver);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public void cancelTouchEventListener(IBinder token, String opPkgName, String attributionTag, long requestId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPkgName);
                    _data.writeString(attributionTag);
                    _data.writeLong(requestId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public void showFingerprintIcon(IBinder mToken, String opPkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongBinder(mToken);
                    _data.writeString(opPkgName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public void hideFingerprintIcon(IBinder mToken, String opPkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongBinder(mToken);
                    _data.writeString(opPkgName);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int getCurrentIconStatus(IBinder mToken, String opPkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeStrongBinder(mToken);
                    _data.writeString(opPkgName);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public boolean needForceUseFingerprintFirst(String opPkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeString(opPkgName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public boolean isFingerprintPay(String opPkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeString(opPkgName);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public List<OplusFingerprint> getEnrolledFingerprints(int userId, int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    List<OplusFingerprint> _result = _reply.createTypedArrayList(OplusFingerprint.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintManager
            public int setFingerprintFlags(int fingerId, int groupId, int flags, int userId, int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusFingerprintManager.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    _data.writeInt(groupId);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(20, _data, _reply, 0);
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
            return 19;
        }
    }
}
