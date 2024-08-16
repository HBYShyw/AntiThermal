package com.android.server.power;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ShellCommand;
import android.util.SparseArray;
import com.android.server.power.PowerManagerService;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class PowerManagerShellCommand extends ShellCommand {
    private static final int LOW_POWER_MODE_ON = 1;
    private final Context mContext;
    private SparseArray<PowerManager.WakeLock> mProxWakelocks = new SparseArray<>();
    private final PowerManagerService.BinderService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerManagerShellCommand(Context context, PowerManagerService.BinderService binderService) {
        this.mContext = context;
        this.mService = binderService;
    }

    public int onCommand(String str) {
        char c;
        if (str == null) {
            return handleDefaultCommands(str);
        }
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            switch (str.hashCode()) {
                case -531688203:
                    if (str.equals("set-adaptive-power-saver-enabled")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 584761923:
                    if (str.equals("list-ambient-display-suppression-tokens")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 774730613:
                    if (str.equals("suppress-ambient-display")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 1032507032:
                    if (str.equals("set-fixed-performance-mode-enabled")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 1369181230:
                    if (str.equals("set-mode")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 1369273846:
                    if (str.equals("set-prox")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            if (c == 0) {
                return runSetAdaptiveEnabled();
            }
            if (c == 1) {
                return runSetMode();
            }
            if (c == 2) {
                return runSetFixedPerformanceModeEnabled();
            }
            if (c == 3) {
                return runSuppressAmbientDisplay();
            }
            if (c == 4) {
                return runListAmbientDisplaySuppressionTokens();
            }
            if (c == 5) {
                return runSetProx();
            }
            return handleDefaultCommands(str);
        } catch (RemoteException e) {
            outPrintWriter.println("Remote exception: " + e);
            return -1;
        }
    }

    private int runSetAdaptiveEnabled() throws RemoteException {
        this.mService.setAdaptivePowerSaveEnabled(Boolean.parseBoolean(getNextArgRequired()));
        return 0;
    }

    private int runSetMode() throws RemoteException {
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            this.mService.setPowerSaveModeEnabled(Integer.parseInt(getNextArgRequired()) == 1);
            return 0;
        } catch (RuntimeException e) {
            outPrintWriter.println("Error: " + e.toString());
            return -1;
        }
    }

    private int runSetFixedPerformanceModeEnabled() throws RemoteException {
        boolean powerModeChecked = this.mService.setPowerModeChecked(3, Boolean.parseBoolean(getNextArgRequired()));
        if (!powerModeChecked) {
            PrintWriter errPrintWriter = getErrPrintWriter();
            errPrintWriter.println("Failed to set FIXED_PERFORMANCE mode");
            errPrintWriter.println("This is likely because Power HAL AIDL is not implemented on this device");
        }
        return powerModeChecked ? 0 : -1;
    }

    private int runSuppressAmbientDisplay() throws RemoteException {
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            this.mService.suppressAmbientDisplay(getNextArgRequired(), Boolean.parseBoolean(getNextArgRequired()));
            return 0;
        } catch (RuntimeException e) {
            outPrintWriter.println("Error: " + e.toString());
            return -1;
        }
    }

    private int runListAmbientDisplaySuppressionTokens() throws RemoteException {
        PrintWriter outPrintWriter = getOutPrintWriter();
        List<String> ambientDisplaySuppressionTokens = this.mService.getAmbientDisplaySuppressionTokens();
        if (ambientDisplaySuppressionTokens.isEmpty()) {
            outPrintWriter.println("none");
            return 0;
        }
        outPrintWriter.println(String.format("[%s]", String.join(", ", ambientDisplaySuppressionTokens)));
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int runSetProx() throws RemoteException {
        char c;
        PrintWriter outPrintWriter = getOutPrintWriter();
        String lowerCase = getNextArgRequired().toLowerCase();
        lowerCase.hashCode();
        boolean z = true;
        int i = -1;
        switch (lowerCase.hashCode()) {
            case -1164222250:
                if (lowerCase.equals("acquire")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 3322014:
                if (lowerCase.equals("list")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1090594823:
                if (lowerCase.equals("release")) {
                    c = 2;
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
                break;
            case 1:
                outPrintWriter.println("Wakelocks:");
                outPrintWriter.println(this.mProxWakelocks);
                return 0;
            case 2:
                z = false;
                break;
            default:
                outPrintWriter.println("Error: Allowed options are 'list' 'enable' and 'disable'.");
                return -1;
        }
        if ("-d".equals(getNextArg())) {
            String nextArg = getNextArg();
            int parseInt = Integer.parseInt(nextArg);
            if (parseInt < 0) {
                outPrintWriter.println("Error: Specified displayId (" + nextArg + ") must a non-negative int.");
                return -1;
            }
            i = parseInt;
        }
        int i2 = i + 1;
        PowerManager.WakeLock wakeLock = this.mProxWakelocks.get(i2);
        if (wakeLock == null) {
            wakeLock = ((PowerManager) this.mContext.getSystemService(PowerManager.class)).newWakeLock(32, "PowerManagerShellCommand[" + i + "]", i);
            this.mProxWakelocks.put(i2, wakeLock);
        }
        if (z) {
            wakeLock.acquire();
        } else {
            wakeLock.release();
        }
        outPrintWriter.println(wakeLock);
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Power manager (power) commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println("");
        outPrintWriter.println("  set-adaptive-power-saver-enabled [true|false]");
        outPrintWriter.println("    enables or disables adaptive power saver.");
        outPrintWriter.println("  set-mode MODE");
        outPrintWriter.println("    sets the power mode of the device to MODE.");
        outPrintWriter.println("    1 turns low power mode on and 0 turns low power mode off.");
        outPrintWriter.println("  set-fixed-performance-mode-enabled [true|false]");
        outPrintWriter.println("    enables or disables fixed performance mode");
        outPrintWriter.println("    note: this will affect system performance and should only be used");
        outPrintWriter.println("          during development");
        outPrintWriter.println("  suppress-ambient-display <token> [true|false]");
        outPrintWriter.println("    suppresses the current ambient display configuration and disables");
        outPrintWriter.println("    ambient display");
        outPrintWriter.println("  list-ambient-display-suppression-tokens");
        outPrintWriter.println("    prints the tokens used to suppress ambient display");
        outPrintWriter.println("  set-prox [list|acquire|release] (-d <display_id>)");
        outPrintWriter.println("    Acquires the proximity sensor wakelock. Wakelock is associated with");
        outPrintWriter.println("    a specific display if specified. 'list' lists wakelocks previously");
        outPrintWriter.println("    created by set-prox including their held status.");
        outPrintWriter.println();
        Intent.printIntentArgsHelp(outPrintWriter, "");
    }
}
