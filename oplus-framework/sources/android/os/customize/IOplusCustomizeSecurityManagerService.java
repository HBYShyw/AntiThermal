package android.os.customize;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeSecurityManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeSecurityManagerService";

    void backupAppData(int i, String str, String str2, String str3, int i2) throws RemoteException;

    Bitmap captureFullScreen() throws RemoteException;

    void clearDeviceOwner(String str) throws RemoteException;

    boolean clearMdmLog() throws RemoteException;

    void clearProfileOwner(ComponentName componentName) throws RemoteException;

    void copyFileToAppData(String str, String str2, String str3, int i) throws RemoteException;

    void deleteAppData(boolean z, String str, String str2, int i) throws RemoteException;

    boolean enableThirdRecord(boolean z) throws RemoteException;

    String executeShellToSetIptables(String str) throws RemoteException;

    List<String> getDeviceInfo(ComponentName componentName) throws RemoteException;

    ComponentName getDeviceOwner() throws RemoteException;

    CharSequence getDeviceOwnerOrganizationName() throws RemoteException;

    List<ComponentName> getEmmAdmin() throws RemoteException;

    Bundle getMobileCommSettings(ComponentName componentName, String str, String str2) throws RemoteException;

    CharSequence getOrganizationName(ComponentName componentName) throws RemoteException;

    String getPhoneNumber(int i) throws RemoteException;

    ComponentName getProfileOwner() throws RemoteException;

    int getServerType() throws RemoteException;

    boolean isCustomDevicePolicyEnabled() throws RemoteException;

    boolean isEnableThirdRecord() throws RemoteException;

    boolean isSetPasswordDisable() throws RemoteException;

    boolean isVerificationSkip() throws RemoteException;

    String[] listIccid() throws RemoteException;

    String[] listImei() throws RemoteException;

    boolean needHideKeyguardByMdm() throws RemoteException;

    String readMdmLog() throws RemoteException;

    void setCustomDevicePolicyEnabled(boolean z) throws RemoteException;

    boolean setDeviceLocked(ComponentName componentName) throws RemoteException;

    boolean setDeviceOwner(ComponentName componentName) throws RemoteException;

    boolean setDeviceUnLocked(ComponentName componentName) throws RemoteException;

    void setEmmAdmin(ComponentName componentName, boolean z) throws RemoteException;

    void setMobileCommSettings(ComponentName componentName, String str, Bundle bundle) throws RemoteException;

    void setOrganizationName(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    boolean setPasswordDisable(boolean z) throws RemoteException;

    boolean setProfileOwner(ComponentName componentName) throws RemoteException;

    boolean setServerType(int i) throws RemoteException;

    void setVerificationSkip(boolean z) throws RemoteException;

    boolean writeMdmLog(String str, String str2, String str3) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeSecurityManagerService {
        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void setEmmAdmin(ComponentName admin, boolean enable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public List<ComponentName> getEmmAdmin() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean setDeviceOwner(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public ComponentName getDeviceOwner() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void clearDeviceOwner(String packageName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void setCustomDevicePolicyEnabled(boolean enable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean isCustomDevicePolicyEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public List<String> getDeviceInfo(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public String executeShellToSetIptables(String commandline) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public String getPhoneNumber(int subId) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public Bundle getMobileCommSettings(ComponentName admin, String business, String setting) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void setMobileCommSettings(ComponentName admin, String business, Bundle bundle) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public Bitmap captureFullScreen() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean setServerType(int serverType) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public int getServerType() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean setDeviceLocked(ComponentName cn2) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean setDeviceUnLocked(ComponentName cn2) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean needHideKeyguardByMdm() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean enableThirdRecord(boolean isEnable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean isEnableThirdRecord() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean setPasswordDisable(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean isSetPasswordDisable() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public String[] listIccid() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public String[] listImei() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void backupAppData(int rootPathMode, String src, String packageName, String dest, int requestId) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean setProfileOwner(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public ComponentName getProfileOwner() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void clearProfileOwner(ComponentName admin) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void setOrganizationName(ComponentName admin, CharSequence name) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void setVerificationSkip(boolean skip) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean isVerificationSkip() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public String readMdmLog() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean writeMdmLog(String event, String result, String describe) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public boolean clearMdmLog() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void deleteAppData(boolean isDir, String src, String packageName, int requestId) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeSecurityManagerService
        public void copyFileToAppData(String from, String src, String packageName, int requestId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeSecurityManagerService {
        static final int TRANSACTION_backupAppData = 25;
        static final int TRANSACTION_captureFullScreen = 13;
        static final int TRANSACTION_clearDeviceOwner = 5;
        static final int TRANSACTION_clearMdmLog = 36;
        static final int TRANSACTION_clearProfileOwner = 28;
        static final int TRANSACTION_copyFileToAppData = 38;
        static final int TRANSACTION_deleteAppData = 37;
        static final int TRANSACTION_enableThirdRecord = 19;
        static final int TRANSACTION_executeShellToSetIptables = 9;
        static final int TRANSACTION_getDeviceInfo = 8;
        static final int TRANSACTION_getDeviceOwner = 4;
        static final int TRANSACTION_getDeviceOwnerOrganizationName = 31;
        static final int TRANSACTION_getEmmAdmin = 2;
        static final int TRANSACTION_getMobileCommSettings = 11;
        static final int TRANSACTION_getOrganizationName = 30;
        static final int TRANSACTION_getPhoneNumber = 10;
        static final int TRANSACTION_getProfileOwner = 27;
        static final int TRANSACTION_getServerType = 15;
        static final int TRANSACTION_isCustomDevicePolicyEnabled = 7;
        static final int TRANSACTION_isEnableThirdRecord = 20;
        static final int TRANSACTION_isSetPasswordDisable = 22;
        static final int TRANSACTION_isVerificationSkip = 33;
        static final int TRANSACTION_listIccid = 23;
        static final int TRANSACTION_listImei = 24;
        static final int TRANSACTION_needHideKeyguardByMdm = 18;
        static final int TRANSACTION_readMdmLog = 34;
        static final int TRANSACTION_setCustomDevicePolicyEnabled = 6;
        static final int TRANSACTION_setDeviceLocked = 16;
        static final int TRANSACTION_setDeviceOwner = 3;
        static final int TRANSACTION_setDeviceUnLocked = 17;
        static final int TRANSACTION_setEmmAdmin = 1;
        static final int TRANSACTION_setMobileCommSettings = 12;
        static final int TRANSACTION_setOrganizationName = 29;
        static final int TRANSACTION_setPasswordDisable = 21;
        static final int TRANSACTION_setProfileOwner = 26;
        static final int TRANSACTION_setServerType = 14;
        static final int TRANSACTION_setVerificationSkip = 32;
        static final int TRANSACTION_writeMdmLog = 35;

        public Stub() {
            attachInterface(this, IOplusCustomizeSecurityManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeSecurityManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeSecurityManagerService)) {
                return (IOplusCustomizeSecurityManagerService) iin;
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
                    return "setEmmAdmin";
                case 2:
                    return "getEmmAdmin";
                case 3:
                    return "setDeviceOwner";
                case 4:
                    return "getDeviceOwner";
                case 5:
                    return "clearDeviceOwner";
                case 6:
                    return "setCustomDevicePolicyEnabled";
                case 7:
                    return "isCustomDevicePolicyEnabled";
                case 8:
                    return "getDeviceInfo";
                case 9:
                    return "executeShellToSetIptables";
                case 10:
                    return "getPhoneNumber";
                case 11:
                    return "getMobileCommSettings";
                case 12:
                    return "setMobileCommSettings";
                case 13:
                    return "captureFullScreen";
                case 14:
                    return "setServerType";
                case 15:
                    return "getServerType";
                case 16:
                    return "setDeviceLocked";
                case 17:
                    return "setDeviceUnLocked";
                case 18:
                    return "needHideKeyguardByMdm";
                case 19:
                    return "enableThirdRecord";
                case 20:
                    return "isEnableThirdRecord";
                case 21:
                    return "setPasswordDisable";
                case 22:
                    return "isSetPasswordDisable";
                case 23:
                    return "listIccid";
                case 24:
                    return "listImei";
                case 25:
                    return "backupAppData";
                case 26:
                    return "setProfileOwner";
                case 27:
                    return "getProfileOwner";
                case 28:
                    return "clearProfileOwner";
                case 29:
                    return "setOrganizationName";
                case 30:
                    return "getOrganizationName";
                case 31:
                    return "getDeviceOwnerOrganizationName";
                case 32:
                    return "setVerificationSkip";
                case 33:
                    return "isVerificationSkip";
                case 34:
                    return "readMdmLog";
                case 35:
                    return "writeMdmLog";
                case 36:
                    return "clearMdmLog";
                case 37:
                    return "deleteAppData";
                case 38:
                    return "copyFileToAppData";
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
                data.enforceInterface(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ComponentName _arg0 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setEmmAdmin(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            List<ComponentName> _result = getEmmAdmin();
                            reply.writeNoException();
                            reply.writeTypedList(_result, 1);
                            return true;
                        case 3:
                            ComponentName _arg02 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result2 = setDeviceOwner(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 4:
                            ComponentName _result3 = getDeviceOwner();
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 5:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            clearDeviceOwner(_arg03);
                            reply.writeNoException();
                            return true;
                        case 6:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setCustomDevicePolicyEnabled(_arg04);
                            reply.writeNoException();
                            return true;
                        case 7:
                            boolean _result4 = isCustomDevicePolicyEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 8:
                            ComponentName _arg05 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            List<String> _result5 = getDeviceInfo(_arg05);
                            reply.writeNoException();
                            reply.writeStringList(_result5);
                            return true;
                        case 9:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            String _result6 = executeShellToSetIptables(_arg06);
                            reply.writeNoException();
                            reply.writeString(_result6);
                            return true;
                        case 10:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result7 = getPhoneNumber(_arg07);
                            reply.writeNoException();
                            reply.writeString(_result7);
                            return true;
                        case 11:
                            ComponentName _arg08 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg12 = data.readString();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            Bundle _result8 = getMobileCommSettings(_arg08, _arg12, _arg2);
                            reply.writeNoException();
                            reply.writeTypedObject(_result8, 1);
                            return true;
                        case 12:
                            ComponentName _arg09 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg13 = data.readString();
                            Bundle _arg22 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            setMobileCommSettings(_arg09, _arg13, _arg22);
                            reply.writeNoException();
                            return true;
                        case 13:
                            Bitmap _result9 = captureFullScreen();
                            reply.writeNoException();
                            reply.writeTypedObject(_result9, 1);
                            return true;
                        case 14:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result10 = setServerType(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 15:
                            int _result11 = getServerType();
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 16:
                            ComponentName _arg011 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result12 = setDeviceLocked(_arg011);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 17:
                            ComponentName _arg012 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result13 = setDeviceUnLocked(_arg012);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 18:
                            boolean _result14 = needHideKeyguardByMdm();
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 19:
                            boolean _arg013 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result15 = enableThirdRecord(_arg013);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 20:
                            boolean _result16 = isEnableThirdRecord();
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 21:
                            boolean _arg014 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result17 = setPasswordDisable(_arg014);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 22:
                            boolean _result18 = isSetPasswordDisable();
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 23:
                            String[] _result19 = listIccid();
                            reply.writeNoException();
                            reply.writeStringArray(_result19);
                            return true;
                        case 24:
                            String[] _result20 = listImei();
                            reply.writeNoException();
                            reply.writeStringArray(_result20);
                            return true;
                        case 25:
                            int _arg015 = data.readInt();
                            String _arg14 = data.readString();
                            String _arg23 = data.readString();
                            String _arg3 = data.readString();
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            backupAppData(_arg015, _arg14, _arg23, _arg3, _arg4);
                            reply.writeNoException();
                            return true;
                        case 26:
                            ComponentName _arg016 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result21 = setProfileOwner(_arg016);
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 27:
                            ComponentName _result22 = getProfileOwner();
                            reply.writeNoException();
                            reply.writeTypedObject(_result22, 1);
                            return true;
                        case 28:
                            ComponentName _arg017 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            clearProfileOwner(_arg017);
                            reply.writeNoException();
                            return true;
                        case 29:
                            ComponentName _arg018 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            CharSequence _arg15 = (CharSequence) data.readTypedObject(TextUtils.CHAR_SEQUENCE_CREATOR);
                            data.enforceNoDataAvail();
                            setOrganizationName(_arg018, _arg15);
                            reply.writeNoException();
                            return true;
                        case 30:
                            ComponentName _arg019 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            CharSequence _result23 = getOrganizationName(_arg019);
                            reply.writeNoException();
                            if (_result23 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result23, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 31:
                            CharSequence _result24 = getDeviceOwnerOrganizationName();
                            reply.writeNoException();
                            if (_result24 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result24, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 32:
                            boolean _arg020 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setVerificationSkip(_arg020);
                            reply.writeNoException();
                            return true;
                        case 33:
                            boolean _result25 = isVerificationSkip();
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 34:
                            String _result26 = readMdmLog();
                            reply.writeNoException();
                            reply.writeString(_result26);
                            return true;
                        case 35:
                            String _arg021 = data.readString();
                            String _arg16 = data.readString();
                            String _arg24 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result27 = writeMdmLog(_arg021, _arg16, _arg24);
                            reply.writeNoException();
                            reply.writeBoolean(_result27);
                            return true;
                        case 36:
                            boolean _result28 = clearMdmLog();
                            reply.writeNoException();
                            reply.writeBoolean(_result28);
                            return true;
                        case 37:
                            boolean _arg022 = data.readBoolean();
                            String _arg17 = data.readString();
                            String _arg25 = data.readString();
                            int _arg32 = data.readInt();
                            data.enforceNoDataAvail();
                            deleteAppData(_arg022, _arg17, _arg25, _arg32);
                            reply.writeNoException();
                            return true;
                        case 38:
                            String _arg023 = data.readString();
                            String _arg18 = data.readString();
                            String _arg26 = data.readString();
                            int _arg33 = data.readInt();
                            data.enforceNoDataAvail();
                            copyFileToAppData(_arg023, _arg18, _arg26, _arg33);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeSecurityManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeSecurityManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void setEmmAdmin(ComponentName admin, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public List<ComponentName> getEmmAdmin() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<ComponentName> _result = _reply.createTypedArrayList(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean setDeviceOwner(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public ComponentName getDeviceOwner() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    ComponentName _result = (ComponentName) _reply.readTypedObject(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void clearDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void setCustomDevicePolicyEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean isCustomDevicePolicyEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public List<String> getDeviceInfo(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public String executeShellToSetIptables(String commandline) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeString(commandline);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public String getPhoneNumber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public Bundle getMobileCommSettings(ComponentName admin, String business, String setting) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeString(business);
                    _data.writeString(setting);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void setMobileCommSettings(ComponentName admin, String business, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeString(business);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public Bitmap captureFullScreen() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    Bitmap _result = (Bitmap) _reply.readTypedObject(Bitmap.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean setServerType(int serverType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeInt(serverType);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public int getServerType() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean setDeviceLocked(ComponentName cn2) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(cn2, 0);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean setDeviceUnLocked(ComponentName cn2) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(cn2, 0);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean needHideKeyguardByMdm() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean enableThirdRecord(boolean isEnable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeBoolean(isEnable);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean isEnableThirdRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean setPasswordDisable(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean isSetPasswordDisable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public String[] listIccid() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public String[] listImei() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void backupAppData(int rootPathMode, String src, String packageName, String dest, int requestId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeInt(rootPathMode);
                    _data.writeString(src);
                    _data.writeString(packageName);
                    _data.writeString(dest);
                    _data.writeInt(requestId);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean setProfileOwner(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public ComponentName getProfileOwner() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    ComponentName _result = (ComponentName) _reply.readTypedObject(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void clearProfileOwner(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void setOrganizationName(ComponentName admin, CharSequence name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    if (name != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(name, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    CharSequence _result = (CharSequence) _reply.readTypedObject(TextUtils.CHAR_SEQUENCE_CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    CharSequence _result = (CharSequence) _reply.readTypedObject(TextUtils.CHAR_SEQUENCE_CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void setVerificationSkip(boolean skip) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeBoolean(skip);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean isVerificationSkip() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public String readMdmLog() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean writeMdmLog(String event, String result, String describe) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeString(event);
                    _data.writeString(result);
                    _data.writeString(describe);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public boolean clearMdmLog() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void deleteAppData(boolean isDir, String src, String packageName, int requestId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeBoolean(isDir);
                    _data.writeString(src);
                    _data.writeString(packageName);
                    _data.writeInt(requestId);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeSecurityManagerService
            public void copyFileToAppData(String from, String src, String packageName, int requestId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeSecurityManagerService.DESCRIPTOR);
                    _data.writeString(from);
                    _data.writeString(src);
                    _data.writeString(packageName);
                    _data.writeInt(requestId);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 37;
        }
    }
}
