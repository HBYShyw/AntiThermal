package com.oplus.uah;

import android.os.Bundle;
import android.os.ServiceManager;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import android.util.Log;
import cn.teddymobile.free.anteater.resources.UriConstants;
import com.oplus.osense.info.OsenseCtrlDataRequest;
import com.oplus.osense.info.OsenseNotifyRequest;
import com.oplus.osense.info.OsenseSaRequest;
import com.oplus.uah.info.UAHResourceInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import vendor.oplus.hardware.urcc.IUrcc;

/* loaded from: classes.dex */
public class UAHAdaptHelper {
    private static final int CTRL_DATA_CLUSTER_INDEX_GOLD = 1;
    private static final int CTRL_DATA_CLUSTER_INDEX_PRIME = 2;
    private static final int CTRL_DATA_CLUSTER_INDEX_SILVER = 0;
    private static final int DEFAULT_NRTICK_VALUE = 3;
    private static final int EVENT_SPLIT_INDEX = 2;
    private static final int GAME_ADP_INDEX_CORE_BOOST = 0;
    private static final int GAME_ADP_INDEX_CORE_LIMIT = 1;
    private static final int GAME_ADP_INDEX_FREQ_BOOST = 2;
    private static final int GAME_ADP_INDEX_FREQ_LIMIT = 3;
    private static final int GROUP_MIG_LENGTH = 2;
    private static final String TAG = "UAH-UahAdaptHelper";
    public static final int UAH_NOTIFY_GAME_ADP_OFF = 0;
    public static final int UAH_NOTIFY_GAME_ADP_ON = 1;
    public static final int UAH_NOTIFY_GAME_MODE_OFF = 0;
    public static final int UAH_NOTIFY_GAME_MODE_ON = 1;
    public static final int UAH_NOTIFY_GAME_MODE_ON_EXTEND = 2;
    public static final int UAH_NOTIFY_GPA_DISABLED = 0;
    public static final int UAH_NOTIFY_GPA_ENABLED = 1;
    public static final int UAH_NOTIFY_NR_TICKS_OFF = 0;
    public static final int UAH_NOTIFY_NR_TICKS_ON = 1;
    public static final int UAH_NOTIFY_SRC_ACCESSIBILITY_MODE = 12;
    public static final int UAH_NOTIFY_SRC_AFFINITY = 100;
    public static final int UAH_NOTIFY_SRC_BACK_LIGHT = 101;
    public static final int UAH_NOTIFY_SRC_BENCHMARK_MODE = 5;
    public static final int UAH_NOTIFY_SRC_CPUBOUNCING_ENABLE = 111;
    public static final int UAH_NOTIFY_SRC_FIX_PERFORMANCE_MODE = 7;
    public static final int UAH_NOTIFY_SRC_FPSGO_ONOFF = 110;
    public static final int UAH_NOTIFY_SRC_GAME_ADP = 9000;
    public static final int UAH_NOTIFY_SRC_GAME_CONFIG = 129;
    public static final int UAH_NOTIFY_SRC_GAME_CONFIG_COLOCATION = 131;
    public static final int UAH_NOTIFY_SRC_GAME_CONFIG_TARGETLOADS = 130;
    public static final int UAH_NOTIFY_SRC_GAME_MODE = 8;
    public static final int UAH_NOTIFY_SRC_GAME_UPGRADE = 14;
    public static final int UAH_NOTIFY_SRC_GPA_ENABLE = 9;
    public static final int UAH_NOTIFY_SRC_LIVE_WALLPAPER = 11;
    public static final int UAH_NOTIFY_SRC_NR_TICKS = 102;
    public static final int UAH_NOTIFY_SRC_OMRG_STATUS = 15;
    public static final int UAH_NOTIFY_SRC_PC_CAST = 134;
    public static final int UAH_NOTIFY_SRC_PERFORMANCE_MODE = 2;
    public static final int UAH_NOTIFY_SRC_POWERSAVE_MODE = 3;
    public static final int UAH_NOTIFY_SRC_SCREEN_RECORDER = 10;
    public static final int UAH_NOTIFY_SRC_SCREEN_STATUS = 1;
    public static final int UAH_NOTIFY_SRC_SUPERPOWERSAVE_MODE = 6;
    public static final int UAH_NOTIFY_SRC_THERMAL_MODE = 4;
    public static final int UAH_NOTIFY_SRC_THERMAL_OFFSET = 128;
    public static final int UAH_NOTIFY_SRC_TOUCH_EVENT = 1000;
    public static final int UAH_NOTIFY_SRC_XIAOBAI_EVALUATION = 16;
    public static final int UAH_NOTIFY_THERMAL_OFFSET_OFF = 0;
    public static final int UAH_NOTIFY_THERMAL_OFFSET_ON = 1;
    public static final int UAH_NOTIFY_TYPE_CPUBOUNCING_DISABLE = 0;
    public static final int UAH_NOTIFY_TYPE_CPUBOUNCING_ENABLE = 1;
    public static final int UAH_NOTIFY_TYPE_CPUBOUNCING_RELEASE = -1;
    public static final int UAH_NOTIFY_TYPE_GAME_UPGRADE_BG = 0;
    public static final int UAH_NOTIFY_TYPE_GAME_UPGRADE_FG = 1;
    public static final int UAH_NOTIFY_TYPE_OMRG_OFF = 0;
    public static final int UAH_NOTIFY_TYPE_OMRG_ON = 1;
    public static final int UAH_NOTIFY_TYPE_OMRG_RELEASE = -1;
    private static final int UAH_RULE_POWER_MODE = 200;
    private static final int UNSET_DEFAULT = -1;
    private IUrcc mService;
    private static final String HAL_INSTANCE_NAME = IUrcc.DESCRIPTOR + "/default";
    private static volatile UAHAdaptHelper sInstance = null;
    private static String[] sActionList = {"ACTION_LAUNCH", "ACTION_COLD_LAUNCH", "ACTION_ACTIVITY_START", "ACTION_ACTIVITY_RESUME", "ACTION_ACTIVITY_FINISH", "ACTION_WINDOW_ROTATION", "ACTION_SWIPE_V", "ACTION_SWIPE_H", "ACTION_DECODE", "ACTION_COMPRESS", "ACTION_BLUETOOTH", "ACTION_CAMERA_OPEN", "ACTION_CAMERA_CLOSE", "ACTION_MTP_BOOST", "ACTION_HARDCODER_L1", "ACTION_HARDCODER_L2", "ACTION_HARDCODER_L3", "ACTION_CAMERA_PREVIEW", "ACTION_CAMERA_CAPTURE", "ACTION_CAMERA_VIDEO", "ACTION_CAMERA_SLOW_720P", "ACTION_CAMERA_SLOW_1080P", "ACTION_CAMERA_FRONT_PREVIEW", "ACTION_CAMERA_VIDEO_PREVIEW_60FPS", "ACTION_CAMERA_VIDEO_PREVIEW_VIRTUAL", "ACTION_CAMERA_VIDEO_PREVIEW_EIS", "ACTION_CAMERA_VIDEO_EIS_60FPS", "ACTION_CAMERA_VIDEO_4K", "ACTION_OIFACE_GAME_LOADING", "ACTION_OIFACE_GAME_BOOST_L1", "ACTION_OIFACE_GAME_BOOST_L2", "ACTION_OIFACE_GAME_BOOST_L3", "ACTION_HYPERBOOST_GPU_BOOST", "ACTION_HYPERBOOST_CPU_BOOST_L1", "ACTION_HYPERBOOST_CPU_BOOST_L2", "ACTION_HYPERBOOST_CPU_BOOST_L3", "ACTION_HYPERBOOST_DDR_BOOST", "ACTION_TOUCH_BOOST", "ACTION_SCREEN_ON_BOOST", "ACTION_INPUT_METHOD_BOOST", "ACTION_STORAGE_BOOST", "ACTION_UNLOCK_BOOST", "ACTION_ANIMATION", "ACTION_CAMERA_VIDEO_UHD60", "ACTION_CAMERA_PREVIEW_VIDEO_EIS", "ACTION_CAMERA_VIDEO_HD30", "ACTION_CAMERA_VIDEO_FHD30", "ACTION_CAMERA_VIDEO_UHD30", "ACTION_CAMERA_3RD_PARTY", "ACTION_CLEAN_UP", "ACTION_SCREEN_SHOT", "ACTION_INSTALLATION", "ACTION_RECORD", "ACTION_CAMERA_FRONT_PREVIEW_PHOTO", "ACTION_LUCKY_MONEY", "ACTION_CAMERA_APS_CAPTURE", "ACTION_FACE_UNLOCK", "ACTION_CAMERA_SLOW_CAPTURE", "ACTION_VOICE_WAKEUP", "ACTION_3RD_CAM_WECHAT_VIDEO_CALL", "ACTION_FILE_SCANNER", "ACTION_WATERMARK_ANIMATION", "ACTION_3RD_CAM_VIDEO_CALL", "ACTION_3RD_CAM_VIDEO_RECORDER", "ACTION_VFX_BOOST", "ACTION_FOREGROUND_BOOST", "ACTION_CAMERA_GIFMAKER", "ACTION_DEFAULT_PLAYER", "ACTION_SWIPE_SLOW_V", "ACTION_SWIPE_SLOW_H", "ACTION_CAMERA_VIDEO_720P", "ACTION_SIM_INIT", "ACTION_3RD_CAM_WHATSAPP_VIDEO_CALL", "ACTION_CTS_PERF", "ACTION_SWIPE_SLOW_EXTEND", "ACTION_LAUNCHER_GPU_BOOST", "ACTION_FILE_COPY_CUT", "ACTION_FILE_ZIP_UNZIP", "ACTION_CAMERA_STIKER_RECORD", "ACTION_CAMERA_3RD_PARTY_LOW_POWER", "ACTION_SWIPE_RECENT", "ACTION_INPUT_BOOST_EXTEND", "ACTION_CAMERA_3RD_PARTY_COMMON", "ACTION_CAMERA_VIDEO_PREVIEW_30FPS", "ACTION_GESTURE_ANIMATION", "ACTION_LAUNCHER_OPEN_APP_ANIMATION", "ACTION_LAUNCHER_CLOSE_APP_ANIMATION", "ACTION_CAMERA_VIDEO_UHD60_CAMERAUNIT", "ACTION_CAMERA_3RD_PARTY_EXPORT", "ACTION_CAMERA_3RD_PARTY_LOW_POWER_EXPORT", "ACTION_CAMERA_3RD_PARTY_MIDDLE_POWER", "ACTION_CAMERA_APS_CAPTURE_L1", "ACTION_CAMERA_APS_CAPTURE_L2", "ACTION_CAMERA_APS_CAPTURE_L3", "ACTION_CAMERA_APS_CAPTURE_L4", "ACTION_CAMERA_3RD_PARTY_HIGH_POWER", "ACTION_CAMERA_3RD_PARTY_SUPER_POWER", "ACTION_SYSTEM_UI_SWIPE", "ACTION_CAMERA_VIDEO_AI_ENHANCE", "ACTION_CAMERA_VIDEO_GIFMAKER_REAR", "ACTION_CAMERA_REAR_VIDEO_1080P", "ACTION_CAMERA_REAR_VIDEO_SUPER_EIS", "ACTION_CAMERA_MULTI_SCENE", "ACTION_CAMERA_MOVIE", "ACTION_CAMERA_3RD_PARTY_PERF", "ACTION_VIRTUAL_KEY", "ACTION_CAMERA_APS_CAPTURE_L1_DEPRECATED", "ACTION_CAMERA_APS_CAPTURE_L2_DEPRECATED", "ACTION_CAMERA_APS_CAPTURE_L3_DEPRECATED", "ACTION_CAMERA_APS_CAPTURE_L4_DEPRECATED", "ACTION_ROTATION_LATENCY_BOOST", "ACTION_VERTICAL_SCROLL_BOOST", "ACTION_PREFLING_SCROLL_BOOST", "ACTION_HORIZONTAL_SCROLL_BOOST", "ACTION_CAMERA_VIDEO_SUPER_EIS", "ACTION_CAMERA_3RD_PARTY_WECHAT_SMALL_WINDOW", "ACTION_CAMERA_MOVIE_ULTRA_WIDE", "ACTION_SIDEBAR_SLIDEOUT_BOOST", "ACTION_3RD_CAM_VIDEO_MIDPERF", "ACTION_3RD_CAM_VIDEO_HIGHPERF", "ACTION_CAMERA_PREVIEW_PORTAIT", "ACTION_CAMERA_MEITU", "ACTION_CAMERA_FRONT_EIS", "ACTION_CAMERA_VIDEO_BLUR", "ACTION_WFD", "ACTION_AUDIO_BOOST_L1", "ACTION_AUDIO_BOOST_L2", "ACTION_AUDIO_BOOST_L3", "ACTION_CAMERA_CONTINUOUS_CAPTURE", "ACTION_CAMERA_3RD_PARTY_SPECIAL_APP", "ACTION_CAMERA_VIDEO_FRONT_NEON_BEAUTY", "ACTION_CAMERA_VIDEO_FRONT_BLUR_BEAUTY", "ACTION_CAMERA_FRONT_PREVIEW_30FPS", "ACTION_CAMERA_FRONT_PREVIEW_30FPS_FACE_NUM_2", "ACTION_CAMERA_VIDEO_BACK_NEON_BEAUTY", "ACTION_CAMERA_SUPERNIGHT", "ACTION_CAMERA_PREVIEW_800_400", "ACTION_LAUNCHER_ICON_EDIT", "ACTION_VIDEO_PLAY_BOOST", "ACTION_CAMERA_VIDEO_HFR_480FPS", "ACTION_CAMERA_VIDEO_8K30", "ACTION_CAMERA_VIDEO_SUPER_STEADY", "ACTION_CAMERA_VIDEO_UHD120", "ACTION_CAMERA_VIDEO_HFR_240FPS", "ACTION_CAMERA_VIDEO_SAT", "ACTION_CAMERA_VIDEO_MACRO_PREVIEW", "ACTION_CAMERA_48M_PREVIEW", "ACTION_CAMERA_CTS_PREVIEW", "ACTION_CAMERA_STICKER_MODE", "ACTION_EPS_FALLBACK_BOOST", "ACTION_CAMERA_CAPTURE_LOWPOWER", "ACTION_CAMERA_MODE_CHANGE", "ACTION_LAUNCHER_ICON_FALLEN", "ACTION_ZOOMWINDOW_DRAG", "ACTION_CAMERA_PREVIEW_RAWSR", "ACTION_CAMERA_CAPTURE_RAWSR", "ACTION_ROUND_CORNER_ANIMATION", "ACTION_STYLUS_OPERATE", "ACTION_CAMERA_SUPERTEXT_CAPTURE", "ACTION_CAMERA_FRONT_VIDEO_BOKEH", "ACTION_CAMERA_VIDEO_FHD30_BOKEH", "ACTION_CAMERA_VIDEO_FHD30_TIMELAPSE", "ACTION_CAMERA_MULTI_SCENE", "ACTION_CAMERA_FRONT_NIGHT_CAPTURE", "ACTION_CAMERA_NIGHT_CAPTURE", "ACTION_HINT_ANIMATION", "ACTION_CAMERA_3RD_PARTY_TIKTOK", "ACTION_CAMERA_FRONT_NIGHT_UI", "ACTION_CAMERA_APS_ICE2P0_CAPTURE", "ACTION_CAMERA_STICKER_ANIMOJI_PREVIEW", "ACTION_CAMERA_CAPTURE_FILTER_BEAUTY", "ACTION_CAMERA_REAR_PORTRAIT_PREVIEW", "ACTION_OTA_PANEL_OPEN", "ACTION_OIFACE_GAME_BG", "ACTION_CAMERA_DUALVIEW_VIDEO", "ACTION_CAST_SCREENOFF_DISABLE_DISPLAY_VOTE", "ACTION_CAMERA_LONG_EXPOSURE", "ACTION_CAMERA_FRONT_PREVIEW_PORTRAIT", "ACTION_CAMERA_FRONT_CAPTURE", "ACTION_CAMERA_FRONT_VIDEO_EIS", "ACTION_CAMERA_AI_VIDEO", "ACTION_CAMERA_ZOOM", "ACTION_CAMERA_APS_THDR_CAPTURE", "ACTION_CAMERA_LOWPOWER", "ACTION_BACKUP_RESTORE", "ACTION_CAMERA_VIDEO_BOKEH_24FPS", "ACTION_MULTI_USER_SWITCH", "ACTION_EXTREME_COLD_MODE", "ACTION_HIGHLIGHT_BOOST", "ACTION_CAMERA_3RD_WECHAT_SCAN", "ACTION_CAMERA_APS_THDR_CAPTURE_L1", "ACTION_CAMERA_THDR_CAPTURE", "ACTION_LIVE_WALLPAPER", "ACTION_CAST", "ACTION_ASSISTANT_SCROLL_BOOST", "ACTION_CAMERA_APS_BOKEH_CAPTURE", "ACTION_GAME_ASSISTANT_SLIDE", "ACTION_CAMERA_APS_INNER_THDR_CAPTURE", "ACTION_CAMERA_APS_INNER_THDR_CAPTURE_L1", "ACTION_LAYOUT_BOOST", "ACTION_CAMERA_WECHAT_PARTY_LOW_POWER"};
    private static int[] sOpCodeTargetLoad = {UAHResourceConstants.CPU_FREQ_TARGET_LOAD_SILVER, UAHResourceConstants.CPU_FREQ_TARGET_LOAD_GOLD, UAHResourceConstants.CPU_FREQ_TARGET_LOAD_PRIME};
    private static int[] sOpCodeAdpXpuCoreBoost = {UAHResourceConstants.CPU_CORE_CTL_MIN_CPUS_SILVER, UAHResourceConstants.CPU_CORE_CTL_MIN_CPUS_GOLD, UAHResourceConstants.CPU_CORE_CTL_MIN_CPUS_PRIME, 16908400};
    private static int[] sOpCodeAdpXpuCoreLimit = {UAHResourceConstants.CPU_CORE_CTL_MAX_CPUS_SILVER, UAHResourceConstants.CPU_CORE_CTL_MAX_CPUS_GOLD, UAHResourceConstants.CPU_CORE_CTL_MAX_CPUS_PRIME, 16908416};
    private static int[] sOpCodeAdpXpuFreqBoost = {16842768, 16842784, 16842800, 16908480};
    private static int[] sOpCodeAdpXpuFreqLimit = {16842912, 16842928, 16842944, 16908464};
    private int mNrTicksValue = 0;
    private int mPerfColocate = -1;
    private String mForegroundCpus = null;
    private HashMap<String, Integer> mMap = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum POWERMODE {
        MODE_NORMAL,
        MODE_POWER_SAVE,
        MODE_HIGH_RERFORMANCE,
        MODE_BENCHMARK,
        MODE_SUPER_POWER_SAVE,
        MODE_SCREEN_OFF,
        MODE_GEEKBENCH
    }

