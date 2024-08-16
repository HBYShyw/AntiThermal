package com.oplus.thermalcontrol;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.StatsEvent;
import android.util.StatsLog;
import android.view.Display;
import b6.LocalLog;
import com.google.gson.GsonBuilder;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.thermalcontrol.ThermalControlUtils;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.GpuLevelConfig;
import com.oplus.thermalcontrol.config.policy.ThermalAmbientPolicy;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import f6.CommonUtil;
import ia.ThermalSmartRefreshUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import y5.AppFeature;

/* loaded from: classes2.dex */
public class ThermalControllerCenter {
    private static final String BRIGHTNESS_TEMP_PREFIX = "@temperature@";
    private static final int CPU_POWER_DEFAULT = 9999;
    private static final String CUR_THERMAL_POLICY_IS_NULL = "mCurThermalPolicy == null";
    private static final String DEFAULT_REASON_PRIORITY_FORCE = "force";
    private static final String DEFAULT_REASON_PRIORITY_NORMAL = "normal";
    private static final String EAP_INTENT_NAME = "oplus.intent.action.GUARDELF_THERMAL_TO_EAP";
    private static final String EAP_SAFE_PERMISSION = "com.oplus.permission.safe.LOG";
    private static final long FOUR_HOUR = 14400000;
    private static final int FRAME_INSERT_FRESH_RATE_120 = 120;
    private static final int FRAME_INSERT_FRESH_RATE_120_INDEX = 3;
    private static final int FRAME_INSERT_FRESH_RATE_90 = 90;
    private static final int FRAME_INSERT_FRESH_RATE_90_INDEX = 1;
    private static final int FRAME_INSERT_STATE = 22;
    private static final int FRAME_INSERT_VALUE_CLOSE = 2;
    private static final int FRAME_INSERT_VALUE_OPEN = 3;
    private static final int LEVEL_3 = 3;
    private static final int LEVEL_5 = 5;
    private static final int LEVEL_6 = 6;
    private static final int LEVEL_7 = 7;
    private static final int MAX_BRIGHTNESS = 255;
    private static final int MSG_INIT_TIMEOUT = 5000;
    private static final int MSG_THERMAL_CONTROL_AMBIENT_STATE_CHANGE = 19;
    private static final int MSG_THERMAL_CONTROL_AMBIENT_TEMP_CHANGE = 18;
    private static final int MSG_THERMAL_CONTROL_BACKUP_HIGH_CHANGED = 9;
    private static final int MSG_THERMAL_CONTROL_BACKUP_RESTORE_CHANGED = 5;
    private static final int MSG_THERMAL_CONTROL_CATEGORY_CHANGED = 1;
    private static final int MSG_THERMAL_CONTROL_CATEGORY_PROTECT_PERIOD = 3;
    private static final int MSG_THERMAL_CONTROL_FLOAT_WINDOW_CHANGED = 16;
    private static final int MSG_THERMAL_CONTROL_FOLDING_MODE_CHANGED = 13;
    private static final int MSG_THERMAL_CONTROL_GAME_MODE_CHANGED = 12;
    private static final int MSG_THERMAL_CONTROL_GT_MODE_CHANGED = 15;
    private static final int MSG_THERMAL_CONTROL_INIT = 0;
    private static final int MSG_THERMAL_CONTROL_MODEM_PROTECT_PERIOD = 7;
    private static final int MSG_THERMAL_CONTROL_RESTRICT_PROTECT_PERIOD = 11;
    private static final int MSG_THERMAL_CONTROL_SAFETY_PROTECT_PERIOD = 17;
    private static final int MSG_THERMAL_CONTROL_SCENE_CHANGE = 10;
    private static final int MSG_THERMAL_CONTROL_SCREEN_STATUS_CHANGED = 4;
    private static final int MSG_THERMAL_CONTROL_SCREEN_UNLOCK = 8;
    private static final int MSG_THERMAL_CONTROL_SPLIT_MODE_CHANGED = 14;
    private static final int MSG_THERMAL_CONTROL_TEMPGEAR_CHANGED = 2;
    private static final int MSG_THERMAL_CONTROL_TRACEINFO_UPLOAD = 6;
    private static final int MSG_THERMAL_CONTROL_TSENSOR_TEMP_CHANGE = 21;
    private static final int RESTRICT_PROTECT_PERIOD_TIMEOUT = 1200000;
    private static final int RM_REDUCE_BRIGHTNESS = 50;
    private static final String SAFETY_SCENARIO_STATE_0 = "0";
    private static final String SAFETY_SCENARIO_STATE_1 = "1";
    private static final int SCENE_CHANGE_DELAY = 5000;
    private static final int SCENE_TYPES_OFFSET = 100;
    private static final int SCENE_TYPE_AI_NAVIGATION = 106;
    private static final int SCENE_TYPE_AUDIO_CALL = 102;
    private static final int SCENE_TYPE_BACKUP_HIGH_PERFORMANCE = 151;
    private static final int SCENE_TYPE_BACKUP_RESTORE = 150;
    private static final int SCENE_TYPE_GAME = 104;
    private static final int SCENE_TYPE_NAVIGATION = 16;
    private static final int SCENE_TYPE_RECORDING = 152;
    private static final int SCENE_TYPE_VIDEO = 101;
    private static final int SCENE_TYPE_VIDEO_CALL = 103;
    private static final int SCENE_TYPE_VIDEO_LIVE = 105;
    private static final String TAG = "ThermalControllerCenter";
    private static final int THERMAL_CONTROL_GT_MODE_DELAY = 500;
    private static final int THERMAL_CONTROL_STATS_ATOM_ID = 100088;
    private static final int TRACE_UPLOAD_DELAY_VAL = 2000;
    private static volatile ThermalControllerCenter sThermalControllerCenter;
    private Context mContext;
    private Display mDisplay;
    private DisplayManager mDisplayManager;
    private ThermalControlHandler mHandler;
    private final HeatSourceController mHeatSourceController;
    private PowerManager mPowerManager;
    private ThermalControlConfig mThermalControlConfig;
    private ThermalControlMessage mThermalControlMessage;
    private ThermalControlMonitor mThermalControlMonitor;
    private ThermalStatusUploader mThermalStatusUploader;
    private ThermalControlUtils mUtils;
    private PowerManager.WakeLock mWakeLock;
    private boolean mThermalControlTest = false;
    private boolean mThermalControlCategory = false;
    private boolean mWifiHotControlState = false;
    private boolean mThermalControlState = true;
    private boolean mThermalControlRestrictAllow = true;
    private boolean mThermalControlModemAllow = true;
    private boolean mIsSafety = false;
    private boolean mIsScreenOn = true;
    private boolean mIsThermalControlEnable = true;
    private boolean mIsSetBrightnessForce = false;
    private boolean mInFrameInsertRL = false;
    private boolean mAppSwitchSafetyMode = false;
    private boolean mAlreadySwitchSafetyMode = false;
    private boolean mIsNeedCheckAppSwitch = true;
    private int mPreTempLevel = 0;
    private int mPreUserMode = 2;
    private int mPreGameUserMode = 2;
    private int mTempGear = 0;
    private int mCategoryIndex = -1;
    private int mEnvTempType = -1;
    private int mAppType = -1;
    private int mTempLevel = -1;
    private int mTemp = 0;
    private int mSafeRestrictLevel = -1;
    private int mUserMode = 2;
    private int mGameUserMode = 7;
    private String mConfigItemName = ThermalControlConfig.ORIGINAL_DEFAULT_ITEM;
    private String mPrePackageName = "com.android.launcher";
    private String mCurPackageName = "com.android.launcher";
    private List<Integer> mAppStates = new ArrayList();
    private String mPreAppCategory = null;
    private ThermalPolicy mPreThermalPolicy = null;
    private ThermalPolicy mCurThermalPolicy = null;
    private CpuLevelConfig.ThermalCpuLevelPolicy mCurCpuPolicy = null;
    private GpuLevelConfig.ThermalGpuLevelPolicy mCurGpuPolicy = null;
    private int mCurThermalSerious = -2;
    private String mSafetyState = null;
    private Object mLock = new Object();

    private ThermalControllerCenter(Context context) {
        this.mPowerManager = null;
        this.mContext = context;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mPowerManager = powerManager;
        this.mWakeLock = powerManager.newWakeLock(1, "ThermalController:center");
        this.mUtils = ThermalControlUtils.getInstance(this.mContext);
        this.mThermalControlMonitor = ThermalControlMonitor.getInstance(context);
        this.mThermalControlConfig = ThermalControlConfig.getInstance(this.mContext);
        HandlerThread handlerThread = new HandlerThread("thermal_Controller_thread");
        handlerThread.start();
        ThermalControlHandler thermalControlHandler = new ThermalControlHandler(context, handlerThread.getLooper());
        this.mHandler = thermalControlHandler;
        thermalControlHandler.sendEmptyMessageDelayed(0, 5000L);
        this.mThermalStatusUploader = ThermalStatusUploader.getInstance(this.mContext, handlerThread.getLooper());
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDisplayManager = displayManager;
        if (displayManager != null) {
            this.mDisplay = displayManager.getDisplay(0);
        }
        this.mHeatSourceController = HeatSourceController.getInstance(context);
    }

    private void checkTsensorCpuExceptScene() {
        if (this.mHeatSourceController.getTsensorCpu(this.mCurPackageName, this.mConfigItemName) != this.mHeatSourceController.getLastTsensorCpu()) {
            startCpuAndGpuCoolingMethod(this.mCurThermalPolicy, this.mTemp);
        }
    }

