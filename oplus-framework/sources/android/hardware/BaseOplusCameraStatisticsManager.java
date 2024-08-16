package android.hardware;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.batterySipper.OplusBaseBatterySipper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemProperties;
import android.os.customize.OplusCustomizeConnectivityManager;
import android.provider.Settings;
import android.util.Log;
import android.view.viewextract.ViewExtractProxy;
import com.oplus.util.OplusHoraeThermalHelper;
import com.oplus.widget.OplusMaxLinearLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public class BaseOplusCameraStatisticsManager {
    public static final boolean LOG_PANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String SYSTEM_FOLDING_MODE_KEYS = "oplus_system_folding_mode";
    private final String TAG = getClass().getSimpleName();
    private final int CURRENT_READ_PERIOD = 1000;
    private final int READ_ACTIVITY_NAME_DELAY = 500;
    private final int ENTER_SIGN = 0;
    private final int EXIT_SIGN = 1;
    private volatile long mConnectTime = -1;
    private BatteryManager mBatteryManager = null;
    private IntentFilter mPlugFilter = null;
    private Intent mBatteryIntent = null;
    private Timer mTimer = null;
    private long mLastDuration = -1;
    private int mFrameCount = 0;
    private int mFrameInterval = 0;
    private int mAvgFrameInterval = 0;
    private int mMaxFrameInterval = Integer.MIN_VALUE;
    private int mMinFrameInterval = OplusMaxLinearLayout.INVALID_MAX_VALUE;
    private int mEnterChargeCounter = 0;
    private int mCurrentCount = -1;
    private int mCurrentReadCount = 1;
    private String mPreviewStopActivityName = null;
    public long mActivityEnterTime = -1;
    public Handler mDataReportThread = null;
    public String mActivityName = null;
    public String mEnterShellTemp = "";
    public String mEnterAmbientTemp = "";

    public void initHandlerThread() {
        if (this.mDataReportThread == null) {
            HandlerThread handlerThread = new HandlerThread("BaseOplusCameraStatisticsManagerReportThread");
            handlerThread.start();
            this.mDataReportThread = new Handler(handlerThread.getLooper());
        }
    }

    public void addInfo(final String cameraId, final int apiLevel, final long connectTime) {
        initHandlerThread();
        this.mDataReportThread.post(new Runnable() { // from class: android.hardware.BaseOplusCameraStatisticsManager.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    BaseOplusCameraStatisticsManager.this.mConnectTime = connectTime;
                    HashMap<String, String> eventMap = new HashMap<>();
                    String name = OplusCameraUtils.getActivityName();
                    eventMap.put(OplusBaseBatterySipper.BundlePkgName, ActivityThread.currentOpPackageName());
                    eventMap.put("cameraId", String.valueOf(cameraId));
                    eventMap.put("apLevel", apiLevel == 2 ? "2" : "1");
                    eventMap.put("halLevel", OplusCustomizeConnectivityManager.WLAN_POLICY_STRING_OFF);
                    eventMap.put("connentTime", String.valueOf(BaseOplusCameraStatisticsManager.this.mConnectTime));
                    eventMap.put(ViewExtractProxy.Constant.ACTIVITY_NAME, name);
                    BaseOplusCameraStatisticsManager.this.buildFoldEvent(eventMap);
                    BaseOplusCameraStatisticsManager.this.buildChargeCounter(eventMap, 0);
                    BaseOplusCameraStatisticsManager.this.mActivityName = name;
                    if (BaseOplusCameraStatisticsManager.this.mActivityEnterTime == -1) {
                        BaseOplusCameraStatisticsManager.this.mEnterShellTemp = String.valueOf(OplusHoraeThermalHelper.getInstance().getCurrentThermal());
                        BaseOplusCameraStatisticsManager.this.mEnterAmbientTemp = String.valueOf(Math.max(OplusHoraeThermalHelper.getInstance().getAmbientThermal(), -1L));
                        BaseOplusCameraStatisticsManager baseOplusCameraStatisticsManager = BaseOplusCameraStatisticsManager.this;
                        baseOplusCameraStatisticsManager.mActivityEnterTime = baseOplusCameraStatisticsManager.mConnectTime;
                    }
                    OplusStatistics.onCommon(ActivityThread.currentApplication().getApplicationContext(), "2012002", "openCamera", (Map<String, String>) eventMap, false);
                    if (BaseOplusCameraStatisticsManager.LOG_PANIC) {
                        Log.d(BaseOplusCameraStatisticsManager.this.TAG, "addInfo, eventMap: " + eventMap.toString());
                    }
                } catch (Exception exception) {
                    Log.e(BaseOplusCameraStatisticsManager.this.TAG, "failure in addInfo", exception);
                }
            }
        });
        this.mDataReportThread.postDelayed(new Runnable() { // from class: android.hardware.BaseOplusCameraStatisticsManager.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    BaseOplusCameraStatisticsManager.this.mActivityName = OplusCameraUtils.getActivityName();
                } catch (Exception e) {
                    Log.e(BaseOplusCameraStatisticsManager.this.TAG, "failure in get delay activity name");
                }
            }
        }, 500L);
    }

    public void buildCommonPreviewInfo(HashMap<String, String> eventMap, long disconnectTime, String cameraId, int apiLevel) {
        String str;
        try {
            if (this.mActivityName == null) {
                return;
            }
            String name = OplusCameraUtils.getActivityName();
            List<ActivityManager.RunningAppProcessInfo> processInfo = ActivityManager.getService().getRunningAppProcesses();
            ActivityManager.RunningAppProcessInfo info = processInfo.get(0);
            boolean processIsFocused = info.isFocused;
            eventMap.put(OplusBaseBatterySipper.BundlePkgName, ActivityThread.currentOpPackageName());
            eventMap.put("camera_id", String.valueOf(cameraId));
            try {
                eventMap.put("apLevel", apiLevel == 2 ? "2" : "1");
                eventMap.put("halLevel", OplusCustomizeConnectivityManager.WLAN_POLICY_STRING_OFF);
                eventMap.put("preview_time", String.valueOf(disconnectTime - this.mConnectTime));
                eventMap.put("connentTime", String.valueOf(this.mActivityEnterTime));
                eventMap.put("enterShellTemp", this.mEnterShellTemp);
                eventMap.put("enterAmbientTemp", this.mEnterAmbientTemp);
                eventMap.put("exitShellTemp", String.valueOf(OplusHoraeThermalHelper.getInstance().getCurrentThermal()));
                eventMap.put("exitAmbientTemp", String.valueOf(Math.max(OplusHoraeThermalHelper.getInstance().getAmbientThermal(), -1L)));
                eventMap.put(ViewExtractProxy.Constant.ACTIVITY_NAME, this.mActivityName);
                buildFoldEvent(eventMap);
                buildChargeCounter(eventMap, 1);
                int i = this.mAvgFrameInterval;
                if (i != 0) {
                    eventMap.put("avg_frame_interval", String.valueOf(i));
                    eventMap.put("max_frame_interval", String.valueOf(this.mMaxFrameInterval));
                    eventMap.put("min_frame_interval", String.valueOf(this.mMinFrameInterval));
                }
                if (LOG_PANIC) {
                    String str2 = this.TAG;
                    StringBuilder append = new StringBuilder().append("buildCommonPreviewInfo, processIsFocused: ").append(processIsFocused).append(", mActivityName: ").append(this.mActivityName).append(", name: ").append(name).append(", mPreviewStopActivityName: ");
                    String str3 = this.mPreviewStopActivityName;
                    if (str3 == null) {
                        str3 = "";
                    }
                    Log.d(str2, append.append(str3).toString());
                }
                if (!processIsFocused || !this.mActivityName.equals(name) || ((str = this.mPreviewStopActivityName) != null && !this.mActivityName.equals(str))) {
                    long j = this.mActivityEnterTime;
                    long totalTime = disconnectTime - j;
                    if (j != -1) {
                        eventMap.put("total_activity_use_time", String.valueOf(totalTime));
                    }
                    this.mConnectTime = -1L;
                    this.mActivityEnterTime = -1L;
                    this.mPreviewStopActivityName = null;
                    this.mCurrentCount = -1;
                    this.mCurrentReadCount = 1;
                }
                this.mFrameInterval = 0;
                this.mFrameCount = 0;
                this.mAvgFrameInterval = 0;
                this.mMaxFrameInterval = Integer.MIN_VALUE;
                this.mMinFrameInterval = OplusMaxLinearLayout.INVALID_MAX_VALUE;
                this.mActivityName = null;
            } catch (Exception e) {
                exception = e;
                Log.e(this.TAG, "failure in addPreviewInfo", exception);
            }
        } catch (Exception e2) {
            exception = e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildChargeCounter(HashMap<String, String> eventMap, int enterExit) {
        if (this.mBatteryManager == null) {
            this.mBatteryManager = (BatteryManager) ActivityThread.currentApplication().getApplicationContext().getSystemService("batterymanager");
        }
        if (enterExit != 0) {
            int exitChargeCounter = this.mBatteryManager.getIntProperty(1) / 1000;
            eventMap.put("power_consumption", String.valueOf(this.mEnterChargeCounter - exitChargeCounter));
            stopReadingCurrent(eventMap);
        } else {
            readCurrent();
            this.mEnterChargeCounter = this.mBatteryManager.getIntProperty(1) / 1000;
        }
        int chargingStatus = this.mBatteryManager.getIntProperty(6);
        eventMap.put("charging_status", String.valueOf(chargingStatus));
        if (LOG_PANIC) {
            Log.i(this.TAG, "buildChargeCounter, chargingStatus: " + chargingStatus);
        }
        if (5 == chargingStatus || 2 == chargingStatus) {
            if (this.mPlugFilter == null) {
                this.mPlugFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
            }
            if (this.mBatteryIntent == null) {
                this.mBatteryIntent = ActivityThread.currentApplication().getApplicationContext().registerReceiver(null, this.mPlugFilter);
            }
            int pluggedMode = this.mBatteryIntent.getIntExtra("plugged", -1);
            eventMap.put("charging_mode", String.valueOf(pluggedMode));
        }
    }

    private void readCurrent() {
        if (this.mTimer == null) {
            Timer timer = new Timer();
            this.mTimer = timer;
            timer.scheduleAtFixedRate(new TimerTask() { // from class: android.hardware.BaseOplusCameraStatisticsManager.3
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (BaseOplusCameraStatisticsManager.this.mBatteryManager != null) {
                        if (-1 == BaseOplusCameraStatisticsManager.this.mCurrentCount) {
                            BaseOplusCameraStatisticsManager baseOplusCameraStatisticsManager = BaseOplusCameraStatisticsManager.this;
                            baseOplusCameraStatisticsManager.mCurrentCount = baseOplusCameraStatisticsManager.mBatteryManager.getIntProperty(2);
                            return;
                        }
                        int currentValue = BaseOplusCameraStatisticsManager.this.mBatteryManager.getIntProperty(2);
                        BaseOplusCameraStatisticsManager.this.mCurrentCount += currentValue;
                        BaseOplusCameraStatisticsManager.this.mCurrentReadCount++;
                    }
                }
            }, 0L, 1000L);
        }
    }

    private void stopReadingCurrent(HashMap<String, String> eventMap) {
        int currentAvg;
        BatteryManager batteryManager;
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
        int i = this.mCurrentReadCount;
        if (1 == i && (batteryManager = this.mBatteryManager) != null) {
            int i2 = this.mCurrentCount;
            if (-1 == i2) {
                currentAvg = batteryManager.getIntProperty(2);
            } else {
                currentAvg = (i2 + batteryManager.getIntProperty(2)) / 2;
            }
        } else {
            currentAvg = this.mCurrentCount / i;
        }
        eventMap.put("current_avg", String.valueOf(currentAvg));
        if (LOG_PANIC) {
            Log.i(this.TAG, "stopReadingCurrent, currentAvg: " + currentAvg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildFoldEvent(HashMap<String, String> eventMap) {
        int fold = Settings.Global.getInt(ActivityThread.currentApplication().getApplicationContext().getContentResolver(), "oplus_system_folding_mode", -1);
        eventMap.put("system_folding_mode", String.valueOf(fold));
    }

    public void statisticFrameRate() {
        if (this.mFrameCount == 0) {
            this.mLastDuration = System.currentTimeMillis();
        }
        this.mFrameCount++;
        this.mFrameInterval++;
        long currentTimeStamp = System.currentTimeMillis();
        if (1000 <= currentTimeStamp - this.mLastDuration) {
            int i = this.mFrameInterval;
            if (i > this.mMaxFrameInterval) {
                this.mMaxFrameInterval = i;
            }
            if (i < this.mMinFrameInterval) {
                this.mMinFrameInterval = i;
            }
            int i2 = this.mAvgFrameInterval;
            if (i2 == 0) {
                this.mAvgFrameInterval = i;
            } else {
                this.mAvgFrameInterval = (i2 + i) / 2;
            }
            this.mFrameInterval = 0;
            this.mLastDuration = currentTimeStamp;
        }
    }

    public void setActivityName(final String name) {
        initHandlerThread();
        this.mDataReportThread.post(new Runnable() { // from class: android.hardware.BaseOplusCameraStatisticsManager.4
            @Override // java.lang.Runnable
            public void run() {
                if (BaseOplusCameraStatisticsManager.LOG_PANIC) {
                    Log.d(BaseOplusCameraStatisticsManager.this.TAG, "setActivityName, name: " + name);
                }
                BaseOplusCameraStatisticsManager.this.mPreviewStopActivityName = name;
            }
        });
    }
}
