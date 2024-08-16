package android.os.customize;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizePackageManagerService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/* loaded from: classes.dex */
public interface IOplusCustomizePackageManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizePackageManagerService";

    void addDisabledDeactivateMdmPackages(List<String> list) throws RemoteException;

    void addDisallowedUninstallPackages(List<String> list) throws RemoteException;

    boolean clearAllSuperWhiteList() throws RemoteException;

    void clearAppData(String str) throws RemoteException;

    boolean clearSuperWhiteList(List<String> list) throws RemoteException;

    boolean getAdbInstallUninstallDisabled() throws RemoteException;

    List<String> getAllInstallSysAppList() throws RemoteException;

    List<String> getClearAppName() throws RemoteException;

    Map<String, String> getContainOplusCertificatePackages() throws RemoteException;

    String getCustomizeDefaultApp(String str) throws RemoteException;

    List<String> getDetachableInstallSysAppList() throws RemoteException;

    List<String> getDisabledDeactivateMdmPackages() throws RemoteException;

    List<String> getDisallowUninstallPackageList() throws RemoteException;

    Bundle getInstallSysAppBundle() throws RemoteException;

    List<String> getPrivInstallSysAppList() throws RemoteException;

    List<String> getSuperWhiteList() throws RemoteException;

    boolean isDisabledDeactivateMdmPackage(String str) throws RemoteException;

    void removeAllDisabledDeactivateMdmPackages() throws RemoteException;

    void removeAllDisallowedUninstallPackages() throws RemoteException;

    void removeCustomizeDefaultApp(String str) throws RemoteException;

    void removeDisabledDeactivateMdmPackages(List<String> list) throws RemoteException;

    void removeDisallowedUninstallPackages(List<String> list) throws RemoteException;

    void setAdbInstallUninstallDisabled(boolean z) throws RemoteException;

    boolean setCustomizeDefaultApp(String str, String str2) throws RemoteException;

    void setInstallSysAppBundle(Bundle bundle) throws RemoteException;

    boolean setSuperWhiteList(List<String> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizePackageManagerService {
        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void addDisabledDeactivateMdmPackages(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void removeDisabledDeactivateMdmPackages(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void removeAllDisabledDeactivateMdmPackages() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public List<String> getDisabledDeactivateMdmPackages() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public boolean isDisabledDeactivateMdmPackage(String adminPackage) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void addDisallowedUninstallPackages(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void removeDisallowedUninstallPackages(List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void removeAllDisallowedUninstallPackages() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public List<String> getDisallowUninstallPackageList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void clearAppData(String packageName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public List<String> getClearAppName() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public boolean setCustomizeDefaultApp(String roleName, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public String getCustomizeDefaultApp(String roleName) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void removeCustomizeDefaultApp(String roleName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void setAdbInstallUninstallDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public boolean getAdbInstallUninstallDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public void setInstallSysAppBundle(Bundle bundle) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public Bundle getInstallSysAppBundle() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public List<String> getPrivInstallSysAppList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public List<String> getDetachableInstallSysAppList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public List<String> getAllInstallSysAppList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public Map<String, String> getContainOplusCertificatePackages() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public boolean setSuperWhiteList(List<String> list) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public List<String> getSuperWhiteList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public boolean clearSuperWhiteList(List<String> clearList) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePackageManagerService
        public boolean clearAllSuperWhiteList() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizePackageManagerService {
        static final int TRANSACTION_addDisabledDeactivateMdmPackages = 1;
        static final int TRANSACTION_addDisallowedUninstallPackages = 6;
        static final int TRANSACTION_clearAllSuperWhiteList = 26;
        static final int TRANSACTION_clearAppData = 10;
        static final int TRANSACTION_clearSuperWhiteList = 25;
        static final int TRANSACTION_getAdbInstallUninstallDisabled = 16;
        static final int TRANSACTION_getAllInstallSysAppList = 21;
        static final int TRANSACTION_getClearAppName = 11;
        static final int TRANSACTION_getContainOplusCertificatePackages = 22;
        static final int TRANSACTION_getCustomizeDefaultApp = 13;
        static final int TRANSACTION_getDetachableInstallSysAppList = 20;
        static final int TRANSACTION_getDisabledDeactivateMdmPackages = 4;
        static final int TRANSACTION_getDisallowUninstallPackageList = 9;
        static final int TRANSACTION_getInstallSysAppBundle = 18;
        static final int TRANSACTION_getPrivInstallSysAppList = 19;
        static final int TRANSACTION_getSuperWhiteList = 24;
        static final int TRANSACTION_isDisabledDeactivateMdmPackage = 5;
        static final int TRANSACTION_removeAllDisabledDeactivateMdmPackages = 3;
        static final int TRANSACTION_removeAllDisallowedUninstallPackages = 8;
        static final int TRANSACTION_removeCustomizeDefaultApp = 14;
        static final int TRANSACTION_removeDisabledDeactivateMdmPackages = 2;
        static final int TRANSACTION_removeDisallowedUninstallPackages = 7;
        static final int TRANSACTION_setAdbInstallUninstallDisabled = 15;
        static final int TRANSACTION_setCustomizeDefaultApp = 12;
        static final int TRANSACTION_setInstallSysAppBundle = 17;
        static final int TRANSACTION_setSuperWhiteList = 23;

        public Stub() {
            attachInterface(this, IOplusCustomizePackageManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizePackageManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizePackageManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizePackageManagerService)) {
                return (IOplusCustomizePackageManagerService) iin;
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
                    return "addDisabledDeactivateMdmPackages";
                case 2:
                    return "removeDisabledDeactivateMdmPackages";
                case 3:
                    return "removeAllDisabledDeactivateMdmPackages";
                case 4:
                    return "getDisabledDeactivateMdmPackages";
                case 5:
                    return "isDisabledDeactivateMdmPackage";
                case 6:
                    return "addDisallowedUninstallPackages";
                case 7:
                    return "removeDisallowedUninstallPackages";
                case 8:
                    return "removeAllDisallowedUninstallPackages";
                case 9:
                    return "getDisallowUninstallPackageList";
                case 10:
                    return "clearAppData";
                case 11:
                    return "getClearAppName";
                case 12:
                    return "setCustomizeDefaultApp";
                case 13:
                    return "getCustomizeDefaultApp";
                case 14:
                    return "removeCustomizeDefaultApp";
                case 15:
                    return "setAdbInstallUninstallDisabled";
                case 16:
                    return "getAdbInstallUninstallDisabled";
                case 17:
                    return "setInstallSysAppBundle";
                case 18:
                    return "getInstallSysAppBundle";
                case 19:
                    return "getPrivInstallSysAppList";
                case 20:
                    return "getDetachableInstallSysAppList";
                case 21:
                    return "getAllInstallSysAppList";
                case 22:
                    return "getContainOplusCertificatePackages";
                case 23:
                    return "setSuperWhiteList";
                case 24:
                    return "getSuperWhiteList";
                case 25:
                    return "clearSuperWhiteList";
                case 26:
                    return "clearAllSuperWhiteList";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, final Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusCustomizePackageManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<String> _arg0 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addDisabledDeactivateMdmPackages(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            List<String> _arg02 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeDisabledDeactivateMdmPackages(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            removeAllDisabledDeactivateMdmPackages();
                            reply.writeNoException();
                            return true;
                        case 4:
                            List<String> _result = getDisabledDeactivateMdmPackages();
                            reply.writeNoException();
                            reply.writeStringList(_result);
                            return true;
                        case 5:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = isDisabledDeactivateMdmPackage(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 6:
                            List<String> _arg04 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addDisallowedUninstallPackages(_arg04);
                            reply.writeNoException();
                            return true;
                        case 7:
                            List<String> _arg05 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeDisallowedUninstallPackages(_arg05);
                            reply.writeNoException();
                            return true;
                        case 8:
                            removeAllDisallowedUninstallPackages();
                            reply.writeNoException();
                            return true;
                        case 9:
                            List<String> _result3 = getDisallowUninstallPackageList();
                            reply.writeNoException();
                            reply.writeStringList(_result3);
                            return true;
                        case 10:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            clearAppData(_arg06);
                            reply.writeNoException();
                            return true;
                        case 11:
                            List<String> _result4 = getClearAppName();
                            reply.writeNoException();
                            reply.writeStringList(_result4);
                            return true;
                        case 12:
                            String _arg07 = data.readString();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result5 = setCustomizeDefaultApp(_arg07, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 13:
                            String _arg08 = data.readString();
                            data.enforceNoDataAvail();
                            String _result6 = getCustomizeDefaultApp(_arg08);
                            reply.writeNoException();
                            reply.writeString(_result6);
                            return true;
                        case 14:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            removeCustomizeDefaultApp(_arg09);
                            reply.writeNoException();
                            return true;
                        case 15:
                            boolean _arg010 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAdbInstallUninstallDisabled(_arg010);
                            reply.writeNoException();
                            return true;
                        case 16:
                            boolean _result7 = getAdbInstallUninstallDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 17:
                            Bundle _arg011 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            setInstallSysAppBundle(_arg011);
                            reply.writeNoException();
                            return true;
                        case 18:
                            Bundle _result8 = getInstallSysAppBundle();
                            reply.writeNoException();
                            reply.writeTypedObject(_result8, 1);
                            return true;
                        case 19:
                            List<String> _result9 = getPrivInstallSysAppList();
                            reply.writeNoException();
                            reply.writeStringList(_result9);
                            return true;
                        case 20:
                            List<String> _result10 = getDetachableInstallSysAppList();
                            reply.writeNoException();
                            reply.writeStringList(_result10);
                            return true;
                        case 21:
                            List<String> _result11 = getAllInstallSysAppList();
                            reply.writeNoException();
                            reply.writeStringList(_result11);
                            return true;
                        case 22:
                            Map<String, String> _result12 = getContainOplusCertificatePackages();
                            reply.writeNoException();
                            if (_result12 == null) {
                                reply.writeInt(-1);
                            } else {
                                reply.writeInt(_result12.size());
                                _result12.forEach(new BiConsumer() { // from class: android.os.customize.IOplusCustomizePackageManagerService$Stub$$ExternalSyntheticLambda0
                                    @Override // java.util.function.BiConsumer
                                    public final void accept(Object obj, Object obj2) {
                                        IOplusCustomizePackageManagerService.Stub.lambda$onTransact$0(reply, (String) obj, (String) obj2);
                                    }
                                });
                            }
                            return true;
                        case 23:
                            List<String> _arg012 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result13 = setSuperWhiteList(_arg012);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 24:
                            List<String> _result14 = getSuperWhiteList();
                            reply.writeNoException();
                            reply.writeStringList(_result14);
                            return true;
                        case 25:
                            List<String> _arg013 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result15 = clearSuperWhiteList(_arg013);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 26:
                            boolean _result16 = clearAllSuperWhiteList();
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$0(Parcel reply, String k, String v) {
            reply.writeString(k);
            reply.writeString(v);
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizePackageManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizePackageManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void addDisabledDeactivateMdmPackages(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void removeDisabledDeactivateMdmPackages(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void removeAllDisabledDeactivateMdmPackages() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public List<String> getDisabledDeactivateMdmPackages() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public boolean isDisabledDeactivateMdmPackage(String adminPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeString(adminPackage);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void addDisallowedUninstallPackages(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void removeDisallowedUninstallPackages(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void removeAllDisallowedUninstallPackages() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public List<String> getDisallowUninstallPackageList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void clearAppData(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public List<String> getClearAppName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public boolean setCustomizeDefaultApp(String roleName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeString(packageName);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public String getCustomizeDefaultApp(String roleName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeString(roleName);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void removeCustomizeDefaultApp(String roleName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeString(roleName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void setAdbInstallUninstallDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public boolean getAdbInstallUninstallDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public void setInstallSysAppBundle(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public Bundle getInstallSysAppBundle() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public List<String> getPrivInstallSysAppList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public List<String> getDetachableInstallSysAppList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public List<String> getAllInstallSysAppList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public Map<String, String> getContainOplusCertificatePackages() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int N = _reply.readInt();
                    final Map<String, String> _result = N < 0 ? null : new HashMap<>();
                    IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.os.customize.IOplusCustomizePackageManagerService$Stub$Proxy$$ExternalSyntheticLambda0
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            IOplusCustomizePackageManagerService.Stub.Proxy.lambda$getContainOplusCertificatePackages$0(_reply, _result, i);
                        }
                    });
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$getContainOplusCertificatePackages$0(Parcel _reply, Map _result, int i) {
                String k = _reply.readString();
                String v = _reply.readString();
                _result.put(k, v);
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public boolean setSuperWhiteList(List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeStringList(list);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public List<String> getSuperWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public boolean clearSuperWhiteList(List<String> clearList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    _data.writeStringList(clearList);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePackageManagerService
            public boolean clearAllSuperWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePackageManagerService.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
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
            return 25;
        }
    }
}
