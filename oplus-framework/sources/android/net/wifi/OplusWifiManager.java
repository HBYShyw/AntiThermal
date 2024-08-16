package android.net.wifi;

import android.content.Context;
import android.net.Network;
import android.net.wifi.IConnectListener;
import android.net.wifi.INeworkSelectionOptCb;
import android.net.wifi.IOplusWifiEventCallback;
import android.net.wifi.IOplusWifiManager;
import android.net.wifi.OplusWifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.OplusPropertyList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.SparseArray;
import com.oplus.network.OlkL2Param;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import oplus.net.wifi.HotspotClient;
import oplus.net.wifi.NeworkSelectionOptCb;

/* loaded from: classes.dex */
public class OplusWifiManager {
    public static final String ACTION_OPLUS_NETWORK_CONDITIONS_MEASURED = "android.net.conn.OPLUS_NETWORK_CONDITIONS_MEASURED";
    public static final int AUTOCONNECT_PORTAL_LOGIN = 2;
    public static final int BAND_2GHZ = 0;
    public static final int BAND_5GHZ = 1;
    public static final int ERROR_PHONE_CLONE_CONNECTED = 101;
    public static final int ERROR_PHONE_CLONE_UNDONE = 102;
    public static final String EXTRA_HOTSPOT_CLIENTS_NUM = "HotspotClientNum";
    public static final String EXTRA_SNIFFER_MODE_STATUS = "SnifferModeStatus";
    public static final String EXTRA_SNIFFER_MODE_STOPPED_REASON = "SnifferModeStoppedReason";
    public static final String IOT_CONNECT_SCAN_RESULTS_AVAILABLE_ACTION = "android.net.iot.connect.wifi.SCAN_RESULTS";
    public static final int MANUCONNECT_PORTAL_LOGIN = 1;
    public static final String NETSHARE_BUNDLE_KEY_FROM_SETTINGS = "config";
    public static final long OPLUS_WIFI_FEATURE_AUTORECONNECT = 128;
    public static final long OPLUS_WIFI_FEATURE_DBS = 16;
    public static final long OPLUS_WIFI_FEATURE_DUALSTA = 8;
    public static final long OPLUS_WIFI_FEATURE_INFRA = 1;
    public static final long OPLUS_WIFI_FEATURE_IOTConnect = 1024;
    public static final long OPLUS_WIFI_FEATURE_LIMITSPEED = 64;
    public static final long OPLUS_WIFI_FEATURE_NETSHARE = 4096;
    public static final long OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE = 8192;
    public static final long OPLUS_WIFI_FEATURE_NOT_SUPPORT_WPA3 = 512;
    public static final long OPLUS_WIFI_FEATURE_Passpoint = 2048;
    public static final long OPLUS_WIFI_FEATURE_SCC = 256;
    public static final long OPLUS_WIFI_FEATURE_SLA = 4;
    public static final long OPLUS_WIFI_FEATURE_WIFI6 = 32;
    public static final int OTHER_LOGIN = 0;
    public static final int PHONE_CLONE_STATE_DONE = 1;
    public static final int PHY_CAPACITY_HE = 8;
    public static final int PHY_CAPACITY_HT = 2;
    public static final int PHY_CAPACITY_NO_HT = 1;
    public static final int PHY_CAPACITY_VHT = 4;
    public static final int SETTINGS_UESR_DISCONNECT_NETWORK = 1;
    public static final int SETTINGS_UESR_REMOVE_NETWORK = 2;
    public static final int SUCCESS_PHONE_CLONE_HANDLED = 0;
    private static final String TAG = "OplusWifiManager";
    public static final String WIFI_HOTSPOT_CLIENTS_CHANGED_ACTION = "oplus.intent.action.WIFI_HOTSPOT_CLIENTS_CHANGED";
    public static final String WIFI_SNIFFER_MODE_CHANGED_ACTION = "android.net.wifi.WIFI_SNIFFER_MODE_CHANGED";
    public static final int WIFI_SNIFFER_MODE_STARTED = 1;
    public static final int WIFI_SNIFFER_MODE_STARTING = 0;
    public static final int WIFI_SNIFFER_MODE_START_FAIL = 2;
    public static final int WIFI_SNIFFER_MODE_STOPED = 4;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_AIRPLANE_ON = 10;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_ALWAYS_SCAN_OFF = 9;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_ALWAYS_SCAN_TOGGLED = 8;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_ENTER_EMERGENCY = 12;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_FRAMEWORK_EXCEPTION = 6;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_NATIVE_DIE = 5;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_NATIVE_EXCEPTION = 4;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_REEST_FOR_START_FAIL = 7;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_REEST_TIMEOUT = 1;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_REQUEST = 0;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_SOFTAP_TOGGLED = 3;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_UNKNOW = 255;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_WIFI_SHUTDOWN = 11;
    public static final int WIFI_SNIFFER_MODE_STOPPED_REASON_WIFI_TOGGLED = 2;
    public static final int WIFI_SNIFFER_MODE_STOPPING = 3;
    public static final int WIFI_SNIFFER_MODE_STOP_FAIL = 5;
    public static final int WIFI_SNIFFER_MODE_UNKNOW = 255;
    private Context mContext;
    private IOplusWifiManager mService;
    private static Map<String, INeworkSelectionOptCb> sOplusWifiOpenApiCbs = new HashMap();
    private static SparseArray<IOplusWifiEventCallback.Stub> sOplusWifiEventCbs = new SparseArray<>();
    private List<ActionListener> mListeners = new ArrayList();
    private boolean mVerboseLoggingEnabled = true;

