package android.os;

import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;
import java.util.ArrayList;
import oplus.util.OplusCommonConstants;

/* loaded from: classes.dex */
public class OplusThermalManager {
    public static final String EXTRA_BATTERY_PHONETEMP = "phoneTemp";
    public static int mConfigVersion = 2018030210;
    public static String mThermalHeatPath = "/sys/class/thermal/thermal_zone5/temp";
    public static String mThermalHeatPath1 = "";
    public static String mThermalHeatPath2 = "/sys/class/power_supply/battery/temp";
    public static String mThermalHeatPath3 = "";
    public static int mHeatAlign = 100;
    public static int mHeat1Align = 100;
    public static int mHeat2Align = 1;
    public static int mHeat3Align = 100;
    public static boolean mThermalFeatureOn = false;
    public static boolean mThermalUploadDcs = true;
    public static boolean mThermalUploadLog = false;
    public static boolean mThermalCaptureLog = false;
    public static boolean mRecordThermalHistory = false;
    public static int mThermalCaptureLogThreshold = 400;
    public static boolean mThermalUploadErrLog = false;
    public static boolean mThermalBatteryTemp = true;
    public static int mHeatRecInterv = 2;
    public static int mCpuLoadRecThreshold = 200;
    public static int mCpuLoadRecInterv = 50;
    public static int mTopCpuRecThreshold = 50;
    public static int mTopCpuRecInterv = 20;
    public static int mMoreHeatThreshold = 490;
    public static int mHeatThreshold = 440;
    public static int mLessHeatThreshold = 410;
    public static int mPreHeatThreshold = 400;
    public static int mHeatIncRatioThreshold = 1;
    public static int mHeatHoldTimeThreshold = 30;
    public static int mHeatHoldUploadTime = OplusCommonConstants.SCREEN_CAST_LAST_CALL_TRANSACTION;
    public static ArrayList<String> mMonitorAppList = new ArrayList<>();
    public static int mMonitorAppLimitTime = 2400000;
    public static int mHeatTopProInterval = 120;
    public static boolean mHeatTopProFeatureOn = true;
    public static int mHeatCaptureCpuFreqInterval = 120;
    public static int mHeatTopProCounts = 5;
    public static int mPreHeatDexOatThreshold = 430;
    public static long mDetectEnvironmentTimeThreshold = OplusNrModeConstant.TAC_UPDATE_PERIOD;
    public static int mDetectEnvironmentTempThreshold = 30;
}
