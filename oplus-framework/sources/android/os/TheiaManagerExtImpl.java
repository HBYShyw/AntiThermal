package android.os;

import android.app.KeyguardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class TheiaManagerExtImpl implements ITheiaManagerExt {
    private static final String KEY_DEAD_MOMENT = "deadMoment";
    private static final String KEY_LOG_INFO = "logInfo";
    private static final String KEY_LOG_TYPE = "logType";
    private static final String KEY_PACKAGE_NAME = "packageName";
    private static final String KEY_PID = "pid";
    private static final String KEY_UID = "uid";
    private static final String SHUTDOWN_ACTION_PROPERTY = "sys.shutdown.requested";
    public static final String THEIA_STAMP_EVENTID = "010106";
    private static final String VALUE_ANR_EVENT = "ApplicationFGAnr";
    private static List<String> sTransitionTimeoutIgnoreList = Arrays.asList("android.cts.install", "com.android.permissioncontroller");
    private static TheiaManager sTheiaManager = TheiaManager.getInstance();
    private static volatile TheiaManagerExtImpl sInstance = null;
    private volatile Context mContext = null;
    private volatile KeyguardManager mKeyguardManager = null;
    private volatile PowerManager mPowerManager = null;
    private Object mConfigLock = new Object();
    private volatile Map<Long, TheiaEventState> mTheiaEventStateMap = new HashMap();
    private volatile long mCurrentVersionId = 0;
    private ArrayMap<TheiaEventInfo, Long> mTheiaEventUploadTime = new ArrayMap<>();
    private ArrayList<Long> mAnrEventRecordStampList = new ArrayList<>(Arrays.asList(1538L, 1794L, 3L));

    public TheiaManagerExtImpl(Object base) {
    }

    public static TheiaManagerExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (TheiaManagerExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new TheiaManagerExtImpl(base);
                }
            }
        }
        if (sInstance.mContext == null && base != null && (base instanceof Context)) {
            sInstance.mContext = (Context) base;
        }
        return sInstance;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:39:0x010f
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    public boolean sendEvent(long r16, long r18, int r20, int r21, long r22, java.lang.String r24) {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.TheiaManagerExtImpl.sendEvent(long, long, int, int, long, java.lang.String):boolean");
    }

    public long getTheiaEventTypeWhenANR(String pkgName) {
        if (pkgName == null) {
            Log.e("ITheiaManagerExt", "getTheiaEventTypeWhenANR: invaild parament.");
            return -1L;
        }
        if (pkgName.equals("com.android.systemui")) {
            return 1794L;
        }
        if (pkgName.equals("com.android.launcher")) {
            return 1538L;
        }
        return 3L;
    }

    public long getTheiaEventTypeWhenAppCrash(String pkgName) {
        if (pkgName == null) {
            Log.e("ITheiaManagerExt", "getTheiaEventTypeWhenAppCrash: invaild parament.");
            return -1L;
        }
        if (pkgName.equals("com.android.systemui")) {
            return 1793L;
        }
        if (pkgName.equals("com.android.launcher")) {
            return 1537L;
        }
        return 5L;
    }

    private boolean checkEventReportedEnv(TheiaEventState eventState) {
        long checkCondition = eventState.getCondition();
        if ((1 & checkCondition) != 0 && isKeyguardLocked()) {
            Log.d("ITheiaManagerExt", "[checkEventReportedEnv] the keyguard is locked.");
            return false;
        }
        if ((2 & checkCondition) != 0 && isScreenOff()) {
            Log.d("ITheiaManagerExt", "[checkEventReportedEnv] the device is in an interactive state.");
            return false;
        }
        if ((4 & checkCondition) != 0 && isShuttingDown()) {
            Log.d("ITheiaManagerExt", "[checkEventReportedEnv] the system is shutting down.");
            return false;
        }
        return true;
    }

    private boolean checkEventReportedFrequent(TheiaEventInfo event, long reportReqThreshold) {
        synchronized (this.mConfigLock) {
            long eventId = event.getTheiaID();
            if (reportReqThreshold <= 0) {
                Log.w("ITheiaManagerExt", "[checkEventUploadFrequent] " + eventId + " is not supported.");
                return false;
            }
            long currentTime = SystemClock.uptimeMillis();
            if (this.mTheiaEventUploadTime.get(event) == null) {
                this.mTheiaEventUploadTime.put(event, Long.valueOf(currentTime));
                return false;
            }
            if (currentTime - this.mTheiaEventUploadTime.get(event).longValue() <= reportReqThreshold) {
                Log.w("ITheiaManagerExt", "[checkEventUploadFrequent] " + eventId + " is reported frequently.");
                return true;
            }
            this.mTheiaEventUploadTime.put(event, Long.valueOf(currentTime));
            return false;
        }
    }

    private void recordAnrEventStamp(long eventId, long timeStamp, int pid, int uid, long logInfo, String extraInfo) {
        HashMap<String, String> logMap = new HashMap<>();
        int processNameIndex = extraInfo.indexOf("processName:");
        int packageVersionIndex = extraInfo.indexOf("packageVersion:");
        String packageName = extraInfo;
        if (processNameIndex != -1 && packageVersionIndex != -1) {
            packageName = extraInfo.substring("processName:".length() + processNameIndex, packageVersionIndex - 1);
        }
        Log.d("ITheiaManagerExt", "recordAnrEventStamp: pnIndex: " + processNameIndex + " pkIndex: " + packageVersionIndex + " packageName: " + packageName);
        logMap.put(KEY_LOG_TYPE, VALUE_ANR_EVENT);
        logMap.put("pid", String.valueOf(pid));
        logMap.put("uid", String.valueOf(uid));
        logMap.put(KEY_LOG_INFO, String.valueOf(logInfo));
        logMap.put("packageName", packageName);
        logMap.put(KEY_DEAD_MOMENT, String.valueOf(timeStamp));
        OplusManager.onStamp(THEIA_STAMP_EVENTID, logMap);
    }

    private boolean isShuttingDown() {
        String shutdown = SystemProperties.get(SHUTDOWN_ACTION_PROPERTY, "");
        if (TextUtils.isEmpty(shutdown)) {
            return false;
        }
        Log.d("ITheiaManagerExt", "[isShuttingDown] reason:" + shutdown);
        return true;
    }

    private boolean isScreenOff() {
        if (this.mPowerManager == null) {
            checkServiceState();
            if (this.mPowerManager == null) {
                Log.w("ITheiaManagerExt", "[isScreenOff] Failed to get the service of power manager.");
                return true;
            }
        }
        return !this.mPowerManager.isInteractive();
    }

    private boolean isKeyguardLocked() {
        if (this.mKeyguardManager == null) {
            checkServiceState();
            if (this.mKeyguardManager == null) {
                Log.w("ITheiaManagerExt", "[isKeyguardLocked] Failed to get the service of keyguard manager.");
                return true;
            }
        }
        return this.mKeyguardManager.isKeyguardLocked();
    }

    private void checkServiceState() {
        if (this.mContext == null) {
            Log.w("ITheiaManagerExt", "[checkServiceState] context is null.");
            return;
        }
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        }
        if (this.mPowerManager == null) {
            this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
        }
    }

    public void updateEventState(Map<Long, TheiaEventState> eventStateMap, long versionId) {
        synchronized (this.mConfigLock) {
            this.mTheiaEventStateMap = eventStateMap;
            this.mCurrentVersionId = versionId;
        }
    }
}
