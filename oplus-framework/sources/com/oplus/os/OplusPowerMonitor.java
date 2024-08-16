package com.oplus.os;

import android.content.Context;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.app.KernelWakeLockInfo;
import com.oplus.app.KernelWakeupInfo;
import com.oplus.app.OplusAlarmInfo;
import com.oplus.app.OplusWakeLockInfo;
import com.oplus.app.SuspendInfo;
import com.oplus.os.IOplusPowerMonitor;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes.dex */
public class OplusPowerMonitor {
    private static final String POWER_MONITOR_SERVICE_NAME = "power_monitor";
    private static final String TAG = "OplusPowerMonitor";
    private static volatile OplusPowerMonitor sInstance = null;
    private Context mContext;
    private IOplusPowerMonitor mOplusPowerMonitorService;

    private OplusPowerMonitor(Context context) {
        this.mOplusPowerMonitorService = null;
        this.mContext = null;
        IBinder serviceBinder = ServiceManager.getService(POWER_MONITOR_SERVICE_NAME);
        this.mOplusPowerMonitorService = IOplusPowerMonitor.Stub.asInterface(serviceBinder);
        this.mContext = context;
        StringBuilder append = new StringBuilder().append("mOplusPowerMonitorService = ");
        String str = this.mOplusPowerMonitorService;
        Log.d(TAG, append.append(str == null ? "null" : str).toString());
    }

    public static OplusPowerMonitor getInstance(Context context) {
        if (sInstance == null || !sInstance.isInited()) {
            synchronized (OplusPowerMonitor.class) {
                if (sInstance == null || !sInstance.isInited()) {
                    sInstance = new OplusPowerMonitor(context);
                }
            }
        }
        return sInstance;
    }

    private boolean isInited() {
        return this.mOplusPowerMonitorService != null;
    }

