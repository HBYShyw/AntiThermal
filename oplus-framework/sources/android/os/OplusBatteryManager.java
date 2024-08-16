package android.os;

import android.os.IGuardElfThermalControl;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusBatteryManager {
    public static final String ACTION_ADDITIONAL_BATTERY_CHANGED = "android.intent.action.ADDITIONAL_BATTERY_CHANGED";
    public static final String EXTRA_BATTERY_CHG_BALANCE_TYPE = "battery_charge_balance_type";
    public static final String EXTRA_BATTERY_CURRENT = "batterycurrent";
    public static final String EXTRA_BATTERY_MIN_VOLTAGE_TYPE = "battery_min_voltage_type";
    public static final String EXTRA_BATTERY_NOTIFY_CODE = "notifycode";
    public static final String EXTRA_BATTERY_NOW_VOLTAGE_TYPE = "battery_now_voltage_type";
    public static final String EXTRA_BATTERY_QUIET_THERM_TYPE = "battery_quiet_therm_type";
    public static final String EXTRA_BATTERY_REALTIME_CAPATICAL = "realtime_capatical";
    public static final String EXTRA_BATTERY_SOC_JUMP = "soc_jump";
    public static final String EXTRA_BMS_HEATING_STATUS = "bms_heating_status";
    public static final String EXTRA_CHARGER_TECHNOLOGY = "chargertechnology";
    public static final String EXTRA_CHARGER_VOLTAGE = "chargervoltage";
    public static final String EXTRA_CHARGE_FAST_CHARGER = "chargefastcharger";
    public static final String EXTRA_CHARGE_PLUGGED = "chargeplugged";
    public static final String EXTRA_CHG_WATTAGE = "chargewattage";
    public static final String EXTRA_ENVIRONMENT_TEMP_TYPE = "environment_temp_type";
    public static final String EXTRA_FAST_CHG_TYPE = "fast_chg_type";
    public static final String EXTRA_OTG_ONLINE = "otgonline";
    public static final String EXTRA_PPS_CHG_MODE = "pps_chg_mode";
    public static final int EXTRA_SLOW_CHARGE_DISABLE = 0;
    public static final int EXTRA_SLOW_CHARGE_ENABLE = 1;
    public static final String EXTRA_USER_DISABLE = "user_disable";
    public static final String EXTRA_USER_ENABLE = "user_enable";
    public static final String EXTRA_WIRELESS_DEVIATED_CHG_TYPE = "wireless_deviated_chg_type";
    public static final String EXTRA_WIRELESS_FAST_CHG_TYPE = "wireless_fast_chg_type";
    public static final String EXTRA_WIRELESS_REVERSE_CHG_TYPE = "wireless_reverse_chg_type";
    public static final int FAST_CHARGER_TECHNOLOGY = 1;
    public static final int NORMAL_CHARGER_TECHNOLOGY = 0;
    public static final int STATUS_BMS_HEATING_ACTIVE = 1;
    public static final int STATUS_BMS_HEATING_DONE = 2;
    public static final int STATUS_BMS_HEATING_INACTIVE = 0;
    public static final int STATUS_BMS_HEATING_QUIT = 3;
    private static final String TAG = "OplusBatteryManager";
    private IGuardElfThermalControl mBaseBatteryService;

    private void getBatteryDataService() {
        IBinder binder = ServiceManager.getService("guardelfthermalcontrol");
        if (binder == null) {
            Slog.d(TAG, "service guardelfthermalcontrol is null");
        } else if (this.mBaseBatteryService != IGuardElfThermalControl.Stub.asInterface(binder)) {
            this.mBaseBatteryService = IGuardElfThermalControl.Stub.asInterface(binder);
        }
    }

    public void setChargingLevel(String data, String name) {
        IBinder binder = ServiceManager.getService("guardelfthermalcontrol");
        if (binder == null) {
            Slog.d(TAG, "service guardelfthermalcontrol is null");
        } else if (this.mBaseBatteryService != IGuardElfThermalControl.Stub.asInterface(binder)) {
            this.mBaseBatteryService = IGuardElfThermalControl.Stub.asInterface(binder);
        }
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "service guardelfthermalcontrol is null");
            return;
        }
        try {
            iGuardElfThermalControl.setChargeLevel(data, name);
            Slog.d(TAG, "set charge value = " + data);
        } catch (RemoteException e) {
            Slog.e(TAG, e.toString());
        }
    }

    public boolean isCameraOn() {
        IBinder binder = ServiceManager.getService("guardelfthermalcontrol");
        if (binder == null) {
            Slog.d(TAG, "service guardelfthermalcontrol is null");
        } else if (this.mBaseBatteryService != IGuardElfThermalControl.Stub.asInterface(binder)) {
            this.mBaseBatteryService = IGuardElfThermalControl.Stub.asInterface(binder);
        }
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "service guardelfthermalcontrol is null");
            return false;
        }
        try {
            boolean isCameraWorking = iGuardElfThermalControl.isCameraOn();
            return isCameraWorking;
        } catch (RemoteException e) {
            Slog.e(TAG, e.toString());
            return false;
        }
    }

    public float getBeginDecimal() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return 0.0f;
        }
        try {
            float beginDecimal = iGuardElfThermalControl.getBeginDecimal();
            return beginDecimal;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return 0.0f;
        }
    }

    public float getEndDecimal() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return 0.0f;
        }
        try {
            float endDecimal = iGuardElfThermalControl.getEndDecimal();
            return endDecimal;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return 0.0f;
        }
    }

    public int getPsyOtgOnline() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int otgOnline = iGuardElfThermalControl.getPsyOtgOnline();
            return otgOnline;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getPsyBatteryHmac() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int batteryHmac = iGuardElfThermalControl.getPsyBatteryHmac();
            return batteryHmac;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getChargerTechnology() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return 0;
        }
        try {
            int chargerTechnology = iGuardElfThermalControl.getChargerTechnology();
            return chargerTechnology;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return 0;
        }
    }

    public int getWirelessPenPresent() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int wirelessPenPresent = iGuardElfThermalControl.getWirelessPenPresent();
            return wirelessPenPresent;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public void setWirelessPenSoc(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return;
        }
        try {
            iGuardElfThermalControl.setWirelessPenSoc(data);
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
    }

    public void setWirelessTXEnable(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return;
        }
        try {
            iGuardElfThermalControl.setWirelessTXEnable(data);
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
    }

    public String getWirelessTXEnable() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return null;
        }
        try {
            String wirelessEnable = iGuardElfThermalControl.getWirelessTXEnable();
            return wirelessEnable;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return null;
        }
    }

    public void setWirelessUserSleepMode(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return;
        }
        try {
            iGuardElfThermalControl.setWirelessUserSleepMode(data);
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
    }

    public int getWirelessUserSleepMode() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int silentMode = iGuardElfThermalControl.getWirelessUserSleepMode();
            return silentMode;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public void setPsyMmiChgEn(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return;
        }
        try {
            iGuardElfThermalControl.setPsyMmiChgEn(data);
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
    }

    public int getPsyBatteryRm() {
        getBatteryDataService();
        int result = -1;
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            result = iGuardElfThermalControl.getPsyBatteryRm();
            Slog.d(TAG, "getPsyBatteryRm: " + result);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return result;
        }
    }

    public int getWiredOtgOnline() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "smOplusBatteryService is null");
            return -1;
        }
        try {
            int wiredOtgOnline = iGuardElfThermalControl.getWiredOtgOnline();
            return wiredOtgOnline;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getPsyBatteryNotify() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "smOplusBatteryService is null");
            return -1;
        }
        try {
            int psyBatteryNotify = iGuardElfThermalControl.getPsyBatteryNotify();
            return psyBatteryNotify;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getWirelessAdapterPower() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int wirelessAdapterPower = iGuardElfThermalControl.getWirelessAdapterPower();
            return wirelessAdapterPower;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getSmartChgMode() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int getSmartChgMode = iGuardElfThermalControl.getSmartChgMode();
            return getSmartChgMode;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int setSmartChgMode(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int setSmartChgMode = iGuardElfThermalControl.setSmartChgMode(data);
            return setSmartChgMode;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getBattPPSChgIng() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int getBattPPSChgIng = iGuardElfThermalControl.getBattPPSChgIng();
            return getBattPPSChgIng;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getBattPPSChgPower() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int getBattPPSChgPower = iGuardElfThermalControl.getBattPPSChgPower();
            return getBattPPSChgPower;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getCustomSelectChgMode() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int getCustomSelectChgMode = iGuardElfThermalControl.getCustomSelectChgMode();
            return getCustomSelectChgMode;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int setCustomSelectChgMode(int mode, boolean enable) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int setCustomSelectChgMode = iGuardElfThermalControl.setCustomSelectChgMode(mode, enable);
            return setCustomSelectChgMode;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getPsyChargeTech() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int getPsyChargeTech = iGuardElfThermalControl.getPsyChargeTech();
            return getPsyChargeTech;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int setSmartCoolDown(int coolDown, int normalCoolDown, String pkgName) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int setSmartCoolDown = iGuardElfThermalControl.setSmartCoolDown(coolDown, normalCoolDown, pkgName);
            return setSmartCoolDown;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public String getQuickModeGain() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return null;
        }
        try {
            String getQuickModeGain = iGuardElfThermalControl.getQuickModeGain();
            return getQuickModeGain;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return "";
        }
    }

    public int getBmsHeatingStatus() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int getBmsHeatingStatus = iGuardElfThermalControl.getBmsHeatingStatus();
            return getBmsHeatingStatus;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int getUIsohValue() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int getUIsohValue = iGuardElfThermalControl.getUIsohValue();
            return getUIsohValue;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public String getReserveSocDebug() {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return null;
        }
        try {
            String reserveSocDebug = iGuardElfThermalControl.getReserveSocDebug();
            return reserveSocDebug;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return "";
        }
    }

    public int setReserveSocDebug(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int result = iGuardElfThermalControl.setReserveSocDebug(data);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int setWlsThirdPartitionInfo(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int result = iGuardElfThermalControl.setWlsThirdPartitionInfo(data);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int setChargerCycle(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int setChargerCycle = iGuardElfThermalControl.setChargerCycle(data);
            return setChargerCycle;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int nightstandby(int status) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int result = iGuardElfThermalControl.nightstandby(status);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int setChgOlcConfig(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        int result = -1;
        try {
            result = iGuardElfThermalControl.setChgOlcConfig(data);
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
        if (result > 0) {
            return 0;
        }
        return result;
    }

    public int setSuperEnduranceStatus(boolean status) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        int result = -1;
        try {
            result = iGuardElfThermalControl.setSuperEnduranceStatus(String.valueOf(status ? 1 : 0));
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
        if (result > 0) {
            return 0;
        }
        return result;
    }

    public int setSuperEnduranceCount(int count) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        int result = -1;
        try {
            result = iGuardElfThermalControl.setSuperEnduranceCount(String.valueOf(count));
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
        if (result > 0) {
            return 0;
        }
        return result;
    }

    public int setPsySlowChgEnable(int percent, int wattage, int status) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int result = iGuardElfThermalControl.setPsySlowChgEnable(percent, wattage, status);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }

    public int setBatteryLogPush(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        int result = -1;
        try {
            result = iGuardElfThermalControl.setBatteryLogPush(data);
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
        }
        if (result > 0) {
            return 0;
        }
        return result;
    }

    public int setChgRusConfig(String data) {
        getBatteryDataService();
        IGuardElfThermalControl iGuardElfThermalControl = this.mBaseBatteryService;
        if (iGuardElfThermalControl == null) {
            Slog.d(TAG, "mOplusBatteryService is null");
            return -1;
        }
        try {
            int result = iGuardElfThermalControl.setChgRusConfig(data);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return -1;
        }
    }
}
