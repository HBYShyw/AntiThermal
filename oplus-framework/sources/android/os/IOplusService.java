package android.os;

import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusService extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusService";

    void StartLogCoreService() throws RemoteException;

    void assertKernelPanic() throws RemoteException;

    boolean closeFlashLight() throws RemoteException;

    boolean copyFile(String str, String str2) throws RemoteException;

    boolean copyFileForDcs(String str, String str2) throws RemoteException;

    boolean deleteFile(String str) throws RemoteException;

    void deleteFileForDcs(String str) throws RemoteException;

    void deleteSystemLogFile() throws RemoteException;

    String getFlashLightState() throws RemoteException;

    String getOplusLogInfoString(int i) throws RemoteException;

    boolean iScoreLogServiceRunning() throws RemoteException;

    boolean openFlashLight() throws RemoteException;

    void sendDeleteStampId(String str) throws RemoteException;

    void sendOnStampEvent(String str, Map map) throws RemoteException;

    void sendQualityDCSEvent(String str, Map map) throws RemoteException;

    void startOplusFileEncodeHelperService() throws RemoteException;

    void startSensorLog(boolean z) throws RemoteException;

    void stopSensorLog() throws RemoteException;

    void unbindCoreLogService() throws RemoteException;

    void unbindOplusFileEncodeHelperService() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusService {
        @Override // android.os.IOplusService
        public void startSensorLog(boolean isOutPutFile) throws RemoteException {
        }

        @Override // android.os.IOplusService
        public void stopSensorLog() throws RemoteException {
        }

        @Override // android.os.IOplusService
        public boolean openFlashLight() throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusService
        public boolean closeFlashLight() throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusService
        public String getFlashLightState() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusService
        public boolean iScoreLogServiceRunning() throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusService
        public void StartLogCoreService() throws RemoteException {
        }

        @Override // android.os.IOplusService
        public void unbindCoreLogService() throws RemoteException {
        }

        @Override // android.os.IOplusService
        public void assertKernelPanic() throws RemoteException {
        }

        @Override // android.os.IOplusService
        public String getOplusLogInfoString(int index) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusService
        public void deleteSystemLogFile() throws RemoteException {
        }

        @Override // android.os.IOplusService
        public boolean copyFileForDcs(String srcPath, String destPath) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusService
        public void deleteFileForDcs(String srcPath) throws RemoteException {
        }

        @Override // android.os.IOplusService
        public void startOplusFileEncodeHelperService() throws RemoteException {
        }

        @Override // android.os.IOplusService
        public void unbindOplusFileEncodeHelperService() throws RemoteException {
        }

        @Override // android.os.IOplusService
        public boolean copyFile(String destPath, String srcPath) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusService
        public boolean deleteFile(String path) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusService
        public void sendOnStampEvent(String eventId, Map map) throws RemoteException {
        }

        @Override // android.os.IOplusService
        public void sendDeleteStampId(String eventId) throws RemoteException {
        }

        @Override // android.os.IOplusService
        public void sendQualityDCSEvent(String eventId, Map map) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusService {
        static final int TRANSACTION_StartLogCoreService = 7;
        static final int TRANSACTION_assertKernelPanic = 9;
        static final int TRANSACTION_closeFlashLight = 4;
        static final int TRANSACTION_copyFile = 16;
        static final int TRANSACTION_copyFileForDcs = 12;
        static final int TRANSACTION_deleteFile = 17;
        static final int TRANSACTION_deleteFileForDcs = 13;
        static final int TRANSACTION_deleteSystemLogFile = 11;
        static final int TRANSACTION_getFlashLightState = 5;
        static final int TRANSACTION_getOplusLogInfoString = 10;
        static final int TRANSACTION_iScoreLogServiceRunning = 6;
        static final int TRANSACTION_openFlashLight = 3;
        static final int TRANSACTION_sendDeleteStampId = 19;
        static final int TRANSACTION_sendOnStampEvent = 18;
        static final int TRANSACTION_sendQualityDCSEvent = 20;
        static final int TRANSACTION_startOplusFileEncodeHelperService = 14;
        static final int TRANSACTION_startSensorLog = 1;
        static final int TRANSACTION_stopSensorLog = 2;
        static final int TRANSACTION_unbindCoreLogService = 8;
        static final int TRANSACTION_unbindOplusFileEncodeHelperService = 15;

        public Stub() {
            attachInterface(this, IOplusService.DESCRIPTOR);
        }

        public static IOplusService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusService)) {
                return (IOplusService) iin;
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
                    return "startSensorLog";
                case 2:
                    return "stopSensorLog";
                case 3:
                    return "openFlashLight";
                case 4:
                    return "closeFlashLight";
                case 5:
                    return "getFlashLightState";
                case 6:
                    return "iScoreLogServiceRunning";
                case 7:
                    return "StartLogCoreService";
                case 8:
                    return "unbindCoreLogService";
                case 9:
                    return "assertKernelPanic";
                case 10:
                    return "getOplusLogInfoString";
                case 11:
                    return "deleteSystemLogFile";
                case 12:
                    return "copyFileForDcs";
                case 13:
                    return "deleteFileForDcs";
                case 14:
                    return "startOplusFileEncodeHelperService";
                case 15:
                    return "unbindOplusFileEncodeHelperService";
                case 16:
                    return "copyFile";
                case 17:
                    return "deleteFile";
                case 18:
                    return "sendOnStampEvent";
                case 19:
                    return "sendDeleteStampId";
                case 20:
                    return "sendQualityDCSEvent";
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
                data.enforceInterface(IOplusService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            startSensorLog(_arg0);
                            return true;
                        case 2:
                            stopSensorLog();
                            return true;
                        case 3:
                            boolean _result = openFlashLight();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 4:
                            boolean _result2 = closeFlashLight();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 5:
                            String _result3 = getFlashLightState();
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 6:
                            boolean _result4 = iScoreLogServiceRunning();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 7:
                            StartLogCoreService();
                            return true;
                        case 8:
                            unbindCoreLogService();
                            return true;
                        case 9:
                            assertKernelPanic();
                            return true;
                        case 10:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result5 = getOplusLogInfoString(_arg02);
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        case 11:
                            deleteSystemLogFile();
                            return true;
                        case 12:
                            String _arg03 = data.readString();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result6 = copyFileForDcs(_arg03, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 13:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            deleteFileForDcs(_arg04);
                            reply.writeNoException();
                            return true;
                        case 14:
                            startOplusFileEncodeHelperService();
                            return true;
                        case 15:
                            unbindOplusFileEncodeHelperService();
                            return true;
                        case 16:
                            String _arg05 = data.readString();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result7 = copyFile(_arg05, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 17:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result8 = deleteFile(_arg06);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 18:
                            String _arg07 = data.readString();
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg13 = data.readHashMap(cl);
                            data.enforceNoDataAvail();
                            sendOnStampEvent(_arg07, _arg13);
                            reply.writeNoException();
                            return true;
                        case 19:
                            String _arg08 = data.readString();
                            data.enforceNoDataAvail();
                            sendDeleteStampId(_arg08);
                            reply.writeNoException();
                            return true;
                        case 20:
                            String _arg09 = data.readString();
                            ClassLoader cl2 = getClass().getClassLoader();
                            Map _arg14 = data.readHashMap(cl2);
                            data.enforceNoDataAvail();
                            sendQualityDCSEvent(_arg09, _arg14);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusService.DESCRIPTOR;
            }

            @Override // android.os.IOplusService
            public void startSensorLog(boolean isOutPutFile) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeBoolean(isOutPutFile);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void stopSensorLog() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public boolean openFlashLight() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public boolean closeFlashLight() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public String getFlashLightState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public boolean iScoreLogServiceRunning() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void StartLogCoreService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void unbindCoreLogService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void assertKernelPanic() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public String getOplusLogInfoString(int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeInt(index);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void deleteSystemLogFile() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public boolean copyFileForDcs(String srcPath, String destPath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeString(srcPath);
                    _data.writeString(destPath);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void deleteFileForDcs(String srcPath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeString(srcPath);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void startOplusFileEncodeHelperService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void unbindOplusFileEncodeHelperService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public boolean copyFile(String destPath, String srcPath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeString(destPath);
                    _data.writeString(srcPath);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public boolean deleteFile(String path) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void sendOnStampEvent(String eventId, Map map) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeString(eventId);
                    _data.writeMap(map);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void sendDeleteStampId(String eventId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeString(eventId);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusService
            public void sendQualityDCSEvent(String eventId, Map map) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusService.DESCRIPTOR);
                    _data.writeString(eventId);
                    _data.writeMap(map);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
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
