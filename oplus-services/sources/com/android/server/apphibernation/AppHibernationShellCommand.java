package com.android.server.apphibernation;

import android.os.ShellCommand;
import android.os.UserHandle;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AppHibernationShellCommand extends ShellCommand {
    private static final int ERROR = -1;
    private static final String GLOBAL_OPT = "--global";
    private static final int SUCCESS = 0;
    private static final String USER_OPT = "--user";
    private final AppHibernationService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppHibernationShellCommand(AppHibernationService appHibernationService) {
        this.mService = appHibernationService;
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        if (str.equals("set-state")) {
            return runSetState();
        }
        if (str.equals("get-state")) {
            return runGetState();
        }
        return handleDefaultCommands(str);
    }

    private int runSetState() {
        int i = -2;
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption == null) {
                break;
            }
            if (nextOption.equals(GLOBAL_OPT)) {
                z = true;
            } else if (nextOption.equals(USER_OPT)) {
                i = UserHandle.parseUserArg(getNextArgRequired());
            } else {
                getErrPrintWriter().println("Error: Unknown option: " + nextOption);
            }
        }
        String nextArgRequired = getNextArgRequired();
        if (nextArgRequired == null) {
            getErrPrintWriter().println("Error: no package specified");
            return -1;
        }
        String nextArgRequired2 = getNextArgRequired();
        if (nextArgRequired2 == null) {
            getErrPrintWriter().println("Error: No state to set specified");
            return -1;
        }
        boolean parseBoolean = Boolean.parseBoolean(nextArgRequired2);
        if (z) {
            this.mService.setHibernatingGlobally(nextArgRequired, parseBoolean);
        } else {
            this.mService.setHibernatingForUser(nextArgRequired, i, parseBoolean);
        }
        return 0;
    }

    private int runGetState() {
        int i = -2;
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption == null) {
                break;
            }
            if (nextOption.equals(GLOBAL_OPT)) {
                z = true;
            } else if (nextOption.equals(USER_OPT)) {
                i = UserHandle.parseUserArg(getNextArgRequired());
            } else {
                getErrPrintWriter().println("Error: Unknown option: " + nextOption);
            }
        }
        String nextArgRequired = getNextArgRequired();
        if (nextArgRequired == null) {
            getErrPrintWriter().println("Error: No package specified");
            return -1;
        }
        getOutPrintWriter().println(z ? this.mService.isHibernatingGlobally(nextArgRequired) : this.mService.isHibernatingForUser(nextArgRequired, i));
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("App hibernation (app_hibernation) commands: ");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println("");
        outPrintWriter.println("  set-state [--user USER_ID] [--global] PACKAGE true|false");
        outPrintWriter.println("    Sets the hibernation state of the package to value specified. Optionally");
        outPrintWriter.println("    may specify a user id or set global hibernation state.");
        outPrintWriter.println("");
        outPrintWriter.println("  get-state [--user USER_ID] [--global] PACKAGE");
        outPrintWriter.println("    Gets the hibernation state of the package. Optionally may specify a user");
        outPrintWriter.println("    id or request global hibernation state.");
        outPrintWriter.println("");
    }
}
