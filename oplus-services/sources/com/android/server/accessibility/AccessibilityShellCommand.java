package com.android.server.accessibility;

import android.app.ActivityManager;
import android.os.Binder;
import android.os.ShellCommand;
import android.os.UserHandle;
import com.android.server.LocalServices;
import com.android.server.wm.WindowManagerInternal;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AccessibilityShellCommand extends ShellCommand {
    final AccessibilityManagerService mService;
    final SystemActionPerformer mSystemActionPerformer;
    final WindowManagerInternal mWindowManagerService = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessibilityShellCommand(AccessibilityManagerService accessibilityManagerService, SystemActionPerformer systemActionPerformer) {
        this.mService = accessibilityManagerService;
        this.mSystemActionPerformer = systemActionPerformer;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int onCommand(String str) {
        char c;
        if (str == null) {
            return handleDefaultCommands(str);
        }
        switch (str.hashCode()) {
            case -859068373:
                if (str.equals("get-bind-instant-service-allowed")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 789489311:
                if (str.equals("set-bind-instant-service-allowed")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1340897306:
                if (str.equals("start-trace")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1748820581:
                if (str.equals("call-system-action")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1857979322:
                if (str.equals("stop-trace")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return runGetBindInstantServiceAllowed();
            case 1:
                return runSetBindInstantServiceAllowed();
            case 2:
            case 4:
                return this.mService.getTraceManager().onShellCommand(str, this);
            case 3:
                return runCallSystemAction();
            default:
                return -1;
        }
    }

    private int runGetBindInstantServiceAllowed() {
        Integer parseUserId = parseUserId();
        if (parseUserId == null) {
            return -1;
        }
        getOutPrintWriter().println(Boolean.toString(this.mService.getBindInstantServiceAllowed(parseUserId.intValue())));
        return 0;
    }

    private int runSetBindInstantServiceAllowed() {
        Integer parseUserId = parseUserId();
        if (parseUserId == null) {
            return -1;
        }
        String nextArgRequired = getNextArgRequired();
        if (nextArgRequired == null) {
            getErrPrintWriter().println("Error: no true/false specified");
            return -1;
        }
        this.mService.setBindInstantServiceAllowed(parseUserId.intValue(), Boolean.parseBoolean(nextArgRequired));
        return 0;
    }

    private int runCallSystemAction() {
        String nextArg;
        int callingUid = Binder.getCallingUid();
        if ((callingUid != 0 && callingUid != 1000 && callingUid != 2000) || (nextArg = getNextArg()) == null) {
            return -1;
        }
        this.mSystemActionPerformer.performSystemAction(Integer.parseInt(nextArg));
        return 0;
    }

    private Integer parseUserId() {
        String nextOption = getNextOption();
        if (nextOption != null) {
            if (nextOption.equals("--user")) {
                return Integer.valueOf(UserHandle.parseUserArg(getNextArgRequired()));
            }
            getErrPrintWriter().println("Unknown option: " + nextOption);
            return null;
        }
        return Integer.valueOf(ActivityManager.getCurrentUser());
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Accessibility service (accessibility) commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println("  set-bind-instant-service-allowed [--user <USER_ID>] true|false ");
        outPrintWriter.println("    Set whether binding to services provided by instant apps is allowed.");
        outPrintWriter.println("  get-bind-instant-service-allowed [--user <USER_ID>]");
        outPrintWriter.println("    Get whether binding to services provided by instant apps is allowed.");
        outPrintWriter.println("  call-system-action <ACTION_ID>");
        outPrintWriter.println("    Calls the system action with the given action id.");
        this.mService.getTraceManager().onHelp(outPrintWriter);
    }
}
