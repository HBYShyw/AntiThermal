package com.android.internal.telephony;

/* loaded from: classes.dex */
public class OplusRecoveryAction {
    public static final int ACTION_AIREPLANE_MODE = 8;
    public static final int ACTION_CLEAN_DATA_CONNECTION = 3;
    public static final int ACTION_DETACH_ATTACH = 7;
    public static final int ACTION_DISABLE_NR = 4;
    public static final int ACTION_DISABLE_SA = 10;
    public static final int ACTION_DISABLE_VOLTE = 1;
    public static final int ACTION_ENABLE_VOLTE = 2;
    public static final int ACTION_INVALID = -1;
    public static final int ACTION_MAX = 11;
    public static final int ACTION_REBOOT_MODEM = 9;
    public static final int ACTION_REREGISTER = 5;
    public static final int ACTION_RESEARCH_NETWORK = 6;
    public static final int STATE_IDLE = 1;
    public static final int STATE_IN_RECOVERY = 2;
    public static final int STATE_RECOVERY_FAILED = 3;
    public static final int STATE_RECOVERY_SUCCESS = 4;
    private int mAction;
    private int mPriority;
    private int mSotId;
    int INVALID = -1;
    private int mState = 1;

    public OplusRecoveryAction(int slotId, int action, int priority) {
        this.mSotId = 0;
        this.mAction = -1;
        this.mPriority = -1;
        this.mSotId = slotId;
        this.mAction = action;
        this.mPriority = priority;
    }

    public int getSlotId() {
        return this.mSotId;
    }

    public int getAction() {
        return this.mAction;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public int getState() {
        return this.mState;
    }

    public String toString() {
        return "RecoveryAction is [ slotId = " + this.mSotId + ", action=" + this.mAction + ", priority=" + this.mPriority + ", state=" + this.mState + "]";
    }
}
