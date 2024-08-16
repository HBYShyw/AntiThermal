package com.oplus.wrapper.content;

/* loaded from: classes.dex */
public class Intent {
    public static final int FLAG_RECEIVER_INCLUDE_BACKGROUND = getFlagReceiverIncludeBackground();
    public static final String ACTION_CALL_PRIVILEGED = getActionCallPrivileged();
    public static final String EXTRA_USER_ID = getExtraUserId();
    public static final String EXTRA_SIM_STATE = getExtraSimState();
    public static final String SIM_STATE_LOADED = getSimStateLoaded();
    public static final String SIM_STATE_ABSENT = getSimStateAbsent();

    private static int getFlagReceiverIncludeBackground() {
        return 16777216;
    }

    private static String getActionCallPrivileged() {
        return "android.intent.action.CALL_PRIVILEGED";
    }

    private static String getExtraUserId() {
        return "android.intent.extra.USER_ID";
    }

    private static String getExtraSimState() {
        return "ss";
    }

    private static String getSimStateLoaded() {
        return "LOADED";
    }

    private static String getSimStateAbsent() {
        return "ABSENT";
    }
}
