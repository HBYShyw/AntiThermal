package android.os;

import android.app.ActivityThread;
import android.os.OplusJankMonitor;
import android.util.Log;
import android.util.StatsEvent;
import android.util.StatsLog;
import android.util.TimeUtils;
import com.oplus.widget.OplusMaxLinearLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class LooperMessageSuperviser {
    private static final int DUMP_MESSAGE_MAX = 10;
    private static final int MAX_MONITOR_DELAY = 100;
    private static final int MAX_OBJ_STRING_LEN = 128;
    private static final int MSG_BIND_APPLICATION = 110;
    private static final int MSG_EXECUTE_TRANSACTION = 159;
    private static final int PROCESS_STATUS_BACKGROUND = 0;
    private static final int PROCESS_STATUS_FOREGROUND = 1;
    private static final int PROCESS_STATUS_UNKNOW = -1;
    private static final String SCREEN_ON = "debug.tracing.screen_state";
    private static final String TAG = "LooperMessageSuperviser";
    private CommandHandler mCommonHandler;
    private int mForegroundFlag = -1;
    private HandlerThread mHandlerThread;
    private boolean mHasJankInfo;
    private boolean mInForegroundRunning;
    private final int mPid;
    private final String mProcNode;
    private StatsEvent.Builder mStatsEventBuilder;
    private static final boolean DEBUG = !SystemProperties.getBoolean("ro.build.release_type", false);
    private static final boolean MONITOR_THREAD_ENABLED = SystemProperties.getBoolean("sys.oplus.healthinfo.loopThread", true);

    public LooperMessageSuperviser() {
        int myPid = Process.myPid();
        this.mPid = myPid;
        this.mProcNode = "/proc/" + myPid + "/jank_info";
        this.mHandlerThread = null;
        this.mCommonHandler = null;
        this.mHasJankInfo = true;
        this.mInForegroundRunning = false;
        this.mStatsEventBuilder = null;
    }

    public void beginLooperMessage(OplusLooperEntry entry, Message msg, int loopPid, int level, int cpuload) {
        if (OplusDebug.DEBUG_SYSTRACE_TAG) {
            Trace.traceBegin(8L, msg.target.getTraceName(msg));
        }
        boolean isForegroundApp = isForegroundApp(this.mPid);
        this.mInForegroundRunning = isForegroundApp;
        if (isForegroundApp && this.mCommonHandler == null) {
            launchMonitorThread();
        }
        OplusTheiaUIMonitor.getInstance().messageBegin(msg, this.mInForegroundRunning, level, cpuload);
        OplusLooperMsgDispatcher.getInstance().startRegisterCurrentMsg(entry, this.mInForegroundRunning, level, cpuload);
    }

    public void endLooperMessage(OplusLooperEntry entry, Message msg, long loopStartTime, int loopPid, int level, int cpuload) {
        boolean isForegroundApp = isForegroundApp(this.mPid);
        this.mInForegroundRunning = isForegroundApp;
        if (isForegroundApp && this.mCommonHandler == null) {
            launchMonitorThread();
        }
        if (this.mInForegroundRunning && monitorReady()) {
            long nowTime = SystemClock.uptimeMillis();
            long processTime = nowTime - loopStartTime;
            int i = 1;
            boolean delayed = processTime >= ((long) OplusDebug.LOOPER_DELAY);
            Message transferMsg = Message.obtain();
            if (delayed && this.mInForegroundRunning) {
                i = 2;
            }
            transferMsg.what = i;
            if (processTime >= OplusDebug.LAUNCH_DELAY && isLaunchFocusMessage(msg)) {
                OplusJankMonitor.LaunchTracker.getInstance().setLaunchStageTime(getPackageName(), processTime, msg.getTarget().getClass().getName(), msg.what);
            }
            if (delayed) {
                StringBuilder sb = uploadLongTimeMessage(processTime, msg, loopStartTime, this.mPid);
                Bundle bundle = new Bundle();
                bundle.putObject("blockedMsg", sb);
                transferMsg.data = bundle;
                if (DEBUG) {
                    debugI("Blocked2.0 msg " + processTime);
                }
                this.mCommonHandler.sendMessage(transferMsg);
            }
        }
        if (OplusDebug.DEBUG_SYSTRACE_TAG) {
            Trace.traceEnd(8L);
        }
        OplusTheiaUIMonitor.getInstance().messageEnd(msg, this.mInForegroundRunning, level, cpuload);
        OplusLooperMsgDispatcher.getInstance().processRegisterFinishMsg(entry, this.mInForegroundRunning, level, cpuload);
    }

    public void dumpMessage(MessageQueue messageQueue) {
        synchronized (messageQueue) {
            Message tempMsg = messageQueue.mMessages;
            if (tempMsg != null) {
                long time = SystemClock.uptimeMillis();
                Log.e(TAG, "Dump messages in Queue: ");
                int count = 0;
                while (tempMsg != null) {
                    count++;
                    if (count > 10) {
                        break;
                    }
                    Log.e(TAG, "Current msg <" + count + ">  = " + toStringLite(tempMsg, this.mStatsEventBuilder, tempMsg.when - time, false));
                    tempMsg = tempMsg.next;
                }
            } else {
                Log.d(TAG, "mMessages is null");
            }
        }
    }

    private void launchMonitorThread() {
        if (MONITOR_THREAD_ENABLED) {
            debugI("MAIN LOOP MONITOR THREAD STARTED");
            this.mHandlerThread = OplusAppBackgroundThread.get();
            this.mCommonHandler = new CommandHandler(this.mHandlerThread.getLooper());
        }
    }

    private boolean monitorReady() {
        HandlerThread handlerThread;
        return (this.mCommonHandler == null || (handlerThread = this.mHandlerThread) == null || !handlerThread.isAlive()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class CommandHandler extends Handler {
        static final int MSG_LOOP_DELAYED_END = 2;
        static final int MSG_LOOP_NORMAL_END = 1;

        CommandHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            long monitorDelayed = SystemClock.uptimeMillis() - msg.when;
            boolean monitorThreadDelay = monitorDelayed > 100;
            switch (msg.what) {
                case 1:
                    LooperMessageSuperviser.this.innerEndLooperMessage(null, false);
                    break;
                case 2:
                    Object objMsg = msg.getData().get("blockedMsg");
                    if (objMsg != null && (objMsg instanceof StringBuilder)) {
                        StringBuilder blockedMsg = (StringBuilder) objMsg;
                        LooperMessageSuperviser.this.innerEndLooperMessage(blockedMsg, true);
                        break;
                    } else {
                        LooperMessageSuperviser.this.innerEndLooperMessage(null, false);
                        break;
                    }
                default:
                    LooperMessageSuperviser.debugI("INVALID COMMAND MSG: " + msg.what);
                    break;
            }
            if (monitorThreadDelay) {
                if (LooperMessageSuperviser.DEBUG) {
                    LooperMessageSuperviser.debugI("loop monitor delayed: " + monitorDelayed + "ms " + LooperMessageSuperviser.this.getPackageName());
                }
                clearDelayedMonitorMessage();
            }
        }

        private void clearDelayedMonitorMessage() {
            removeMessages(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPackageName() {
        String packageName = ActivityThread.currentProcessName();
        if (packageName == null) {
            return "system_server";
        }
        return packageName;
    }

    private boolean isLaunchFocusMessage(Message msg) {
        return isFrameDisplayMessage(msg) || isActivityLifecycleMessage(msg) || isApplicationBindMessage(msg);
    }

    private boolean isActivityLifecycleMessage(Message msg) {
        if (!isMessageOk(msg)) {
            return false;
        }
        String activityLifecycleMessageClass = msg.getTarget().getClass().getName();
        int what = msg.what;
        if (what != 159 || !activityLifecycleMessageClass.contains("android.app.ActivityThread$H")) {
            return false;
        }
        return true;
    }

    private boolean isApplicationBindMessage(Message msg) {
        if (!isMessageOk(msg)) {
            return false;
        }
        String applicationBindMessageClass = msg.getTarget().getClass().getName();
        int what = msg.what;
        if (what != 110 || !applicationBindMessageClass.contains("android.app.ActivityThread$H")) {
            return false;
        }
        return true;
    }

    private boolean isFrameDisplayMessage(Message msg) {
        if (!isMessageOk(msg)) {
            return false;
        }
        String frameDisplayMessageTargetClass = msg.getTarget().getClass().getName();
        if (!frameDisplayMessageTargetClass.contains("android.view.Choreographer$FrameHandler")) {
            return false;
        }
        return true;
    }

    private boolean isMessageOk(Message msg) {
        return (msg == null || msg.getTarget() == null || msg.getTarget().getClass() == null) ? false : true;
    }

    private String getProcessName() {
        String processName = ActivityThread.currentProcessName();
        if (processName == null) {
            return "system_server";
        }
        return processName;
    }

    private boolean isForegroundApp(int pid) {
        return this.mForegroundFlag == 1 && isScreenOn() && Process.myPid() == Process.myTid();
    }

    boolean isScreenOn() {
        int state = SystemProperties.getInt(SCREEN_ON, 2);
        return state == 2;
    }

    private int getSchedGroup(int pid) {
        try {
            return Process.getProcessGroup(pid);
        } catch (Exception e) {
            return OplusMaxLinearLayout.INVALID_MAX_VALUE;
        }
    }

    private int getThreadSchedulePolicy(int pid) {
        try {
            return Process.getThreadScheduler(pid);
        } catch (Exception e) {
            return 0;
        }
    }

    private StringBuilder uploadLongTimeMessage(long processTime, Message message, long timeFirst, int pid) {
        String processName = getProcessName();
        this.mStatsEventBuilder = StatsEvent.newBuilder().setAtomId(100004).writeLong(System.currentTimeMillis()).writeInt(this.mPid).writeString(processName);
        StringBuilder sb = new StringBuilder();
        sb.append("Package name: ");
        sb.append(processName);
        sb.append(" [ ");
        sb.append("schedGroup: ");
        sb.append(getSchedGroup(pid));
        sb.append(" schedPolicy: ");
        sb.append(getThreadSchedulePolicy(pid));
        sb.append(" ]");
        sb.append(" process the message: ");
        sb.append(toStringLite(message, this.mStatsEventBuilder, processTime + timeFirst, true));
        sb.append(" took ");
        sb.append(processTime);
        this.mStatsEventBuilder.writeLong(processTime);
        sb.append(" ms");
        this.mStatsEventBuilder.writeString(sb.toString());
        this.mStatsEventBuilder.usePooledBuffer();
        return sb;
    }

    public void setForebackStatus() {
        this.mForegroundFlag = ActivityThread.currentActivityThread().mOplusActivityThreadExt.isTopApp() ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void innerEndLooperMessage(StringBuilder blockedMsg, boolean isDelayed) {
        if (isDelayed && blockedMsg != null) {
            StatsEvent.Builder builder = this.mStatsEventBuilder;
            if (builder != null) {
                StatsLog.write(builder.build());
            }
            blockedMsg.append("\n");
            logP("Quality", "Blocked msg = " + blockedMsg.toString());
        }
    }

    private String toStringLite(Message msg, StatsEvent.Builder statsEventBuilder, long now, boolean showObj) {
        if (msg == null && statsEventBuilder != null) {
            statsEventBuilder.writeInt(0);
            statsEventBuilder.writeString("NULL_MESSAGE");
            return "NULL_MESSAGE";
        }
        StringBuilder b = new StringBuilder();
        b.append("{ when=");
        TimeUtils.formatDuration(msg.when - now, b);
        if (msg.target != null) {
            b.append(" what=");
            b.append(msg.what);
            if (statsEventBuilder != null) {
                statsEventBuilder.writeInt(msg.what);
            }
            b.append(" target=");
            b.append(msg.target.getClass().getName());
            if (statsEventBuilder != null) {
                statsEventBuilder.writeString(msg.target.getClass().getName());
            }
            if (msg.callback != null) {
                b.append(" callback=");
                b.append(msg.callback.getClass().getName());
            }
            if (msg.arg1 != 0) {
                b.append(" arg1=");
                b.append(msg.arg1);
            }
            if (msg.arg2 != 0) {
                b.append(" arg2=");
                b.append(msg.arg2);
            }
            if (showObj && msg.obj != null) {
                String objStr = msg.obj.toString().length() > 128 ? "TOO_LONG" : msg.obj.toString();
                b.append(" obj=");
                b.append(objStr);
            }
        } else {
            b.append(" barrier=");
            b.append(msg.arg1);
            if (statsEventBuilder != null) {
                statsEventBuilder.writeInt(msg.arg1);
                statsEventBuilder.writeString("NULL_TARGET");
            }
            if (msg.callback != null) {
                b.append(" callback=");
                b.append(msg.callback);
            }
            if (showObj && msg.obj != null) {
                String objStr2 = msg.obj.toString().length() > 128 ? "TOO LONG" : msg.obj.toString();
                b.append(" obj=");
                b.append(objStr2);
            }
        }
        b.append(" }");
        return b.toString();
    }

    private void logP(String tag, String content) {
        callDeclaredMethod(null, "android.util.Log", "p", new Class[]{String.class, String.class}, new Object[]{tag, content});
    }

    private Object callDeclaredMethod(Object target, String cls_name, String method_name, Class[] parameterTypes, Object[] args) {
        try {
            Class cls = Class.forName(cls_name);
            Method method = cls.getDeclaredMethod(method_name, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            return result;
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "ClassNotFoundException : " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            Log.i(TAG, "NoSuchMethodException : " + e3.getMessage());
            e3.printStackTrace();
            return null;
        } catch (SecurityException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    public static void debugI(String logContent) {
        if (DEBUG) {
            Log.i(TAG, logContent);
        }
    }
}
