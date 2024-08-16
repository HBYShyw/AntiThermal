package android.os.customize;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeApplicationManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeApplicationManagerService";

    void addAppAlarmWhiteList(List<String> list) throws RemoteException;

    void addDisallowedRunningApp(List<String> list) throws RemoteException;

    void addPersistentApp(List<String> list, String str) throws RemoteException;

    void addTrustedAppStore(String str) throws RemoteException;

    void addTrustedAppStoreList(List<String> list) throws RemoteException;

    void cleanBackgroundProcess() throws RemoteException;

    void clearAllTrustedAppStore() throws RemoteException;

    void deleteTrustedAppStore(String str) throws RemoteException;

    void enableTrustedAppStore(boolean z) throws RemoteException;

    boolean forceStopPackage(List<String> list) throws RemoteException;

    List<String> getAERDeviceOwnerApp() throws RemoteException;

    List<String> getAppAlarmWhiteList() throws RemoteException;

    List<String> getDisallowedRunningApp() throws RemoteException;

    List getPersistentApp() throws RemoteException;

    List<String> getSpecificCutoutModeAppList(int i) throws RemoteException;

    boolean getStopLockTaskAvailability() throws RemoteException;

    String getTopAppPackageName() throws RemoteException;

    List<String> getTrustedAppStore() throws RemoteException;

    void interceptStopLockTask(boolean z) throws RemoteException;

    boolean isAllowControlAppRun() throws RemoteException;

    boolean isAllowTrustedAppStore() throws RemoteException;

    boolean isTrustedAppStoreEnabled() throws RemoteException;

    void killApplicationProcess(String str) throws RemoteException;

    boolean removeAllAppAlarmWhiteList() throws RemoteException;

    boolean removeAppAlarmWhiteList(List<String> list) throws RemoteException;

    void removeDisallowedRunningApp(List<String> list) throws RemoteException;

    void removePersistentApp(List<String> list) throws RemoteException;

    void setAERDeviceOwnerApp(List<String> list) throws RemoteException;

    void setAllowControlAppRun(boolean z) throws RemoteException;

    void setAllowTrustedAppStore(boolean z) throws RemoteException;

    void setSpecificCutoutModeAppList(List<String> list, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeApplicationManagerService {
        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public boolean forceStopPackage(List<String> pkgs) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public String getTopAppPackageName() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void killApplicationProcess(String packageName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void cleanBackgroundProcess() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void addDisallowedRunningApp(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void removeDisallowedRunningApp(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public List<String> getDisallowedRunningApp() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void addTrustedAppStore(String appStorePkgName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void deleteTrustedAppStore(String appStorePkgName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void enableTrustedAppStore(boolean enable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public boolean isTrustedAppStoreEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public List<String> getTrustedAppStore() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void setAllowTrustedAppStore(boolean enable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public boolean isAllowTrustedAppStore() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void addTrustedAppStoreList(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void clearAllTrustedAppStore() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void addAppAlarmWhiteList(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public List<String> getAppAlarmWhiteList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public boolean removeAppAlarmWhiteList(List<String> packageNames) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public boolean removeAllAppAlarmWhiteList() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void setAllowControlAppRun(boolean enable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public boolean isAllowControlAppRun() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void addPersistentApp(List<String> packageNames, String label) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void removePersistentApp(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public List getPersistentApp() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public List<String> getSpecificCutoutModeAppList(int cutoutMode) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void setSpecificCutoutModeAppList(List<String> pkgList, int cutoutMode) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void interceptStopLockTask(boolean intercept) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public boolean getStopLockTaskAvailability() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public void setAERDeviceOwnerApp(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeApplicationManagerService
        public List<String> getAERDeviceOwnerApp() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeApplicationManagerService {
        static final int TRANSACTION_addAppAlarmWhiteList = 17;
        static final int TRANSACTION_addDisallowedRunningApp = 5;
        static final int TRANSACTION_addPersistentApp = 23;
        static final int TRANSACTION_addTrustedAppStore = 8;
        static final int TRANSACTION_addTrustedAppStoreList = 15;
        static final int TRANSACTION_cleanBackgroundProcess = 4;
        static final int TRANSACTION_clearAllTrustedAppStore = 16;
        static final int TRANSACTION_deleteTrustedAppStore = 9;
        static final int TRANSACTION_enableTrustedAppStore = 10;
        static final int TRANSACTION_forceStopPackage = 1;
        static final int TRANSACTION_getAERDeviceOwnerApp = 31;
        static final int TRANSACTION_getAppAlarmWhiteList = 18;
        static final int TRANSACTION_getDisallowedRunningApp = 7;
        static final int TRANSACTION_getPersistentApp = 25;
        static final int TRANSACTION_getSpecificCutoutModeAppList = 26;
        static final int TRANSACTION_getStopLockTaskAvailability = 29;
        static final int TRANSACTION_getTopAppPackageName = 2;
        static final int TRANSACTION_getTrustedAppStore = 12;
        static final int TRANSACTION_interceptStopLockTask = 28;
        static final int TRANSACTION_isAllowControlAppRun = 22;
        static final int TRANSACTION_isAllowTrustedAppStore = 14;
        static final int TRANSACTION_isTrustedAppStoreEnabled = 11;
        static final int TRANSACTION_killApplicationProcess = 3;
        static final int TRANSACTION_removeAllAppAlarmWhiteList = 20;
        static final int TRANSACTION_removeAppAlarmWhiteList = 19;
        static final int TRANSACTION_removeDisallowedRunningApp = 6;
        static final int TRANSACTION_removePersistentApp = 24;
        static final int TRANSACTION_setAERDeviceOwnerApp = 30;
        static final int TRANSACTION_setAllowControlAppRun = 21;
        static final int TRANSACTION_setAllowTrustedAppStore = 13;
        static final int TRANSACTION_setSpecificCutoutModeAppList = 27;

        public Stub() {
            attachInterface(this, IOplusCustomizeApplicationManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeApplicationManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeApplicationManagerService)) {
                return (IOplusCustomizeApplicationManagerService) iin;
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
                    return "forceStopPackage";
                case 2:
                    return "getTopAppPackageName";
                case 3:
                    return "killApplicationProcess";
                case 4:
                    return "cleanBackgroundProcess";
                case 5:
                    return "addDisallowedRunningApp";
                case 6:
                    return "removeDisallowedRunningApp";
                case 7:
                    return "getDisallowedRunningApp";
                case 8:
                    return "addTrustedAppStore";
                case 9:
                    return "deleteTrustedAppStore";
                case 10:
                    return "enableTrustedAppStore";
                case 11:
                    return "isTrustedAppStoreEnabled";
                case 12:
                    return "getTrustedAppStore";
                case 13:
                    return "setAllowTrustedAppStore";
                case 14:
                    return "isAllowTrustedAppStore";
                case 15:
                    return "addTrustedAppStoreList";
                case 16:
                    return "clearAllTrustedAppStore";
                case 17:
                    return "addAppAlarmWhiteList";
                case 18:
                    return "getAppAlarmWhiteList";
                case 19:
                    return "removeAppAlarmWhiteList";
                case 20:
                    return "removeAllAppAlarmWhiteList";
                case 21:
                    return "setAllowControlAppRun";
                case 22:
                    return "isAllowControlAppRun";
                case 23:
                    return "addPersistentApp";
                case 24:
                    return "removePersistentApp";
                case 25:
                    return "getPersistentApp";
                case 26:
                    return "getSpecificCutoutModeAppList";
                case 27:
                    return "setSpecificCutoutModeAppList";
                case 28:
                    return "interceptStopLockTask";
                case 29:
                    return "getStopLockTaskAvailability";
                case 30:
                    return "setAERDeviceOwnerApp";
                case 31:
                    return "getAERDeviceOwnerApp";
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
                data.enforceInterface(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<String> _arg0 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result = forceStopPackage(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _result2 = getTopAppPackageName();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            killApplicationProcess(_arg02);
                            reply.writeNoException();
                            return true;
                        case 4:
                            cleanBackgroundProcess();
                            reply.writeNoException();
                            return true;
                        case 5:
                            List<String> _arg03 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addDisallowedRunningApp(_arg03);
                            reply.writeNoException();
                            return true;
                        case 6:
                            List<String> _arg04 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeDisallowedRunningApp(_arg04);
                            reply.writeNoException();
                            return true;
                        case 7:
                            List<String> _result3 = getDisallowedRunningApp();
                            reply.writeNoException();
                            reply.writeStringList(_result3);
                            return true;
                        case 8:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            addTrustedAppStore(_arg05);
                            reply.writeNoException();
                            return true;
                        case 9:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            deleteTrustedAppStore(_arg06);
                            reply.writeNoException();
                            return true;
                        case 10:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            enableTrustedAppStore(_arg07);
                            reply.writeNoException();
                            return true;
                        case 11:
                            boolean _result4 = isTrustedAppStoreEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 12:
                            List<String> _result5 = getTrustedAppStore();
                            reply.writeNoException();
                            reply.writeStringList(_result5);
                            return true;
                        case 13:
                            boolean _arg08 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAllowTrustedAppStore(_arg08);
                            reply.writeNoException();
                            return true;
                        case 14:
                            boolean _result6 = isAllowTrustedAppStore();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 15:
                            List<String> _arg09 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addTrustedAppStoreList(_arg09);
                            reply.writeNoException();
                            return true;
                        case 16:
                            clearAllTrustedAppStore();
                            reply.writeNoException();
                            return true;
                        case 17:
                            List<String> _arg010 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addAppAlarmWhiteList(_arg010);
                            reply.writeNoException();
                            return true;
                        case 18:
                            List<String> _result7 = getAppAlarmWhiteList();
                            reply.writeNoException();
                            reply.writeStringList(_result7);
                            return true;
                        case 19:
                            List<String> _arg011 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result8 = removeAppAlarmWhiteList(_arg011);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 20:
                            boolean _result9 = removeAllAppAlarmWhiteList();
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 21:
                            boolean _arg012 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAllowControlAppRun(_arg012);
                            reply.writeNoException();
                            return true;
                        case 22:
                            boolean _result10 = isAllowControlAppRun();
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 23:
                            List<String> _arg013 = data.createStringArrayList();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            addPersistentApp(_arg013, _arg1);
                            reply.writeNoException();
                            return true;
                        case 24:
                            List<String> _arg014 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removePersistentApp(_arg014);
                            reply.writeNoException();
                            return true;
                        case 25:
                            List _result11 = getPersistentApp();
                            reply.writeNoException();
                            reply.writeList(_result11);
                            return true;
                        case 26:
                            int _arg015 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result12 = getSpecificCutoutModeAppList(_arg015);
                            reply.writeNoException();
                            reply.writeStringList(_result12);
                            return true;
                        case 27:
                            List<String> _arg016 = data.createStringArrayList();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            setSpecificCutoutModeAppList(_arg016, _arg12);
                            reply.writeNoException();
                            return true;
                        case 28:
                            boolean _arg017 = data.readBoolean();
                            data.enforceNoDataAvail();
                            interceptStopLockTask(_arg017);
                            reply.writeNoException();
                            return true;
                        case 29:
                            boolean _result13 = getStopLockTaskAvailability();
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 30:
                            List<String> _arg018 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            setAERDeviceOwnerApp(_arg018);
                            reply.writeNoException();
                            return true;
                        case 31:
                            List<String> _result14 = getAERDeviceOwnerApp();
                            reply.writeNoException();
                            reply.writeStringList(_result14);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeApplicationManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeApplicationManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public boolean forceStopPackage(List<String> pkgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgs);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public String getTopAppPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void killApplicationProcess(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void cleanBackgroundProcess() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void addDisallowedRunningApp(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void removeDisallowedRunningApp(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public List<String> getDisallowedRunningApp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void addTrustedAppStore(String appStorePkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeString(appStorePkgName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void deleteTrustedAppStore(String appStorePkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeString(appStorePkgName);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void enableTrustedAppStore(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public boolean isTrustedAppStoreEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public List<String> getTrustedAppStore() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void setAllowTrustedAppStore(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public boolean isAllowTrustedAppStore() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void addTrustedAppStoreList(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void clearAllTrustedAppStore() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void addAppAlarmWhiteList(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public List<String> getAppAlarmWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public boolean removeAppAlarmWhiteList(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public boolean removeAllAppAlarmWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void setAllowControlAppRun(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public boolean isAllowControlAppRun() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void addPersistentApp(List<String> packageNames, String label) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    _data.writeString(label);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void removePersistentApp(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public List getPersistentApp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public List<String> getSpecificCutoutModeAppList(int cutoutMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeInt(cutoutMode);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void setSpecificCutoutModeAppList(List<String> pkgList, int cutoutMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgList);
                    _data.writeInt(cutoutMode);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void interceptStopLockTask(boolean intercept) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeBoolean(intercept);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public boolean getStopLockTaskAvailability() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public void setAERDeviceOwnerApp(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeApplicationManagerService
            public List<String> getAERDeviceOwnerApp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeApplicationManagerService.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
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
            return 30;
        }
    }
}
