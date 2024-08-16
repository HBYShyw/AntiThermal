package android.os;

import java.util.Objects;

/* loaded from: classes.dex */
public class OplusHistoryMsg {
    public static final int BACKWARD_MSG_HANDLER_THRESHOLD = 200;
    public static final int BACKWARD_MSG_MAX_SLOW_THRESHOLD = 200;
    public static final int BACKWARD_MSG_PENDING_COUNT = 20;
    public static final String MSG_TYPE_DISPATCH_HMAIN_SLOW = "msg_h_handler_slow";
    public static final String MSG_TYPE_DISPATCH_SELF_SLOW = "msg_slef_handler_slow";
    public static final String MSG_TYPE_DISPATCH_SLOW = "msg_dispatch_slow";
    public static final String MSG_TYPE_NORMAL = "msg_normal_ms";
    public static final String M_HANDLER_NAME = "android.app.ActivityThread$H";
    int mArg1;
    int mArg2;
    String mCallback;
    int mCount = 1;
    long mEndTime;
    private long mStartTime;
    String mTarget;
    String mType;
    long mWall;
    int mWhat;
    long mWhen;

    private OplusHistoryMsg() {
    }

    public static OplusHistoryMsg startMsg(Message msg) {
        OplusHistoryMsg oplusMsg = convertMsg(msg);
        oplusMsg.mStartTime = SystemClock.uptimeMillis();
        return oplusMsg;
    }

    public static OplusHistoryMsg convertMsg(Message msg) {
        OplusHistoryMsg oplusMsg = new OplusHistoryMsg();
        oplusMsg.mCallback = msg.callback == null ? "" : msg.callback.getClass().getName();
        oplusMsg.mTarget = msg.target != null ? msg.target.getClass().getName() : "";
        oplusMsg.mWhen = msg.when;
        oplusMsg.mArg1 = msg.arg1;
        oplusMsg.mArg2 = msg.arg2;
        oplusMsg.mWhat = msg.what;
        return oplusMsg;
    }

    public static OplusHistoryMsg endMsg(OplusHistoryMsg msg) {
        long uptimeMillis = SystemClock.uptimeMillis();
        msg.mEndTime = uptimeMillis;
        msg.mWall = uptimeMillis - msg.mStartTime;
        generateMsgType(msg);
        return msg;
    }

    private static void generateMsgType(OplusHistoryMsg msg) {
        String type;
        long wall = msg.mWall;
        String str = msg.mTarget;
        if (str != null) {
            if ("android.app.ActivityThread$H".equals(str) && wall > 200) {
                type = "msg_h_handler_slow";
            } else if (wall > 200) {
                type = "msg_dispatch_slow";
            } else {
                type = "msg_normal_ms";
            }
        } else if (wall > 200) {
            type = "msg_dispatch_slow";
        } else {
            type = "msg_normal_ms";
        }
        msg.mType = type;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OplusHistoryMsg that = (OplusHistoryMsg) o;
        if (Objects.equals(this.mType, that.mType) && Objects.equals(this.mCallback, that.mCallback) && Objects.equals(this.mTarget, that.mTarget)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mType, this.mCallback, this.mTarget);
    }

    public String toString() {
        return "OplusHistoryLooperMessage{mStartTime=" + this.mStartTime + ", mWall=" + this.mWall + ", count=" + this.mCount + ", mEndTime=" + this.mEndTime + ", type=" + this.mType + ", callback=" + this.mCallback + ", target=" + this.mTarget + ", when=" + this.mWhen + ", arg1=" + this.mArg1 + ", arg2=" + this.mArg2 + ", what=" + this.mWhat + '}';
    }

    public String toStringWithoutCount() {
        return "{ start=" + this.mStartTime + ", wall=" + this.mWall + ", callback=" + this.mCallback + ", target=" + this.mTarget + '}';
    }

    public void setType(String type) {
        this.mType = type;
    }
}