    public static ThermalControllerCenter getInstance(Context context) {
        if (sThermalControllerCenter == null) {
            synchronized (ThermalControllerCenter.class) {
                if (sThermalControllerCenter == null) {
                    sThermalControllerCenter = new ThermalControllerCenter(context);
                }
            }
        }
        return sThermalControllerCenter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTsensorTempChangedMessageLocked(Message message) {
        this.mHeatSourceController.updateTsensorSourceMap(message.arg1);
        this.mHeatSourceController.updateTsensorSourceLevel();
        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_TSENSOR_TEMP_CHANGE, cpuTemp:" + message.arg1 + ", " + this.mHeatSourceController.getTsensorLevelPolices());
        if (!this.mThermalControlConfig.isThermalControlEnable()) {
            stopCollingMethods();
            return;
        }
        if (this.mTempLevel < 0) {
            stopCollingMethods();
            return;
        }
        ThermalPolicy thermalPolicy = this.mCurThermalPolicy;
        if (thermalPolicy == null) {
            LocalLog.a(TAG, CUR_THERMAL_POLICY_IS_NULL);
        } else {
            startCpuAndGpuCoolingMethod(thermalPolicy, this.mTemp);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSafetyStateExcludedScene() {
        if (this.mTempLevel < this.mThermalControlConfig.getHighTempSafetyLevel()) {
            return true;
        }
        if (!this.mUtils.isAging() || this.mTempLevel >= this.mThermalControlConfig.getAgingHighTempSafetyLevel()) {
            return this.mUtils.getUserMode() == 0 && this.mTempLevel < this.mThermalControlConfig.getRacingHighTempSafetyLevel();
        }
        return true;
    }

    private boolean isThermalExcludedScene(String str) {
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal " + str + ": Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return true;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to " + str);
            return true;
        }
        if (!AppFeature.z() || this.mThermalControlConfig.isAgingThermalControlEnable()) {
            return false;
        }
        LocalLog.a(TAG, "ThermalControl in aging test, not respond to " + str);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDestory$1() {
        this.mThermalControlMonitor.stopMonitor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0() {
        this.mThermalControlMonitor.startMonitor();
    }

    private void startCameraCoolingMethod(ThermalPolicy thermalPolicy) {
        boolean z10;
        boolean z11 = false;
        if (thermalPolicy.disFlashlight == 1) {
            this.mUtils.setCameraFlashDisableState(true);
            z10 = false;
        } else {
            this.mUtils.setCameraFlashDisableState(false);
            z10 = true;
        }
        int i10 = thermalPolicy.stopCameraVideo;
        if (i10 > 0) {
            if (i10 != this.mUtils.getCameraVideoStopState()) {
                this.mUtils.setCameraVideoStopState(thermalPolicy.stopCameraVideo);
                z10 = false;
            }
        } else if (this.mUtils.getCameraVideoStopState() != 0) {
            this.mUtils.setCameraVideoStopState(0);
        }
        if (thermalPolicy.disCamera == 1) {
            this.mUtils.setCameraExitState(true);
        } else {
            this.mUtils.setCameraExitState(false);
            z11 = z10;
        }
        int cameraBrightness = this.mHeatSourceController.getCameraBrightness(thermalPolicy);
        if (!z11 || cameraBrightness < 0) {
            return;
        }
        this.mUtils.setCameraScreenBrightness(cameraBrightness);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCpuAndGpuCoolingMethod(ThermalPolicy thermalPolicy, int i10) {
        ThermalPolicy thermalPolicy2;
        CpuLevelConfig.ThermalCpuLevelPolicy cpuPolicy = this.mHeatSourceController.getCpuPolicy(thermalPolicy, this.mCurPackageName, this.mConfigItemName);
        GpuLevelConfig.ThermalGpuLevelPolicy gpuPolicy = this.mHeatSourceController.getGpuPolicy(thermalPolicy);
        int thermalSerious = this.mThermalControlConfig.getThermalSerious(this.mTempLevel, thermalPolicy.thermalSerious);
        if (cpuPolicy.equals(this.mCurCpuPolicy) && gpuPolicy.equals(this.mCurGpuPolicy) && this.mCurThermalSerious == thermalSerious && (thermalPolicy2 = this.mPreThermalPolicy) != null && thermalPolicy.cpuPower == thermalPolicy2.cpuPower) {
            LocalLog.l(TAG, "Same as last cpu policy, gpu policy and thermalSerious");
            return;
        }
        this.mCurThermalSerious = thermalSerious;
        this.mCurCpuPolicy = cpuPolicy;
        this.mCurGpuPolicy = gpuPolicy;
        if (cpuPolicy.mLevel < 0 && gpuPolicy.mLevel < 0 && thermalPolicy.cpuPower <= 0) {
            LocalLog.a(TAG, "cpu and gpu no limit or default, stopCpuAndGpuControl");
            this.mUtils.stopCpuAndGpuControl(i10, this.mTempLevel, this.mCurPackageName);
            this.mUtils.stopCpuAndGpuControl(this.mCurThermalSerious);
        } else {
            this.mUtils.setCpuAndGpuLevel(cpuPolicy, gpuPolicy, thermalSerious, this.mTempLevel, getOrmsParam(this.mCurPackageName, i10, cpuPolicy, gpuPolicy));
            this.mUtils.startCpuAndGpuControl(cpuPolicy, gpuPolicy, this.mCurThermalSerious);
        }
    }

    private void startFpsCoolingMethod(int i10) {
        if (this.mCurPackageName.equals(this.mPrePackageName)) {
            if (i10 > 0) {
                this.mUtils.setFps(i10, this.mCurPackageName);
                return;
            } else {
                this.mUtils.resetFps(this.mCurPackageName);
                return;
            }
        }
        ThermalPolicy thermalPolicy = this.mPreThermalPolicy;
        if (thermalPolicy != null && thermalPolicy.fps > 0) {
            this.mUtils.resetFps(this.mPrePackageName);
        }
        if (i10 > 0) {
            this.mUtils.setFps(i10, this.mCurPackageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startHeatOffsetMethod(boolean z10, boolean z11) {
        if (z11 || z10) {
            startCpuAndGpuCoolingMethod(this.mCurThermalPolicy, this.mTemp);
        }
        if (z10) {
            startModemCoolingMethod(this.mCurThermalPolicy.modem);
            startBrightnessCoolingMethod(this.mCurThermalPolicy.brightness, this.mTemp);
        }
        if (z10 || z11) {
            return;
        }
        checkTsensorCpuExceptScene();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startModemCoolingMethod(int i10) {
        int modemLevel = this.mHeatSourceController.getModemLevel(i10);
        if (modemLevel < 0) {
            this.mUtils.set5GStatus(0);
            return;
        }
        if (modemLevel == 3) {
            if (this.mThermalControlModemAllow) {
                start5GModemMethods(modemLevel);
                return;
            } else {
                LocalLog.l(TAG, "in modem protect period, not set modem");
                return;
            }
        }
        this.mUtils.set5GStatus(modemLevel);
    }

    private void startWifiHotSpotCoolingMethod(int i10) {
        if (this.mUtils.isBackupRestore()) {
            LocalLog.a(TAG, "isBackupResotre");
            return;
        }
        if (!this.mWifiHotControlState && i10 == 1) {
            this.mUtils.disWifiHotSpot(true);
            this.mWifiHotControlState = true;
        } else if (i10 != 1) {
            this.mUtils.disWifiHotSpot(false);
            this.mWifiHotControlState = false;
        }
    }

    private void startZoomWindowRestrict(boolean z10) {
        if (z10 && this.mTempLevel > this.mPreTempLevel && this.mAppSwitchSafetyMode && isAmbientTempMeetConditions()) {
            List<ThermalControlUtils.WindowInfo> floatingWindowsInfo = this.mUtils.getFloatingWindowsInfo(true);
            if (floatingWindowsInfo.size() != 1) {
                LocalLog.a(TAG, "ignore zoom window restrict, windowCount=" + floatingWindowsInfo.size());
                return;
            }
            ThermalControlUtils.WindowInfo windowInfo = floatingWindowsInfo.get(0);
            int zoomWindowRestrictPolicy = this.mThermalControlConfig.getZoomWindowRestrictPolicy(windowInfo.pkg, windowInfo.activityName, windowInfo.type);
            if (zoomWindowRestrictPolicy != 0) {
                this.mUtils.disZoomWindow(windowInfo, zoomWindowRestrictPolicy, this.mTempLevel);
                return;
            }
            LocalLog.a(TAG, "ignore zoom window restrict, not in restrict list: " + windowInfo);
            return;
        }
        LocalLog.a(TAG, "ignore zoom window restrict, isTmpGearChange=" + z10 + ", mPreTempLevel=" + this.mPreTempLevel + ", mAppSwitchSafetyMode=" + this.mAppSwitchSafetyMode + ", AmbientTemperature=" + this.mUtils.getAmbientTemperature());
    }

    public void checkAppScene(List<Integer> list, int i10) {
        if (list.contains(104)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_104";
            return;
        }
        if (list.contains(105)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_105";
            return;
        }
        if (list.contains(103)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_103";
            return;
        }
        if (this.mUtils.isPhoneInCall(this.mContext)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_102";
            return;
        }
        if (list.contains(102)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_102";
            return;
        }
        if (this.mUtils.isBackupRestore() && !this.mUtils.isBackupRestoreScene()) {
            if (this.mUtils.isBackupHighPerformance()) {
                this.mCategoryIndex = 3;
                this.mConfigItemName = "scene_151";
                return;
            } else {
                this.mCategoryIndex = 3;
                this.mConfigItemName = "scene_150";
                return;
            }
        }
        if (this.mUtils.isRecordingOn()) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_152";
            return;
        }
        if (list.contains(101)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_101";
            return;
        }
        if (list.contains(106)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_16";
        } else if (i10 > 0) {
            this.mCategoryIndex = 4;
            this.mConfigItemName = "category_" + i10;
        }
    }

    public void checkPolicyScene() {
        if (this.mConfigItemName.contains("scene_")) {
            int parseInt = Integer.parseInt(this.mConfigItemName.split("_")[1]);
            if (parseInt == 16) {
                parseInt = 106;
            }
            if (parseInt > 100) {
                this.mUtils.setPolicyCurrentScene(parseInt);
                return;
            }
            return;
        }
        this.mUtils.setPolicyCurrentScene(100);
    }

    public void checkSafetyNeedChangeRestrict(String str, String str2, boolean z10, boolean z11) {
        if (isSafetyNeedChangeRestrict()) {
            ThermalPolicy thermalPolicyInfo = this.mThermalControlConfig.getThermalPolicyInfo(this.mCategoryIndex, this.mConfigItemName, str, str2, this.mTempLevel, this.mIsScreenOn);
            if (thermalPolicyInfo == null) {
                for (int i10 = this.mTempLevel; i10 >= 0; i10--) {
                    thermalPolicyInfo = this.mThermalControlConfig.getThermalPolicyInfo(this.mCategoryIndex, this.mConfigItemName, str, str2, i10, this.mIsScreenOn);
                    if (thermalPolicyInfo != null) {
                        break;
                    }
                }
            }
            if (thermalPolicyInfo != null) {
                this.mSafeRestrictLevel = thermalPolicyInfo.restrict;
            } else {
                this.mSafeRestrictLevel = 0;
            }
            LocalLog.l(TAG, "mSafeRestrictLevel=" + this.mSafeRestrictLevel);
            startRestrictCoolingMethod(this.mSafeRestrictLevel, this.mTemp, z10, z11);
        }
    }

    public void checkScreenOffScene() {
        if (this.mUtils.isPhoneInCall(this.mContext)) {
            this.mCategoryIndex = 3;
            this.mConfigItemName = "scene_102";
            return;
        }
        if (this.mUtils.isBackupRestore() && !this.mUtils.isBackupRestoreScene()) {
            if (this.mUtils.isBackupHighPerformance()) {
                this.mCategoryIndex = 3;
                this.mConfigItemName = "scene_151";
                return;
            } else {
                this.mCategoryIndex = 3;
                this.mConfigItemName = "scene_150";
                return;
            }
        }
        this.mCategoryIndex = 2;
        this.mConfigItemName = ThermalControlConfig.ORIGINAL_SCREEN_OFF_ITEM;
    }

    public ThermalPolicy getCurThermalPolicy() {
        synchronized (this.mLock) {
            ThermalPolicy thermalPolicy = this.mCurThermalPolicy;
            if (thermalPolicy == null) {
                return null;
            }
            return thermalPolicy.m26clone();
        }
    }

    public int getEnvironmentTemperatureType() {
        if (this.mUtils.getAmbientTemperature() > 0) {
            if (this.mUtils.getAmbientTemperature() > this.mThermalControlConfig.getAmbientTemperatureUpperLimit() || this.mUtils.getAmbientTemperature() < this.mThermalControlConfig.getAmbientTemperatureLowerLimit()) {
                return 1;
            }
            if (!LocalLog.f()) {
                return 0;
            }
            LocalLog.a(TAG, "AmbientTemperature meet certain conditions.");
            return 0;
        }
        return this.mUtils.getEnvironmentTemperatureType();
    }

    public String getOrmsParam(String str, int i10, CpuLevelConfig.ThermalCpuLevelPolicy thermalCpuLevelPolicy, GpuLevelConfig.ThermalGpuLevelPolicy thermalGpuLevelPolicy) {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("pkgName", str);
            jSONArray.put(jSONObject2);
            JSONArray jSONArray2 = new JSONArray();
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("temperature", i10);
            jSONArray2.put(jSONObject3);
            jSONObject.put(DeviceDomainManager.ARG_PKG, jSONArray);
            jSONObject.put("temperature", jSONArray2);
            if (thermalCpuLevelPolicy != null) {
                thermalCpuLevelPolicy.putIntoJsonObject(jSONObject);
            }
            if (thermalGpuLevelPolicy != null) {
                thermalGpuLevelPolicy.putIntoJsonObject(jSONObject);
            }
            if (LocalLog.g()) {
                LocalLog.a(TAG, "getOrmsParam, " + thermalCpuLevelPolicy + ", " + thermalGpuLevelPolicy + ", tsensorCpu:" + this.mHeatSourceController.getLastTsensorCpu() + ", " + jSONObject.toString());
            }
        } catch (JSONException e10) {
            LocalLog.b(TAG, "setCpuAndGpuLevel e=" + e10);
        }
        return jSONObject.toString();
    }

    public String getSafetyScenarioState() {
        if (this.mThermalControlConfig.isSafetyOptimizeEnabled()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.mUtils.isCharging() ? "1" : "0");
            sb2.append(this.mThermalControlMonitor.isTorchOn() ? "1" : "0");
            sb2.append(this.mUtils.getWifiHotSpotState() ? "1" : "0");
            sb2.append(this.mThermalControlMonitor.isOplusCameraVideoOn() ? "1" : "0");
            sb2.append(this.mUtils.isAudioOrNavigation(this.mAppStates) ? "1" : "0");
            return sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mUtils.isCharging() ? "1" : "0");
        sb3.append(this.mThermalControlMonitor.isTorchOn() ? "1" : "0");
        sb3.append(this.mUtils.getWifiHotSpotState() ? "1" : "0");
        sb3.append(this.mThermalControlMonitor.isOplusCameraOn() ? "1" : "0");
        sb3.append(this.mUtils.isAudioOrNavigation(this.mAppStates) ? "1" : "0");
        return sb3.toString();
    }

    public int getTempGear(int i10, int i11, int i12, int i13) {
        ThermalAmbientPolicy thermalAmbientPolicy;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int additionValWithModeType = i11 + this.mThermalControlConfig.getAdditionValWithModeType(i12) + this.mThermalControlConfig.getAdditionValWithModeType(i13) + this.mThermalControlConfig.getAdditionValWithFloatWindow();
        if (i10 == 4 && elapsedRealtime <= FOUR_HOUR && this.mThermalControlConfig.getIsCrossUpdate()) {
            LocalLog.l(TAG, "is in game scene and forbid ota control");
        } else {
            additionValWithModeType += this.mThermalControlConfig.getOtaThermalControlVal();
        }
        return (!this.mThermalControlConfig.isAmbientOffsetMode() || (thermalAmbientPolicy = this.mThermalControlConfig.getThermalAmbientPolicy(this.mUtils.getAmbientTempState())) == null) ? additionValWithModeType : additionValWithModeType + thermalAmbientPolicy.thermalOffsetLevel;
    }

    public int getTempLevel() {
        return this.mTempLevel;
    }

    public boolean isAmbientTempMeetConditions() {
        if (this.mUtils.getAmbientTemperature() <= 0 || this.mUtils.getAmbientTemperature() > this.mThermalControlConfig.getAmbientTemperatureUpperLimit() || this.mUtils.getAmbientTemperature() < this.mThermalControlConfig.getAmbientTemperatureLowerLimit()) {
            return false;
        }
        if (!LocalLog.f()) {
            return true;
        }
        LocalLog.a(TAG, "AmbientTemperature meet certain conditions.");
        return true;
    }

    public boolean isSafetyNeedChangeRestrict() {
        if (!this.mThermalControlConfig.isSafetyOptimizeEnabled() || this.mTempGear == this.mTempLevel || this.mUserMode == 0) {
            return false;
        }
        return this.mAppType == 4 || SystemClock.elapsedRealtime() > FOUR_HOUR || !this.mThermalControlConfig.getIsCrossUpdate();
    }

    public boolean isSafetyScenario() {
        ThermalControlConfig thermalControlConfig;
        if ((this.mUtils.isFoldingMode() && this.mUtils.isSplitMode()) || (thermalControlConfig = this.mThermalControlConfig) == null) {
            return false;
        }
        if (!thermalControlConfig.isSafetyTestEnable()) {
            LocalLog.a(TAG, "isSafetyTestEnable=false");
            return false;
        }
        this.mSafetyState = getSafetyScenarioState();
        LocalLog.l(TAG, "mSafetyState=" + this.mSafetyState);
        return this.mThermalControlConfig.getSafetyTestConfig().contains(this.mSafetyState);
    }

    public boolean isSafetymode() {
        if (!this.mThermalControlConfig.isSafetyOptimizeEnabled()) {
            return this.mEnvTempType == 0 && this.mIsSafety;
        }
        if (this.mIsNeedCheckAppSwitch) {
            if (this.mEnvTempType != 0 || !this.mAppSwitchSafetyMode || !this.mIsSafety) {
                return false;
            }
            this.mIsNeedCheckAppSwitch = false;
            LocalLog.l(TAG, "MSG_THERMAL_CONTROL_SAFETY_PROTECT_PERIOD enter.");
            sendThermalControlSafetyProtect();
            return true;
        }
        LocalLog.a(TAG, "MSG_THERMAL_CONTROL_SAFETY_PROTECT_PERIOD.");
        if (this.mEnvTempType != 0 || !this.mIsSafety) {
            return false;
        }
        if (this.mAppSwitchSafetyMode) {
            LocalLog.l(TAG, "MSG_THERMAL_CONTROL_SAFETY_PROTECT_PERIOD refresh.");
            sendThermalControlSafetyProtect();
        }
        return true;
    }

    public boolean isThermalControlTest() {
        return this.mThermalControlTest;
    }

    public void onDestory() {
        ThermalControlMessage.getInstance(this.mContext).destroy();
        this.mHandler.post(new Runnable() { // from class: com.oplus.thermalcontrol.b
            @Override // java.lang.Runnable
            public final void run() {
                ThermalControllerCenter.this.lambda$onDestory$1();
            }
        });
        this.mUtils.onDestory();
        this.mUtils.unregisterThermalBinder(ThermalStatusListener.getInstance(this.mContext));
    }

    public void onStart(Looper looper) {
        this.mHandler.post(new Runnable() { // from class: com.oplus.thermalcontrol.c
            @Override // java.lang.Runnable
            public final void run() {
                ThermalControllerCenter.this.lambda$onStart$0();
            }
        });
        this.mUtils.onStart(looper);
        ThermalControlMessage.getInstance(this.mContext).init(looper);
    }

    public void reloadGTstate(boolean z10) {
        this.mThermalControlConfig.noteGTState(z10);
        sendGtModeChangedMessage();
    }

    public void sendAmbientTempChangeMessage() {
        if (isThermalExcludedScene("AmbientTempChange")) {
            return;
        }
        this.mHandler.removeMessages(18);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 18;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendBackupHighPerformanceChangedMessage() {
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            this.mUtils.setHighTempSafetyState(0);
            LocalLog.l(TAG, "Thermal BackupRestoreChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to BackupRestore changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to BackupRestore changed.");
            return;
        }
        this.mHandler.removeMessages(9);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 9;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendBackupRestoreChangedMessage() {
        if (!ThermalControlUtils.getInstance(this.mContext).getUserForeground()) {
            LocalLog.a(TAG, "BackupRestoreChanged user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            this.mUtils.setHighTempSafetyState(0);
            LocalLog.l(TAG, "Thermal BackupRestoreChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to BackupRestore changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to BackupRestore changed.");
            return;
        }
        this.mHandler.removeMessages(5);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 5;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendCategoryChangedMessage() {
        int i10;
        int i11;
        if (!ThermalControlUtils.getInstance(this.mContext).getUserForeground()) {
            LocalLog.a(TAG, "CategoryChanged user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            this.mUtils.setHighTempSafetyState(0);
            LocalLog.l(TAG, "Thermal Temp CategoryChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (!this.mThermalControlConfig.isThermalControlEnable()) {
            stopCollingMethods();
            return;
        }
        if (this.mTempLevel <= this.mThermalControlConfig.getAppSwitchSafetyModeLevel()) {
            this.mAppSwitchSafetyMode = true;
        } else {
            this.mAppSwitchSafetyMode = false;
        }
        synchronized (this.mLock) {
            this.mThermalControlCategory = false;
            if (AppFeature.A()) {
                LocalLog.a(TAG, "ThermalControl in engineering test, not respond to category changed.");
            } else if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
                LocalLog.a(TAG, "ThermalControl in aging test, not respond to category changed.");
            } else {
                if (this.mPreThermalPolicy != null) {
                    this.mUtils.resetFps(this.mPrePackageName);
                }
                this.mCurThermalPolicy = this.mHandler.getThermalPolicy(this.mTempLevel, true, false);
                if (this.mHandler.hasMessages(1)) {
                    this.mHandler.removeMessages(1);
                }
                if (this.mHandler.hasMessages(3)) {
                    this.mHandler.removeMessages(3);
                }
                ThermalPolicy thermalPolicy = this.mCurThermalPolicy;
                if (thermalPolicy == null) {
                    if (!this.mThermalControlState) {
                        stopCategoryChangeMethods();
                    }
                    Message obtainMessage = this.mHandler.obtainMessage();
                    obtainMessage.what = 1;
                    this.mHandler.sendMessage(obtainMessage);
                } else {
                    ThermalPolicy thermalPolicy2 = this.mPreThermalPolicy;
                    if (thermalPolicy2 == null) {
                        if (thermalPolicy.cpu >= 0) {
                            LocalLog.l(TAG, "MSG_THERMAL_CONTROL_CATEGORY_PROTECT_PERIOD enter");
                            this.mThermalControlCategory = true;
                            this.mPrePackageName = this.mCurPackageName;
                            startCategoryChangeMethods(this.mCurThermalPolicy);
                            Message obtainMessage2 = this.mHandler.obtainMessage();
                            obtainMessage2.what = 3;
                            this.mHandler.sendMessageDelayed(obtainMessage2, this.mThermalControlConfig.getCategoryMsgDelayVal());
                        } else {
                            Message obtainMessage3 = this.mHandler.obtainMessage();
                            obtainMessage3.what = 1;
                            this.mHandler.sendMessage(obtainMessage3);
                        }
                    } else if (thermalPolicy.cpu <= thermalPolicy2.cpu && ((i10 = thermalPolicy.cpuPower) < 0 || ((i11 = thermalPolicy2.cpuPower) >= 0 && i10 >= i11))) {
                        int chargeLevel = this.mHeatSourceController.getChargeLevel(thermalPolicy);
                        if (chargeLevel >= 0) {
                            this.mUtils.setChargingLevel(chargeLevel, this.mCurThermalPolicy.speedChargeAdd, this.mCurPackageName);
                        }
                        Message obtainMessage4 = this.mHandler.obtainMessage();
                        obtainMessage4.what = 1;
                        this.mHandler.sendMessage(obtainMessage4);
                    } else {
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_CATEGORY_PROTECT_PERIOD enter.");
                        this.mThermalControlCategory = true;
                        this.mPrePackageName = this.mCurPackageName;
                        startCategoryChangeMethods(this.mCurThermalPolicy);
                        Message obtainMessage5 = this.mHandler.obtainMessage();
                        obtainMessage5.what = 3;
                        this.mHandler.sendMessageDelayed(obtainMessage5, this.mThermalControlConfig.getCategoryMsgDelayVal());
                    }
                }
            }
        }
    }

    public void sendChargeChangedMessage() {
        int chargeLevel;
        if (!ThermalControlUtils.getInstance(this.mContext).getUserForeground()) {
            LocalLog.a(TAG, "ChargeChanged user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        LocalLog.l(TAG, "sendChargeChangedMessage.");
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal Temp ChargeChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (!this.mThermalControlConfig.isThermalControlEnable()) {
            this.mUtils.setChargingLevel(0, 0, this.mCurPackageName);
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to charge changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to charge changed.");
            return;
        }
        synchronized (this.mLock) {
            ThermalPolicy thermalPolicy = this.mCurThermalPolicy;
            if (thermalPolicy != null && (chargeLevel = this.mHeatSourceController.getChargeLevel(thermalPolicy)) >= 0) {
                this.mUtils.setChargingLevel(chargeLevel, this.mCurThermalPolicy.speedChargeAdd, this.mCurPackageName);
            }
        }
    }

    public void sendCosaGameModeChangedMessage() {
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal CosaGameModeChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to BackupRestore changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to BackupRestore changed.");
            return;
        }
        this.mHandler.removeMessages(12);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 12;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendCosaRefreshSetChangedMessage() {
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond cosa refresh set changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to cosa refresh set changed.");
            return;
        }
        synchronized (this.mLock) {
            ThermalPolicy thermalPolicy = this.mCurThermalPolicy;
            if (thermalPolicy != null && thermalPolicy.fps > 0) {
                LocalLog.l(TAG, "sendCosaRefreshSetChangedMessage, fps:" + this.mCurThermalPolicy.fps + ",mCurPackageName:" + this.mCurPackageName);
                this.mUtils.setFps(this.mCurThermalPolicy.fps, this.mCurPackageName);
            }
        }
    }

    public void sendFloatWindowChangeMessage() {
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal FloatWindowChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to BackupRestore changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to BackupRestore changed.");
            return;
        }
        this.mHandler.removeMessages(16);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 16;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendFoldingModeMessage() {
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal FoldingModeChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to BackupRestore changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to BackupRestore changed.");
            return;
        }
        this.mHandler.removeMessages(13);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 13;
        this.mHandler.sendMessageDelayed(obtainMessage, 5000L);
    }

    public void sendGtModeChangedMessage() {
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal Temp GearChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to BackupRestore changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to BackupRestore changed.");
            return;
        }
        if (this.mHandler.hasMessages(15)) {
            this.mHandler.removeMessages(15);
        }
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 15;
        this.mHandler.sendMessageDelayed(obtainMessage, 500L);
    }

    public void sendSceneChangeMessage() {
        if (!ThermalControlUtils.getInstance(this.mContext).getUserForeground()) {
            LocalLog.a(TAG, "SceneChange user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal SceneChange: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to Scene Change.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to Scene Change.");
            return;
        }
        this.mHandler.removeMessages(10);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 10;
        this.mHandler.sendMessageDelayed(obtainMessage, 5000L);
    }

    public void sendScreenChangedMessage() {
        if (!ThermalControlUtils.getInstance(this.mContext).getUserForeground()) {
            LocalLog.a(TAG, "ScreenChanged user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal Temp ScreenChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to screen changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to screen changed.");
            return;
        }
        this.mHandler.removeMessages(4);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 4;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendScreenUnLockMessage() {
        if (!ThermalControlUtils.getInstance(this.mContext).getUserForeground()) {
            LocalLog.a(TAG, "ScreenUnLock user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal Temp ScreenUnLock: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to screen unlock.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to screen unlock.");
            return;
        }
        this.mHandler.removeMessages(8);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 8;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendSplitModeMessage() {
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            stopCollingMethods();
            LocalLog.l(TAG, "Thermal SplitModeChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.a(TAG, "ThermalControl in engineering test, not respond to BackupRestore changed.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.a(TAG, "ThermalControl in aging test, not respond to BackupRestore changed.");
            return;
        }
        this.mHandler.removeMessages(14);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 14;
        this.mHandler.sendMessageDelayed(obtainMessage, 5000L);
    }

    public void sendTempGearChangedMessage(int i10, long j10, String str) {
        try {
        } catch (Exception e10) {
            LocalLog.b(TAG, e10.toString());
        }
        if (ActivityManager.getCurrentUser() != this.mContext.getUserId()) {
            this.mUtils.setUserForeground(false);
            this.mTempLevel = i10;
            LocalLog.a(TAG, "TempGearChanged user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        if (!this.mUtils.getUserForeground()) {
            this.mUtils.setUserForeground(true);
            LocalLog.a(TAG, "user " + this.mContext.getUserId() + " is foreground but getUserForeground error");
        }
        if (this.mUtils.getDeviceProvisioned() != 1 && !this.mUtils.isBackupRestore()) {
            this.mTempLevel = i10;
            stopCollingMethods();
            this.mUtils.setHighTempSafetyState(0);
            LocalLog.l(TAG, "Thermal Temp GearChanged: Settings.Global.DEVICE_PROVISIONED=" + this.mUtils.getDeviceProvisioned());
            return;
        }
        if (AppFeature.A()) {
            LocalLog.l(TAG, "ThermalControl in engineering test, not respond to the bottom temperature report.");
            return;
        }
        if (AppFeature.z() && !this.mThermalControlConfig.isAgingThermalControlEnable()) {
            LocalLog.l(TAG, "ThermalControl in aging test, not respond to the bottom temperature report.");
            return;
        }
        if (this.mThermalControlTest) {
            LocalLog.l(TAG, "ThermalControl in test, not respond to the bottom temperature report.");
            return;
        }
        this.mHandler.removeMessages(2);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 2;
        obtainMessage.arg1 = i10;
        obtainMessage.obj = str;
        this.mHandler.sendMessageDelayed(obtainMessage, j10);
    }

    public void sendTempGearChangedMessageForTest(int i10, String str) {
        LocalLog.l(TAG, "sendTempGearChangedMessageForTest.");
        if (ActivityManager.getCurrentUser() != this.mContext.getUserId()) {
            this.mUtils.setUserForeground(false);
            LocalLog.a(TAG, "TempGearChanged user " + this.mContext.getUserId() + " is not foreground");
            return;
        }
        if (!this.mUtils.getUserForeground()) {
            this.mUtils.setUserForeground(true);
            LocalLog.a(TAG, "user " + this.mContext.getUserId() + " is foreground but getUserForeground error");
        }
        this.mHandler.removeMessages(2);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 2;
        obtainMessage.arg1 = i10;
        obtainMessage.obj = str;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void sendThermalControlSafetyProtect() {
        if (this.mHandler.hasMessages(17)) {
            this.mHandler.removeMessages(17);
        }
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 17;
        this.mHandler.sendMessageDelayed(obtainMessage, this.mThermalControlConfig.getSafetyProtectMsgDelayVal());
    }

    public void sendTsensorTempChangedMessage(int i10) {
        if (isThermalExcludedScene("TsensorTempChanged")) {
            return;
        }
        this.mHandler.removeMessages(21);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 21;
        obtainMessage.arg1 = i10;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void setThermalControlTest(boolean z10) {
        this.mThermalControlTest = z10;
    }

    public void start5GModemMethods(int i10) {
        LocalLog.a(TAG, "MSG_THERMAL_CONTROL_MODEM_PROTECT_PERIOD enter.");
        this.mUtils.set5GStatus(i10);
        this.mThermalControlModemAllow = false;
        if (this.mHandler.hasMessages(7)) {
            this.mHandler.removeMessages(7);
        }
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 7;
        this.mHandler.sendMessageDelayed(obtainMessage, 1200000L);
    }

    public void startBrightnessCoolingMethod(int i10, int i11) {
        int brightnessLevel = this.mHeatSourceController.getBrightnessLevel(i10);
        if (brightnessLevel >= 0) {
            if (brightnessLevel != this.mUtils.getCurrentBrightness()) {
                if (this.mTempLevel >= this.mThermalControlConfig.getSafetyThermalLevelVal()) {
                    String str = this.mCurPackageName + BRIGHTNESS_TEMP_PREFIX + i11 + "@" + DEFAULT_REASON_PRIORITY_FORCE;
                    if (y5.b.G()) {
                        this.mUtils.setSpecBrightness(brightnessLevel, str, 50);
                    } else {
                        this.mUtils.setSpecBrightness(brightnessLevel, str, -1);
                    }
                    this.mIsSetBrightnessForce = true;
                    return;
                }
                String str2 = this.mCurPackageName + BRIGHTNESS_TEMP_PREFIX + i11 + "@" + DEFAULT_REASON_PRIORITY_NORMAL;
                if (y5.b.G()) {
                    this.mUtils.setSpecBrightness(brightnessLevel, str2, 50);
                } else {
                    this.mUtils.setSpecBrightness(brightnessLevel, str2, -1);
                }
                this.mIsSetBrightnessForce = false;
                return;
            }
            if (this.mTempLevel >= this.mThermalControlConfig.getSafetyThermalLevelVal() && !this.mIsSetBrightnessForce) {
                String str3 = this.mCurPackageName + BRIGHTNESS_TEMP_PREFIX + i11 + "@" + DEFAULT_REASON_PRIORITY_FORCE;
                if (y5.b.G()) {
                    this.mUtils.setSpecBrightness(brightnessLevel, str3, 50);
                } else {
                    this.mUtils.setSpecBrightness(brightnessLevel, str3, -1);
                }
                this.mIsSetBrightnessForce = true;
                return;
            }
            if (this.mTempLevel >= this.mThermalControlConfig.getSafetyThermalLevelVal() || !this.mIsSetBrightnessForce) {
                return;
            }
            String str4 = this.mCurPackageName + BRIGHTNESS_TEMP_PREFIX + i11 + "@" + DEFAULT_REASON_PRIORITY_NORMAL;
            if (y5.b.G()) {
                this.mUtils.setSpecBrightness(brightnessLevel, str4, 50);
            } else {
                this.mUtils.setSpecBrightness(brightnessLevel, str4, -1);
            }
            this.mIsSetBrightnessForce = false;
            return;
        }
        this.mUtils.setSpecBrightness(0, this.mCurPackageName + BRIGHTNESS_TEMP_PREFIX + i11 + "@" + DEFAULT_REASON_PRIORITY_NORMAL, -1);
    }

    public void startCategoryChangeMethods(ThermalPolicy thermalPolicy) {
        int chargeLevel = this.mHeatSourceController.getChargeLevel(thermalPolicy);
        if (chargeLevel >= 0) {
            synchronized (this.mLock) {
                this.mUtils.setChargingLevel(chargeLevel, this.mCurThermalPolicy.speedChargeAdd, this.mCurPackageName);
            }
        }
        if (this.mThermalControlConfig.getAdditionValWithFloatWindow() > 0 && this.mTempLevel < this.mThermalControlConfig.getFloatWindowRefreshLevel()) {
            LocalLog.a(TAG, "Float Window, not set Refresh.");
            this.mUtils.setRefreshRate(0);
        } else if (this.mUtils.getRefreshRate(thermalPolicy.refreshRate) < this.mUtils.getCurrentRefreshRate()) {
            if (AppFeature.E()) {
                ThermalSmartRefreshUtil.f(this.mContext).i(thermalPolicy, true);
            } else {
                this.mUtils.setRefreshRate(thermalPolicy.refreshRate);
            }
        }
        int i10 = thermalPolicy.fps;
        if (i10 > 0) {
            this.mUtils.setFps(i10, this.mCurPackageName);
        }
        this.mUtils.setWifiSpeed(this.mHeatSourceController.getWifiSpeed(thermalPolicy), this.mTempLevel, this.mConfigItemName);
    }

    public void startCollingMethods(ThermalPolicy thermalPolicy, int i10, boolean z10, boolean z11) {
        if (thermalPolicy == null) {
            LocalLog.a(TAG, "thermalPolicy == null");
            stopCollingMethods();
            return;
        }
        LocalLog.l(TAG, "startCollingMethods,thermalPolicy=" + thermalPolicy);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "ambientMode:" + this.mThermalControlConfig.getAmbientPolicyModeVal() + ", ambientState:" + this.mUtils.getAmbientTempState());
        }
        this.mThermalControlState = true;
        if (this.mThermalControlConfig.getAdditionValWithFloatWindow() > 0 && this.mTempLevel < this.mThermalControlConfig.getFloatWindowRefreshLevel()) {
            LocalLog.a(TAG, "Float Window, not set Refresh.");
            this.mUtils.setRefreshRate(0);
        } else if (this.mPreThermalPolicy != null && this.mUtils.getRefreshRate(thermalPolicy.refreshRate) == this.mUtils.getCurrentRefreshRate()) {
            if (AppFeature.E()) {
                ThermalSmartRefreshUtil.f(this.mContext).i(thermalPolicy, false);
            }
        } else if (AppFeature.E()) {
            ThermalSmartRefreshUtil.f(this.mContext).i(thermalPolicy, true);
        } else {
            this.mUtils.setRefreshRate(thermalPolicy.refreshRate);
        }
        startModemCoolingMethod(thermalPolicy.modem);
        int chargeLevel = this.mHeatSourceController.getChargeLevel(thermalPolicy);
        if (chargeLevel >= 0) {
            this.mUtils.setChargingLevel(chargeLevel, thermalPolicy.speedChargeAdd, this.mCurPackageName);
        }
        startBrightnessCoolingMethod(thermalPolicy.brightness, i10);
        startCameraCoolingMethod(thermalPolicy);
        startWifiHotSpotCoolingMethod(thermalPolicy.disWifiHotSpot);
        if (thermalPolicy.disTorch == 1) {
            this.mUtils.setTorchState(false);
        } else {
            this.mUtils.setTorchState(true);
        }
        startFrameInsertCoolingMethod(thermalPolicy.disFrameInsert);
        startFpsCoolingMethod(thermalPolicy.fps);
        ThermalPolicy thermalPolicy2 = this.mPreThermalPolicy;
        if (thermalPolicy2 == null || thermalPolicy.disVideoSR != thermalPolicy2.disVideoSR) {
            this.mUtils.setVideosrControlState(thermalPolicy.disVideoSR);
        }
        this.mUtils.setWifiSpeed(this.mHeatSourceController.getWifiSpeed(thermalPolicy), this.mTempLevel, this.mConfigItemName);
        startCpuAndGpuCoolingMethod(thermalPolicy, i10);
        if (!isSafetyNeedChangeRestrict()) {
            startRestrictCoolingMethod(thermalPolicy.restrict, i10, z10, z11);
        }
        this.mPreThermalPolicy = thermalPolicy;
        this.mHandler.removeMessages(6);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 6;
        obtainMessage.arg1 = z10 ? 1 : 0;
        obtainMessage.arg2 = z11 ? 1 : 0;
        this.mHandler.sendMessage(obtainMessage);
        if (!z11 || this.mTempLevel < this.mThermalControlConfig.getThermalOptimizationReportLevel()) {
            return;
        }
        this.mUtils.onThermalOptimization();
    }

    public void startFrameInsertCoolingMethod(int i10) {
        if (!y5.b.F()) {
            if (i10 == 1) {
                this.mUtils.disableFrameInsert(true);
                return;
            } else {
                this.mUtils.disableFrameInsert(false);
                return;
            }
        }
        int intForUser = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "display_memc_game_enable", 0, 0);
        LocalLog.a(TAG, "disFrameInsert memcState  =" + intForUser);
        if (intForUser == 22) {
            Display display = this.mDisplay;
            int refreshRate = display != null ? (int) display.getMode().getRefreshRate() : 0;
            LocalLog.a(TAG, "disFrameInsert refreshRate =" + refreshRate);
            if (i10 == 2 && this.mInFrameInsertRL) {
                this.mInFrameInsertRL = false;
                if (refreshRate == 120) {
                    this.mUtils.requestRefreshRate(false, 3);
                    return;
                } else {
                    if (refreshRate == 90) {
                        this.mUtils.requestRefreshRate(false, 1);
                        return;
                    }
                    return;
                }
            }
            if (i10 == 3) {
                this.mInFrameInsertRL = true;
                if (refreshRate == 120) {
                    this.mUtils.requestRefreshRate(true, 3);
                } else if (refreshRate == 90) {
                    this.mUtils.requestRefreshRate(true, 1);
                }
            }
        }
    }

    public void startRestrictCoolingMethod(int i10, int i11, boolean z10, boolean z11) {
        int i12;
        if (this.mUtils.isBackupRestore()) {
            LocalLog.a(TAG, "is BackupResotre");
            return;
        }
        if (!z11) {
            LocalLog.a(TAG, "not TempGear Change, no setAppControlLevel");
            return;
        }
        if (this.mUtils.isAging() && i11 < 63) {
            LocalLog.a(TAG, "isAging,temp=" + i11);
            this.mUtils.startHighTemperature(0);
            this.mUtils.setAppControlLevel(0);
            return;
        }
        if (i10 == 3) {
            this.mUtils.startHighTemperature(i10);
            if (this.mThermalControlRestrictAllow && (i12 = this.mTempLevel) > this.mPreTempLevel && i12 == this.mThermalControlConfig.getRestrictTempGearVal()) {
                startRestrictMethods(i10);
            } else {
                LocalLog.l(TAG, "in restrict protect period, not set restrict");
            }
            this.mAlreadySwitchSafetyMode = false;
            return;
        }
        if (i10 == 5) {
            this.mUtils.startHighTemperature(i10);
            return;
        }
        if (i10 != 6) {
            if (i10 != 7) {
                this.mUtils.startHighTemperature(i10);
                this.mAlreadySwitchSafetyMode = false;
                return;
            }
            this.mUtils.startHighTemperature(i10);
            this.mAlreadySwitchSafetyMode = false;
            if (this.mThermalControlConfig.isSafetyOptimizeEnabled()) {
                LocalLog.a(TAG, "mAppSwitchSafetyMode=" + this.mAppSwitchSafetyMode + ",AmbientTemperature=" + this.mUtils.getAmbientTemperature());
                startZoomWindowRestrict(z11);
                return;
            }
            return;
        }
        if (this.mThermalControlConfig.isSafetyOptimizeEnabled()) {
            LocalLog.l(TAG, "mAppSwitchSafetyMode=" + this.mAppSwitchSafetyMode + ",AmbientTemperature=" + this.mUtils.getAmbientTemperature());
            if (this.mTempLevel > this.mPreTempLevel && this.mAppSwitchSafetyMode && isAmbientTempMeetConditions()) {
                this.mUtils.startHighTemperature(5);
                this.mAlreadySwitchSafetyMode = true;
                return;
            } else if (this.mAlreadySwitchSafetyMode) {
                LocalLog.a(TAG, "AlreadySwitchSafetyMode.");
                return;
            } else {
                this.mUtils.startHighTemperature(0);
                return;
            }
        }
        this.mUtils.startHighTemperature(i10);
    }

    public void startRestrictMethods(int i10) {
        LocalLog.a(TAG, "MSG_THERMAL_CONTROL_RESTRICT_PROTECT_PERIOD enter.");
        this.mUtils.setAppControlLevel(i10);
        this.mThermalControlRestrictAllow = false;
        this.mHandler.removeMessages(11);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 11;
        this.mHandler.sendMessageDelayed(obtainMessage, 1200000L);
    }

    public void stopCategoryChangeMethods() {
        if (this.mUtils.getChargingLevel() != 0) {
            this.mUtils.setChargingLevel(0, 0, this.mCurPackageName);
        }
        if (this.mUtils.getRefreshRate(0) < this.mUtils.getCurrentRefreshRate()) {
            this.mUtils.setRefreshRate(0);
        }
    }

    public void stopCollingMethods() {
        int i10;
        if (this.mThermalControlState) {
            LocalLog.l(TAG, "stopCollingMethods");
            int currentTemperature = (int) this.mUtils.getCurrentTemperature(false);
            this.mUtils.stopCpuAndGpuControl(currentTemperature, this.mTempLevel, this.mCurPackageName);
            int thermalSerious = this.mThermalControlConfig.getThermalSerious(this.mTempLevel, -2);
            this.mCurThermalSerious = thermalSerious;
            this.mUtils.stopCpuAndGpuControl(thermalSerious);
            this.mCurCpuPolicy = null;
            this.mCurGpuPolicy = null;
            this.mUtils.setCameraExitState(false);
            this.mUtils.setCameraVideoStopState(0);
            this.mUtils.setCameraFlashDisableState(false);
            this.mUtils.setCameraScreenBrightness(255);
            this.mUtils.setSpecBrightness(0, this.mCurPackageName + BRIGHTNESS_TEMP_PREFIX + currentTemperature + "@" + DEFAULT_REASON_PRIORITY_NORMAL, -1);
            this.mIsSetBrightnessForce = false;
            this.mUtils.setRefreshRate(0);
            this.mUtils.setVideosrControlState(0);
            this.mUtils.resetFps(this.mCurPackageName);
            this.mUtils.set5GStatus(0);
            this.mUtils.disableFrameInsert(false);
            this.mUtils.setChargingLevel(0, 0, this.mCurPackageName);
            this.mUtils.setTorchState(true);
            this.mUtils.disWifiHotSpot(false);
            this.mWifiHotControlState = false;
            this.mUtils.startHighTemperature(0);
            this.mUtils.setAppControlLevel(0);
            this.mUtils.setWifiSpeed(0, this.mTempLevel, this.mConfigItemName);
            this.mPreThermalPolicy = null;
            this.mThermalControlState = false;
            long currentTimeMillis = System.currentTimeMillis();
            boolean isScreenOn = this.mUtils.isScreenOn();
            boolean isCharging = this.mUtils.isCharging();
            if ((this.mUtils.isTraceMonitorOn() || this.mThermalControlMonitor.isOplusCameraOn()) && this.mAppStates != null) {
                ArrayList arrayList = new ArrayList();
                ThermalTraceInfo thermalTraceInfo = new ThermalTraceInfo(currentTimeMillis, this.mTempLevel, currentTemperature, this.mEnvTempType, isScreenOn, isCharging, this.mIsSafety, this.mSafetyState, this.mCurPackageName, this.mAppType, this.mAppStates.toString(), this.mUserMode, this.mUtils.getFoldingMode(), this.mUtils.isSplitMode(), this.mUtils.isSpeedCharging(), this.mAppSwitchSafetyMode, this.mUtils.getAmbientTemperature(), this.mUtils.getAmbientTempState(), null, this.mUtils.getZoomWindowsInfo(arrayList), this.mUtils.getPipWindowsInfo(arrayList), this.mUtils.getAppFloatingWindowsInfo(arrayList));
                Intent intent = new Intent(EAP_INTENT_NAME);
                intent.putExtra("ThermalTraceInfo", new GsonBuilder().setPrettyPrinting().create().toJson(thermalTraceInfo));
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, EAP_SAFE_PERMISSION);
            }
        }
        if (this.mUtils.getChargingLevel() != 0) {
            i10 = 0;
            this.mUtils.setChargingLevel(0, 0, this.mCurPackageName);
        } else {
            i10 = 0;
        }
        if (y5.b.E() && Settings.System.getIntForUser(this.mContext.getContentResolver(), "gt_mode_state_setting", i10, i10) == 1) {
            LocalLog.l(TAG, "stopCollingMethods reset gt fps mCurPackageName =" + this.mCurPackageName);
            this.mUtils.resetFps(this.mCurPackageName);
        }
    }

    /* loaded from: classes2.dex */
    public class ThermalControlHandler extends Handler {
        private static final String TAG = "ThermalControllHandler";
        private Context mContext;
        private Looper mLooper;
        private int mNextAmbientState;

        public ThermalControlHandler(Context context, Looper looper) {
            super(looper);
            this.mNextAmbientState = -1;
            this.mContext = context;
            this.mLooper = looper;
        }

        private int getNextAmbientState(boolean z10, int i10, int i11) {
            int i12;
            ThermalAmbientPolicy thermalAmbientPolicy;
            ThermalAmbientPolicy thermalAmbientPolicy2;
            if (z10) {
                do {
                    int i13 = i10;
                    i10--;
                    i12 = i13;
                    if (i10 < -3) {
                        return i12;
                    }
                    thermalAmbientPolicy = ThermalControllerCenter.this.mThermalControlConfig.getThermalAmbientPolicy(i10);
                    if (thermalAmbientPolicy == null) {
                        return i12;
                    }
                } while (i11 <= thermalAmbientPolicy.fallBackTemp);
                return i10;
            }
            do {
                int i14 = i10;
                i10++;
                i12 = i14;
                if (i10 > 7 || (thermalAmbientPolicy2 = ThermalControllerCenter.this.mThermalControlConfig.getThermalAmbientPolicy(i10)) == null) {
                    break;
                }
            } while (i11 >= thermalAmbientPolicy2.triggerTemp);
            return i12;
        }

        private void sendAmbientTempCheckMsg(boolean z10, int i10, int i11, long j10) {
            int nextAmbientState = getNextAmbientState(z10, i10, i11);
            if (nextAmbientState == i10) {
                if (ThermalControllerCenter.this.mHandler.hasMessages(19)) {
                    ThermalControllerCenter.this.mHandler.removeMessages(19);
                }
                LocalLog.l(TAG, "Same as last ambient state");
                return;
            }
            if (ThermalControllerCenter.this.mHandler.hasMessages(19)) {
                if (this.mNextAmbientState == nextAmbientState) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("ambient ");
                    sb2.append(z10 ? "fallback to " : "trigger to ");
                    sb2.append(nextAmbientState);
                    sb2.append(" msg is on the way, waiting...");
                    LocalLog.l(TAG, sb2.toString());
                    return;
                }
                ThermalControllerCenter.this.mHandler.removeMessages(19);
            }
            this.mNextAmbientState = nextAmbientState;
            Message obtain = Message.obtain();
            obtain.what = 19;
            obtain.arg1 = nextAmbientState;
            ThermalControllerCenter.this.mHandler.sendMessageDelayed(obtain, j10);
            LocalLog.l(TAG, "ambient next state: " + nextAmbientState + ", checkDelay=" + j10);
        }

        public ThermalPolicy getThermalPolicy(int i10, boolean z10, boolean z11) {
            ThermalControllerCenter thermalControllerCenter = ThermalControllerCenter.this;
            thermalControllerCenter.mCurPackageName = thermalControllerCenter.mUtils.getForegroundPkg();
            ThermalControllerCenter thermalControllerCenter2 = ThermalControllerCenter.this;
            thermalControllerCenter2.mAppType = thermalControllerCenter2.mUtils.getAppType(ThermalControllerCenter.this.mCurPackageName);
            ThermalControllerCenter thermalControllerCenter3 = ThermalControllerCenter.this;
            thermalControllerCenter3.mAppStates = thermalControllerCenter3.mUtils.getAppState(ThermalControllerCenter.this.mCurPackageName);
            ThermalControllerCenter thermalControllerCenter4 = ThermalControllerCenter.this;
            thermalControllerCenter4.mCurThermalPolicy = getThermalPolicy(i10, thermalControllerCenter4.mCurPackageName, ThermalControllerCenter.this.mAppType, ThermalControllerCenter.this.mAppStates, z10, z11);
            if (ThermalControllerCenter.this.mCurThermalPolicy == null) {
                return null;
            }
            if (ThermalControllerCenter.this.mUtils.isFoldingMode() && ThermalControllerCenter.this.mUtils.isSplitMode()) {
                String splitForegroundPkg = ThermalControllerCenter.this.mUtils.getSplitForegroundPkg();
                int appType = ThermalControllerCenter.this.mUtils.getAppType(splitForegroundPkg);
                List<Integer> appState = ThermalControllerCenter.this.mUtils.getAppState(splitForegroundPkg);
                ThermalPolicy thermalPolicy = getThermalPolicy(i10, splitForegroundPkg, appType, appState, z10, z11);
                if (thermalPolicy == null) {
                    if (ThermalControllerCenter.this.mCurThermalPolicy.cpu >= 0) {
                        ThermalControllerCenter.this.mCurPackageName = splitForegroundPkg;
                        ThermalControllerCenter.this.mAppType = appType;
                        ThermalControllerCenter.this.mAppStates = appState;
                        ThermalControllerCenter.this.mCurThermalPolicy = null;
                    }
                } else if (ThermalControllerCenter.this.mCurThermalPolicy.cpu > thermalPolicy.cpu) {
                    ThermalControllerCenter.this.mCurPackageName = splitForegroundPkg;
                    ThermalControllerCenter.this.mAppType = appType;
                    ThermalControllerCenter.this.mAppStates = appState;
                    ThermalControllerCenter.this.mCurThermalPolicy = thermalPolicy;
                }
            }
            return ThermalControllerCenter.this.mCurThermalPolicy;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (ThermalControllerCenter.this.mLock) {
                switch (message.what) {
                    case 0:
                        ThermalControllerCenter.this.mUtils.registerThermalBinder(ThermalStatusListener.getInstance(this.mContext));
                        ThermalControllerCenter.this.mUtils.setIpaFeatureState(ThermalControllerCenter.this.mThermalControlConfig.isIpaFeatureEnable());
                        if (AppFeature.A()) {
                            LocalLog.l(TAG, "ThermalControl in engineering test.");
                            break;
                        } else if (AppFeature.z() && !ThermalControllerCenter.this.mThermalControlConfig.isAgingThermalControlEnable()) {
                            LocalLog.a(TAG, "ThermalControl in aging test.");
                            break;
                        } else if (ThermalControllerCenter.this.mThermalControlTest) {
                            LocalLog.a(TAG, "ThermalControl in test.");
                            break;
                        } else {
                            ThermalControllerCenter thermalControllerCenter = ThermalControllerCenter.this;
                            thermalControllerCenter.mTempLevel = thermalControllerCenter.mUtils.getCurrentThermalStatus();
                            LocalLog.a(TAG, "temp level = " + ThermalControllerCenter.this.mTempLevel);
                            ThermalControllerCenter thermalControllerCenter2 = ThermalControllerCenter.this;
                            thermalControllerCenter2.mPreTempLevel = thermalControllerCenter2.mTempLevel;
                            if (ThermalControllerCenter.this.mUtils.getDeviceProvisioned() != 1 && !ThermalControllerCenter.this.mUtils.isBackupRestore()) {
                                ThermalControllerCenter.this.stopCollingMethods();
                                ThermalControllerCenter.this.mUtils.setHighTempSafetyState(0);
                                LocalLog.l(TAG, "Thermal Control: Settings.Global.DEVICE_PROVISIONED=" + ThermalControllerCenter.this.mUtils.getDeviceProvisioned());
                                break;
                            } else {
                                if (ThermalControllerCenter.this.isSafetyStateExcludedScene()) {
                                    ThermalControllerCenter.this.mUtils.setHighTempSafetyState(0);
                                } else {
                                    ThermalControllerCenter.this.mUtils.setHighTempSafetyState(1);
                                }
                                if (!ThermalControllerCenter.this.mThermalControlConfig.isThermalControlEnable()) {
                                    ThermalControllerCenter.this.stopCollingMethods();
                                    break;
                                } else if (ThermalControllerCenter.this.mPreTempLevel < 0) {
                                    ThermalControllerCenter.this.stopCollingMethods();
                                    break;
                                } else {
                                    ThermalControllerCenter.this.mCurPackageName = CommonUtil.y();
                                    ThermalControllerCenter thermalControllerCenter3 = ThermalControllerCenter.this;
                                    thermalControllerCenter3.mPrePackageName = thermalControllerCenter3.mCurPackageName;
                                    ThermalControllerCenter thermalControllerCenter4 = ThermalControllerCenter.this;
                                    thermalControllerCenter4.mAppType = thermalControllerCenter4.mUtils.getAppType(ThermalControllerCenter.this.mPrePackageName);
                                    ThermalControllerCenter thermalControllerCenter5 = ThermalControllerCenter.this;
                                    thermalControllerCenter5.mAppStates = thermalControllerCenter5.mUtils.getAppState(ThermalControllerCenter.this.mPrePackageName);
                                    ThermalControllerCenter thermalControllerCenter6 = ThermalControllerCenter.this;
                                    thermalControllerCenter6.mPreUserMode = thermalControllerCenter6.mUtils.getUserMode();
                                    ThermalControllerCenter thermalControllerCenter7 = ThermalControllerCenter.this;
                                    thermalControllerCenter7.mPreGameUserMode = thermalControllerCenter7.mUtils.getGameUserMode();
                                    ThermalControllerCenter thermalControllerCenter8 = ThermalControllerCenter.this;
                                    thermalControllerCenter8.mEnvTempType = thermalControllerCenter8.getEnvironmentTemperatureType();
                                    ThermalControllerCenter thermalControllerCenter9 = ThermalControllerCenter.this;
                                    thermalControllerCenter9.mIsSafety = thermalControllerCenter9.isSafetyScenario();
                                    ThermalControllerCenter thermalControllerCenter10 = ThermalControllerCenter.this;
                                    thermalControllerCenter10.mTempGear = thermalControllerCenter10.getTempGear(thermalControllerCenter10.mAppType, ThermalControllerCenter.this.mPreTempLevel, ThermalControllerCenter.this.mPreUserMode, ThermalControllerCenter.this.mPreGameUserMode);
                                    if (ThermalControllerCenter.this.isSafetymode()) {
                                        ThermalControllerCenter thermalControllerCenter11 = ThermalControllerCenter.this;
                                        ThermalControlConfig unused = thermalControllerCenter11.mThermalControlConfig;
                                        thermalControllerCenter11.mCategoryIndex = 1;
                                        ThermalControllerCenter thermalControllerCenter12 = ThermalControllerCenter.this;
                                        ThermalControlConfig unused2 = thermalControllerCenter12.mThermalControlConfig;
                                        thermalControllerCenter12.mConfigItemName = ThermalControlConfig.ORIGINAL_SAFETY_TEST_ITEM;
                                        ThermalControllerCenter thermalControllerCenter13 = ThermalControllerCenter.this;
                                        thermalControllerCenter13.mTempGear = thermalControllerCenter13.mPreTempLevel + ThermalControllerCenter.this.mThermalControlConfig.getOtaThermalControlVal();
                                    } else if (ThermalControllerCenter.this.mAppStates != null && !ThermalControllerCenter.this.mAppStates.isEmpty()) {
                                        if (ThermalControllerCenter.this.mAppStates.contains(104)) {
                                            ThermalControllerCenter thermalControllerCenter14 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused3 = thermalControllerCenter14.mThermalControlConfig;
                                            thermalControllerCenter14.mCategoryIndex = 3;
                                            ThermalControllerCenter.this.mConfigItemName = "scene_104";
                                        } else if (ThermalControllerCenter.this.mAppStates.contains(105)) {
                                            ThermalControllerCenter thermalControllerCenter15 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused4 = thermalControllerCenter15.mThermalControlConfig;
                                            thermalControllerCenter15.mCategoryIndex = 3;
                                            ThermalControllerCenter.this.mConfigItemName = "scene_105";
                                        } else if (ThermalControllerCenter.this.mAppStates.contains(103)) {
                                            ThermalControllerCenter thermalControllerCenter16 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused5 = thermalControllerCenter16.mThermalControlConfig;
                                            thermalControllerCenter16.mCategoryIndex = 3;
                                            ThermalControllerCenter.this.mConfigItemName = "scene_103";
                                        } else if (ThermalControllerCenter.this.mUtils.isPhoneInCall(this.mContext)) {
                                            ThermalControllerCenter thermalControllerCenter17 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused6 = thermalControllerCenter17.mThermalControlConfig;
                                            thermalControllerCenter17.mCategoryIndex = 3;
                                            ThermalControllerCenter.this.mConfigItemName = "scene_102";
                                        } else if (ThermalControllerCenter.this.mAppStates.contains(102)) {
                                            ThermalControllerCenter thermalControllerCenter18 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused7 = thermalControllerCenter18.mThermalControlConfig;
                                            thermalControllerCenter18.mCategoryIndex = 3;
                                            ThermalControllerCenter.this.mConfigItemName = "scene_102";
                                        } else if (ThermalControllerCenter.this.mAppStates.contains(101)) {
                                            ThermalControllerCenter thermalControllerCenter19 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused8 = thermalControllerCenter19.mThermalControlConfig;
                                            thermalControllerCenter19.mCategoryIndex = 3;
                                            ThermalControllerCenter.this.mConfigItemName = "scene_101";
                                        } else if (ThermalControllerCenter.this.mAppStates.contains(106)) {
                                            ThermalControllerCenter thermalControllerCenter20 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused9 = thermalControllerCenter20.mThermalControlConfig;
                                            thermalControllerCenter20.mCategoryIndex = 3;
                                            ThermalControllerCenter.this.mConfigItemName = "scene_16";
                                        } else if (ThermalControllerCenter.this.mAppType > 0) {
                                            ThermalControllerCenter thermalControllerCenter21 = ThermalControllerCenter.this;
                                            ThermalControlConfig unused10 = thermalControllerCenter21.mThermalControlConfig;
                                            thermalControllerCenter21.mCategoryIndex = 4;
                                            ThermalControllerCenter.this.mPreAppCategory = "category_" + ThermalControllerCenter.this.mAppType;
                                            ThermalControllerCenter thermalControllerCenter22 = ThermalControllerCenter.this;
                                            thermalControllerCenter22.mConfigItemName = thermalControllerCenter22.mPreAppCategory;
                                        }
                                    } else if (ThermalControllerCenter.this.mUtils.isPhoneInCall(this.mContext)) {
                                        ThermalControllerCenter thermalControllerCenter23 = ThermalControllerCenter.this;
                                        thermalControllerCenter23.mTempGear = thermalControllerCenter23.mPreTempLevel + ThermalControllerCenter.this.mThermalControlConfig.getAdditionValWithModeType(ThermalControllerCenter.this.mPreUserMode) + ThermalControllerCenter.this.mThermalControlConfig.getAdditionValWithModeType(ThermalControllerCenter.this.mPreGameUserMode) + ThermalControllerCenter.this.mThermalControlConfig.getOtaThermalControlVal() + ThermalControllerCenter.this.mThermalControlConfig.getAdditionValWithFloatWindow();
                                        ThermalControllerCenter thermalControllerCenter24 = ThermalControllerCenter.this;
                                        ThermalControlConfig unused11 = thermalControllerCenter24.mThermalControlConfig;
                                        thermalControllerCenter24.mCategoryIndex = 3;
                                        ThermalControllerCenter.this.mConfigItemName = "scene_102";
                                    }
                                    if (ThermalControllerCenter.this.mAppType > 0) {
                                        ThermalControllerCenter.this.mPreAppCategory = "category_" + ThermalControllerCenter.this.mAppType;
                                    }
                                    boolean isScreenOn = ThermalControllerCenter.this.mUtils.isScreenOn();
                                    ThermalControllerCenter thermalControllerCenter25 = ThermalControllerCenter.this;
                                    thermalControllerCenter25.mCurThermalPolicy = thermalControllerCenter25.mThermalControlConfig.getThermalPolicyInfo(ThermalControllerCenter.this.mCategoryIndex, ThermalControllerCenter.this.mConfigItemName, ThermalControllerCenter.this.mPrePackageName, ThermalControllerCenter.this.mPreAppCategory, ThermalControllerCenter.this.mTempGear, isScreenOn);
                                    int currentTemperature = (int) ThermalControllerCenter.this.mUtils.getCurrentTemperature(true);
                                    ThermalControllerCenter thermalControllerCenter26 = ThermalControllerCenter.this;
                                    thermalControllerCenter26.mUserMode = thermalControllerCenter26.mPreUserMode;
                                    if (ThermalControllerCenter.this.isSafetyNeedChangeRestrict()) {
                                        ThermalPolicy thermalPolicyInfo = ThermalControllerCenter.this.mThermalControlConfig.getThermalPolicyInfo(ThermalControllerCenter.this.mCategoryIndex, ThermalControllerCenter.this.mConfigItemName, ThermalControllerCenter.this.mPrePackageName, ThermalControllerCenter.this.mPreAppCategory, ThermalControllerCenter.this.mTempLevel, isScreenOn);
                                        if (thermalPolicyInfo == null) {
                                            for (int i10 = ThermalControllerCenter.this.mTempLevel; i10 >= 0; i10--) {
                                                thermalPolicyInfo = ThermalControllerCenter.this.mThermalControlConfig.getThermalPolicyInfo(ThermalControllerCenter.this.mCategoryIndex, ThermalControllerCenter.this.mConfigItemName, ThermalControllerCenter.this.mPrePackageName, ThermalControllerCenter.this.mPreAppCategory, i10, isScreenOn);
                                                if (thermalPolicyInfo == null) {
                                                }
                                            }
                                        }
                                        if (thermalPolicyInfo != null) {
                                            ThermalControllerCenter.this.mSafeRestrictLevel = thermalPolicyInfo.restrict;
                                        } else {
                                            ThermalControllerCenter.this.mSafeRestrictLevel = 0;
                                        }
                                        ThermalControllerCenter thermalControllerCenter27 = ThermalControllerCenter.this;
                                        thermalControllerCenter27.startRestrictCoolingMethod(thermalControllerCenter27.mSafeRestrictLevel, currentTemperature, false, false);
                                    }
                                    ThermalControllerCenter thermalControllerCenter28 = ThermalControllerCenter.this;
                                    thermalControllerCenter28.startCollingMethods(thermalControllerCenter28.mCurThermalPolicy, currentTemperature, false, false);
                                    break;
                                }
                            }
                        }
                        break;
                    case 1:
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_CATEGORY_CHANGED");
                        if (!ThermalControllerCenter.this.mThermalControlConfig.isThermalControlEnable()) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else if (ThermalControllerCenter.this.mTempLevel < 0) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else {
                            boolean updateHeatSourceOffsets = ThermalControllerCenter.this.mHeatSourceController.updateHeatSourceOffsets(ThermalControllerCenter.this.mCurThermalPolicy, false);
                            boolean updateTsensorSourceLevel = ThermalControllerCenter.this.mHeatSourceController.updateTsensorSourceLevel();
                            if (ThermalControllerCenter.this.mCurThermalPolicy == null) {
                                ThermalControllerCenter.this.stopCollingMethods();
                                LocalLog.a(TAG, "mCurThermalPolicy is null");
                            } else if (!ThermalControllerCenter.this.mCurThermalPolicy.equals(ThermalControllerCenter.this.mPreThermalPolicy)) {
                                ThermalControllerCenter thermalControllerCenter29 = ThermalControllerCenter.this;
                                thermalControllerCenter29.mTemp = (int) thermalControllerCenter29.mUtils.getCurrentTemperature(false);
                                ThermalControllerCenter thermalControllerCenter30 = ThermalControllerCenter.this;
                                thermalControllerCenter30.startCollingMethods(thermalControllerCenter30.mCurThermalPolicy, ThermalControllerCenter.this.mTemp, true, false);
                            } else {
                                if (ThermalControllerCenter.this.mThermalControlConfig.getAdditionValWithFloatWindow() > 0 && ThermalControllerCenter.this.mTempLevel < ThermalControllerCenter.this.mThermalControlConfig.getFloatWindowRefreshLevel()) {
                                    LocalLog.a(TAG, "Float Window, not set Refresh.");
                                    ThermalControllerCenter.this.mUtils.setRefreshRate(0);
                                } else if (ThermalControllerCenter.this.mUtils.getRefreshRate(ThermalControllerCenter.this.mCurThermalPolicy.refreshRate) != ThermalControllerCenter.this.mUtils.getCurrentRefreshRate()) {
                                    if (AppFeature.E()) {
                                        ThermalSmartRefreshUtil.f(this.mContext).i(ThermalControllerCenter.this.mCurThermalPolicy, true);
                                    } else {
                                        ThermalControllerCenter.this.mUtils.setRefreshRate(ThermalControllerCenter.this.mCurThermalPolicy.refreshRate);
                                    }
                                }
                                if (ThermalControllerCenter.this.mCurThermalPolicy.fps > 0) {
                                    ThermalControllerCenter.this.mUtils.setFps(ThermalControllerCenter.this.mCurThermalPolicy.fps, ThermalControllerCenter.this.mCurPackageName);
                                }
                                ThermalControllerCenter.this.startHeatOffsetMethod(updateHeatSourceOffsets, updateTsensorSourceLevel);
                                LocalLog.a(TAG, "Same as last policy");
                            }
                            ThermalControllerCenter thermalControllerCenter31 = ThermalControllerCenter.this;
                            thermalControllerCenter31.mPreUserMode = thermalControllerCenter31.mUserMode;
                            ThermalControllerCenter thermalControllerCenter32 = ThermalControllerCenter.this;
                            thermalControllerCenter32.mPreGameUserMode = thermalControllerCenter32.mGameUserMode;
                            ThermalControllerCenter thermalControllerCenter33 = ThermalControllerCenter.this;
                            thermalControllerCenter33.mPrePackageName = thermalControllerCenter33.mCurPackageName;
                            break;
                        }
                    case 2:
                        ThermalControllerCenter thermalControllerCenter34 = ThermalControllerCenter.this;
                        thermalControllerCenter34.mPreTempLevel = thermalControllerCenter34.mTempLevel;
                        ThermalControllerCenter.this.mTempLevel = message.arg1;
                        if (ThermalControllerCenter.this.isSafetyStateExcludedScene()) {
                            ThermalControllerCenter.this.mUtils.setHighTempSafetyState(0);
                        } else {
                            ThermalControllerCenter.this.mUtils.setHighTempSafetyState(1);
                        }
                        ThermalControllerCenter thermalControllerCenter35 = ThermalControllerCenter.this;
                        thermalControllerCenter35.mTemp = (int) thermalControllerCenter35.mUtils.getCurrentTemperature(false);
                        Map<String, Integer> updateHeatSourceMap = ThermalControllerCenter.this.mHeatSourceController.updateHeatSourceMap(message.obj);
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_TEMPGEAR_CHANGED,tempLevel=" + ThermalControllerCenter.this.mTempLevel + ",mTemp=" + ThermalControllerCenter.this.mTemp + ", heatSourceMap=" + updateHeatSourceMap);
                        if (!ThermalControllerCenter.this.mThermalControlConfig.isThermalControlEnable()) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else {
                            ThermalControllerCenter.this.mUtils.sendThermalLevelChangeBroadcast(ThermalControllerCenter.this.mTempLevel, ThermalControllerCenter.this.mTemp, updateHeatSourceMap);
                            if (ThermalControllerCenter.this.mTempLevel < 0) {
                                ThermalControllerCenter.this.stopCollingMethods();
                                break;
                            } else {
                                ThermalControllerCenter thermalControllerCenter36 = ThermalControllerCenter.this;
                                thermalControllerCenter36.mCurThermalPolicy = getThermalPolicy(thermalControllerCenter36.mTempLevel, false, true);
                                if (ThermalControllerCenter.this.mThermalControlCategory) {
                                    LocalLog.a(TAG, "ThermalControl in category change protect, not respond to the bottom temperature report. ");
                                    if (!ThermalControllerCenter.this.isSafetyNeedChangeRestrict()) {
                                        ThermalControllerCenter thermalControllerCenter37 = ThermalControllerCenter.this;
                                        thermalControllerCenter37.startRestrictCoolingMethod(thermalControllerCenter37.mCurThermalPolicy != null ? ThermalControllerCenter.this.mCurThermalPolicy.restrict : 0, ThermalControllerCenter.this.mTemp, false, true);
                                        break;
                                    }
                                } else {
                                    boolean updateHeatSourceOffsets2 = ThermalControllerCenter.this.mHeatSourceController.updateHeatSourceOffsets(ThermalControllerCenter.this.mCurThermalPolicy, true);
                                    ThermalControllerCenter.this.mHeatSourceController.updateTsensorSourceLevel();
                                    if (ThermalControllerCenter.this.mCurThermalPolicy == null) {
                                        ThermalControllerCenter.this.stopCollingMethods();
                                        ThermalControllerCenter.this.mUtils.stopCpuAndGpuControl(ThermalControllerCenter.this.mTemp, ThermalControllerCenter.this.mTempLevel, ThermalControllerCenter.this.mCurPackageName);
                                        ThermalControllerCenter thermalControllerCenter38 = ThermalControllerCenter.this;
                                        thermalControllerCenter38.mCurThermalSerious = thermalControllerCenter38.mThermalControlConfig.getThermalSerious(ThermalControllerCenter.this.mTempLevel, -2);
                                        ThermalControllerCenter.this.mUtils.stopCpuAndGpuControl(ThermalControllerCenter.this.mCurThermalSerious);
                                        ThermalControllerCenter.this.mCurCpuPolicy = null;
                                        ThermalControllerCenter.this.mCurGpuPolicy = null;
                                        LocalLog.a(TAG, ThermalControllerCenter.CUR_THERMAL_POLICY_IS_NULL);
                                    } else if (!ThermalControllerCenter.this.mCurThermalPolicy.equals(ThermalControllerCenter.this.mPreThermalPolicy)) {
                                        ThermalControllerCenter thermalControllerCenter39 = ThermalControllerCenter.this;
                                        thermalControllerCenter39.startCollingMethods(thermalControllerCenter39.mCurThermalPolicy, ThermalControllerCenter.this.mTemp, false, true);
                                    } else {
                                        ThermalControllerCenter thermalControllerCenter40 = ThermalControllerCenter.this;
                                        thermalControllerCenter40.startCpuAndGpuCoolingMethod(thermalControllerCenter40.mCurThermalPolicy, ThermalControllerCenter.this.mTemp);
                                        if (updateHeatSourceOffsets2) {
                                            ThermalControllerCenter thermalControllerCenter41 = ThermalControllerCenter.this;
                                            thermalControllerCenter41.startModemCoolingMethod(thermalControllerCenter41.mCurThermalPolicy.modem);
                                            ThermalControllerCenter thermalControllerCenter42 = ThermalControllerCenter.this;
                                            thermalControllerCenter42.startBrightnessCoolingMethod(thermalControllerCenter42.mCurThermalPolicy.brightness, ThermalControllerCenter.this.mTemp);
                                        }
                                        if (!ThermalControllerCenter.this.isSafetyNeedChangeRestrict()) {
                                            int i11 = ThermalControllerCenter.this.mCurThermalPolicy != null ? ThermalControllerCenter.this.mCurThermalPolicy.restrict : 0;
                                            if (i11 != ThermalControllerCenter.this.mUtils.getAppControlLevel()) {
                                                ThermalControllerCenter thermalControllerCenter43 = ThermalControllerCenter.this;
                                                thermalControllerCenter43.startRestrictCoolingMethod(i11, thermalControllerCenter43.mTemp, false, true);
                                            }
                                        }
                                        LocalLog.a(TAG, "Same as last policy");
                                    }
                                    ThermalControllerCenter thermalControllerCenter44 = ThermalControllerCenter.this;
                                    thermalControllerCenter44.mPreUserMode = thermalControllerCenter44.mUserMode;
                                    ThermalControllerCenter thermalControllerCenter45 = ThermalControllerCenter.this;
                                    thermalControllerCenter45.mPreGameUserMode = thermalControllerCenter45.mGameUserMode;
                                    ThermalControllerCenter thermalControllerCenter46 = ThermalControllerCenter.this;
                                    thermalControllerCenter46.mPrePackageName = thermalControllerCenter46.mCurPackageName;
                                    break;
                                }
                            }
                        }
                        break;
                    case 3:
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_CATEGORY_PROTECT_PERIOD quit.");
                        ThermalControllerCenter.this.mThermalControlCategory = false;
                        if (!ThermalControllerCenter.this.mThermalControlConfig.isThermalControlEnable()) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else if (ThermalControllerCenter.this.mTempLevel < 0) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else {
                            ThermalControllerCenter thermalControllerCenter47 = ThermalControllerCenter.this;
                            thermalControllerCenter47.mCurThermalPolicy = getThermalPolicy(thermalControllerCenter47.mTempLevel, true, false);
                            boolean updateHeatSourceOffsets3 = ThermalControllerCenter.this.mHeatSourceController.updateHeatSourceOffsets(ThermalControllerCenter.this.mCurThermalPolicy, false);
                            boolean updateTsensorSourceLevel2 = ThermalControllerCenter.this.mHeatSourceController.updateTsensorSourceLevel();
                            if (ThermalControllerCenter.this.mCurThermalPolicy == null) {
                                ThermalControllerCenter.this.stopCollingMethods();
                                LocalLog.a(TAG, ThermalControllerCenter.CUR_THERMAL_POLICY_IS_NULL);
                            } else if (!ThermalControllerCenter.this.mCurThermalPolicy.equals(ThermalControllerCenter.this.mPreThermalPolicy)) {
                                ThermalControllerCenter thermalControllerCenter48 = ThermalControllerCenter.this;
                                thermalControllerCenter48.startCollingMethods(thermalControllerCenter48.mCurThermalPolicy, ThermalControllerCenter.this.mTemp, true, false);
                            } else {
                                LocalLog.a(TAG, "Same as last policy");
                                if (ThermalControllerCenter.this.mThermalControlConfig.getAdditionValWithFloatWindow() > 0 && ThermalControllerCenter.this.mTempLevel < ThermalControllerCenter.this.mThermalControlConfig.getFloatWindowRefreshLevel()) {
                                    LocalLog.a(TAG, "Float Window, not set Refresh.");
                                    ThermalControllerCenter.this.mUtils.setRefreshRate(0);
                                } else if (ThermalControllerCenter.this.mUtils.getRefreshRate(ThermalControllerCenter.this.mCurThermalPolicy.refreshRate) != ThermalControllerCenter.this.mUtils.getCurrentRefreshRate()) {
                                    if (AppFeature.E()) {
                                        ThermalSmartRefreshUtil.f(this.mContext).i(ThermalControllerCenter.this.mCurThermalPolicy, true);
                                    } else {
                                        ThermalControllerCenter.this.mUtils.setRefreshRate(ThermalControllerCenter.this.mCurThermalPolicy.refreshRate);
                                    }
                                }
                                int chargeLevel = ThermalControllerCenter.this.mHeatSourceController.getChargeLevel(ThermalControllerCenter.this.mCurThermalPolicy);
                                if (chargeLevel != ThermalControllerCenter.this.mUtils.getPolicyChargeLevel() || ThermalControllerCenter.this.mCurThermalPolicy.speedChargeAdd != ThermalControllerCenter.this.mUtils.getSpeedChargeAddLevel()) {
                                    ThermalControllerCenter.this.mUtils.setChargingLevel(chargeLevel, ThermalControllerCenter.this.mCurThermalPolicy.speedChargeAdd, ThermalControllerCenter.this.mCurPackageName);
                                }
                                if (ThermalControllerCenter.this.mCurThermalPolicy.fps > 0) {
                                    ThermalControllerCenter.this.mUtils.setFps(ThermalControllerCenter.this.mCurThermalPolicy.fps, ThermalControllerCenter.this.mCurPackageName);
                                } else {
                                    ThermalControllerCenter.this.mUtils.resetFps(ThermalControllerCenter.this.mCurPackageName);
                                }
                                ThermalControllerCenter.this.startHeatOffsetMethod(updateHeatSourceOffsets3, updateTsensorSourceLevel2);
                            }
                            ThermalControllerCenter thermalControllerCenter49 = ThermalControllerCenter.this;
                            thermalControllerCenter49.mPreUserMode = thermalControllerCenter49.mUserMode;
                            ThermalControllerCenter thermalControllerCenter50 = ThermalControllerCenter.this;
                            thermalControllerCenter50.mPreGameUserMode = thermalControllerCenter50.mGameUserMode;
                            ThermalControllerCenter thermalControllerCenter51 = ThermalControllerCenter.this;
                            thermalControllerCenter51.mPrePackageName = thermalControllerCenter51.mCurPackageName;
                            break;
                        }
                        break;
                    case 4:
                    case 5:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_STATUS_CHANGED,msg:" + message.what);
                        if (!ThermalControllerCenter.this.mThermalControlConfig.isThermalControlEnable()) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else if (ThermalControllerCenter.this.mTempLevel < 0) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else {
                            ThermalControllerCenter thermalControllerCenter52 = ThermalControllerCenter.this;
                            thermalControllerCenter52.mCurThermalPolicy = getThermalPolicy(thermalControllerCenter52.mTempLevel, false, false);
                            boolean updateHeatSourceOffsets4 = ThermalControllerCenter.this.mHeatSourceController.updateHeatSourceOffsets(ThermalControllerCenter.this.mCurThermalPolicy, false);
                            boolean updateTsensorSourceLevel3 = ThermalControllerCenter.this.mHeatSourceController.updateTsensorSourceLevel();
                            if (ThermalControllerCenter.this.mCurThermalPolicy == null) {
                                ThermalControllerCenter.this.stopCollingMethods();
                                LocalLog.a(TAG, ThermalControllerCenter.CUR_THERMAL_POLICY_IS_NULL);
                            } else if (!ThermalControllerCenter.this.mCurThermalPolicy.equals(ThermalControllerCenter.this.mPreThermalPolicy)) {
                                ThermalControllerCenter thermalControllerCenter53 = ThermalControllerCenter.this;
                                thermalControllerCenter53.startCollingMethods(thermalControllerCenter53.mCurThermalPolicy, ThermalControllerCenter.this.mTemp, false, false);
                            } else {
                                ThermalControllerCenter.this.startHeatOffsetMethod(updateHeatSourceOffsets4, updateTsensorSourceLevel3);
                                LocalLog.a(TAG, "Same as last policy");
                            }
                            ThermalControllerCenter thermalControllerCenter54 = ThermalControllerCenter.this;
                            thermalControllerCenter54.mPreUserMode = thermalControllerCenter54.mUserMode;
                            ThermalControllerCenter thermalControllerCenter55 = ThermalControllerCenter.this;
                            thermalControllerCenter55.mPreGameUserMode = thermalControllerCenter55.mGameUserMode;
                            ThermalControllerCenter thermalControllerCenter56 = ThermalControllerCenter.this;
                            thermalControllerCenter56.mPrePackageName = thermalControllerCenter56.mCurPackageName;
                            break;
                        }
                    case 6:
                        LocalLog.a(TAG, "MSG_THERMAL_CONTROL_TRACEINFO_UPLOAD.");
                        int i12 = message.arg1;
                        int i13 = message.arg2;
                        long currentTimeMillis = System.currentTimeMillis();
                        boolean isScreenOn2 = ThermalControllerCenter.this.mUtils.isScreenOn();
                        boolean isCharging = ThermalControllerCenter.this.mUtils.isCharging();
                        if (ThermalControllerCenter.this.mAppStates != null) {
                            List<ThermalControlUtils.WindowInfo> arrayList = new ArrayList<>();
                            if (isScreenOn2 && i13 == 1 && ThermalControllerCenter.this.mTempLevel >= ThermalControllerCenter.this.mThermalControlConfig.getFloatingWindowsInfoAcquireLevel()) {
                                arrayList = ThermalControllerCenter.this.mUtils.getFloatingWindowsInfo(false);
                                LocalLog.l(TAG, "ZoomWindowsInfo=" + ThermalControllerCenter.this.mUtils.getZoomWindowsInfo(arrayList) + ",PipWindowsInfo=" + ThermalControllerCenter.this.mUtils.getPipWindowsInfo(arrayList) + ",AppFloatingWindowsInfo=" + ThermalControllerCenter.this.mUtils.getAppFloatingWindowsInfo(arrayList));
                            }
                            ThermalTraceInfo thermalTraceInfo = new ThermalTraceInfo(currentTimeMillis, ThermalControllerCenter.this.mTempLevel, ThermalControllerCenter.this.mTemp, ThermalControllerCenter.this.mEnvTempType, isScreenOn2, isCharging, ThermalControllerCenter.this.mIsSafety, ThermalControllerCenter.this.mSafetyState, ThermalControllerCenter.this.mCurPackageName, ThermalControllerCenter.this.mAppType, ThermalControllerCenter.this.mAppStates.toString(), ThermalControllerCenter.this.mUserMode, ThermalControllerCenter.this.mUtils.getFoldingMode(), ThermalControllerCenter.this.mUtils.isSplitMode(), ThermalControllerCenter.this.mUtils.isSpeedCharging(), ThermalControllerCenter.this.mAppSwitchSafetyMode, ThermalControllerCenter.this.mUtils.getAmbientTemperature(), ThermalControllerCenter.this.mUtils.getAmbientTempState(), ThermalControllerCenter.this.mCurThermalPolicy, ThermalControllerCenter.this.mUtils.getZoomWindowsInfo(arrayList), ThermalControllerCenter.this.mUtils.getPipWindowsInfo(arrayList), ThermalControllerCenter.this.mUtils.getAppFloatingWindowsInfo(arrayList));
                            if (ThermalControllerCenter.this.mUtils.isTraceMonitorOn() || ThermalControllerCenter.this.mThermalControlMonitor.isOplusCameraOn()) {
                                if (ThermalControllerCenter.this.mCurThermalPolicy != null) {
                                    StatsLog.write(StatsEvent.newBuilder().setAtomId(ThermalControllerCenter.THERMAL_CONTROL_STATS_ATOM_ID).writeInt(thermalTraceInfo.folding_mode).writeBoolean(thermalTraceInfo.is_split).writeString(ThermalControllerCenter.this.mCurThermalPolicy.categoryName).writeInt(ThermalControllerCenter.this.mCurThermalPolicy.gearLevel).writeInt(thermalTraceInfo.trigger_level).writeInt(ThermalControllerCenter.this.mCurThermalPolicy.cpu).writeInt(ThermalControllerCenter.this.mCurThermalPolicy.gpu).writeInt(ThermalControllerCenter.this.mCurThermalPolicy.charge).writeBoolean(thermalTraceInfo.safety_test_on).writeInt(ThermalControllerCenter.this.mCurThermalPolicy.modem).usePooledBuffer().build());
                                }
                                Intent intent = new Intent(ThermalControllerCenter.EAP_INTENT_NAME);
                                intent.putExtra("ThermalTraceInfo", new GsonBuilder().setPrettyPrinting().create().toJson(thermalTraceInfo));
                                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, ThermalControllerCenter.EAP_SAFE_PERMISSION);
                            }
                            if (i12 == 0) {
                                if (LocalLog.f()) {
                                    LocalLog.a(TAG, "uploadTempLevelChangeTraceEvent.");
                                }
                                ThermalControllerCenter.this.mThermalStatusUploader.uploadTempLevelChangeTraceEvent(thermalTraceInfo);
                                break;
                            }
                        }
                        break;
                    case 7:
                        LocalLog.a(TAG, "MSG_THERMAL_CONTROL_MODEM_PROTECT_PERIOD quit.");
                        if (ThermalControllerCenter.this.mCurThermalPolicy != null && ThermalControllerCenter.this.mCurThermalPolicy.modem == 3) {
                            ThermalControllerCenter thermalControllerCenter57 = ThermalControllerCenter.this;
                            thermalControllerCenter57.start5GModemMethods(thermalControllerCenter57.mCurThermalPolicy.modem);
                            break;
                        } else {
                            ThermalControllerCenter.this.mThermalControlModemAllow = true;
                            break;
                        }
                    case 11:
                        LocalLog.a(TAG, "MSG_THERMAL_CONTROL_RESTRICT_PROTECT_PERIOD quit.");
                        ThermalControllerCenter.this.mThermalControlRestrictAllow = true;
                        break;
                    case 17:
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_SAFETY_PROTECT_PERIOD quit.");
                        ThermalControllerCenter.this.mIsNeedCheckAppSwitch = true;
                        break;
                    case 18:
                        int ambientTemperature = ThermalControllerCenter.this.mUtils.getAmbientTemperature();
                        int ambientTempState = ThermalControllerCenter.this.mUtils.getAmbientTempState();
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_AMBIENT_TEMP_CHANGE, ambientTemp:" + ambientTemperature + " state:" + ambientTempState);
                        ThermalAmbientPolicy thermalAmbientPolicy = ThermalControllerCenter.this.mThermalControlConfig.getThermalAmbientPolicy(ambientTempState);
                        if (ThermalControllerCenter.this.mThermalControlConfig.isAmbientPolicyEnable() && thermalAmbientPolicy != null) {
                            if (ambientTemperature > thermalAmbientPolicy.triggerTemp) {
                                sendAmbientTempCheckMsg(false, ambientTempState, ambientTemperature, ThermalControllerCenter.this.mThermalControlConfig.getAmbientKeepTimeVal());
                                break;
                            } else if (ambientTemperature <= thermalAmbientPolicy.fallBackTemp) {
                                sendAmbientTempCheckMsg(true, ambientTempState, ambientTemperature, ThermalControllerCenter.this.mThermalControlConfig.getAmbientKeepTimeVal());
                                break;
                            } else {
                                if (ThermalControllerCenter.this.mHandler.hasMessages(19)) {
                                    ThermalControllerCenter.this.mHandler.removeMessages(19);
                                }
                                LocalLog.l(TAG, "Same as last ambient state");
                                break;
                            }
                        }
                        LocalLog.a(TAG, "Ambient Policy is disable or empty");
                        ThermalControllerCenter.this.mHandler.removeMessages(19);
                        if (ambientTempState != 2) {
                            Message obtain = Message.obtain();
                            obtain.what = 19;
                            obtain.arg1 = 2;
                            this.mNextAmbientState = -1;
                            ThermalControllerCenter.this.mHandler.sendMessage(obtain);
                            break;
                        }
                        break;
                    case 19:
                        int i14 = message.arg1;
                        LocalLog.l(TAG, "MSG_THERMAL_CONTROL_AMBIENT_STATE_CHANGE: state=" + i14);
                        ThermalControllerCenter.this.mUtils.setAmbientTempState(i14);
                        if (!ThermalControllerCenter.this.mThermalControlConfig.isThermalControlEnable()) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else if (ThermalControllerCenter.this.mTempLevel < 0) {
                            ThermalControllerCenter.this.stopCollingMethods();
                            break;
                        } else {
                            ThermalControllerCenter thermalControllerCenter58 = ThermalControllerCenter.this;
                            thermalControllerCenter58.mCurThermalPolicy = getThermalPolicy(thermalControllerCenter58.mTempLevel, false, false);
                            boolean updateHeatSourceOffsets5 = ThermalControllerCenter.this.mHeatSourceController.updateHeatSourceOffsets(ThermalControllerCenter.this.mCurThermalPolicy, false);
                            boolean updateTsensorSourceLevel4 = ThermalControllerCenter.this.mHeatSourceController.updateTsensorSourceLevel();
                            if (ThermalControllerCenter.this.mCurThermalPolicy == null) {
                                ThermalControllerCenter.this.stopCollingMethods();
                                LocalLog.a(TAG, ThermalControllerCenter.CUR_THERMAL_POLICY_IS_NULL);
                            } else if (!ThermalControllerCenter.this.mCurThermalPolicy.equals(ThermalControllerCenter.this.mPreThermalPolicy)) {
                                ThermalControllerCenter thermalControllerCenter59 = ThermalControllerCenter.this;
                                thermalControllerCenter59.startCollingMethods(thermalControllerCenter59.mCurThermalPolicy, ThermalControllerCenter.this.mTemp, false, false);
                            } else {
                                ThermalControllerCenter.this.startHeatOffsetMethod(updateHeatSourceOffsets5, updateTsensorSourceLevel4);
                                LocalLog.a(TAG, "Same as last policy");
                            }
                            ThermalControllerCenter thermalControllerCenter60 = ThermalControllerCenter.this;
                            thermalControllerCenter60.mPreUserMode = thermalControllerCenter60.mUserMode;
                            ThermalControllerCenter thermalControllerCenter61 = ThermalControllerCenter.this;
                            thermalControllerCenter61.mPreGameUserMode = thermalControllerCenter61.mGameUserMode;
                            ThermalControllerCenter thermalControllerCenter62 = ThermalControllerCenter.this;
                            thermalControllerCenter62.mPrePackageName = thermalControllerCenter62.mCurPackageName;
                            break;
                        }
                    case 21:
                        ThermalControllerCenter.this.handleTsensorTempChangedMessageLocked(message);
                        break;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0245  */
        /* JADX WARN: Removed duplicated region for block: B:7:0x01e8  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public ThermalPolicy getThermalPolicy(int i10, String str, int i11, List<Integer> list, boolean z10, boolean z11) {
            String str2;
            ThermalPolicy thermalPolicyInfo;
            ThermalControllerCenter thermalControllerCenter = ThermalControllerCenter.this;
            ThermalControlConfig unused = thermalControllerCenter.mThermalControlConfig;
            thermalControllerCenter.mCategoryIndex = -1;
            ThermalControllerCenter thermalControllerCenter2 = ThermalControllerCenter.this;
            ThermalControlConfig unused2 = thermalControllerCenter2.mThermalControlConfig;
            thermalControllerCenter2.mConfigItemName = ThermalControlConfig.ORIGINAL_DEFAULT_ITEM;
            ThermalControllerCenter thermalControllerCenter3 = ThermalControllerCenter.this;
            thermalControllerCenter3.mUserMode = thermalControllerCenter3.mUtils.getUserMode();
            ThermalControllerCenter thermalControllerCenter4 = ThermalControllerCenter.this;
            thermalControllerCenter4.mGameUserMode = thermalControllerCenter4.mUtils.getGameUserMode();
            ThermalControllerCenter thermalControllerCenter5 = ThermalControllerCenter.this;
            thermalControllerCenter5.mEnvTempType = thermalControllerCenter5.getEnvironmentTemperatureType();
            ThermalControllerCenter thermalControllerCenter6 = ThermalControllerCenter.this;
            thermalControllerCenter6.mTempGear = thermalControllerCenter6.getTempGear(thermalControllerCenter6.mAppType, i10, ThermalControllerCenter.this.mUserMode, ThermalControllerCenter.this.mGameUserMode);
            LocalLog.l(TAG, "tempLevel=" + i10 + ",userMode=" + ThermalControllerCenter.this.mUserMode + ",gameUserMode=" + ThermalControllerCenter.this.mGameUserMode + ",appType=" + i11 + ",envTempType=" + ThermalControllerCenter.this.mEnvTempType + ",appStates=" + list + ",appSwitchSafetyMode=" + ThermalControllerCenter.this.mAppSwitchSafetyMode);
            ThermalControllerCenter thermalControllerCenter7 = ThermalControllerCenter.this;
            thermalControllerCenter7.mIsSafety = thermalControllerCenter7.isSafetyScenario();
            ThermalControllerCenter thermalControllerCenter8 = ThermalControllerCenter.this;
            thermalControllerCenter8.mIsScreenOn = thermalControllerCenter8.mUtils.isScreenOn();
            if (ThermalControllerCenter.this.isSafetymode()) {
                ThermalControllerCenter thermalControllerCenter9 = ThermalControllerCenter.this;
                thermalControllerCenter9.mTempGear = i10 + thermalControllerCenter9.mThermalControlConfig.getOtaThermalControlVal();
                ThermalControllerCenter thermalControllerCenter10 = ThermalControllerCenter.this;
                ThermalControlConfig unused3 = thermalControllerCenter10.mThermalControlConfig;
                thermalControllerCenter10.mCategoryIndex = 1;
                ThermalControllerCenter thermalControllerCenter11 = ThermalControllerCenter.this;
                ThermalControlConfig unused4 = thermalControllerCenter11.mThermalControlConfig;
                thermalControllerCenter11.mConfigItemName = ThermalControlConfig.ORIGINAL_SAFETY_TEST_ITEM;
            } else if (!ThermalControllerCenter.this.mIsScreenOn) {
                ThermalControllerCenter.this.checkScreenOffScene();
            } else if (list != null && !list.isEmpty()) {
                ThermalControllerCenter.this.checkAppScene(list, i11);
            } else if (ThermalControllerCenter.this.mUtils.isPhoneInCall(this.mContext)) {
                ThermalControllerCenter thermalControllerCenter12 = ThermalControllerCenter.this;
                thermalControllerCenter12.mTempGear = i10 + thermalControllerCenter12.mThermalControlConfig.getAdditionValWithModeType(ThermalControllerCenter.this.mUserMode) + ThermalControllerCenter.this.mThermalControlConfig.getAdditionValWithModeType(ThermalControllerCenter.this.mGameUserMode) + ThermalControllerCenter.this.mThermalControlConfig.getOtaThermalControlVal() + ThermalControllerCenter.this.mThermalControlConfig.getAdditionValWithFloatWindow();
                ThermalControllerCenter thermalControllerCenter13 = ThermalControllerCenter.this;
                ThermalControlConfig unused5 = thermalControllerCenter13.mThermalControlConfig;
                thermalControllerCenter13.mCategoryIndex = 3;
                ThermalControllerCenter.this.mConfigItemName = "scene_102";
            } else if (ThermalControllerCenter.this.mUtils.isBackupRestore() && !ThermalControllerCenter.this.mUtils.isBackupRestoreScene()) {
                if (ThermalControllerCenter.this.mUtils.isBackupHighPerformance()) {
                    ThermalControllerCenter thermalControllerCenter14 = ThermalControllerCenter.this;
                    ThermalControlConfig unused6 = thermalControllerCenter14.mThermalControlConfig;
                    thermalControllerCenter14.mCategoryIndex = 3;
                    ThermalControllerCenter.this.mConfigItemName = "scene_151";
                } else {
                    ThermalControllerCenter thermalControllerCenter15 = ThermalControllerCenter.this;
                    ThermalControlConfig unused7 = thermalControllerCenter15.mThermalControlConfig;
                    thermalControllerCenter15.mCategoryIndex = 3;
                    ThermalControllerCenter.this.mConfigItemName = "scene_150";
                }
            } else if (ThermalControllerCenter.this.mUtils.isRecordingOn()) {
                ThermalControllerCenter thermalControllerCenter16 = ThermalControllerCenter.this;
                ThermalControlConfig unused8 = thermalControllerCenter16.mThermalControlConfig;
                thermalControllerCenter16.mCategoryIndex = 3;
                ThermalControllerCenter.this.mConfigItemName = "scene_152";
            } else if (i11 > 0) {
                str2 = "category_" + i11;
                ThermalControllerCenter.this.mConfigItemName = str2;
                ThermalControllerCenter.this.checkPolicyScene();
                if (i11 > 0) {
                    str2 = "category_" + i11;
                }
                LocalLog.l(TAG, "tempGear=" + ThermalControllerCenter.this.mTempGear + ",configItemName=" + ThermalControllerCenter.this.mConfigItemName);
                thermalPolicyInfo = ThermalControllerCenter.this.mThermalControlConfig.getThermalPolicyInfo(ThermalControllerCenter.this.mCategoryIndex, ThermalControllerCenter.this.mConfigItemName, str, str2, ThermalControllerCenter.this.mTempGear, ThermalControllerCenter.this.mIsScreenOn);
                if (thermalPolicyInfo == null) {
                    for (int i12 = ThermalControllerCenter.this.mTempGear; i12 >= 0; i12--) {
                        thermalPolicyInfo = ThermalControllerCenter.this.mThermalControlConfig.getThermalPolicyInfo(ThermalControllerCenter.this.mCategoryIndex, ThermalControllerCenter.this.mConfigItemName, str, str2, i12, ThermalControllerCenter.this.mIsScreenOn);
                        if (thermalPolicyInfo != null) {
                            break;
                        }
                    }
                }
                ThermalControllerCenter.this.checkSafetyNeedChangeRestrict(str, str2, z10, z11);
                return thermalPolicyInfo;
            }
            str2 = null;
            ThermalControllerCenter.this.checkPolicyScene();
            if (i11 > 0) {
            }
            LocalLog.l(TAG, "tempGear=" + ThermalControllerCenter.this.mTempGear + ",configItemName=" + ThermalControllerCenter.this.mConfigItemName);
            thermalPolicyInfo = ThermalControllerCenter.this.mThermalControlConfig.getThermalPolicyInfo(ThermalControllerCenter.this.mCategoryIndex, ThermalControllerCenter.this.mConfigItemName, str, str2, ThermalControllerCenter.this.mTempGear, ThermalControllerCenter.this.mIsScreenOn);
            if (thermalPolicyInfo == null) {
            }
            ThermalControllerCenter.this.checkSafetyNeedChangeRestrict(str, str2, z10, z11);
            return thermalPolicyInfo;
        }
    }
}
