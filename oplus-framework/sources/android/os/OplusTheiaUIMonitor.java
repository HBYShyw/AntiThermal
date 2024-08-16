package android.os;

import android.app.ActivityThread;
import android.app.OplusActivityManager;
import android.app.servertransaction.ActivityLifecycleItem;
import android.app.servertransaction.ClientTransaction;
import android.content.Intent;
import android.net.OplusNetworkingControlManager;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.Log;
import android.util.StatsEvent;
import android.util.StatsLog;
import android.util.TimeUtils;
import com.oplus.neuron.NsConstants;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class OplusTheiaUIMonitor {
    private static final int BIND_APPLICATION_MSG = 110;
    private static final String CLASS_LOG = "android.util.Log";
    private static final long DEFAULT_CHECK_DOUBLE_INTERVAL;
    private static final long DEFAULT_CHECK_INTERVAL;
    private static final long DEFAULT_HUNG_MSG_THRESHOLD;
    private static final long DEFAULT_H_MSG_HANDLE_THRESHOLD;
    private static final double DEFAULT_LONG_MSG_PRECENT_THRESHOLD;
    private static final long DEFAULT_MSG_HANDLE_THRESHOLD;
    private static final long DEFAULT_MSG_WINDOW_LIMIT;
    private static final long DEFAULT_MSG_WINDOW_MIN_SIZE;
    private static final long DEFAULT_MSG_WINDOW_SIZE;
    private static final int DEFAULT_PENDING_MSG_LIMIT;
    private static final double DEFAULT_RECENT_MSG_PRECENT_THRESHOLD;
    private static final long DEFAULT_RECENT_MSG_WINDOW_MIN_SIZE;
    private static final long DEFAULT_RECENT_MSG_WINDOW_SIZE;
    private static final long DEFAULT_SMALL_MSG_THRESHOLD;
    private static final long DEFAULT_UPDATE_USAGE_THRESHOLD;
    private static final String EVENT_BLOCK = "Block";
    private static final String EVENT_MESSAGE = "Message";
    private static final String EVENT_RESUME = "Resume";
    private static final int EXECUTE_TRANSACTION_MSG = 159;
    private static final int GOT_TRACE_FREQUENTLY_THRESHOLD = 60000;
    private static final int HIGH_FREQUENCY_LEVEL = 2;
    private static final long JIFFIES_MS;
    private static final boolean LOG_SWITCH_ON;
    private static final int LOW_FREQUENCY_LEVEL = 0;
    private static final int MAX_OBJ_STRING_LEN = 128;
    private static final int MAX_RETRY_INIT_THRESHOLD = 6;
    private static final int MAX_TRACE_LENGTH = 30;
    private static final String MESSAGE_NULL = "NULL MESSAGE";
    private static final String METHOD_P = "p";
    private static final int MID_FREQUENCY_LEVEL = 1;
    private static final boolean PRINT_DETAIL_LOG;
    private static final int[] PROCESS_STATS_FORMAT;
    private static final int PROCESS_STAT_STIME = 3;
    private static final int PROCESS_STAT_UTIME = 2;
    private static final int RELAUNCH_ACTIVITY_MSG = 160;
    private static final int SCREEN_OFF_WAIT_TIME = 30000;
    private static final String TAG = "OplusTheiaUIMonitorTag";
    private static final boolean UIMONITOR_ENABLE_FOR_DEBUG;
    private static List<String> sIgnoreAppAgingTestList;
    private static List<String> sIgnoreAppList;
    private static OplusTheiaUIMonitor sInstance;
    private static Method sLogP;
    private static Looper sMainLooper;
    private static int sPid;
    private volatile StringBuilder mBlockTrace;
    private long[] mCheckInterval;
    private volatile int mCpuLoad;
    private SimpleDateFormat mDateFormat;
    private boolean mInitPackageInfo;
    private AtomicInteger mMessageGetStackCount;
    private int mMessageGetStackCountMax;
    private long mMessageGetStackInterval;
    private volatile long mMessageProcessStartTime;
    private MonitorThread mMonitorThread;
    private MsgsWindowBuffer mMsgsWindowBuffer;
    private final long[] mProcessStatsData;
    private int mRetryInitCount;
    private boolean mSpecialApp;
    private ArrayList<String> mSpecialAppList;
    private volatile long mStartUtmStmCount;
    private long mUILooperMessageTimeout;
    private boolean mUITimeoutEnable;

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.uito.detaillog", false);
        PRINT_DETAIL_LOG = z;
        UIMONITOR_ENABLE_FOR_DEBUG = SystemProperties.getBoolean("persist.sys.uito.enable", true);
        LOG_SWITCH_ON = SystemProperties.getBoolean("persist.sys.assert.panic", false) && z;
        JIFFIES_MS = 1000 / Os.sysconf(OsConstants._SC_CLK_TCK);
        PROCESS_STATS_FORMAT = new int[]{32, 544, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224};
        long j = SystemProperties.getLong("persist.sys.uito.windowsize", TimeUnit.SECONDS.toMillis(10L));
        DEFAULT_MSG_WINDOW_SIZE = j;
        long j2 = (long) (j * 0.9d);
        DEFAULT_MSG_WINDOW_MIN_SIZE = j2;
        DEFAULT_RECENT_MSG_WINDOW_SIZE = j / 2;
        DEFAULT_RECENT_MSG_WINDOW_MIN_SIZE = j2 / 2;
        DEFAULT_MSG_WINDOW_LIMIT = SystemProperties.getLong("persist.sys.uito.windowlimit", 50L);
        DEFAULT_H_MSG_HANDLE_THRESHOLD = SystemProperties.getLong("persist.sys.uito.mainthreshold", 100L);
        long j3 = SystemProperties.getLong("persist.sys.uito.handlethreshold", 300L);
        DEFAULT_MSG_HANDLE_THRESHOLD = j3;
        DEFAULT_UPDATE_USAGE_THRESHOLD = j3 * 3;
        DEFAULT_SMALL_MSG_THRESHOLD = j3 / 3;
        long j4 = SystemProperties.getLong("persist.sys.uito.checkinterval", TimeUnit.SECONDS.toMillis(6L));
        DEFAULT_CHECK_INTERVAL = j4;
        DEFAULT_CHECK_DOUBLE_INTERVAL = j4 * 2;
        DEFAULT_HUNG_MSG_THRESHOLD = SystemProperties.getLong("persist.sys.uito.hung", TimeUnit.SECONDS.toMillis(4L));
        DEFAULT_PENDING_MSG_LIMIT = SystemProperties.getInt("persist.sys.uito.pendingmsglimit", 5);
        DEFAULT_RECENT_MSG_PRECENT_THRESHOLD = SystemProperties.getInt("persist.sys.uito.recentprecent", 90) / 100.0d;
        DEFAULT_LONG_MSG_PRECENT_THRESHOLD = SystemProperties.getInt("persist.sys.uito.longprecent", 60) / 100.0d;
        sLogP = null;
        sInstance = null;
        sPid = Process.myPid();
        sMainLooper = null;
        sIgnoreAppList = Arrays.asList("com.coloros.backuprestore", "com.youku.phone", "com.dragon.read", "com.miHoYo.hkrpg", "com.hunantv.imgo.activity", "com.android.systemui", "com.android.launcher", "android.camera.cts");
        sIgnoreAppAgingTestList = Arrays.asList("com.oplus.camera", "com.coloros.phonemanager");
        try {
            Class clz = Class.forName(CLASS_LOG);
            Method declaredMethod = clz.getDeclaredMethod(METHOD_P, String.class, String.class);
            sLogP = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "failed to find class.");
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "failed to find method.");
        } catch (Exception e3) {
            Log.e(TAG, "failed to set sLogP");
        }
    }

    /* loaded from: classes.dex */
    private class TheiaConst {
        public static final long THEIA_ST_BASE = 4298113024L;
        public static final long THEIA_ST_UTO = 4298113029L;

        private TheiaConst() {
        }
    }

    private OplusTheiaUIMonitor() {
        this.mProcessStatsData = new long[4];
        boolean z = false;
        this.mUITimeoutEnable = false;
        this.mMonitorThread = null;
        this.mUILooperMessageTimeout = 6000L;
        this.mMessageGetStackCountMax = 2;
        this.mStartUtmStmCount = -1L;
        this.mMessageProcessStartTime = Long.MAX_VALUE;
        this.mBlockTrace = new StringBuilder();
        this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.mMessageGetStackCount = new AtomicInteger(0);
        this.mInitPackageInfo = false;
        this.mRetryInitCount = 0;
        this.mSpecialApp = false;
        long j = DEFAULT_CHECK_INTERVAL;
        this.mCheckInterval = new long[]{j, (2 * j) / 3, j / 3};
        this.mSpecialAppList = new ArrayList<>(Arrays.asList("com.android.systemui", "com.test.tiledemo"));
        this.mMsgsWindowBuffer = null;
        this.mCpuLoad = 0;
        this.mMessageGetStackInterval = this.mUILooperMessageTimeout / this.mMessageGetStackCountMax;
        if (UIMONITOR_ENABLE_FOR_DEBUG && getUITimeoutEnableFromProp()) {
            z = true;
        }
        this.mUITimeoutEnable = z;
    }

    public static OplusTheiaUIMonitor getInstance() {
        return OplusTheiaUIMonitorHolder.SINSTANCE;
    }

    /* loaded from: classes.dex */
    private static class OplusTheiaUIMonitorHolder {
        private static final OplusTheiaUIMonitor SINSTANCE = new OplusTheiaUIMonitor();

        private OplusTheiaUIMonitorHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MonitorThread extends Thread {
        private long mLastGotTracedMessageStartTime;
        public volatile boolean mStarted;

        public MonitorThread() {
            super("UIMonitorThread");
            this.mStarted = false;
            this.mLastGotTracedMessageStartTime = -1L;
        }

        private boolean isUILooperGetStackTimeout(int count) {
            return OplusTheiaUIMonitor.this.isMessageProcessing() && SystemClock.uptimeMillis() - OplusTheiaUIMonitor.this.mMessageProcessStartTime >= OplusTheiaUIMonitor.this.mMessageGetStackInterval * ((long) count);
        }

        private void handleOnce() throws InterruptedException {
            int count = 1;
            while (OplusTheiaUIMonitor.this.isMessageProcessing() && count <= OplusTheiaUIMonitor.this.mMessageGetStackCountMax && isUILooperGetStackTimeout(count)) {
                OplusTheiaUIMonitor oplusTheiaUIMonitor = OplusTheiaUIMonitor.this;
                oplusTheiaUIMonitor.appendBlockTrace(oplusTheiaUIMonitor.getUIThreadMiniStackInfo(Looper.getMainLooper().getThread(), OplusTheiaUIMonitor.this.getPackageName(), Process.myPid()), count == 1);
                if (count == OplusTheiaUIMonitor.this.mMessageGetStackCountMax) {
                    this.mLastGotTracedMessageStartTime = OplusTheiaUIMonitor.this.mMessageProcessStartTime;
                    OplusTheiaUIMonitor.detailLog("handleOnce set mLastGotTracedMessageStartTime:" + this.mLastGotTracedMessageStartTime);
                    OplusTheiaUIMonitor oplusTheiaUIMonitor2 = OplusTheiaUIMonitor.this;
                    oplusTheiaUIMonitor2.sendUITimeoutEvent(oplusTheiaUIMonitor2.getPackageName(), OplusTheiaUIMonitor.this.getBlockTrace(), Process.myPid(), true, OplusTheiaUIMonitor.EVENT_BLOCK);
                }
                SystemClock.sleep(OplusTheiaUIMonitor.this.mMessageGetStackInterval);
                count++;
            }
        }

        private boolean isMessageHaveGotTrace() {
            return this.mLastGotTracedMessageStartTime == OplusTheiaUIMonitor.this.mMessageProcessStartTime;
        }

        private boolean isGotTraceTooFrequently() {
            return this.mLastGotTracedMessageStartTime != -1 && SystemClock.uptimeMillis() - this.mLastGotTracedMessageStartTime < OplusTheiaUIMonitor.this.mUILooperMessageTimeout + 60000;
        }

        private boolean needHandle() {
            return (isMessageHaveGotTrace() || isGotTraceTooFrequently()) ? false : true;
        }

        private boolean isScreenOn() {
            int state = SystemProperties.getInt("debug.tracing.screen_state", 2);
            return state == 2;
        }

        private long checkLooperUsage() {
            int freqLevel = OplusTheiaUIMonitor.this.mMsgsWindowBuffer.checkLooperUsage();
            if (freqLevel > 2) {
                if (OplusTheiaUIMonitor.this.mMsgsWindowBuffer.checkPendingMessagesState(OplusTheiaUIMonitor.this.mMsgsWindowBuffer.getCheckUsageTime())) {
                    Log.w(OplusTheiaUIMonitor.TAG, "[checkLooperUsage] the main thread may be blocked.");
                    OplusTheiaUIMonitor.this.mMsgsWindowBuffer.sendUITimeoutMsgEvent(OplusTheiaUIMonitor.this.getUIThreadMiniStackInfo(Looper.getMainLooper().getThread(), OplusTheiaUIMonitor.this.getPackageName(), Process.myPid()));
                    return 60000L;
                }
                Log.w(OplusTheiaUIMonitor.TAG, "[checkLooperUsage] the load of the main thread is high, but the pending message is not blocked.");
                return OplusTheiaUIMonitor.this.mCheckInterval[2];
            }
            return OplusTheiaUIMonitor.this.mCheckInterval[freqLevel];
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            long sleepTime;
            while (!isInterrupted() && this.mStarted) {
                try {
                    if (!isScreenOn()) {
                        sleepTime = 30000;
                        OplusTheiaUIMonitor.detailLog("MonitorThread,screen off, sleepTime:30000");
                    } else if (isGotTraceTooFrequently()) {
                        long gotTracedTime = this.mLastGotTracedMessageStartTime + OplusTheiaUIMonitor.this.mUILooperMessageTimeout;
                        sleepTime = gotTracedTime == -1 ? OplusTheiaUIMonitor.this.mMessageGetStackInterval : (60000 + gotTracedTime) - SystemClock.uptimeMillis();
                    } else if (isMessageHaveGotTrace()) {
                        sleepTime = OplusTheiaUIMonitor.this.mMessageGetStackInterval;
                    } else if (OplusTheiaUIMonitor.this.mSpecialApp) {
                        sleepTime = checkLooperUsage();
                    } else {
                        handleOnce();
                        long startTime = OplusTheiaUIMonitor.this.mMessageProcessStartTime;
                        sleepTime = Long.MAX_VALUE == startTime ? OplusTheiaUIMonitor.this.mMessageGetStackInterval : (OplusTheiaUIMonitor.this.mMessageGetStackInterval + startTime) - SystemClock.uptimeMillis();
                    }
                    if (sleepTime > 0) {
                        SystemClock.sleep(sleepTime);
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private boolean isClickMessage(Message msg) {
        Runnable callback = msg.getCallback();
        if (callback != null && "PerformClick".equals(callback.getClass().getSimpleName())) {
            return true;
        }
        return false;
    }

    private void checkStartMonitorThreadIfNeeded(boolean bForegroudApp) {
        if (bForegroudApp && this.mMonitorThread == null) {
            MonitorThread monitorThread = new MonitorThread();
            this.mMonitorThread = monitorThread;
            monitorThread.mStarted = true;
            this.mMonitorThread.start();
        }
    }

    private void checkStopMonitorThreadIfNeeded(boolean bForegroudApp) {
        MonitorThread monitorThread;
        if (!bForegroudApp && (monitorThread = this.mMonitorThread) != null) {
            monitorThread.mStarted = false;
            this.mMonitorThread.interrupt();
            this.mMonitorThread = null;
        }
    }

    private void checkStartOrStopMonitorThreadIfNeeded(boolean bForegroudApp) {
        checkStartMonitorThreadIfNeeded(bForegroudApp);
        checkStopMonitorThreadIfNeeded(bForegroudApp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMessageProcessing() {
        return this.mMessageProcessStartTime != Long.MAX_VALUE;
    }

    public void messageBegin(Message msg, boolean bForegroudApp, int level, int cpuload) {
        messageBegin(msg, bForegroudApp);
        this.mCpuLoad = cpuload;
    }

    public void messageBegin(Message msg, boolean bForegroudApp) {
        if (!this.mInitPackageInfo) {
            initPackageInfo();
        }
        if (!this.mUITimeoutEnable || !isMainLooper()) {
            return;
        }
        MsgsWindowBuffer msgsWindowBuffer = this.mMsgsWindowBuffer;
        if (msgsWindowBuffer != null) {
            msgsWindowBuffer.processMsgBegin(msg);
        }
        checkStartOrStopMonitorThreadIfNeeded(bForegroudApp || this.mSpecialApp);
        if (!bForegroudApp || isStartupMessage(msg) || this.mSpecialApp) {
            return;
        }
        this.mMessageProcessStartTime = SystemClock.uptimeMillis();
    }

    public void messageEnd(Message msg, boolean bForegroudApp, int level, int cpuload) {
        messageEnd(msg, bForegroudApp);
        this.mCpuLoad = cpuload;
    }

    public void messageEnd(Message msg, boolean bForegroudApp) {
        if (!this.mUITimeoutEnable || !isMainLooper()) {
            return;
        }
        MsgsWindowBuffer msgsWindowBuffer = this.mMsgsWindowBuffer;
        if (msgsWindowBuffer != null) {
            msgsWindowBuffer.processMsgEnd();
        } else {
            if (!isMessageProcessing()) {
                return;
            }
            if (isMessageTimeoutBlock()) {
                sendUITimeoutEvent(getPackageName(), getUIThreadInfo(msg), Process.myPid(), false, EVENT_RESUME);
            }
            this.mMessageProcessStartTime = Long.MAX_VALUE;
            checkStopMonitorThreadIfNeeded(bForegroudApp);
        }
    }

    private boolean isMainLooper() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public void queueIdleBegin() {
        MsgsWindowBuffer msgsWindowBuffer;
        if (!this.mUITimeoutEnable || !isMainLooper() || (msgsWindowBuffer = this.mMsgsWindowBuffer) == null) {
            return;
        }
        msgsWindowBuffer.processQueueIdleBegin();
    }

    public void queueIdleEnd() {
        MsgsWindowBuffer msgsWindowBuffer;
        if (!this.mUITimeoutEnable || !isMainLooper() || (msgsWindowBuffer = this.mMsgsWindowBuffer) == null) {
            return;
        }
        msgsWindowBuffer.processQueueIdleEnd();
    }

    private void initPackageInfo() {
        String packageName = ActivityThread.currentPackageName();
        if (TextUtils.isEmpty(packageName)) {
            int i = this.mRetryInitCount;
            this.mRetryInitCount = i + 1;
            if (i < 6) {
                return;
            }
        }
        if (sIgnoreAppList.contains(packageName)) {
            Log.i(TAG, "[initPackageInfo] ignore app: " + packageName);
            this.mUITimeoutEnable = false;
        }
        if (sIgnoreAppAgingTestList.contains(packageName) && "1".equals(SystemProperties.get("persist.sys.agingtest", "0"))) {
            Log.i(TAG, "[initPackageInfo] ignoreAgingTest app: " + packageName);
            this.mUITimeoutEnable = false;
        }
        if (this.mSpecialAppList.contains(packageName)) {
            this.mMsgsWindowBuffer = new MsgsWindowBuffer();
            this.mSpecialApp = true;
            detailLog("[initPackageInfo] packageName: " + packageName);
        }
        this.mInitPackageInfo = true;
    }

    private String getTimeString(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        return this.mDateFormat.format(calendar.getTime());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getUIThreadMiniStackInfo(Thread thread, String packageName, int pid) {
        long timeStamp = System.currentTimeMillis();
        StatsEvent.Builder statsEventBuilder = StatsEvent.newBuilder().setAtomId(100005).writeLong(timeStamp).writeInt(pid).writeString(packageName);
        StringBuilder infoBuffer = new StringBuilder();
        infoBuffer.append("----- pid ").append(pid).append(" at ").append(getTimeString(timeStamp)).append(" -----\n");
        infoBuffer.append("Cmd line: ").append(packageName).append("\n");
        infoBuffer.append("\"").append(thread.getName()).append("\"").append(" prio=").append(thread.getPriority()).append(" tid=").append(thread.getId()).append(" ").append(thread.getState()).append(" sysTid=").append(pid).append("\n");
        StackTraceElement[] stackArray = thread.getStackTrace();
        int nTraceLength = stackArray.length < 30 ? stackArray.length : 30;
        for (int j = 0; j < nTraceLength; j++) {
            StackTraceElement element = stackArray[j];
            infoBuffer.append("  at ").append(element.toString()).append("\n");
        }
        String info = infoBuffer.toString();
        detailLog("getUIThreadMiniStackInfo info:" + info);
        String stackInfo = info.replace("\n", ";");
        statsEventBuilder.writeString(stackInfo);
        statsEventBuilder.usePooledBuffer();
        StatsLog.write(statsEventBuilder.build());
        logP("Quality", "stackInfo :" + stackInfo);
        return info;
    }

    private String getUIThreadInfo(Message msg) {
        long uptimeCost = SystemClock.uptimeMillis() - this.mMessageProcessStartTime;
        long currentTimeStamp = System.currentTimeMillis();
        StringBuilder uiThreadBuffer = new StringBuilder();
        uiThreadBuffer.append("[UITimeout MainThread Info]\n");
        uiThreadBuffer.append("Message:").append(msg).append("\n");
        uiThreadBuffer.append("Message start time:").append(getTimeString(currentTimeStamp - uptimeCost)).append("\n");
        uiThreadBuffer.append("Message end time:").append(getTimeString(currentTimeStamp)).append("\n");
        uiThreadBuffer.append("Message cost time:").append(uptimeCost).append("ms\n");
        uiThreadBuffer.append("Cpu cost time:").append(getMessageCpuCostTime(Process.myPid())).append(" ms(between first got stack to message end)").append(", cpuload:").append(this.mCpuLoad).append("\n\n");
        uiThreadBuffer.append(getBlockTrace());
        return uiThreadBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUITimeoutEvent(String packageName, String threadInfo, int pid, boolean isFirstBlock, String eventType) {
        Intent args = new Intent();
        args.putExtra(OplusNetworkingControlManager.EXTRA_PACKAGE_NAME, packageName);
        args.putExtra(isFirstBlock ? "blockTrace" : "mainThreadInfo", threadInfo);
        args.putExtra(NsConstants.PID, pid);
        args.putExtra("uiotEvent", eventType);
        if (LOG_SWITCH_ON | PRINT_DETAIL_LOG) {
            Log.i(TAG, "sendUITimoutBlockEvent args:" + args);
        }
        try {
            OplusActivityManager.getInstance().sendTheiaEvent(TheiaConst.THEIA_ST_UTO, args);
        } catch (Exception e) {
            detailLog("sendTheiaEvent, am is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPackageName() {
        String packageName = ActivityThread.currentPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return "system";
        }
        return packageName;
    }

    private boolean isMessageTimeoutBlock() {
        return this.mMessageGetStackCount.get() >= this.mMessageGetStackCountMax && SystemClock.uptimeMillis() - this.mMessageProcessStartTime >= this.mUILooperMessageTimeout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appendBlockTrace(String strBlockTrace, boolean clearTrace) {
        if (clearTrace) {
            this.mBlockTrace.delete(0, this.mBlockTrace.length());
            this.mMessageGetStackCount.set(0);
            this.mStartUtmStmCount = getCpuJiffyForPid(Process.myPid());
        }
        this.mBlockTrace.append("Stack ").append(this.mMessageGetStackCount.incrementAndGet()).append(":\n");
        this.mBlockTrace.append(strBlockTrace).append("\n");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBlockTrace() {
        return this.mBlockTrace.toString();
    }

    private boolean isStartupMessage(Message msg) {
        if (msg.what == 110 || msg.what == 160) {
            return true;
        }
        if (msg.what != 159 || msg.obj == null || !(msg.obj instanceof ClientTransaction)) {
            return false;
        }
        ClientTransaction transaction = (ClientTransaction) msg.obj;
        ActivityLifecycleItem activityli = transaction.getLifecycleStateRequest();
        if (activityli == null) {
            return false;
        }
        int state = activityli.getTargetState();
        if (state != 1 && state != 2 && state != 3 && state != 7) {
            return false;
        }
        return true;
    }

    private Object callLogPStaticMethod(Object[] args) {
        Method method = sLogP;
        if (method != null) {
            try {
                Object result = method.invoke(null, args);
                return result;
            } catch (IllegalAccessException | InvocationTargetException e) {
                Log.e(TAG, "failed to invoke method.");
                return null;
            }
        }
        Log.e(TAG, "failed to invoke logP static method.");
        return null;
    }

    private void logP(String tag, String content) {
        callLogPStaticMethod(new Object[]{tag, content});
    }

    public long getCpuJiffyForPid(int pid) {
        String statFile = "/proc/" + pid + "/task/" + pid + "/stat";
        long[] statsData = this.mProcessStatsData;
        if (Process.readProcFile(statFile, PROCESS_STATS_FORMAT, null, statsData, null)) {
            return statsData[2] + statsData[3];
        }
        return -1L;
    }

    private long getMessageCpuCostTime(int pid) {
        long endUtmStmCount = getCpuJiffyForPid(pid);
        if (endUtmStmCount != -1 && this.mStartUtmStmCount != -1) {
            return (endUtmStmCount - this.mStartUtmStmCount) * JIFFIES_MS;
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void detailLog(String msg) {
        if (PRINT_DETAIL_LOG) {
            Log.i("OplusTheiaUIMonitorTag-Detail", msg);
        }
    }

    private boolean getUITimeoutEnableFromProp() {
        long nEventEnableMask = SystemProperties.getInt("sys.theia.event_enable_mask", 0);
        long flagUITimeout = 1 << ((int) 5);
        return (nEventEnableMask & flagUITimeout) == flagUITimeout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MsgsWindowBuffer {
        private static final int IDLE_HANDLE_CATEGORY = 1;
        private static final String MAIN_THREAD_HANDLER = "sMainThreadHandler";
        private static final int WAIT_CATEGORY = 0;
        private static final int WORK_HANDLE_CATEGORY = 2;
        private volatile long mCheckUsageTime;
        private volatile Message mCurrentMessage;
        private MsgItem mEndItem;
        private MsgItem mEpollWaitMsgItem;
        private Handler mH;
        private MsgItem mIdelHandleMsgItem;
        private Object mListLock;
        private double[] mLongPrecentLevel;
        private volatile long[] mLongTimeCount;
        private volatile long mMessageDispatchEnd;
        private volatile long mMessageDispatchStart;
        private MsgItem mMiddleItem;
        private long mMsgsRecentWindowRemainSize;
        private long mMsgsWindowLimit;
        private long mMsgsWindowRemainSize;
        private volatile long mQueueIdleEnd;
        private volatile long mQueueIdleStart;
        private double[] mRecentPrecentLevel;
        private volatile long[] mRecentTimeCount;
        private volatile long mReportBlockTime;
        private MsgItem mStartItem;
        private MsgItem mTemporaryMsgItem;

        public MsgsWindowBuffer() {
            MsgItem msgItem = new MsgItem(-1, 0L, null, 0);
            this.mStartItem = msgItem;
            this.mMiddleItem = msgItem;
            this.mEndItem = msgItem;
            this.mMsgsWindowRemainSize = OplusTheiaUIMonitor.DEFAULT_MSG_WINDOW_SIZE;
            this.mMsgsRecentWindowRemainSize = OplusTheiaUIMonitor.DEFAULT_RECENT_MSG_WINDOW_SIZE;
            this.mMsgsWindowLimit = OplusTheiaUIMonitor.DEFAULT_MSG_WINDOW_LIMIT;
            this.mCurrentMessage = null;
            this.mMessageDispatchStart = 0L;
            this.mMessageDispatchEnd = 0L;
            this.mQueueIdleStart = 0L;
            this.mQueueIdleEnd = 0L;
            this.mReportBlockTime = 0L;
            this.mCheckUsageTime = SystemClock.uptimeMillis();
            this.mRecentTimeCount = new long[3];
            this.mLongTimeCount = new long[3];
            this.mRecentPrecentLevel = new double[]{OplusTheiaUIMonitor.DEFAULT_RECENT_MSG_PRECENT_THRESHOLD / 3.0d, (OplusTheiaUIMonitor.DEFAULT_RECENT_MSG_PRECENT_THRESHOLD * 2.0d) / 3.0d, OplusTheiaUIMonitor.DEFAULT_RECENT_MSG_PRECENT_THRESHOLD};
            this.mLongPrecentLevel = new double[]{OplusTheiaUIMonitor.DEFAULT_LONG_MSG_PRECENT_THRESHOLD / 3.0d, (OplusTheiaUIMonitor.DEFAULT_LONG_MSG_PRECENT_THRESHOLD * 2.0d) / 3.0d, OplusTheiaUIMonitor.DEFAULT_LONG_MSG_PRECENT_THRESHOLD};
            this.mEpollWaitMsgItem = null;
            this.mIdelHandleMsgItem = null;
            this.mTemporaryMsgItem = null;
            this.mH = null;
            this.mListLock = new Object();
            this.mH = (Handler) getDeclaredStaticFieldValue(ActivityThread.class, MAIN_THREAD_HANDLER);
        }

        public void processMsgBegin(Message msg) {
            this.mMessageDispatchStart = SystemClock.uptimeMillis();
            this.mCurrentMessage = msg;
            long waitWall = this.mMessageDispatchStart - Math.max(this.mQueueIdleEnd, this.mMessageDispatchEnd);
            handleWaitMsg(waitWall);
        }

        public void processMsgEnd() {
            this.mMessageDispatchEnd = SystemClock.uptimeMillis();
            long wall = this.mMessageDispatchEnd - this.mMessageDispatchStart;
            if (this.mCurrentMessage.getTarget() == this.mH && (wall > OplusTheiaUIMonitor.DEFAULT_H_MSG_HANDLE_THRESHOLD || this.mCurrentMessage.what > 0)) {
                handleMainThreadHMsg(wall);
            } else if (wall > OplusTheiaUIMonitor.DEFAULT_MSG_HANDLE_THRESHOLD) {
                handleSlowDispatchMsg(wall);
            } else {
                handleTemporaryMsg(wall);
            }
            this.mCurrentMessage = null;
        }

        public void processQueueIdleBegin() {
            this.mQueueIdleStart = SystemClock.uptimeMillis();
            long waitWall = this.mQueueIdleStart - Math.max(this.mQueueIdleEnd, this.mMessageDispatchEnd);
            handleWaitMsg(waitWall);
        }

        public void processQueueIdleEnd() {
            this.mQueueIdleEnd = SystemClock.uptimeMillis();
            long wall = this.mQueueIdleEnd - this.mQueueIdleStart;
            handleQueueIdle(wall);
        }

        private void handleWaitMsg(long wall) {
            MsgItem msgItem = this.mEpollWaitMsgItem;
            if (msgItem != null) {
                msgItem.incCount(1);
                this.mEpollWaitMsgItem.incWall(wall);
            } else {
                this.mEpollWaitMsgItem = new MsgItem(0, wall, "epoll wait", 1);
            }
        }

        private void handleQueueIdle(long wall) {
            MsgItem msgItem = this.mIdelHandleMsgItem;
            if (msgItem != null) {
                msgItem.incCount(1);
                this.mIdelHandleMsgItem.incWall(wall);
            } else {
                this.mIdelHandleMsgItem = new MsgItem(1, wall, "queueIdle", 1);
            }
        }

        private void handleMainThreadHMsg(long wall) {
            handleTeamMsgInternal();
            MsgItem msgItem = new MsgItem(2, wall, toStringLite(this.mCurrentMessage, true), 1);
            processNewMsgItemLock(msgItem);
        }

        private void handleSlowDispatchMsg(long wall) {
            handleTeamMsgInternal();
            MsgItem msgItem = new MsgItem(20, wall, toStringLite(this.mCurrentMessage, true), 1);
            processNewMsgItemLock(msgItem);
        }

        private void handleTemporaryMsg(long wall) {
            MsgItem msgItem = this.mTemporaryMsgItem;
            if (msgItem != null) {
                msgItem.incCount(1);
                this.mTemporaryMsgItem.incWall(wall);
            } else {
                this.mTemporaryMsgItem = new MsgItem(10, wall, toStringLite(this.mCurrentMessage, false), 1);
            }
            if (this.mTemporaryMsgItem.getWall() > OplusTheiaUIMonitor.DEFAULT_MSG_HANDLE_THRESHOLD) {
                handleTeamMsgInternal();
            }
        }

        private void handleTeamMsgInternal() {
            MsgItem msgItem = this.mEpollWaitMsgItem;
            if (msgItem != null && msgItem.getWall() > OplusTheiaUIMonitor.DEFAULT_SMALL_MSG_THRESHOLD) {
                processNewMsgItemLock(this.mEpollWaitMsgItem);
                this.mEpollWaitMsgItem = null;
            }
            MsgItem msgItem2 = this.mIdelHandleMsgItem;
            if (msgItem2 != null && msgItem2.getWall() > OplusTheiaUIMonitor.DEFAULT_SMALL_MSG_THRESHOLD) {
                processNewMsgItemLock(this.mIdelHandleMsgItem);
                this.mIdelHandleMsgItem = null;
            }
            MsgItem msgItem3 = this.mTemporaryMsgItem;
            if (msgItem3 != null && msgItem3.getWall() > OplusTheiaUIMonitor.DEFAULT_SMALL_MSG_THRESHOLD) {
                processNewMsgItemLock(this.mTemporaryMsgItem);
                this.mTemporaryMsgItem = null;
            }
        }

        private void processNewMsgItemLock(MsgItem msgItem) {
            while (checkWindowBufferOverFlow(msgItem, this.mMsgsWindowRemainSize, this.mMsgsWindowLimit) && this.mStartItem.getNext() != null) {
                MsgItem next = this.mStartItem.getNext();
                this.mStartItem = next;
                this.mMsgsWindowRemainSize += next.getWall();
                this.mMsgsWindowLimit++;
                updateMessageStatistics(this.mStartItem.getType(), this.mStartItem.getWall(), false, false);
            }
            while (checkWindowBufferOverFlow(msgItem, this.mMsgsRecentWindowRemainSize, this.mMsgsWindowLimit) && this.mMiddleItem.getNext() != null) {
                MsgItem next2 = this.mMiddleItem.getNext();
                this.mMiddleItem = next2;
                this.mMsgsRecentWindowRemainSize += next2.getWall();
                updateMessageStatistics(this.mMiddleItem.getType(), this.mMiddleItem.getWall(), true, false);
            }
            synchronized (this.mListLock) {
                this.mEndItem.setNext(msgItem);
                this.mEndItem = msgItem;
            }
            this.mMsgsWindowRemainSize -= msgItem.getWall();
            this.mMsgsRecentWindowRemainSize -= msgItem.getWall();
            this.mMsgsWindowLimit--;
            updateMessageStatistics(this.mEndItem.getType(), this.mEndItem.getWall(), false, true);
            if (OplusTheiaUIMonitor.LOG_SWITCH_ON) {
                StringBuilder sb = new StringBuilder("[processNewMsgItemLock] ");
                sb.append("add msgItem=").append(this.mEndItem).append(", mMsgsWindowRemainSize=").append(this.mMsgsWindowRemainSize).append(", mMsgsRecentWindowRemainSize=").append(this.mMsgsRecentWindowRemainSize).append(", mMsgsWindowLimit=").append(this.mMsgsWindowLimit);
                Log.i(OplusTheiaUIMonitor.TAG, sb.toString());
            }
        }

        private void updateMessageStatistics(int type, long time, boolean isRecent, boolean isAdd) {
            int index = convertMessageTypeToStatisticsType(type);
            if (isAdd) {
                long[] jArr = this.mRecentTimeCount;
                jArr[index] = jArr[index] + time;
                long[] jArr2 = this.mLongTimeCount;
                jArr2[index] = jArr2[index] + time;
                return;
            }
            long[] tmpTimeCount = isRecent ? this.mRecentTimeCount : this.mLongTimeCount;
            tmpTimeCount[index] = tmpTimeCount[index] - time;
        }

        private int convertMessageTypeToStatisticsType(int msgType) {
            switch (msgType) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                default:
                    return 2;
            }
        }

        private boolean checkWindowBufferOverFlow(MsgItem msgItem, long windowRemainSize, long windowLimit) {
            return windowRemainSize - msgItem.getWall() < 0 || windowLimit - 1 < 0;
        }

        private List getCurrentMsgWindow() {
            ArrayList<MsgItem> buffer = new ArrayList<>();
            synchronized (this.mListLock) {
                MsgItem tmp = this.mStartItem;
                for (long windowSize = OplusTheiaUIMonitor.DEFAULT_MSG_WINDOW_SIZE - this.mMsgsWindowLimit; tmp.getNext() != null && windowSize > 0; windowSize--) {
                    buffer.add(tmp.getNext());
                    tmp = tmp.getNext();
                }
            }
            return buffer;
        }

        private Object getDeclaredStaticFieldValue(Class clazz, String fieldName) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object result = field.get(null);
                return result;
            } catch (IllegalAccessException | IllegalArgumentException e) {
                Log.w(OplusTheiaUIMonitor.TAG, "Failed to get declared static field.", e);
                return null;
            } catch (NoSuchFieldException e2) {
                Log.w(OplusTheiaUIMonitor.TAG, "Failed to find the field: " + e2.getMessage(), e2);
                return null;
            }
        }

        private List<Message> getPendingMessages(long now) {
            ArrayList<Message> pendingMessage = new ArrayList<>();
            int num = 1;
            MessageQueue queue = Looper.getMainLooper().mQueue;
            synchronized (queue) {
                try {
                    Message tmp = queue.mMessages;
                    while (tmp != null) {
                        if (OplusTheiaUIMonitor.LOG_SWITCH_ON) {
                            Log.d(OplusTheiaUIMonitor.TAG, "[id:" + num + "] msg: " + toStringLite(tmp, now, false));
                        }
                        int num2 = num + 1;
                        try {
                            if (num > OplusTheiaUIMonitor.DEFAULT_PENDING_MSG_LIMIT) {
                                break;
                            }
                            pendingMessage.add(tmp);
                            tmp = tmp.next;
                            num = num2;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    return pendingMessage;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean checkPendingMessagesState(long now) {
            List<Message> pendingMessage = getPendingMessages(now);
            for (Message tmp : pendingMessage) {
                if (now - tmp.when > OplusTheiaUIMonitor.DEFAULT_HUNG_MSG_THRESHOLD) {
                    return true;
                }
            }
            return false;
        }

        private int calculateFreqLevel(double load, double[] freqLevels) {
            for (int i = 0; i < 3; i++) {
                if (load <= freqLevels[i]) {
                    return i;
                }
            }
            return 3;
        }

        public void sendUITimeoutMsgEvent(String stackTrace) {
            String mainThreadInfo = OplusTheiaUIMonitor.this.mMsgsWindowBuffer.dumpMessageHistory(stackTrace);
            if (OplusTheiaUIMonitor.LOG_SWITCH_ON) {
                OplusTheiaUIMonitor.detailLog("[sendUITimeoutMsgEvent] Looper Message: \n" + mainThreadInfo);
            }
            OplusTheiaUIMonitor oplusTheiaUIMonitor = OplusTheiaUIMonitor.this;
            oplusTheiaUIMonitor.sendUITimeoutEvent(oplusTheiaUIMonitor.getPackageName(), mainThreadInfo, Process.myPid(), false, OplusTheiaUIMonitor.EVENT_MESSAGE);
            Log.w(OplusTheiaUIMonitor.TAG, "[sendUITimeoutMsgEvent] The application may be doing too much work on its main thread.");
        }

        public long getCheckUsageTime() {
            return this.mCheckUsageTime;
        }

        public int checkLooperUsage() {
            long recentTimeWindow;
            String str;
            this.mCheckUsageTime = SystemClock.uptimeMillis();
            if (this.mCheckUsageTime - this.mReportBlockTime < 60000) {
                Log.w(OplusTheiaUIMonitor.TAG, "[checkLooperUsage] block has happended, please wait " + ((this.mReportBlockTime + 60000) - this.mCheckUsageTime) + "ms.");
                return 0;
            }
            long blockWorkTime = 0;
            if (this.mCurrentMessage != null && this.mMessageDispatchEnd < this.mMessageDispatchStart) {
                blockWorkTime = this.mCheckUsageTime - this.mMessageDispatchStart;
            }
            long recentTimeWindow2 = (OplusTheiaUIMonitor.DEFAULT_RECENT_MSG_WINDOW_SIZE - this.mMsgsRecentWindowRemainSize) + blockWorkTime;
            long longTimeWindow = (OplusTheiaUIMonitor.DEFAULT_MSG_WINDOW_SIZE - this.mMsgsWindowRemainSize) + blockWorkTime;
            if (recentTimeWindow2 < OplusTheiaUIMonitor.DEFAULT_RECENT_MSG_WINDOW_MIN_SIZE) {
                recentTimeWindow = recentTimeWindow2;
                str = OplusTheiaUIMonitor.TAG;
            } else {
                if (longTimeWindow >= OplusTheiaUIMonitor.DEFAULT_MSG_WINDOW_MIN_SIZE) {
                    double waitRecentPrecent = this.mRecentTimeCount[0] / recentTimeWindow2;
                    double idleHandleRecentPrecent = this.mRecentTimeCount[1] / recentTimeWindow2;
                    double workHandleRecentPrecent = (this.mRecentTimeCount[2] + blockWorkTime) / recentTimeWindow2;
                    double recentLoad = idleHandleRecentPrecent + workHandleRecentPrecent;
                    int recentLoadLevel = calculateFreqLevel(recentLoad, this.mRecentPrecentLevel);
                    double waitPrecent = this.mLongTimeCount[0] / longTimeWindow;
                    double idleHandlePrecent = this.mLongTimeCount[1] / longTimeWindow;
                    double workHandlePrecent = (this.mLongTimeCount[2] + blockWorkTime) / longTimeWindow;
                    double longLoad = idleHandlePrecent + workHandlePrecent;
                    int longLoadLevel = (recentLoadLevel + calculateFreqLevel(longLoad, this.mLongPrecentLevel)) / 2;
                    if (longLoadLevel >= 2) {
                        Log.w(OplusTheiaUIMonitor.TAG, "[checkLooperUsage] Load: " + recentLoad + " / " + longLoad + ", Block wait: " + blockWorkTime + "ms , FreqLevel: " + longLoadLevel);
                    }
                    if (!OplusTheiaUIMonitor.LOG_SWITCH_ON) {
                        return longLoadLevel;
                    }
                    StringBuilder sb = new StringBuilder("----- Output from UIMonitor -----\n");
                    sb.append("Recent(").append(recentTimeWindow2).append("ms): ").append("wait=").append(waitRecentPrecent).append(" idleHandle=").append(idleHandleRecentPrecent).append(" workHandle=").append(workHandleRecentPrecent).append("\n");
                    sb.append("Long(").append(longTimeWindow).append("ms): ").append("wait=").append(waitPrecent).append(" idleHandle=").append(idleHandlePrecent).append(" workHandle=").append(workHandlePrecent).append("\n");
                    sb.append("Load: ").append(recentLoad).append(" / ").append(longLoad).append(", Block wait: ").append(blockWorkTime).append("ms, FreqLevel: ").append(longLoadLevel).append("\n");
                    sb.append("----- End output from UIMonitor -----");
                    Log.i(OplusTheiaUIMonitor.TAG, "[checkLooperUsage] " + sb.toString());
                    return longLoadLevel;
                }
                recentTimeWindow = recentTimeWindow2;
                str = OplusTheiaUIMonitor.TAG;
            }
            if (OplusTheiaUIMonitor.LOG_SWITCH_ON) {
                Log.w(str, "[checkLooperUsage] the window buffer is not fuller. recentTimeWindow:" + recentTimeWindow + " longTimeWindow:" + longTimeWindow);
                return 1;
            }
            return 1;
        }

        public String dumpMessageHistory(String stackTrace) {
            this.mReportBlockTime = SystemClock.uptimeMillis();
            int msgCount = 0;
            List<MsgItem> historyRecords = getCurrentMsgWindow();
            StringBuilder sb = new StringBuilder("\n** Main Thread Historical Message **\n");
            for (MsgItem item : historyRecords) {
                msgCount++;
                sb.append("[id:").append(msgCount).append("] ").append(item).append("\n");
            }
            sb.append("** Current Handle Message **\n");
            if (this.mCurrentMessage != null) {
                sb.append(this.mReportBlockTime - this.mMessageDispatchStart).append("ms ago: ").append(toStringLite(this.mCurrentMessage, this.mReportBlockTime, true)).append("\n");
            } else {
                sb.append(this.mReportBlockTime - this.mMessageDispatchEnd).append("ms ago: epoll_wait\n");
            }
            sb.append("stackTrace:\n");
            sb.append(stackTrace);
            sb.append("\n");
            sb.append("** Pending Message **\n");
            List<Message> pendingMessage = getPendingMessages(this.mReportBlockTime);
            for (Message tmp : pendingMessage) {
                sb.append(toStringLite(tmp, this.mReportBlockTime, false)).append("\n");
            }
            sb.append("\n** Message Dump End **\n");
            return sb.toString();
        }

        private String toStringLite(Message msg, boolean showObj) {
            return toStringLite(msg, SystemClock.uptimeMillis(), showObj);
        }

        private String toStringLite(Message msg, long now, boolean showObj) {
            if (msg == null) {
                return OplusTheiaUIMonitor.MESSAGE_NULL;
            }
            StringBuilder buf = new StringBuilder();
            buf.append("{ when=");
            TimeUtils.formatDuration(msg.when - now, buf);
            if (msg.target != null) {
                buf.append(" what=").append(msg.what);
                if (msg.arg1 != 0) {
                    buf.append(" arg1=").append(msg.arg1);
                }
                if (msg.arg2 != 0) {
                    buf.append(" arg2=").append(msg.arg2);
                }
            } else {
                buf.append(" barrier=").append(msg.arg1);
            }
            try {
                appendDetailMsgInfo(msg, showObj, buf);
            } catch (Exception e) {
                buf.append(" failed to append: " + e.getMessage());
            }
            buf.append(" }");
            return buf.toString();
        }

        private void appendDetailMsgInfo(Message msg, boolean showObj, StringBuilder buf) {
            if (msg.target != null) {
                buf.append(" target=").append(msg.target.getClass().getName());
            }
            if (msg.callback != null) {
                buf.append(" callback=").append(msg.callback.getClass().getName());
            }
            if (showObj && msg.obj != null) {
                String objStr = msg.obj.toString().length() > 128 ? "TOO LONG" : msg.obj.toString();
                buf.append(" obj=").append(objStr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MsgItem {
        public static final int TYPE_IDLE_HANDLE = 1;
        public static final int TYPE_MAIN_THREAD_H_MSG = 2;
        public static final int TYPE_NORMAL_DISPATCH_MSG = 10;
        public static final int TYPE_SLOW_DISPATCH_MSG = 20;
        public static final int TYPE_UNKOWN = -1;
        public static final int TYPE_WAIT = 0;
        private int mCount;
        private String mMsg;
        private volatile MsgItem mNext = null;
        private final int mType;
        private long mWall;

        public MsgItem(int type, long wall, String msg, int count) {
            this.mType = type;
            this.mWall = wall;
            this.mMsg = msg;
            this.mCount = count;
        }

        public int getType() {
            return this.mType;
        }

        public long getWall() {
            return this.mWall;
        }

        public void incWall(long wall) {
            this.mWall += wall;
        }

        public String getMsg() {
            return this.mMsg;
        }

        public void setMsg(String msg) {
            this.mMsg = msg;
        }

        public int getCount() {
            return this.mCount;
        }

        public void incCount(int count) {
            this.mCount += count;
        }

        public MsgItem getNext() {
            return this.mNext;
        }

        public void setNext(MsgItem msgItem) {
            this.mNext = msgItem;
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("{ type=").append(this.mType).append(", wall=").append(this.mWall).append(", count=").append(this.mCount).append(", msg=").append(this.mMsg).append(", next=").append(System.identityHashCode(this.mNext)).append(" }");
            return b.toString();
        }
    }
}
