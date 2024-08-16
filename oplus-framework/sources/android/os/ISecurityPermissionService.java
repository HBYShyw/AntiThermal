package android.os;

import android.content.pm.PackagePermission;
import android.os.ISecurityPermissionService;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/* loaded from: classes.dex */
public interface ISecurityPermissionService extends IInterface {
    public static final String DESCRIPTOR = "android.os.ISecurityPermissionService";

    void basicTypes(int i, long j, boolean z, float f, double d, String str) throws RemoteException;

    boolean checkOplusPermission(String str, int i, int i2) throws RemoteException;

    long getLastUpdateTime() throws RemoteException;

    void putActivityStartWhiteList(Bundle bundle) throws RemoteException;

    PackagePermission queryPackagePermissionsAsUser(String str, int i) throws RemoteException;

    int queryPermissionAsUser(String str, String str2, int i) throws RemoteException;

    Map<String, String> readRecommendPermissions(String str) throws RemoteException;

    void updateCachedPermission(String str, int i, boolean z) throws RemoteException;

    void writeRecommendPermissions(String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISecurityPermissionService {
        @Override // android.os.ISecurityPermissionService
        public boolean checkOplusPermission(String permission, int pid, int uid) throws RemoteException {
            return false;
        }

        @Override // android.os.ISecurityPermissionService
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
        }

        @Override // android.os.ISecurityPermissionService
        public int queryPermissionAsUser(String pkgName, String permissionName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.os.ISecurityPermissionService
        public PackagePermission queryPackagePermissionsAsUser(String pkgName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.os.ISecurityPermissionService
        public void updateCachedPermission(String pkgName, int userId, boolean delete) throws RemoteException {
        }

        @Override // android.os.ISecurityPermissionService
        public void writeRecommendPermissions(String recommendBody, boolean fromLocal) throws RemoteException {
        }

        @Override // android.os.ISecurityPermissionService
        public Map<String, String> readRecommendPermissions(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.os.ISecurityPermissionService
        public long getLastUpdateTime() throws RemoteException {
            return 0L;
        }

        @Override // android.os.ISecurityPermissionService
        public void putActivityStartWhiteList(Bundle bundle) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISecurityPermissionService {
        static final int TRANSACTION_basicTypes = 2;
        static final int TRANSACTION_checkOplusPermission = 1;
        static final int TRANSACTION_getLastUpdateTime = 8;
        static final int TRANSACTION_putActivityStartWhiteList = 9;
        static final int TRANSACTION_queryPackagePermissionsAsUser = 4;
        static final int TRANSACTION_queryPermissionAsUser = 3;
        static final int TRANSACTION_readRecommendPermissions = 7;
        static final int TRANSACTION_updateCachedPermission = 5;
        static final int TRANSACTION_writeRecommendPermissions = 6;

        public Stub() {
            attachInterface(this, ISecurityPermissionService.DESCRIPTOR);
        }

        public static ISecurityPermissionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISecurityPermissionService.DESCRIPTOR);
            if (iin != null && (iin instanceof ISecurityPermissionService)) {
                return (ISecurityPermissionService) iin;
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
                    return "checkOplusPermission";
                case 2:
                    return "basicTypes";
                case 3:
                    return "queryPermissionAsUser";
                case 4:
                    return "queryPackagePermissionsAsUser";
                case 5:
                    return "updateCachedPermission";
                case 6:
                    return "writeRecommendPermissions";
                case 7:
                    return "readRecommendPermissions";
                case 8:
                    return "getLastUpdateTime";
                case 9:
                    return "putActivityStartWhiteList";
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
                data.enforceInterface(ISecurityPermissionService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ISecurityPermissionService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = checkOplusPermission(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            long _arg12 = data.readLong();
                            boolean _arg22 = data.readBoolean();
                            float _arg3 = data.readFloat();
                            double _arg4 = data.readDouble();
                            String _arg5 = data.readString();
                            data.enforceNoDataAvail();
                            basicTypes(_arg02, _arg12, _arg22, _arg3, _arg4, _arg5);
                            reply.writeNoException();
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            String _arg13 = data.readString();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result2 = queryPermissionAsUser(_arg03, _arg13, _arg23);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            PackagePermission _result3 = queryPackagePermissionsAsUser(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            int _arg15 = data.readInt();
                            boolean _arg24 = data.readBoolean();
                            data.enforceNoDataAvail();
                            updateCachedPermission(_arg05, _arg15, _arg24);
                            reply.writeNoException();
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            boolean _arg16 = data.readBoolean();
                            data.enforceNoDataAvail();
                            writeRecommendPermissions(_arg06, _arg16);
                            reply.writeNoException();
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            data.enforceNoDataAvail();
                            Map<String, String> _result4 = readRecommendPermissions(_arg07);
                            reply.writeNoException();
                            if (_result4 == null) {
                                reply.writeInt(-1);
                            } else {
                                reply.writeInt(_result4.size());
                                _result4.forEach(new BiConsumer() { // from class: android.os.ISecurityPermissionService$Stub$$ExternalSyntheticLambda0
                                    @Override // java.util.function.BiConsumer
                                    public final void accept(Object obj, Object obj2) {
                                        ISecurityPermissionService.Stub.lambda$onTransact$0(reply, (String) obj, (String) obj2);
                                    }
                                });
                            }
                            return true;
                        case 8:
                            long _result5 = getLastUpdateTime();
                            reply.writeNoException();
                            reply.writeLong(_result5);
                            return true;
                        case 9:
                            Bundle _arg08 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            putActivityStartWhiteList(_arg08);
                            reply.writeNoException();
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
        private static class Proxy implements ISecurityPermissionService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISecurityPermissionService.DESCRIPTOR;
            }

            @Override // android.os.ISecurityPermissionService
            public boolean checkOplusPermission(String permission, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISecurityPermissionService
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeInt(anInt);
                    _data.writeLong(aLong);
                    _data.writeBoolean(aBoolean);
                    _data.writeFloat(aFloat);
                    _data.writeDouble(aDouble);
                    _data.writeString(aString);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISecurityPermissionService
            public int queryPermissionAsUser(String pkgName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISecurityPermissionService
            public PackagePermission queryPackagePermissionsAsUser(String pkgName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    PackagePermission _result = (PackagePermission) _reply.readTypedObject(PackagePermission.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISecurityPermissionService
            public void updateCachedPermission(String pkgName, int userId, boolean delete) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    _data.writeBoolean(delete);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISecurityPermissionService
            public void writeRecommendPermissions(String recommendBody, boolean fromLocal) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeString(recommendBody);
                    _data.writeBoolean(fromLocal);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISecurityPermissionService
            public Map<String, String> readRecommendPermissions(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int N = _reply.readInt();
                    final Map<String, String> _result = N < 0 ? null : new HashMap<>();
                    IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.os.ISecurityPermissionService$Stub$Proxy$$ExternalSyntheticLambda0
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            ISecurityPermissionService.Stub.Proxy.lambda$readRecommendPermissions$0(_reply, _result, i);
                        }
                    });
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$readRecommendPermissions$0(Parcel _reply, Map _result, int i) {
                String k = _reply.readString();
                String v = _reply.readString();
                _result.put(k, v);
            }

            @Override // android.os.ISecurityPermissionService
            public long getLastUpdateTime() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISecurityPermissionService
            public void putActivityStartWhiteList(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecurityPermissionService.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
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
