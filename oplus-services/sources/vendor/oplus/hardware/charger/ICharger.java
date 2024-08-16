package vendor.oplus.hardware.charger;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ICharger extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$charger$ICharger".replace('$', '.');
    public static final String HASH = "587d614e5ed419609cd450643820624cf151d465";
    public static final int VERSION = 3;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements ICharger {
        @Override // vendor.oplus.hardware.charger.ICharger
        public int VolDividerIcWorkModeSet(String str) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int chgExchangeMesgInit() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int chgExchangeSohMesgInit() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getAcType() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattAuthenticate() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getBattGaugeInfo() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattPPSChgIng() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattPPSChgPower() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getBattParamNoplug() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattShortIcOtpStatus() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattSubCurrent() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattVoocChgIng() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBatteryVoltageNow() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getBccCsvData() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBccExpStatus() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getBmsHeatingRunningStatus() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBmsHeatingStatus() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getChargerControl() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerCoolDown() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerCriticalLog() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerIdVolt() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerLog() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargingModeInGsmCall() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getChgOlcConfig() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getCpVbatDeviation() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getCustomSelectChgMode() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getDevinfoFastchg() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getFastCharge() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getParallelChgMosTestResult() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyAcOnline() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryCC() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryCurrentNow() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryFcc() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryHmac() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryLevel() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryNotify() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryPchg() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryPchgResetCount() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryRm() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getPsyBatterySN() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryShortFeature() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryShortStatus() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getPsyBatteryStatus() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryTemp() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyChargeTech() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyFastChgType() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyInputCurrent() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyOtgOnline() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyOtgSwitch() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyPcPortOnline() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyQGVbatDeviation() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyTypeOrientation() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyUsbOnline() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyUsbStatus() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getPsyWirelessRX() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getPsyWirelessRxVersion() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getPsyWirelessTX() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getPsyWirelessTxVersion() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getQgVbatDeviation() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getQuickModeGain() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getReserveSocDebug() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getSmartChgMode() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getUIsohValue() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getUisohDebugParameterInfo() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getUsbInputCurrentNow() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getUsbPrimalType() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWiredOtgOnline() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessAdapterPower() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessCapacity() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessChargePumpEn() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessCurrentNow() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getWirelessDeviated() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessOnline() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessPenPresent() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessPtmcId() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessRXEnable() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessRealType() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String getWirelessTXEnable() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessUserSleepMode() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessVoltageNow() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String healthd_update_ui_soc_decimal() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int nightstandby(int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int notifyScreenStatus(int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String queryChargeInfo() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String queryWlsPencilInfo() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setBatteryLogPush(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setBobStatus(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargeEMMode(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerControl(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerCoolDown(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerCriticalLog(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerCycle(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerFactoryModeTest(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerLog(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargingModeInGsmCall(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChgOlcConfig(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChgRusConfig(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChgStatusToBcc(int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setCustomSelectChgMode(int i, boolean z) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setFastchgFwUpdate(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setPsyMmiChgEn(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setPsyOtgSwitch(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setPsySlowChgEn(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setReserveSocDebug(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setShipMode(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSmartChgMode(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSmartCoolDown(int i, int i2, String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSuperEnduranceCount(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSuperEnduranceStatus(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setTbattPwrOff(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setUisohDebugInfo(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setUsbPrimalType(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessChargePumpEn(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessFtmMode(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessIconDelay(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessIdtAdcTest(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessPenSoc(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessRXEnable(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessTXEnable(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessUserSleepMode(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWlsThirdPartitionInfo(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public testKitFeatureTestResult testKitFeatureTest(int i) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String testKitGetFeatureList() throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public String testKitGetFeatureName(int i) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int testKitGetFeatureNum() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int updateUiSohToPartion() throws RemoteException {
            return 0;
        }
    }

    int VolDividerIcWorkModeSet(String str) throws RemoteException;

    int chgExchangeMesgInit() throws RemoteException;

    int chgExchangeSohMesgInit() throws RemoteException;

    int getAcType() throws RemoteException;

    int getBattAuthenticate() throws RemoteException;

    String getBattGaugeInfo() throws RemoteException;

    int getBattPPSChgIng() throws RemoteException;

    int getBattPPSChgPower() throws RemoteException;

    String getBattParamNoplug() throws RemoteException;

    int getBattShortIcOtpStatus() throws RemoteException;

    int getBattSubCurrent() throws RemoteException;

    int getBattVoocChgIng() throws RemoteException;

    int getBatteryVoltageNow() throws RemoteException;

    String getBccCsvData() throws RemoteException;

    int getBccExpStatus() throws RemoteException;

    String getBmsHeatingRunningStatus() throws RemoteException;

    int getBmsHeatingStatus() throws RemoteException;

    String getChargerControl() throws RemoteException;

    int getChargerCoolDown() throws RemoteException;

    int getChargerCriticalLog() throws RemoteException;

    int getChargerIdVolt() throws RemoteException;

    int getChargerLog() throws RemoteException;

    int getChargingModeInGsmCall() throws RemoteException;

    String getChgOlcConfig() throws RemoteException;

    int getCpVbatDeviation() throws RemoteException;

    int getCustomSelectChgMode() throws RemoteException;

    String getDevinfoFastchg() throws RemoteException;

    int getFastCharge() throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    int getParallelChgMosTestResult() throws RemoteException;

    int getPsyAcOnline() throws RemoteException;

    int getPsyBatteryCC() throws RemoteException;

    int getPsyBatteryCurrentNow() throws RemoteException;

    int getPsyBatteryFcc() throws RemoteException;

    int getPsyBatteryHmac() throws RemoteException;

    int getPsyBatteryLevel() throws RemoteException;

    int getPsyBatteryNotify() throws RemoteException;

    int getPsyBatteryPchg() throws RemoteException;

    int getPsyBatteryPchgResetCount() throws RemoteException;

    int getPsyBatteryRm() throws RemoteException;

    String getPsyBatterySN() throws RemoteException;

    int getPsyBatteryShortFeature() throws RemoteException;

    int getPsyBatteryShortStatus() throws RemoteException;

    String getPsyBatteryStatus() throws RemoteException;

    int getPsyBatteryTemp() throws RemoteException;

    int getPsyChargeTech() throws RemoteException;

    int getPsyFastChgType() throws RemoteException;

    int getPsyInputCurrent() throws RemoteException;

    int getPsyOtgOnline() throws RemoteException;

    int getPsyOtgSwitch() throws RemoteException;

    int getPsyPcPortOnline() throws RemoteException;

    int getPsyQGVbatDeviation() throws RemoteException;

    int getPsyTypeOrientation() throws RemoteException;

    int getPsyUsbOnline() throws RemoteException;

    int getPsyUsbStatus() throws RemoteException;

    String getPsyWirelessRX() throws RemoteException;

    String getPsyWirelessRxVersion() throws RemoteException;

    String getPsyWirelessTX() throws RemoteException;

    String getPsyWirelessTxVersion() throws RemoteException;

    int getQgVbatDeviation() throws RemoteException;

    String getQuickModeGain() throws RemoteException;

    String getReserveSocDebug() throws RemoteException;

    int getSmartChgMode() throws RemoteException;

    int getUIsohValue() throws RemoteException;

    String getUisohDebugParameterInfo() throws RemoteException;

    int getUsbInputCurrentNow() throws RemoteException;

    int getUsbPrimalType() throws RemoteException;

    int getWiredOtgOnline() throws RemoteException;

    int getWirelessAdapterPower() throws RemoteException;

    int getWirelessCapacity() throws RemoteException;

    int getWirelessChargePumpEn() throws RemoteException;

    int getWirelessCurrentNow() throws RemoteException;

    String getWirelessDeviated() throws RemoteException;

    int getWirelessOnline() throws RemoteException;

    int getWirelessPenPresent() throws RemoteException;

    int getWirelessPtmcId() throws RemoteException;

    int getWirelessRXEnable() throws RemoteException;

    int getWirelessRealType() throws RemoteException;

    String getWirelessTXEnable() throws RemoteException;

    int getWirelessUserSleepMode() throws RemoteException;

    int getWirelessVoltageNow() throws RemoteException;

    String healthd_update_ui_soc_decimal() throws RemoteException;

    int nightstandby(int i) throws RemoteException;

    int notifyScreenStatus(int i) throws RemoteException;

    String queryChargeInfo() throws RemoteException;

    String queryWlsPencilInfo() throws RemoteException;

    int setBatteryLogPush(String str) throws RemoteException;

    int setBobStatus(String str) throws RemoteException;

    int setChargeEMMode(String str) throws RemoteException;

    int setChargerControl(String str) throws RemoteException;

    int setChargerCoolDown(String str) throws RemoteException;

    int setChargerCriticalLog(String str) throws RemoteException;

    int setChargerCycle(String str) throws RemoteException;

    int setChargerFactoryModeTest(String str) throws RemoteException;

    int setChargerLog(String str) throws RemoteException;

    int setChargingModeInGsmCall(String str) throws RemoteException;

    int setChgOlcConfig(String str) throws RemoteException;

    int setChgRusConfig(String str) throws RemoteException;

    int setChgStatusToBcc(int i) throws RemoteException;

    int setCustomSelectChgMode(int i, boolean z) throws RemoteException;

    int setFastchgFwUpdate(String str) throws RemoteException;

    int setPsyMmiChgEn(String str) throws RemoteException;

    int setPsyOtgSwitch(String str) throws RemoteException;

    int setPsySlowChgEn(String str) throws RemoteException;

    int setReserveSocDebug(String str) throws RemoteException;

    int setShipMode(String str) throws RemoteException;

    int setSmartChgMode(String str) throws RemoteException;

    int setSmartCoolDown(int i, int i2, String str) throws RemoteException;

    int setSuperEnduranceCount(String str) throws RemoteException;

    int setSuperEnduranceStatus(String str) throws RemoteException;

    int setTbattPwrOff(String str) throws RemoteException;

    int setUisohDebugInfo(String str) throws RemoteException;

    int setUsbPrimalType(String str) throws RemoteException;

    int setWirelessChargePumpEn(String str) throws RemoteException;

    int setWirelessFtmMode(String str) throws RemoteException;

    int setWirelessIconDelay(String str) throws RemoteException;

    int setWirelessIdtAdcTest(String str) throws RemoteException;

    int setWirelessPenSoc(String str) throws RemoteException;

    int setWirelessRXEnable(String str) throws RemoteException;

    int setWirelessTXEnable(String str) throws RemoteException;

    int setWirelessUserSleepMode(String str) throws RemoteException;

    int setWlsThirdPartitionInfo(String str) throws RemoteException;

    testKitFeatureTestResult testKitFeatureTest(int i) throws RemoteException;

    String testKitGetFeatureList() throws RemoteException;

    String testKitGetFeatureName(int i) throws RemoteException;

    int testKitGetFeatureNum() throws RemoteException;

    int updateUiSohToPartion() throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements ICharger {
        static final int TRANSACTION_VolDividerIcWorkModeSet = 1;
        static final int TRANSACTION_chgExchangeMesgInit = 2;
        static final int TRANSACTION_chgExchangeSohMesgInit = 3;
        static final int TRANSACTION_getAcType = 4;
        static final int TRANSACTION_getBattAuthenticate = 5;
        static final int TRANSACTION_getBattGaugeInfo = 126;
        static final int TRANSACTION_getBattPPSChgIng = 6;
        static final int TRANSACTION_getBattPPSChgPower = 7;
        static final int TRANSACTION_getBattParamNoplug = 8;
        static final int TRANSACTION_getBattShortIcOtpStatus = 9;
        static final int TRANSACTION_getBattSubCurrent = 10;
        static final int TRANSACTION_getBattVoocChgIng = 11;
        static final int TRANSACTION_getBatteryVoltageNow = 12;
        static final int TRANSACTION_getBccCsvData = 13;
        static final int TRANSACTION_getBccExpStatus = 14;
        static final int TRANSACTION_getBmsHeatingRunningStatus = 15;
        static final int TRANSACTION_getBmsHeatingStatus = 16;
        static final int TRANSACTION_getChargerControl = 17;
        static final int TRANSACTION_getChargerCoolDown = 18;
        static final int TRANSACTION_getChargerCriticalLog = 19;
        static final int TRANSACTION_getChargerIdVolt = 20;
        static final int TRANSACTION_getChargerLog = 21;
        static final int TRANSACTION_getChargingModeInGsmCall = 122;
        static final int TRANSACTION_getChgOlcConfig = 114;
        static final int TRANSACTION_getCpVbatDeviation = 120;
        static final int TRANSACTION_getCustomSelectChgMode = 22;
        static final int TRANSACTION_getDevinfoFastchg = 23;
        static final int TRANSACTION_getFastCharge = 24;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getParallelChgMosTestResult = 25;
        static final int TRANSACTION_getPsyAcOnline = 26;
        static final int TRANSACTION_getPsyBatteryCC = 27;
        static final int TRANSACTION_getPsyBatteryCurrentNow = 28;
        static final int TRANSACTION_getPsyBatteryFcc = 29;
        static final int TRANSACTION_getPsyBatteryHmac = 30;
        static final int TRANSACTION_getPsyBatteryLevel = 31;
        static final int TRANSACTION_getPsyBatteryNotify = 32;
        static final int TRANSACTION_getPsyBatteryPchg = 33;
        static final int TRANSACTION_getPsyBatteryPchgResetCount = 34;
        static final int TRANSACTION_getPsyBatteryRm = 35;
        static final int TRANSACTION_getPsyBatterySN = 125;
        static final int TRANSACTION_getPsyBatteryShortFeature = 36;
        static final int TRANSACTION_getPsyBatteryShortStatus = 37;
        static final int TRANSACTION_getPsyBatteryStatus = 38;
        static final int TRANSACTION_getPsyBatteryTemp = 39;
        static final int TRANSACTION_getPsyChargeTech = 40;
        static final int TRANSACTION_getPsyFastChgType = 41;
        static final int TRANSACTION_getPsyInputCurrent = 42;
        static final int TRANSACTION_getPsyOtgOnline = 43;
        static final int TRANSACTION_getPsyOtgSwitch = 44;
        static final int TRANSACTION_getPsyPcPortOnline = 45;
        static final int TRANSACTION_getPsyQGVbatDeviation = 46;
        static final int TRANSACTION_getPsyTypeOrientation = 47;
        static final int TRANSACTION_getPsyUsbOnline = 48;
        static final int TRANSACTION_getPsyUsbStatus = 49;
        static final int TRANSACTION_getPsyWirelessRX = 50;
        static final int TRANSACTION_getPsyWirelessRxVersion = 51;
        static final int TRANSACTION_getPsyWirelessTX = 52;
        static final int TRANSACTION_getPsyWirelessTxVersion = 53;
        static final int TRANSACTION_getQgVbatDeviation = 54;
        static final int TRANSACTION_getQuickModeGain = 55;
        static final int TRANSACTION_getReserveSocDebug = 56;
        static final int TRANSACTION_getSmartChgMode = 57;
        static final int TRANSACTION_getUIsohValue = 58;
        static final int TRANSACTION_getUisohDebugParameterInfo = 59;
        static final int TRANSACTION_getUsbInputCurrentNow = 60;
        static final int TRANSACTION_getUsbPrimalType = 61;
        static final int TRANSACTION_getWiredOtgOnline = 62;
        static final int TRANSACTION_getWirelessAdapterPower = 63;
        static final int TRANSACTION_getWirelessCapacity = 64;
        static final int TRANSACTION_getWirelessChargePumpEn = 65;
        static final int TRANSACTION_getWirelessCurrentNow = 66;
        static final int TRANSACTION_getWirelessDeviated = 67;
        static final int TRANSACTION_getWirelessOnline = 68;
        static final int TRANSACTION_getWirelessPenPresent = 69;
        static final int TRANSACTION_getWirelessPtmcId = 70;
        static final int TRANSACTION_getWirelessRXEnable = 71;
        static final int TRANSACTION_getWirelessRealType = 72;
        static final int TRANSACTION_getWirelessTXEnable = 73;
        static final int TRANSACTION_getWirelessUserSleepMode = 74;
        static final int TRANSACTION_getWirelessVoltageNow = 75;
        static final int TRANSACTION_healthd_update_ui_soc_decimal = 76;
        static final int TRANSACTION_nightstandby = 77;
        static final int TRANSACTION_notifyScreenStatus = 78;
        static final int TRANSACTION_queryChargeInfo = 79;
        static final int TRANSACTION_queryWlsPencilInfo = 113;
        static final int TRANSACTION_setBatteryLogPush = 121;
        static final int TRANSACTION_setBobStatus = 118;
        static final int TRANSACTION_setChargeEMMode = 80;
        static final int TRANSACTION_setChargerControl = 81;
        static final int TRANSACTION_setChargerCoolDown = 82;
        static final int TRANSACTION_setChargerCriticalLog = 83;
        static final int TRANSACTION_setChargerCycle = 84;
        static final int TRANSACTION_setChargerFactoryModeTest = 85;
        static final int TRANSACTION_setChargerLog = 86;
        static final int TRANSACTION_setChargingModeInGsmCall = 123;
        static final int TRANSACTION_setChgOlcConfig = 115;
        static final int TRANSACTION_setChgRusConfig = 124;
        static final int TRANSACTION_setChgStatusToBcc = 87;
        static final int TRANSACTION_setCustomSelectChgMode = 88;
        static final int TRANSACTION_setFastchgFwUpdate = 89;
        static final int TRANSACTION_setPsyMmiChgEn = 90;
        static final int TRANSACTION_setPsyOtgSwitch = 91;
        static final int TRANSACTION_setPsySlowChgEn = 119;
        static final int TRANSACTION_setReserveSocDebug = 92;
        static final int TRANSACTION_setShipMode = 93;
        static final int TRANSACTION_setSmartChgMode = 94;
        static final int TRANSACTION_setSmartCoolDown = 95;
        static final int TRANSACTION_setSuperEnduranceCount = 117;
        static final int TRANSACTION_setSuperEnduranceStatus = 116;
        static final int TRANSACTION_setTbattPwrOff = 96;
        static final int TRANSACTION_setUisohDebugInfo = 97;
        static final int TRANSACTION_setUsbPrimalType = 98;
        static final int TRANSACTION_setWirelessChargePumpEn = 99;
        static final int TRANSACTION_setWirelessFtmMode = 100;
        static final int TRANSACTION_setWirelessIconDelay = 101;
        static final int TRANSACTION_setWirelessIdtAdcTest = 102;
        static final int TRANSACTION_setWirelessPenSoc = 103;
        static final int TRANSACTION_setWirelessRXEnable = 104;
        static final int TRANSACTION_setWirelessTXEnable = 105;
        static final int TRANSACTION_setWirelessUserSleepMode = 106;
        static final int TRANSACTION_setWlsThirdPartitionInfo = 107;
        static final int TRANSACTION_testKitFeatureTest = 108;
        static final int TRANSACTION_testKitGetFeatureList = 109;
        static final int TRANSACTION_testKitGetFeatureName = 110;
        static final int TRANSACTION_testKitGetFeatureNum = 111;
        static final int TRANSACTION_updateUiSohToPartion = 112;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "VolDividerIcWorkModeSet";
                case 2:
                    return "chgExchangeMesgInit";
                case 3:
                    return "chgExchangeSohMesgInit";
                case 4:
                    return "getAcType";
                case 5:
                    return "getBattAuthenticate";
                case 6:
                    return "getBattPPSChgIng";
                case 7:
                    return "getBattPPSChgPower";
                case 8:
                    return "getBattParamNoplug";
                case 9:
                    return "getBattShortIcOtpStatus";
                case 10:
                    return "getBattSubCurrent";
                case 11:
                    return "getBattVoocChgIng";
                case 12:
                    return "getBatteryVoltageNow";
                case 13:
                    return "getBccCsvData";
                case 14:
                    return "getBccExpStatus";
                case 15:
                    return "getBmsHeatingRunningStatus";
                case 16:
                    return "getBmsHeatingStatus";
                case 17:
                    return "getChargerControl";
                case 18:
                    return "getChargerCoolDown";
                case 19:
                    return "getChargerCriticalLog";
                case 20:
                    return "getChargerIdVolt";
                case 21:
                    return "getChargerLog";
                case 22:
                    return "getCustomSelectChgMode";
                case 23:
                    return "getDevinfoFastchg";
                case 24:
                    return "getFastCharge";
                case 25:
                    return "getParallelChgMosTestResult";
                case 26:
                    return "getPsyAcOnline";
                case 27:
                    return "getPsyBatteryCC";
                case 28:
                    return "getPsyBatteryCurrentNow";
                case 29:
                    return "getPsyBatteryFcc";
                case 30:
                    return "getPsyBatteryHmac";
                case 31:
                    return "getPsyBatteryLevel";
                case 32:
                    return "getPsyBatteryNotify";
                case 33:
                    return "getPsyBatteryPchg";
                case 34:
                    return "getPsyBatteryPchgResetCount";
                case 35:
                    return "getPsyBatteryRm";
                case 36:
                    return "getPsyBatteryShortFeature";
                case 37:
                    return "getPsyBatteryShortStatus";
                case 38:
                    return "getPsyBatteryStatus";
                case 39:
                    return "getPsyBatteryTemp";
                case 40:
                    return "getPsyChargeTech";
                case 41:
                    return "getPsyFastChgType";
                case 42:
                    return "getPsyInputCurrent";
                case 43:
                    return "getPsyOtgOnline";
                case 44:
                    return "getPsyOtgSwitch";
                case 45:
                    return "getPsyPcPortOnline";
                case 46:
                    return "getPsyQGVbatDeviation";
                case 47:
                    return "getPsyTypeOrientation";
                case 48:
                    return "getPsyUsbOnline";
                case 49:
                    return "getPsyUsbStatus";
                case 50:
                    return "getPsyWirelessRX";
                case 51:
                    return "getPsyWirelessRxVersion";
                case 52:
                    return "getPsyWirelessTX";
                case 53:
                    return "getPsyWirelessTxVersion";
                case 54:
                    return "getQgVbatDeviation";
                case 55:
                    return "getQuickModeGain";
                case 56:
                    return "getReserveSocDebug";
                case 57:
                    return "getSmartChgMode";
                case 58:
                    return "getUIsohValue";
                case 59:
                    return "getUisohDebugParameterInfo";
                case 60:
                    return "getUsbInputCurrentNow";
                case 61:
                    return "getUsbPrimalType";
                case 62:
                    return "getWiredOtgOnline";
                case 63:
                    return "getWirelessAdapterPower";
                case 64:
                    return "getWirelessCapacity";
                case 65:
                    return "getWirelessChargePumpEn";
                case 66:
                    return "getWirelessCurrentNow";
                case 67:
                    return "getWirelessDeviated";
                case 68:
                    return "getWirelessOnline";
                case 69:
                    return "getWirelessPenPresent";
                case 70:
                    return "getWirelessPtmcId";
                case 71:
                    return "getWirelessRXEnable";
                case 72:
                    return "getWirelessRealType";
                case 73:
                    return "getWirelessTXEnable";
                case 74:
                    return "getWirelessUserSleepMode";
                case 75:
                    return "getWirelessVoltageNow";
                case 76:
                    return "healthd_update_ui_soc_decimal";
                case TRANSACTION_nightstandby /* 77 */:
                    return "nightstandby";
                case TRANSACTION_notifyScreenStatus /* 78 */:
                    return "notifyScreenStatus";
                case TRANSACTION_queryChargeInfo /* 79 */:
                    return "queryChargeInfo";
                case 80:
                    return "setChargeEMMode";
                case 81:
                    return "setChargerControl";
                case 82:
                    return "setChargerCoolDown";
                case TRANSACTION_setChargerCriticalLog /* 83 */:
                    return "setChargerCriticalLog";
                case TRANSACTION_setChargerCycle /* 84 */:
                    return "setChargerCycle";
                case TRANSACTION_setChargerFactoryModeTest /* 85 */:
                    return "setChargerFactoryModeTest";
                case TRANSACTION_setChargerLog /* 86 */:
                    return "setChargerLog";
                case 87:
                    return "setChgStatusToBcc";
                case 88:
                    return "setCustomSelectChgMode";
                case TRANSACTION_setFastchgFwUpdate /* 89 */:
                    return "setFastchgFwUpdate";
                case 90:
                    return "setPsyMmiChgEn";
                case 91:
                    return "setPsyOtgSwitch";
                case 92:
                    return "setReserveSocDebug";
                case 93:
                    return "setShipMode";
                case TRANSACTION_setSmartChgMode /* 94 */:
                    return "setSmartChgMode";
                case TRANSACTION_setSmartCoolDown /* 95 */:
                    return "setSmartCoolDown";
                case TRANSACTION_setTbattPwrOff /* 96 */:
                    return "setTbattPwrOff";
                case TRANSACTION_setUisohDebugInfo /* 97 */:
                    return "setUisohDebugInfo";
                case 98:
                    return "setUsbPrimalType";
                case 99:
                    return "setWirelessChargePumpEn";
                case 100:
                    return "setWirelessFtmMode";
                case 101:
                    return "setWirelessIconDelay";
                case 102:
                    return "setWirelessIdtAdcTest";
                case 103:
                    return "setWirelessPenSoc";
                case 104:
                    return "setWirelessRXEnable";
                case 105:
                    return "setWirelessTXEnable";
                case 106:
                    return "setWirelessUserSleepMode";
                case 107:
                    return "setWlsThirdPartitionInfo";
                case 108:
                    return "testKitFeatureTest";
                case 109:
                    return "testKitGetFeatureList";
                case TRANSACTION_testKitGetFeatureName /* 110 */:
                    return "testKitGetFeatureName";
                case 111:
                    return "testKitGetFeatureNum";
                case 112:
                    return "updateUiSohToPartion";
                case 113:
                    return "queryWlsPencilInfo";
                case TRANSACTION_getChgOlcConfig /* 114 */:
                    return "getChgOlcConfig";
                case TRANSACTION_setChgOlcConfig /* 115 */:
                    return "setChgOlcConfig";
                case TRANSACTION_setSuperEnduranceStatus /* 116 */:
                    return "setSuperEnduranceStatus";
                case TRANSACTION_setSuperEnduranceCount /* 117 */:
                    return "setSuperEnduranceCount";
                case TRANSACTION_setBobStatus /* 118 */:
                    return "setBobStatus";
                case TRANSACTION_setPsySlowChgEn /* 119 */:
                    return "setPsySlowChgEn";
                case 120:
                    return "getCpVbatDeviation";
                case 121:
                    return "setBatteryLogPush";
                case 122:
                    return "getChargingModeInGsmCall";
                case 123:
                    return "setChargingModeInGsmCall";
                case 124:
                    return "setChgRusConfig";
                case 125:
                    return "getPsyBatterySN";
                case 126:
                    return "getBattGaugeInfo";
                default:
                    switch (i) {
                        case TRANSACTION_getInterfaceHash /* 16777214 */:
                            return "getInterfaceHash";
                        case TRANSACTION_getInterfaceVersion /* 16777215 */:
                            return "getInterfaceVersion";
                        default:
                            return null;
                    }
            }
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, ICharger.DESCRIPTOR);
        }

        public static ICharger asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ICharger.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof ICharger)) {
                return (ICharger) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = ICharger.DESCRIPTOR;
            if (i >= 1 && i <= TRANSACTION_getInterfaceVersion) {
                parcel.enforceInterface(str);
            }
            switch (i) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    parcel2.writeNoException();
                    parcel2.writeString(getInterfaceHash());
                    return true;
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    parcel2.writeNoException();
                    parcel2.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    parcel2.writeString(str);
                    return true;
                default:
                    switch (i) {
                        case 1:
                            String readString = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int VolDividerIcWorkModeSet = VolDividerIcWorkModeSet(readString);
                            parcel2.writeNoException();
                            parcel2.writeInt(VolDividerIcWorkModeSet);
                            return true;
                        case 2:
                            int chgExchangeMesgInit = chgExchangeMesgInit();
                            parcel2.writeNoException();
                            parcel2.writeInt(chgExchangeMesgInit);
                            return true;
                        case 3:
                            int chgExchangeSohMesgInit = chgExchangeSohMesgInit();
                            parcel2.writeNoException();
                            parcel2.writeInt(chgExchangeSohMesgInit);
                            return true;
                        case 4:
                            int acType = getAcType();
                            parcel2.writeNoException();
                            parcel2.writeInt(acType);
                            return true;
                        case 5:
                            int battAuthenticate = getBattAuthenticate();
                            parcel2.writeNoException();
                            parcel2.writeInt(battAuthenticate);
                            return true;
                        case 6:
                            int battPPSChgIng = getBattPPSChgIng();
                            parcel2.writeNoException();
                            parcel2.writeInt(battPPSChgIng);
                            return true;
                        case 7:
                            int battPPSChgPower = getBattPPSChgPower();
                            parcel2.writeNoException();
                            parcel2.writeInt(battPPSChgPower);
                            return true;
                        case 8:
                            String battParamNoplug = getBattParamNoplug();
                            parcel2.writeNoException();
                            parcel2.writeString(battParamNoplug);
                            return true;
                        case 9:
                            int battShortIcOtpStatus = getBattShortIcOtpStatus();
                            parcel2.writeNoException();
                            parcel2.writeInt(battShortIcOtpStatus);
                            return true;
                        case 10:
                            int battSubCurrent = getBattSubCurrent();
                            parcel2.writeNoException();
                            parcel2.writeInt(battSubCurrent);
                            return true;
                        case 11:
                            int battVoocChgIng = getBattVoocChgIng();
                            parcel2.writeNoException();
                            parcel2.writeInt(battVoocChgIng);
                            return true;
                        case 12:
                            int batteryVoltageNow = getBatteryVoltageNow();
                            parcel2.writeNoException();
                            parcel2.writeInt(batteryVoltageNow);
                            return true;
                        case 13:
                            String bccCsvData = getBccCsvData();
                            parcel2.writeNoException();
                            parcel2.writeString(bccCsvData);
                            return true;
                        case 14:
                            int bccExpStatus = getBccExpStatus();
                            parcel2.writeNoException();
                            parcel2.writeInt(bccExpStatus);
                            return true;
                        case 15:
                            String bmsHeatingRunningStatus = getBmsHeatingRunningStatus();
                            parcel2.writeNoException();
                            parcel2.writeString(bmsHeatingRunningStatus);
                            return true;
                        case 16:
                            int bmsHeatingStatus = getBmsHeatingStatus();
                            parcel2.writeNoException();
                            parcel2.writeInt(bmsHeatingStatus);
                            return true;
                        case 17:
                            String chargerControl = getChargerControl();
                            parcel2.writeNoException();
                            parcel2.writeString(chargerControl);
                            return true;
                        case 18:
                            int chargerCoolDown = getChargerCoolDown();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerCoolDown);
                            return true;
                        case 19:
                            int chargerCriticalLog = getChargerCriticalLog();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerCriticalLog);
                            return true;
                        case 20:
                            int chargerIdVolt = getChargerIdVolt();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerIdVolt);
                            return true;
                        case 21:
                            int chargerLog = getChargerLog();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerLog);
                            return true;
                        case 22:
                            int customSelectChgMode = getCustomSelectChgMode();
                            parcel2.writeNoException();
                            parcel2.writeInt(customSelectChgMode);
                            return true;
                        case 23:
                            String devinfoFastchg = getDevinfoFastchg();
                            parcel2.writeNoException();
                            parcel2.writeString(devinfoFastchg);
                            return true;
                        case 24:
                            int fastCharge = getFastCharge();
                            parcel2.writeNoException();
                            parcel2.writeInt(fastCharge);
                            return true;
                        case 25:
                            int parallelChgMosTestResult = getParallelChgMosTestResult();
                            parcel2.writeNoException();
                            parcel2.writeInt(parallelChgMosTestResult);
                            return true;
                        case 26:
                            int psyAcOnline = getPsyAcOnline();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyAcOnline);
                            return true;
                        case 27:
                            int psyBatteryCC = getPsyBatteryCC();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryCC);
                            return true;
                        case 28:
                            int psyBatteryCurrentNow = getPsyBatteryCurrentNow();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryCurrentNow);
                            return true;
                        case 29:
                            int psyBatteryFcc = getPsyBatteryFcc();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryFcc);
                            return true;
                        case 30:
                            int psyBatteryHmac = getPsyBatteryHmac();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryHmac);
                            return true;
                        case 31:
                            int psyBatteryLevel = getPsyBatteryLevel();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryLevel);
                            return true;
                        case 32:
                            int psyBatteryNotify = getPsyBatteryNotify();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryNotify);
                            return true;
                        case 33:
                            int psyBatteryPchg = getPsyBatteryPchg();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryPchg);
                            return true;
                        case 34:
                            int psyBatteryPchgResetCount = getPsyBatteryPchgResetCount();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryPchgResetCount);
                            return true;
                        case 35:
                            int psyBatteryRm = getPsyBatteryRm();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryRm);
                            return true;
                        case 36:
                            int psyBatteryShortFeature = getPsyBatteryShortFeature();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryShortFeature);
                            return true;
                        case 37:
                            int psyBatteryShortStatus = getPsyBatteryShortStatus();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryShortStatus);
                            return true;
                        case 38:
                            String psyBatteryStatus = getPsyBatteryStatus();
                            parcel2.writeNoException();
                            parcel2.writeString(psyBatteryStatus);
                            return true;
                        case 39:
                            int psyBatteryTemp = getPsyBatteryTemp();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyBatteryTemp);
                            return true;
                        case 40:
                            int psyChargeTech = getPsyChargeTech();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyChargeTech);
                            return true;
                        case 41:
                            int psyFastChgType = getPsyFastChgType();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyFastChgType);
                            return true;
                        case 42:
                            int psyInputCurrent = getPsyInputCurrent();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyInputCurrent);
                            return true;
                        case 43:
                            int psyOtgOnline = getPsyOtgOnline();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyOtgOnline);
                            return true;
                        case 44:
                            int psyOtgSwitch = getPsyOtgSwitch();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyOtgSwitch);
                            return true;
                        case 45:
                            int psyPcPortOnline = getPsyPcPortOnline();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyPcPortOnline);
                            return true;
                        case 46:
                            int psyQGVbatDeviation = getPsyQGVbatDeviation();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyQGVbatDeviation);
                            return true;
                        case 47:
                            int psyTypeOrientation = getPsyTypeOrientation();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyTypeOrientation);
                            return true;
                        case 48:
                            int psyUsbOnline = getPsyUsbOnline();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyUsbOnline);
                            return true;
                        case 49:
                            int psyUsbStatus = getPsyUsbStatus();
                            parcel2.writeNoException();
                            parcel2.writeInt(psyUsbStatus);
                            return true;
                        case 50:
                            String psyWirelessRX = getPsyWirelessRX();
                            parcel2.writeNoException();
                            parcel2.writeString(psyWirelessRX);
                            return true;
                        case 51:
                            String psyWirelessRxVersion = getPsyWirelessRxVersion();
                            parcel2.writeNoException();
                            parcel2.writeString(psyWirelessRxVersion);
                            return true;
                        case 52:
                            String psyWirelessTX = getPsyWirelessTX();
                            parcel2.writeNoException();
                            parcel2.writeString(psyWirelessTX);
                            return true;
                        case 53:
                            String psyWirelessTxVersion = getPsyWirelessTxVersion();
                            parcel2.writeNoException();
                            parcel2.writeString(psyWirelessTxVersion);
                            return true;
                        case 54:
                            int qgVbatDeviation = getQgVbatDeviation();
                            parcel2.writeNoException();
                            parcel2.writeInt(qgVbatDeviation);
                            return true;
                        case 55:
                            String quickModeGain = getQuickModeGain();
                            parcel2.writeNoException();
                            parcel2.writeString(quickModeGain);
                            return true;
                        case 56:
                            String reserveSocDebug = getReserveSocDebug();
                            parcel2.writeNoException();
                            parcel2.writeString(reserveSocDebug);
                            return true;
                        case 57:
                            int smartChgMode = getSmartChgMode();
                            parcel2.writeNoException();
                            parcel2.writeInt(smartChgMode);
                            return true;
                        case 58:
                            int uIsohValue = getUIsohValue();
                            parcel2.writeNoException();
                            parcel2.writeInt(uIsohValue);
                            return true;
                        case 59:
                            String uisohDebugParameterInfo = getUisohDebugParameterInfo();
                            parcel2.writeNoException();
                            parcel2.writeString(uisohDebugParameterInfo);
                            return true;
                        case 60:
                            int usbInputCurrentNow = getUsbInputCurrentNow();
                            parcel2.writeNoException();
                            parcel2.writeInt(usbInputCurrentNow);
                            return true;
                        case 61:
                            int usbPrimalType = getUsbPrimalType();
                            parcel2.writeNoException();
                            parcel2.writeInt(usbPrimalType);
                            return true;
                        case 62:
                            int wiredOtgOnline = getWiredOtgOnline();
                            parcel2.writeNoException();
                            parcel2.writeInt(wiredOtgOnline);
                            return true;
                        case 63:
                            int wirelessAdapterPower = getWirelessAdapterPower();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessAdapterPower);
                            return true;
                        case 64:
                            int wirelessCapacity = getWirelessCapacity();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessCapacity);
                            return true;
                        case 65:
                            int wirelessChargePumpEn = getWirelessChargePumpEn();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessChargePumpEn);
                            return true;
                        case 66:
                            int wirelessCurrentNow = getWirelessCurrentNow();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessCurrentNow);
                            return true;
                        case 67:
                            String wirelessDeviated = getWirelessDeviated();
                            parcel2.writeNoException();
                            parcel2.writeString(wirelessDeviated);
                            return true;
                        case 68:
                            int wirelessOnline = getWirelessOnline();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessOnline);
                            return true;
                        case 69:
                            int wirelessPenPresent = getWirelessPenPresent();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessPenPresent);
                            return true;
                        case 70:
                            int wirelessPtmcId = getWirelessPtmcId();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessPtmcId);
                            return true;
                        case 71:
                            int wirelessRXEnable = getWirelessRXEnable();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessRXEnable);
                            return true;
                        case 72:
                            int wirelessRealType = getWirelessRealType();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessRealType);
                            return true;
                        case 73:
                            String wirelessTXEnable = getWirelessTXEnable();
                            parcel2.writeNoException();
                            parcel2.writeString(wirelessTXEnable);
                            return true;
                        case 74:
                            int wirelessUserSleepMode = getWirelessUserSleepMode();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessUserSleepMode);
                            return true;
                        case 75:
                            int wirelessVoltageNow = getWirelessVoltageNow();
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessVoltageNow);
                            return true;
                        case 76:
                            String healthd_update_ui_soc_decimal = healthd_update_ui_soc_decimal();
                            parcel2.writeNoException();
                            parcel2.writeString(healthd_update_ui_soc_decimal);
                            return true;
                        case TRANSACTION_nightstandby /* 77 */:
                            int readInt = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int nightstandby = nightstandby(readInt);
                            parcel2.writeNoException();
                            parcel2.writeInt(nightstandby);
                            return true;
                        case TRANSACTION_notifyScreenStatus /* 78 */:
                            int readInt2 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int notifyScreenStatus = notifyScreenStatus(readInt2);
                            parcel2.writeNoException();
                            parcel2.writeInt(notifyScreenStatus);
                            return true;
                        case TRANSACTION_queryChargeInfo /* 79 */:
                            String queryChargeInfo = queryChargeInfo();
                            parcel2.writeNoException();
                            parcel2.writeString(queryChargeInfo);
                            return true;
                        case 80:
                            String readString2 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargeEMMode = setChargeEMMode(readString2);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargeEMMode);
                            return true;
                        case 81:
                            String readString3 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargerControl2 = setChargerControl(readString3);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerControl2);
                            return true;
                        case 82:
                            String readString4 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargerCoolDown2 = setChargerCoolDown(readString4);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerCoolDown2);
                            return true;
                        case TRANSACTION_setChargerCriticalLog /* 83 */:
                            String readString5 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargerCriticalLog2 = setChargerCriticalLog(readString5);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerCriticalLog2);
                            return true;
                        case TRANSACTION_setChargerCycle /* 84 */:
                            String readString6 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargerCycle = setChargerCycle(readString6);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerCycle);
                            return true;
                        case TRANSACTION_setChargerFactoryModeTest /* 85 */:
                            String readString7 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargerFactoryModeTest = setChargerFactoryModeTest(readString7);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerFactoryModeTest);
                            return true;
                        case TRANSACTION_setChargerLog /* 86 */:
                            String readString8 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargerLog2 = setChargerLog(readString8);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargerLog2);
                            return true;
                        case 87:
                            int readInt3 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int chgStatusToBcc = setChgStatusToBcc(readInt3);
                            parcel2.writeNoException();
                            parcel2.writeInt(chgStatusToBcc);
                            return true;
                        case 88:
                            int readInt4 = parcel.readInt();
                            boolean readBoolean = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            int customSelectChgMode2 = setCustomSelectChgMode(readInt4, readBoolean);
                            parcel2.writeNoException();
                            parcel2.writeInt(customSelectChgMode2);
                            return true;
                        case TRANSACTION_setFastchgFwUpdate /* 89 */:
                            String readString9 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int fastchgFwUpdate = setFastchgFwUpdate(readString9);
                            parcel2.writeNoException();
                            parcel2.writeInt(fastchgFwUpdate);
                            return true;
                        case 90:
                            String readString10 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int psyMmiChgEn = setPsyMmiChgEn(readString10);
                            parcel2.writeNoException();
                            parcel2.writeInt(psyMmiChgEn);
                            return true;
                        case 91:
                            String readString11 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int psyOtgSwitch2 = setPsyOtgSwitch(readString11);
                            parcel2.writeNoException();
                            parcel2.writeInt(psyOtgSwitch2);
                            return true;
                        case 92:
                            String readString12 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int reserveSocDebug2 = setReserveSocDebug(readString12);
                            parcel2.writeNoException();
                            parcel2.writeInt(reserveSocDebug2);
                            return true;
                        case 93:
                            String readString13 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int shipMode = setShipMode(readString13);
                            parcel2.writeNoException();
                            parcel2.writeInt(shipMode);
                            return true;
                        case TRANSACTION_setSmartChgMode /* 94 */:
                            String readString14 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int smartChgMode2 = setSmartChgMode(readString14);
                            parcel2.writeNoException();
                            parcel2.writeInt(smartChgMode2);
                            return true;
                        case TRANSACTION_setSmartCoolDown /* 95 */:
                            int readInt5 = parcel.readInt();
                            int readInt6 = parcel.readInt();
                            String readString15 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int smartCoolDown = setSmartCoolDown(readInt5, readInt6, readString15);
                            parcel2.writeNoException();
                            parcel2.writeInt(smartCoolDown);
                            return true;
                        case TRANSACTION_setTbattPwrOff /* 96 */:
                            String readString16 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int tbattPwrOff = setTbattPwrOff(readString16);
                            parcel2.writeNoException();
                            parcel2.writeInt(tbattPwrOff);
                            return true;
                        case TRANSACTION_setUisohDebugInfo /* 97 */:
                            String readString17 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int uisohDebugInfo = setUisohDebugInfo(readString17);
                            parcel2.writeNoException();
                            parcel2.writeInt(uisohDebugInfo);
                            return true;
                        case 98:
                            String readString18 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int usbPrimalType2 = setUsbPrimalType(readString18);
                            parcel2.writeNoException();
                            parcel2.writeInt(usbPrimalType2);
                            return true;
                        case 99:
                            String readString19 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessChargePumpEn2 = setWirelessChargePumpEn(readString19);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessChargePumpEn2);
                            return true;
                        case 100:
                            String readString20 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessFtmMode = setWirelessFtmMode(readString20);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessFtmMode);
                            return true;
                        case 101:
                            String readString21 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessIconDelay = setWirelessIconDelay(readString21);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessIconDelay);
                            return true;
                        case 102:
                            String readString22 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessIdtAdcTest = setWirelessIdtAdcTest(readString22);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessIdtAdcTest);
                            return true;
                        case 103:
                            String readString23 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessPenSoc = setWirelessPenSoc(readString23);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessPenSoc);
                            return true;
                        case 104:
                            String readString24 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessRXEnable2 = setWirelessRXEnable(readString24);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessRXEnable2);
                            return true;
                        case 105:
                            String readString25 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessTXEnable2 = setWirelessTXEnable(readString25);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessTXEnable2);
                            return true;
                        case 106:
                            String readString26 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wirelessUserSleepMode2 = setWirelessUserSleepMode(readString26);
                            parcel2.writeNoException();
                            parcel2.writeInt(wirelessUserSleepMode2);
                            return true;
                        case 107:
                            String readString27 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int wlsThirdPartitionInfo = setWlsThirdPartitionInfo(readString27);
                            parcel2.writeNoException();
                            parcel2.writeInt(wlsThirdPartitionInfo);
                            return true;
                        case 108:
                            int readInt7 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            testKitFeatureTestResult testKitFeatureTest = testKitFeatureTest(readInt7);
                            parcel2.writeNoException();
                            parcel2.writeTypedObject(testKitFeatureTest, 1);
                            return true;
                        case 109:
                            String testKitGetFeatureList = testKitGetFeatureList();
                            parcel2.writeNoException();
                            parcel2.writeString(testKitGetFeatureList);
                            return true;
                        case TRANSACTION_testKitGetFeatureName /* 110 */:
                            int readInt8 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            String testKitGetFeatureName = testKitGetFeatureName(readInt8);
                            parcel2.writeNoException();
                            parcel2.writeString(testKitGetFeatureName);
                            return true;
                        case 111:
                            int testKitGetFeatureNum = testKitGetFeatureNum();
                            parcel2.writeNoException();
                            parcel2.writeInt(testKitGetFeatureNum);
                            return true;
                        case 112:
                            int updateUiSohToPartion = updateUiSohToPartion();
                            parcel2.writeNoException();
                            parcel2.writeInt(updateUiSohToPartion);
                            return true;
                        case 113:
                            String queryWlsPencilInfo = queryWlsPencilInfo();
                            parcel2.writeNoException();
                            parcel2.writeString(queryWlsPencilInfo);
                            return true;
                        case TRANSACTION_getChgOlcConfig /* 114 */:
                            String chgOlcConfig = getChgOlcConfig();
                            parcel2.writeNoException();
                            parcel2.writeString(chgOlcConfig);
                            return true;
                        case TRANSACTION_setChgOlcConfig /* 115 */:
                            String readString28 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chgOlcConfig2 = setChgOlcConfig(readString28);
                            parcel2.writeNoException();
                            parcel2.writeInt(chgOlcConfig2);
                            return true;
                        case TRANSACTION_setSuperEnduranceStatus /* 116 */:
                            String readString29 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int superEnduranceStatus = setSuperEnduranceStatus(readString29);
                            parcel2.writeNoException();
                            parcel2.writeInt(superEnduranceStatus);
                            return true;
                        case TRANSACTION_setSuperEnduranceCount /* 117 */:
                            String readString30 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int superEnduranceCount = setSuperEnduranceCount(readString30);
                            parcel2.writeNoException();
                            parcel2.writeInt(superEnduranceCount);
                            return true;
                        case TRANSACTION_setBobStatus /* 118 */:
                            String readString31 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int bobStatus = setBobStatus(readString31);
                            parcel2.writeNoException();
                            parcel2.writeInt(bobStatus);
                            return true;
                        case TRANSACTION_setPsySlowChgEn /* 119 */:
                            String readString32 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int psySlowChgEn = setPsySlowChgEn(readString32);
                            parcel2.writeNoException();
                            parcel2.writeInt(psySlowChgEn);
                            return true;
                        case 120:
                            int cpVbatDeviation = getCpVbatDeviation();
                            parcel2.writeNoException();
                            parcel2.writeInt(cpVbatDeviation);
                            return true;
                        case 121:
                            String readString33 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int batteryLogPush = setBatteryLogPush(readString33);
                            parcel2.writeNoException();
                            parcel2.writeInt(batteryLogPush);
                            return true;
                        case 122:
                            int chargingModeInGsmCall = getChargingModeInGsmCall();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargingModeInGsmCall);
                            return true;
                        case 123:
                            String readString34 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chargingModeInGsmCall2 = setChargingModeInGsmCall(readString34);
                            parcel2.writeNoException();
                            parcel2.writeInt(chargingModeInGsmCall2);
                            return true;
                        case 124:
                            String readString35 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int chgRusConfig = setChgRusConfig(readString35);
                            parcel2.writeNoException();
                            parcel2.writeInt(chgRusConfig);
                            return true;
                        case 125:
                            String psyBatterySN = getPsyBatterySN();
                            parcel2.writeNoException();
                            parcel2.writeString(psyBatterySN);
                            return true;
                        case 126:
                            String battGaugeInfo = getBattGaugeInfo();
                            parcel2.writeNoException();
                            parcel2.writeString(battGaugeInfo);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements ICharger {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICharger.DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int VolDividerIcWorkModeSet(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method VolDividerIcWorkModeSet is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int chgExchangeMesgInit() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method chgExchangeMesgInit is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int chgExchangeSohMesgInit() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method chgExchangeSohMesgInit is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getAcType() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getAcType is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattAuthenticate() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattAuthenticate is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattPPSChgIng() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattPPSChgIng is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattPPSChgPower() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattPPSChgPower is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getBattParamNoplug() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattParamNoplug is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattShortIcOtpStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattShortIcOtpStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattSubCurrent() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattSubCurrent is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattVoocChgIng() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattVoocChgIng is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBatteryVoltageNow() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(12, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBatteryVoltageNow is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getBccCsvData() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBccCsvData is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBccExpStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(14, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBccExpStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getBmsHeatingRunningStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(15, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBmsHeatingRunningStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBmsHeatingStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(16, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBmsHeatingStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getChargerControl() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(17, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargerControl is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerCoolDown() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(18, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargerCoolDown is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerCriticalLog() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(19, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargerCriticalLog is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerIdVolt() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(20, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargerIdVolt is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerLog() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(21, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargerLog is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getCustomSelectChgMode() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(22, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getCustomSelectChgMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getDevinfoFastchg() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(23, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getDevinfoFastchg is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getFastCharge() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(24, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getFastCharge is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getParallelChgMosTestResult() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(25, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getParallelChgMosTestResult is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyAcOnline() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(26, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyAcOnline is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryCC() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(27, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryCC is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryCurrentNow() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(28, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryCurrentNow is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryFcc() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(29, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryFcc is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryHmac() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(30, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryHmac is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(31, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryLevel is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryNotify() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(32, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryNotify is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryPchg() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(33, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryPchg is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryPchgResetCount() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(34, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryPchgResetCount is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryRm() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(35, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryRm is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryShortFeature() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(36, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryShortFeature is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryShortStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(37, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryShortStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getPsyBatteryStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(38, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryTemp() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(39, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatteryTemp is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyChargeTech() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(40, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyChargeTech is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyFastChgType() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(41, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyFastChgType is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyInputCurrent() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(42, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyInputCurrent is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyOtgOnline() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(43, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyOtgOnline is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyOtgSwitch() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(44, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyOtgSwitch is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyPcPortOnline() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(45, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyPcPortOnline is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyQGVbatDeviation() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(46, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyQGVbatDeviation is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyTypeOrientation() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(47, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyTypeOrientation is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyUsbOnline() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(48, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyUsbOnline is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyUsbStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(49, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyUsbStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getPsyWirelessRX() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(50, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyWirelessRX is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getPsyWirelessRxVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(51, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyWirelessRxVersion is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getPsyWirelessTX() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(52, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyWirelessTX is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getPsyWirelessTxVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(53, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyWirelessTxVersion is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getQgVbatDeviation() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(54, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getQgVbatDeviation is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getQuickModeGain() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(55, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getQuickModeGain is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getReserveSocDebug() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(56, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getReserveSocDebug is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getSmartChgMode() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(57, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getSmartChgMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getUIsohValue() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(58, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getUIsohValue is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getUisohDebugParameterInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(59, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getUisohDebugParameterInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getUsbInputCurrentNow() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(60, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getUsbInputCurrentNow is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getUsbPrimalType() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(61, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getUsbPrimalType is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWiredOtgOnline() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(62, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWiredOtgOnline is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessAdapterPower() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(63, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessAdapterPower is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessCapacity() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(64, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessCapacity is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessChargePumpEn() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(65, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessChargePumpEn is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessCurrentNow() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(66, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessCurrentNow is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getWirelessDeviated() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(67, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessDeviated is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessOnline() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(68, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessOnline is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessPenPresent() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(69, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessPenPresent is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessPtmcId() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(70, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessPtmcId is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessRXEnable() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(71, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessRXEnable is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessRealType() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(72, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessRealType is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getWirelessTXEnable() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(73, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessTXEnable is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessUserSleepMode() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(74, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessUserSleepMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessVoltageNow() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(75, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getWirelessVoltageNow is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String healthd_update_ui_soc_decimal() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(76, obtain, obtain2, 0)) {
                        throw new RemoteException("Method healthd_update_ui_soc_decimal is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int nightstandby(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(Stub.TRANSACTION_nightstandby, obtain, obtain2, 0)) {
                        throw new RemoteException("Method nightstandby is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int notifyScreenStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(Stub.TRANSACTION_notifyScreenStatus, obtain, obtain2, 0)) {
                        throw new RemoteException("Method notifyScreenStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String queryChargeInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(Stub.TRANSACTION_queryChargeInfo, obtain, obtain2, 0)) {
                        throw new RemoteException("Method queryChargeInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargeEMMode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(80, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargeEMMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerControl(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(81, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargerControl is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerCoolDown(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(82, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargerCoolDown is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerCriticalLog(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setChargerCriticalLog, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargerCriticalLog is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerCycle(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setChargerCycle, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargerCycle is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerFactoryModeTest(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setChargerFactoryModeTest, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargerFactoryModeTest is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerLog(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setChargerLog, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargerLog is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChgStatusToBcc(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(87, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChgStatusToBcc is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setCustomSelectChgMode(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(88, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setCustomSelectChgMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setFastchgFwUpdate(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setFastchgFwUpdate, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setFastchgFwUpdate is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setPsyMmiChgEn(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(90, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setPsyMmiChgEn is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setPsyOtgSwitch(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(91, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setPsyOtgSwitch is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setReserveSocDebug(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(92, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setReserveSocDebug is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setShipMode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(93, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setShipMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSmartChgMode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setSmartChgMode, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setSmartChgMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSmartCoolDown(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setSmartCoolDown, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setSmartCoolDown is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setTbattPwrOff(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setTbattPwrOff, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setTbattPwrOff is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setUisohDebugInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setUisohDebugInfo, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setUisohDebugInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setUsbPrimalType(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(98, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setUsbPrimalType is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessChargePumpEn(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(99, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessChargePumpEn is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessFtmMode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(100, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessFtmMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessIconDelay(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(101, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessIconDelay is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessIdtAdcTest(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(102, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessIdtAdcTest is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessPenSoc(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(103, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessPenSoc is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessRXEnable(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(104, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessRXEnable is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessTXEnable(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(105, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessTXEnable is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessUserSleepMode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(106, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWirelessUserSleepMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWlsThirdPartitionInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(107, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setWlsThirdPartitionInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public testKitFeatureTestResult testKitFeatureTest(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(108, obtain, obtain2, 0)) {
                        throw new RemoteException("Method testKitFeatureTest is unimplemented.");
                    }
                    obtain2.readException();
                    return (testKitFeatureTestResult) obtain2.readTypedObject(testKitFeatureTestResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String testKitGetFeatureList() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(109, obtain, obtain2, 0)) {
                        throw new RemoteException("Method testKitGetFeatureList is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String testKitGetFeatureName(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(Stub.TRANSACTION_testKitGetFeatureName, obtain, obtain2, 0)) {
                        throw new RemoteException("Method testKitGetFeatureName is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int testKitGetFeatureNum() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(111, obtain, obtain2, 0)) {
                        throw new RemoteException("Method testKitGetFeatureNum is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int updateUiSohToPartion() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(112, obtain, obtain2, 0)) {
                        throw new RemoteException("Method updateUiSohToPartion is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String queryWlsPencilInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(113, obtain, obtain2, 0)) {
                        throw new RemoteException("Method queryWlsPencilInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getChgOlcConfig() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(Stub.TRANSACTION_getChgOlcConfig, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChgOlcConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChgOlcConfig(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setChgOlcConfig, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChgOlcConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSuperEnduranceStatus(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setSuperEnduranceStatus, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setSuperEnduranceStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSuperEnduranceCount(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setSuperEnduranceCount, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setSuperEnduranceCount is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setBobStatus(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setBobStatus, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setBobStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setPsySlowChgEn(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(Stub.TRANSACTION_setPsySlowChgEn, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setPsySlowChgEn is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getCpVbatDeviation() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(120, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getCpVbatDeviation is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setBatteryLogPush(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(121, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setBatteryLogPush is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargingModeInGsmCall() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(122, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargingModeInGsmCall is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargingModeInGsmCall(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(123, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargingModeInGsmCall is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChgRusConfig(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(124, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChgRusConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getPsyBatterySN() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(125, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPsyBatterySN is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public String getBattGaugeInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                    if (!this.mRemote.transact(126, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBattGaugeInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedVersion = obtain2.readInt();
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(ICharger.DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedHash = obtain2.readString();
                        obtain2.recycle();
                        obtain.recycle();
                    } catch (Throwable th) {
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
