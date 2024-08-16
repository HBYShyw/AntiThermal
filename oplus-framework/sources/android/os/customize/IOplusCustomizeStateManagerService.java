package android.os.customize;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeStateManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeStateManagerService";

    List<String> getAllowedAllFilesAccessList() throws RemoteException;

    List<String> getAllowedGetUsageStatusList() throws RemoteException;

    List<String> getAllowedNotificationListenerAccessList() throws RemoteException;

    List<String> getAppRuntimeExceptionInfo() throws RemoteException;

    String[] getDeviceState() throws RemoteException;

    int getExtStorageMode(String str) throws RemoteException;

    List<String> getRunningApplication() throws RemoteException;

    boolean getScreenOnStatus() throws RemoteException;

    boolean getSystemIntegrity() throws RemoteException;

    void setAllowedAllFilesAccessList(List<String> list) throws RemoteException;

    void setAllowedGetUsageStatusList(List<String> list) throws RemoteException;

    void setAllowedNotificationListenerAccessList(List<String> list) throws RemoteException;

    void setExtStorageMode(String str, String str2, int i, boolean z) throws RemoteException;

    void setScreenOnStatus(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeStateManagerService {
        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public List<String> getAppRuntimeExceptionInfo() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public List<String> getRunningApplication() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public String[] getDeviceState() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public boolean getSystemIntegrity() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public void setScreenOnStatus(boolean screenOnstatus) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public boolean getScreenOnStatus() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public void setAllowedNotificationListenerAccessList(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public List<String> getAllowedNotificationListenerAccessList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public void setAllowedGetUsageStatusList(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public List<String> getAllowedGetUsageStatusList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public void setAllowedAllFilesAccessList(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public List<String> getAllowedAllFilesAccessList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public void setExtStorageMode(String packageName, String permissionName, int choice, boolean systemFixed) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeStateManagerService
        public int getExtStorageMode(String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeStateManagerService {
        static final int TRANSACTION_getAllowedAllFilesAccessList = 12;
        static final int TRANSACTION_getAllowedGetUsageStatusList = 10;
        static final int TRANSACTION_getAllowedNotificationListenerAccessList = 8;
        static final int TRANSACTION_getAppRuntimeExceptionInfo = 1;
        static final int TRANSACTION_getDeviceState = 3;
        static final int TRANSACTION_getExtStorageMode = 14;
        static final int TRANSACTION_getRunningApplication = 2;
        static final int TRANSACTION_getScreenOnStatus = 6;
        static final int TRANSACTION_getSystemIntegrity = 4;
        static final int TRANSACTION_setAllowedAllFilesAccessList = 11;
        static final int TRANSACTION_setAllowedGetUsageStatusList = 9;
        static final int TRANSACTION_setAllowedNotificationListenerAccessList = 7;
        static final int TRANSACTION_setExtStorageMode = 13;
        static final int TRANSACTION_setScreenOnStatus = 5;

        public Stub() {
            attachInterface(this, IOplusCustomizeStateManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeStateManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeStateManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeStateManagerService)) {
                return (IOplusCustomizeStateManagerService) iin;
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
                    return "getAppRuntimeExceptionInfo";
                case 2:
                    return "getRunningApplication";
                case 3:
                    return "getDeviceState";
                case 4:
                    return "getSystemIntegrity";
                case 5:
                    return "setScreenOnStatus";
                case 6:
                    return "getScreenOnStatus";
                case 7:
                    return "setAllowedNotificationListenerAccessList";
                case 8:
                    return "getAllowedNotificationListenerAccessList";
                case 9:
                    return "setAllowedGetUsageStatusList";
                case 10:
                    return "getAllowedGetUsageStatusList";
                case 11:
                    return "setAllowedAllFilesAccessList";
                case 12:
                    return "getAllowedAllFilesAccessList";
                case 13:
                    return "setExtStorageMode";
                case 14:
                    return "getExtStorageMode";
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
                data.enforceInterface(IOplusCustomizeStateManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<String> _result = getAppRuntimeExceptionInfo();
                            reply.writeNoException();
                            reply.writeStringList(_result);
                            return true;
                        case 2:
                            List<String> _result2 = getRunningApplication();
                            reply.writeNoException();
                            reply.writeStringList(_result2);
                            return true;
                        case 3:
                            String[] _result3 = getDeviceState();
                            reply.writeNoException();
                            reply.writeStringArray(_result3);
                            return true;
                        case 4:
                            boolean _result4 = getSystemIntegrity();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 5:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setScreenOnStatus(_arg0);
                            reply.writeNoException();
                            return true;
                        case 6:
                            boolean _result5 = getScreenOnStatus();
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 7:
                            List<String> _arg02 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            setAllowedNotificationListenerAccessList(_arg02);
                            reply.writeNoException();
                            return true;
                        case 8:
                            List<String> _result6 = getAllowedNotificationListenerAccessList();
                            reply.writeNoException();
                            reply.writeStringList(_result6);
                            return true;
                        case 9:
                            List<String> _arg03 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            setAllowedGetUsageStatusList(_arg03);
                            reply.writeNoException();
                            return true;
                        case 10:
                            List<String> _result7 = getAllowedGetUsageStatusList();
                            reply.writeNoException();
                            reply.writeStringList(_result7);
                            return true;
                        case 11:
                            List<String> _arg04 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            setAllowedAllFilesAccessList(_arg04);
                            reply.writeNoException();
                            return true;
                        case 12:
                            List<String> _result8 = getAllowedAllFilesAccessList();
                            reply.writeNoException();
                            reply.writeStringList(_result8);
                            return true;
                        case 13:
                            String _arg05 = data.readString();
                            String _arg1 = data.readString();
                            int _arg2 = data.readInt();
                            boolean _arg3 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setExtStorageMode(_arg05, _arg1, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 14:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            int _result9 = getExtStorageMode(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeStateManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeStateManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public List<String> getAppRuntimeExceptionInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public List<String> getRunningApplication() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public String[] getDeviceState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public boolean getSystemIntegrity() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public void setScreenOnStatus(boolean screenOnstatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    _data.writeBoolean(screenOnstatus);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public boolean getScreenOnStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public void setAllowedNotificationListenerAccessList(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public List<String> getAllowedNotificationListenerAccessList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public void setAllowedGetUsageStatusList(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public List<String> getAllowedGetUsageStatusList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public void setAllowedAllFilesAccessList(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public List<String> getAllowedAllFilesAccessList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public void setExtStorageMode(String packageName, String permissionName, int choice, boolean systemFixed) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(choice);
                    _data.writeBoolean(systemFixed);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeStateManagerService
            public int getExtStorageMode(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeStateManagerService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(14, _data, _reply, 0);
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
            return 13;
        }
    }
}