    public void recordAlarmWakeupEvent() {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.recordAlarmWakeupEvent();
                return;
            } catch (Exception e) {
                Log.e(TAG, "recordAlarmWakeupEvent failed.", e);
                return;
            }
        }
        Log.e(TAG, "recordAlarmWakeupEvent failed: service unavailable");
    }

    public void recordAppWakeupEvent(int alarmType, String alarmPackageName) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.recordAppWakeupEvent(alarmType, alarmPackageName);
                return;
            } catch (Exception e) {
                Log.e(TAG, "recordAppWakeupEvent failed.", e);
                return;
            }
        }
        Log.e(TAG, "recordAppWakeupEvent failed: service unavailable");
    }

    public void recordAppWakeupInfoEvent(OplusAlarmInfo oplusAlarmInfo) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.recordAppWakeupInfoEvent(oplusAlarmInfo);
                return;
            } catch (Exception e) {
                Log.e(TAG, "recordAppWakeupInfoEvent failed.", e);
                return;
            }
        }
        Log.e(TAG, "recordAppWakeupInfoEvent failed: service unavailable");
    }

    public void resetWakeupEventRecords() {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.resetWakeupEventRecords();
                return;
            } catch (Exception e) {
                Log.e(TAG, "resetWakeupEventRecords failed.", e);
                return;
            }
        }
        Log.e(TAG, "resetWakeupEventRecords failed: service unavailable");
    }

    public List<OplusAlarmInfo> getAlarmWakeUpInfo(long starttime, long endtime) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getAlarmWakeUpInfo(starttime, endtime);
            } catch (Exception e) {
                Log.e(TAG, "getAlarmWakeUpInfo failed.", e);
                return null;
            }
        }
        Log.e(TAG, "getAlarmWakeUpInfo failed: service unavailable");
        return null;
    }

    public void recordWakeLockAcquireEvent(OplusWakeLockInfo wakeLockInfo) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.recordWakeLockAcquireEvent(wakeLockInfo);
                return;
            } catch (Exception e) {
                Log.e(TAG, "recordWakeLockAcquireEvent failed.", e);
                return;
            }
        }
        Log.e(TAG, "recordWakeLockAcquireEvent failed: service unavailable");
    }

    public void recordWakeLockReleaseEvent(OplusWakeLockInfo wakeLockInfo) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.recordWakeLockReleaseEvent(wakeLockInfo);
                return;
            } catch (Exception e) {
                Log.e(TAG, "recordWakeLockEvent failed.", e);
                return;
            }
        }
        Log.e(TAG, "recordWakeLockEvent failed: service unavailable");
    }

    public List<OplusWakeLockInfo> getWakeLockInfo(long starttime, long endtime) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getWakeLockInfo(starttime, endtime);
            } catch (Exception e) {
                Log.e(TAG, "getWakeLockInfo failed.", e);
                return null;
            }
        }
        Log.e(TAG, "getWakeLockInfo failed: service unavailable");
        return null;
    }

    public String getRpmStatsFilePath() {
        String path = null;
        if (this.mOplusPowerMonitorService != null) {
            try {
                Class<?> powerMonitorClass = Class.forName(IOplusPowerMonitor.DESCRIPTOR);
                Method getRpmStatsFilePathMethod = powerMonitorClass.getMethod("getRpmStatsFilePath", new Class[0]);
                if (getRpmStatsFilePathMethod != null) {
                    getRpmStatsFilePathMethod.setAccessible(true);
                    Object result = getRpmStatsFilePathMethod.invoke(this.mOplusPowerMonitorService, new Object[0]);
                    path = (String) result;
                }
            } catch (Exception e) {
                Log.e(TAG, "getRpmStatsFilePath failed.", e);
            }
        } else {
            Log.e(TAG, "getRpmStatsFilePath failed: service unavailable");
        }
        Log.d(TAG, "getRpmStatsFilePath:" + path);
        return path;
    }

    public String getRpmMasterStatsFilePath() {
        String path = null;
        if (this.mOplusPowerMonitorService != null) {
            try {
                Class<?> powerMonitorClass = Class.forName(IOplusPowerMonitor.DESCRIPTOR);
                Method getRpmMasterStatsFilePathMethod = powerMonitorClass.getMethod("getRpmMasterStatsFilePath", new Class[0]);
                if (getRpmMasterStatsFilePathMethod != null) {
                    getRpmMasterStatsFilePathMethod.setAccessible(true);
                    Object result = getRpmMasterStatsFilePathMethod.invoke(this.mOplusPowerMonitorService, new Object[0]);
                    path = (String) result;
                }
            } catch (Exception e) {
                Log.e(TAG, "getRpmMasterStatsFilePath failed.", e);
            }
        } else {
            Log.e(TAG, "getRpmMasterStatsFilePath failed: service unavailable");
        }
        Log.d(TAG, "getRpmMasterStatsFilePath:" + path);
        return path;
    }

    public void acquireSuspendBlocker(String name) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.acquireSuspendBlocker(name);
                return;
            } catch (Exception e) {
                Log.e(TAG, "acquireSuspendBlocker failed.", e);
                return;
            }
        }
        Log.e(TAG, "acquireSuspendBlocker failed: service unavailable");
    }

    public void releaseSuspendBlocker(String name) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                iOplusPowerMonitor.releaseSuspendBlocker(name);
                return;
            } catch (Exception e) {
                Log.e(TAG, "releaseSuspendBlocker failed.", e);
                return;
            }
        }
        Log.e(TAG, "releaseSuspendBlocker failed: service unavailable");
    }

    public KernelWakeLockInfo[] getKernelWakeLockStats() {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getWakeLockStats();
            } catch (Exception e) {
                Log.e(TAG, "getKernelWakeLockStats failed", e);
                return null;
            }
        }
        Log.e(TAG, "getKernelWakeLockStats failed: service unavailable.");
        return null;
    }

    public KernelWakeupInfo[] getKernelWakeupStats() {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getWakeupStats();
            } catch (Exception e) {
                Log.e(TAG, "getKernelWakeupStats failed", e);
                return null;
            }
        }
        Log.e(TAG, "getKernelWakeupStats failed: service unavailable.");
        return null;
    }

    public SuspendInfo getKernelSuspendStats() {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getSuspendStats();
            } catch (Exception e) {
                Log.e(TAG, "getKernelSuspendStats failed", e);
                return null;
            }
        }
        Log.e(TAG, "getKernelSuspendStats failed: service unavailable.");
        return null;
    }

    public String getWakeups() {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getWakeups();
            } catch (Exception e) {
                Log.e(TAG, "getWakeups failed", e);
                return null;
            }
        }
        Log.e(TAG, "getWakeups failed: service unavailable.");
        return null;
    }

    public String[] getSubSystem() {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getSubSystem();
            } catch (Exception e) {
                Log.e(TAG, "getSubSystem failed", e);
                return null;
            }
        }
        Log.e(TAG, "getSubSystem failed: service unavailable.");
        return null;
    }

    public ParcelFileDescriptor getWakeLockInfoFileDescriptor(long starttime, long endtime) {
        IOplusPowerMonitor iOplusPowerMonitor = this.mOplusPowerMonitorService;
        if (iOplusPowerMonitor != null) {
            try {
                return iOplusPowerMonitor.getWakeLockInfoFileDescriptor(starttime, endtime);
            } catch (Exception e) {
                Log.e(TAG, "getWakeLockInfoFileDescriptor failed", e);
                return null;
            }
        }
        Log.e(TAG, "getWakeLockInfoFileDescriptor failed: service unavailable.");
        return null;
    }
}
