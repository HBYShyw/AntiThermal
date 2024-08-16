package com.android.server.wm;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.BackgroundStartPrivileges;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Process;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.Slog;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.Preconditions;
import com.android.server.am.PendingIntentRecord;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class BackgroundActivityStartController {
    static final int BAL_ALLOW_ALLOWLISTED_COMPONENT = 3;
    static final int BAL_ALLOW_ALLOWLISTED_UID = 2;
    static final int BAL_ALLOW_DEFAULT = 1;
    static final int BAL_ALLOW_FOREGROUND = 9;
    static final int BAL_ALLOW_GRACE_PERIOD = 8;
    static final int BAL_ALLOW_PENDING_INTENT = 5;
    static final int BAL_ALLOW_PERMISSION = 6;
    static final int BAL_ALLOW_SAW_PERMISSION = 7;
    static final int BAL_ALLOW_SDK_SANDBOX = 10;
    static final int BAL_ALLOW_VISIBLE_WINDOW = 4;
    static final int BAL_BLOCK = 0;
    private static final String TAG = "ActivityTaskManager";
    public static final String VERDICT_ALLOWED = "Activity start allowed";
    public static final String VERDICT_WOULD_BE_ALLOWED_IF_SENDER_GRANTS_BAL = "Activity start would be allowed if the sender granted BAL privileges";
    private IBackgroundActivityStartControllerExt mBackgroundActivityStartControllerExt = (IBackgroundActivityStartControllerExt) ExtLoader.type(IBackgroundActivityStartControllerExt.class).base(this).create();
    private final ActivityTaskManagerService mService;
    private final ActivityTaskSupervisor mSupervisor;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public @interface BalCode {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String balCodeToString(int i) {
        switch (i) {
            case 0:
                return "BAL_BLOCK";
            case 1:
                return "BAL_ALLOW_DEFAULT";
            case 2:
                return "BAL_ALLOW_ALLOWLISTED_UID";
            case 3:
                return "BAL_ALLOW_ALLOWLISTED_COMPONENT";
            case 4:
                return "BAL_ALLOW_VISIBLE_WINDOW";
            case 5:
                return "BAL_ALLOW_PENDING_INTENT";
            case 6:
                return "BAL_ALLOW_PERMISSION";
            case 7:
                return "BAL_ALLOW_SAW_PERMISSION";
            case 8:
                return "BAL_ALLOW_GRACE_PERIOD";
            case 9:
                return "BAL_ALLOW_FOREGROUND";
            case 10:
                return "BAL_ALLOW_SDK_SANDBOX";
            default:
                throw new IllegalArgumentException("Unexpected value: " + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackgroundActivityStartController(ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor) {
        this.mService = activityTaskManagerService;
        this.mSupervisor = activityTaskSupervisor;
    }

    private boolean isHomeApp(int i, String str) {
        if (this.mService.mHomeProcess != null) {
            return i == this.mService.mHomeProcess.mUid;
        }
        if (str == null) {
            return false;
        }
        ComponentName defaultHomeActivity = this.mService.getPackageManagerInternalLocked().getDefaultHomeActivity(UserHandle.getUserId(i));
        return defaultHomeActivity != null && str.equals(defaultHomeActivity.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldAbortBackgroundActivityStart(int i, int i2, String str, int i3, int i4, WindowProcessController windowProcessController, PendingIntentRecord pendingIntentRecord, BackgroundStartPrivileges backgroundStartPrivileges, Intent intent, ActivityOptions activityOptions) {
        return checkBackgroundActivityStart(i, i2, str, i3, i4, windowProcessController, pendingIntentRecord, backgroundStartPrivileges, intent, activityOptions) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:129:0x03d7  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x03e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int checkBackgroundActivityStart(int i, int i2, String str, int i3, int i4, WindowProcessController windowProcessController, PendingIntentRecord pendingIntentRecord, BackgroundStartPrivileges backgroundStartPrivileges, Intent intent, ActivityOptions activityOptions) {
        boolean z;
        int i5;
        BackgroundStartPrivileges backgroundStartPrivileges2;
        boolean z2;
        int i6;
        int i7;
        boolean z3;
        String str2;
        boolean z4;
        boolean z5;
        int i8;
        int i9;
        WindowProcessController windowProcessController2;
        int i10;
        int i11;
        String str3;
        int i12;
        int logStartAllowedAndReturnCode;
        if (this.mBackgroundActivityStartControllerExt.interceptBackgroundActivityStartBegin(intent, i, i2, str, i3, i4)) {
            return 2;
        }
        int appId = UserHandle.getAppId(i);
        boolean z6 = pendingIntentRecord == null || activityOptions == null || activityOptions.getPendingIntentCreatorBackgroundActivityStartMode() != 2;
        if (z6) {
            if (i == 0 || appId == 1000 || appId == 1027) {
                return logStartAllowedAndReturnCode(2, false, i, i3, intent, "Important callingUid");
            }
            if (isHomeApp(i, str)) {
                return logStartAllowedAndReturnCode(3, false, i, i3, intent, "Home app");
            }
            WindowState currentInputMethodWindow = this.mService.mRootWindowContainer.getCurrentInputMethodWindow();
            if (currentInputMethodWindow != null && appId == currentInputMethodWindow.mOwnerUid) {
                return logStartAllowedAndReturnCode(3, false, i, i3, intent, "Active ime");
            }
        }
        int balAppSwitchesState = this.mService.getBalAppSwitchesState();
        if (!ActivityTaskManagerService.LTW_DISABLE && this.mBackgroundActivityStartControllerExt.isFromBackgroundWhiteList(this.mService, i3)) {
            return 1;
        }
        int uidState = this.mService.mActiveUids.getUidState(i);
        boolean hasActiveVisibleWindow = this.mService.hasActiveVisibleWindow(i);
        boolean z7 = uidState <= 1;
        boolean z8 = (((balAppSwitchesState == 2 || balAppSwitchesState == 1) || this.mService.mActiveUids.hasNonAppVisibleWindow(i)) && hasActiveVisibleWindow) || z7;
        if (z6 && z8) {
            this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidHasAnyVisbleWindowOrPersistentProcess", true, false);
            return logStartAllowedAndReturnCode(4, false, i, i3, intent, "callingUidHasAnyVisibleWindow = " + i + ", isCallingUidPersistentSystemProcess = " + z7);
        }
        int uidState2 = i == i3 ? uidState : this.mService.mActiveUids.getUidState(i3);
        boolean hasActiveVisibleWindow2 = i == i3 ? hasActiveVisibleWindow : this.mService.hasActiveVisibleWindow(i3);
        boolean z9 = i == i3 ? z7 : UserHandle.getAppId(i3) == 1000 || uidState2 <= 1;
        if (Process.isSdkSandboxUid(i3)) {
            if (this.mService.hasActiveVisibleWindow(Process.getAppUidForSdkSandboxUid(i3))) {
                return logStartAllowedAndReturnCode(10, false, i, i3, intent, "uid in SDK sandbox has visible (non-toast) window");
            }
        }
        String packageNameIfUnique = this.mService.getPackageNameIfUnique(i3, i4);
        BackgroundStartPrivileges backgroundStartPrivilegesAllowedByCaller = PendingIntentRecord.getBackgroundStartPrivilegesAllowedByCaller(activityOptions, i3, packageNameIfUnique);
        boolean z10 = activityOptions == null || activityOptions.getPendingIntentBackgroundActivityStartMode() == 0;
        boolean z11 = z10 || backgroundStartPrivilegesAllowedByCaller.allowsBackgroundActivityStarts();
        String str4 = backgroundStartPrivilegesAllowedByCaller.allowsBackgroundActivityStarts() ? VERDICT_ALLOWED : VERDICT_WOULD_BE_ALLOWED_IF_SENDER_GRANTS_BAL;
        if (i3 == i || !z11) {
            z = z7;
            i5 = uidState2;
            backgroundStartPrivileges2 = backgroundStartPrivilegesAllowedByCaller;
            z2 = hasActiveVisibleWindow;
            i6 = uidState;
            i7 = balAppSwitchesState;
            z3 = z9;
            str2 = packageNameIfUnique;
            z4 = false;
            z5 = true;
            i8 = 0;
        } else {
            z = z7;
            i5 = uidState2;
            backgroundStartPrivileges2 = backgroundStartPrivilegesAllowedByCaller;
            z2 = hasActiveVisibleWindow;
            i6 = uidState;
            i7 = balAppSwitchesState;
            z3 = z9;
            z4 = false;
            str2 = packageNameIfUnique;
            z5 = true;
            i8 = checkPiBackgroundActivityStart(i, i3, backgroundStartPrivileges, intent, activityOptions, hasActiveVisibleWindow2, z3, str4);
        }
        if (i8 != 0 && backgroundStartPrivileges2.allowsBackgroundActivityStarts() && !z10) {
            return i8;
        }
        if (z6) {
            if (ActivityTaskManagerService.checkPermission("android.permission.START_ACTIVITIES_FROM_BACKGROUND", i2, i) == 0) {
                return logStartAllowedAndReturnCode(6, i8, backgroundStartPrivileges2, true, i, i3, intent, "START_ACTIVITIES_FROM_BACKGROUND permission granted");
            }
            if (this.mSupervisor.mRecentTasks.isCallerRecents(i)) {
                return logStartAllowedAndReturnCode(3, i8, backgroundStartPrivileges2, true, i, i3, intent, "Recents Component");
            }
            if (this.mService.isDeviceOwner(i)) {
                this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidDeviceOwner", z4, z4);
                return logStartAllowedAndReturnCode(3, i8, backgroundStartPrivileges2, true, i, i3, intent, "Device Owner");
            }
            if (this.mService.isAffiliatedProfileOwner(i)) {
                return logStartAllowedAndReturnCode(3, i8, backgroundStartPrivileges2, true, i, i3, intent, "Affiliated Profile Owner");
            }
            if (this.mService.isAssociatedCompanionApp(UserHandle.getUserId(i), i)) {
                this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidHasCompanionDevice", z4, z4);
                return logStartAllowedAndReturnCode(3, i8, backgroundStartPrivileges2, true, i, i3, intent, "Companion App");
            }
            if (this.mService.hasSystemAlertWindowPermission(i, i2, str)) {
                Slog.w(TAG, "Background activity start for " + str + " allowed because SYSTEM_ALERT_WINDOW permission is granted.");
                this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidHasSYSTEM_ALERT_WINDOW", z4, z4);
                return logStartAllowedAndReturnCode(7, i8, backgroundStartPrivileges2, true, i, i3, intent, "SYSTEM_ALERT_WINDOW permission is granted");
            }
            if (isSystemExemptFlagEnabled() && this.mService.getAppOpsManager().checkOpNoThrow(130, i, str) == 0) {
                return logStartAllowedAndReturnCode(6, i8, backgroundStartPrivileges2, true, i, i3, intent, "OP_SYSTEM_EXEMPT_FROM_ACTIVITY_BG_START_RESTRICTION appop is granted");
            }
        }
        boolean z12 = (windowProcessController == null && z11 && i8 == 0) ? z5 : z4;
        if (z12) {
            i9 = i3;
            windowProcessController2 = this.mService.getProcessController(i4, i9);
            i10 = i9;
        } else {
            i9 = i3;
            windowProcessController2 = windowProcessController;
            i10 = i;
        }
        if (windowProcessController2 == null || !z6) {
            i11 = i7;
        } else {
            int i13 = i7;
            int areBackgroundActivityStartsAllowed = windowProcessController2.areBackgroundActivityStartsAllowed(i13);
            if (areBackgroundActivityStartsAllowed == 0) {
                i12 = areBackgroundActivityStartsAllowed;
                i11 = i13;
                ArraySet<WindowProcessController> processes = this.mService.mProcessMap.getProcesses(i10);
                if (processes != null) {
                    for (int size = processes.size() - 1; size >= 0; size--) {
                        WindowProcessController valueAt = processes.valueAt(size);
                        int areBackgroundActivityStartsAllowed2 = valueAt.areBackgroundActivityStartsAllowed(i11);
                        if (valueAt != windowProcessController2 && areBackgroundActivityStartsAllowed2 != 0) {
                            if (z12) {
                                logStartAllowedAndReturnCode = logStartAllowedAndReturnCode(areBackgroundActivityStartsAllowed2, true, i, i3, intent, "process" + valueAt.getPid() + " from uid " + i10 + " is allowed", str4);
                            } else {
                                return logStartAllowedAndReturnCode(areBackgroundActivityStartsAllowed2, i8, backgroundStartPrivileges2, true, i, i3, intent, "process" + valueAt.getPid() + " from uid " + i10 + " is allowed");
                            }
                        }
                    }
                }
                if (z12) {
                    int i14 = i12;
                    Preconditions.checkState(i14 == 0 ? z5 : false, "balAllowedForCaller = " + i14 + " (should have returned)");
                } else if (i8 != 0 && backgroundStartPrivileges2.allowsBackgroundActivityStarts() && !z10) {
                    return i8;
                }
            } else if (z12) {
                i12 = areBackgroundActivityStartsAllowed;
                i11 = i13;
                logStartAllowedAndReturnCode = logStartAllowedAndReturnCode(areBackgroundActivityStartsAllowed, true, i, i3, intent, "callerApp process (pid = " + windowProcessController2.getPid() + ", uid = " + i10 + ") is allowed", str4);
            } else {
                return logStartAllowedAndReturnCode(areBackgroundActivityStartsAllowed, i8, backgroundStartPrivileges2, true, i, i3, intent, "callerApp process (pid = " + windowProcessController2.getPid() + ", uid = " + i10 + ") is allowed");
            }
            i8 = logStartAllowedAndReturnCode;
            if (z12) {
            }
        }
        this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_forbid_AOSP", false, false);
        if (str2 == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(i == i9 ? str : this.mService.mContext.getPackageManager().getNameForUid(i9));
            sb.append("[debugOnly]");
            str3 = sb.toString();
        } else {
            str3 = str2;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" [callingPackage: ");
        sb2.append(str);
        sb2.append("; callingUid: ");
        sb2.append(i);
        sb2.append("; appSwitchState: ");
        sb2.append(i11);
        sb2.append("; callingUidHasAnyVisibleWindow: ");
        boolean z13 = z2;
        sb2.append(z13);
        sb2.append("; callingUidProcState: ");
        int i15 = i6;
        sb2.append(DebugUtils.valueToString(ActivityManager.class, "PROCESS_STATE_", i15));
        sb2.append("; isCallingUidPersistentSystemProcess: ");
        sb2.append(z);
        sb2.append("; balAllowedByPiSender: ");
        BackgroundStartPrivileges backgroundStartPrivileges3 = backgroundStartPrivileges2;
        sb2.append(backgroundStartPrivileges3);
        sb2.append("; realCallingPackage: ");
        sb2.append(str3);
        sb2.append("; realCallingUid: ");
        sb2.append(i9);
        sb2.append("; realCallingUidHasAnyVisibleWindow: ");
        boolean z14 = hasActiveVisibleWindow2;
        sb2.append(z14);
        sb2.append("; realCallingUidProcState: ");
        int i16 = i5;
        sb2.append(DebugUtils.valueToString(ActivityManager.class, "PROCESS_STATE_", i16));
        sb2.append("; isRealCallingUidPersistentSystemProcess: ");
        sb2.append(z3);
        sb2.append("; originatingPendingIntent: ");
        boolean z15 = z10;
        sb2.append(pendingIntentRecord);
        sb2.append("; backgroundStartPrivileges: ");
        sb2.append(backgroundStartPrivileges);
        sb2.append("; intent: ");
        sb2.append(intent);
        sb2.append("; callerApp: ");
        sb2.append(windowProcessController2);
        sb2.append("; inVisibleTask: ");
        sb2.append((windowProcessController2 == null || !windowProcessController2.hasActivityInVisibleTask()) ? false : z5);
        sb2.append("]");
        String sb3 = sb2.toString();
        if (i8 != 0) {
            Preconditions.checkState(z15, "resultIfPiSenderAllowsBal = " + balCodeToString(i8) + " at the end but logVerdictChangeByPiDefaultChange = false");
            if (backgroundStartPrivileges3.allowsBackgroundActivityStarts()) {
                Slog.wtf(TAG, "With BAL hardening this activity start would be blocked!" + sb3);
                return i8;
            }
            Slog.wtf(TAG, "Without BAL hardening this activity start would be allowed!" + sb3);
        }
        Slog.w(TAG, "Background activity launch blocked" + sb3);
        if (!this.mService.isActivityStartsLoggingEnabled()) {
            return 0;
        }
        this.mSupervisor.getActivityMetricsLogger().logAbortedBgActivityStart(intent, windowProcessController2, i, str, i15, z13, i3, i16, z14, pendingIntentRecord != null ? z5 : false);
        return 0;
    }

    private int checkPiBackgroundActivityStart(int i, int i2, BackgroundStartPrivileges backgroundStartPrivileges, Intent intent, ActivityOptions activityOptions, boolean z, boolean z2, String str) {
        if (PendingIntentRecord.isPendingIntentBalAllowedByPermission(activityOptions) && ActivityManager.checkComponentPermission("android.permission.START_ACTIVITIES_FROM_BACKGROUND", i2, -1, true) == 0) {
            return logStartAllowedAndReturnCode(5, false, i, i2, intent, "realCallingUid has BAL permission. realCallingUid: " + i2, str);
        }
        if (z || this.mBackgroundActivityStartControllerExt.startAllowedIfRealCallingUidIsHome(this.mService, i2)) {
            return logStartAllowedAndReturnCode(5, false, i, i2, intent, "realCallingUid has visible (non-toast) window. realCallingUid: " + i2, str);
        }
        if (z2 && backgroundStartPrivileges.allowsBackgroundActivityStarts()) {
            return logStartAllowedAndReturnCode(5, false, i, i2, intent, "realCallingUid is persistent system process AND intent sender allowed (allowBackgroundActivityStart = true). realCallingUid: " + i2, str);
        }
        if (!this.mService.isAssociatedCompanionApp(UserHandle.getUserId(i2), i2)) {
            return 0;
        }
        return logStartAllowedAndReturnCode(5, false, i, i2, intent, "realCallingUid is a companion app. realCallingUid: " + i2, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int logStartAllowedAndReturnCode(int i, boolean z, int i2, int i3, Intent intent, int i4, String str) {
        String str2;
        if (ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
            str2 = "[Process(" + i4 + ")]" + str;
        } else {
            str2 = "";
        }
        return logStartAllowedAndReturnCode(i, z, i2, i3, intent, str2);
    }

    static int logStartAllowedAndReturnCode(int i, boolean z, int i2, int i3, Intent intent, String str) {
        return logStartAllowedAndReturnCode(i, z, i2, i3, intent, str, VERDICT_ALLOWED);
    }

    static int logStartAllowedAndReturnCode(int i, int i2, BackgroundStartPrivileges backgroundStartPrivileges, boolean z, int i3, int i4, Intent intent, String str) {
        return (i2 == 0 || !backgroundStartPrivileges.allowsBackgroundActivityStarts()) ? logStartAllowedAndReturnCode(i, z, i3, i4, intent, str, VERDICT_ALLOWED) : i2;
    }

    static int logStartAllowedAndReturnCode(int i, boolean z, int i2, int i3, Intent intent, String str, String str2) {
        statsLogBalAllowed(i, i2, i3, intent);
        if (ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
            StringBuilder sb = new StringBuilder();
            if (z) {
                sb.append("Background ");
            }
            sb.append(str2 + ": " + str + ". callingUid: " + i2 + ". ");
            sb.append("BAL Code: ");
            sb.append(balCodeToString(i));
            if (str2.equals(VERDICT_ALLOWED)) {
                Slog.i(TAG, sb.toString());
            } else {
                Slog.d(TAG, sb.toString());
            }
        }
        return i;
    }

    private static boolean isSystemExemptFlagEnabled() {
        return DeviceConfig.getBoolean("window_manager", "system_exempt_from_activity_bg_start_restriction_enabled", true);
    }

    private static void statsLogBalAllowed(int i, int i2, int i3, Intent intent) {
        if (i == 5 && (i2 == 1000 || i3 == 1000)) {
            FrameworkStatsLog.write(632, intent != null ? intent.getComponent().flattenToShortString() : "", i, i2, i3);
        }
        if (i == 6 || i == 9 || i == 7) {
            FrameworkStatsLog.write(632, "", i, i2, i3);
        }
    }
}
