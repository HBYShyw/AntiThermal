package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeConnectivityManagerService;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeConnectivityManager {
    private static final String SERVICE_NAME = "OplusCustomizeConnectivityManagerService";
    private static final String TAG = "OplusCustomizeConnectivityManager";
    public static final int WLAN_POLICY_DEFAULT = 2;
    public static final int WLAN_POLICY_FORCE_OFF = 0;
    public static final int WLAN_POLICY_FORCE_ON = 5;
    public static final int WLAN_POLICY_OFF = 3;
    public static final int WLAN_POLICY_OFF_WITH_SCAN = 1;
    public static final int WLAN_POLICY_ON = 4;
    public static final String WLAN_POLICY_STRING_DEFAULT = "2";
    public static final String WLAN_POLICY_STRING_FORCE_OFF = "0";
    public static final String WLAN_POLICY_STRING_FORCE_ON = "5";
    public static final String WLAN_POLICY_STRING_OFF = "3";
    public static final String WLAN_POLICY_STRING_OFF_WITH_SCAN = "1";
    public static final String WLAN_POLICY_STRING_ON = "4";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeConnectivityManager sInstance;
    private IOplusCustomizeConnectivityManagerService mOplusCustomizeConnectivityManagerServiceService;

    private OplusCustomizeConnectivityManager() {
        getOplusCustomizeConnectivityManagerService();
    }

    public static final OplusCustomizeConnectivityManager getInstance(Context context) {
        OplusCustomizeConnectivityManager oplusCustomizeConnectivityManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeConnectivityManager();
                }
                oplusCustomizeConnectivityManager = sInstance;
            }
            return oplusCustomizeConnectivityManager;
        }
        return sInstance;
    }

    private IOplusCustomizeConnectivityManagerService getOplusCustomizeConnectivityManagerService() {
        IOplusCustomizeConnectivityManagerService iOplusCustomizeConnectivityManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeConnectivityManagerServiceService == null) {
                this.mOplusCustomizeConnectivityManagerServiceService = IOplusCustomizeConnectivityManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeConnectivityManagerService = this.mOplusCustomizeConnectivityManagerServiceService;
        }
        return iOplusCustomizeConnectivityManagerService;
    }

    public boolean setUserProfilesDisabled(boolean disable) {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().setUserProfilesDisabled(disable);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setUserProfilesDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setUserProfilesDisabled Error" + e2);
            return false;
        }
    }

    public boolean isUserProfilesDisabled() {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().isUserProfilesDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isUserProfilesDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isUserProfilesDisabled Error" + e2);
            return false;
        }
    }

    public int getBluetoothPolicies() {
        try {
            return getOplusCustomizeConnectivityManagerService().getBluetoothPolicies();
        } catch (Exception e) {
            Slog.e(TAG, "getBluetoothPolicies fail!");
            return 2;
        }
    }

    public boolean setBluetoothPolicies(int paramInt) {
        try {
            return getOplusCustomizeConnectivityManagerService().setBluetoothPolicies(paramInt);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothPolicies fail!");
            return false;
        }
    }

    public boolean addBluetoothDevicesToWhiteLists(List<String> devices) {
        try {
            return getOplusCustomizeConnectivityManagerService().addBluetoothDevicesToWhiteLists(devices);
        } catch (Exception e) {
            Slog.e(TAG, "addBluetoothDevicesToWhiteLists fail!");
            return false;
        }
    }

    public boolean isWhiteListedDevice(String device) {
        try {
            return getOplusCustomizeConnectivityManagerService().isWhiteListedDevice(device);
        } catch (Exception e) {
            Slog.e(TAG, "isWhiteListedDevice fail!");
            return false;
        }
    }

    public List<String> getBluetoothDevicesFromWhiteLists() {
        try {
            return getOplusCustomizeConnectivityManagerService().getBluetoothDevicesFromWhiteLists();
        } catch (Exception e) {
            Slog.e(TAG, "getBluetoothDevicesFromWhiteLists fail!");
            return new ArrayList();
        }
    }

    public boolean removeBluetoothDevicesFromWhiteLists(List<String> devices) {
        try {
            return getOplusCustomizeConnectivityManagerService().removeBluetoothDevicesFromWhiteLists(devices);
        } catch (Exception e) {
            Slog.e(TAG, "getBluetoothDevicesFromWhiteLists fail!");
            return false;
        }
    }

    public boolean addBluetoothDevicesToBlackLists(List<String> devices) {
        try {
            return getOplusCustomizeConnectivityManagerService().addBluetoothDevicesToBlackLists(devices);
        } catch (Exception e) {
            Slog.e(TAG, "addBluetoothDevicesToBlackLists fail!");
            return false;
        }
    }

    public boolean isBlackListedDevice(String device) {
        try {
            return getOplusCustomizeConnectivityManagerService().isBlackListedDevice(device);
        } catch (Exception e) {
            Slog.e(TAG, "isBlackListedDevice fail!");
            return false;
        }
    }

    public List<String> getBluetoothDevicesFromBlackLists() {
        try {
            return getOplusCustomizeConnectivityManagerService().getBluetoothDevicesFromBlackLists();
        } catch (Exception e) {
            Slog.e(TAG, "getBluetoothDevicesFromBlackLists fail!");
            return new ArrayList();
        }
    }

    public boolean removeBluetoothDevicesFromBlackLists(List<String> devices) {
        try {
            return getOplusCustomizeConnectivityManagerService().removeBluetoothDevicesFromBlackLists(devices);
        } catch (Exception e) {
            Slog.e(TAG, "removeBluetoothDevicesFromBlackLists fail!");
            return false;
        }
    }

    public boolean setWifiEditDisabled(boolean disable) {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().setWifiEditDisabled(disable);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiEditDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setWifiEditDisabled Error" + e2);
            return false;
        }
    }

    public boolean isWifiEditDisabled() {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().isWifiEditDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isWifiEditDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isWifiEditDisabled Error" + e2);
            return false;
        }
    }

    public boolean setWifiRestrictionList(List<String> list, String type) {
        try {
            return getOplusCustomizeConnectivityManagerService().setWifiRestrictionList(list, type);
        } catch (Exception e) {
            Slog.e(TAG, "setWifiRestrictionList Error" + e);
            return false;
        }
    }

    public boolean removeFromRestrictionList(List<String> list, String type) {
        try {
            return getOplusCustomizeConnectivityManagerService().removeFromRestrictionList(list, type);
        } catch (Exception e) {
            Slog.e(TAG, "removeFromRestrictionList Error" + e);
            return false;
        }
    }

    public List<String> getWifiRestrictionList(String type) {
        try {
            return getOplusCustomizeConnectivityManagerService().getWifiRestrictionList(type);
        } catch (Exception e) {
            Slog.e(TAG, "getWifiRestrictionList Error" + e);
            return null;
        }
    }

    public boolean setWifiAutoConnectionDisabled(boolean disable) {
        try {
            return getOplusCustomizeConnectivityManagerService().setWifiAutoConnectionDisabled(disable);
        } catch (Exception e) {
            Slog.e(TAG, "setWifiAutoConnectionDisabled Error" + e);
            return false;
        }
    }

    public boolean isWifiAutoConnectionDisabled() {
        try {
            return getOplusCustomizeConnectivityManagerService().isWifiAutoConnectionDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isWifiAutoConnectionDisabled Error" + e);
            return false;
        }
    }

    public boolean setSecurityLevel(int level) {
        try {
            return getOplusCustomizeConnectivityManagerService().setSecurityLevel(level);
        } catch (Exception e) {
            Slog.e(TAG, "setSecurityLevel Error" + e);
            return false;
        }
    }

    public int getSecurityLevel() {
        try {
            return getOplusCustomizeConnectivityManagerService().getSecurityLevel();
        } catch (Exception e) {
            Slog.e(TAG, "getSecurityLevel Error" + e);
            return 0;
        }
    }

    public boolean setWifiApDisabled(boolean disable) {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().setWifiApDisabled(disable);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiApDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setWifiApDisabled Error" + e2);
            return false;
        }
    }

    public boolean isWifiApDisabled() {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().isWifiApDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isWifiApDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isWifiApDisabled Error" + e2);
            return false;
        }
    }

    public boolean setWifiP2pDisabled(boolean disable) {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().setWifiP2pDisabled(disable);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiP2pDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setWifiP2pDisabled Error" + e2);
            return false;
        }
    }

    public boolean isWifiP2pDisabled() {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().isWifiP2pDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isWifiP2pDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isWifiP2pDisabled Error" + e2);
            return false;
        }
    }

    public boolean setWifiApPolicies(int level) {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().setWifiApPolicies(level);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiApPolicies", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setWifiApPolicies Error" + e2);
            return false;
        }
    }

    public int getWifiApPolicies() {
        try {
            int result = getOplusCustomizeConnectivityManagerService().getWifiApPolicies();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getWifiApPolicies", e);
            return 0;
        } catch (Exception e2) {
            Slog.e(TAG, "getWifiApPolicies Error" + e2);
            return 0;
        }
    }

    public List<String> getWlanApClientWhiteList() {
        try {
            return getOplusCustomizeConnectivityManagerService().getWlanApClientWhiteList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getWlanApWhiteList", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getWlanApWhiteList" + e2);
            return null;
        }
    }

    public List<String> getWlanApClientBlackList() {
        try {
            return getOplusCustomizeConnectivityManagerService().getWlanApClientBlackList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getWlanApClientBlackList", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getWlanApClientBlackList" + e2);
            return null;
        }
    }

    public boolean setWlanApClientBlackList(List<String> list) {
        try {
            return getOplusCustomizeConnectivityManagerService().setWlanApClientBlackList(list);
        } catch (Exception e) {
            Slog.e(TAG, "setWlanApClientBlackList Error" + e);
            return false;
        }
    }

    public boolean removeWlanApClientBlackList(List<String> list) {
        try {
            return getOplusCustomizeConnectivityManagerService().removeWlanApClientBlackList(list);
        } catch (Exception e) {
            Slog.e(TAG, "removeWlanApClientBlackList Error" + e);
            return false;
        }
    }

    public List<String> getWlanConfiguration() {
        try {
            return getOplusCustomizeConnectivityManagerService().getWlanConfiguration();
        } catch (RemoteException e) {
            Slog.e(TAG, "getWlanConfiguration", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getWlanConfiguration" + e2);
            return null;
        }
    }

    public String getWifiMacAddress() {
        try {
            return getOplusCustomizeConnectivityManagerService().getWifiMacAddress();
        } catch (RemoteException e) {
            Slog.e(TAG, "getWifiMacAddress", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getWifiMacAddress" + e2);
            return null;
        }
    }

    public void turnOnGPS(ComponentName admin, boolean on) {
        try {
            getOplusCustomizeConnectivityManagerService().turnOnGPS(admin, on);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceDisabled", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceDisabled Error" + e2);
        }
    }

    public boolean isGPSTurnOn(ComponentName admin) {
        try {
            return getOplusCustomizeConnectivityManagerService().isGPSTurnOn(admin);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceDisabled Error" + e2);
            return false;
        }
    }

    public void setGPSDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeConnectivityManagerService().setGPSDisabled(admin, disabled);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceDisabled", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceDisabled Error" + e2);
        }
    }

    public boolean isGPSDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeConnectivityManagerService().isGPSDisabled(admin);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceDisabled Error" + e2);
            return false;
        }
    }

    public String getDevicePosition(ComponentName admin) {
        try {
            return getOplusCustomizeConnectivityManagerService().getDevicePosition(admin);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceDisabled", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceDisabled Error" + e2);
            return null;
        }
    }

    public boolean setUnSecureSoftApDisabled(boolean disable) {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().setUnSecureSoftApDisabled(disable);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setUnSecureSoftApDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setUnSecureSoftApDisabled Error" + e2);
            return false;
        }
    }

    public boolean isUnSecureSoftApDisabled() {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().isUnSecureSoftApDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isUnSecureSoftApDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isUnSecureSoftApDisabled" + e2);
            return false;
        }
    }

    public boolean setWlanPolicies(ComponentName admin, int mode) {
        try {
            return getOplusCustomizeConnectivityManagerService().setWlanPolicies(admin, mode);
        } catch (Exception e) {
            Slog.e(TAG, "setWlanPolicies fail!", e);
            return false;
        }
    }

    public int getWlanPolicies(ComponentName admin) {
        try {
            return getOplusCustomizeConnectivityManagerService().getWlanPolicies(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getWlanPolicies fail!", e);
            return 2;
        }
    }

    public boolean isWlanForceEnabled() {
        try {
            return getOplusCustomizeConnectivityManagerService().isWlanForceEnabled();
        } catch (Exception e) {
            Slog.e(TAG, "isWlanForceEnabled fail!", e);
            return false;
        }
    }

    public boolean isWlanForceDisabled() {
        try {
            return getOplusCustomizeConnectivityManagerService().isWlanForceDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isWlanForceDisabled fail!", e);
            return false;
        }
    }

    public boolean setNetworkSettingsResetDisabled(boolean disabled) {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().setNetworkSettingsResetDisabled(disabled);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setNetworkSettingsResetDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setNetworkSettingsResetDisabled Error" + e2);
            return false;
        }
    }

    public boolean isNetworkSettingsResetDisabled() {
        try {
            boolean result = getOplusCustomizeConnectivityManagerService().isNetworkSettingsResetDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isNetworkSettingsResetDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isNetworkSettingsResetDisabled Error" + e2);
            return false;
        }
    }
}
