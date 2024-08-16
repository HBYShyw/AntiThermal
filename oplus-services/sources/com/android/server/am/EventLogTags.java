package com.android.server.am;

import android.util.EventLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class EventLogTags {
    public static final int AM_ANR = 30008;
    public static final int AM_BROADCAST_DISCARD_APP = 30025;
    public static final int AM_BROADCAST_DISCARD_FILTER = 30024;
    public static final int AM_COMPACT = 30063;
    public static final int AM_CRASH = 30039;
    public static final int AM_CREATE_SERVICE = 30030;
    public static final int AM_DESTROY_SERVICE = 30031;
    public static final int AM_DROP_PROCESS = 30033;
    public static final int AM_FOREGROUND_SERVICE_DENIED = 30101;
    public static final int AM_FOREGROUND_SERVICE_START = 30100;
    public static final int AM_FOREGROUND_SERVICE_STOP = 30102;
    public static final int AM_FOREGROUND_SERVICE_TIMED_OUT = 30103;
    public static final int AM_FREEZE = 30068;
    public static final int AM_INTENT_SENDER_REDIRECT_USER = 30110;
    public static final int AM_KILL = 30023;
    public static final int AM_LOW_MEMORY = 30017;
    public static final int AM_MEMINFO = 30046;
    public static final int AM_MEM_FACTOR = 30050;
    public static final int AM_PRE_BOOT = 30045;
    public static final int AM_PROCESS_CRASHED_TOO_MUCH = 30032;
    public static final int AM_PROCESS_START_TIMEOUT = 30037;
    public static final int AM_PROC_BAD = 30015;
    public static final int AM_PROC_BOUND = 30010;
    public static final int AM_PROC_DIED = 30011;
    public static final int AM_PROC_GOOD = 30016;
    public static final int AM_PROC_START = 30014;
    public static final int AM_PROVIDER_LOST_PROCESS = 30036;
    public static final int AM_PSS = 30047;
    public static final int AM_SCHEDULE_SERVICE_RESTART = 30035;
    public static final int AM_SERVICE_CRASHED_TOO_MUCH = 30034;
    public static final int AM_STOP_IDLE_SERVICE = 30056;
    public static final int AM_SWITCH_USER = 30041;
    public static final int AM_UID_ACTIVE = 30054;
    public static final int AM_UID_IDLE = 30055;
    public static final int AM_UID_RUNNING = 30052;
    public static final int AM_UID_STOPPED = 30053;
    public static final int AM_UNFREEZE = 30069;
    public static final int AM_USER_STATE_CHANGED = 30051;
    public static final int AM_WTF = 30040;
    public static final int BOOT_PROGRESS_AMS_READY = 3040;
    public static final int BOOT_PROGRESS_ENABLE_SCREEN = 3050;
    public static final int CONFIGURATION_CHANGED = 2719;
    public static final int CPU = 2721;
    public static final int SSM_USER_COMPLETED_EVENT = 30088;
    public static final int SSM_USER_STARTING = 30082;
    public static final int SSM_USER_STOPPED = 30087;
    public static final int SSM_USER_STOPPING = 30086;
    public static final int SSM_USER_SWITCHING = 30083;
    public static final int SSM_USER_UNLOCKED = 30085;
    public static final int SSM_USER_UNLOCKING = 30084;
    public static final int UC_CONTINUE_USER_SWITCH = 30080;
    public static final int UC_DISPATCH_USER_SWITCH = 30079;
    public static final int UC_FINISH_USER_BOOT = 30078;
    public static final int UC_FINISH_USER_STOPPED = 30074;
    public static final int UC_FINISH_USER_STOPPING = 30073;
    public static final int UC_FINISH_USER_UNLOCKED = 30071;
    public static final int UC_FINISH_USER_UNLOCKED_COMPLETED = 30072;
    public static final int UC_FINISH_USER_UNLOCKING = 30070;
    public static final int UC_SEND_USER_BROADCAST = 30081;
    public static final int UC_START_USER_INTERNAL = 30076;
    public static final int UC_SWITCH_USER = 30075;
    public static final int UC_UNLOCK_USER = 30077;
    public static final int UM_USER_VISIBILITY_CHANGED = 30091;

    private EventLogTags() {
    }

    public static void writeConfigurationChanged(int i) {
        EventLog.writeEvent(CONFIGURATION_CHANGED, i);
    }

    public static void writeCpu(int i, int i2, int i3, int i4, int i5, int i6) {
        EventLog.writeEvent(CPU, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6));
    }

    public static void writeBootProgressAmsReady(long j) {
        EventLog.writeEvent(BOOT_PROGRESS_AMS_READY, j);
    }

    public static void writeBootProgressEnableScreen(long j) {
        EventLog.writeEvent(BOOT_PROGRESS_ENABLE_SCREEN, j);
    }

    public static void writeAmAnr(int i, int i2, String str, int i3, String str2) {
        EventLog.writeEvent(AM_ANR, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), str2);
    }

    public static void writeAmProcBound(int i, int i2, String str) {
        EventLog.writeEvent(AM_PROC_BOUND, Integer.valueOf(i), Integer.valueOf(i2), str);
    }

    public static void writeAmProcDied(int i, int i2, String str, int i3, int i4) {
        EventLog.writeEvent(AM_PROC_DIED, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public static void writeAmProcStart(int i, int i2, int i3, String str, String str2, String str3) {
        EventLog.writeEvent(AM_PROC_START, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), str, str2, str3);
    }

    public static void writeAmProcBad(int i, int i2, String str) {
        EventLog.writeEvent(AM_PROC_BAD, Integer.valueOf(i), Integer.valueOf(i2), str);
    }

    public static void writeAmProcGood(int i, int i2, String str) {
        EventLog.writeEvent(AM_PROC_GOOD, Integer.valueOf(i), Integer.valueOf(i2), str);
    }

    public static void writeAmLowMemory(int i) {
        EventLog.writeEvent(AM_LOW_MEMORY, i);
    }

    public static void writeAmKill(int i, int i2, String str, int i3, String str2) {
        EventLog.writeEvent(AM_KILL, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), str2);
    }

    public static void writeAmBroadcastDiscardFilter(int i, int i2, String str, int i3, int i4) {
        EventLog.writeEvent(AM_BROADCAST_DISCARD_FILTER, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public static void writeAmBroadcastDiscardApp(int i, int i2, String str, int i3, String str2) {
        EventLog.writeEvent(AM_BROADCAST_DISCARD_APP, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), str2);
    }

    public static void writeAmCreateService(int i, int i2, String str, int i3, int i4) {
        EventLog.writeEvent(AM_CREATE_SERVICE, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public static void writeAmDestroyService(int i, int i2, int i3) {
        EventLog.writeEvent(AM_DESTROY_SERVICE, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
    }

    public static void writeAmProcessCrashedTooMuch(int i, String str, int i2) {
        EventLog.writeEvent(AM_PROCESS_CRASHED_TOO_MUCH, Integer.valueOf(i), str, Integer.valueOf(i2));
    }

    public static void writeAmDropProcess(int i) {
        EventLog.writeEvent(AM_DROP_PROCESS, i);
    }

    public static void writeAmServiceCrashedTooMuch(int i, int i2, String str, int i3) {
        EventLog.writeEvent(AM_SERVICE_CRASHED_TOO_MUCH, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3));
    }

    public static void writeAmScheduleServiceRestart(int i, String str, long j) {
        EventLog.writeEvent(AM_SCHEDULE_SERVICE_RESTART, Integer.valueOf(i), str, Long.valueOf(j));
    }

    public static void writeAmProviderLostProcess(int i, String str, int i2, String str2) {
        EventLog.writeEvent(AM_PROVIDER_LOST_PROCESS, Integer.valueOf(i), str, Integer.valueOf(i2), str2);
    }

    public static void writeAmProcessStartTimeout(int i, int i2, int i3, String str) {
        EventLog.writeEvent(AM_PROCESS_START_TIMEOUT, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), str);
    }

    public static void writeAmCrash(int i, int i2, String str, int i3, String str2, String str3, String str4, int i4, int i5) {
        EventLog.writeEvent(AM_CRASH, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), str2, str3, str4, Integer.valueOf(i4), Integer.valueOf(i5));
    }

    public static void writeAmWtf(int i, int i2, String str, int i3, String str2, String str3) {
        EventLog.writeEvent(AM_WTF, Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3), str2, str3);
    }

    public static void writeAmSwitchUser(int i) {
        EventLog.writeEvent(AM_SWITCH_USER, i);
    }

    public static void writeAmPreBoot(int i, String str) {
        EventLog.writeEvent(AM_PRE_BOOT, Integer.valueOf(i), str);
    }

    public static void writeAmMeminfo(long j, long j2, long j3, long j4, long j5) {
        EventLog.writeEvent(AM_MEMINFO, Long.valueOf(j), Long.valueOf(j2), Long.valueOf(j3), Long.valueOf(j4), Long.valueOf(j5));
    }

    public static void writeAmPss(int i, int i2, String str, long j, long j2, long j3, long j4, int i3, int i4, long j5) {
        EventLog.writeEvent(AM_PSS, Integer.valueOf(i), Integer.valueOf(i2), str, Long.valueOf(j), Long.valueOf(j2), Long.valueOf(j3), Long.valueOf(j4), Integer.valueOf(i3), Integer.valueOf(i4), Long.valueOf(j5));
    }

    public static void writeAmMemFactor(int i, int i2) {
        EventLog.writeEvent(AM_MEM_FACTOR, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void writeAmUserStateChanged(int i, int i2) {
        EventLog.writeEvent(AM_USER_STATE_CHANGED, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void writeAmUidRunning(int i) {
        EventLog.writeEvent(AM_UID_RUNNING, i);
    }

    public static void writeAmUidStopped(int i) {
        EventLog.writeEvent(AM_UID_STOPPED, i);
    }

    public static void writeAmUidActive(int i) {
        EventLog.writeEvent(AM_UID_ACTIVE, i);
    }

    public static void writeAmUidIdle(int i) {
        EventLog.writeEvent(AM_UID_IDLE, i);
    }

    public static void writeAmStopIdleService(int i, String str) {
        EventLog.writeEvent(AM_STOP_IDLE_SERVICE, Integer.valueOf(i), str);
    }

    public static void writeAmCompact(int i, String str, String str2, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, int i2, long j10, int i3, int i4, long j11, long j12) {
        EventLog.writeEvent(AM_COMPACT, Integer.valueOf(i), str, str2, Long.valueOf(j), Long.valueOf(j2), Long.valueOf(j3), Long.valueOf(j4), Long.valueOf(j5), Long.valueOf(j6), Long.valueOf(j7), Long.valueOf(j8), Long.valueOf(j9), Integer.valueOf(i2), Long.valueOf(j10), Integer.valueOf(i3), Integer.valueOf(i4), Long.valueOf(j11), Long.valueOf(j12));
    }

    public static void writeAmFreeze(int i, String str) {
        EventLog.writeEvent(AM_FREEZE, Integer.valueOf(i), str);
    }

    public static void writeAmUnfreeze(int i, String str) {
        EventLog.writeEvent(AM_UNFREEZE, Integer.valueOf(i), str);
    }

    public static void writeUcFinishUserUnlocking(int i) {
        EventLog.writeEvent(UC_FINISH_USER_UNLOCKING, i);
    }

    public static void writeUcFinishUserUnlocked(int i) {
        EventLog.writeEvent(UC_FINISH_USER_UNLOCKED, i);
    }

    public static void writeUcFinishUserUnlockedCompleted(int i) {
        EventLog.writeEvent(UC_FINISH_USER_UNLOCKED_COMPLETED, i);
    }

    public static void writeUcFinishUserStopping(int i) {
        EventLog.writeEvent(UC_FINISH_USER_STOPPING, i);
    }

    public static void writeUcFinishUserStopped(int i) {
        EventLog.writeEvent(UC_FINISH_USER_STOPPED, i);
    }

    public static void writeUcSwitchUser(int i) {
        EventLog.writeEvent(UC_SWITCH_USER, i);
    }

    public static void writeUcStartUserInternal(int i, int i2, int i3) {
        EventLog.writeEvent(UC_START_USER_INTERNAL, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
    }

    public static void writeUcUnlockUser(int i) {
        EventLog.writeEvent(UC_UNLOCK_USER, i);
    }

    public static void writeUcFinishUserBoot(int i) {
        EventLog.writeEvent(UC_FINISH_USER_BOOT, i);
    }

    public static void writeUcDispatchUserSwitch(int i, int i2) {
        EventLog.writeEvent(UC_DISPATCH_USER_SWITCH, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void writeUcContinueUserSwitch(int i, int i2) {
        EventLog.writeEvent(UC_CONTINUE_USER_SWITCH, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void writeUcSendUserBroadcast(int i, String str) {
        EventLog.writeEvent(UC_SEND_USER_BROADCAST, Integer.valueOf(i), str);
    }

    public static void writeSsmUserStarting(int i) {
        EventLog.writeEvent(SSM_USER_STARTING, i);
    }

    public static void writeSsmUserSwitching(int i, int i2) {
        EventLog.writeEvent(SSM_USER_SWITCHING, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void writeSsmUserUnlocking(int i) {
        EventLog.writeEvent(SSM_USER_UNLOCKING, i);
    }

    public static void writeSsmUserUnlocked(int i) {
        EventLog.writeEvent(SSM_USER_UNLOCKED, i);
    }

    public static void writeSsmUserStopping(int i) {
        EventLog.writeEvent(SSM_USER_STOPPING, i);
    }

    public static void writeSsmUserStopped(int i) {
        EventLog.writeEvent(SSM_USER_STOPPED, i);
    }

    public static void writeSsmUserCompletedEvent(int i, int i2) {
        EventLog.writeEvent(SSM_USER_COMPLETED_EVENT, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void writeUmUserVisibilityChanged(int i, int i2) {
        EventLog.writeEvent(UM_USER_VISIBILITY_CHANGED, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void writeAmForegroundServiceStart(int i, String str, int i2, String str2, int i3, int i4, int i5, int i6, int i7, int i8, String str3, int i9) {
        EventLog.writeEvent(AM_FOREGROUND_SERVICE_START, Integer.valueOf(i), str, Integer.valueOf(i2), str2, Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), str3, Integer.valueOf(i9));
    }

    public static void writeAmForegroundServiceDenied(int i, String str, int i2, String str2, int i3, int i4, int i5, int i6, int i7, int i8, String str3, int i9) {
        EventLog.writeEvent(AM_FOREGROUND_SERVICE_DENIED, Integer.valueOf(i), str, Integer.valueOf(i2), str2, Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), str3, Integer.valueOf(i9));
    }

    public static void writeAmForegroundServiceStop(int i, String str, int i2, String str2, int i3, int i4, int i5, int i6, int i7, int i8, String str3, int i9) {
        EventLog.writeEvent(AM_FOREGROUND_SERVICE_STOP, Integer.valueOf(i), str, Integer.valueOf(i2), str2, Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), str3, Integer.valueOf(i9));
    }

    public static void writeAmForegroundServiceTimedOut(int i, String str, int i2, String str2, int i3, int i4, int i5, int i6, int i7, int i8, String str3, int i9) {
        EventLog.writeEvent(AM_FOREGROUND_SERVICE_TIMED_OUT, Integer.valueOf(i), str, Integer.valueOf(i2), str2, Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), str3, Integer.valueOf(i9));
    }

    public static void writeAmIntentSenderRedirectUser(int i) {
        EventLog.writeEvent(AM_INTENT_SENDER_REDIRECT_USER, i);
    }
}
