package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeConnectivityManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeConnectivityManagerService";

    boolean addBluetoothDevicesToBlackLists(List<String> list) throws RemoteException;

    boolean addBluetoothDevicesToWhiteLists(List<String> list) throws RemoteException;

    List<String> getBluetoothDevicesFromBlackLists() throws RemoteException;

    List<String> getBluetoothDevicesFromWhiteLists() throws RemoteException;

    int getBluetoothPolicies() throws RemoteException;

    String getDevicePosition(ComponentName componentName) throws RemoteException;

    int getSecurityLevel() throws RemoteException;

    int getWifiApPolicies() throws RemoteException;

    String getWifiMacAddress() throws RemoteException;

    List<String> getWifiRestrictionList(String str) throws RemoteException;

    List<String> getWlanApClientBlackList() throws RemoteException;

    List<String> getWlanApClientWhiteList() throws RemoteException;

    List<String> getWlanConfiguration() throws RemoteException;

    int getWlanPolicies(ComponentName componentName) throws RemoteException;

    boolean isBlackListedDevice(String str) throws RemoteException;

    boolean isGPSDisabled(ComponentName componentName) throws RemoteException;

    boolean isGPSTurnOn(ComponentName componentName) throws RemoteException;

    boolean isNetworkSettingsResetDisabled() throws RemoteException;

    boolean isUnSecureSoftApDisabled() throws RemoteException;

    boolean isUserProfilesDisabled() throws RemoteException;

    boolean isWhiteListedDevice(String str) throws RemoteException;

    boolean isWifiApDisabled() throws RemoteException;

    boolean isWifiAutoConnectionDisabled() throws RemoteException;

    boolean isWifiEditDisabled() throws RemoteException;

    boolean isWifiP2pDisabled() throws RemoteException;

    boolean isWlanForceDisabled() throws RemoteException;

    boolean isWlanForceEnabled() throws RemoteException;

    boolean removeBluetoothDevicesFromBlackLists(List<String> list) throws RemoteException;

    boolean removeBluetoothDevicesFromWhiteLists(List<String> list) throws RemoteException;

    boolean removeFromRestrictionList(List<String> list, String str) throws RemoteException;

    boolean removeWlanApClientBlackList(List<String> list) throws RemoteException;

    boolean setBluetoothPolicies(int i) throws RemoteException;

    void setGPSDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setNetworkSettingsResetDisabled(boolean z) throws RemoteException;

    boolean setSecurityLevel(int i) throws RemoteException;

    boolean setUnSecureSoftApDisabled(boolean z) throws RemoteException;

    boolean setUserProfilesDisabled(boolean z) throws RemoteException;

    boolean setWifiApDisabled(boolean z) throws RemoteException;

    boolean setWifiApPolicies(int i) throws RemoteException;

    boolean setWifiAutoConnectionDisabled(boolean z) throws RemoteException;

    boolean setWifiEditDisabled(boolean z) throws RemoteException;

    boolean setWifiP2pDisabled(boolean z) throws RemoteException;

    boolean setWifiRestrictionList(List<String> list, String str) throws RemoteException;

    boolean setWlanApClientBlackList(List<String> list) throws RemoteException;

    boolean setWlanPolicies(ComponentName componentName, int i) throws RemoteException;

    void turnOnGPS(ComponentName componentName, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeConnectivityManagerService {
        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setUserProfilesDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isUserProfilesDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public int getBluetoothPolicies() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setBluetoothPolicies(int paramInt) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean addBluetoothDevicesToWhiteLists(List<String> devices) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isWhiteListedDevice(String device) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public List<String> getBluetoothDevicesFromWhiteLists() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean removeBluetoothDevicesFromWhiteLists(List<String> devices) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean addBluetoothDevicesToBlackLists(List<String> devices) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isBlackListedDevice(String device) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public List<String> getBluetoothDevicesFromBlackLists() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean removeBluetoothDevicesFromBlackLists(List<String> devices) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWifiEditDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isWifiEditDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWifiRestrictionList(List<String> list, String type) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean removeFromRestrictionList(List<String> list, String type) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public List<String> getWifiRestrictionList(String type) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWifiAutoConnectionDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isWifiAutoConnectionDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setSecurityLevel(int level) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public int getSecurityLevel() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWifiApDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isWifiApDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWifiP2pDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isWifiP2pDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWifiApPolicies(int level) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public int getWifiApPolicies() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public List<String> getWlanApClientWhiteList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public List<String> getWlanApClientBlackList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWlanApClientBlackList(List<String> list) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean removeWlanApClientBlackList(List<String> list) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public List<String> getWlanConfiguration() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public String getWifiMacAddress() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public void turnOnGPS(ComponentName admin, boolean on) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isGPSTurnOn(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public void setGPSDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isGPSDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public String getDevicePosition(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setUnSecureSoftApDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isUnSecureSoftApDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setWlanPolicies(ComponentName admin, int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public int getWlanPolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isWlanForceEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isWlanForceDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean setNetworkSettingsResetDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
        public boolean isNetworkSettingsResetDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeConnectivityManagerService {
        static final int TRANSACTION_addBluetoothDevicesToBlackLists = 9;
        static final int TRANSACTION_addBluetoothDevicesToWhiteLists = 5;
        static final int TRANSACTION_getBluetoothDevicesFromBlackLists = 11;
        static final int TRANSACTION_getBluetoothDevicesFromWhiteLists = 7;
        static final int TRANSACTION_getBluetoothPolicies = 3;
        static final int TRANSACTION_getDevicePosition = 38;
        static final int TRANSACTION_getSecurityLevel = 21;
        static final int TRANSACTION_getWifiApPolicies = 27;
        static final int TRANSACTION_getWifiMacAddress = 33;
        static final int TRANSACTION_getWifiRestrictionList = 17;
        static final int TRANSACTION_getWlanApClientBlackList = 29;
        static final int TRANSACTION_getWlanApClientWhiteList = 28;
        static final int TRANSACTION_getWlanConfiguration = 32;
        static final int TRANSACTION_getWlanPolicies = 42;
        static final int TRANSACTION_isBlackListedDevice = 10;
        static final int TRANSACTION_isGPSDisabled = 37;
        static final int TRANSACTION_isGPSTurnOn = 35;
        static final int TRANSACTION_isNetworkSettingsResetDisabled = 46;
        static final int TRANSACTION_isUnSecureSoftApDisabled = 40;
        static final int TRANSACTION_isUserProfilesDisabled = 2;
        static final int TRANSACTION_isWhiteListedDevice = 6;
        static final int TRANSACTION_isWifiApDisabled = 23;
        static final int TRANSACTION_isWifiAutoConnectionDisabled = 19;
        static final int TRANSACTION_isWifiEditDisabled = 14;
        static final int TRANSACTION_isWifiP2pDisabled = 25;
        static final int TRANSACTION_isWlanForceDisabled = 44;
        static final int TRANSACTION_isWlanForceEnabled = 43;
        static final int TRANSACTION_removeBluetoothDevicesFromBlackLists = 12;
        static final int TRANSACTION_removeBluetoothDevicesFromWhiteLists = 8;
        static final int TRANSACTION_removeFromRestrictionList = 16;
        static final int TRANSACTION_removeWlanApClientBlackList = 31;
        static final int TRANSACTION_setBluetoothPolicies = 4;
        static final int TRANSACTION_setGPSDisabled = 36;
        static final int TRANSACTION_setNetworkSettingsResetDisabled = 45;
        static final int TRANSACTION_setSecurityLevel = 20;
        static final int TRANSACTION_setUnSecureSoftApDisabled = 39;
        static final int TRANSACTION_setUserProfilesDisabled = 1;
        static final int TRANSACTION_setWifiApDisabled = 22;
        static final int TRANSACTION_setWifiApPolicies = 26;
        static final int TRANSACTION_setWifiAutoConnectionDisabled = 18;
        static final int TRANSACTION_setWifiEditDisabled = 13;
        static final int TRANSACTION_setWifiP2pDisabled = 24;
        static final int TRANSACTION_setWifiRestrictionList = 15;
        static final int TRANSACTION_setWlanApClientBlackList = 30;
        static final int TRANSACTION_setWlanPolicies = 41;
        static final int TRANSACTION_turnOnGPS = 34;

        public Stub() {
            attachInterface(this, IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeConnectivityManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeConnectivityManagerService)) {
                return (IOplusCustomizeConnectivityManagerService) iin;
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
                    return "setUserProfilesDisabled";
                case 2:
                    return "isUserProfilesDisabled";
                case 3:
                    return "getBluetoothPolicies";
                case 4:
                    return "setBluetoothPolicies";
                case 5:
                    return "addBluetoothDevicesToWhiteLists";
                case 6:
                    return "isWhiteListedDevice";
                case 7:
                    return "getBluetoothDevicesFromWhiteLists";
                case 8:
                    return "removeBluetoothDevicesFromWhiteLists";
                case 9:
                    return "addBluetoothDevicesToBlackLists";
                case 10:
                    return "isBlackListedDevice";
                case 11:
                    return "getBluetoothDevicesFromBlackLists";
                case 12:
                    return "removeBluetoothDevicesFromBlackLists";
                case 13:
                    return "setWifiEditDisabled";
                case 14:
                    return "isWifiEditDisabled";
                case 15:
                    return "setWifiRestrictionList";
                case 16:
                    return "removeFromRestrictionList";
                case 17:
                    return "getWifiRestrictionList";
                case 18:
                    return "setWifiAutoConnectionDisabled";
                case 19:
                    return "isWifiAutoConnectionDisabled";
                case 20:
                    return "setSecurityLevel";
                case 21:
                    return "getSecurityLevel";
                case 22:
                    return "setWifiApDisabled";
                case 23:
                    return "isWifiApDisabled";
                case 24:
                    return "setWifiP2pDisabled";
                case 25:
                    return "isWifiP2pDisabled";
                case 26:
                    return "setWifiApPolicies";
                case 27:
                    return "getWifiApPolicies";
                case 28:
                    return "getWlanApClientWhiteList";
                case 29:
                    return "getWlanApClientBlackList";
                case 30:
                    return "setWlanApClientBlackList";
                case 31:
                    return "removeWlanApClientBlackList";
                case 32:
                    return "getWlanConfiguration";
                case 33:
                    return "getWifiMacAddress";
                case 34:
                    return "turnOnGPS";
                case 35:
                    return "isGPSTurnOn";
                case 36:
                    return "setGPSDisabled";
                case 37:
                    return "isGPSDisabled";
                case 38:
                    return "getDevicePosition";
                case 39:
                    return "setUnSecureSoftApDisabled";
                case 40:
                    return "isUnSecureSoftApDisabled";
                case 41:
                    return "setWlanPolicies";
                case 42:
                    return "getWlanPolicies";
                case 43:
                    return "isWlanForceEnabled";
                case 44:
                    return "isWlanForceDisabled";
                case 45:
                    return "setNetworkSettingsResetDisabled";
                case 46:
                    return "isNetworkSettingsResetDisabled";
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
                data.enforceInterface(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result = setUserProfilesDisabled(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _result2 = isUserProfilesDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            int _result3 = getBluetoothPolicies();
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result4 = setBluetoothPolicies(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 5:
                            List<String> _arg03 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result5 = addBluetoothDevicesToWhiteLists(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result6 = isWhiteListedDevice(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 7:
                            List<String> _result7 = getBluetoothDevicesFromWhiteLists();
                            reply.writeNoException();
                            reply.writeStringList(_result7);
                            return true;
                        case 8:
                            List<String> _arg05 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result8 = removeBluetoothDevicesFromWhiteLists(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 9:
                            List<String> _arg06 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result9 = addBluetoothDevicesToBlackLists(_arg06);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 10:
                            String _arg07 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result10 = isBlackListedDevice(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 11:
                            List<String> _result11 = getBluetoothDevicesFromBlackLists();
                            reply.writeNoException();
                            reply.writeStringList(_result11);
                            return true;
                        case 12:
                            List<String> _arg08 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result12 = removeBluetoothDevicesFromBlackLists(_arg08);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 13:
                            boolean _arg09 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result13 = setWifiEditDisabled(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 14:
                            boolean _result14 = isWifiEditDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 15:
                            List<String> _arg010 = data.createStringArrayList();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result15 = setWifiRestrictionList(_arg010, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 16:
                            List<String> _arg011 = data.createStringArrayList();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result16 = removeFromRestrictionList(_arg011, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 17:
                            String _arg012 = data.readString();
                            data.enforceNoDataAvail();
                            List<String> _result17 = getWifiRestrictionList(_arg012);
                            reply.writeNoException();
                            reply.writeStringList(_result17);
                            return true;
                        case 18:
                            boolean _arg013 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result18 = setWifiAutoConnectionDisabled(_arg013);
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 19:
                            boolean _result19 = isWifiAutoConnectionDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result19);
                            return true;
                        case 20:
                            int _arg014 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result20 = setSecurityLevel(_arg014);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 21:
                            int _result21 = getSecurityLevel();
                            reply.writeNoException();
                            reply.writeInt(_result21);
                            return true;
                        case 22:
                            boolean _arg015 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result22 = setWifiApDisabled(_arg015);
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        case 23:
                            boolean _result23 = isWifiApDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 24:
                            boolean _arg016 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result24 = setWifiP2pDisabled(_arg016);
                            reply.writeNoException();
                            reply.writeBoolean(_result24);
                            return true;
                        case 25:
                            boolean _result25 = isWifiP2pDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 26:
                            int _arg017 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result26 = setWifiApPolicies(_arg017);
                            reply.writeNoException();
                            reply.writeBoolean(_result26);
                            return true;
                        case 27:
                            int _result27 = getWifiApPolicies();
                            reply.writeNoException();
                            reply.writeInt(_result27);
                            return true;
                        case 28:
                            List<String> _result28 = getWlanApClientWhiteList();
                            reply.writeNoException();
                            reply.writeStringList(_result28);
                            return true;
                        case 29:
                            List<String> _result29 = getWlanApClientBlackList();
                            reply.writeNoException();
                            reply.writeStringList(_result29);
                            return true;
                        case 30:
                            List<String> _arg018 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result30 = setWlanApClientBlackList(_arg018);
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 31:
                            List<String> _arg019 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result31 = removeWlanApClientBlackList(_arg019);
                            reply.writeNoException();
                            reply.writeBoolean(_result31);
                            return true;
                        case 32:
                            List<String> _result32 = getWlanConfiguration();
                            reply.writeNoException();
                            reply.writeStringList(_result32);
                            return true;
                        case 33:
                            String _result33 = getWifiMacAddress();
                            reply.writeNoException();
                            reply.writeString(_result33);
                            return true;
                        case 34:
                            ComponentName _arg020 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg13 = data.readBoolean();
                            data.enforceNoDataAvail();
                            turnOnGPS(_arg020, _arg13);
                            reply.writeNoException();
                            return true;
                        case 35:
                            ComponentName _arg021 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result34 = isGPSTurnOn(_arg021);
                            reply.writeNoException();
                            reply.writeBoolean(_result34);
                            return true;
                        case 36:
                            ComponentName _arg022 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg14 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setGPSDisabled(_arg022, _arg14);
                            reply.writeNoException();
                            return true;
                        case 37:
                            ComponentName _arg023 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result35 = isGPSDisabled(_arg023);
                            reply.writeNoException();
                            reply.writeBoolean(_result35);
                            return true;
                        case 38:
                            ComponentName _arg024 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            String _result36 = getDevicePosition(_arg024);
                            reply.writeNoException();
                            reply.writeString(_result36);
                            return true;
                        case 39:
                            boolean _arg025 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result37 = setUnSecureSoftApDisabled(_arg025);
                            reply.writeNoException();
                            reply.writeBoolean(_result37);
                            return true;
                        case 40:
                            boolean _result38 = isUnSecureSoftApDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result38);
                            return true;
                        case 41:
                            ComponentName _arg026 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result39 = setWlanPolicies(_arg026, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result39);
                            return true;
                        case 42:
                            ComponentName _arg027 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result40 = getWlanPolicies(_arg027);
                            reply.writeNoException();
                            reply.writeInt(_result40);
                            return true;
                        case 43:
                            boolean _result41 = isWlanForceEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result41);
                            return true;
                        case 44:
                            boolean _result42 = isWlanForceDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result42);
                            return true;
                        case 45:
                            boolean _arg028 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result43 = setNetworkSettingsResetDisabled(_arg028);
                            reply.writeNoException();
                            reply.writeBoolean(_result43);
                            return true;
                        case 46:
                            boolean _result44 = isNetworkSettingsResetDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result44);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeConnectivityManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeConnectivityManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setUserProfilesDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isUserProfilesDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public int getBluetoothPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setBluetoothPolicies(int paramInt) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeInt(paramInt);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean addBluetoothDevicesToWhiteLists(List<String> devices) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(devices);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isWhiteListedDevice(String device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeString(device);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public List<String> getBluetoothDevicesFromWhiteLists() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean removeBluetoothDevicesFromWhiteLists(List<String> devices) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(devices);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean addBluetoothDevicesToBlackLists(List<String> devices) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(devices);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isBlackListedDevice(String device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeString(device);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public List<String> getBluetoothDevicesFromBlackLists() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean removeBluetoothDevicesFromBlackLists(List<String> devices) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(devices);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWifiEditDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isWifiEditDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWifiRestrictionList(List<String> list, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeString(type);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean removeFromRestrictionList(List<String> list, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeString(type);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public List<String> getWifiRestrictionList(String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeString(type);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWifiAutoConnectionDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isWifiAutoConnectionDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setSecurityLevel(int level) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeInt(level);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public int getSecurityLevel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWifiApDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isWifiApDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWifiP2pDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isWifiP2pDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWifiApPolicies(int level) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeInt(level);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public int getWifiApPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public List<String> getWlanApClientWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public List<String> getWlanApClientBlackList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWlanApClientBlackList(List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(list);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean removeWlanApClientBlackList(List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeStringList(list);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public List<String> getWlanConfiguration() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public String getWifiMacAddress() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public void turnOnGPS(ComponentName admin, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(on);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isGPSTurnOn(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public void setGPSDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isGPSDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public String getDevicePosition(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setUnSecureSoftApDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isUnSecureSoftApDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setWlanPolicies(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public int getWlanPolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isWlanForceEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isWlanForceDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean setNetworkSettingsResetDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeConnectivityManagerService
            public boolean isNetworkSettingsResetDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeConnectivityManagerService.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
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
            return 45;
        }
    }
}
