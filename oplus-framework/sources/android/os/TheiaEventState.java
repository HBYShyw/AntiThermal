package android.os;

/* loaded from: classes.dex */
public class TheiaEventState {
    public static final long CHECK_LOCK_SCREEN = 1;
    public static final long CHECK_SCREEN_OFF = 2;
    public static final long CHECK_SHUTTING_DOWN = 4;
    private EventState mEventState;
    private final String mId;
    private final String mName;
    private final String mType;

    public TheiaEventState(String id, String name, String type, EventState state) {
        this.mId = id;
        this.mName = name;
        this.mType = type;
        this.mEventState = state;
    }

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public String getType() {
        return this.mType;
    }

    public boolean isEnable() {
        return this.mEventState.enable;
    }

    public long getReportFreq() {
        return this.mEventState.reportFreq;
    }

    public long getLogType() {
        return this.mEventState.logType;
    }

    public long getDelay() {
        return this.mEventState.delay;
    }

    public long getCondition() {
        return this.mEventState.condition;
    }

    public long getRecoveryId() {
        return this.mEventState.recoveryId;
    }

    public String getExtraInfo() {
        return this.mEventState.extraInfo;
    }

    public void updateEnableSate(boolean enable) {
        this.mEventState.enable = enable;
    }

    public void updateReportFreqSate(long reportFreq) {
        this.mEventState.reportFreq = reportFreq;
    }

    public void updateLogTypeSate(long logType) {
        this.mEventState.logType = logType;
    }

    public void updateDelaySate(long delay) {
        this.mEventState.delay = delay;
    }

    public void updateConditionSate(long condition) {
        this.mEventState.condition = condition;
    }

    public void updateRecoveryIdSate(long recoveryId) {
        this.mEventState.recoveryId = recoveryId;
    }

    public void updateExtraInfoSate(String extraInfo) {
        this.mEventState.extraInfo = extraInfo;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder("TheiaEvent:[");
        buffer.append("id:").append(this.mId).append(" name:").append(this.mName).append(" type:").append(this.mType).append(" enable:").append(this.mEventState.enable).append(" reportFreq:").append(this.mEventState.reportFreq).append(" logType:").append(this.mEventState.logType).append(" delay:").append(this.mEventState.delay).append(" condition:").append(this.mEventState.condition).append(" recoveryId:").append(this.mEventState.recoveryId).append(" extraInfo:").append(this.mEventState.extraInfo).append("]");
        return buffer.toString();
    }

    /* loaded from: classes.dex */
    public static class EventState {
        public long condition;
        public long delay;
        public boolean enable;
        public String extraInfo;
        public long logType;
        public long recoveryId;
        public long reportFreq;

        public EventState setEnable(boolean enable) {
            this.enable = enable;
            return this;
        }

        public EventState setReportFreq(long reportFreq) {
            this.reportFreq = reportFreq;
            return this;
        }

        public EventState setLogType(long logType) {
            this.logType = logType;
            return this;
        }

        public EventState setDelay(long delay) {
            this.delay = delay;
            return this;
        }

        public EventState setCondition(long condition) {
            this.condition = condition;
            return this;
        }

        public EventState setRecoveryId(long recoveryId) {
            this.recoveryId = recoveryId;
            return this;
        }

        public EventState setExtraInfo(String extraInfo) {
            this.extraInfo = extraInfo;
            return this;
        }

        public TheiaEventState build(String id, String name, String type) {
            return new TheiaEventState(id, name, type, this);
        }
    }
}
