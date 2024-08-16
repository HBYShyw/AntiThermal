package com.android.server.accounts;

import android.app.ActivityManager;
import android.os.ShellCommand;
import android.os.UserHandle;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AccountManagerServiceShellCommand extends ShellCommand {
    final AccountManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccountManagerServiceShellCommand(AccountManagerService accountManagerService) {
        this.mService = accountManagerService;
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        if (str.equals("get-bind-instant-service-allowed")) {
            return runGetBindInstantServiceAllowed();
        }
        if (str.equals("set-bind-instant-service-allowed")) {
            return runSetBindInstantServiceAllowed();
        }
        return -1;
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

    private Integer parseUserId() {
        String nextOption = getNextOption();
        if (nextOption != null) {
            if (nextOption.equals("--user")) {
                int parseUserArg = UserHandle.parseUserArg(getNextArgRequired());
                if (parseUserArg == -2) {
                    return Integer.valueOf(ActivityManager.getCurrentUser());
                }
                if (parseUserArg == -1) {
                    getErrPrintWriter().println("USER_ALL not supported. Specify a user.");
                    return null;
                }
                if (parseUserArg < 0) {
                    getErrPrintWriter().println("Invalid user: " + parseUserArg);
                    return null;
                }
                return Integer.valueOf(parseUserArg);
            }
            getErrPrintWriter().println("Unknown option: " + nextOption);
            return null;
        }
        return Integer.valueOf(ActivityManager.getCurrentUser());
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Account manager service commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println("  set-bind-instant-service-allowed [--user <USER_ID> (current user if not specified)] true|false ");
        outPrintWriter.println("    Set whether binding to services provided by instant apps is allowed.");
        outPrintWriter.println("  get-bind-instant-service-allowed [--user <USER_ID> (current user if not specified)]");
        outPrintWriter.println("    Get whether binding to services provided by instant apps is allowed.");
    }
}
