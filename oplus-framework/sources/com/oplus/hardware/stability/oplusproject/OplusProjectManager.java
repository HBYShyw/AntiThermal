package com.oplus.hardware.stability.oplusproject;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import vendor.oplus.hardware.stability.oplus_project.IOplusProject;

/* loaded from: classes.dex */
public class OplusProjectManager {
    private static final String TAG = "OplusProjectManager";
    private static volatile OplusProjectManager sInstance;
    private static String sServiceName = IOplusProject.DESCRIPTOR + "/default";
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.hardware.stability.oplusproject.OplusProjectManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.i(OplusProjectManager.TAG, OplusProjectManager.sServiceName + " binderDied");
            synchronized (OplusProjectManager.class) {
                OplusProjectManager.this.mProjectService = null;
            }
        }
    };
    private volatile IOplusProject mProjectService;

    /* loaded from: classes.dex */
    public static class AgingType {
        public static final int ACTION_GET_AGINGTEST_DUMP_CURRENT_COUNT = 1000090;
        public static final int ACTION_GET_AGINGTEST_FULL_DUMP_INFO = 1000082;
        public static final int ACTION_GET_AGINGTEST_LAST_DUMP_INFO = 1000080;
        public static final int ACTION_GET_AGINGTEST_MEMTEST_SUPPORT_INFO = 1000086;
        public static final int ACTION_GET_SET_AGINGTEST_ABNORMAL_INFO = 1000091;
        public static final int ACTION_GET_SET_AGINGTEST_APP_FATAL_FLAG_INFO = 1000093;
        public static final int ACTION_GET_SET_AGINGTEST_APP_FULL_TESTCOUNT = 1000095;
        public static final int ACTION_GET_SET_AGINGTEST_APP_INFO = 1000092;
        public static final int ACTION_GET_SET_AGINGTEST_APP_MAGIC_FLAG = 1000094;
        public static final int ACTION_GET_SET_AGINGTEST_APP_PART_TESTCOUNT = 1000096;
        public static final int ACTION_GET_SET_AGINGTEST_CONTROL_INFO = 1000083;
        public static final int ACTION_GET_SET_AGINGTEST_FLAG_INFO = 1000085;
        public static final int ACTION_GET_SET_AGINGTEST_MAGIC_FLAG = 1000084;
        public static final int ACTION_GET_SET_AGINGTEST_MEMTEST_ENABLE_INFO = 1000087;
        public static final int ACTION_GET_SET_AGINGTEST_MEMTEST_RESULT_INFO = 1000089;
        public static final int ACTION_GET_SET_AGINGTEST_STAGE_INFO = 1000088;
        public static final int ACTION_SET_AGINGTEST_VERSION_TO_DUMP_INFO = 1000081;
    }

    /* loaded from: classes.dex */
    public static class BootMode {
        public static final int MSM_BOOT_MODE_AGING = 998;
        public static final int MSM_BOOT_MODE_CHARGE = 7;
        public static final int MSM_BOOT_MODE_FACTORY = 3;
        public static final int MSM_BOOT_MODE_FASTBOOT = 1;
        public static final int MSM_BOOT_MODE_MOS = 6;
        public static final int MSM_BOOT_MODE_NORMAL = 0;
        public static final int MSM_BOOT_MODE_RECOVERY = 2;
        public static final int MSM_BOOT_MODE_RF = 4;
        public static final int MSM_BOOT_MODE_SAFE = 999;
        public static final int MSM_BOOT_MODE_SAU = 9;
        public static final int MSM_BOOT_MODE_SILENCE = 8;
        public static final int MSM_BOOT_MODE_WLAN = 5;
    }

    private OplusProjectManager() {
    }

    public static OplusProjectManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusProjectManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusProjectManager();
                }
            }
        }
        return sInstance;
    }

    private synchronized IOplusProject getService() {
        if (this.mProjectService == null) {
            IBinder binder = ServiceManager.getService(sServiceName);
            if (binder == null) {
                Log.w(TAG, "getService fail." + sServiceName);
                return null;
            }
            try {
                binder.linkToDeath(this.mDeathRecipient, 0);
                this.mProjectService = IOplusProject.Stub.asInterface(binder);
                if (this.mProjectService == null) {
                    Log.e(TAG, "asInterface fail.");
                }
            } catch (RemoteException e) {
                Log.e(TAG, "linkToDeath fail ", e);
                return null;
            }
        }
        return this.mProjectService;
    }

    public int getProjectId() {
        try {
            IOplusProject service = getService();
            if (service != null) {
                return service.get_project();
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(TAG, "get_project failed.", e);
            return -1;
        }
    }

    public int getPcbVersion() {
        try {
            IOplusProject service = getService();
            if (service != null) {
                return service.get_pcb_version();
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(TAG, "get_pcb_version failed.", e);
            return -1;
        }
    }

    public int getRfId() {
        try {
            IOplusProject service = getService();
            if (service != null) {
                return service.get_rf_type();
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(TAG, "get_rf_type failed.", e);
            return -1;
        }
    }

    public int getEngVersion() {
        try {
            IOplusProject service = getService();
            if (service != null) {
                return service.get_eng_version();
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(TAG, "get_eng_version failed.", e);
            return -1;
        }
    }

    public int getBootMode() {
        try {
            IOplusProject service = getService();
            if (service != null) {
                return service.get_ftmmode();
            }
            return -1;
        } catch (RemoteException | IllegalArgumentException e) {
            Log.e(TAG, "get_ftmmode failed.", e);
            return -1;
        }
    }

    public int[] readAgingData(int type) {
        try {
            IOplusProject service = getService();
            if (service != null) {
                return service.readAgingData(type);
            }
            return null;
        } catch (RemoteException | IllegalArgumentException e) {
            Log.e(TAG, "get_phoenix failed.", e);
            return null;
        }
    }

    public boolean saveAgingData(int type, int[] saveAgingData, int length) {
        try {
            IOplusProject service = getService();
            if (service != null) {
                return service.saveAgingData(type, saveAgingData, length);
            }
            return false;
        } catch (RemoteException | IllegalArgumentException e) {
            Log.e(TAG, "get_phoenix failed.", e);
            return false;
        }
    }
}
