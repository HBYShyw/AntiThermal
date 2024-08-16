package android.os;

import android.util.Log;
import android.util.TimeUtils;

/* loaded from: classes.dex */
class LooperMsgTimeTracker {
    private static final long DISPATCH_TIMEOUT = 1500;
    private static final int DUMP_MESSAGE_MAX = 10;
    private static final String NULL_MESSAGE = "NULL_MESSAGE";
    private static final String TAG = "ANR_LOG";
    private Message mCurrentMsg;
    private long mStartTime;

    LooperMsgTimeTracker() {
    }

    public void start(Message msg) {
        this.mStartTime = SystemClock.uptimeMillis();
        this.mCurrentMsg = msg;
    }

    public void stop() {
        long cost = SystemClock.uptimeMillis() - this.mStartTime;
        if (cost >= DISPATCH_TIMEOUT) {
            dumpMsgListWhenAnr(cost);
        }
    }

    @Deprecated
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
                    Log.e(TAG, "Current msg <" + count + ">  = " + toStringLite(tempMsg, tempMsg.when - time, false));
                    tempMsg = tempMsg.next;
                }
            } else {
                Log.d(TAG, "mMessages is null");
            }
        }
    }

    private void dumpMsgListWhenAnr(long cost) {
        String msgStrLite = toStringLite(this.mCurrentMsg, cost, true);
        String temp = "Blocked msg = " + msgStrLite + " , cost  = " + cost + " ms";
        Log.e(TAG, temp);
    }

    private String toStringLite(Message msg, long duration, boolean showObj) {
        if (msg == null) {
            return NULL_MESSAGE;
        }
        StringBuilder b = new StringBuilder();
        b.append("{ when=");
        if (msg.when == 0) {
            TimeUtils.formatDuration(0L, b);
        } else {
            TimeUtils.formatDuration(duration, b);
        }
        if (msg.target != null) {
            b.append(" what=");
            b.append(msg.what);
            b.append(" target=");
            b.append(msg.target.getClass().getName());
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
                b.append(" obj=");
                b.append(msg.obj);
            }
        } else {
            b.append(" barrier=");
            b.append(msg.arg1);
            if (msg.callback != null) {
                b.append(" callback=");
                b.append(msg.callback);
            }
            if (showObj && msg.obj != null) {
                b.append(" obj=");
                b.append(msg.obj);
            }
        }
        b.append(" }");
        return b.toString();
    }
}
