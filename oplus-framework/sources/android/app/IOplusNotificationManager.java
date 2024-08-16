package android.app;

import android.app.IOplusNotificationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/* loaded from: classes.dex */
public interface IOplusNotificationManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusNotificationManager";

    boolean canModifyNotificationPermission(String str, int i) throws RemoteException;

    void checkDriveMode(boolean z, int i, int i2) throws RemoteException;

    boolean checkGetOpenid(String str, int i, String str2) throws RemoteException;

    boolean checkGetStdid(String str, int i, String str2) throws RemoteException;

    void clearOpenid(String str, int i, String str2) throws RemoteException;

    void clearStdid(String str, int i, String str2) throws RemoteException;

    Map<String, Bundle> getAllAppsNotificationPermissions() throws RemoteException;

    boolean getAppBanner(String str, int i) throws RemoteException;

    int getAppVisibility(String str, int i) throws RemoteException;

    int getBadgeOption(String str, int i) throws RemoteException;

    String getDynamicRingtone(String str, String str2) throws RemoteException;

    String[] getEnableNavigationApps(int i) throws RemoteException;

    int getNavigationMode(String str, int i) throws RemoteException;

    String getOpenid(String str, int i, String str2) throws RemoteException;

    String getStdid(String str, int i, String str2) throws RemoteException;

    int getStowOption(String str, int i) throws RemoteException;

    boolean isAppRingtonePermissionGranted(String str, int i) throws RemoteException;

    boolean isAppVibrationPermissionGranted(String str, int i) throws RemoteException;

    boolean isDriveNavigationMode(String str, int i) throws RemoteException;

    boolean isNavigationMode(int i) throws RemoteException;

    boolean isNumbadgeSupport(String str, int i) throws RemoteException;

    boolean isSuppressedByDriveMode(int i) throws RemoteException;

    void setAppBanner(String str, int i, boolean z) throws RemoteException;

    void setAppRingtonePermission(String str, int i, boolean z) throws RemoteException;

    void setAppVibrationPermission(String str, int i, boolean z) throws RemoteException;

    void setAppVisibility(String str, int i, int i2) throws RemoteException;

    void setBadgeOption(String str, int i, int i2) throws RemoteException;

    void setNumbadgeSupport(String str, int i, boolean z) throws RemoteException;

    void setStowOption(String str, int i, int i2) throws RemoteException;

    void setSuppressedByDriveMode(boolean z, int i) throws RemoteException;

    boolean shouldInterceptSound(String str, int i) throws RemoteException;

    boolean shouldKeepAlive(String str, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNotificationManager {
        @Override // android.app.IOplusNotificationManager
        public boolean shouldInterceptSound(String pkg, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public boolean shouldKeepAlive(String pkg, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public int getNavigationMode(String pkg, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusNotificationManager
        public boolean isDriveNavigationMode(String pkg, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public boolean isNavigationMode(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public String[] getEnableNavigationApps(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusNotificationManager
        public boolean isSuppressedByDriveMode(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public void setSuppressedByDriveMode(boolean mode, int userId) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public String getStdid(String pkg, int uid, String type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusNotificationManager
        public void clearStdid(String pkg, int uid, String type) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public void setBadgeOption(String pkg, int uid, int option) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public int getBadgeOption(String pkg, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusNotificationManager
        public boolean isNumbadgeSupport(String pkg, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public void setNumbadgeSupport(String pkg, int uid, boolean support) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public int getStowOption(String pkg, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusNotificationManager
        public void setStowOption(String pkg, int uid, int option) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public boolean checkGetStdid(String pkg, int uid, String type) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public void setAppBanner(String pkg, int uid, boolean enabled) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public boolean getAppBanner(String pkg, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public int getAppVisibility(String pkg, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusNotificationManager
        public void setAppVisibility(String pkg, int uid, int option) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public void setAppRingtonePermission(String pkg, int uid, boolean permissionGranted) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public boolean isAppRingtonePermissionGranted(String pkg, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public void setAppVibrationPermission(String pkg, int uid, boolean permissionGranted) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public boolean isAppVibrationPermissionGranted(String pkg, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public boolean canModifyNotificationPermission(String pkg, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public String getDynamicRingtone(String uri, String pkg) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusNotificationManager
        public String getOpenid(String pkg, int uid, String type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusNotificationManager
        public void clearOpenid(String pkg, int uid, String type) throws RemoteException {
        }

        @Override // android.app.IOplusNotificationManager
        public boolean checkGetOpenid(String pkg, int uid, String type) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusNotificationManager
        public Map<String, Bundle> getAllAppsNotificationPermissions() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusNotificationManager
        public void checkDriveMode(boolean mode, int pid, int uid) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNotificationManager {
        static final int TRANSACTION_canModifyNotificationPermission = 26;
        static final int TRANSACTION_checkDriveMode = 32;
        static final int TRANSACTION_checkGetOpenid = 30;
        static final int TRANSACTION_checkGetStdid = 17;
        static final int TRANSACTION_clearOpenid = 29;
        static final int TRANSACTION_clearStdid = 10;
        static final int TRANSACTION_getAllAppsNotificationPermissions = 31;
        static final int TRANSACTION_getAppBanner = 19;
        static final int TRANSACTION_getAppVisibility = 20;
        static final int TRANSACTION_getBadgeOption = 12;
        static final int TRANSACTION_getDynamicRingtone = 27;
        static final int TRANSACTION_getEnableNavigationApps = 6;
        static final int TRANSACTION_getNavigationMode = 3;
        static final int TRANSACTION_getOpenid = 28;
        static final int TRANSACTION_getStdid = 9;
        static final int TRANSACTION_getStowOption = 15;
        static final int TRANSACTION_isAppRingtonePermissionGranted = 23;
        static final int TRANSACTION_isAppVibrationPermissionGranted = 25;
        static final int TRANSACTION_isDriveNavigationMode = 4;
        static final int TRANSACTION_isNavigationMode = 5;
        static final int TRANSACTION_isNumbadgeSupport = 13;
        static final int TRANSACTION_isSuppressedByDriveMode = 7;
        static final int TRANSACTION_setAppBanner = 18;
        static final int TRANSACTION_setAppRingtonePermission = 22;
        static final int TRANSACTION_setAppVibrationPermission = 24;
        static final int TRANSACTION_setAppVisibility = 21;
        static final int TRANSACTION_setBadgeOption = 11;
        static final int TRANSACTION_setNumbadgeSupport = 14;
        static final int TRANSACTION_setStowOption = 16;
        static final int TRANSACTION_setSuppressedByDriveMode = 8;
        static final int TRANSACTION_shouldInterceptSound = 1;
        static final int TRANSACTION_shouldKeepAlive = 2;

        public Stub() {
            attachInterface(this, IOplusNotificationManager.DESCRIPTOR);
        }

        public static IOplusNotificationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNotificationManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNotificationManager)) {
                return (IOplusNotificationManager) iin;
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
                    return "shouldInterceptSound";
                case 2:
                    return "shouldKeepAlive";
                case 3:
                    return "getNavigationMode";
                case 4:
                    return "isDriveNavigationMode";
                case 5:
                    return "isNavigationMode";
                case 6:
                    return "getEnableNavigationApps";
                case 7:
                    return "isSuppressedByDriveMode";
                case 8:
                    return "setSuppressedByDriveMode";
                case 9:
                    return "getStdid";
                case 10:
                    return "clearStdid";
                case 11:
                    return "setBadgeOption";
                case 12:
                    return "getBadgeOption";
                case 13:
                    return "isNumbadgeSupport";
                case 14:
                    return "setNumbadgeSupport";
                case 15:
                    return "getStowOption";
                case 16:
                    return "setStowOption";
                case 17:
                    return "checkGetStdid";
                case 18:
                    return "setAppBanner";
                case 19:
                    return "getAppBanner";
                case 20:
                    return "getAppVisibility";
                case 21:
                    return "setAppVisibility";
                case 22:
                    return "setAppRingtonePermission";
                case 23:
                    return "isAppRingtonePermissionGranted";
                case 24:
                    return "setAppVibrationPermission";
                case 25:
                    return "isAppVibrationPermissionGranted";
                case 26:
                    return "canModifyNotificationPermission";
                case 27:
                    return "getDynamicRingtone";
                case 28:
                    return "getOpenid";
                case 29:
                    return "clearOpenid";
                case 30:
                    return "checkGetOpenid";
                case 31:
                    return "getAllAppsNotificationPermissions";
                case 32:
                    return "checkDriveMode";
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
                data.enforceInterface(IOplusNotificationManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNotificationManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = shouldInterceptSound(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result2 = shouldKeepAlive(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result3 = getNavigationMode(_arg03, _arg13);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result4 = isDriveNavigationMode(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = isNavigationMode(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            String[] _result6 = getEnableNavigationApps(_arg06);
                            reply.writeNoException();
                            reply.writeStringArray(_result6);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result7 = isSuppressedByDriveMode(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 8:
                            boolean _arg08 = data.readBoolean();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            setSuppressedByDriveMode(_arg08, _arg15);
                            return true;
                        case 9:
                            String _arg09 = data.readString();
                            int _arg16 = data.readInt();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            String _result8 = getStdid(_arg09, _arg16, _arg2);
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 10:
                            String _arg010 = data.readString();
                            int _arg17 = data.readInt();
                            String _arg22 = data.readString();
                            data.enforceNoDataAvail();
                            clearStdid(_arg010, _arg17, _arg22);
                            reply.writeNoException();
                            return true;
                        case 11:
                            String _arg011 = data.readString();
                            int _arg18 = data.readInt();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            setBadgeOption(_arg011, _arg18, _arg23);
                            return true;
                        case 12:
                            String _arg012 = data.readString();
                            int _arg19 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result9 = getBadgeOption(_arg012, _arg19);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 13:
                            String _arg013 = data.readString();
                            int _arg110 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result10 = isNumbadgeSupport(_arg013, _arg110);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 14:
                            String _arg014 = data.readString();
                            int _arg111 = data.readInt();
                            boolean _arg24 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setNumbadgeSupport(_arg014, _arg111, _arg24);
                            return true;
                        case 15:
                            String _arg015 = data.readString();
                            int _arg112 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result11 = getStowOption(_arg015, _arg112);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 16:
                            String _arg016 = data.readString();
                            int _arg113 = data.readInt();
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            setStowOption(_arg016, _arg113, _arg25);
                            return true;
                        case 17:
                            String _arg017 = data.readString();
                            int _arg114 = data.readInt();
                            String _arg26 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result12 = checkGetStdid(_arg017, _arg114, _arg26);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 18:
                            String _arg018 = data.readString();
                            int _arg115 = data.readInt();
                            boolean _arg27 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAppBanner(_arg018, _arg115, _arg27);
                            return true;
                        case 19:
                            String _arg019 = data.readString();
                            int _arg116 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result13 = getAppBanner(_arg019, _arg116);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 20:
                            String _arg020 = data.readString();
                            int _arg117 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result14 = getAppVisibility(_arg020, _arg117);
                            reply.writeNoException();
                            reply.writeInt(_result14);
                            return true;
                        case 21:
                            String _arg021 = data.readString();
                            int _arg118 = data.readInt();
                            int _arg28 = data.readInt();
                            data.enforceNoDataAvail();
                            setAppVisibility(_arg021, _arg118, _arg28);
                            return true;
                        case 22:
                            String _arg022 = data.readString();
                            int _arg119 = data.readInt();
                            boolean _arg29 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAppRingtonePermission(_arg022, _arg119, _arg29);
                            return true;
                        case 23:
                            String _arg023 = data.readString();
                            int _arg120 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result15 = isAppRingtonePermissionGranted(_arg023, _arg120);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 24:
                            String _arg024 = data.readString();
                            int _arg121 = data.readInt();
                            boolean _arg210 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAppVibrationPermission(_arg024, _arg121, _arg210);
                            return true;
                        case 25:
                            String _arg025 = data.readString();
                            int _arg122 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result16 = isAppVibrationPermissionGranted(_arg025, _arg122);
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 26:
                            String _arg026 = data.readString();
                            int _arg123 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result17 = canModifyNotificationPermission(_arg026, _arg123);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 27:
                            String _arg027 = data.readString();
                            String _arg124 = data.readString();
                            data.enforceNoDataAvail();
                            String _result18 = getDynamicRingtone(_arg027, _arg124);
                            reply.writeNoException();
                            reply.writeString(_result18);
                            return true;
                        case 28:
                            String _arg028 = data.readString();
                            int _arg125 = data.readInt();
                            String _arg211 = data.readString();
                            data.enforceNoDataAvail();
                            String _result19 = getOpenid(_arg028, _arg125, _arg211);
                            reply.writeNoException();
                            reply.writeString(_result19);
                            return true;
                        case 29:
                            String _arg029 = data.readString();
                            int _arg126 = data.readInt();
                            String _arg212 = data.readString();
                            data.enforceNoDataAvail();
                            clearOpenid(_arg029, _arg126, _arg212);
                            reply.writeNoException();
                            return true;
                        case 30:
                            String _arg030 = data.readString();
                            int _arg127 = data.readInt();
                            String _arg213 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result20 = checkGetOpenid(_arg030, _arg127, _arg213);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 31:
                            Map<String, Bundle> _result21 = getAllAppsNotificationPermissions();
                            reply.writeNoException();
                            if (_result21 == null) {
                                reply.writeInt(-1);
                            } else {
                                reply.writeInt(_result21.size());
                                _result21.forEach(new BiConsumer() { // from class: android.app.IOplusNotificationManager$Stub$$ExternalSyntheticLambda0
                                    @Override // java.util.function.BiConsumer
                                    public final void accept(Object obj, Object obj2) {
                                        IOplusNotificationManager.Stub.lambda$onTransact$0(reply, (String) obj, (Bundle) obj2);
                                    }
                                });
                            }
                            return true;
                        case 32:
                            boolean _arg031 = data.readBoolean();
                            int _arg128 = data.readInt();
                            int _arg214 = data.readInt();
                            data.enforceNoDataAvail();
                            checkDriveMode(_arg031, _arg128, _arg214);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$0(Parcel reply, String k, Bundle v) {
            reply.writeString(k);
            reply.writeTypedObject(v, 1);
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNotificationManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNotificationManager.DESCRIPTOR;
            }

            @Override // android.app.IOplusNotificationManager
            public boolean shouldInterceptSound(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
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

            @Override // android.app.IOplusNotificationManager
            public boolean shouldKeepAlive(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public int getNavigationMode(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
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

            @Override // android.app.IOplusNotificationManager
            public boolean isDriveNavigationMode(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean isNavigationMode(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public String[] getEnableNavigationApps(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean isSuppressedByDriveMode(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setSuppressedByDriveMode(boolean mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeBoolean(mode);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public String getStdid(String pkg, int uid, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(type);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void clearStdid(String pkg, int uid, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(type);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setBadgeOption(String pkg, int uid, int option) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(option);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public int getBadgeOption(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean isNumbadgeSupport(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setNumbadgeSupport(String pkg, int uid, boolean support) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeBoolean(support);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public int getStowOption(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setStowOption(String pkg, int uid, int option) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(option);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean checkGetStdid(String pkg, int uid, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(type);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setAppBanner(String pkg, int uid, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean getAppBanner(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public int getAppVisibility(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setAppVisibility(String pkg, int uid, int option) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(option);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setAppRingtonePermission(String pkg, int uid, boolean permissionGranted) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeBoolean(permissionGranted);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean isAppRingtonePermissionGranted(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void setAppVibrationPermission(String pkg, int uid, boolean permissionGranted) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeBoolean(permissionGranted);
                    this.mRemote.transact(24, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean isAppVibrationPermissionGranted(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean canModifyNotificationPermission(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public String getDynamicRingtone(String uri, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(uri);
                    _data.writeString(pkg);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public String getOpenid(String pkg, int uid, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(type);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public void clearOpenid(String pkg, int uid, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(type);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public boolean checkGetOpenid(String pkg, int uid, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(type);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusNotificationManager
            public Map<String, Bundle> getAllAppsNotificationPermissions() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int N = _reply.readInt();
                    final Map<String, Bundle> _result = N < 0 ? null : new HashMap<>();
                    IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.app.IOplusNotificationManager$Stub$Proxy$$ExternalSyntheticLambda0
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            IOplusNotificationManager.Stub.Proxy.lambda$getAllAppsNotificationPermissions$0(_reply, _result, i);
                        }
                    });
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$getAllAppsNotificationPermissions$0(Parcel _reply, Map _result, int i) {
                String k = _reply.readString();
                Bundle v = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                _result.put(k, v);
            }

            @Override // android.app.IOplusNotificationManager
            public void checkDriveMode(boolean mode, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNotificationManager.DESCRIPTOR);
                    _data.writeBoolean(mode);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    this.mRemote.transact(32, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 31;
        }
    }
}
