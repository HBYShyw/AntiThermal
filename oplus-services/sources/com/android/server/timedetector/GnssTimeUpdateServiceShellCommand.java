package com.android.server.timedetector;

import android.os.ShellCommand;
import java.io.PrintWriter;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class GnssTimeUpdateServiceShellCommand extends ShellCommand {
    private static final String SHELL_COMMAND_SERVICE_NAME = "gnss_time_update_service";
    private static final String SHELL_COMMAND_START_GNSS_LISTENING = "start_gnss_listening";
    private final GnssTimeUpdateService mGnssTimeUpdateService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GnssTimeUpdateServiceShellCommand(GnssTimeUpdateService gnssTimeUpdateService) {
        Objects.requireNonNull(gnssTimeUpdateService);
        this.mGnssTimeUpdateService = gnssTimeUpdateService;
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        if (str.equals(SHELL_COMMAND_START_GNSS_LISTENING)) {
            return runStartGnssListening();
        }
        return handleDefaultCommands(str);
    }

    private int runStartGnssListening() {
        getOutPrintWriter().println(this.mGnssTimeUpdateService.startGnssListening());
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.printf("Network Time Update Service (%s) commands:\n", SHELL_COMMAND_SERVICE_NAME);
        outPrintWriter.printf("  help\n", new Object[0]);
        outPrintWriter.printf("    Print this help text.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", SHELL_COMMAND_START_GNSS_LISTENING);
        outPrintWriter.printf("    Forces the service in to GNSS listening mode (if it isn't already).\n", new Object[0]);
        outPrintWriter.printf("    Prints true if the service is listening after this command.\n", new Object[0]);
        outPrintWriter.println();
    }
}