    /* loaded from: classes.dex */
    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    public OplusWifiManager(Context context) {
        this.mService = null;
        this.mContext = context;
        this.mService = IOplusWifiManager.Stub.asInterface(ServiceManager.getService("opluswifikitservice"));
    }

    public long getOplusSupportedFeatures() throws RemoteException {
        try {
            long features = this.mService.getOplusSupportedFeatures();
            return features;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1L;
        }
    }

    public boolean isFeatureSupported(long feature) throws RemoteException {
        return (getOplusSupportedFeatures() & feature) == feature;
    }

    public boolean setSlaAppState(String pkgName, boolean enabled) throws RemoteException {
        try {
            boolean success = this.mService.setSlaAppState(pkgName, enabled);
            return success;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean getSlaAppState(String pkgName) throws RemoteException {
        try {
            boolean success = this.mService.getSlaAppState(pkgName);
            return success;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public String[] getAllSlaAppsAndStates() throws RemoteException {
        try {
            String[] appsStates = this.mService.getAllSlaAppsAndStates();
            return appsStates;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public long getSlaTotalTraffic() {
        try {
            long totalTraffic = this.mService.getSlaTotalTraffic();
            return totalTraffic;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0L;
        }
    }

    public String[] getAllSlaAcceleratedApps() throws RemoteException {
        try {
            String[] appsStates = this.mService.getAllSlaAcceleratedApps();
            return appsStates;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public String[] getAllDualStaApps() throws RemoteException {
        try {
            String[] appsStates = this.mService.getAllDualStaApps();
            return appsStates;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public int getSlaWorkMode() throws RemoteException {
        try {
            int type = this.mService.getSlaWorkMode();
            return type;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public WifiInfo getOplusSta2ConnectionInfo() throws RemoteException {
        ExtendsWifiInfo extWifiInfo = null;
        try {
            extWifiInfo = this.mService.getOplusSta2ConnectionInfo();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        return extWifiInfo.getWifiInfo();
    }

    public String getOplusSta2CurConfigKey() throws RemoteException {
        try {
            String configKey = this.mService.getOplusSta2CurConfigKey();
            return configKey;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void requestDualStaNetwork(String pkgName) throws RemoteException {
        try {
            this.mService.requestDualStaNetwork(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void releaseDualStaNetwork(String pkgName) throws RemoteException {
        try {
            this.mService.releaseDualStaNetwork(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void disableDualSta() throws RemoteException {
        try {
            this.mService.disableDualSta();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean isDualStaSupported() throws RemoteException {
        return isFeatureSupported(8L);
    }

    public boolean isPrimaryWifi(Network network) throws RemoteException {
        if (network == null) {
            Log.w(TAG, "isPrimaryWifi network == null");
            return false;
        }
        try {
            boolean isPrimary = this.mService.isPrimaryWifi(network);
            return isPrimary;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public Network getSecondaryWifiNetwork() throws RemoteException {
        try {
            return this.mService.getSecondaryWifiNetwork();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public boolean shouldLaunchBrowser(Network network, String redirectUrl) throws RemoteException {
        if (network == null) {
            Log.w(TAG, "shouldLaunchBrowser network == null");
            return false;
        }
        try {
            boolean launchBrowser = this.mService.shouldLaunchBrowser(network, redirectUrl);
            return launchBrowser;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public void clearWifiOCloudData(boolean hardDelete) throws RemoteException {
        try {
            this.mService.clearWifiOCloudData(hardDelete);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public List<String> getWifiOCloudData(boolean dirtyOnly) throws RemoteException {
        List<String> dataList = new ArrayList<>();
        try {
            List<String> dataList2 = this.mService.getWifiOCloudData(dirtyOnly);
            return dataList2;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return dataList;
        }
    }

    public void updateGlobalId(int itemId, String globalId) throws RemoteException {
        try {
            this.mService.updateGlobalId(itemId, globalId);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void removeNetworkByGlobalId(String configKey, String globalId, boolean hardDelete) throws RemoteException {
        try {
            this.mService.removeNetworkByGlobalId(configKey, globalId, hardDelete);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setDirtyFlag(String globalId, boolean value) throws RemoteException {
        try {
            this.mService.setDirtyFlag(globalId, value);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean hasOCloudDirtyData() throws RemoteException {
        try {
            boolean hasData = this.mService.hasOCloudDirtyData();
            return hasData;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public void dealWithCloudBackupResult(List<String> cloudData, String type) throws RemoteException {
        try {
            this.mService.dealWithCloudBackupResult(cloudData, type);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void dealWithCloudRecoveryData(List<String> cloudData, String type) throws RemoteException {
        try {
            this.mService.dealWithCloudRecoveryData(cloudData, type);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean checkPasspointCAExist(String fqdn) throws RemoteException {
        try {
            boolean state = this.mService.checkPasspointCAExist(fqdn);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean checkPasspointXMLCAExpired(String fqdn) throws RemoteException {
        try {
            boolean state = this.mService.checkPasspointXMLCAExpired(fqdn);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public List<ScanResult> passpointANQPScanResults(List<ScanResult> scanResults) throws RemoteException {
        List<ScanResult> results = null;
        try {
            results = new ArrayList<>();
            List<ScanResult> results2 = this.mService.passpointANQPScanResults(scanResults);
            return results2;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return results;
        }
    }

    public boolean checkInternalPasspointPresetProvider(String fqdn) throws RemoteException {
        try {
            boolean state = this.mService.checkInternalPasspointPresetProvider(fqdn);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean checkFWKSupportPasspoint() throws RemoteException {
        try {
            boolean state = this.mService.checkFWKSupportPasspoint();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean setPasspointCertifiedState(String signature) throws RemoteException {
        try {
            boolean state = this.mService.setPasspointCertifiedState(signature);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public String getPasspointUserName(String fqdn) throws RemoteException {
        try {
            String userName = this.mService.getPasspointUserName(fqdn);
            return userName;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public boolean getPasspointCertifiedState(String fqdn) throws RemoteException {
        try {
            boolean certified = this.mService.getPasspointCertifiedState(fqdn);
            return certified;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean isP2p5GSupported() throws RemoteException {
        try {
            boolean state = this.mService.isP2p5GSupported();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean isSoftap5GSupported() throws RemoteException {
        try {
            boolean state = this.mService.isSoftap5GSupported();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean isP2p6GHzBandSupported() throws RemoteException {
        try {
            boolean state = this.mService.isP2p6GHzBandSupported();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean isSoftap6GHzBandSupported() throws RemoteException {
        try {
            boolean state = this.mService.isSoftap6GHzBandSupported();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public String getTargetMacAddress(String targetIpAddress) {
        try {
            String targetMacAddress = this.mService.getTargetMacAddress(targetIpAddress);
            return targetMacAddress;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return "02:00:00:00:00:00";
        }
    }

    public void setBlockedHotspotClients(List<HotspotClient> clientList) throws RemoteException {
        try {
            this.mService.setBlockedHotspotClients(clientList);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setAllowedHotspotClients(List<HotspotClient> clientList) throws RemoteException {
        try {
            this.mService.setAllowedHotspotClients(clientList);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public List<HotspotClient> getBlockedHotspotClients() throws RemoteException {
        List<HotspotClient> results = null;
        try {
            results = new ArrayList<>();
            List<HotspotClient> results2 = this.mService.getBlockedHotspotClients();
            return results2;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return results;
        }
    }

    public List<HotspotClient> getAllowedHotspotClients() throws RemoteException {
        try {
            List<HotspotClient> results = this.mService.getAllowedHotspotClients();
            return results;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public List<HotspotClient> getConnectedHotspotClients() throws RemoteException {
        try {
            List<HotspotClient> results = this.mService.getConnectedHotspotClients();
            return results;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void notifyGameModeState(boolean state, String pkgName) throws RemoteException {
        try {
            this.mService.notifyGameModeState(state, pkgName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void notifyGameLatency(String lantencyInfo, String pkgName) throws RemoteException {
        try {
            this.mService.notifyGameLatency(lantencyInfo, pkgName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void notifyGameHighTemperature(int temperature, String pkgName) throws RemoteException {
        try {
            this.mService.notifyGameHighTemperature(temperature, pkgName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void notifyGameInfoJsonStr(String gameInfo) throws RemoteException {
        try {
            this.mService.notifyGameInfoJsonStr(gameInfo);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void notifyMeetingWorkingState(boolean state, String pkgName) throws RemoteException {
        try {
            this.mService.notifyMeetingWorkingState(state, pkgName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public Map<String, String> getGameMainStreamServIp() {
        try {
            return this.mService.getGameMainStreamServIp();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public int startSnifferMode(int channel, int bandwidth, int maxPacketNum, int maxPacketSize) throws RemoteException {
        try {
            int state = this.mService.startSnifferMode(channel, bandwidth, maxPacketNum, maxPacketSize);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public int stopSnifferMode() throws RemoteException {
        try {
            int state = this.mService.stopSnifferMode();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public int getSnifferState() throws RemoteException {
        try {
            int state = this.mService.getSnifferState();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public int startFTMMode() throws RemoteException {
        try {
            int state = this.mService.startFTMMode();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public int stopFTMMode() throws RemoteException {
        try {
            int state = this.mService.stopFTMMode();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public int sendFTMCommand(String ftmCmd) throws RemoteException {
        try {
            int state = this.mService.sendFTMCommand(ftmCmd);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public int getFTMState() throws RemoteException {
        try {
            this.mService.getFTMState();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        return 0;
    }

    public boolean resetConnectionMode() throws RemoteException {
        try {
            boolean state = this.mService.resetConnectionMode();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean changeConnectionMode(int mode) throws RemoteException {
        try {
            boolean state = this.mService.changeConnectionMode(mode);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean setupFTMdaemon() throws RemoteException {
        try {
            boolean state = this.mService.setupFTMdaemon();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean executeDriverCommand(String command) throws RemoteException {
        try {
            boolean state = this.mService.executeDriverCommand(command);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public String executeDriverCommandWithResult(String command) throws RemoteException {
        try {
            String result = this.mService.executeDriverCommandWithResult(command);
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return OplusPropertyList.UNKNOWN;
        }
    }

    public boolean wifiLoadDriver(boolean enable) throws RemoteException {
        try {
            boolean state = this.mService.wifiLoadDriver(enable);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean checkWiFiDriverStatus() throws RemoteException {
        try {
            boolean state = this.mService.checkWiFiDriverStatus();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public String getFwStatus() {
        try {
            String result = this.mService.getFwStatus();
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return OplusPropertyList.UNKNOWN;
        }
    }

    public String getDeviceName() {
        try {
            String result = this.mService.getDeviceName();
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return OplusPropertyList.UNKNOWN;
        }
    }

    public boolean agingTestForWifi() throws RemoteException {
        try {
            boolean state = this.mService.agingTestForWifi();
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean setWifiSmartAntennaAction(int action) throws RemoteException {
        try {
            boolean state = this.mService.setWifiSmartAntennaAction(action);
            return state;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public void removeHeIeFromProbeRequest(boolean remove) throws RemoteException {
        try {
            this.mService.removeHeIeFromProbeRequest(remove);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setP2pPowerSave(boolean enable) throws RemoteException {
        try {
            this.mService.setP2pPowerSave(enable);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public int getDBSCapacity() throws RemoteException {
        int cap = 0;
        Log.d(TAG, "getDBSCapacity");
        try {
            cap = this.mService.getDBSCapacity();
            Log.d(TAG, "getDBSCapacity cap = " + cap);
            return cap;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return cap;
        }
    }

    public int getPHYCapacity(int band) {
        try {
            int cap = this.mService.getPHYCapacity(band);
            return cap;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    public int[] getSupportedChannels(int band) throws RemoteException {
        try {
            int[] freqs = this.mService.getSupportedChannels(band);
            return freqs;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public int[] getAvoidChannels() {
        try {
            int[] freqs = this.mService.getAvoidChannels();
            return freqs;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public List<ActionListener> getListener() {
        return this.mListeners;
    }

    private void removeActionListener(int key) {
        this.mListeners.remove(key);
    }

    public boolean sendIOTConnectProbeReq(String addVendorIE, int[] channels, String hiddenSSIDList) throws RemoteException {
        boolean state = false;
        try {
            state = this.mService.sendIOTConnectProbeReq(addVendorIE, channels, hiddenSSIDList);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        Log.d(TAG, "sendIOTConnectProbeReq called: " + addVendorIE + " " + state);
        return state;
    }

    public List<ScanResult> getIOTConnectScanResults() throws RemoteException {
        List<ScanResult> results = null;
        try {
            results = this.mService.getIOTConnectScanResults();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        Log.d(TAG, "getIOTConnectScanResults called: ");
        for (ScanResult result : results) {
            Log.d(TAG, "getIOTConnectScanResults called:  freq:" + result.frequency);
        }
        return results;
    }

    @Deprecated
    public boolean suspendFWKPeriodicScan(int disableInterval) throws RemoteException {
        boolean ret = false;
        try {
            ret = this.mService.suspendFWKPeriodicScan(disableInterval);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        Log.d(TAG, "suspendFWKPeriodicScan called: " + disableInterval + " " + ret);
        return ret;
    }

    @Deprecated
    public void resumeFWKPeriodicScan() throws RemoteException {
        try {
            this.mService.resumeFWKPeriodicScan();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        Log.d(TAG, "resumeFWKPeriodicScan called");
    }

    public boolean iotConnectScanBusy() throws RemoteException {
        boolean ret = true;
        try {
            ret = this.mService.iotConnectScanBusy();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        Log.d(TAG, "iotConnectScanBusy called: " + ret);
        return ret;
    }

    public boolean checkFWKSupplicantScanBusy() throws RemoteException {
        boolean ret = true;
        try {
            ret = this.mService.checkFWKSupplicantScanBusy();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        Log.d(TAG, "checkFWKSupplicantScanBusy called: " + ret);
        return ret;
    }

    public List<OplusScanStatistics> getAllScanStatisticsList(int timeAgo) throws RemoteException {
        try {
            List<OplusScanStatistics> statList = this.mService.getAllScanStatisticsList(timeAgo);
            return statList;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public int handlePhoneCloneStatus(int cloneStatus) {
        try {
            int ret = this.mService.handlePhoneCloneStatus(cloneStatus);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public List<OplusScanStatistics> getScanStatisticsList(String packageName, int timeAgo) throws RemoteException {
        try {
            List<OplusScanStatistics> statList = this.mService.getScanStatisticsList(packageName, timeAgo);
            return statList;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ActionListenerProxy extends IConnectListener.Stub {
        private final String mActionTag;
        private final ActionListener mCallback;
        private final Handler mHandler;

        ActionListenerProxy(String actionTag, Looper looper, ActionListener callback) {
            this.mActionTag = actionTag;
            this.mHandler = new Handler(looper);
            this.mCallback = callback;
        }

        @Override // android.net.wifi.IConnectListener
        public void onSuccess() {
            if (OplusWifiManager.this.mVerboseLoggingEnabled) {
                Log.v(OplusWifiManager.TAG, "ActionListenerProxy:" + this.mActionTag + ": onSuccess");
            }
            this.mHandler.post(new Runnable() { // from class: android.net.wifi.OplusWifiManager$ActionListenerProxy$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    OplusWifiManager.ActionListenerProxy.this.lambda$onSuccess$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0() {
            this.mCallback.onSuccess();
        }

        @Override // android.net.wifi.IConnectListener
        public void onFailure(final int reason) {
            if (OplusWifiManager.this.mVerboseLoggingEnabled) {
                Log.v(OplusWifiManager.TAG, "ActionListenerProxy:" + this.mActionTag + ": onFailure=" + reason);
            }
            this.mHandler.post(new Runnable() { // from class: android.net.wifi.OplusWifiManager$ActionListenerProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusWifiManager.ActionListenerProxy.this.lambda$onFailure$1(reason);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$1(int reason) {
            this.mCallback.onFailure(reason);
        }
    }

    public void connect(String pkgName, WifiConfiguration config, ActionListener listener) throws RemoteException {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }
        ExtendsWifiConfig extConfig = new ExtendsWifiConfig(config);
        connectInternal(pkgName, extConfig, -1, listener);
    }

    public void connect(String pkgName, Integer networkId, ActionListener listener) throws RemoteException {
        connectInternal(pkgName, null, networkId.intValue(), listener);
    }

    private void connectInternal(String pkgName, ExtendsWifiConfig config, int networkId, ActionListener listener) throws RemoteException {
        int callbackIdentifier;
        IBinder binder;
        ActionListenerProxy listenerProxy;
        IBinder asBinder;
        int configstate = 0;
        if (listener != null) {
            ActionListenerProxy listenerProxy2 = new ActionListenerProxy("connect", Looper.getMainLooper(), listener);
            IBinder binder2 = new Binder();
            int callbackIdentifier2 = listener.hashCode();
            callbackIdentifier = callbackIdentifier2;
            binder = binder2;
            listenerProxy = listenerProxy2;
        } else {
            callbackIdentifier = 0;
            binder = null;
            listenerProxy = null;
        }
        if (config != null) {
            configstate = 1;
        }
        if (listenerProxy == null) {
            asBinder = null;
        } else {
            try {
                asBinder = listenerProxy.asBinder();
            } catch (RemoteException e) {
                e = e;
                e.rethrowFromSystemServer();
            }
        }
        IBinder listenerProxyBinder = asBinder;
        try {
            this.mService.connect(configstate, config, pkgName, networkId, binder, listenerProxyBinder, callbackIdentifier);
        } catch (RemoteException e2) {
            e = e2;
            e.rethrowFromSystemServer();
        }
    }

    public void setLogOn(long size, String param) throws RemoteException {
        try {
            this.mService.setLogOn(size, param);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setLogOff() throws RemoteException {
        try {
            this.mService.setLogOff();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setLogDump() throws RemoteException {
        try {
            this.mService.setLogDump();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public int tryToRestoreWifiSetting() throws RemoteException {
        try {
            int result = this.mService.tryToRestoreWifiSetting();
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public boolean isSupportSnifferCaptureWithUdp() throws RemoteException {
        try {
            boolean ret = this.mService.isSupportSnifferCaptureWithUdp();
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean isInSnifferMode() throws RemoteException {
        try {
            boolean ret = this.mService.isInSnifferMode();
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public int keepSnifferMode(int timeout) throws RemoteException {
        try {
            int ret = this.mService.keepSnifferMode(timeout);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int setToWifiSnifferMode(boolean enable) throws RemoteException {
        try {
            int ret = this.mService.setToWifiSnifferMode(enable, true);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int setToWifiSnifferMode(boolean enable, boolean restoreWifi) throws RemoteException {
        try {
            int ret = this.mService.setToWifiSnifferMode(enable, restoreWifi);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int setSnifferParamWithUdp(int freq, int bandWidth, boolean hT40Above, boolean discardDataBody, boolean discardMgmtBody, int filter) throws RemoteException {
        try {
            int ret = this.mService.setSnifferParamWithUdp(freq, bandWidth, hT40Above, discardDataBody, discardMgmtBody, filter);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int startSnifferCaptureWithUdp(int port) throws RemoteException {
        try {
            int ret = this.mService.startSnifferCaptureWithUdp(port);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int stopSnifferCaptureWithUdp() throws RemoteException {
        try {
            int ret = this.mService.stopSnifferCaptureWithUdp();
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public boolean setWifiEnabledOnlyForTest(boolean enable) throws RemoteException {
        try {
            boolean ret = this.mService.setWifiEnabledOnlyForTest(enable);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean setWifiAssistantPolicies(int mode) throws RemoteException {
        try {
            boolean ret = this.mService.setWifiAssistantPolicies(mode);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public int getCurNetworkState(String pkgName) throws RemoteException {
        try {
            int states = this.mService.getCurNetworkState(pkgName);
            return states;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public Network getCurrentNetwork() throws RemoteException {
        try {
            Network network = this.mService.getCurrentNetwork();
            return network;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public boolean isSubwifiManuconnect() throws RemoteException {
        try {
            boolean ret = this.mService.isSubwifiManuconnect();
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return true;
        }
    }

    public List<ScanResult> getCandiateNetwork(String pkgName) throws RemoteException {
        try {
            List<ScanResult> results = this.mService.getCandiateNetwork(pkgName);
            return results;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public boolean setWifiRestrictionList(String packageName, List<String> list, String type) throws RemoteException {
        try {
            boolean state = this.mService.setWifiRestrictionList(packageName, list, type);
            return state;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<String> getWifiRestrictionList(String packageName, String type) throws RemoteException {
        try {
            List<String> dataList = this.mService.getWifiRestrictionList(packageName, type);
            return dataList;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean removeFromRestrictionList(String packageName, List<String> list, String type) throws RemoteException {
        try {
            boolean state = this.mService.removeFromRestrictionList(packageName, list, type);
            return state;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setWifiAutoConnectionDisabled(boolean disable) throws RemoteException {
        try {
            boolean state = this.mService.setWifiAutoConnectionDisabled(disable);
            return state;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWifiAutoConnectionDisabled() throws RemoteException {
        try {
            boolean state = this.mService.isWifiAutoConnectionDisabled();
            return state;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSupportSpecialBandSoftAp(int band) throws RemoteException {
        if (band != 1) {
            return false;
        }
        try {
            boolean ret = this.mService.isSupport5g160MSoftAp();
            return ret;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSupportSpecialBandSta(int band) throws RemoteException {
        if (band != 1) {
            return false;
        }
        try {
            boolean ret = this.mService.isSupport5g160MStaForPhoneClone();
            return ret;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int enable5g160MSoftAp(boolean enabled, int channel) throws RemoteException {
        try {
            int ret = this.mService.enable5g160MSoftAp(enabled, channel);
            return ret;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean startRxSensTest(WifiConfiguration config, String ip) throws RemoteException {
        ExtendsWifiConfig extendConfig = new ExtendsWifiConfig(config);
        return startRxSensTest(extendConfig, ip);
    }

    public boolean startRxSensTest(ExtendsWifiConfig config, String ip) throws RemoteException {
        try {
            boolean state = this.mService.startRxSensTest(config, ip);
            return state;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void stopRxSensTest() throws RemoteException {
        try {
            this.mService.stopRxSensTest();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPortalLoginType() throws RemoteException {
        return 0;
    }

    public void setNetworkCaptiveState(boolean state) throws RemoteException {
    }

    public String getNetStateInfo(int type) throws RemoteException {
        try {
            String currentWifiNetStateInfo = this.mService.getNetStateInfo(type);
            return currentWifiNetStateInfo;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addDnsRecord(int errCode, int latencyMs, int netId, String hostname, String[] ipAddresses) throws RemoteException {
        try {
            this.mService.addDnsRecord(errCode, latencyMs, netId, hostname, ipAddresses);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void addTcpSyncRecord(int netId, int uid, int errCode, int latency, String ipAddr) throws RemoteException {
        try {
            this.mService.addTcpSyncRecord(netId, uid, errCode, latency, ipAddr);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void addTcpRttRecord(long[] data) throws RemoteException {
        try {
            this.mService.addTcpRttRecord(data);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean attRequestAuthenticationBeforeTurnOnHotspot(int type, boolean state, int carrierId) throws RemoteException {
        try {
            boolean ret = this.mService.attRequestAuthenticationBeforeTurnOnHotspot(type, state, carrierId);
            return ret;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setWifiEventCallback(Executor executor, OplusWifiEventCallback callback) throws RemoteException {
        if (executor == null || callback == null) {
            return;
        }
        IOplusWifiEventCallback.Stub binderCallback = new OplusWifiEventCallbackProxy(executor, callback);
        try {
            synchronized (sOplusWifiEventCbs) {
                int listenerIdentifier = System.identityHashCode(callback);
                if (sOplusWifiEventCbs.contains(listenerIdentifier)) {
                    Log.d(TAG, "wifi event cantains, ignore register");
                } else {
                    sOplusWifiEventCbs.put(listenerIdentifier, binderCallback);
                    this.mService.setWifiEventCallback(binderCallback);
                }
            }
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void removeWifiEventCallback(OplusWifiEventCallback callback) throws RemoteException {
        if (callback == null) {
            return;
        }
        try {
            synchronized (sOplusWifiEventCbs) {
                int listenerIdentifier = System.identityHashCode(callback);
                if (!sOplusWifiEventCbs.contains(listenerIdentifier)) {
                    Log.d(TAG, "unknown external callback " + listenerIdentifier);
                    return;
                }
                IOplusWifiEventCallback.Stub binderCallback = sOplusWifiEventCbs.get(listenerIdentifier);
                this.mService.removeWifiEventCallback(binderCallback);
                sOplusWifiEventCbs.remove(listenerIdentifier);
            }
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void clearWifiEventCallback() throws RemoteException {
        try {
            this.mService.clearWifiEventCallback();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setScanThrottleInfoList(List<ScanThrottleInfo> exceptionList, boolean foreground) throws RemoteException {
        if (exceptionList == null) {
            return;
        }
        try {
            this.mService.setScanThrottleInfoList(exceptionList, foreground);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void clearScanThrottleInfoList() throws RemoteException {
        try {
            this.mService.clearScanThrottleInfoList();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public int getDualStaEnableState(String pkgName) throws RemoteException {
        try {
            int ret = this.mService.getDualStaEnableState(pkgName);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int getULLState(String pkgName) throws RemoteException {
        try {
            int ret = this.mService.getULLState(pkgName);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int getORouterBoostSate(String pkgName) throws RemoteException {
        try {
            int ret = this.mService.getORouterBoostSate(pkgName);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public void enableQoEMonitor(String pkgName) throws RemoteException {
        try {
            this.mService.enableQoEMonitor(pkgName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void disableQoEMonitor(String pkgName) throws RemoteException {
        try {
            this.mService.disableQoEMonitor(pkgName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public OlkL2Param getL2Param(String pkgName, int ifname) throws RemoteException {
        try {
            OlkL2Param l2param = new OlkL2Param(this.mService.getL2Param(pkgName, ifname));
            return l2param;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public Map<String, Integer> getL2ParamMap(String pkgName, int ifname) throws RemoteException {
        try {
            return this.mService.getL2Param(pkgName, ifname);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void sendABRchange(String pkgName, int netId, int abrGear) throws RemoteException {
        try {
            this.mService.sendABRchange(pkgName, netId, abrGear);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void sendStartScoreChange(String pkgName, int netId, int score) throws RemoteException {
        try {
            this.mService.sendStartScoreChange(pkgName, netId, score);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void doRecoveryFromSettingsForSsv() throws RemoteException {
        try {
            this.mService.doRecoveryFromSettingsForSsv();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void notifyMediaEventToWifi(String msg, String pkgName) throws RemoteException {
        try {
            this.mService.notifyMediaEventToWifi(msg, pkgName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public Bundle getConfiguredNetworkFromPairedDevice(String ssid, int keymgmt) throws RemoteException {
        try {
            Bundle bundle = this.mService.getConfiguredNetworkFromPairedDevice(ssid, keymgmt);
            return bundle;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public List<Bundle> getConfiguredNetworksFromPairedDevice() throws RemoteException {
        List<Bundle> bundleList = new ArrayList<>();
        try {
            List<Bundle> bundleList2 = this.mService.getConfiguredNetworksFromPairedDevice();
            return bundleList2;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return bundleList;
        }
    }

    public boolean isWcnLoadSuccess() {
        try {
            boolean result = this.mService.isWcnLoadSuccess();
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public Map<String, String> getWcnBootStatus() {
        try {
            return this.mService.getWcnBootStatus();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void manualReconnect() throws RemoteException {
        try {
            this.mService.manualReconnect();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public int requestDualSta(String pkgName) throws RemoteException {
        try {
            int result = this.mService.requestDualSta(pkgName);
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public int releaseDualSta(String pkgName) throws RemoteException {
        try {
            int result = this.mService.releaseDualSta(pkgName);
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public void reportBssScore(String pkgName, Map<String, String> map) throws RemoteException {
        try {
            this.mService.reportBssScore(pkgName, map);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void triggerNetworkSelection(String pkgName, int reason) throws RemoteException {
        try {
            this.mService.triggerNetworkSelection(pkgName, reason);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void registerNetworkSelectionOpt(String pkgName, Executor executor, NeworkSelectionOptCb callback) {
        if (executor == null || callback == null) {
            return;
        }
        if (pkgName == null) {
            Log.d(TAG, "pkg name is null");
            return;
        }
        try {
            synchronized (sOplusWifiOpenApiCbs) {
                if (sOplusWifiOpenApiCbs.containsKey(pkgName)) {
                    Log.d(TAG, pkgName + " update its cb");
                    sOplusWifiOpenApiCbs.remove(pkgName);
                }
                INeworkSelectionOptCb.Stub binderCallback = new NeworkSelectionOptCbProxy(executor, callback);
                this.mService.registerNetworkSelectionOpt(pkgName, binderCallback);
                sOplusWifiOpenApiCbs.put(pkgName, binderCallback);
            }
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void unregisterNetworkSelectionOpt(String pkgName, NeworkSelectionOptCb callback) {
        if (callback == null) {
            Log.d(TAG, "callback is null");
            return;
        }
        if (pkgName == null) {
            Log.d(TAG, "pkg name is null");
            return;
        }
        try {
            synchronized (sOplusWifiOpenApiCbs) {
                this.mService.unregisterNetworkSelectionOpt(pkgName, sOplusWifiOpenApiCbs.get(pkgName));
                if (sOplusWifiOpenApiCbs.containsKey(pkgName)) {
                    Log.d(TAG, pkgName + " remove its Network Selection Cb" + sOplusWifiOpenApiCbs.get(pkgName));
                    sOplusWifiOpenApiCbs.remove(pkgName);
                }
            }
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean isOpenApiSupported() {
        try {
            boolean result = this.mService.isOpenApiSupported();
            return result;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public void settingsDispatchOplusWifi(int eventType, Bundle bundle) throws RemoteException {
        try {
            this.mService.settingsDispatchOplusWifi(eventType, bundle);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public int getNetworkAutoChangeDefaultValue() {
        try {
            int value = this.mService.getNetworkAutoChangeDefaultValue();
            return value;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }

    public boolean getWJDLinkStats(int iface, String[] skInfoParaIn, Map<String, String> skInfoOut, boolean[] jitterParaIn, Map<String, String> jitterInfoOut, Map<String, String> jitterDetailOut) throws RemoteException {
        try {
            boolean ret = this.mService.getWJDLinkStats(iface, skInfoParaIn, skInfoOut, jitterParaIn, jitterInfoOut, jitterDetailOut);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public void factoryReset() throws RemoteException {
        try {
            this.mService.factoryReset();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean isP2PBeingUsedByForeground() {
        try {
            return this.mService.isP2PBeingUsedByForeground();
        } catch (RemoteException e) {
            Log.e(TAG, "OplusWifiService is dead.");
            return false;
        }
    }

    public boolean enableP2p6g320M(int freq) {
        boolean ret = false;
        try {
            ret = this.mService.enableP2p6g320M(freq);
            Log.d(TAG, "enableP2p6g320M ret = " + ret);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return ret;
        }
    }

    public boolean disableP2p6g320M() {
        boolean ret = false;
        try {
            ret = this.mService.disableP2p6g320M();
            Log.d(TAG, "disableP2p6g320M ret = " + ret);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return ret;
        }
    }

    public boolean isSupportP2p6G320M() {
        boolean ret = false;
        try {
            ret = this.mService.isSupportP2p6G320M();
            Log.d(TAG, "isSupportP2p6G320M ret = " + ret);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return ret;
        }
    }

    public boolean setWifiLowLatencyEnabled(boolean enable) {
        try {
            this.mService.setWifiLowLatencyEnabled(enable);
            return true;
        } catch (RemoteException e) {
            Log.d(TAG, "setWifiLowLatencyEnabled error " + e.getMessage());
            return true;
        }
    }
}