    private UAHAdaptHelper() {
        initHashMap();
    }

    public static UAHAdaptHelper getInstance() {
        if (sInstance == null) {
            synchronized (UAHAdaptHelper.class) {
                if (sInstance == null) {
                    sInstance = new UAHAdaptHelper();
                }
            }
        }
        return sInstance;
    }

    private void initHashMap() {
        if (sActionList != null && this.mMap != null) {
            int i = 0;
            while (true) {
                String[] strArr = sActionList;
                if (i < strArr.length) {
                    this.mMap.put(strArr[i], Integer.valueOf(i));
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public int getEventIdbyAction(String action) {
        if (action == null) {
            return -1;
        }
        String[] filter = action.split("_", 2);
        HashMap<String, Integer> hashMap = this.mMap;
        if (hashMap == null || hashMap.isEmpty() || filter.length <= 1) {
            return -1;
        }
        return this.mMap.getOrDefault(filter[1], -1).intValue();
    }

    public boolean getAidlService() {
        if (this.mService != null) {
            return true;
        }
        this.mService = IUrcc.Stub.asInterface(ServiceManager.checkService(HAL_INSTANCE_NAME));
        Log.i(TAG, "getAidlService uah urcc service =  " + this.mService);
        return this.mService != null;
    }

    public long[][][] adaptGetPerfLimit(String identity) {
        return UAHPerfManager.getInstance().getGetHistory();
    }

    public int adaptGetModeStatus(String identity, int mode) {
        return UAHPerfManager.getInstance().getModeStatus(identity, mode);
    }

    public void adaptClrSceneAction(String identity, long handle) {
        int handleInt = (int) handle;
        UAHPerfManager.getInstance().uahRelease(handleInt);
    }

    public long adaptSetSceneAction(String identity, OsenseSaRequest request) {
        String scene = request.getScene();
        String action = request.getAction();
        int timeout = request.getTimeout();
        if (TextUtils.isEmpty(action)) {
            Bundle info = request.getInfo();
            if (info == null) {
                Log.e(TAG, "setSceneAction... the bundle of OsenseSaRequest is null ");
                return -1L;
            }
            String act = info.getString(Telephony.WapPush.ACTION, "");
            if (TextUtils.isEmpty(act)) {
                Log.e(TAG, "setSceneAction... the bundle of OsenseSaRequest is null ");
                return -1L;
            }
            info.putString("identity", identity);
        } else {
            Bundle info2 = new Bundle();
            info2.putString("identity", identity);
            info2.putString(UriConstants.RESULT_COLUMN_SCENE, scene);
            info2.putString(Telephony.WapPush.ACTION, action);
            info2.putInt("timeout", timeout);
        }
        int eventId = getEventIdbyAction(action);
        int handleResult = UAHPerfManager.getInstance().tansSa2Event(eventId, scene, identity, timeout);
        return handleResult;
    }

    public void clearControlData(String identity) {
        UAHPerfManager.getInstance().tansUahRuleCtrl(9001, 0, null);
    }

    private List<UAHResourceInfo> getCpuCoreFreqList(OsenseCtrlDataRequest request) {
        int cpuClusterNum = request.cpuClusterNum;
        List<UAHResourceInfo> list = new ArrayList<>();
        for (int m = 0; m < cpuClusterNum; m++) {
            switch (m) {
                case 0:
                    try {
                        UAHResourceInfo minCoreCpu0 = new UAHResourceInfo(UAHResourceConstants.CPU_CORE_CTL_MIN_CPUS_SILVER, String.valueOf(request.cpuCoreCtrlData[0][0]));
                        UAHResourceInfo maxCoreCpu0 = new UAHResourceInfo(UAHResourceConstants.CPU_CORE_CTL_MAX_CPUS_SILVER, String.valueOf(request.cpuCoreCtrlData[0][1]));
                        UAHResourceInfo minFreqCpu0 = new UAHResourceInfo(16842768, String.valueOf(request.cpuFreqCtrlData[0][0]));
                        UAHResourceInfo maxFreqCpu0 = new UAHResourceInfo(16842912, String.valueOf(request.cpuFreqCtrlData[0][1]));
                        list.add(minCoreCpu0);
                        list.add(maxCoreCpu0);
                        list.add(minFreqCpu0);
                        list.add(maxFreqCpu0);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e(TAG, "invalid ControlData size");
                        return null;
                    }
                case 1:
                    try {
                        UAHResourceInfo minCoreCpu1 = new UAHResourceInfo(UAHResourceConstants.CPU_CORE_CTL_MIN_CPUS_GOLD, String.valueOf(request.cpuCoreCtrlData[1][0]));
                        UAHResourceInfo maxCoreCpu1 = new UAHResourceInfo(UAHResourceConstants.CPU_CORE_CTL_MAX_CPUS_GOLD, String.valueOf(request.cpuCoreCtrlData[1][1]));
                        UAHResourceInfo minFreqCpu1 = new UAHResourceInfo(16842784, String.valueOf(request.cpuFreqCtrlData[1][0]));
                        UAHResourceInfo maxFreqCpu1 = new UAHResourceInfo(16842928, String.valueOf(request.cpuFreqCtrlData[1][1]));
                        list.add(minCoreCpu1);
                        list.add(maxCoreCpu1);
                        list.add(minFreqCpu1);
                        list.add(maxFreqCpu1);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        Log.e(TAG, "invalid ControlData size");
                        return null;
                    }
                case 2:
                    try {
                        UAHResourceInfo minCoreCpu2 = new UAHResourceInfo(UAHResourceConstants.CPU_CORE_CTL_MIN_CPUS_PRIME, String.valueOf(request.cpuCoreCtrlData[2][0]));
                        UAHResourceInfo maxCoreCpu2 = new UAHResourceInfo(UAHResourceConstants.CPU_CORE_CTL_MAX_CPUS_PRIME, String.valueOf(request.cpuCoreCtrlData[2][1]));
                        UAHResourceInfo minFreqCpu2 = new UAHResourceInfo(16842800, String.valueOf(request.cpuFreqCtrlData[2][0]));
                        UAHResourceInfo maxFreqCpu2 = new UAHResourceInfo(16842944, String.valueOf(request.cpuFreqCtrlData[2][1]));
                        list.add(minCoreCpu2);
                        list.add(maxCoreCpu2);
                        list.add(minFreqCpu2);
                        list.add(maxFreqCpu2);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e3) {
                        Log.e(TAG, "invalid ControlData size");
                        return null;
                    }
                default:
                    Log.e(TAG, "ControlData: unsupport cpu cluster num:" + m);
                    break;
            }
        }
        return list;
    }

    private List<UAHResourceInfo> getGpuCoreFreqList(OsenseCtrlDataRequest request) {
        int gpuClusterNum = request.gpuClusterNum;
        List<UAHResourceInfo> list = new ArrayList<>();
        for (int m = 0; m < gpuClusterNum; m++) {
            switch (m) {
                case 0:
                    try {
                        UAHResourceInfo minCoreGpu0 = new UAHResourceInfo(16908400, String.valueOf(request.gpuCoreCtrlData[0][0]));
                        UAHResourceInfo maxCoreGpu0 = new UAHResourceInfo(16908416, String.valueOf(request.gpuCoreCtrlData[0][1]));
                        UAHResourceInfo minFreqGpu0 = new UAHResourceInfo(16908480, String.valueOf(request.gpuFreqCtrlData[0][0]));
                        UAHResourceInfo maxFreqGpu0 = new UAHResourceInfo(16908464, String.valueOf(request.gpuFreqCtrlData[0][1]));
                        list.add(minCoreGpu0);
                        list.add(maxCoreGpu0);
                        list.add(minFreqGpu0);
                        list.add(maxFreqGpu0);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e(TAG, "invalid ControlData size");
                        return null;
                    }
                default:
                    Log.e(TAG, "ControlData: unsupport gpu cluster num:" + m);
                    break;
            }
        }
        return list;
    }

    private List<UAHResourceInfo> getCpuMigList(OsenseCtrlDataRequest request) {
        int cpuClusterNum = request.cpuClusterNum;
        List<UAHResourceInfo> list = new ArrayList<>();
        for (int m = 0; m < cpuClusterNum - 1; m++) {
            switch (m) {
                case 0:
                    try {
                        UAHResourceInfo migUp = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_UPMIGRATE, String.valueOf(request.cpuMigData[0][0]));
                        UAHResourceInfo migDown = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_DOWNMIGRATE, String.valueOf(request.cpuMigData[0][1]));
                        list.add(migUp);
                        list.add(migDown);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e(TAG, "invalid ControlData size");
                        return null;
                    }
                case 1:
                    try {
                        UAHResourceInfo migUp1 = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_UPMIGRATE, String.valueOf(request.cpuMigData[1][0]));
                        UAHResourceInfo migDown1 = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_DOWNMIGRATE, String.valueOf(request.cpuMigData[1][1]));
                        list.add(migUp1);
                        list.add(migDown1);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        Log.e(TAG, "invalid ControlData size");
                        return null;
                    }
                default:
                    Log.e(TAG, "ControlData: unsupport gpu cluster num:" + m);
                    break;
            }
        }
        return list;
    }

