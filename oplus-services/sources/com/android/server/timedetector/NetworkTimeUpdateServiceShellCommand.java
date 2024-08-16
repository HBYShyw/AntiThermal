package com.android.server.timedetector;

import android.os.ShellCommand;
import android.util.NtpTrustedTime;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class NetworkTimeUpdateServiceShellCommand extends ShellCommand {
    private static final String SET_SERVER_CONFIG_SERVER_ARG = "--server";
    private static final String SET_SERVER_CONFIG_TIMEOUT_ARG = "--timeout_millis";
    private static final String SHELL_COMMAND_FORCE_REFRESH = "force_refresh";
    private static final String SHELL_COMMAND_RESET_SERVER_CONFIG = "reset_server_config_for_tests";
    private static final String SHELL_COMMAND_SERVICE_NAME = "network_time_update_service";
    private static final String SHELL_COMMAND_SET_SERVER_CONFIG = "set_server_config_for_tests";
    private final NetworkTimeUpdateService mNetworkTimeUpdateService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NetworkTimeUpdateServiceShellCommand(NetworkTimeUpdateService networkTimeUpdateService) {
        Objects.requireNonNull(networkTimeUpdateService);
        this.mNetworkTimeUpdateService = networkTimeUpdateService;
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1679617267:
                if (str.equals(SHELL_COMMAND_SET_SERVER_CONFIG)) {
                    c = 0;
                    break;
                }
                break;
            case 65977594:
                if (str.equals(SHELL_COMMAND_RESET_SERVER_CONFIG)) {
                    c = 1;
                    break;
                }
                break;
            case 1891346823:
                if (str.equals(SHELL_COMMAND_FORCE_REFRESH)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return runSetServerConfig();
            case 1:
                return runResetServerConfig();
            case 2:
                return runForceRefresh();
            default:
                return handleDefaultCommands(str);
        }
    }

    private int runForceRefresh() {
        getOutPrintWriter().println(this.mNetworkTimeUpdateService.forceRefreshForTests());
        return 0;
    }

    private int runSetServerConfig() {
        ArrayList arrayList = new ArrayList();
        Duration duration = null;
        while (true) {
            String nextArg = getNextArg();
            if (nextArg != null) {
                if (nextArg.equals(SET_SERVER_CONFIG_TIMEOUT_ARG)) {
                    duration = Duration.ofMillis(Integer.parseInt(getNextArgRequired()));
                } else if (nextArg.equals(SET_SERVER_CONFIG_SERVER_ARG)) {
                    try {
                        arrayList.add(NtpTrustedTime.parseNtpUriStrict(getNextArgRequired()));
                    } catch (URISyntaxException e) {
                        throw new IllegalArgumentException("Bad NTP server value", e);
                    }
                } else {
                    throw new IllegalArgumentException("Unknown option: " + nextArg);
                }
            } else {
                if (arrayList.isEmpty()) {
                    throw new IllegalArgumentException("Missing required option: ----server");
                }
                if (duration == null) {
                    throw new IllegalArgumentException("Missing required option: ----timeout_millis");
                }
                this.mNetworkTimeUpdateService.setServerConfigForTests(new NtpTrustedTime.NtpConfig(arrayList, duration));
                return 0;
            }
        }
    }

    private int runResetServerConfig() {
        this.mNetworkTimeUpdateService.setServerConfigForTests(null);
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.printf("Network Time Update Service (%s) commands:\n", SHELL_COMMAND_SERVICE_NAME);
        outPrintWriter.printf("  help\n", new Object[0]);
        outPrintWriter.printf("    Print this help text.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", SHELL_COMMAND_FORCE_REFRESH);
        outPrintWriter.printf("    Refreshes the latest time. Prints whether it was successful.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", SHELL_COMMAND_SET_SERVER_CONFIG);
        outPrintWriter.printf("    Sets the NTP server config for tests. The config is not persisted.\n", new Object[0]);
        outPrintWriter.printf("      Options: %s <uri> [%s <additional uris>]+ %s <millis>\n", SET_SERVER_CONFIG_SERVER_ARG, SET_SERVER_CONFIG_SERVER_ARG, SET_SERVER_CONFIG_TIMEOUT_ARG);
        outPrintWriter.printf("      NTP server URIs must be in the form \"ntp://hostname\" or \"ntp://hostname:port\"\n", new Object[0]);
        outPrintWriter.printf("  %s\n", SHELL_COMMAND_RESET_SERVER_CONFIG);
        outPrintWriter.printf("    Resets/clears the NTP server config set via %s.\n", SHELL_COMMAND_SET_SERVER_CONFIG);
        outPrintWriter.println();
    }
}
