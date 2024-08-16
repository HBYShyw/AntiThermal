package android.os;

import java.util.Objects;

/* loaded from: classes.dex */
public class OplusLooperEntry {
    public static final int BACKWARD_MSG_HANDLER_THRESHOLD = 200;
    public static final int BACKWARD_MSG_MAX_SLOW_THRESHOLD = 200;
    public static final String MSG_TYPE_DISPATCH_HMAIN_SLOW = "msg_h_handler_slow";
    public static final String MSG_TYPE_DISPATCH_SELF_SLOW = "msg_slef_handler_slow";
    public static final String MSG_TYPE_DISPATCH_SLOW = "msg_dispatch_slow";
    public static final String MSG_TYPE_NORMAL = "msg_normal_ms";
    public static final String M_HANDLER_NAME = "android.app.ActivityThread$H";
    public String callback;
    public int mArg1;
    public int mArg2;
    public int mCount;
    public long mCpuStartMicro;
    public String mHistoryType;
    public long mStartTime;
    public long mWalltime;
    public int mWhat;
    public long mWhen;
    public String messageName;
    public Object obj;
    public String target;

    public void upDataLooperEntry(OplusLooperEntry msg) {
        this.callback = msg.callback;
        this.target = msg.target;
        this.mWhen = msg.mWhen;
        this.mArg1 = msg.mArg1;
        this.mArg2 = msg.mArg2;
        this.mWhat = msg.mWhat;
        this.mStartTime = msg.mStartTime;
        this.mCpuStartMicro = msg.mCpuStartMicro;
        this.messageName = msg.messageName;
    }

    public void upDataLooperEntry(Message msg) {
        this.callback = msg.callback == null ? "" : msg.callback.getClass().getSimpleName();
        this.target = msg.target == null ? "" : msg.target.getClass().getName();
        this.mWhen = msg.when;
        this.mArg1 = msg.arg1;
        this.mArg2 = msg.arg2;
        this.mWhat = msg.what;
        this.mStartTime = SystemClock.elapsedRealtime();
        this.mCpuStartMicro = SystemClock.currentThreadTimeMicro();
        this.messageName = msg.target != null ? msg.target.getClass().getName() : "";
    }

    public void updateLoopTime() {
        this.mWalltime = SystemClock.elapsedRealtime() - this.mStartTime;
        generateHistoryMsgType();
    }

    private void generateHistoryMsgType() {
        String type;
        String str = this.target;
        if (str != null) {
            if ("android.app.ActivityThread$H".equals(str) && this.mWalltime > 200) {
                type = "msg_h_handler_slow";
            } else if (this.mWalltime > 200) {
                type = "msg_dispatch_slow";
            } else {
                type = "msg_normal_ms";
            }
        } else if (this.mWalltime > 200) {
            type = "msg_dispatch_slow";
        } else {
            type = "msg_normal_ms";
        }
        this.mHistoryType = type;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OplusLooperEntry that = (OplusLooperEntry) o;
        if (Objects.equals(this.mHistoryType, that.mHistoryType) && Objects.equals(this.callback, that.callback) && Objects.equals(this.target, that.target)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mHistoryType, this.callback, this.target);
    }

    public String getTarget() {
        return this.target;
    }

    public long getWhen() {
        return this.mWhen;
    }

    public String toString() {
        return "Entry{target='" + this.target + "', callback='" + this.callback + "', messageName='" + this.messageName + "', wall=" + this.mWalltime + "', mWhen=" + this.mWhen + ", mArg1=" + this.mArg1 + ", mArg2=" + this.mArg2 + ", mWhat=" + this.mWhat + ", mCount=" + this.mCount;
    }

    public String toStringWithoutCount() {
        return "{ start=" + this.mStartTime + ", wall=" + this.mWalltime + "'target='" + this.target + "', callback='" + this.callback;
    }
}
