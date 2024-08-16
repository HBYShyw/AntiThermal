package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeSettingsManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeSettingsManagerService";

    void addManageNotificationToWhiteList(List<String> list) throws RemoteException;

    void deleteManageNotificationFromWhiteList() throws RemoteException;

    long getAutoScreenOffTime(ComponentName componentName) throws RemoteException;

    List<String> getManageNotificationWhiteList() throws RemoteException;

    String getRomVersion(ComponentName componentName) throws RemoteException;

    boolean getTopWatermark() throws RemoteException;

    boolean isBackupRestoreDisabled(ComponentName componentName) throws RemoteException;

    boolean isDeveloperOptionsDisabled(ComponentName componentName) throws RemoteException;

    boolean isProtectEyesOn(ComponentName componentName) throws RemoteException;

    boolean isRestoreFactoryDisabled(ComponentName componentName) throws RemoteException;

    boolean isSIMLockDisabled(ComponentName componentName) throws RemoteException;

    boolean isScreenOffTimeSetByPolicy(ComponentName componentName) throws RemoteException;

    boolean isSearchIndexDisabled(ComponentName componentName) throws RemoteException;

    boolean isTimeAndDateSetDisabled(ComponentName componentName) throws RemoteException;

    void removeManageNotificationFromWhiteList(List<String> list) throws RemoteException;

    boolean setAutoScreenOffTime(ComponentName componentName, long j) throws RemoteException;

    boolean setBackupRestoreDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setDevelopmentOptionsDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setInterceptAllNotifications(boolean z) throws RemoteException;

    boolean setInterceptNonSystemNotifications(boolean z) throws RemoteException;

    boolean setRestoreFactoryDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setSIMLockDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setSearchIndexDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setTimeAndDateSetDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setTopWatermarkDisable() throws RemoteException;

    void setTopWatermarkEnable(String str) throws RemoteException;

    boolean shouldInterceptAllNotifications() throws RemoteException;

    boolean shouldInterceptNonSystemNotifications() throws RemoteException;

    void storeLastManualScreenOffTimeout(ComponentName componentName, int i) throws RemoteException;

    boolean turnOnProtectEyes(ComponentName componentName, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeSettingsManagerService {
        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean turnOnProtectEyes(ComponentName componentName, boolean on) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isProtectEyesOn(ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setSIMLockDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isSIMLockDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public void setTopWatermarkEnable(String displayText) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public void setTopWatermarkDisable() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean getTopWatermark() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setBackupRestoreDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isBackupRestoreDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setAutoScreenOffTime(ComponentName componentName, long millis) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public long getAutoScreenOffTime(ComponentName componentName) throws RemoteException {
            return 0L;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isScreenOffTimeSetByPolicy(ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public void storeLastManualScreenOffTimeout(ComponentName componentName, int value) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setSearchIndexDisabled(ComponentName componentName, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isSearchIndexDisabled(ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public String getRomVersion(ComponentName componentName) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setRestoreFactoryDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isRestoreFactoryDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setDevelopmentOptionsDisabled(ComponentName componentName, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isDeveloperOptionsDisabled(ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setTimeAndDateSetDisabled(ComponentName componentName, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean isTimeAndDateSetDisabled(ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setInterceptAllNotifications(boolean intercepted) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean shouldInterceptAllNotifications() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean setInterceptNonSystemNotifications(boolean intercepted) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public boolean shouldInterceptNonSystemNotifications() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public void addManageNotificationToWhiteList(List<String> pkgList) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public void removeManageNotificationFromWhiteList(List<String> pkgList) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public List<String> getManageNotificationWhiteList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSettingsManagerService
        public void deleteManageNotificationFromWhiteList() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeSettingsManagerService {
        static final int TRANSACTION_addManageNotificationToWhiteList = 27;
        static final int TRANSACTION_deleteManageNotificationFromWhiteList = 30;
        static final int TRANSACTION_getAutoScreenOffTime = 11;
        static final int TRANSACTION_getManageNotificationWhiteList = 29;
        static final int TRANSACTION_getRomVersion = 16;
        static final int TRANSACTION_getTopWatermark = 7;
        static final int TRANSACTION_isBackupRestoreDisabled = 9;
        static final int TRANSACTION_isDeveloperOptionsDisabled = 20;
        static final int TRANSACTION_isProtectEyesOn = 2;
        static final int TRANSACTION_isRestoreFactoryDisabled = 18;
        static final int TRANSACTION_isSIMLockDisabled = 4;
        static final int TRANSACTION_isScreenOffTimeSetByPolicy = 12;
        static final int TRANSACTION_isSearchIndexDisabled = 15;
        static final int TRANSACTION_isTimeAndDateSetDisabled = 22;
        static final int TRANSACTION_removeManageNotificationFromWhiteList = 28;
        static final int TRANSACTION_setAutoScreenOffTime = 10;
        static final int TRANSACTION_setBackupRestoreDisabled = 8;
        static final int TRANSACTION_setDevelopmentOptionsDisabled = 19;
        static final int TRANSACTION_setInterceptAllNotifications = 23;
        static final int TRANSACTION_setInterceptNonSystemNotifications = 25;
        static final int TRANSACTION_setRestoreFactoryDisabled = 17;
        static final int TRANSACTION_setSIMLockDisabled = 3;
        static final int TRANSACTION_setSearchIndexDisabled = 14;
        static final int TRANSACTION_setTimeAndDateSetDisabled = 21;
        static final int TRANSACTION_setTopWatermarkDisable = 6;
        static final int TRANSACTION_setTopWatermarkEnable = 5;
        static final int TRANSACTION_shouldInterceptAllNotifications = 24;
        static final int TRANSACTION_shouldInterceptNonSystemNotifications = 26;
        static final int TRANSACTION_storeLastManualScreenOffTimeout = 13;
        static final int TRANSACTION_turnOnProtectEyes = 1;

        public Stub() {
            attachInterface(this, IOplusCustomizeSettingsManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeSettingsManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeSettingsManagerService)) {
                return (IOplusCustomizeSettingsManagerService) iin;
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
                    return "turnOnProtectEyes";
                case 2:
                    return "isProtectEyesOn";
                case 3:
                    return "setSIMLockDisabled";
                case 4:
                    return "isSIMLockDisabled";
                case 5:
                    return "setTopWatermarkEnable";
                case 6:
                    return "setTopWatermarkDisable";
                case 7:
                    return "getTopWatermark";
                case 8:
                    return "setBackupRestoreDisabled";
                case 9:
                    return "isBackupRestoreDisabled";
                case 10:
                    return "setAutoScreenOffTime";
                case 11:
                    return "getAutoScreenOffTime";
                case 12:
                    return "isScreenOffTimeSetByPolicy";
                case 13:
                    return "storeLastManualScreenOffTimeout";
                case 14:
                    return "setSearchIndexDisabled";
                case 15:
                    return "isSearchIndexDisabled";
                case 16:
                    return "getRomVersion";
                case 17:
                    return "setRestoreFactoryDisabled";
                case 18:
                    return "isRestoreFactoryDisabled";
                case 19:
                    return "setDevelopmentOptionsDisabled";
                case 20:
                    return "isDeveloperOptionsDisabled";
                case 21:
                    return "setTimeAndDateSetDisabled";
                case 22:
                    return "isTimeAndDateSetDisabled";
                case 23:
                    return "setInterceptAllNotifications";
                case 24:
                    return "shouldInterceptAllNotifications";
                case 25:
                    return "setInterceptNonSystemNotifications";
                case 26:
                    return "shouldInterceptNonSystemNotifications";
                case 27:
                    return "addManageNotificationToWhiteList";
                case 28:
                    return "removeManageNotificationFromWhiteList";
                case 29:
                    return "getManageNotificationWhiteList";
                case 30:
                    return "deleteManageNotificationFromWhiteList";
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
                data.enforceInterface(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ComponentName _arg0 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result = turnOnProtectEyes(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            ComponentName _arg02 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result2 = isProtectEyesOn(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            ComponentName _arg03 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result3 = setSIMLockDisabled(_arg03, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            ComponentName _arg04 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result4 = isSIMLockDisabled(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            setTopWatermarkEnable(_arg05);
                            reply.writeNoException();
                            return true;
                        case 6:
                            setTopWatermarkDisable();
                            reply.writeNoException();
                            return true;
                        case 7:
                            boolean _result5 = getTopWatermark();
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 8:
                            ComponentName _arg06 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg13 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result6 = setBackupRestoreDisabled(_arg06, _arg13);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 9:
                            ComponentName _arg07 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result7 = isBackupRestoreDisabled(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 10:
                            ComponentName _arg08 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            long _arg14 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result8 = setAutoScreenOffTime(_arg08, _arg14);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 11:
                            ComponentName _arg09 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            long _result9 = getAutoScreenOffTime(_arg09);
                            reply.writeNoException();
                            reply.writeLong(_result9);
                            return true;
                        case 12:
                            ComponentName _arg010 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result10 = isScreenOffTimeSetByPolicy(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 13:
                            ComponentName _arg011 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            storeLastManualScreenOffTimeout(_arg011, _arg15);
                            reply.writeNoException();
                            return true;
                        case 14:
                            ComponentName _arg012 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg16 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result11 = setSearchIndexDisabled(_arg012, _arg16);
                            reply.writeNoException();
                            reply.writeBoolean(_result11);
                            return true;
                        case 15:
                            ComponentName _arg013 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result12 = isSearchIndexDisabled(_arg013);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 16:
                            ComponentName _arg014 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            String _result13 = getRomVersion(_arg014);
                            reply.writeNoException();
                            reply.writeString(_result13);
                            return true;
                        case 17:
                            ComponentName _arg015 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg17 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result14 = setRestoreFactoryDisabled(_arg015, _arg17);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 18:
                            ComponentName _arg016 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result15 = isRestoreFactoryDisabled(_arg016);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 19:
                            ComponentName _arg017 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg18 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result16 = setDevelopmentOptionsDisabled(_arg017, _arg18);
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 20:
                            ComponentName _arg018 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result17 = isDeveloperOptionsDisabled(_arg018);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 21:
                            ComponentName _arg019 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg19 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result18 = setTimeAndDateSetDisabled(_arg019, _arg19);
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 22:
                            ComponentName _arg020 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result19 = isTimeAndDateSetDisabled(_arg020);
                            reply.writeNoException();
                            reply.writeBoolean(_result19);
                            return true;
                        case 23:
                            boolean _arg021 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result20 = setInterceptAllNotifications(_arg021);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 24:
                            boolean _result21 = shouldInterceptAllNotifications();
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 25:
                            boolean _arg022 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result22 = setInterceptNonSystemNotifications(_arg022);
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        case 26:
                            boolean _result23 = shouldInterceptNonSystemNotifications();
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 27:
                            List<String> _arg023 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addManageNotificationToWhiteList(_arg023);
                            reply.writeNoException();
                            return true;
                        case 28:
                            List<String> _arg024 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeManageNotificationFromWhiteList(_arg024);
                            reply.writeNoException();
                            return true;
                        case 29:
                            List<String> _result24 = getManageNotificationWhiteList();
                            reply.writeNoException();
                            reply.writeStringList(_result24);
                            return true;
                        case 30:
                            deleteManageNotificationFromWhiteList();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeSettingsManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeSettingsManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean turnOnProtectEyes(ComponentName componentName, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeBoolean(on);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isProtectEyesOn(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setSIMLockDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isSIMLockDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public void setTopWatermarkEnable(String displayText) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeString(displayText);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public void setTopWatermarkDisable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean getTopWatermark() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setBackupRestoreDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isBackupRestoreDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setAutoScreenOffTime(ComponentName componentName, long millis) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeLong(millis);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public long getAutoScreenOffTime(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isScreenOffTimeSetByPolicy(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public void storeLastManualScreenOffTimeout(ComponentName componentName, int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeInt(value);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setSearchIndexDisabled(ComponentName componentName, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isSearchIndexDisabled(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public String getRomVersion(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setRestoreFactoryDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isRestoreFactoryDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setDevelopmentOptionsDisabled(ComponentName componentName, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isDeveloperOptionsDisabled(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setTimeAndDateSetDisabled(ComponentName componentName, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean isTimeAndDateSetDisabled(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setInterceptAllNotifications(boolean intercepted) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeBoolean(intercepted);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean shouldInterceptAllNotifications() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean setInterceptNonSystemNotifications(boolean intercepted) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeBoolean(intercepted);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public boolean shouldInterceptNonSystemNotifications() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public void addManageNotificationToWhiteList(List<String> pkgList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgList);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public void removeManageNotificationFromWhiteList(List<String> pkgList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgList);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public List<String> getManageNotificationWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSettingsManagerService
            public void deleteManageNotificationFromWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSettingsManagerService.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 29;
        }
    }
}
