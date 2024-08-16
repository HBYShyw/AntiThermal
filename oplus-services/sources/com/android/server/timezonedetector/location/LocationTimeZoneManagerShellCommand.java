package com.android.server.timezonedetector.location;

import android.app.time.LocationTimeZoneAlgorithmStatus;
import android.os.ShellCommand;
import android.util.IndentingPrintWriter;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.timedetector.ServerFlags;
import com.android.server.timezonedetector.LocationAlgorithmEvent;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.timezonedetector.location.LocationTimeZoneProvider;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class LocationTimeZoneManagerShellCommand extends ShellCommand {
    private final LocationTimeZoneManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationTimeZoneManagerShellCommand(LocationTimeZoneManagerService locationTimeZoneManagerService) {
        this.mService = locationTimeZoneManagerService;
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -385184143:
                if (str.equals("start_with_test_providers")) {
                    c = 0;
                    break;
                }
                break;
            case 3540994:
                if (str.equals("stop")) {
                    c = 1;
                    break;
                }
                break;
            case 109757538:
                if (str.equals("start")) {
                    c = 2;
                    break;
                }
                break;
            case 248094771:
                if (str.equals("clear_recorded_provider_states")) {
                    c = 3;
                    break;
                }
                break;
            case 943200902:
                if (str.equals("dump_state")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return runStartWithTestProviders();
            case 1:
                return runStop();
            case 2:
                return runStart();
            case 3:
                return runClearRecordedProviderStates();
            case 4:
                return runDumpControllerState();
            default:
                return handleDefaultCommands(str);
        }
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.printf("Location Time Zone Manager (%s) commands for tests:\n", "location_time_zone_manager");
        outPrintWriter.printf("  help\n", new Object[0]);
        outPrintWriter.printf("    Print this help text.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", "start");
        outPrintWriter.printf("    Starts the service, creating location time zone providers.\n", new Object[0]);
        outPrintWriter.printf("  %s <primary package name|%2$s> <secondary package name|%2$s> <record states>\n", "start_with_test_providers", "@null");
        outPrintWriter.printf("    Starts the service with test provider packages configured / provider permission checks disabled.\n", new Object[0]);
        outPrintWriter.printf("    <record states> - true|false, determines whether state recording is enabled.\n", new Object[0]);
        outPrintWriter.printf("    See %s and %s.\n", "dump_state", "clear_recorded_provider_states");
        outPrintWriter.printf("  %s\n", "stop");
        outPrintWriter.printf("    Stops the service, destroying location time zone providers.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", "clear_recorded_provider_states");
        outPrintWriter.printf("    Clears recorded provider state. See also %s and %s.\n", "start_with_test_providers", "dump_state");
        outPrintWriter.printf("    Note: This is only intended for use during testing.\n", new Object[0]);
        outPrintWriter.printf("  %s [%s]\n", "dump_state", "--proto");
        outPrintWriter.printf("    Dumps service state for tests as text or binary proto form.\n", new Object[0]);
        outPrintWriter.printf("    See the LocationTimeZoneManagerServiceStateProto definition for details.\n", new Object[0]);
        outPrintWriter.println();
        outPrintWriter.printf("This service is also affected by the following device_config flags in the %s namespace:\n", "system_time");
        outPrintWriter.printf("  %s\n", ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE);
        outPrintWriter.printf("    Overrides the mode of the primary provider. Values=%s|%s\n", ServiceConfigAccessor.PROVIDER_MODE_DISABLED, ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        outPrintWriter.printf("  %s\n", ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE);
        outPrintWriter.printf("    Overrides the mode of the secondary provider. Values=%s|%s\n", ServiceConfigAccessor.PROVIDER_MODE_DISABLED, ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        outPrintWriter.printf("  %s\n", ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS);
        outPrintWriter.printf("    Sets the amount of time the service waits when uncertain before making an 'uncertain' suggestion to the time zone detector.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS);
        outPrintWriter.printf("    Sets the initialization time passed to the providers.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS);
        outPrintWriter.printf("    Sets the amount of extra time added to the providers' initialization time.\n", new Object[0]);
        outPrintWriter.printf("  %s\n", ServerFlags.KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS);
        outPrintWriter.printf("    Sets the amount of time that must pass between equivalent LTZP events before they will be reported to the system server.\n", new Object[0]);
        outPrintWriter.println();
        outPrintWriter.printf("Typically, use '%s' to stop the service before setting individual flags and '%s' after to restart it.\n", "stop", "start");
        outPrintWriter.println();
        outPrintWriter.printf("See \"adb shell cmd device_config\" for more information on setting flags.\n", new Object[0]);
        outPrintWriter.println();
        outPrintWriter.printf("Also see \"adb shell cmd %s help\" for higher-level location time zone commands / settings.\n", "time_zone_detector");
        outPrintWriter.println();
    }

    private int runStart() {
        try {
            this.mService.start();
            getOutPrintWriter().println("Service started");
            return 0;
        } catch (RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private int runStartWithTestProviders() {
        try {
            this.mService.startWithTestProviders(parseProviderPackageName(getNextArgRequired()), parseProviderPackageName(getNextArgRequired()), Boolean.parseBoolean(getNextArgRequired()));
            getOutPrintWriter().println("Service started (test mode)");
            return 0;
        } catch (RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private int runStop() {
        try {
            this.mService.stop();
            getOutPrintWriter().println("Service stopped");
            return 0;
        } catch (RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private int runClearRecordedProviderStates() {
        try {
            this.mService.clearRecordedProviderStates();
            return 0;
        } catch (IllegalStateException e) {
            reportError(e);
            return 2;
        }
    }

    private int runDumpControllerState() {
        DualDumpOutputStream dualDumpOutputStream;
        try {
            LocationTimeZoneManagerServiceState stateForTests = this.mService.getStateForTests();
            if (stateForTests == null) {
                return 0;
            }
            if ("--proto".equals(getNextOption())) {
                dualDumpOutputStream = new DualDumpOutputStream(new ProtoOutputStream(getOutFileDescriptor()));
            } else {
                dualDumpOutputStream = new DualDumpOutputStream(new IndentingPrintWriter(getOutPrintWriter(), "  "));
            }
            if (stateForTests.getLastEvent() != null) {
                LocationAlgorithmEvent lastEvent = stateForTests.getLastEvent();
                long start = dualDumpOutputStream.start("last_event", 1146756268033L);
                LocationTimeZoneAlgorithmStatus algorithmStatus = lastEvent.getAlgorithmStatus();
                long start2 = dualDumpOutputStream.start("algorithm_status", 1146756268035L);
                dualDumpOutputStream.write("status", 1159641169921L, convertDetectionAlgorithmStatusToEnumToProtoEnum(algorithmStatus.getStatus()));
                dualDumpOutputStream.end(start2);
                if (lastEvent.getSuggestion() != null) {
                    long start3 = dualDumpOutputStream.start("suggestion", 1146756268033L);
                    Iterator<String> it = lastEvent.getSuggestion().getZoneIds().iterator();
                    while (it.hasNext()) {
                        dualDumpOutputStream.write("zone_ids", 2237677961217L, it.next());
                    }
                    dualDumpOutputStream.end(start3);
                }
                Iterator<String> it2 = lastEvent.getDebugInfo().iterator();
                while (it2.hasNext()) {
                    dualDumpOutputStream.write("debug_info", 2237677961218L, it2.next());
                }
                dualDumpOutputStream.end(start);
            }
            writeControllerStates(dualDumpOutputStream, stateForTests.getControllerStates());
            writeProviderStates(dualDumpOutputStream, stateForTests.getPrimaryProviderStates(), "primary_provider_states", 2246267895810L);
            writeProviderStates(dualDumpOutputStream, stateForTests.getSecondaryProviderStates(), "secondary_provider_states", 2246267895811L);
            dualDumpOutputStream.flush();
            return 0;
        } catch (RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private static void writeControllerStates(DualDumpOutputStream dualDumpOutputStream, List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            dualDumpOutputStream.write("controller_states", 2259152797700L, convertControllerStateToProtoEnum(it.next()));
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static int convertControllerStateToProtoEnum(String str) {
        char c;
        switch (str.hashCode()) {
            case -1166336595:
                if (str.equals("STOPPED")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -468307734:
                if (str.equals("PROVIDERS_INITIALIZING")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 433141802:
                if (str.equals("UNKNOWN")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 478389753:
                if (str.equals("DESTROYED")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 872357833:
                if (str.equals("UNCERTAIN")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1386911874:
                if (str.equals("CERTAIN")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1917201485:
                if (str.equals("INITIALIZING")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2066319421:
                if (str.equals("FAILED")) {
                    c = 5;
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
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            default:
                return 0;
        }
    }

    private static void writeProviderStates(DualDumpOutputStream dualDumpOutputStream, List<LocationTimeZoneProvider.ProviderState> list, String str, long j) {
        for (LocationTimeZoneProvider.ProviderState providerState : list) {
            long start = dualDumpOutputStream.start(str, j);
            dualDumpOutputStream.write("state", 1159641169921L, convertProviderStateEnumToProtoEnum(providerState.stateEnum));
            dualDumpOutputStream.end(start);
        }
    }

    private static int convertProviderStateEnumToProtoEnum(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                throw new IllegalArgumentException("Unknown stateEnum=" + i);
        }
    }

    private static int convertDetectionAlgorithmStatusToEnumToProtoEnum(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                if (i == 3) {
                    return 3;
                }
                throw new IllegalArgumentException("Unknown statusEnum=" + i);
            }
        }
        return i2;
    }

    private void reportError(Throwable th) {
        PrintWriter errPrintWriter = getErrPrintWriter();
        errPrintWriter.println("Error: ");
        th.printStackTrace(errPrintWriter);
    }

    private static String parseProviderPackageName(String str) {
        if (str.equals("@null")) {
            return null;
        }
        return str;
    }
}
