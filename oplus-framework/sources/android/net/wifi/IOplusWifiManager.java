package android.net.wifi;

import android.net.Network;
import android.net.wifi.INeworkSelectionOptCb;
import android.net.wifi.IOplusWifiEventCallback;
import android.net.wifi.IOplusWifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.OlkConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import oplus.net.wifi.HotspotClient;

/* loaded from: classes.dex */
public interface IOplusWifiManager extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IOplusWifiManager";

    void addDnsRecord(int i, int i2, int i3, String str, String[] strArr) throws RemoteException;

    void addTcpRttRecord(long[] jArr) throws RemoteException;

    void addTcpSyncRecord(int i, int i2, int i3, int i4, String str) throws RemoteException;

    boolean agingTestForWifi() throws RemoteException;

    boolean attRequestAuthenticationBeforeTurnOnHotspot(int i, boolean z, int i2) throws RemoteException;

    boolean changeConnectionMode(int i) throws RemoteException;

    boolean checkFWKSupplicantScanBusy() throws RemoteException;

    boolean checkFWKSupportPasspoint() throws RemoteException;

    boolean checkInternalPasspointPresetProvider(String str) throws RemoteException;

    boolean checkPasspointCAExist(String str) throws RemoteException;

    boolean checkPasspointXMLCAExpired(String str) throws RemoteException;

    boolean checkWiFiDriverStatus() throws RemoteException;

    void clearScanThrottleInfoList() throws RemoteException;

    void clearWifiEventCallback() throws RemoteException;

    void clearWifiOCloudData(boolean z) throws RemoteException;

    void connect(int i, ExtendsWifiConfig extendsWifiConfig, String str, int i2, IBinder iBinder, IBinder iBinder2, int i3) throws RemoteException;

    void dealWithCloudBackupResult(List<String> list, String str) throws RemoteException;

    void dealWithCloudRecoveryData(List<String> list, String str) throws RemoteException;

    void disableDualSta() throws RemoteException;

    boolean disableP2p6g320M() throws RemoteException;

    void disableQoEMonitor(String str) throws RemoteException;

    void doRecoveryFromSettingsForSsv() throws RemoteException;

    int enable5g160MSoftAp(boolean z, int i) throws RemoteException;

    boolean enableP2p6g320M(int i) throws RemoteException;

    void enableQoEMonitor(String str) throws RemoteException;

    boolean executeDriverCommand(String str) throws RemoteException;

    String executeDriverCommandWithResult(String str) throws RemoteException;

    void factoryReset() throws RemoteException;

    String[] getAllDualStaApps() throws RemoteException;

    List<OplusScanStatistics> getAllScanStatisticsList(int i) throws RemoteException;

    String[] getAllSlaAcceleratedApps() throws RemoteException;

    String[] getAllSlaAppsAndStates() throws RemoteException;

    List<HotspotClient> getAllowedHotspotClients() throws RemoteException;

    int[] getAvoidChannels() throws RemoteException;

    List<HotspotClient> getBlockedHotspotClients() throws RemoteException;

    List<ScanResult> getCandiateNetwork(String str) throws RemoteException;

    Bundle getConfiguredNetworkFromPairedDevice(String str, int i) throws RemoteException;

    List<Bundle> getConfiguredNetworksFromPairedDevice() throws RemoteException;

    List<HotspotClient> getConnectedHotspotClients() throws RemoteException;

    int getCurNetworkState(String str) throws RemoteException;

    Network getCurrentNetwork() throws RemoteException;

    int getDBSCapacity() throws RemoteException;

    String getDeviceName() throws RemoteException;

    int getDualStaEnableState(String str) throws RemoteException;

    int getFTMState() throws RemoteException;

    String getFwStatus() throws RemoteException;

    Map<String, String> getGameMainStreamServIp() throws RemoteException;

    List<ScanResult> getIOTConnectScanResults() throws RemoteException;

    Map getL2Param(String str, int i) throws RemoteException;

    String getNetStateInfo(int i) throws RemoteException;

    int getNetworkAutoChangeDefaultValue() throws RemoteException;

    int getORouterBoostSate(String str) throws RemoteException;

    ExtendsWifiInfo getOplusSta2ConnectionInfo() throws RemoteException;

    String getOplusSta2CurConfigKey() throws RemoteException;

    long getOplusSupportedFeatures() throws RemoteException;

    int getPHYCapacity(int i) throws RemoteException;

    boolean getPasspointCertifiedState(String str) throws RemoteException;

    String getPasspointUserName(String str) throws RemoteException;

    List<OplusScanStatistics> getScanStatisticsList(String str, int i) throws RemoteException;

    Network getSecondaryWifiNetwork() throws RemoteException;

    boolean getSlaAppState(String str) throws RemoteException;

    long getSlaTotalTraffic() throws RemoteException;

    int getSlaWorkMode() throws RemoteException;

    int getSnifferState() throws RemoteException;

    int[] getSupportedChannels(int i) throws RemoteException;

    String getTargetMacAddress(String str) throws RemoteException;

    int getULLState(String str) throws RemoteException;

    boolean getWJDLinkStats(int i, String[] strArr, Map<String, String> map, boolean[] zArr, Map<String, String> map2, Map<String, String> map3) throws RemoteException;

    Map getWcnBootStatus() throws RemoteException;

    List<String> getWifiOCloudData(boolean z) throws RemoteException;

    List<String> getWifiRestrictionList(String str, String str2) throws RemoteException;

    int handlePhoneCloneStatus(int i) throws RemoteException;

    boolean hasOCloudDirtyData() throws RemoteException;

    boolean iotConnectScanBusy() throws RemoteException;

    boolean isInSnifferMode() throws RemoteException;

    boolean isOpenApiSupported() throws RemoteException;

    boolean isP2PBeingUsedByForeground() throws RemoteException;

    boolean isP2p5GSupported() throws RemoteException;

    boolean isP2p6GHzBandSupported() throws RemoteException;

    boolean isPrimaryWifi(Network network) throws RemoteException;

    boolean isSoftap5GSupported() throws RemoteException;

    boolean isSoftap6GHzBandSupported() throws RemoteException;

    boolean isSubwifiManuconnect() throws RemoteException;

    boolean isSupport5g160MSoftAp() throws RemoteException;

    boolean isSupport5g160MStaForPhoneClone() throws RemoteException;

    boolean isSupportP2p6G320M() throws RemoteException;

    boolean isSupportSnifferCaptureWithUdp() throws RemoteException;

    boolean isWcnLoadSuccess() throws RemoteException;

    boolean isWifiAutoConnectionDisabled() throws RemoteException;

    int keepSnifferMode(int i) throws RemoteException;

    void manualReconnect() throws RemoteException;

    void notifyGameHighTemperature(int i, String str) throws RemoteException;

    void notifyGameInfoJsonStr(String str) throws RemoteException;

    void notifyGameLatency(String str, String str2) throws RemoteException;

    void notifyGameModeState(boolean z, String str) throws RemoteException;

    void notifyMediaEventToWifi(String str, String str2) throws RemoteException;

    void notifyMeetingWorkingState(boolean z, String str) throws RemoteException;

    List<ScanResult> passpointANQPScanResults(List<ScanResult> list) throws RemoteException;

    void registerNetworkSelectionOpt(String str, INeworkSelectionOptCb iNeworkSelectionOptCb) throws RemoteException;

    int releaseDualSta(String str) throws RemoteException;

    void releaseDualStaNetwork(String str) throws RemoteException;

    boolean removeFromRestrictionList(String str, List<String> list, String str2) throws RemoteException;

    void removeHeIeFromProbeRequest(boolean z) throws RemoteException;

    void removeNetworkByGlobalId(String str, String str2, boolean z) throws RemoteException;

    void removeWifiEventCallback(IOplusWifiEventCallback iOplusWifiEventCallback) throws RemoteException;

    void reportBssScore(String str, Map<String, String> map) throws RemoteException;

    int requestDualSta(String str) throws RemoteException;

    void requestDualStaNetwork(String str) throws RemoteException;

    boolean resetConnectionMode() throws RemoteException;

    void resumeFWKPeriodicScan() throws RemoteException;

    void saveExternalPeerAddress(String str) throws RemoteException;

    void sendABRchange(String str, int i, int i2) throws RemoteException;

    int sendFTMCommand(String str) throws RemoteException;

    boolean sendIOTConnectProbeReq(String str, int[] iArr, String str2) throws RemoteException;

    void sendStartScoreChange(String str, int i, int i2) throws RemoteException;

    void setAllowedHotspotClients(List<HotspotClient> list) throws RemoteException;

    void setBlockedHotspotClients(List<HotspotClient> list) throws RemoteException;

    void setCastContent(int i, int i2, int i3, String str) throws RemoteException;

    void setCastStatus(int i, int i2, int i3, String str) throws RemoteException;

    void setDirtyFlag(String str, boolean z) throws RemoteException;

    void setLogDump() throws RemoteException;

    void setLogOff() throws RemoteException;

    void setLogOn(long j, String str) throws RemoteException;

    boolean setNfcTriggered(boolean z) throws RemoteException;

    void setOshareEnabled(boolean z, int i, boolean z2, boolean z3) throws RemoteException;

    void setP2pPowerSave(boolean z) throws RemoteException;

    boolean setPasspointCertifiedState(String str) throws RemoteException;

    boolean setPcAutonomousGo(boolean z, int i, String str) throws RemoteException;

    void setScanThrottleInfoList(List<ScanThrottleInfo> list, boolean z) throws RemoteException;

    boolean setSlaAppState(String str, boolean z) throws RemoteException;

    int setSnifferParamWithUdp(int i, int i2, boolean z, boolean z2, boolean z3, int i3) throws RemoteException;

    int setToWifiSnifferMode(boolean z, boolean z2) throws RemoteException;

    boolean setWifiAssistantPolicies(int i) throws RemoteException;

    boolean setWifiAutoConnectionDisabled(boolean z) throws RemoteException;

    boolean setWifiEnabledOnlyForTest(boolean z) throws RemoteException;

    void setWifiEventCallback(IOplusWifiEventCallback iOplusWifiEventCallback) throws RemoteException;

    boolean setWifiLowLatencyEnabled(boolean z) throws RemoteException;

    boolean setWifiRestrictionList(String str, List<String> list, String str2) throws RemoteException;

    boolean setWifiSmartAntennaAction(int i) throws RemoteException;

    void settingsDispatchOplusWifi(int i, Bundle bundle) throws RemoteException;

    boolean setupFTMdaemon() throws RemoteException;

    boolean shouldLaunchBrowser(Network network, String str) throws RemoteException;

    int startFTMMode() throws RemoteException;

    boolean startRxSensTest(ExtendsWifiConfig extendsWifiConfig, String str) throws RemoteException;

    int startSnifferCaptureWithUdp(int i) throws RemoteException;

    int startSnifferMode(int i, int i2, int i3, int i4) throws RemoteException;

    int stopFTMMode() throws RemoteException;

    void stopRxSensTest() throws RemoteException;

    int stopSnifferCaptureWithUdp() throws RemoteException;

    int stopSnifferMode() throws RemoteException;

    boolean suspendFWKPeriodicScan(int i) throws RemoteException;

    void triggerNetworkSelection(String str, int i) throws RemoteException;

    int tryToRestoreWifiSetting() throws RemoteException;

    void unregisterNetworkSelectionOpt(String str, INeworkSelectionOptCb iNeworkSelectionOptCb) throws RemoteException;

    void updateGlobalId(int i, String str) throws RemoteException;

    boolean wifiLoadDriver(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusWifiManager {
        @Override // android.net.wifi.IOplusWifiManager
        public long getOplusSupportedFeatures() throws RemoteException {
            return 0L;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setSlaAppState(String pkgName, boolean enabled) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean getSlaAppState(String pkgName) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void disableDualSta() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String[] getAllSlaAppsAndStates() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String[] getAllSlaAcceleratedApps() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String[] getAllDualStaApps() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getSlaWorkMode() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public long getSlaTotalTraffic() throws RemoteException {
            return 0L;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public ExtendsWifiInfo getOplusSta2ConnectionInfo() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String getOplusSta2CurConfigKey() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String getTargetMacAddress(String targetIpAddress) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setWifiRestrictionList(String packageName, List<String> list, String type) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<String> getWifiRestrictionList(String packageName, String type) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean removeFromRestrictionList(String packageName, List<String> list, String type) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setWifiAutoConnectionDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isWifiAutoConnectionDisabled() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void requestDualStaNetwork(String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void releaseDualStaNetwork(String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isPrimaryWifi(Network network) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public Network getSecondaryWifiNetwork() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean shouldLaunchBrowser(Network network, String redirectUrl) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void clearWifiOCloudData(boolean hardDelete) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<String> getWifiOCloudData(boolean dirtyOnly) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void updateGlobalId(int itemId, String globalId) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void removeNetworkByGlobalId(String configKey, String globalId, boolean hardDelete) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setDirtyFlag(String globalId, boolean value) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean hasOCloudDirtyData() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void dealWithCloudBackupResult(List<String> cloudData, String type) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void dealWithCloudRecoveryData(List<String> cloudData, String type) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean checkPasspointCAExist(String fqdn) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean checkPasspointXMLCAExpired(String fqdn) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<ScanResult> passpointANQPScanResults(List<ScanResult> scanResults) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean checkInternalPasspointPresetProvider(String fqdn) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean checkFWKSupportPasspoint() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setPasspointCertifiedState(String signature) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String getPasspointUserName(String fqdn) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean getPasspointCertifiedState(String fqdn) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isP2p5GSupported() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isSoftap5GSupported() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isP2p6GHzBandSupported() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isSoftap6GHzBandSupported() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setBlockedHotspotClients(List<HotspotClient> clientList) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setAllowedHotspotClients(List<HotspotClient> clientList) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<HotspotClient> getBlockedHotspotClients() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<HotspotClient> getAllowedHotspotClients() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<HotspotClient> getConnectedHotspotClients() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void notifyGameModeState(boolean state, String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void notifyGameLatency(String latencyInfo, String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void notifyGameHighTemperature(int temperature, String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void notifyGameInfoJsonStr(String gameInfo) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void notifyMeetingWorkingState(boolean state, String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public Map<String, String> getGameMainStreamServIp() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void addDnsRecord(int errCode, int latencyMs, int netId, String hostname, String[] ipAddresses) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void addTcpSyncRecord(int netId, int uid, int errCode, int latency, String ipAddr) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void addTcpRttRecord(long[] data) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int startSnifferMode(int channel, int bandwidth, int maxPacketNum, int maxPacketSize) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int stopSnifferMode() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getSnifferState() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int startFTMMode() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int stopFTMMode() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int sendFTMCommand(String ftmCmd) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getFTMState() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean resetConnectionMode() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean changeConnectionMode(int mode) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setupFTMdaemon() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean executeDriverCommand(String command) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String executeDriverCommandWithResult(String command) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean wifiLoadDriver(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean checkWiFiDriverStatus() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String getFwStatus() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean agingTestForWifi() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setWifiSmartAntennaAction(int action) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String getDeviceName() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void removeHeIeFromProbeRequest(boolean remove) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setP2pPowerSave(boolean enable) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getDBSCapacity() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getPHYCapacity(int band) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int[] getSupportedChannels(int band) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int[] getAvoidChannels() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean sendIOTConnectProbeReq(String addVendorIE, int[] channels, String hiddenSSIDList) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<ScanResult> getIOTConnectScanResults() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean suspendFWKPeriodicScan(int disableInterval) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void resumeFWKPeriodicScan() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean iotConnectScanBusy() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean checkFWKSupplicantScanBusy() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isSupport5g160MSoftAp() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isSupport5g160MStaForPhoneClone() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int enable5g160MSoftAp(boolean enabled, int channel) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<OplusScanStatistics> getScanStatisticsList(String packageName, int timeAgo) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<OplusScanStatistics> getAllScanStatisticsList(int timeAgo) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int handlePhoneCloneStatus(int cloneStatus) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void connect(int configState, ExtendsWifiConfig config, String pkgeName, int networkId, IBinder binder, IBinder callback, int callbackIdentifier) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<ScanResult> getCandiateNetwork(String pkgName) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getCurNetworkState(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public Network getCurrentNetwork() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isSubwifiManuconnect() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setLogOn(long size, String param) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setLogOff() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setLogDump() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int tryToRestoreWifiSetting() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isSupportSnifferCaptureWithUdp() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isInSnifferMode() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int keepSnifferMode(int timeout) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int setToWifiSnifferMode(boolean enable, boolean restoreWifi) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int setSnifferParamWithUdp(int freq, int bandWidth, boolean hT40Above, boolean discardDataBody, boolean discardMgmtBody, int filter) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int startSnifferCaptureWithUdp(int port) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int stopSnifferCaptureWithUdp() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setWifiEnabledOnlyForTest(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setWifiAssistantPolicies(int mode) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean startRxSensTest(ExtendsWifiConfig config, String ip) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void stopRxSensTest() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public String getNetStateInfo(int type) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean attRequestAuthenticationBeforeTurnOnHotspot(int type, boolean state, int carrierId) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getDualStaEnableState(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getULLState(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getORouterBoostSate(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void enableQoEMonitor(String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void disableQoEMonitor(String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public Map getL2Param(String pkgName, int netId) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void sendABRchange(String pkgName, int netId, int abrGear) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void sendStartScoreChange(String pkgName, int netId, int score) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setWifiEventCallback(IOplusWifiEventCallback callback) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void removeWifiEventCallback(IOplusWifiEventCallback callback) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void clearWifiEventCallback() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setScanThrottleInfoList(List<ScanThrottleInfo> exceptionList, boolean foreground) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void clearScanThrottleInfoList() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setNfcTriggered(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void saveExternalPeerAddress(String address) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setOshareEnabled(boolean enable, int freq, boolean isGo, boolean isStaticIp) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setPcAutonomousGo(boolean enable, int freq, String reverse) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setCastStatus(int type, int state, int role, String reverse) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void setCastContent(int type, int state, int role, String reverse) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void doRecoveryFromSettingsForSsv() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void notifyMediaEventToWifi(String msg, String pkgName) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public Bundle getConfiguredNetworkFromPairedDevice(String ssid, int keymgmt) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public List<Bundle> getConfiguredNetworksFromPairedDevice() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isWcnLoadSuccess() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public Map getWcnBootStatus() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int requestDualSta(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int releaseDualSta(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void registerNetworkSelectionOpt(String pkgName, INeworkSelectionOptCb nwSelectionOptCb) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void unregisterNetworkSelectionOpt(String pkgName, INeworkSelectionOptCb nwSelectionOptCb) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void reportBssScore(String pkgName, Map<String, String> map) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void triggerNetworkSelection(String pkgName, int reason) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isOpenApiSupported() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void manualReconnect() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void settingsDispatchOplusWifi(int eventType, Bundle bundle) throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean getWJDLinkStats(int iface, String[] skInfoParaIn, Map<String, String> skInfoOut, boolean[] jitterParaIn, Map<String, String> jitterInfoOut, Map<String, String> jitterDetailOut) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public void factoryReset() throws RemoteException {
        }

        @Override // android.net.wifi.IOplusWifiManager
        public int getNetworkAutoChangeDefaultValue() throws RemoteException {
            return 0;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isP2PBeingUsedByForeground() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean enableP2p6g320M(int freq) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean disableP2p6g320M() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean isSupportP2p6G320M() throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.IOplusWifiManager
        public boolean setWifiLowLatencyEnabled(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusWifiManager {
        static final int TRANSACTION_addDnsRecord = 54;
        static final int TRANSACTION_addTcpRttRecord = 56;
        static final int TRANSACTION_addTcpSyncRecord = 55;
        static final int TRANSACTION_agingTestForWifi = 72;
        static final int TRANSACTION_attRequestAuthenticationBeforeTurnOnHotspot = 114;
        static final int TRANSACTION_changeConnectionMode = 65;
        static final int TRANSACTION_checkFWKSupplicantScanBusy = 86;
        static final int TRANSACTION_checkFWKSupportPasspoint = 35;
        static final int TRANSACTION_checkInternalPasspointPresetProvider = 34;
        static final int TRANSACTION_checkPasspointCAExist = 31;
        static final int TRANSACTION_checkPasspointXMLCAExpired = 32;
        static final int TRANSACTION_checkWiFiDriverStatus = 70;
        static final int TRANSACTION_clearScanThrottleInfoList = 127;
        static final int TRANSACTION_clearWifiEventCallback = 125;
        static final int TRANSACTION_clearWifiOCloudData = 23;
        static final int TRANSACTION_connect = 93;
        static final int TRANSACTION_dealWithCloudBackupResult = 29;
        static final int TRANSACTION_dealWithCloudRecoveryData = 30;
        static final int TRANSACTION_disableDualSta = 4;
        static final int TRANSACTION_disableP2p6g320M = 154;
        static final int TRANSACTION_disableQoEMonitor = 119;
        static final int TRANSACTION_doRecoveryFromSettingsForSsv = 134;
        static final int TRANSACTION_enable5g160MSoftAp = 89;
        static final int TRANSACTION_enableP2p6g320M = 153;
        static final int TRANSACTION_enableQoEMonitor = 118;
        static final int TRANSACTION_executeDriverCommand = 67;
        static final int TRANSACTION_executeDriverCommandWithResult = 68;
        static final int TRANSACTION_factoryReset = 150;
        static final int TRANSACTION_getAllDualStaApps = 7;
        static final int TRANSACTION_getAllScanStatisticsList = 91;
        static final int TRANSACTION_getAllSlaAcceleratedApps = 6;
        static final int TRANSACTION_getAllSlaAppsAndStates = 5;
        static final int TRANSACTION_getAllowedHotspotClients = 46;
        static final int TRANSACTION_getAvoidChannels = 80;
        static final int TRANSACTION_getBlockedHotspotClients = 45;
        static final int TRANSACTION_getCandiateNetwork = 94;
        static final int TRANSACTION_getConfiguredNetworkFromPairedDevice = 136;
        static final int TRANSACTION_getConfiguredNetworksFromPairedDevice = 137;
        static final int TRANSACTION_getConnectedHotspotClients = 47;
        static final int TRANSACTION_getCurNetworkState = 95;
        static final int TRANSACTION_getCurrentNetwork = 96;
        static final int TRANSACTION_getDBSCapacity = 77;
        static final int TRANSACTION_getDeviceName = 74;
        static final int TRANSACTION_getDualStaEnableState = 115;
        static final int TRANSACTION_getFTMState = 63;
        static final int TRANSACTION_getFwStatus = 71;
        static final int TRANSACTION_getGameMainStreamServIp = 53;
        static final int TRANSACTION_getIOTConnectScanResults = 82;
        static final int TRANSACTION_getL2Param = 120;
        static final int TRANSACTION_getNetStateInfo = 113;
        static final int TRANSACTION_getNetworkAutoChangeDefaultValue = 151;
        static final int TRANSACTION_getORouterBoostSate = 117;
        static final int TRANSACTION_getOplusSta2ConnectionInfo = 10;
        static final int TRANSACTION_getOplusSta2CurConfigKey = 11;
        static final int TRANSACTION_getOplusSupportedFeatures = 1;
        static final int TRANSACTION_getPHYCapacity = 78;
        static final int TRANSACTION_getPasspointCertifiedState = 38;
        static final int TRANSACTION_getPasspointUserName = 37;
        static final int TRANSACTION_getScanStatisticsList = 90;
        static final int TRANSACTION_getSecondaryWifiNetwork = 21;
        static final int TRANSACTION_getSlaAppState = 3;
        static final int TRANSACTION_getSlaTotalTraffic = 9;
        static final int TRANSACTION_getSlaWorkMode = 8;
        static final int TRANSACTION_getSnifferState = 59;
        static final int TRANSACTION_getSupportedChannels = 79;
        static final int TRANSACTION_getTargetMacAddress = 12;
        static final int TRANSACTION_getULLState = 116;
        static final int TRANSACTION_getWJDLinkStats = 149;
        static final int TRANSACTION_getWcnBootStatus = 139;
        static final int TRANSACTION_getWifiOCloudData = 24;
        static final int TRANSACTION_getWifiRestrictionList = 14;
        static final int TRANSACTION_handlePhoneCloneStatus = 92;
        static final int TRANSACTION_hasOCloudDirtyData = 28;
        static final int TRANSACTION_iotConnectScanBusy = 85;
        static final int TRANSACTION_isInSnifferMode = 103;
        static final int TRANSACTION_isOpenApiSupported = 146;
        static final int TRANSACTION_isP2PBeingUsedByForeground = 152;
        static final int TRANSACTION_isP2p5GSupported = 39;
        static final int TRANSACTION_isP2p6GHzBandSupported = 41;
        static final int TRANSACTION_isPrimaryWifi = 20;
        static final int TRANSACTION_isSoftap5GSupported = 40;
        static final int TRANSACTION_isSoftap6GHzBandSupported = 42;
        static final int TRANSACTION_isSubwifiManuconnect = 97;
        static final int TRANSACTION_isSupport5g160MSoftAp = 87;
        static final int TRANSACTION_isSupport5g160MStaForPhoneClone = 88;
        static final int TRANSACTION_isSupportP2p6G320M = 155;
        static final int TRANSACTION_isSupportSnifferCaptureWithUdp = 102;
        static final int TRANSACTION_isWcnLoadSuccess = 138;
        static final int TRANSACTION_isWifiAutoConnectionDisabled = 17;
        static final int TRANSACTION_keepSnifferMode = 104;
        static final int TRANSACTION_manualReconnect = 147;
        static final int TRANSACTION_notifyGameHighTemperature = 50;
        static final int TRANSACTION_notifyGameInfoJsonStr = 51;
        static final int TRANSACTION_notifyGameLatency = 49;
        static final int TRANSACTION_notifyGameModeState = 48;
        static final int TRANSACTION_notifyMediaEventToWifi = 135;
        static final int TRANSACTION_notifyMeetingWorkingState = 52;
        static final int TRANSACTION_passpointANQPScanResults = 33;
        static final int TRANSACTION_registerNetworkSelectionOpt = 142;
        static final int TRANSACTION_releaseDualSta = 141;
        static final int TRANSACTION_releaseDualStaNetwork = 19;
        static final int TRANSACTION_removeFromRestrictionList = 15;
        static final int TRANSACTION_removeHeIeFromProbeRequest = 75;
        static final int TRANSACTION_removeNetworkByGlobalId = 26;
        static final int TRANSACTION_removeWifiEventCallback = 124;
        static final int TRANSACTION_reportBssScore = 144;
        static final int TRANSACTION_requestDualSta = 140;
        static final int TRANSACTION_requestDualStaNetwork = 18;
        static final int TRANSACTION_resetConnectionMode = 64;
        static final int TRANSACTION_resumeFWKPeriodicScan = 84;
        static final int TRANSACTION_saveExternalPeerAddress = 129;
        static final int TRANSACTION_sendABRchange = 121;
        static final int TRANSACTION_sendFTMCommand = 62;
        static final int TRANSACTION_sendIOTConnectProbeReq = 81;
        static final int TRANSACTION_sendStartScoreChange = 122;
        static final int TRANSACTION_setAllowedHotspotClients = 44;
        static final int TRANSACTION_setBlockedHotspotClients = 43;
        static final int TRANSACTION_setCastContent = 133;
        static final int TRANSACTION_setCastStatus = 132;
        static final int TRANSACTION_setDirtyFlag = 27;
        static final int TRANSACTION_setLogDump = 100;
        static final int TRANSACTION_setLogOff = 99;
        static final int TRANSACTION_setLogOn = 98;
        static final int TRANSACTION_setNfcTriggered = 128;
        static final int TRANSACTION_setOshareEnabled = 130;
        static final int TRANSACTION_setP2pPowerSave = 76;
        static final int TRANSACTION_setPasspointCertifiedState = 36;
        static final int TRANSACTION_setPcAutonomousGo = 131;
        static final int TRANSACTION_setScanThrottleInfoList = 126;
        static final int TRANSACTION_setSlaAppState = 2;
        static final int TRANSACTION_setSnifferParamWithUdp = 106;
        static final int TRANSACTION_setToWifiSnifferMode = 105;
        static final int TRANSACTION_setWifiAssistantPolicies = 110;
        static final int TRANSACTION_setWifiAutoConnectionDisabled = 16;
        static final int TRANSACTION_setWifiEnabledOnlyForTest = 109;
        static final int TRANSACTION_setWifiEventCallback = 123;
        static final int TRANSACTION_setWifiLowLatencyEnabled = 156;
        static final int TRANSACTION_setWifiRestrictionList = 13;
        static final int TRANSACTION_setWifiSmartAntennaAction = 73;
        static final int TRANSACTION_settingsDispatchOplusWifi = 148;
        static final int TRANSACTION_setupFTMdaemon = 66;
        static final int TRANSACTION_shouldLaunchBrowser = 22;
        static final int TRANSACTION_startFTMMode = 60;
        static final int TRANSACTION_startRxSensTest = 111;
        static final int TRANSACTION_startSnifferCaptureWithUdp = 107;
        static final int TRANSACTION_startSnifferMode = 57;
        static final int TRANSACTION_stopFTMMode = 61;
        static final int TRANSACTION_stopRxSensTest = 112;
        static final int TRANSACTION_stopSnifferCaptureWithUdp = 108;
        static final int TRANSACTION_stopSnifferMode = 58;
        static final int TRANSACTION_suspendFWKPeriodicScan = 83;
        static final int TRANSACTION_triggerNetworkSelection = 145;
        static final int TRANSACTION_tryToRestoreWifiSetting = 101;
        static final int TRANSACTION_unregisterNetworkSelectionOpt = 143;
        static final int TRANSACTION_updateGlobalId = 25;
        static final int TRANSACTION_wifiLoadDriver = 69;

        public Stub() {
            attachInterface(this, IOplusWifiManager.DESCRIPTOR);
        }

        public static IOplusWifiManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusWifiManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusWifiManager)) {
                return (IOplusWifiManager) iin;
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
                    return "getOplusSupportedFeatures";
                case 2:
                    return "setSlaAppState";
                case 3:
                    return "getSlaAppState";
                case 4:
                    return "disableDualSta";
                case 5:
                    return "getAllSlaAppsAndStates";
                case 6:
                    return "getAllSlaAcceleratedApps";
                case 7:
                    return "getAllDualStaApps";
                case 8:
                    return "getSlaWorkMode";
                case 9:
                    return "getSlaTotalTraffic";
                case 10:
                    return "getOplusSta2ConnectionInfo";
                case 11:
                    return "getOplusSta2CurConfigKey";
                case 12:
                    return "getTargetMacAddress";
                case 13:
                    return "setWifiRestrictionList";
                case 14:
                    return "getWifiRestrictionList";
                case 15:
                    return "removeFromRestrictionList";
                case 16:
                    return "setWifiAutoConnectionDisabled";
                case 17:
                    return "isWifiAutoConnectionDisabled";
                case 18:
                    return "requestDualStaNetwork";
                case 19:
                    return "releaseDualStaNetwork";
                case 20:
                    return OlkConstants.FUN_IS_PRIMARY_WIFI;
                case 21:
                    return "getSecondaryWifiNetwork";
                case 22:
                    return "shouldLaunchBrowser";
                case 23:
                    return "clearWifiOCloudData";
                case 24:
                    return "getWifiOCloudData";
                case 25:
                    return "updateGlobalId";
                case 26:
                    return "removeNetworkByGlobalId";
                case 27:
                    return "setDirtyFlag";
                case 28:
                    return "hasOCloudDirtyData";
                case 29:
                    return "dealWithCloudBackupResult";
                case 30:
                    return "dealWithCloudRecoveryData";
                case 31:
                    return "checkPasspointCAExist";
                case 32:
                    return "checkPasspointXMLCAExpired";
                case 33:
                    return "passpointANQPScanResults";
                case 34:
                    return "checkInternalPasspointPresetProvider";
                case 35:
                    return "checkFWKSupportPasspoint";
                case 36:
                    return "setPasspointCertifiedState";
                case 37:
                    return "getPasspointUserName";
                case 38:
                    return "getPasspointCertifiedState";
                case 39:
                    return "isP2p5GSupported";
                case 40:
                    return "isSoftap5GSupported";
                case 41:
                    return "isP2p6GHzBandSupported";
                case 42:
                    return "isSoftap6GHzBandSupported";
                case 43:
                    return "setBlockedHotspotClients";
                case 44:
                    return "setAllowedHotspotClients";
                case 45:
                    return "getBlockedHotspotClients";
                case 46:
                    return "getAllowedHotspotClients";
                case 47:
                    return "getConnectedHotspotClients";
                case 48:
                    return "notifyGameModeState";
                case 49:
                    return "notifyGameLatency";
                case 50:
                    return "notifyGameHighTemperature";
                case 51:
                    return "notifyGameInfoJsonStr";
                case 52:
                    return "notifyMeetingWorkingState";
                case 53:
                    return "getGameMainStreamServIp";
                case 54:
                    return "addDnsRecord";
                case 55:
                    return "addTcpSyncRecord";
                case 56:
                    return "addTcpRttRecord";
                case 57:
                    return "startSnifferMode";
                case 58:
                    return "stopSnifferMode";
                case 59:
                    return "getSnifferState";
                case 60:
                    return "startFTMMode";
                case 61:
                    return "stopFTMMode";
                case 62:
                    return "sendFTMCommand";
                case 63:
                    return "getFTMState";
                case 64:
                    return "resetConnectionMode";
                case 65:
                    return "changeConnectionMode";
                case 66:
                    return "setupFTMdaemon";
                case 67:
                    return "executeDriverCommand";
                case 68:
                    return "executeDriverCommandWithResult";
                case 69:
                    return "wifiLoadDriver";
                case 70:
                    return "checkWiFiDriverStatus";
                case 71:
                    return "getFwStatus";
                case 72:
                    return "agingTestForWifi";
                case 73:
                    return "setWifiSmartAntennaAction";
                case 74:
                    return "getDeviceName";
                case 75:
                    return "removeHeIeFromProbeRequest";
                case 76:
                    return "setP2pPowerSave";
                case 77:
                    return "getDBSCapacity";
                case 78:
                    return "getPHYCapacity";
                case 79:
                    return "getSupportedChannels";
                case 80:
                    return "getAvoidChannels";
                case 81:
                    return "sendIOTConnectProbeReq";
                case 82:
                    return "getIOTConnectScanResults";
                case 83:
                    return "suspendFWKPeriodicScan";
                case 84:
                    return "resumeFWKPeriodicScan";
                case 85:
                    return "iotConnectScanBusy";
                case 86:
                    return "checkFWKSupplicantScanBusy";
                case 87:
                    return "isSupport5g160MSoftAp";
                case 88:
                    return "isSupport5g160MStaForPhoneClone";
                case 89:
                    return "enable5g160MSoftAp";
                case 90:
                    return "getScanStatisticsList";
                case 91:
                    return "getAllScanStatisticsList";
                case 92:
                    return "handlePhoneCloneStatus";
                case 93:
                    return "connect";
                case 94:
                    return "getCandiateNetwork";
                case 95:
                    return "getCurNetworkState";
                case 96:
                    return "getCurrentNetwork";
                case 97:
                    return "isSubwifiManuconnect";
                case 98:
                    return "setLogOn";
                case 99:
                    return "setLogOff";
                case 100:
                    return "setLogDump";
                case 101:
                    return "tryToRestoreWifiSetting";
                case 102:
                    return "isSupportSnifferCaptureWithUdp";
                case 103:
                    return "isInSnifferMode";
                case 104:
                    return "keepSnifferMode";
                case 105:
                    return "setToWifiSnifferMode";
                case 106:
                    return "setSnifferParamWithUdp";
                case 107:
                    return "startSnifferCaptureWithUdp";
                case 108:
                    return "stopSnifferCaptureWithUdp";
                case 109:
                    return "setWifiEnabledOnlyForTest";
                case 110:
                    return "setWifiAssistantPolicies";
                case 111:
                    return "startRxSensTest";
                case 112:
                    return "stopRxSensTest";
                case 113:
                    return "getNetStateInfo";
                case 114:
                    return "attRequestAuthenticationBeforeTurnOnHotspot";
                case 115:
                    return "getDualStaEnableState";
                case 116:
                    return OlkConstants.FUN_GET_ULL_STATE;
                case 117:
                    return "getORouterBoostSate";
                case 118:
                    return OlkConstants.FUN_ENABLE_QOE_MONITOR;
                case 119:
                    return OlkConstants.FUN_DISABLE_QOE_MONITOR;
                case 120:
                    return OlkConstants.FUN_GET_L2_PARAM;
                case 121:
                    return OlkConstants.FUN_SEND_ABR_CHANGE;
                case 122:
                    return "sendStartScoreChange";
                case 123:
                    return "setWifiEventCallback";
                case 124:
                    return "removeWifiEventCallback";
                case 125:
                    return "clearWifiEventCallback";
                case 126:
                    return "setScanThrottleInfoList";
                case 127:
                    return "clearScanThrottleInfoList";
                case 128:
                    return "setNfcTriggered";
                case 129:
                    return "saveExternalPeerAddress";
                case 130:
                    return "setOshareEnabled";
                case 131:
                    return "setPcAutonomousGo";
                case 132:
                    return "setCastStatus";
                case 133:
                    return "setCastContent";
                case 134:
                    return "doRecoveryFromSettingsForSsv";
                case 135:
                    return "notifyMediaEventToWifi";
                case 136:
                    return "getConfiguredNetworkFromPairedDevice";
                case 137:
                    return "getConfiguredNetworksFromPairedDevice";
                case 138:
                    return "isWcnLoadSuccess";
                case 139:
                    return "getWcnBootStatus";
                case 140:
                    return OlkConstants.FUN_REQUEST_DUALSTA;
                case 141:
                    return OlkConstants.FUN_RELEASE_DUALSTA;
                case 142:
                    return "registerNetworkSelectionOpt";
                case 143:
                    return "unregisterNetworkSelectionOpt";
                case 144:
                    return "reportBssScore";
                case 145:
                    return "triggerNetworkSelection";
                case 146:
                    return "isOpenApiSupported";
                case 147:
                    return "manualReconnect";
                case 148:
                    return "settingsDispatchOplusWifi";
                case 149:
                    return "getWJDLinkStats";
                case 150:
                    return "factoryReset";
                case 151:
                    return "getNetworkAutoChangeDefaultValue";
                case 152:
                    return "isP2PBeingUsedByForeground";
                case 153:
                    return "enableP2p6g320M";
                case 154:
                    return "disableP2p6g320M";
                case 155:
                    return "isSupportP2p6G320M";
                case 156:
                    return "setWifiLowLatencyEnabled";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, final Parcel data, final Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusWifiManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusWifiManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            long _result = getOplusSupportedFeatures();
                            reply.writeNoException();
                            reply.writeLong(_result);
                            return true;
                        case 2:
                            String _arg0 = data.readString();
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result2 = setSlaAppState(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result3 = getSlaAppState(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            disableDualSta();
                            reply.writeNoException();
                            return true;
                        case 5:
                            String[] _result4 = getAllSlaAppsAndStates();
                            reply.writeNoException();
                            reply.writeStringArray(_result4);
                            return true;
                        case 6:
                            String[] _result5 = getAllSlaAcceleratedApps();
                            reply.writeNoException();
                            reply.writeStringArray(_result5);
                            return true;
                        case 7:
                            String[] _result6 = getAllDualStaApps();
                            reply.writeNoException();
                            reply.writeStringArray(_result6);
                            return true;
                        case 8:
                            int _result7 = getSlaWorkMode();
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 9:
                            long _result8 = getSlaTotalTraffic();
                            reply.writeNoException();
                            reply.writeLong(_result8);
                            return true;
                        case 10:
                            ExtendsWifiInfo _result9 = getOplusSta2ConnectionInfo();
                            reply.writeNoException();
                            reply.writeTypedObject(_result9, 1);
                            return true;
                        case 11:
                            String _result10 = getOplusSta2CurConfigKey();
                            reply.writeNoException();
                            reply.writeString(_result10);
                            return true;
                        case 12:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            String _result11 = getTargetMacAddress(_arg03);
                            reply.writeNoException();
                            reply.writeString(_result11);
                            return true;
                        case 13:
                            String _arg04 = data.readString();
                            List<String> _arg12 = data.createStringArrayList();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result12 = setWifiRestrictionList(_arg04, _arg12, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 14:
                            String _arg05 = data.readString();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            List<String> _result13 = getWifiRestrictionList(_arg05, _arg13);
                            reply.writeNoException();
                            reply.writeStringList(_result13);
                            return true;
                        case 15:
                            String _arg06 = data.readString();
                            List<String> _arg14 = data.createStringArrayList();
                            String _arg22 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result14 = removeFromRestrictionList(_arg06, _arg14, _arg22);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 16:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result15 = setWifiAutoConnectionDisabled(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 17:
                            boolean _result16 = isWifiAutoConnectionDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 18:
                            String _arg08 = data.readString();
                            data.enforceNoDataAvail();
                            requestDualStaNetwork(_arg08);
                            reply.writeNoException();
                            return true;
                        case 19:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            releaseDualStaNetwork(_arg09);
                            reply.writeNoException();
                            return true;
                        case 20:
                            Network _arg010 = (Network) data.readTypedObject(Network.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result17 = isPrimaryWifi(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 21:
                            Network _result18 = getSecondaryWifiNetwork();
                            reply.writeNoException();
                            reply.writeTypedObject(_result18, 1);
                            return true;
                        case 22:
                            Network _arg011 = (Network) data.readTypedObject(Network.CREATOR);
                            String _arg15 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result19 = shouldLaunchBrowser(_arg011, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result19);
                            return true;
                        case 23:
                            boolean _arg012 = data.readBoolean();
                            data.enforceNoDataAvail();
                            clearWifiOCloudData(_arg012);
                            reply.writeNoException();
                            return true;
                        case 24:
                            boolean _arg013 = data.readBoolean();
                            data.enforceNoDataAvail();
                            List<String> _result20 = getWifiOCloudData(_arg013);
                            reply.writeNoException();
                            reply.writeStringList(_result20);
                            return true;
                        case 25:
                            int _arg014 = data.readInt();
                            String _arg16 = data.readString();
                            data.enforceNoDataAvail();
                            updateGlobalId(_arg014, _arg16);
                            reply.writeNoException();
                            return true;
                        case 26:
                            String _arg015 = data.readString();
                            String _arg17 = data.readString();
                            boolean _arg23 = data.readBoolean();
                            data.enforceNoDataAvail();
                            removeNetworkByGlobalId(_arg015, _arg17, _arg23);
                            reply.writeNoException();
                            return true;
                        case 27:
                            String _arg016 = data.readString();
                            boolean _arg18 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setDirtyFlag(_arg016, _arg18);
                            reply.writeNoException();
                            return true;
                        case 28:
                            boolean _result21 = hasOCloudDirtyData();
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 29:
                            List<String> _arg017 = data.createStringArrayList();
                            String _arg19 = data.readString();
                            data.enforceNoDataAvail();
                            dealWithCloudBackupResult(_arg017, _arg19);
                            reply.writeNoException();
                            return true;
                        case 30:
                            List<String> _arg018 = data.createStringArrayList();
                            String _arg110 = data.readString();
                            data.enforceNoDataAvail();
                            dealWithCloudRecoveryData(_arg018, _arg110);
                            reply.writeNoException();
                            return true;
                        case 31:
                            String _arg019 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result22 = checkPasspointCAExist(_arg019);
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        case 32:
                            String _arg020 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result23 = checkPasspointXMLCAExpired(_arg020);
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 33:
                            List<ScanResult> _arg021 = data.createTypedArrayList(ScanResult.CREATOR);
                            data.enforceNoDataAvail();
                            List<ScanResult> _result24 = passpointANQPScanResults(_arg021);
                            reply.writeNoException();
                            reply.writeTypedList(_result24, 1);
                            return true;
                        case 34:
                            String _arg022 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result25 = checkInternalPasspointPresetProvider(_arg022);
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 35:
                            boolean _result26 = checkFWKSupportPasspoint();
                            reply.writeNoException();
                            reply.writeBoolean(_result26);
                            return true;
                        case 36:
                            String _arg023 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result27 = setPasspointCertifiedState(_arg023);
                            reply.writeNoException();
                            reply.writeBoolean(_result27);
                            return true;
                        case 37:
                            String _arg024 = data.readString();
                            data.enforceNoDataAvail();
                            String _result28 = getPasspointUserName(_arg024);
                            reply.writeNoException();
                            reply.writeString(_result28);
                            return true;
                        case 38:
                            String _arg025 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result29 = getPasspointCertifiedState(_arg025);
                            reply.writeNoException();
                            reply.writeBoolean(_result29);
                            return true;
                        case 39:
                            boolean _result30 = isP2p5GSupported();
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 40:
                            boolean _result31 = isSoftap5GSupported();
                            reply.writeNoException();
                            reply.writeBoolean(_result31);
                            return true;
                        case 41:
                            boolean _result32 = isP2p6GHzBandSupported();
                            reply.writeNoException();
                            reply.writeBoolean(_result32);
                            return true;
                        case 42:
                            boolean _result33 = isSoftap6GHzBandSupported();
                            reply.writeNoException();
                            reply.writeBoolean(_result33);
                            return true;
                        case 43:
                            List<HotspotClient> _arg026 = data.createTypedArrayList(HotspotClient.CREATOR);
                            data.enforceNoDataAvail();
                            setBlockedHotspotClients(_arg026);
                            reply.writeNoException();
                            return true;
                        case 44:
                            List<HotspotClient> _arg027 = data.createTypedArrayList(HotspotClient.CREATOR);
                            data.enforceNoDataAvail();
                            setAllowedHotspotClients(_arg027);
                            reply.writeNoException();
                            return true;
                        case 45:
                            List<HotspotClient> _result34 = getBlockedHotspotClients();
                            reply.writeNoException();
                            reply.writeTypedList(_result34, 1);
                            return true;
                        case 46:
                            List<HotspotClient> _result35 = getAllowedHotspotClients();
                            reply.writeNoException();
                            reply.writeTypedList(_result35, 1);
                            return true;
                        case 47:
                            List<HotspotClient> _result36 = getConnectedHotspotClients();
                            reply.writeNoException();
                            reply.writeTypedList(_result36, 1);
                            return true;
                        case 48:
                            boolean _arg028 = data.readBoolean();
                            String _arg111 = data.readString();
                            data.enforceNoDataAvail();
                            notifyGameModeState(_arg028, _arg111);
                            reply.writeNoException();
                            return true;
                        case 49:
                            String _arg029 = data.readString();
                            String _arg112 = data.readString();
                            data.enforceNoDataAvail();
                            notifyGameLatency(_arg029, _arg112);
                            reply.writeNoException();
                            return true;
                        case 50:
                            int _arg030 = data.readInt();
                            String _arg113 = data.readString();
                            data.enforceNoDataAvail();
                            notifyGameHighTemperature(_arg030, _arg113);
                            reply.writeNoException();
                            return true;
                        case 51:
                            String _arg031 = data.readString();
                            data.enforceNoDataAvail();
                            notifyGameInfoJsonStr(_arg031);
                            reply.writeNoException();
                            return true;
                        case 52:
                            boolean _arg032 = data.readBoolean();
                            String _arg114 = data.readString();
                            data.enforceNoDataAvail();
                            notifyMeetingWorkingState(_arg032, _arg114);
                            reply.writeNoException();
                            return true;
                        case 53:
                            Map<String, String> _result37 = getGameMainStreamServIp();
                            reply.writeNoException();
                            if (_result37 == null) {
                                reply.writeInt(-1);
                            } else {
                                reply.writeInt(_result37.size());
                                _result37.forEach(new BiConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$$ExternalSyntheticLambda0
                                    @Override // java.util.function.BiConsumer
                                    public final void accept(Object obj, Object obj2) {
                                        IOplusWifiManager.Stub.lambda$onTransact$0(reply, (String) obj, (String) obj2);
                                    }
                                });
                            }
                            return true;
                        case 54:
                            int _arg033 = data.readInt();
                            int _arg115 = data.readInt();
                            int _arg24 = data.readInt();
                            String _arg3 = data.readString();
                            String[] _arg4 = data.createStringArray();
                            data.enforceNoDataAvail();
                            addDnsRecord(_arg033, _arg115, _arg24, _arg3, _arg4);
                            reply.writeNoException();
                            return true;
                        case 55:
                            int _arg034 = data.readInt();
                            int _arg116 = data.readInt();
                            int _arg25 = data.readInt();
                            int _arg32 = data.readInt();
                            String _arg42 = data.readString();
                            data.enforceNoDataAvail();
                            addTcpSyncRecord(_arg034, _arg116, _arg25, _arg32, _arg42);
                            reply.writeNoException();
                            return true;
                        case 56:
                            long[] _arg035 = data.createLongArray();
                            data.enforceNoDataAvail();
                            addTcpRttRecord(_arg035);
                            reply.writeNoException();
                            return true;
                        case 57:
                            int _arg036 = data.readInt();
                            int _arg117 = data.readInt();
                            int _arg26 = data.readInt();
                            int _arg33 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result38 = startSnifferMode(_arg036, _arg117, _arg26, _arg33);
                            reply.writeNoException();
                            reply.writeInt(_result38);
                            return true;
                        case 58:
                            int _result39 = stopSnifferMode();
                            reply.writeNoException();
                            reply.writeInt(_result39);
                            return true;
                        case 59:
                            int _result40 = getSnifferState();
                            reply.writeNoException();
                            reply.writeInt(_result40);
                            return true;
                        case 60:
                            int _result41 = startFTMMode();
                            reply.writeNoException();
                            reply.writeInt(_result41);
                            return true;
                        case 61:
                            int _result42 = stopFTMMode();
                            reply.writeNoException();
                            reply.writeInt(_result42);
                            return true;
                        case 62:
                            String _arg037 = data.readString();
                            data.enforceNoDataAvail();
                            int _result43 = sendFTMCommand(_arg037);
                            reply.writeNoException();
                            reply.writeInt(_result43);
                            return true;
                        case 63:
                            int _result44 = getFTMState();
                            reply.writeNoException();
                            reply.writeInt(_result44);
                            return true;
                        case 64:
                            boolean _result45 = resetConnectionMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result45);
                            return true;
                        case 65:
                            int _arg038 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result46 = changeConnectionMode(_arg038);
                            reply.writeNoException();
                            reply.writeBoolean(_result46);
                            return true;
                        case 66:
                            boolean _result47 = setupFTMdaemon();
                            reply.writeNoException();
                            reply.writeBoolean(_result47);
                            return true;
                        case 67:
                            String _arg039 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result48 = executeDriverCommand(_arg039);
                            reply.writeNoException();
                            reply.writeBoolean(_result48);
                            return true;
                        case 68:
                            String _arg040 = data.readString();
                            data.enforceNoDataAvail();
                            String _result49 = executeDriverCommandWithResult(_arg040);
                            reply.writeNoException();
                            reply.writeString(_result49);
                            return true;
                        case 69:
                            boolean _arg041 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result50 = wifiLoadDriver(_arg041);
                            reply.writeNoException();
                            reply.writeBoolean(_result50);
                            return true;
                        case 70:
                            boolean _result51 = checkWiFiDriverStatus();
                            reply.writeNoException();
                            reply.writeBoolean(_result51);
                            return true;
                        case 71:
                            String _result52 = getFwStatus();
                            reply.writeNoException();
                            reply.writeString(_result52);
                            return true;
                        case 72:
                            boolean _result53 = agingTestForWifi();
                            reply.writeNoException();
                            reply.writeBoolean(_result53);
                            return true;
                        case 73:
                            int _arg042 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result54 = setWifiSmartAntennaAction(_arg042);
                            reply.writeNoException();
                            reply.writeBoolean(_result54);
                            return true;
                        case 74:
                            String _result55 = getDeviceName();
                            reply.writeNoException();
                            reply.writeString(_result55);
                            return true;
                        case 75:
                            boolean _arg043 = data.readBoolean();
                            data.enforceNoDataAvail();
                            removeHeIeFromProbeRequest(_arg043);
                            reply.writeNoException();
                            return true;
                        case 76:
                            boolean _arg044 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setP2pPowerSave(_arg044);
                            reply.writeNoException();
                            return true;
                        case 77:
                            int _result56 = getDBSCapacity();
                            reply.writeNoException();
                            reply.writeInt(_result56);
                            return true;
                        case 78:
                            int _arg045 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result57 = getPHYCapacity(_arg045);
                            reply.writeNoException();
                            reply.writeInt(_result57);
                            return true;
                        case 79:
                            int _arg046 = data.readInt();
                            data.enforceNoDataAvail();
                            int[] _result58 = getSupportedChannels(_arg046);
                            reply.writeNoException();
                            reply.writeIntArray(_result58);
                            return true;
                        case 80:
                            int[] _result59 = getAvoidChannels();
                            reply.writeNoException();
                            reply.writeIntArray(_result59);
                            return true;
                        case 81:
                            String _arg047 = data.readString();
                            int[] _arg118 = data.createIntArray();
                            String _arg27 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result60 = sendIOTConnectProbeReq(_arg047, _arg118, _arg27);
                            reply.writeNoException();
                            reply.writeBoolean(_result60);
                            return true;
                        case 82:
                            List<ScanResult> _result61 = getIOTConnectScanResults();
                            reply.writeNoException();
                            reply.writeTypedList(_result61, 1);
                            return true;
                        case 83:
                            int _arg048 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result62 = suspendFWKPeriodicScan(_arg048);
                            reply.writeNoException();
                            reply.writeBoolean(_result62);
                            return true;
                        case 84:
                            resumeFWKPeriodicScan();
                            reply.writeNoException();
                            return true;
                        case 85:
                            boolean _result63 = iotConnectScanBusy();
                            reply.writeNoException();
                            reply.writeBoolean(_result63);
                            return true;
                        case 86:
                            boolean _result64 = checkFWKSupplicantScanBusy();
                            reply.writeNoException();
                            reply.writeBoolean(_result64);
                            return true;
                        case 87:
                            boolean _result65 = isSupport5g160MSoftAp();
                            reply.writeNoException();
                            reply.writeBoolean(_result65);
                            return true;
                        case 88:
                            boolean _result66 = isSupport5g160MStaForPhoneClone();
                            reply.writeNoException();
                            reply.writeBoolean(_result66);
                            return true;
                        case 89:
                            boolean _arg049 = data.readBoolean();
                            int _arg119 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result67 = enable5g160MSoftAp(_arg049, _arg119);
                            reply.writeNoException();
                            reply.writeInt(_result67);
                            return true;
                        case 90:
                            String _arg050 = data.readString();
                            int _arg120 = data.readInt();
                            data.enforceNoDataAvail();
                            List<OplusScanStatistics> _result68 = getScanStatisticsList(_arg050, _arg120);
                            reply.writeNoException();
                            reply.writeTypedList(_result68, 1);
                            return true;
                        case 91:
                            int _arg051 = data.readInt();
                            data.enforceNoDataAvail();
                            List<OplusScanStatistics> _result69 = getAllScanStatisticsList(_arg051);
                            reply.writeNoException();
                            reply.writeTypedList(_result69, 1);
                            return true;
                        case 92:
                            int _arg052 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result70 = handlePhoneCloneStatus(_arg052);
                            reply.writeNoException();
                            reply.writeInt(_result70);
                            return true;
                        case 93:
                            int _arg053 = data.readInt();
                            ExtendsWifiConfig _arg121 = (ExtendsWifiConfig) data.readTypedObject(ExtendsWifiConfig.CREATOR);
                            String _arg28 = data.readString();
                            int _arg34 = data.readInt();
                            IBinder _arg43 = data.readStrongBinder();
                            IBinder _arg5 = data.readStrongBinder();
                            int _arg6 = data.readInt();
                            data.enforceNoDataAvail();
                            connect(_arg053, _arg121, _arg28, _arg34, _arg43, _arg5, _arg6);
                            reply.writeNoException();
                            return true;
                        case 94:
                            String _arg054 = data.readString();
                            data.enforceNoDataAvail();
                            List<ScanResult> _result71 = getCandiateNetwork(_arg054);
                            reply.writeNoException();
                            reply.writeTypedList(_result71, 1);
                            return true;
                        case 95:
                            String _arg055 = data.readString();
                            data.enforceNoDataAvail();
                            int _result72 = getCurNetworkState(_arg055);
                            reply.writeNoException();
                            reply.writeInt(_result72);
                            return true;
                        case 96:
                            Network _result73 = getCurrentNetwork();
                            reply.writeNoException();
                            reply.writeTypedObject(_result73, 1);
                            return true;
                        case 97:
                            boolean _result74 = isSubwifiManuconnect();
                            reply.writeNoException();
                            reply.writeBoolean(_result74);
                            return true;
                        case 98:
                            long _arg056 = data.readLong();
                            String _arg122 = data.readString();
                            data.enforceNoDataAvail();
                            setLogOn(_arg056, _arg122);
                            reply.writeNoException();
                            return true;
                        case 99:
                            setLogOff();
                            reply.writeNoException();
                            return true;
                        case 100:
                            setLogDump();
                            reply.writeNoException();
                            return true;
                        case 101:
                            int _result75 = tryToRestoreWifiSetting();
                            reply.writeNoException();
                            reply.writeInt(_result75);
                            return true;
                        case 102:
                            boolean _result76 = isSupportSnifferCaptureWithUdp();
                            reply.writeNoException();
                            reply.writeBoolean(_result76);
                            return true;
                        case 103:
                            boolean _result77 = isInSnifferMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result77);
                            return true;
                        case 104:
                            int _arg057 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result78 = keepSnifferMode(_arg057);
                            reply.writeNoException();
                            reply.writeInt(_result78);
                            return true;
                        case 105:
                            boolean _arg058 = data.readBoolean();
                            boolean _arg123 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result79 = setToWifiSnifferMode(_arg058, _arg123);
                            reply.writeNoException();
                            reply.writeInt(_result79);
                            return true;
                        case 106:
                            int _arg059 = data.readInt();
                            int _arg124 = data.readInt();
                            boolean _arg29 = data.readBoolean();
                            boolean _arg35 = data.readBoolean();
                            boolean _arg44 = data.readBoolean();
                            int _arg52 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result80 = setSnifferParamWithUdp(_arg059, _arg124, _arg29, _arg35, _arg44, _arg52);
                            reply.writeNoException();
                            reply.writeInt(_result80);
                            return true;
                        case 107:
                            int _arg060 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result81 = startSnifferCaptureWithUdp(_arg060);
                            reply.writeNoException();
                            reply.writeInt(_result81);
                            return true;
                        case 108:
                            int _result82 = stopSnifferCaptureWithUdp();
                            reply.writeNoException();
                            reply.writeInt(_result82);
                            return true;
                        case 109:
                            boolean _arg061 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result83 = setWifiEnabledOnlyForTest(_arg061);
                            reply.writeNoException();
                            reply.writeBoolean(_result83);
                            return true;
                        case 110:
                            int _arg062 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result84 = setWifiAssistantPolicies(_arg062);
                            reply.writeNoException();
                            reply.writeBoolean(_result84);
                            return true;
                        case 111:
                            ExtendsWifiConfig _arg063 = (ExtendsWifiConfig) data.readTypedObject(ExtendsWifiConfig.CREATOR);
                            String _arg125 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result85 = startRxSensTest(_arg063, _arg125);
                            reply.writeNoException();
                            reply.writeBoolean(_result85);
                            return true;
                        case 112:
                            stopRxSensTest();
                            reply.writeNoException();
                            return true;
                        case 113:
                            int _arg064 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result86 = getNetStateInfo(_arg064);
                            reply.writeNoException();
                            reply.writeString(_result86);
                            return true;
                        case 114:
                            int _arg065 = data.readInt();
                            boolean _arg126 = data.readBoolean();
                            int _arg210 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result87 = attRequestAuthenticationBeforeTurnOnHotspot(_arg065, _arg126, _arg210);
                            reply.writeNoException();
                            reply.writeBoolean(_result87);
                            return true;
                        case 115:
                            String _arg066 = data.readString();
                            data.enforceNoDataAvail();
                            int _result88 = getDualStaEnableState(_arg066);
                            reply.writeNoException();
                            reply.writeInt(_result88);
                            return true;
                        case 116:
                            String _arg067 = data.readString();
                            data.enforceNoDataAvail();
                            int _result89 = getULLState(_arg067);
                            reply.writeNoException();
                            reply.writeInt(_result89);
                            return true;
                        case 117:
                            String _arg068 = data.readString();
                            data.enforceNoDataAvail();
                            int _result90 = getORouterBoostSate(_arg068);
                            reply.writeNoException();
                            reply.writeInt(_result90);
                            return true;
                        case 118:
                            String _arg069 = data.readString();
                            data.enforceNoDataAvail();
                            enableQoEMonitor(_arg069);
                            reply.writeNoException();
                            return true;
                        case 119:
                            String _arg070 = data.readString();
                            data.enforceNoDataAvail();
                            disableQoEMonitor(_arg070);
                            reply.writeNoException();
                            return true;
                        case 120:
                            String _arg071 = data.readString();
                            int _arg127 = data.readInt();
                            data.enforceNoDataAvail();
                            Map _result91 = getL2Param(_arg071, _arg127);
                            reply.writeNoException();
                            reply.writeMap(_result91);
                            return true;
                        case 121:
                            String _arg072 = data.readString();
                            int _arg128 = data.readInt();
                            int _arg211 = data.readInt();
                            data.enforceNoDataAvail();
                            sendABRchange(_arg072, _arg128, _arg211);
                            reply.writeNoException();
                            return true;
                        case 122:
                            String _arg073 = data.readString();
                            int _arg129 = data.readInt();
                            int _arg212 = data.readInt();
                            data.enforceNoDataAvail();
                            sendStartScoreChange(_arg073, _arg129, _arg212);
                            reply.writeNoException();
                            return true;
                        case 123:
                            IOplusWifiEventCallback _arg074 = IOplusWifiEventCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setWifiEventCallback(_arg074);
                            reply.writeNoException();
                            return true;
                        case 124:
                            IOplusWifiEventCallback _arg075 = IOplusWifiEventCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            removeWifiEventCallback(_arg075);
                            reply.writeNoException();
                            return true;
                        case 125:
                            clearWifiEventCallback();
                            reply.writeNoException();
                            return true;
                        case 126:
                            List<ScanThrottleInfo> _arg076 = data.createTypedArrayList(ScanThrottleInfo.CREATOR);
                            boolean _arg130 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setScanThrottleInfoList(_arg076, _arg130);
                            reply.writeNoException();
                            return true;
                        case 127:
                            clearScanThrottleInfoList();
                            reply.writeNoException();
                            return true;
                        case 128:
                            boolean _arg077 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result92 = setNfcTriggered(_arg077);
                            reply.writeNoException();
                            reply.writeBoolean(_result92);
                            return true;
                        case 129:
                            String _arg078 = data.readString();
                            data.enforceNoDataAvail();
                            saveExternalPeerAddress(_arg078);
                            reply.writeNoException();
                            return true;
                        case 130:
                            boolean _arg079 = data.readBoolean();
                            int _arg131 = data.readInt();
                            boolean _arg213 = data.readBoolean();
                            boolean _arg36 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setOshareEnabled(_arg079, _arg131, _arg213, _arg36);
                            reply.writeNoException();
                            return true;
                        case 131:
                            boolean _arg080 = data.readBoolean();
                            int _arg132 = data.readInt();
                            String _arg214 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result93 = setPcAutonomousGo(_arg080, _arg132, _arg214);
                            reply.writeNoException();
                            reply.writeBoolean(_result93);
                            return true;
                        case 132:
                            int _arg081 = data.readInt();
                            int _arg133 = data.readInt();
                            int _arg215 = data.readInt();
                            String _arg37 = data.readString();
                            data.enforceNoDataAvail();
                            setCastStatus(_arg081, _arg133, _arg215, _arg37);
                            reply.writeNoException();
                            return true;
                        case 133:
                            int _arg082 = data.readInt();
                            int _arg134 = data.readInt();
                            int _arg216 = data.readInt();
                            String _arg38 = data.readString();
                            data.enforceNoDataAvail();
                            setCastContent(_arg082, _arg134, _arg216, _arg38);
                            reply.writeNoException();
                            return true;
                        case 134:
                            doRecoveryFromSettingsForSsv();
                            reply.writeNoException();
                            return true;
                        case 135:
                            String _arg083 = data.readString();
                            String _arg135 = data.readString();
                            data.enforceNoDataAvail();
                            notifyMediaEventToWifi(_arg083, _arg135);
                            reply.writeNoException();
                            return true;
                        case 136:
                            String _arg084 = data.readString();
                            int _arg136 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result94 = getConfiguredNetworkFromPairedDevice(_arg084, _arg136);
                            reply.writeNoException();
                            reply.writeTypedObject(_result94, 1);
                            return true;
                        case 137:
                            List<Bundle> _result95 = getConfiguredNetworksFromPairedDevice();
                            reply.writeNoException();
                            reply.writeTypedList(_result95, 1);
                            return true;
                        case 138:
                            boolean _result96 = isWcnLoadSuccess();
                            reply.writeNoException();
                            reply.writeBoolean(_result96);
                            return true;
                        case 139:
                            Map _result97 = getWcnBootStatus();
                            reply.writeNoException();
                            reply.writeMap(_result97);
                            return true;
                        case 140:
                            String _arg085 = data.readString();
                            data.enforceNoDataAvail();
                            int _result98 = requestDualSta(_arg085);
                            reply.writeNoException();
                            reply.writeInt(_result98);
                            return true;
                        case 141:
                            String _arg086 = data.readString();
                            data.enforceNoDataAvail();
                            int _result99 = releaseDualSta(_arg086);
                            reply.writeNoException();
                            reply.writeInt(_result99);
                            return true;
                        case 142:
                            String _arg087 = data.readString();
                            INeworkSelectionOptCb _arg137 = INeworkSelectionOptCb.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerNetworkSelectionOpt(_arg087, _arg137);
                            reply.writeNoException();
                            return true;
                        case 143:
                            String _arg088 = data.readString();
                            INeworkSelectionOptCb _arg138 = INeworkSelectionOptCb.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterNetworkSelectionOpt(_arg088, _arg138);
                            reply.writeNoException();
                            return true;
                        case 144:
                            String _arg089 = data.readString();
                            int N = data.readInt();
                            final Map<String, String> _arg139 = N < 0 ? null : new HashMap<>();
                            IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$$ExternalSyntheticLambda1
                                @Override // java.util.function.IntConsumer
                                public final void accept(int i) {
                                    IOplusWifiManager.Stub.lambda$onTransact$1(data, _arg139, i);
                                }
                            });
                            data.enforceNoDataAvail();
                            reportBssScore(_arg089, _arg139);
                            reply.writeNoException();
                            return true;
                        case 145:
                            String _arg090 = data.readString();
                            int _arg140 = data.readInt();
                            data.enforceNoDataAvail();
                            triggerNetworkSelection(_arg090, _arg140);
                            reply.writeNoException();
                            return true;
                        case 146:
                            boolean _result100 = isOpenApiSupported();
                            reply.writeNoException();
                            reply.writeBoolean(_result100);
                            return true;
                        case 147:
                            manualReconnect();
                            reply.writeNoException();
                            return true;
                        case 148:
                            int _arg091 = data.readInt();
                            Bundle _arg141 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            settingsDispatchOplusWifi(_arg091, _arg141);
                            reply.writeNoException();
                            return true;
                        case 149:
                            int _arg092 = data.readInt();
                            String[] _arg142 = data.createStringArray();
                            Map<String, String> _arg217 = new HashMap<>();
                            boolean[] _arg39 = data.createBooleanArray();
                            Map<String, String> _arg45 = new HashMap<>();
                            Map<String, String> _arg53 = new HashMap<>();
                            data.enforceNoDataAvail();
                            boolean _result101 = getWJDLinkStats(_arg092, _arg142, _arg217, _arg39, _arg45, _arg53);
                            reply.writeNoException();
                            reply.writeBoolean(_result101);
                            reply.writeInt(_arg217.size());
                            _arg217.forEach(new BiConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$$ExternalSyntheticLambda2
                                @Override // java.util.function.BiConsumer
                                public final void accept(Object obj, Object obj2) {
                                    IOplusWifiManager.Stub.lambda$onTransact$2(reply, (String) obj, (String) obj2);
                                }
                            });
                            reply.writeInt(_arg45.size());
                            _arg45.forEach(new BiConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$$ExternalSyntheticLambda3
                                @Override // java.util.function.BiConsumer
                                public final void accept(Object obj, Object obj2) {
                                    IOplusWifiManager.Stub.lambda$onTransact$3(reply, (String) obj, (String) obj2);
                                }
                            });
                            reply.writeInt(_arg53.size());
                            _arg53.forEach(new BiConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$$ExternalSyntheticLambda4
                                @Override // java.util.function.BiConsumer
                                public final void accept(Object obj, Object obj2) {
                                    IOplusWifiManager.Stub.lambda$onTransact$4(reply, (String) obj, (String) obj2);
                                }
                            });
                            return true;
                        case 150:
                            factoryReset();
                            reply.writeNoException();
                            return true;
                        case 151:
                            int _result102 = getNetworkAutoChangeDefaultValue();
                            reply.writeNoException();
                            reply.writeInt(_result102);
                            return true;
                        case 152:
                            boolean _result103 = isP2PBeingUsedByForeground();
                            reply.writeNoException();
                            reply.writeBoolean(_result103);
                            return true;
                        case 153:
                            int _arg093 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result104 = enableP2p6g320M(_arg093);
                            reply.writeNoException();
                            reply.writeBoolean(_result104);
                            return true;
                        case 154:
                            boolean _result105 = disableP2p6g320M();
                            reply.writeNoException();
                            reply.writeBoolean(_result105);
                            return true;
                        case 155:
                            boolean _result106 = isSupportP2p6G320M();
                            reply.writeNoException();
                            reply.writeBoolean(_result106);
                            return true;
                        case 156:
                            boolean _arg094 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result107 = setWifiLowLatencyEnabled(_arg094);
                            reply.writeNoException();
                            reply.writeBoolean(_result107);
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

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$1(Parcel data, Map _arg1, int i) {
            String k = data.readString();
            String v = data.readString();
            _arg1.put(k, v);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$2(Parcel reply, String k, String v) {
            reply.writeString(k);
            reply.writeString(v);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$3(Parcel reply, String k, String v) {
            reply.writeString(k);
            reply.writeString(v);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$4(Parcel reply, String k, String v) {
            reply.writeString(k);
            reply.writeString(v);
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusWifiManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusWifiManager.DESCRIPTOR;
            }

            @Override // android.net.wifi.IOplusWifiManager
            public long getOplusSupportedFeatures() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setSlaAppState(String pkgName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean getSlaAppState(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void disableDualSta() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String[] getAllSlaAppsAndStates() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String[] getAllSlaAcceleratedApps() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String[] getAllDualStaApps() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getSlaWorkMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public long getSlaTotalTraffic() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public ExtendsWifiInfo getOplusSta2ConnectionInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    ExtendsWifiInfo _result = (ExtendsWifiInfo) _reply.readTypedObject(ExtendsWifiInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String getOplusSta2CurConfigKey() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String getTargetMacAddress(String targetIpAddress) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(targetIpAddress);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setWifiRestrictionList(String packageName, List<String> list, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStringList(list);
                    _data.writeString(type);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<String> getWifiRestrictionList(String packageName, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean removeFromRestrictionList(String packageName, List<String> list, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(packageName);
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

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setWifiAutoConnectionDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isWifiAutoConnectionDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void requestDualStaNetwork(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void releaseDualStaNetwork(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isPrimaryWifi(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeTypedObject(network, 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public Network getSecondaryWifiNetwork() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    Network _result = (Network) _reply.readTypedObject(Network.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean shouldLaunchBrowser(Network network, String redirectUrl) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeTypedObject(network, 0);
                    _data.writeString(redirectUrl);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void clearWifiOCloudData(boolean hardDelete) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(hardDelete);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<String> getWifiOCloudData(boolean dirtyOnly) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(dirtyOnly);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void updateGlobalId(int itemId, String globalId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(itemId);
                    _data.writeString(globalId);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void removeNetworkByGlobalId(String configKey, String globalId, boolean hardDelete) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(configKey);
                    _data.writeString(globalId);
                    _data.writeBoolean(hardDelete);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setDirtyFlag(String globalId, boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(globalId);
                    _data.writeBoolean(value);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean hasOCloudDirtyData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void dealWithCloudBackupResult(List<String> cloudData, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeStringList(cloudData);
                    _data.writeString(type);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void dealWithCloudRecoveryData(List<String> cloudData, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeStringList(cloudData);
                    _data.writeString(type);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean checkPasspointCAExist(String fqdn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(fqdn);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean checkPasspointXMLCAExpired(String fqdn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(fqdn);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<ScanResult> passpointANQPScanResults(List<ScanResult> scanResults) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeTypedList(scanResults, 0);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    List<ScanResult> _result = _reply.createTypedArrayList(ScanResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean checkInternalPasspointPresetProvider(String fqdn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(fqdn);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean checkFWKSupportPasspoint() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setPasspointCertifiedState(String signature) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(signature);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String getPasspointUserName(String fqdn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(fqdn);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean getPasspointCertifiedState(String fqdn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(fqdn);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isP2p5GSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isSoftap5GSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isP2p6GHzBandSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isSoftap6GHzBandSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setBlockedHotspotClients(List<HotspotClient> clientList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeTypedList(clientList, 0);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setAllowedHotspotClients(List<HotspotClient> clientList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeTypedList(clientList, 0);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<HotspotClient> getBlockedHotspotClients() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    List<HotspotClient> _result = _reply.createTypedArrayList(HotspotClient.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<HotspotClient> getAllowedHotspotClients() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    List<HotspotClient> _result = _reply.createTypedArrayList(HotspotClient.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<HotspotClient> getConnectedHotspotClients() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    List<HotspotClient> _result = _reply.createTypedArrayList(HotspotClient.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void notifyGameModeState(boolean state, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(state);
                    _data.writeString(pkgName);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void notifyGameLatency(String latencyInfo, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(latencyInfo);
                    _data.writeString(pkgName);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void notifyGameHighTemperature(int temperature, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(temperature);
                    _data.writeString(pkgName);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void notifyGameInfoJsonStr(String gameInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(gameInfo);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void notifyMeetingWorkingState(boolean state, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(state);
                    _data.writeString(pkgName);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public Map<String, String> getGameMainStreamServIp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    int N = _reply.readInt();
                    final Map<String, String> _result = N < 0 ? null : new HashMap<>();
                    IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$Proxy$$ExternalSyntheticLambda0
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            IOplusWifiManager.Stub.Proxy.lambda$getGameMainStreamServIp$0(_reply, _result, i);
                        }
                    });
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$getGameMainStreamServIp$0(Parcel _reply, Map _result, int i) {
                String k = _reply.readString();
                String v = _reply.readString();
                _result.put(k, v);
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void addDnsRecord(int errCode, int latencyMs, int netId, String hostname, String[] ipAddresses) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(errCode);
                    _data.writeInt(latencyMs);
                    _data.writeInt(netId);
                    _data.writeString(hostname);
                    _data.writeStringArray(ipAddresses);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void addTcpSyncRecord(int netId, int uid, int errCode, int latency, String ipAddr) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(uid);
                    _data.writeInt(errCode);
                    _data.writeInt(latency);
                    _data.writeString(ipAddr);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void addTcpRttRecord(long[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeLongArray(data);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int startSnifferMode(int channel, int bandwidth, int maxPacketNum, int maxPacketSize) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(channel);
                    _data.writeInt(bandwidth);
                    _data.writeInt(maxPacketNum);
                    _data.writeInt(maxPacketSize);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int stopSnifferMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getSnifferState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int startFTMMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int stopFTMMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int sendFTMCommand(String ftmCmd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(ftmCmd);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getFTMState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean resetConnectionMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean changeConnectionMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setupFTMdaemon() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean executeDriverCommand(String command) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(command);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String executeDriverCommandWithResult(String command) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(command);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean wifiLoadDriver(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean checkWiFiDriverStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String getFwStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean agingTestForWifi() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setWifiSmartAntennaAction(int action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(action);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String getDeviceName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void removeHeIeFromProbeRequest(boolean remove) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(remove);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setP2pPowerSave(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getDBSCapacity() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getPHYCapacity(int band) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(band);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int[] getSupportedChannels(int band) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(band);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int[] getAvoidChannels() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean sendIOTConnectProbeReq(String addVendorIE, int[] channels, String hiddenSSIDList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(addVendorIE);
                    _data.writeIntArray(channels);
                    _data.writeString(hiddenSSIDList);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<ScanResult> getIOTConnectScanResults() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    List<ScanResult> _result = _reply.createTypedArrayList(ScanResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean suspendFWKPeriodicScan(int disableInterval) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(disableInterval);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void resumeFWKPeriodicScan() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean iotConnectScanBusy() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean checkFWKSupplicantScanBusy() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isSupport5g160MSoftAp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isSupport5g160MStaForPhoneClone() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int enable5g160MSoftAp(boolean enabled, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    _data.writeInt(channel);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<OplusScanStatistics> getScanStatisticsList(String packageName, int timeAgo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(timeAgo);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    List<OplusScanStatistics> _result = _reply.createTypedArrayList(OplusScanStatistics.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<OplusScanStatistics> getAllScanStatisticsList(int timeAgo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(timeAgo);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    List<OplusScanStatistics> _result = _reply.createTypedArrayList(OplusScanStatistics.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int handlePhoneCloneStatus(int cloneStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(cloneStatus);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void connect(int configState, ExtendsWifiConfig config, String pkgeName, int networkId, IBinder binder, IBinder callback, int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(configState);
                    _data.writeTypedObject(config, 0);
                    _data.writeString(pkgeName);
                    _data.writeInt(networkId);
                    _data.writeStrongBinder(binder);
                    _data.writeStrongBinder(callback);
                    _data.writeInt(callbackIdentifier);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<ScanResult> getCandiateNetwork(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    List<ScanResult> _result = _reply.createTypedArrayList(ScanResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getCurNetworkState(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public Network getCurrentNetwork() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    Network _result = (Network) _reply.readTypedObject(Network.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isSubwifiManuconnect() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setLogOn(long size, String param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeLong(size);
                    _data.writeString(param);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setLogOff() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setLogDump() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int tryToRestoreWifiSetting() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isSupportSnifferCaptureWithUdp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isInSnifferMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int keepSnifferMode(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(timeout);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int setToWifiSnifferMode(boolean enable, boolean restoreWifi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    _data.writeBoolean(restoreWifi);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int setSnifferParamWithUdp(int freq, int bandWidth, boolean hT40Above, boolean discardDataBody, boolean discardMgmtBody, int filter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(freq);
                    _data.writeInt(bandWidth);
                    _data.writeBoolean(hT40Above);
                    _data.writeBoolean(discardDataBody);
                    _data.writeBoolean(discardMgmtBody);
                    _data.writeInt(filter);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int startSnifferCaptureWithUdp(int port) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(port);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int stopSnifferCaptureWithUdp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setWifiEnabledOnlyForTest(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setWifiAssistantPolicies(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean startRxSensTest(ExtendsWifiConfig config, String ip) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeTypedObject(config, 0);
                    _data.writeString(ip);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void stopRxSensTest() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public String getNetStateInfo(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean attRequestAuthenticationBeforeTurnOnHotspot(int type, boolean state, int carrierId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeBoolean(state);
                    _data.writeInt(carrierId);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getDualStaEnableState(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getULLState(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getORouterBoostSate(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void enableQoEMonitor(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void disableQoEMonitor(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public Map getL2Param(String pkgName, int netId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(netId);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void sendABRchange(String pkgName, int netId, int abrGear) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(netId);
                    _data.writeInt(abrGear);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void sendStartScoreChange(String pkgName, int netId, int score) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(netId);
                    _data.writeInt(score);
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setWifiEventCallback(IOplusWifiEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void removeWifiEventCallback(IOplusWifiEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void clearWifiEventCallback() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setScanThrottleInfoList(List<ScanThrottleInfo> exceptionList, boolean foreground) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeTypedList(exceptionList, 0);
                    _data.writeBoolean(foreground);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void clearScanThrottleInfoList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setNfcTriggered(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void saveExternalPeerAddress(String address) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setOshareEnabled(boolean enable, int freq, boolean isGo, boolean isStaticIp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    _data.writeInt(freq);
                    _data.writeBoolean(isGo);
                    _data.writeBoolean(isStaticIp);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setPcAutonomousGo(boolean enable, int freq, String reverse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    _data.writeInt(freq);
                    _data.writeString(reverse);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setCastStatus(int type, int state, int role, String reverse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(state);
                    _data.writeInt(role);
                    _data.writeString(reverse);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void setCastContent(int type, int state, int role, String reverse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(state);
                    _data.writeInt(role);
                    _data.writeString(reverse);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void doRecoveryFromSettingsForSsv() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void notifyMediaEventToWifi(String msg, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(msg);
                    _data.writeString(pkgName);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public Bundle getConfiguredNetworkFromPairedDevice(String ssid, int keymgmt) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(ssid);
                    _data.writeInt(keymgmt);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public List<Bundle> getConfiguredNetworksFromPairedDevice() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                    List<Bundle> _result = _reply.createTypedArrayList(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isWcnLoadSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public Map getWcnBootStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int requestDualSta(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int releaseDualSta(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void registerNetworkSelectionOpt(String pkgName, INeworkSelectionOptCb nwSelectionOptCb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongInterface(nwSelectionOptCb);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void unregisterNetworkSelectionOpt(String pkgName, INeworkSelectionOptCb nwSelectionOptCb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongInterface(nwSelectionOptCb);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void reportBssScore(String pkgName, Map<String, String> map) throws RemoteException {
                final Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    if (map == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(map.size());
                        map.forEach(new BiConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$Proxy$$ExternalSyntheticLambda1
                            @Override // java.util.function.BiConsumer
                            public final void accept(Object obj, Object obj2) {
                                IOplusWifiManager.Stub.Proxy.lambda$reportBssScore$1(_data, (String) obj, (String) obj2);
                            }
                        });
                    }
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$reportBssScore$1(Parcel _data, String k, String v) {
                _data.writeString(k);
                _data.writeString(v);
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void triggerNetworkSelection(String pkgName, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(reason);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isOpenApiSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void manualReconnect() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void settingsDispatchOplusWifi(int eventType, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(eventType);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean getWJDLinkStats(int iface, String[] skInfoParaIn, final Map<String, String> skInfoOut, boolean[] jitterParaIn, final Map<String, String> jitterInfoOut, final Map<String, String> jitterDetailOut) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(iface);
                    _data.writeStringArray(skInfoParaIn);
                    _data.writeBooleanArray(jitterParaIn);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    if (skInfoOut != null) {
                        skInfoOut.clear();
                    }
                    IntStream.range(0, _reply.readInt()).forEach(new IntConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$Proxy$$ExternalSyntheticLambda2
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            IOplusWifiManager.Stub.Proxy.lambda$getWJDLinkStats$2(_reply, skInfoOut, i);
                        }
                    });
                    if (jitterInfoOut != null) {
                        jitterInfoOut.clear();
                    }
                    IntStream.range(0, _reply.readInt()).forEach(new IntConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$Proxy$$ExternalSyntheticLambda3
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            IOplusWifiManager.Stub.Proxy.lambda$getWJDLinkStats$3(_reply, jitterInfoOut, i);
                        }
                    });
                    if (jitterDetailOut != null) {
                        jitterDetailOut.clear();
                    }
                    IntStream.range(0, _reply.readInt()).forEach(new IntConsumer() { // from class: android.net.wifi.IOplusWifiManager$Stub$Proxy$$ExternalSyntheticLambda4
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            IOplusWifiManager.Stub.Proxy.lambda$getWJDLinkStats$4(_reply, jitterDetailOut, i);
                        }
                    });
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$getWJDLinkStats$2(Parcel _reply, Map skInfoOut, int i) {
                String k = _reply.readString();
                String v = _reply.readString();
                skInfoOut.put(k, v);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$getWJDLinkStats$3(Parcel _reply, Map jitterInfoOut, int i) {
                String k = _reply.readString();
                String v = _reply.readString();
                jitterInfoOut.put(k, v);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$getWJDLinkStats$4(Parcel _reply, Map jitterDetailOut, int i) {
                String k = _reply.readString();
                String v = _reply.readString();
                jitterDetailOut.put(k, v);
            }

            @Override // android.net.wifi.IOplusWifiManager
            public void factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public int getNetworkAutoChangeDefaultValue() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isP2PBeingUsedByForeground() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean enableP2p6g320M(int freq) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeInt(freq);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean disableP2p6g320M() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean isSupportP2p6G320M() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IOplusWifiManager
            public boolean setWifiLowLatencyEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWifiManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(156, _data, _reply, 0);
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
            return 155;
        }
    }
}
