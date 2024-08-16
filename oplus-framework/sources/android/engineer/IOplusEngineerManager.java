package android.engineer;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusEngineerManager extends IInterface {
    public static final String DESCRIPTOR = "android.engineer.IOplusEngineerManager";

    boolean fastbootUnlock(byte[] bArr, int i) throws RemoteException;

    String getBootImgWaterMark() throws RemoteException;

    byte[] getCalibrationStatusFromNvram() throws RemoteException;

    String getCarrierVersion() throws RemoteException;

    byte[] getCarrierVersionFromNvram() throws RemoteException;

    String getDownloadStatus() throws RemoteException;

    String getSimOperatorSwitchStatus() throws RemoteException;

    String getSystemProperties(String str, String str2) throws RemoteException;

    boolean isEngineerItemInBlackList(int i, String str) throws RemoteException;

    byte[] readEngineerData(int i) throws RemoteException;

    boolean saveCarrierVersionToNvram(byte[] bArr) throws RemoteException;

    boolean saveEngineerData(int i, byte[] bArr, int i2) throws RemoteException;

    boolean setCarrierVersion(String str) throws RemoteException;

    boolean setSimOperatorSwitch(String str) throws RemoteException;

    void setSystemProperties(String str, String str2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusEngineerManager {
        @Override // android.engineer.IOplusEngineerManager
        public String getDownloadStatus() throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public String getCarrierVersion() throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public boolean setCarrierVersion(String version) throws RemoteException {
            return false;
        }

        @Override // android.engineer.IOplusEngineerManager
        public byte[] getCarrierVersionFromNvram() throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public boolean saveCarrierVersionToNvram(byte[] version) throws RemoteException {
            return false;
        }

        @Override // android.engineer.IOplusEngineerManager
        public byte[] getCalibrationStatusFromNvram() throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public boolean setSimOperatorSwitch(String state) throws RemoteException {
            return false;
        }

        @Override // android.engineer.IOplusEngineerManager
        public String getSimOperatorSwitchStatus() throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public String getBootImgWaterMark() throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public byte[] readEngineerData(int type) throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public boolean saveEngineerData(int type, byte[] engineerData, int length) throws RemoteException {
            return false;
        }

        @Override // android.engineer.IOplusEngineerManager
        public boolean fastbootUnlock(byte[] data, int length) throws RemoteException {
            return false;
        }

        @Override // android.engineer.IOplusEngineerManager
        public void setSystemProperties(String key, String val) throws RemoteException {
        }

        @Override // android.engineer.IOplusEngineerManager
        public String getSystemProperties(String key, String val) throws RemoteException {
            return null;
        }

        @Override // android.engineer.IOplusEngineerManager
        public boolean isEngineerItemInBlackList(int type, String item) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusEngineerManager {
        static final int TRANSACTION_fastbootUnlock = 12;
        static final int TRANSACTION_getBootImgWaterMark = 9;
        static final int TRANSACTION_getCalibrationStatusFromNvram = 6;
        static final int TRANSACTION_getCarrierVersion = 2;
        static final int TRANSACTION_getCarrierVersionFromNvram = 4;
        static final int TRANSACTION_getDownloadStatus = 1;
        static final int TRANSACTION_getSimOperatorSwitchStatus = 8;
        static final int TRANSACTION_getSystemProperties = 14;
        static final int TRANSACTION_isEngineerItemInBlackList = 15;
        static final int TRANSACTION_readEngineerData = 10;
        static final int TRANSACTION_saveCarrierVersionToNvram = 5;
        static final int TRANSACTION_saveEngineerData = 11;
        static final int TRANSACTION_setCarrierVersion = 3;
        static final int TRANSACTION_setSimOperatorSwitch = 7;
        static final int TRANSACTION_setSystemProperties = 13;

        public Stub() {
            attachInterface(this, IOplusEngineerManager.DESCRIPTOR);
        }

        public static IOplusEngineerManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusEngineerManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusEngineerManager)) {
                return (IOplusEngineerManager) iin;
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
                    return "getDownloadStatus";
                case 2:
                    return "getCarrierVersion";
                case 3:
                    return "setCarrierVersion";
                case 4:
                    return "getCarrierVersionFromNvram";
                case 5:
                    return "saveCarrierVersionToNvram";
                case 6:
                    return "getCalibrationStatusFromNvram";
                case 7:
                    return "setSimOperatorSwitch";
                case 8:
                    return "getSimOperatorSwitchStatus";
                case 9:
                    return "getBootImgWaterMark";
                case 10:
                    return "readEngineerData";
                case 11:
                    return "saveEngineerData";
                case 12:
                    return "fastbootUnlock";
                case 13:
                    return "setSystemProperties";
                case 14:
                    return "getSystemProperties";
                case 15:
                    return "isEngineerItemInBlackList";
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
                data.enforceInterface(IOplusEngineerManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusEngineerManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _result = getDownloadStatus();
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 2:
                            String _result2 = getCarrierVersion();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result3 = setCarrierVersion(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            byte[] _result4 = getCarrierVersionFromNvram();
                            reply.writeNoException();
                            reply.writeByteArray(_result4);
                            return true;
                        case 5:
                            byte[] _arg02 = data.createByteArray();
                            data.enforceNoDataAvail();
                            boolean _result5 = saveCarrierVersionToNvram(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            byte[] _result6 = getCalibrationStatusFromNvram();
                            reply.writeNoException();
                            reply.writeByteArray(_result6);
                            return true;
                        case 7:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result7 = setSimOperatorSwitch(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 8:
                            String _result8 = getSimOperatorSwitchStatus();
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 9:
                            String _result9 = getBootImgWaterMark();
                            reply.writeNoException();
                            reply.writeString(_result9);
                            return true;
                        case 10:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            byte[] _result10 = readEngineerData(_arg04);
                            reply.writeNoException();
                            reply.writeByteArray(_result10);
                            return true;
                        case 11:
                            int _arg05 = data.readInt();
                            byte[] _arg1 = data.createByteArray();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result11 = saveEngineerData(_arg05, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result11);
                            return true;
                        case 12:
                            byte[] _arg06 = data.createByteArray();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result12 = fastbootUnlock(_arg06, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 13:
                            String _arg07 = data.readString();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            setSystemProperties(_arg07, _arg13);
                            reply.writeNoException();
                            return true;
                        case 14:
                            String _arg08 = data.readString();
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            String _result13 = getSystemProperties(_arg08, _arg14);
                            reply.writeNoException();
                            reply.writeString(_result13);
                            return true;
                        case 15:
                            int _arg09 = data.readInt();
                            String _arg15 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result14 = isEngineerItemInBlackList(_arg09, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusEngineerManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusEngineerManager.DESCRIPTOR;
            }

            @Override // android.engineer.IOplusEngineerManager
            public String getDownloadStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public String getCarrierVersion() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public boolean setCarrierVersion(String version) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeString(version);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public byte[] getCarrierVersionFromNvram() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public boolean saveCarrierVersionToNvram(byte[] version) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeByteArray(version);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public byte[] getCalibrationStatusFromNvram() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public boolean setSimOperatorSwitch(String state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeString(state);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public String getSimOperatorSwitchStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public String getBootImgWaterMark() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public byte[] readEngineerData(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public boolean saveEngineerData(int type, byte[] engineerData, int length) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(engineerData);
                    _data.writeInt(length);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public boolean fastbootUnlock(byte[] data, int length) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeByteArray(data);
                    _data.writeInt(length);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public void setSystemProperties(String key, String val) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(val);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public String getSystemProperties(String key, String val) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(val);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.engineer.IOplusEngineerManager
            public boolean isEngineerItemInBlackList(int type, String item) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusEngineerManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(item);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 14;
        }
    }
}
