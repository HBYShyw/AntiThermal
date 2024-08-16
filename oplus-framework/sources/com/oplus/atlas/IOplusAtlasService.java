package com.oplus.atlas;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.atlas.IOplusAtlasAudioCallback;
import com.oplus.atlas.IOplusAtlasServiceCallback;
import com.oplus.network.OlkConstants;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusAtlasService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.atlas.IOplusAtlasService";

    boolean checkIsInDaemonlistByName(String str, String str2) throws RemoteException;

    boolean checkIsInDaemonlistByUid(String str, int i) throws RemoteException;

    String getAttributeByAppName(String str, String str2) throws RemoteException;

    String getAttributeByAppUid(String str, int i) throws RemoteException;

    List<String> getConfigAppList(String str) throws RemoteException;

    String getListInfoByAppName(String str, String str2) throws RemoteException;

    String getListInfoByAppUid(String str, int i) throws RemoteException;

    String getPackageNameByPid(int i) throws RemoteException;

    String getPackageNameByUid(int i) throws RemoteException;

    String getParameters(String str) throws RemoteException;

    boolean interfaceCallPermissionCheck(String str, String str2) throws RemoteException;

    void registerAudioCallback(IOplusAtlasAudioCallback iOplusAtlasAudioCallback) throws RemoteException;

    void registerCallback(IOplusAtlasServiceCallback iOplusAtlasServiceCallback) throws RemoteException;

    void setEvent(String str, String str2) throws RemoteException;

    void setEventToNative(String str, String str2) throws RemoteException;

    void setParameters(String str) throws RemoteException;

    void unRegisterAudioCallback(IOplusAtlasAudioCallback iOplusAtlasAudioCallback) throws RemoteException;

    void unRegisterCallback(IOplusAtlasServiceCallback iOplusAtlasServiceCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusAtlasService {
        @Override // com.oplus.atlas.IOplusAtlasService
        public void setEvent(String event, String value) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public void setEventToNative(String event, String value) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public void setParameters(String keyValuePairs) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public String getParameters(String keyValuePairs) throws RemoteException {
            return null;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public boolean interfaceCallPermissionCheck(String function, String callPackageName) throws RemoteException {
            return false;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public void registerCallback(IOplusAtlasServiceCallback callback) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public void unRegisterCallback(IOplusAtlasServiceCallback callback) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public void registerAudioCallback(IOplusAtlasAudioCallback callback) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public void unRegisterAudioCallback(IOplusAtlasAudioCallback callback) throws RemoteException {
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public boolean checkIsInDaemonlistByName(String module, String packageName) throws RemoteException {
            return false;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public boolean checkIsInDaemonlistByUid(String module, int uid) throws RemoteException {
            return false;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public String getPackageNameByUid(int uid) throws RemoteException {
            return null;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public String getPackageNameByPid(int pid) throws RemoteException {
            return null;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public String getAttributeByAppName(String module, String packageName) throws RemoteException {
            return null;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public String getAttributeByAppUid(String module, int uid) throws RemoteException {
            return null;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public String getListInfoByAppUid(String module, int uid) throws RemoteException {
            return null;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public String getListInfoByAppName(String module, String packageName) throws RemoteException {
            return null;
        }

        @Override // com.oplus.atlas.IOplusAtlasService
        public List<String> getConfigAppList(String module) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusAtlasService {
        static final int TRANSACTION_checkIsInDaemonlistByName = 10;
        static final int TRANSACTION_checkIsInDaemonlistByUid = 11;
        static final int TRANSACTION_getAttributeByAppName = 14;
        static final int TRANSACTION_getAttributeByAppUid = 15;
        static final int TRANSACTION_getConfigAppList = 18;
        static final int TRANSACTION_getListInfoByAppName = 17;
        static final int TRANSACTION_getListInfoByAppUid = 16;
        static final int TRANSACTION_getPackageNameByPid = 13;
        static final int TRANSACTION_getPackageNameByUid = 12;
        static final int TRANSACTION_getParameters = 4;
        static final int TRANSACTION_interfaceCallPermissionCheck = 5;
        static final int TRANSACTION_registerAudioCallback = 8;
        static final int TRANSACTION_registerCallback = 6;
        static final int TRANSACTION_setEvent = 1;
        static final int TRANSACTION_setEventToNative = 2;
        static final int TRANSACTION_setParameters = 3;
        static final int TRANSACTION_unRegisterAudioCallback = 9;
        static final int TRANSACTION_unRegisterCallback = 7;

        public Stub() {
            attachInterface(this, IOplusAtlasService.DESCRIPTOR);
        }

        public static IOplusAtlasService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusAtlasService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusAtlasService)) {
                return (IOplusAtlasService) iin;
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
                    return "setEvent";
                case 2:
                    return "setEventToNative";
                case 3:
                    return "setParameters";
                case 4:
                    return "getParameters";
                case 5:
                    return "interfaceCallPermissionCheck";
                case 6:
                    return OlkConstants.FUN_REGISTER_CALLBACK;
                case 7:
                    return "unRegisterCallback";
                case 8:
                    return "registerAudioCallback";
                case 9:
                    return "unRegisterAudioCallback";
                case 10:
                    return "checkIsInDaemonlistByName";
                case 11:
                    return "checkIsInDaemonlistByUid";
                case 12:
                    return "getPackageNameByUid";
                case 13:
                    return "getPackageNameByPid";
                case 14:
                    return "getAttributeByAppName";
                case 15:
                    return "getAttributeByAppUid";
                case 16:
                    return "getListInfoByAppUid";
                case 17:
                    return "getListInfoByAppName";
                case 18:
                    return "getConfigAppList";
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
                data.enforceInterface(IOplusAtlasService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusAtlasService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            setEvent(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            setEventToNative(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            setParameters(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            String _result = getParameters(_arg04);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = interfaceCallPermissionCheck(_arg05, _arg13);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 6:
                            IOplusAtlasServiceCallback _arg06 = IOplusAtlasServiceCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerCallback(_arg06);
                            reply.writeNoException();
                            return true;
                        case 7:
                            IOplusAtlasServiceCallback _arg07 = IOplusAtlasServiceCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unRegisterCallback(_arg07);
                            reply.writeNoException();
                            return true;
                        case 8:
                            IOplusAtlasAudioCallback _arg08 = IOplusAtlasAudioCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerAudioCallback(_arg08);
                            reply.writeNoException();
                            return true;
                        case 9:
                            IOplusAtlasAudioCallback _arg09 = IOplusAtlasAudioCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unRegisterAudioCallback(_arg09);
                            reply.writeNoException();
                            return true;
                        case 10:
                            String _arg010 = data.readString();
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result3 = checkIsInDaemonlistByName(_arg010, _arg14);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 11:
                            String _arg011 = data.readString();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result4 = checkIsInDaemonlistByUid(_arg011, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result5 = getPackageNameByUid(_arg012);
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result6 = getPackageNameByPid(_arg013);
                            reply.writeNoException();
                            reply.writeString(_result6);
                            return true;
                        case 14:
                            String _arg014 = data.readString();
                            String _arg16 = data.readString();
                            data.enforceNoDataAvail();
                            String _result7 = getAttributeByAppName(_arg014, _arg16);
                            reply.writeNoException();
                            reply.writeString(_result7);
                            return true;
                        case 15:
                            String _arg015 = data.readString();
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result8 = getAttributeByAppUid(_arg015, _arg17);
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 16:
                            String _arg016 = data.readString();
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result9 = getListInfoByAppUid(_arg016, _arg18);
                            reply.writeNoException();
                            reply.writeString(_result9);
                            return true;
                        case 17:
                            String _arg017 = data.readString();
                            String _arg19 = data.readString();
                            data.enforceNoDataAvail();
                            String _result10 = getListInfoByAppName(_arg017, _arg19);
                            reply.writeNoException();
                            reply.writeString(_result10);
                            return true;
                        case 18:
                            String _arg018 = data.readString();
                            data.enforceNoDataAvail();
                            List<String> _result11 = getConfigAppList(_arg018);
                            reply.writeNoException();
                            reply.writeStringList(_result11);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusAtlasService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusAtlasService.DESCRIPTOR;
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public void setEvent(String event, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(event);
                    _data.writeString(value);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public void setEventToNative(String event, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(event);
                    _data.writeString(value);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public void setParameters(String keyValuePairs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(keyValuePairs);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public String getParameters(String keyValuePairs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(keyValuePairs);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public boolean interfaceCallPermissionCheck(String function, String callPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(function);
                    _data.writeString(callPackageName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public void registerCallback(IOplusAtlasServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public void unRegisterCallback(IOplusAtlasServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public void registerAudioCallback(IOplusAtlasAudioCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public void unRegisterAudioCallback(IOplusAtlasAudioCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public boolean checkIsInDaemonlistByName(String module, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(packageName);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public boolean checkIsInDaemonlistByUid(String module, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeInt(uid);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public String getPackageNameByUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public String getPackageNameByPid(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public String getAttributeByAppName(String module, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(packageName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public String getAttributeByAppUid(String module, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeInt(uid);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public String getListInfoByAppUid(String module, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeInt(uid);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public String getListInfoByAppName(String module, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(packageName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.atlas.IOplusAtlasService
            public List<String> getConfigAppList(String module) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasService.DESCRIPTOR);
                    _data.writeString(module);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 17;
        }
    }
}
