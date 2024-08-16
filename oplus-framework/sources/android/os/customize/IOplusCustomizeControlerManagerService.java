package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeControlerManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeControlerManagerService";

    void addAccessibilityServiceToWhiteList(List<String> list) throws RemoteException;

    void deleteAccessibilityServiceWhiteList() throws RemoteException;

    void disableAccessibilityService(ComponentName componentName) throws RemoteException;

    void enableAccessibilityService(ComponentName componentName) throws RemoteException;

    boolean formatSDCard(String str) throws RemoteException;

    List<ComponentName> getAccessibilityService() throws RemoteException;

    List<String> getAccessibilityServiceWhiteList() throws RemoteException;

    boolean getAirplaneMode(ComponentName componentName) throws RemoteException;

    String getCustomAnimationPath() throws RemoteException;

    String getCustomSoundPath() throws RemoteException;

    String getEnabledAccessibilityServicesName() throws RemoteException;

    void installSystemUpdate(String str) throws RemoteException;

    boolean isAccessibilityServiceEnabled() throws RemoteException;

    boolean isDisableKeyguardForgetPassword() throws RemoteException;

    boolean isDisabledKeyguardPolicy(String str) throws RemoteException;

    void rebootDevice() throws RemoteException;

    void removeAccessibilityServiceFromWhiteList(List<String> list) throws RemoteException;

    void setAirplaneMode(ComponentName componentName, boolean z) throws RemoteException;

    void setCustomAnimationPath(String str) throws RemoteException;

    void setCustomSettingsMenu(ComponentName componentName, List<String> list) throws RemoteException;

    void setCustomSoundPath(String str) throws RemoteException;

    boolean setDisableKeyguardForgetPassword(boolean z) throws RemoteException;

    void setDisabledKeyguardPolicy(boolean z, String str) throws RemoteException;

    void setMaxDelayTimeForCustomizeRebootanim(int i) throws RemoteException;

    boolean setSysTime(ComponentName componentName, long j) throws RemoteException;

    void shutdownDevice() throws RemoteException;

    void wipeDeviceData() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeControlerManagerService {
        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void shutdownDevice() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void rebootDevice() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void wipeDeviceData() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void installSystemUpdate(String updatePath) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public boolean formatSDCard(String diskId) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void enableAccessibilityService(ComponentName componentName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void disableAccessibilityService(ComponentName componentName) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public boolean isAccessibilityServiceEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public List<ComponentName> getAccessibilityService() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public String getEnabledAccessibilityServicesName() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void addAccessibilityServiceToWhiteList(List<String> serviceList) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void removeAccessibilityServiceFromWhiteList(List<String> serviceList) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public List<String> getAccessibilityServiceWhiteList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void deleteAccessibilityServiceWhiteList() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public boolean setDisableKeyguardForgetPassword(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public boolean isDisableKeyguardForgetPassword() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void setDisabledKeyguardPolicy(boolean disable, String key) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public boolean isDisabledKeyguardPolicy(String key) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void setAirplaneMode(ComponentName admin, boolean on) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public boolean getAirplaneMode(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public boolean setSysTime(ComponentName admin, long mills) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void setCustomSettingsMenu(ComponentName admin, List<String> deleteMenus) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void setMaxDelayTimeForCustomizeRebootanim(int timeout) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void setCustomAnimationPath(String customAnimPath) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public String getCustomAnimationPath() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public void setCustomSoundPath(String customSoundPath) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeControlerManagerService
        public String getCustomSoundPath() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeControlerManagerService {
        static final int TRANSACTION_addAccessibilityServiceToWhiteList = 11;
        static final int TRANSACTION_deleteAccessibilityServiceWhiteList = 14;
        static final int TRANSACTION_disableAccessibilityService = 7;
        static final int TRANSACTION_enableAccessibilityService = 6;
        static final int TRANSACTION_formatSDCard = 5;
        static final int TRANSACTION_getAccessibilityService = 9;
        static final int TRANSACTION_getAccessibilityServiceWhiteList = 13;
        static final int TRANSACTION_getAirplaneMode = 20;
        static final int TRANSACTION_getCustomAnimationPath = 25;
        static final int TRANSACTION_getCustomSoundPath = 27;
        static final int TRANSACTION_getEnabledAccessibilityServicesName = 10;
        static final int TRANSACTION_installSystemUpdate = 4;
        static final int TRANSACTION_isAccessibilityServiceEnabled = 8;
        static final int TRANSACTION_isDisableKeyguardForgetPassword = 16;
        static final int TRANSACTION_isDisabledKeyguardPolicy = 18;
        static final int TRANSACTION_rebootDevice = 2;
        static final int TRANSACTION_removeAccessibilityServiceFromWhiteList = 12;
        static final int TRANSACTION_setAirplaneMode = 19;
        static final int TRANSACTION_setCustomAnimationPath = 24;
        static final int TRANSACTION_setCustomSettingsMenu = 22;
        static final int TRANSACTION_setCustomSoundPath = 26;
        static final int TRANSACTION_setDisableKeyguardForgetPassword = 15;
        static final int TRANSACTION_setDisabledKeyguardPolicy = 17;
        static final int TRANSACTION_setMaxDelayTimeForCustomizeRebootanim = 23;
        static final int TRANSACTION_setSysTime = 21;
        static final int TRANSACTION_shutdownDevice = 1;
        static final int TRANSACTION_wipeDeviceData = 3;

        public Stub() {
            attachInterface(this, IOplusCustomizeControlerManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeControlerManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeControlerManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeControlerManagerService)) {
                return (IOplusCustomizeControlerManagerService) iin;
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
                    return "shutdownDevice";
                case 2:
                    return "rebootDevice";
                case 3:
                    return "wipeDeviceData";
                case 4:
                    return "installSystemUpdate";
                case 5:
                    return "formatSDCard";
                case 6:
                    return "enableAccessibilityService";
                case 7:
                    return "disableAccessibilityService";
                case 8:
                    return "isAccessibilityServiceEnabled";
                case 9:
                    return "getAccessibilityService";
                case 10:
                    return "getEnabledAccessibilityServicesName";
                case 11:
                    return "addAccessibilityServiceToWhiteList";
                case 12:
                    return "removeAccessibilityServiceFromWhiteList";
                case 13:
                    return "getAccessibilityServiceWhiteList";
                case 14:
                    return "deleteAccessibilityServiceWhiteList";
                case 15:
                    return "setDisableKeyguardForgetPassword";
                case 16:
                    return "isDisableKeyguardForgetPassword";
                case 17:
                    return "setDisabledKeyguardPolicy";
                case 18:
                    return "isDisabledKeyguardPolicy";
                case 19:
                    return "setAirplaneMode";
                case 20:
                    return "getAirplaneMode";
                case 21:
                    return "setSysTime";
                case 22:
                    return "setCustomSettingsMenu";
                case 23:
                    return "setMaxDelayTimeForCustomizeRebootanim";
                case 24:
                    return "setCustomAnimationPath";
                case 25:
                    return "getCustomAnimationPath";
                case 26:
                    return "setCustomSoundPath";
                case 27:
                    return "getCustomSoundPath";
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
                data.enforceInterface(IOplusCustomizeControlerManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            shutdownDevice();
                            reply.writeNoException();
                            return true;
                        case 2:
                            rebootDevice();
                            reply.writeNoException();
                            return true;
                        case 3:
                            wipeDeviceData();
                            reply.writeNoException();
                            return true;
                        case 4:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            installSystemUpdate(_arg0);
                            reply.writeNoException();
                            return true;
                        case 5:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result = formatSDCard(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 6:
                            ComponentName _arg03 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            enableAccessibilityService(_arg03);
                            reply.writeNoException();
                            return true;
                        case 7:
                            ComponentName _arg04 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            disableAccessibilityService(_arg04);
                            reply.writeNoException();
                            return true;
                        case 8:
                            boolean _result2 = isAccessibilityServiceEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 9:
                            List<ComponentName> _result3 = getAccessibilityService();
                            reply.writeNoException();
                            reply.writeTypedList(_result3, 1);
                            return true;
                        case 10:
                            String _result4 = getEnabledAccessibilityServicesName();
                            reply.writeNoException();
                            reply.writeString(_result4);
                            return true;
                        case 11:
                            List<String> _arg05 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addAccessibilityServiceToWhiteList(_arg05);
                            reply.writeNoException();
                            return true;
                        case 12:
                            List<String> _arg06 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeAccessibilityServiceFromWhiteList(_arg06);
                            reply.writeNoException();
                            return true;
                        case 13:
                            List<String> _result5 = getAccessibilityServiceWhiteList();
                            reply.writeNoException();
                            reply.writeStringList(_result5);
                            return true;
                        case 14:
                            deleteAccessibilityServiceWhiteList();
                            reply.writeNoException();
                            return true;
                        case 15:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result6 = setDisableKeyguardForgetPassword(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 16:
                            boolean _result7 = isDisableKeyguardForgetPassword();
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 17:
                            boolean _arg08 = data.readBoolean();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            setDisabledKeyguardPolicy(_arg08, _arg1);
                            reply.writeNoException();
                            return true;
                        case 18:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result8 = isDisabledKeyguardPolicy(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 19:
                            ComponentName _arg010 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAirplaneMode(_arg010, _arg12);
                            reply.writeNoException();
                            return true;
                        case 20:
                            ComponentName _arg011 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result9 = getAirplaneMode(_arg011);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 21:
                            ComponentName _arg012 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            long _arg13 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result10 = setSysTime(_arg012, _arg13);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 22:
                            ComponentName _arg013 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            List<String> _arg14 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            setCustomSettingsMenu(_arg013, _arg14);
                            reply.writeNoException();
                            return true;
                        case 23:
                            int _arg014 = data.readInt();
                            data.enforceNoDataAvail();
                            setMaxDelayTimeForCustomizeRebootanim(_arg014);
                            reply.writeNoException();
                            return true;
                        case 24:
                            String _arg015 = data.readString();
                            data.enforceNoDataAvail();
                            setCustomAnimationPath(_arg015);
                            reply.writeNoException();
                            return true;
                        case 25:
                            String _result11 = getCustomAnimationPath();
                            reply.writeNoException();
                            reply.writeString(_result11);
                            return true;
                        case 26:
                            String _arg016 = data.readString();
                            data.enforceNoDataAvail();
                            setCustomSoundPath(_arg016);
                            reply.writeNoException();
                            return true;
                        case 27:
                            String _result12 = getCustomSoundPath();
                            reply.writeNoException();
                            reply.writeString(_result12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeControlerManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeControlerManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void shutdownDevice() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void rebootDevice() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void wipeDeviceData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void installSystemUpdate(String updatePath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeString(updatePath);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public boolean formatSDCard(String diskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeString(diskId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void enableAccessibilityService(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void disableAccessibilityService(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public boolean isAccessibilityServiceEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public List<ComponentName> getAccessibilityService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    List<ComponentName> _result = _reply.createTypedArrayList(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public String getEnabledAccessibilityServicesName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void addAccessibilityServiceToWhiteList(List<String> serviceList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeStringList(serviceList);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void removeAccessibilityServiceFromWhiteList(List<String> serviceList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeStringList(serviceList);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public List<String> getAccessibilityServiceWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void deleteAccessibilityServiceWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public boolean setDisableKeyguardForgetPassword(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public boolean isDisableKeyguardForgetPassword() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void setDisabledKeyguardPolicy(boolean disable, String key) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    _data.writeString(key);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public boolean isDisabledKeyguardPolicy(String key) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void setAirplaneMode(ComponentName admin, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(on);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public boolean getAirplaneMode(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public boolean setSysTime(ComponentName admin, long mills) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeLong(mills);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void setCustomSettingsMenu(ComponentName admin, List<String> deleteMenus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeStringList(deleteMenus);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void setMaxDelayTimeForCustomizeRebootanim(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeInt(timeout);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void setCustomAnimationPath(String customAnimPath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeString(customAnimPath);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public String getCustomAnimationPath() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public void setCustomSoundPath(String customSoundPath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    _data.writeString(customSoundPath);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeControlerManagerService
            public String getCustomSoundPath() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeControlerManagerService.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 26;
        }
    }
}