    public void setControlData(OsenseCtrlDataRequest request) {
        int cpuClusterNum = request.cpuClusterNum;
        int gpuClusterNum = request.gpuClusterNum;
        if (cpuClusterNum <= 1 || gpuClusterNum <= 0) {
            Log.e(TAG, "ControlData: cluster number error, CPU: " + cpuClusterNum + " GPU " + gpuClusterNum);
            return;
        }
        List<UAHResourceInfo> listAll = new ArrayList<>();
        List<UAHResourceInfo> listCpu = getCpuCoreFreqList(request);
        List<UAHResourceInfo> listGpu = getGpuCoreFreqList(request);
        List<UAHResourceInfo> listCpuMig = getCpuMigList(request);
        listAll.addAll(listCpu);
        listAll.addAll(listGpu);
        listAll.addAll(listCpuMig);
        UAHPerfManager.getInstance().tansUahRuleCtrl(9001, 1, listAll);
    }

    private List<UAHResourceInfo> getGameUpgradeList(int src, int type, int p1, int p2, int p3, String p4) {
        List<UAHResourceInfo> list = new ArrayList<>();
        if (p1 <= 0) {
            Log.w(TAG, "No perfColocate sent");
        } else {
            UAHResourceInfo info = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_MIN_TASK_UTIL_FOR_COLOCATION, String.valueOf(p1));
            list.add(info);
        }
        if (p4 == null) {
            Log.e(TAG, "No foregroundCpus sent");
        } else {
            UAHResourceInfo info2 = new UAHResourceInfo(UAHResourceConstants.CPU_CPUSET_FOREGROUND_CPUS, p4);
            list.add(info2);
        }
        return list;
    }

    private void clrGameUpgradeMode(int src, int type) {
        List<UAHResourceInfo> list = new ArrayList<>();
        UAHResourceInfo infoColocate = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_MIN_TASK_UTIL_FOR_COLOCATION, String.valueOf(-1));
        UAHResourceInfo infoForegroundCpus = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_MIN_TASK_UTIL_FOR_COLOCATION, null);
        list.add(infoColocate);
        list.add(infoForegroundCpus);
        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list);
    }

    private List<UAHResourceInfo> getGroupMigInfo(String groupMigValue) {
        List<UAHResourceInfo> list = new ArrayList<>();
        if (groupMigValue.contains("-")) {
            String[] splitsGroup = groupMigValue.split("-");
            if (splitsGroup.length >= 2) {
                int up = Integer.parseInt(splitsGroup[0]);
                int down = Integer.parseInt(splitsGroup[1]);
                if (up <= down) {
                    Log.e(TAG, "group up down migrate set error :up must be bigger than down!");
                } else if (up == -1 || down == -1) {
                    Log.e(TAG, "Error, invalid group up down migrate value ");
                } else {
                    UAHResourceInfo groupUp = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_GROUP_UPMIGRATE, String.valueOf(up));
                    UAHResourceInfo groupDown = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_GROUP_DOWNMIGRATE, String.valueOf(down));
                    list.add(groupUp);
                    list.add(groupDown);
                }
            }
        }
        return list;
    }

    private List<UAHResourceInfo> getColocationList(String str) {
        List<UAHResourceInfo> listGroupMig;
        List<UAHResourceInfo> list = new ArrayList<>();
        if (str.contains(":")) {
            String[] configColocation = str.split(",");
            for (String config : configColocation) {
                String[] splits = config.split(":");
                if (splits.length >= 2) {
                    String key = splits[0];
                    String value = splits[1];
                    if (key.equals("colocation")) {
                        UAHResourceInfo colocation = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_MIN_TASK_UTIL_FOR_COLOCATION, value);
                        list.add(colocation);
                    } else if (key.equals("groupmigrate") && (listGroupMig = getGroupMigInfo(value)) != null) {
                        list.addAll(listGroupMig);
                    }
                }
            }
        }
        return list;
    }

    private List<UAHResourceInfo> getTargetLoadList(String targetLoadString) {
        List<UAHResourceInfo> list = new ArrayList<>();
        if (targetLoadString != null) {
            if (targetLoadString.contains("-")) {
                String[] configForEachCluster = targetLoadString.split(",");
                for (int i = 0; i < configForEachCluster.length; i++) {
                    String config = configForEachCluster[i];
                    String[] splits = config.split("-");
                    if (splits.length >= 2) {
                        String tl = splits[1];
                        try {
                            UAHResourceInfo info = new UAHResourceInfo(sOpCodeTargetLoad[i], tl);
                            list.add(info);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e(TAG, "getTargetLoadList1 invalid targetLoadString ");
                            return null;
                        }
                    }
                }
            } else {
                String[] cblStrs = targetLoadString.split(",");
                for (int index = 0; index < cblStrs.length; index++) {
                    if (!cblStrs[index].equals("-1")) {
                        try {
                            UAHResourceInfo info2 = new UAHResourceInfo(sOpCodeTargetLoad[index], cblStrs[index]);
                            list.add(info2);
                        } catch (ArrayIndexOutOfBoundsException e2) {
                            Log.e(TAG, "getTargetLoadList1 invalid targetLoadString ");
                            return null;
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<UAHResourceInfo> gameAdpCoreBoost(String[] coreBoost) {
        List<UAHResourceInfo> list = new ArrayList<>();
        if (coreBoost != null && coreBoost.length > 0) {
            for (int i = 0; i < coreBoost.length; i++) {
                try {
                    UAHResourceInfo info = new UAHResourceInfo(sOpCodeAdpXpuCoreBoost[i], coreBoost[i].trim());
                    list.add(info);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e(TAG, "gameAdpCoreBoost invalid length");
                    return null;
                }
            }
        }
        return list;
    }

    private List<UAHResourceInfo> gameAdpCoreLimit(String[] coreLimit) {
        List<UAHResourceInfo> list = new ArrayList<>();
        if (coreLimit != null && coreLimit.length > 0) {
            for (int i = 0; i < coreLimit.length; i++) {
                try {
                    UAHResourceInfo info = new UAHResourceInfo(sOpCodeAdpXpuCoreLimit[i], coreLimit[i].trim());
                    list.add(info);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e(TAG, "gameAdpCoreLimit invalid length");
                    return null;
                }
            }
        }
        return list;
    }

    private List<UAHResourceInfo> gameAdpFreqBoost(String[] freqBoost) {
        List<UAHResourceInfo> list = new ArrayList<>();
        if (freqBoost != null && freqBoost.length > 0) {
            for (int i = 0; i < freqBoost.length; i++) {
                try {
                    UAHResourceInfo info = new UAHResourceInfo(sOpCodeAdpXpuFreqBoost[i], freqBoost[i].trim());
                    list.add(info);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e(TAG, "gameAdpFreqBoost invalid length");
                    return null;
                }
            }
        }
        return list;
    }

    private List<UAHResourceInfo> gameAdpFreqLimit(String[] freqLimit) {
        List<UAHResourceInfo> list = new ArrayList<>();
        if (freqLimit != null && freqLimit.length > 0) {
            for (int i = 0; i < freqLimit.length; i++) {
                try {
                    UAHResourceInfo info = new UAHResourceInfo(sOpCodeAdpXpuFreqLimit[i], freqLimit[i].trim());
                    list.add(info);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e(TAG, "gameAdpFreqLimit invalid length");
                    return null;
                }
            }
        }
        return list;
    }

    private List<UAHResourceInfo> getGameAdpList(String strAdp) {
        if (strAdp != null && strAdp.length() > 0) {
            List<UAHResourceInfo> listAll = new ArrayList<>();
            String[] allTypeParamTable = strAdp.split(";");
            for (int index = 0; index < allTypeParamTable.length; index++) {
                switch (index) {
                    case 0:
                        String[] coreBoost = allTypeParamTable[index].split(",");
                        List<UAHResourceInfo> coreBoostList = gameAdpCoreBoost(coreBoost);
                        if (coreBoostList != null) {
                            listAll.addAll(coreBoostList);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        String[] coreLimit = allTypeParamTable[index].split(",");
                        List<UAHResourceInfo> coreLimitList = gameAdpCoreLimit(coreLimit);
                        if (coreLimitList != null) {
                            listAll.addAll(coreLimitList);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        String[] freqBoost = allTypeParamTable[index].split(",");
                        List<UAHResourceInfo> freqBoostList = gameAdpFreqBoost(freqBoost);
                        if (freqBoostList != null) {
                            listAll.addAll(freqBoostList);
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        String[] freqLimit = allTypeParamTable[index].split(",");
                        List<UAHResourceInfo> freqLimitList = gameAdpFreqLimit(freqLimit);
                        if (freqLimitList != null) {
                            listAll.addAll(freqLimitList);
                            break;
                        } else {
                            break;
                        }
                }
            }
            return listAll;
        }
        return null;
    }

    private int parseSrcId(int src) {
        switch (src) {
            case 2:
                int ruleStateNew = POWERMODE.MODE_HIGH_RERFORMANCE.ordinal();
                return ruleStateNew;
            case 3:
                int ruleStateNew2 = POWERMODE.MODE_POWER_SAVE.ordinal();
                return ruleStateNew2;
            case 4:
            default:
                Log.e(TAG, "parseSrcId unsupport  src = " + src);
                return -1;
            case 5:
                int ruleStateNew3 = POWERMODE.MODE_BENCHMARK.ordinal();
                return ruleStateNew3;
            case 6:
                int ruleStateNew4 = POWERMODE.MODE_SUPER_POWER_SAVE.ordinal();
                return ruleStateNew4;
        }
    }

    public void adaptSetNotification(String identity, OsenseNotifyRequest request) {
        int src = request.getMsgSrc();
        int type = request.getMsgType();
        int p1 = request.getParam1();
        int p2 = request.getParam2();
        int p3 = request.getParam3();
        String p4 = request.getParam4();
        Log.d(TAG, "adaptSetNotification identity = " + identity + "src = " + src + " , type " + type + " ,p1 = " + p1 + " ,p2 = " + p2 + " ,p3 = " + p3 + " ,p4 =" + p4);
        switch (src) {
            case 1:
            case 10:
            case 11:
            case 12:
            case 16:
            case 110:
            case 129:
            case 134:
            case 1000:
                UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, null);
                return;
            case 2:
            case 3:
            case 5:
            case 6:
                int newRuleState = parseSrcId(src);
                if (type == 1) {
                    UAHPerfManager.getInstance().tansUahRuleCtrl(200, newRuleState, null);
                    return;
                } else {
                    UAHPerfManager.getInstance().tansUahRuleCtrl(200, POWERMODE.MODE_NORMAL.ordinal(), null);
                    return;
                }
            case 8:
                switch (type) {
                    case 0:
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, null);
                        return;
                    case 1:
                    case 2:
                        List<UAHResourceInfo> list = new ArrayList<>();
                        if (p1 == 1) {
                            UAHResourceInfo info = new UAHResourceInfo(16843680, String.valueOf(0));
                            list.add(info);
                        }
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list);
                        return;
                    default:
                        Log.e(TAG, "Unknown notify type: " + src + " - " + type);
                        return;
                }
            case 9:
                switch (type) {
                    case 0:
                        List<UAHResourceInfo> list2 = new ArrayList<>();
                        UAHResourceInfo infoTouchBoost = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_TOUCH_BOOST, "enable 1");
                        list2.add(infoTouchBoost);
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list2);
                        return;
                    case 1:
                        List<UAHResourceInfo> list3 = new ArrayList<>();
                        UAHResourceInfo infoTouchBoost2 = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_TOUCH_BOOST, "enable 0");
                        UAHResourceInfo infoFpsgo = new UAHResourceInfo(UAHResourceConstants.CPU_FPSGO_FORCE_ONOFF, "0");
                        list3.add(infoTouchBoost2);
                        list3.add(infoFpsgo);
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list3);
                        return;
                    default:
                        Log.e(TAG, "Unknown notify type: " + src + " - " + type);
                        return;
                }
            case 14:
                switch (type) {
                    case 0:
                        clrGameUpgradeMode(src, type);
                        return;
                    case 1:
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, getGameUpgradeList(src, type, p1, p2, p3, p4));
                        return;
                    default:
                        Log.e(TAG, "UAH_NOTIFY_SRC_GAME_UPGRADE Unknown notify type: " + src + " - " + type);
                        return;
                }
            case 15:
                switch (type) {
                    case -1:
                    case 0:
                    case 1:
                        byte typeLocal = (byte) (type & 255);
                        int mergedPara = (p1 << 8) | Byte.toUnsignedInt(typeLocal);
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, mergedPara, null);
                        return;
                    default:
                        Log.e(TAG, "Unknown notify type: " + src + " - " + type);
                        return;
                }
            case 102:
                try {
                    switch (type) {
                        case 0:
                            List<UAHResourceInfo> list4 = new ArrayList<>();
                            UAHResourceInfo info2 = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_WINDOW_TICKS_UPDATE, String.valueOf(0));
                            list4.add(info2);
                            UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list4);
                            break;
                        case 1:
                            if (p1 == 0 || p1 == -1) {
                                List<UAHResourceInfo> list5 = new ArrayList<>();
                                UAHResourceInfo info3 = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_WINDOW_TICKS_UPDATE, String.valueOf(3));
                                list5.add(info3);
                                UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list5);
                                break;
                            } else {
                                List<UAHResourceInfo> list6 = new ArrayList<>();
                                UAHResourceInfo info4 = new UAHResourceInfo(UAHResourceConstants.CPU_SCHED_WINDOW_TICKS_UPDATE, String.valueOf(p1));
                                list6.add(info4);
                                UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list6);
                                break;
                            }
                            break;
                        default:
                            Log.e(TAG, "UAH_NOTIFY_SRC_NR_TICKS Unknown notify type: " + src + " - " + type);
                            return;
                    }
                    return;
                } catch (Exception e) {
                    Log.e(TAG, "UAH_NOTIFY_SRC_NR_TICKS error, because of " + e);
                    return;
                }
            case 111:
                switch (type) {
                    case -1:
                    case 0:
                    case 1:
                        List<UAHResourceInfo> list7 = new ArrayList<>();
                        UAHResourceInfo info5 = new UAHResourceInfo(16843680, String.valueOf(type));
                        list7.add(info5);
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, list7);
                        return;
                    default:
                        Log.e(TAG, "error cpu bouncing notification value: " + type);
                        return;
                }
            case 128:
                switch (type) {
                    case 0:
                    case 1:
                        return;
                    default:
                        Log.e(TAG, "Unknown notify type: " + src + " - " + type);
                        return;
                }
            case 130:
                switch (type) {
                    case 0:
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, null);
                        return;
                    case 1:
                        if (p4 != null && !"".equals(p4)) {
                            UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, getTargetLoadList(p4));
                            return;
                        }
                        return;
                    default:
                        Log.e(TAG, "Unknown notify type: " + src + " - " + type);
                        return;
                }
            case 131:
                switch (type) {
                    case 0:
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, null);
                        return;
                    case 1:
                        if (p4 != null && !"".equals(p4)) {
                            UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, getColocationList(p4));
                            return;
                        }
                        return;
                    default:
                        Log.e(TAG, "Unknown notify type: " + src + " - " + type);
                        return;
                }
            case 9000:
                switch (type) {
                    case 0:
                        UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, null);
                        return;
                    case 1:
                        if (p4 != null && !"".equals(p4)) {
                            UAHPerfManager.getInstance().tansUahRuleCtrl(src, type, getGameAdpList(p4));
                            return;
                        }
                        return;
                    default:
                        Log.e(TAG, "Unknown notify type: " + src + " - " + type);
                        return;
                }
            default:
                Log.e(TAG, "Unknown notify src: " + src + " - " + type);
                return;
        }
    }
}
