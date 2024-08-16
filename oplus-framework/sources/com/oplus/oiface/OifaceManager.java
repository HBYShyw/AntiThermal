package com.oplus.oiface;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.oiface.IOIfaceInternalService;

/* loaded from: classes.dex */
public class OifaceManager {
    public static final int APP_ENTER = 7;
    public static final int APP_PIP_CLOSED = 9;
    public static final int APP_PIP_OPENED = 8;
    public static final int APP_PIP_SWITCH = 10;
    public static final int BIND_CPU_CLUSTER_ALL = 0;
    public static final int CPU_CLUSTER_GOLD = 1;
    public static final int CPU_CLUSTER_PRIME = 3;
    public static final int CPU_CLUSTER_SILVER = 2;
    private static final String DISCONNECTED = "{\"oiface\":\"disconnected\"}";
    public static final int FPS_PER_SECOND = 0;
    public static final int FPS_RAW_DATA = 1;
    public static final int GPA_CPU_CLUSTER_GOLD = 2;
    public static final int GPA_CPU_CLUSTER_PRIME = 4;
    public static final int GPA_CPU_CLUSTER_SILVER = 1;
    public static final int NETWORK_STATUS_DATA = 2;
    public static final int NETWORK_STATUS_DATA_OFF = 3;
    public static final int NETWORK_STATUS_WIFI = 0;
    public static final int NETWORK_STATUS_WIFI_OFF = 1;
    public static final int PERF_MODE_NORMAL = 0;
    public static final int PERF_MODE_PERFORMANCE = 2;
    public static final int PERF_MODE_POWER_SAVE = 1;
    public static final int REGISTER_GAME_SCENE = 1;
    public static final int REGISTER_GAME_STATUS = 2;
    public static final int SCREEN_OFF = 0;
    public static final int SCREEN_ON = 1;
    private static final String TAG = "OifaceManager";
    public static final int THERMAL_TEMP_BACK = 2;
    public static final int THERMAL_TEMP_FRONT = 1;
    public static final int THERMAL_TEMP_GOLD = 16;
    public static final int THERMAL_TEMP_GPU = 64;
    public static final int THERMAL_TEMP_PRIME = 32;
    public static final int THERMAL_TEMP_SIDE = 4;
    public static final int THERMAL_TEMP_SILVER = 8;
    private static volatile OifaceManager sInstance = null;
    private String mIdentity;
    private IBinder mRemote;
    private IOIfaceInternalService mService;
    private IOIfaceCallback mOifaceCallback = null;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.oiface.OifaceManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(OifaceManager.TAG, "Oiface died");
            OifaceManager.this.mService = null;
            if (OifaceManager.this.mOifaceCallback != null) {
                try {
                    OifaceManager.this.mOifaceCallback.onSystemNotify(OifaceManager.DISCONNECTED);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void checkService() {
        if (this.mService == null) {
            IBinder service = ServiceManager.getService("oplusoiface");
            this.mRemote = service;
            if (service == null) {
                Log.d(TAG, "unable to getService oplusoiface");
                return;
            }
            IOIfaceInternalService asInterface = IOIfaceInternalService.Stub.asInterface(service);
            this.mService = asInterface;
            if (asInterface != null) {
                try {
                    this.mRemote.linkToDeath(this.mDeathRecipient, 0);
                    return;
                } catch (Exception e) {
                    Log.e(TAG, "connect to oiface failed");
                    this.mService = null;
                    return;
                }
            }
            Log.d(TAG, "connect to oiface failed");
        }
    }

    private OifaceManager(String identity) {
        this.mIdentity = identity;
        checkService();
    }

    public static OifaceManager getInstance(String identity) {
        if (sInstance == null) {
            synchronized (OifaceManager.class) {
                if (sInstance == null) {
                    sInstance = new OifaceManager(identity);
                }
            }
        }
        return sInstance;
    }

    public boolean currentNetwork(int status) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.currentNetwork(status);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean bindGameTask(int type, int tid) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.bindGameTask(type, tid);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean enableHQV(int enable) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.enableHQV(enable);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public String registerHQV(String packageName, int type, String config) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            String result = iOIfaceInternalService.registerHQV(packageName, type, config);
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public boolean setHalfHQV(int half) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.setHalfHQV(half);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean registerClientThroughCosa(IOIfaceCallback cb, String json, int uid, int pid) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            int result = iOIfaceInternalService.registerClientThroughCosa(cb, json, uid, pid);
            Log.e(TAG, "registerClientThroughCosa return " + result);
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public String setGeneralSignalCosa(String json, int uid, int pid) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            String result = iOIfaceInternalService.setGeneralSignalCosa(json, uid, pid);
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public boolean registerNetworkListener(int thresholdMs, int minReportMs, IOIfaceCallback oifaceCallback) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.registerNetworkListener(thresholdMs, minReportMs, oifaceCallback);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean registerGameRoleListener(int type, String config, IOIfaceCallback oifaceCallback) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.registerGameRoleListener(type, config, oifaceCallback);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean unRegisterGameRoleListener(IOIfaceCallback oifaceCallback) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.unRegisterGameRoleListener(oifaceCallback);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public String getSupportGameStartPackage() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            String result = iOIfaceInternalService.getSupportGameStartPackage();
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public String getAllLoadInfo(String packageName) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            String result = iOIfaceInternalService.getAllLoadInfo(packageName);
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public boolean setGCPEffectMode(int mode) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            String result = iOIfaceInternalService.setGCPEffectMode(mode);
            Log.e(TAG, "setGCPEffectMode return " + result);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public String getDeviceID() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            String result = iOIfaceInternalService.getDeviceID();
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public boolean setPerfMode(int mode) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.setPerfMode(mode);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean notifyScreenEvent(int mode) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.notifyScreenEvent(mode);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean currentPkgStatus(int status, String packageName, String newPIPPackage, String oldPIPPackage) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.currentPkgStatus(status, packageName, newPIPPackage, oldPIPPackage);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public int getFPS(String packageName, int type) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return 0;
        }
        try {
            int fps = iOIfaceInternalService.getFPS(packageName, type);
            return fps;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public String generalOifaceSignal(String signal) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            String result = iOIfaceInternalService.generalOifaceSignal(signal);
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public boolean oifaceDecision(String decision) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.oifaceDecision(decision);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean oifaceControl(String control) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.oifaceControl(control);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public int getCpuClusterNum() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1;
        }
        try {
            return iOIfaceInternalService.getCpuClusterNum();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1;
        }
    }

