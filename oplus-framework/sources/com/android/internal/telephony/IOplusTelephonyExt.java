package com.android.internal.telephony;

import android.app.PendingIntent;
import android.net.Network;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.CellIdentity;
import android.telephony.IOplusGeoFenceEventCallBack;
import android.telephony.SignalStrength;
import com.android.internal.telephony.IOplusTelephonyExtCallback;
import com.oplus.network.OlkConstants;
import com.oplus.uah.info.UAHPerfConstants;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusTelephonyExt extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IOplusTelephonyExt";

    void addGeoFenceEventCallBack(IOplusGeoFenceEventCallBack iOplusGeoFenceEventCallBack) throws RemoteException;

    boolean barCell(int i, int i2, int i3, int i4, long j) throws RemoteException;

    boolean canSupSlotSaSupport() throws RemoteException;

    void changeEsimStatus(int i) throws RemoteException;

    boolean cleanApnState(int i) throws RemoteException;

    boolean cleanupConnections(int i) throws RemoteException;

    boolean clearCellBlacklist(int i) throws RemoteException;

    boolean delCellBlacklist(int i, long j) throws RemoteException;

    boolean disableCellularDataPrio(String str) throws RemoteException;

    boolean disableEndc(int i) throws RemoteException;

    boolean enableCellularDataPrio(String str) throws RemoteException;

    void enableUiccApplications(int i, boolean z) throws RemoteException;

    long getActionExecuteTime(int i, int i2) throws RemoteException;

    int getCardType(int i) throws RemoteException;

    CellIdentity getCellIdentity(int i, String str, String str2) throws RemoteException;

    String getCellIdentityOperator(int i) throws RemoteException;

    Bundle getCellInfo(int i) throws RemoteException;

    int getCurrentDelayLevel() throws RemoteException;

    int getCurrentTransport(int i, int i2) throws RemoteException;

    boolean getDataCallListAction(int i) throws RemoteException;

    int getDataNetworkInternetCid(int i) throws RemoteException;

    int getDualPsEnableState() throws RemoteException;

    List<String> getEcholocateMetrics(int i) throws RemoteException;

    int getFiveGUperLayerIndAvailable(int i) throws RemoteException;

    String getFullIccId(int i) throws RemoteException;

    boolean getHasNrSecondaryServingCell() throws RemoteException;

    String getIccCardType(int i) throws RemoteException;

    boolean getIsNrAvailable() throws RemoteException;

    int getLastAction(int i) throws RemoteException;

    String[] getLteCdmaImsi(int i) throws RemoteException;

    long[] getModemTxTime() throws RemoteException;

    String getNetworkConfig(String str) throws RemoteException;

    int getNewPreferredNetworkMode(int i) throws RemoteException;

    int getNrBearerAllocation(int i) throws RemoteException;

    boolean getNrConnect() throws RemoteException;

    boolean getNrModeChangedAllow() throws RemoteException;

    Bundle getNrModeChangedEvent() throws RemoteException;

    void getNrModeToCheck(int i, Bundle bundle) throws RemoteException;

    int[] getNsaSignalStrength() throws RemoteException;

    String getOemSpn(int i) throws RemoteException;

    String getOperatorNumericForData(int i) throws RemoteException;

    SignalStrength getOrigSignalStrength(int i) throws RemoteException;

    int getPreferDdsSwitchPhoneId() throws RemoteException;

    int getPreferSubId() throws RemoteException;

    int getPreferredDefaultNetMode(int i) throws RemoteException;

    int getPreferredNetworkType(int i) throws RemoteException;

    int getRealNrState(int i) throws RemoteException;

    long getReceiveMmsTime(int i) throws RemoteException;

    long getReceiveSmsTime() throws RemoteException;

    long getReceiveSmsTimebyPhoneId(int i) throws RemoteException;

    long getReleaseMmsNetworkTime() throws RemoteException;

    IBinder getRemoteMessenger() throws RemoteException;

    boolean getRoamingReduction() throws RemoteException;

    int getSaMode(int i) throws RemoteException;

    boolean getSaPrioState(int i) throws RemoteException;

    String getSelectConfig(int i) throws RemoteException;

    int getSendSmsMapSize() throws RemoteException;

    long getSendSmsTime() throws RemoteException;

    int[] getSimlockOfflineLock(int i) throws RemoteException;

    int getSoftSimCardSlotId() throws RemoteException;

    int getSubId(int i) throws RemoteException;

    boolean getSupSlotSaSupport() throws RemoteException;

    double getTelephonyPowerLost() throws RemoteException;

    String getTelephonyPowerState() throws RemoteException;

    boolean getTempDdsSwitch() throws RemoteException;

    int getUserSaMode() throws RemoteException;

    boolean getVoNrVisible(int i) throws RemoteException;

    boolean isAlreadyUpdated() throws RemoteException;

    boolean isApnEnabled(int i) throws RemoteException;

    boolean isApnInException(int i) throws RemoteException;

    boolean isBackOffSaEnabled() throws RemoteException;

    boolean isDataConnectivityPossible(int i) throws RemoteException;

    boolean isGameAccelerateEnable() throws RemoteException;

    boolean isImsUseEnabled(int i) throws RemoteException;

    boolean isImsValid(int i) throws RemoteException;

    boolean isInDelayOOSState(int i) throws RemoteException;

    boolean isInNsa() throws RemoteException;

    int isPrimaryCellularNetwork(Network network) throws RemoteException;

    boolean isRequestingMms() throws RemoteException;

    boolean isUriFileExist(String str) throws RemoteException;

    boolean lockCellAction(int i, String str) throws RemoteException;

    boolean prioritizeDefaultDataSubscription(boolean z) throws RemoteException;

    boolean psDetachAttachAction(int i) throws RemoteException;

    boolean radioPower(int i) throws RemoteException;

    void registerCallback(String str, IOplusTelephonyExtCallback iOplusTelephonyExtCallback) throws RemoteException;

    void registerForImsRegTime(int i) throws RemoteException;

    void releaseDualPsNetwork(String str) throws RemoteException;

    void removeGeoFenceEventCallBack(IOplusGeoFenceEventCallBack iOplusGeoFenceEventCallBack) throws RemoteException;

    void reportGameEnterOrLeave(int i, String str, boolean z) throws RemoteException;

    String reportGameErrorCauseToCenter() throws RemoteException;

    void reportGameInfo(String str) throws RemoteException;

    void reportNetWorkLatency(String str) throws RemoteException;

    void reportNetWorkLevel(String str) throws RemoteException;

    void requestDualPsNetwork(String str) throws RemoteException;

    Bundle requestForTelephonyEvent(int i, int i2, Bundle bundle) throws RemoteException;

    boolean reregisterNetwork(int i) throws RemoteException;

    boolean resetBarCell(int i, int i2, int i3, int i4) throws RemoteException;

    void resetImsRegTimer(int i, int i2) throws RemoteException;

    boolean resetRadioSmooth(int i) throws RemoteException;

    boolean resetRsrpBackoff(int i, String str, int i2, int i3) throws RemoteException;

    boolean rsrpBackoff(int i, String str, int i2, int i3, int i4) throws RemoteException;

    void sendDdsSwitchEvent() throws RemoteException;

    void sendMultipartTextForSubscriberWithOptionsOem(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, int i2, boolean z2, int i3, int i4) throws RemoteException;

    void sendRecoveryRequest(int i, int i2) throws RemoteException;

    void sendTextForSubscriberWithOptionsOem(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, boolean z2, int i3, int i4) throws RemoteException;

    boolean set5gIconDelayTimer(int i, int i2) throws RemoteException;

    boolean setAllowedNetworkTypesBitmap(int i, int i2) throws RemoteException;

    boolean setCellBlackList(int i, long j) throws RemoteException;

    void setCurrentDelayLevel(int i) throws RemoteException;

    boolean setDataPrioLevel(long j, long j2, long j3) throws RemoteException;

    int setDisplayNumberExt(String str, int i) throws RemoteException;

    void setDualDataUserPreference(boolean z) throws RemoteException;

    boolean setGameAccelerateEnable(boolean z) throws RemoteException;

    void setGameErrorCauseToCenter(String str) throws RemoteException;

    boolean setNrMode(int i, int i2, String str) throws RemoteException;

    void setNrModeChangedAllow(boolean z) throws RemoteException;

    void setNwCongestionCfg(int i, int i2, byte[] bArr, int i3) throws RemoteException;

    void setPreferDdsSwitchPhoneId(int i) throws RemoteException;

    boolean setPreferredNetworkType(int i, int i2) throws RemoteException;

    boolean setSaLtePingpongState(int i, int i2) throws RemoteException;

    boolean setSaMode(int i, int i2) throws RemoteException;

    boolean setSaSilenceMode(int i, boolean z, int i2, int i3, String str) throws RemoteException;

    boolean setSelectConfig(int i, String str) throws RemoteException;

    void setSimlockOfflineLock(int i, boolean z, int i2, int[] iArr, long j) throws RemoteException;

    void setSupSlotSaSupport(boolean z) throws RemoteException;

    void setViceCardGameMode(boolean z, String str, String str2) throws RemoteException;

    void simProfileRefresh(int i, boolean z, int[] iArr) throws RemoteException;

    void startMobileDataHongbaoPolicy(int i, int i2, String str, String str2) throws RemoteException;

    void triggerImsReregStatus(int i, int i2) throws RemoteException;

    void unRegisterCallback(IOplusTelephonyExtCallback iOplusTelephonyExtCallback) throws RemoteException;

    void unregisterForImsRegTime(int i) throws RemoteException;

    boolean updateSmartDdsSwitch(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusTelephonyExt {
        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public Bundle requestForTelephonyEvent(int slotId, int eventId, Bundle data) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void registerCallback(String packageName, IOplusTelephonyExtCallback callback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void unRegisterCallback(IOplusTelephonyExtCallback callback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getSoftSimCardSlotId() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getOperatorNumericForData(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void startMobileDataHongbaoPolicy(int time1, int time2, String value1, String value2) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void sendTextForSubscriberWithOptionsOem(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod, int encodingType) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void sendMultipartTextForSubscriberWithOptionsOem(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod, int encodingType) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getCardType(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void sendRecoveryRequest(int slotIndex, int action) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public long getActionExecuteTime(int slotIndex, int action) throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getLastAction(int slotIndex) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void reportNetWorkLatency(String info) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void reportNetWorkLevel(String info) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void reportGameEnterOrLeave(int gameUid, String gamePackageName, boolean enter) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int setDisplayNumberExt(String number, int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String[] getLteCdmaImsi(int phoneid) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setViceCardGameMode(boolean enabled, String gamePkgName, String value) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void changeEsimStatus(int newState) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getFullIccId(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public Bundle getCellInfo(int slotId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getCellIdentityOperator(int slotId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getRoamingReduction() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isImsUseEnabled(int soltId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isImsValid(int soltId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getCurrentTransport(int soltId, int apnType) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getFiveGUperLayerIndAvailable(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getNrBearerAllocation(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getNrConnect() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getNetworkConfig(String key) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getTelephonyPowerState() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public double getTelephonyPowerLost() throws RemoteException {
            return 0.0d;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public long[] getModemTxTime() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isAlreadyUpdated() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getDataCallListAction(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean disableEndc(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setPreferredNetworkType(int slotId, int networkType) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getPreferredNetworkType(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean cleanupConnections(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getSaMode(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setSaMode(int slotId, int mode) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean psDetachAttachAction(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean radioPower(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setCellBlackList(int type, long cellid) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean delCellBlacklist(int type, long cellid) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean clearCellBlacklist(int type) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean resetRadioSmooth(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isApnEnabled(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getRealNrState(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean reregisterNetwork(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setSaLtePingpongState(int slotId, int isLteNrPingPong) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public IBinder getRemoteMessenger() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getVoNrVisible(int phoneId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getUserSaMode() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isInDelayOOSState(int phoneId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getSubId(int slotIndex) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void enableUiccApplications(int subId, boolean enabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean canSupSlotSaSupport() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setSupSlotSaSupport(boolean support) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getSupSlotSaSupport() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getTempDdsSwitch() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setNrModeChangedAllow(boolean enable) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getNrModeChangedAllow() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public Bundle getNrModeChangedEvent() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void addGeoFenceEventCallBack(IOplusGeoFenceEventCallBack cb) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void removeGeoFenceEventCallBack(IOplusGeoFenceEventCallBack cb) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getSelectConfig(int soltId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setSelectConfig(int phoneId, String configId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setAllowedNetworkTypesBitmap(int slotId, int networkTypeBitmap) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void reportGameInfo(String gameInfo) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String reportGameErrorCauseToCenter() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void getNrModeToCheck(int phoneId, Bundle bundle) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public List<String> getEcholocateMetrics(int index) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getIsNrAvailable() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getHasNrSecondaryServingCell() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isUriFileExist(String vUri) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setNrMode(int slotId, int mode, String caller) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean set5gIconDelayTimer(int slotId, int delayTimer) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getOemSpn(int slotId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public CellIdentity getCellIdentity(int slotId, String callingPackage, String callingFeatureId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getPreferredDefaultNetMode(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getNewPreferredNetworkMode(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public String getIccCardType(int slotId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getPreferSubId() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean lockCellAction(int slotId, String cmdStr) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean rsrpBackoff(int phoneId, String plmn, int arfch, int pci, int offset) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean resetRsrpBackoff(int phoneId, String plmn, int arfch, int pci) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean barCell(int phoneId, int rat, int arfch, int pci, long barTime) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean resetBarCell(int phoneId, int rat, int arfch, int pci) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isApnInException(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean cleanApnState(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public SignalStrength getOrigSignalStrength(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean prioritizeDefaultDataSubscription(boolean enable) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean enableCellularDataPrio(String packageName) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean disableCellularDataPrio(String packageName) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setGameAccelerateEnable(boolean b) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isGameAccelerateEnable() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void simProfileRefresh(int slotId, boolean fileChange, int[] fileList) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean updateSmartDdsSwitch(boolean changeNext) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getDataNetworkInternetCid(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setPreferDdsSwitchPhoneId(int phoneId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getPreferDdsSwitchPhoneId() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public long getReceiveMmsTime(int phoneId) throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public long getReceiveSmsTime() throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public long getSendSmsTime() throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getSendSmsMapSize() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public long getReceiveSmsTimebyPhoneId(int phoneId) throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isDataConnectivityPossible(int phoneId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void sendDdsSwitchEvent() throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isBackOffSaEnabled() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isInNsa() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int[] getNsaSignalStrength() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setGameErrorCauseToCenter(String gameErrorCause) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getCurrentDelayLevel() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setCurrentDelayLevel(int delayLevel) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void resetImsRegTimer(int phoneId, int regTimer) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void registerForImsRegTime(int slotId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void unregisterForImsRegTime(int slotId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void triggerImsReregStatus(int phoneId, int action) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean isRequestingMms() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public long getReleaseMmsNetworkTime() throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setSaSilenceMode(int phoneId, boolean enable, int module, int priority, String event) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setNwCongestionCfg(int slotId, int cmd, byte[] data, int datalen) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean setDataPrioLevel(long rat, long uplink, long downlink) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int getDualPsEnableState() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void requestDualPsNetwork(String pkgName) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void releaseDualPsNetwork(String pkgName) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int isPrimaryCellularNetwork(Network network) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setDualDataUserPreference(boolean enable) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public boolean getSaPrioState(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public void setSimlockOfflineLock(int slotId, boolean enabled, int time, int[] remind, long serverTimestamp) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExt
        public int[] getSimlockOfflineLock(int slotId) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusTelephonyExt {
        static final int TRANSACTION_addGeoFenceEventCallBack = 76;
        static final int TRANSACTION_barCell = 106;
        static final int TRANSACTION_canSupSlotSaSupport = 69;
        static final int TRANSACTION_changeEsimStatus = 26;
        static final int TRANSACTION_cleanApnState = 109;
        static final int TRANSACTION_cleanupConnections = 48;
        static final int TRANSACTION_clearCellBlacklist = 55;
        static final int TRANSACTION_delCellBlacklist = 54;
        static final int TRANSACTION_disableCellularDataPrio = 117;
        static final int TRANSACTION_disableEndc = 44;
        static final int TRANSACTION_enableCellularDataPrio = 116;
        static final int TRANSACTION_enableUiccApplications = 68;
        static final int TRANSACTION_getActionExecuteTime = 18;
        static final int TRANSACTION_getCardType = 10;
        static final int TRANSACTION_getCellIdentity = 97;
        static final int TRANSACTION_getCellIdentityOperator = 29;
        static final int TRANSACTION_getCellInfo = 28;
        static final int TRANSACTION_getCurrentDelayLevel = 137;
        static final int TRANSACTION_getCurrentTransport = 33;
        static final int TRANSACTION_getDataCallListAction = 43;
        static final int TRANSACTION_getDataNetworkInternetCid = 123;
        static final int TRANSACTION_getDualPsEnableState = 148;
        static final int TRANSACTION_getEcholocateMetrics = 85;
        static final int TRANSACTION_getFiveGUperLayerIndAvailable = 35;
        static final int TRANSACTION_getFullIccId = 27;
        static final int TRANSACTION_getHasNrSecondaryServingCell = 87;
        static final int TRANSACTION_getIccCardType = 101;
        static final int TRANSACTION_getIsNrAvailable = 86;
        static final int TRANSACTION_getLastAction = 19;
        static final int TRANSACTION_getLteCdmaImsi = 24;
        static final int TRANSACTION_getModemTxTime = 41;
        static final int TRANSACTION_getNetworkConfig = 38;
        static final int TRANSACTION_getNewPreferredNetworkMode = 100;
        static final int TRANSACTION_getNrBearerAllocation = 36;
        static final int TRANSACTION_getNrConnect = 37;
        static final int TRANSACTION_getNrModeChangedAllow = 74;
        static final int TRANSACTION_getNrModeChangedEvent = 75;
        static final int TRANSACTION_getNrModeToCheck = 84;
        static final int TRANSACTION_getNsaSignalStrength = 135;
        static final int TRANSACTION_getOemSpn = 96;
        static final int TRANSACTION_getOperatorNumericForData = 6;
        static final int TRANSACTION_getOrigSignalStrength = 112;
        static final int TRANSACTION_getPreferDdsSwitchPhoneId = 125;
        static final int TRANSACTION_getPreferSubId = 102;
        static final int TRANSACTION_getPreferredDefaultNetMode = 99;
        static final int TRANSACTION_getPreferredNetworkType = 46;
        static final int TRANSACTION_getRealNrState = 58;
        static final int TRANSACTION_getReceiveMmsTime = 126;
        static final int TRANSACTION_getReceiveSmsTime = 127;
        static final int TRANSACTION_getReceiveSmsTimebyPhoneId = 130;
        static final int TRANSACTION_getReleaseMmsNetworkTime = 144;
        static final int TRANSACTION_getRemoteMessenger = 63;
        static final int TRANSACTION_getRoamingReduction = 30;
        static final int TRANSACTION_getSaMode = 49;
        static final int TRANSACTION_getSaPrioState = 153;
        static final int TRANSACTION_getSelectConfig = 78;
        static final int TRANSACTION_getSendSmsMapSize = 129;
        static final int TRANSACTION_getSendSmsTime = 128;
        static final int TRANSACTION_getSimlockOfflineLock = 168;
        static final int TRANSACTION_getSoftSimCardSlotId = 5;
        static final int TRANSACTION_getSubId = 67;
        static final int TRANSACTION_getSupSlotSaSupport = 71;
        static final int TRANSACTION_getTelephonyPowerLost = 40;
        static final int TRANSACTION_getTelephonyPowerState = 39;
        static final int TRANSACTION_getTempDdsSwitch = 72;
        static final int TRANSACTION_getUserSaMode = 65;
        static final int TRANSACTION_getVoNrVisible = 64;
        static final int TRANSACTION_isAlreadyUpdated = 42;
        static final int TRANSACTION_isApnEnabled = 57;
        static final int TRANSACTION_isApnInException = 108;
        static final int TRANSACTION_isBackOffSaEnabled = 133;
        static final int TRANSACTION_isDataConnectivityPossible = 131;
        static final int TRANSACTION_isGameAccelerateEnable = 119;
        static final int TRANSACTION_isImsUseEnabled = 31;
        static final int TRANSACTION_isImsValid = 32;
        static final int TRANSACTION_isInDelayOOSState = 66;
        static final int TRANSACTION_isInNsa = 134;
        static final int TRANSACTION_isPrimaryCellularNetwork = 151;
        static final int TRANSACTION_isRequestingMms = 143;
        static final int TRANSACTION_isUriFileExist = 88;
        static final int TRANSACTION_lockCellAction = 103;
        static final int TRANSACTION_prioritizeDefaultDataSubscription = 115;
        static final int TRANSACTION_psDetachAttachAction = 51;
        static final int TRANSACTION_radioPower = 52;
        static final int TRANSACTION_registerCallback = 3;
        static final int TRANSACTION_registerForImsRegTime = 140;
        static final int TRANSACTION_releaseDualPsNetwork = 150;
        static final int TRANSACTION_removeGeoFenceEventCallBack = 77;
        static final int TRANSACTION_reportGameEnterOrLeave = 22;
        static final int TRANSACTION_reportGameErrorCauseToCenter = 83;
        static final int TRANSACTION_reportGameInfo = 82;
        static final int TRANSACTION_reportNetWorkLatency = 20;
        static final int TRANSACTION_reportNetWorkLevel = 21;
        static final int TRANSACTION_requestDualPsNetwork = 149;
        static final int TRANSACTION_requestForTelephonyEvent = 2;
        static final int TRANSACTION_reregisterNetwork = 59;
        static final int TRANSACTION_resetBarCell = 107;
        static final int TRANSACTION_resetImsRegTimer = 139;
        static final int TRANSACTION_resetRadioSmooth = 56;
        static final int TRANSACTION_resetRsrpBackoff = 105;
        static final int TRANSACTION_rsrpBackoff = 104;
        static final int TRANSACTION_sendDdsSwitchEvent = 132;
        static final int TRANSACTION_sendMultipartTextForSubscriberWithOptionsOem = 9;
        static final int TRANSACTION_sendRecoveryRequest = 17;
        static final int TRANSACTION_sendTextForSubscriberWithOptionsOem = 8;
        static final int TRANSACTION_set5gIconDelayTimer = 94;
        static final int TRANSACTION_setAllowedNetworkTypesBitmap = 80;
        static final int TRANSACTION_setCellBlackList = 53;
        static final int TRANSACTION_setCurrentDelayLevel = 138;
        static final int TRANSACTION_setDataPrioLevel = 147;
        static final int TRANSACTION_setDisplayNumberExt = 23;
        static final int TRANSACTION_setDualDataUserPreference = 152;
        static final int TRANSACTION_setGameAccelerateEnable = 118;
        static final int TRANSACTION_setGameErrorCauseToCenter = 136;
        static final int TRANSACTION_setNrMode = 93;
        static final int TRANSACTION_setNrModeChangedAllow = 73;
        static final int TRANSACTION_setNwCongestionCfg = 146;
        static final int TRANSACTION_setPreferDdsSwitchPhoneId = 124;
        static final int TRANSACTION_setPreferredNetworkType = 45;
        static final int TRANSACTION_setSaLtePingpongState = 61;
        static final int TRANSACTION_setSaMode = 50;
        static final int TRANSACTION_setSaSilenceMode = 145;
        static final int TRANSACTION_setSelectConfig = 79;
        static final int TRANSACTION_setSimlockOfflineLock = 167;
        static final int TRANSACTION_setSupSlotSaSupport = 70;
        static final int TRANSACTION_setViceCardGameMode = 25;
        static final int TRANSACTION_simProfileRefresh = 120;
        static final int TRANSACTION_startMobileDataHongbaoPolicy = 7;
        static final int TRANSACTION_triggerImsReregStatus = 142;
        static final int TRANSACTION_unRegisterCallback = 4;
        static final int TRANSACTION_unregisterForImsRegTime = 141;
        static final int TRANSACTION_updateSmartDdsSwitch = 121;

        public Stub() {
            attachInterface(this, IOplusTelephonyExt.DESCRIPTOR);
        }

        public static IOplusTelephonyExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusTelephonyExt.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusTelephonyExt)) {
                return (IOplusTelephonyExt) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 2:
                    return "requestForTelephonyEvent";
                case 3:
                    return OlkConstants.FUN_REGISTER_CALLBACK;
                case 4:
                    return "unRegisterCallback";
                case 5:
                    return "getSoftSimCardSlotId";
                case 6:
                    return "getOperatorNumericForData";
                case 7:
                    return "startMobileDataHongbaoPolicy";
                case 8:
                    return "sendTextForSubscriberWithOptionsOem";
                case 9:
                    return "sendMultipartTextForSubscriberWithOptionsOem";
                case 10:
                    return "getCardType";
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 34:
                case 47:
                case 60:
                case 62:
                case 81:
                case 89:
                case 90:
                case 91:
                case 92:
                case 95:
                case 98:
                case 110:
                case 111:
                case 113:
                case 114:
                case 122:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case UAHPerfConstants.UAH_EVENT_CAMERA_FRONT_NIGHT_CAPTURE /* 163 */:
                case UAHPerfConstants.UAH_EVENT_CAMERA_NIGHT_CAPTURE /* 164 */:
                case 165:
                case 166:
                default:
                    return null;
                case 17:
                    return "sendRecoveryRequest";
                case 18:
                    return "getActionExecuteTime";
                case 19:
                    return "getLastAction";
                case 20:
                    return "reportNetWorkLatency";
                case 21:
                    return "reportNetWorkLevel";
                case 22:
                    return "reportGameEnterOrLeave";
                case 23:
                    return "setDisplayNumberExt";
                case 24:
                    return "getLteCdmaImsi";
                case 25:
                    return "setViceCardGameMode";
                case 26:
                    return "changeEsimStatus";
                case 27:
                    return "getFullIccId";
                case 28:
                    return "getCellInfo";
                case 29:
                    return "getCellIdentityOperator";
                case 30:
                    return "getRoamingReduction";
                case 31:
                    return "isImsUseEnabled";
                case 32:
                    return "isImsValid";
                case 33:
                    return "getCurrentTransport";
                case 35:
                    return "getFiveGUperLayerIndAvailable";
                case 36:
                    return "getNrBearerAllocation";
                case 37:
                    return "getNrConnect";
                case 38:
                    return "getNetworkConfig";
                case 39:
                    return "getTelephonyPowerState";
                case 40:
                    return "getTelephonyPowerLost";
                case 41:
                    return "getModemTxTime";
                case 42:
                    return "isAlreadyUpdated";
                case 43:
                    return "getDataCallListAction";
                case 44:
                    return "disableEndc";
                case 45:
                    return "setPreferredNetworkType";
                case 46:
                    return "getPreferredNetworkType";
                case 48:
                    return "cleanupConnections";
                case 49:
                    return "getSaMode";
                case 50:
                    return "setSaMode";
                case 51:
                    return "psDetachAttachAction";
                case 52:
                    return "radioPower";
                case 53:
                    return "setCellBlackList";
                case 54:
                    return "delCellBlacklist";
                case 55:
                    return "clearCellBlacklist";
                case 56:
                    return "resetRadioSmooth";
                case 57:
                    return "isApnEnabled";
                case 58:
                    return "getRealNrState";
                case 59:
                    return "reregisterNetwork";
                case 61:
                    return "setSaLtePingpongState";
                case 63:
                    return "getRemoteMessenger";
                case 64:
                    return "getVoNrVisible";
                case 65:
                    return "getUserSaMode";
                case 66:
                    return "isInDelayOOSState";
                case 67:
                    return "getSubId";
                case 68:
                    return "enableUiccApplications";
                case 69:
                    return "canSupSlotSaSupport";
                case 70:
                    return "setSupSlotSaSupport";
                case 71:
                    return "getSupSlotSaSupport";
                case 72:
                    return "getTempDdsSwitch";
                case 73:
                    return "setNrModeChangedAllow";
                case 74:
                    return "getNrModeChangedAllow";
                case 75:
                    return "getNrModeChangedEvent";
                case 76:
                    return "addGeoFenceEventCallBack";
                case 77:
                    return "removeGeoFenceEventCallBack";
                case 78:
                    return "getSelectConfig";
                case 79:
                    return "setSelectConfig";
                case 80:
                    return "setAllowedNetworkTypesBitmap";
                case 82:
                    return "reportGameInfo";
                case 83:
                    return "reportGameErrorCauseToCenter";
                case 84:
                    return "getNrModeToCheck";
                case 85:
                    return "getEcholocateMetrics";
                case 86:
                    return "getIsNrAvailable";
                case 87:
                    return "getHasNrSecondaryServingCell";
                case 88:
                    return "isUriFileExist";
                case 93:
                    return "setNrMode";
                case 94:
                    return "set5gIconDelayTimer";
                case 96:
                    return "getOemSpn";
                case 97:
                    return "getCellIdentity";
                case 99:
                    return "getPreferredDefaultNetMode";
                case 100:
                    return "getNewPreferredNetworkMode";
                case 101:
                    return "getIccCardType";
                case 102:
                    return "getPreferSubId";
                case 103:
                    return "lockCellAction";
                case 104:
                    return "rsrpBackoff";
                case 105:
                    return "resetRsrpBackoff";
                case 106:
                    return "barCell";
                case 107:
                    return "resetBarCell";
                case 108:
                    return "isApnInException";
                case 109:
                    return "cleanApnState";
                case 112:
                    return "getOrigSignalStrength";
                case 115:
                    return "prioritizeDefaultDataSubscription";
                case 116:
                    return "enableCellularDataPrio";
                case 117:
                    return "disableCellularDataPrio";
                case 118:
                    return "setGameAccelerateEnable";
                case 119:
                    return "isGameAccelerateEnable";
                case 120:
                    return "simProfileRefresh";
                case 121:
                    return "updateSmartDdsSwitch";
                case 123:
                    return "getDataNetworkInternetCid";
                case 124:
                    return "setPreferDdsSwitchPhoneId";
                case 125:
                    return "getPreferDdsSwitchPhoneId";
                case 126:
                    return "getReceiveMmsTime";
                case 127:
                    return "getReceiveSmsTime";
                case 128:
                    return "getSendSmsTime";
                case 129:
                    return "getSendSmsMapSize";
                case 130:
                    return "getReceiveSmsTimebyPhoneId";
                case 131:
                    return "isDataConnectivityPossible";
                case 132:
                    return "sendDdsSwitchEvent";
                case 133:
                    return "isBackOffSaEnabled";
                case 134:
                    return "isInNsa";
                case 135:
                    return "getNsaSignalStrength";
                case 136:
                    return "setGameErrorCauseToCenter";
                case 137:
                    return "getCurrentDelayLevel";
                case 138:
                    return "setCurrentDelayLevel";
                case 139:
                    return "resetImsRegTimer";
                case 140:
                    return "registerForImsRegTime";
                case 141:
                    return "unregisterForImsRegTime";
                case 142:
                    return "triggerImsReregStatus";
                case 143:
                    return "isRequestingMms";
                case 144:
                    return "getReleaseMmsNetworkTime";
                case 145:
                    return "setSaSilenceMode";
                case 146:
                    return "setNwCongestionCfg";
                case 147:
                    return "setDataPrioLevel";
                case 148:
                    return "getDualPsEnableState";
                case 149:
                    return OlkConstants.FUN_REQUEST_DUALPS;
                case 150:
                    return OlkConstants.FUN_RELEASE_DUALPS;
                case 151:
                    return OlkConstants.FUN_IS_PRIMARY_CELLULAR;
                case 152:
                    return "setDualDataUserPreference";
                case 153:
                    return "getSaPrioState";
                case 167:
                    return "setSimlockOfflineLock";
                case 168:
                    return "getSimlockOfflineLock";
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusTelephonyExt.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusTelephonyExt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 2:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result = requestForTelephonyEvent(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 3:
                            String _arg02 = data.readString();
                            IOplusTelephonyExtCallback _arg12 = IOplusTelephonyExtCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerCallback(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 4:
                            IOplusTelephonyExtCallback _arg03 = IOplusTelephonyExtCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unRegisterCallback(_arg03);
                            reply.writeNoException();
                            return true;
                        case 5:
                            int _result2 = getSoftSimCardSlotId();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 6:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result3 = getOperatorNumericForData(_arg04);
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 7:
                            int _arg05 = data.readInt();
                            int _arg13 = data.readInt();
                            String _arg22 = data.readString();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            startMobileDataHongbaoPolicy(_arg05, _arg13, _arg22, _arg3);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg06 = data.readInt();
                            String _arg14 = data.readString();
                            String _arg23 = data.readString();
                            String _arg32 = data.readString();
                            String _arg4 = data.readString();
                            PendingIntent _arg5 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                            PendingIntent _arg6 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                            boolean _arg7 = data.readBoolean();
                            int _arg8 = data.readInt();
                            boolean _arg9 = data.readBoolean();
                            int _arg10 = data.readInt();
                            int _arg11 = data.readInt();
                            data.enforceNoDataAvail();
                            sendTextForSubscriberWithOptionsOem(_arg06, _arg14, _arg23, _arg32, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10, _arg11);
                            reply.writeNoException();
                            return true;
                        case 9:
                            int _arg07 = data.readInt();
                            String _arg15 = data.readString();
                            String _arg24 = data.readString();
                            String _arg33 = data.readString();
                            List<String> _arg42 = data.createStringArrayList();
                            List<PendingIntent> _arg52 = data.createTypedArrayList(PendingIntent.CREATOR);
                            List<PendingIntent> _arg62 = data.createTypedArrayList(PendingIntent.CREATOR);
                            boolean _arg72 = data.readBoolean();
                            int _arg82 = data.readInt();
                            boolean _arg92 = data.readBoolean();
                            int _arg102 = data.readInt();
                            int _arg112 = data.readInt();
                            data.enforceNoDataAvail();
                            sendMultipartTextForSubscriberWithOptionsOem(_arg07, _arg15, _arg24, _arg33, _arg42, _arg52, _arg62, _arg72, _arg82, _arg92, _arg102, _arg112);
                            reply.writeNoException();
                            return true;
                        case 10:
                            int _arg08 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result4 = getCardType(_arg08);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 34:
                        case 47:
                        case 60:
                        case 62:
                        case 81:
                        case 89:
                        case 90:
                        case 91:
                        case 92:
                        case 95:
                        case 98:
                        case 110:
                        case 111:
                        case 113:
                        case 114:
                        case 122:
                        case 154:
                        case 155:
                        case 156:
                        case 157:
                        case 158:
                        case 159:
                        case 160:
                        case 161:
                        case 162:
                        case UAHPerfConstants.UAH_EVENT_CAMERA_FRONT_NIGHT_CAPTURE /* 163 */:
                        case UAHPerfConstants.UAH_EVENT_CAMERA_NIGHT_CAPTURE /* 164 */:
                        case 165:
                        case 166:
                        default:
                            return super.onTransact(code, data, reply, flags);
                        case 17:
                            int _arg09 = data.readInt();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            sendRecoveryRequest(_arg09, _arg16);
                            reply.writeNoException();
                            return true;
                        case 18:
                            int _arg010 = data.readInt();
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result5 = getActionExecuteTime(_arg010, _arg17);
                            reply.writeNoException();
                            reply.writeLong(_result5);
                            return true;
                        case 19:
                            int _arg011 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result6 = getLastAction(_arg011);
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 20:
                            String _arg012 = data.readString();
                            data.enforceNoDataAvail();
                            reportNetWorkLatency(_arg012);
                            reply.writeNoException();
                            return true;
                        case 21:
                            String _arg013 = data.readString();
                            data.enforceNoDataAvail();
                            reportNetWorkLevel(_arg013);
                            reply.writeNoException();
                            return true;
                        case 22:
                            int _arg014 = data.readInt();
                            String _arg18 = data.readString();
                            boolean _arg25 = data.readBoolean();
                            data.enforceNoDataAvail();
                            reportGameEnterOrLeave(_arg014, _arg18, _arg25);
                            reply.writeNoException();
                            return true;
                        case 23:
                            String _arg015 = data.readString();
                            int _arg19 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result7 = setDisplayNumberExt(_arg015, _arg19);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 24:
                            int _arg016 = data.readInt();
                            data.enforceNoDataAvail();
                            String[] _result8 = getLteCdmaImsi(_arg016);
                            reply.writeNoException();
                            reply.writeStringArray(_result8);
                            return true;
                        case 25:
                            boolean _arg017 = data.readBoolean();
                            String _arg110 = data.readString();
                            String _arg26 = data.readString();
                            data.enforceNoDataAvail();
                            setViceCardGameMode(_arg017, _arg110, _arg26);
                            reply.writeNoException();
                            return true;
                        case 26:
                            int _arg018 = data.readInt();
                            data.enforceNoDataAvail();
                            changeEsimStatus(_arg018);
                            reply.writeNoException();
                            return true;
                        case 27:
                            int _arg019 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result9 = getFullIccId(_arg019);
                            reply.writeNoException();
                            reply.writeString(_result9);
                            return true;
                        case 28:
                            int _arg020 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result10 = getCellInfo(_arg020);
                            reply.writeNoException();
                            reply.writeTypedObject(_result10, 1);
                            return true;
                        case 29:
                            int _arg021 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result11 = getCellIdentityOperator(_arg021);
                            reply.writeNoException();
                            reply.writeString(_result11);
                            return true;
                        case 30:
                            boolean _result12 = getRoamingReduction();
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 31:
                            int _arg022 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result13 = isImsUseEnabled(_arg022);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 32:
                            int _arg023 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result14 = isImsValid(_arg023);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 33:
                            int _arg024 = data.readInt();
                            int _arg111 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result15 = getCurrentTransport(_arg024, _arg111);
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 35:
                            int _arg025 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result16 = getFiveGUperLayerIndAvailable(_arg025);
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 36:
                            int _arg026 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result17 = getNrBearerAllocation(_arg026);
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 37:
                            boolean _result18 = getNrConnect();
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 38:
                            String _arg027 = data.readString();
                            data.enforceNoDataAvail();
                            String _result19 = getNetworkConfig(_arg027);
                            reply.writeNoException();
                            reply.writeString(_result19);
                            return true;
                        case 39:
                            String _result20 = getTelephonyPowerState();
                            reply.writeNoException();
                            reply.writeString(_result20);
                            return true;
                        case 40:
                            double _result21 = getTelephonyPowerLost();
                            reply.writeNoException();
                            reply.writeDouble(_result21);
                            return true;
                        case 41:
                            long[] _result22 = getModemTxTime();
                            reply.writeNoException();
                            reply.writeLongArray(_result22);
                            return true;
                        case 42:
                            boolean _result23 = isAlreadyUpdated();
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 43:
                            int _arg028 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result24 = getDataCallListAction(_arg028);
                            reply.writeNoException();
                            reply.writeBoolean(_result24);
                            return true;
                        case 44:
                            int _arg029 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result25 = disableEndc(_arg029);
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 45:
                            int _arg030 = data.readInt();
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result26 = setPreferredNetworkType(_arg030, _arg113);
                            reply.writeNoException();
                            reply.writeBoolean(_result26);
                            return true;
                        case 46:
                            int _arg031 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result27 = getPreferredNetworkType(_arg031);
                            reply.writeNoException();
                            reply.writeInt(_result27);
                            return true;
                        case 48:
                            int _arg032 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result28 = cleanupConnections(_arg032);
                            reply.writeNoException();
                            reply.writeBoolean(_result28);
                            return true;
                        case 49:
                            int _arg033 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result29 = getSaMode(_arg033);
                            reply.writeNoException();
                            reply.writeInt(_result29);
                            return true;
                        case 50:
                            int _arg034 = data.readInt();
                            int _arg114 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result30 = setSaMode(_arg034, _arg114);
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 51:
                            int _arg035 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result31 = psDetachAttachAction(_arg035);
                            reply.writeNoException();
                            reply.writeBoolean(_result31);
                            return true;
                        case 52:
                            int _arg036 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result32 = radioPower(_arg036);
                            reply.writeNoException();
                            reply.writeBoolean(_result32);
                            return true;
                        case 53:
                            int _arg037 = data.readInt();
                            long _arg115 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result33 = setCellBlackList(_arg037, _arg115);
                            reply.writeNoException();
                            reply.writeBoolean(_result33);
                            return true;
                        case 54:
                            int _arg038 = data.readInt();
                            long _arg116 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result34 = delCellBlacklist(_arg038, _arg116);
                            reply.writeNoException();
                            reply.writeBoolean(_result34);
                            return true;
                        case 55:
                            int _arg039 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result35 = clearCellBlacklist(_arg039);
                            reply.writeNoException();
                            reply.writeBoolean(_result35);
                            return true;
                        case 56:
                            int _arg040 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result36 = resetRadioSmooth(_arg040);
                            reply.writeNoException();
                            reply.writeBoolean(_result36);
                            return true;
                        case 57:
                            int _arg041 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result37 = isApnEnabled(_arg041);
                            reply.writeNoException();
                            reply.writeBoolean(_result37);
                            return true;
                        case 58:
                            int _arg042 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result38 = getRealNrState(_arg042);
                            reply.writeNoException();
                            reply.writeInt(_result38);
                            return true;
                        case 59:
                            int _arg043 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result39 = reregisterNetwork(_arg043);
                            reply.writeNoException();
                            reply.writeBoolean(_result39);
                            return true;
                        case 61:
                            int _arg044 = data.readInt();
                            int _arg117 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result40 = setSaLtePingpongState(_arg044, _arg117);
                            reply.writeNoException();
                            reply.writeBoolean(_result40);
                            return true;
                        case 63:
                            IBinder _result41 = getRemoteMessenger();
                            reply.writeNoException();
                            reply.writeStrongBinder(_result41);
                            return true;
                        case 64:
                            int _arg045 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result42 = getVoNrVisible(_arg045);
                            reply.writeNoException();
                            reply.writeBoolean(_result42);
                            return true;
                        case 65:
                            int _result43 = getUserSaMode();
                            reply.writeNoException();
                            reply.writeInt(_result43);
                            return true;
                        case 66:
                            int _arg046 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result44 = isInDelayOOSState(_arg046);
                            reply.writeNoException();
                            reply.writeBoolean(_result44);
                            return true;
                        case 67:
                            int _arg047 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result45 = getSubId(_arg047);
                            reply.writeNoException();
                            reply.writeInt(_result45);
                            return true;
                        case 68:
                            int _arg048 = data.readInt();
                            boolean _arg118 = data.readBoolean();
                            data.enforceNoDataAvail();
                            enableUiccApplications(_arg048, _arg118);
                            reply.writeNoException();
                            return true;
                        case 69:
                            boolean _result46 = canSupSlotSaSupport();
                            reply.writeNoException();
                            reply.writeBoolean(_result46);
                            return true;
                        case 70:
                            boolean _arg049 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSupSlotSaSupport(_arg049);
                            reply.writeNoException();
                            return true;
                        case 71:
                            boolean _result47 = getSupSlotSaSupport();
                            reply.writeNoException();
                            reply.writeBoolean(_result47);
                            return true;
                        case 72:
                            boolean _result48 = getTempDdsSwitch();
                            reply.writeNoException();
                            reply.writeBoolean(_result48);
                            return true;
                        case 73:
                            boolean _arg050 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setNrModeChangedAllow(_arg050);
                            reply.writeNoException();
                            return true;
                        case 74:
                            boolean _result49 = getNrModeChangedAllow();
                            reply.writeNoException();
                            reply.writeBoolean(_result49);
                            return true;
                        case 75:
                            Bundle _result50 = getNrModeChangedEvent();
                            reply.writeNoException();
                            reply.writeTypedObject(_result50, 1);
                            return true;
                        case 76:
                            IOplusGeoFenceEventCallBack _arg051 = IOplusGeoFenceEventCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            addGeoFenceEventCallBack(_arg051);
                            reply.writeNoException();
                            return true;
                        case 77:
                            IOplusGeoFenceEventCallBack _arg052 = IOplusGeoFenceEventCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            removeGeoFenceEventCallBack(_arg052);
                            reply.writeNoException();
                            return true;
                        case 78:
                            int _arg053 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result51 = getSelectConfig(_arg053);
                            reply.writeNoException();
                            reply.writeString(_result51);
                            return true;
                        case 79:
                            int _arg054 = data.readInt();
                            String _arg119 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result52 = setSelectConfig(_arg054, _arg119);
                            reply.writeNoException();
                            reply.writeBoolean(_result52);
                            return true;
                        case 80:
                            int _arg055 = data.readInt();
                            int _arg120 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result53 = setAllowedNetworkTypesBitmap(_arg055, _arg120);
                            reply.writeNoException();
                            reply.writeBoolean(_result53);
                            return true;
                        case 82:
                            String _arg056 = data.readString();
                            data.enforceNoDataAvail();
                            reportGameInfo(_arg056);
                            reply.writeNoException();
                            return true;
                        case 83:
                            String _result54 = reportGameErrorCauseToCenter();
                            reply.writeNoException();
                            reply.writeString(_result54);
                            return true;
                        case 84:
                            int _arg057 = data.readInt();
                            Bundle _arg121 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            getNrModeToCheck(_arg057, _arg121);
                            reply.writeNoException();
                            return true;
                        case 85:
                            int _arg058 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result55 = getEcholocateMetrics(_arg058);
                            reply.writeNoException();
                            reply.writeStringList(_result55);
                            return true;
                        case 86:
                            boolean _result56 = getIsNrAvailable();
                            reply.writeNoException();
                            reply.writeBoolean(_result56);
                            return true;
                        case 87:
                            boolean _result57 = getHasNrSecondaryServingCell();
                            reply.writeNoException();
                            reply.writeBoolean(_result57);
                            return true;
                        case 88:
                            String _arg059 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result58 = isUriFileExist(_arg059);
                            reply.writeNoException();
                            reply.writeBoolean(_result58);
                            return true;
                        case 93:
                            int _arg060 = data.readInt();
                            int _arg122 = data.readInt();
                            String _arg27 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result59 = setNrMode(_arg060, _arg122, _arg27);
                            reply.writeNoException();
                            reply.writeBoolean(_result59);
                            return true;
                        case 94:
                            int _arg061 = data.readInt();
                            int _arg123 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result60 = set5gIconDelayTimer(_arg061, _arg123);
                            reply.writeNoException();
                            reply.writeBoolean(_result60);
                            return true;
                        case 96:
                            int _arg062 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result61 = getOemSpn(_arg062);
                            reply.writeNoException();
                            reply.writeString(_result61);
                            return true;
                        case 97:
                            int _arg063 = data.readInt();
                            String _arg124 = data.readString();
                            String _arg28 = data.readString();
                            data.enforceNoDataAvail();
                            CellIdentity _result62 = getCellIdentity(_arg063, _arg124, _arg28);
                            reply.writeNoException();
                            reply.writeTypedObject(_result62, 1);
                            return true;
                        case 99:
                            int _arg064 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result63 = getPreferredDefaultNetMode(_arg064);
                            reply.writeNoException();
                            reply.writeInt(_result63);
                            return true;
                        case 100:
                            int _arg065 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result64 = getNewPreferredNetworkMode(_arg065);
                            reply.writeNoException();
                            reply.writeInt(_result64);
                            return true;
                        case 101:
                            int _arg066 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result65 = getIccCardType(_arg066);
                            reply.writeNoException();
                            reply.writeString(_result65);
                            return true;
                        case 102:
                            int _result66 = getPreferSubId();
                            reply.writeNoException();
                            reply.writeInt(_result66);
                            return true;
                        case 103:
                            int _arg067 = data.readInt();
                            String _arg125 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result67 = lockCellAction(_arg067, _arg125);
                            reply.writeNoException();
                            reply.writeBoolean(_result67);
                            return true;
                        case 104:
                            int _arg068 = data.readInt();
                            String _arg126 = data.readString();
                            int _arg29 = data.readInt();
                            int _arg34 = data.readInt();
                            int _arg43 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result68 = rsrpBackoff(_arg068, _arg126, _arg29, _arg34, _arg43);
                            reply.writeNoException();
                            reply.writeBoolean(_result68);
                            return true;
                        case 105:
                            int _arg069 = data.readInt();
                            String _arg127 = data.readString();
                            int _arg210 = data.readInt();
                            int _arg35 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result69 = resetRsrpBackoff(_arg069, _arg127, _arg210, _arg35);
                            reply.writeNoException();
                            reply.writeBoolean(_result69);
                            return true;
                        case 106:
                            int _arg070 = data.readInt();
                            int _arg128 = data.readInt();
                            int _arg211 = data.readInt();
                            int _arg36 = data.readInt();
                            long _arg44 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result70 = barCell(_arg070, _arg128, _arg211, _arg36, _arg44);
                            reply.writeNoException();
                            reply.writeBoolean(_result70);
                            return true;
                        case 107:
                            int _arg071 = data.readInt();
                            int _arg129 = data.readInt();
                            int _arg212 = data.readInt();
                            int _arg37 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result71 = resetBarCell(_arg071, _arg129, _arg212, _arg37);
                            reply.writeNoException();
                            reply.writeBoolean(_result71);
                            return true;
                        case 108:
                            int _arg072 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result72 = isApnInException(_arg072);
                            reply.writeNoException();
                            reply.writeBoolean(_result72);
                            return true;
                        case 109:
                            int _arg073 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result73 = cleanApnState(_arg073);
                            reply.writeNoException();
                            reply.writeBoolean(_result73);
                            return true;
                        case 112:
                            int _arg074 = data.readInt();
                            data.enforceNoDataAvail();
                            SignalStrength _result74 = getOrigSignalStrength(_arg074);
                            reply.writeNoException();
                            reply.writeTypedObject(_result74, 1);
                            return true;
                        case 115:
                            boolean _arg075 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result75 = prioritizeDefaultDataSubscription(_arg075);
                            reply.writeNoException();
                            reply.writeBoolean(_result75);
                            return true;
                        case 116:
                            String _arg076 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result76 = enableCellularDataPrio(_arg076);
                            reply.writeNoException();
                            reply.writeBoolean(_result76);
                            return true;
                        case 117:
                            String _arg077 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result77 = disableCellularDataPrio(_arg077);
                            reply.writeNoException();
                            reply.writeBoolean(_result77);
                            return true;
                        case 118:
                            boolean _arg078 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result78 = setGameAccelerateEnable(_arg078);
                            reply.writeNoException();
                            reply.writeBoolean(_result78);
                            return true;
                        case 119:
                            boolean _result79 = isGameAccelerateEnable();
                            reply.writeNoException();
                            reply.writeBoolean(_result79);
                            return true;
                        case 120:
                            int _arg079 = data.readInt();
                            boolean _arg130 = data.readBoolean();
                            int[] _arg213 = data.createIntArray();
                            data.enforceNoDataAvail();
                            simProfileRefresh(_arg079, _arg130, _arg213);
                            reply.writeNoException();
                            return true;
                        case 121:
                            boolean _arg080 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result80 = updateSmartDdsSwitch(_arg080);
                            reply.writeNoException();
                            reply.writeBoolean(_result80);
                            return true;
                        case 123:
                            int _arg081 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result81 = getDataNetworkInternetCid(_arg081);
                            reply.writeNoException();
                            reply.writeInt(_result81);
                            return true;
                        case 124:
                            int _arg082 = data.readInt();
                            data.enforceNoDataAvail();
                            setPreferDdsSwitchPhoneId(_arg082);
                            reply.writeNoException();
                            return true;
                        case 125:
                            int _result82 = getPreferDdsSwitchPhoneId();
                            reply.writeNoException();
                            reply.writeInt(_result82);
                            return true;
                        case 126:
                            int _arg083 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result83 = getReceiveMmsTime(_arg083);
                            reply.writeNoException();
                            reply.writeLong(_result83);
                            return true;
                        case 127:
                            long _result84 = getReceiveSmsTime();
                            reply.writeNoException();
                            reply.writeLong(_result84);
                            return true;
                        case 128:
                            long _result85 = getSendSmsTime();
                            reply.writeNoException();
                            reply.writeLong(_result85);
                            return true;
                        case 129:
                            int _result86 = getSendSmsMapSize();
                            reply.writeNoException();
                            reply.writeInt(_result86);
                            return true;
                        case 130:
                            int _arg084 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result87 = getReceiveSmsTimebyPhoneId(_arg084);
                            reply.writeNoException();
                            reply.writeLong(_result87);
                            return true;
                        case 131:
                            int _arg085 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result88 = isDataConnectivityPossible(_arg085);
                            reply.writeNoException();
                            reply.writeBoolean(_result88);
                            return true;
                        case 132:
                            sendDdsSwitchEvent();
                            reply.writeNoException();
                            return true;
                        case 133:
                            boolean _result89 = isBackOffSaEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result89);
                            return true;
                        case 134:
                            boolean _result90 = isInNsa();
                            reply.writeNoException();
                            reply.writeBoolean(_result90);
                            return true;
                        case 135:
                            int[] _result91 = getNsaSignalStrength();
                            reply.writeNoException();
                            reply.writeIntArray(_result91);
                            return true;
                        case 136:
                            String _arg086 = data.readString();
                            data.enforceNoDataAvail();
                            setGameErrorCauseToCenter(_arg086);
                            reply.writeNoException();
                            return true;
                        case 137:
                            int _result92 = getCurrentDelayLevel();
                            reply.writeNoException();
                            reply.writeInt(_result92);
                            return true;
                        case 138:
                            int _arg087 = data.readInt();
                            data.enforceNoDataAvail();
                            setCurrentDelayLevel(_arg087);
                            reply.writeNoException();
                            return true;
                        case 139:
                            int _arg088 = data.readInt();
                            int _arg131 = data.readInt();
                            data.enforceNoDataAvail();
                            resetImsRegTimer(_arg088, _arg131);
                            reply.writeNoException();
                            return true;
                        case 140:
                            int _arg089 = data.readInt();
                            data.enforceNoDataAvail();
                            registerForImsRegTime(_arg089);
                            reply.writeNoException();
                            return true;
                        case 141:
                            int _arg090 = data.readInt();
                            data.enforceNoDataAvail();
                            unregisterForImsRegTime(_arg090);
                            reply.writeNoException();
                            return true;
                        case 142:
                            int _arg091 = data.readInt();
                            int _arg132 = data.readInt();
                            data.enforceNoDataAvail();
                            triggerImsReregStatus(_arg091, _arg132);
                            reply.writeNoException();
                            return true;
                        case 143:
                            boolean _result93 = isRequestingMms();
                            reply.writeNoException();
                            reply.writeBoolean(_result93);
                            return true;
                        case 144:
                            long _result94 = getReleaseMmsNetworkTime();
                            reply.writeNoException();
                            reply.writeLong(_result94);
                            return true;
                        case 145:
                            int _arg092 = data.readInt();
                            boolean _arg133 = data.readBoolean();
                            int _arg214 = data.readInt();
                            int _arg38 = data.readInt();
                            String _arg45 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result95 = setSaSilenceMode(_arg092, _arg133, _arg214, _arg38, _arg45);
                            reply.writeNoException();
                            reply.writeBoolean(_result95);
                            return true;
                        case 146:
                            int _arg093 = data.readInt();
                            int _arg134 = data.readInt();
                            byte[] _arg215 = data.createByteArray();
                            int _arg39 = data.readInt();
                            data.enforceNoDataAvail();
                            setNwCongestionCfg(_arg093, _arg134, _arg215, _arg39);
                            reply.writeNoException();
                            return true;
                        case 147:
                            long _arg094 = data.readLong();
                            long _arg135 = data.readLong();
                            long _arg216 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result96 = setDataPrioLevel(_arg094, _arg135, _arg216);
                            reply.writeNoException();
                            reply.writeBoolean(_result96);
                            return true;
                        case 148:
                            int _result97 = getDualPsEnableState();
                            reply.writeNoException();
                            reply.writeInt(_result97);
                            return true;
                        case 149:
                            String _arg095 = data.readString();
                            data.enforceNoDataAvail();
                            requestDualPsNetwork(_arg095);
                            reply.writeNoException();
                            return true;
                        case 150:
                            String _arg096 = data.readString();
                            data.enforceNoDataAvail();
                            releaseDualPsNetwork(_arg096);
                            reply.writeNoException();
                            return true;
                        case 151:
                            Network _arg097 = (Network) data.readTypedObject(Network.CREATOR);
                            data.enforceNoDataAvail();
                            int _result98 = isPrimaryCellularNetwork(_arg097);
                            reply.writeNoException();
                            reply.writeInt(_result98);
                            return true;
                        case 152:
                            boolean _arg098 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setDualDataUserPreference(_arg098);
                            reply.writeNoException();
                            return true;
                        case 153:
                            int _arg099 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result99 = getSaPrioState(_arg099);
                            reply.writeNoException();
                            reply.writeBoolean(_result99);
                            return true;
                        case 167:
                            int _arg0100 = data.readInt();
                            boolean _arg136 = data.readBoolean();
                            int _arg217 = data.readInt();
                            int[] _arg310 = data.createIntArray();
                            long _arg46 = data.readLong();
                            data.enforceNoDataAvail();
                            setSimlockOfflineLock(_arg0100, _arg136, _arg217, _arg310, _arg46);
                            reply.writeNoException();
                            return true;
                        case 168:
                            int _arg0101 = data.readInt();
                            data.enforceNoDataAvail();
                            int[] _result100 = getSimlockOfflineLock(_arg0101);
                            reply.writeNoException();
                            reply.writeIntArray(_result100);
                            return true;
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusTelephonyExt {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusTelephonyExt.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public Bundle requestForTelephonyEvent(int slotId, int eventId, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(eventId);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void registerCallback(String packageName, IOplusTelephonyExtCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void unRegisterCallback(IOplusTelephonyExtCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getSoftSimCardSlotId() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getOperatorNumericForData(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void startMobileDataHongbaoPolicy(int time1, int time2, String value1, String value2) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(time1);
                    _data.writeInt(time2);
                    _data.writeString(value1);
                    _data.writeString(value2);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void sendTextForSubscriberWithOptionsOem(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod, int encodingType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(destAddr);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(scAddr);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(text);
                    try {
                        _data.writeTypedObject(sentIntent, 0);
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeTypedObject(deliveryIntent, 0);
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeBoolean(persistMessageForNonDefaultSmsApp);
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(priority);
                    try {
                        _data.writeBoolean(expectMore);
                    } catch (Throwable th8) {
                        th = th8;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(validityPeriod);
                        try {
                            _data.writeInt(encodingType);
                        } catch (Throwable th9) {
                            th = th9;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        this.mRemote.transact(8, _data, _reply, 0);
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th11) {
                        th = th11;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th12) {
                    th = th12;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void sendMultipartTextForSubscriberWithOptionsOem(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod, int encodingType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(destinationAddress);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(scAddress);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeStringList(parts);
                    try {
                        _data.writeTypedList(sentIntents, 0);
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeTypedList(deliveryIntents, 0);
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeBoolean(persistMessageForNonDefaultSmsApp);
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(priority);
                    try {
                        _data.writeBoolean(expectMore);
                    } catch (Throwable th8) {
                        th = th8;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(validityPeriod);
                        try {
                            _data.writeInt(encodingType);
                        } catch (Throwable th9) {
                            th = th9;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        this.mRemote.transact(9, _data, _reply, 0);
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th11) {
                        th = th11;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th12) {
                    th = th12;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getCardType(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void sendRecoveryRequest(int slotIndex, int action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(action);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public long getActionExecuteTime(int slotIndex, int action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(action);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getLastAction(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void reportNetWorkLatency(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void reportNetWorkLevel(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void reportGameEnterOrLeave(int gameUid, String gamePackageName, boolean enter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(gameUid);
                    _data.writeString(gamePackageName);
                    _data.writeBoolean(enter);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int setDisplayNumberExt(String number, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(number);
                    _data.writeInt(subId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String[] getLteCdmaImsi(int phoneid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneid);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setViceCardGameMode(boolean enabled, String gamePkgName, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    _data.writeString(gamePkgName);
                    _data.writeString(value);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void changeEsimStatus(int newState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(newState);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getFullIccId(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public Bundle getCellInfo(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getCellIdentityOperator(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getRoamingReduction() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isImsUseEnabled(int soltId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(soltId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isImsValid(int soltId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(soltId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getCurrentTransport(int soltId, int apnType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(soltId);
                    _data.writeInt(apnType);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getFiveGUperLayerIndAvailable(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getNrBearerAllocation(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getNrConnect() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getNetworkConfig(String key) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getTelephonyPowerState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public double getTelephonyPowerLost() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    double _result = _reply.readDouble();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public long[] getModemTxTime() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isAlreadyUpdated() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getDataCallListAction(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean disableEndc(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setPreferredNetworkType(int slotId, int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(networkType);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getPreferredNetworkType(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean cleanupConnections(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getSaMode(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setSaMode(int slotId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(mode);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean psDetachAttachAction(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean radioPower(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setCellBlackList(int type, long cellid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeLong(cellid);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean delCellBlacklist(int type, long cellid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeLong(cellid);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean clearCellBlacklist(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean resetRadioSmooth(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isApnEnabled(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getRealNrState(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean reregisterNetwork(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setSaLtePingpongState(int slotId, int isLteNrPingPong) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isLteNrPingPong);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public IBinder getRemoteMessenger() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getVoNrVisible(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getUserSaMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isInDelayOOSState(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getSubId(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void enableUiccApplications(int subId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean canSupSlotSaSupport() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setSupSlotSaSupport(boolean support) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeBoolean(support);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getSupSlotSaSupport() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getTempDdsSwitch() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setNrModeChangedAllow(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getNrModeChangedAllow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public Bundle getNrModeChangedEvent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void addGeoFenceEventCallBack(IOplusGeoFenceEventCallBack cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void removeGeoFenceEventCallBack(IOplusGeoFenceEventCallBack cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getSelectConfig(int soltId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(soltId);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setSelectConfig(int phoneId, String configId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(configId);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setAllowedNetworkTypesBitmap(int slotId, int networkTypeBitmap) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(networkTypeBitmap);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void reportGameInfo(String gameInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(gameInfo);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String reportGameErrorCauseToCenter() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void getNrModeToCheck(int phoneId, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public List<String> getEcholocateMetrics(int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(index);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getIsNrAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getHasNrSecondaryServingCell() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isUriFileExist(String vUri) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(vUri);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setNrMode(int slotId, int mode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(mode);
                    _data.writeString(caller);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean set5gIconDelayTimer(int slotId, int delayTimer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(delayTimer);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getOemSpn(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public CellIdentity getCellIdentity(int slotId, String callingPackage, String callingFeatureId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(callingPackage);
                    _data.writeString(callingFeatureId);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    CellIdentity _result = (CellIdentity) _reply.readTypedObject(CellIdentity.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getPreferredDefaultNetMode(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getNewPreferredNetworkMode(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public String getIccCardType(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getPreferSubId() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean lockCellAction(int slotId, String cmdStr) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(cmdStr);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean rsrpBackoff(int phoneId, String plmn, int arfch, int pci, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(plmn);
                    _data.writeInt(arfch);
                    _data.writeInt(pci);
                    _data.writeInt(offset);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean resetRsrpBackoff(int phoneId, String plmn, int arfch, int pci) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(plmn);
                    _data.writeInt(arfch);
                    _data.writeInt(pci);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean barCell(int phoneId, int rat, int arfch, int pci, long barTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(rat);
                    _data.writeInt(arfch);
                    _data.writeInt(pci);
                    _data.writeLong(barTime);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean resetBarCell(int phoneId, int rat, int arfch, int pci) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(rat);
                    _data.writeInt(arfch);
                    _data.writeInt(pci);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isApnInException(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean cleanApnState(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public SignalStrength getOrigSignalStrength(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                    SignalStrength _result = (SignalStrength) _reply.readTypedObject(SignalStrength.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean prioritizeDefaultDataSubscription(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean enableCellularDataPrio(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean disableCellularDataPrio(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setGameAccelerateEnable(boolean b) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeBoolean(b);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isGameAccelerateEnable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void simProfileRefresh(int slotId, boolean fileChange, int[] fileList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeBoolean(fileChange);
                    _data.writeIntArray(fileList);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean updateSmartDdsSwitch(boolean changeNext) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeBoolean(changeNext);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getDataNetworkInternetCid(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setPreferDdsSwitchPhoneId(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getPreferDdsSwitchPhoneId() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public long getReceiveMmsTime(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public long getReceiveSmsTime() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public long getSendSmsTime() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getSendSmsMapSize() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public long getReceiveSmsTimebyPhoneId(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isDataConnectivityPossible(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void sendDdsSwitchEvent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isBackOffSaEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isInNsa() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int[] getNsaSignalStrength() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setGameErrorCauseToCenter(String gameErrorCause) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(gameErrorCause);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getCurrentDelayLevel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setCurrentDelayLevel(int delayLevel) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(delayLevel);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void resetImsRegTimer(int phoneId, int regTimer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(regTimer);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void registerForImsRegTime(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void unregisterForImsRegTime(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void triggerImsReregStatus(int phoneId, int action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(action);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean isRequestingMms() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public long getReleaseMmsNetworkTime() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setSaSilenceMode(int phoneId, boolean enable, int module, int priority, String event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(enable);
                    _data.writeInt(module);
                    _data.writeInt(priority);
                    _data.writeString(event);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setNwCongestionCfg(int slotId, int cmd, byte[] data, int datalen) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(cmd);
                    _data.writeByteArray(data);
                    _data.writeInt(datalen);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean setDataPrioLevel(long rat, long uplink, long downlink) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeLong(rat);
                    _data.writeLong(uplink);
                    _data.writeLong(downlink);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int getDualPsEnableState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void requestDualPsNetwork(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void releaseDualPsNetwork(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int isPrimaryCellularNetwork(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeTypedObject(network, 0);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setDualDataUserPreference(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public boolean getSaPrioState(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public void setSimlockOfflineLock(int slotId, boolean enabled, int time, int[] remind, long serverTimestamp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeBoolean(enabled);
                    _data.writeInt(time);
                    _data.writeIntArray(remind);
                    _data.writeLong(serverTimestamp);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExt
            public int[] getSimlockOfflineLock(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 167;
        }
    }
}
