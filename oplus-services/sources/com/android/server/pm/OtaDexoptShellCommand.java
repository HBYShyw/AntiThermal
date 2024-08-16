package com.android.server.pm;

import android.content.pm.IOtaDexopt;
import android.os.RemoteException;
import android.os.ShellCommand;
import java.io.PrintWriter;
import java.util.Locale;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class OtaDexoptShellCommand extends ShellCommand {
    final IOtaDexopt mInterface;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OtaDexoptShellCommand(OtaDexoptService otaDexoptService) {
        this.mInterface = otaDexoptService;
    }

    public int onCommand(String str) {
        char c;
        if (str == null) {
            return handleDefaultCommands((String) null);
        }
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            switch (str.hashCode()) {
                case -1001078227:
                    if (str.equals("progress")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -318370553:
                    if (str.equals("prepare")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3089282:
                    if (str.equals("done")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 3377907:
                    if (str.equals("next")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 3540684:
                    if (str.equals("step")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 856774308:
                    if (str.equals("cleanup")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            if (c == 0) {
                return runOtaPrepare();
            }
            if (c == 1) {
                return runOtaCleanup();
            }
            if (c == 2) {
                return runOtaDone();
            }
            if (c == 3) {
                return runOtaStep();
            }
            if (c == 4) {
                return runOtaNext();
            }
            if (c == 5) {
                return runOtaProgress();
            }
            return handleDefaultCommands(str);
        } catch (RemoteException e) {
            outPrintWriter.println("Remote exception: " + e);
            return -1;
        }
    }

    private int runOtaPrepare() throws RemoteException {
        this.mInterface.prepare();
        getOutPrintWriter().println("Success");
        return 0;
    }

    private int runOtaCleanup() throws RemoteException {
        this.mInterface.cleanup();
        return 0;
    }

    private int runOtaDone() throws RemoteException {
        PrintWriter outPrintWriter = getOutPrintWriter();
        if (this.mInterface.isDone()) {
            outPrintWriter.println("OTA complete.");
            return 0;
        }
        outPrintWriter.println("OTA incomplete.");
        return 0;
    }

    private int runOtaStep() throws RemoteException {
        this.mInterface.dexoptNextPackage();
        return 0;
    }

    private int runOtaNext() throws RemoteException {
        getOutPrintWriter().println(this.mInterface.nextDexoptCommand());
        return 0;
    }

    private int runOtaProgress() throws RemoteException {
        getOutPrintWriter().format(Locale.ROOT, "%.2f", Float.valueOf(this.mInterface.getProgress()));
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("OTA Dexopt (ota) commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println("");
        outPrintWriter.println("  prepare");
        outPrintWriter.println("    Prepare an OTA dexopt pass, collecting all packages.");
        outPrintWriter.println("  done");
        outPrintWriter.println("    Replies whether the OTA is complete or not.");
        outPrintWriter.println("  step");
        outPrintWriter.println("    OTA dexopt the next package.");
        outPrintWriter.println("  next");
        outPrintWriter.println("    Get parameters for OTA dexopt of the next package.");
        outPrintWriter.println("  cleanup");
        outPrintWriter.println("    Clean up internal states. Ends an OTA session.");
    }
}