    public long[] getCpuAvailableFreqTable(int type) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getCpuAvailableFreqTable(type);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public long[] getCpuLimitedFreqs(int type) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getCpuLimitedFreqs(type);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public long[] getCpuCurrentFreq(int type) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getCpuCurrentFreq(type);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public float[] getCpuLoads(int type) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getCpuLoads(type);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public long[] getGpuAvailableFreqTable() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getGpuAvailableFreqTable();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public long[] getGpuLimitedFreqs() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getGpuLimitedFreqs();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public long getGpuCurrentFreq() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1L;
        }
        try {
            return iOIfaceInternalService.getGpuCurrentFreq();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1L;
        }
    }

    public float getGpuLoad() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1.0f;
        }
        try {
            return iOIfaceInternalService.getGpuLoad();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1.0f;
        }
    }

    public float[] getThermalTemps(int type) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getThermalTemps(type);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public boolean enableHapticScreenCaptureService(int enable) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.enableHapticScreenCaptureService(enable);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean setTouchSensibility(int level) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.setTouchSensibility(level);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean setTouchResponsiveness(int level) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.setTouchResponsiveness(level);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean setGyroscopeLevel(int level) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.setGyroscopeLevel(level);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean setTouchProtection(boolean enabled) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.setTouchProtection(enabled);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean registerOifaceCallback(IOIfaceCallback oifaceCallback) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return false;
        }
        try {
            iOIfaceInternalService.registerOifaceCallback(oifaceCallback);
            this.mOifaceCallback = oifaceCallback;
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public int getBatteryRemain() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1;
        }
        try {
            return iOIfaceInternalService.getBatteryRemain();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1;
        }
    }

    public float getBatteryCurrentNow() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1.0f;
        }
        try {
            return iOIfaceInternalService.getBatteryCurrentNow();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1.0f;
        }
    }

    public int getSuperVOOCStatus() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1;
        }
        try {
            return iOIfaceInternalService.getSuperVOOCStatus();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1;
        }
    }

    public float[] getGPASystemInfo() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getGPASystemInfo();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public void setCoolExFilterType(int type, String config) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService != null) {
            try {
                iOIfaceInternalService.setCoolExFilterType(type, config);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public void setGameModeStatus(int status, String packageName) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService != null) {
            try {
                iOIfaceInternalService.setGameModeStatus(status, packageName);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public int getGameModeStatus() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1;
        }
        try {
            return iOIfaceInternalService.getGameModeStatus();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1;
        }
    }

    public String getCurrentGamePackage() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            String result = iOIfaceInternalService.getCurrentGamePackage();
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public void setInstalledGameList(String[] games) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService != null) {
            try {
                iOIfaceInternalService.setInstalledGameList(games);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public String[] getInstalledGameList() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getInstalledGameList();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public String getCpuTimeInState() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getCpuTimeInState();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public String triggerFrameStat(String status, String packageName) {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.triggerFrameStat(status, packageName);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public String getChipName() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getChipName();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public int[] getCpuClusterInfo() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return null;
        }
        try {
            return iOIfaceInternalService.getCpuClusterInfo();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public int getBatteryFCC() {
        checkService();
        IOIfaceInternalService iOIfaceInternalService = this.mService;
        if (iOIfaceInternalService == null) {
            return -1;
        }
        try {
            return iOIfaceInternalService.getBatteryFCC();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1;
        }
    }
}
