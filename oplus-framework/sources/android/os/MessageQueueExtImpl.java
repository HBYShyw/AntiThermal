package android.os;

import android.util.Log;
import android.util.Slog;

/* loaded from: classes.dex */
public class MessageQueueExtImpl implements IMessageQueueExt {
    private static final long BARRIER_LOG_INTERVAL = 5000;
    private static final int MAX_BLOCK_COUNT = 10;
    private static final int MAX_DUMP_INDEX = 9;
    private static final int SIGNAL_ABORT = 6;
    private static final String TAG = "MessageQueueExtImpl";
    private long mLastBarrierLogTime = 0;
    private MessageQueue mMessageQueue;
    private static final boolean DEBUG_AGINGVERSION = "1".equals(SystemProperties.get("persist.sys.agingtest", "0"));
    private static final boolean IS_SYSTEM_SERVER = "system_server".equals(getCmdlineByPid(Process.myPid()));
    private static final boolean DEBUG_BARRIER = "1".equals(SystemProperties.get("persist.sys.stabilty.dynamic.barrierdebug", "0"));

    public MessageQueueExtImpl(Object base) {
        this.mMessageQueue = (MessageQueue) base;
    }

    public void queueIdleBegin() {
        OplusTheiaUIMonitor.getInstance().queueIdleBegin();
    }

    public void queueIdleEnd() {
        OplusTheiaUIMonitor.getInstance().queueIdleEnd();
    }

    public void checkBarrierPostThread(Message barrier, int token) {
        Looper mainLooper;
        Looper mainLooper2;
        if (DEBUG_AGINGVERSION && (mainLooper2 = Looper.getMainLooper()) != null && mainLooper2.getQueue() == this.mMessageQueue && Process.myTid() != Process.myPid()) {
            RuntimeException here = new RuntimeException("here");
            here.fillInStackTrace();
            Slog.w(TAG, "postSynBarrier to main thread's messagequeue through non-main thread, BARRIER: " + token, here);
            if (IS_SYSTEM_SERVER) {
                Slog.e(TAG, "about to crash " + Process.myPid() + " due to postSynBarrier to main thread's messagequeue through non-main thread");
                Process.sendSignal(Process.myTid(), 6);
            }
        }
        if (DEBUG_BARRIER && (mainLooper = Looper.getMainLooper()) != null && mainLooper.getQueue() == this.mMessageQueue) {
            RuntimeException here2 = new RuntimeException("here");
            here2.fillInStackTrace();
            barrier.obj = Log.getStackTraceString(here2) + "post by Tid: " + Process.myTid();
        }
    }

    public void briefDumpForBarrier(Message headMsg) {
        synchronized (this.mMessageQueue) {
            int n = 0;
            long now = SystemClock.uptimeMillis();
            for (Message msg = headMsg; msg != null; msg = msg.next) {
                Slog.i(TAG, "Message " + n + ": " + msg.toString(now));
                if (msg.target == null && msg.obj != null) {
                    Slog.i(TAG, "Message " + n + ": " + msg.obj);
                }
                n++;
                if (n > 9) {
                    break;
                }
            }
        }
    }

    public void checkBlockedBarrier(Message headMsg, int msgCount) {
        if (DEBUG_BARRIER && headMsg != null && headMsg.target == null && headMsg.obj != null) {
            long now = SystemClock.uptimeMillis();
            if (msgCount > 10 && now - this.mLastBarrierLogTime > BARRIER_LOG_INTERVAL) {
                this.mLastBarrierLogTime = now;
                briefDumpForBarrier(headMsg);
            }
        }
    }

    private static String getCmdlineByPid(int pid) {
        String[] cmdline = new String[1];
        if (!Process.readProcFile("/proc/" + pid + "/cmdline", new int[]{4096}, cmdline, null, null)) {
            return "";
        }
        return cmdline[0];
    }
}
